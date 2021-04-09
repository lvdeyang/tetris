package com.sumavision.bvc.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private <T> RestResult<T> runtimeExceptionHandler(Exception e) {
		log.error("---------> huge error!", e);
		return RestResultGenerator.genErrorResult(ErrorCode.SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private <T> RestResult<T> illegalParamsExceptionHandler(MethodArgumentNotValidException e) {
		log.error("---------> invalid request!", e);
		return RestResultGenerator.genErrorResult(ErrorCode.ILLEGAL_PARAMS);
	}

}
