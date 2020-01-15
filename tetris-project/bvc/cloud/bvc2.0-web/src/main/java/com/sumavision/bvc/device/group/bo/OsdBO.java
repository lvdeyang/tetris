package com.sumavision.bvc.device.group.bo;

public class OsdBO {

	/** 文本内容 */
	private String content;
	
	/** 字体 */
	private String font = "黑体";
	
	/** 字体大小 1到100 */
	private int height = 20;
	
	/** 字体颜色 */
	private String color = String.valueOf(Integer.parseInt("ffffff", 16));
	
	/** 横坐标万分比分子 */
	private int x;
	
	/** 纵坐标万分比分子 */
	private int y;
	
	/** 图层顺序 */
	private int index = 0;
	
	/** 是否展示 */
	private int show = 1;

	public String getContent() {
		return content;
	}

	public OsdBO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getFont() {
		return font;
	}

	public OsdBO setFont(String font) {
		this.font = font;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public OsdBO setHeight(int height) {
		this.height = height;
		return this;
	}

	public String getColor() {
		return color;
	}

	public OsdBO setColor(String color) {
		this.color = color;
		return this;
	}

	public int getX() {
		return x;
	}

	public OsdBO setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public OsdBO setY(int y) {
		this.y = y;
		return this;
	}

	public int getIndex() {
		return index;
	}

	public OsdBO setIndex(int index) {
		this.index = index;
		return this;
	}

	public int getShow() {
		return show;
	}

	public OsdBO setShow(int show) {
		this.show = show;
		return this;
	}
	
}
