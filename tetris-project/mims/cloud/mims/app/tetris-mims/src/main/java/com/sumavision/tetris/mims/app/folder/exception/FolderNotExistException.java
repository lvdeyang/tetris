package com.sumavision.tetris.mims.app.folder.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 根据id未查询到文件夹<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 下午3:50:19
 */
public class FolderNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public FolderNotExistException(Long folderId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("文件夹(")
															 .append(folderId)
															 .append(")已经不存在！")
															 .toString());
	}
	
	public FolderNotExistException(String message){
		super(StatusCode.FORBIDDEN, message);
	}
	
}
