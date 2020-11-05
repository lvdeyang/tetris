package com.sumavision.tetris.bvc.business.bo;

import java.util.List;

/**
 * 描述一个成员的角色增减<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月9日 上午10:43:58
 */
public class ModifyMemberRoleBO {
	
	/** 成员id */
	private Long memberId;
	
	/** 绑定角色id */
	private List<Long> addRoleIds;
	
	/** 解绑角色id */
	private List<Long> removeRoleIds;

	public Long getMemberId() {
		return memberId;
	}

	public ModifyMemberRoleBO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public List<Long> getAddRoleIds() {
		return addRoleIds;
	}

	public ModifyMemberRoleBO setAddRoleIds(List<Long> addRoleIds) {
		this.addRoleIds = addRoleIds;
		return this;
	}

	public List<Long> getRemoveRoleIds() {
		return removeRoleIds;
	}

	public ModifyMemberRoleBO setRemoveRoleIds(List<Long> removeRoleIds) {
		this.removeRoleIds = removeRoleIds;
		return this;
	}
	
}
