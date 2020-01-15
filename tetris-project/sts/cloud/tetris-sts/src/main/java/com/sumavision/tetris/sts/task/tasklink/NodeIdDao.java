package com.sumavision.tetris.sts.task.tasklink;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;


@RepositoryDefinition(domainClass = NodeIdPO.class, idClass = Long.class)
public interface NodeIdDao extends CommonDao<NodeIdPO>{

}
