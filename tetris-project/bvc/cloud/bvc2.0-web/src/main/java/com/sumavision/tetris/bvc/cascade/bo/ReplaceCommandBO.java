package com.sumavision.tetris.bvc.cascade.bo;

/**
 * 接替指挥<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午1:55:53
 */
public class ReplaceCommandBO {

	/** 操作成员id */
	private String op;
	
	/** 接替成员id */
	private String targid;

	public String getOp() {
		return op;
	}

	public ReplaceCommandBO setOp(String op) {
		this.op = op;
		return this;
	}

	public String getTargid() {
		return targid;
	}

	public ReplaceCommandBO setTargid(String targid) {
		this.targid = targid;
		return this;
	}
	
}
