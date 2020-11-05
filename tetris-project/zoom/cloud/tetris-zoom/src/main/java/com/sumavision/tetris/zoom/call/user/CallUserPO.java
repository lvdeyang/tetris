package com.sumavision.tetris.zoom.call.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_ZOOM_CALL_USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"UNIQUE_KEY"})})
public class CallUserPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 呼叫用户id */
	private String srcUserId;
	
	/** 呼叫用户号码 */
	private String srcUserno;
	
	/** 呼叫用户昵称 */
	private String srcNickname;
	
	/** 呼叫用户设备id */
	private String srcBundleId;
	
	/** 呼叫用户设备接入层id */
	private String srcLayerId;
	
	/** 呼叫用户视频通道id */
	private String srcVideoChannelId;
	
	/** 呼叫用户音频通道id */
	private String srcAudioChannelId;
	
	/** 被呼叫用户id */
	private String dstUserId;
	
	/** 被呼叫用户号码 */
	private String dstUserno;
	
	/** 被呼叫用户昵称 */
	private String dstNickname;
	
	/** 被呼叫设备id */
	private String dstBundleId;
	
	/** 被呼叫设备接入层id */
	private String dstLayerId;
	
	/** 被呼叫视频通道id */
	private String dstVideoChannelId;
	
	/** 呗呼叫音频通道id */
	private String dstAudioChannelId;
	
	/** 唯一建约束 srcUserId-dstUserId */
	private String uniqueKey;
	
	/** 通话状态 */
	private CallUserStatus status;
	
	/** 通话类型 */
	private CallUserType type;

	@Column(name = "SRC_USER_ID")
	public String getSrcUserId() {
		return srcUserId;
	}

	public void setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
	}

	@Column(name = "SRC_USERNO")
	public String getSrcUserno() {
		return srcUserno;
	}

	public void setSrcUserno(String srcUserno) {
		this.srcUserno = srcUserno;
	}

	@Column(name = "SRC_NICKNAME")
	public String getSrcNickname() {
		return srcNickname;
	}

	public void setSrcNickname(String srcNickname) {
		this.srcNickname = srcNickname;
	}

	@Column(name = "SRC_BUNDLE_ID")
	public String getSrcBundleId() {
		return srcBundleId;
	}

	public void setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
	}

	@Column(name = "SRC_LAYER_ID")
	public String getSrcLayerId() {
		return srcLayerId;
	}

	public void setSrcLayerId(String srcLayerId) {
		this.srcLayerId = srcLayerId;
	}

	@Column(name = "SRC_VIDEO_CHANNEL_ID")
	public String getSrcVideoChannelId() {
		return srcVideoChannelId;
	}

	public void setSrcVideoChannelId(String srcVideoChannelId) {
		this.srcVideoChannelId = srcVideoChannelId;
	}

	@Column(name = "SRC_AUDIO_CHANNEL_ID")
	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public void setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
	}

	@Column(name = "DST_USER_ID")
	public String getDstUserId() {
		return dstUserId;
	}

	public void setDstUserId(String dstUserId) {
		this.dstUserId = dstUserId;
	}

	@Column(name = "DST_USERNO")
	public String getDstUserno() {
		return dstUserno;
	}

	public void setDstUserno(String dstUserno) {
		this.dstUserno = dstUserno;
	}

	@Column(name = "DST_NICKNAME")
	public String getDstNickname() {
		return dstNickname;
	}

	public void setDstNickname(String dstNickname) {
		this.dstNickname = dstNickname;
	}

	@Column(name = "DST_BUNDLE_ID")
	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	@Column(name = "DST_LAYER_ID")
	public String getDstLayerId() {
		return dstLayerId;
	}

	public void setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
	}

	@Column(name = "DST_VIDEO_CHANNEL_ID")
	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public void setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
	}

	@Column(name = "DST_AUDIO_CHANNEL_ID")
	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

	@Column(name = "UNIQUE_KEY")
	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
	public void setUniqueKey(){
		this.uniqueKey = CallUserPO.generateUniqueKey(this.srcUserId, this.dstUserId);
	}
	
	public static String generateUniqueKey(String userId0, String userId1){
		String key0 = null;
		String key1 = null;
		if(userId0.compareTo(userId1) > 0){
			key0 = userId0;
			key1 = userId1;
		}else{
			key0 = userId1;
			key1 = userId0;
		}
		return new StringBufferWrapper().append(key0).append("-").append(key1).toString();
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public CallUserStatus getStatus() {
		return status;
	}

	public void setStatus(CallUserStatus status) {
		this.status = status;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public CallUserType getType() {
		return type;
	}

	public void setType(CallUserType type) {
		this.type = type;
	}
	
}
