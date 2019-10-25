package com.sumavision.tetris.websocket.message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.core.SessionQueue;
import com.sumavision.tetris.websocket.core.config.ApplicationConfig;
import com.sumavision.tetris.websocket.core.load.balance.SessionMetadataDAO;
import com.sumavision.tetris.websocket.core.load.balance.SessionMetadataPO;
import com.sumavision.tetris.websocket.message.exception.TargetUserNotFoundException;
import com.sumavision.tetris.websocket.message.exception.WebsocketMessageLoadBalacePostConsumeFailException;
import com.sumavision.tetris.websocket.message.exception.WebsocketMessageLoadBalacePostSendFailException;

@Service
@Transactional(rollbackFor = Exception.class)
public class WebsocketMessageService {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SessionMetadataDAO sessionMetadataDao;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private WebsocketMessageDAO websocketMessageDao;
	
	/**
	 * 处理离线消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:19:18
	 * @param Long userId 用户id
	 */
	@Deprecated
	public void offlineMessage(Long userId) throws Exception{
		List<WebsocketMessagePO> messages = websocketMessageDao.findByUserIdAndConsumed(userId, false);
		if(messages!=null && messages.size()>0){
			SessionQueue queue = SessionQueue.getInstance();
			Session session = queue.get(userId);
			for(WebsocketMessagePO message:messages){
				session.getBasicRemote().sendText(message.getMessage());
				message.setConsumed(true);
			}
			websocketMessageDao.save(messages);
		}
	} 
	
	/**
	 * 发送websocket消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:07:05
	 * @param Long userId 用户id
	 * @param String message 推送消息内容
	 * @param WebsocketMessageType type 消息类型
	 * @param Long fromUserId 消息来源用户id
	 * @param String fromUsername 消息来源用户名称
	 */
	public WebsocketMessageVO send(
			Long userId, 
			String message, 
			WebsocketMessageType type, 
			Long fromUserId,
			String fromUsername) throws Exception{
		
		SessionMetadataPO metadata = sessionMetadataDao.findByUserId(userId);
		List<UserVO> users = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList());
		if(users==null || users.size()<=0) throw new TargetUserNotFoundException();
		UserVO user = users.get(0);
		if(metadata == null){
			//离线消息
			WebsocketMessagePO messageEntity = new WebsocketMessagePO();
			messageEntity.setUserId(user.getId());
			messageEntity.setUsername(user.getNickname());
			messageEntity.setMessage(message);
			messageEntity.setMessageType(WebsocketMessageType.valueOf(type.toString()));
			messageEntity.setConsumed(false);
			messageEntity.setUpdateTime(new Date());
			messageEntity.setFromUserId(fromUserId);
			messageEntity.setFromUsername(fromUsername);
			websocketMessageDao.save(messageEntity);
			return new WebsocketMessageVO().set(messageEntity);
		}else{
			if(metadata.getServerIp().equals(applicationConfig.getIp()) && 
					metadata.getServerPort().equals(applicationConfig.getPort())){
				WebsocketMessagePO messageEntity = new WebsocketMessagePO();
				messageEntity.setUserId(user.getId());
				messageEntity.setUsername(user.getNickname());
				messageEntity.setMessage(message);
				messageEntity.setMessageType(WebsocketMessageType.valueOf(type.toString()));
				messageEntity.setConsumed(false);
				messageEntity.setUpdateTime(new Date());
				messageEntity.setFromUserId(fromUserId);
				messageEntity.setFromUsername(fromUsername);
				websocketMessageDao.save(messageEntity);
				try{
					SessionQueue queue = SessionQueue.getInstance();
					Session session = queue.get(userId);
					if(session != null){
						session.getBasicRemote().sendText(message);
						messageEntity.setConsumed(true);
						websocketMessageDao.save(messageEntity);
					}
					return new WebsocketMessageVO().set(messageEntity);
				}catch(Exception e){
					throw e;
				}
			}else{
				return postSend(userId, user.getNickname(), message, type, fromUserId, fromUsername, metadata.getServerIp(), metadata.getServerPort());
			}
		}
	}
	
	/**
	 * 消息转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 上午11:38:24
	 * @param Long userId 用户id
	 * @param String message 推送消息内容
	 * @param WebsocketMessageType type 消息类型
	 * @param Long fromUserId 消息来源用户id
	 * @param String fromUsername 消息来源用户名称
	 * @param String ip 目标ip
	 * @param String port 目标端口
	 */
	public WebsocketMessageVO postSend(
			Long userId, 
			String username,
			String message, 
			WebsocketMessageType type, 
			Long fromUserId, 
			String fromUsername,
			String ip, 
			String port) throws Exception{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("Authorization");
		StringBuffer url = new StringBuffer();
		url.append("http://").append(ip).append(":").append(port).append("/message/send");
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setHeader("Authorization", token);
			List<NameValuePair> keyValues = new ArrayList<NameValuePair>();  
			keyValues.add(new BasicNameValuePair("userId", userId.toString()));
			keyValues.add(new BasicNameValuePair("message", message));	
			keyValues.add(new BasicNameValuePair("type", type.toString()));
			keyValues.add(new BasicNameValuePair("fromUserId", fromUserId.toString()));
			keyValues.add(new BasicNameValuePair("fromUsername", fromUsername));
			httpPost.setEntity(new UrlEncodedFormEntity(keyValues, "UTF-8")); 
		    response = httpclient.execute(httpPost);
		    if(response.getStatusLine().getStatusCode() != 200){
	        	throw new WebsocketMessageLoadBalacePostSendFailException(ip, port, fromUsername, username, message);
		    }else{
		    	StringBuffer jsonBodyResponse = new StringBuffer();
		    	HttpEntity entity = response.getEntity();
	 	        InputStream in = entity.getContent();
	 	        String line = "";
	 	        bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
	 	        while ((line=bReader.readLine()) != null) {
	 	        	jsonBodyResponse.append(line);
	 			}
	 	        EntityUtils.consume(entity);
	 	        return JSON.parseObject(jsonBodyResponse.toString()).getObject("data", WebsocketMessageVO.class);
		    }
		}finally{
			if(bReader != null) bReader.close();
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
	/**
	 * 重发消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午4:05:16
	 * @param Long id 消息id
	 */
	public void resend(Long id) throws Exception{
		WebsocketMessagePO message = websocketMessageDao.findOne(id);
		SessionMetadataPO metadata = sessionMetadataDao.findByUserId(message.getUserId());
		if(metadata == null) return;
		if(metadata.getServerIp().equals(applicationConfig.getIp()) && 
				metadata.getServerPort().equals(applicationConfig.getPort())){
			try{
				SessionQueue queue = SessionQueue.getInstance();
				Session session = queue.get(message.getUserId());
				session.getBasicRemote().sendText(message.getMessage());
			}catch(Exception e){
				throw e;
			}
		}else{
			postResend(id, metadata.getServerIp(), metadata.getServerPort());
		}
	}
	
	/**
	 * 负载均衡重发消息转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午4:05:39
	 * @param Long id 消息id
	 * @param String ip 目标服务器ip
	 * @param String port 目标服务器端口
	 */
	public void postResend(
			Long id,
			String ip,
			String port) throws Exception{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("Authorization");
		StringBuffer url = new StringBuffer();
		url.append("http://").append(ip).append(":").append(port).append("/message/resend");
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setHeader("Authorization", token);
			List<NameValuePair> keyValues = new ArrayList<NameValuePair>();  
			keyValues.add(new BasicNameValuePair("id", id.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(keyValues, "UTF-8")); 
		    response = httpclient.execute(httpPost);
		    if(response.getStatusLine().getStatusCode() != 200){
	        	throw new WebsocketMessageLoadBalacePostSendFailException(ip, port, id);
		    }
		}finally{
			if(bReader != null) bReader.close();
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
	/**
	 * 消费一个消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午5:03:01
	 * @param Long id 消息id
	 */
	public void consume(Long id) throws Exception{
		WebsocketMessagePO entity = websocketMessageDao.findOne(id);
		SessionMetadataPO metadata = sessionMetadataDao.findByUserId(entity.getUserId());
		if(metadata == null){
			entity.setConsumed(true);
			websocketMessageDao.save(entity);
		}else{
			if(metadata.getServerIp().equals(applicationConfig.getIp()) && 
					metadata.getServerPort().equals(applicationConfig.getPort())){
				try{
					SessionQueue queue = SessionQueue.getInstance();
					Session session = queue.get(entity.getUserId());
					session.getBasicRemote().sendText(JSON.toJSONString(new HashMapWrapper<String, String>().put("cmd", "messageConsumed")
																											.put("messageId", id.toString())
																											.getMap()));
					entity.setConsumed(true);
				}catch(Exception e){
					e.printStackTrace();
					entity.setConsumed(true);
					websocketMessageDao.save(entity);
				}
				
			}else{
				postConsume(id, metadata.getServerIp(), metadata.getServerPort());
			}
		}
	}
	
	/**
	 * 转发消费一个消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午5:21:18
	 * @param Long id 消息id
	 * @param String ip 目标ip
	 * @param String port 目标端口
	 */
	private void postConsume(Long id, String ip, String port) throws Exception{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("Authorization");
		StringBuffer url = new StringBuffer();
		url.append("http://").append(ip).append(":").append(port).append("/message/consume");
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setHeader("Authorization", token);
			List<NameValuePair> keyValues = new ArrayList<NameValuePair>();  
			keyValues.add(new BasicNameValuePair("id", id.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(keyValues, "UTF-8")); 
		    response = httpclient.execute(httpPost);
		    if(response.getStatusLine().getStatusCode() != 200){
	        	throw new WebsocketMessageLoadBalacePostConsumeFailException(ip, port, id);
		    }
		}finally{
			if(bReader != null) bReader.close();
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
}
