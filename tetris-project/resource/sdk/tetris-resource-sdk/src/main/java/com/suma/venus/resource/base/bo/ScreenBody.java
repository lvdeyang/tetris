package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 
 * @author lxw
 *
 */
public class ScreenBody {

	private String screen_id;
	
	private List<RectBody> rects;

	public String getScreen_id() {
		return screen_id;
	}

	public void setScreen_id(String screen_id) {
		this.screen_id = screen_id;
	}

	public List<RectBody> getRects() {
		return rects;
	}

	public void setRects(List<RectBody> rects) {
		this.rects = rects;
	}
	
}
