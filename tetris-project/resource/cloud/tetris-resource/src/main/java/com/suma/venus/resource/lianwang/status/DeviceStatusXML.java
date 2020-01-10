package com.suma.venus.resource.lianwang.status;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="devitem")
public class DeviceStatusXML {

	//协议里是11位数字格式
	private String devid;
	
	private Integer status;
	
	private Integer devtype;
	
	private String visitednodeid;
	
	public DeviceStatusXML() {
	}

	public DeviceStatusXML(String devid, Integer status, Integer devtype, String visitednodeid) {
		this.devid = devid;
		this.status = status;
		this.devtype = devtype;
		this.visitednodeid = visitednodeid;
	}

	@XmlElement(name="devid")
	public String getDevid() {
		return devid;
	}

	public void setDevid(String devid) {
		this.devid = devid;
	}

	@XmlElement(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@XmlElement(name="devtype")
	public Integer getDevtype() {
		return devtype;
	}

	public void setDevtype(Integer devtype) {
		this.devtype = devtype;
	}

	@XmlElement(name="visitednodeid")
	public String getVisitednodeid() {
		return visitednodeid;
	}

	public void setVisitednodeid(String visitednodeid) {
		this.visitednodeid = visitednodeid;
	}
	
}
