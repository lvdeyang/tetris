package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.RecordLiveChannelPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = RecordLiveChannelPO.class, idClass = long.class)
public interface RecordLiveChannelDAO extends MetBaseDAO<RecordLiveChannelPO>{

	public List<RecordLiveChannelPO> findByRecordUuid(String recordUuid);
}