package com.sumavision.tetris.sts.device.group;

import com.sumavision.tetris.sts.device.DevicePO;
import com.sumavision.tetris.sts.device.DeviceVO;

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

    private List<DeviceVO> devices;

    public DeviceGroupVO(DeviceGroupPO deviceGroup) {
        this.id = deviceGroup.getId();
        this.name = deviceGroup.getName();
        this.devices = new ArrayList<>();
    }

    public DeviceGroupVO(DeviceGroupPO deviceGroup , List<DevicePO> devicePOs) {
        this.id = deviceGroup.getId();
        this.name = deviceGroup.getName();
        this.devices = new ArrayList<>();
        devicePOs.stream().forEach(devicePO -> this.devices.add(new DeviceVO(devicePO)));
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

}
