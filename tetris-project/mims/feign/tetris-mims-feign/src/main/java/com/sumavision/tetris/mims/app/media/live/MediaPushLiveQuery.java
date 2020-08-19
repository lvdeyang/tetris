package com.sumavision.tetris.mims.app.media.live;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaPushLiveQuery {
	@Autowired
	private MediaPushLiveFeign mediaPushLiveFeign;
	
	/**
	 * 加载所有的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	public List<MediaPushLiveVO> loadAll() throws Exception{
		return JsonBodyResponseParser.parseArray(mediaPushLiveFeign.loadAll(), MediaPushLiveVO.class);
	}
}
