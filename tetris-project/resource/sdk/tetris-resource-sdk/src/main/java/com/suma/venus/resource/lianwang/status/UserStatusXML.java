package com.suma.venus.resource.lianwang.status;

import javax.xml.bind.annotation.XmlElement;

public class UserStatusXML {

	//看协议格式应该对应的userNo
	private String userid;
	
	//状态（1--在线、0--不在线）
	private Integer status;
	
	//登录服务节点ID（32位uuID格式）
	private String visitednodeid;
	
	//11位设备编号格式
	private String binddevid;
	
	public UserStatusXML() {
	}
	
	public UserStatusXML(String userid, Integer status, String visitednodeid, String binddevid) {
		this.userid = userid;
		this.status = status;
		this.visitednodeid = visitednodeid;
		this.binddevid = binddevid;
	}

	@XmlElement(name="userid")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@XmlElement(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@XmlElement(name="visitednodeid")
	public String getVisitednodeid() {
		return visitednodeid;
	}

	public void setVisitednodeid(String visitednodeid) {
		this.visitednodeid = visitednodeid;
	}

	@XmlElement(name="binddevid")
	public String getBinddevid() {
		return binddevid;
	}

	public void setBinddevid(String binddevid) {
		this.binddevid = binddevid;
	}
	
}
