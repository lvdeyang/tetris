package com.sumavision.tetris.cs.bak;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name = "TETRIS_CS_SEND_VERSION")
public class VersionSendPO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 频道id */
	private Long channelId;
	
	/** 下发tar包名称 */
	private String fileName;
	
	/** 下发tar包大小 */
	private String fileSize;
	
	/** 下发tar包地址 */
	private String filePath;
	
	/** 播发版本号 */
	private String version;
	
	/** 平台播发id */
	private String broadId;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_SIZE")
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "VERSION")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "BROAD_ID")
	public String getBroadId() {
		return broadId;
	}

	public void setBroadId(String broadId) {
		this.broadId = broadId;
	}
}
