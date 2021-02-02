package com.sumavision.signal.bvc.director.dao;/**
 * Created by Poemafar on 2020/9/2 20:17
 */

import com.sumavision.signal.bvc.director.po.DirectorTaskSrcPO;
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
@RepositoryDefinition(domainClass = DirectorTaskSrcPO.class, idClass = long.class)
public interface DirectorTaskSrcDAO extends BaseDAO<DirectorTaskSrcPO> {

    public List<DirectorTaskSrcPO>  findByBundleIdAndTaskId(String BundleId,Long taskId);

    public void deleteByTaskId(Long taskId);
}
