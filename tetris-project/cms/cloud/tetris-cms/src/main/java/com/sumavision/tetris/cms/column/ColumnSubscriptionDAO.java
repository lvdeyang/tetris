package com.sumavision.tetris.cms.column;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.cms.article.ArticleKeepPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ColumnSubscriptionPO.class, idClass = Long.class)
public interface ColumnSubscriptionDAO extends BaseDAO<ColumnSubscriptionPO>{
	/**
	 * 根据用户组织id查询订阅栏目<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午3:11:32
	 * @param userId 用户id
	 * @return List<ColumnSubscriptionPO>
	 */
	public List<ColumnSubscriptionPO> findByUserId(Long userId);
	
	/**
	 * 根据用户组织id和栏目id查询栏目<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午3:11:32
	 * @param userId 用户id
	 * @param columnId 栏目id
	 * @return ColumnSubscriptionPO
	 */
	public ColumnSubscriptionPO findByUserIdAndColumnId(Long userId,Long columnId);
}
