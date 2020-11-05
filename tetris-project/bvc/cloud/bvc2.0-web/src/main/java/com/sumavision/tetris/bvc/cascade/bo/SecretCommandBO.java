package com.sumavision.tetris.bvc.cascade.bo;

/**
 * 专向指挥<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午1:58:51
 */
public class SecretCommandBO {

	/** 上级成员id */
	private String upid;
	
	/** 下级成员id */
	private String downid;

	public String getUpid() {
		return upid;
	}

	public SecretCommandBO setUpid(String upid) {
		this.upid = upid;
		return this;
	}

	public String getDownid() {
		return downid;
	}

	public SecretCommandBO setDownid(String downid) {
		this.downid = downid;
		return this;
	}
	
}
