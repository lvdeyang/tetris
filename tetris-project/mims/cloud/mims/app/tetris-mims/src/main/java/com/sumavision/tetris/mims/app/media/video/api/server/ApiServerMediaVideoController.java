package com.sumavision.tetris.mims.app.media.video.api.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/media/video")
public class ApiServerMediaVideoController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(
			String name,
			String fileName,
			Long fileSize,
			String mimeType,
			String tags,
			String keyWords,
			String remark,
			String storePath,
			String previewUrl,
			HttpServletRequest request
			) throws Exception{
		
		UserVO user = userQuery.current();
		
		/*mediaVideoService.addTask(
				user, name, tags, keyWords, 
				remark, task, folder, version, 
				storePath, previewUrl, date)*/
		return null;
	}
	
}
