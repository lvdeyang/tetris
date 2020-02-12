package com.suma.venus.alarmoprlog.orm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.alarmoprlog.orm.entity.OprlogPO;

@RepositoryDefinition(domainClass = OprlogPO.class, idClass = Long.class)
public interface IOprlogDAO extends JpaRepository<OprlogPO, Long>, JpaSpecificationExecutor<OprlogPO> {

}
