package com.yc.piclib.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.piclib.domain.PicDomain;
import com.yc.piclib.future.PiclibFuture;
import com.yc.piclib.services.AsyncThreadPool;
import com.yc.piclib.services.FastefsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/piclib")
public class PicController {

    private static Logger logger = LoggerFactory.getLogger(PicController.class.getName());

    @Autowired
    private PiclibFuture piclibFuture;

    @Value("${file.path.head:http://39.99.175.175/}")
    private String pathHead;

    @Autowired
    private FastefsClient fastefsClient;

    /**
     * 上传图片
     *
     * @return
     */
    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public void uploadPic(@RequestParam("pictureFile") MultipartFile multipartFile, String description, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = fastefsClient.uplodFile(multipartFile);
            AsyncThreadPool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("文件通过nginx访问的路径:" + (pathHead + filename));
                        savePic(multipartFile, pathHead + filename, description);  //调用数据库操作
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("pathInfo", pathHead + filename);
            data.put("width", image.getWidth());
            data.put("height", image.getHeight());

            ObjectMapper mapper = new ObjectMapper();
            String ret = mapper.writeValueAsString(data);

            response.setContentType("text/html;charset=utf8");
            response.getOutputStream().write(ret.getBytes());
            response.flushBuffer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //数据库操作
    private CompletableFuture<String> savePic(MultipartFile multipartFile, String filename, String description) throws Exception {
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        PicDomain picDomain = new PicDomain();
        picDomain.setDescription(description);
        picDomain.setPath(filename);
        return piclibFuture.create(picDomain).thenApply(info -> {
            return info;
        });
    }


    @RequestMapping(value = "/{id}")
    public CompletableFuture<String> findById(@PathVariable Integer id) {
        return piclibFuture.findById(id);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public CompletableFuture<String> findAll(Integer page, Integer pageSize, String description) {
        return piclibFuture.findPage(page, pageSize, description);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CompletableFuture<String> save(@RequestBody PicDomain picDomain) throws Exception {
        return piclibFuture.create(picDomain);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public CompletableFuture<String> delete(@PathVariable Integer id) throws Exception {
        return piclibFuture.delete(id);
    }


}
