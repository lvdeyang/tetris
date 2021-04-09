package com.sumavision.tetris.bvc.business.po.info;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 协同指挥成员信息<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月26日 下午3:13:40
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP_COMMAND_COOPERATE")
public class GroupCommandCooperatePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** groupId，冗余，方便查找 */
	private Long groupId;
		
	private Long memberId;
	
	private int memberLevel;
	
	private String memberCode;
	
	/** 关联GroupCommandInfoPO */
	private GroupCommandInfoPO commandInfo;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public int getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(int memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
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

	public GroupCommandCooperatePO(){
		
	}
	
	public GroupCommandCooperatePO(Long groupId){
		this.groupId = groupId;
	}
}
