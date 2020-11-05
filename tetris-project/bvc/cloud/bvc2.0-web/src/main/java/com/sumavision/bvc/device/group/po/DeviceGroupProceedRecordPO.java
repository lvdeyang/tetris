package com.sumavision.bvc.device.group.po;

import java.util.Date;

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
@Table(name="BVC_DEVICE_PROCEED_RECORD")
public class DeviceGroupProceedRecordPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	private Date startTime;
	
	private Date endTime;
	
	/** 该次记录的会议是否已经结束 */
	private Boolean finished = false;
	
	/** 成员总数（开会时） */
	private int totalMemberNumber;
	
	/** 在线成员总数（开会时） */
	private int onlineMemberNumber;
	
	/** 被授权看会的人数（开会时），用于作为“看会人数” */
	private int authorizationMemberNumber;
	
	/** 关联会议 */
	private Long groupId;
	
	/** 会议名称 */
	private String groupName;
	
	/** 建会人员id*/
	private Long userId;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public int getTotalMemberNumber() {
		return totalMemberNumber;
	}

	public void setTotalMemberNumber(int totalMemberNumber) {
		this.totalMemberNumber = totalMemberNumber;
	}

	public int getOnlineMemberNumber() {
		return onlineMemberNumber;
	}

	public void setOnlineMemberNumber(int onlineMemberNumber) {
		this.onlineMemberNumber = onlineMemberNumber;
	}

	public int getAuthorizationMemberNumber() {
		return authorizationMemberNumber;
	}

	public void setAuthorizationMemberNumber(int authorizationMemberNumber) {
		this.authorizationMemberNumber = authorizationMemberNumber;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public Long getUserId() {
		return userId;
	}

	public DeviceGroupProceedRecordPO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
}
