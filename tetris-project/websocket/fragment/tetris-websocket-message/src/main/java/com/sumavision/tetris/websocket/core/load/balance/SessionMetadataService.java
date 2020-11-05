package com.sumavision.tetris.websocket.core.load.balance;

import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.core.SessionQueue;
import com.sumavision.tetris.websocket.core.config.ApplicationConfig;

@Service
@Transactional(rollbackFor = Exception.class)
public class SessionMetadataService {

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private SessionMetadataDAO sessionMetadataDao;
	
	/**
	 * 添加session元数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:42:10
	 * @param UserBO user 用户数据
	 * @param Session session websocket session
	 * @return SessionMetadataPO session元数据
	 */
	public SessionMetadataPO add(
			UserVO user,
			Session session) throws Exception{
		try{
			SessionMetadataPO metadata = null;
			metadata = sessionMetadataDao.findByUserId(user.getId().toString());
			if(metadata == null){
				metadata = new SessionMetadataPO();
			}
			metadata.setUserId(user.getId().toString());
			metadata.setServerIp(applicationConfig.getIp());
			metadata.setServerPort(applicationConfig.getPort());
			metadata.setUsername(user.getNickname());
			metadata.setSessionId(session.getId());
			sessionMetadataDao.save(metadata);
			SessionQueue.getInstance().put(user.getId(), session);
			return metadata;
		}catch(Exception e){
			SessionQueue.getInstance().remove(user.getId());
			throw e;
		}
	}
	
	/**
	 * 断开websocket链接<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:52:46
	 * @param Session session websocket session
	 */
	public String remove(Session session) throws Exception{
		SessionMetadataPO metadata = sessionMetadataDao.findBySessionId(session.getId());
		if(metadata != null){
			sessionMetadataDao.delete(metadata);
			SessionQueue.getInstance().remove(metadata.getUserId());
			return metadata.getUserId();
		}
		return null;
	}
	
}
