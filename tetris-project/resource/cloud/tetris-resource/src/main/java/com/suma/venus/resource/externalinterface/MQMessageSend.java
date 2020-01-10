package com.suma.venus.resource.externalinterface;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.RequestBaseBO;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.resource.dao.MQMessageDao;
import com.suma.venus.resource.pojo.MQMessagePO;

/**
 * 消息队列发送消息类，主要封装了同步发送消息的接口
 * 
 * @author lxw
 *
 */
@Service
public class MQMessageSend {

	private final static Logger LOGGER = LoggerFactory.getLogger(MQMessageSend.class);
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MQMessageDao mqMessageDao;
	
	/**
	 * 发送请求消息至单个节点并等待目标节点回复信息
	 * @param dstNodeId 目标节点
	 * @param requestBO 请求消息体
	 * @param timeOut 超时时间,单位毫秒
	 * @return resoonseBo 返回处理结果，包含处理的errMsg及信息
	 * @throws Exception
	 */
	public ResponseBO msgSendAndWaitResp(String dstNodeId,RequestBaseBO requestBO,long timeOut) throws Exception {
		String requestId = requestBO.getMessage().getMessage_header().getSequence_id();
		//消息投递到目标节点，即返回responses
		messageService.msgSend2SingleNode(dstNodeId, JSONObject.toJSONString(requestBO),timeOut);
				
	    /**获取消息requestId的锁，并休眠,等待收到requestId对应的消息时唤醒**/
	    synchronized (requestId.intern()) {
            //休眠，等待接收到该requestId对应的回复消息时唤醒,或等待超时唤醒
	    	requestId.intern().wait(timeOut);
	    	MQMessagePO mqMessagePO = mqMessageDao.findByRequestId(requestId);
	    	if(null == mqMessagePO){
	    		return null;
	    	}
	    	LOGGER.info("Current thread wake up ; MsgRequestId " + requestId + " is notified ; Response message is " + mqMessagePO.getTextMessage());
	        ResponseBO resp = JSONObject.parseObject(mqMessagePO.getTextMessage(), ResponseBO.class);
	        mqMessageDao.delete(mqMessagePO);
	        if(null != resp){
	        	resp.setResult(0);
	            return resp;
	        }
        }
			    
		return null;
	}
}
