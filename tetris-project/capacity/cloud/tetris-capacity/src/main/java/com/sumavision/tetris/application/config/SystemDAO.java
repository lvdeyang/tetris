package com.sumavision.tetris.application.config;/**
 * Created by Poemafar on 2021/1/8 15:17
 */

import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @ClassName: SystemDAO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/1/8 15:17
 */
@RepositoryDefinition(domainClass = SystemPO.class, idClass = Long.class)
public interface SystemDAO extends BaseDAO<SystemPO> {
}
