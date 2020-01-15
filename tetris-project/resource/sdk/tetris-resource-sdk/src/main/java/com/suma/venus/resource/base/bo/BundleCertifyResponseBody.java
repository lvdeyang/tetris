package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 用户特征bundle和设备特征bundle认证过程返回的结果消息体
 * @author lxw
 *
 */
public class BundleCertifyResponseBody {
	
	/**认证结果*/
	private String result;
	
	/**错误信息*/
	private String error_message;

	/**bundleId*/
	private String bundle_id;
	
	/**账号名*/
	private String username;
	
	/**账号名对应的虚拟用户ID*/
	private Long userId;
	
	private List<BusinessLayerBody> business_layers;
	
	/**bundle附加属性**/
	private String bundle_extra_info;
	
	/**用户附加属性**/
	private String user_extra_info;
	
	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public List<BusinessLayerBody> getBusiness_layers() {
		return business_layers;
	}

	public void setBusiness_layers(List<BusinessLayerBody> business_layers) {
		this.business_layers = business_layers;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBundle_extra_info() {
		return bundle_extra_info;
	}

	public void setBundle_extra_info(String bundle_extra_info) {
		this.bundle_extra_info = bundle_extra_info;
	}

	public String getUser_extra_info() {
		return user_extra_info;
	}

	public void setUser_extra_info(String user_extra_info) {
		this.user_extra_info = user_extra_info;
	}
	
}
