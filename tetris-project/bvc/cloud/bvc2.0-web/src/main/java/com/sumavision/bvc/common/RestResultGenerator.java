package com.sumavision.bvc.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestResultGenerator {

	/**
	 * normal
	 * 
	 * @param success
	 * @param data
	 * @param message
	 * @param <T>
	 * @return
	 */
	public static <T> RestResult<T> genResult(boolean success, T data, String message) {
		RestResult<T> result = RestResult.newInstance();
		result.setResult(success);
		result.setData(data);
		result.setMessage(message);
		if (log.isDebugEnabled()) {
			log.debug("generate rest result:{}", result);
		}
		return result;
	}

	/**
	 * success
	 * 
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> RestResult<T> genSuccessResult(T data) {

		return genResult(true, data, null);
	}

	/**
	 * error message
	 * 
	 * @param message
	 *            error message
	 * @param <T>
	 * @return
	 */
	public static <T> RestResult<T> genErrorResult(String message) {

		return genResult(false, null, message);
	}

	/**
	 * error
	 * 
	 * @param error
	 *            error enum
	 * @param <T>
	 * @return
	 */
	public static <T> RestResult<T> genErrorResult(ErrorCode error) {

		return genErrorResult(error.getMessage());
	}

	/**
	 * success no message
	 * 
	 * @return
	 */
	public static RestResult genSuccessResult() {
		return genSuccessResult(null);
	}
}
