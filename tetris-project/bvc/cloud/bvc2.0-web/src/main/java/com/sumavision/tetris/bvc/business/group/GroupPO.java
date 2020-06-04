package com.sumavision.tetris.bvc.business.group;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 业务组（目前设想做指挥，会议以及内蒙会控和zoom会议，其他如监控，点播，呼叫是否需要其他数据结构这个不清楚）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:37:07
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP")
public class GroupPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 名称 */
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
	
	/** 业务类型 */
	private BusinessType businessType;
	
	/** 发流方式：单播或组播 */
	private TransmissionMode transmissionMode;
	
	/** 会议中的模式，主席模式/讨论模式 */
	private GroupSpeakType speakType = GroupSpeakType.CHAIRMAN;
	
	/** 来源类型，本系统创建/外部系统创建 */
	private OriginType originType = OriginType.INNER;
	
	/** 状态 */
	private GroupStatus status;
	
	/** 音视频参数模板id */
	private Long audioVideoTemplateId;

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SUBJECT")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIGHT_TIME")
	public Date getFightTime() {
		return fightTime;
	}

	public void setFightTime(Date fightTime) {
		this.fightTime = fightTime;
	}

	@Column(name = "BUSINESS_TYPE")
	@Enumerated(value = EnumType.STRING)
	public BusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TRANSMISSION_MODE")
	public TransmissionMode getTransmissionMode() {
		return transmissionMode;
	}

	public void setTransmissionMode(TransmissionMode transmissionMode) {
		this.transmissionMode = transmissionMode;
	}

	@Column(name = "STATUS")
	@Enumerated(value = EnumType.STRING)
	public GroupStatus getStatus() {
		return status;
	}

	public void setStatus(GroupStatus status) {
		this.status = status;
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

	@Column(name = "AUDIO_VIDEO_TEMPLATE_ID")
	public Long getAudioVideoTemplateId() {
		return audioVideoTemplateId;
	}

	public void setAudioVideoTemplateId(Long audioVideoTemplateId) {
		this.audioVideoTemplateId = audioVideoTemplateId;
	}
	
}
