package com.sumavision.bvc.device.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_DEVICE_GROUP_AUTHORIZATION_MEMBER")
public class DeviceGroupAuthorizationMemberPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 执行层id, 对应资源层nodeUid*/
	private String layerId;
	
	/** 设备id */
	private String bundleId;

	/** 用户名称，使用较少*/
	private String userName;
	
	/** 用户id，使用较少*/
	private String userId;
	
	/** 关联设备组 */
	private DeviceGroupAuthorizationPO authorization;
		
	public DeviceGroupAuthorizationMemberPO(){
		
	}
	
	/**
	 * 构造函数，从bundlePO得到权限成员（无法获取userId）
	 * @param bundlePO
	 */
	public DeviceGroupAuthorizationMemberPO(BundlePO bundlePO){
		this.layerId = bundlePO.getAccessNodeUid();
		this.bundleId = bundlePO.getBundleId();
		this.userName = (bundlePO.getUsername());
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@ManyToOne
	@JoinColumn(name = "AUTHORIZATION_ID")
	public DeviceGroupAuthorizationPO getAuthorization() {
		return authorization;
	}

	public void setAuthorization(DeviceGroupAuthorizationPO authorization) {
		this.authorization = authorization;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
}
