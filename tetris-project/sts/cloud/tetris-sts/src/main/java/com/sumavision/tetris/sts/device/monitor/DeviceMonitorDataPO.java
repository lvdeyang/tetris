package com.sumavision.tetris.sts.device.monitor;

import com.sumavision.tetris.sts.common.CommonPO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="DeviceMonitorDataPO")
public class DeviceMonitorDataPO extends CommonPO<DeviceMonitorDataPO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5417990759987052941L;

	//设备节点id
	private Long deviceId;
	//内存占用
	private Integer memOccupy;
	//内存大小
	private Long memSize;
	//cpu占用
	private Integer cpuOccupy;
	//cpu温度
	private Integer cpuTemperature;
	//gpu占用
	private Integer gpuOccupy;
	//获取时间
	private Date getTime;
	//硬盘占用
	private Integer diskOccupy;
	//网卡流量，对应监控组件里的NetworkBO
	private String netCardFlowJsonArray;
	//数据类型
	private DeviceMonitorDateType dateType = DeviceMonitorDateType.ORIGINAL;
	
	public static enum DeviceMonitorDateType{
		//原始数据
		ORIGINAL,
		//15分钟平均数据
		FIFTEEN_MINUTES_AVERAGE		
	}
	
	@Column
	public Integer getMemOccupy() {
		return memOccupy;
	}
	public void setMemOccupy(Integer memOccupy) {
		this.memOccupy = memOccupy;
	}
	@Column
	public Long getMemSize() {
		return memSize;
	}
	public void setMemSize(Long memSize) {
		this.memSize = memSize;
	}
	@Column
	public Integer getCpuOccupy() {
		return cpuOccupy;
	}
	public void setCpuOccupy(Integer cpuOccupy) {
		this.cpuOccupy = cpuOccupy;
	}
	@Column
	public Integer getCpuTemperature() {
		return cpuTemperature;
	}
	public void setCpuTemperature(Integer cpuTemperature) {
		this.cpuTemperature = cpuTemperature;
	}
	@Column
	public Integer getGpuOccupy() {
		return gpuOccupy;
	}
	public void setGpuOccupy(Integer gpuOccupy) {
		this.gpuOccupy = gpuOccupy;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	@Column
	@Enumerated(EnumType.STRING)
	public DeviceMonitorDateType getDateType() {
		return dateType;
	}
	public void setDateType(DeviceMonitorDateType dateType) {
		this.dateType = dateType;
	}
	@Column
	public Integer getDiskOccupy() {
		return diskOccupy;
	}
	public void setDiskOccupy(Integer diskOccupy) {
		this.diskOccupy = diskOccupy;
	}
	@Column(columnDefinition="TEXT")
	public String getNetCardFlowJsonArray() {
		return netCardFlowJsonArray;
	}
	public void setNetCardFlowJsonArray(String netCardFlowJsonArray) {
		this.netCardFlowJsonArray = netCardFlowJsonArray;
	}
	
}
