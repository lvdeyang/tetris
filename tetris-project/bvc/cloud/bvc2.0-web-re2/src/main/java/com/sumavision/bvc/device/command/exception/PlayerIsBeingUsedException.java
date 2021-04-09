package com.sumavision.bvc.device.command.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

/**
 * 
* @ClassName: PlayerIsBeingUsedException 
* @Description: 当前台指定使用某个播放器，而该播放器已经被别的业务使用
* @author zsy
* @date 2020年1月2日 下午4:28:25 
*
 */
public class PlayerIsBeingUsedException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public PlayerIsBeingUsedException() {
		super(StatusCode.FORBIDDEN, "该播放器正在使用！");
	}

}
