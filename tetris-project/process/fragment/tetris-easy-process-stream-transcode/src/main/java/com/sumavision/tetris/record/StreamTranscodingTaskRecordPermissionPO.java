package com.sumavision.tetris.record;

import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Table(name = "TETRIS_STREAM_TRANSCODING_TASK_RECORD_PERMISSION")
public class StreamTranscodingTaskRecordPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 转码任务id */
	private Long messageId;
	/** 录制任务输入url */
	private String url;
	/** 注入媒资的id */
	private String mediaId;
	/** 注入媒资的uuid */
	private String mediaUuid;
	/** 回调url */
	private String recordCallback;

	@Column(name = "MESSAEG_ID")
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "media_id")
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "media_uuid")
	public String getMediaUuid() {
		return mediaUuid;
	}

	public void setMediaUuid(String mediaUuid) {
		this.mediaUuid = mediaUuid;
	}

	@Column(name = "RECORD_CALLBACK")
	public String getRecordCallback() {
		return recordCallback;
	}

	public void setRecordCallback(String recordCallback) {
		this.recordCallback = recordCallback;
	}
}
