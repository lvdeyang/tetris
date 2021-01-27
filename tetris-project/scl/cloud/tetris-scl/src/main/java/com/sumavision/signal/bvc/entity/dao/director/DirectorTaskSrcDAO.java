package com.sumavision.signal.bvc.entity.dao.director;/**
 * Created by Poemafar on 2020/9/2 20:17
 */

import com.sumavision.signal.bvc.entity.po.director.DirectorTaskPO;
import com.sumavision.signal.bvc.entity.po.director.DirectorTaskSrcPO;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @ClassName:
 * @Description TODO
 * @Author: Mr
 * @Version：1.0
 * @Date 2020/9/2 20:17
 */
@RepositoryDefinition(domainClass = DirectorTaskSrcPO.class, idClass = long.class)
public interface DirectorTaskSrcDAO extends BaseDAO<DirectorTaskSrcPO> {


}
