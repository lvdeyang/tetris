package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 设置用户直播/点播资源权限BO
 * @author lxw
 *
 */
public class SetUserAuthBO {

	/**用户名列表*/
	private List<String> usernames;
	
	/**点播ID*/
	private String pid;
	
	/**直播ID*/
	private String cid;

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	
}
