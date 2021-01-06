package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class ProgramFileBO {

	private Integer start_ms;
	
	private Integer duration;
	
	private String start_time;

	public Integer getStart_ms() {
		return start_ms;
	}

	public ProgramFileBO setStart_ms(Integer start_ms) {
		this.start_ms = start_ms;
		return this;
	}

	public Integer getDuration() {
		return duration;
	}

	public ProgramFileBO setDuration(Integer duration) {
		this.duration = duration;
		return this;
	}

	public String getStart_time() {
		return start_time;
	}

	public ProgramFileBO setStart_time(String start_time) {
		this.start_time = start_time;
		return this;
	}

	public ProgramFileBO() {
	}

	public ProgramFileBO(JSONObject fileObj) throws BaseException {
		if (fileObj.containsKey("start_time")){
			this.start_time = fileObj.getString("start_time");
		}else if (fileObj.containsKey("startTime")){
			this.start_time = fileObj.getString("startTime");
		}
		if (fileObj.containsKey("duration")){
			this.duration=fileObj.getInteger("duration");
		}else {
			throw new BaseException(StatusCode.FORBIDDEN,"transform param duration not found");
		}
		if (fileObj.containsKey("start_ms")){
			this.start_ms=fileObj.getInteger("start_ms");
		}else if (fileObj.containsKey("seek")){
			this.start_ms=fileObj.getInteger("seek");
		}else if (fileObj.containsKey("startMs")){
			this.start_ms=fileObj.getInteger("startMs");
		}else {
			throw new BaseException(StatusCode.FORBIDDEN,"transform param start_ms(seek) not found");
		}
	}
}
