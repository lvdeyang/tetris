package com.sumavision.tetris.system.storage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class SystemStorageQuery {

	@Autowired
	private SystemStorageDAO systemStorageDao;
	
	/**
	 * 分页查询系统存储<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午10:22:24
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return List<SystemStoragePO> 存储列表
	 */
	public List<SystemStoragePO> findAll(int currentPage, int pageSize) throws Exception{
		Pageable pageable = PageRequest.of(currentPage-1, pageSize);
		Page<SystemStoragePO> pagedEntities = systemStorageDao.findAll(pageable);
		if(pagedEntities == null) return null;
		return pagedEntities.getContent();
	}
	
}
