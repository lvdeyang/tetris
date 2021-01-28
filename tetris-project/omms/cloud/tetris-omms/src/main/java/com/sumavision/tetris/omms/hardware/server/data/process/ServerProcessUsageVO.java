package com.sumavision.tetris.omms.hardware.server.data.process;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServerProcessUsageVO extends AbstractBaseVO<ServerProcessUsageVO, ServerProcessUsagePO>{

private Long dataId;
	
	private String name;
	
	private String cpuUsage;
	
	private String memoryUsage;
	
	public Long getDataId() {
		return dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public String getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	@Override
	public ServerProcessUsageVO set(ServerProcessUsagePO entity) throws Exception {
		return null;
	}

}
