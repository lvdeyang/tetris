package com.sumavision.tetris.cms.column;

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
import com.sumavision.tetris.cms.template.exception.TemplateTagMoveFailException;
import com.sumavision.tetris.cms.template.exception.TemplateTagNotExistException;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cms/column")
public class ColumnController {

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private ColumnDAO columnDao;

	@Autowired
	private ColumnQuery columnQuery;

	@Autowired
	private ColumnService columnService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/tree")
	public Object listTree(HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		List<ColumnVO> rootColumns = columnQuery.querycolumnTree();

		return rootColumns;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/root")
	public Object listRoot(HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

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

		UserVO user = userQuery.current();

		// TODO 权限校验
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

		UserVO user = userQuery.current();

		// TODO 权限校验
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

		UserVO user = userQuery.current();

		// TODO 权限校验

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryByRegion(id, province, city, district, page);

		return column;
	}
	

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root")
	public Object addRoot(HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		ColumnPO column = columnService.addRoot();

		return new ColumnVO().set(column);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append")
	public Object append(Long parentId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		ColumnPO parent = columnDao.findOne(parentId);
		if (parent == null) {
			throw new TemplateTagNotExistException(parentId);
		}

		ColumnPO columnPO = columnService.append(parent);

		return new ColumnVO().set(columnPO);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/{id}")
	public Object update(@PathVariable Long id, String name, String remark, HttpServletRequest request)
			throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		ColumnPO columnPO = columnDao.findOne(id);
		if (columnPO == null) {
			throw new TemplateTagNotExistException(id);
		}

		columnPO = columnService.update(columnPO, name, remark);

		return new ColumnVO().set(columnPO);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(@PathVariable Long id, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		ColumnPO columnPO = columnDao.findOne(id);

		if (columnPO != null) {
			columnService.remove(columnPO);
		}

		return null;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object move(Long sourceId, Long targetId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		ColumnPO sourceCol = columnDao.findOne(sourceId);
		if (sourceCol == null) {
			throw new TemplateTagNotExistException(sourceId);
		}

		ColumnPO targetCol = columnDao.findOne(targetId);
		if (targetCol == null) {
			throw new TemplateTagNotExistException(targetId);
		}

		if (targetCol.getId().equals(sourceCol.getParentId())) {
			return false;
		}

		if (targetCol.getParentPath() != null && targetCol.getParentPath().indexOf(sourceCol.getId().toString()) >= 0) {
			throw new TemplateTagMoveFailException(sourceId, targetId);
		}

		columnService.move(sourceCol, targetCol);

		return true;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/top/{id}")
	public Object top(@PathVariable Long id, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		ColumnPO col = columnDao.findOne(id);
		if (col == null) {
			throw new TemplateTagNotExistException(id);
		}

		if (col.getParentId() == null)
			return false;

		columnService.top(col);

		return true;
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
		
		UserVO user = userQuery.current();

		// TODO 权限校验
		Pageable page = new PageRequest(currentPage-1, pageSize);
		List<ArticleVO> list = columnService.search(search, page);
		
		return new HashMapWrapper<String, Object>().put("articles", list)
												   .getMap();
	}

}
