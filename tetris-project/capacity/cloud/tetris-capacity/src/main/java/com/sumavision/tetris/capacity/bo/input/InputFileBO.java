package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class InputFileBO {

	private List<InputFileObjectBO> file_array;

	public List<InputFileObjectBO> getFile_array() {
		return file_array;
	}

	public InputFileBO setFile_array(List<InputFileObjectBO> file_array) {
		this.file_array = file_array;
		return this;
	}

	public InputFileBO() {
	}

	public InputFileBO(List<InputFileObjectBO> file_array) {
		this.file_array = file_array;
	}

	public InputFileBO(String file_array) {
		this.file_array  = JSONObject.parseArray(file_array,InputFileObjectBO.class);
	}

}
