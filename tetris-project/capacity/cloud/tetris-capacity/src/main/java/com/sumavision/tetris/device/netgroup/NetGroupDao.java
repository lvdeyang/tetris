package com.sumavision.tetris.device.netgroup;

import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

/**
 * Created by Poemafar on 2019/12/16 13:49
 */
@RepositoryDefinition(domainClass = NetGroupPO.class, idClass = Long.class)
public interface NetGroupDao extends BaseDAO<NetGroupPO> {

    @Query(value = "select id from NetGroupPO")
    public List<Long> findAllId();

    @Query(value = "select distinct s from NetGroupPO s where s.netType = 'INPUT'  ")
    public List<NetGroupPO> findInputNetGroup();

    @Query(value = "select distinct s from NetGroupPO s where s.netType = 'OUTPUT' ")
    public List<NetGroupPO> findOutputNetGroup();

    public NetGroupPO findTopByNetName(String netName);

    public List<NetGroupPO> findByNetNameAndIdNot(String netName, Long id);


}
