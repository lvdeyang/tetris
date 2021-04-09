package com.sumavision.bvc.device.monitor.live.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MonitorLiveUserQuery {

	@Autowired
	private MonitorLiveUserDAO monitorLiveUserDao;

	/**
	 * 分页查询用户点播用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 上午11:55:44
	 * @param Long userId 业务用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorLiveUserPO> 点播用户任务列表
	 */
	public List<MonitorLiveUserPO> findByUserId(
			Long userId, 
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorLiveUserPO> pagedEntities = monitorLiveUserDao.findByUserId(userId, page);
		if(pagedEntities == null){
			return null;
		}
		return pagedEntities.getContent();
	}
	
	/**
	 * 分页查询用户点播用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 上午10:20:25
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorLiveUserPO> 点播用户任务列表
	 */
	public List<MonitorLiveUserPO> findAll(
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorLiveUserPO> pagedEntities = monitorLiveUserDao.findAll(page);
		if(pagedEntities == null){
			return null;
		}
		return pagedEntities.getContent();
	}
	
}
