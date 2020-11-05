package com.sumavision.tetris.sts.device.monitor;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.Date;

public class DeviceMonitorBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2824876183878555888L;

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
	
	private Integer diskOccupy;
	
	private JSONArray netCardFlowJsonArray;

	private Date date;
	
	
	
	public DeviceMonitorBO(DeviceMonitorDataPO deviceMonitorDataPO){
		setMemOccupy(deviceMonitorDataPO.getMemOccupy());
		setMemSize(deviceMonitorDataPO.getMemSize());
		setCpuOccupy(deviceMonitorDataPO.getCpuOccupy());
		setCpuTemperature(deviceMonitorDataPO.getCpuTemperature());
		setGpuOccupy(deviceMonitorDataPO.getGpuOccupy());
		setDiskOccupy(deviceMonitorDataPO.getDiskOccupy());
		try {
			setNetCardFlowJsonArray(JSONArray.parseArray(deviceMonitorDataPO.getNetCardFlowJsonArray()));
		} catch (Exception e) {
			// TODO: handle exception
			setNetCardFlowJsonArray(new JSONArray());
		}
		setDate(deviceMonitorDataPO.getGetTime());
	}
	
	public Integer getMemOccupy() {
		return memOccupy;
	}
	public void setMemOccupy(Integer memOccupy) {
		this.memOccupy = memOccupy;
	}
	public Long getMemSize() {
		return memSize;
	}
	public void setMemSize(Long memSize) {
		this.memSize = memSize;
	}
	public Integer getCpuOccupy() {
		return cpuOccupy;
	}
	public void setCpuOccupy(Integer cpuOccupy) {
		this.cpuOccupy = cpuOccupy;
	}
	public Integer getCpuTemperature() {
		return cpuTemperature;
	}
	public void setCpuTemperature(Integer cpuTemperature) {
		this.cpuTemperature = cpuTemperature;
	}
	public Integer getGpuOccupy() {
		return gpuOccupy;
	}
	public void setGpuOccupy(Integer gpuOccupy) {
		this.gpuOccupy = gpuOccupy;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getDiskOccupy() {
		return diskOccupy;
	}

	public void setDiskOccupy(Integer diskOccupy) {
		this.diskOccupy = diskOccupy;
	}

	public JSONArray getNetCardFlowJsonArray() {
		return netCardFlowJsonArray;
	}

	public void setNetCardFlowJsonArray(JSONArray netCardFlowJsonArray) {
		this.netCardFlowJsonArray = netCardFlowJsonArray;
	}
	
	
}
