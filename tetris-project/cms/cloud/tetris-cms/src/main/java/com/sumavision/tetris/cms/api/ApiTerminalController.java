package com.sumavision.tetris.cms.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.cms.column.ColumnQuery;
import com.sumavision.tetris.cms.column.ColumnService;
import com.sumavision.tetris.cms.column.ColumnVO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserVO;

/**
 * 外部app接口<br/>
 * <b>作者:</b>ldy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月11日 下午1:12:35
 */
@Controller
@RequestMapping(value = "/api/terminal/cms/column")
public class ApiTerminalController {
	
	private static int i = 0;
	
	@Autowired
	private ColumnQuery columnQuery;
	
	@Autowired
	private ColumnService columnService;
	
	@Autowired
	private ColumnRelationArticleService columnRelationArticleService;
	
	@Autowired
	private ArticleDAO articleDAO;

	/**
	 * 根据组织id查询目录<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午10:51:02
	 * @param groupId 组织id
	 * @return List<ColumnVO> 目录列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/root")
	public Object listRoot(String groupId, HttpServletRequest request) throws Exception {

		UserVO user = new UserVO().setGroupId(groupId);
		
		List<ColumnVO> rootColumns = columnQuery.querycolumnTree(user);

		return rootColumns;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/{id}")
	public Object query(
			String groupId,
			@PathVariable Long id,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.query(id, page);

		return column;
	}
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryCommand")
	public Object queryCommand(
			String groupId,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {

		UserVO user = new UserVO().setGroupId(groupId);
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryCommand(user, page);

		return column;
	}
	
	/**
	 * 获取栏目地区下的文章<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:24:45
	 * @param id
	 * @param province
	 * @param city
	 * @param district
	 * @return ColumnVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryRegion")
	public Object queryRegion(
			String groupId,
			Long id,
			String province, 
			String city, 
			String district,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryByRegion(id, province, city, district, page);

		return column;
	}	
	
	/**
	 * 文章搜索<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午3:18:58
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/search")
	public Object search(
			String groupId,
			String search,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = new UserVO().setGroupId(groupId);
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		List<ArticleVO> list = columnService.search(user, search, page);
		
		return new HashMapWrapper<String, Object>().put("articles", list)
												   .getMap();
	}
	
	/**
	 * 文章通知<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月15日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/inform")
	public Object inform(HttpServletRequest request) throws Exception{
		
		ArrayList<Long> aa = new ArrayList<Long>();
		
		aa.add(17l);
		aa.add(15l);
		aa.add(10l);
		aa.add(5l);
		
		Long articleId = null;
		
		if(i < 4){
			articleId = aa.get(i++);
		}else{
			i = 1;
			articleId = aa.get(0);
		}
		
		System.out.println(articleId);
		
		columnRelationArticleService.inform(null, articleId);		
	
		return null;
	}
	
}
