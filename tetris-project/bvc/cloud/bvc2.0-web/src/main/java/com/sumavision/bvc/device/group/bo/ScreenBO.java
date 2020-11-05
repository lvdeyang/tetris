package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * @ClassName: 描述屏幕 <br/>
 * @author lvdeyang
 * @date 2018年9月18日 下午6:45:58 
 */
public class ScreenBO {

	/** 屏幕的序号 */
	private String id;

	/** 迭代三正式使用的屏幕的序号。上边参数在逻辑层也会被转换成screen_id */
	private String screen_id;
	
	private String screen_status = "Open";
	
	/** 屏幕的显示 */
	private DisplayBO display;
	
	/** 描述屏幕覆盖 */
	private List<RectBO> overlaps;
	
	/** 迭代三正式使用的。上边的2个参数在逻辑层也会被转换成rects */
	private List<RectBO> rects;

	public String getId() {
		return id;
	}

	public ScreenBO setId(String id) {
		this.id = id;
		return this;
	}

	public String getScreen_status() {
		return screen_status;
	}

	public ScreenBO setScreen_status(String screen_status) {
		this.screen_status = screen_status;
		return this;
	}

	public DisplayBO getDisplay() {
		return display;
	}

	public ScreenBO setDisplay(DisplayBO display) {
		this.display = display;
		return this;
	}

	public List<RectBO> getOverlaps() {
		return overlaps;
	}

	public ScreenBO setOverlaps(List<RectBO> overlaps) {
		this.overlaps = overlaps;
		return this;
	}
	
	public String getScreen_id() {
		return screen_id;
	}

	public ScreenBO setScreen_id(String screen_id) {
		this.screen_id = screen_id;
		return this;
	}

	public List<RectBO> getRects() {
		return rects;
	}

	public ScreenBO setRects(List<RectBO> rects) {
		this.rects = rects;
		return this;
	}

	/**
	 * @Title: 单屏布局<br/> 
	 * @return ScreenBO
	 */
	public static ScreenBO SINGLE(String rectId){
		ScreenBO screen = new ScreenBO();
		if(rectId != null){
			screen.setDisplay(new DisplayBO().setRect_id(rectId)
				  .setDisplay_rect(new RectBO().setX(0)
											   .setY(0)
											   .setWidth(10000)
											   .setHeight(10000)
											   .setZ_index(1)))
				  .setOverlaps(new ArrayList<RectBO>());
		}		
		return screen;
	}
	
	/**
	 * @Title: 大屏远端1，小屏本地<br/> 
	 * @param channelId 本地覆盖的通道id
	 * @return ScreenBO 
	 */
	public static ScreenBO REMOTE_LARGE(String channelId, String rectId1, String rectId2){		
		ScreenBO screen = new ScreenBO();		
		if(rectId1 != null){	
			screen.setDisplay(new DisplayBO().setRect_id(rectId1)
											 .setDisplay_rect(new RectBO().setX(0)
													 					  .setY(0)
													 					  .setWidth(10000)
													 					  .setHeight(10000)
													 					  .setZ_index(1)));
		}
		if(channelId != null && rectId2 != null){
			screen.setOverlaps(new ArrayListWrapper<RectBO>().add(new RectBO().setRect_id(rectId2)
																			  .setX(6667)
																			  .setY(6667)
																			  .setWidth(3333)
																			  .setHeight(3333)
																			  .setChannel_id(channelId)
																			  .setZ_index(2)
																	  		  .setType("single"))
															 .getList());
		}
		return screen;
	}
	
	/**
	 * @Title: 大屏本地，小屏远端1 <br/>
	 * @param channelId 本地覆盖的通道id
	 * @return ScreenBO
	 */
	public static ScreenBO REMOTE_SMALL(String channelId, String rectId1, String rectId2){
		ScreenBO screen = new ScreenBO();
		if(rectId1 != null){	
			screen.setDisplay(new DisplayBO().setRect_id(rectId1)
										     .setDisplay_rect(new RectBO().setX(6667)
																	      .setY(6667)
																	      .setWidth(3333)
																	      .setHeight(3333)
																	      .setZ_index(2)));											 
		}
		if(channelId != null && rectId2 != null){
			screen.setOverlaps(new ArrayListWrapper<RectBO>().add(new RectBO().setRect_id(rectId2)
																			  .setX(0)
																			  .setY(0)
																			  .setWidth(10000)
																			  .setHeight(10000)
																			  .setChannel_id(channelId)
																			  .setZ_index(1)
																			  .setType("single"))
														     .getList());
		}
		return screen;
	}
	
	/**
	 * @Title: ppt模式远端1，小屏<br/>
	 * @return ScreenBO
	 */
	public static ScreenBO PPT_REMOTE1(String rectId){
		ScreenBO screen = new ScreenBO();
		if(rectId != null){
			screen.setDisplay(new DisplayBO().setRect_id(rectId)
				   .setDisplay_rect(new RectBO().setX(6667)
												.setY(6667)
												.setWidth(3333)
												.setHeight(3333)
												.setZ_index(2)));
		}		
		return screen;
	}
	
	/**
	 * @Title: ppt模式远端2，大屏<br/> 
	 * @return ScreenBO
	 */
	public static ScreenBO PPT_REMOTE2(String rectId){
		ScreenBO screen = new ScreenBO();
		if(rectId != null){
			screen.setDisplay(new DisplayBO().setRect_id(rectId)
				   .setDisplay_rect(new RectBO().setX(0)
											    .setY(0)
											    .setWidth(10000)
											    .setHeight(10000)
											    .setZ_index(1)));
		}
		return screen;
	}
	
}