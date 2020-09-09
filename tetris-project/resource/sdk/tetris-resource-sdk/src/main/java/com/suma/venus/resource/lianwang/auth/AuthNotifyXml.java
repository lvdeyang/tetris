package com.suma.venus.resource.lianwang.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.suma.venus.resource.pojo.BundlePO;

@XmlRootElement(name="control")
public class AuthNotifyXml {

	private String commandname = "authnotify";
	
	private String seq = BundlePO.createBundleId();
	
	private Long ts = new Date().getTime();
	
	//授权操作类型 add | edit | remove
	private String operation;
	
	//授权操作服务节点
	private String authnodeid;
	
	//授权操作用户ID
	private String authuserid;
	
	//接受授权用户ID
	private String userid;
	
	//用户操作权限列表
	private List<UserAuthXml> userlist = new ArrayList<UserAuthXml>();
	
	//设备操作权限列表
	private List<DevAuthXml> devlist = new ArrayList<DevAuthXml>();

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

	@XmlElement(name="operation")
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@XmlElement(name="authnodeid")
	public String getAuthnodeid() {
		return authnodeid;
	}

	public void setAuthnodeid(String authnodeid) {
		this.authnodeid = authnodeid;
	}

	@XmlElement(name="authuserid")
	public String getAuthuserid() {
		return authuserid;
	}

	public void setAuthuserid(String authuserid) {
		this.authuserid = authuserid;
	}

	@XmlElement(name="userid")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@XmlElementWrapper(name = "userlist") 
    @XmlElement(name = "useritem")
	public List<UserAuthXml> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<UserAuthXml> userlist) {
		this.userlist = userlist;
	}

	@XmlElementWrapper(name = "devlist") 
    @XmlElement(name = "devitem")
	public List<DevAuthXml> getDevlist() {
		return devlist;
	}

	public void setDevlist(List<DevAuthXml> devlist) {
		this.devlist = devlist;
	}
	
}
