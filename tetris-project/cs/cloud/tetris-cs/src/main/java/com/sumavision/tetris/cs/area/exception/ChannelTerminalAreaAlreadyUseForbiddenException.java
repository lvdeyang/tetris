package com.sumavision.tetris.cs.area.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class ChannelTerminalAreaAlreadyUseForbiddenException extends BaseException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger LOG = LoggerFactory.getLogger(ChannelTerminalAreaAlreadyUseForbiddenException.class);
	
	public ChannelTerminalAreaAlreadyUseForbiddenException(String info) {
		super(StatusCode.FORBIDDEN, info.toString());
	}
}
