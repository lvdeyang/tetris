package com.suma.venus.resource.lianwang.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.resource.lianwang.status.NotifyUserDeviceXML;
import com.suma.venus.resource.util.XMLBeanUtils;


@Component
public class AuthXmlUtil {
	
	@Autowired
	private MessageService messageService;
	
	//发送授权信息消息
	public void sendAuthNotifyXmlMsg(String layerId,String msg){
		messageService.msgSend2SingleNode(layerId, msg);
	}
	
	//构造整包发送的authnotify 消息
	public JSONObject createWholeAuthNotifyMessage(String xml,String layerId) {
		JSONObject contentJson = new JSONObject();
		contentJson.put("method", "message");
		contentJson.put("content-type", "application/command+xml");
		contentJson.put("resources", xml);
		JSONObject passbyJson = new JSONObject();
		passbyJson.put("bundle_id", "");
		passbyJson.put("layer_id", layerId);
		passbyJson.put("pass_by_content", contentJson);
		JSONObject msgBodyJson = new JSONObject();
		msgBodyJson.put("pass_by", passbyJson);
		
		JSONObject msgHeaderJson = new JSONObject();
		msgHeaderJson.put("destination_id", layerId);
		msgHeaderJson.put("message_name", "passby");
		msgHeaderJson.put("message_type", "passby");
		msgHeaderJson.put("sequence_id", UUID.randomUUID().toString());
		msgHeaderJson.put("source_id", "suma-venus-resource");
		
		JSONObject messageJson = new JSONObject();
		messageJson.put("message_header", msgHeaderJson);
		messageJson.put("message_body", msgBodyJson);
		
		JSONObject json = new JSONObject();
		json.put("message", messageJson);
		return json;
	}
	
	public JSONObject createAuthNotifyMessage(String dstUser, String xml,String layerId) {
		
		JSONObject content = new JSONObject();
		content.put("cmd", "send_node_message");
		content.put("type", "sync");
		content.put("commandname", "authnotify");
		content.put("dst_no", dstUser);
		content.put("content", xml);

		JSONObject passbyJson = new JSONObject();
		passbyJson.put("bundle_id", "");
		passbyJson.put("layer_id", layerId);
		passbyJson.put("pass_by_content", content);
		JSONObject msgBodyJson = new JSONObject();
		msgBodyJson.put("pass_by", passbyJson);

		return msgBodyJson;
	}

}
