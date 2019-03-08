package com.sumavision.tetris.cms.classify;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ClassifyPO.class, idClass = Long.class)
public interface ClassifyDAO extends BaseDAO<ClassifyPO>{

	/**
	 * 分页查询分类列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 下午6:29:45
	 * @param Collection<Long> except 例外分类id列表
	 * @param Pageable page 分页信息
	 * @return Page<ClassifyPO> 用户列表
	 */
	@Query(value = "SELECT classify FROM com.sumavision.tetris.cms.classify.ClassifyPO classify WHERE classify.id NOT IN ?1")
	public Page<ClassifyPO> findWithExcept(Collection<Long> except, Pageable page);
}
