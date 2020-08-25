package com.sumavision.bvc.command.group.basic;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.EditStatus;
import com.sumavision.bvc.command.group.enumeration.GroupSpeakType;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 指挥组<br/>
 * @Description: 老版本： 普通、专向；会议<br/>
 * @author zsy 
 * @date 2019年9月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP")
public class CommandGroupPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 组名称 */
	private String name;
	
	/** 主题（标准里的，暂时按照一个指挥一个主题来做） */
	private String subject;
	
	/** 创建用户id */
	private Long userId;
	
	/** 创建用户名 */
	private String userName;
	
	/** 创建时间 */
	private Date createtime;
	
	/** 最后一次开始时间 */
	private Date startTime;
	
	/** 最后一次停止时间 */
	private Date endTime;
	
	/** 设置的作战时间 */
	private Date fightTime;
	
	/** 组类型 */
	private GroupType type;
	
	/** 会议中的模式，主席模式/讨论模式 */
	private GroupSpeakType speakType = GroupSpeakType.CHAIRMAN;
	
	/** 来源类型，本系统创建/外部系统创建 */
	private OriginType originType = OriginType.INNER;
	
	/** 组状态 */
	private GroupStatus status = GroupStatus.STOP;
	
	/** 编辑类型：正常（预设），临时，已删除 */
	private EditStatus editStatus = EditStatus.NORMAL;
	
	/** 参数方案 */
	private CommandGroupAvtplPO avtpl;
	
	/** 当前参数档位 */
	private GearsLevel currentGearLevel;
	
	/** 关联成员 */
	private List<CommandGroupMemberPO> members;
	
	/** 成员、议程配置、角色 通过各自PO中的groupId与group关联 */
	
	/** 关联转发 */
	private List<CommandGroupForwardPO> forwards;
	
	/** 关联转发点播 */
	private List<CommandGroupForwardDemandPO> forwardDemands;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "SUBJECT")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "FIGHT_TIME")
	public Date getFightTime() {
		return fightTime;
	}

	public void setFightTime(Date fightTime) {
		this.fightTime = fightTime;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SPEAK_TYPE")
	public GroupSpeakType getSpeakType() {
		return speakType;
	}

	public void setSpeakType(GroupSpeakType speakType) {
		this.speakType = speakType;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "ORIGIN_TYPE")
	public OriginType getOriginType() {
		return originType;
	}

	public void setOriginType(OriginType originType) {
		this.originType = originType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public GroupStatus getStatus() {
		return status;
	}

	public void setStatus(GroupStatus status) {
		this.status = status;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "EDIT_STATUS")
	public EditStatus getEditStatus() {
		return editStatus;
	}

	public void setEditStatus(EditStatus editStatus) {
		this.editStatus = editStatus;
	}

	@OneToOne(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public CommandGroupAvtplPO getAvtpl() {
		return avtpl;
	}

	public void setAvtpl(CommandGroupAvtplPO avtpl) {
		this.avtpl = avtpl;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "CURRENT_GEAR_LEVEL")
	public GearsLevel getCurrentGearLevel() {
		return currentGearLevel;
	}

	public void setCurrentGearLevel(GearsLevel currentGearLevel) {
		this.currentGearLevel = currentGearLevel;
	}
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupMemberPO> getMembers() {
		return members;
	}

	public void setMembers(List<CommandGroupMemberPO> members) {
		this.members = members;
	}
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupForwardPO> getForwards() {
		return forwards;
	}

	public void setForwards(List<CommandGroupForwardPO> forwards) {
		this.forwards = forwards;
	}
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupForwardDemandPO> getForwardDemands() {
		return forwardDemands;
	}

	public void setForwardDemands(List<CommandGroupForwardDemandPO> forwardDemands) {
		this.forwardDemands = forwardDemands;
	}
}
