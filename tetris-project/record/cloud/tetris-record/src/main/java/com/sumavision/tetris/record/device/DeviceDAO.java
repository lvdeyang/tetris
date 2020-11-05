package com.sumavision.tetris.record.device;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO;

@RepositoryDefinition(domainClass = DevicePO.class, idClass = Long.class)
public interface DeviceDAO extends BaseDAO<DevicePO> {

	@Query(value = "select * from record_device d where d.deviceName like %:keyword% ", nativeQuery = true)
	List<DevicePO> findByKeywordLike(String keyword);

}
