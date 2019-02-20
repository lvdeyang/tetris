package com.sumavision.tetris.cms.column;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.cms.template.TemplatePO;
import com.sumavision.tetris.cms.template.TemplateTagPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ColumnPO.class, idClass = Long.class)
public interface ColumnDAO extends BaseDAO<ColumnPO>{

	
	@Query(value = "SELECT * FROM TETRIS_CMS_COLUMN WHERE PARENT_PATH LIKE ?1 OR PARENT_PATH LIKE ?2", nativeQuery = true)
	public List<ColumnPO> findAllSubColumns(String reg1, String reg2);
}
