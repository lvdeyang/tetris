package com.sumavision.bvc.device.group.bo;

/**
 * @ClassName: 布局参数<br/> 
 * @author lvdeyang
 * @date 2018年9月18日 下午6:53:48 
 */
public class RectBO {

	/** 横偏移 0~10000 */
	private int x;
	
	/** 纵偏移 0~10000 */
	private int y;
	
	/** 宽 0~10000 */
	private int width;
	
	/** 高 0~10000 */
	private int height;
	
	/** 图层级别 0~100000 */
	private int z_index;
	
	/** 通道id */
	private String channel_id;
	
	/** 区域id */
	private String rect_id;
	
	/** 屏幕覆盖类型  */
	private String type = "single";

	public int getX() {
		return x;
	}

	public RectBO setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public RectBO setY(int y) {
		this.y = y;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public RectBO setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public RectBO setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getZ_index() {
		return z_index;
	}

	public RectBO setZ_index(int z_index) {
		this.z_index = z_index;
		return this;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public RectBO setChannel_id(String channel_id) {
		this.channel_id = channel_id;
		return this;
	}

	public String getRect_id() {
		return rect_id;
	}

	public RectBO setRect_id(String rect_id) {
		this.rect_id = rect_id;
		return this;
	}

	public String getType() {
		return type;
	}

	public RectBO setType(String type) {
		this.type = type;
		return this;
	}
	
}
