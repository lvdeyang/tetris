package com.sumavision.tetris.websocket.core.load.balance;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Node;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.eureka.sdk.EurekaSdk;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.websocket.core.config.ApplicationConfig;

@Controller
@RequestMapping(value = "/websocket/server")
public class WebsocketServerController {

	@Autowired
	private EurekaSdk eurekaSdk;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/addr")
	public Object addr(HttpServletRequest request) throws Exception{
		
		String response = eurekaSdk.findByAppId(applicationConfig.getId());
		
		XMLReader reader = new XMLReader(response);
		List<Node> instances = reader.readNodeList("application.instance");
		Random random =new Random();
		int index = random.nextInt(instances.size());
		Node target = instances.get(index);
		
		String ip = reader.readString("ipAddr", target);
		String port = reader.readString("port", target);
		String addr = new StringBufferWrapper().append("ws://").append(ip).append(":").append(port).append("/server/").toString();
		return addr;
	}
	
}
