package com.sumavision.tetris.cms.article;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserVO;

/**
 * 文章查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月24日 下午3:58:50
 */
@Component
public class ArticleQuery {

	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ArticleUserPermissionDAO articleUserPermissionDao;
	
	/**
	 * 分页查询文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月24日 下午4:11:33
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ArticlePO> 文章列表
	 */
	public List<ArticlePO> findAll(int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ArticlePO> articles = articleDao.findAll(page);
		return articles.getContent();
	}
	
	/**
	 * 根据用户分页查询文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午4:06:27
	 * @param user 用户
	 * @param currentPage 当前页
	 * @param pageSize 每页数据量
	 * @return Page<ArticlePO> 文章列表
	 */
	public Page<ArticlePO> findAllByUser(UserVO user, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ArticlePO> articles = null;
		if(user.getGroupId() != null){
			articles = articleDao.findAllByGroupId(user.getGroupId(), page);
		}else if(user.getUuid() != null){
			articles = articleDao.findAllByUserId(user.getUuid(), page);
		}
		
		return articles;
	}
	
	/**
	 * 条件分页查询文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月23日 上午11:02:57
	 * @param name 文章名
	 * @param author 作者
	 * @param region 地区
	 * @param classify 分类
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param currentPage 当前页码
	 * @param pageSize 每页数据量
	 * @return List<ArticlePO> 文章列表
	 */
	public List<ArticlePO> findAllBySearch(
						String name, String author, 
						String region, String classify,
						String beginTime,String endTime,
						int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ArticlePO> articles = articleDao.findAllBySearch(new StringBufferWrapper().append("%").append(name).append("%").toString(),
											new StringBufferWrapper().append("%").append(author).append("%").toString(),
											new StringBufferWrapper().append("%").append(region).append("%").toString(), 
											new StringBufferWrapper().append("%").append(classify).append("%").toString(),
											beginTime, endTime, page);
		return articles.getContent();		
	}
	
	/**
	 * 生成html页面内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 上午11:57:04
	 * @param String articleHtml 文章内容html
	 * @param String keyWords 页面关键字
	 * @param String title 页面标题
	 * @return
	 */
	public String generateHtml(String articleHtml, String keyWords, String title){
		String preHtml = "<!DOCTYPE HTML>"+
		"<html lang=\"zh-cmn-Hans\">"+
		"<head>"+
		    "<!-- 声明文档使用的字符编码 -->"+
		    "<meta charset=\"utf-8\">"+
		    "<!-- 优先使用 IE 最新版本和 Chrome -->"+
		    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/>"+
		    "<!-- 页面描述 -->"+
		    "<meta name=\"description\" content=\"\"/>"+
		    "<!-- 页面关键词 -->"+
		    "<meta name=\"keywords\" content=\""+keyWords+"\"/>"+
		    "<!-- 网页作者 -->"+
		    "<meta name=\"author\" content=\"name, email@gmail.com\"/>"+
		    "<!-- 搜索引擎抓取 -->"+
		    "<meta name=\"robots\" content=\"index,follow\"/>"+
		    "<!-- 为移动设备添加 viewport -->"+
		    "<meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=3, minimum-scale=1, user-scalable=no\">"+
		    "<!-- `width=device-width` 会导致 iPhone 5 添加到主屏后以 WebApp 全屏模式打开页面时出现黑边 http://bigc.at/ios-webapp-viewport-meta.orz -->"+
		    "<!-- iOS 设备 begin -->"+
		    "<meta name=\"apple-mobile-web-app-title\" content=\"标题\">"+
		    "<!-- 添加到主屏后的标题（iOS 6 新增） -->"+
		    "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\"/>"+
		    "<!-- 是否启用 WebApp 全屏模式，删除苹果默认的工具栏和菜单栏 -->"+
		    "<meta name=\"apple-itunes-app\" content=\"app-id=myAppStoreID, affiliate-data=myAffiliateData, app-argument=myURL\">"+
		    "<!-- 添加智能 App 广告条 Smart App Banner（iOS 6+ Safari） -->"+
		    "<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\"/>"+
		    "<!-- 设置苹果工具栏颜色 -->"+
		    "<meta name=\"format-detection\" content=\"telphone=no, email=no\"/>"+
		    "<!-- 忽略页面中的数字识别为电话，忽略email识别 -->"+
		    "<!-- 启用360浏览器的极速模式(webkit) -->"+
		    "<meta name=\"renderer\" content=\"webkit\">"+
		    "<!-- 避免IE使用兼容模式 -->"+
		    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"+
		    "<!-- 不让百度转码 -->"+
		    "<meta http-equiv=\"Cache-Control\" content=\"no-siteapp\" />"+
		    "<!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->"+
		    "<meta name=\"HandheldFriendly\" content=\"true\">"+
		    "<!-- 微软的老式浏览器 -->"+
		    "<meta name=\"MobileOptimized\" content=\"320\">"+
		    "<!-- uc强制竖屏 -->"+
		    "<meta name=\"screen-orientation\" content=\"portrait\">"+
		    "<!-- QQ强制竖屏 -->"+
		    "<meta name=\"x5-orientation\" content=\"portrait\">"+
		    "<!-- UC强制全屏 -->"+
		    "<meta name=\"full-screen\" content=\"yes\">"+
		    "<!-- QQ强制全屏 -->"+
		    "<meta name=\"x5-fullscreen\" content=\"true\">"+
		    "<!-- UC应用模式 -->"+
		    "<meta name=\"browsermode\" content=\"application\">"+
		    "<!-- QQ应用模式 -->"+
		    "<meta name=\"x5-page-mode\" content=\"app\">"+
		    "<!-- windows phone 点击无高光 -->"+
		    "<meta name=\"msapplication-tap-highlight\" content=\"no\">"+
		    "<title>"+title+"</title>"+
		    "<style type=\"text/css\">"+
		    "    html, body{padding:0; margin:0; width:100%; height:100%; box-sizing:border-box;}"+
		    "    body{padding:10px;}"+
		    "</style>"+
		    " <link href=\"/web/lib/video/video.css\" rel=\"stylesheet\">"+
		"</head>"+
		"<body>";
		String sufHtml = "</body><script src=\"/web/lib/video/video.js\"></script></html>";
		return new StringBufferWrapper().append(preHtml).append(articleHtml).append(sufHtml).toString();
	}
	
	/**
	 * 分页查询文章（前端接口）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:32:06
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ArticleVO> 用户列表
	 */
	public Map<String, Object> list(UserVO user, int currentPage, int pageSize) throws Exception{
		Page<ArticlePO> page = findAllByUser(user, currentPage, pageSize);
		List<ArticlePO> articles = page.getContent();
		List<ArticleVO> view_articles = ArticleVO.getConverter(ArticleVO.class).convert(articles, ArticleVO.class);
		long total = page.getTotalElements();
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", view_articles)
												   .getMap();
	}
	
	/**
	 * 分页查询文章（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午6:37:06
	 * @param Collection<Long> except 例外文章id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 数据总量
	 * @return List<ArticleVO> rows 用户列表
	 */
	public Map<String, Object> listWithExcept(UserVO user, Collection<Long> except, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ArticlePO> pages = null;
		if(user.getGroupId() != null){
			pages = articleDao.findWithExceptByGroupId(except, user.getGroupId(), page);
		}else if(user.getUuid() != null){
			pages = articleDao.findWithExceptByUserId(except, user.getUuid(), page);
		}
		
		List<ArticlePO> articles = pages.getContent();
		long total = pages.getTotalElements();
		List<ArticleVO> view_articles = ArticleVO.getConverter(ArticleVO.class).convert(articles, ArticleVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
											       .put("rows", view_articles)
											       .getMap();
	}
	
	/**
	 * 校验文章和用户是否匹配<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午3:05:12
	 * @param articleId 文章id
	 * @param user 用户
	 */
	public boolean hasPermission(Long articleId, UserVO user) throws Exception{
		ArticleUserPermissionPO permission = null;
		if(user.getGroupId() != null){
			permission = articleUserPermissionDao.findByArticleIdAndGroupId(articleId, user.getGroupId());
		}else if(user.getUuid() != null){
			permission = articleUserPermissionDao.findByArticleIdAndUserId(articleId, user.getUuid());
		}
		
		if(permission == null) return false;
		return true;
	}
	
	/**
	 * 获取所有直播文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月10日 下午3:05:12
	 * @param user 用户
	 * @throws Exception 
	 */
	public List<ArticleVO> findAllLive(UserVO user) throws Exception{
		
		List<ArticlePO> articles = articleDao.findAllByGroupId(user.getGroupId());
		
		if (articles == null || articles.size() <= 0) return new ArrayList<ArticleVO>();
				
		return ArticleVO.getConverter(ArticleVO.class).convert(articles, ArticleVO.class);
	}
	
}
