package com.sumavision.tetris.cs.area.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class ChannelTerminalAreaAlreadyUseException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger LOG = LoggerFactory.getLogger(ChannelTerminalAreaAlreadyUseException.class);
	
	public ChannelTerminalAreaAlreadyUseException(String info) {
		super(StatusCode.CONFLICT, info.toString());
	}
}
