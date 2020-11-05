package com.sumavision.bvc.device.group.bo;

/**
 * 接收双向呼叫认证是否接听<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月21日 下午3:23:17
 */
public class UserActionPassbyContentBO implements BasePassByContent{

	private String cmd = "venus_invite_user_action";
	
	private String ldap_user;
	
	private String local_user;
	
	/** 1接听, 2拒绝, 3:没登录,4:没有这个用户 */
	private int action;

	public String getCmd() {
		return cmd;
	}

	public UserActionPassbyContentBO setCmd(String cmd) {
		this.cmd = cmd;
		return this;
	}

	public String getLdap_user() {
		return ldap_user;
	}

	public UserActionPassbyContentBO setLdap_user(String ldap_user) {
		this.ldap_user = ldap_user;
		return this;
	}

	public String getLocal_user() {
		return local_user;
	}

	public UserActionPassbyContentBO setLocal_user(String local_user) {
		this.local_user = local_user;
		return this;
	}

	public int getAction() {
		return action;
	}

	public UserActionPassbyContentBO setAction(int action) {
		this.action = action;
		return this;
	}
	
}
