package com.sumavision.tetris.zoom;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SourceGroupPO.class, idClass = Long.class)
public interface SourceGroupDAO extends BaseDAO<SourceGroupPO>{

	/**
	 * 根据id查询组并按照名称排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 上午9:53:38
	 * @param Collection<Long> ids 组id列表
	 * @return List<SourceGroupPO> 组列表
	 */
	public List<SourceGroupPO> findByIdInOrderByNameAsc(Collection<Long> ids);
	
	/**
	 * 查询用户联系人分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午3:35:36
	 * @param String userId 用户id
	 * @param SourceGroupType type 分组类型
	 * @return List<SourceGroupPO> 分组列表
	 */
	public List<SourceGroupPO> findByUserIdAndTypeOrderByNameAsc(String userId, SourceGroupType type);
	
}
