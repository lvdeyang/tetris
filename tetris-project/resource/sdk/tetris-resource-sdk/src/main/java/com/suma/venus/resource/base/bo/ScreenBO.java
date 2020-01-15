package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 屏信息BO
 * @author lxw
 *
 */
public class ScreenBO {

	private String screen_id;
	
	private List<RectBO> rects;

	public String getScreen_id() {
		return screen_id;
	}

	public void setScreen_id(String screen_id) {
		this.screen_id = screen_id;
	}

	public List<RectBO> getRects() {
		return rects;
	}

	public void setRects(List<RectBO> rects) {
		this.rects = rects;
	}
	
}
