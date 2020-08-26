package com.sumavision.tetris.omms.software.service.installation;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = InstallationPackagePO.class, idClass = Long.class)
public interface InstallationPackageDAO extends BaseDAO<InstallationPackagePO>{

}
