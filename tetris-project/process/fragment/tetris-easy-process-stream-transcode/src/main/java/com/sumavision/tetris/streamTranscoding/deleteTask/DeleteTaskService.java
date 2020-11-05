package com.sumavision.tetris.streamTranscoding.deleteTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.record.RecordService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskQuery;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskVO;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.ToolRequestType;
import com.sumavision.tetris.streamTranscoding.deleteTask.requestVO.InputVO;
import com.sumavision.tetris.streamTranscoding.deleteTask.requestVO.MessageVO;
import com.sumavision.tetris.streamTranscoding.deleteTask.requestVO.TargetVO;
import com.sumavision.tetris.streamTranscoding.deleteTask.responseVO.ResponseVO;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeleteTaskService {
	
	@Autowired
	private StreamTranscodingAdapter streamTranscodingAdapter;
	
	@Autowired
	private StreamTranscodingTaskQuery streamTranscodingTaskQuery;
	
	@Autowired
	private StreamTranscodingTaskService streamTranscodingTaskService;
	
	@Autowired
	private RecordService recordService;
	
	public void delete(UserVO user, Long messageId) throws Exception {
		
		StreamTranscodingTaskVO task = streamTranscodingTaskQuery.questByMessageId(messageId);
		
		StreamTranscodingTaskVO newTask = streamTranscodingTaskService.addTaskForDeleteInput(user, task.getInputId());
		
		InputVO inputVO = new InputVO(newTask.getInputId());
		
		TargetVO targetVO = new TargetVO(inputVO);
		
		MessageVO messageVO = new MessageVO(newTask.getId(), targetVO);
		
		ResponseVO responseVO = streamTranscodingAdapter.deleteTask(messageVO);
		
		if (responseVO.getAck() != 0) {
			throw new HttpRequestErrorException(ToolRequestType.DELETE_TASK.getAction());
		}
		
		if (task.isRecord()) {
			recordService.delete(messageId, null);
		}
	}
}
