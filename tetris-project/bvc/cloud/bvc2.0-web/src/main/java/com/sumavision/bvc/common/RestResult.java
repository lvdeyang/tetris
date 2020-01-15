package com.sumavision.bvc.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T> {
	private boolean result;

	private String message;

	private T data;

	private RestResult() {
	}

	public static <T> RestResult<T> newInstance() {
		return new RestResult<>();
	}

	@Override
	public String toString() {
		return "RestResult{" + "result=" + result + ", message='" + message + '\'' + ", data=" + data + '}';
	}
}
