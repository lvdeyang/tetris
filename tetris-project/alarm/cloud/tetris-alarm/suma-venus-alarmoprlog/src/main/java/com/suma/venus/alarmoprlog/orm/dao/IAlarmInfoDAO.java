package com.suma.venus.alarmoprlog.orm.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;

public interface IAlarmInfoDAO extends JpaRepository<AlarmInfoPO, Long>, JpaSpecificationExecutor<AlarmInfoPO> {

	public AlarmInfoPO findByAlarmCode(String alarmCode);

	@Query("select u from AlarmInfoPO u where concat(u.alarmName,u.alarmCode,ifnull(u.alarmBrief,'')) like %:keyword%")
	public Page<AlarmInfoPO> findByKeywordContaining(Pageable pageable, @Param("keyword") String keyword);
	
	Optional<AlarmInfoPO> findById(Long id);

}
