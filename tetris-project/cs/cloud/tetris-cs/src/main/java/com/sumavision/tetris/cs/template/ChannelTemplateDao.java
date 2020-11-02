package com.sumavision.tetris.cs.template;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ChannelTemplatePO.class, idClass = Long.class)
public interface ChannelTemplateDao extends BaseDAO<ChannelTemplatePO> {

}
