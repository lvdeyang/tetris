package com.suma.venus.resource.vo;

public class NodeVO {

	private String app_code;
	
	private String sig_code;
	
	private String sip_addr;
	
	private String sig_user;
	
	private String sig_pwd;
	
	private boolean is_publish = false;

	public String getApp_code() {
		return app_code;
	}

	public NodeVO setApp_code(String app_code) {
		this.app_code = app_code;
		return this;
	}

	public String getSig_code() {
		return sig_code;
	}

	public NodeVO setSig_code(String sig_code) {
		this.sig_code = sig_code;
		return this;
	}

	public String getSip_addr() {
		return sip_addr;
	}

	public NodeVO setSip_addr(String sip_addr) {
		this.sip_addr = sip_addr;
		return this;
	}

	public String getSig_user() {
		return sig_user;
	}

	public NodeVO setSig_user(String sig_user) {
		this.sig_user = sig_user;
		return this;
	}

	public String getSig_pwd() {
		return sig_pwd;
	}

	public NodeVO setSig_pwd(String sig_pwd) {
		this.sig_pwd = sig_pwd;
		return this;
	}

	public boolean isIs_publish() {
		return is_publish;
	}

	public NodeVO setIs_publish(boolean is_publish) {
		this.is_publish = is_publish;
		return this;
	}
	
}
