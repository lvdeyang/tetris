package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;

/**
 * @ClassName: 透传信息内容 
 * @author wjw
 * @date 2018年10月11日 上午9:01:07
 */
public class PassByContentBO implements BasePassByContent{

	/** 1会议，3多人通话 */
	private String type = "";
	
	/** type为多人通话时，表示通话类型：video/audio */
	private String mode;
	
	/** xxx会议 */
	private String caller_name = "";
	
	/** 通知的唯一标识 */
	private String uuid = "";
	
	/** 业务唯一标识 */
	private String groupId = "";
	
	/** 业务唯一标识 */
	private String groupUuid = "";
	
	/** 是否可以拒绝 */
	private boolean deniable = false;
	
	/** userId */
	private String userId;
	
	/** 成员变更透传用 */
	private List<BundleBO> bundles;

	public String getType() {
		return type;
	}

	public PassByContentBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public PassByContentBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getCaller_name() {
		return caller_name;
	}

	public PassByContentBO setCaller_name(String caller_name) {
		this.caller_name = caller_name;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public PassByContentBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public PassByContentBO setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public PassByContentBO setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
		return this;
	}

	public boolean isDeniable() {
		return deniable;
	}

	public PassByContentBO setDeniable(boolean deniable) {
		this.deniable = deniable;
		return this;
	}
	
	public String getUserId() {
		return userId;
	}

	public PassByContentBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public List<BundleBO> getBundles() {
		return bundles;
	}

	public PassByContentBO setBundles(List<BundleBO> bundles) {
		this.bundles = bundles;
		return this;
	}

	/**
	 * @Title: 透传InComingCall 
	 * @param group 设备组信息
	 * @param  member 设备成员信息
	 * @return PassByContentBO
	 */
	public PassByContentBO setIncomingCall(DeviceGroupPO group, DeviceGroupMemberPO member){
		this.setType(group.getType().getProtocalId())
			.setCaller_name(group.getName())
			.setUuid(member.getUuid())
			.setGroupUuid(group.getUuid());
		
		if(member.getUserId() != null){
			this.setUserId(member.getUserId().toString());
		}
		
		return this;
	}
	
//614
	public PassByContentBO setIncomingCall(GroupPO group){
		this.setType("1")
			.setCaller_name(group.getName())
			.setUuid(group.getUuid())
			.setGroupUuid(group.getUuid())
			.setGroupId(group.getId().toString())
		 	.setUserId(group.getUserId().toString());
		
		return this;
	}
	/**
	 * @Title: 透传HangUp
	 * @param group 设备组信息
	 * @param member 设备成员信息
	 * @return PassByContentBO
	 */
	public PassByContentBO setHangUp(DeviceGroupPO group, DeviceGroupMemberPO member){
		this.setType(group.getType().getProtocalId())
		   .setCaller_name(group.getName())
		   .setGroupUuid(group.getUuid());
		
		return this;
	}

//614
	public PassByContentBO setHangUp(GroupPO group){
		this.setType("1")
		   .setCaller_name(group.getName())
		   .setGroupUuid(group.getUuid());
		
		return this;
	}
	
	/**
	 * @Title: 成员变更MemberUpdate 
	 * @param group
	 * @param members
	 * @return PassByContentBO 
	 */
	public PassByContentBO setMemberUpdate(DeviceGroupPO group, Collection<DeviceGroupMemberPO> members){
		this.setGroupUuid(group.getUuid())
			.setBundles(new ArrayList<BundleBO>());
		
		for(DeviceGroupMemberPO member: members){
			BundleBO bundle = new BundleBO().setBundleId(member.getBundleId())
											.setName(member.getBundleName());
			
			this.getBundles().add(bundle);
		}
		
		return this;
	}
	
	/**
	 * @Title: 管理员变更AdministratorUpdate 
	 * @param group
	 * @return PassByContentBO
	 * @throws
	 */
	public PassByContentBO setAdministratorUpdate(DeviceGroupPO group){
		this.setGroupUuid(group.getUuid());
		
		return this;
	}
}
