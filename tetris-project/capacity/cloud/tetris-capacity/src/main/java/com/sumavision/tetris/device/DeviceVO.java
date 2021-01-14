package com.sumavision.tetris.device;

import com.sumavision.tetris.business.common.SpringBeanFactory;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;

import java.io.Serializable;
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

	/**
	 * 任务总数
	 */
	private Integer taskNum;

	//设备主备属性
	private String backType;
	
	/**数据网卡分组id*/
    private String dataNetIds;
    
	//转发网卡分组id（转发工作模式）
	private Long transmitNetId;

//    private List<TransAuthVO> authChannels;

    private Integer channelUsedNum = 0;

    private Integer channelTotalNum = 0;

    private String deviceIp;

	List<NetCardInfoPO> netCards;

	private Boolean netConfig;

	private String funUnitStatus;

    public DeviceVO() {
    }

	public DeviceVO(DevicePO devicePO) {
		NetCardInfoDao netCardInfoDao = SpringBeanFactory.getBean(NetCardInfoDao.class);
		TaskOutputDAO taskOutputDAO = SpringBeanFactory.getBean(TaskOutputDAO.class);
		this.id = devicePO.getId();
		this.name = devicePO.getName();
		this.groupId = devicePO.getDeviceGroupId();
		this.backType = devicePO.getBackType().name();
		this.deviceIp=devicePO.getDeviceIp();
		this.netConfig=devicePO.getNetConfig();
		this.funUnitStatus=devicePO.getFunUnitStatus().name();
		this.taskNum = taskOutputDAO.countDistinctByCapacityIpAndOutputNotNullAndTaskNotNull(devicePO.getDeviceIp());
		List<NetCardInfoPO> nets = netCardInfoDao.findByDeviceId(devicePO.getId());
		if (nets!=null && !nets.isEmpty()){
			this.netCards=nets;
		}
	}


	public DeviceVO(DevicePO devicePO,List<NetCardInfoPO> netCards) {
    	this.id = devicePO.getId();
    	this.name = devicePO.getName();
    	this.groupId = devicePO.getDeviceGroupId();
    	this.backType = devicePO.getBackType().name();
    	this.deviceIp=devicePO.getDeviceIp();
    	this.netConfig=devicePO.getNetConfig();
    	this.funUnitStatus=devicePO.getFunUnitStatus().name();
    	this.netCards=netCards;
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


	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getDataNetIds() {
		return dataNetIds;
	}

	public void setDataNetIds(String dataNetIds) {
		this.dataNetIds = dataNetIds;
	}

//	public List<TransAuthVO> getAuthChannels() {
//		return authChannels;
//	}

//	public void setAuthChannels(List<TransAuthVO> authChannels) {
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

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public List<NetCardInfoPO> getNetCards() {
		return netCards;
	}

	public void setNetCards(List<NetCardInfoPO> netCards) {
		this.netCards = netCards;
	}

	public Boolean getNetConfig() {
		return netConfig;
	}

	public void setNetConfig(Boolean netConfig) {
		this.netConfig = netConfig;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	public String getFunUnitStatus() {
		return funUnitStatus;
	}

	public void setFunUnitStatus(String funUnitStatus) {
		this.funUnitStatus = funUnitStatus;
	}
}
