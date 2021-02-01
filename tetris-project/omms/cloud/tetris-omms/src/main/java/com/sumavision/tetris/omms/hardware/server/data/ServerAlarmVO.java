package com.sumavision.tetris.omms.hardware.server.data;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServerAlarmVO extends AbstractBaseVO<ServerAlarmVO, ServerAlarmPO>{
	
	/** cpu使用率限制 */
	private Long cpuRate;
	
	/** 内存使用率限制 */
	private Long memoryRate;
	
	/** 硬盘使用率限制 */
	private Long diskRate;
	
	/** 单个进程CPU使用 */
	private Long processCpu;
	
	public Long getProcessCpu() {
		return processCpu;
	}

	public void setProcessCpu(Long processCpu) {
		this.processCpu = processCpu;
	}

	public Long getCpuRate() {
		return cpuRate;
	}

	public void setCpuRate(Long cpuRate) {
		this.cpuRate = cpuRate;
	}

	public Long getMemoryRate() {
		return memoryRate;
	}

	public void setMemoryRate(Long memoryRate) {
		this.memoryRate = memoryRate;
	}

	public Long getDiskRate() {
		return diskRate;
	}

	public void setDiskRate(Long diskRate) {
		this.diskRate = diskRate;
	}

	@Override
	public ServerAlarmVO set(ServerAlarmPO entity) throws Exception {
		ServerAlarmVO vo = new ServerAlarmVO();
		vo.setCpuRate(entity.getCpuRate());
		vo.setMemoryRate(entity.getMemoryRate());
		vo.setDiskRate(entity.getDiskRate());
		vo.setProcessCpu(entity.getProcessCpu());
		return vo;
	}

}
