package com.sumavision.tetris.application.template.feign;/**
 * Created by Poemafar on 2020/12/9 17:35
 */

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.annotation.OprLog;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: TemplateController
 * @Description TODO 用于前台创建模板，删除模板，修改模板
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/9 17:35
 */
@Controller
@RequestMapping(value = "/template/task/feign")
public class TemplateTaskFeignController {

    @Autowired
    TemplateTaskService templateTaskService;

    @OprLog(name = "template")
    @JsonBody
    @ResponseBody
    @RequestMapping(value = "/add")
    public Object addTask(String taskInfo) throws Exception{
        TemplateTaskVO taskBO = JSONObject.parseObject(taskInfo, TemplateTaskVO.class);
        return templateTaskService.addTask(taskBO);
    }

    @OprLog(name = "template")
    @JsonBody
    @ResponseBody
    @RequestMapping(value = "/delete")
    public Object getAllTemplate(String task) throws Exception{
        JSONObject taskObj = JSONObject.parseObject(task);
        String taskId = taskObj.getString("task_id");
        return templateTaskService.deleteTask(taskId);
    }



}
