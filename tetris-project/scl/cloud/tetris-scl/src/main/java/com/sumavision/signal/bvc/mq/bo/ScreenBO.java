package com.sumavision.signal.bvc.mq.bo;

import java.util.List;

public class ScreenBO {

	private String screen_id;
	
	private String screen_status;
	
	private List<RectBO> rects;

	public String getScreen_id() {
		return screen_id;
	}

	public void setScreen_id(String screen_id) {
		this.screen_id = screen_id;
	}

	public String getScreen_status() {
		return screen_status;
	}

	public void setScreen_status(String screen_status) {
		this.screen_status = screen_status;
	}

	public List<RectBO> getRects() {
		return rects;
	}

	public void setRects(List<RectBO> rects) {
		this.rects = rects;
	}
}
