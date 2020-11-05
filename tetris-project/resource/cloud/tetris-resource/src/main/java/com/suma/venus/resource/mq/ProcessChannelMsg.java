package com.suma.venus.resource.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.externalinterface.InterfaceToResource;

/**
 * 处理通道相关操作的消息请求
 * @author lxw
 *
 */
@Component
public class ProcessChannelMsg {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessChannelMsg.class);
	
	@Autowired
	private InterfaceToResource interfaceToResource;
	
	@Autowired
	private WriteRespMsg writeRespMsg;
	
}
