package com.sumavision.tetris.omms.hardware.server.data;


import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServerOneDimensionalDataVO extends AbstractBaseVO<ServerOneDimensionalDataVO, ServerOneDimensionalDataPO>{

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

	public Long getMemoryTotal() {
		return memoryTotal;
	}

	public ServerOneDimensionalDataVO setMemoryTotal(Long memoryTotal) {
		this.memoryTotal = memoryTotal;
		return this;
	}

	public Long getMemoryBuff() {
		return memoryBuff;
	}

	public ServerOneDimensionalDataVO setMemoryBuff(Long memoryBuff) {
		this.memoryBuff = memoryBuff;
		return this;
	}

	public Long getMemoryUsed() {
		return memoryUsed;
	}

	public ServerOneDimensionalDataVO setMemoryUsed(Long memoryUsed) {
		this.memoryUsed = memoryUsed;
		return this;
	}

	public Long getMemoryFree() {
		return memoryFree;
	}

	public ServerOneDimensionalDataVO setMemoryFree(Long memoryFree) {
		this.memoryFree = memoryFree;
		return this;
	}

	public Long getTaskTotal() {
		return taskTotal;
	}

	public ServerOneDimensionalDataVO setTaskTotal(Long taskTotal) {
		this.taskTotal = taskTotal;
		return this;
	}

	public Long getTaskRunning() {
		return taskRunning;
	}

	public ServerOneDimensionalDataVO setTaskRunning(Long taskRunning) {
		this.taskRunning = taskRunning;
		return this;
	}

	public Long getTaskSleeping() {
		return taskSleeping;
	}

	public ServerOneDimensionalDataVO setTaskSleeping(Long taskSleeping) {
		this.taskSleeping = taskSleeping;
		return this;
	}

	public Long getTaskZombie() {
		return taskZombie;
	}

	public ServerOneDimensionalDataVO setTaskZombie(Long taskZombie) {
		this.taskZombie = taskZombie;
		return this;
	}

	public String getCpuName() {
		return cpuName;
	}

	public ServerOneDimensionalDataVO setCpuName(String cpuName) {
		this.cpuName = cpuName;
		return this;
	}

	public Float getCpuOccupy() {
		return cpuOccupy;
	}

	public ServerOneDimensionalDataVO setCpuOccupy(Float cpuOccupy) {
		this.cpuOccupy = cpuOccupy;
		return this;
	}

	public String getSystemTime() {
		return systemTime;
	}

	public ServerOneDimensionalDataVO setSystemTime(String systemTime) {
		this.systemTime = systemTime;
		return this;
	}

	public String getLastRebootTime() {
		return lastRebootTime;
	}

	public ServerOneDimensionalDataVO setLastRebootTime(String lastRebootTime) {
		this.lastRebootTime = lastRebootTime;
		return this;
	}

	public String getUpTime() {
		return upTime;
	}

	public ServerOneDimensionalDataVO setUpTime(String upTime) {
		this.upTime = upTime;
		return this;
	}

	public String getTemperature() {
		return temperature;
	}

	public ServerOneDimensionalDataVO setTemperature(String temperature) {
		this.temperature = temperature;
		return this;
	}

	public Long getFanspeed() {
		return fanspeed;
	}

	public ServerOneDimensionalDataVO setFanspeed(Long fanspeed) {
		this.fanspeed = fanspeed;
		return this;
	}

	@Override
	public ServerOneDimensionalDataVO set(ServerOneDimensionalDataPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setMemoryTotal(entity.getMemoryTotal())
			.setMemoryBuff(entity.getMemoryBuff())
			.setMemoryFree(entity.getMemoryFree())
			.setMemoryUsed(entity.getMemoryUsed())
			.setTaskRunning(entity.getTaskRunning())
			.setTaskSleeping(entity.getTaskSleeping())
			.setTaskTotal(entity.getTaskTotal())
			.setTaskZombie(entity.getTaskZombie())
			.setCpuName(entity.getCpuName())
			.setCpuOccupy(entity.getCpuOccupy())
			.setSystemTime(entity.getSystemTime())
			.setLastRebootTime(entity.getLastRebootTime())
			.setUpTime(entity.getUpTime())
			.setTemperature(entity.getTemperature())
			.setFanspeed(entity.getFanspeed());
		return this;
	}
	
}
