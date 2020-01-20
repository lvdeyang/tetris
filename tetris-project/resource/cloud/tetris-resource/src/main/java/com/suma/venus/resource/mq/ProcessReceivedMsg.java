package com.suma.venus.resource.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.RequestBaseBO;
import com.suma.venus.message.bo.VenusMessageHead.MsgType;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.resource.dao.MQMessageDao;
import com.suma.venus.resource.externalinterface.InterfaceToResource;
import com.suma.venus.resource.pojo.MQMessagePO;

/**
 * 消息处理类，处理接收到的activemq消息
 *
 * @author lxw 2018年6月12日
 */
@Component
public class ProcessReceivedMsg {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessReceivedMsg.class);
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private InterfaceToResource interfaceToResource;
    
    @Autowired
    private WriteRespMsg writeRespMsg;
    
    @Autowired
    private ProcessBundleMsg processBundleMsg;
    
    @Autowired
    private ProcessLayerMsg processLayerMsg;
    
    @Autowired
    private MQMessageDao mqMessageDao;
    
    /**
     * 根据接口协议的数据类型解析msg中的方法名，根据相应方法名注入上层解析接口进行处理
     */
    public void process(String msg) throws Exception {
    	ResponseBO responseBo = JSONObject.parseObject(msg, ResponseBO.class);
    	String msgType = responseBo.getMessage().getMessage_header().getMessage_type();
        //如果能解析出methodName认为是请求消息，否则认为是回复消息
        if(MsgType.request.toString().equalsIgnoreCase(msgType) || MsgType.notification.toString().equalsIgnoreCase(msgType) 
        		|| MsgType.alert.toString().equalsIgnoreCase(msgType) || MsgType.passby.toString().equalsIgnoreCase(msgType)){
            RequestBaseBO requestBase = JSONObject.parseObject(msg, RequestBaseBO.class);
            switch (requestBase.getMessage().getMessage_header().getMessage_name()) {
//            case MQConstant.BUNDLE_STATUS_NOTIFY:
//            	processBundleMsg.updateBundleStatus(msg);
//            	break;
            default:
                break;
            }
        }else if(MsgType.response.toString().equalsIgnoreCase(msgType)){
            //处理回复消息
            processRespMsg(msg);
        }
    }

    /**处理收到的响应类消息*/
    private void processRespMsg(String textMessage) {
    	LOGGER.info("receive response message : " + textMessage);
        ResponseBO responseBo = JSONObject.parseObject(textMessage, ResponseBO.class);
        String requestId = responseBo.getMessage().getMessage_header().getSequence_id();
        
//      if(null != requestId){
//      	MessageIds.messageInfoMap.put(requestId, responseBo);
//      }
        
        MQMessagePO mqMessagePO = new MQMessagePO();
        mqMessagePO.setRequestId(requestId);
        mqMessagePO.setTextMessage(textMessage);
        mqMessageDao.save(mqMessagePO);
        
        synchronized (requestId.intern()) {
			requestId.intern().notify();
		}

    }
    
}
