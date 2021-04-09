package com.sumavision.tetris.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="TETRIS_BVC_SYSTEM_CONFIGURATION")
public class SystemConfigurationPO extends AbstractBasePO{
	
	/** 磁盘大小*/
	private Integer totalSizeMb;

	@Column(name="TOTAL_SIZE_MB")
	public Integer getTotalSizeMb() {
		return totalSizeMb;
	}

	public SystemConfigurationPO setTotalSizeMb(Integer totalSizeMb) {
		this.totalSizeMb = totalSizeMb;
		return this;
	}
	
	
}
