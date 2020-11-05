package com.sumavision.tetris.mims.app.folder.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

/**
 * 用户对文件夹没有权限<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 下午3:26:55
 */
public class UserHasNoPermissionForFolderException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	/** 没有权限 */
	public static final int NOPERMISSION = -1;
	
	/** 当前文件夹 */
	public static final int CURRENT = 0;
	
	/** 在父文件夹下创建文件夹 */
	public static final int PARENT_CREATE = 1;
	
	public UserHasNoPermissionForFolderException(int code) {
		super(StatusCode.ERROR, getMessage(code));
	}
	
	private static String getMessage(int code){
		String message = null;
		switch (code) {
		case CURRENT:
			message = "您对当前文件夹没有权限";
			break;
		case PARENT_CREATE:
			message = "您没有权限在当前文件夹下创建文件夹";
			break;
		case NOPERMISSION:
			message = "您没有权限";
			break;
		}
		return message;
	}
	
}
