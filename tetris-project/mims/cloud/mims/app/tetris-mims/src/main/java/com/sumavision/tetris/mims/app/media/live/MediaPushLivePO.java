package com.sumavision.tetris.mims.app.media.live;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * push使用的直播媒资<br/>
 * @author lzp
 *
 */
@Entity
@Table(name = "MIMS_MEDIA_PUSH_LIVE")
public class MediaPushLivePO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 原始媒资 */
	public static final String VERSION_OF_ORIGIN = "0.0";
	
	/** 二次生产媒资 */
	public static final String VERSION_OF_SECONDARY = "0.1";
	
	/** 标签分隔符 */
	public static final String SEPARATOR_TAG = ",";
	
	/** 关键字分隔符 */
	public static final String SEPARATOR_KEYWORDS = ",";
	
	/** 媒资别名 */
	private String name;
	
	/** 上传者id */
	private String authorId;
	
	/** 上传者 */
	private String authorName;
	
	/** 媒资创建时间 */
	private Date createTime;
	
	/** 备注 */
	private String remarks;
	
	/** 标签，格式：,分割 */
	private String tags;
	
	/** 文件夹id */
	private Long folderId;
	
	/** 关键字， 格式：,分割*/
	private String keyWords;
	
	/** 频点 */
	private String freq;
	
	/** 音频pid */
	private String audioPid;
	
	/** 视频pid */
	private String videoPid;
	
	/** 审核状态 */
	private ReviewStatus reviewStatus;
	
	/** 审核流程id */
	private String processInstanceId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AUTHORID")
	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	@Column(name = "AUTHOR_NAME")
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "TAGS")
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "KEY_WORDS")
	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	@Column(name = "FREQ")
	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	@Column(name = "AUDIO_PID")
	public String getAudioPid() {
		return audioPid;
	}

	public void setAudioPid(String audioPid) {
		this.audioPid = audioPid;
	}

	@Column(name = "VIDEO_PID")
	public String getVideoPid() {
		return videoPid;
	}

	public void setVideoPid(String videoPid) {
		this.videoPid = videoPid;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "REVIEW_STATUS")
	public ReviewStatus getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(ReviewStatus reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	@Column(name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/**
	 * 复制视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:45:49
	 * @return MediaLivePO 复制的直播媒资
	 */
	public MediaPushLivePO copy(){
		MediaPushLivePO copy_live = new MediaPushLivePO();
		copy_live.setUuid(this.getUuid());
		copy_live.setUpdateTime(new Date());
		copy_live.setName(this.getName());
		copy_live.setAuthorId(this.getAuthorId());
		copy_live.setAuthorName(this.getAuthorName());
		copy_live.setCreateTime(this.getCreateTime());
		copy_live.setRemarks(this.getRemarks());
		copy_live.setTags(this.getTags());
		copy_live.setKeyWords(this.getKeyWords());
		copy_live.setFreq(this.getFreq());
		copy_live.setAudioPid(this.getAudioPid());
		copy_live.setVideoPid(this.getVideoPid());
		return copy_live;
	}
}
