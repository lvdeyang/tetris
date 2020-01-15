package com.sumavision.bvc.device.monitor.live.call;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 呼叫用户业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 下午4:42:31
 */
@Entity
@Table(name = "BVC_MONITOR_LIVE_CALL")
public class MonitorLiveCallPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

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
	
	/** 被叫用户绑定编码器视频通道名称 */
	private String calledEncoderVideoChannelName;
	
	/** 被叫用户绑定编码器音频通道id */
	private String calledEncoderAudioChannelId;
	
	/** 被叫用户绑定编码器音频通道类型 */
	private String calledEncoderAudioBaseType;
	
	/** 被叫用户绑定编码器音频通道名称 */
	private String calledEncoderAudioChannelName;
	
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
	
	/** 被叫用户绑定解码器视频通道名称 */
	private String calledDecoderVideoChannelName;
	
	/** 被叫用户绑定解码器音频通道id */
	private String calledDecoderAudioChannelId;
	
	/** 被叫用户绑定解码器音频通道类型 */
	private String calledDecoderAudioBaseType;
	
	/** 被叫用户绑定解码器音频通道名称 */
	private String calledDecoderAudioChannelName;
	
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
	
	/** 主叫用户绑定编码器视频通道名称 */
	private String callEncoderVideoChannelName;
	
	/** 主叫用户绑定编码器音频通道id */
	private String callEncoderAudioChannelId;
	
	/** 主叫用户绑定编码器音频通道类型 */
	private String callEncoderAudioBaseType;
	
	/** 主叫用户绑定编码器音频通道名称 */
	private String callEncoderAudioChannelName;
	
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
	
	/** 主叫用户绑定解码器视频通道名称 */
	private String callDecoderVideoChannelName;
	
	/** 主叫用户绑定解码器音频通道id */
	private String callDecoderAudioChannelId;
	
	/** 主叫用户绑定解码器音频通道类型 */
	private String callDecoderAudioBaseType;
	
	/** 主叫用户绑定解码器音频通道名称 */
	private String callDecoderAudioChannelName;

	/** codec 模板 */
	private Long avTplId;
	
	/** codec模板档位 */
	private Long gearId;
	
	/** 点播设备任务类型 */
	private LiveType type;
	
	@Column(name = "CALLED_USER_ID")
	public Long getCalledUserId() {
		return calledUserId;
	}

	public void setCalledUserId(Long calledUserId) {
		this.calledUserId = calledUserId;
	}

	@Column(name = "CALLED_USERNO")
	public String getCalledUserno() {
		return calledUserno;
	}

	public void setCalledUserno(String calledUserno) {
		this.calledUserno = calledUserno;
	}

	@Column(name = "CALLED_USERNAME")
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

	@Column(name = "CALLED_ENCODER_VIDEO_CHANNEL_NAME")
	public String getCalledEncoderVideoChannelName() {
		return calledEncoderVideoChannelName;
	}

	public void setCalledEncoderVideoChannelName(String calledEncoderVideoChannelName) {
		this.calledEncoderVideoChannelName = calledEncoderVideoChannelName;
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

	@Column(name = "CALLED_ENCODER_AUDIO_CHANNEL_NAME")
	public String getCalledEncoderAudioChannelName() {
		return calledEncoderAudioChannelName;
	}

	public void setCalledEncoderAudioChannelName(String calledEncoderAudioChannelName) {
		this.calledEncoderAudioChannelName = calledEncoderAudioChannelName;
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

	@Column(name = "CALLED_DECODER_VIDEO_CHANNEL_NAME")
	public String getCalledDecoderVideoChannelName() {
		return calledDecoderVideoChannelName;
	}

	public void setCalledDecoderVideoChannelName(String calledDecoderVideoChannelName) {
		this.calledDecoderVideoChannelName = calledDecoderVideoChannelName;
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

	@Column(name = "CALLED_DECODER_AUDIO_CHANNEL_NAME")
	public String getCalledDecoderAudioChannelName() {
		return calledDecoderAudioChannelName;
	}

	public void setCalledDecoderAudioChannelName(String calledDecoderAudioChannelName) {
		this.calledDecoderAudioChannelName = calledDecoderAudioChannelName;
	}
	
	@Column(name = "CALL_USER_ID")
	public Long getCallUserId() {
		return callUserId;
	}

	public void setCallUserId(Long callUserId) {
		this.callUserId = callUserId;
	}

	@Column(name = "CALL_USERNO")
	public String getCallUserno() {
		return callUserno;
	}

	public void setCallUserno(String callUserno) {
		this.callUserno = callUserno;
	}

	@Column(name = "CALL_USERNAME")
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

	@Column(name = "CALL_ENCODER_VIDEO_CHANNEL_NAME")
	public String getCallEncoderVideoChannelName() {
		return callEncoderVideoChannelName;
	}

	public void setCallEncoderVideoChannelName(String callEncoderVideoChannelName) {
		this.callEncoderVideoChannelName = callEncoderVideoChannelName;
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

	@Column(name = "CALL_ENCODER_AUDIO_CHANNEL_NAME")
	public String getCallEncoderAudioChannelName() {
		return callEncoderAudioChannelName;
	}

	public void setCallEncoderAudioChannelName(String callEncoderAudioChannelName) {
		this.callEncoderAudioChannelName = callEncoderAudioChannelName;
	}

	@Column(name = "CALL_DECODER_BUNDLE_ID")
	public String getCallDecoderBundleId() {
		return callDecoderBundleId;
	}

	public void setCallDecoderBundleId(String callDecoderBundleId) {
		this.callDecoderBundleId = callDecoderBundleId;
	}

	@Column(name = "CALL_DECODER_BUNDLE_NAME")
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

	@Column(name = "CALL_DECODER_VIDEO_CHANNEL_NAME")
	public String getCallDecoderVideoChannelName() {
		return callDecoderVideoChannelName;
	}

	public void setCallDecoderVideoChannelName(String callDecoderVideoChannelName) {
		this.callDecoderVideoChannelName = callDecoderVideoChannelName;
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

	@Column(name = "CALL_DECODER_AUDIO_CHANNEL_NAME")
	public String getCallDecoderAudioChannelName() {
		return callDecoderAudioChannelName;
	}

	public void setCallDecoderAudioChannelName(String callDecoderAudioChannelName) {
		this.callDecoderAudioChannelName = callDecoderAudioChannelName;
	}

	@Column(name = "AVTPL_ID")
	public Long getAvTplId() {
		return avTplId;
	}

	public void setAvTplId(Long avTplId) {
		this.avTplId = avTplId;
	}

	@Column(name = "GEAR_ID")
	public Long getGearId() {
		return gearId;
	}

	public void setGearId(Long gearId) {
		this.gearId = gearId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public LiveType getType() {
		return type;
	}

	public void setType(LiveType type) {
		this.type = type;
	}
	
	public MonitorLiveCallPO(){}
	
	public MonitorLiveCallPO(
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
			String callDecoderAudioBaseType,
			Long avTplId,
			Long gearId,
			LiveType type) throws Exception{
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
		this.calledEncoderVideoChannelName = ChannelType.transChannelName(calledEncoderVideoChannelId);
		this.calledEncoderAudioChannelId = calledEncoderAudioChannelId;
		this.calledEncoderAudioBaseType = calledEncoderAudioBaseType;
		this.calledEncoderAudioChannelName = ChannelType.transChannelName(calledEncoderAudioChannelId);
		this.calledDecoderBundleId = calledDecoderBundleId;
		this.calledDecoderBundleName = calledDecoderBundleName;
		this.calledDecoderBundleType = calledDecoderBundleType;
		this.calledDecoderLayerId = calledDecoderLayerId;
		this.calledDecoderVideoChannelId = calledDecoderVideoChannelId;
		this.calledDecoderVideoBaseType = calledDecoderVideoBaseType;
		this.calledDecoderVideoChannelName = ChannelType.transChannelName(calledDecoderVideoChannelId);
		this.calledDecoderAudioChannelId = calledDecoderAudioChannelId;
		this.calledDecoderAudioBaseType = calledDecoderAudioBaseType;
		this.calledDecoderAudioChannelName = ChannelType.transChannelName(calledDecoderAudioChannelId);
		this.callUserId = callUserId;
		this.callUserno = callUserno;
		this.callUsername = callUsername;
		this.callEncoderBundleId = callEncoderBundleId;
		this.callEncoderBundleName = callEncoderBundleName;
		this.callEncoderBundleType = callEncoderBundleType;
		this.callEncoderLayerId = callEncoderLayerId;
		this.callEncoderVideoChannelId = callEncoderVideoChannelId;
		this.callEncoderVideoBaseType = callEncoderVideoBaseType;
		this.callEncoderVideoChannelName = ChannelType.transChannelName(callEncoderVideoChannelId);
		this.callEncoderAudioChannelId = callEncoderAudioChannelId;
		this.callEncoderAudioBaseType = callEncoderAudioBaseType;
		this.callEncoderAudioChannelName = ChannelType.transChannelName(callEncoderAudioChannelId);
		this.callDecoderBundleId = callDecoderBundleId;
		this.callDecoderBundleName = callDecoderBundleName;
		this.callDecoderBundleType = callDecoderBundleType;
		this.callDecoderLayerId = callDecoderLayerId;
		this.callDecoderVideoChannelId = callDecoderVideoChannelId;
		this.callDecoderVideoBaseType = callDecoderVideoBaseType;
		this.callDecoderVideoChannelName = ChannelType.transChannelName(callDecoderVideoChannelId);
		this.callDecoderAudioChannelId = callDecoderAudioChannelId;
		this.callDecoderAudioBaseType = callDecoderAudioBaseType;
		this.callDecoderAudioChannelName = ChannelType.transChannelName(callDecoderAudioChannelId);
		this.avTplId = avTplId;
		this.gearId = gearId;
		this.type = type;
	}
	
}
