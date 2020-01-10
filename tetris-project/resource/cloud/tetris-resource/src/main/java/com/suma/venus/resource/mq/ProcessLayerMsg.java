package com.suma.venus.resource.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.externalinterface.InterfaceToResource;

@Component
public class ProcessLayerMsg {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessLayerMsg.class);
	
	@Autowired
	private InterfaceToResource interfaceToResource;

	@Autowired
	private WriteRespMsg writeRespMsg;
	
}
