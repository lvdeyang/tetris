package com.sumavision.bvc.device.group.po;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 授权看会的观众
 * @author zsy
 * @date 2018年12月23日 上午10:20:11 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_AUTHORIZATION")
public class DeviceGroupAuthorizationPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 授权方案名称 */
	private String name;
	
	/** 授权成员 */
	private Set<DeviceGroupAuthorizationMemberPO> authorizationMembers = new HashSet<DeviceGroupAuthorizationMemberPO>();
	
	/** 关联直播 */
	private Set<RecordLiveChannelPO> liveChannels = new HashSet<RecordLiveChannelPO>();
	
	/** 关联点播 */
	private Set<RecordAssetPO> assets = new HashSet<RecordAssetPO>();
	
	/** 关联会议 */
	private String groupUuid;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "authorization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<DeviceGroupAuthorizationMemberPO> getAuthorizationMembers() {
		return authorizationMembers;
	}

	public void setAuthorizationMembers(Set<DeviceGroupAuthorizationMemberPO> authorizationMembers) {
		this.authorizationMembers = authorizationMembers;
	}

	@OneToMany(mappedBy = "authorization", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<RecordLiveChannelPO> getLiveChannels() {
		return liveChannels;
	}

	public void setLiveChannels(Set<RecordLiveChannelPO> liveChannels) {
		this.liveChannels = liveChannels;
	}

	@OneToMany(mappedBy = "authorization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<RecordAssetPO> getAssets() {
		return assets;
	}

	public void setAssets(Set<RecordAssetPO> assets) {
		this.assets = assets;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}		
}
