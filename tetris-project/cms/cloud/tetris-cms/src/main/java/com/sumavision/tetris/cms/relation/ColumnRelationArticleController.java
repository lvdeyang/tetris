package com.sumavision.tetris.cms.relation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cms/column/relation/article")
public class ColumnRelationArticleController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ColumnRelationArticleDAO columnRelationArticleDao;
	
	@Autowired
	private ColumnRelationArticleService columnRelationArticleService;

	/**
	 * 栏目绑定文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param String articleIds 文章id数组[id, id]
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bind")
	public Object bind(
			Long columnId, 
			String articleIds, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(articleIds == null) return null;
		
		return columnRelationArticleService.bind(columnId, JSON.parseArray(articleIds, String.class));
	}
	
	/**
	 * 栏目文章上移<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/up")
	public Object up(
			Long columnId, 
			Long articleId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return columnRelationArticleService.up(columnId, articleId);
	}
	
	/**
	 * 栏目文章下移<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/down")
	public Object down(
			Long columnId, 
			Long articleId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return columnRelationArticleService.down(columnId, articleId);
	}
	
	/**
	 * 栏目文章下移<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/inform")
	public Object inform(
			Long columnId, 
			Long articleId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		columnRelationArticleService.inform(columnId, articleId);
		
		return null;
	}
	
	/**
	 * 栏目文章下移<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/top")
	public Object top(
			Long columnId, 
			Long articleId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return columnRelationArticleService.top(columnId, articleId);
	}
	
	/**
	 * 栏目文章置底<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bottom")
	public Object bottom(
			Long columnId, 
			Long articleId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return columnRelationArticleService.bottom(columnId, articleId);
	}
	
	/**
	 * 根据标签查询文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 上午10:58:46
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
		
		List<ColumnRelationArticlePO> articles = null;

		articles = columnRelationArticleDao.findByColumnIdOrderByArticleOrder(tagId);
		
		return ColumnRelationArticleVO.getConverter(ColumnRelationArticleVO.class).convert(articles, ColumnRelationArticleVO.class);
	}
	
	/**
	 * 下架一个文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午2:11:48
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
		
		ColumnRelationArticlePO relation = columnRelationArticleDao.findOne(id);
		
		if(relation != null){
			columnRelationArticleDao.delete(relation);
		}
		
		return null;
	}
}
