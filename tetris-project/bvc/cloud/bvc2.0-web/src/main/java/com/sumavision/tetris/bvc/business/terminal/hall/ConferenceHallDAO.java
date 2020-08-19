package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;


@RepositoryDefinition(domainClass = ConferenceHallPO.class, idClass = Long.class)
public interface ConferenceHallDAO extends BaseDAO<ConferenceHallPO>{

	/**
	 * 根据名称模糊查询会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 上午11:36:35
	 * @param String name 名称
	 * @param Pageable page 分页信息
	 * @return Page<ConferenceHallPO> 会场列表
	 */
	public Page<ConferenceHallPO> findByNameLike(String name, Pageable page);
	
	public List<ConferenceHallPO> findByFolderId(Long folderId);
}
