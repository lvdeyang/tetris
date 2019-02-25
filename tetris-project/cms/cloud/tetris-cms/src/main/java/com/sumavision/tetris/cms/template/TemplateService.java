package com.sumavision.tetris.cms.template;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.user.UserVO;

/**
 * 内容模板增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月18日 下午1:31:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TemplateService {

	@Autowired
	private TemplateDAO templateDao;
	
	/**
	 * 添加一个内容模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 下午1:32:31
	 * @param String name 模板名称
	 * @param String type 模板类型
	 * @param String remark 模板备注
	 * @param TemplateTagPO tag 标签
	 * @return TemplatePO 内容模板
	 */
	public TemplatePO add(
			UserVO user,
			String name,
			String type,
			String icon,
			String style,
			Integer serial,
			String remark,
			TemplateTagPO tag) throws Exception{
		
		TemplatePO template = new TemplatePO();
		template.setAuthorId(user.getUuid());
		template.setAuthorName(user.getNickname());
		template.setName(name);
		template.setType(TemplateType.fromName(type));
		template.setIcon(icon);
		template.setStyle(style);
		template.setSerial(serial);
		template.setRemark(remark);
		template.setTemplateTagId(tag==null?null:tag.getId());
		template.setUpdateTime(new Date());
		templateDao.save(template);
		
		return template;
	}
	
}
