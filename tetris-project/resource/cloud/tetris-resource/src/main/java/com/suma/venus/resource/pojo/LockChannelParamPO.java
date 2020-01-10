package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

/***
 * 被锁定的channel_id及其对应的参数
 * @author lxw
 *
 */
@Entity
public class LockChannelParamPO extends CommonPO<LockChannelParamPO>{

	private String channelId;
	
	private String bundleId;
	
	private Long taskId;
	
	private Long userId;
	
	/**记录channel当前被锁定的参数*/
	private String channelParam;
	
	/**占用音频解码路数**/
	private Integer useAudioSrcCnt = 0;

	/**占用视频解码路数**/
	private Integer useVideoSrcCnt = 0;
	
	@Column(name="channel_id")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	@Column(name="bundle_id")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name="task_id")
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@Column(name="user_id")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Type(type="text")
	@Column(name="channel_param",length=20000)
	public String getChannelParam() {
		return channelParam;
	}

	public void setChannelParam(String channelParam) {
		this.channelParam = channelParam;
	}

	@Column(name="use_audio_src_cnt")
	public Integer getUseAudioSrcCnt() {
		return useAudioSrcCnt;
	}

	public void setUseAudioSrcCnt(Integer useAudioSrcCnt) {
		this.useAudioSrcCnt = useAudioSrcCnt;
	}

	@Column(name="use_video_src_cnt")
	public Integer getUseVideoSrcCnt() {
		return useVideoSrcCnt;
	}

	public void setUseVideoSrcCnt(Integer useVideoSrcCnt) {
		this.useVideoSrcCnt = useVideoSrcCnt;
	}
}
