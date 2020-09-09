package com.sumavision.tetris.transcoding.getStatus;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.transcoding.Adapter;
import com.sumavision.tetris.transcoding.RequestCmdType;
import com.sumavision.tetris.transcoding.addTask.requestVO.AddTaskVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.MsgHeaderVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TranscodeJobsVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TranscodeVO;
import com.sumavision.tetris.transcoding.getStatus.VO.GetStatusResponseVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class GetStatusService {
	@Autowired
	Adapter adapter;

	/**
	 * 过适配器获取转码任务进度<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return HashMapWrapper<String, Integer> 转码任务id，转码任务进度
	 */
	public HashMapWrapper<String, Integer> getFromAdapter(List<String> ids) throws Exception {
		if (ids == null || ids.size() <= 0)
			return null;

		AddTaskVO getStatus = new AddTaskVO();

		MsgHeaderVO msgHeader = new MsgHeaderVO();
		msgHeader.setTransactionId(adapter.getTransactionId());
		msgHeader.setCmdType(RequestCmdType.GET_STATUS.getTypeName());

		TranscodeJobsVO transcodeJobs = new TranscodeJobsVO();
		List<TranscodeVO> transcodes = new ArrayList<TranscodeVO>();

		for (String id : ids) {
			TranscodeVO transcode = new TranscodeVO();
			transcode.setId(id);
			transcodes.add(transcode);
		}
		
		transcodeJobs.setTranscode(transcodes);
		
		getStatus.setMsgHeader(msgHeader);
		getStatus.setTranscodeJobs(transcodeJobs);
		
		GetStatusResponseVO response = adapter.questStatus(getStatus);
		
		HashMapWrapper<String, Integer> idToRate = analysisResponse(response);
		
		return idToRate;
	}
	
	/**
	 * 解析获取进度返回信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return HashMapWrapper<String, Integer> 转码任务id，转码任务进度
	 */
	private HashMapWrapper<String, Integer> analysisResponse(GetStatusResponseVO response){
		HashMapWrapper<String, Integer> analysisReturn = new HashMapWrapper<String, Integer>();
		
		List<com.sumavision.tetris.transcoding.getStatus.VO.TranscodeVO> transcodes = response.getTranscodeJobs().getTranscodes();
		
		if (transcodes != null && transcodes.size() > 0) {
			for(com.sumavision.tetris.transcoding.getStatus.VO.TranscodeVO transcode: transcodes){
				if (transcode.getResultString().equals("OK")) {
					analysisReturn.put(transcode.getId(), transcode.getCompleteRate());
				}
			}
		}
		
		return analysisReturn;
	}
}
