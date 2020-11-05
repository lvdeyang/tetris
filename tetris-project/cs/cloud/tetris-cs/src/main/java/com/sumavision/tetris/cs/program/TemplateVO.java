package com.sumavision.tetris.cs.program;

import java.util.List;

public class TemplateVO {
	private Long id;
	private String name;
	private Long screenNum;
	private List<TemplateScreenVO> screen;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getScreenNum() {
		return screenNum;
	}
	public void setScreenNum(Long screenNum) {
		this.screenNum = screenNum;
	}
	public List<TemplateScreenVO> getScreen() {
		return screen;
	}
	public void setScreen(List<TemplateScreenVO> screen) {
		this.screen = screen;
	}
}
