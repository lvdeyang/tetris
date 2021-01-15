package com.sumavision.tetris.user.exception;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class NicknameAlreadyExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(NicknameAlreadyExistException.class);
	
	private static final long serialVersionUID = 1L;

	public NicknameAlreadyExistException(String nickname) {
		super(StatusCode.FORBIDDEN, "用户昵称已存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("用户昵称已经存在！")
										   .append(nickname)
										   .toString());
	}
	
	public NicknameAlreadyExistException(Collection<String> nicknames) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户昵称在系统中已存在！").append(nicknames.toString().replace("[", "").replace("]", "")).toString());
	}

}
