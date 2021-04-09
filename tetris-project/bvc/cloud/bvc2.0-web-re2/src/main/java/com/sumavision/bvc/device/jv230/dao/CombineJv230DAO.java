package com.sumavision.bvc.device.jv230.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineJv230PO.class, idClass = long.class)
public interface CombineJv230DAO extends MetBaseDAO<CombineJv230PO>{

	/**
	 * @Title: 获取用户创建的拼接屏<br/> 
	 * @param userId 用户id
	 * @return List<CombineJv230PO>
	 */
	public List<CombineJv230PO> findByUserId(Long userId);
	
	public Page<CombineJv230PO> findByUserId(Long userId, Pageable pageable);
	
}
