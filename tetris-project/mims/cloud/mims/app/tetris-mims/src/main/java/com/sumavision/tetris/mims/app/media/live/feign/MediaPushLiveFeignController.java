package com.sumavision.tetris.mims.app.media.live.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.live.MediaPushLiveQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/push/live/feign")
public class MediaPushLiveFeignController {
	@Autowired
	private MediaPushLiveQuery mediaPushLiveQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception {
		return mediaPushLiveQuery.loadAll();
	}
}
