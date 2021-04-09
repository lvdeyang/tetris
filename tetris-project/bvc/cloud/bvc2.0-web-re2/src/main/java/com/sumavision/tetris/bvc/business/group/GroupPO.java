package com.sumavision.tetris.bvc.business.group;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.sumavision.bvc.command.group.enumeration.EditStatus;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import antlr.Version;

/**
 * 业务组（指挥，会议以及内蒙会控和zoom会议）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:37:07
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP", uniqueConstraints = {@UniqueConstraint(columnNames="GROUP_NUMBER")})
public class GroupPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 查找预约会议的时间间隔*/
	public static final Long SCHEDULING_INTERVAL = 9000L;

	/** 名称 */
	private String name;
	
	/** 主题（标准里的） */
	private String subject;
	
	/** 创建人id */
	private Long userId;
	
	/** 创建人用户名 */
	private String userName;
	
	/** 创建人号码，级联使用 */
	private String userCode;
	
	/** 创建时间 */
	private Date createtime;
	
	/** 最后一次开始时间 */
	private Date startTime;
	
	/** 最后一次停止时间 */
	private Date endTime;
	
	/** 设置的作战时间 */
	private Date fightTime;
	
	/** 业务类型 */
	private BusinessType groupBusinessType;
	
	/** 发流方式：单播或组播 */
	private TransmissionMode transmissionMode = TransmissionMode.UNICAST;
	
	/** 会议中的模式，主席模式/讨论模式 */
//	private GroupSpeakType speakType = GroupSpeakType.CHAIRMAN;
	
	/** 来源类型，本系统创建/外部系统创建 */
	private OriginType originType = OriginType.INNER;
	
	/** 状态（不再包含提醒状态） */
	private GroupStatus status;
	
	/** 编辑类型：正常（预设），临时，已删除 */
	private EditStatus editStatus = EditStatus.NORMAL;
	
	/** 是否处于提醒状态 */
	private Boolean remind = false;
	
	/** 音视频参数模板id */
	private Long audioVideoTemplateId;
	
	/** 点播等业务，指定播放器位置 */
	private Integer locationIndex;
	
	/** 成员 */
	private List<BusinessGroupMemberPO> members;
	
	/** 参数方案 */
	private DeviceGroupAvtplPO avtpl;
	
	/** 预约会议类型*/
	private BusinessOrderGroupType orderGroupType;

	/** 预约开始时间*/
	private Date orderBeginTime;
	
	/** 预约结束时间*/
	private Date orderEndTime;
	
	/** 是否锁定会议*/
	private GroupLock groupLock;
	
	/** 会议号码*/
	private Long groupNumber;
	
	/** 当BusinessOrderGroupType有值时，字段有效。true需要执行预约会议，false已经执行过不再需要执行预约会议*/
	//该字段用来控制一个场景：当预约会议提前开启，并在预约开启之前结束。那么到预约时间由此字段判断是否需要再次开启会议。
	private Boolean executeGroup;
	
	/** 是否正在录制 */
	private Boolean isRecord = false;
	
	/** 是否正在发布直播流 */
	private Boolean isPublishment = false;
	
	/** 预约会议执行后数字+1*/
	private Integer version = 0;
	
	@Column(name = "version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(Integer locationIndex) {
		this.locationIndex = locationIndex;
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

	@Column(name = "USER_CODE")
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
		return groupBusinessType;
	}

	public void setBusinessType(BusinessType groupBusinessType) {
		this.groupBusinessType = groupBusinessType;
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

//	@Enumerated(value = EnumType.STRING)
//	@Column(name = "SPEAK_TYPE")
//	public GroupSpeakType getSpeakType() {
//		return speakType;
//	}
//
//	public void setSpeakType(GroupSpeakType speakType) {
//		this.speakType = speakType;
//	}

	@Column(name = "REMIND")
	public Boolean getRemind() {
		return remind;
	}

	public void setRemind(Boolean remind) {
		this.remind = remind;
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

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberPO> getMembers() {
		return members;
	}

	public void setMembers(List<BusinessGroupMemberPO> members) {
		this.members = members;
	}

	@OneToOne(mappedBy = "businessGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public DeviceGroupAvtplPO getAvtpl() {
		return avtpl;
	}

	public void setAvtpl(DeviceGroupAvtplPO avtpl) {
		this.avtpl = avtpl;
	}

	@Column(name = "ORDER_GROUP_TYPE")
	@Enumerated(value = EnumType.STRING)
	public BusinessOrderGroupType getOrderGroupType() {
		return orderGroupType;
	}

	public GroupPO setOrderGroupType(BusinessOrderGroupType orderGroupType) {
		this.orderGroupType = orderGroupType;
		return this;
	}

	@Column(name = "ORDER_BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getOrderBeginTime() {
		return orderBeginTime;
	}

	public GroupPO setOrderBeginTime(Date orderBeginTime) {
		this.orderBeginTime = orderBeginTime;
		return this;
	}

	@Column(name = "ORDER_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getOrderEndTime() {
		return orderEndTime;
	}

	public GroupPO setOrderEndTime(Date orderEndTime) {
		this.orderEndTime = orderEndTime;
		return this;
	}

	@Column(name = "GROUP_NUMBER")
	public Long getGroupNumber() {
		return groupNumber;
	}

	public GroupPO setGroupNumber(Long groupNumber) {
		this.groupNumber = groupNumber;
		return this;
	}

	@Column(name = "GROUP_LOCK")
	@Enumerated(value = EnumType.STRING)
	public GroupLock getGroupLock() {
		return groupLock;
	}

	public GroupPO setGroupLock(GroupLock groupLock) {
		this.groupLock = groupLock;
		return this;
	}

	@Column(name = "EXECUTE_GROUP")
	public Boolean getExecuteGroup() {
		return executeGroup;
	}

	public GroupPO setExecuteGroup(Boolean executeGroup) {
		this.executeGroup = executeGroup;
		return this;
	}

	@Column(name = "IS_RECORD")
	public Boolean getIsRecord() {
		return isRecord;
	}

	public GroupPO setIsRecord(Boolean isRecord) {
		this.isRecord = isRecord;
		return this;
	}

	@Column(name = "IS_PUBLISHMENT")
	public Boolean getIsPublishment() {
		return isPublishment;
	}

	public GroupPO setIsPublishment(Boolean isPublishment) {
		this.isPublishment = isPublishment;
		return this;
	}

	@Column(name = "EDIT_STATUS")
	@Enumerated(EnumType.STRING)
	public EditStatus getEditStatus() {
		return editStatus;
	}

	public GroupPO setEditStatus(EditStatus editStatus) {
		this.editStatus = editStatus;
		return this;
	}
	
}
