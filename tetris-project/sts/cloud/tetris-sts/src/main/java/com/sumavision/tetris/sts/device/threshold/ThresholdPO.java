package com.sumavision.tetris.sts.device.threshold;

import com.sumavision.tetris.sts.common.CommonPO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="threshold")
public class ThresholdPO extends CommonPO<ThresholdPO> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1551770308558000584L;

	/**
	 * CPU占用百分比
	 */
	private Integer cpuOccupyTh;
	/**
	 * GPU占用百分比
	 */
	private Integer gpuOccupyTh;
	/**
	 * 内存占用百分比
	 */
	private Integer memOccupyTh;
	/**
	 * CPU温度
	 */
	private Integer cpuTemperatureTh;
	
	/**
	 * 网卡流量阈值，单位b
	 */
	private Long netCardFlowMax;
	
	/**
	 * 硬盘占用百分比
	 */
	private Integer diskOccupyTh;
	
	@Column
	public Integer getCpuOccupyTh() {
		return cpuOccupyTh;
	}
	public void setCpuOccupyTh(Integer cpuOccupyTh) {
		this.cpuOccupyTh = cpuOccupyTh;
	}
	@Column
	public Integer getGpuOccupyTh() {
		return gpuOccupyTh;
	}
	public void setGpuOccupyTh(Integer gpuOccupyTh) {
		this.gpuOccupyTh = gpuOccupyTh;
	}
	@Column
	public Integer getMemOccupyTh() {
		return memOccupyTh;
	}
	public void setMemOccupyTh(Integer memOccupyTh) {
		this.memOccupyTh = memOccupyTh;
	}
	@Column
	public Integer getCpuTemperatureTh() {
		return cpuTemperatureTh;
	}
	public void setCpuTemperatureTh(Integer cpuTemperatureTh) {
		this.cpuTemperatureTh = cpuTemperatureTh;
	}
	@Column
	public Long getNetCardFlowMax() {
		return netCardFlowMax;
	}
	public void setNetCardFlowMax(Long netCardFlowMax) {
		this.netCardFlowMax = netCardFlowMax;
	}
	@Column
	public Integer getDiskOccupyTh() {
		return diskOccupyTh;
	}
	public void setDiskOccupyTh(Integer diskOccupyTh) {
		this.diskOccupyTh = diskOccupyTh;
	}
	
}
