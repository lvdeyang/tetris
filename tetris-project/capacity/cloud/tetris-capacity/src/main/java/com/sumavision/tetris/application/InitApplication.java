package com.sumavision.tetris.application;/**
 * Created by Poemafar on 2020/11/16 16:48
 */

import com.sumavision.tetris.application.template.feign.TemplateTaskService;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName: InitApplication
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/16 16:48
 */
@Component
public class InitApplication implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitApplication.class);

    @Autowired
    TemplateTaskService templateTaskService;

    @Autowired
    TaskInputDAO taskInputDao;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOGGER.info("CAPACITY SERVICE START TO INIT ... ...");

        templateTaskService.init();

        startTimerToClearNoUseInputs();


        LOGGER.info("CAPACITY SERVICE INIT COMPLETED");
    }

    public void startTimerToClearNoUseInputs(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    //1小时清除
                    List<TaskInputPO> inputs = taskInputDao.findByCount(0);
                    if(inputs != null && inputs.size() > 0){
                        Date date = new Date();
                        List<TaskInputPO> needRemoveInputs = new ArrayList<TaskInputPO>();
                        for(TaskInputPO input: inputs){
                            if((date.getTime() - input.getUpdateTime().getTime()) > 1000 * 60 * 60){
                                needRemoveInputs.add(input);
                            }
                        }
                        if(needRemoveInputs.size() > 0){
                            taskInputDao.deleteInBatch(needRemoveInputs);
                        }
                    }

                } catch (Exception e) {
                    LOGGER.error("清除输入线程执行异常！", e);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,0,60*1000);

    }
}
