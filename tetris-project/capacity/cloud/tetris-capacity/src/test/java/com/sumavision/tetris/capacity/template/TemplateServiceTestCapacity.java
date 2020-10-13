package com.sumavision.tetris.capacity.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.ResultBO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.business.director.service.DirectorTaskService;
import com.sumavision.tetris.business.push.service.ScheduleService;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.capacity.TetrisCapacityApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Poemafar on 2020/7/28 11:55
 */
public class TemplateServiceTestCapacity extends TetrisCapacityApplicationTest {


    @Autowired
    TemplateService templateService;

    @Autowired
    DirectorTaskService directorTaskService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    TaskService taskService;

    @Autowired
    TranscodeTaskService transcodeTaskService;

    @Test
    public void testTransfer() throws Exception {
        // scheduleService.test();

//        ResultBO resultBO = taskService.transferStream("10.10.40.207",
//                "TS_PASSBY",
//                ProtocolType.UDP_TS,
//                "udp://10.10.40.207:13412",
//                ProtocolType.UDP_TS,
//                "udp://10.10.40.24:13414",
//                BusinessType.DIRECTOR);
//        System.out.println(JSONObject.toJSONString(resultBO));
    }

    @Test
    public void deleteTask() throws Exception {
//        transcodeTaskService.delete("TS_PASSBY",BusinessType.DIRECTOR);
    }

    @Test
    public void test() throws CommonException {
       // scheduleService.test();
    }

    @Test
    public void testJson() throws  CommonException{
        String outputUrl = "http://10.10.40.103:1010/";

        String urlWithoutHead = outputUrl.split("://")[1];
        String pubName = urlWithoutHead.substring(urlWithoutHead.indexOf(":")).split("/",2)[1] ;
        System.out.println(pubName);
    }

}