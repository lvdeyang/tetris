package com.sumavision.tetris.streamTranscoding.deleteOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskOutputPermissionQuery;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskOutputPermissionVO;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskQuery;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskVO;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.ToolRequestType;
import com.sumavision.tetris.streamTranscoding.addOutput.requestVO.InputVO;
import com.sumavision.tetris.streamTranscoding.api.server.OutParamVO;
import com.sumavision.tetris.streamTranscoding.deleteOutput.requestVO.MessageVO;
import com.sumavision.tetris.streamTranscoding.deleteOutput.requestVO.TargetVO;
import com.sumavision.tetris.streamTranscoding.deleteOutput.responseVO.ResponseVO;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeleteOutputService {

	@Autowired
	private StreamTranscodingAdapter streamTranscodingAdapter;
	
	@Autowired
	private StreamTranscodingTaskService streamTranscodingTaskService;
	
	@Autowired
	private StreamTranscodingTaskQuery streamTranscodingTaskQuery;
	
	@Autowired
	private StreamTranscodingTaskOutputPermissionQuery streamTranscodingTaskOutputPermissionQuery;
	
	/**
	 * 流转码任务删除输出(直接请求小工具)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:49:03
	 * @param user 用户信息
	 * @param outParamVO 输出信息
	 */
	public void delete(UserVO user, OutParamVO outParamVO) throws Exception{
		
		String[] urlSplit = outParamVO.getOutputUrl().split(":");
		
		StreamTranscodingTaskOutputPermissionVO permissionVO = streamTranscodingTaskOutputPermissionQuery.queryByOutputIpAndPort(urlSplit[0], urlSplit[1]);
		
		StreamTranscodingTaskVO taskVO = streamTranscodingTaskQuery.questByMessageId(permissionVO.getMessageId());
		
		StreamTranscodingTaskVO newTaskVO = streamTranscodingTaskService.addTaskForDeleteOutput(user, taskVO.getInputId(), taskVO.getProgNum());
		
		InputVO input = new InputVO(taskVO.getInputId());
		InputVO prog = new InputVO((long)taskVO.getProgNum());
		InputVO task = new InputVO(1l);
		InputVO output = new InputVO(permissionVO.getId());
		
		TargetVO targetVO = new TargetVO(input, prog, task, output);
		
		MessageVO message = new MessageVO(newTaskVO.getId(), targetVO);
		
		ResponseVO responseVO = streamTranscodingAdapter.deleteOutput(message);
		
		if (responseVO.getAck() != 0) {
			throw new HttpRequestErrorException(ToolRequestType.DELETE_OUTPUT.getAction());
		}
	}
}
