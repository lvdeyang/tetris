package com.sumavision.tetris.mims.app.folder.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

/**
 * 当操作的文件夹类型不是预期类型时抛此异常<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月23日 上午10:29:32
 */
public class FolderTypeCannotMatchException extends BaseException{

	private static final long serialVersionUID = 1L;

	/** 不是素材库文件夹提示信息 */
	public static final int PERSONAL = 0;
	
	/** 不是媒资库文件夹提示信息 */
	public static final int GROUP = 1;
	
	/** 不是共享库文件夹提示信息 */
	public static final int SHARE = 2;
	
	public FolderTypeCannotMatchException(int code) {
		super(StatusCode.ERROR, getMessage(code));
	}
	
	public static String getMessage(int code){
		String message = "";
		switch (code) {
			case PERSONAL:
				message = "当前文件夹不是素材库文件夹，无法操作";
				break;
			case GROUP:
				message = "当前文件夹不是媒资库文件夹，无法操作";
				break;
			case SHARE:
				message = "当前文件夹不是共享库文件夹，无法操作";
				break;
		}
		return message;
	}
	
}
