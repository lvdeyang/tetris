package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class TaskCutBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3673660390656538542L;
	
	private Integer width;
	private Integer height;
	private Integer x;
	private Integer y;
	
	
	public TaskCutBO(Integer width, Integer height, Integer x, Integer y) {
		super();
		this.width = width;
		this.height = height;
		this.x = x;
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
	
}
