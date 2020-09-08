package com.sumavision.tetris.business.transcode.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.transcode.bo.CheckInputBO;
import com.sumavision.tetris.business.transcode.vo.*;
import com.sumavision.tetris.capacity.bo.input.*;
import com.sumavision.tetris.capacity.bo.output.*;
import com.sumavision.tetris.capacity.bo.request.*;
import com.sumavision.tetris.capacity.bo.response.*;
import com.sumavision.tetris.capacity.bo.task.*;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.constant.EncodeConstant;
import com.sumavision.tetris.capacity.enumeration.InputResponseEnum;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.capacity.service.ResponseService;
import com.sumavision.tetris.capacity.template.TemplateService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流转码<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月12日 下午1:43:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TranscodeTaskService {

	private static final Logger LOG = LoggerFactory.getLogger(TranscodeTaskService.class);

	public static final String BACK_UP = "backup";
	
	public static final String COVER = "cover";
	
	@Autowired
	private TaskInputDAO taskInputDao;
	
	@Autowired
	private TaskOutputDAO taskOutputDao;
	
	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private ResponseService responseService;
	
	@Autowired
	private CapacityProps capacityProps;

	@Autowired
	private TemplateService templateService;

	/**
	 * 刷源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午5:05:52
	 * @param AnalysisInputVO analysisInput 刷源信息
	 * @return String input转String
	 */
	public String analysisInput(AnalysisInputVO analysisInput) throws Exception{
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		
		InputBO inputBO = analysisInput.getInput();
		
		inputBO.setId(uuid);
		
		String capacityIp = analysisInput.getDevice_ip();
		
		String uniq = "";
		if(inputBO.getUdp_ts() != null){
			uniq = new StringBuffer().append(inputBO.getUdp_ts().getSource_ip())
									 .append("%")
									 .append(inputBO.getUdp_ts().getSource_port())
									 .append("%")
									 .append(inputBO.getUdp_ts().getLocal_ip())
									 .toString();
		}
		if(inputBO.getRtp_ts() != null){
			uniq = new StringBuffer().append(inputBO.getRtp_ts().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getRtp_ts().getSource_port())
					 				 .append("%")
									 .append(inputBO.getRtp_ts().getLocal_ip())
					 				 .toString();
		}
		if(inputBO.getHttp_ts() != null){
			uniq = inputBO.getHttp_ts().getUrl();
		}
		if(inputBO.getSrt_ts() != null){
			uniq = new StringBuffer().append(inputBO.getSrt_ts().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getSrt_ts().getSource_port())
					 				 .append("%")
					 				 .toString();
		}
		if(inputBO.getHls() != null){
			uniq = inputBO.getHls().getUrl();
		}
		if(inputBO.getDash() != null){
			uniq = inputBO.getDash().getUrl();
		}
		if(inputBO.getMss() != null){
			uniq = inputBO.getMss().getUrl();
		}
		if(inputBO.getRtsp() != null){
			uniq = inputBO.getRtsp().getUrl();
		}
		if(inputBO.getRtmp() != null){
			uniq = inputBO.getRtmp().getUrl();
		}
		if(inputBO.getHttp_flv() != null){
			uniq = inputBO.getHttp_flv().getUrl();
		}
		if(inputBO.getSdi() != null){
			uniq = new StringBuffer().append(inputBO.getSdi().getCard_no())
	 				 				 .append("%")
					 				 .append(inputBO.getSdi().getCard_port())
					 				 .toString();
		}
		if(inputBO.getRtp_es() != null){
			uniq = new StringBuffer().append("%")
									 .append(inputBO.getRtp_es().getLocal_port())
									 .append("%")
									 .toString();
		}
		if(inputBO.getFile() != null){
			uniq = uuid;
		}
		if(inputBO.getUdp_pcm() != null){
			uniq = new StringBuffer().append(inputBO.getUdp_pcm().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getUdp_pcm().getSource_port())
					 				 .toString();
		}
		
		TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
		if(inputPO == null || inputPO.getCount().equals(0)){
			
			AllRequest allRequest = new AllRequest();
			allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());
			
			//添源
			AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
			responseService.allResponseProcess(allResponse);
			
			//刷源
			AnalysisResponse response = capacityService.getAnalysis(uuid, capacityIp);
			
			//删源
			capacityService.deleteAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
			
			return JSON.toJSONString(response.getInput());
			
		}else{
			
			String inputId = JSONObject.parseObject(inputPO.getInput(), InputBO.class).getId();
			
			//刷源
			AnalysisResponse response = capacityService.getAnalysis(inputId, capacityIp);
			
			return JSON.toJSONString(response.getInput());
		}
		
	}


	public void previewInput(CreateInputPreviewVO createInputPreviewVO) throws Exception {

		InputBO inputBO = createInputPreviewVO.getInput_array().get(0);

		if (createInputPreviewVO.getProgram_number() == null){
			throw new BaseException(StatusCode.ERROR,"param[program number] not exist");
		}

		String pubName = inputBO.getId();
		String playName = inputBO.getId();
		String taskId = inputBO.getId();

		String uniq = generateUniq(inputBO);
		TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
		if (inputPO!=null && inputPO.getCount()>0){ //输入存在的话就将现有输入进行替换下，以免input_id不一致
			inputBO = JSONObject.parseObject(inputPO.getInput(),InputBO.class);
			createInputPreviewVO.getInput_array().remove(0);
			createInputPreviewVO.getInput_array().add(inputBO);
		}
		ProgramBO taskProgramBO = inputBO.getProgram_array().stream()
				.filter(p->createInputPreviewVO.getProgram_number().equals(p.getProgram_number()))
				.findFirst()
				.get();
		if (taskProgramBO == null) {
			throw new BaseException(StatusCode.ERROR,"cannot find program, program number: "+createInputPreviewVO.getProgram_number());
		}

		taskProgramBO.getAudio_array().get(0).setAudio_column("on");

		List<TaskBO> taskBOs = trans2TaskBO(taskId, inputBO, createInputPreviewVO.getProgram_number());

//		List<OutputBO> outputBOs = trans2HLSOutputBO(taskId, taskBOs, pubName, playName);
		String outputUrl = new StringBuilder().append("rtmp://")
				.append(createInputPreviewVO.getDevice_ip())
				.append(":1935")
				.append("/")
				.append("live")
				.append("/")
				.append(playName)
				.toString();
		List<OutputBO> outputBOs = trans2RTMPOutputBO(taskId, taskBOs, outputUrl);

		save(taskId, createInputPreviewVO.getDevice_ip(), createInputPreviewVO.getInput_array()  , taskBOs, outputBOs, BusinessType.TRANSCODE);


	}

	public List<TaskBO> trans2TaskBO(
			String taskId,
			InputBO input,
			Integer programNo) throws Exception {

		String videoTaskId = new StringBufferWrapper().append("task-preview-video-")
				.append(taskId)
				.toString();
		String audioTaskId = new StringBufferWrapper().append("task-preview-audio-")
				.append(taskId)
				.toString();
		String encodeVideoId = new StringBufferWrapper().append("encode-video-")
				.append(taskId)
				.toString();
		String encodeAudioId = new StringBufferWrapper().append("encode-audio-")
				.append(taskId)
				.toString();

		List<TaskBO> tasks = new ArrayList<TaskBO>();

		/*******
		 * 视频 *
		 *******/

		ProgramBO taskProgramBO = input.getProgram_array().stream().filter(p->programNo.equals(p.getProgram_number()))
				 .findFirst()
				 .get();
		if (taskProgramBO == null){
			throw new BaseException(StatusCode.ERROR,"cannot find program, program number: "+programNo);
		}
		//视频转码,,,
		TaskSourceBO videoSource = new TaskSourceBO().setInput_id(input.getId())
				.setProgram_number(programNo)
				.setElement_pid(taskProgramBO.getVideo_array().get(0).getPid());

		TaskBO videoTask = new TaskBO().setId(videoTaskId)
				.setType("video")
				.setRaw_source(videoSource)
				.setEncode_array(new ArrayList<EncodeBO>());

		EncodeBO videoEncode = new EncodeBO().setEncode_id(encodeVideoId)
				.setProcess_array(new ArrayList<PreProcessingBO>());

		//scale
		ScaleBO scale = new ScaleBO().setPlat("cpu")
				.setWidth(480)
				.setHeight(272);

		//音柱
		AudioColumnBO audioColumnBO = new AudioColumnBO().setPlayflag("on").setPlat("cpu");

		PreProcessingBO scaleProcessing = new PreProcessingBO().setScale(scale);
		PreProcessingBO audioColumnProcessing = new PreProcessingBO().setAudiocolumn(audioColumnBO);
		videoEncode.getProcess_array().add(scaleProcessing);
		videoEncode.getProcess_array().add(audioColumnProcessing);

		String params = templateService.getVideoEncodeMap(EncodeConstant.TplVideoEncoder.VENCODER_X264);
		JSONObject obj = JSONObject.parseObject(params);
		obj.put("bitrate",1500);
		obj.put("max_bitrate",2000);
		obj.put("rc_mode","cbr");
		obj.put("ratio","4:3");
		obj.put("resolution","480x272");

		videoEncode.setH264(obj);

		videoTask.getEncode_array().add(videoEncode);

		tasks.add(videoTask);

		/*******
		 * 音频 *
		 *******/
		//音频转码
		TaskSourceBO audioSource = new TaskSourceBO().setInput_id(input.getId())
				.setProgram_number(programNo)
				.setElement_pid(taskProgramBO.getAudio_array().get(0).getPid());

		TaskBO audioTask = new TaskBO().setId(audioTaskId)
				.setType("audio")
				.setRaw_source(audioSource)
				.setEncode_array(new ArrayList<EncodeBO>());

		EncodeBO audioEncode = new EncodeBO().setEncode_id(encodeAudioId);

		String aacMap = templateService.getAudioEncodeMap("aac");
		JSONObject aacObj = JSONObject.parseObject(aacMap);


		audioEncode.setAac(aacObj);

		audioEncode.setProcess_array(new ArrayList<PreProcessingBO>());

		ResampleBO resample = new ResampleBO().setSample_rate(Float.valueOf(aacObj.getFloat("sample_rate")*1000).intValue())
				.setChannel_layout(aacObj.getString("channel_layout"))
				.setFormat(aacObj.getString("sample_fmt"));

		PreProcessingBO audio_decode_processing = new PreProcessingBO().setResample(resample);
		audioEncode.getProcess_array().add(audio_decode_processing);

		audioTask.getEncode_array().add(audioEncode);

		tasks.add(audioTask);

		return tasks;
	}


	/**
	 *
	 * @param taskId
	 * @param tasks
	 * @param pubName 发布文件名
	 * @param playName m3u8名
	 * @return
	 * @throws Exception
	 */
	public List<OutputBO> trans2HLSOutputBO(
			String taskId,
			List<TaskBO> tasks,
			String pubName,
			String playName
	) throws Exception {

		String outputId =  new StringBufferWrapper().append("task-preview-output-")
				.append(taskId)
				.toString();

		List<OutputBO> outputs = new ArrayList<OutputBO>();

		OutputBO output = new OutputBO();

		output.setId(outputId);

		//输出udp
		OutputStorageBO outputStorageBO = new OutputStorageBO().setUrl("file").setDir_name(pubName).setCan_del("true");
		List<OutputMediaGroupBO> outputMediaGroupBOS = new ArrayList<>();
		OutputMediaGroupBO media = new OutputMediaGroupBO();
		media.setAudio_array(new ArrayList<>());
		for(TaskBO taskBO: tasks){
			//视频
			if(taskBO.getId().contains("video")){
				media.setVideo_task_id(taskBO.getId())
						.setVideo_encode_id(taskBO.getEncode_array().get(0).getEncode_id())
						.setVideo_bitrate(0)
						.setBandwidth(0);

			}
			//音频
			if(taskBO.getId().contains("audio")){
				OutputAudioBO outputAudioBO = new OutputAudioBO();
				outputAudioBO.setTask_id(taskBO.getId());
				outputAudioBO.setEncode_id(taskBO.getEncode_array().get(0).getEncode_id());
				outputAudioBO.setBitrate(0);
				media.getAudio_array().add(outputAudioBO);
			}
		}
		outputMediaGroupBOS.add(media);
		OutputHlsBO hlsBO = new OutputHlsBO()
				.setMedia_group_array(outputMediaGroupBOS)
				.setTotal_seg_count(10)
				.setI_frames_only(false)
				.setPlaylist_name(playName)
				.setPlaylist_seg_count(3)
				.setMax_seg_duration(5)
				.setStorage(outputStorageBO);

		output.setHls(hlsBO);

		outputs.add(output);

		return outputs;
	}


	/**
	 * 生成RTMP输出
	 * @param taskId
	 * @param tasks
	 * @param pubName
	 * @param playName
	 * @return
	 * @throws Exception
	 */
	public List<OutputBO> trans2RTMPOutputBO(
			String taskId,
			List<TaskBO> tasks,
			String outputUrl
	) throws Exception {

		String outputId =  new StringBufferWrapper().append("task-preview-output-")
				.append(taskId)
				.toString();

		List<OutputBO> outputs = new ArrayList<OutputBO>();

		OutputBO output = new OutputBO();

		output.setId(outputId);

		//输出rtmp

		List<BaseMediaBO> outputMedias = new ArrayList<>();

		tasks.stream().forEach(t->{
			BaseMediaBO media = new BaseMediaBO();
			media.setTask_id(t.getId());
			media.setEncode_id(t.getEncode_array().get(0).getEncode_id());
			outputMedias.add(media);
		});
		OutputRtmpBO rtmpBO = new OutputRtmpBO()
				.setMedia_array(outputMedias)
				.setVid_exist(true)
				.setAud_exist(true)
				.setPub_user("")
				.setPub_password("")
				.setServer_url(outputUrl);

		output.setRtmp(rtmpBO);

		outputs.add(output);

		return outputs;
	}





	public String addInputs(CreateInputsVO createInputsVO) throws Exception {

		String deviceIp = createInputsVO.getDevice_ip();
		if (deviceIp == null || deviceIp.isEmpty()){
			throw new  Exception("cannot parse capacity ip");
		}

		for (int i = 0; i < createInputsVO.getInput_array().size(); i++) {
			InputBO inputBO = createInputsVO.getInput_array().get(i);
			String uniq = generateUniq(inputBO);
			TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
			if (inputPO == null) {
				inputPO = new TaskInputPO();
				inputPO.setCreateTime(new Date());
				inputPO.setUpdateTime(inputPO.getCreateTime());
				inputPO.setUniq(uniq);
				inputPO.setType(BusinessType.TRANSCODE);
				inputPO.setInput(JSON.toJSONString(inputBO));
				inputPO.setCapacityIp(deviceIp);
				taskInputDao.save(inputPO);
			} else if (inputPO.getCount().equals(0)) {
				inputPO.setInput(JSON.toJSONString(inputBO));
				inputPO.setType(BusinessType.TRANSCODE);
				inputPO.setCreateTime(new Date());
				inputPO.setUpdateTime(inputPO.getCreateTime());
				inputPO.setCount(inputPO.getCount() + 1);
				inputPO.setCapacityIp(deviceIp);
				taskInputDao.save(inputPO);
			} else {
				inputPO.setUpdateTime(inputPO.getCreateTime());
				inputPO.setCapacityIp(deviceIp);
				inputPO.setCount(inputPO.getCount() + 1);
				taskInputDao.save(inputPO);
			}
		}
		CreateInputsRequest createInputsRequest = new CreateInputsRequest();
		createInputsRequest.setMsg_id(createInputsVO.getMsg_id());
		createInputsRequest.setInput_array(createInputsVO.getInput_array());
		CreateInputsResponse createInputsResponse = capacityService.createInputs(deviceIp, createInputsRequest);
		return JSON.toJSONString(createInputsResponse);
	}

	/**
	 * 添加流转码任务--只适合单个input<br/>
	 * 			   TODO: back_up和cover暂时不明确用法，明确了再补
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月13日 下午3:55:48
	 * @param TranscodeTaskVO transcode 流转码信息
	 */
	public void addTranscodeTask(TranscodeTaskVO transcode) throws Exception{
		
		String taskUuid = transcode.getTask_id();
		String capacityIp = transcode.getDevice_ip();

		List<InputBO> inputBOs = transcode.getInput_array();
		List<TaskBO> taskBOs = transcode.getTask_array();
		List<OutputBO> outputBOs = transcode.getOutput_array();

		save(taskUuid, capacityIp, inputBOs, taskBOs, outputBOs, BusinessType.TRANSCODE);

//		if(transcode.getInput_array().size() == 1){
//
//			InputBO inputBO = transcode.getInput_array().iterator().next();
//			List<TaskBO> taskBOs = transcode.getTask_array();
//			List<OutputBO> outputBOs = transcode.getOutput_array();
//
//			String uniq = generateUniq(inputBO);
//
//			save(taskUuid, uniq, capacityIp, inputBO, taskBOs, outputBOs, BusinessType.TRANSCODE);
//		}else if(transcode.getInput_array().size() > 1){
//			InputBO backUpInput = null;
//			List<CheckInputBO> checks = new ArrayList<CheckInputBO>();
//			for(InputBO input: transcode.getInput_array()){
//				String uniq = generateUniq(input);
//				if(uniq.equals(BACK_UP)){
//					backUpInput = input;
//					checks.add(transferNormalInput(input, new StringBufferWrapper().append(uniq)
//																		.append("-")
//																		.append(taskUuid)
//																		.toString(), taskUuid));
//				}else{
//					checks.add(transferNormalInput(input, uniq, taskUuid));
//				}
//			}
//
//			//替换inputId
//			if(backUpInput != null){
//				if(checks != null && checks.size() > 0){
//					List<BackUpProgramBO> programs = new ArrayList<BackUpProgramBO>();
//					if(backUpInput.getBack_up_es() != null){
//						programs = backUpInput.getBack_up_es().getProgram_array();
//					}
//					if(backUpInput.getBack_up_passby() != null){
//						programs = backUpInput.getBack_up_passby().getProgram_array();
//					}
//					if(backUpInput.getBack_up_raw() != null){
//						programs = backUpInput.getBack_up_raw().getProgram_array();
//					}
//					for(BackUpProgramBO program: programs){
//						for(CheckInputBO check: checks){
//							if(check.isExist() && program.getInput_id().equals(check.getReplaceInputId())){
//								program.setInput_id(check.getExsitInputId());
//								break;
//							}
//						}
//					}
//
//					List<Long> inputIds = new ArrayList<Long>();
//					List<InputBO> needSendInputs = new ArrayList<InputBO>();
//					for(CheckInputBO check: checks){
//						inputIds.add(check.getInputId());
//						if(!check.isExist()){
//							needSendInputs.add(check.getInputBO());
//						}
//					}
//
//					sendProtocal(taskUuid, capacityIp, JSON.toJSONString(inputIds), needSendInputs, transcode.getTask_array(), transcode.getOutput_array());
//				}
//			}
//		}
	}

	public void save(
			String taskUuid,
			String capacityIp,
			List<InputBO> inputBOs,
			List<TaskBO> taskBOs,
			List<OutputBO> outputBOs,
			BusinessType businessType) throws Exception{
		AllRequest allRequest = new AllRequest();

		try {
			List<InputBO> needSendInputArray = new ArrayList<>();
			Set<Long> inputsInDB = new HashSet<>();
			for (int i = 0; i < inputBOs.size(); i++) {
				InputBO inputBO = inputBOs.get(i);
				String uniq = generateUniq(inputBO);
				TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
				if (inputPO == null) {
					inputPO = new TaskInputPO();
					inputPO.setCreateTime(new Date());
					inputPO.setUpdateTime(inputPO.getCreateTime());
					inputPO.setUniq(uniq);
					inputPO.setType(businessType);
					inputPO.setInput(JSON.toJSONString(inputBO));
					inputPO.setCapacityIp(capacityIp);
					taskInputDao.save(inputPO);
					needSendInputArray.add(inputBO);
				} else if (inputPO.getCount().equals(0)) {
					inputPO.setInput(JSON.toJSONString(inputBO));
					inputPO.setType(businessType);
					inputPO.setCreateTime(new Date());
					inputPO.setUpdateTime(inputPO.getCreateTime());
					inputPO.setCount(inputPO.getCount() + 1);
					inputPO.setCapacityIp(capacityIp);
					taskInputDao.save(inputPO);
					needSendInputArray.add(inputBO);
				} else {
					inputPO.setUpdateTime(inputPO.getCreateTime());
					inputPO.setCapacityIp(capacityIp);
					inputPO.setCount(inputPO.getCount() + 1);
					taskInputDao.save(inputPO);
				}
				inputsInDB.add(inputPO.getId());
			}

			TaskOutputPO output = new TaskOutputPO();
			output.setInputList( JSON.toJSONString(inputsInDB));
			output.setOutput(JSON.toJSONString(outputBOs));
			output.setTask(JSON.toJSONString(taskBOs));
			output.setTaskUuid(taskUuid);
			output.setType(businessType);
			output.setCapacityIp(capacityIp);
			output.setUpdateTime(new Date());
			taskOutputDao.save(output);

			allRequest.setInput_array(new ArrayListWrapper<InputBO>().addAll(needSendInputArray).getList());
			allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
			allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());

			AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
			responseService.allResponseProcess(allResponse);

		}catch (ObjectOptimisticLockingFailureException e) {

			// 版本不对，version校验
			System.out.println("save校验version版本不对");
			Thread.sleep(300);
			save(taskUuid, capacityIp, inputBOs, taskBOs, outputBOs, businessType);

		} catch (ConstraintViolationException e) {
			//数据已存在（ip，port校验）
			System.out.println("校验输入已存在");
			Thread.sleep(300);
			save(taskUuid, capacityIp, inputBOs, taskBOs, outputBOs, businessType);
		} catch (BaseException e){

			capacityService.deleteAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
			throw e;

		} catch (Exception e) {

			if(!(e instanceof ConstraintViolationException)){
				throw e;
			}

		}
	}




	/**
	 * 删除流转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午8:51:49
	 * @param String id 任务标识
	 */
	public void deleteTranscodeTask(String id) throws Exception{
		
		TaskOutputPO output = delete(id);
		
		if(output != null){
			taskOutputDao.delete(output);
		}

	}
	
	/**
	 * 添加任务流程 -- 一个输入，多个任务，多个输出，
	 *             输入计数+1（并发），
	 *             输出直接存储（不管并发）
	 *             说明：联合unique校验insert（url）； 数据行锁（乐观锁version）校验update<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月12日 下午5:11:11
	 * @param String taskUuid 任务标识
	 * @param String uniq 校验标识
	 * @param String capacityIp 能力ip
	 * @param InputBO inputBO 输入
	 * @param List<TaskBO> taskBOs 任务
	 * @param List<OutputBO> outputBOs 输出
	 * @param BusinessType businessType 业务类型
	 */
	public void save(
			String taskUuid,
			String uniq,
			String capacityIp,
			InputBO inputBO,
			List<TaskBO> taskBOs,
			List<OutputBO> outputBOs,
			BusinessType businessType) throws Exception{
		
		TaskInputPO input = taskInputDao.findByUniq(uniq);
		
		if(input == null){
			
			AllRequest allRequest = new AllRequest();
			try {
				
				input = new TaskInputPO();
				input.setUpdateTime(new Date());
				input.setUniq(uniq);
				input.setTaskUuid(taskUuid);
				input.setInput(JSON.toJSONString(inputBO));
				input.setType(businessType);
				taskInputDao.save(input);
				
				TaskOutputPO output = new TaskOutputPO();
				output.setInputId(input.getId());
				output.setOutput(JSON.toJSONString(outputBOs));
				output.setTask(JSON.toJSONString(taskBOs));
				output.setTaskUuid(taskUuid);
				output.setType(businessType);
				output.setCapacityIp(capacityIp);
				output.setUpdateTime(new Date());
				
				taskOutputDao.save(output);

				allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());
				allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
				allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
				
				AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
				
				responseService.allResponseProcess(allResponse);
			
			} catch (ConstraintViolationException e) {
				
				//数据已存在（ip，port校验）
				System.out.println("校验输入已存在");
				Thread.sleep(300);
				save(taskUuid, uniq, capacityIp, inputBO, taskBOs, outputBOs, businessType);
				
			} catch (BaseException e){
				
				capacityService.deleteAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
				throw e;
				
			} catch (Exception e) {
				
				if(!(e instanceof ConstraintViolationException)){
					throw e;
				}
				
			}
			
		}else{
			
			AllRequest allRequest = new AllRequest();
			try {
				
				if(input.getCount().equals(0)){
					input.setInput(JSON.toJSONString(inputBO));
					input.setTaskUuid(taskUuid);
					input.setType(businessType);
				}
				input.setUpdateTime(new Date());
				input.setCount(input.getCount() + 1);
				taskInputDao.save(input);
				
				TaskOutputPO output = new TaskOutputPO();
				output.setInputId(input.getId());
				output.setOutput(JSON.toJSONString(outputBOs));
				output.setTask(JSON.toJSONString(taskBOs));
				output.setTaskUuid(taskUuid);
				output.setType(businessType);
				output.setCapacityIp(capacityIp);
				output.setUpdateTime(new Date());
				
				taskOutputDao.save(output);
				
				if(input.getCount().equals(1)){
					
					allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());
					allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
					allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
				
				}else{
					
					InputBO existInput = JSONObject.parseObject(input.getInput(), InputBO.class);
					transformInput(taskBOs, existInput);
					allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
					allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
					
				}
				
				AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
				
				responseService.allResponseProcess(allResponse);
							
			} catch (ObjectOptimisticLockingFailureException e) {
				
				// 版本不对，version校验
				System.out.println("save校验version版本不对");
				Thread.sleep(300);
				save(taskUuid, uniq, capacityIp, inputBO, taskBOs, outputBOs, businessType);
				
			} catch (BaseException e){
				
				capacityService.deleteAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
				throw e;

			} catch (Exception e) {
				
				if(!(e instanceof ObjectOptimisticLockingFailureException)){
					throw e;
				}
			}
		}
	}
	
	/**
	 * 删除任务流程 -- 输入计数减 一（并发）
	 * 			        输出返回，上层删除（不管并发）
	 * 			        数据没有清除，起线程清除<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月13日 下午3:01:09
	 * @param String taskUuid 任务标识
	 * @return TaskOutputPO 输出
	 */
	public TaskOutputPO delete(String taskUuid) throws Exception {
		
		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskUuid, BusinessType.TRANSCODE);
		
		if(output != null){
			
			InputBO needRevoceCoverInputBO = null;
			if(output.getCoverId() != null){
				
				TaskInputPO cover = taskInputDao.findOne(output.getCoverId());
				
				if(cover != null){
					
					InputBO coverInputBO = JSONObject.parseObject(cover.getInput(), InputBO.class);
				
					try {

						cover.setUpdateTime(new Date());
						if(cover.getCount() >= 1){
							cover.setCount(cover.getCount() - 1);
						}
						taskInputDao.save(cover);
						
						if(cover.getCount().equals(0) && cover.getInput() != null){
							needRevoceCoverInputBO = coverInputBO;
						}
				
						output.setCoverId(null);
						
						taskOutputDao.save(output);
						
					} catch (ObjectOptimisticLockingFailureException e) {
						
						// 版本不对，version校验
						System.out.println("delete校验version版本不对");
						Thread.sleep(300);
						output = delete(taskUuid);
					}
				}
			}
			
			if(output.getInputId() != null){
				TaskInputPO input = taskInputDao.findOne(output.getInputId());
				
				if(input != null){
					
					try {

						input.setUpdateTime(new Date());
						if(input.getCount() >= 1){
							input.setCount(input.getCount() - 1);
						}
						taskInputDao.save(input);
						
						AllRequest allRequest = new AllRequest();
						allRequest.setInput_array(new ArrayList<InputBO>());
						
						List<OutputBO> outputBOs = JSONObject.parseArray(output.getOutput(), OutputBO.class);
						List<TaskBO> tasks = JSONObject.parseArray(output.getTask(), TaskBO.class);

						//任务要能删掉
						InputBO inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
						
						if(input.getCount().equals(0) && input.getInput() != null){
							allRequest.getInput_array().add(inputBO);
						}
						if(needRevoceCoverInputBO != null){
							allRequest.getInput_array().add(needRevoceCoverInputBO);
						}
						if(tasks != null){
							allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(tasks).getList());
						}
						if(outputBOs != null){
							allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
						}
					
						capacityService.deleteAllAddMsgId(allRequest, output.getCapacityIp(), capacityProps.getPort());
						
						output.setOutput(null);
						output.setTask(null);
						
						taskOutputDao.save(output);
						
					} catch (ObjectOptimisticLockingFailureException e) {
						
						// 版本不对，version校验
						System.out.println("delete校验version版本不对");
						Thread.sleep(300);
						output = delete(taskUuid);
					}
				}
			}

			if(output.getInputList() != null){
				
				List<Long> inputIds = JSONArray.parseArray(output.getInputList(), Long.class);
				List<TaskInputPO> inputs = taskInputDao.findByIdIn(inputIds);
				if(inputs != null && inputs.size() > 0){
					
					AllRequest allRequest = new AllRequest();
					allRequest.setInput_array(new ArrayList<InputBO>());
					for(TaskInputPO input: inputs){
						try {

							input.setUpdateTime(new Date());
							if(input.getCount() >= 1){
								input.setCount(input.getCount() - 1);
							}
							
							taskInputDao.save(input);
							
							InputBO inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
							
							if(input.getCount().equals(0) && input.getInput() != null){
								allRequest.getInput_array().add(inputBO);
							}

						} catch (ObjectOptimisticLockingFailureException e) {
							
							// 版本不对，version校验
							System.out.println("delete校验version版本不对");
							Thread.sleep(300);
							output = delete(taskUuid);
						}
					}
					
					if(needRevoceCoverInputBO != null){
						allRequest.getInput_array().add(needRevoceCoverInputBO);
					}
					
					List<OutputBO> outputBOs = JSONObject.parseArray(output.getOutput(), OutputBO.class);
					List<TaskBO> tasks = JSONObject.parseArray(output.getTask(), TaskBO.class);
					
					if(tasks != null){
						allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(tasks).getList());
					}
					if(outputBOs != null){
						allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
					}
				
					capacityService.deleteAllAddMsgId(allRequest, output.getCapacityIp(), capacityProps.getPort());
					
					output.setOutput(null);
					output.setTask(null);
					
					taskOutputDao.save(output);
				}
			}
	
		}
		
		return output;
	}
	
	/**
	 * 为所有任务换为已存在的输入id<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月12日 下午4:21:14
	 * @param List<TaskBO> tasks 所有任务
	 * @param InputBO input 输入
	 */
	public void transformInput(List<TaskBO> tasks, InputBO input) throws Exception{
		
		String inputId = input.getId();
		for(TaskBO task: tasks){
			if(task.getRaw_source() != null){
				task.getRaw_source().setInput_id(inputId);
			}
			if(task.getEs_source() != null){
				task.getEs_source().setInput_id(inputId);
			}
			if(task.getPassby_source() != null){
				task.getPassby_source().setInput_id(inputId);
			}
		}
	}
	
	/**
	 * 生成输入校验标识<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 上午10:48:07
	 * @param InputBO inputBO 输入
	 * @return String 输入校验标识
	 */
	public String generateUniq(InputBO inputBO) throws Exception{
		String uniq = "";
		if(inputBO.getUdp_ts() != null){
			uniq = new StringBuffer().append(inputBO.getUdp_ts().getSource_ip())
									 .append("%")
									 .append(inputBO.getUdp_ts().getSource_port())
									 .append("%")
									 .append(inputBO.getUdp_ts().getLocal_ip())
									 .toString();
		}
		if(inputBO.getRtp_ts() != null){
			uniq = new StringBuffer().append(inputBO.getRtp_ts().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getRtp_ts().getSource_port())
					 				 .append("%")
									 .append(inputBO.getRtp_ts().getLocal_ip())
					 				 .toString();
		}
		if(inputBO.getHttp_ts() != null){
			uniq = inputBO.getHttp_ts().getUrl();
		}
		if(inputBO.getSrt_ts() != null){
			uniq = new StringBuffer().append(inputBO.getSrt_ts().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getSrt_ts().getSource_port())
					 				 .append("%")
					 				 .toString();
		}
		if(inputBO.getHls() != null){
			uniq = inputBO.getHls().getUrl();
		}
		if(inputBO.getDash() != null){
			uniq = inputBO.getDash().getUrl();
		}
		if(inputBO.getMss() != null){
			uniq = inputBO.getMss().getUrl();
		}
		if(inputBO.getRtsp() != null){
			uniq = inputBO.getRtsp().getUrl();
		}
		if(inputBO.getRtmp() != null){
			uniq = inputBO.getRtmp().getUrl();
		}
		if(inputBO.getHttp_flv() != null){
			uniq = inputBO.getHttp_flv().getUrl();
		}
		if(inputBO.getSdi() != null){
			uniq = new StringBuffer().append(inputBO.getSdi().getCard_no())
	 				 				 .append("%")
					 				 .append(inputBO.getSdi().getCard_port())
					 				 .toString();
		}
		if(inputBO.getRtp_es() != null){
			uniq = new StringBuffer().append("%")
									 .append(inputBO.getRtp_es().getLocal_port())
									 .append("%")
									 .toString();
		}
		if(inputBO.getFile() != null){
			uniq = inputBO.getId();
		}
		if(inputBO.getUdp_pcm() != null){
			uniq = new StringBuffer().append(inputBO.getUdp_pcm().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getUdp_pcm().getSource_port())
					 				 .toString();
		}
		//不管是否同源，每个任务一个备份关系
		if(inputBO.getBack_up_es() != null || inputBO.getBack_up_passby() != null || inputBO.getBack_up_raw() != null){
			uniq = inputBO.getId();
		}
//		if(inputBO.getBack_up_es() != null ){
//			uniq = inputBO.getBack_up_es().getProgram_array().stream().map(BackUpProgramBO::getInput_id).collect(Collectors.joining("%"));
//		}
//		if (inputBO.getBack_up_passby() != null) {
//			uniq = inputBO.getBack_up_passby().getProgram_array().stream().map(BackUpProgramBO::getInput_id).collect(Collectors.joining("%"));
//		}
//		if (inputBO.getBack_up_raw() != null) {
//			uniq = inputBO.getBack_up_raw().getProgram_array().stream().map(BackUpProgramBO::getInput_id).collect(Collectors.joining("%"));
//		}

		return uniq;
	}


	/**
	 * 输入校验<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 上午11:01:57
	 * @param InputBO inputBO 输入
	 * @param String uniq 输入校验标识
	 * @param String taskUuid 任务标识
	 * @return InputCheckBO
	 */
	public CheckInputBO transferNormalInput(InputBO inputBO, String uniq, String taskUuid) throws Exception{
		
		TaskInputPO input = taskInputDao.findByUniq(uniq);
		CheckInputBO check = new CheckInputBO();
		boolean isExist = false;
		if(input == null){
			
			try {
				
				input = new TaskInputPO();
				input.setUpdateTime(new Date());
				input.setUniq(uniq);
				input.setTaskUuid(taskUuid);
				input.setInput(JSON.toJSONString(inputBO));
				input.setType(BusinessType.TRANSCODE);
				taskInputDao.save(input);
			
			} catch (ConstraintViolationException e) {
				
				//数据已存在（ip，port校验）
				System.out.println("校验输入已存在");
				Thread.sleep(300);
				transferNormalInput(inputBO, uniq, taskUuid);
				
			} catch (Exception e) {
				
				if(!(e instanceof ConstraintViolationException)){
					throw e;
				}
				
			}
			
		}else{
			
			try {
				
				if(input.getCount().equals(0)){
					input.setInput(JSON.toJSONString(inputBO));
					input.setTaskUuid(taskUuid);
					input.setType(BusinessType.TRANSCODE);
				}else{
					check.setReplaceInputId(inputBO.getId());
					inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
					check.setExsitInputId(inputBO.getId());
					check.setInputBO(inputBO);
					isExist = true;
				}
				input.setUpdateTime(new Date());
				input.setCount(input.getCount() + 1);
				taskInputDao.save(input);
				
			} catch (ObjectOptimisticLockingFailureException e) {
				
				// 版本不对，version校验
				System.out.println("save校验version版本不对");
				Thread.sleep(300);
				transferNormalInput(inputBO, uniq, taskUuid);
				
			}catch (Exception e) {
				
				if(!(e instanceof ObjectOptimisticLockingFailureException)){
					throw e;
				}
			}
		}
		if(!isExist){
			check.setInputBO(inputBO);
		}
		check.setExist(isExist);
		check.setInputId(input.getId());

		return check;
	}
	
	/**
	 * 多源备份源发送协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午12:00:34
	 * @param String taskUuid
	 * @param String capacityIp
	 * @param String inputIds
	 * @param List<InputBO> inputBOs
	 * @param List<TaskBO> taskBOs
	 * @param List<OutputBO> outputBOs
	 */
	public void sendProtocal(
			String taskUuid,
			String capacityIp,
			String inputIds,
			List<InputBO> inputBOs,
			List<TaskBO> taskBOs,
			List<OutputBO> outputBOs) throws Exception{
		
		AllRequest allRequest = new AllRequest();
		try {
			
			TaskOutputPO output = new TaskOutputPO();
			output.setInputList(inputIds);
			output.setOutput(JSON.toJSONString(outputBOs));
			output.setTask(JSON.toJSONString(taskBOs));
			output.setTaskUuid(taskUuid);
			output.setType(BusinessType.TRANSCODE);
			output.setCapacityIp(capacityIp);
			output.setUpdateTime(new Date());
			
			taskOutputDao.save(output);

			allRequest.setInput_array(new ArrayListWrapper<InputBO>().addAll(inputBOs).getList());
			allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
			allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
			
			AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
			
			responseService.allResponseProcess(allResponse);
		
		} catch (BaseException e){
			
			capacityService.deleteAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
			throw e;
			
		} catch (Exception e) {
			
			if(!(e instanceof ConstraintViolationException)){
				throw e;
			}
			
		}
	}




	/**
	 * 切换备份源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 上午8:33:45
	 * @param String inputId 备份源输入id
	 * @param String index 备份源索引
	 * @param String ip 能力ip
	 */
	public void changeBackUp(String inputId, String index,String mode, String ip) throws Exception{

		ResultCodeResponse result = capacityService.changeBackUp(inputId, index,mode, ip, capacityProps.getPort());
		
		if(!result.getResult_code().equals(InputResponseEnum.SUCCESS.getCode())){
			throw new BaseException(StatusCode.FORBIDDEN, result.getResult_msg());
		}
	}
	
	/**
	 * 添加盖播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 上午8:54:02
	 * @param String taskId 任务id
	 * @param InputBO input 盖播源
	 */
	public void addCover(String taskId, InputBO input) throws Exception{
		
		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskId, BusinessType.TRANSCODE);

		if(output != null){
			
			String capacityIp = output.getCapacityIp();
			
			if(output.getCoverId() != null) throw new BaseException(StatusCode.FORBIDDEN, "该任务的盖播已存在");
			
			TaskInputPO taskInput = null;
			
			if(output.getInputId() != null){
				
				//单源
				taskInput = taskInputDao.findOne(output.getInputId());
				
			}else if(output.getInputList() != null){
				//多源
				List<Long> inputIds = JSONArray.parseArray(output.getInputList(), Long.class);
				if (inputIds.size()==1){
					taskInput = taskInputDao.findOne(inputIds.get(0));
				}else {
					List<TaskInputPO> inputPOs = taskInputDao.findByIdIn(inputIds);
					for (TaskInputPO inputPO : inputPOs) {
						JSONObject inputObj = JSONObject.parseObject(inputPO.getInput());
						if (inputObj.containsKey("back_up_es") || inputObj.containsKey("back_up_passby") || inputObj.containsKey("back_up_raw")) {
							taskInput = inputPO;
							break;
						}
					}
				}
			}
			
			if(taskInput != null){
				
				String coverUuid = new StringBufferWrapper().append(COVER)
															.append("-")
															.append(taskInput.getId())
															.toString();
				
				InputBO exsitInputBO = JSONObject.parseObject(taskInput.getInput(), InputBO.class);

				input.getCover().getProgram_array().iterator().next().setInput_id(exsitInputBO.getId());
				
				CheckInputBO check = transferNormalInput(input, coverUuid, taskId);
				InputBO coverInput = check.getInputBO();
				
				if(!check.isExist()){
					
					//向能力添加盖播input
					AllRequest all = new AllRequest();
					all.setInput_array(new ArrayListWrapper<InputBO>().add(coverInput).getList());
					AllResponse response = capacityService.createAllAddMsgId(all, capacityIp, capacityProps.getPort());
					responseService.allResponseProcess(response);
					
				}
				
				//切换task中input
				List<TaskBO> tasks = JSONArray.parseArray(output.getTask(), TaskBO.class);
				for(TaskBO task: tasks){
					PutTaskSourceRequest source = new PutTaskSourceRequest();
					if(task.getEs_source() != null){
						
						TaskSourceBO sourceBO = task.getEs_source();
						sourceBO.setInput_id(coverInput.getId());
						source.setEs_source(sourceBO);
						
					}
					if(task.getRaw_source() != null){
						
						TaskSourceBO sourceBO = task.getRaw_source();
						sourceBO.setInput_id(coverInput.getId());
						source.setRaw_source(sourceBO);
						
					}
					if(task.getPassby_source() != null){
						
						TaskSourceBO sourceBO = task.getPassby_source();
						sourceBO.setInput_id(coverInput.getId());
						source.setPassby_source(sourceBO);
						
					}
					
					capacityService.modifyTaskSourceAddMsgId(task.getId(), capacityIp, source);
				}
				
				output.setTask(JSON.toJSONString(tasks));
				output.setCoverId(check.getInputId());
				
				taskOutputDao.save(output);
						
			}
		}
	}
	
	/**
	 * 删除盖播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 上午8:55:03
	 * @param String taskId 盖播源id
	 */
	public void deleteCover(String taskId) throws Exception{
		
		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskId, BusinessType.TRANSCODE);

		if(output != null){
			
			if(output.getCoverId() == null) throw new BaseException(StatusCode.FORBIDDEN, "盖播不存在！");
			
			TaskInputPO cover = taskInputDao.findOne(output.getCoverId());
			
			if(cover != null){
				
				InputBO coverInputBO = JSONObject.parseObject(cover.getInput(), InputBO.class);
				
				String replaceInputId = coverInputBO.getCover().getProgram_array().iterator().next().getInput_id();
				
				//替换task中inputId
				List<TaskBO> tasks = JSONArray.parseArray(output.getTask(), TaskBO.class);
				for(TaskBO task: tasks){
					PutTaskSourceRequest source = new PutTaskSourceRequest();
					if(task.getEs_source() != null){
						
						TaskSourceBO sourceBO = task.getEs_source();
						sourceBO.setInput_id(replaceInputId);
						source.setEs_source(sourceBO);
						
					}
					if(task.getRaw_source() != null){
						
						TaskSourceBO sourceBO = task.getRaw_source();
						sourceBO.setInput_id(replaceInputId);
						source.setRaw_source(sourceBO);
						
					}
					if(task.getPassby_source() != null){
						
						TaskSourceBO sourceBO = task.getPassby_source();
						sourceBO.setInput_id(replaceInputId);
						source.setPassby_source(sourceBO);
						
					}
					
					capacityService.modifyTaskSourceAddMsgId(task.getId(), output.getCapacityIp(), source);
					
				}
				
				try {

					cover.setUpdateTime(new Date());
					if(cover.getCount() >= 1){
						cover.setCount(cover.getCount() - 1);
					}
					taskInputDao.save(cover);
					
					AllRequest allRequest = new AllRequest();
					
					InputBO inputBO = JSONObject.parseObject(cover.getInput(), InputBO.class);
					
					if(cover.getCount().equals(0) && cover.getInput() != null){
						allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());
						capacityService.deleteAllAddMsgId(allRequest, output.getCapacityIp(), capacityProps.getPort());
					}
			
					output.setCoverId(null);
					
					taskOutputDao.save(output);
					
				} catch (ObjectOptimisticLockingFailureException e) {
					
					// 版本不对，version校验
					System.out.println("delete校验version版本不对");
					Thread.sleep(300);
					output = delete(taskId);
				}
			}
			
		}
		
	}
	
	/**
	 * 添加输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 上午11:37:24
	 * @param Long taskId 转码任务id
	 * @param List<OutputBO> outputs 需要添加的输出
	 */
	public void addOutput(Long taskId, List<OutputBO> outputs) throws Exception{
		
		TaskOutputPO task = taskOutputDao.findByTaskUuidAndType(taskId.toString(), BusinessType.TRANSCODE);
		
		if(task == null){
			throw new BaseException(StatusCode.ERROR, "任务不存在在！");
		}
		
		CreateOutputsRequest outputsRequest = new CreateOutputsRequest();
		outputsRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputs).getList());
		//创建输出
		CreateOutputsResponse outputResponse = capacityService.createOutputsWithMsgId(outputsRequest, task.getCapacityIp());
		
		//创建输出返回处理 -- 回滚
		List<String> outputIds = responseService.outputResponseProcess(outputResponse, null, null, task.getCapacityIp());
		
		outputs.addAll(outputs);
		
		task.setOutput(JSON.toJSONString(outputs));
		
		taskOutputDao.save(task);
		
	}
	
	/**
	 * 删除流转码输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午1:19:54
	 * @param Long taskId 任务id
	 * @param Long outputId 输出id
	 */
	public void deleteOutput(Long taskId, Long outputId) throws Exception{
		
		TaskOutputPO taskPO = taskOutputDao.findByTaskUuidAndType(taskId.toString(), BusinessType.TRANSCODE);
		
		if(taskPO == null){
			throw new BaseException(StatusCode.ERROR, "任务不存在！");
		}
		
		List<OutputBO> outputs = JSONObject.parseArray(taskPO.getOutput(), OutputBO.class);
		
		List<OutputBO> needDeleteOutputs = new ArrayList<OutputBO>();
		if(outputs != null && outputs.size() > 0){
			for(OutputBO outputBO: outputs){
				if(outputBO.getId().equals(outputId.toString())){
					needDeleteOutputs.add(outputBO);
				}
			}
		}
		
		if(needDeleteOutputs.size() > 0){
			
			outputs.removeAll(needDeleteOutputs);
			
			DeleteOutputsRequest delete = new DeleteOutputsRequest().setOutput_array(new ArrayList<IdRequest>());
			for(OutputBO outputBO: needDeleteOutputs){
				IdRequest idRequest = new IdRequest().setId(outputBO.getId());
				delete.getOutput_array().add(idRequest);
			}

			capacityService.deleteOutputsWithMsgId(delete, taskPO.getCapacityIp());
			
			taskPO.setOutput(JSON.toJSONString(outputs));
			taskOutputDao.save(taskPO);
			
		}
		
	}
	
	/**
	 * 设置告警列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月29日 下午3:32:23
	 * @param String ip 转换模块ip
	 * @param String alarmlist 告警列表
	 */
	public void putAlarmList(String ip, String alarmlist) throws Exception{
		capacityService.putAlarmList(ip, capacityProps.getPort(), alarmlist);
	}
	
	/**
	 * 清空转换模块上所有任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午2:41:38
	 * @param String ip 转换模块ip
	 */
	public void removeAll(String ip) throws Exception{
		
		List<TaskOutputPO> outputs = taskOutputDao.findByCapacityIp(ip);
		
		if(outputs != null && outputs.size() > 0){
			Set<Long> inputIds = new HashSet<Long>();
			for(TaskOutputPO outputPO: outputs){
				if(!Objects.isNull(outputPO.getInputId())){
					//单源
					inputIds.add(outputPO.getInputId());
				}else if(!StringUtils.isEmpty(outputPO.getInputList())){
					//备份源
					inputIds.addAll(JSONArray.parseArray(outputPO.getInputList(), Long.class));
				}else if(!Objects.isNull(outputPO.getCoverId())){
					//盖播
					inputIds.add(outputPO.getCoverId());
				}else if(!Objects.isNull(outputPO.getScheduleId())){
					//排期
					inputIds.add(outputPO.getScheduleId());
				}else if(!Objects.isNull(outputPO.getPrevId())){
					//追加排期prev
					inputIds.add(outputPO.getPrevId());
				}else if(!Objects.isNull(outputPO.getNextId())){
					//追加排期next
					inputIds.add(outputPO.getNextId());
				}
			}
			
			List<TaskInputPO> inputPOs = taskInputDao.findByIdIn(inputIds);	
			if(inputPOs != null && inputPOs.size() > 0){
				for(TaskInputPO inputPO: inputPOs){
					inputPO.setCount(0);
				}
			}
			
			taskInputDao.save(inputPOs);
			taskOutputDao.deleteInBatch(outputs);
			
		}
		
		//清空转换模块上面所有任务
		capacityService.removeAll(ip);
	}

	public TaskInputPO addInputToDB(InputBO inputBO,BusinessType busType) throws Exception {
		String uniq = generateUniq(inputBO);
		TaskInputPO inputPO = new TaskInputPO();
		inputPO.setCreateTime(new Date());
		inputPO.setUpdateTime(inputPO.getCreateTime());
		inputPO.setUniq(uniq);
		inputPO.setType(busType);
		inputPO.setInput(JSON.toJSONString(inputBO));
		taskInputDao.save(inputPO);
		return inputPO;
	}

	public void updateInputToDB(InputBO inputBO,BusinessType busType) throws Exception {
		String uniq = generateUniq(inputBO);
		TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
		inputPO.setUpdateTime(new Date());
		inputPO.setUniq(uniq);
		inputPO.setType(BusinessType.TRANSCODE);
		inputPO.setInput(JSON.toJSONString(inputBO));
		taskInputDao.save(inputPO);
	}





	/**
	 * 处理某个设备上关于某个任务的所有任务命令<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午2:41:38
	 * @param String ip 转换模块ip
	 */
	public void modifyTranscodeTask(TaskSetVO taskSetVO) throws Exception {
//		先发命令
		String taskUuid = taskSetVO.getTask_link_id().toString();
		String capacityIp = taskSetVO.getDevice_ip();

		TaskOutputPO taskOutputPO = taskOutputDao.findByTaskUuidAndType(taskUuid, BusinessType.TRANSCODE);
		if (null == taskOutputPO){
			LOG.warn("task not exist, id: {}", taskUuid);
			return;
		}

		List<Long> inputIds = JSONArray.parseArray(taskOutputPO.getInputList(), Long.class);
		List<TaskInputPO> taskInputPOS = taskInputDao.findByIdIn(inputIds);

		//增加输入
		if (Objects.nonNull(taskSetVO.getCreate_input())){
			capacityService.createInputs(capacityIp, taskSetVO.getCreate_input());
			List<Long> inputList = JSONObject.parseArray(taskOutputPO.getInputList(),Long.class);
			taskSetVO.getCreate_input().getInput_array().stream().forEach(i-> {
				try {
					TaskInputPO inputPO = addInputToDB(i,BusinessType.TRANSCODE);
					if (!inputList.contains(inputPO.getId())) {
						inputList.add(inputPO.getId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			taskOutputPO.setInputList(JSONObject.toJSONString(inputList));
		}



		//修改输入参数
		if (Objects.nonNull(taskSetVO.getModify_input_params())){
			for (int i=0;i<taskSetVO.getModify_input_params().size();i++) {
				PutInputsRequest putInputsRequest = taskSetVO.getModify_input_params().get(i);
				capacityService.modifyInputs(capacityIp, putInputsRequest);
				updateInputToDB(putInputsRequest.getInput(),BusinessType.TRANSCODE);
			}
		}



		//删除输入
		if (Objects.nonNull(taskSetVO.getDelete_input())){
			capacityService.deleteInputs(capacityIp, taskSetVO.getDelete_input());
			List<String> delInputList = taskSetVO.getDelete_input().getInput_array().stream().map(IdRequest::getId).collect(Collectors.toList());
			List<Long> inputList = JSONObject.parseArray(taskOutputPO.getInputList(),Long.class);
			taskInputPOS.stream().forEach(i->{
				InputBO inputBO = JSONObject.parseObject(i.getInput(),InputBO.class);
				if (delInputList.contains(inputBO.getId())){
					taskInputDao.delete(i);
					inputList.remove(i.getId());
				}
			});
			taskOutputPO.setInputList(JSONObject.toJSONString(inputList));
		}

		//增加节目
		if (Objects.nonNull(taskSetVO.getCreate_program())){
			CreateProgramsRequest createProgramsRequest = taskSetVO.getCreate_program();
			capacityService.createProgram(capacityIp, createProgramsRequest);
			taskInputPOS.stream().forEach(i-> {
				InputBO inputBO = JSONObject.parseObject(i.getInput(), InputBO.class);
				if (inputBO.getId().equals(createProgramsRequest.getInput_id())){
					inputBO.getProgram_array().addAll(createProgramsRequest.getProgram_array());
					i.setInput(JSONObject.toJSONString(inputBO));
					taskInputDao.save(i);
				}
			});
		}

		//删除节目
		if (Objects.nonNull(taskSetVO.getDelete_program())){
			DeleteProgramRequest deleteProgramRequest = taskSetVO.getDelete_program();
			capacityService.deleteProgram(capacityIp,deleteProgramRequest );

			taskInputPOS.stream().forEach(i-> {
				InputBO inputBO = JSONObject.parseObject(i.getInput(), InputBO.class);
				if (inputBO.getId().equals(deleteProgramRequest.getInput_id())){
					inputBO.getProgram_array().removeIf(p->{
						return deleteProgramRequest.getProgram_array().stream().anyMatch(d->d.getProgram_number().equals(p.getProgram_number()));
					});
					i.setInput(JSONObject.toJSONString(inputBO));
					taskInputDao.save(i);
				}
			});
		}

		//修改节目备份
		modifyBackupMode(taskSetVO,taskInputPOS,capacityIp);

		//创建任务
		if (Objects.nonNull(taskSetVO.getAdd_task())){
			CreateTaskRequest createTaskRequest = taskSetVO.getAdd_task();
			capacityService.createTasksWithMsgId(createTaskRequest,capacityIp);
			List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
			createTaskRequest.getTask_array().stream().forEach(t->{
				oriTaskBOS.add(t);
			});
			taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
		}


		//删除任务
		if (Objects.nonNull(taskSetVO.getDelete_task())){
			DeleteTasksRequest deleteTasksRequest = taskSetVO.getDelete_task();
			capacityService.deleteTasksWithMsgId(deleteTasksRequest,capacityIp);
			List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
			deleteTasksRequest.getTask_array().forEach(t->{
				oriTaskBOS.removeIf(o->o.getId().equals(t.getId()));
			});
			taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
		}


		//修改编码预处理
		if (Objects.nonNull(taskSetVO.getModify_decode_process())){
			for (int i=0;i<taskSetVO.getModify_decode_process().size();i++) {
				PutTaskDecodeProcessRequest putTaskDecodeProcessRequest = taskSetVO.getModify_decode_process().get(i);
				capacityService.modifyTaskDecodeProcess(capacityIp, putTaskDecodeProcessRequest);
				List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
				oriTaskBOS.stream().filter(t->putTaskDecodeProcessRequest.getTask_id().equals(t.getId())).forEach(t->{
					t.getDecode_process_array().clear();
					t.setDecode_process_array(putTaskDecodeProcessRequest.getProcess_array());
				});
				taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
			}
		}

		//增加编码
		if (Objects.nonNull(taskSetVO.getAdd_encoders())&& !taskSetVO.getAdd_encoders().isEmpty()){
			for (int i=0;i<taskSetVO.getAdd_encoders().size();i++) {
				AddTaskEncodeRequest addTaskEncodeRequest = taskSetVO.getAdd_encoders().get(i);
				List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
				for (int j=0;j<oriTaskBOS.size();j++){
					TaskBO oriTask = oriTaskBOS.get(j);
					if (oriTask.getId().equals(addTaskEncodeRequest.getTask_id())){
						capacityService.addTaskEncode(capacityIp, addTaskEncodeRequest);
						oriTask.getEncode_array().addAll(addTaskEncodeRequest.getEncode_array());
					}
				}
				taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
			}
		}

		//修改编码
		if (Objects.nonNull(taskSetVO.getModify_encoders())&& !taskSetVO.getModify_encoders().isEmpty()){
			for (int i=0;i<taskSetVO.getModify_encoders().size();i++) {
				PutTaskEncodeRequest putTaskEncodeRequest = taskSetVO.getModify_encoders().get(i);
				List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
				for (int j=0;j<oriTaskBOS.size();j++){
					TaskBO oriTask = oriTaskBOS.get(j);
					if (oriTask.getId().equals(putTaskEncodeRequest.getTask_id())){
						capacityService.modifyTaskEncode(capacityIp, putTaskEncodeRequest);
						oriTask.getEncode_array().removeIf(e->e.getEncode_id().equals(putTaskEncodeRequest.getEncode_param().getEncode_id()));
						oriTask.getEncode_array().add(putTaskEncodeRequest.getEncode_param());
					}
				}
				taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
			}
		}

		//修改编码模式
		if (Objects.nonNull(taskSetVO.getModify_decode_mode()) && !taskSetVO.getModify_decode_mode().isEmpty()){
			for (int i = 0; i < taskSetVO.getModify_decode_mode().size(); i++) {
				PatchDecodeRequest patchDecodeRequest = taskSetVO.getModify_decode_mode().get(i);

				Integer programNum = Integer.parseInt(patchDecodeRequest.getProgram_num());
				Integer pid = Integer.parseInt(patchDecodeRequest.getPid());
				String encodeMode = patchDecodeRequest.getDecode_mode();
				capacityService.modifyProgramDecodeMode(capacityIp,patchDecodeRequest);
				if (programNum == null || pid == null || encodeMode == null || encodeMode.isEmpty() ){
					throw new Exception("modify encode param parse error");
				}

				taskInputPOS.stream().forEach(inputPO-> {
					InputBO inputBO = JSONObject.parseObject(inputPO.getInput(), InputBO.class);
					inputBO.getProgram_array().stream().filter(in->programNum.equals(in.getProgram_number())).forEach(p->{
						if (p.getAudio_array()!=null &&!p.getAudio_array().isEmpty()) { p.getAudio_array().stream().filter(a -> pid.equals(a.getPid())).forEach(a -> a.setDecode_mode(encodeMode)); }
						if (p.getVideo_array()!=null &&!p.getVideo_array().isEmpty()) {p.getVideo_array().stream().filter(a->pid.equals(a.getPid())).forEach(a->a.setDecode_mode(encodeMode));}
						if (p.getSubtitle_array()!=null &&!p.getSubtitle_array().isEmpty()) {p.getSubtitle_array().stream().filter(a->pid.equals(a.getPid())).forEach(a->a.setDecode_mode(encodeMode));}
					});
					inputPO.setInput(JSONObject.toJSONString(inputBO));
					taskInputDao.save(inputPO);
				});
			}

		}

		//删除编码,协议有问题
		if (Objects.nonNull(taskSetVO.getDelete_encoders())&& !taskSetVO.getDelete_encoders().isEmpty()){
			for (int i=0;i<taskSetVO.getDelete_encoders().size();i++) {
				DeleteTaskEncodeResponse deleteTaskEncodeResponse = taskSetVO.getDelete_encoders().get(i);
				List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
				for (int j=0;j<oriTaskBOS.size();j++){
					TaskBO oriTask = oriTaskBOS.get(j);
					if (oriTask.getId().equals(deleteTaskEncodeResponse.getTask_id())){
						capacityService.deleteTaskEncode(capacityIp, deleteTaskEncodeResponse);
						for (int k=0; k < oriTask.getEncode_array().size(); k++) {
							EncodeBO encodeBO = oriTask.getEncode_array().get(k);
							deleteTaskEncodeResponse.getEncode_array().stream()
									.filter(e->e.getEncode_id().equals(encodeBO.getEncode_id()))
									.forEach(e->{
										oriTask.getEncode_array().remove(encodeBO);
									});
						}
					}
				}
				taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
			}
		}

		//修改源
		if (Objects.nonNull(taskSetVO.getModify_source())){
			for (int i=0;i<taskSetVO.getModify_source().size();i++) {
				PutTaskSourceRequest putTaskSourceRequest = taskSetVO.getModify_source().get(i);
				capacityService.modifyTaskSource(capacityIp,putTaskSourceRequest);
				List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
				oriTaskBOS.stream().filter(t->putTaskSourceRequest.getTask_id().equals(t.getId())).forEach(t->{
					if (putTaskSourceRequest.getRaw_source()!=null){ t.setRaw_source(putTaskSourceRequest.getRaw_source()); }
					if (putTaskSourceRequest.getEs_source()!=null){ t.setEs_source(putTaskSourceRequest.getEs_source()); }
					if (putTaskSourceRequest.getPassby_source()!=null){ t.setPassby_source(putTaskSourceRequest.getPassby_source()); }
					if (putTaskSourceRequest.getVideo_mix_source()!=null){ t.setVideo_mix_source(putTaskSourceRequest.getVideo_mix_source()); }
					if (putTaskSourceRequest.getAudio_mix_source()!=null){ t.setAudio_mix_source(putTaskSourceRequest.getAudio_mix_source()); }
				});
				taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
			}
		}

		//增加输出
		if (Objects.nonNull(taskSetVO.getAdd_output())) {
			CreateOutputsResponse outputResponse = capacityService.createOutputsWithMsgId(taskSetVO.getAdd_output(),capacityIp);
			List<String> outputIds = responseService.outputResponseProcess(outputResponse, null, null, capacityIp);

			JSONArray oriOutputs = JSON.parseArray(taskOutputPO.getOutput());
			oriOutputs.addAll(taskSetVO.getAdd_output().getOutput_array());
			taskOutputPO.setOutput(JSON.toJSONString(oriOutputs));
		}
		//修改输出
		if (Objects.nonNull(taskSetVO.getModify_output())&& !taskSetVO.getModify_output().isEmpty()){
			for (int i=0;i<taskSetVO.getModify_output().size();i++) {
				List<OutputBO> oriOutputs = JSONObject.parseArray(taskOutputPO.getOutput(), OutputBO.class);
				List<String> needModifyOutIds = taskSetVO.getModify_output().stream().map(PutOutputRequest::getOutput).map(OutputBO::getId).collect(Collectors.toList());
				List<OutputBO> needModifyOutputs = oriOutputs.stream().filter(o->needModifyOutIds.contains(o.getId())).collect(Collectors.toList());
				if (!needModifyOutputs.isEmpty()) {
					capacityService.modifyOutputById(capacityIp, taskSetVO.getModify_output().get(i));
					oriOutputs.removeAll(needModifyOutputs);
					oriOutputs.addAll(taskSetVO.getModify_output().stream().map(PutOutputRequest::getOutput).collect(Collectors.toList()));
					taskOutputPO.setOutput(JSON.toJSONString(oriOutputs));
				}else{
					LOG.warn("modify output not exist");
				}
			}
		}
		//删除输出
		if (Objects.nonNull(taskSetVO.getDelete_output()) && StringUtils.isNotBlank(taskOutputPO.getOutput())) {
			List<OutputBO> oriOutputs = JSONObject.parseArray(taskOutputPO.getOutput(), OutputBO.class);
			List<String> needDeleteOutIds = taskSetVO.getDelete_output().getOutput_array().stream().map(IdRequest::getId).collect(Collectors.toList());
			List<OutputBO> needDelOutputs = oriOutputs.stream().filter(o->needDeleteOutIds.contains(o.getId())).collect(Collectors.toList());
			if (!needDeleteOutIds.isEmpty()) {
				capacityService.deleteOutputsWithMsgId(taskSetVO.getDelete_output(), capacityIp);
				oriOutputs.removeAll(needDelOutputs);
				taskOutputPO.setOutput(JSON.toJSONString(oriOutputs));
			}else{
				LOG.warn("delete output not exist");
			}
		}

		taskOutputDao.save(taskOutputPO);
	}

    /**
     * 获取设备支持的硬件平台<br/>
     * <b>作者:</b>yzx<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年6月23日 下午2:41:38
     * @param String ip 转换模块ip
     */
    public String getPlatform(String ip) throws Exception {
        PlatformResponse platformResponse = capacityService.getPlatforms(ip);
        List<String> platforms = new ArrayList<>();
        platformResponse.getPlatform_array().stream().forEach(p->{
            JSONObject jsonObject = JSONObject.parseObject(p);
            platforms.add(jsonObject.getString("platform"));
        });
		return JSON.toJSONString(platforms);
    }

	public String analysisStream(AnalysisStreamVO asVO) throws Exception {
    	if (asVO == null){
    		return "";
		}
    	String responseBody = "";
		TaskInputPO anaInputPO = null;
		List<TaskInputPO> inputPOS = taskInputDao.findByType(BusinessType.TRANSCODE);
		for (int i = 0; i < inputPOS.size(); i++) {
			TaskInputPO inputPO = inputPOS.get(i);
			InputBO inputBO = JSONObject.parseObject(inputPO.getInput(), InputBO.class);
			if (inputBO.getId().equals(asVO.getInputId())) {
				anaInputPO = inputPO;
				break;
			}
		}
		if (anaInputPO == null){
			throw new Exception("input not exist");
		}
		if ("start".equals(asVO.getType())){
			responseBody = capacityService.startAnalysisStreamToTransform(anaInputPO.getCapacityIp(), asVO.getInputId());
		}else if ("delete".equals(asVO.getType())){
			responseBody = capacityService.deleteAnalysisStreamToTransform(anaInputPO.getCapacityIp(), asVO.getInputId());
		}else if ("get".equals(asVO.getType())){
			responseBody = capacityService.getAnalysisStreamToTransform(anaInputPO.getCapacityIp(), asVO.getInputId());
		}else{
    		throw new Exception("not support type: "+ asVO.getType());
		}

		return responseBody;
	}

 	public void modifyTranscodeInput(InputSetVO inputSetVO) throws Exception {
		String capacityIp = inputSetVO.getDevice_ip();

		List<TaskInputPO> taskInputPOS = taskInputDao.findByTypeAndCapacityIp(BusinessType.TRANSCODE,capacityIp);
		if (taskInputPOS ==null || !taskInputPOS.isEmpty()){
			LOG.warn("modify input, cannot find input");
			return;
		}



		//修改视音频参数
		modifyElements(inputSetVO,taskInputPOS,capacityIp);
	}
	/**
	 * 修改输入备份关系
	 * @param inputSetVO
	 * @param taskInputPOS
	 * @param capacityIp
	 * @throws Exception
	 */
	public void modifyBackupMode(TaskSetVO inputSetVO,List<TaskInputPO> taskInputPOS,String capacityIp) throws Exception {
		if (Objects.nonNull(inputSetVO.getModify_backup_mode())&& !inputSetVO.getModify_backup_mode().isEmpty()) {
			for (int i = 0; i < inputSetVO.getModify_backup_mode().size(); i++) {
				PutBackupModeRequest putBackupModeRequest = inputSetVO.getModify_backup_mode().get(i);
				for (int i1 = 0; i1 < taskInputPOS.size(); i1++) {
					TaskInputPO inputPO = taskInputPOS.get(i1);
					InputBO inputBO = JSONObject.parseObject(inputPO.getInput(),InputBO.class);
					if (inputBO.getId().equals(putBackupModeRequest.getInputId())){
						if (inputBO.getBack_up_passby() != null) {
							inputBO.getBack_up_passby().setMode(putBackupModeRequest.getMode());
							inputBO.getBack_up_passby().setSelect_index(putBackupModeRequest.getSelect_index());
						}
						if (inputBO.getBack_up_es() != null) {
							inputBO.getBack_up_es().setMode(putBackupModeRequest.getMode());
							inputBO.getBack_up_es().setSelect_index(putBackupModeRequest.getSelect_index());
						}
						if (inputBO.getBack_up_raw() != null) {
							inputBO.getBack_up_raw().setMode(putBackupModeRequest.getMode());
							inputBO.getBack_up_raw().setSelect_index(putBackupModeRequest.getSelect_index());
						}
						updateInputToDB(inputBO,BusinessType.TRANSCODE);
						capacityService.changeBackup(capacityIp, putBackupModeRequest);
					}
				}
			}
		}
	}

	/**
	 * 修改指定节目媒体参数
	 * @param taskSetVO
	 * @param taskInputPOS 已存输入数据
	 * @param capacityIp 转换模块IP
	 * @throws Exception
	 */
    public void modifyElements(InputSetVO inputSetVO,List<TaskInputPO> taskInputPOS,String capacityIp) throws Exception {
		if (Objects.nonNull(inputSetVO.getModify_program_param()) && !inputSetVO.getModify_program_param().isEmpty()){
			for (int i = 0; i < inputSetVO.getModify_program_param().size(); i++) {
				PutElementsRequest putElementsRequest = inputSetVO.getModify_program_param().get(i);
				ModifyElementBO modifyElementBO = putElementsRequest.getParam();
				for (int j = 0; j < taskInputPOS.size(); j++) {
					TaskInputPO inputPO = taskInputPOS.get(j);
					InputBO oriInput = JSONObject.parseObject(inputPO.getInput(),InputBO.class );
					if (!oriInput.getId().equals(putElementsRequest.getInput_id())){
						continue;
					}
					for (int k = 0; k < oriInput.getProgram_array().size(); k++) {
						ProgramBO programBO = oriInput.getProgram_array().get(k);
						if (!programBO.getProgram_number().equals(putElementsRequest.getProgram_num())) {
							continue;
						}
						for (int x = 0; x < programBO.getVideo_array().size(); x++) {
							ProgramVideoBO programVideoBO = programBO.getVideo_array().get(x);
							if (!programVideoBO.getPid().equals(putElementsRequest.getPid())){
								continue;
							}
							if (!Strings.isNullOrEmpty(modifyElementBO.getBackup_mode())) {
								programVideoBO.setBackup_mode(modifyElementBO.getBackup_mode());
							}
							if (modifyElementBO.getCutoff_time() != null) {
								programVideoBO.setCutoff_time(modifyElementBO.getCutoff_time());
							}
							if (!Strings.isNullOrEmpty(modifyElementBO.getDecode_mode())) {
								programVideoBO.setDecode_mode(modifyElementBO.getDecode_mode());
							}
							if (!Strings.isNullOrEmpty(modifyElementBO.getDeinterlace_mode())){
								programVideoBO.setDeinterlace_mode(modifyElementBO.getDeinterlace_mode());
							}
							if (modifyElementBO.getNv_card_idx()!=null){
								programVideoBO.setNv_card_idx(modifyElementBO.getNv_card_idx());
							}
							if (!Strings.isNullOrEmpty(modifyElementBO.getPattern_path())) {
								programVideoBO.setPattern_path(modifyElementBO.getPattern_path());
							}
							updateInputToDB(oriInput,BusinessType.TRANSCODE);
							capacityService.modifyProgramParamToTransform(capacityIp,putElementsRequest);
							break;
						}
						for (int y = 0; y < programBO.getVideo_array().size(); y++){
							ProgramAudioBO programAudioBO = programBO.getAudio_array().get(y);
							if (!programAudioBO.getPid().equals(putElementsRequest.getPid())) {
								continue;
							}
							if (!Strings.isNullOrEmpty(modifyElementBO.getDecode_mode())){
								programAudioBO.setDecode_mode(modifyElementBO.getDecode_mode());
							}
							updateInputToDB(oriInput,BusinessType.TRANSCODE);
							capacityService.modifyProgramParamToTransform(capacityIp,putElementsRequest);
							break;
						}
						//todo 字幕不管
					}

				}
			}
		}
	}

}
