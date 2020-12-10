package com.sumavision.tetris.capacity.server;/**
 * Created by Poemafar on 2020/12/10 16:24
 */

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: TemplateService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/10 16:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TemplateService {

    @Autowired
    private CapacityFeign capacityFeign;


    /**
     * @MethodName: addTask
     * @Description: 通过模板下发任务
     * @param taskInfo 任务参数
     * @Return: java.lang.String
     * @Author: Poemafar
     * @Date: 2020/12/10 16:27
     **/
    public String addTask(String taskInfo) throws Exception{
        return JsonBodyResponseParser.parseObject(capacityFeign.addTaskByTemplate(taskInfo), String.class);
    }
}
