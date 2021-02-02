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

	public ServerProcessUsageVO setDataId(Long dataId) {
		this.dataId = dataId;
		return this;
	}

	public String getName() {
		return name;
	}

	public ServerProcessUsageVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getCpuUsage() {
		return cpuUsage;
	}

	public ServerProcessUsageVO setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
		return this;
	}

	public String getMemoryUsage() {
		return memoryUsage;
	}

	public ServerProcessUsageVO setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
		return this;
	}

	@Override
	public ServerProcessUsageVO set(ServerProcessUsagePO entity) throws Exception {
		this.setDataId(entity.getDataId())
			.setCpuUsage(entity.getCpuUsage())
			.setName(entity.getName())
			.setUpdateTime(entity.getUpdateTime())
			.setMemoryUsage(entity.getMemoryUsage());
		return this;
	}

}
