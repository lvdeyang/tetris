package com.sumavision.signal.bvc.capacity.bo.input;

import java.util.List;

/**
 * cover参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午3:03:26
 */
public class CoverBO {

	/** 模式 auto/manual */
	private String mode;
	
	private List<CoverFileBO> files;
	
	private List<BackUpProgramBO> program_array;

	public String getMode() {
		return mode;
	}

	public CoverBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public List<CoverFileBO> getFiles() {
		return files;
	}

	public CoverBO setFiles(List<CoverFileBO> files) {
		this.files = files;
		return this;
	}

	public List<BackUpProgramBO> getProgram_array() {
		return program_array;
	}

	public CoverBO setProgram_array(List<BackUpProgramBO> program_array) {
		this.program_array = program_array;
		return this;
	}
	
}
