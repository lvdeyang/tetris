package com.sumavision.tetris.mims.app.media.tag.api.screen.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/screen/web/tag")
public class ApiScreenWebTagController {
	@Autowired
	private TagQuery tagQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download/map")
	public Object getDownloadMap(Long parentId, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return tagQuery.queryTagMediaAndDownloadCount(user, parentId);
	}
}
