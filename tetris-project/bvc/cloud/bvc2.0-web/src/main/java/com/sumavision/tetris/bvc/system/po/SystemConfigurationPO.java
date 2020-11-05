package com.sumavision.tetris.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * <p>不需要全局磁盘大小</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月29日 上午11:04:47
 */
@Entity
@Table(name="TETRIS_BVC_SYSTEM_CONFIGURATION")
@Deprecated
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
