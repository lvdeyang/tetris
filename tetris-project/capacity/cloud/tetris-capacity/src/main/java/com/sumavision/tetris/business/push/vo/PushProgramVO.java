package com.sumavision.tetris.business.push.vo;

import java.util.List;

public class PushProgramVO {

	private String type;

	private PushFileVO file;//兼容旧版本，模板任务接口调完后用的file_array

	private List<PushFileVO> file_array;
	
	private PushStreamVO stream;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PushFileVO> getFile_array() {
		return file_array;
	}

	public PushProgramVO setFile_array(List<PushFileVO> file_array) {
		this.file_array = file_array;
		return this;
	}

	public PushStreamVO getStream() {
		return stream;
	}

	public void setStream(PushStreamVO stream) {
		this.stream = stream;
	}

	public PushFileVO getFile() {
		return file;
	}

	public PushProgramVO setFile(PushFileVO file) {
		this.file = file;
		return this;
	}
}
