package com.sumavision.bvc.device.monitor.message;

import java.util.Date;
import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorCalledMessagePO.class, idClass = Long.class)
public interface MonitorCalledMessageDAO extends MetBaseDAO<MonitorCalledMessagePO>{
	
	/**
	 * 查询用户的被叫消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午3:40:26
	 * @param String receiveUser 用户名
	 * @return MonitorCalledMessagePO 消息
	 */
	public MonitorCalledMessagePO findByReceiveUser(String receiveUser);
	
	/**
	 * 查询需要清除的用户视频通知<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午7:21:35
	 * @param Date time 时间节点
	 * @return List<MonitorCalledMessagePO> 消息列表
	 */
	public List<MonitorCalledMessagePO> findByUpdateTimeBefore(Date time);
	
}
