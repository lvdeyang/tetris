package com.sumavision.tetris.mims.app.media.stream.audio;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
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
	
	/**
	 * 根据预览地址查询音频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:15:05
	 * @param Collection<String> previewUrls 预览地址列表
	 * @return List<MediaAudioStreamVO> 音频流列表
	 */
	public List<MediaAudioStreamVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		return JsonBodyResponseParser.parseArray(mediaAudioStreamFeign.findByPreviewUrlIn(JSON.toJSONString(previewUrls)), MediaAudioStreamVO.class);
	}
	
	/**
	 * 根据id查询音频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:15:05
	 * @param JSONString previewUrls 预览地址列表
	 * @return MediaAudioStreamVO 音频流列表
	 */
	public MediaAudioStreamVO findById(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaAudioStreamFeign.findById(id), MediaAudioStreamVO.class);
	}
}
