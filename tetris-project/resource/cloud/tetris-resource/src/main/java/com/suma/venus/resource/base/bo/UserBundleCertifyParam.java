package com.suma.venus.resource.base.bo;
/**
 * 用户特征bundle认证请求参数
 * @author lxw
 *
 */
public class UserBundleCertifyParam {

	/**用户名*/
	private String username;
	
	/**密码*/
	private String password;
	
	/**bundle形态，如jv210、tvos等*/
	private String bundle_model;
	
	/**bundle类型，如VenusTerminal、VenusMixer等*/
	private String bundle_type;
	
	/**ca卡号*/
	private String ca_num;

	/*设备序列号*/
	private String serial_num;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBundle_model() {
		return bundle_model;
	}

	public void setBundle_model(String bundle_model) {
		this.bundle_model = bundle_model;
	}

	public String getBundle_type() {
		return bundle_type;
	}

	public void setBundle_type(String bundle_type) {
		this.bundle_type = bundle_type;
	}

	public String getCa_num() {
		return ca_num;
	}

	public void setCa_num(String ca_num) {
		this.ca_num = ca_num;
	}

	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

}
