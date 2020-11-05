package com.sumavision.tetris.bvc.cascade.bo;

/**
 * 授权指挥<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午1:50:40
 */
public class AuthCommandBO {

	/** 操作成员id */
	private String op;
	
	/** 被授权成员id */
	private String accepauthid;
	
	/** 这个字段不是很好理解 */
	private String cmdedid;

	public String getOp() {
		return op;
	}

	public AuthCommandBO setOp(String op) {
		this.op = op;
		return this;
	}

	public String getAccepauthid() {
		return accepauthid;
	}

	public AuthCommandBO setAccepauthid(String accepauthid) {
		this.accepauthid = accepauthid;
		return this;
	}

	public String getCmdedid() {
		return cmdedid;
	}

	public AuthCommandBO setCmdedid(String cmdedid) {
		this.cmdedid = cmdedid;
		return this;
	}
	
}
