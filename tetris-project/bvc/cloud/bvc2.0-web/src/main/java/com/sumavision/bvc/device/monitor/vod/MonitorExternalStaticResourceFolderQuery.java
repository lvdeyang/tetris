package com.sumavision.bvc.device.monitor.vod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MonitorExternalStaticResourceFolderQuery {

	@Autowired
	private MonitorExternalStaticResourceFolderDAO monitorExternalStaticResourceFolderDao;
	
	/**
	 * 分页查询用户创建的外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午9:59:11
	 * @param Long userId 用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorExternalStaticResourceFolderPO> 文件夹列表
	 */
	public List<MonitorExternalStaticResourceFolderPO> findByCreateUserId(Long userId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorExternalStaticResourceFolderPO> pagedEntities = monitorExternalStaticResourceFolderDao.findByCreateUserId(userId.toString(), page);
		return pagedEntities.getContent();
	}
	
	/**
	 * 分页查询外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 下午4:30:54
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorExternalStaticResourceFolderPO> 文件夹列表
	 */
	public List<MonitorExternalStaticResourceFolderPO> findAll(int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorExternalStaticResourceFolderPO> pagedEntities = monitorExternalStaticResourceFolderDao.findAll(page);
		return pagedEntities.getContent();
	}
	
}
