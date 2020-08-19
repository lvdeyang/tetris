package com.sumavision.tetris.easy.process.stream.transcode;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-easy-process", configuration = FeignConfiguration.class)
public interface StreamTranscodeFeign {

	/**
	 * 获取配置文件信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月27日 下午3:12:07
	 * @return JSONObject 配置信息
	 */
	@RequestMapping(value = "/stream/transcode/feign/get/profile")
	public JSONObject getProfile() throws Exception;
	
	/**
	 * 流转码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:01:59
	 * @param Long assetId 资源仓库中流id
	 * @param Long assetPath 直接使用流url
	 * @param Boolean record 是否录制
	 * @param Integer bePCM 音频源pcm
	 * @param String mediaType 资源仓库中流类型
	 * @param String recordCallback 录制回调
	 * @param Integer progNum 节目号
	 * @param String task 输出信息
	 * @param String inputParam 输入信息
	 */
	@RequestMapping(value = "/stream/transcode/feign/task/add")
	public JSONObject addTask(
			@RequestParam("assetId") Long assetId,
			@RequestParam("assetPath") String assetPath,
			@RequestParam("record") boolean record,
			@RequestParam("bePCM") Integer bePCM,
			@RequestParam("mediaType") String mediaType,
			@RequestParam("recordCallback") String recordCallback,
			@RequestParam("progNum") Integer progNum,
			@RequestParam("deviceIp") String deviceIp,
			@RequestParam("task") String task,
			@RequestParam("inputParam") String inputParam);
	
	/**
	 * 添加使用流转码能力推流转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午2:23:20
	 * @param String fileUrl 文件地址(m3u8为http，其他文件为ftp地址)
	 * @param Integer playTime 循环次数
	 * @param Long seek 首个文件seek时间
	 * @param Boolean record 是否录制
	 * @param Integer bePCM 音频源pcm
	 * @param String mediaType 资源仓库中流类型
	 * @param String recordCallback 录制回调
	 * @param Integer progNum 节目号
	 * @param String task 输出信息
	 * @param String inputParam 输入信息
	 * @return String 流程号
	 */
	@RequestMapping(value = "/stream/transcode/feign/task/add/file")
	public JSONObject addFileTask(
			@RequestParam("fileUrl") String fileUrl,
			@RequestParam("playTime") Integer playTime,
			@RequestParam("seek") Long seek,
			@RequestParam("record") boolean record,
			@RequestParam("bePCM") Integer bePCM,
			@RequestParam("mediaType") String mediaType,
			@RequestParam("recordCallback") String recordCallback,
			@RequestParam("progNum") Integer progNum,
			@RequestParam("deviceIp") String deviceIp,
			@RequestParam("task") String task,
			@RequestParam("inputParam") String inputParam);
	
	/**
	 * 删除转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:08:19
	 * @param Long id 任务id
	 */
	@RequestMapping(value = "/stream/transcode/feign/delete/task")
	public JSONObject deleteTask(@RequestParam("id") String id);
	
	/**
	 * 根据任务id添加输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 * @param String outputParam 输出信息
	 */
	@RequestMapping(value = "/stream/transcode/feign/add/output")
	public JSONObject addOutput(@RequestParam("id") String id,@RequestParam("outputParam") String outputParam);
	
	/**
	 * 根据任务id删除输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 * @param String outputParam 输出信息
	 */
	@RequestMapping(value = "/stream/transcode/feign/delete/output")
	public JSONObject deleteOutput(@RequestParam("id") String id, @RequestParam("outputParam") String outputParam);
	
	/**
	 * 根据任务id删除全部输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 */
	@RequestMapping(value = "/stream/transcode/feign/delete/output/all")
	public JSONObject deleteAllOutput(@RequestParam("id") String id);
	
	/**
	 * 是否需要文件转码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月11日 下午5:55:26
	 * @param Long mediaId 资源id
	 * @param String mediaType 资源类型(audio/video)
	 * @return String transcodeJob 转码参数
	 * @return String param 转码模板
	 * @return String name 输出资源名称
	 * @return String folderId 资源目录id
	 */
	@RequestMapping(value = "/stream/transcode/feign/check/edit")
	public JSONObject checkEdit(@RequestParam("mediaId") Long mediaId, @RequestParam("mediaType") String mediaType);
	
	/**
	 * 根据uuid获取资源判断是否需要文件转码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月11日 下午5:55:26
	 * @param Long mediaId 资源id
	 * @param String mediaType 资源类型(audio/video)
	 * @return String transcodeJob 转码参数
	 * @return String param 转码模板
	 * @return String name 输出资源名称
	 * @return String folderId 资源目录id
	 */
	@RequestMapping(value = "/stream/transcode/feign/check/edit/by/uuid")
	public JSONObject checkEditByUuid(@RequestParam("uuid") String uuid);
}
