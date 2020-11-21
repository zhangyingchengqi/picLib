package com.yc;


import com.yc.piclib.domain.PageDomain;
import com.yc.piclib.domain.PicDomain;
import com.yc.piclib.service.PicService;
import org.jboss.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DaoConfiguration.class})
public class TestService {

    private static final Logger logger = Logger.getLogger(TestService.class);
    @Autowired
    private PicService picService;

    @Test
    public void testList() {
        logger.info("调用PicService.list");
        List<PicDomain> list = picService.list();
        System.out.println(list);
    }

    @Test
    public void testListByPage() {
        PicDomain picDomain = new PicDomain(1, "abc", "good");
        logger.info("调用PicService.listByPage");
        PageDomain<PicDomain> list = picService.listByPage(picDomain);
        System.out.println(list);
    }


}
