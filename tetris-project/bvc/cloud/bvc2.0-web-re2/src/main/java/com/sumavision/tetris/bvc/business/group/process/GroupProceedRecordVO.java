package com.sumavision.tetris.bvc.business.group.process;

import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.tetris.commons.util.date.DateUtil;

public class GroupProceedRecordVO{

	private String startTime;
	
	private String endTime;
	
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
	
	/** 创建会议人员id*/
	private Long userId;
	
	public String getStartTime() {
		return startTime;
	}

	public GroupProceedRecordVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public GroupProceedRecordVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public Boolean getFinished() {
		return finished;
	}

	public GroupProceedRecordVO setFinished(Boolean finished) {
		this.finished = finished;
		return this;
	}

	public int getTotalMemberNumber() {
		return totalMemberNumber;
	}

	public GroupProceedRecordVO setTotalMemberNumber(int totalMemberNumber) {
		this.totalMemberNumber = totalMemberNumber;
		return this;
	}

	public int getOnlineMemberNumber() {
		return onlineMemberNumber;
	}

	public GroupProceedRecordVO setOnlineMemberNumber(int onlineMemberNumber) {
		this.onlineMemberNumber = onlineMemberNumber;
		return this;
	}

	public Long getGroupId() {
		return groupId;
	}

	public GroupProceedRecordVO setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getGroupName() {
		return groupName;
	}

	public GroupProceedRecordVO setGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}
	
	public Long getUserId() {
		return userId;
	}

	public GroupProceedRecordVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public GroupProceedRecordVO set(GroupProceedRecordPO entity) throws Exception {
		this.setStartTime(entity.getStartTime()==null?"":DateUtil.format(entity.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(entity.getEndTime()==null?"":DateUtil.format(entity.getEndTime(), DateUtil.dateTimePattern))
			.setFinished(entity.getFinished())
			.setTotalMemberNumber(entity.getTotalMemberNumber())
			.setOnlineMemberNumber(entity.getOnlineMemberNumber())
			.setGroupId(entity.getGroupId())
			.setGroupName(entity.getGroupName())
			.setUserId(entity.getUserId());
		return this;
	}
	
}
