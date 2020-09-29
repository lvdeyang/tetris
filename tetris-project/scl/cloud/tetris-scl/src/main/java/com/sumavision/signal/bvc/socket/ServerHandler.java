package com.sumavision.signal.bvc.socket;

import javax.annotation.PostConstruct;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.sumavision.signal.bvc.common.SpringBeanFactory;
import org.apache.catalina.core.ApplicationContext;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.fifthg.bo.socket.FifthgSocketBO;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.signal.bvc.feign.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ServerHandler extends IoHandlerAdapter {

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	
	/* 在本类中调用其他service需要使用以下方法
	 * 
	 * */
	
	//1.声明service类
	/*@Autowired
	private TbBoxService tbBoxService;*/
	@Autowired
	FifthGenerationKnapsackFeign fifthGenerationKnapsackFeign;
    private static ServerHandler serverHandler ;

    
    //2通过@PostConstruct实现初始化bean之前进行的操作
//    @PostConstruct
    public void init() { 
        serverHandler = this; 
        serverHandler.fifthGenerationKnapsackFeign=this.fifthGenerationKnapsackFeign;
        //serverHandler.tbBoxService = this.tbBoxService;.
        //3.调用时需要加前缀 如 serverHandler.tbBoxService
    }  
    
	public void sessionCreated(IoSession session) throws Exception { //用户连接到服务器
		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig(); 
		cfg.setSoLinger(0);
		LOGGER.info("[服务建立]" + session.getId());
		
	}
	

	
    public void messageReceived(IoSession session, Object message)throws Exception {//接收消息
    	IoBuffer bbuf = (IoBuffer) message;
    	byte[] byten = new byte[bbuf.limit()];
    	bbuf.get(byten, bbuf.position(), bbuf.limit());
        String temp=new String(byten);
        temp = temp.trim();
		LOGGER.info("[收到消息]" + session.getId() + ",消息内容：" + temp.replaceAll("\r|\n|\t",""));
        if(temp.contains("sn")){

			Pattern pattern =Pattern.compile("\\{([\\s\\S]*)\\}");
			Matcher matcher = pattern.matcher(temp);
			if (matcher.find()){
				String tempSocket = "{" +matcher.group(1) +"}";
				//向资源上报设备上线
				FifthgSocketBO bo = JSONObject.parseObject(tempSocket, FifthgSocketBO.class);
				BundleDao bundleDao = SpringBeanFactory.getBean(BundleDao.class);
				BundlePO bundlePO = bundleDao.findByBundleId(bo.getSn());
				JSONObject portObj=serverHandler.fifthGenerationKnapsackFeign.doRegister(bo.getSn());
				if (bundlePO != null){
					bundlePO.setDeviceIp(bo.getIp());
					bundlePO.setOnlineStatus(BundlePO.ONLINE_STATUS.ONLINE);
					bundlePO.setBundleName("5G");
					bundleDao.save(bundlePO);
				}

				JSONObject retObj=new JSONObject();
				retObj.put("data", portObj.getInteger("data"));
				retObj.put("status", portObj.getInteger("status"));
				messageResponse(session, retObj.toString());


			}

        }
        
	}

	public void sessionClosed(IoSession session) throws Exception {   //用户从服务器断开
		LOGGER.info("[服务断开]" + session.getId());
    	
    	//向资源上报设备下线
    	
	}
    
    public void messageSent(IoSession session, Object message){ //发送消息结束
    	//logger.info("[发送消息结束]" + session.getId() + "message" + message);
    }
    
    public void sessionIdle(IoSession session, IdleStatus status)throws Exception {//重连
		LOGGER.info("[服务重连]" + session.getId() + "status" + status.toString());
    }
	
    public void exceptionCaught(IoSession session, Throwable cause)throws Exception {//连接发生异常
		cause.printStackTrace();
		LOGGER.info("[服务异常]" + session.getId());
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
