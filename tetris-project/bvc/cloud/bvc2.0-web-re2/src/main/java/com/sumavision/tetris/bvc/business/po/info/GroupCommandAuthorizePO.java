package com.sumavision.tetris.bvc.business.po.info;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 授权指挥信息<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月26日 下午3:13:40
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP_COMMAND_AUTHORIZE")
public class GroupCommandAuthorizePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** groupId，冗余，方便查找 */
	private Long groupId;
	
	/** 授权接收成员，被授权后具有其他人权限 */
	private Long acceptMemberId;
	
	private int acceptMemberLevel;
	
	private String acceptMemberCode;
	
	private Long acceptMemberOriginRoleId;
	
	/** 被指挥成员。按测试大纲，该字段只能是主席，由“授权接收成员”拥有主席的权限。按大白本，该字段可以是其它人，意义不明 */
	private Long cmdMemberId;
	
	private int cmdMemberLevel;
	
	private String cmdMemberCode;
	
	/** 关联GroupCommandInfoPO */
	private GroupCommandInfoPO commandInfo;
	
	public Long getAcceptMemberId() {
		return acceptMemberId;
	}

	public void setAcceptMemberId(Long acceptMemberId) {
		this.acceptMemberId = acceptMemberId;
	}

	public int getAcceptMemberLevel() {
		return acceptMemberLevel;
	}

	public void setAcceptMemberLevel(int acceptMemberLevel) {
		this.acceptMemberLevel = acceptMemberLevel;
	}

	public String getAcceptMemberCode() {
		return acceptMemberCode;
	}

	public void setAcceptMemberCode(String acceptMemberCode) {
		this.acceptMemberCode = acceptMemberCode;
	}

	public Long getAcceptMemberOriginRoleId() {
		return acceptMemberOriginRoleId;
	}

	public void setAcceptMemberOriginRoleId(Long acceptMemberOriginRoleId) {
		this.acceptMemberOriginRoleId = acceptMemberOriginRoleId;
	}

	public Long getCmdMemberId() {
		return cmdMemberId;
	}

	public void setCmdMemberId(Long cmdMemberId) {
		this.cmdMemberId = cmdMemberId;
	}

	public int getCmdMemberLevel() {
		return cmdMemberLevel;
	}

	public void setCmdMemberLevel(int cmdMemberLevel) {
		this.cmdMemberLevel = cmdMemberLevel;
	}

	public String getCmdMemberCode() {
		return cmdMemberCode;
	}

	public void setCmdMemberCode(String cmdMemberCode) {
		this.cmdMemberCode = cmdMemberCode;
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

	public GroupCommandAuthorizePO(){
		
	}
	
	public GroupCommandAuthorizePO(Long groupId){
		this.groupId = groupId;
	}
}
