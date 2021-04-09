package com.sumavision.bvc.command.group.user;

import java.util.Date;

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
@Table(name = "BVC_USER_LIVE_CALL", uniqueConstraints={@UniqueConstraint(columnNames = {"CALLED_USER_ID", "CALL_USER_ID"})})
public class UserLiveCallPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 如果是从点播转呼叫而来，则记录点播CommandVodPO的id */
	private Long formerVodId;
	
	/** 呼叫消息id */
	private Long messageId;
	
	private CallType callType = CallType.LOCAL_LOCAL;
	
	private UserCallType type;
	
	/** 通话状态 */
	private CallStatus status = CallStatus.WAITING_FOR_RESPONSE;
	
	/************
	 ***被叫用户***
	 ************/
	
	/** 被叫用户id */
	private Long calledUserId;
	
	/** 被叫用户号码 */
	private String calledUserno;
	
	/** 被叫用户名 */
	private String calledUsername;
	
	/********************
	 ***被叫用户绑定编码器***
	 ********************/
	
	/** 被叫用户绑定编码器 */
	private String calledEncoderBundleId;
	
	/** 被叫用户绑定编码器名称 */
	private String calledEncoderBundleName;
	
	/** 被叫用户绑定编码器类型 */
	private String calledEncoderBundleType;
	
	/** 被叫用户绑定编码器接入层 */
	private String calledEncoderLayerId;
	
	/** 被叫用户绑定编码器视频通道id */
	private String calledEncoderVideoChannelId;
	
	/** 被叫用户绑定编码器视频通道类型 */
	private String calledEncoderVideoBaseType;
	
	/** 被叫用户绑定编码器音频通道id */
	private String calledEncoderAudioChannelId;
	
	/** 被叫用户绑定编码器音频通道类型 */
	private String calledEncoderAudioBaseType;
	
	/********************
	 ***被叫用户绑定解码器***
	 ********************/
	
	/** 被叫用户绑定解码器 */
	private String calledDecoderBundleId;
	
	/** 被叫用户绑定解码器名称 */
	private String calledDecoderBundleName;
	
	/** 被叫用户绑定解码器类型 */
	private String calledDecoderBundleType;
	
	/** 被叫用户绑定解码器接入层 */
	private String calledDecoderLayerId;
	
	/** 被叫用户绑定解码器视频通道id */
	private String calledDecoderVideoChannelId;
	
	/** 被叫用户绑定解码器视频通道类型 */
	private String calledDecoderVideoBaseType;
	
	/** 被叫用户绑定解码器音频通道id */
	private String calledDecoderAudioChannelId;
	
	/** 被叫用户绑定解码器音频通道类型 */
	private String calledDecoderAudioBaseType;
	
	/************
	 ***主叫用户***
	 ************/
	
	/** 主叫用户id */
	private Long callUserId;
	
	/** 主叫用户号码 */
	private String callUserno;
	
	/** 主叫用户名 */
	private String callUsername;
	
	/********************
	 ***主叫用户绑定编码器***
	 ********************/
	
	/** 主叫用户绑定编码器 */
	private String callEncoderBundleId;
	
	/** 主叫用户绑定编码器名称 */
	private String callEncoderBundleName;
	
	/** 主叫用户绑定编码器类型 */
	private String callEncoderBundleType;
	
	/** 主叫用户绑定编码器接入层 */
	private String callEncoderLayerId;
	
	/** 主叫用户绑定编码器视频通道id */
	private String callEncoderVideoChannelId;
	
	/** 主叫用户绑定编码器视频通道类型 */
	private String callEncoderVideoBaseType;
	
	/** 主叫用户绑定编码器音频通道id */
	private String callEncoderAudioChannelId;
	
	/** 主叫用户绑定编码器音频通道类型 */
	private String callEncoderAudioBaseType;
	
	/********************
	 ***主叫用户绑定解码器***
	 ********************/
	
	/** 主叫用户绑定解码器 */
	private String callDecoderBundleId;
	
	/** 主叫用户绑定解码器名称 */
	private String callDecoderBundleName;
	
	/** 主叫用户绑定解码器类型 */
	private String callDecoderBundleType;
	
	/** 主叫用户绑定解码器接入层 */
	private String callDecoderLayerId;
	
	/** 主叫用户绑定解码器视频通道id */
	private String callDecoderVideoChannelId;
	
	/** 主叫用户绑定解码器视频通道类型 */
	private String callDecoderVideoBaseType;

	/** 主叫用户绑定解码器音频通道id */
	private String callDecoderAudioChannelId;
	
	/** 主叫用户绑定解码器音频通道类型 */
	private String callDecoderAudioBaseType;
	
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

	@Column(name = "CALLED_USER_ID")
	public Long getCalledUserId() {
		return calledUserId;
	}

	public void setCalledUserId(Long calledUserId) {
		this.calledUserId = calledUserId;
	}

	@Column(name = "CALLED_USER_NO")
	public String getCalledUserno() {
		return calledUserno;
	}

	public void setCalledUserno(String calledUserno) {
		this.calledUserno = calledUserno;
	}

	@Column(name = "CALLED_USER_NAME")
	public String getCalledUsername() {
		return calledUsername;
	}

	public void setCalledUsername(String calledUsername) {
		this.calledUsername = calledUsername;
	}

	@Column(name = "CALLED_ENCODER_BUNDLE_ID")
	public String getCalledEncoderBundleId() {
		return calledEncoderBundleId;
	}

	public void setCalledEncoderBundleId(String calledEncoderBundleId) {
		this.calledEncoderBundleId = calledEncoderBundleId;
	}

	@Column(name = "CALLED_ENCODER_BUNDLE_NAME")
	public String getCalledEncoderBundleName() {
		return calledEncoderBundleName;
	}

	public void setCalledEncoderBundleName(String calledEncoderBundleName) {
		this.calledEncoderBundleName = calledEncoderBundleName;
	}

	@Column(name = "CALLED_ENCODER_BUNDLE_TYPE")
	public String getCalledEncoderBundleType() {
		return calledEncoderBundleType;
	}

	public void setCalledEncoderBundleType(String calledEncoderBundleType) {
		this.calledEncoderBundleType = calledEncoderBundleType;
	}

	@Column(name = "CALLED_ENCODER_LAYER_ID")
	public String getCalledEncoderLayerId() {
		return calledEncoderLayerId;
	}

	public void setCalledEncoderLayerId(String calledEncoderLayerId) {
		this.calledEncoderLayerId = calledEncoderLayerId;
	}

	@Column(name = "CALLED_ENCODER_VIDEO_CHANNEL_ID")
	public String getCalledEncoderVideoChannelId() {
		return calledEncoderVideoChannelId;
	}

	public void setCalledEncoderVideoChannelId(String calledEncoderVideoChannelId) {
		this.calledEncoderVideoChannelId = calledEncoderVideoChannelId;
	}

	@Column(name = "CALLED_ENCODER_VIDEO_BASE_TYPE")
	public String getCalledEncoderVideoBaseType() {
		return calledEncoderVideoBaseType;
	}

	public void setCalledEncoderVideoBaseType(String calledEncoderVideoBaseType) {
		this.calledEncoderVideoBaseType = calledEncoderVideoBaseType;
	}

	@Column(name = "CALLED_ENCODER_AUDIO_CHANNEL_ID")
	public String getCalledEncoderAudioChannelId() {
		return calledEncoderAudioChannelId;
	}

	public void setCalledEncoderAudioChannelId(String calledEncoderAudioChannelId) {
		this.calledEncoderAudioChannelId = calledEncoderAudioChannelId;
	}

	@Column(name = "CALLED_ENCODER_AUDIO_BASE_TYPE")
	public String getCalledEncoderAudioBaseType() {
		return calledEncoderAudioBaseType;
	}

	public void setCalledEncoderAudioBaseType(String calledEncoderAudioBaseType) {
		this.calledEncoderAudioBaseType = calledEncoderAudioBaseType;
	}

	@Column(name = "CALLED_DECODER_BUNDLE_ID")
	public String getCalledDecoderBundleId() {
		return calledDecoderBundleId;
	}

	public void setCalledDecoderBundleId(String calledDecoderBundleId) {
		this.calledDecoderBundleId = calledDecoderBundleId;
	}

	@Column(name = "CALLED_DECODER_BUNDLE_NAME")
	public String getCalledDecoderBundleName() {
		return calledDecoderBundleName;
	}

	public void setCalledDecoderBundleName(String calledDecoderBundleName) {
		this.calledDecoderBundleName = calledDecoderBundleName;
	}

	@Column(name = "CALLED_DECODER_BUNDLE_TYPE")
	public String getCalledDecoderBundleType() {
		return calledDecoderBundleType;
	}

	public void setCalledDecoderBundleType(String calledDecoderBundleType) {
		this.calledDecoderBundleType = calledDecoderBundleType;
	}

	@Column(name = "CALLED_DECODER_LAYER_ID")
	public String getCalledDecoderLayerId() {
		return calledDecoderLayerId;
	}

	public void setCalledDecoderLayerId(String calledDecoderLayerId) {
		this.calledDecoderLayerId = calledDecoderLayerId;
	}

	@Column(name = "CALLED_DECODER_VIDEO_CHANNEL_ID")
	public String getCalledDecoderVideoChannelId() {
		return calledDecoderVideoChannelId;
	}

	public void setCalledDecoderVideoChannelId(String calledDecoderVideoChannelId) {
		this.calledDecoderVideoChannelId = calledDecoderVideoChannelId;
	}

	@Column(name = "CALLED_DECODER_VIDEO_BASE_TYPE")
	public String getCalledDecoderVideoBaseType() {
		return calledDecoderVideoBaseType;
	}

	public void setCalledDecoderVideoBaseType(String calledDecoderVideoBaseType) {
		this.calledDecoderVideoBaseType = calledDecoderVideoBaseType;
	}

	@Column(name = "CALLED_DECODER_AUDIO_CHANNEL_ID")
	public String getCalledDecoderAudioChannelId() {
		return calledDecoderAudioChannelId;
	}

	public void setCalledDecoderAudioChannelId(String calledDecoderAudioChannelId) {
		this.calledDecoderAudioChannelId = calledDecoderAudioChannelId;
	}

	@Column(name = "CALLED_DECODER_AUDIO_BASE_TYPE")
	public String getCalledDecoderAudioBaseType() {
		return calledDecoderAudioBaseType;
	}

	public void setCalledDecoderAudioBaseType(String calledDecoderAudioBaseType) {
		this.calledDecoderAudioBaseType = calledDecoderAudioBaseType;
	}

	@Column(name = "CALL_USER_ID")
	public Long getCallUserId() {
		return callUserId;
	}

	public void setCallUserId(Long callUserId) {
		this.callUserId = callUserId;
	}

	@Column(name = "CALL_USER_NO")
	public String getCallUserno() {
		return callUserno;
	}

	public void setCallUserno(String callUserno) {
		this.callUserno = callUserno;
	}

	@Column(name = "CALL_USER_NAME")
	public String getCallUsername() {
		return callUsername;
	}

	public void setCallUsername(String callUsername) {
		this.callUsername = callUsername;
	}

	@Column(name = "CALL_ENCODER_BUNDLE_ID")
	public String getCallEncoderBundleId() {
		return callEncoderBundleId;
	}

	public void setCallEncoderBundleId(String callEncoderBundleId) {
		this.callEncoderBundleId = callEncoderBundleId;
	}

	@Column(name = "CALL_ENCODER_BUNDLE_NAME")
	public String getCallEncoderBundleName() {
		return callEncoderBundleName;
	}

	public void setCallEncoderBundleName(String callEncoderBundleName) {
		this.callEncoderBundleName = callEncoderBundleName;
	}

	@Column(name = "CALL_ENCODER_BUNDLE_TYPE")
	public String getCallEncoderBundleType() {
		return callEncoderBundleType;
	}

	public void setCallEncoderBundleType(String callEncoderBundleType) {
		this.callEncoderBundleType = callEncoderBundleType;
	}

	@Column(name = "CALL_ENCODER_LAYER_ID")
	public String getCallEncoderLayerId() {
		return callEncoderLayerId;
	}

	public void setCallEncoderLayerId(String callEncoderLayerId) {
		this.callEncoderLayerId = callEncoderLayerId;
	}

	@Column(name = "CALL_ENCODER_VIDEO_CHANNEL_ID")
	public String getCallEncoderVideoChannelId() {
		return callEncoderVideoChannelId;
	}

	public void setCallEncoderVideoChannelId(String callEncoderVideoChannelId) {
		this.callEncoderVideoChannelId = callEncoderVideoChannelId;
	}

	@Column(name = "CALL_ENCODER_VIDEO_BASE_TYPE")
	public String getCallEncoderVideoBaseType() {
		return callEncoderVideoBaseType;
	}

	public void setCallEncoderVideoBaseType(String callEncoderVideoBaseType) {
		this.callEncoderVideoBaseType = callEncoderVideoBaseType;
	}

	@Column(name = "CALL_ENCODER_AUDIO_CHANNEL_ID")
	public String getCallEncoderAudioChannelId() {
		return callEncoderAudioChannelId;
	}

	public void setCallEncoderAudioChannelId(String callEncoderAudioChannelId) {
		this.callEncoderAudioChannelId = callEncoderAudioChannelId;
	}

	@Column(name = "CALL_ENCODER_AUDIO_BASE_TYPE")
	public String getCallEncoderAudioBaseType() {
		return callEncoderAudioBaseType;
	}

	public void setCallEncoderAudioBaseType(String callEncoderAudioBaseType) {
		this.callEncoderAudioBaseType = callEncoderAudioBaseType;
	}

	@Column(name = "CALL_DECODER_BUNDLE_ID")
	public String getCallDecoderBundleId() {
		return callDecoderBundleId;
	}

	public void setCallDecoderBundleId(String callDecoderBundleId) {
		this.callDecoderBundleId = callDecoderBundleId;
	}

	@Column(name = "CALL_DECODE_BUNDLE_NAME")
	public String getCallDecoderBundleName() {
		return callDecoderBundleName;
	}

	public void setCallDecoderBundleName(String callDecoderBundleName) {
		this.callDecoderBundleName = callDecoderBundleName;
	}

	@Column(name = "CALL_DECODER_BUNDLE_TYPE")
	public String getCallDecoderBundleType() {
		return callDecoderBundleType;
	}

	public void setCallDecoderBundleType(String callDecoderBundleType) {
		this.callDecoderBundleType = callDecoderBundleType;
	}

	@Column(name = "CALL_DECODER_LAYER_ID")
	public String getCallDecoderLayerId() {
		return callDecoderLayerId;
	}

	public void setCallDecoderLayerId(String callDecoderLayerId) {
		this.callDecoderLayerId = callDecoderLayerId;
	}

	@Column(name = "CALL_DECODER_VIDEO_CHANNEL_ID")
	public String getCallDecoderVideoChannelId() {
		return callDecoderVideoChannelId;
	}

	public void setCallDecoderVideoChannelId(String callDecoderVideoChannelId) {
		this.callDecoderVideoChannelId = callDecoderVideoChannelId;
	}

	@Column(name = "CALL_DECODER_VIDEO_BASE_TYPE")
	public String getCallDecoderVideoBaseType() {
		return callDecoderVideoBaseType;
	}

	public void setCallDecoderVideoBaseType(String callDecoderVideoBaseType) {
		this.callDecoderVideoBaseType = callDecoderVideoBaseType;
	}

	@Column(name = "CALL_DECODER_AUDIO_CHANNEL_ID")
	public String getCallDecoderAudioChannelId() {
		return callDecoderAudioChannelId;
	}

	public void setCallDecoderAudioChannelId(String callDecoderAudioChannelId) {
		this.callDecoderAudioChannelId = callDecoderAudioChannelId;
	}

	@Column(name = "CALL_DECODER_AUDIO_BASE_TYPE")
	public String getCallDecoderAudioBaseType() {
		return callDecoderAudioBaseType;
	}

	public void setCallDecoderAudioBaseType(String callDecoderAudioBaseType) {
		this.callDecoderAudioBaseType = callDecoderAudioBaseType;
	}
	
	public UserLiveCallPO(){}
	
	public UserLiveCallPO(
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
	}
	
}
