package com.sumavision.bvc.device.monitor.subtitle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MonitorSubtitleQuery {

	@Autowired
	private MonitorSubtitleDAO monitorSubtitleDao;
	
	/**
	 * 分页查询字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 下午2:12:33
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorSubtitlePO> 字幕列表
	 */
	public List<MonitorSubtitlePO> findAll(int currentPage, int pageSize){
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorSubtitlePO> pagedEntities = monitorSubtitleDao.findAll(page);
		return pagedEntities.getContent();
	}
	
	/**
	 * 分页查询用户创建的字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 上午10:49:14
	 * @param Long userId 用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorSubtitlePO> 字幕列表
	 */
	public List<MonitorSubtitlePO> findByUserId(Long userId, int currentPage, int pageSize){
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorSubtitlePO> pagedEntities = monitorSubtitleDao.findByUserId(userId.toString(), page);
		return pagedEntities.getContent();
	}
	
}
