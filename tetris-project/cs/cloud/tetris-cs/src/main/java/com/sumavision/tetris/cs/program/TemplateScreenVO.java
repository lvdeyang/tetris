package com.sumavision.tetris.cs.program;

import java.util.List;

public class TemplateScreenVO {
	private Long no;
	private String width;
	private String height;
	private String top;
	private String left;
	private List<ScreenVO> data;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public List<ScreenVO> getData() {
		return data;
	}
	public void setData(List<ScreenVO> data) {
		this.data = data;
	}
}
