package com.sumavision.tetris.cs.schedule.api.qt.response;

import java.util.List;

public class ApiQtScheduleScreenVO {
	/** 离屏幕顶端距离(百分比) */
	private String top;
	
	/** 离屏幕左端距离(百分比) */
	private String left;
	
	/** 占宽(百分比) */
	private String width;
	
	/** 占高(百分比) */
	private String height;
	
	/** 分屏位置 */
	private Long no;
	
	/** 分屏节目列表 */
	private List<ApiQtScheduleScreenProgramVO> program;

	public String getTop() {
		return top;
	}

	public ApiQtScheduleScreenVO setTop(String top) {
		this.top = top;
		return this;
	}

	public String getLeft() {
		return left;
	}

	public ApiQtScheduleScreenVO setLeft(String left) {
		this.left = left;
		return this;
	}

	public String getWidth() {
		return width;
	}

	public ApiQtScheduleScreenVO setWidth(String width) {
		this.width = width;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public ApiQtScheduleScreenVO setHeight(String height) {
		this.height = height;
		return this;
	}

	public Long getNo() {
		return no;
	}

	public ApiQtScheduleScreenVO setNo(Long no) {
		this.no = no;
		return this;
	}

	public List<ApiQtScheduleScreenProgramVO> getProgram() {
		return program;
	}

	public ApiQtScheduleScreenVO setProgram(List<ApiQtScheduleScreenProgramVO> program) {
		this.program = program;
		return this;
	}
}
