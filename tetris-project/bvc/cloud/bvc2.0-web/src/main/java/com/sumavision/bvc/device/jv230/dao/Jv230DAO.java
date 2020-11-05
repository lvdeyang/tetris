package com.sumavision.bvc.device.jv230.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = Jv230PO.class, idClass = long.class)
public interface Jv230DAO extends MetBaseDAO<Jv230PO>{

	/**
	 * @Title: 查询拼接屏下的所有jv230设备<br/> 
	 * @param combineJv230Id 拼接屏id
	 * @return List<Jv230PO> jv230集合
	 */
	@Query("from Jv230PO jv230 where jv230.combineJv230.id = ?1")
	public List<Jv230PO> queryByCombineJv230Id(Long combineJv230Id);
	
}
