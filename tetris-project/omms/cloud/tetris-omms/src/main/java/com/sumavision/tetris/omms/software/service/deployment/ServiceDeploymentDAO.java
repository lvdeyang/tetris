package com.sumavision.tetris.omms.software.service.deployment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServiceDeploymentPO.class, idClass = Long.class)
public interface ServiceDeploymentDAO extends BaseDAO<ServiceDeploymentPO>{
	
	public Page<ServiceDeploymentPO> findByServerId(Long serverId,Pageable page);
	
	public int countByServerId(Long serverId);
	
	/**
	 * 查询服务器上部署的服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 下午5:33:04
	 * @param Long serverId 服务器id
	 * @return List<ServiceDeploymentPO> 部署列表
	 */
	public List<ServiceDeploymentPO> findByServerId(Long serverId);
	
	public List<ServiceDeploymentPO> findByServiceTypeId(Long serviceTypeId);
	
	public ServiceDeploymentPO findByServerIdAndServiceTypeId(Long serverId,Long serviceTypeId);
}
