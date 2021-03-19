package com.sumavision.tetris.cs.channel;

/**
 * 同步任务参数<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月11日 上午11:07:07
 */

import java.util.List;

public class syncVO {
	
	/** 转换IP */
	private String deviceIp;
	
	/** 转换业务类型 */
	private String businesstype;
	
	/** 播发任务Id */
	private List<String> jobIds;

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public List<String> getJobIds() {
		return jobIds;
	}

	public void setJobIds(List<String> jobIds) {
		this.jobIds = jobIds;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	
	

}
