package com.sumavision.tetris.cs.template;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name = "TETRIS_CS_CHANNEL_TEMPLATE")
public class ChannelTemplatePO extends AbstractBasePO {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	public static final String VERSION_OF_ORIGIN = "0.0";

	/** 名称 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
