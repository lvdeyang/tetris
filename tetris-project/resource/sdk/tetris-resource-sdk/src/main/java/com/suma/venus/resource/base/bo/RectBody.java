package com.suma.venus.resource.base.bo;

/**
 * 
 * @author lxw
 *
 */
public class RectBody {

	private String rect_id;
	
	private String channel;
	
	private String type;
	
	private Integer x;
	
	private Integer y;
	
	private Integer height;
	
	private Integer width;
	
	private Integer z_index;
	
	private CutBody cut;

	public String getRect_id() {
		return rect_id;
	}

	public void setRect_id(String rect_id) {
		this.rect_id = rect_id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getZ_index() {
		return z_index;
	}

	public void setZ_index(Integer z_index) {
		this.z_index = z_index;
	}

	public CutBody getCut() {
		return cut;
	}

	public void setCut(CutBody cut) {
		this.cut = cut;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
