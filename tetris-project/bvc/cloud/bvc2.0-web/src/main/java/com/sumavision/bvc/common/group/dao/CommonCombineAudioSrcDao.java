package com.sumavision.bvc.common.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.common.group.po.CommonCombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;
@RepositoryDefinition(domainClass = CommonCombineAudioSrcPO.class, idClass = long.class)
public interface CommonCombineAudioSrcDao extends MetBaseDAO<CommonCombineAudioSrcPO>{

}

