package com.sumavision.tetris.cms.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.cms.column.ColumnQuery;
import com.sumavision.tetris.cms.column.ColumnService;
import com.sumavision.tetris.cms.column.ColumnVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/terminal/cms/column")
public class ApiTerminalController {
	
	@Autowired
	private ColumnQuery columnQuery;
	
	@Autowired
	private ColumnService columnService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/root")
	public Object listRoot(HttpServletRequest request) throws Exception {

		List<ColumnVO> rootColumns = columnQuery.querycolumnTree();

		return rootColumns;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/{id}")
	public Object query(
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
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryCommand(page);

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
			String search,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		List<ArticleVO> list = columnService.search(search, page);
		
		return new HashMapWrapper<String, Object>().put("articles", list)
												   .getMap();
	}
}
