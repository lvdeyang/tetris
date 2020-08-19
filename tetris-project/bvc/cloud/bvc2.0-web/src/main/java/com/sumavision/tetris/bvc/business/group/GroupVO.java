package com.sumavision.tetris.bvc.business.group;

import java.util.Date;

import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallVO;
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

	
	/** 来源类型，本系统创建/外部系统创建 */
	private OriginType originType = OriginType.INNER;
	
	/** 状态 */
	private GroupStatus status;
	
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

	public Date getCreatetime() {
		return createtime;
	}

	public GroupVO setCreatetime(Date createtime) {
		this.createtime = createtime;
		return this;
	}

	public Date getStartTime() {
		return startTime;
	}

	public GroupVO setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	public Date getEndTime() {
		return endTime;
	}

	public GroupVO setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public Date getFightTime() {
		return fightTime;
	}

	public GroupVO setFightTime(Date fightTime) {
		this.fightTime = fightTime;
		return this;
	}

	public BusinessType getGroupBusinessType() {
		return groupBusinessType;
	}

	public GroupVO setGroupBusinessType(BusinessType groupBusinessType) {
		this.groupBusinessType = groupBusinessType;
		return this;
	}

	public TransmissionMode getTransmissionMode() {
		return transmissionMode;
	}

	public GroupVO setTransmissionMode(TransmissionMode transmissionMode) {
		this.transmissionMode = transmissionMode;
		return this;
	}

	public OriginType getOriginType() {
		return originType;
	}

	public GroupVO setOriginType(OriginType originType) {
		this.originType = originType;
		return this;
	}

	public GroupStatus getStatus() {
		return status;
	}

	public GroupVO setStatus(GroupStatus status) {
		this.status = status;
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
		// TODO Auto-generated method stub
		this.setName(entity.getName())
		    .setSubject(entity.getSubject())
		    .setUserId(entity.getUserId())
		    .setUserName(entity.getUserName())
		    .setCreatetime(entity.getCreatetime())
		    .setStartTime(entity.getStartTime())
		    .setEndTime(entity.getEndTime())
		    .setFightTime(entity.getFightTime())
		    .setGroupBusinessType(entity.getBusinessType())
		    .setTransmissionMode(entity.getTransmissionMode())
		    .setOriginType(entity.getOriginType())
		    .setStatus(entity.getStatus())
		    .setAudioVideoTemplateId(entity.getAudioVideoTemplateId());
		
		return this;
	}

}
