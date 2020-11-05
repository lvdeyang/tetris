package com.sumavision.tetris.omms.software.service.installation.history;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = InstallationPackageHistoryPO.class, idClass = Long.class)
public interface InstallationPackageHistoryDAO extends BaseDAO<InstallationPackageHistoryPO>{

	public List<InstallationPackageHistoryPO> findByServiceTypeId(Long serviceTypeId);
	
	public Page<InstallationPackageHistoryPO> findByServiceTypeId(Long serviceTypeId, Pageable page);
	
	public int countByServiceTypeId(Long serviceTypeId);
	
}
