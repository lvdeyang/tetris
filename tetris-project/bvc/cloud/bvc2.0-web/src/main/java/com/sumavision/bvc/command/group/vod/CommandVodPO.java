package com.sumavision.bvc.command.group.vod;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.VodType;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_COMMAND_VOD")
public class CommandVodPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 点播类型，区分点播用户还是点播设备 */
	private VodType vodType;
	
	/** 目的设备类型 */
	private DstDeviceType dstDeviceType;
	
	/************
	 *源--被点播方*
	 ************/
	
	private Long sourceUserId;
	
	/** 被点播用户/设备号码 */
	private String sourceNo;
	
	/** 点播文件时，记录文件名 */
	private String sourceUserName;
	
	private String sourceBundleId;
	
	private String sourceBundleName;
	
	private String sourceBundleType;
	
	private String sourceLayerId;
	
	private String sourceVideoChannelId;
	
	private String sourceVideoBaseType;
	
	private String sourceAudioChannelId;
	
	private String sourceAudioBaseType;
	
	/** 点播文件时，记录播放地址 */
	private String playUrl;
	
	/************
	 *目的--播放器*
	 ************/
	
	private Long dstUserId;
	
	/** 发起人的号码；不是播放器的！！！ */
	private String dstUserNo;
	
	private String dstUserName;
	
	private String dstBundleId;
	
	private String dstBundleName;
	
	private String dstBundleType;
	
	private String dstLayerId;
	
	private String dstVideoChannelId;
	
	private String dstVideoBaseType;
	
	private String dstAudioChannelId;
	
	private String dstAudioBaseType;

	public VodType getVodType() {
		return vodType;
	}

	public void setVodType(VodType vodType) {
		this.vodType = vodType;
	}

	@Enumerated(value = EnumType.STRING)
	public DstDeviceType getDstDeviceType() {
		return dstDeviceType;
	}

	public void setDstDeviceType(DstDeviceType dstDeviceType) {
		this.dstDeviceType = dstDeviceType;
	}

	public Long getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getSourceUserName() {
		return sourceUserName;
	}

	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}

	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public void setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
	}

	public String getSourceBundleName() {
		return sourceBundleName;
	}

	public void setSourceBundleName(String sourceBundleName) {
		this.sourceBundleName = sourceBundleName;
	}

	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
	}

	public String getSourceVideoChannelId() {
		return sourceVideoChannelId;
	}

	public void setSourceVideoChannelId(String sourceVideoChannelId) {
		this.sourceVideoChannelId = sourceVideoChannelId;
	}

	public String getSourceVideoBaseType() {
		return sourceVideoBaseType;
	}

	public void setSourceVideoBaseType(String sourceVideoBaseType) {
		this.sourceVideoBaseType = sourceVideoBaseType;
	}

	public String getSourceAudioChannelId() {
		return sourceAudioChannelId;
	}

	public void setSourceAudioChannelId(String sourceAudioChannelId) {
		this.sourceAudioChannelId = sourceAudioChannelId;
	}

	public String getSourceAudioBaseType() {
		return sourceAudioBaseType;
	}

	public void setSourceAudioBaseType(String sourceAudioBaseType) {
		this.sourceAudioBaseType = sourceAudioBaseType;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public Long getDstUserId() {
		return dstUserId;
	}

	public void setDstUserId(Long dstUserId) {
		this.dstUserId = dstUserId;
	}

	public String getDstUserNo() {
		return dstUserNo;
	}

	public void setDstUserNo(String dstUserNo) {
		this.dstUserNo = dstUserNo;
	}

	public String getDstUserName() {
		return dstUserName;
	}

	public void setDstUserName(String dstUserName) {
		this.dstUserName = dstUserName;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	public String getDstBundleName() {
		return dstBundleName;
	}

	public void setDstBundleName(String dstBundleName) {
		this.dstBundleName = dstBundleName;
	}

	public String getDstLayerId() {
		return dstLayerId;
	}

	public void setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
	}

	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public void setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
	}

	public String getDstVideoBaseType() {
		return dstVideoBaseType;
	}

	public void setDstVideoBaseType(String dstVideoBaseType) {
		this.dstVideoBaseType = dstVideoBaseType;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

	public String getDstAudioBaseType() {
		return dstAudioBaseType;
	}

	public void setDstAudioBaseType(String dstAudioBaseType) {
		this.dstAudioBaseType = dstAudioBaseType;
	}
	
	public String getSourceBundleType() {
		return sourceBundleType;
	}

	public void setSourceBundleType(String sourceBundleType) {
		this.sourceBundleType = sourceBundleType;
	}

	public String getDstBundleType() {
		return dstBundleType;
	}

	public void setDstBundleType(String dstBundleType) {
		this.dstBundleType = dstBundleType;
	}

	public CommandVodPO(){}

	public CommandVodPO(VodType vodType, Long sourceUserId, String sourceNo, String sourceUserName, String sourceBundleId,
			String sourceBundleName, String sourceBundleType, String sourceLayerId, String sourceVideoChannelId,
			String sourceVideoBaseType, String sourceAudioChannelId, String sourceAudioBaseType, Long dstUserId, String dstUserNo,
			String dstUserName, String dstBundleId, String dstBundleName, String dstBundleType, String dstLayerId,
			String dstVideoChannelId, String dstVideoBaseType, String dstAudioChannelId, String dstAudioBaseType) {
		this.vodType = vodType;
		this.sourceUserId = sourceUserId;
		this.sourceNo = sourceNo;
		this.sourceUserName = sourceUserName;
		this.sourceBundleId = sourceBundleId;
		this.sourceBundleName = sourceBundleName;
		this.sourceBundleType = sourceBundleType;
		this.sourceLayerId = sourceLayerId;
		this.sourceVideoChannelId = sourceVideoChannelId;
		this.sourceVideoBaseType = sourceVideoBaseType;
		this.sourceAudioChannelId = sourceAudioChannelId;
		this.sourceAudioBaseType = sourceAudioBaseType;
		this.dstUserId = dstUserId;
		this.dstUserNo = dstUserNo;
		this.dstUserName = dstUserName;
		this.dstBundleId = dstBundleId;
		this.dstBundleName = dstBundleName;
		this.dstBundleType = dstBundleType;
		this.dstLayerId = dstLayerId;
		this.dstVideoChannelId = dstVideoChannelId;
		this.dstVideoBaseType = dstVideoBaseType;
		this.dstAudioChannelId = dstAudioChannelId;
		this.dstAudioBaseType = dstAudioBaseType;
	}

}
