package com.sumavision.bvc.device.jv230.bo;

public class PositionBO {

	/** 万分比,范围0--10000 */
	private int x;
	
	/** 万分比,范围0--10000 */
	private int y;
	
	/** 万分比,范围0--10000 */
	private int width;
	
	/** 万分比,范围0--10000 */
	private int height;
	
	/** 层级--display需要 */
	private int z_index;

	public int getX() {
		return x;
	}

	public PositionBO setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public PositionBO setY(int y) {
		this.y = y;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public PositionBO setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public PositionBO setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getZ_index() {
		return z_index;
	}

	public PositionBO setZ_index(int z_index) {
		this.z_index = z_index;
		return this;
	}
	
	
}
