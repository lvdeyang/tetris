package com.sumavision.tetris.cms.portal;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticleQuery;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.cms.column.ColumnQuery;
import com.sumavision.tetris.cms.column.ColumnService;
import com.sumavision.tetris.cms.column.ColumnVO;
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
	
	
}
