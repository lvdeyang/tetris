package com.suma.venus.resource.base.bo;

public class ErrorDescription {

	private Integer error_code;
	
	public ErrorDescription() {}

	public ErrorDescription(Integer error_code) {
		super();
		this.error_code = error_code;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}

	@Override
	public String toString() {
		return "ErrorDescription [error_code=" + error_code + "]";
	}
	
}
