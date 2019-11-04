package com.sumavision.tetris.streamTranscoding.addOutput;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskOutputPermissionDAO;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskOutputPermissionService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskOutputPermissionVO;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskQuery;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskVO;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.ToolRequestType;
import com.sumavision.tetris.streamTranscoding.addOutput.requestVO.InputVO;
import com.sumavision.tetris.streamTranscoding.addOutput.requestVO.MessageVO;
import com.sumavision.tetris.streamTranscoding.addOutput.requestVO.TargetVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.BodyVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.OutputVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.TSUDPVO;
import com.sumavision.tetris.streamTranscoding.addTask.responseVO.ResponseVO;
import com.sumavision.tetris.streamTranscoding.api.server.OutParamVO;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddOutputService {
	@Autowired
	private StreamTranscodingAdapter streamTranscodingAdapter;
	
	@Autowired
	private StreamTranscodingTaskQuery streamTranscodingTaskQuery;
	
	@Autowired
	private StreamTranscodingTaskService streamTranscodingTaskService;
	
	@Autowired
	private StreamTranscodingTaskOutputPermissionDAO streamTranscodingTaskOutputPermissionDAO;
	
	@Autowired
	private StreamTranscodingTaskOutputPermissionService streamTranscodingTaskOutputPermissionService;
	
	public void addOutput(UserVO user, Long messageId, List<OutParamVO> outParams) throws Exception {
		
		StreamTranscodingTaskVO taskVO = streamTranscodingTaskQuery.questByMessageId(messageId);
		
		StreamTranscodingTaskVO newTaskVO = streamTranscodingTaskService.addTaskForAddOutput(user, taskVO.getInputId(), taskVO.getProgNum());
		
		List<StreamTranscodingTaskOutputPermissionVO> permissionVOs = streamTranscodingTaskOutputPermissionService.addPermissions(outParams, taskVO.getId());
		
		Long newMessageId = newTaskVO.getId();
		
		InputVO inputVO = new InputVO(newTaskVO.getInputId());
		InputVO progVO = new InputVO((long)newTaskVO.getProgNum());
		InputVO task = new InputVO(1l);
		
		TargetVO targetVO = new TargetVO(inputVO, progVO, task);
		
		List<OutputVO> outputVOs = new ArrayList<OutputVO>();
		
		for (int i = 0; i < outParams.size(); i++) {
			 OutParamVO outParam = outParams.get(i);
			 String[] urlSplit = outParam.getOutputUrl().split(":");
			TSUDPVO udpvo = new TSUDPVO(outParam.getLocalIp(), urlSplit[0], urlSplit[1], outParam.getProgNum(), taskVO.getaCodec(), outParam.getCutoff());
			
			udpvo.setAudlPid(outParam.getAud1pid());
			udpvo.setPcrPid(outParam.getPcrpid());
			udpvo.setPmtPid(outParam.getPmtpid());
			udpvo.setTsidPid(outParam.getTsidpid());
			udpvo.setVidlPid(outParam.getVid1pid());
			udpvo.setVidlType(taskVO.getvCodec());
			
			switch (taskVO.getEsType()) {
			case 0:
				outputVOs.add(new OutputVO(permissionVOs.get(i).getOutputId(), "TS-UDP", udpvo, null, null));
				break;
			case 1: 
				outputVOs.add(new OutputVO(permissionVOs.get(i).getOutputId(), "ES-UDP", null, udpvo, null));
				break;
			default:
				outputVOs.add(new OutputVO(permissionVOs.get(i).getOutputId(), "RTP-UDP", null, null, udpvo));
				break;
			}
		}
		
		BodyVO bodyVO = new BodyVO(null, null, outputVOs);
		
		MessageVO messageVO = new MessageVO(newMessageId, targetVO, bodyVO);
		
		ResponseVO response = streamTranscodingAdapter.addOutput(messageVO);
		
		if (response.getAck() != 0) {
			throw new HttpRequestErrorException(ToolRequestType.ADD_OUTPUT.getAction());
		}
	}
	
	public String newOutputPort(List<String> exceptPort) throws Exception{
		//文件转流再转码的起始端口+2000为录制起始端口
		Long recordStartPort = Long.parseLong(streamTranscodingAdapter.getRecordInfo().get("startPort")) + 2000;
		
		String jsonString = streamTranscodingAdapter.readProfile();
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		String recordIp = jsonObject.getString("recordIp");
		
		List<String> permissionPOs = streamTranscodingTaskOutputPermissionDAO.findByOutputIp(recordIp);
		
		Long returnPort = 0l;
		for (Long i = recordStartPort; i < recordStartPort + 2000; i++){
			String port = i.toString();
			if (permissionPOs.contains(port) || (exceptPort != null && exceptPort.contains(port))) continue;
			return port;
		}
		
		return returnPort.toString();
	}
}
