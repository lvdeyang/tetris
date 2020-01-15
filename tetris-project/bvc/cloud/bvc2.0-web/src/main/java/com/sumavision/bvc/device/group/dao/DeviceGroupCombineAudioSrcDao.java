package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;
@RepositoryDefinition(domainClass = CombineAudioSrcPO.class, idClass = long.class)
public interface DeviceGroupCombineAudioSrcDao extends MetBaseDAO<CombineAudioSrcPO>{

}

