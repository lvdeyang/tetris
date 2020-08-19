package com.suma.venus.resource.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.externalinterface.InterfaceToResource;

/**
 * 处理资源相关操作的消息请求
 * @author lxw
 *
 */
@Component
public class ProcessResourceMsg {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessResourceMsg.class);
	
	@Autowired
	private InterfaceToResource interfaceToResource;
	
	@Autowired
	private WriteRespMsg writeRespMsg;
    
}
