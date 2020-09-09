package com.sumavision.tetris.mims.app.media.stream.video.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/video/stream/feign")
public class MediaVideoStreamFeignController {

	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	/**
	 * 加载文件夹下的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoStreamVO> 视频流媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaVideoStreamQuery.load(folderId);
	}
	
	/**
	 * 加载所有视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoStreamVO> 视频流媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(
			HttpServletRequest request) throws Exception{
		
		return mediaVideoStreamQuery.loadAll();
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaVideoStreamVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/collection")
	public Object loadCollection(Long folderId, HttpServletRequest request) throws Exception{
		return mediaVideoStreamQuery.loadCollection(folderId);
	}
	
	/**
	 * 添加上传视频流媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param String previewUrl 流地址
	 * @param String name 媒资名称
	 * @return MediaVideoStreamVO 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(
			String previewUrl, 
			String name,
			String streamType,
			HttpServletRequest request) throws Exception{
		
		return  mediaVideoStreamService.addVideoStreamTask(previewUrl, "", name, streamType);
	}
	
	/**
	 * 视频流媒资删除<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:03
	 * @param Long mediaId 视频流媒资id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String mediaIds, 
			HttpServletRequest request) throws Exception{
		List<Long> mediaIdList = JSONArray.parseArray(mediaIds, Long.class);
		mediaVideoStreamService.removeByIds(mediaIdList);
		return null;
	}
	
	/** 
	 * 修改媒资
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param Long mediaId 媒资名称
	 * @param String previewUrl 媒资地址
	 * @param String name 媒资名称
	 * @return MediaVideoStreamVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long mediaId, 
			String previewUrl,
			String name,
			String streamType,
			HttpServletRequest request) throws Exception{
		return mediaVideoStreamService.edit(mediaId, previewUrl, name, streamType);
	}
	
	/**
	 * 根据id查询视频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:28:10
	 * @param JSONString previewUrls 预览地址列表
	 * @return List<MediaVideoStreamVO> 视频流列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id")
	public Object findById(
			Long id, 
			HttpServletRequest request) throws Exception{
		return mediaVideoStreamQuery.findById(id);
	}
	
	/**
	 * 根据预览地址查询视频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:28:10
	 * @param JSONString previewUrls 预览地址列表
	 * @return List<MediaVideoStreamVO> 视频流列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/preview/url/in")
	public Object findByPreviewUrlIn(
			String previewUrls, 
			HttpServletRequest request) throws Exception{
		return mediaVideoStreamQuery.findByPreviewUrlIn(JSONArray.parseArray(previewUrls, String.class));
	}
	
}
