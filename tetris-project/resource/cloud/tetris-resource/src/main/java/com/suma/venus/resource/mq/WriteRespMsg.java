package com.suma.venus.resource.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.service.MessageService;

/**
 * 回复响应消息的封装类
 * @author lxw
 *
 */
@Component
public class WriteRespMsg {

	@Autowired
	private MessageService messageService;
	
	public void writeResp(String srcNodeId,ResponseBO resp){
		messageService.writeResp(srcNodeId, resp);
	}
	
}
