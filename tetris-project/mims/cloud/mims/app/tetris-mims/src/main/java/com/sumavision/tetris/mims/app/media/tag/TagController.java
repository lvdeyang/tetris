package com.sumavision.tetris.mims.app.media.tag;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.tag.exception.UserHasNoGroupException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/tag")
public class TagController {
	@Autowired
	private UserQuery userQuery;

	@Autowired
	private TagService tagService;
	
	@Autowired
	private TagQuery tagQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addTag(Long parentId, String name, String remark, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (user.getGroupId() == null) throw new UserHasNoGroupException(user.getNickname());
		
		return tagService.add(user, name, parentId, remark);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object removeTag(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (user.getGroupId() == null) throw new UserHasNoGroupException(user.getNickname());
		
		tagService.remove(user, id);
		
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object editTag(Long id, String name, String remark, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (user.getGroupId() == null) throw new UserHasNoGroupException(user.getNickname());
		
		return tagService.edit(user, id, name, remark);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/get")
	public Object getTagList(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (user.getGroupId() == null) throw new UserHasNoGroupException(user.getNickname());
		
		return tagQuery.getTagTree(user);
	}
}
