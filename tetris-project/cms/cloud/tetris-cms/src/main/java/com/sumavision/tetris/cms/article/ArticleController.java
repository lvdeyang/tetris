package com.sumavision.tetris.cms.article;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.article.exception.ArticleNotExistException;
import com.sumavision.tetris.cms.classify.ClassifyVO;
import com.sumavision.tetris.cms.region.RegionVO;
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
	
	@Autowired
	private ArticleClassifyPermissionDAO articleClassifyPermissionDao;
	
	@Autowired
	private ArticleRegionPermissionDAO articleRegionPermissionDao;
	
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
		List<Long> articleIds = new ArrayList<Long>();
		for(ArticlePO entity: entities){
			articleIds.add(entity.getId());
		}
		List<ArticleClassifyPermissionPO> classifies = articleClassifyPermissionDao.findByArticleIdIn(articleIds);
		List<ArticleRegionPermissionPO> regions = articleRegionPermissionDao.findByArticleIdIn(articleIds);
		
		List<ArticleVO> articles = ArticleVO.getConverter(ArticleVO.class).convert(entities, ArticleVO.class);
		for(ArticleVO article: articles){
			article.setClassifies(new ArrayList<ClassifyVO>())
				   .setRegions(new ArrayList<RegionVO>());
			//分类
			for(ArticleClassifyPermissionPO classify: classifies){
				if(classify.getArticleId().equals(article.getId())){
					ClassifyVO classifyVO = new ClassifyVO().setId(classify.getClassifyId())
															.setName(classify.getClassifyName());
					article.getClassifies().add(classifyVO);
				}
			}
			//地区
			for(ArticleRegionPermissionPO region: regions){
				if(region.getArticleId().equals(article.getId())){
					RegionVO regionVO = new RegionVO().setId(region.getRegionId())
													  .setName(region.getRegionName());
					article.getRegions().add(regionVO);
				}
			}
		}
		
		Long total = articleDao.count();
		
		return new HashMapWrapper<String, Object>().put("rows", articles)
												   .put("total", total)
												   .getMap();
	}
	
	/**
	 * 查询文章类型<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 上午10:11:08
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/type")
	public Object queryType(HttpServletRequest request) throws Exception{
		
		//查询文章类型
		Set<String> articleTypes = new HashSet<String>();
		ArticleType[] types = ArticleType.values();
		for(ArticleType type: types){
			articleTypes.add(type.getName());
		}
		
		return new HashMapWrapper<String, Object>().put("type", articleTypes)
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
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			String classify,
			String region,
			String type,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		List<JSONObject> classifies = JSONArray.parseArray(classify, JSONObject.class);
		List<JSONObject> regions = JSONArray.parseArray(region, JSONObject.class);
		
		ArticlePO article = articleService.add(user, name, author, publishTime, thumbnail, remark, classifies, regions, type);
		
		List<Long> articleIds = new ArrayList<Long>();
		articleIds.add(article.getId());
		
		List<ArticleClassifyPermissionPO> classifyPermissions = articleClassifyPermissionDao.findByArticleIdIn(articleIds);
		List<ArticleRegionPermissionPO> regionPermissions = articleRegionPermissionDao.findByArticleIdIn(articleIds);

		ArticleVO articleVO = new ArticleVO().set(article)
											 .setClassifies(new ArrayList<ClassifyVO>())
										 	 .setRegions(new ArrayList<RegionVO>());
		//分类
		for(ArticleClassifyPermissionPO classifyPermission: classifyPermissions){
			if(classifyPermission.getArticleId().equals(article.getId())){
				ClassifyVO classifyVO = new ClassifyVO().setId(classifyPermission.getClassifyId())
														.setName(classifyPermission.getClassifyName());
				articleVO.getClassifies().add(classifyVO);
			}
		}
		//地区
		for(ArticleRegionPermissionPO regionPermission: regionPermissions){
			if(regionPermission.getArticleId().equals(article.getId())){
				RegionVO regionVO = new RegionVO().setId(regionPermission.getRegionId())
												  .setName(regionPermission.getRegionName());
				articleVO.getRegions().add(regionVO);
			}
		}
		
		return articleVO;
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
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			String classify,
			String region,
			String type,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ArticlePO article = articleDao.findOne(id);
		if(article == null){
			throw new ArticleNotExistException(id);
		}
		
		List<JSONObject> classifies = JSONArray.parseArray(classify, JSONObject.class);
		List<JSONObject> regions = JSONArray.parseArray(region, JSONObject.class);
		
		article = articleService.edit(article, name, author, publishTime, thumbnail, remark, classifies, regions, type);
		List<Long> articleIds = new ArrayList<Long>();
		articleIds.add(article.getId());
		
		List<ArticleClassifyPermissionPO> classifyPermissions = articleClassifyPermissionDao.findByArticleIdIn(articleIds);
		List<ArticleRegionPermissionPO> regionPermissions = articleRegionPermissionDao.findByArticleIdIn(articleIds);

		ArticleVO articleVO = new ArticleVO().set(article)
											 .setClassifies(new ArrayList<ClassifyVO>())
										 	 .setRegions(new ArrayList<RegionVO>());
		//分类
		for(ArticleClassifyPermissionPO classifyPermission: classifyPermissions){
			if(classifyPermission.getArticleId().equals(article.getId())){
				ClassifyVO classifyVO = new ClassifyVO().setId(classifyPermission.getClassifyId())
														.setName(classifyPermission.getClassifyName());
				articleVO.getClassifies().add(classifyVO);
			}
		}
		//地区
		for(ArticleRegionPermissionPO regionPermission: regionPermissions){
			if(regionPermission.getArticleId().equals(article.getId())){
				RegionVO regionVO = new RegionVO().setId(regionPermission.getRegionId())
												  .setName(regionPermission.getRegionName());
				articleVO.getRegions().add(regionVO);
			}
		}
		
		return articleVO;
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
	
	/**
	 * 分页文章用户（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:33:31
	 * @param JSONString except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<ArticleVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/except")
	public Object listWithExceptIds(
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(except == null){
			return articleQuery.list(currentPage, pageSize);
		}else{
			List<Long> exceptIds = JSON.parseArray(except, Long.class);
			return articleQuery.listWithExcept(exceptIds, currentPage, pageSize);
		}
		
	}
	
}
