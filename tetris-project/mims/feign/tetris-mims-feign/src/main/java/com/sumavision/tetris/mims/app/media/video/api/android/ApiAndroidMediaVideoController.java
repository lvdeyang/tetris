package com.sumavision.tetris.mims.app.media.video.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/android/media/video")
public class ApiAndroidMediaVideoController {
//	@Autowired
//	private MediaVideoQuery mediaVideoQuery;
//	
//	/**
//	 * 加载所有的视频媒资<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年6月4日 下午4:03:27
//	 * @return List<MediaVideoVO> 视频媒资列表
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/load/all")
//	public Object loadAll(HttpServletRequest request) throws Exception{
//		
//		return mediaVideoQuery.loadAll();
//		
//	}
}
