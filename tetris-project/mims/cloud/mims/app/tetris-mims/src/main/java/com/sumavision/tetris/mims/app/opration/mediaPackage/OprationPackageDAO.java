package com.sumavision.tetris.mims.app.opration.mediaPackage;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OprationPackagePO.class, idClass = Long.class)
public interface OprationPackageDAO extends BaseDAO<OprationPackagePO>{

}
