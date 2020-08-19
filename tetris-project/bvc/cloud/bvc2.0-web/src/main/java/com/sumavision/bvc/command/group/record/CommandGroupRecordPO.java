package com.sumavision.bvc.command.group.record;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 指挥录制<br/>
 * @Description: 录制<br/>
 * @author zsy 
 * @date 2019年11月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_RECORD")
public class CommandGroupRecordPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 录制状态 */
	private boolean run = true;
	
	/** 分屏情况 */
	private PlayerSplitLayout playerSplitLayout;
	
	/** 创建用户id，操作录制的用户 */
	private Long recordUserId;
	
	/** 创建指挥的用户id */
	private Long groupUserId;
	
	/** 指挥id */
	private Long groupId;
	
	/** 指挥名称 */
	private String groupName;
	
	/** 开始时间 */
	private Date startTime;
	
	/** 停止时间 */
	private Date endTime;
	
	/** 关联片段 */
	private List<CommandGroupRecordFragmentPO> fragments;
	
	@Column(name = "RUN")
	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PLAYER_SPLIT_LAYOUT")
	public PlayerSplitLayout getPlayerSplitLayout() {
		return playerSplitLayout;
	}

	public void setPlayerSplitLayout(PlayerSplitLayout playerSplitLayout) {
		this.playerSplitLayout = playerSplitLayout;
	}

	@Column(name = "RECORD_USER_ID")
	public Long getRecordUserId() {
		return recordUserId;
	}

	public void setRecordUserId(Long recordUserId) {
		this.recordUserId = recordUserId;
	}

	@Column(name = "GROUP_USER_ID")
	public Long getGroupUserId() {
		return groupUserId;
	}

	public void setGroupUserId(Long groupUserId) {
		this.groupUserId = groupUserId;
	}

	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	
	@OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupRecordFragmentPO> getFragments() {
		return fragments;
	}

	public void setFragments(List<CommandGroupRecordFragmentPO> fragments) {
		this.fragments = fragments;
	}
	
	public CommandGroupRecordPO setGroupInfo(CommandGroupPO group){		
		this.groupUserId = group.getUserId();		
		this.groupId = group.getId();
		this.groupName = group.getName();
		return this;
	}
	
	public CommandGroupRecordPO setGroupInfo(GroupPO group){		
		this.groupUserId = group.getUserId();		
		this.groupId = group.getId();
		this.groupName = group.getName();
		return this;
	}
}
