package com.sumavision.tetris.cms.template;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cms.template.exception.TemplateNotExistException;

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
	
	/**
	 * 根据模板额外用途查询模板<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 上午8:54:28
	 * @param templates 所有模板
	 * @param templateId 模板额外用途
	 * @return TemplatePO 模板信息
	 */
	public TemplatePO queryTemplateByTemplateId(List<TemplatePO> templates, String templateId) throws Exception{
		for(TemplatePO template: templates){
			if(template.getTemplateId().equals(TemplateId.fromType(templateId))){
				return template;
			}
		}
		
		throw new TemplateNotExistException(templateId);
	}
	
}
