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
	
	/**
	 * 登录用户websocket连接地址<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:11:10
	 * @return String 登录用户websocket连接地址
	 */
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
		System.out.println(addr);
		return addr;
	}
	
	/**
	 * 游客websocket连接地址<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:10:32
	 * @return String 游客websocket连接地址
	 */
	public String touristAddr() throws Exception{
		String response = eurekaSdk.findByAppId(applicationConfig.getId());
		
		XMLReader reader = new XMLReader(response);
		List<Node> instances = reader.readNodeList("application.instance");
		Random random =new Random();
		int index = random.nextInt(instances.size());
		Node target = instances.get(index);
		
		String ip = reader.readString("ipAddr", target);
		String port = reader.readString("port", target);
		String addr = new StringBufferWrapper().append("ws://").append(ip).append(":").append(port).append("/tourist/server/websocket/").toString();
		System.out.println(addr);
		return addr;
	}
	
}
