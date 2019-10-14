package com.sumavision.tetris.mims.app.media.encode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_ENCODE_MEDIA_AUDIO")
public class AudioFileEncodePO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 关联音频媒资id */
	private Long mediaId;
	
	/** 加密文件存储地址 */
	private String filePath;
	
	/** 加密文件预览地址 */
	private String previewUrl;

	@Column(name = "MEDIA_ID")
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	
	/**
	 * 复制音频加密信息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月8日 上午10:34:27
	 */
	public AudioFileEncodePO copy(){
		AudioFileEncodePO audioEncode = new AudioFileEncodePO();
		audioEncode.setFilePath(this.getFilePath());
		audioEncode.setPreviewUrl(this.getPreviewUrl());
		return audioEncode;
	}
}
