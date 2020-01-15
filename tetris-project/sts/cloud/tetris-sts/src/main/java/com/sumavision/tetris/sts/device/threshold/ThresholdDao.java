package com.sumavision.tetris.sts.device.threshold;

import com.sumavision.tetris.sts.common.CommonDao;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ThresholdPO.class, idClass = Long.class)
public interface ThresholdDao extends CommonDao<ThresholdPO> {

	public ThresholdPO findTopByIdIsNotNull();
	
}
