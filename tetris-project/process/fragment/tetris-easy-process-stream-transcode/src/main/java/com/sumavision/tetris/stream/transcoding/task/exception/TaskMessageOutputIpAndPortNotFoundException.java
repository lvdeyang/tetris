package com.sumavision.tetris.stream.transcoding.task.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TaskMessageOutputIpAndPortNotFoundException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(TaskMessageOutputIpAndPortNotFoundException.class);
	
	public TaskMessageOutputIpAndPortNotFoundException(String ip, String port){
		super(StatusCode.NOTFOUND, "输出任务查询失败");
		
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("查询输出任务:")
				.append("Ip")
				.append(ip)
				.append(",Port:")
				.append(port)
				.append("失败")
				.toString());
	}
}
