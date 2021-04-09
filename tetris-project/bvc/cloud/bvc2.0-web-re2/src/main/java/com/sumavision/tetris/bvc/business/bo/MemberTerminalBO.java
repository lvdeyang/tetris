package com.sumavision.tetris.bvc.business.bo;

import java.util.List;

import com.sumavision.tetris.bvc.business.group.GroupMemberType;

/**
 * 成员及其对应的终端类型<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月9日 上午10:43:58
 */
public class MemberTerminalBO {
	
	/** 成员类型：用户/虚拟设备*/
	private GroupMemberType groupMemberType;
	
	/** 用户id/hallId */
	private String originId;
	
	/** 用户对应的终端类型 */
	private Long terminalId;
	
	/** 当是会场的时候，标识该会场是否是单个设备包装为会场,true的时候才是从设备包装为会场*/
	private Boolean fromDevice;

	public GroupMemberType getGroupMemberType() {
		return groupMemberType;
	}

	public MemberTerminalBO setGroupMemberType(GroupMemberType groupMemberType) {
		this.groupMemberType = groupMemberType;
		return this;
	}

	public String getOriginId() {
		return originId;
	}

	public MemberTerminalBO setOriginId(String originId) {
		this.originId = originId;
		return this;
	}

	public MemberTerminalBO setUserId(String originId) {
		this.originId = originId;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public MemberTerminalBO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public Boolean getFromDevice() {
		return fromDevice;
	}

	public MemberTerminalBO setFromDevice(Boolean fromDevice) {
		this.fromDevice = fromDevice;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		String str = groupMemberType + originId + terminalId;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberTerminalBO bo = (MemberTerminalBO) obj;
		if(groupMemberType!=null && groupMemberType.equals(bo.getGroupMemberType())
				&& originId!=null && originId.equals(bo.getOriginId())){
//				&& terminalId!=null && terminalId.equals(bo.getTerminalId())){
			return true;
		}
		return false;
	}
	
}
