package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineAudioPO.class, idClass = long.class)
public interface DeviceGroupCombineAudioDao extends MetBaseDAO<CombineAudioPO>{

}
