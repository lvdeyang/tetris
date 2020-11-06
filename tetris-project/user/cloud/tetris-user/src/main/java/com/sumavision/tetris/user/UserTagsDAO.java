package com.sumavision.tetris.user;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月18日 下午4:55:22
 */
@RepositoryDefinition(domainClass = UserTagsPO.class, idClass = Long.class)
public interface UserTagsDAO extends BaseDAO<UserTagsPO>{
	
	public List<UserTagsPO> findByUserId(Long userId);
	
	public List<UserTagsPO> findByUserIdAndTagName(Long userId,String tagName);
    
	public void deleteByUserId(Long userId);
	
}
