package com.sumavision.bvc.device.group.po;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.type.TrueFalseType;

import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组中的录制 
 * @author lvdeyang
 * @date 2018年8月8日 下午4:52:53 
 */
@Entity
@Table(name="BVC_GROUP_RECORD_INFO")
public class GroupRecordInfoPO  extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 录制状态 */
	private boolean run;
	
	/** 创建用户id，操作录制的用户 */
	private Long recordUserId;
	
	/** 创建会议的用户id */
	private Long groupUserId;
	
	/** 会议id */
	private Long groupId;
	
	/** 会议名称 */
	private String groupName;
	
	/** 开始时间 */
	private Date startTime;
	
	/** 停止时间 */
	private Date endTime;
	
	/** 关联片段 */
	private List<GroupRecordPO> records;

	@Column(name = "RUN")
	public boolean isRun() {
		return run;
	}

	public GroupRecordInfoPO setRun(boolean run) {
		this.run = run;
		return this;
	}

	@Column(name = "RECORD_USERID")
	public Long getRecordUserId() {
		return recordUserId;
	}

	public GroupRecordInfoPO setRecordUserId(Long recordUserId) {
		this.recordUserId = recordUserId;
		return this;
	}

	@Column(name = "GROUP_USERID")
	public Long getGroupUserId() {
		return groupUserId;
	}

	public GroupRecordInfoPO setGroupUserId(Long groupUserId) {
		this.groupUserId = groupUserId;
		return this;
	}

	@Column(name = "GROUPID")
	public Long getGroupId() {
		return groupId;
	}

	public GroupRecordInfoPO setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}

	public GroupRecordInfoPO setGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public GroupRecordInfoPO setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public GroupRecordInfoPO setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	@OneToMany(mappedBy = "recordInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GroupRecordPO> getRecords() {
		return records;
	}

	public GroupRecordInfoPO setRecords(List<GroupRecordPO> records) {
		this.records = records;
		return this;
	}
	
	public GroupRecordInfoPO setGroupInfo(GroupPO group){		
		this.groupUserId = group.getUserId();		
		this.groupId = group.getId();
		this.groupName = group.getName();
		return this;
	}

}
