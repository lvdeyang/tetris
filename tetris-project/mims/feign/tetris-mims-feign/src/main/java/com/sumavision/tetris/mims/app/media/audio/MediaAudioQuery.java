package com.sumavision.tetris.mims.app.media.audio;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaAudioQuery {

	@Autowired
	private MediaAudioFeign mediaAudioFeign;
	
	/**
	 * 加载文件夹下的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaAudioVO> 音频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaAudioFeign.load(folderId), Map.class);
	}
	
	/**
	 * 加载所有的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	public List<MediaAudioVO> loadAll() throws Exception{
		return JsonBodyResponseParser.parseArray(mediaAudioFeign.loadAll(), MediaAudioVO.class);
	}
	
	/**
	 * 生成文件存储预览路径(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:03:27
	 * @return String 预览路径
	 * @throws Exception 
	 */
	public String buildPreviewUrl(String name, String folderUuid) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaAudioFeign.buildUrl(name, folderUuid), String.class);
	}
	
	/**
	 * 获取热门推荐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午11:20:41
	 * @return List<MediaAudioVO> 热门视频媒资
	 */
	public List<MediaAudioVO> loadRecommend() throws Exception{
		return JsonBodyResponseParser.parseArray(mediaAudioFeign.loadRecommend(), MediaAudioVO.class);
	}
	
	/**
	 * 获取下载量排名列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:34:26
	 */
	public List<MediaAudioVO> loadHot() throws Exception {
		return JsonBodyResponseParser.parseArray(mediaAudioFeign.loadHot(), MediaAudioVO.class);
	}
}
