package com.sumavision.tetris.bvc.business.po.info;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 接替指挥信息<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月26日 下午3:13:40
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP_COMMAND_REPLACE")
public class GroupCommandReplacePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** groupId，冗余，方便查找 */
	private Long groupId;
	
	/** 接替成员，接替其他人。根据大白本，该字段应该是操作人。根据测试大纲，使用此字段作为接替成员 */
	private Long opMemberId;
	
	private int opMemberLevel;
	
	private String opMemberCode;
	
	/** 接替指挥前的roleId，用于恢复 */
	private Long opMemberOriginRoleId;
	
	/** 被接替成员 */
	private Long targetMemberId;
	
	private int targetMemberLevel;
	
	private String targetMemberCode;
	
	/** 接替指挥前的roleId，用于恢复 */
	private Long targetMemberOriginRoleId;
	
	/** 关联GroupCommandInfoPO */
	private GroupCommandInfoPO commandInfo;

	public Long getTargetMemberId() {
		return targetMemberId;
	}

	public void setTargetMemberId(Long memberId) {
		this.targetMemberId = memberId;
	}

	
	public Long getOpMemberId() {
		return opMemberId;
	}

	public void setOpMemberId(Long opMemberId) {
		this.opMemberId = opMemberId;
	}

	public int getOpMemberLevel() {
		return opMemberLevel;
	}

	public void setOpMemberLevel(int opMemberLevel) {
		this.opMemberLevel = opMemberLevel;
	}

	public String getOpMemberCode() {
		return opMemberCode;
	}

	public void setOpMemberCode(String opMemberCode) {
		this.opMemberCode = opMemberCode;
	}

	public Long getOpMemberOriginRoleId() {
		return opMemberOriginRoleId;
	}

	public void setOpMemberOriginRoleId(Long opMemberOriginRoleId) {
		this.opMemberOriginRoleId = opMemberOriginRoleId;
	}

	public int getTargetMemberLevel() {
		return targetMemberLevel;
	}

	public void setTargetMemberLevel(int targetMemberLevel) {
		this.targetMemberLevel = targetMemberLevel;
	}

	public String getTargetMemberCode() {
		return targetMemberCode;
	}

	public void setTargetMemberCode(String targetMemberCode) {
		this.targetMemberCode = targetMemberCode;
	}

	public Long getTargetMemberOriginRoleId() {
		return targetMemberOriginRoleId;
	}

	public void setTargetMemberOriginRoleId(Long targetMemberOriginRoleId) {
		this.targetMemberOriginRoleId = targetMemberOriginRoleId;
	}

	@ManyToOne
	@JoinColumn(name = "COMMAND_INFO_ID")
	public GroupCommandInfoPO getCommandInfo() {
		return commandInfo;
	}

	public void setCommandInfo(GroupCommandInfoPO commandInfo) {
		this.commandInfo = commandInfo;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public GroupCommandReplacePO(){
		
	}
	
	public GroupCommandReplacePO(Long groupId){
		this.groupId = groupId;
	}
}
