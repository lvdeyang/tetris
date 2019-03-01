package com.sumavision.tetris.cms.article;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ArticleClassifyPermissionPO.class, idClass = Long.class)
public interface ArticleClassifyPermissionDAO extends BaseDAO<ArticleClassifyPermissionPO>{

}