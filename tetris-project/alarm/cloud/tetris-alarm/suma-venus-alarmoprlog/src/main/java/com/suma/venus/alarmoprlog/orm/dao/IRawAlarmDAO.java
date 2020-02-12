package com.suma.venus.alarmoprlog.orm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.alarmoprlog.orm.entity.RawAlarmPO;

@RepositoryDefinition(domainClass = RawAlarmPO.class, idClass = Long.class)
public interface IRawAlarmDAO extends JpaRepository<RawAlarmPO, Long>, JpaSpecificationExecutor<RawAlarmPO> {

	public List<RawAlarmPO> findByAlarmInfo_AlarmCode(String alarmCode);

	public List<RawAlarmPO> findByAlarmPOId(Long alarmPOId);

	void deleteById(Long id);

	// void deleteAll(List<RawAlarmPO> rawAlarmPOs);

}
