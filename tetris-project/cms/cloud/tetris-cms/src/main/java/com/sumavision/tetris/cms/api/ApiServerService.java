package com.sumavision.tetris.cms.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleQuery;
import com.sumavision.tetris.cms.article.ArticleRegionPermissionDAO;
import com.sumavision.tetris.cms.article.ArticleRegionPermissionPO;
import com.sumavision.tetris.cms.article.ArticleType;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.cms.region.RegionDAO;
import com.sumavision.tetris.cms.region.RegionPO;
import com.sumavision.tetris.cms.region.RegionQuery;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleDAO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticlePO;
import com.sumavision.tetris.cms.template.TemplateDAO;
import com.sumavision.tetris.cms.template.TemplatePO;
import com.sumavision.tetris.cms.template.TemplateQuery;
import com.sumavision.tetris.cms.template.TemplateType;
import com.sumavision.tetris.cms.template.TemplateVO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiServerService {
	
	@Autowired
	private Path path;
	
	@Autowired
	private TemplateQuery templateQuery;
	
	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ArticleRegionPermissionDAO articleRegionPermissionDao;
	
	@Autowired
	private TemplateDAO templateDao;
	
	@Autowired
	private ArticleQuery articleQuery;
	
	@Autowired
	private RegionDAO regionDao;
	
	@Autowired
	private ColumnDAO columnDao;
	
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
			List<String> columns,
			String name, 
			String type,
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			Boolean command,
			List<JSONObject> contents,
			List<String> regions) throws Exception{
		
		ArticlePO article = new ArticlePO();
		article.setName(name);
		article.setAuthor(author);
		article.setPublishTime(publishTime);
		article.setThumbnail(thumbnail);
		article.setRemark(remark);
		article.setUpdateTime(new Date());
		article.setType(ArticleType.valueOf(type));
		
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
		List<JSONObject> moduleJsons = new ArrayList<JSONObject>();
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
			view_template.setJs(jsJson.toString());
			
			String newHtml = contentHtml.replace("${"+jsKey+"}", contentValue);
			allHtml.append(newHtml);
			
			JSONObject module = new JSONObject();
			module.put("id", "module_" + new Date().getTime());
			module.put("template", view_template);
			module.put("render", null);
			module.put("mousein", null);
			
			moduleJsons.add(module);
		}
		
		File file = new File(article.getStorePath());
		FileUtils.writeStringToFile(file, articleQuery.generateHtml(allHtml.toString(), "", ""));
		article.setModules(moduleJsons.toString());
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

}
