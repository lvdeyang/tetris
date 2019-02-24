package com.sumavision.tetris.cms.article;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		String baseFolder = "";
		
		ArticlePO article = new ArticlePO();
		article.setName(name);
		article.setRemark(remark);
		article.setUpdateTime(new Date());
		articleDao.save(article);
		
		return new ArticleVO().set(article);
	}
	
}
