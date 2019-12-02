package com.sumavision.tetris.system.storage.gadget;
/**
 * Cpu信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 下午3:02:28
 */
public class Cpu{
	
	/** cpu型号 */
	private String name;
	
	/** cpu使用率 */
	private String usageRate;
	
	/** 总内存大小 ，单位：B*/
	private long totalMemorySize;
	
	/** 使用内存大小 ，单位：B*/
	private long usedMemorySize;

	public String getName() {
		return name;
	}

	public Cpu setName(String name) {
		this.name = name;
		return this;
	}

	public String getUsageRate() {
		return usageRate;
	}

	public Cpu setUsageRate(String usageRate) {
		this.usageRate = usageRate;
		return this;
	}

	public long getTotalMemorySize() {
		return totalMemorySize;
	}

	public Cpu setTotalMemorySize(long totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
		return this;
	}

	public long getUsedMemorySize() {
		return usedMemorySize;
	}

	public Cpu setUsedMemorySize(long usedMemorySize) {
		this.usedMemorySize = usedMemorySize;
		return this;
	}
	
}