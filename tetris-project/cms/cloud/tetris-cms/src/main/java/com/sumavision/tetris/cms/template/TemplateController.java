package com.sumavision.tetris.cms.template;

import java.util.Date;
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
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
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
	
	@Autowired
	private TemplateQuery templateQuery;
	
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
	 * 查询templateId列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月19日 下午3:27:45
	 * @return Set<String> templateId列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/ids")
	public Object queryIds(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateId[] values = TemplateId.values();
		
		Set<String> ids = new HashSet<String>();
		for(TemplateId value:values){
			ids.add(value.getName());
		}
		
		return ids;
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
			String templateId,
			String icon,
			String style,
			Integer serial,
			String remark,
			Long tagId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO tag = templateTagDao.findById(tagId);
		
		TemplatePO template = templateService.add(user, name, type, templateId, icon, style, serial, remark, tag);
		
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
		
		TemplatePO template = templateDao.findById(id);
		
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
			String icon,
			String style,
			Integer serial,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplatePO template = templateDao.findById(id);
		
		if(template == null){
			throw new TemplateNotExistException(id);
		}
		
		template.setName(name);
		template.setType(TemplateType.fromName(type));
		template.setIcon(icon);
		template.setStyle(style);
		template.setSerial(serial);
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
		
		TemplatePO template = templateDao.findById(id);
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
			TemplateTagPO tag = templateTagDao.findById(tagId);
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
	
	/**
	 * 保存模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月22日 下午4:41:56
	 * @param @PathVariable Long id 模板id
	 * @param String html 模板内容
	 * @param JSONString js 变量描述
	 * @return TemplateVO 模板数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/{id}")
	public Object save(
			@PathVariable Long id,
			String html,
			String js,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplatePO template = templateDao.findById(id);
		if(template == null){
			throw new TemplateNotExistException(id);
		}
		
		template.setHtml(html);
		template.setJs(js);
		template.setUpdateTime(new Date());
		templateDao.save(template);
		
		return new TemplateVO().set(template);
	}
	
	/**
	 * 查询模板内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月24日 上午11:49:56
	 * @param @PathVariable Long id 模板id
	 * @return String html
	 * @return String js 
	 * @return String css
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/content/{id}")
	public Object content(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplatePO template = templateDao.findById(id);
		if(template == null){
			throw new TemplateNotExistException(id);
		}
		
		String html = template.getHtml();
		String js = template.getJs();
		String css = template.getCss();
		
		return new HashMapWrapper<String, String>().put("html", html)
												   .put("css", css)
												   .put("js", js)
												   .getMap();
	}
	
	/**
	 * 查询文章模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 下午4:49:41
	 * @return List<TemplateVO> 文章模板列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/article/templates")
	public Object queryArticleTemplates(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<TemplatePO> templates = templateQuery.queryArticleTemplates();
		
		List<TemplateVO> view_templates = TemplateVO.getConverter(TemplateVO.class).convert(templates, TemplateVO.class);
		
		return view_templates;
	}
	
}
