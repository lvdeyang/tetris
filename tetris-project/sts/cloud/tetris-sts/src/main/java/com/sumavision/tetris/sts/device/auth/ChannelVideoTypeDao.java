package com.sumavision.tetris.sts.device.auth;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;


@RepositoryDefinition(domainClass = ChannelVideoTypePO.class, idClass = Long.class)
public interface ChannelVideoTypeDao extends CommonDao<ChannelVideoTypePO>{
	
	ChannelVideoTypePO findById(Long id);

	ChannelVideoTypePO findTopByVideoTypeAndWidthAndHeightAndType(String videoType , Integer width, Integer height, String type);

	ChannelVideoTypePO findTopByVideoTypeLikeAndWidthAndHeightAndType(String videoType , Integer width, Integer height, String type);

}
