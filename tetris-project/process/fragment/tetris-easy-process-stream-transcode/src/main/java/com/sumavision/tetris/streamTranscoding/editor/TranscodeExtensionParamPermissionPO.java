package com.sumavision.tetris.streamTranscoding.editor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_MEDIA_EDITOR_EXTENSION_PARAM_PERMISSION")
public class TranscodeExtensionParamPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 扩展名 */
	private String extension;
	
	/** 模板名 */
	private String param;

	@Column(name = "EXTENSION")
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Column(name = "PARAM")
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
