package com.sumavision.tetris.business.push.vo;

public class ScheduleInputVO {

	private PushProgramVO prev;
	
	private PushProgramVO next;

	public PushProgramVO getPrev() {
		return prev;
	}

	public void setPrev(PushProgramVO prev) {
		this.prev = prev;
	}

	public PushProgramVO getNext() {
		return next;
	}

	public void setNext(PushProgramVO next) {
		this.next = next;
	}
	
}
