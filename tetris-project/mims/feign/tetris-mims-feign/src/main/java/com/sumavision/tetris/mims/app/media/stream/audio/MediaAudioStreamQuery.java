package com.sumavision.tetris.mims.app.media.stream.audio;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaAudioStreamQuery {

	@Autowired
	private MediaAudioStreamFeign mediaAudioStreamFeign;
	
	/**
	 * 加载文件夹下的音频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaAudioStreamVO> 音频流媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaAudioStreamFeign.load(folderId), Map.class);
	}
	
}
