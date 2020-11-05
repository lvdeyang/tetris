package com.sumavision.tetris.easy.process.access.point;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AccessPointPO.class, idClass = Long.class)
public interface AccessPointDAO extends BaseDAO<AccessPointPO>{

	/**
	 * 获取服务下所有的接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午1:20:39
	 * @param Long serviceId 服务id
	 * @return List<AccessPointPO> 接入点列表
	 */
	public List<AccessPointPO> findByServiceId(Long serviceId);
	
	/**
	 * 分页查询服务下所有的接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午3:27:14
	 * @param Long serviceId 服务id
	 * @param Pageable page 分页信息
	 * @return List<AccessPointPO> 接入点列表
	 */
	public Page<AccessPointPO> findByServiceId(Long serviceId, Pageable page);
	
	/**
	 * 统计服务下的所有接入点数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午4:02:02
	 * @param Long serviceId 服务id
	 * @return int 统计结果
	 */
	public int countByServiceId(Long serviceId);
	
	/**
	 * 根据作用域获取接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月26日 上午10:59:13
	 * @param AccessPointScope scope 作用域
	 * @return List<AccessPointPO> 接入点列表
	 */
	public List<AccessPointPO> findByScope(AccessPointScope scope);
	
}
