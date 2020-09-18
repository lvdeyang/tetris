package com.sumavision.bvc.device.group.bo;

public class RecordDateTimeBO {
	
	private String start="start:";
	private String end="end:";
	
	public String getStart() {
		return start;
	}
	public RecordDateTimeBO setStart(String start) {
		this.start += start;
		return this;
	}
	public String getEnd() {
		return end;
	}
	public RecordDateTimeBO setEnd(String end) {
		this.end += end;
		return this;
	}
}
