package com.sumavision.tetris.zoom.call.user;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CallUserPO.class, idClass = Long.class)
public interface CallUserDAO extends BaseDAO<CallUserPO>{

	/**
	 * 查询两个用户之间的呼叫<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月25日 下午3:23:13
	 * @param String uniqueKey 唯一键
	 * @return CallUserPO 呼叫信息
	 */
	public CallUserPO findByUniqueKey(String uniqueKey);
	
	/**
	 * 模糊查询用户呼叫<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午9:55:09
	 * @param String uniqueKeyExpression 唯一键表达式
	 * @return List<CallUserPO> 呼叫信息列表
	 */
	public List<CallUserPO> findByUniqueKeyLike(String uniqueKeyExpression);
	
}
