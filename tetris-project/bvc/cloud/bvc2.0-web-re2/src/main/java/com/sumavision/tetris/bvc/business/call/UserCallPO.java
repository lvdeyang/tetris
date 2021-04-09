package com.sumavision.tetris.bvc.business.call;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sumavision.bvc.command.group.enumeration.CallStatus;
import com.sumavision.bvc.command.group.enumeration.CallType;
import com.sumavision.bvc.command.group.enumeration.UserCallType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_USER_CALL", uniqueConstraints={@UniqueConstraint(columnNames = {"CALLED_USER_ID", "CALL_USER_ID"})})
public class UserCallPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 如果是从点播转呼叫而来，则记录点播VodPO的id */
	private Long formerVodId;
	
	/** 呼叫websocket消息id */
	private Long messageId;
	
	private CallType callType = CallType.LOCAL_LOCAL;
	
	/** 音视频/语音 */
	private UserCallType type;
	
	/** 通话状态 */
	private CallStatus status = CallStatus.WAITING_FOR_RESPONSE;
	
	/************
	 ***主叫用户***
	 ************/
	
	/** 主叫用户id */
	private Long callUserId;
	
	/** 主叫用户名 */
	private String callUsername;
	
	/** 主叫用户号码 */
	private String callUserNo;
	
	/************
	 ***被叫用户***
	 ************/
	
	/** 被叫用户id */
	private Long calledUserId;
	
	/** 被叫用户名 */
	private String calledUsername;
	
	/** 被叫用户号码 */
	private String calledUserNo;
	
	/** 隶属业务id */
	private Long groupId;
	
	@Column(name = "FORMER_VOD_ID")
	public Long getFormerVodId() {
		return formerVodId;
	}

	public void setFormerVodId(Long formerVodId) {
		this.formerVodId = formerVodId;
	}

	@Column(name = "MESSAGE_ID")
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "CALL_TYPE")
	public CallType getCallType() {
		return callType;
	}

	public void setCallType(CallType callType) {
		this.callType = callType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "type")
	public UserCallType getType() {
		return type;
	}

	public void setType(UserCallType type) {
		this.type = type;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	public CallStatus getStatus() {
		return status;
	}

	public void setStatus(CallStatus status) {
		this.status = status;
	}

	@Column(name = "CALL_USER_ID")
	public Long getCallUserId() {
		return callUserId;
	}

	public void setCallUserId(Long callUserId) {
		this.callUserId = callUserId;
	}

	@Column(name = "CALLED_USER_ID")
	public Long getCalledUserId() {
		return calledUserId;
	}

	public void setCalledUserId(Long calledUserId) {
		this.calledUserId = calledUserId;
	}

	@Column(name = "CALLED_USER_NAME")
	public String getCalledUsername() {
		return calledUsername;
	}

	public void setCalledUsername(String calledUsername) {
		this.calledUsername = calledUsername;
	}

	public String getCalledUserNo() {
		return calledUserNo;
	}

	public void setCalledUserNo(String calledUserNo) {
		this.calledUserNo = calledUserNo;
	}

	@Column(name = "CALL_USER_NAME")
	public String getCallUsername() {
		return callUsername;
	}

	public void setCallUsername(String callUsername) {
		this.callUsername = callUsername;
	}
	
	public String getCallUserNo() {
		return callUserNo;
	}

	public void setCallUserNo(String callUserNo) {
		this.callUserNo = callUserNo;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public UserCallPO(){}
	
	/*public UserCallPO(
			Long calledUserId,
			String calledUserno,
			String calledUsername,
			String calledEncoderBundleId,
			String calledEncoderBundleName,
			String calledEncoderBundleType,
			String calledEncoderLayerId,
			String calledEncoderVideoChannelId,
			String calledEncoderVideoBaseType,
			String calledEncoderAudioChannelId,
			String calledEncoderAudioBaseType,
			String calledDecoderBundleId,
			String calledDecoderBundleName,
			String calledDecoderBundleType,
			String calledDecoderLayerId,
			String calledDecoderVideoChannelId,
			String calledDecoderVideoBaseType,
			String calledDecoderAudioChannelId,
			String calledDecoderAudioBaseType,
			Long callUserId,
			String callUserno,
			String callUsername,
			String callEncoderBundleId,
			String callEncoderBundleName,
			String callEncoderBundleType,
			String callEncoderLayerId,
			String callEncoderVideoChannelId,
			String callEncoderVideoBaseType,
			String callEncoderAudioChannelId,
			String callEncoderAudioBaseType,
			String callDecoderBundleId,
			String callDecoderBundleName,
			String callDecoderBundleType,
			String callDecoderLayerId,
			String callDecoderVideoChannelId,
			String callDecoderVideoBaseType,
			String callDecoderAudioChannelId,
			String callDecoderAudioBaseType) throws Exception{
		this.setUpdateTime(new Date());
		this.calledUserId = calledUserId;
		this.calledUserno = calledUserno;
		this.calledUsername = calledUsername;
		this.calledEncoderBundleId = calledEncoderBundleId;
		this.calledEncoderBundleName = calledEncoderBundleName;
		this.calledEncoderBundleType = calledEncoderBundleType;
		this.calledEncoderLayerId = calledEncoderLayerId;
		this.calledEncoderVideoChannelId = calledEncoderVideoChannelId;
		this.calledEncoderVideoBaseType = calledEncoderVideoBaseType;
		this.calledEncoderAudioChannelId = calledEncoderAudioChannelId;
		this.calledEncoderAudioBaseType = calledEncoderAudioBaseType;
		this.calledDecoderBundleId = calledDecoderBundleId;
		this.calledDecoderBundleName = calledDecoderBundleName;
		this.calledDecoderBundleType = calledDecoderBundleType;
		this.calledDecoderLayerId = calledDecoderLayerId;
		this.calledDecoderVideoChannelId = calledDecoderVideoChannelId;
		this.calledDecoderVideoBaseType = calledDecoderVideoBaseType;
		this.calledDecoderAudioChannelId = calledDecoderAudioChannelId;
		this.calledDecoderAudioBaseType = calledDecoderAudioBaseType;
		this.callUserId = callUserId;
		this.callUserno = callUserno;
		this.callUsername = callUsername;
		this.callEncoderBundleId = callEncoderBundleId;
		this.callEncoderBundleName = callEncoderBundleName;
		this.callEncoderBundleType = callEncoderBundleType;
		this.callEncoderLayerId = callEncoderLayerId;
		this.callEncoderVideoChannelId = callEncoderVideoChannelId;
		this.callEncoderVideoBaseType = callEncoderVideoBaseType;
		this.callEncoderAudioChannelId = callEncoderAudioChannelId;
		this.callEncoderAudioBaseType = callEncoderAudioBaseType;
		this.callDecoderBundleId = callDecoderBundleId;
		this.callDecoderBundleName = callDecoderBundleName;
		this.callDecoderBundleType = callDecoderBundleType;
		this.callDecoderLayerId = callDecoderLayerId;
		this.callDecoderVideoChannelId = callDecoderVideoChannelId;
		this.callDecoderVideoBaseType = callDecoderVideoBaseType;
		this.callDecoderAudioChannelId = callDecoderAudioChannelId;
		this.callDecoderAudioBaseType = callDecoderAudioBaseType;
	}*/
	
}
