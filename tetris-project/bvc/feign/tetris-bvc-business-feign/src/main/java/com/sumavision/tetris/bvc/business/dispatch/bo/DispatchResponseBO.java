package com.sumavision.tetris.bvc.business.dispatch.bo;

public class DispatchResponseBO {

	private String taskId = "";

	private String result = "success";//fail
	
	private int code = 200;
	
	private Long dispatchId;
	
	private String errMsg;
	
	public String getTaskId() {
		return taskId;
	}

	public DispatchResponseBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getResult() {
		return result;
	}

	public DispatchResponseBO setResult(String result) {
		this.result = result;
		return this;
	}

	public int getCode() {
		return code;
	}

	public DispatchResponseBO setCode(int code) {
		this.code = code;
		return this;
	}

	public Long getDispatchId() {
		return dispatchId;
	}

	public DispatchResponseBO setDispatchId(Long dispatchId) {
		this.dispatchId = dispatchId;
		return this;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public DispatchResponseBO setErrMsg(String errMsg) {
		this.errMsg = errMsg;
		return this;
	}

}
