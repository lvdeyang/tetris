package com.sumavision.tetris.mims.app.media.stream.audio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_MEDIA_AUDIO_STREAM")
public class MediaAudioStreamPO extends AbstractBasePO{

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
	
	/** 预览地址 */
	private String previewUrl;
	
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
	
	/** 文件上传状态 */
	private UploadStatus uploadStatus;
	
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

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
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
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "UPLOAD_STATUS")
	public UploadStatus getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
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
	 * 复制音频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:45:49
	 * @return MediaVideoStreamPO 复制的音频流媒资
	 */
	public MediaAudioStreamPO copy(){
		MediaAudioStreamPO copy_audioStream = new MediaAudioStreamPO();
		copy_audioStream.setUuid(this.getUuid());
		copy_audioStream.setUpdateTime(new Date());
		copy_audioStream.setName(this.getName());
		copy_audioStream.setPreviewUrl(this.getPreviewUrl());
		copy_audioStream.setAuthorId(this.getAuthorId());
		copy_audioStream.setAuthorName(this.getAuthorName());
		copy_audioStream.setCreateTime(this.getCreateTime());
		copy_audioStream.setRemarks(this.getRemarks());
		copy_audioStream.setTags(this.getTags());
		copy_audioStream.setKeyWords(this.getKeyWords());
		copy_audioStream.setUploadStatus(this.getUploadStatus());
		return copy_audioStream;
	}
	
}
