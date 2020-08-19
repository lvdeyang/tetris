package com.sumavision.signal.bvc.entity.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.signal.bvc.entity.enumeration.InternetAccessType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流转发器网口<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午1:17:02
 */
@Entity
@Table(name = "BVC_SIGNAL_INTERNET_ACCESS")
public class InternetAccessPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 网口ip地址 */
	private String address;
	
	/** 网口类型 */
	private InternetAccessType type;
	
	/** 所属流转发器id */
	private Long repeaterId;

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public InternetAccessType getType() {
		return type;
	}

	public void setType(InternetAccessType type) {
		this.type = type;
	}

	@Column(name = "REPEATER_ID")
	public Long getRepeaterId() {
		return repeaterId;
	}

	public void setRepeaterId(Long repeaterId) {
		this.repeaterId = repeaterId;
	}

}
