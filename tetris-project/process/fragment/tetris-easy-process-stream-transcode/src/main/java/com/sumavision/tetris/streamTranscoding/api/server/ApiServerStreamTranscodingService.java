package com.sumavision.tetris.streamTranscoding.api.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.addOutput.AddOutputService;
import com.sumavision.tetris.streamTranscoding.editor.TranscodeExtensionParamPermissionDAO;
import com.sumavision.tetris.streamTranscoding.editor.TranscodeExtensionParamPermissionPO;
import com.sumavision.tetris.streamTranscoding.editor.TranscodeMediaVO;
import com.sumavision.tetris.streamTranscoding.exception.MediaStreamHasNoPreviewUrlException;
import com.sumavision.tetris.streamTranscodingProcessVO.FileToStreamVO;
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
	private AddOutputService addOutputService;
	
	@Autowired
	private TranscodeExtensionParamPermissionDAO transcodeExtensionParamPermissionDAO;
	
	public StreamTranscodingProcessVO fileParamFormat(
			MediaAVideoVO media,
			boolean record,
			Integer playCount,
			String stopCallback,
			String mediaType,
			String recordCallback,
			Integer progNum,
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
		
		StreamTranscodingVO streamTranscodingVO = new StreamTranscodingVO();
		streamTranscodingVO.setTranscoding(true);
		streamTranscodingVO.setNeedRecordOutput(false);
		streamTranscodingVO.setBePCM(0);
		streamTranscodingVO.setMediaType(mediaType);
		streamTranscodingVO.setProgNum(progNum);
		if (inputParam != null && !inputParam.isEmpty()) {
			InputParamVO inputParamVO = JSON.parseObject(inputParam, InputParamVO.class);
			streamTranscodingVO.setInputParam(inputParamVO);
		}
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);
		List<OutParamVO> outParamVOs = taskVO.getOutParam();
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(record);
		if (record) {
//			List<String> excepPort = new ArrayList<String>();
//			for (OutParamVO outParamVO : outParamVOs) {
//				excepPort.add(outParamVO.getOutputUrl().split(":")[1]);
//			}
//			String recordPort = addOutputService.newOutputPort(excepPort);
//			String recordIp = streamTranscodingAdapter.getRecordInfo().get("ip");
			recordVO.setRecordCallback(recordCallback);
//			recordVO.setAssetIp(recordIp);
//			recordVO.setAssetPort(recordPort);
		}
		streamTranscodingVO.setTaskVO(taskVO);
		
		return new StreamTranscodingProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamTranscodingVO(streamTranscodingVO)
				.setRecordVO(recordVO);
	}
	
	public Map<String, String> ifMediaEdit(MediaAVideoVO media) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if (media == null) return null;
		String[] mediaSplit = media.getPreviewUrl().split("\\.");
		String end = mediaSplit[mediaSplit.length - 1];
		TranscodeExtensionParamPermissionPO permissionPO = transcodeExtensionParamPermissionDAO.findByExtension(end);
		if (permissionPO == null) return null;
		TranscodeMediaVO transcodeMediaVO = new TranscodeMediaVO();
		transcodeMediaVO.setUuid(media.getUuid());
		transcodeMediaVO.setStartTime(0l);
		transcodeMediaVO.setEndTime(Long.parseLong(media.getDuration()));
		map.put("transcodeJob", JSONArray.toJSONString(new ArrayListWrapper<TranscodeMediaVO>().add(transcodeMediaVO).getList()));
		map.put("param", permissionPO.getParam());
		if (media.getName().endsWith(end)) {
			map.put("name", media.getName().split("\\." + end)[0]);
		} else {
			map.put("name", media.getName());
		}
		map.put("folderId", media.getFolderId().toString());
		return map;
	}
	
	public StreamTranscodingProcessVO streamParamFormat(
			Long assetId,
			String assetPath,
			boolean record,
			Integer bePCM,
			String mediaType,
			String recordCallback,
			Integer progNum,
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
		streamTranscodingVO.setBePCM(bePCM);
		streamTranscodingVO.setMediaType(mediaType);
		streamTranscodingVO.setProgNum(progNum);
		streamTranscodingVO.setTranscoding(true);
		if (inputParam != null && !inputParam.isEmpty()) {
			InputParamVO inputParamVO = JSON.parseObject(inputParam, InputParamVO.class);
			streamTranscodingVO.setInputParam(inputParamVO);
		}
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);
		List<OutParamVO> outParamVOs = taskVO.getOutParam();
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(record);
		if (record) {
//			List<String> excepPort = new ArrayList<String>();
//			for (OutParamVO outParamVO : taskVO.getOutParam()) {
//				excepPort.add(outParamVO.getOutputUrl().split(":")[1]);
//			}
//			String recordPort = addOutputService.newOutputPort(excepPort);
//			String recordIp = streamTranscodingAdapter.getRecordInfo().get("ip");
			recordVO.setRecordCallback(recordCallback);
//			recordVO.setAssetIp(recordIp);
//			recordVO.setAssetPort(recordPort);
		}
		streamTranscodingVO.setTaskVO(taskVO);
		
		return new StreamTranscodingProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamTranscodingVO(streamTranscodingVO)
				.setRecordVO(recordVO);
	}
}
