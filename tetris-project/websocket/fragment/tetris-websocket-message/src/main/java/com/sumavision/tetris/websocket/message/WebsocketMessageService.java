package com.sumavision.tetris.websocket.message;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.websocket.Session;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.core.SessionQueue;
import com.sumavision.tetris.websocket.core.config.ApplicationConfig;
import com.sumavision.tetris.websocket.core.load.balance.SessionMetadataDAO;
import com.sumavision.tetris.websocket.core.load.balance.SessionMetadataPO;
import com.sumavision.tetris.websocket.message.exception.TargetUserNotFoundException;

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
	public void send(
			Long userId, 
			String message, 
			WebsocketMessageType type, 
			Long fromUserId,
			String fromUsername) throws Exception{
		
		SessionMetadataPO metadata = sessionMetadataDao.findByUserId(userId);
		if(metadata == null){
			//离线消息
			List<UserVO> users = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList());
			if(users==null || users.size()<=0) throw new TargetUserNotFoundException();
			UserVO user = users.get(0);
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
		}else{
			if(metadata.getServerIp().equals(applicationConfig.getIp()) && 
					metadata.getServerPort().equals(applicationConfig.getPort())){
				List<UserVO> users = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList());
				if(users==null || users.size()<=0) throw new TargetUserNotFoundException();
				UserVO user = users.get(0);
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
					session.getBasicRemote().sendText(message);
					messageEntity.setConsumed(true);
					websocketMessageDao.save(messageEntity);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				postSend(userId, message, type, fromUserId, fromUsername, metadata.getServerIp(), metadata.getServerPort());
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
	public void postSend(
			Long userId, 
			String message, 
			WebsocketMessageType type, 
			Long fromUserId, 
			String fromUsername,
			String ip, 
			String port) throws Exception{
		StringBuffer url = new StringBuffer();
		url.append("http://").append(ip).append(":").append(port).append("/message/send");
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url.toString());
			List<NameValuePair> keyValues = new ArrayList<NameValuePair>();  
			keyValues.add(new BasicNameValuePair("userId", userId.toString()));
			keyValues.add(new BasicNameValuePair("message", message));	
			keyValues.add(new BasicNameValuePair("type", type.toString()));
			keyValues.add(new BasicNameValuePair("fromUserId", fromUserId.toString()));
			keyValues.add(new BasicNameValuePair("fromUsername", fromUsername));
			httpPost.setEntity(new UrlEncodedFormEntity(keyValues, "UTF-8")); 
		    response = httpclient.execute(httpPost);
		    if(response.getStatusLine().getStatusCode() != 200){
		    	StringBuffer error = new StringBuffer();
	        	error.append(response.getStatusLine().getStatusCode());
	        	error.append("@$$@请求失败");
	        	throw new Exception(error.toString());
		    }
		}finally{
			if(bReader != null) bReader.close();
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
}
