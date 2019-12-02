package com.sumavision.tetris.streamTranscoding.api.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.addOutput.AddOutputService;
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
	private MediaAVideoQuery mediaAVideoQuery;
	
	@Autowired
	private AddOutputService addOutputService;
	
	public StreamTranscodingProcessVO fileParamFormat(String assetPath, boolean record, Integer playCount, String stopCallback, String mediaType, String recordCallback, Integer progNum, String task) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setFileUrl(assetPath);
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
	
	public StreamTranscodingProcessVO streamParamFormat(Long assetId, String assetPath, boolean record, Integer bePCM, String mediaType, String recordCallback, Integer progNum, String task) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setNeed(false);
		
		StreamTranscodingVO streamTranscodingVO = new StreamTranscodingVO();
		if ((assetPath == null || assetPath.isEmpty()) && assetId != null) {
			MediaAVideoVO media = mediaAVideoQuery.loadByIdAndType(assetId, mediaType);
			assetPath = media.getPreviewUrl();
		}
		streamTranscodingVO.setAssetUrl(assetPath);
		streamTranscodingVO.setNeedRecordOutput(record);
		streamTranscodingVO.setBePCM(bePCM);
		streamTranscodingVO.setMediaType(mediaType);
		streamTranscodingVO.setProgNum(progNum);
		streamTranscodingVO.setTranscoding(true);
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
