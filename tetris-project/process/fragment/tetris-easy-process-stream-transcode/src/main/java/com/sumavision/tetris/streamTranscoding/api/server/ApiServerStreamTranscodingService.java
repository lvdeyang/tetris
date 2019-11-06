package com.sumavision.tetris.streamTranscoding.api.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
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
			List<String> excepPort = new ArrayList<String>();
			for (OutParamVO outParamVO : outParamVOs) {
				excepPort.add(outParamVO.getOutputUrl().split(":")[1]);
			}
			String recordPort = addOutputService.newOutputPort(excepPort);
			String recordIp = streamTranscodingAdapter.getRecordInfo().get("ip");
			recordVO.setRecordCallback(recordCallback);
			recordVO.setAssetIp(recordIp);
			recordVO.setAssetPort(recordPort);
			
			//给流转码添加一路输出到录制
			OutParamVO recordOutParamVO = outParamVOs.get(outParamVOs.size() - 1).copy();
			recordOutParamVO.setOutputUrl(new StringBufferWrapper().append("udp://").append(recordIp).append(":").append(recordPort).toString());
			outParamVOs.add(recordOutParamVO);
		}
		streamTranscodingVO.setTaskVO(taskVO);
		
		return new StreamTranscodingProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamTranscodingVO(streamTranscodingVO)
				.setRecordVO(recordVO);
	}
	
	public StreamTranscodingProcessVO streamParamFormat(String assetPath, boolean record, Integer bePCM, String mediaType, String recordCallback, Integer progNum, String task) throws Exception{
		FileToStreamVO fileToStreamVO = new FileToStreamVO();
		fileToStreamVO.setNeed(false);
		
		StreamTranscodingVO streamTranscodingVO = new StreamTranscodingVO();
		streamTranscodingVO.setAssetUrl(assetPath);
		streamTranscodingVO.setNeedRecordOutput(record);
		streamTranscodingVO.setBePCM(bePCM);
		streamTranscodingVO.setMediaType(mediaType);
		streamTranscodingVO.setProgNum(progNum);
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);
		List<OutParamVO> outParamVOs = taskVO.getOutParam();
		
		RecordVO recordVO = new RecordVO();
		recordVO.setRecord(record);
		if (record) {
			List<String> excepPort = new ArrayList<String>();
			for (OutParamVO outParamVO : taskVO.getOutParam()) {
				excepPort.add(outParamVO.getOutputUrl().split(":")[1]);
			}
			String recordPort = addOutputService.newOutputPort(excepPort);
			String recordIp = streamTranscodingAdapter.getRecordInfo().get("ip");
			recordVO.setRecordCallback(recordCallback);
			recordVO.setAssetIp(recordIp);
			recordVO.setAssetPort(recordPort);
			
			if (bePCM == 0) {
				//给流转码添加一路输出到录制
				streamTranscodingVO.setTranscoding(false);
				OutParamVO recordOutParamVO = outParamVOs.get(outParamVOs.size() - 1).copy();
				recordOutParamVO.setOutputUrl(new StringBufferWrapper().append("udp://").append(recordIp).append(":").append(recordPort).toString());
				outParamVOs.add(recordOutParamVO);
			} else {
				streamTranscodingVO.setTranscoding(true);
			}
		}
		streamTranscodingVO.setTaskVO(taskVO);
		
		return new StreamTranscodingProcessVO().setFileToStreamVO(fileToStreamVO)
				.setStreamTranscodingVO(streamTranscodingVO)
				.setRecordVO(recordVO);
	}
}
