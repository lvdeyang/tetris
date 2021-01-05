package com.sumavision.tetris.application.alarm;/**
 * Created by Poemafar on 2020/12/28 10:26
 */

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.alarm.bo.http.AlarmNotifyBO;
import com.sumavision.tetris.application.alarm.service.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: AlarmController
 * @Description TODO 告警服务发来的告警
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/28 10:26
 */
@RestController
@RequestMapping(value = "/tetris-capacity/alarm/")
public class AlarmController {

    private static Logger LOGGER = LoggerFactory.getLogger(AlarmController.class);

    @Autowired
    AlarmService alarmService;

    @RequestMapping(value = "/{code}", method = RequestMethod.POST)
    public ResponseEntity receiveAlarm(@PathVariable String code, @RequestBody AlarmNotifyBO alarmNotifyBO) {
        LOGGER.info("Receive alarm from [alarm service], code: " + code + ", alarm : "
                + JSON.toJSONString(alarmNotifyBO));
        alarmService.receiveAlarm(code, alarmNotifyBO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
