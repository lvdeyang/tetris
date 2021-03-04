package com.sumavision.tetris.config.server;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.spring.eureka.application.EurekaFeign.MemoryQuery;

@Component
public class UserServerPropsQuery {

	@Autowired
	private ServerProps serverProps;
	
	@Autowired
	private MemoryQuery memoryQuery;
	
	/**
	 * 中心查询用户服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:36
	 * @return ServerProps 服务属性
	 * @throws Exception 
	 */
	public ServerProps queryProps() throws Exception{
		
		String xmlString = memoryQuery.findAll();
		XMLReader reader = new XMLReader(xmlString); 
		String ip = null;
		String port  = null;
		List<Node> nodes = reader.readNodeList("applications.application");
		for (Node node : nodes){
			List<Node> nodesto = reader.readNodeList("application.instance",node);
			Random r = new Random();
			int i = r.nextInt(nodesto.size());
			Node instanceNode = nodesto.get(i);
			String name = reader.readString("application.name", node).toLowerCase();
			if(name.equalsIgnoreCase("TETRIS-USER")){
				ip = reader.readString("instance.hostName",instanceNode);
				port = reader.readString("instance.port",instanceNode);
			}
		}
		serverProps.setIp(ip);
		serverProps.setPort(port);
		
		return serverProps;
	}
	
}
