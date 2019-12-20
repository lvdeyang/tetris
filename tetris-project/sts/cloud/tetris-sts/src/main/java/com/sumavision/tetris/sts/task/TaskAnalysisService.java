package com.sumavision.tetris.sts.task;



import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.*;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.CommonConstants;
import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.CommonUtil;
import com.sumavision.tetris.sts.common.ErrorCodes;
import com.sumavision.tetris.sts.common.NodeIdManageUtil;
import com.sumavision.tetris.sts.common.CommonConstants.NodeStatus;
import com.sumavision.tetris.sts.device.ChannelVideoTypeDao;
import com.sumavision.tetris.sts.device.ChannelVideoTypePO;
import com.sumavision.tetris.sts.device.DeviceChannelAuthDao;
import com.sumavision.tetris.sts.device.DeviceChannelAuthPO;
import com.sumavision.tetris.sts.device.DeviceGroupDao;
import com.sumavision.tetris.sts.device.DeviceGroupPO;
import com.sumavision.tetris.sts.device.DeviceNodeDao;
import com.sumavision.tetris.sts.device.DeviceNodePO;
import com.sumavision.tetris.sts.device.ResolutionBO;
import com.sumavision.tetris.sts.netgroup.NetCardInfoDao;
import com.sumavision.tetris.sts.netgroup.NetCardInfoPO;
import com.sumavision.tetris.sts.task.source.AudioElement;
import com.sumavision.tetris.sts.task.source.AudioParamPO;
import com.sumavision.tetris.sts.task.source.InputPO;
import com.sumavision.tetris.sts.task.source.InputPO.InputType;
import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.source.SourceDao;
import com.sumavision.tetris.sts.task.source.SourcePO;
import com.sumavision.tetris.sts.task.source.SourcePO.SourceType;
import com.sumavision.tetris.sts.task.source.SubtitleElement;
import com.sumavision.tetris.sts.task.source.VideoElement;
import com.sumavision.tetris.sts.task.source.VideoParamPO;
import com.sumavision.tetris.sts.task.tasklink.OutputDao;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;
import com.sumavision.tetris.sts.task.tasklink.OutputPO.OutputType;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkDao;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO.TaskLinkStatus;
import com.sumavision.tetris.sts.task.tasklink.TransTaskPO;

@Component
public class TaskAnalysisService {
	static Logger logger = LogManager.getLogger(TaskAnalysisService.class);
	
	@Autowired
	private SourceDao sourceDao;
	
	@Autowired
	private TaskLinkDao taskLinkDao;
	
	@Autowired
	private NodeIdManageUtil nodeIdManageUtil;
	
	@Autowired
	private DeviceNodeDao deviceNodeDao;
	
	@Autowired
	private DeviceGroupDao deviceGroupDao;
	
	@Autowired
	private ChannelVideoTypeDao channelVideoTypeDao;
	
	@Autowired
	private DeviceChannelAuthDao deviceChannelAuthDao;
	
	@Autowired
	private OutputDao outputDao;
	
	@Autowired
	private NetCardInfoDao netCardInfoDao;
	
	@Autowired
	private NodeUtil nodeUtil;
	
	public JSONObject analysisAddTask(JSONObject jsonObject) throws Exception{
		//服务端参数校验
		//checkTaskObjAtServer(jsonObject);
		JSONObject addTaskJsonObject = null;
		
		Long taskLinkId = jsonObject.getLong("id");
		if (taskLinkId == null || taskLinkId == 0) {
			// 添加任务
			TaskLinkPO taskLinkPO = new TaskLinkPO();
			taskLinkPO.generateFromJson(jsonObject);
			taskLinkPO.setAddTime(new Date());
			taskLinkPO.setPreview(0);
			SourcePO sourcePO = sourceDao.findOne(taskLinkPO.getSourceId());
			if(SourceType.PASSBY.equals(sourcePO.getSourceType())){
				taskLinkPO.setProgramName("ts透传");
				taskLinkPO.setWorkProgramName("ts透传");
				taskLinkPO.setWorkProgramNum(0);
			}else{
				ProgramPO programPO = getProgramPOByNum(taskLinkPO.getProgramNum(), sourcePO.getProgramPOs());
				taskLinkPO.setProgramName(programPO.getProgramName());
				taskLinkPO.setWorkProgramName(programPO.getProgramName());
				taskLinkPO.setWorkProgramNum(programPO.getProgramNum());	
			}
			taskLinkPO.setSourceName(sourcePO.getSourceName());
			taskLinkPO.setWorkSourceName(sourcePO.getSourceName());
			taskLinkPO.setWorkSourceId(sourcePO.getId());

			TaskLinkStatus linkStatus = jsonObject.getObject("status", TaskLinkStatus.class);
			taskLinkPO.setLinkStatus(linkStatus);
			taskLinkDao.save(taskLinkPO);
			if (linkStatus == TaskLinkStatus.RUN) {
				DeviceNodePO deviceNodePO = deviceNodeDao.findOne(taskLinkPO.getCurDeviceNodeId());
		    	ProgramPO programPO = getProgramPOByNum(taskLinkPO.getProgramNum(),sourcePO.getProgramPOs());
				DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(taskLinkPO.getDeviceGroupId());
				
				TaskParamVO taskParamVO = null;
				try {
					taskParamVO = JSONObject.parseObject(taskLinkPO.getTaskParamDetail(), TaskParamVO.class);
				}catch (JSONException e){
					logger.error(e.getMessage(),e.getCause());
					throw new BaseException(StatusCode.FORBIDDEN,ErrorCodes.TASK_PARAM_PARSE_ERROR);
				}
				
				//网关授权通道
				if(deviceNodePO.getEncapsulateAuthPO().getOutputRestNum() < taskParamVO.getOutputs().size()){
		    		logger.error("device encapsulateAuth full, encapsulateAuthId " + deviceNodePO.getEncapsulateAuthPO().getId());
					throw new BaseException(StatusCode.ERROR, ErrorCodes.ENCAPSULATE_AUTH_FULL);
				}
				
				//提取输入参数
				VideoElement videoElement = getVideoElementByPid(taskParamVO.getVideoPid(), programPO);
				String sourceVideoType = transCodec(videoElement.getType());
				ChannelVideoTypePO inChannelVideoTypePO = getChannleTypeByVideoTypeLikeAndWidthAndHeightAndType(sourceVideoType, videoElement.getWidth(), videoElement.getHeight(), "in");
				if (inChannelVideoTypePO == null) {
					//无可用通道
					logger.error("analysisAddTask, inputChannel don't exist; " + videoElement.getType() + "_" + videoElement.getWidth() + "x" + videoElement.getHeight());
					throw new BaseException(StatusCode.ERROR, ErrorCodes.TRANS_AUTH_CHANNEL_FULL);
				}
				
				//输入、输出videoType的id
				Long inputTypeId = inChannelVideoTypePO.getId();
				String outputTypeIds = getOutputTypeIds(taskParamVO.getVideoParams(), sourceVideoType);
				//根据选卡逻辑确定授权通道
				DeviceChannelAuthPO deviceChannelAuthPO = getChannelAuth(taskLinkPO.getnCardIndex(), inputTypeId, outputTypeIds, deviceNodePO);
		    	if(null == deviceChannelAuthPO){
		    		logger.error("device auth full, deviceId " + deviceNodePO.getId() + ", inputType :" + inputTypeId + ", outputTypeIds :" + outputTypeIds);
		    		throw new BaseException(StatusCode.ERROR, ErrorCodes.TRANS_AUTH_CHANNEL_FULL);
		    	}
				
				InputPO inputPO = getJsonInputPO(deviceNodePO.getId(), sourcePO);
				ProgramPO inputProgramPO = getJsonProgramPO(programPO, inputPO.getNodeId(), deviceChannelAuthPO.getCardNumber(), taskLinkPO);
				TransTaskPO transTaskPO = getJsonTransTaskPO(taskLinkPO, taskParamVO, inputPO, programPO,inputProgramPO, null, deviceChannelAuthPO);
				//output
				List<OutputPO> outputPOs = new ArrayList<OutputPO>();
				for(OutputBO outputBO : taskParamVO.getOutputs()){
					//检查本输出是否和之前输出存在重复
					if(outputBO.getType().equals(ProtoType.TSUDP) || outputBO.getType().equals(ProtoType.TSRTP)){
						if(outputDao.findTopByIpAndPortAndNetGroupId(outputBO.getIp(), outputBO.getPort(), outputBO.getNetGroupId()) != null){
							logger.error("analysisContainTrans error, output repeat :" + outputBO.getIp() + ":" + outputBO.getPort());
							throw new BaseException(StatusCode.ERROR, ErrorCodes.TASK_OUTPUT_REPEAT);
						}
					} else {
						if(outputDao.findTopByIpAndPortAndTypeAndPubName(outputBO.getIp(), outputBO.getPort(), outputBO.getType(),outputBO.getPubName()) != null){
							logger.error("analysisContainTrans error, output repeat :" + outputBO.getIp() + ":" + outputBO.getPort() + ", pubName :" + outputBO.getPubName());
							throw new BaseException(StatusCode.ERROR, ErrorCodes.TASK_OUTPUT_REPEAT);
						}
					}
					
					if(null != outputBO.getNetGroupId()){
						try {
							NetCardInfoPO netCard = netCardInfoDao.findTopByDeviceIdAndNetGroupIdAndStatus(deviceNodePO.getId(), outputBO.getNetGroupId(),NetCardInfoPO.LINK_STATUS_NORMAL);
							if(null != netCard){
								OutputPO outputPO = getJsonOutputPO(inputPO, programPO, outputBO,transTaskPO.getNodeId(), netCard.getIpv4());
								outputPOs.add(outputPO);
								continue;
							}
						} catch (Exception e) {
							//转码直接输出报错的话，再走后面流程尝试网关输出
							throw new BaseException(StatusCode.ERROR, ErrorCodes.CREATE_TASK_ERROR);
						}
					}
				}
				
				addTaskJsonObject = new JSONObject();
				addTaskJsonObject.put("task_name", taskLinkPO.getLinkName());
				addTaskJsonObject.put("device_ip", deviceNodePO.getDeviceIp());
				addTaskJsonObject.put("task_type", taskLinkPO.getTaskType());
				addTaskJsonObject.put("task_id", taskLinkPO.getId());
				addTaskJsonObject.put("input_array", nodeUtil.getJsonInputNode(inputPO,programPO).getJSONArray("input_array"));
				addTaskJsonObject.put("task_array", nodeUtil.getJsonTaskNode(transTaskPO).getJSONArray("task_array"));
				addTaskJsonObject.put("output_array", nodeUtil.getJsonOutputNode(outputPOs, inputProgramPO, nodeUtil.getOutputMediaEncodeMessage()).getJSONArray("output_array"));
				
				
//				CreateAddTaskNode createAddTaskNode = new CreateAddTaskNode();
//				createAddTaskNode.setDevice_ip(deviceNodePO.getDeviceIp());
//				createAddTaskNode.setTask_id(taskLinkId);
//				createAddTaskNode.setTask_name(taskLinkPO.getLinkName());
//				createAddTaskNode.setTask_type(taskLinkPO.getTaskType());
//				createAddTaskNode.setInput_array(nodeUtil.getJsonInputNode(inputPO,programPO).getString("input_array"));
//				nodeUtil.getJsonInputNode(inputPO);
//				nodeUtil.getJsonProgramNode(inputProgramPO);
//				nodeUtil.getJsonTaskNode(transTaskPO);
//				nodeUtil.getJsonOutputNode(outputPOs, inputProgramPO, nodeUtil.getOutputMediaEncodeMessage());
			}
		}
		return  addTaskJsonObject;
	}
	
	
	public OutputPO getJsonOutputPO(InputPO inputPO, ProgramPO inputProgramPO, OutputBO outputBO, Long transTaskNodeId, String transOutLocalIp) throws BaseException{
		OutputPO transOutputPO = new OutputPO();
		transOutputPO.setNodeId(nodeIdManageUtil.getNewNodeId());
		transOutputPO.setOutputType(OutputType.NEWABILITY);
		transOutputPO.setInputId(inputPO.getNodeId());
		transOutputPO.setProgramId((null==inputProgramPO.getNodeId()) ? inputProgramPO.getProgramNum() : inputProgramPO.getNodeId().intValue());
		if (null != transTaskNodeId) {
			transOutputPO.setTaskId(transTaskNodeId);
		}
		transOutputPO.setDeviceId(inputPO.getDeviceId());
		transOutputPO.setPubName(outputBO.getPubName());
		transOutputPO.setLocalIp(transOutLocalIp);
		transOutputPO.setNetGroupId(outputBO.getNetGroupId());
		transOutputPO.setIp(outputBO.getIp());
		transOutputPO.setPort(outputBO.getPort());
		transOutputPO.setStreamMediaId(outputBO.getStreamMediaId());
		transOutputPO.setType(outputBO.getType());
		transOutputPO.setJsonParam(outputBO.generateJsonOutParam().toJSONString());
		return transOutputPO;
	}
	
	public TransTaskPO getJsonTransTaskPO(TaskLinkPO taskLinkPO,TaskParamVO taskParamVO,InputPO transInputPO,ProgramPO programPO,ProgramPO transInputProgramPO,OutputPO inpubOutputPO,DeviceChannelAuthPO availDeviceChannelAuthPO){
		TransTaskPO transTaskPO = new TransTaskPO();
		transTaskPO.setLinkId(taskLinkPO.getId());
		transTaskPO.setNodeId(nodeIdManageUtil.getNewNodeId());
		transTaskPO.setDeviceId(transInputPO.getDeviceId());
		transTaskPO.setInputId(transInputPO.getNodeId());
		transTaskPO.setSourceId(taskLinkPO.getSourceId());
		transTaskPO.setProgramNum(taskLinkPO.getProgramNum());
		transTaskPO.setTaskType(taskLinkPO.getTaskType());
		transTaskPO.setNodeStatus(NodeStatus.NORMAL);
		transTaskPO.setCardNumber(availDeviceChannelAuthPO.getCardNumber());
		transTaskPO.setDeviceChannelId(availDeviceChannelAuthPO.getId());
		transTaskPO.setProgramNodeId(transInputProgramPO.getNodeId());
		
		for(VideoParamBO videoParamBO : taskParamVO.getVideoParams()){
			VideoParamPO videoParamPO = videoParamBO.transToVideoParamPO();
			transTaskPO.getVideoParams().add(videoParamPO);
		}
		for(AudioParamBO audioParamBO : taskParamVO.getAudioParams()){
			AudioParamPO audioParamPO = audioParamBO.transToAudioParamPO();
			transTaskPO.getAudioParams().add(audioParamPO);
		}
		
		return transTaskPO;
	}
	
	//根据选卡逻辑确定授权通道
	private DeviceChannelAuthPO getChannelAuth(Integer nCardIndex, Long inputTypeId, String outputTypeIds,DeviceNodePO transDevice) throws BaseException {
		List<DeviceChannelAuthPO> deviceChannelAuthPOS = new ArrayList<>();
		DeviceChannelAuthPO choosedChannel = null;
		if(nCardIndex < 0){
//			//自动选卡
//	    	List<Object> usedChannelTypes = deviceChannelAuthDao.getUsedChannelTypes(transDevice.getId());
//	    	if(usedChannelTypes.size() > 1){
//	    		//通道混加占用情况，尝试从小工具上报信息中获得最优卡
//	    		List<Integer> cardNumbers = deviceMonitorService.queryBestCardNums(transDevice.getDeviceIp());
//	    		if(null != cardNumbers && !cardNumbers.isEmpty()){
//	    			//遍历卡，找第一个通道可以满足条件的卡
//	    			for (Integer cardNumber : cardNumbers) {
//	    				deviceChannelAuthPOS = deviceChannelAuthDao.findByDeviceIdAndCardNumberAndInputTypeAndOutputTypeAndAuthNumRestNot(
//	    	    				transDevice.getId(), cardNumber, inputTypeId, outputTypeIds, 0);
//	    				if(null != deviceChannelAuthPOS && !deviceChannelAuthPOS.isEmpty()){
//	    					choosedChannel = deviceChannelAuthPOS.get(0);
//	    					break;
//	    				}
//					}
//	    		} 
//	    	}
	    	//当设备没有混加通道，或有混加通道但是从小工具上报信息接口中无法找到有效卡号，使用普通方法找最优卡
	    	if(null == deviceChannelAuthPOS || deviceChannelAuthPOS.isEmpty()){
				choosedChannel = deviceChannelAuthDao.findRestBestChannel(transDevice.getId(), inputTypeId, outputTypeIds);
	    	}
		}else{
			//手动选卡
			deviceChannelAuthPOS = deviceChannelAuthDao.findByDeviceIdAndCardNumberAndInputTypeAndOutputTypeAndAuthNumRestNot(
    				transDevice.getId(), nCardIndex, inputTypeId, outputTypeIds, 0);//get(0)有漏洞会数组越界
			if(deviceChannelAuthPOS.isEmpty()){
				logger.error("device auth full, deviceId " + transDevice.getId() + ", inputType :" + inputTypeId + "outputTypeIds :" + outputTypeIds);
				throw new BaseException(StatusCode.ERROR, ErrorCodes.TRANS_AUTH_CHANNEL_FULL);
			}else{
				choosedChannel = deviceChannelAuthPOS.get(0);
			}
		}

		return choosedChannel;
	}

	private String getOutputTypeIds(Set<VideoParamBO> videoParamBOs, String inputVideoType) throws BaseException {
		List<Long> outputTypeList = new ArrayList<Long>();
		for (VideoParamBO videoParamBO : videoParamBOs) {
			ChannelVideoTypePO outChannelVideoTypePO;
			if (videoParamBO.getCodec().equals("passby")) {
				outChannelVideoTypePO = getChannleTypeByVideoTypeLikeAndWidthAndHeightAndType(inputVideoType,videoParamBO.getWidth(), videoParamBO.getHeight(),"out");
			}else {
				outChannelVideoTypePO = getChannleTypeByVideoTypeLikeAndWidthAndHeightAndType(videoParamBO.getCodec(),videoParamBO.getWidth(), videoParamBO.getHeight(),"out");
			}
			
			if (outChannelVideoTypePO != null) {
				outputTypeList.add(outChannelVideoTypePO.getId());
			}else {
				//输出视频类型没有找到，相当于无法找到相应的授权通道
				logger.error("analysisAddTask, outputChannel don't exist; " + videoParamBO.getCodec() + "_" + videoParamBO.getWidth() + "x" + videoParamBO.getHeight());
				throw new BaseException(StatusCode.ERROR, ErrorCodes.CHANNEL_NOT_FOUND);
			}
		}
		Collections.sort(outputTypeList);
		return CommonUtil.anylistToString(outputTypeList);
	}
	
	
	private ChannelVideoTypePO getChannleTypeByVideoTypeLikeAndWidthAndHeightAndType(String videoType , Integer width, Integer height, String type){
    	if(type.equals("in")){
    		if(videoType.contains("avs")){
    			videoType = "avs";
    		}else if(!videoType.equals("yuv")){
    			videoType = "non-avs";
    		}
    	}else if(videoType.contains("avs")){
    		videoType = "avs";
    	}

        ResolutionBO taskResolutionBO = new ResolutionBO(width,height);
        return channelVideoTypeDao.findTopByVideoTypeAndWidthAndHeightAndType(videoType,taskResolutionBO.getWidth(),taskResolutionBO.getHeight(),type);
    }
	
	
	/**
     * 转换编码格式，统一名称，并按h254、h265、mpeg2、avs顺序排序
     * @param str
     * @return
     */
    private String transCodec(String str){
        StringBuilder stringBuilder = new StringBuilder();
        
        if(str.equals(CommonConstants.ENCODE_TYPE_NON_AVS)){
            stringBuilder.append(CommonConstants.ENCODE_TYPE_NON_AVS);
        }
        
        if(str.contains(CommonConstants.ENCODE_TYPE_H264)){
            stringBuilder.append(CommonConstants.ENCODE_TYPE_H264);
        }

        if(str.contains("hevc") || str.contains(CommonConstants.ENCODE_TYPE_H265)){
            if(stringBuilder.length() > 0){
                stringBuilder.append(",");
            }
            stringBuilder.append(CommonConstants.ENCODE_TYPE_H265);
        }
        
        if(str.contains(CommonConstants.ENCODE_TYPE_MPEG2)){
            if(stringBuilder.length() > 0){
                stringBuilder.append(",");
            }
            stringBuilder.append(CommonConstants.ENCODE_TYPE_MPEG2);
        }

        if(str.contains(CommonConstants.ENCODE_TYPE_AVS) && !str.equals(CommonConstants.ENCODE_TYPE_NON_AVS)){
        	if(stringBuilder.length() > 0){
                stringBuilder.append(",");
            }
            stringBuilder.append(CommonConstants.ENCODE_TYPE_AVS);
        }

        //sdi
        if(str.contains("yuv") || str.contains("YUV")){
            if(stringBuilder.length() > 0){
                stringBuilder.append(",");
            }
            stringBuilder.append("yuv");
        }
        
        return stringBuilder.toString();
    }
	
	/**
     * 根据videoPid查找响应video参数
     * @param videoPid
     * @param programPO
     * @return
     * @throws BaseException
     */
    public static VideoElement getVideoElementByPid(Integer videoPid, ProgramPO programPO) throws BaseException{
    	for(VideoElement videoElement :programPO.getVideoElements()){
    		if(videoElement.getPid().equals(videoPid)){
    			return videoElement;
    		}
    	}
    	throw new BaseException(StatusCode.ERROR, ErrorCodes.TASK_VIDEO_PARAM_NOT_FOUND);
    }
    
	private ProgramPO getProgramPOByNum(Integer programNum,List<ProgramPO> programPOs){
    	for(ProgramPO programPO : programPOs){
    		if(programPO.getProgramNum().intValue() == programNum){
    			return programPO;
    		}
    	}
    	return null;
    }
	
	private InputPO getJsonInputPO(Long deviceNodeId,SourcePO sourcePO) throws Exception{
//		String localIp = deviceDaoService.findDataLocalIp(deviceNodeId, sourcePO.getNetGroupId());
//		if(localIp == null){
//			logger.error("analysisAddTask getJsonInputPO error, device:" + deviceNodeId+" data localip null");
//			throw new BaseException(StatusCode.ERROR, ErrorCodes.DEVICE_NODE_NOT_FOUND);
//		}
		
		InputPO inputPO = new InputPO();
		inputPO.setInputType(InputType.NEWABILITY);
		inputPO.setNodeId(nodeIdManageUtil.getNewNodeId());
		inputPO.setType(CommonConstants.INPUT_TYPE_STREAM);
		
		//inputPO.setLocalIp(localIp);
		//暂时写死
		inputPO.setLocalIp(sourcePO.getSourceIp());
		
		inputPO.setDeviceId(deviceNodeId);
		inputPO.setSourceIp(sourcePO.getSourceIp());
		inputPO.setPort(sourcePO.getSourcePort());
		inputPO.setSourceId(sourcePO.getId());
		inputPO.setNodeStatus(NodeStatus.NORMAL);
		inputPO.setUrlType(sourcePO.getProtoType());
		inputPO.setUrl(sourcePO.getSourceUrl());
		
		inputPO.setIsIgmpv3(sourcePO.getIsIgmpv3());
		inputPO.setFilterMode(sourcePO.getFilterMode());
		inputPO.setFilterIpSegmentsJson(sourcePO.getFilterIpSegmentsJson());
		inputPO.setFilterIpSegments(sourcePO.getFilterIpSegments());
		inputPO.setGenPts(sourcePO.getGenPts());
		inputPO.setModeSelect(sourcePO.getModeSelect());
		inputPO.setLatency(sourcePO.getLatency());
		return inputPO;
	}
	
	private ProgramPO getJsonProgramPO(ProgramPO programPO,Long inputId, Integer cardNum, TaskLinkPO taskLinkPO){
		ProgramPO jsonProgramPO = new ProgramPO();
		jsonProgramPO.setInputId(inputId);
		jsonProgramPO.setNodeId(nodeIdManageUtil.getNewNodeId());
		jsonProgramPO.setCardNum(cardNum);
		jsonProgramPO.setProgramName(programPO.getProgramName());
		jsonProgramPO.setProgramProvider(programPO.getProgramProvider());
		jsonProgramPO.setProgramNum(programPO.getProgramNum());
		jsonProgramPO.setPcrPid(programPO.getPcrPid());
		jsonProgramPO.setPmtPid(programPO.getPmtPid());
		jsonProgramPO.setDeinterlaceMode(programPO.getDeinterlaceMode());
		
		//现在先写成统一的decode_mode为cpu，后续该参数应该从taskLinkPO页面进行获取,pid也应该修改为从输出那获取
		if (null != programPO.getAudioElements()) {
			List<AudioElement> audioElements = programPO.getAudioElements();
			for (AudioElement audioElement : audioElements) {
				audioElement.setDecode_mode(CommonConstants.DECODE_MODE_CPU);
			}
			jsonProgramPO.setAudioElements(audioElements);
			jsonProgramPO.setAudioJson(JSONObject.toJSONString(audioElements));
		}
		
		if (null != programPO.getVideoElements()) {
			List<VideoElement> videoElements = programPO.getVideoElements();
			for (VideoElement videoElement : videoElements) {
				videoElement.setDecode_mode(null==taskLinkPO.getProgramDecodeType()?CommonConstants.DECODE_MODE_CPU:taskLinkPO.getProgramDecodeType());
			}
			jsonProgramPO.setVideoElements(videoElements);
			jsonProgramPO.setVideoJson(JSONObject.toJSONString(videoElements));
		}
		
		if (null != programPO.getSubtitleElements()) {
			List<SubtitleElement> subtitleElements = programPO.getSubtitleElements();
			for (SubtitleElement subtitleElement : subtitleElements) {
				subtitleElement.setDecode_mode(CommonConstants.DECODE_MODE_CPU);
			}
			jsonProgramPO.setSubtitleElements(subtitleElements);
			jsonProgramPO.setSubtitleJson(JSONObject.toJSONString(subtitleElements));
		}
		
		jsonProgramPO.setBackupMode(programPO.getBackupMode());

//		List<VideoElement> videoElements = new ArrayList<VideoElement>();
//		String[] videoPids = programPO.getVidPids().split(",");
//		for(int index = 0; index < videoPids.length; index++){
//			//只根据输出更新pid，其余信息从节目中获取
//			VideoElement videoElement = programPO.getVideoElements().get(index).clone();
//			videoElement.setPid(Integer.parseInt(videoPids[index]));
//			videoElements.add(videoElement);
//		}
//		jsonProgramPO.setVideoElements(videoElements);
//		//audio信息
//		List<AudioElement> audioElements = new ArrayList<AudioElement>();
//		String[] audioPids = tsUdpBO.getAudPids().split(",");
//		for(int index = 0; index < audioPids.length; index++){
//			//只根据输出更新pid，其余信息从节目中获取
//			AudioElement audioElement = programPO.getAudioElements().get(index).clone();
//			audioElement.setPid(Integer.parseInt(audioPids[index]));
//			audioElements.add(audioElement);
//		}
//		jsonProgramPO.setAudioElements(audioElements);
//		
		return jsonProgramPO;
	}
	
	
	
//	public void checkTaskObjAtServer(JSONObject jsonObject) throws CommonException {
//		Integer taskType = APIUtil.getDocParam(jsonObject,"taskType",Integer.class);
//		if (taskType<0 || taskType>1){
//			throw new CommonException("taskType error");
//		}
//		Long deviceNode = APIUtil.getDocParam(jsonObject,"deviceNode",Long.class);
//		DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceNode);
//		if (deviceNodePO == null) {
//			throw new CommonException("device node not exists");
//		}
//		Integer nCardIndex = APIUtil.getDocParam(jsonObject,"nCardIndex",Integer.class);
//		if (nCardIndex<-1 || nCardIndex >= deviceNodePO.getnCardNum()){
//			throw new CommonException("nCardIndex should greater than -1 and less than the card count of device");
//		}
//		JSONObject taskParam = APIUtil.getDocParam(jsonObject,"taskParam",JSONObject.class);
//		JSONArray outputs = APIUtil.getDocParam(taskParam,"outputs",JSONArray.class);
//		Iterator outIter=outputs.iterator();
//		while (outIter.hasNext()){
//			JSONObject output = (JSONObject) outIter.next();
//			if (output.containsKey("ip")){
//				String ip = output.getString("ip");
//				Pattern pattern = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
//				Matcher matcher = pattern.matcher(ip);
//				if (!matcher.matches()) {
//                    logger.error(MessageFormat.format("some output ip format {0} error",ip));
//					throw new CommonException(MessageFormat.format("some output ip format {0} error",ip));
//				}
//			}
//			if (output.containsKey("port")){
//				Integer port = output.getInteger("port");
//				if (port > 65535 || port < 100){
//                    logger.error(MessageFormat.format("some output port {0} error",port));
//					throw new CommonException(MessageFormat.format("some output port {0} error",port));
//				}
//			}
//			if (output.containsKey("type")){
//			    try {
//			        output.getObject("type", CommonConstants.ProtoType.class);
//                }catch (JSONException e){
//			        logger.error(e.getMessage(),e.getCause());
//			        throw new CommonException("some output type error");
//                }
//            }
//		}
//
//	}

}
