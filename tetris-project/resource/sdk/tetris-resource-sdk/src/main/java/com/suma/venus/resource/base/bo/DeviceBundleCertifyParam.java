package com.suma.venus.resource.base.bo;

/***
 * 设备特征bundle认证请求参数
 * @author lxw
 *
 */
public class DeviceBundleCertifyParam {

	/**设备账号名*/
	private String username;
	
	/**设备账号密码*/
	private String password;
	
	/**设备账号密码类型*/
	private String password_type;
	
	/**设备账号认证标识ID*/
	private String serial_num;
	
	/**
	 * 密码处理方式
	 * @author lxw
	 */
	public enum PasswordType{
		NOMAL, //正常处理
		SIP //SIP终端对应的加密处理
	}

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

	public String getPassword_type() {
		return password_type;
	}

	public void setPassword_type(String password_type) {
		this.password_type = password_type;
	}

	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

}
