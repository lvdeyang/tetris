package com.sumavision.tetris.bvc.model.layout;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.bo.positionDstBO;
import com.sumavision.tetris.orm.po.AbstractBasePO;
/**
 * 虚拟源/画面<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月23日 下午3:37:05
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_LAYOUT")
public class LayoutPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 画面名称 */
	private String name;
	
	/** 虚拟源中源的数量*/
	private Long positionNum;

	@Column(name = "POSITION_NUM")
	public Long getPositionNum() {
		return positionNum;
	}

	public void setPositionNum(Long positionNum) {
		this.positionNum = positionNum;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
