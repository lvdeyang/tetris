package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.RecordAssetPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = RecordAssetPO.class, idClass = long.class)
public interface RecordAssetDAO extends MetBaseDAO<RecordAssetPO>{

//	public RecordLiveChannelPO findByRecordUuid(String recordUuid);
}
