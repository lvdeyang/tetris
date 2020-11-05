package com.sumavision.tetris.system.storage.gadget;

/**
 * 目录信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 下午3:10:34
 */
public class Directory {

	/** 目录路径 */
	private String path;
	
	/** 目录占用空间大小，单位：B */
	private long size;

	public String getPath() {
		return path;
	}

	public Directory setPath(String path) {
		this.path = path;
		return this;
	}

	public long getSize() {
		return size;
	}

	public Directory setSize(long size) {
		this.size = size;
		return this;
	}
	
}
