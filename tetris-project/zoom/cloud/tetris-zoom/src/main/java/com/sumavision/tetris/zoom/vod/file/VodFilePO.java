package com.sumavision.tetris.zoom.vod.file;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_ZOOM_VOD_FILE")
public class VodFilePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 用户id */
	private String userId;

	/** 用户昵称 */
	private String nickname;
	
	/** 文件id */
	private String fileId;
	
	/** 文件名称 */
	private String fileName;
	
	/** 文件url */
	private String url;

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "NICKNAME")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "FILE_ID")
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
