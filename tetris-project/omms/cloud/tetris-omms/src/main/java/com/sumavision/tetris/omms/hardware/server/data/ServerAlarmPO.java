package com.sumavision.tetris.omms.hardware.server.data;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_OMMS_SERVER_ALARM")
public class ServerAlarmPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
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
	
}
