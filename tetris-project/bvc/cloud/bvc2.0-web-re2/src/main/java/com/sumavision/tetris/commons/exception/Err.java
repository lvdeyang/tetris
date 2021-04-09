/**
 * Copyright (C) 2014 Sumavision
 *
 *
 * @className:platform.base.exception.Err
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:zhuzheng
 * 
 */
package com.sumavision.tetris.commons.exception;

import static com.sumavision.tetris.commons.context.SpringContext.i18n;

public class Err {
	  public static Err UNKOWN_ERROR = new Err("sys_10001");
	  public static Err PARSE_ERR = new Err("sys_10002", "解析失败");
	  public static Err SEND_TIME_OUT = new Err("com_10003","发送消息超时");
	  public static Err CHANGE_DIRECTORY_FAILED = new Err("file_10001", "打开目录失败");
	  
	  String errCode;
	  String errMsg;
	  
	  public Err(String errCode)
	  {
	    this.errCode = errCode;
	    this.errMsg = i18n(errCode, errCode);
	  }
	  
	  public Err(String errCode, String errMsg)
	  {
	    this.errCode = errCode;
	    String message = i18n(errCode, errCode);
	    this.errMsg = (message==null||message.isEmpty())?errMsg:message;
	  }

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}
	  

	  
	  
}
