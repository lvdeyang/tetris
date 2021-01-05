package com.sumavision.tetris.device.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumavision.tetris.business.common.SpringBeanFactory;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.DeviceVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lost on 2017/2/24.
 */
public class DeviceGroupVO implements Serializable {
    private static final long serialVersionUID = -2255122012670477163L;

    private Long id;

    private String name;

    private String backupStrategy;

    private List<DeviceVO> devices;

    private Integer deviceNum;

    private Integer taskNum;

    private Boolean autoBackupFlag = true;

    public DeviceGroupVO(DeviceGroupPO deviceGroup) {
        this.id = deviceGroup.getId();
        this.name = deviceGroup.getName();
        this.backupStrategy = deviceGroup.getBackupStrategy().name();
        this.autoBackupFlag=deviceGroup.getAutoBackupFlag();
        this.devices = new ArrayList<>();
    }

    public DeviceGroupVO(DeviceGroupPO deviceGroup , List<DevicePO> devicePOs) {
        TaskOutputDAO taskOutputDAO = SpringBeanFactory.getBean(TaskOutputDAO.class);
        this.id = deviceGroup.getId();
        this.name = deviceGroup.getName();
        this.backupStrategy = deviceGroup.getBackupStrategy().name();
        this.autoBackupFlag = deviceGroup.getAutoBackupFlag();
        this.devices = new ArrayList<>();
        this.deviceNum = devicePOs.size();
        Integer taskNum = 0;
        for (int i = 0; i < devicePOs.size(); i++) {
            DevicePO devicePO = devicePOs.get(i);
            Integer curDevTaskNum = taskOutputDAO.countDistinctByCapacityIpAndOutputNotNullAndTaskNotNull(devicePO.getDeviceIp());
            DeviceVO deviceVO = new DeviceVO(devicePO);
            deviceVO.setTaskNum(curDevTaskNum);
            this.devices.add(deviceVO);
            taskNum+=curDevTaskNum;
        }
        this.taskNum = taskNum;
    }

    public List<DeviceVO> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceVO> devices) {
        this.devices = devices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public String getBackupStrategy() {
        return backupStrategy;
    }

    public void setBackupStrategy(String backupStrategy) {
        this.backupStrategy = backupStrategy;
    }

    public Boolean getAutoBackupFlag() {
        return autoBackupFlag;
    }

    public void setAutoBackupFlag(Boolean autoBackupFlag) {
        this.autoBackupFlag = autoBackupFlag;
    }
}
