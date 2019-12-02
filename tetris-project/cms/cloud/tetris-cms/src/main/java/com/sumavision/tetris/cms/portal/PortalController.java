package com.sumavision.tetris.cms.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticleMediaPermissionDAO;
import com.sumavision.tetris.cms.article.ArticleMediaPermissionPO;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleQuery;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.cms.column.ColumnQuery;
import com.sumavision.tetris.cms.column.ColumnService;
import com.sumavision.tetris.cms.column.ColumnVO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;


@Controller
@RequestMapping("/portal")
public class PortalController {
	
	@Autowired
	private UserQuery userQuery;

	@Autowired
	private ColumnDAO columnDao;
	
	@Autowired
	private ColumnQuery columnQuery;
	
	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ArticleQuery articleQuery;
	
	@Autowired
	private ColumnService columnService;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/column/list")
	public Object listTree(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
        List<ColumnVO> rootColumns =columnQuery.queryColumnRoot(user);
		return rootColumns;
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/article/new/list")
	public Object listNewArticle(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return articleQuery.findAllOrderByUpdateTime(user.getGroupId(),1,7);
	}
	
	/**
	 * 查询推荐文章<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryCommand")
	public Object queryCommand(
			HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();
		Pageable page = new PageRequest(0, 10);
		ColumnVO column = columnService.queryCommand(user, page);

		return column;
	}
	/**
	 * 查询栏目下子栏目及文章<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/{id}")
	public Object queryColArticle(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception {
		
		UserVO userVO = userQuery.current();
		Pageable page = new PageRequest(0, 100);
		ColumnVO column = columnService.query(userVO, id, page);

		return column;
	}
	
	
	@Autowired
	ArticleMediaPermissionDAO articleMediaPermissionDAO;
	/**
	 * 查询下载量最大的文章<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryhot")
	public Object queryHotArticle(
			HttpServletRequest request) throws Exception {
		List<MediaAudioVO> mediaAudioVOs= mediaAudioQuery.loadHot();
		List<ArticleVO> articleVOs=new ArrayList<ArticleVO>();
		for (MediaAudioVO mediaAudioVO : mediaAudioVOs) {
			List<ArticleMediaPermissionPO> articleMediaPermissionPOs=articleMediaPermissionDAO.findByMediaId(mediaAudioVO.getId());
		    if(articleMediaPermissionPOs!=null&&!articleMediaPermissionPOs.isEmpty()){
		    	ArticleVO articleVO=new ArticleVO();
		    	articleVO.set(articleDao.findOne(articleMediaPermissionPOs.get(0).getArticleId()));
		    	articleVO.setMediaDownloadCount(mediaAudioVO.getDownloadCount());
		    	articleVOs.add(articleVO);
		    }
		}
		
		return articleVOs;

	}
	/**
	 * 查询智能推荐的文章<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryforu")
	public Object queryForuArticle(
			HttpServletRequest request) throws Exception {
		List<MediaAudioVO> mediaAudioVOs= mediaAudioQuery.loadRecommend();
		List<ArticleVO> articleVOs=new ArrayList<ArticleVO>();
		for (MediaAudioVO mediaAudioVO : mediaAudioVOs) {
			List<ArticleMediaPermissionPO> articleMediaPermissionPOs=articleMediaPermissionDAO.findByMediaId(mediaAudioVO.getId());
		    if(articleMediaPermissionPOs!=null&&!articleMediaPermissionPOs.isEmpty()){
		    	ArticleVO articleVO=new ArticleVO();
		    	articleVO.set(articleDao.findOne(articleMediaPermissionPOs.get(0).getArticleId()));
		    	articleVOs.add(articleVO);
		    }
		}
		return articleVOs;
	}
	
	/**
	 * 查询推荐文章<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/articleInfo/{articleId}")
	public Object articleInfo(
			@PathVariable Long articleId,
			HttpServletRequest request) throws Exception {

		Map<String, Object> ret=new HashMap<String, Object>();
        ArticlePO articlePO=articleDao.findOne(articleId);
        ret.put("articleName", articlePO.getName());
        List<ArticleMediaPermissionPO> amPos=articleMediaPermissionDAO.findByArticleId(articleId);
        if(amPos!=null&&!amPos.isEmpty()){
        	MediaAudioVO mediaAudioVO=mediaAudioQuery.getById(amPos.get(0).getMediaId());
        	ret.put("mediaUrl", mediaAudioVO.getPreviewUrl());
        }
		return ret;
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download/{articleId}")
	public Object download(
			@PathVariable Long articleId,
			HttpServletRequest request) throws Exception {

        ArticlePO articlePO=articleDao.findOne(articleId);
        List<ArticleMediaPermissionPO> amPos=articleMediaPermissionDAO.findByArticleId(articleId);
        if(amPos!=null&&!amPos.isEmpty()){
        	MediaAudioVO mediaAudioVO=mediaAudioQuery.download(amPos.get(0).getMediaId());
        	
        }
		return null;
	}
}
