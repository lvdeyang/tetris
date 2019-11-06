package com.sumavision.tetris.stream.transcoding.task.exception;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TaskMessageIdNotFoundException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(TaskMessageIdNotFoundException.class);
	
	public TaskMessageIdNotFoundException(Long id){
		super(StatusCode.NOTFOUND, "任务id不存在");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("任务id:")
				.append(id)
				.append("检索失败")
				.toString());
	}
}
