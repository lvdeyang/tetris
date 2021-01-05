package com.sumavision.tetris.capacity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.template.feign.TemplateTaskService;
import com.sumavision.tetris.application.template.feign.TemplateTaskVO;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.business.director.service.DirectorTaskService;
import com.sumavision.tetris.business.push.service.ScheduleService;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.yjgb.service.TransformService;
import com.sumavision.tetris.capacity.template.TemplateService;
import com.sumavision.tetris.capacity.util.http.HttpUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Poemafar on 2020/7/28 11:55
 */
public class CapacityServiceTest extends TetrisCapacityApplicationTest {


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

    @Autowired
    TemplateTaskService templateTaskService;

    @Autowired
    TransformService transformService;

    @Autowired
    TaskOutputDAO taskOutputDAO;

    @Test
    public void testAlarmUrl() throws Exception {


    }

}