package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaVideoStreamQuery {

	@Autowired
	private MediaVideoStreamFeign mediaVideoStreamFeign;
	
	/**
	 * 加载文件夹下的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoStreamVO> 视频流媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaVideoStreamFeign.load(folderId), Map.class);
	}
	
	/**
	 * 加载所有的视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoStreamVO> 视频流媒资列表
	 */
	public List<MediaVideoStreamVO> loadAll() throws Exception{
		return JsonBodyResponseParser.parseArray(mediaVideoStreamFeign.loadAll(), MediaVideoStreamVO.class);
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaVideoStreamVO
	 */
	public MediaVideoStreamVO loadCollection(Long folderId) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaVideoStreamFeign.loadCollection(folderId), MediaVideoStreamVO.class);
	}
	
	/**
	 * 根据预览地址查询视频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:28:10
	 * @param String previewUrl 预览地址
	 * @return List<MediaVideoStreamVO> 视频流列表
	 */
	public List<MediaVideoStreamVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		return JsonBodyResponseParser.parseArray(mediaVideoStreamFeign.findByPreviewUrlIn(JSON.toJSONString(previewUrls)), MediaVideoStreamVO.class);
	}
	
	/**
	 * 根据id查询音频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:15:05
	 * @param JSONString previewUrls 预览地址列表
	 * @return MediaAudioStreamVO 音频流列表
	 */
	public MediaVideoStreamVO findById(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaVideoStreamFeign.findById(id), MediaVideoStreamVO.class);
	}
}
