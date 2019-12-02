package com.sumavision.tetris.mims.app.media.audio;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaAudioFeign {

	/**
	 * 加载文件夹下的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaAudioVO> 音频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@RequestMapping(value = "/media/audio/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 加载所有的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	@RequestMapping(value = "/media/audio/feign/load/all")
	public JSONObject loadAll() throws Exception;
	
	/**
	 * 获取热门推荐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午11:20:41
	 * @return List<MediaAudioVO> 热门视频媒资
	 */
	@RequestMapping(value = "/media/audio/feign/load/recommend")
	public JSONObject loadRecommend() throws Exception;
	
	/**
	 * 获取下载量排名列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:34:26
	 */
	@RequestMapping(value = "/media/audio/feign/load/hot")
	public JSONObject loadHot() throws Exception;
	
	/**
	 * 根据uuid获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午4:03:27
	 * @return List<MediaAudioVO> 音频媒资列表
	 */
	@RequestMapping(value = "/media/audio/feign/quest/by/uuid")
	public JSONObject getByUuids(@RequestParam("uuids") String uuids) throws Exception;
	
	/**
	 * 根据id获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月9日 下午2:35:54
	 * @param id 媒资id
	 * @return MediaAudioVO 音频媒资列表
	 */
	@RequestMapping(value = "/media/audio/feign/quest/by/id")
	public JSONObject getById(@RequestParam("id") Long id) throws Exception;
	
	/**
	 * 生成文件存储预览路径(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:03:27
	 * @return String 预览路径
	 * @throws Exception 
	 */
	@RequestMapping(value = "/media/audio/feign/build/url")
	public JSONObject buildUrl(@RequestParam("name") String name, @RequestParam("folderUuid") String folderUuid) throws Exception;
	
	@RequestMapping(value = "/media/audio/feign/add")
	public JSONObject add(
				@RequestParam("userId") String userId,
				@RequestParam("userName") String userName,
				@RequestParam("groupId") String groupId,
				@RequestParam("name") String name,
				@RequestParam("fileName") String fileName,
				@RequestParam("size") Long size,
				@RequestParam("folderType") String folderType,
				@RequestParam("mimeType") String mimeType,
				@RequestParam("uploadTempPath") String uploadTempPath) throws Exception;
	
	/**
	 * 根据预览地址查询音频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:31:15
	 * @param String previewUrl 预览地址
	 * @return List<MediaAudioVO> 音频列表
	 */
	@RequestMapping(value = "/media/audio/feign/find/by/preview/url/in")
	public JSONObject findByPreviewUrlIn(@RequestParam("previewUrls") String previewUrls) throws Exception;
	
	/**
	 * 下载计数<br/>
	 * <b>作者:</b>Mr<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:31:15
	 * @param Long id 媒资Id
	 * @return MediaAudioVO 音频媒资
	 */
	@RequestMapping(value = "/media/audio/feign/download")
	public JSONObject download(@RequestParam("id") Long id) throws Exception;
}