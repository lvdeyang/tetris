package com.suma.venus.resource.base.bo;


/**
 * 响应消息体
 * @author lxw
 *
 */
public class ResponseBody {

	public static final String SUCCESS = "success";
	
	public static final String FAIL = "fail";
	
	protected String result;
	
	protected ErrorDescription error_description;
	
	public ResponseBody(){}

	public ResponseBody(String result) {
		this.result = result;
	}
	
	public ResponseBody(String result, Integer errorCode) {
		this.result = result;
		this.error_description = new ErrorDescription(errorCode);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ErrorDescription getError_description() {
		return error_description;
	}

	public void setError_description(ErrorDescription error_description) {
		this.error_description = error_description;
	}
	
}
