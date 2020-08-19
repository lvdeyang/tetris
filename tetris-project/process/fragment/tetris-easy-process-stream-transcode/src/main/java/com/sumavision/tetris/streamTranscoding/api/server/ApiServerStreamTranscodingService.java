package com.sumavision.tetris.streamTranscoding.api.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mims.app.media.editor.MediaFileEditorVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.exception.MediaStreamHasNoPreviewUrlException;
import com.sumavision.tetris.streamTranscoding.extension.deal.editor.TranscodeExtensionParamPermissionDAO;
import com.sumavision.tetris.streamTranscoding.extension.deal.editor.TranscodeExtensionParamPermissionPO;
import com.sumavision.tetris.streamTranscoding.extension.deal.editor.TranscodeMediaVO;
import com.sumavision.tetris.streamTranscoding.extension.deal.stream.TranscodeStreamExtensionDAO;
import com.sumavision.tetris.streamTranscoding.extension.deal.stream.TranscodeStreamExtensionPO;
import com.sumavision.tetris.streamTranscodingProcessVO.FileToStreamVO;
import com.sumavision.tetris.streamTranscodingProcessVO.FileVO;
import com.sumavision.tetris.streamTranscodingProcessVO.RecordVO;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingProcessVO;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiServerStreamTranscodingService {
	@Autowired
	private StreamTranscodingAdapter streamTranscodingAdapter;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	@Autowired
	private TranscodeExtensionParamPermissionDAO transcodeExtensionParamPermissionDAO;
	
	@Autowired
	private TranscodeStreamExtensionDAO transcodeStreamExtensionDAO;
	
	@Autowired
	private ProcessService processService;
	
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
	public String addTask(
			Long assetId,
			String assetPath,
			boolean record,
			Integer bePCM,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String inputParam) throws Exception {
		StreamTranscodingProcessVO processVO = streamParamFormat(assetId, assetPath, record, bePCM, mediaType, recordCallback, progNum, deviceIp, task, inputParam);
		JSONObject variables = new JSONObject();
		variables.put("_pa51_file_fileToStreamInfo", JSON.toJSONString(processVO.getFileToStreamVO()));
		variables.put("_pa51_file_streamTranscodingInfo", JSON.toJSONString(processVO.getStreamTranscodingVO()));
		variables.put("_pa51_file_recordInfo", JSON.toJSONString(processVO.getRecordVO()));
		variables.put("ifMediaEdit", false);
		
		return processService.startByKey("_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
	}
	
	/**
	 * 文件推流流转码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:01:59
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
	 */
	public String addFileTask(
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
			String inputParam) throws Exception {
		StreamTranscodingProcessVO processVO = fileParamFormat(fileUrl, playTime, seek, record, bePCM, mediaType, recordCallback, progNum, deviceIp, task, inputParam);
		JSONObject variables = new JSONObject();
		variables.put("_pa51_file_fileToStreamInfo", JSON.toJSONString(processVO.getFileToStreamVO()));
		variables.put("_pa51_file_streamTranscodingInfo", JSON.toJSONString(processVO.getStreamTranscodingVO()));
		variables.put("_pa51_file_recordInfo", JSON.toJSONString(processVO.getRecordVO()));
		variables.put("ifMediaEdit", false);
		
		return processService.startByKey("_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
	}
	

	/**
	 * 删除转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:08:19
	 * @param Long id 任务id
	 */
	public String deleteTask(String id) throws Exception {
		JSONObject variables = new JSONObject();
		variables.put("_pa52_messageId", id);
		variables.put("_pa54_messageId", id);
		variables.put("_pa55_messageId", id);
		String processInstanceId = processService.startByKey("_delete_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
		return processInstanceId;
	}
	
	/**
	 * 把文件转流参数转换为流程节点VO<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:08:30
	 * @param MediaAVideoVO media 媒资信息
	 * @param Boolean record 是否录制
	 * @param Integer playCount 文件循环次数
	 * @param String mediaType 文件媒资类型
	 * @param String recordCallback 录制回调地址
	 * @param Integer progNum 节目号
	 * @param String task 输出参数
	 * @param String inputParam 输入参数
	 */
	public StreamTranscodingProcessVO fileParamFormat(
			MediaAVideoVO media,
			boolean record,
			Integer playCount,
			String stopCallback,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String inputParam) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setFileUrl(media.getPreviewUrl());
		fileToStreamVO.setDuration(media.getDuration());
		fileToStreamVO.setNeed(true);
		fileToStreamVO.setPlayCount(playCount);
		fileToStreamVO.setStartPort(streamTranscodingAdapter.getRecordInfo().get("startPort"));
		fileToStreamVO.setStopCallback(stopCallback);
		fileToStreamVO.setToolIp(streamTranscodingAdapter.getToolInfo().get("ip").toString());
		MediaFileEditorVO editorVO = media.getEditorInfo();
		if (editorVO != null) {
			String previewUrl = editorVO.getPreviewUrl();
			String duration = editorVO.getDuration();
			if (previewUrl != null && !previewUrl.isEmpty()) fileToStreamVO.setFileUrl(previewUrl);
			if (duration != null && !duration.isEmpty() && !duration.equals("-")) fileToStreamVO.setDuration(duration);
		}
		
		StreamTranscodingVO streamTranscodingVO = new StreamTranscodingVO();
		streamTranscodingVO.setTranscoding(true);
		streamTranscodingVO.setNeedRecordOutput(false);
		streamTranscodingVO.setBePCM(0);
		streamTranscodingVO.setMediaType(mediaType);
		streamTranscodingVO.setProgNum(progNum);
		streamTranscodingVO.setDeviceIp(deviceIp);
		if (inputParam != null && !inputParam.isEmpty()) {
			InputParamVO inputParamVO = JSON.parseObject(inputParam, InputParamVO.class);
			streamTranscodingVO.setInputParam(inputParamVO);
		}
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);
		List<OutParamVO> outParamVOs = taskVO.getOutParam();
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(record);
		if (record) {
			recordVO.setRecordCallback(recordCallback);
		}
		streamTranscodingVO.setTaskVO(taskVO);
		
		return new StreamTranscodingProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamTranscodingVO(streamTranscodingVO)
				.setRecordVO(recordVO);
	}
	
	/**
	 * 获取文件转流参数<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:11:29
	 * @param MediaAVideoVO media 文件信息
	 * @return String transcodeJob 转码参数
	 * @return String param 转码模板
	 * @return String name 输出资源名称
	 * @return String folderId 资源目录id
	 */
	public FileDealVO ifMediaEdit(MediaAVideoVO media) throws Exception {
		FileDealVO fileDealVO = new FileDealVO();
		if (media == null) return fileDealVO;
		
		//获取文件扩展名
		String[] mediaSplit = media.getPreviewUrl().split("\\.");
		String extension = mediaSplit[mediaSplit.length - 1];
		
		//判断流转码能力是否支持该文件格式
		TranscodeStreamExtensionPO streamExtensionPO = transcodeStreamExtensionDAO.findByExtension(extension);
		if (streamExtensionPO != null) {
			fileDealVO.setIfFileStream(true);
			fileDealVO.setFileUrl(media.getPreviewUrl());
			String duration = media.getDuration();
			if (duration != null && !duration.isEmpty() && !duration.equals("-")) fileDealVO.setDuration(Long.parseLong(duration));
		}
		
		//如果文件已经转码，则不再进行判断，直接使用推流能力
		MediaFileEditorVO editorVO = media.getEditorInfo();
		if (editorVO == null || editorVO.getPreviewUrl() == null || editorVO.getPreviewUrl().isEmpty()) {
			//判断是否有文件转码配置
			TranscodeExtensionParamPermissionPO permissionPO = transcodeExtensionParamPermissionDAO.findByExtension(extension);
			if (permissionPO != null) {
				TranscodeMediaVO transcodeMediaVO = new TranscodeMediaVO();
				transcodeMediaVO.setUuid(media.getUuid());
				transcodeMediaVO.setStartTime(0l);
				transcodeMediaVO.setEndTime(Long.parseLong(media.getDuration()));
				fileDealVO.setIfMediaEdit(true)
				.setTranscodeJob(JSONArray.toJSONString(new ArrayListWrapper<TranscodeMediaVO>().add(transcodeMediaVO).getList()))
				.setParam(permissionPO.getParam())
				.setName(media.getName().endsWith(extension) ? media.getName().split("\\." + extension)[0] : media.getName())
				.setFolderId(media.getFolderId().toString());
			}
		} else {
			//如果原文件不可直接使用流转码能力推流，则判断转码后的文件是否可以
			if (streamExtensionPO == null) {
				String[] newMediaSplit = editorVO.getPreviewUrl().split("\\.");
				String newExtension = mediaSplit[newMediaSplit.length - 1];
				TranscodeStreamExtensionPO newStreamExtensionPO = transcodeStreamExtensionDAO.findByExtension(newExtension);
				if (newStreamExtensionPO != null) {
					fileDealVO.setIfFileStream(true);
					fileDealVO.setFileUrl(editorVO.getPreviewUrl());
					String duration = editorVO.getDuration();
					if (duration != null && !duration.isEmpty() && !duration.equals("-")) fileDealVO.setDuration(Long.parseLong(duration));
				}
			}
		}
		
		return fileDealVO;
	}
	
	/**
	 * 把流转码参数转换为流程节点VO<br/>
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
	public StreamTranscodingProcessVO streamParamFormat(
			Long assetId,
			String assetPath,
			boolean record,
			Integer bePCM,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String inputParam) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setNeed(false);
		
		StreamTranscodingVO streamTranscodingVO = new StreamTranscodingVO();
		if ((assetPath == null || assetPath.isEmpty()) && assetId != null) {
			switch (mediaType.toLowerCase()) {
			case "video":
				MediaVideoStreamVO mediaVideoStreamVO = mediaVideoStreamQuery.findById(assetId);
				List<String> urls = mediaVideoStreamVO.getPreviewUrl();
				if (urls == null || urls.isEmpty()) throw new MediaStreamHasNoPreviewUrlException(assetId);
				assetPath = mediaVideoStreamVO.getPreviewUrl().get(0);
				break;
			case "audio":
				MediaAudioStreamVO mediaAudioStreamVO = mediaAudioStreamQuery.findById(assetId);
				assetPath = mediaAudioStreamVO.getPreviewUrl();
				break;
			default:
				throw new ErrorTypeException("mediaType", mediaType);
			}
		}
		streamTranscodingVO.setAssetUrl(assetPath);
		streamTranscodingVO.setNeedRecordOutput(record);
		streamTranscodingVO.setBePCM(bePCM == null ? 0 : bePCM);
		streamTranscodingVO.setMediaType(mediaType);
		streamTranscodingVO.setProgNum(progNum);
		streamTranscodingVO.setTranscoding(true);
		streamTranscodingVO.setDeviceIp(deviceIp);
		if (inputParam != null && !inputParam.isEmpty()) {
			InputParamVO inputParamVO = JSON.parseObject(inputParam, InputParamVO.class);
			streamTranscodingVO.setInputParam(inputParamVO);
		}
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);
		List<OutParamVO> outParamVOs = taskVO.getOutParam();
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(record);
		if (record) {
			recordVO.setRecordCallback(recordCallback);
		}
		streamTranscodingVO.setTaskVO(taskVO);
		
		return new StreamTranscodingProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamTranscodingVO(streamTranscodingVO)
				.setRecordVO(recordVO);
	}
	
	/**
	 * 把流转码参数转换为流程节点VO<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:01:59
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
	 */
	public StreamTranscodingProcessVO fileParamFormat(
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
			String inputParam) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setNeed(false);
		
		StreamTranscodingVO streamTranscodingVO = new StreamTranscodingVO();
		streamTranscodingVO.setFiles(new ArrayListWrapper<FileVO>().add(new FileVO().setUrl(streamTranscodingAdapter.changeHttpToFtp(fileUrl)).setCount(playTime)).getList());
		streamTranscodingVO.setNeedRecordOutput(record);
		streamTranscodingVO.setBePCM(bePCM == null ? 0 : bePCM);
		streamTranscodingVO.setMediaType(mediaType);
		streamTranscodingVO.setProgNum(progNum);
		streamTranscodingVO.setTranscoding(true);
		streamTranscodingVO.setDeviceIp(deviceIp);
		if (inputParam != null && !inputParam.isEmpty()) {
			InputParamVO inputParamVO = JSON.parseObject(inputParam, InputParamVO.class);
			streamTranscodingVO.setInputParam(inputParamVO);
		}
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(record);
		if (record) {
			recordVO.setRecordCallback(recordCallback);
		}
		streamTranscodingVO.setTaskVO(taskVO);
		
		return new StreamTranscodingProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamTranscodingVO(streamTranscodingVO)
				.setRecordVO(recordVO);
	}
}
