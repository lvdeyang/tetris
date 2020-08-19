package com.sumavision.signal.bvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.VenusMessageHead;
import com.suma.venus.message.mq.VenusMessage;
import com.suma.venus.message.service.MessageService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MQSendService {
	
	private static Long seq = 0l;

	@Autowired
	private MessageService messageService;
	
	/** pullDataRequest */
	public void pullDataRequestMessage(String dstId, String srcId, String srcBundleId, String srcChannelId,
			String dstBundleId, String dstChannelId, String ip, String port){
		
		String guolaiwan = "guolaiwan0805";
		
		VenusMessage msg = new VenusMessage();
		VenusMessageHead message_header = new VenusMessageHead();
		JSONObject message_body = new JSONObject();
		
		message_header.setDestination_id(dstId);
		message_header.setMessage_type("request");
		message_header.setMessage_name("pullData");
		message_header.setSequence_id("");
		message_header.setSource_id(srcId);
		
		message_body.put("method", "pullData");
		message_body.put("seq", seq);
		message_body.put("peerBundleId", guolaiwan);
		message_body.put("peerChannelId", dstChannelId);
		message_body.put("localBundleId", srcBundleId);
		message_body.put("localChannelID", srcChannelId);
		message_body.put("peerEndpoint", "0.0.0.0:" + port);
		message_body.put("peerReflectEndpoint", ip + ":" + port);
		
		msg.setMessage_header(message_header);
		msg.setMessage_body(message_body);
		
		JSONObject message = new JSONObject();
		message.put("message", msg);
		
		System.out.println("++++++++ request ++++++++:" + JSONObject.toJSONString(message));
		
		messageService.msgSend2SingleNode(dstId, JSONObject.toJSONString(message));
		
		seq++;
	}
	

	/** pullDataRequest */
	public void pullDataResponseMessage(String dstId, String srcId, String srcBundleId, String srcChannelId,
		String dstBundleId, String dstChannelId, String dstIp, String dstPort, String srcIp, String srcPort,  String seqString){		
		
		VenusMessage msg = new VenusMessage();
		VenusMessageHead message_header = new VenusMessageHead();
		JSONObject message_body = new JSONObject();
		
		message_header.setDestination_id(dstId);
		message_header.setMessage_type("response");
		message_header.setMessage_name("pullData");
		message_header.setSequence_id("");
		message_header.setSource_id(srcId);
		
		message_body.put("method", "pullData");
		message_body.put("seq", seqString);
		message_body.put("result", 1);
		message_body.put("peerBundleId", dstBundleId);
		message_body.put("peerChannelId", dstChannelId);
		message_body.put("localBundleId", srcBundleId);
		message_body.put("localChannelID", srcChannelId);
		message_body.put("peerEndpoint", dstIp);
		message_body.put("peerReflectEndpoint", dstPort);
		message_body.put("localEndpoint", srcIp);
		message_body.put("localReflectEndpoint", srcPort);
		
		msg.setMessage_header(message_header);
		msg.setMessage_body(message_body);
		
		JSONObject message = new JSONObject();
		message.put("message", msg);
		
		System.out.println("++++++++ response:" + JSONObject.toJSONString(message));
		
		messageService.msgSend2SingleNode(dstId, JSONObject.toJSONString(message));
	}
	
}
