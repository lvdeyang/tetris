package com.sumavision.bvc.device.group.bo;

/**
 * @ClassName: 描述屏幕内源的裁剪和布局<br> 
 * @author lvdeyang
 * @date 2018年9月19日 上午8:20:01 
 */
public class DisplayBO {
	
	/** 屏幕区域id */
	private String rect_id;

	/** 描述源的裁剪 */
	private RectBO src_cut;
	
	/** 描述该屏幕的布局 */
	private RectBO display_rect;

	public String getRect_id() {
		return rect_id;
	}

	public DisplayBO setRect_id(String rect_id) {
		this.rect_id = rect_id;
		return this;
	}

	public RectBO getSrc_cut() {
		return src_cut;
	}

	public DisplayBO setSrc_cut(RectBO src_cut) {
		this.src_cut = src_cut;
		return this;
	}

	public RectBO getDisplay_rect() {
		return display_rect;
	}

	public DisplayBO setDisplay_rect(RectBO display_rect) {
		this.display_rect = display_rect;
		return this;
	}
	
}
