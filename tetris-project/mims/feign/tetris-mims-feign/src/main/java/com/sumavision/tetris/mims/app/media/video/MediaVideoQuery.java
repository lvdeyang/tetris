package com.sumavision.tetris.mims.app.media.video;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaVideoQuery {

	@Autowired
	private MediaVideoFeign mediaVideoFeign;
	
	/**
	 * 加载文件夹下的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoVO> 视频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaVideoFeign.load(folderId), Map.class);
	}
	
	/**
	 * 加载所有的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	public List<MediaVideoVO> loadAll() throws Exception{
		return JsonBodyResponseParser.parseArray(mediaVideoFeign.loadAll(), MediaVideoVO.class);
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaVideoVO
	 */
	public MediaVideoVO loadCollection(Long folderId) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaVideoFeign.loadCollection(folderId), MediaVideoVO.class);
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
		return JsonBodyResponseParser.parseObject(mediaVideoFeign.buildUrl(name, folderUuid), String.class);
	}
	
	/**
	 * 根据预览地址查询视频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:49:17
	 * @param String previewUrl 预览地址
	 * @return List<MediaVideoVO> 视频列表
	 */
	public List<MediaVideoVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		return JsonBodyResponseParser.parseArray(mediaVideoFeign.findByPreviewUrlIn(JSON.toJSONString(previewUrls)), MediaVideoVO.class);
	}
}
