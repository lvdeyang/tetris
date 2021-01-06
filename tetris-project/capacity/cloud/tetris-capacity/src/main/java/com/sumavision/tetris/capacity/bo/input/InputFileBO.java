package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

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

	public InputFileBO(String file_array) throws BaseException {
		List<InputFileObjectBO> inputFileObjectBOS = new ArrayList<>();
		JSONArray files = JSONArray.parseArray(file_array);
		for (int i = 0; i < files.size(); i++) {
			InputFileObjectBO fileObjectBO = new InputFileObjectBO();
			JSONObject fileJSON = files.getJSONObject(i);
			if (fileJSON.containsKey("count")){
				fileObjectBO.setLoop_count(fileJSON.getInteger("count"));
			}else if (fileJSON.containsKey("loop_count")){
				fileObjectBO.setLoop_count(fileJSON.getInteger("loop_count"));
			}
			if (fileJSON.containsKey("seek")){
				fileObjectBO.setStart_ms(fileJSON.getInteger("seek"));
			}else if(fileJSON.containsKey("start_ms")){
				fileObjectBO.setStart_ms(fileJSON.getInteger("start_ms"));
			}
			if (fileJSON.containsKey("duration")){
				fileObjectBO.setDuration(fileJSON.getInteger("duration"));
			}
			if (fileJSON.containsKey("url")){
				fileObjectBO.setUrl(fileJSON.getString("url"));
			}else{
				throw new BaseException(StatusCode.FORBIDDEN,"file url not found");
			}
			inputFileObjectBOS.add(fileObjectBO);
		}
		this.file_array  = inputFileObjectBOS;
	}

}
