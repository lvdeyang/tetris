package com.sumavision.tetris.cms.article;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
			String remark) throws Exception{
		
		ArticlePO article = new ArticlePO();
		article.setName(name);
		article.setAuthor(author);
		article.setPublishTime(publishTime);
		article.setThumbnail(thumbnail);
		article.setRemark(remark);
		article.setUpdateTime(new Date());
		
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
			String remark) throws Exception{
		
		article.setName(name);
		article.setAuthor(author);
		article.setPublishTime(publishTime);
		article.setThumbnail(thumbnail);
		article.setRemark(remark);
		article.setUpdateTime(new Date());
		articleDao.save(article);
		
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
			String storePath = article.getStorePath();
			File html = new File(storePath);
			if(html.exists()){
				html.delete();
			}
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
