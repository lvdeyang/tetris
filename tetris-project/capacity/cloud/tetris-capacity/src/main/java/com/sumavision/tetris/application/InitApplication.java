package com.sumavision.tetris.application;/**
 * Created by Poemafar on 2020/11/16 16:48
 */

import com.sumavision.tetris.application.template.InputFactory;
import com.sumavision.tetris.application.template.TemplateTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOGGER.info("CAPACITY SERVICE START TO INIT ... ...");

        templateTaskService.init();

        LOGGER.info("CAPACITY SERVICE INIT COMPLETED");
    }
}
