package com.sumavision.tetris.sts.device.monitor;

import com.sumavision.tetris.sts.common.CommonDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.sts.device.monitor.DeviceMonitorDataPO.DeviceMonitorDateType;
import java.util.Date;
import java.util.List;

@RepositoryDefinition(domainClass = DeviceMonitorDataPO.class, idClass = Long.class)
public interface DeviceMonitorDataDao extends CommonDao<DeviceMonitorDataPO> {

	List<DeviceMonitorDataPO> findByDeviceIdAndDateTypeAndGetTimeBetween(Long deviceId, DeviceMonitorDateType dateType, Date startDate, Date endDate);

	Page<DeviceMonitorDataPO> findByDeviceIdAndDateTypeOrderByGetTimeDesc(Long deviceId, DeviceMonitorDateType dateType, Pageable pageRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	void deleteByDateTypeAndGetTimeLessThan(DeviceMonitorDateType dateType, Date date);
}
