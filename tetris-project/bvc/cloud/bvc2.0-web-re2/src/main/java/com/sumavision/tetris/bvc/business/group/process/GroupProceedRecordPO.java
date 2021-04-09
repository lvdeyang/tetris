package com.sumavision.tetris.bvc.business.group.process;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 开会情况记录<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月19日 上午10:20:19
 */
@Entity
@Table(name="BVC_GROUP_PROCEED_RECORD")
public class GroupProceedRecordPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	private Date startTime;
	
	private Date endTime;
	
	/** 该次记录的会议是否已经结束 */
	private Boolean finished = false;
	
	/** 成员总数（开会时） */
	private int totalMemberNumber;
	
	/** 在线成员总数（开会时） */
	private int onlineMemberNumber;
	
	/** 关联会议 */
	private Long groupId;
	
	/** 会议名称 */
	private String groupName;
	
	/** 建会人员id*/
	private Long userId;

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

	@Column(name = "FINISHED")
	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	@Column(name = "TOTAL_MEMBER_NUMBER")
	public int getTotalMemberNumber() {
		return totalMemberNumber;
	}

	public void setTotalMemberNumber(int totalMemberNumber) {
		this.totalMemberNumber = totalMemberNumber;
	}

	@Column(name = "ONLINE_MEMBER_NUMBER")
	public int getOnlineMemberNumber() {
		return onlineMemberNumber;
	}

	public void setOnlineMemberNumber(int onlineMemberNumber) {
		this.onlineMemberNumber = onlineMemberNumber;
	}

	@Column(name = "GROUPID")
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
	
	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}

	public GroupProceedRecordPO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
}
