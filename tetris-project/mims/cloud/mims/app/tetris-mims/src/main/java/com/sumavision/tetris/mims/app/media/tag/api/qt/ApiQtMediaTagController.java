package com.sumavision.tetris.mims.app.media.tag.api.qt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.exception.UserHasNoGroupException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/qt/media/tag")
public class ApiQtMediaTagController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TagQuery tagQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/get")
	public Object getTagList(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (user.getGroupId() == null) throw new UserHasNoGroupException(user.getNickname());
		
		return tagQuery.getTagTree(user);
	}
}
