package com.sumavision.tetris.oldCMS.api.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.oldCMS.StreamAdapter;
import com.sumavision.tetris.streamTranscodingProcessVO.FileToStreamVO;
import com.sumavision.tetris.streamTranscodingProcessVO.RecordVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiServerStreamService {
	@Autowired
	private StreamAdapter streamAdapter;
	
	public StreamProcessVO fileParamFormat(
			String assetPath,
			String duration,
			Integer playTime,
			String mediaType,
			Integer audioType,
			String audioParam,
			Integer esType,
			Boolean record,
			String recordCallback,
			Integer streamPubType,
			String sipParam,
			String videoParam,
			String outputParam,
			String stopCallBack) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setFileUrl(assetPath);
		fileToStreamVO.setDuration(duration);
		fileToStreamVO.setNeed(true);
		fileToStreamVO.setPlayCount(playTime);
		fileToStreamVO.setStartPort(streamAdapter.getOldCMSInfo().get("startPort"));
		fileToStreamVO.setStopCallback(stopCallBack);
		fileToStreamVO.setToolIp(streamAdapter.getOldCMSInfo().get("ip").toString());
		
		StreamVO streamVO = new StreamVO();
		streamVO.setIsTranscoding(true);
		streamVO.setMediaType(mediaType);
		streamVO.setAudioType(audioType);
		streamVO.setAudioParam(audioParam);
		streamVO.setEsType(esType);
		streamVO.setStreamPubType(streamPubType);
		streamVO.setSipParam(sipParam);
		streamVO.setVideoParam(videoParam);
		streamVO.setOutputParam(outputParam);
		streamVO.setLocalIp(streamAdapter.getOldCMSInfo().get("ip").toString());
		streamVO.setRecord(record);
		streamVO.setRecordCallback(recordCallback);
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(false);
		
		return new StreamProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamVO(streamVO)
				.setRecordVO(recordVO);
	}
	
	public StreamProcessVO streamParamFormat(
			String assetPath,
			String mediaType,
			Integer audioType,
			String audioParam,
			Integer bePCM,
			Integer esType,
			Boolean record,
			String recordCallback,
			Integer streamPubType,
			String inputParam,
			String sipParam,
			String videoParam,
			Integer progNum,
			String outputParam,
			String dataParam) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setNeed(false);
		
		StreamVO streamVO = new StreamVO();
		streamVO.setAssetPath(assetPath);
		streamVO.setIsTranscoding(true);
		streamVO.setMediaType(mediaType);
		streamVO.setAudioType(audioType);
		streamVO.setAudioParam(audioParam);
		streamVO.setBePCM(bePCM);
		streamVO.setEsType(esType);
		streamVO.setStreamPubType(streamPubType);
		streamVO.setInputParam(inputParam);
		streamVO.setSipParam(sipParam);
		streamVO.setVideoParam(videoParam);
		streamVO.setProgNum(progNum == null ? null : (long)progNum);
		streamVO.setOutputParam(outputParam);
		streamVO.setDataParam(dataParam);
		streamVO.setLocalIp(streamAdapter.getOldCMSInfo().get("ip").toString());
		streamVO.setRecord(record);
		streamVO.setRecordCallback(recordCallback);
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(false);
		
		return new StreamProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamVO(streamVO)
				.setRecordVO(recordVO);
	}
}
