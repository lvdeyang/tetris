package com.suma.venus.resource.lianwang.auth;

import javax.xml.bind.annotation.XmlElement;

public class UserAuthXml {
	
	private String userid;
	
	private String auth;
	
	public UserAuthXml() {}
	
	public UserAuthXml(String userid, String auth) {
		this.userid = userid;
		this.auth = auth;
	}

	@XmlElement(name="userid")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@XmlElement(name="auth")
	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
	
}
