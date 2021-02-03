package com.sumavision.tetris.omms.hardware.server.data.process;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_OMMS_SERVER_PROCESS_USAGE")
public class ServerProcessUsagePO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 一维数据的id */
	private Long dataId;
	
	/** 进程名称 */
	private String name;
	
	/** CPU使用率 */
	private String cpuUsage;
	
	/** 内存使用率*/
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
	
}
