package com.sumavision.tetris.cms.template;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
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
	
	/**
	 * 查询没有打标签的内容模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 上午11:04:33
	 * @return List<TemplatePO> 内容模板列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CMS_TEMPLATE WHERE TEMPLATE_TAG_ID IS NULL", nativeQuery = true)
	public List<TemplatePO> findByTemplateTagIdIsNull();
	
	/**
	 * 根据类型查询模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 下午4:43:13
	 * @param String type 模板类型
	 * @return List<TemplatePO> 模板列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CMS_TEMPLATE WHERE TYPE=?1 ORDER BY SERIAL ASC", nativeQuery = true)
	public List<TemplatePO> findByTypeOrderBySerialAsc(String type);

}
