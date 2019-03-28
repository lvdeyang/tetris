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
import com.sumavision.tetris.cms.column.exception.UserHasNotPermissionForColumnException;
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

		List<ColumnVO> rootColumns = columnQuery.querycolumnTree(user);

		return rootColumns;
	}
	

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root")
	public Object addRoot(HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		ColumnPO column = columnService.addRoot(user);

		return new ColumnVO().set(column);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append")
	public Object append(Long parentId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		if(!columnQuery.hasPermission(parentId, user)){
			throw new UserHasNotPermissionForColumnException(parentId, user);
		}

		ColumnPO parent = columnDao.findOne(parentId);
		if (parent == null) {
			throw new TemplateTagNotExistException(parentId);
		}

		ColumnPO column = columnService.append(user, parent);

		return new ColumnVO().set(column);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/{id}")
	public Object update(@PathVariable Long id, String name, String code, String remark, HttpServletRequest request)
			throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		if(!columnQuery.hasPermission(id, user)){
			throw new UserHasNotPermissionForColumnException(id, user);
		}

		ColumnPO columnPO = columnDao.findOne(id);
		if (columnPO == null) {
			throw new TemplateTagNotExistException(id);
		}

		columnPO = columnService.update(columnPO, name, code, remark);

		return new ColumnVO().set(columnPO);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(@PathVariable Long id, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		if(!columnQuery.hasPermission(id, user)){
			throw new UserHasNotPermissionForColumnException(id, user);
		}

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
		if(!columnQuery.hasPermission(sourceId, user)){
			throw new UserHasNotPermissionForColumnException(sourceId, user);
		}
		if(!columnQuery.hasPermission(targetId, user)){
			throw new UserHasNotPermissionForColumnException(targetId, user);
		}

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
		if(!columnQuery.hasPermission(id, user)){
			throw new UserHasNotPermissionForColumnException(id, user);
		}

		ColumnPO col = columnDao.findOne(id);
		if (col == null) {
			throw new TemplateTagNotExistException(id);
		}

		if (col.getParentId() == null)
			return false;

		columnService.top(col);

		return true;
	}

}
