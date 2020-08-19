package com.sumavision.tetris.bvc.business.group.secret;

import javax.persistence.Column;

import javax.persistence.Table;

import javax.persistence.Entity;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 会话内专向<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:39:27
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP_SECRETPO")
public class GroupSecretPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 发起专向的成员id */
	private Long inviteMemberId;
	
	/** 被动专向的成员id */
	private Long invitedMemberId;
	
	/** 隶属业务id */
	private Long groupId;

	@Column(name = "INVITE_MEMBER_ID")
	public Long getInviteMemberId() {
		return inviteMemberId;
	}

	public void setInviteMemberId(Long inviteMemberId) {
		this.inviteMemberId = inviteMemberId;
	}

	@Column(name = "INVITED_MEMBER_ID")
	public Long getInvitedMemberId() {
		return invitedMemberId;
	}

	public void setInvitedMemberId(Long invitedMemberId) {
		this.invitedMemberId = invitedMemberId;
	}

	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
}
