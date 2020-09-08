/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午6:41:45
 */
@RepositoryDefinition(domainClass = SourcePO.class, idClass = Long.class)
public interface SourceDAO extends BaseDAO<SourcePO>{

	public List<SourcePO> findByGuideId(Long guideId);
	
}
