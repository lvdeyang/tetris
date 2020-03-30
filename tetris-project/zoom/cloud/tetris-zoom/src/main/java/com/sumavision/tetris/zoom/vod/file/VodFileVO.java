package com.sumavision.tetris.zoom.vod.file;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class VodFileVO extends AbstractBaseVO<VodFileVO, VodFilePO>{

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

	public String getUserId() {
		return userId;
	}

	public VodFileVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public VodFileVO setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public String getFileId() {
		return fileId;
	}

	public VodFileVO setFileId(String fileId) {
		this.fileId = fileId;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public VodFileVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public VodFileVO setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public VodFileVO set(VodFilePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUserId(entity.getUserId())
			.setNickname(entity.getNickname())
			.setFileId(entity.getFileId())
			.setFileName(entity.getFileName())
			.setUrl(entity.getUrl());
		return this;
	}
	
}
