package com.sumavision.tetris.patrol.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_PATROL_ADDRESS")
public class AddressPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 地址名称 */
	private String name;
	
	/** 地址签到url */
	private String url;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
