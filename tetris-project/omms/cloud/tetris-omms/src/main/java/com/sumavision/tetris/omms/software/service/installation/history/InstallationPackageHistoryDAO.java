package com.sumavision.tetris.omms.software.service.installation.history;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = InstallationPackageHistoryPO.class, idClass = Long.class)
public interface InstallationPackageHistoryDAO extends BaseDAO<InstallationPackageHistoryPO>{

}
