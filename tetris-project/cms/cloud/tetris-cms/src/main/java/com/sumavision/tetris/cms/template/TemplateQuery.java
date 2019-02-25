package com.sumavision.tetris.cms.template;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 模板查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月25日 下午4:46:14
 */
@Component
public class TemplateQuery {

	@Autowired
	private TemplateDAO templateDao;
	
	/**
	 * 查询文章排版模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 下午4:46:09
	 * @return List<TemplatePO> 模板列表
	 */
	public List<TemplatePO> queryArticleTemplates(){
		List<TemplatePO> templates = templateDao.findByTypeOrderBySerialAsc(TemplateType.ARTICLE.toString());
		return templates;
	}
	
}
