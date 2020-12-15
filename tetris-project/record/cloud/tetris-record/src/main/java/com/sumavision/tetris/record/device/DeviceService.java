package com.sumavision.tetris.record.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.capacity.server.CapacityService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.record.strategy.RecordStrategyDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO;
import com.sumavision.tetris.record.task.service.RecordCapacityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);


    @Autowired
    DeviceDAO deviceDAO;

    @Autowired
    RecordCapacityService recordCapacityService;

    @Autowired
    CapacityService capacityService;

    @Autowired
    RecordStrategyDAO recordStrategyDAO;

    public void sync(Long id) throws BaseException {
        DevicePO devicePO = deviceDAO.findOne(id);
        if (devicePO==null){
            throw new BaseException(StatusCode.FORBIDDEN,"device not found,id:"+id);
        }

        JSONObject syncObj = new JSONObject();
        List<String> jobIds = new ArrayList<>();
        List<RecordStrategyPO> recordTasks = recordStrategyDAO.findByDeviceIdAndStatus(devicePO.getId(), RecordStrategyPO.EStrategyStatus.RUNNING);
        if (recordTasks==null || recordTasks.isEmpty()){
            return;
        }
        recordTasks.stream().forEach(t->{
            jobIds.add(t.getCapacityTaskId());
        });
        syncObj.put("deviceIp",devicePO.getDeviceIP());
        syncObj.put("jobIds",jobIds);
        syncObj.put("businessType","RECORD");
        try {
            LOGGER.info("Request: <sync-all>, params: {}",syncObj.toJSONString());
            String result = capacityService.sync(syncObj.toJSONString());
            LOGGER.info("Request: <sync-all>, params: {}",result);
            //todo 如果能力测缺任务还需要重下任务
        } catch (Exception e) {
            LOGGER.error("sync fail",e);
            throw new BaseException(StatusCode.ERROR,"同步失败");
        }

    }
}
