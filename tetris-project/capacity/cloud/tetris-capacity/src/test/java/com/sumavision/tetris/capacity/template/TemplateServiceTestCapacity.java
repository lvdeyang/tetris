package com.sumavision.tetris.capacity.template;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.business.director.service.DirectorTaskService;
import com.sumavision.tetris.capacity.TetrisCapacityApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Poemafar on 2020/7/28 11:55
 */
public class TemplateServiceTestCapacity extends TetrisCapacityApplicationTest {


    @Autowired
    TemplateService templateService;

    @Autowired
    DirectorTaskService directorTaskService;

    @Test
    public void test() throws CommonException {

           String template = directorTaskService.getEncodeTemplateParamByEncodeType("x264");
        System.out.println(template);
    }

    @Test
    public void testJson() throws  CommonException{
        String jsonStr = "{\"sample_rate\":\"44.1\",\"sample_fmt\":\"s16\",\"bitrate\":\"128\",\"channel_layout\":\"stereo\",\"type\":\"mpeg4-aac-lc\"}";
        JSONObject obj = JSONObject.parseObject(jsonStr);

        System.out.println( obj.getFloat("sample_rate"));

    }

}