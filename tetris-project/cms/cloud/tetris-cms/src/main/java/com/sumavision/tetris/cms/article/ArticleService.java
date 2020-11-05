package com.sumavision.tetris.cms.article;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.cms.region.RegionDAO;
import com.sumavision.tetris.cms.region.RegionPO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleDAO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticlePO;
import com.sumavision.tetris.cms.template.TemplateDAO;
import com.sumavision.tetris.cms.template.TemplatePO;
import com.sumavision.tetris.cms.template.TemplateQuery;
import com.sumavision.tetris.cms.template.TemplateType;
import com.sumavision.tetris.cms.template.TemplateVO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
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
	
	@Autowired
	private ArticleUserPermissionDAO articleUserPermissionDao;
	
	@Autowired
	private TemplateDAO templateDao;
	
	@Autowired
	private TemplateQuery templateQuery;
	
	@Autowired
	private ColumnDAO columnDao;
	
	@Autowired
	private RegionDAO regionDao;
	
	@Autowired
	private ArticleMediaPermissionDAO articleMediaPermissionDao;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;

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
	public ArticlePO generateWithInternalTemplate(		
			List<String> columns,
			String name, 
			String type,
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			String keywords,
			Boolean command,
			List<JSONObject> contents,
			List<String> regions,
			UserVO user,
			Boolean... ifLive) throws Exception{
		
		ArticlePO article = new ArticlePO();
		article.setName(name);
		article.setAuthor(author);
		article.setPublishTime(publishTime);
		article.setThumbnail(thumbnail);
		article.setRemark(remark);
		article.setKeywords(keywords);
		article.setUpdateTime(new Date());
		article.setType(ArticleType.valueOf(type));
		if (ifLive != null && ifLive.length > 0) {
			article.setIfLive(ifLive[0]);
		}
		
		String webappPath = path.webappPath();
		String separator = File.separator;
		
		String baseFolder = new StringBufferWrapper().append(webappPath)
													 .append("cms")
													 .append(separator)
													 .append("resource")
													 .append(separator)
													 .append("article")
													 .append(separator)
													 .append("yjgb")
													 .append(separator)
													 .append("internal")
													 .append(separator)
													 .toString();
		File folderFile = new File(baseFolder);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		
		String baseUrl = new StringBufferWrapper().append("cms/resource/article/yjgb/internal")
												  .append("/")
												  .toString();
		String fileName = new StringBufferWrapper().append(article.getUuid()).append(".html").toString();
		
		File html = new File(new StringBufferWrapper().append(baseFolder).append(fileName).toString());
		if(!html.exists()) html.createNewFile();
		
		article.setPreviewUrl(new StringBufferWrapper().append(baseUrl).append(fileName).toString());
		article.setStorePath(new StringBufferWrapper().append(baseFolder).append(fileName).toString());
		articleDao.save(article);
		
		//查询所有内置模板
		StringBufferWrapper allHtml = new StringBufferWrapper();
		JSONArray moduleJsons = new JSONArray();
		List<TemplatePO> templates = templateDao.findByTypeOrderBySerialAsc(TemplateType.INTERNAL.toString());
		for(JSONObject content: contents){
			String contentType = content.getString("type");
			String contentValue = content.getString("value");
			
			TemplatePO template = templateQuery.queryTemplateByTemplateId(templates, contentType);
			TemplateVO view_template = new TemplateVO().setWithHtmlAndJs(template);
			String contentHtml = template.getHtml();
			String contentJs = template.getJs();
			JSONObject jsJson = JSONObject.parseArray(contentJs, JSONObject.class).get(0);
			jsJson.put("value", contentValue);
			String jsKey = jsJson.getString("key");
			
			JSONArray jsArray = new JSONArray();
			jsArray.add(jsJson);
			view_template.setJs(jsArray.toJSONString());
			
			String newHtml = contentHtml.replace("${"+jsKey+"}", contentValue);
			allHtml.append(newHtml);
			
			JSONObject module = new JSONObject();
			module.put("id", "module" + jsKey);
			module.put("template", view_template);
			module.put("render", null);
			module.put("mousein", null);
			
			moduleJsons.add(module);
		}
		
		File file = new File(article.getStorePath());
		FileUtils.writeStringToFile(file, articleQuery.generateHtml(allHtml.toString(), "", ""));
		article.setModules(moduleJsons.toJSONString());
		article.setUpdateTime(new Date());
		articleDao.save(article);		
		
		//绑定地区
		if(regions != null && regions.size() > 0){
			List<RegionPO> allRegions = regionDao.findByCodeIn(regions);
			List<ArticleRegionPermissionPO> articleRegionPermissions = new ArrayList<ArticleRegionPermissionPO>();
			for(RegionPO regionPO: allRegions){
				ArticleRegionPermissionPO bindRegion = new ArticleRegionPermissionPO();
				bindRegion.setArticleId(article.getId());
				bindRegion.setArticleName(article.getName());
				bindRegion.setRegionId(regionPO.getId());
				bindRegion.setRegionName(regionPO.getName());
				articleRegionPermissions.add(bindRegion);
			}
			articleRegionPermissionDao.save(articleRegionPermissions);
		}		
		
		//绑定栏目
		if(columns != null && columns.size() > 0){
			List<ColumnPO> allColumns = columnDao.findByCodeIn(columns);
			List<ColumnRelationArticlePO> relations = columnRelationArticleDao.findByColumnCodeIn(columns);
			List<ColumnRelationArticlePO> columnRelationArticles = new ArrayList<ColumnRelationArticlePO>();
			for(ColumnPO columnPO: allColumns){
				
				Long tag = 0l;
				
				List<ColumnRelationArticlePO> exsitRelations = queryRelationsByColumnCode(relations, columnPO.getCode());		
				if(exsitRelations != null && exsitRelations.size()>0){
					Collections.sort(exsitRelations, new ColumnRelationArticlePO.ArticleOrderComparator());
					tag = exsitRelations.get(0).getArticleOrder();
				}
				
				ColumnRelationArticlePO bindColumn = new ColumnRelationArticlePO();
				bindColumn.setArticleId(article.getId());
				bindColumn.setArticleName(article.getName());
				bindColumn.setCommand(command);
				bindColumn.setArticleOrder(++tag);
				bindColumn.setColumnId(columnPO.getId());
				bindColumn.setColumnName(columnPO.getName());
				bindColumn.setColumnCode(columnPO.getCode());
				columnRelationArticles.add(bindColumn);
			}
			columnRelationArticleDao.save(columnRelationArticles);
		}
		
		addPermission(user, article);
		
		//创建文章媒资关联
		analyseMediasFromModules(article.getId(), JSONArray.parseArray(article.getModules()));
		
		return article;
	}
	
	/**
	 * 根据栏目编号筛选关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 下午5:38:37
	 * @param relations 关联表
	 * @param code 栏目编号
	 * @return List<ColumnRelationArticlePO>
	 */
	public List<ColumnRelationArticlePO> queryRelationsByColumnCode(List<ColumnRelationArticlePO> relations, String code){
		List<ColumnRelationArticlePO> needRelations = new ArrayList<ColumnRelationArticlePO>();
		for(ColumnRelationArticlePO relation: relations){
			if(relation.getColumnCode().equals(code)){
				needRelations.add(relation);
			}
		}
		return needRelations;
	}
	
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
		
		addPermission(user, article);
		
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
			articleUserPermissionDao.deleteByArticleIdIn(ids);
			articleMediaPermissionDao.deleteByArticleId(article.getId());
			
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
		
		//创建文章媒资关联
		analyseMediasFromModules(article.getId(), JSONArray.parseArray(article.getModules()));
		
		return article;
	}
	
	/**
	 * 添加文章用户关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午3:30:46
	 * @param user 用户
	 * @param article 文章
	 * @return ArticleUserPermissionPO 文章用户关联
	 */
	public ArticleUserPermissionPO addPermission(UserVO user, ArticlePO article) throws Exception{
		
		ArticleUserPermissionPO permission = new ArticleUserPermissionPO();
		permission.setArticleId(article.getId());
		permission.setUserId(user.getUuid());
		permission.setGroupId(user.getGroupId());
		
		articleUserPermissionDao.save(permission);
		
		return permission;
	}
	
	/**
	 * 分析并创建文章和媒资关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午1:47:33
	 * @param Long articleId 文章id
	 * @param JSONArray modules 文章内容
	 */
	private void analyseMediasFromModules(Long articleId, JSONArray modules) throws Exception{
		List<ArticleMediaPermissionPO> permissions = articleMediaPermissionDao.findByArticleId(articleId);
		if(permissions!=null && permissions.size()>0){
			articleMediaPermissionDao.deleteInBatch(permissions);
		}
		
		Set<String> linkSuffixes = new HashSet<String>();
		Set<String> links = new HashSet<String>();
		for(int i=0; i<modules.size(); i++){
			JSONObject module = modules.getJSONObject(i);
			JSONArray medias = module.getJSONObject("template")
									 .getJSONArray("js");
			for(int j=0; j<medias.size(); j++){
				String url = medias.getJSONObject(j).getString("value");
				if(!url.startsWith("http://")){
					continue;
				}
				String path = new URL(url).getPath();
				linkSuffixes.add(path.substring(1, path.length()));
				links.add(url);
			}
		}
		permissions = new ArrayList<ArticleMediaPermissionPO>();
		List<MediaAudioVO> audios = mediaAudioQuery.findByPreviewUrlIn(linkSuffixes);
		if(audios!=null && audios.size()>0){
			for(MediaAudioVO audio:audios){
				ArticleMediaPermissionPO permission = new ArticleMediaPermissionPO();
				permission.setArticleId(articleId);
				permission.setMediaId(audio.getId());
				permission.setMediaType(MediaType.AUDIO);
				permissions.add(permission);
			}
		}
		List<MediaPictureVO> pictures = mediaPictureQuery.findByPreviewUrlIn(linkSuffixes);
		if(pictures!=null && pictures.size()>0){
			for(MediaPictureVO picture:pictures){
				ArticleMediaPermissionPO permission = new ArticleMediaPermissionPO();
				permission.setArticleId(articleId);
				permission.setMediaId(picture.getId());
				permission.setMediaType(MediaType.PICTURE);
				permissions.add(permission);
			}
		}
		List<MediaVideoVO> videos = mediaVideoQuery.findByPreviewUrlIn(linkSuffixes);
		if(videos!=null && videos.size()>0){
			for(MediaVideoVO video:videos){
				ArticleMediaPermissionPO permission = new ArticleMediaPermissionPO();
				permission.setArticleId(articleId);
				permission.setMediaId(video.getId());
				permission.setMediaType(MediaType.VIDEO);
				permissions.add(permission);
			}
		}
		List<MediaAudioStreamVO> audioStreams = mediaAudioStreamQuery.findByPreviewUrlIn(links);
		if(audioStreams!=null && audioStreams.size()>0){
			for(MediaAudioStreamVO audioStream:audioStreams){
				ArticleMediaPermissionPO permission = new ArticleMediaPermissionPO();
				permission.setArticleId(articleId);
				permission.setMediaId(audioStream.getId());
				permission.setMediaType(MediaType.AUDIO_STREAM);
				permissions.add(permission);
			}
		}
		List<MediaVideoStreamVO> videoStreams = mediaVideoStreamQuery.findByPreviewUrlIn(links);
		if(videoStreams!=null && videoStreams.size()>0){
			for(MediaVideoStreamVO videoStream:videoStreams){
				ArticleMediaPermissionPO permission = new ArticleMediaPermissionPO();
				permission.setArticleId(articleId);
				permission.setMediaId(videoStream.getId());
				permission.setMediaType(MediaType.VIDEO_STREAM);
				permissions.add(permission);
			}
		}
		if(permissions.size() > 0){
			articleMediaPermissionDao.save(permissions);
		}
	}
	
}
