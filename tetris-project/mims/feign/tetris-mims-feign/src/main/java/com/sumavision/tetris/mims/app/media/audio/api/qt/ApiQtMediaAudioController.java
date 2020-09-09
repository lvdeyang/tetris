package com.sumavision.tetris.mims.app.media.audio.api.qt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/qt/media/audio")
public class ApiQtMediaAudioController {

//	@Autowired
//	private MediaAudioQuery mediaAudioQuery;
//	
//	/**
//	 * 加载所有的音频媒资<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2018年12月6日 下午4:03:27
//	 * @return List<MediaAudioVO> 视频媒资列表
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/load/all")
//	public Object loadAll(HttpServletRequest request) throws Exception{
//		
//		return mediaAudioQuery.loadAll();
//		
//	}
	
}
