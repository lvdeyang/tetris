package com.sumavision.tetris.mims.app.media.picture;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaPictureQuery {

	@Autowired
	private MediaPictureFeign mediaPictureFeign;
	
	/**
	 * 查询文件夹下的文件夹以及图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:46:46
	 * @param Long folderId 当前文件夹
	 * @return rows List<MediaPictureVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaPictureFeign.load(folderId), Map.class);
	}
	
	/**
	 * 加载所有的图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaPictureVO> 视频媒资列表
	 */
	public List<MediaPictureVO> loadAll() throws Exception{
		return JsonBodyResponseParser.parseArray(mediaPictureFeign.loadAll(), MediaPictureVO.class);
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaPictureVO
	 */
	public MediaPictureVO loadCollection(Long folderId) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaPictureFeign.loadCollection(folderId), MediaPictureVO.class);
	}
	
	/**
	 * 根据预览地址查询图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:34:58
	 * @param Collection<String> preivewUrls 预览地址列表
	 * @return List<MediaPictureVO> 图片列表
	 */
	public List<MediaPictureVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		return JsonBodyResponseParser.parseArray(mediaPictureFeign.findByPreviewUrlIn(JSON.toJSONString(previewUrls)), MediaPictureVO.class);
	}
}
