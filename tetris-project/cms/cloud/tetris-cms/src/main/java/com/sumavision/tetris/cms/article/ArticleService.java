package com.sumavision.tetris.cms.article;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleDAO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserVO;

/**
 * 文章增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月24日 下午3:59:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService {

	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ArticleQuery articleQuery;
	
	@Autowired
	private Path path;
	
	@Autowired
	private ArticleClassifyPermissionDAO articleClassifyPermissionDao;
	
	@Autowired
	private ArticleRegionPermissionDAO articleRegionPermissionDao;
	
	@Autowired
	private ColumnRelationArticleDAO columnRelationArticleDao;
	
	/**
	 * 添加文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 上午9:02:13
	 * @param UserVO user 用户
	 * @param String name 文章名
	 * @param String remark 备注
	 * @return ArticlePO 文章数据
	 */
	public ArticlePO add(
			UserVO user, 
			String name, 
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			List<JSONObject> classifies,
			List<JSONObject> regions,
			String type) throws Exception{
		
		ArticlePO article = new ArticlePO();
		article.setName(name);
		article.setAuthor(author);
		article.setPublishTime(publishTime);
		article.setThumbnail(thumbnail);
		article.setRemark(remark);
		article.setUpdateTime(new Date());
		article.setType(ArticleType.fromName(type));
		
		String webappPath = path.webappPath();
		String separator = File.separator;
		
		String baseFolder = new StringBufferWrapper().append(webappPath)
													 .append("cms")
													 .append(separator)
													 .append("resource")
													 .append(separator)
													 .append("article")
													 .append(separator)
													 .append(user.getUuid())
													 .append(separator)
													 .toString();
		File folderFile = new File(baseFolder);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		
		String baseUrl = new StringBufferWrapper().append("cms/resource/article/")
												  .append(user.getUuid())
												  .append("/")
												  .toString();
		String fileName = new StringBufferWrapper().append(article.getUuid()).append(".html").toString();
		
		File html = new File(new StringBufferWrapper().append(baseFolder).append(fileName).toString());
		if(!html.exists()) html.createNewFile();
		
		article.setPreviewUrl(new StringBufferWrapper().append(baseUrl).append(fileName).toString());
		article.setStorePath(new StringBufferWrapper().append(baseFolder).append(fileName).toString());
		articleDao.save(article);
		
		//绑定分类
		if(classifies != null && classifies.size() > 0){
			List<ArticleClassifyPermissionPO> articleClassifyPermissions = new ArrayList<ArticleClassifyPermissionPO>();
			for(JSONObject classify: classifies){
				ArticleClassifyPermissionPO bindClassify = new ArticleClassifyPermissionPO();
				bindClassify.setArticleId(article.getId());
				bindClassify.setArticleName(article.getName());
				bindClassify.setClassifyId(classify.getLong("id"));
				bindClassify.setClassifyName(classify.getString("name"));
				articleClassifyPermissions.add(bindClassify);
			}
			articleClassifyPermissionDao.save(articleClassifyPermissions);
		}

		//绑定地区
		if(regions != null && regions.size() > 0){
			List<ArticleRegionPermissionPO> articleRegionPermissions = new ArrayList<ArticleRegionPermissionPO>();
			for(JSONObject region: regions){
				ArticleRegionPermissionPO bindRegion = new ArticleRegionPermissionPO();
				bindRegion.setArticleId(article.getId());
				bindRegion.setArticleName(article.getName());
				bindRegion.setRegionId(region.getLong("id"));
				bindRegion.setRegionName(region.getString("name"));
				articleRegionPermissions.add(bindRegion);
			}
			articleRegionPermissionDao.save(articleRegionPermissions);
		}
		
		return article;
	}
	
	/**
	 * 修改文章元数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 上午9:04:07
	 * @param ArticlePO article 文章
	 * @param String name 文章名称
	 * @param String remark 备注
	 * @return ArticlePO 文章
	 */
	public ArticlePO edit(
			ArticlePO article, 
			String name, 
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			List<JSONObject> classifies,
			List<JSONObject> regions,
			String type) throws Exception{
		
		article.setName(name);
		article.setAuthor(author);
		article.setPublishTime(publishTime);
		article.setThumbnail(thumbnail);
		article.setRemark(remark);
		article.setUpdateTime(new Date());
		article.setType(ArticleType.fromName(type));
		articleDao.save(article);
		
		List<Long> articleIds = new ArrayList<Long>();
		articleIds.add(article.getId());
		
		List<ArticleClassifyPermissionPO> classifyPermissions = articleClassifyPermissionDao.findByArticleIdIn(articleIds);
		List<ArticleRegionPermissionPO> regionPermissions = articleRegionPermissionDao.findByArticleIdIn(articleIds);

		//对比增删
		List<ArticleClassifyPermissionPO> needRemoveClassifies = new ArrayList<ArticleClassifyPermissionPO>();
		List<JSONObject> needAddClassifies = new ArrayList<JSONObject>();
		List<ArticleRegionPermissionPO> needRemoveRegions = new ArrayList<ArticleRegionPermissionPO>();
		List<JSONObject> needAddRegions = new ArrayList<JSONObject>();
		
		if(classifyPermissions != null && classifyPermissions.size()>0){
			for(ArticleClassifyPermissionPO classifyPermission: classifyPermissions){
				boolean needRemove = true;
				for(JSONObject classify: classifies){
					if(classify.getLong("id").equals(classifyPermission.getClassifyId()) && classify.getString("name").equals(classifyPermission.getClassifyName())){
						needRemove = false;
						break;
					}
				}
				if(needRemove){
					needRemoveClassifies.add(classifyPermission);
				}
			}
		}
		
		if(classifies != null && classifies.size() > 0){
			for(JSONObject classify: classifies){
				boolean needAdd = true;
				for(ArticleClassifyPermissionPO classifyPermission: classifyPermissions){
					if(classify.getLong("id").equals(classifyPermission.getClassifyId()) && classify.getString("name").equals(classifyPermission.getClassifyName())){
						needAdd = false;
						break;
					}
				}
				if(needAdd){
					needAddClassifies.add(classify);
				}
			}
		}
		
		if(regionPermissions != null && regionPermissions.size()>0){
			for(ArticleRegionPermissionPO regionPermission: regionPermissions){
				boolean needRemove = true;
				for(JSONObject region: regions){
					if(region.getLong("id").equals(regionPermission.getRegionId()) && region.getString("name").equals(regionPermission.getRegionName())){
						needRemove = false;
						break;
					}
				}
				if(needRemove){
					needRemoveRegions.add(regionPermission);
				}
			}
		}
		
		if(regions != null && regions.size() > 0){
			for(JSONObject region: regions){
				boolean needAdd = true;
				for(ArticleRegionPermissionPO regionPermission: regionPermissions){
					if(region.getLong("id").equals(regionPermission.getRegionId()) && region.getString("name").equals(regionPermission.getRegionName())){
						needAdd = false;
						break;
					}
				}
				if(needAdd){
					needAddRegions.add(region);
				}
			}
		}
		
		articleClassifyPermissionDao.deleteInBatch(needRemoveClassifies);
		articleRegionPermissionDao.deleteInBatch(needRemoveRegions);
		
		//绑定分类
		if(needAddClassifies != null && needAddClassifies.size() > 0){
			List<ArticleClassifyPermissionPO> articleClassifyPermissions = new ArrayList<ArticleClassifyPermissionPO>();
			for(JSONObject classify: needAddClassifies){
				ArticleClassifyPermissionPO bindClassify = new ArticleClassifyPermissionPO();
				bindClassify.setArticleId(article.getId());
				bindClassify.setArticleName(article.getName());
				bindClassify.setClassifyId(classify.getLong("id"));
				bindClassify.setClassifyName(classify.getString("name"));
				articleClassifyPermissions.add(bindClassify);
			}
			articleClassifyPermissionDao.save(articleClassifyPermissions);
		}

		//绑定地区
		if(needAddRegions != null && needAddRegions.size() > 0){
			List<ArticleRegionPermissionPO> articleRegionPermissions = new ArrayList<ArticleRegionPermissionPO>();
			for(JSONObject region: needAddRegions){
				ArticleRegionPermissionPO bindRegion = new ArticleRegionPermissionPO();
				bindRegion.setArticleId(article.getId());
				bindRegion.setArticleName(article.getName());
				bindRegion.setRegionId(region.getLong("id"));
				bindRegion.setRegionName(region.getString("name"));
				articleRegionPermissions.add(bindRegion);
			}
			articleRegionPermissionDao.save(articleRegionPermissions);
		}
		
		return article;
	}
	
	/**
	 * 删除文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 上午9:47:51
	 * @param ArticlePO article 待删除的文章
	 */
	public void remove(ArticlePO article){
		if(article != null){
			
			Set<Long> ids = new HashSet<Long>();
			ids.add(article.getId());
			
			//删文件
			String storePath = article.getStorePath();
			File html = new File(storePath);
			if(html.exists()){
				html.delete();
			}
			
			//删关联
			articleClassifyPermissionDao.deleteByArticleIdIn(ids);
			articleRegionPermissionDao.deleteByArticleIdIn(ids);
			columnRelationArticleDao.deleteByArticleIdIn(ids);
			
			articleDao.delete(article);
		}
	}
	
	/**
	 * 保存文章内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 上午10:57:29
	 * @param ArticlePO article 文章
	 * @param String html html内容
	 * @param JSONString modules 文章排版内容json
	 * @return ArticlePO 文章
	 */
	public ArticlePO save(ArticlePO article, String html, String modules) throws Exception{
		
		File file = new File(article.getStorePath());
		FileUtils.writeStringToFile(file, articleQuery.generateHtml(html, "", ""));
		article.setModules(modules);
		article.setUpdateTime(new Date());
		articleDao.save(article);
		
		return article;
	}
	
}
