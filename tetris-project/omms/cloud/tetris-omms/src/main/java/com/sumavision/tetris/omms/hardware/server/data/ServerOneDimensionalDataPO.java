package com.sumavision.tetris.omms.hardware.server.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务器一维数据统计<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年8月24日 下午5:23:55
 */
@Entity
@Table(name = "TETRIS_OMMS_SERVER_ONE_DIMENSIONAL_DATA")
public class ServerOneDimensionalDataPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 内存总数 */
	private Long memoryTotal;
	
	/** 内存已缓存 kb*/
	private Long memoryBuff;
	
	/** 内存使用中 kb*/
	private Long memoryUsed;
	
	/** 内存空闲 kb*/
	private Long memoryFree;

	/** 进程总数 */
	private Long taskTotal;
	
	/** 进程正在运行数 */
	private Long taskRunning;
	
	/** 进程睡眠数 */
	private Long taskSleeping;
	
	/** 进程僵尸数 */
	private Long taskZombie;
	
	/** cpu名称 */
	private String cpuName;
	
	/** cpu使用率 */
	private Float cpuOccupy;
	
	/** 系统时间 */
	private String systemTime;
	
	/** 开机时间 */
	private String lastRebootTime;
	
	/** 运行时间 */
	private String upTime;
	
	/** CPU温度 */
	private String temperature;
	
	/** 风扇转速 */
	private Long fanspeed;
	
	/** 服务器id */
	private Long serverId;

	@Column(name = "MEMORY_TOTAL")
	public Long getMemoryTotal() {
		return memoryTotal;
	}

	public void setMemoryTotal(Long memoryTotal) {
		this.memoryTotal = memoryTotal;
	}

	@Column(name = "MEMORY_BUFF")
	public Long getMemoryBuff() {
		return memoryBuff;
	}

	public void setMemoryBuff(Long memoryBuff) {
		this.memoryBuff = memoryBuff;
	}

	@Column(name = "MEMORY_USED")
	public Long getMemoryUsed() {
		return memoryUsed;
	}

	public void setMemoryUsed(Long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}

	@Column(name = "MEMORY_FREE")
	public Long getMemoryFree() {
		return memoryFree;
	}

	public void setMemoryFree(Long memoryFree) {
		this.memoryFree = memoryFree;
	}

	@Column(name = "TASK_TOTAL")
	public Long getTaskTotal() {
		return taskTotal;
	}

	public void setTaskTotal(Long taskTotal) {
		this.taskTotal = taskTotal;
	}

	@Column(name = "TASK_RUNNING")
	public Long getTaskRunning() {
		return taskRunning;
	}

	public void setTaskRunning(Long taskRunning) {
		this.taskRunning = taskRunning;
	}

	@Column(name = "TASK_SLEEPING")
	public Long getTaskSleeping() {
		return taskSleeping;
	}

	public void setTaskSleeping(Long taskSleeping) {
		this.taskSleeping = taskSleeping;
	}

	@Column(name = "TASK_ZOMBIE")
	public Long getTaskZombie() {
		return taskZombie;
	}

	public void setTaskZombie(Long taskZombie) {
		this.taskZombie = taskZombie;
	}

	@Column(name = "CPU_NAME")
	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	@Column(name = "CPU_OCCUPY")
	public Float getCpuOccupy() {
		return cpuOccupy;
	}

	public void setCpuOccupy(Float cpuOccupy) {
		this.cpuOccupy = cpuOccupy;
	}

	@Column(name = "SYSTEM_TIME")
	public String getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}

	@Column(name = "LAST_REBOOT_TIME")
	public String getLastRebootTime() {
		return lastRebootTime;
	}

	public void setLastRebootTime(String lastRebootTime) {
		this.lastRebootTime = lastRebootTime;
	}

	@Column(name = "UP_TIME")
	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}
	
	@Column(name = "TEMPERATURE")
	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	@Column(name = "FANSPEED")
	public Long getFanspeed() {
		return fanspeed;
	}

	public void setFanspeed(Long fanspeed) {
		this.fanspeed = fanspeed;
	}

	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	
}
