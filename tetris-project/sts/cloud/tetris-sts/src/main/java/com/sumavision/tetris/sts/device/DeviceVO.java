package com.sumavision.tetris.sts.device;

import com.sumavision.tetris.sts.common.CommonConstants.*;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备层VO
 * @author lxw
 *
 */
public class DeviceVO implements Serializable {
    private static final long serialVersionUID = -5116155229075548719L;

    private Long id;

    private String name;

	private Long groupId;
	
	//设备类型，分为SDM2.0、SDM3.0、服务器
	private DeviceType deviceType;
	
	//设备主备属性
	private BackType backType;
	
//	private List<DeviceInfoVO> deviceInfoVOs;
	
	/**数据网卡分组id*/
    private String dataNetIds;
    
	//转发网卡分组id（转发工作模式）
	private Long transmitNetId;

//    private List<DeviceChannelAuthVO> authChannels;

    private Integer channelUsedNum = 0;

    private Integer channelTotalNum = 0;
	
    public DeviceVO() {
    }

    public DeviceVO(DevicePO devicePO) {
    	this.id = devicePO.getId();
    	this.name = devicePO.getName();
    	this.groupId = devicePO.getGroupId();
    	this.deviceType = devicePO.getDeviceType();
    	this.backType = devicePO.getBackType();
//    	this.deviceInfoVOs = new ArrayList<DeviceInfoVO>();
    }
    
    public DeviceVO(DevicePO devicePO, List<DeviceNodePO> deviceNodePOS) {
    	this.id = devicePO.getId();
    	this.name = devicePO.getName();
    	this.groupId = devicePO.getGroupId();
    	this.deviceType = devicePO.getDeviceType();
    	this.backType = devicePO.getBackType();
//    	this.deviceInfoVOs = new ArrayList<DeviceInfoVO>();
//    	deviceNodePOS.stream().forEach(deviceInfoPO -> {
//    		DeviceInfoVO deviceVO = new DeviceInfoVO(deviceInfoPO);
//    		this.deviceInfoVOs.add(deviceVO);
//            this.channelUsedNum += deviceVO.getChannelUsedNum();
//            this.channelTotalNum += deviceVO.getChannelTotalNum();
//    	});
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

//	public List<DeviceInfoVO> getDeviceInfoVOs() {
//		return deviceInfoVOs;
//	}

//	public void setDeviceInfoVOs(List<DeviceInfoVO> deviceInfoVOs) {
//		this.deviceInfoVOs = deviceInfoVOs;
//	}

	public String getDataNetIds() {
		return dataNetIds;
	}

	public void setDataNetIds(String dataNetIds) {
		this.dataNetIds = dataNetIds;
	}

//	public List<DeviceChannelAuthVO> getAuthChannels() {
//		return authChannels;
//	}

//	public void setAuthChannels(List<DeviceChannelAuthVO> authChannels) {
//		this.authChannels = authChannels;
//	}

	public Integer getChannelUsedNum() {
		return channelUsedNum;
	}

	public void setChannelUsedNum(Integer channelUsedNum) {
		this.channelUsedNum = channelUsedNum;
	}

	public Integer getChannelTotalNum() {
		return channelTotalNum;
	}

	public void setChannelTotalNum(Integer channelTotalNum) {
		this.channelTotalNum = channelTotalNum;
	}

	public Long getTransmitNetId() {
		return transmitNetId;
	}

	public void setTransmitNetId(Long transmitNetId) {
		this.transmitNetId = transmitNetId;
	}

	public BackType getBackType() {
		return backType;
	}

	public void setBackType(BackType backType) {
		this.backType = backType;
	}
	
}
