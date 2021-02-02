package com.sumavision.signal.bvc.director.dao;/**
 * Created by Poemafar on 2020/9/2 20:17
 */

import com.sumavision.signal.bvc.director.po.DirectorTaskPO;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

/**
 * @ClassName:
 * @Description TODO
 * @Author: Mr
 * @Version：1.0
 * @Date 2020/9/2 20:17
 */
@RepositoryDefinition(domainClass = DirectorTaskPO.class, idClass = long.class)
public interface DirectorTaskDAO extends BaseDAO<DirectorTaskPO> {

    public List<DirectorTaskPO> findByBusinessId(String businessId);

}
