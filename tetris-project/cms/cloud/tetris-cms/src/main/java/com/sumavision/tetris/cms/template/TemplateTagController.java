package com.sumavision.tetris.cms.template;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cms.template.exception.TemplateTagMoveFailException;
import com.sumavision.tetris.cms.template.exception.TemplateTagNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cms/template/tag")
public class TemplateTagController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TemplateTagQuery templateTagQuery;
	
	@Autowired
	private TemplateTagDAO templateTagDao;
	
	@Autowired
	private TemplateTagService templateTagService;
	
	/**
	 * 查询标签树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午2:55:24
	 * @return List<TemplateTagVO> 标签根列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/tree")
	public Object listTree(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<TemplateTagVO> rootTags = templateTagQuery.queryTagTree();
		
		return rootTags;
	}
	
	/**
	 * 添加根标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:20:37
	 * @return TemplateTagVO 标签
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root")
	public Object addRoot(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO tag = templateTagService.addRoot();
		
		return new TemplateTagVO().set(tag);
	}
	
	/**
	 * 添加一个标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:01:16
	 * @param String remark 备注
	 * @return TemplateTagVO 标签
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append")
	public Object append(
			Long parentId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO parent = templateTagDao.findOne(parentId);
		if(parent == null){
			throw new TemplateTagNotExistException(parentId);
		}
		
		TemplateTagPO tag = templateTagService.append(parent);
		
		return new TemplateTagVO().set(tag);
	}	
	
	/**
	 * 修改标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:29:32
	 * @param String name 标签名称
	 * @param String remark 标签备注
	 * @return TemplateTagVO 标签
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name, 
			String remark, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO tag = templateTagDao.findOne(id);
		if(tag == null){
			throw new TemplateTagNotExistException(id);
		}
		
		tag = templateTagService.update(tag, name, remark);
		
		return new TemplateTagVO().set(tag);
	}
	
	/**
	 * 删除标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:47:29
	 * @param @PathVariable Long id 标签id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO tag = templateTagDao.findOne(id);	
		
		if(tag != null){
			templateTagService.remove(tag);
		}
		
		return null;
	}
	
	/**
	 * 移动标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午4:20:34
	 * @param Long sourceId 被移动的标签id
	 * @param Long targetId 移动的目标标签
	 * @return boolean 移动结果
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object move(
			Long sourceId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO sourceTag = templateTagDao.findOne(sourceId);	
		if(sourceTag == null){
			throw new TemplateTagNotExistException(sourceId);
		}
		
		TemplateTagPO targetTag = templateTagDao.findOne(targetId);
		if(targetTag == null){
			throw new TemplateTagNotExistException(targetId);
		}
		
		if(targetTag.getId().equals(sourceTag.getParentId())){
			return false;
		}
		
		if(targetTag.getParentPath()!=null && targetTag.getParentPath().indexOf(sourceTag.getId().toString())>=0){
			throw new TemplateTagMoveFailException(sourceId, targetId);
		}
		
		templateTagService.move(sourceTag, targetTag);
		
		return true;
	}
	
	/**
	 * 标签置顶<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 上午10:00:23
	 * @param @PathVariable id 标签id
	 * @param boolean 节点是否移动
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/top/{id}")
	public Object top(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		TemplateTagPO tag = templateTagDao.findOne(id);	
		if(tag == null){
			throw new TemplateTagNotExistException(id);
		}
		
		if(tag.getParentId() == null) return false;
		
		templateTagService.top(tag);
		
		return true;
	}
	
}
