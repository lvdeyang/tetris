package com.sumavision.bvc.device.monitor.osd;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MonitorOsdQuery {

	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	/**
	 * 分页查询osd配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 下午6:51:50
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorOsdPO> osd列表
	 */
	public List<MonitorOsdPO> findAll(int currentPage, int pageSize) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorOsdPO> pagedEntities = monitorOsdDao.findAll(page);
		return pagedEntities.getContent();
	}
	
	
	/**
	 * 分页查询用户创建的osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午4:59:24
	 * @param Long userId 用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorOsdPO> osd列表
	 */
	public List<MonitorOsdPO> findByUserId(Long userId, int currentPage, int pageSize){
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorOsdPO> pagedEntities = monitorOsdDao.findByUserId(userId.toString(), page);
		return pagedEntities.getContent();
	}
	
}
