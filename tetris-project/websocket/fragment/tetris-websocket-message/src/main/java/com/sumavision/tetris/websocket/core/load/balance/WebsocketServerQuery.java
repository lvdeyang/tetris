package com.sumavision.tetris.websocket.core.load.balance;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.eureka.sdk.EurekaSdk;
import com.sumavision.tetris.websocket.core.config.ApplicationConfig;

@Component
public class WebsocketServerQuery {

	@Autowired
	private EurekaSdk eurekaSdk;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	public String addr() throws Exception{
		
		String response = eurekaSdk.findByAppId(applicationConfig.getId());
		
		XMLReader reader = new XMLReader(response);
		List<Node> instances = reader.readNodeList("application.instance");
		Random random =new Random();
		int index = random.nextInt(instances.size());
		Node target = instances.get(index);
		
		String ip = reader.readString("ipAddr", target);
		String port = reader.readString("port", target);
		String addr = new StringBufferWrapper().append("ws://").append(ip).append(":").append(port).append("/server/websocket/").toString();
		return addr;
	}
	
}
