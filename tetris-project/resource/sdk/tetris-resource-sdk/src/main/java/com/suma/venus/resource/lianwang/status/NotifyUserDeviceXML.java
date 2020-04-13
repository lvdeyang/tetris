package com.suma.venus.resource.lianwang.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.suma.venus.resource.pojo.BundlePO;

@XmlRootElement(name="notify")
public class NotifyUserDeviceXML {

	private String commandname = "syncinfo";
	
	private String seq = BundlePO.createBundleId();
	
	private Long ts = new Date().getTime();
	
	private List<UserStatusXML> userlist = new ArrayList<UserStatusXML>();
	
	private List<DeviceStatusXML> devlist = new ArrayList<DeviceStatusXML>();

	@XmlElement(name="commandname")
	public String getCommandname() {
		return commandname;
	}

	public void setCommandname(String commandname) {
		this.commandname = commandname;
	}

	@XmlElement(name="seq")
	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	@XmlElement(name="ts")
	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	@XmlElementWrapper(name = "userlist") 
    @XmlElement(name = "useritem")
	public List<UserStatusXML> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<UserStatusXML> userlist) {
		this.userlist = userlist;
	}

	@XmlElementWrapper(name = "devlist") 
    @XmlElement(name = "devitem")
	public List<DeviceStatusXML> getDevlist() {
		return devlist;
	}

	public void setDevlist(List<DeviceStatusXML> devlist) {
		this.devlist = devlist;
	}
	
}
