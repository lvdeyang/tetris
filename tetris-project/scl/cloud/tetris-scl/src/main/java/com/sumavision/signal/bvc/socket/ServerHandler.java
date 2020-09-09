package com.sumavision.signal.bvc.socket;

import javax.annotation.PostConstruct;

import org.apache.catalina.core.ApplicationContext;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.fifthg.bo.socket.FifthgSocketBO;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.signal.bvc.feign.*;
@Component
public class ServerHandler extends IoHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/* 在本类中调用其他service需要使用以下方法
	 * 
	 * */
	
	//1.声明service类
	/*@Autowired
	private TbBoxService tbBoxService;*/

	FifthGenerationKnapsackFeign fifthGenerationKnapsackFeign;
    private static ServerHandler serverHandler ;
    
    //2通过@PostConstruct实现初始化bean之前进行的操作
    @PostConstruct 
    public void init() { 
        serverHandler = this; 
        serverHandler.fifthGenerationKnapsackFeign=this.fifthGenerationKnapsackFeign;
        //serverHandler.tbBoxService = this.tbBoxService;.
        //3.调用时需要加前缀 如 serverHandler.tbBoxService
    }  
    
	@Override  
	public void sessionCreated(IoSession session) throws Exception { //用户连接到服务器
		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig(); 
		cfg.setSoLinger(0);
		logger.info("[服务建立]" + session.getId());
		
	}
	

	
    @Override
    public void messageReceived(IoSession session, Object message)throws Exception {//接收消息
    	IoBuffer bbuf = (IoBuffer) message;
    	byte[] byten = new byte[bbuf.limit()];
    	bbuf.get(byten, bbuf.position(), bbuf.limit());
        String temp=new String(byten);
        temp = temp.trim();
        logger.info("[收到消息]" + session.getId() + ",消息内容：" + temp);
        if(temp.contains("sn")){
        	
        	String tempSocket = "{"+temp.split("\\{")[1].split("\\}")[0]+"}";
        	
        	//向资源上报设备上线
        	FifthgSocketBO bo = JSONObject.parseObject(tempSocket, FifthgSocketBO.class);
        	JSONObject portObj=serverHandler.fifthGenerationKnapsackFeign.doRegister(bo.getSn());
        	JSONObject retObj=new JSONObject();
        	retObj.put("data", portObj.getInteger("data"));
        	retObj.put("status", portObj.getInteger("status"));
        	messageResponse(session, retObj.toString());
        }
        
	}

	@Override  
	public void sessionClosed(IoSession session) throws Exception {   //用户从服务器断开
    	logger.info("[服务断开]" + session.getId());
    	
    	//向资源上报设备下线
    	
	}
    
    @Override
    public void messageSent(IoSession session, Object message){ //发送消息结束
    	//logger.info("[发送消息结束]" + session.getId() + "message" + message);
    }
    
    @Override
    public void sessionIdle(IoSession session, IdleStatus status)throws Exception {//重连
    	logger.info("[服务重连]" + session.getId() + "status" + status.toString());
    }
	
	@Override
    public void exceptionCaught(IoSession session, Throwable cause)throws Exception {//连接发生异常
		cause.printStackTrace();
        logger.info("[服务异常]" + session.getId());
	}
	
	public void messageResponse(IoSession session, String message) throws Exception{
		//response
        session.write(IoBuffer.wrap(message.getBytes()));
	}
	
	public static void main(String[] args) throws Exception{
		
		String temp = "{ \"ip\": \"119.3.178.195\", \"name\": \"5G Encoder\", \"readCommunity\": \"public\", \"writeCommunity\": \"private\", \"webPort\": 10143, \"port\": 10144, \"sn\": \"RD115M19525\", \"onOff\": 1, \"vRate\": 15000000, \"sysRate\": 28000000 }";

		System.out.println(temp);
		
		String tempSocket1 = temp.split("\\{")[1];
		
		System.out.println(tempSocket1);
				
		String tempSocket = tempSocket1.split("\\}")[0];
		
		System.out.println(tempSocket);
    	
    	//向资源上报设备上线
    	FifthgSocketBO bo = JSONObject.parseObject(new StringBufferWrapper().append("{").append(tempSocket).append("}").toString(), FifthgSocketBO.class);
    	
    	System.out.println(bo);
		
	}
	
}
