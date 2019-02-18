package com.sumavision.tetris.cms.template;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cms.template.exception.TemplateNotExistException;
import com.sumavision.tetris.cms.template.exception.TemplateTagNotExistException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;


@Controller
@RequestMapping(value = "/cms/template")
public class TemplateController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TemplateDAO templateDao;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private TemplateTagDAO templateTagDao;
	
	/**
	 * 查询类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 上午11:55:07
	 * @return Set<String> 类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateType[] values = TemplateType.values();
		
		Set<String> types = new HashSet<String>();
		for(TemplateType value:values){
			types.add(value.getName());
		}
		
		return types;
	}
	
	/**
	 * 根据标签查询模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 上午10:58:46
	 * @param Long tagId 标签id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long tagId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<TemplatePO> templates = null;
		
		if(tagId.longValue() == -1l){
			templates = templateDao.findByTemplateTagIdIsNull();
		}else{
			templates = templateDao.findByTemplateTagIdIn(new ArrayListWrapper<Long>().add(tagId).getList());
		}
		
		return TemplateVO.getConverter(TemplateVO.class).convert(templates, TemplateVO.class);
	}
	
	/**
	 * 添加一个内容模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 下午1:16:35
	 * @param String name 模板名称
	 * @param String type 模板类型
	 * @param String remark 模板备注
	 * @param String tagId 标签id
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String type,
			String remark,
			Long tagId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO tag = templateTagDao.findOne(tagId);
		
		TemplatePO template = templateService.add(name, type, remark, tag);
		
		return new TemplateVO().set(template);
	}
	
	/**
	 * 删除一个内容模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 下午2:11:48
	 * @param @PathVariable Long id 模板id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplatePO template = templateDao.findOne(id);
		
		if(template != null){
			templateDao.delete(template);
		}
		
		return null;
	}
	
	/**
	 * 修改模板元数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 下午2:35:18
	 * @param @PathVariable Long id 模板id
	 * @param String name 模板名称
	 * @param String type 模板类型
	 * @param String remark 备注
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String type,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplatePO template = templateDao.findOne(id);
		
		if(template == null){
			throw new TemplateNotExistException(id);
		}
		
		template.setName(name);
		template.setType(TemplateType.fromName(type));
		template.setRemark(remark);
		templateDao.save(template);
		
		return new TemplateVO().set(template);
	}
	
	/**
	 * 修改模板标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 下午3:41:35
	 * @param @PathVariable Long id 模板id
	 * @param Long tagId 标签id
	 * @param boolean 是否移动
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/tag/{id}")
	public Object changeTag(
			@PathVariable Long id,
			Long tagId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplatePO template = templateDao.findOne(id);
		if(template == null){
			throw new TemplateNotExistException(id);
		}
		
		if(tagId.longValue() == -1){
			if(template.getTemplateTagId() == null){
				return false;
			}else{
				template.setTemplateTagId(null);
				templateDao.save(template);
			}
		}else{
			TemplateTagPO tag = templateTagDao.findOne(tagId);
			if(tag == null) throw new TemplateTagNotExistException(tagId);
			if(tag.getId().equals(template.getTemplateTagId())){
				return false;
			}else{
				template.setTemplateTagId(tag.getId());
				templateDao.save(template);
			}
		}
		
		return true;
	}
	
}
