package com.sumavision.signal.bvc.entity.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流转发器 设备<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 上午11:52:03
 */
@Entity
@Table(name = "BVC_SIGNAL_REPEATER")
public class RepeaterPO extends AbstractBasePO {
	
	private static final long serialVersionUID = 1L;
	
	/** 流转发器名称 */
	private String name;
	
	/** 集群ip */
	private String ip;
	
	/** 流转发器ip */
	private String address;
	
	/** 流转发器请求port */
	private String port;
	
	/** 流转发器类型：主/备 */
	private RepeaterType type;
	
	/** 关联流转发器id */
	private String repeaterId;
	
	/** 控制开关是否打开 */
	private boolean isOpen;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "PORT")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public RepeaterType getType() {
		return type;
	}

	public void setType(RepeaterType type) {
		this.type = type;
	}

	@Column(name = "REPEATER_ID")
	public String getRepeaterId() {
		return repeaterId;
	}

	public void setRepeaterId(String repeaterId) {
		this.repeaterId = repeaterId;
	}

	@Column(name = "IS_OPEN")
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
}
