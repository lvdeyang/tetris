package com.sumavision.tetris.zoom.jv220;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = Jv220UserAllocationPO.class, idClass = Long.class)
public interface Jv220UserAllocationDAO extends BaseDAO<Jv220UserAllocationPO>{

	/**
	 * 根据用户id查询jv220用户分配<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:15:27
	 * @param Long userId 用户id
	 * @return Jv220UserAllocationPO jv220用户分配
	 */
	public Jv220UserAllocationPO findByUserId(Long userId);
	
	/**
	 * 根据用户id列表查询jv220用户分配<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:17:13
	 * @param Collection<Long> userIds 用户id列表
	 * @return List<Jv220UserAllocationPO> jv220用户分配
	 */
	public List<Jv220UserAllocationPO> findByUserIdIn(Collection<Long> userIds);
	 
}
