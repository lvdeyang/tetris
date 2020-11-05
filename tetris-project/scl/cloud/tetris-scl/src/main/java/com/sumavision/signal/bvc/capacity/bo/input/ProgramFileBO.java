package com.sumavision.signal.bvc.capacity.bo.input;

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
	
}
