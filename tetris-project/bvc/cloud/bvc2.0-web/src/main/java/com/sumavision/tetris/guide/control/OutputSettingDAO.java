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
 * <b>日期：</b>2020年9月4日 上午9:03:17
 */
@RepositoryDefinition(domainClass = VideoParametersPO.class, idClass = Long.class)
public interface OutputSettingDAO extends BaseDAO<OutputSettingPO>{
	
	public List<OutputSettingPO> findByGuidePO(Long guidePO);
	
	public void deleteByGuidePO(Long guidePO);
}
