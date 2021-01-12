package com.sumavision.tetris.business.transcode.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.alarm.service.AlarmService;
import com.sumavision.tetris.application.preview.PreviewDAO;
import com.sumavision.tetris.application.preview.PreviewPO;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.Util.NodeUtil;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.business.common.service.TaskService;
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
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

	@Autowired
	private TaskService taskService;

	@Autowired
	private SyncService syncService;

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private PreviewDAO previewDao;

	@Autowired
	private NodeUtil nodeUtil;

	@Value("${constant.default.audio.column:0}")
	private Integer audioColumn;

	@Value("${constant.default.video.fps: -1}")
	private String fps;

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

	/**
	 * 判断是否需要转发流
	 * @param previewDevice
	 * @param receiveDevice
	 * @param inputBO
	 * @return
	 */
	public boolean beTransferStream(String previewDevice,String receiveDevice,InputBO inputBO){
		if (receiveDevice==null || previewDevice.equals(receiveDevice)){
			return false;
		}else {
			if (inputBO.getUdp_ts() != null && !CommonUtil.isMulticast(inputBO.getUdp_ts().getSource_ip())) { //udp单播 转发
				return true;
			}
			if (inputBO.getRtp_ts() != null && !CommonUtil.isMulticast(inputBO.getRtp_ts().getSource_ip())) { //rtp单播 转发
				return true;
			}
		}
		return false;
	}

	public String transferStream(InputBO inputBO,String previewDevice,String receiveDevice) throws Exception {
		//转发
		LOG.info("start to transfer stream");
		String transJobId = UUID.randomUUID().toString();
		ProtocolType inType = ProtocolType.getProtocolType(inputBO);
		String inUrl = nodeUtil.getUrl(inputBO);
		String outUrl = "";
		Integer outPort = nodeUtil.getPortByDevice(previewDevice);
		if (outPort==null){
			throw new BaseException(StatusCode.FORBIDDEN,"no port for transfer stream");
		}
		if (inputBO.getUdp_ts()!=null){
			outUrl = "udp://"+previewDevice+":"+outPort;
			inputBO.getUdp_ts().setSource_ip(previewDevice);
			inputBO.getUdp_ts().setSource_port(outPort);
			inputBO.getUdp_ts().setLocal_ip(previewDevice);
		}
		if (inputBO.getRtp_ts()!=null){
			outUrl = "rtp://"+previewDevice+":"+outPort;
			inputBO.getRtp_ts().setSource_ip(previewDevice);
			inputBO.getRtp_ts().setSource_port(outPort);
			inputBO.getRtp_ts().setLocal_ip(previewDevice);
		}
		taskService.transferStream(receiveDevice,transJobId, inType, inUrl,null, inType, outUrl,BusinessType.TRANSCODE);
		LOG.info("transfer stream completed");
		return transJobId;
	}




	public void createPreviewForInput(InputPreviewVO inputPreviewVO) throws Exception {

		PreviewPO previewPO = new PreviewPO();
		InputBO inputBO = inputPreviewVO.getInput_array().get(0);

		String transferJobId ="";
		if (beTransferStream(inputPreviewVO.getDevice_ip(), inputPreviewVO.getReceive_stream_device(),inputBO)) {
			transferJobId = transferStream(inputBO, inputPreviewVO.getDevice_ip(), inputPreviewVO.getReceive_stream_device());
			previewPO.setTransferTaskId(transferJobId);
		}

		if (inputPreviewVO.getProgram_number() == null){
			throw new BaseException(StatusCode.ERROR,"param[program number] not exist");
		}

		String pubName = inputBO.getId();
		String playName = inputBO.getId();
		previewPO.setInputId(inputBO.getId());
		String taskId = UUID.randomUUID().toString();

		String uniq = taskService.generateUniq(inputBO);
		TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
		if (inputPO!=null && inputPO.getCount()>0){ //输入存在的话就将现有输入进行替换下，以免input_id不一致
			inputBO = JSONObject.parseObject(inputPO.getInput(),InputBO.class);
			inputPreviewVO.getInput_array().remove(0);
			inputPreviewVO.getInput_array().add(inputBO);
		}
		ProgramBO taskProgramBO = inputBO.getProgram_array().stream()
				.filter(p-> inputPreviewVO.getProgram_number().equals(p.getProgram_number()))
				.findFirst()
				.get();
		if (taskProgramBO == null) {
			throw new BaseException(StatusCode.ERROR,"cannot find program, program number: "+ inputPreviewVO.getProgram_number());
		}

		fps=StringUtils.deleteWhitespace(fps);
		if (fps.equals("-1")){
			fps = taskProgramBO.getVideo_array().get(0).getFps();
		}

		if (audioColumn>0) {
			taskProgramBO.getAudio_array().get(0).setAudio_column("on");
		}

		List<TaskBO> taskBOs = trans2TaskBO(taskId, inputBO, fps, inputPreviewVO.getProgram_number());

//		List<OutputBO> outputBOs = trans2HLSOutputBO(taskId, taskBOs, pubName, playName);
		String outputUrl = new StringBuilder().append("rtmp://")
				.append(inputPreviewVO.getDevice_ip())
				.append(":1935")
				.append("/")
				.append("live")
				.append("/")
				.append(playName)
				.toString();
		List<OutputBO> outputBOs = trans2RTMPOutputBO(taskId, taskBOs, outputUrl);

		try {
			save(taskId, inputPreviewVO.getDevice_ip(), inputPreviewVO.getInput_array(), taskBOs, outputBOs, BusinessType.TRANSCODE);
		} catch (Exception e) {
			if (transferJobId != null && !transferJobId.isEmpty()) {
				taskService.deleteTranscodeTask(transferJobId);
			}
			throw e;
		}

		previewPO.setPreviewTaskId(taskId);
		previewPO.setUpdateTime(new Date());
		previewDao.save(previewPO);

	}

	public List<TaskBO> trans2TaskBO(
			String taskId,
			InputBO input,
			String fps,
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

		List<TaskBO> tasks = new ArrayList();

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
		if(audioColumn>0) {
			AudioColumnBO audioColumnBO = new AudioColumnBO().setPlayflag("on").setPlat("cpu");
			PreProcessingBO audioColumnProcessing = new PreProcessingBO().setAudiocolumn(audioColumnBO);
			videoEncode.getProcess_array().add(audioColumnProcessing);
		}

		PreProcessingBO scaleProcessing = new PreProcessingBO().setScale(scale);
		videoEncode.getProcess_array().add(scaleProcessing);

		String params = templateService.getVideoEncodeMap(EncodeConstant.TplVideoEncoder.VENCODER_X264);
		JSONObject obj = JSONObject.parseObject(params);
		obj.put("bitrate",1500);
		obj.put("max_bitrate",2000);
		obj.put("rc_mode","cbr");
		obj.put("ratio","4:3");
		if (!fps.equals("0")||!fps.isEmpty()) {
			obj.put("fps", fps);
		}
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
		OutputStorageBO outputStorageBO = new OutputStorageBO().setUrl("file").setDir_name(pubName).setCan_del(true);
		List<OutputStorageBO> outputStorageBOS = new ArrayList<>();
		outputStorageBOS.add(outputStorageBO);

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
				.setStorage_array(outputStorageBOS);

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
			String uniq = taskService.generateUniq(inputBO);
			TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
			if (inputPO == null) {
				inputPO = new TaskInputPO();
				inputPO.setCreateTime(new Date());
				inputPO.setUpdateTime(inputPO.getCreateTime());
				inputPO.setUniq(uniq);
				inputPO.setType(BusinessType.TRANSCODE);
				inputPO.setInput(JSON.toJSONString(inputBO));
				inputPO.setNodeId(inputBO.getId());
				inputPO.setCapacityIp(deviceIp);
				taskInputDao.save(inputPO);
			} else if (inputPO.getCount().equals(0)) {
				inputPO.setInput(JSON.toJSONString(inputBO));
				inputPO.setNodeId(inputBO.getId());
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

//		syncService.checkAndSyncTask(taskUuid,BusinessType.TRANSCODE);

		String capacityIp = transcode.getDevice_ip();

		List<InputBO> inputBOs = transcode.getInput_array();
		List<TaskBO> taskBOs = transcode.getTask_array();
		List<OutputBO> outputBOs = transcode.getOutput_array();

		taskService.checkLegalBeforeCreateTask(inputBOs);
		save(taskUuid, capacityIp, inputBOs, taskBOs, outputBOs, BusinessType.TRANSCODE);

	}

	public void save(
			String taskUuid,
			String capacityIp,
			List<InputBO> inputBOs,
			List<TaskBO> taskBOs,
			List<OutputBO> outputBOs,
			BusinessType businessType) throws Exception{
		AllRequest allRequest = new AllRequest();

		GetInputsResponse transInputs = capacityService.getInputs(capacityIp);
		try {
			Boolean beBackInput = beBackupInput(inputBOs);
			List<InputBO> needSendInputArray = new ArrayList<>();
			Set<Long> inputsInDB = new HashSet<>();
			for (int i = 0; i < inputBOs.size(); i++) {
				InputBO inputBO = inputBOs.get(i);
				String uniq = taskService.generateUniq(inputBO);//这判重合理不，是不是还不如直接看转换
				TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
				InputBO realInput = taskService.getTransformInput(transInputs, inputBO);
				if (inputPO == null) {
					inputPO = new TaskInputPO();
					inputPO.setCreateTime(new Date());
					inputPO.setUpdateTime(inputPO.getCreateTime());
					inputPO.setUniq(uniq);
					inputPO.setType(businessType);
					inputPO.setInput(JSON.toJSONString(inputBO));
					inputPO.setNodeId(inputBO.getId());
					inputPO.setUrl(nodeUtil.getUrl(inputBO));
					inputPO.setCapacityIp(capacityIp);
					taskInputDao.save(inputPO);
					if (realInput==null) {
						needSendInputArray.add(inputBO);
					}else{
						inputPO.setInput(JSON.toJSONString(realInput));
						inputPO.setNodeId(realInput.getId());//如果改了ID，task里也得改的
						taskBOs.stream().forEach(t->{
							if (!beBackInput && t.getPassby_source() != null) { t.getPassby_source().setInput_id(realInput.getId()); }
							if (!beBackInput && t.getEs_source()!=null){ t.getEs_source().setInput_id(realInput.getId()); }
							if (!beBackInput && t.getRaw_source()!=null){ t.getRaw_source().setInput_id(realInput.getId()); }
						});
					}
				} else if (inputPO.getCount().equals(0)) {
					inputPO.setInput(JSON.toJSONString(inputBO));
					inputPO.setNodeId(inputBO.getId());
					inputPO.setType(businessType);
					inputPO.setCreateTime(new Date());
					inputPO.setUpdateTime(inputPO.getCreateTime());
					inputPO.setUrl(nodeUtil.getUrl(inputBO));
					inputPO.setCount(inputPO.getCount() + 1);
					inputPO.setCapacityIp(capacityIp);
					taskInputDao.save(inputPO);
					if (realInput==null) {
						needSendInputArray.add(inputBO);
					}else{
						inputPO.setInput(JSON.toJSONString(realInput));
						inputPO.setNodeId(realInput.getId());
						taskBOs.stream().forEach(t->{
							if (!beBackInput && t.getPassby_source() != null) { t.getPassby_source().setInput_id(realInput.getId()); }
							if (!beBackInput && t.getEs_source()!=null){ t.getEs_source().setInput_id(realInput.getId()); }
							if (!beBackInput && t.getRaw_source()!=null){ t.getRaw_source().setInput_id(realInput.getId()); }
						});
					}
				} else {
					inputPO.setUpdateTime(inputPO.getCreateTime());
					inputPO.setCapacityIp(capacityIp);
					inputPO.setCount(inputPO.getCount() + 1);
					taskInputDao.save(inputPO);
					if (realInput==null){
						needSendInputArray.add(inputBO);
					}
					//真的重复了，一定要验下task里的inputid对不对
					String inputJsonStr = inputPO.getInput();
					InputBO curInputBO = JSONObject.parseObject(inputJsonStr, InputBO.class);
					taskBOs.stream().forEach(t->{
						if (t.getPassby_source() != null) {
							if (curInputBO.getBack_up_passby()!=null) {
								t.getPassby_source().setInput_id(curInputBO.getId());
							}else{
								t.getPassby_source().setInput_id(curInputBO.getId());
							}
						}
						if (t.getEs_source()!=null){
							if (curInputBO.getBack_up_es()!=null) {
								t.getEs_source().setInput_id(curInputBO.getId());
							}else{
								t.getEs_source().setInput_id(curInputBO.getId());
							}
						}
						if (t.getRaw_source()!=null){
							if (curInputBO.getBack_up_raw()!=null) {
								t.getRaw_source().setInput_id(curInputBO.getId());
							}else{
								t.getRaw_source().setInput_id(curInputBO.getId());
							}
						}
					});
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
			LOG.warn("save校验version版本不对");
			Thread.sleep(300);
			save(taskUuid, capacityIp, inputBOs, taskBOs, outputBOs, businessType);

		} catch (DataIntegrityViolationException e){
			LOG.warn("唯一性约束，校验输入已存在");
			Thread.sleep(300);
			save(taskUuid, capacityIp, inputBOs, taskBOs, outputBOs, businessType);
		} catch (ConstraintViolationException e) {
			//数据已存在（ip，port校验）
			LOG.warn("校验输入已存在");
			Thread.sleep(300);
			save(taskUuid, capacityIp, inputBOs, taskBOs, outputBOs, businessType);
		} catch (BaseException e){
			capacityService.deleteAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
//			taskService.delete(taskUuid,BusinessType.TRANSCODE);
			throw e;
		} catch (Exception e) {

			if(!(e instanceof ConstraintViolationException)){
				throw e;
			}

		}
	}


	public Boolean beBackupInput(List<InputBO> inputBOS){
		return inputBOS.stream().anyMatch(i->i.getBack_up_raw()!=null || i.getBack_up_es()!=null || i.getBack_up_passby()!=null);
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
				input.setNodeId(inputBO.getId());
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
				LOG.warn("校验输入已存在");
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
					input.setNodeId(inputBO.getId());
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
				LOG.warn("save校验version版本不对");
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
	 * 删除所有任务
	 */
	public void deleteAll(){
		List<TaskOutputPO> taskOutputPOS = taskOutputDao.findAll();
		for (int i = 0; i < taskOutputPOS.size(); i++) {
			TaskOutputPO taskOutputPO =	taskOutputPOS.get(i);
			if (taskOutputPO.getTaskUuid()!=null){
				try {
					taskService.delete(taskOutputPO.getTaskUuid(),taskOutputPO.getType());
				} catch (Exception e) {
					LOG.error("task delete fail, taskUuid: {}",taskOutputPO.getTaskUuid());
					e.printStackTrace();
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
		return  taskService.delete(taskUuid,BusinessType.TRANSCODE);
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
				input.setNodeId(inputBO.getId());
				input.setType(BusinessType.TRANSCODE);
				taskInputDao.save(input);
			} catch (ConstraintViolationException e) {
				//数据已存在（ip，port校验）
				LOG.warn("校验输入已存在");
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
					input.setNodeId(inputBO.getId());
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
				LOG.warn("save校验version版本不对");
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

			if(output.getCoverId() != null){
				throw new IllegalArgumentException("该任务的盖播已存在");
			}

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
						JSONObject inputObj = JSON.parseObject(inputPO.getInput());
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

				InputBO exsitInputBO = JSON.parseObject(taskInput.getInput(), InputBO.class);

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
				List<TaskBO> tasks = JSON.parseArray(output.getTask(), TaskBO.class);
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

			if(output.getCoverId() == null){
				throw new IllegalArgumentException("盖播不存在！");
			}

			TaskInputPO cover = taskInputDao.findOne(output.getCoverId());

			if(cover != null){

				InputBO coverInputBO = JSON.parseObject(cover.getInput(), InputBO.class);

				String replaceInputId = coverInputBO.getCover().getProgram_array().iterator().next().getInput_id();

				//替换task中inputId
				List<TaskBO> tasks = JSON.parseArray(output.getTask(), TaskBO.class);
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

					AllRequest allRequest = new AllRequest();
					if (!taskService.beUseForInputWithoutTask(cover.getId(),taskId)) {
						cover.setUpdateTime(new Date());
						cover.setCount(0);
						InputBO inputBO = JSON.parseObject(cover.getInput(), InputBO.class);
						allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());
					}else{
						cover.setCount(cover.getCount() - 1);
					}
					capacityService.deleteAllAddMsgId(allRequest, output.getCapacityIp(), capacityProps.getPort());
					taskInputDao.save(cover);
					output.setCoverId(null);
					taskOutputDao.save(output);
				} catch (ObjectOptimisticLockingFailureException e) {

					// 版本不对，version校验
					LOG.warn("delete校验version版本不对",e);
					Thread.sleep(300);
					delete(taskId);
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
			throw new IllegalArgumentException("任务不存在在！");
		}

		CreateOutputsRequest outputsRequest = new CreateOutputsRequest();
		outputsRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputs).getList());
		//创建输出
		CreateOutputsResponse outputResponse = capacityService.createOutputsWithMsgId(outputsRequest, task.getCapacityIp());

		//创建输出返回处理 -- 回滚
		responseService.outputResponseProcess(outputResponse, null, null, task.getCapacityIp());

		List<OutputBO> oriOutputs = JSON.parseArray(task.getOutput(),OutputBO.class);
		oriOutputs.addAll(outputs);

		task.setOutput(JSON.toJSONString(oriOutputs));

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
			throw new IllegalArgumentException("任务不存在！");
		}

		List<OutputBO> outputs = JSON.parseArray(taskPO.getOutput(), OutputBO.class);

		List<OutputBO> needDeleteOutputs = new ArrayList<OutputBO>();
		if(outputs != null && outputs.size() > 0){
			for(OutputBO outputBO: outputs){
				if(outputBO.getId().equals(outputId.toString())){
					needDeleteOutputs.add(outputBO);
				}
			}
		}

		if(outputs !=null && needDeleteOutputs.size() > 0){

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
			JSONObject jsonObject = JSON.parseObject(p);
			platforms.add(jsonObject.getString("platform"));
		});
		return JSON.toJSONString(platforms);
	}

	/**
	 * 码流分析
	 * @param asVO
	 * @param busType
	 * @return
	 * @throws Exception
	 */
	public String analysisStream(AnalysisStreamVO asVO,BusinessType busType) throws Exception {
		if (asVO == null){
			return "";
		}
		String responseBody = "";
		if ("start".equals(asVO.getType())){
			taskService.addInputsAfterRepeat(asVO.getDeviceIp(),asVO.getInput_array(),busType);
			String uniq = taskService.generateUniq(asVO.getInput_array().get(0));
			TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
			responseBody = capacityService.startAnalysisStreamToTransform(asVO.getDeviceIp(),inputPO.getNodeId());
			taskInputDao.updateAnalysisById(inputPO.getId(),1);//开启分析
		}else if ("delete".equals(asVO.getType())){
			String uniq = taskService.generateUniq(asVO.getInput_array().get(0));
			TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
			if (inputPO!=null){
				responseBody = capacityService.deleteAnalysisStreamToTransform(asVO.getDeviceIp(), inputPO.getNodeId());
				taskInputDao.updateAnalysisById(inputPO.getId(),0);//关掉分析
			}
			taskService.deleteInputsAfterCheckRepeat(asVO.getDeviceIp(),asVO.getInput_array());
		}else if ("get".equals(asVO.getType())){
			String uniq = taskService.generateUniq(asVO.getInput_array().get(0));
			TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
			responseBody = capacityService.getAnalysisStreamToTransform(asVO.getDeviceIp(), inputPO.getNodeId());
		}else{
			throw new IllegalArgumentException("not support type: "+ asVO.getType());
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
					InputBO oriInput = JSON.parseObject(inputPO.getInput(),InputBO.class );
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
							taskService.updateInputToDB(oriInput,BusinessType.TRANSCODE);
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
							taskService.updateInputToDB(oriInput,BusinessType.TRANSCODE);
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
