package com.suma.venus.alarmoprlog.orm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;

@RepositoryDefinition(domainClass = AlarmPO.class, idClass = Long.class)
public interface IAlarmDAO extends JpaRepository<AlarmPO, Long>, JpaSpecificationExecutor<AlarmPO> {
	
	Optional<AlarmPO> findById(Long id);
	
	List<AlarmPO> findByAlarmStatus(EAlarmStatus status);
	
	List<AlarmPO> findByLastAlarm_AlarmInfo_AlarmCode(String alarmCode);

	void deleteById(Long id);
	
	void deleteAll();
}
