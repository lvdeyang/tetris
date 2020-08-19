package com.suma.venus.alarmoprlog.orm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;

@RepositoryDefinition(domainClass = SubscribeAlarmPO.class, idClass = Long.class)
public interface ISubscribeAlarmDAO
		extends JpaRepository<SubscribeAlarmPO, Long>, JpaSpecificationExecutor<SubscribeAlarmPO> {

	public List<SubscribeAlarmPO> findByAlarmInfo_AlarmCodeAndSubServiceNameAndMsgCallbackId(String alarmCode,
			String subsService, String msgCallbackId);

	public List<SubscribeAlarmPO> findByAlarmInfo_AlarmCode(String alarmCode);

	public List<SubscribeAlarmPO> findBySubServiceName(String subServiceName);

	@Query("select u from SubscribeAlarmPO u left join u.alarmInfo p where concat(u.subServiceName, p.alarmCode, p.alarmName, p.alarmLevel, ifnull(u.subsObj,'')) like %:keyword%")
	public Page<SubscribeAlarmPO> findByKeywordContaining(Pageable pageable, @Param("keyword") String keyword);

}
