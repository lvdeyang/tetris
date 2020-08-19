package com.sumavision.tetris.streamTranscoding.api.server;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscodingProcessVO.FileVO;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingProcessVO;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/stream/transcoding")
public class ApiServerStreamTranscodingController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ApiServerStreamTranscodingService apiServerStreamTranscodingService;
	
	@Autowired
	private StreamTranscodingAdapter adapter;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	@Autowired
	private ProcessService processService;
	
	/**
	 * 添加文件转流转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午9:55:00
	 * @param Long assetId 文件id
	 * @param Boolean record 是否录制
	 * @param Integer playTime 文件循环次数
	 * @param String mediaType 文件媒资类型
	 * @param String recordCallback 录制回调地址
	 * @param Integer progNum 节目号
	 * @param String task 输出参数
	 * @param String stopCallback 转流停止回调
	 * @param String inputParam 输入参数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task/file")
	public Object addTaskFile(
			Long assetId,
			boolean record,
			Integer playTime,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String stopCallback,
			String inputParam,
			HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		MediaAVideoVO media = mediaAVideoQuery.loadByIdAndType(assetId, mediaType);
		if (media == null) return null;
		
		StreamTranscodingProcessVO processVO = apiServerStreamTranscodingService.fileParamFormat(media, record, playTime, stopCallback, mediaType, recordCallback, progNum, deviceIp, task, inputParam);
		JSONObject variables = new JSONObject();
		
		//判断是否文件转码
		FileDealVO fileDeal = apiServerStreamTranscodingService.ifMediaEdit(media);
		if (fileDeal.getIfFileStream()) {
			variables.put("ifMediaEdit", false);
			StreamTranscodingVO streamTranscodingVO = processVO.getStreamTranscodingVO();
			streamTranscodingVO.setFiles(
					new ArrayListWrapper<FileVO>()
					.add(new FileVO().setUrl(adapter.changeHttpToFtp(fileDeal.getFileUrl())).setCount(playTime).setSeek(0l)).getList()
					);
		} else {
			if (fileDeal.getIfMediaEdit()) {
				variables.put("ifMediaEdit", true);
				variables.put("_pa60_transcodeJob", fileDeal.getTranscodeJob());
				variables.put("_pa60_param", fileDeal.getParam());
				variables.put("_pa60_name", fileDeal.getName());
				variables.put("_pa60_folderId", fileDeal.getFolderId());
			}
		}
		
		variables.put("_pa51_file_fileToStreamInfo", JSON.toJSONString(processVO.getFileToStreamVO()));
		variables.put("_pa51_file_streamTranscodingInfo", JSON.toJSONString(processVO.getStreamTranscodingVO()));
		variables.put("_pa51_file_recordInfo", JSON.toJSONString(processVO.getRecordVO()));
		
		String processInstanceId = processService.startByKey("_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
		
		return new HashMapWrapper<String, Object>().put("id", processInstanceId)
				.getMap();
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
	@RequestMapping(value = "/add/task")
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
			HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		String processInstanceId = apiServerStreamTranscodingService.addTask(assetId, assetPath, record, bePCM, mediaType, recordCallback, progNum, deviceIp, task, inputParam);
		
		return new HashMapWrapper<String, Object>().put("id", processInstanceId)
				.getMap();
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
		UserVO user = userQuery.current();
		System.out.println("start:" + new Date());
		String processInstanceId = apiServerStreamTranscodingService.deleteTask(id);
		System.out.println("end:" + new Date());
		return new HashMapWrapper<String, Object>().put("id", processInstanceId)
				.getMap();
	}
}
