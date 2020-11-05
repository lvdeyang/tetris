package com.sumavision.bvc.device.monitor.message;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.bvc.device.monitor.live.MonitorUserCallService;
import com.sumavision.bvc.device.monitor.message.exception.MessageAlreadyTimeoutException;
import com.sumavision.tetris.commons.util.date.DateUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorCalledMessageService implements MessageService{

	@Autowired
	private MonitorCalledMessageDAO monitorCalledMessageDao;
	
	@Autowired
	private MonitorUserCallService monitorUserCallService;
	
	/** 消息超时时间60秒 */
	private static final int TIMEOUT = 60;
	
	@Override
	public void clear() throws Exception {
		
		Date now = new Date();
		Date timeSecodsAgo = DateUtil.addSecond(now, -TIMEOUT);
		
		List<MonitorCalledMessagePO> messages = monitorCalledMessageDao.findByUpdateTimeBefore(timeSecodsAgo);
		if(messages!=null && messages.size()>0){
			for(MonitorCalledMessagePO message:messages){
				//消息超时
				monitorUserCallService.sendUserTwoSideCallActionWithPassBy(message.getLayerId(), message.getCallUser(), message.getReceiveUser(), 6, message.getNetworkUserId());
			}
			monitorCalledMessageDao.deleteInBatch(messages);
		}
	}
	
	/**
	 * 被叫用户接受通话请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午9:32:55
	 * @param Long id 被叫消息id
	 * @param Long userId 操作用户
	 */
	public void accept(
			Long id, 
			Long userId) throws Exception{
		
		MonitorCalledMessagePO message = monitorCalledMessageDao.findOne(id);
		if(message == null){
			throw new MessageAlreadyTimeoutException();
		}
		
		monitorUserCallService.sendUserTwoSideCallActionWithPassBy(message.getLayerId(), message.getCallUser(), message.getReceiveUser(), 1, userId);
		monitorCalledMessageDao.delete(message);
	}
	
	/**
	 * 被叫用户拒绝通话请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午9:32:55
	 * @param Long id 被叫消息id
	 * @param Long userId 操作用户
	 */
	public void refuse(
			Long id, 
			Long userId) throws Exception{
		
		MonitorCalledMessagePO message = monitorCalledMessageDao.findOne(id);
		if(message == null){
			return;
		}
		
		monitorUserCallService.sendUserTwoSideCallActionWithPassBy(message.getLayerId(), message.getCallUser(), message.getReceiveUser(), 2, userId);
		monitorCalledMessageDao.delete(message);
	}
	
}
