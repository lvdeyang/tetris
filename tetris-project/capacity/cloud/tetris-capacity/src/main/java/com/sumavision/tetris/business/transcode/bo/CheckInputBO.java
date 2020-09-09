package com.sumavision.tetris.business.transcode.bo;

import com.sumavision.tetris.capacity.bo.input.InputBO;

/**
 * 校验输入参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月17日 上午11:21:44
 */
public class CheckInputBO {
	
	private boolean isExist;

	private String exsitInputId;
	
	private String replaceInputId;
	
	/** 持久化的inputId */
	private Long inputId;
	
	private InputBO inputBO;

	public String getExsitInputId() {
		return exsitInputId;
	}

	public CheckInputBO setExsitInputId(String exsitInputId) {
		this.exsitInputId = exsitInputId;
		return this;
	}

	public String getReplaceInputId() {
		return replaceInputId;
	}

	public CheckInputBO setReplaceInputId(String replaceInputId) {
		this.replaceInputId = replaceInputId;
		return this;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	public Long getInputId() {
		return inputId;
	}

	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}

	public InputBO getInputBO() {
		return inputBO;
	}

	public void setInputBO(InputBO inputBO) {
		this.inputBO = inputBO;
	}
	
}
