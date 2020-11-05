package com.sumavision.signal.bvc.entity.dao;/**
 * Created by Poemafar on 2020/9/2 20:17
 */

import com.sumavision.signal.bvc.entity.po.ProgramPO;
import com.sumavision.signal.bvc.entity.po.SourcePO;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

/**
 * @ClassName: AccessBundleDAO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 20:17
 */
@RepositoryDefinition(domainClass = SourcePO.class, idClass = long.class)
public interface SourceDAO extends BaseDAO<SourcePO> {


}
