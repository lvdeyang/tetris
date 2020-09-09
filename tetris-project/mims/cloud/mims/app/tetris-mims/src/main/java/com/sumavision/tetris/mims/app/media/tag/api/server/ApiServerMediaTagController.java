package com.sumavision.tetris.mims.app.media.tag.api.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.TagService;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mims.app.media.tag.exception.UserHasNoGroupException;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/media/tag")
public class ApiServerMediaTagController {
	@Autowired
	private TagQuery tagQuery;
	@Autowired
	private TagService tagService;
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	@Autowired
	private UserQuery userQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addTag(Long parentId, String name, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (user.getGroupId() == null) throw new UserHasNoGroupException(user.getNickname());
		
		return tagService.add(user, name, parentId, "");
	}
	/**
	 * 根据父id获取标签树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月3日 上午10:51:56
	 * @param Long id 父标签id
	 * @return List<TagVO> 标签列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/tree")
	public Object getTree(Long id, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		if (user.getGroupId() == null) throw new UserHasNoGroupException(user.getNickname());
		
		if (id == null) {
			return tagQuery.getTagTree(user);
		} else {
			return tagQuery.getTagTreeByParent(user, id);
		}
	}
	
	/**
	 * 根据父id获取子标签和素材数量<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月3日 上午10:56:41
	 * @param Long id 父标签id
	 * @return List<TagVO> 标签列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/num")
	public Object getNum(Long id, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<TagVO> tags = tagQuery.getTagTreeByParent(user, id);
		
		mediaAudioQuery.queryCountByTags(user, tags);
		mediaVideoQuery.queryCountByTags(user, tags);
		
		return tags;
	}
}
