package com.sumavision.tetris.cs.channel.broad.terminal.requestBO;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class RequestTerminalScheduleScreenBO {
	/** 分屏序号 */
	private Integer no;
	
	/** 横向缩放比例 */
	private String width;
	
	/** 纵向缩放比例 */
	private String height;
	
	/** 纵向位置 */
	private String top;
	
	/** 横向位置 */
	private String left;
	
	/** 排期表 */
	private List<RequestTerminalScheduleScreenProgramBO> program;

	public Integer getNo() {
		return no;
	}

	public RequestTerminalScheduleScreenBO setNo(Integer no) {
		this.no = no;
		return this;
	}

	public String getWidth() {
		return width;
	}

	public RequestTerminalScheduleScreenBO setWidth(String width) {
		this.width = width;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public RequestTerminalScheduleScreenBO setHeight(String height) {
		this.height = height;
		return this;
	}

	public String getTop() {
		return top;
	}

	public RequestTerminalScheduleScreenBO setTop(String top) {
		this.top = top;
		return this;
	}

	public String getLeft() {
		return left;
	}

	public RequestTerminalScheduleScreenBO setLeft(String left) {
		this.left = left;
		return this;
	}

	public List<RequestTerminalScheduleScreenProgramBO> getProgram() {
		return program;
	}

	public RequestTerminalScheduleScreenBO setProgram(List<RequestTerminalScheduleScreenProgramBO> program) {
		this.program = program;
		return this;
	}
	
	public RequestTerminalScheduleScreenBO setFromJson(JSONObject jsonObject) throws Exception {
		if (jsonObject.containsKey("no")) this.setNo(jsonObject.getInteger("no"));
		if (jsonObject.containsKey("width")) this.setWidth(jsonObject.getString("width"));
		if (jsonObject.containsKey("height")) this.setHeight(jsonObject.getString("height"));
		if (jsonObject.containsKey("top")) this.setTop(jsonObject.getString("top"));
		if (jsonObject.containsKey("left")) this.setLeft(jsonObject.getString("left"));
		return this;
	}
}
