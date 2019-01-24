package com.sumavision.tetris.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 公司信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月24日 上午8:59:06
 */
@Entity
@Table(name = "TETRIS_COMPANY")
public class CompanyPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 公司名称 */
	private String name;
	
	/** 申请人 */
	private String userId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
