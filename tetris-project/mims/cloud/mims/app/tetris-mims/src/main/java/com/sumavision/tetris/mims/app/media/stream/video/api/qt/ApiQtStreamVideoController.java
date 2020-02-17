package com.sumavision.tetris.mims.app.media.stream.video.api.qt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/qt/media/stream/video")
public class ApiQtStreamVideoController {
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	

	/**
	 * 以列表形式获取所有视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月16日 下午1:34:52
	 * @return List<MediaVideoStreamVO> 视频流媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception {
		return mediaVideoStreamQuery.loadAll();
	}
}
