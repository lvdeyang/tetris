package com.sumavision.signal.bvc.entity.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.entity.po.HeartBeatBundlePO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = HeartBeatBundlePO.class, idClass = long.class)
public interface HeartBeatBundleDAO extends BaseDAO<HeartBeatBundlePO>{

}
