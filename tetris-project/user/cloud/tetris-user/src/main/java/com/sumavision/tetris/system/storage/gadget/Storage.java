package com.sumavision.tetris.system.storage.gadget;

/**
 * 存储信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 下午3:17:57
 */
public class Storage {

	/** 存储间总大小，单位：B */
	private long mountSpaceTotalSize;
	
	/** 存储空间剩余大小，单位：B */
	private long mountSpaceFreeSize;

	public long getMountSpaceTotalSize() {
		return mountSpaceTotalSize;
	}

	public Storage setMountSpaceTotalSize(long mountSpaceTotalSize) {
		this.mountSpaceTotalSize = mountSpaceTotalSize;
		return this;
	}

	public long getMountSpaceFreeSize() {
		return mountSpaceFreeSize;
	}

	public Storage setMountSpaceFreeSize(long mountSpaceFreeSize) {
		this.mountSpaceFreeSize = mountSpaceFreeSize;
		return this;
	}
	
}
