package com.sumavision.tetris.business.director.vo;

/**
 * 图片参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月20日 上午8:54:27
 */
public class PictureVO {

	/** 图片路径，应该包含文件名称和后缀 */
	private String path;
	
	private Integer x = 0;
	
	private Integer y = 0;
	
	private Integer width = 0;
	
	private Integer height = 0;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
}
