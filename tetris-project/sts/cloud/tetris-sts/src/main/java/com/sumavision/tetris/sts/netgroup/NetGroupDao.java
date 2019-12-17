package com.sumavision.tetris.sts.netgroup;

import com.sumavision.tetris.sts.common.CommonDao;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

/**
 * Created by Poemafar on 2019/12/16 13:49
 */
@RepositoryDefinition(domainClass = NetGroupPO.class, idClass = Long.class)
public interface NetGroupDao extends CommonDao<NetGroupPO> {

    public NetGroupPO findTopByNetNameAndBeDeleteFalse(String netName);

    public List<NetGroupPO> findByNetNameAndIdNotAndBeDeleteFalse(String netName , Long id);
}
