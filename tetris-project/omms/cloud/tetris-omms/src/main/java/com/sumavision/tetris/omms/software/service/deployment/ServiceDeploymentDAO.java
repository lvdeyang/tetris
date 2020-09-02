package com.sumavision.tetris.omms.software.service.deployment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServiceDeploymentPO.class, idClass = Long.class)
public interface ServiceDeploymentDAO extends BaseDAO<ServiceDeploymentPO>{
	
	public Page<ServiceDeploymentPO> findByServerId(Long serverId,Pageable page);
	
	public int countByServerId(Long serverId);
	
	/*@Query(value = "SELECT * FROM tetris_omms_service_deployment AS a LEFT JOIN tetris_omms_installation_package AS b ON a.service_type_id = b.service_type_id WHERE a.server_id = ?1",
			nativeQuery = true)
	public Page<ServiceDeploymentPO> findByCondition(Long serverId,Pageable page);*/
	
	/**
	 * SELECT
    A.id AS AID,
    A.content AS AContent,
    B.id AS BID,
    B.content AS BContent
	FROM
	    A
	LEFT JOIN B ON (A.id = B.id);
	 */

	/*@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1", nativeQuery = true)*/
}
