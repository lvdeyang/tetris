package com.sumavision.tetris.cms.article;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cms.article.exception.ArticleNotExistException;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cms/article")
public class ArticleController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ArticleQuery articleQuery;
	
	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ArticleService articleService;
	
	/**
	 * 查询所有的文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月24日 下午4:06:29
	 * @return rows List<ArticleVO> 文章列表
	 * @return total long 文章数量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<ArticlePO> entities = articleQuery.findAll(currentPage, pageSize);
		
		List<ArticleVO> articles = ArticleVO.getConverter(ArticleVO.class).convert(entities, ArticleVO.class);
		
		Long total = articleDao.count();
		
		return new HashMapWrapper<String, Object>().put("rows", articles)
												   .put("total", total)
												   .getMap();
	}
	
	/**
	 * 添加文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 上午8:57:18
	 * @param String name 文章名称
	 * @param String remark 备注
	 * @return ArticleVO 文章数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ArticlePO article = articleService.add(user, name, remark);
		
		return new ArticleVO().set(article);
	}
	
	/**
	 * 修改文章元数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 上午8:59:48
	 * @param @PathVariable Long id 文章id
	 * @param String name 文章名称
	 * @param String remark 备注
	 * @return ArticleVO 文章数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ArticlePO article = articleDao.findOne(id);
		if(article == null){
			throw new ArticleNotExistException(id);
		}
		
		article = articleService.edit(article, name, remark);
		
		return new ArticleVO().set(article);
	}
	
	/**
	 * 删除文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 上午9:49:01
	 * @param @PathVariable Long id 文章id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ArticlePO article = articleDao.findOne(id);
		
		articleService.remove(article);
		
		return null;
	}
	
	/**
	 * 查询文章排版内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 上午10:40:49
	 * @param @PathVariable Long id 文章id
	 * @return JSONString 排版内容json
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/modules/{id}")
	public Object queryModules(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ArticlePO article = articleDao.findOne(id);
		if(article == null){
			throw new ArticleNotExistException(id);
		}
		
		return article.getModules();
	}
	
	/**
	 * 保存文章内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 上午10:59:33
	 * @param @PathVariable Long id 文章id
	 * @param String html html内容
	 * @param JSONString modules 模板配置内容
	 * @return ArticleVO 文章
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/{id}")
	public Object save(
			@PathVariable Long id,
			String html,
			String modules,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ArticlePO article = articleDao.findOne(id);
		if(article == null){
			throw new ArticleNotExistException(id);
		}
		
		article = articleService.save(article, html, modules);
		
		return new ArticleVO().set(article);
	}
	
}
