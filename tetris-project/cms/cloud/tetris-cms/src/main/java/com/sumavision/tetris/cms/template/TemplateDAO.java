package com.sumavision.tetris.cms.template;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TemplatePO.class, idClass = Long.class)
public interface TemplateDAO extends BaseDAO<TemplatePO>{

	/**
	 * 根据标签获取内容模板（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午4:05:34
	 * @param Collection<Long> templateTagIds 标签id列表
	 * @return List<TemplatePO> 内容模板列表
	 */
	public List<TemplatePO> findByTemplateTagIdIn(Collection<Long> templateTagIds);
	
}
