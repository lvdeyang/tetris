package com.sumavision.tetris.streamTranscoding.addTask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskOutputPermissionService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskOutputPermissionVO;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskVO;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.ToolRequestType;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.AudioVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.BodyVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.InputVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.MessageVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.OutputVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.TSUDPVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.TaskVO;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.VideoVO;
import com.sumavision.tetris.streamTranscoding.addTask.responseVO.ResponseVO;
import com.sumavision.tetris.streamTranscoding.api.server.CodecParamVO;
import com.sumavision.tetris.streamTranscoding.api.server.OutParamVO;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestParamErrorException;
import com.sumavision.tetris.streamTranscodingProcessVO.RecordVO;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingVO;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamTranscodingAddTaskService {
	
	@Autowired
	private StreamTranscodingAdapter streamTranscodingAdapter;
	
	@Autowired
	private StreamTranscodingTaskService streamTranscodingTaskService;
	
	@Autowired
	private StreamTranscodingTaskOutputPermissionService streamTranscodingTaskOutputPermissionService;
	
	public String addStreamTask(UserVO user, StreamTranscodingVO streamTranscodingVO, RecordVO recordVO) throws Exception{
		String assetPath = streamTranscodingVO.getAssetUrl();
		String mediaType = streamTranscodingVO.getMediaType();
		Integer progNum = streamTranscodingVO.getProgNum();
		boolean needRecord = streamTranscodingVO.isNeedRecordOutput();
		com.sumavision.tetris.streamTranscoding.api.server.TaskVO task = streamTranscodingVO.getTaskVO();
		if (task == null) return null;
		Integer esType = task.getEsType();
		CodecParamVO codecParam = task.getCodecParam();
		List<OutParamVO> outParams = task.getOutParam();
		
		boolean record = recordVO == null ? false : recordVO.isRecord();
		String recordCallback = recordVO == null ? null : recordVO.getRecordCallback();
		
		StreamTranscodingTaskVO taskPO = streamTranscodingTaskService.addTaskForAddInput(user, record, recordCallback, progNum, esType, codecParam.getVcodec(), codecParam.getAcodec(), null);
		List<StreamTranscodingTaskOutputPermissionVO> permissionVOs = streamTranscodingTaskOutputPermissionService.addPermissions(outParams, taskPO.getId());
		
		String messageId = taskPO.getId().toString();
		Long inputId = taskPO.getInputId();
		Long taskId = 1l;
		
		String url = null;
		if (assetPath == null || assetPath.isEmpty()) return null;
		String[] splits = assetPath.split(":");
		if (splits.length < 2) throw new HttpRequestParamErrorException(assetPath);
		switch (streamTranscodingVO.getBePCM()) {
		case 1:
			url = "udp-pcm:" + splits[1];
			break;
		case 2:
			url = "udp-adpcm:" + splits[1];
		default:
			url = assetPath;
			break;
		}
		
		InputVO inputVO = new InputVO(inputId, url, 0);
		
		VideoVO videoVO = null;
		if (mediaType.equals("video")) {
			videoVO = new VideoVO(codecParam.getVcodec(), codecParam.getVbitrate(), codecParam.getVresolution(), codecParam.getVratio(), null);
		}
		AudioVO audioVO = new AudioVO(codecParam.getAcodec(), codecParam.getAsample(), codecParam.getAbitrate(), codecParam.getChLayout());
		TaskVO taskVO = new TaskVO(taskId, audioVO, videoVO);
		
		if (outParams == null || outParams.isEmpty()) return null;
		List<OutputVO> outputVOs = new ArrayList<OutputVO>();
		for (int i = 0; i < outParams.size(); i++) {
			OutParamVO outParam = outParams.get(i);
			String[] urlSplit = outParam.getOutputUrl().split(":");
			TSUDPVO udpvo = new TSUDPVO(outParam.getLocalIp(), urlSplit[0], urlSplit[1], outParam.getProgNum(), codecParam.getAcodec(), outParam.getCutoff());
			
			udpvo.setAudlPid(outParam.getAud1pid());
			udpvo.setPcrPid(outParam.getPcrpid());
			udpvo.setPmtPid(outParam.getPmtpid());
			udpvo.setTsidPid(outParam.getTsidpid());
			if (mediaType.equals("video")) {
				udpvo.setVidlPid(outParam.getVid1pid());
				udpvo.setVidlType(codecParam.getVcodec());
			}
			
			switch (esType) {
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
		
		//添加收录的本地输出
		if (needRecord) {
			Long port = Long.parseLong(recordVO.getAssetPort());
			addLocalOutput(user, recordVO.getAssetIp(), port, inputVO, taskVO, outParams.get(0));
		}
		
		BodyVO bodyVO = new BodyVO(inputVO, new ArrayListWrapper<TaskVO>().add(taskVO).getList(), outputVOs);
		
		MessageVO messageVO = new MessageVO(Long.valueOf(messageId), bodyVO);
		
		ResponseVO response = streamTranscodingAdapter.addTask(messageVO);
		
		if (response != null && response.getAck() == 0) {
			return messageId;
		} else {
			throw new HttpRequestErrorException(ToolRequestType.ADD_TASK.getAction());
		}
	}
	
	//添加任务，输出流到本地(当录制参数为true时使用)
	public void addLocalOutput(UserVO user, String ip, Long port, InputVO inputVO, TaskVO taskVO, OutParamVO outParamVO) throws Exception{
		StreamTranscodingTaskVO taskPO = streamTranscodingTaskService.addTaskForAddInput(user, false, "", 0, 0, taskVO.getVideo() != null ? taskVO.getAudio().getCodec() : null, taskVO.getAudio().getCodec(), null);
		
		List<StreamTranscodingTaskOutputPermissionVO> permissionVOs = streamTranscodingTaskOutputPermissionService.addPermissions(new ArrayListWrapper<OutParamVO>().add(outParamVO).getList(), taskPO.getId());
		
		Long messageId = taskPO.getId();
		Long inputId = taskPO.getInputId();
		Long taskId = 1l;
		
		InputVO input = inputVO.copy();
		input.setId(inputId);
		
		TaskVO task = taskVO.copy();
		task.setId(taskId);
		
		TSUDPVO udpvo = new TSUDPVO(outParamVO.getLocalIp(), ip, port.toString(), outParamVO.getProgNum(), task.getAudio().getCodec(), outParamVO.getCutoff());
		List<OutputVO> output = new ArrayList<OutputVO>();
		output.add(new OutputVO(permissionVOs.get(0).getOutputId(), "TS-UDP", udpvo, null, null));
		
		BodyVO bodyVO = new BodyVO(input, new ArrayListWrapper<TaskVO>().add(task).getList(), output);
		MessageVO messageVO = new MessageVO(messageId, bodyVO);
		streamTranscodingAdapter.addTask(messageVO);
	}
}
