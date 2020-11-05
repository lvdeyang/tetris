package com.sumavision.tetris.bvc.business.dispatch.bo;

public class DispatchResponseBodyBO {

	private String taskId = "";

	private String result = "success";//fail
	
	private int code = 200;
	
	private Long dispatchId;
	
	private String errMsg;
	
	public String getTaskId() {
		return taskId;
	}

	public DispatchResponseBodyBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getResult() {
		return result;
	}

	public DispatchResponseBodyBO setResult(String result) {
		this.result = result;
		return this;
	}

	public int getCode() {
		return code;
	}

	public DispatchResponseBodyBO setCode(int code) {
		this.code = code;
		return this;
	}

	public Long getDispatchId() {
		return dispatchId;
	}

	public DispatchResponseBodyBO setDispatchId(Long dispatchId) {
		this.dispatchId = dispatchId;
		return this;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public DispatchResponseBodyBO setErrMsg(String errMsg) {
		this.errMsg = errMsg;
		return this;
	}

}
