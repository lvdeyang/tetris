package com.sumavision.bvc.device.monitor.vod;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorExternalStaticResourceFolderPO.class, idClass = Long.class)
public interface MonitorExternalStaticResourceFolderDAO extends MetBaseDAO<MonitorExternalStaticResourceFolderPO>{
	
	/**
	 * 分页查询用户创建的外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午9:55:35
	 * @param String createUserId 用户id
	 * @param Pageable page 分页信息
	 * @return List<MonitorExternalStaticResourceFolderPO> 文件夹列表
	 */
	public Page<MonitorExternalStaticResourceFolderPO> findByCreateUserId(String createUserId, Pageable page);
	
	/**
	 * 统计用户创建的外部文件夹数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午9:57:10
	 * @param String userId 用户id
	 * @return int 文件夹数量
	 */
	public int countByCreateUserId(String createUserId);
	
}
