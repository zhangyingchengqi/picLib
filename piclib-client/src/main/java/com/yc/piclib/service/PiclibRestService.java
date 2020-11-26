package com.yc.piclib.service;

import com.google.gson.Gson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yc.piclib.client.PiclibClient;
import com.yc.piclib.domain.PicDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//Hystrix服务层:  调用 PiclibClient,实现断路器功能
@Service
public class PiclibRestService {
    @Autowired
    private PiclibClient piclibClient;


    @HystrixCommand(fallbackMethod = "findByIdFallback")
    public String findById(Integer id) {
        return piclibClient.findById(id);
    }

    private String findByIdFallback(Integer id) {
        Map map = new HashMap();
        map.put("code", "-1");
        map.put("msg", "服务异常");
        return new Gson().toJson(map);
    }

    @HystrixCommand(fallbackMethod = "findByIdFallback")
    public String findAll(Integer page, Integer pageSize,
                          String description) {
        return piclibClient.findAll(page, pageSize, description);
    }

    @HystrixCommand(fallbackMethod = "findByIdFallback")
    public String create(PicDomain picDomain) {
        return piclibClient.create(picDomain);
    }

    @HystrixCommand(fallbackMethod = "findByIdFallback")
    public String delete(Integer id) {
        return piclibClient.delete(id);
    }


}
