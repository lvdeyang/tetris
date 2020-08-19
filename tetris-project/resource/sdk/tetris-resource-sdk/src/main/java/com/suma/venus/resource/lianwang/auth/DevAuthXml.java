package com.suma.venus.resource.lianwang.auth;

import javax.xml.bind.annotation.XmlElement;

public class DevAuthXml {

	private String devid;
	
	private String auth;
	
	public DevAuthXml() {}

	public DevAuthXml(String devid, String auth) {
		this.devid = devid;
		this.auth = auth;
	}

	@XmlElement(name="devid")
	public String getDevid() {
		return devid;
	}

	public void setDevid(String devid) {
		this.devid = devid;
	}

	@XmlElement(name="auth")
	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
	
}
