package com.sumavision.tetris.cms.relation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.cms.article.ArticleQuery;
import com.sumavision.tetris.cms.article.exception.UserHasNotPermissionForArticleException;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.cms.column.ColumnQuery;
import com.sumavision.tetris.cms.column.ColumnVO;
import com.sumavision.tetris.cms.column.exception.UserHasNotPermissionForColumnException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cms/column/relation/article")
public class ColumnRelationArticleController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ColumnQuery columnQuery;
	
	@Autowired
	private ArticleQuery articleQuery;
	
	@Autowired
	private ColumnRelationArticleDAO columnRelationArticleDao;
	
	@Autowired
	private ColumnRelationArticleService columnRelationArticleService;
	
	@Autowired
	private ColumnDAO columnDao;

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
		if(!columnQuery.hasPermission(columnId, user)){
			throw new UserHasNotPermissionForColumnException(columnId, user);
		}
		
		//TODO 权限校验
		
		if(articleIds == null) return null;
		
		return columnRelationArticleService.bindArticle(columnId, JSON.parseArray(articleIds, String.class));
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
		if(!columnQuery.hasPermission(columnId, user)){
			throw new UserHasNotPermissionForColumnException(columnId, user);
		}
		if(!articleQuery.hasPermission(articleId, user)){
			throw new UserHasNotPermissionForArticleException(articleId, user);
		}		
		
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
		if(!columnQuery.hasPermission(columnId, user)){
			throw new UserHasNotPermissionForColumnException(columnId, user);
		}
		if(!articleQuery.hasPermission(articleId, user)){
			throw new UserHasNotPermissionForArticleException(articleId, user);
		}
		
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
		if(!columnQuery.hasPermission(columnId, user)){
			throw new UserHasNotPermissionForColumnException(columnId, user);
		}
		if(!articleQuery.hasPermission(articleId, user)){
			throw new UserHasNotPermissionForArticleException(articleId, user);
		}
		
		System.out.println("send push");
		
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
		if(!columnQuery.hasPermission(columnId, user)){
			throw new UserHasNotPermissionForColumnException(columnId, user);
		}
		if(!articleQuery.hasPermission(articleId, user)){
			throw new UserHasNotPermissionForArticleException(articleId, user);
		}
		
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
		if(!columnQuery.hasPermission(columnId, user)){
			throw new UserHasNotPermissionForColumnException(columnId, user);
		}
		if(!articleQuery.hasPermission(articleId, user)){
			throw new UserHasNotPermissionForArticleException(articleId, user);
		}	
		
		return columnRelationArticleService.bottom(columnId, articleId);
	}
	
	/**
	 * 根据栏目查询文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 上午10:58:46
	 * @param Long columnId 栏目id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long columnId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		if(!columnQuery.hasPermission(columnId, user)){
			throw new UserHasNotPermissionForColumnException(columnId, user);
		}
		
		List<ColumnRelationArticlePO> articles = null;

		articles = columnRelationArticleDao.findByColumnIdOrderByArticleOrder(columnId);
		
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
	
	/**
	 * 推荐栏目文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午3:31:43
	 * @param id
	 * @return ColumnRelationArticleVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/command/{id}")
	public Object command(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ColumnRelationArticlePO relation = columnRelationArticleDao.findOne(id);
		
		if(relation != null){
			relation.setCommand(true);
		}
		
		columnRelationArticleDao.save(relation);
		
		return new ColumnRelationArticleVO().set(relation);
	}
	
	/**
	 * 取消推荐栏目文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午3:31:43
	 * @param id
	 * @return ColumnRelationArticleVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/not/command/{id}")
	public Object notCommand(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ColumnRelationArticlePO relation = columnRelationArticleDao.findOne(id);
		
		if(relation != null){
			relation.setCommand(false);
		}
		
		columnRelationArticleDao.save(relation);
		
		return new ColumnRelationArticleVO().set(relation);
	}
	
	/**
	 * 根据文章查询栏目<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 上午10:58:46
	 * @param Long articleId 文章id
	 * @return List<ColumnVO> 栏目列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/column")
	public Object loadColumn(
			Long articleId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		if(!articleQuery.hasPermission(articleId, user)){
			throw new UserHasNotPermissionForArticleException(articleId, user);
		}	
		
		List<ColumnPO> columns = null;
		List<Long> columnIds = new ArrayList<Long>();

		List<ColumnRelationArticlePO> relations = columnRelationArticleDao.findByArticleId(articleId);
		for(ColumnRelationArticlePO relation: relations){
			columnIds.add(relation.getColumnId());
		}
		
		columns = columnDao.findAll(columnIds);
		
		return ColumnVO.getConverter(ColumnVO.class).convert(columns, ColumnVO.class);
	}	
}
