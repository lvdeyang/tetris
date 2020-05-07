package com.sumavision.tetris.streamTranscoding.feign;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.capacity.server.TransformService;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.api.server.ApiServerStreamTranscodingService;

@Controller
@RequestMapping(value = "/stream/transcode/feign")
public class ApiFeignStreamTranscodeController {
	@Autowired
	private StreamTranscodingAdapter adapter;
	
	@Autowired
	private ApiServerStreamTranscodingService apiServerStreamTranscodingService;
	
	@Autowired
	private TransformService transformService;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	/**
	 * 获取配置文件信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月27日 下午3:12:07
	 * @return StreamTranscodeProfileVO 配置信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/profile")
	public Object getProfile(HttpServletRequest request) throws Exception {
		Map<String, String> toolInfo = adapter.getToolInfo();
		Map<String, String> recordInfo = adapter.getRecordInfo();
		StreamTranscodeProfileVO profileVO = new StreamTranscodeProfileVO();
		profileVO.setToolIp(toolInfo.get("ip"));
		profileVO.setUdpStartPort(recordInfo.get("startPort"));
		return profileVO;
	}

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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(
			Long assetId,
			String assetPath,
			boolean record,
			Integer bePCM,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String inputParam,
			HttpServletRequest request) throws Exception {
		String processInstanceId = apiServerStreamTranscodingService.addTask(assetId, assetPath, record, bePCM, mediaType, recordCallback, progNum, deviceIp, task, inputParam);
		
		return processInstanceId;
	}
	
	/**
	 * 添加使用流转码能力推流转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午2:23:20
	 * @param String fileUrl 文件地址
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add/file")
	public Object addFileTask(
			String fileUrl,
			Integer playTime,
			Long seek,
			boolean record,
			Integer bePCM,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String inputParam,
			HttpServletRequest request) throws Exception {
		String processInstanceId = apiServerStreamTranscodingService.addFileTask(fileUrl, playTime, seek, record, bePCM, mediaType, recordCallback, progNum, deviceIp, task, inputParam);
		
		return processInstanceId;
	}
	
	/**
	 * 删除转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:08:19
	 * @param Long id 任务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/task")
	public Object deleteTask(String id, HttpServletRequest request) throws Exception{
		String processInstanceId = apiServerStreamTranscodingService.deleteTask(id);
		return processInstanceId;
	}
	
	/**
	 * 根据任务id添加输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 * @param String outputParam 输出信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(String id, String outputParam, HttpServletRequest request) throws Exception {
		
		transformService.addOutput(id, outputParam);
		
		return null;
	}
	
	/**
	 * 根据任务id删除输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 * @param String outputParam 输出信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/output")
	public Object deleteOutput(String id, String outputParam, HttpServletRequest request) throws Exception {
		
		transformService.deleteOutput(id, outputParam);
		
		return null;
	}
	
	/**
	 * 根据任务id删除所有输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/output/all")
	public Object deleteAllOutput(String id, HttpServletRequest request) throws Exception {
		
		transformService.deleteAllOutput(id);
		
		return null;
	}
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/check/edit")
	public Object checkEdit(Long mediaId, String mediaType, HttpServletRequest request) throws Exception {
		MediaAVideoVO media = mediaAVideoQuery.loadByIdAndType(mediaId, mediaType);
		if (media == null) return null;
		return apiServerStreamTranscodingService.ifMediaEdit(media);
	}
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/check/edit/by/uuid")
	public Object checkEdit(String uuid, HttpServletRequest request) throws Exception {
		HashMapWrapper<String, MediaAVideoVO> map = mediaAVideoQuery.getByUuids(new ArrayListWrapper<String>().add(uuid).getList());
		if (map != null && map.containsKey(uuid)) {
			MediaAVideoVO media = map.getMap().get(uuid);
			if (media != null) return apiServerStreamTranscodingService.ifMediaEdit(media);
		}
		return null;
	}
}
