package com.sumavision.tetris.commons.exception.code;

/**
 * 业务状态码
 * lvdeyang 2017年6月23日
 */
public enum StatusCode {

	//成功
	SUCCESS(200),
	
	//服务器拒绝
	FORBIDDEN(403),
	
	//未找到资源
	NOTFOUND(404),
	
	//超时
	TIMEOUT(408),
	
	//冲突
	CONFLICT(409),
	
	//异常
	ERROR(500);
	
	private int code;
	
	private StatusCode(int code){
		this.code = code;
	}
	
	public int getCode(){
		return this.code;
	}
	
	@Override
	public String toString(){
		return String.valueOf(this.code);
	}
	
}
