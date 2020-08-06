package com.sumavision.tetris.capacity.template;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.capacity.TetrisApplicationTest;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.PreProcessingBO;
import com.sumavision.tetris.capacity.constant.EncodeConstant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by Poemafar on 2020/7/28 11:55
 */
public class TemplateServiceTest  extends TetrisApplicationTest {


    @Autowired
    TemplateService templateService;

    @Test
    public void test() throws CommonException {


        String mp2Map = templateService.getVideoEncodeMap(EncodeConstant.TplVideoEncoder.VENCODER_X264);
        JSONObject mp2Obj = JSONObject.parseObject(mp2Map);
//        mp2Obj.put("bitrate",String.valueOf(1200/1000));
//        mp2Obj.put("sample_rate",String.valueOf(3400/1000));

        System.out.println(mp2Obj);
    }

}