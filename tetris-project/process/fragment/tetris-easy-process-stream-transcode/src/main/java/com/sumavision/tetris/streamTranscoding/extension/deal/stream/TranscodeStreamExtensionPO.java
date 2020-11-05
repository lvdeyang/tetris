package com.sumavision.tetris.streamTranscoding.extension.deal.stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_MEDIA_STREAM_EXTENSION")
public class TranscodeStreamExtensionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 扩展名 */
	private String extension;

	@Column(name = "EXTENSION")
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
