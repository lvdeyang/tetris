package com.sumavision.tetris.record.strategy;

import java.util.Date;

public class TimeSegmentBO {
	private Date start;
	private Date end;
	private String stgItemName;
	
	public TimeSegmentBO(Date start, Date end, String stgItemName){
		this.start = start;
		this.end = end;
		this.stgItemName = stgItemName;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getStgItemName() {
		return stgItemName;
	}

	public void setStgItemName(String stgItemName) {
		this.stgItemName = stgItemName;
	}
	
	
}
