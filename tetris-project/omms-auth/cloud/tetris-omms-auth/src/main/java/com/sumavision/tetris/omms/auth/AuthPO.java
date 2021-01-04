package com.sumavision.tetris.omms.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;
@Entity
@Table(name = "TETRIS_OMMS_AUTH")
public class AuthPO extends AbstractBasePO {

	private String name;
	private String content;//json
	private String deviceId;
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Lob
	@Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT", nullable = true)
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="DEVICE_ID")
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
