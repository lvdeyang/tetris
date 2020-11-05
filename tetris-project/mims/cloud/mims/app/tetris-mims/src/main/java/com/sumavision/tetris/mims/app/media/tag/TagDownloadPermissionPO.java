package com.sumavision.tetris.mims.app.media.tag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_TAG_USER_DOWNLOAD_PERMISSION")
public class TagDownloadPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 标签id */
	private Long tagId;
	
	/** 下载次数 */
	private Long downloadCount;
	
	/** 下载用户id */
	private Long userId;
	
	/** 媒资类型 */
	private String type;

	@Column(name = "TAG_ID")
	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	@Column(name = "DOWNLOAD_COUNT")
	public Long getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
