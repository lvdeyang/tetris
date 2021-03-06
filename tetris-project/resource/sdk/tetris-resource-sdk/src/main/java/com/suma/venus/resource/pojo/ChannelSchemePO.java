package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 通道channel能力配置表
 * @author lxw
 *
 */
@Entity
public class ChannelSchemePO extends CommonPO<ChannelSchemePO>{

	/*所属bundle的ID*/
	private String bundleId;
	
	/*配置的通道模板的ID*/
	private Long channelTemplateID;
	
	/*配置的通道模板的channelName*/
	private String channelName;
	
	/*配置的通道标号*/
	private String channelId;
	
	private LockStatus channelStatus = LockStatus.IDLE;
	
	/**操作计数*/
	private Integer operateIndex = 0;
	
	/**-----------云导播---------------*/
	
	/**编码格式*/
	private String codec;
	
	/**分辨率*/
	private String resolution;
	
	/**帧率*/
	private String fps;
	
	/**码率*/
	private String bitrate;
	
	/**采样率*/
	private String samplateRate;
	
	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getSamplateRate() {
		return samplateRate;
	}

	public void setSamplateRate(String samplateRate) {
		this.samplateRate = samplateRate;
	}

	public enum LockStatus{
		BUSY,
		IDLE
	}

	@Column(name="bundle_id")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	
	@Column(name="channeltemplate_id")
	public Long getChannelTemplateID() {
		return channelTemplateID;
	}

	public void setChannelTemplateID(Long channelTemplateID) {
		this.channelTemplateID = channelTemplateID;
	}

	@Column(name="channel_id")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	@Column(name="channel_name")
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Column(name="channel_status")
	@Enumerated(EnumType.STRING)
	public LockStatus getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(LockStatus channelStatus) {
		this.channelStatus = channelStatus;
	}

	@Column(name="operate_index")
	public Integer getOperateIndex() {
		return operateIndex;
	}

	public void setOperateIndex(Integer operateIndex) {
		this.operateIndex = operateIndex;
	}
}
