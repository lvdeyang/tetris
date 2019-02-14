package com.sumavision.tetris.cms.template;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TemplateTagPO.class, idClass = Long.class)
public interface TemplateTagDAO extends BaseDAO<TemplateTagPO>{

	/**
	 * 查询标签下的所有子标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:56:01
	 * @param String reg1 '%/id'
	 * @param String reg2 '%/id/%'
	 * @return List<TemplateTagPO> 标签列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CMS_TEMPLATE_TAG WHERE PARENT_PATH LIKE ?1 OR PARENT_PATH LIKE ?2", nativeQuery = true)
	public List<TemplateTagPO> findAllSubTags(String reg1, String reg2);
	
}
