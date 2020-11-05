package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class GroupVO extends AbstractBaseVO<GroupVO, GroupPO> {

	/** 名称 */
	private String name;
	
	/** 主题（标准里的） */
	private String subject;
	
	/** 创建人id */
	private Long userId;
	
	/** 创建人用户名 */
	private String userName;
	
	/** 创建时间 */
	private String createtime;
	
	/** 最后一次开始时间 */
	private String startTime;
	
	/** 最后一次停止时间 */
	private String endTime;
	
	/** 设置的作战时间 */
	private String fightTime;
	
	/** 业务类型 */
	private String groupBusinessType;
	
	private String groupBusinessTypeName;
	
	/** 发流方式：单播或组播 */
	private String transmissionMode;
	
	private String transmissionModeName;
	
	/** 会议中的模式，主席模式/讨论模式 */
	
	/** 来源类型，本系统创建/外部系统创建 */
	private String originType;
	
	private String originTypeName;
	
	/** 状态 */
	private String status;
	
	private String statusName;
	
	/** 音视频参数模板id */
	private Long audioVideoTemplateId;
	
	
	public String getName() {
		return name;
	}

	public GroupVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public GroupVO setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public GroupVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public GroupVO setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getCreatetime() {
		return createtime;
	}

	public GroupVO setCreatetime(String createtime) {
		this.createtime = createtime;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public GroupVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public GroupVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getFightTime() {
		return fightTime;
	}

	public GroupVO setFightTime(String fightTime) {
		this.fightTime = fightTime;
		return this;
	}

	public String getGroupBusinessType() {
		return groupBusinessType;
	}

	public GroupVO setGroupBusinessType(String groupBusinessType) {
		this.groupBusinessType = groupBusinessType;
		return this;
	}

	public String getGroupBusinessTypeName() {
		return groupBusinessTypeName;
	}

	public GroupVO setGroupBusinessTypeName(String groupBusinessTypeName) {
		this.groupBusinessTypeName = groupBusinessTypeName;
		return this;
	}

	public String getTransmissionMode() {
		return transmissionMode;
	}

	public GroupVO setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
		return this;
	}
	
	public String getTransmissionModeName() {
		return transmissionModeName;
	}

	public GroupVO setTransmissionModeName(String transmissionModeName) {
		this.transmissionModeName = transmissionModeName;
		return this;
	}

	public String getOriginType() {
		return originType;
	}

	public GroupVO setOriginType(String originType) {
		this.originType = originType;
		return this;
	}
	
	public String getOriginTypeName() {
		return originTypeName;
	}

	public GroupVO setOriginTypeName(String originTypeName) {
		this.originTypeName = originTypeName;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public GroupVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getStatusName() {
		return statusName;
	}

	public GroupVO setStatusName(String statusName) {
		this.statusName = statusName;
		return this;
	}

	public Long getAudioVideoTemplateId() {
		return audioVideoTemplateId;
	}

	public GroupVO setAudioVideoTemplateId(Long audioVideoTemplateId) {
		this.audioVideoTemplateId = audioVideoTemplateId;
		return this;
	}
	
	@Override
	public GroupVO set(GroupPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
		    .setSubject(entity.getSubject())
		    .setUserId(entity.getUserId())
		    .setUserName(entity.getUserName())
		    .setCreatetime(entity.getCreatetime()==null?"":DateUtil.format(entity.getCreatetime(), DateUtil.dateTimePattern))
		    .setStartTime(entity.getStartTime()==null?"":DateUtil.format(entity.getStartTime(), DateUtil.dateTimePattern))
		    .setEndTime(entity.getEndTime()==null?"":DateUtil.format(entity.getEndTime(), DateUtil.dateTimePattern))
		    .setFightTime(entity.getFightTime()==null?"":DateUtil.format(entity.getFightTime(), DateUtil.dateTimePattern))
		    .setGroupBusinessType(entity.getBusinessType().toString())
		    .setGroupBusinessTypeName(entity.getBusinessType().getName())
		    .setTransmissionMode(entity.getTransmissionMode().toString())
		    .setTransmissionModeName(entity.getTransmissionMode().getName())
		    .setOriginType(entity.getOriginType().toString())
		    .setOriginTypeName(entity.getOriginType().getName())
		    .setStatus(entity.getStatus().toString())
		    .setStatusName(entity.getStatus().getName())
		    .setAudioVideoTemplateId(entity.getAudioVideoTemplateId());
		return this;
	}

}
