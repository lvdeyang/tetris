package com.sumavision.tetris.transcoding.addTask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskService;
import com.sumavision.tetris.transcoding.Adapter;
import com.sumavision.tetris.transcoding.RequestCmdType;
import com.sumavision.tetris.transcoding.addTask.requestVO.AddTaskVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.MsgHeaderVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TranscodeJobsVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TranscodeVO;
import com.sumavision.tetris.transcoding.addTask.rsponseVO.AddTaskResponseVO;
import com.sumavision.tetris.transcoding.getStatus.GetStatusHeartbeatThread;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddTaskService {
	
	@Autowired
	Adapter adapter;
	
	@Autowired
	MediaEditorTaskService mediaEditorTaskService;
	
	private GetStatusHeartbeatThread getStatusHeartbeatThread;
	
	/**
	 * 添加转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<String> 下成功的转码任务id
	 */
	public List<String> add(String __processInstanceId__, Long __accessPointId__,String transcodes) throws Exception{
		List<TranscodeVO> transcodeVOs = JSONObject.parseArray(transcodes, TranscodeVO.class);
		TranscodeJobsVO transcodeJobs = new TranscodeJobsVO();
		transcodeJobs.setTranscode(transcodeVOs);
		
		MsgHeaderVO msgHeader = new MsgHeaderVO();
		msgHeader.setCmdType(RequestCmdType.ADD_TASK.getTypeName());
		msgHeader.setTransactionId(adapter.getTransactionId());
		
		AddTaskVO addTask = new AddTaskVO();
		addTask.setMsgHeader(msgHeader);
		addTask.setTranscodeJobs(transcodeJobs);
		
		AddTaskResponseVO response = adapter.addTask(addTask);
		return dealAddResponse(response.getTranscodeJobs().getTranscodes(), __processInstanceId__, __accessPointId__);
	}
	
	/**
	 * 解析添加转码任务的云转码返回信息，开启循环获取进度进程<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return HashMapWrapper<String, Integer> 转码任务id，转码任务进度
	 */
	public List<String> dealAddResponse(List<com.sumavision.tetris.transcoding.addTask.rsponseVO.TranscodeVO> transcodes, String __processInstanceId__, Long __accessPointId__) throws Exception{
		if (transcodes == null || transcodes.size() < 0) {
			return null;
		}else {
			List<String> ids = new ArrayList<String>();
			for(com.sumavision.tetris.transcoding.addTask.rsponseVO.TranscodeVO transcode:transcodes){
				if(transcode.getResultCode() == 0 && transcode.getResultString().equals("OK")){
					ids.add(transcode.getId());
				}
			}
			
			//存数据库
			mediaEditorTaskService.addMediaEditorTask(__processInstanceId__, __accessPointId__, ids);
			
			//开启获取任务进度线程
			if (getStatusHeartbeatThread == null) {
				getStatusHeartbeatThread = new GetStatusHeartbeatThread();
				getStatusHeartbeatThread.start();
			}else {
				getStatusHeartbeatThread.setSleep(5000);
			}
			
			return ids;
		}
	}
}
