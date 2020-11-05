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
 * <b>日期：</b>2020年9月27日 下午2:35:10
 */
@RepositoryDefinition(domainClass = OutputGroupPO.class, idClass = Long.class)
public interface OutputGroupDAO extends BaseDAO<OutputGroupPO>{
	
	public List<OutputGroupPO> findByGuideId(Long guideId);

}
