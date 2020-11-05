package com.sumavision.tetris.business.push.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.capacity.bo.request.*;
import com.sumavision.tetris.capacity.constant.EncodeConstant;
import com.sumavision.tetris.capacity.template.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.bo.CapacityDeleteBO;
import com.sumavision.tetris.business.common.bo.InputCheckBO;
import com.sumavision.tetris.business.common.bo.InputMapBO;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.push.vo.PushFileVO;
import com.sumavision.tetris.business.push.vo.PushOutputVO;
import com.sumavision.tetris.business.push.vo.PushProgramVO;
import com.sumavision.tetris.business.push.vo.PushStreamVO;
import com.sumavision.tetris.business.push.vo.ScheduleTaskVO;
import com.sumavision.tetris.capacity.bo.input.CommonTsBO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.InputFileBO;
import com.sumavision.tetris.capacity.bo.input.InputFileObjectBO;
import com.sumavision.tetris.capacity.bo.input.InputScheduleBO;
import com.sumavision.tetris.capacity.bo.input.ProgramAudioBO;
import com.sumavision.tetris.capacity.bo.input.ProgramBO;
import com.sumavision.tetris.capacity.bo.input.ProgramElementBO;
import com.sumavision.tetris.capacity.bo.input.ProgramFileBO;
import com.sumavision.tetris.capacity.bo.input.ProgramOutputBO;
import com.sumavision.tetris.capacity.bo.input.ProgramStreamBO;
import com.sumavision.tetris.capacity.bo.input.ProgramVideoBO;
import com.sumavision.tetris.capacity.bo.input.ScheduleProgramBO;
import com.sumavision.tetris.capacity.bo.output.CommonTsOutputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.output.OutputMediaBO;
import com.sumavision.tetris.capacity.bo.output.OutputProgramBO;
import com.sumavision.tetris.capacity.bo.response.AllResponse;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.H264BO;
import com.sumavision.tetris.capacity.bo.task.PreProcessingBO;
import com.sumavision.tetris.capacity.bo.task.ResampleBO;
import com.sumavision.tetris.capacity.bo.task.ScaleBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.bo.task.TaskSourceBO;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.enumeration.InputResponseEnum;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.capacity.service.ResponseService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class ScheduleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

	public static final String SCHEDULE = "schedule";
	
	@Autowired
	private TaskInputDAO taskInputDao;
	
	@Autowired
	private TaskOutputDAO taskOutputDao;
	
	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private CapacityProps capacityProps;
	
	@Autowired
	private ResponseService responseService;

	@Autowired
	private TemplateService templateService;

	@Value("${constant.default.video.encode:x264}")
	private String vEncodeType;

	@Value("${constant.default.video.resolution:1280x720}")
	private String vResolution;

	@Value("${constant.default.audio.encode:aac}")
	private String aEncodeType;


	/**
	 * 添加排期任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午3:51:19
	 * @param ScheduleTaskVO task 排期任务信息
	 * @return String 任务标识
	 */
	@Transactional(rollbackFor = Exception.class)
	public String addPushTask(ScheduleTaskVO task) throws Exception{
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		
		//生成排期源
		InputBO schedule = push2ScheduleInputBO(uuid, task);
		TaskInputPO input = new TaskInputPO();
		input.setUpdateTime(new Date());
		input.setUniq(new StringBufferWrapper().append(SCHEDULE)
											   .append("-")
											   .append(uuid)
											   .toString());
		input.setTaskUuid(uuid);
		input.setInput(JSON.toJSONString(schedule));
		input.setType(BusinessType.PUSH);
		taskInputDao.save(input);
		
		List<TaskBO> tasks = push2TaskBO(uuid, schedule, task);
		
		List<OutputBO> outputs = push2OutputBO(uuid, tasks, task);
		
		//需要创建的源--发协议
		List<InputBO> needCreateInputs = new ArrayList<InputBO>();
		//所有源：需要创建的源+已经存在的源--生成备份源
		List<InputBO> allInputs = new ArrayList<InputBO>();
		//所有源的持久化id--数据库持久化
		List<Long> allInputIds = new ArrayList<Long>();
		
		//file不需要check，stream需要check同源
		InputCheckBO preCheck = push2SingleInput(uuid, task.getMediaType(), task.getInput().getPrev());
		allInputs.add(preCheck.getInputBO());
		allInputIds.add(preCheck.getInputId());
		if(!preCheck.isExist()){
			needCreateInputs.add(preCheck.getInputBO());
		}
		
		InputCheckBO nextCheck = null;
		if(task.getInput().getNext() != null){
			nextCheck = push2SingleInput(uuid, task.getMediaType(), task.getInput().getNext());
			allInputs.add(nextCheck.getInputBO());
			allInputIds.add(nextCheck.getInputId());
			if(!nextCheck.isExist()){
				needCreateInputs.add(nextCheck.getInputBO());
			}
		}
	
		allInputIds.add(input.getId());
		
		List<ScheduleProgramBO> schedules = new ArrayList<ScheduleProgramBO>();
		ScheduleProgramBO preScheduleProgramBO = push2ScheduleProgram(task.getMediaType(), preCheck.getInputBO(), task.getInput().getPrev());
		schedules.add(preScheduleProgramBO);
		if(nextCheck != null){
			ScheduleProgramBO nextScheduleProgramBO = push2ScheduleProgram(task.getMediaType(), nextCheck.getInputBO(), task.getInput().getNext());
			schedules.add(nextScheduleProgramBO);
		}
		
		//发送排期input/task/output
		AllRequest request = sendProtocal(uuid, task.getDeviceIp(), null, input.getId(), new ArrayListWrapper<InputBO>().add(schedule).getList(), tasks, outputs);
		
		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(uuid, BusinessType.PUSH);
		output.setPrevId(preCheck.getInputId());
		if(nextCheck != null){
			output.setNextId(nextCheck.getInputId());
		}
		
		taskOutputDao.save(output);
		
		try {
			sendSchedule(task.getDeviceIp(), schedule.getId(), null, needCreateInputs, schedules);
		} catch (Exception e) {
			
			capacityService.deleteAllAddMsgId(request, task.getDeviceIp(), capacityProps.getPort());
			throw e;
		}
		
		return uuid;
	}
	
	/**
	 * push生成单源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午2:29:59
	 * @param String taskId 任务标识
	 * @param String mediaType 媒体类型
	 * @param PushProgramVO pushInput push输入
	 * @return InputCheckBO
	 */
	public InputCheckBO push2SingleInput(
			String taskId, 
			String mediaType, 
			PushProgramVO pushInput) throws Exception{
		
		InputCheckBO check = new InputCheckBO();
		
		if(pushInput.getType().equals("stream")){
			
			String uniq = "";
			if(pushInput.getStream().getPcm().equals("udp")){
				//udp_ts			
				String sourceIp = pushInput.getStream().getUrl().split("@")[1].split(":")[0];
				
				int sourcePort = Integer.valueOf(pushInput.getStream().getUrl().split("@")[1].split(":")[1]).intValue();
				
				uniq = new StringBufferWrapper().append(sourceIp)
											    .append("@")
											    .append(sourcePort)
											    .toString();
			}
			
			TaskInputPO input = taskInputDao.findTopByUniq(uniq);
			boolean isExist = false;
			InputBO inputBO = new InputBO();
			if(input == null){
					
				inputBO = push2StreamInput(taskId, mediaType, pushInput);
				
				input = new TaskInputPO();
				input.setUpdateTime(new Date());
				input.setUniq(uniq);
				input.setTaskUuid(taskId);
				input.setInput(JSON.toJSONString(inputBO));
				input.setType(BusinessType.PUSH);
				taskInputDao.save(input);
					
			}else{

				if(input.getCount().equals(0)){
					inputBO = push2StreamInput(taskId, mediaType, pushInput);
					input.setInput(JSON.toJSONString(inputBO));
					input.setTaskUuid(taskId);
					input.setType(BusinessType.PUSH);
				}else{
					inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
					isExist = true;
				}
				input.setUpdateTime(new Date());
				input.setCount(input.getCount() + 1);
				taskInputDao.save(input);
						
			}
				
			check.setInputId(input.getId());
			check.setExist(isExist);
			check.setInputBO(inputBO);
			
		}else if(pushInput.getType().equals("file")){
			
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			
			String uniq = new StringBufferWrapper().append(taskId)
												   .append("@")
												   .append("file@")
												   .append(uuid)
												   .toString();
			
			//file不需要源校验
			TaskInputPO input = new TaskInputPO();
			InputBO inputBO = new InputBO();
			inputBO = push2FileInput(uuid, taskId, mediaType, pushInput);
			
			input = new TaskInputPO();
			input.setUpdateTime(new Date());
			input.setUniq(uniq);
			input.setTaskUuid(taskId);
			input.setInput(JSON.toJSONString(inputBO));
			input.setType(BusinessType.PUSH);
			taskInputDao.save(input);
			
			check.setExist(false);
			check.setInputId(input.getId());
			check.setInputBO(inputBO);
			
		}
		
		return check;
	}
	
	public InputBO push2ScheduleInputBO(
			String taskId,
			ScheduleTaskVO task) throws Exception{
		
		String scheduleInputId = new StringBufferWrapper().append(SCHEDULE)
													  .append("-")
													  .append(taskId)
													  .toString();
		
		// 创建输入
		InputBO input = new InputBO().setId(scheduleInputId)
									 .setProgram_array(new ArrayList<ProgramBO>())
									 .setMedia_type_once_map(new JSONObject());
		
		ProgramOutputBO outPro = new ProgramOutputBO().setProgram_number(1)
													  .setElement_array(new ArrayList<ProgramElementBO>());
		if("video".equals(task.getMediaType())){
			
			ProgramElementBO velementBO = new ProgramElementBO().setType("video").setPid(513);
			ProgramElementBO aelementBO = new ProgramElementBO().setType("audio").setPid(514);

			outPro.getElement_array().add(velementBO);
			outPro.getElement_array().add(aelementBO);
			
			ProgramBO program = new ProgramBO().setProgram_number(1)
											   .setVideo_array(new ArrayList<ProgramVideoBO>())
											   .setAudio_array(new ArrayList<ProgramAudioBO>())
												.setMedia_type_once_map(new JSONObject());
//断流静帧，静音
			ProgramVideoBO video = new ProgramVideoBO().setPid(513).setBackup_mode("still_picture").setCutoff_time(400);
			ProgramAudioBO audio = new ProgramAudioBO().setPid(514).setBackup_mode("silence").setCutoff_time(400);

			program.getVideo_array().add(video);
			program.getAudio_array().add(audio);
			input.getProgram_array().add(program);
		}else if("audio".equals(task.getMediaType())){
			
			ProgramElementBO aelementBO = new ProgramElementBO().setType("audio").setPid(514);

			outPro.getElement_array().add(aelementBO);
			
			ProgramBO program = new ProgramBO().setProgram_number(1)
								   			   .setAudio_array(new ArrayList<ProgramAudioBO>())
												.setMedia_type_once_map(new JSONObject());
			
			ProgramAudioBO audio = new ProgramAudioBO().setPid(514);
			
			program.getAudio_array().add(audio);
			input.getProgram_array().add(program);
		}
		
		InputScheduleBO schedule = new InputScheduleBO().setStream_type("raw")
													    .setOutput_program(outPro);
		input.setSchedule(schedule);
		
		return input;
		
	}
	
	/**
	 * 生成排期节目协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月6日 下午2:39:31
	 * @param String mediaType 媒体类型
	 * @param InputBO input 排期输入input
	 * @param PushProgramVO program 节目信息
	 * @return ScheduleProgramBO 排期节目
	 */
	public ScheduleProgramBO push2ScheduleProgram(
			String mediaType,
			InputBO input,
			PushProgramVO program) throws Exception{
		
		ScheduleProgramBO scheduleProgram = new ScheduleProgramBO();
		scheduleProgram.setInput_id(input.getId())
					   .setProgram_number(1)
					   .setElement_array(new ArrayList<ProgramElementBO>());
		
		if("video".equals(mediaType)){
			
			ProgramElementBO velementBO = new ProgramElementBO().setType("video").setPid(513);
			ProgramElementBO aelementBO = new ProgramElementBO().setType("audio").setPid(514);

			scheduleProgram.getElement_array().add(velementBO);
			scheduleProgram.getElement_array().add(aelementBO);
		}else if("audio".equals(mediaType)){
			
			ProgramElementBO aelementBO = new ProgramElementBO().setType("audio").setPid(514);
			
			scheduleProgram.getElement_array().add(aelementBO);
		}
		
		if("file".equals(program.getType())){
			ProgramFileBO file = new ProgramFileBO().setStart_ms(program.getFile().getSeek().intValue())
											        .setDuration(program.getFile().getDuration().intValue())
											        .setStart_time(program.getFile().getStartTime());
			
			scheduleProgram.setFile(file);
		}else if("stream".equals(program.getType())){
			ProgramStreamBO stream = new ProgramStreamBO().setStart_time(program.getStream().getStartTime())
														  .setEnd_time(program.getStream().getEndTime());
			scheduleProgram.setLive(stream);
		}
		
		return scheduleProgram;
		
	}
	
	/**
	 * push生成文件源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 上午11:28:03
	 * @param String taskId 任务id
	 * @param String mediaType 媒体类型
	 * @param PushInputVO pushInput push输入信息
	 * @return InputBO 输入
	 */
	public InputBO push2FileInput(
			String uuid,
			String taskId, 
			String mediaType, 
			PushProgramVO pushInput) throws Exception{
		
		String inputId = new StringBufferWrapper().append("input-")
												  .append(taskId)
												  .append("-file-")
												  .append(uuid)
												  .toString();
		
		InputBO inputBO = new InputBO();
		inputBO.setId(inputId)
		       .setMedia_type_once_map(new JSONObject())
		       .setProgram_array(new ArrayList<ProgramBO>());
		
		PushFileVO fileVO = pushInput.getFile();
		InputFileBO file = new InputFileBO().setFile_array(new ArrayList<InputFileObjectBO>());

		InputFileObjectBO fileObject = new InputFileObjectBO().setUrl(fileVO.getUrl())
															  .setLoop_count(fileVO.getCount().intValue())
															  .setStart_ms(fileVO.getSeek().intValue())
															  .setDuration(fileVO.getDuration().intValue());
		
		file.getFile_array().add(fileObject);
		
		inputBO.setFile(file);
		
		if(mediaType.equals("video")){
			ProgramBO program = new ProgramBO().setProgram_number(1)
										       .setVideo_array(new ArrayList<ProgramVideoBO>())
										       .setAudio_array(new ArrayList<ProgramAudioBO>())
												.setMedia_type_once_map(new JSONObject());

			ProgramVideoBO video = new ProgramVideoBO().setPid(513)
						   							   .setDecode_mode("cpu");
			ProgramAudioBO audio = new ProgramAudioBO().setPid(514)
													   .setDecode_mode("cpu");
			
			program.getVideo_array().add(video);
			program.getAudio_array().add(audio);
			
			inputBO.getProgram_array().add(program);
		}
		
		if(mediaType.equals("audio")){
			ProgramBO program = new ProgramBO().setProgram_number(1)
										       .setAudio_array(new ArrayList<ProgramAudioBO>())
												.setMedia_type_once_map(new JSONObject());

			ProgramAudioBO audio = new ProgramAudioBO().setPid(514)
													   .setDecode_mode("cpu");
			
			program.getAudio_array().add(audio);
			
			inputBO.getProgram_array().add(program);
		}
		
		return inputBO;
	}
	
	/**
	 * push生成流源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午1:29:44
	 * @param String taskId 任务id
	 * @param String mediaType 媒体类型
	 * @param PushInputVO pushInput push输入信息
	 * @return InputBO 输入
	 */
	public InputBO push2StreamInput(
			String taskId, 
			String mediaType, 
			PushProgramVO pushInput) throws Exception{
		
		

		InputBO inputBO = new InputBO();
		inputBO.setMedia_type_once_map(new JSONObject())
			   .setProgram_array(new ArrayList<ProgramBO>());
		
		PushStreamVO stream = pushInput.getStream();
		
		if(stream.getPcm().equals("udp")){
			
			//udp_ts			
			String sourceIp = stream.getUrl().split("@")[1].split(":")[0];
			
			int sourcePort = Integer.valueOf(stream.getUrl().split("@")[1].split(":")[1]).intValue();
			
			String inputId = new StringBufferWrapper().append("input-")
													  .append(taskId)
													  .append("-stream-")
													  .append(sourceIp)
													  .append("-")
													  .append(sourcePort)
													  .toString();
			
			CommonTsBO udp_ts = new CommonTsBO().setSource_ip(sourceIp)
												.setSource_port(sourcePort);
			
			inputBO.setId(inputId)
				   .setUdp_ts(udp_ts);
		}
		
		if(mediaType.equals("video")){
			ProgramBO program = new ProgramBO().setProgram_number(1)
										       .setVideo_array(new ArrayList<ProgramVideoBO>())
										       .setAudio_array(new ArrayList<ProgramAudioBO>())
												.setMedia_type_once_map(new JSONObject());

			ProgramVideoBO video = new ProgramVideoBO().setPid(513)
						   							   .setDecode_mode("cpu");
			ProgramAudioBO audio = new ProgramAudioBO().setPid(514)
													   .setDecode_mode("cpu");
			
			program.getVideo_array().add(video);
			program.getAudio_array().add(audio);
			
			inputBO.getProgram_array().add(program);
		}
		
		if(mediaType.equals("audio")){
			ProgramBO program = new ProgramBO().setProgram_number(1)
										       .setAudio_array(new ArrayList<ProgramAudioBO>())
												.setMedia_type_once_map(new JSONObject());

			ProgramAudioBO audio = new ProgramAudioBO().setPid(514)
													   .setDecode_mode("cpu");
			
			program.getAudio_array().add(audio);
			
			inputBO.getProgram_array().add(program);
		}
		
		return inputBO;
	}
	
	/**
	 * push生成任务信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午2:29:57
	 * @param String taskId 任务id
	 * @param InputBO input 输入
	 * @param PushTaskVO push信息
	 * @return List<TaskBO> 任务
	 */
	public List<TaskBO> push2TaskBO(
			String taskId,
			InputBO input, 
			ScheduleTaskVO push) throws Exception {
		
		String videoTaskId = new StringBufferWrapper().append("task-video-")
													  .append(taskId)
													  .toString();
		String audioTaskId = new StringBufferWrapper().append("task-audio-")
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
		if(push.getMediaType().equals("video")){
			
			//视频转码
			TaskSourceBO videoSource = new TaskSourceBO().setInput_id(input.getId())
														 .setProgram_number(input.getProgram_array().get(0).getProgram_number())
														 .setElement_pid(input.getProgram_array().get(0).getVideo_array().get(0).getPid());

			TaskBO videoTask = new TaskBO().setId(videoTaskId)
										   .setType("video")
										   .setRaw_source(videoSource)
										   .setEncode_array(new ArrayList<EncodeBO>());
			
			EncodeBO videoEncode = new EncodeBO().setEncode_id(encodeVideoId)
												 .setProcess_array(new ArrayList<PreProcessingBO>());
			
			//scale
			ScaleBO scale = new ScaleBO().setPlat("cpu")
										 
										 .setWidth(Integer.valueOf(vResolution.split("x")[0]))
										 .setHeight(Integer.valueOf(vResolution.split("x")[1]));
			
			PreProcessingBO preProcessing = new PreProcessingBO().setScale(scale);
			videoEncode.getProcess_array().add(preProcessing);
			

			EncodeConstant.TplVideoEncoder videoEncoder = EncodeConstant.TplVideoEncoder.getTplVideoEncoder(vEncodeType);
			String params = templateService.getVideoEncodeMap(videoEncoder);
			JSONObject obj = JSONObject.parseObject(params);
			
			obj.put("bitrate",2300);
			obj.put("max_bitrate",2300);
			obj.put("rc_mode","cbr");
			obj.put("ratio","16:9");
			
			obj.put("resolution",vResolution);

			
			EncodeConstant.VideoType type = EncodeConstant.VideoType.getVideoType(videoEncoder);
			switch (type) {
				case h264:
					videoEncode.setH264(obj);
					break;
				case h265:
					videoEncode.setHevc(obj);
					break;
				case mpeg2:
					videoEncode.setMpeg2(obj);
					break;
				default:
					throw new CommonException("video encode type not support, type: "+type.name());
			}
			
			videoTask.getEncode_array().add(videoEncode);
			
			tasks.add(videoTask);
		}
		
		/*******
		 * 音频 *
		 *******/
		if(push.getMediaType().equals("video") || push.getMediaType().equals("audio")){
			
			//音频转码
			TaskSourceBO audioSource = new TaskSourceBO().setInput_id(input.getId())
					 									 .setProgram_number(input.getProgram_array().get(0).getProgram_number())
					 									 .setElement_pid(input.getProgram_array().get(0).getAudio_array().get(0).getPid());

			TaskBO audioTask = new TaskBO().setId(audioTaskId)
										   .setType("audio")
										   .setRaw_source(audioSource)
										   .setEncode_array(new ArrayList<EncodeBO>());

			EncodeBO audioEncode = new EncodeBO().setEncode_id(encodeAudioId);

			
			String aMap = templateService.getAudioEncodeMap(aEncodeType);
			JSONObject aObj = JSONObject.parseObject(aMap);


			EncodeConstant.TplAudioEncoder audioEncoder = EncodeConstant.TplAudioEncoder.getTplAudioEncoder(aEncodeType);
			EncodeConstant.AudioType audioType = EncodeConstant.AudioType.getAudioType(audioEncoder);

			switch (audioType){
				case aac:
					audioEncode.setAac(aObj);
					break;
				case dolby:
					audioEncode.setDolby(aObj);
					break;
				case mp2:
					audioEncode.setMp2(aObj);
					break;
				case mp3:
					audioEncode.setMp3(aObj);
					break;
				default:
					throw new CommonException("audio encode type not support, type: "+audioType.name());
			}

			
		 
			audioEncode.setProcess_array(new ArrayList());
			
			 
			ResampleBO resample = new ResampleBO().setSample_rate(Float.valueOf(aObj.getFloat("sample_rate")*1000).intValue())
													.setChannel_layout(aObj.getString("channel_layout"))
													.setFormat(aObj.getString("sample_fmt"));

			PreProcessingBO audio_decode_processing = new PreProcessingBO().setResample(resample);
			audioEncode.getProcess_array().add(audio_decode_processing);
			
			audioTask.getEncode_array().add(audioEncode);
			
			tasks.add(audioTask);
			
		}

		return tasks;
	}
	
	/**
	 * push生成输出信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午1:41:32
	 * @param String taskId 任务id
	 * @param List<TaskBO> tasks 转码任务
	 * @param PushTaskVO push push信息
	 * @return List<OutputBO> 转码输出
	 */
	public List<OutputBO> push2OutputBO(
			String taskId, 
			List<TaskBO> tasks, 
			ScheduleTaskVO push) throws Exception {

		List<OutputBO> outputs = new ArrayList();
		for(PushOutputVO outputVO: push.getOutput()){
			
			OutputBO output = new OutputBO();
			
			if("udp".equals(outputVO.getType())){
				
				//udp_ts			
				String outputIp = outputVO.getUrl().split("@")[1].split(":")[0];
				
				int outputPort = Integer.valueOf(outputVO.getUrl().split("@")[1].split(":")[1]).intValue();
				
				String outputId = new StringBufferWrapper().append("output-")
														   .append(taskId)
														   .append("-")
															.append(outputVO.getLocalIp())
															.append("-")
														   .append(outputIp)
														   .append("-")
														   .append(outputPort)
														   .toString();
				
				output.setId(outputId);
				
				//输出udp
				CommonTsOutputBO udp_ts = new CommonTsOutputBO().setUdp_ts()
																.setIp(outputIp)
																.setPort(outputPort)
																.setRate_ctrl("CBR")//中广电信用的
																.setBitrate(3500000)//中广电信用的
																.setPcr_int(20)//中广电信用的
																.setLocal_ip(outputVO.getLocalIp())
																.setProgram_array(new ArrayList<OutputProgramBO>());

				//拼媒体
				List<OutputMediaBO> medias = new ArrayList<OutputMediaBO>();
				for(TaskBO taskBO: tasks){
					//视频
					if(taskBO.getId().contains("video")){
						OutputMediaBO media = new OutputMediaBO().setTask_id(taskBO.getId())
																 .setType("video")
																 .setEncode_id(taskBO.getEncode_array().iterator().next().getEncode_id())
																 .setPid(513);
						medias.add(media);
					}
					//音频
					if(taskBO.getId().contains("audio")){
						OutputMediaBO media = new OutputMediaBO().setTask_id(taskBO.getId())
																 .setType("audio")
																 .setEncode_id(taskBO.getEncode_array().iterator().next().getEncode_id())
																 .setPid(514);
						medias.add(media);
					}
				}
				
				OutputProgramBO program = new OutputProgramBO().setProgram_number(301)
															   .setPmt_pid(101)
															   .setPcr_pid(100)
															   .setMedia_array(new ArrayListWrapper<OutputMediaBO>().addAll(medias).getList());
				
				udp_ts.getProgram_array().add(program);	
				output.setUdp_ts(udp_ts);
				
				outputs.add(output);
			}
		}

		return outputs;
	}
	
	/**
	 * 发送转码协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午3:23:27
	 * @param String taskUuid 任务标识
	 * @param String capacityIp 转换模块ip
	 * @param Long inputId 单源输入id
	 * @param String inputIds 多源备份输入idList
	 * @param List<InputBO> inputBOs 输入参数
	 * @param List<TaskBO> taskBOs 任务参数
	 * @param List<OutputBO> outputBOs 输出参数
	 */
	public AllRequest sendProtocal(
			String taskUuid,
			String capacityIp,
			Long inputId, 
			Long scheduleId,
			List<InputBO> inputBOs,
			List<TaskBO> taskBOs,
			List<OutputBO> outputBOs) throws Exception{
		
		AllRequest allRequest = new AllRequest();
		try {
			
			TaskOutputPO output = new TaskOutputPO();
			output.setInputId(inputId);
			output.setScheduleId(scheduleId);
			output.setOutput(JSON.toJSONString(outputBOs));
			output.setTask(JSON.toJSONString(taskBOs));
			output.setTaskUuid(taskUuid);
			output.setType(BusinessType.PUSH);
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
			
		}
		
		return allRequest;
	}
	
	/**
	 * 发送追加排期协议br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月6日 下午2:42:11
	 * @param String deviceIp 转码模块ip
	 * @param String inputId 排期输入id
	 * @param String deleteId 需要删除的输入id
	 * @param List<InputBO> inputs 需要添加的输入
	 * @param List<ScheduleProgramBO> schedules 需要添加的排期
	 */
	public void sendSchedule(
			String deviceIp,
			String inputId,
			String deleteId,
			List<InputBO> inputs,
			List<ScheduleProgramBO> schedules) throws Exception{
		
		PutScheduleRequest request = new PutScheduleRequest();
		
		if(deleteId != null){
			JSONObject delete = new JSONObject();
			delete.put("input_id", deleteId);
			
			request.setDelete_input(delete);
		}
		
		request.setInput_array(new ArrayListWrapper<InputBO>().addAll(inputs).getList());
		ScheduleRequest scheduleRequest = new ScheduleRequest();
		scheduleRequest.setSource_array(new ArrayListWrapper<ScheduleProgramBO>().addAll(schedules).getList());
		request.setSchedule(scheduleRequest);
		
		Date date = new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        System.out.println(sdf.format(date));
		System.out.println("change:" + JSON.toJSONString(request));
		
		ResultCodeResponse response = capacityService.putSchedule(deviceIp, capacityProps.getPort(), inputId, request);
		
		if(!response.getResult_code().equals(InputResponseEnum.SUCCESS.getCode())){
			throw new BaseException(StatusCode.ERROR, response.getResult_msg());
		}
	}
	
	/**
	 * 删除排期任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午4:32:12
	 * @param String taskUuid 任务标识
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deletePushTask(String taskUuid) throws Exception {
		
		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskUuid, BusinessType.PUSH);
		
		if(output != null){
			
			try{
			
				List<Long> inputIds = new ArrayList<Long>();
				if(output.getPrevId() != null){
					inputIds.add(output.getPrevId());
				}
				if(output.getNextId() != null){
					inputIds.add(output.getNextId());
				}
				if(output.getScheduleId() != null){
					inputIds.add(output.getScheduleId());
				}
				
				List<TaskInputPO> inputs = taskInputDao.findByIdIn(inputIds);
				
				AllRequest allRequest = new AllRequest();
				allRequest.setInput_array(new ArrayList<InputBO>());
				
				for(TaskInputPO input: inputs){
					if(input == null) {
						continue;
					}
					input.setUpdateTime(new Date());
					if(input.getCount() >= 1){
						input.setCount(input.getCount() - 1);
					}
					taskInputDao.save(input);
					if(input.getCount().equals(0) && input.getInput() != null){
						InputBO inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
						allRequest.getInput_array().add(inputBO);
					}
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
				
			} catch (ObjectOptimisticLockingFailureException e) {
				
				// 版本不对，version校验
				System.out.println("delete校验version版本不对");
				Thread.sleep(300);
				deletePushTask(taskUuid);
			}
		}
		
		taskOutputDao.delete(output);
	}


	@Transactional(rollbackFor = Exception.class)
	public void clearPushTask(String taskUuid) throws Exception {

		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskUuid, BusinessType.PUSH);
		List<TaskInputPO> inputs = taskInputDao.findByTaskUuidAndType(taskUuid, BusinessType.PUSH);
		TaskInputPO scheduleInput = new TaskInputPO();

		if (inputs == null || inputs.isEmpty()){
			//输入不存在
			throw new BaseException(StatusCode.ERROR,"not find input for task");
		}else{
			for (int i = 0; i < inputs.size(); i++) {
				TaskInputPO inputPO = inputs.get(i);
				if (inputPO.getUniq().contains("schedule")) {
					scheduleInput = inputPO;
					break;
				}
			}
		}
		if (output == null){
			//输出不存在
			throw new BaseException(StatusCode.ERROR,"not find output for task");
		}
		InputBO inputBO =  JSONObject.parseObject(scheduleInput.getInput(),InputBO.class);
		String inputId = inputBO.getId();

		DeleteScheduleRequest deleteScheduleRequest = new DeleteScheduleRequest();
		List<InputIdRequest> inputIdRequests = new ArrayList<>();
		try{
			List<Long> inputIds = new ArrayList<Long>();
			if(output.getPrevId() != null){
				inputIds.add(output.getPrevId());
				output.setPrevId(null);
			}
			if(output.getNextId() != null){
				inputIds.add(output.getNextId());
				output.setNextId(null);
			}

			List<TaskInputPO> taskInputPOS = taskInputDao.findByIdIn(inputIds);

			AllRequest allRequest = new AllRequest();
			allRequest.setInput_array(new ArrayList<InputBO>());

			for(TaskInputPO taskInput: taskInputPOS){
				if(taskInput == null){
					continue;
				}
				taskInput.setUpdateTime(new Date());
				if(taskInput.getCount() >= 1){
					taskInput.setCount(taskInput.getCount() - 1);
				}
				InputBO curInput = JSONObject.parseObject(taskInput.getInput(),InputBO.class);
				inputIdRequests.add(new InputIdRequest(curInput.getId()));
				taskInputDao.save(taskInputPOS);
			}
			deleteScheduleRequest.setDelete_inputs(inputIdRequests);
			capacityService.clearSchedule(output.getCapacityIp(),capacityProps.getPort(),inputId,deleteScheduleRequest);
			taskOutputDao.save(output);
		} catch (ObjectOptimisticLockingFailureException e) {

			// 版本不对，version校验
			System.out.println("delete校验version版本不对");
			Thread.sleep(300);
			clearPushTask(taskUuid);
		}

	}
	/**
	 * 追加排期<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月6日 下午2:45:01
	 * @param String taskId 任务标识
	 * @param String mediaType 媒体类型
	 * @param PushProgramVO program 追加节目信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changeSchedule(
			String taskId, 
			String mediaType,
			PushProgramVO program) throws Exception{
		
		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskId, BusinessType.PUSH);
		Long prevId = output.getPrevId();
		Long scheduleId = output.getScheduleId();
		if(scheduleId == null){
			throw new BaseException(StatusCode.ERROR, "没有schedule!");
		}
		
		List<Long> inputIds = new ArrayList<Long>();
		inputIds.add(scheduleId);
		if(prevId != null){
			inputIds.add(prevId);
		}
		
		InputCheckBO check = push2SingleInput(taskId, mediaType, program);
		
		TaskInputPO scheduleInput = null;
		String deleteId = null;
		if(inputIds.size() > 0){
			List<TaskInputPO> inputs = taskInputDao.findByIdIn(inputIds);
			
			if(prevId != null){
				TaskInputPO prevPO = null;
				for(TaskInputPO input: inputs){
					if(input.getId().equals(prevId)){
						prevPO = input;
						break;
					}
				}
				
				if(prevPO != null){
					prevPO.setUpdateTime(new Date());
					if(prevPO.getCount() >= 1){
						prevPO.setCount(prevPO.getCount() - 1);
					}
					taskInputDao.save(prevPO);
					
					if(prevPO.getCount().equals(0)){
						deleteId = JSONObject.parseObject(prevPO.getInput(), InputBO.class).getId();
					}
				}
			}
			
			for(TaskInputPO input: inputs){
				if(input.getId().equals(scheduleId)){
					scheduleInput = input;
					break;
				}
			}
			
		}
		
		if(scheduleInput == null) throw new BaseException(StatusCode.ERROR, "没有schedule!");
		InputBO	scheduleInputBO = JSONObject.parseObject(scheduleInput.getInput(), InputBO.class);
		
		ScheduleProgramBO scheduleProgram = push2ScheduleProgram(mediaType, check.getInputBO(), program);
		
		List<InputBO> needInputs = new ArrayList<InputBO>();
		if(!check.isExist()){
			needInputs.add(check.getInputBO());
		}
		
		sendSchedule(output.getCapacityIp(), scheduleInputBO.getId(), deleteId, needInputs, new ArrayListWrapper<ScheduleProgramBO>().add(scheduleProgram).getList());
		
		output.setPrevId(output.getNextId());
		output.setNextId(check.getInputId());
		
		taskOutputDao.save(output);

	}
	
	/**
	 * 批量删除push编单任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月27日 上午11:13:23
	 * @param List<String> taskIds push任务ids
	 */
	@Transactional(rollbackFor = Exception.class)
 	public void batchDeletePushTask(List<String> taskIds) throws Exception{
		
		List<TaskOutputPO> outputs = taskOutputDao.findByTaskUuidInAndType(taskIds, BusinessType.PUSH);
		
		//不要想着循环调用单个任务删除！
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for(TaskOutputPO output: outputs){
			
			Long prevId = output.getPrevId();
			Long nextId = output.getNextId();
			Long scheduleId = output.getScheduleId();
			if(prevId != null){
				if(map.get(prevId) == null){
					map.put(prevId, 1);
				}else{
					int count = map.get(prevId);
					map.put(prevId, count + 1);
				}
			}
			if(nextId != null){
				if(map.get(nextId) == null){
					map.put(nextId, 1);
				}else{
					int count = map.get(nextId);
					map.put(nextId, count + 1);
				}
			}
			if(scheduleId != null){
				if(map.get(scheduleId) == null){
					map.put(scheduleId, 1);
				}else{
					int count = map.get(scheduleId);
					map.put(scheduleId, count + 1);
				}
			}
		}
		
		List<InputMapBO> needDeleteInputs = new ArrayList<InputMapBO>();
		Set<Long> inputIds = map.keySet();
		if(inputIds != null && inputIds.size() > 0){
			List<TaskInputPO> inputs = taskInputDao.findByIdIn(inputIds);

			for(TaskInputPO input: inputs){

				int i = map.get(input.getId()).intValue();
				
				input.setUpdateTime(new Date());
				if(input.getCount() >= 1){
					input.setCount(input.getCount() - i);
				}
				
				InputBO inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
				
				if(input.getCount().equals(0) && input.getInput() != null){
					InputMapBO needDeleteInput = new InputMapBO();
					needDeleteInput.setInputId(input.getId());
					needDeleteInput.setInputBO(inputBO);
					needDeleteInputs.add(needDeleteInput);
				}
			}
			
			taskInputDao.save(inputs);
		}
		
		List<CapacityDeleteBO> bos = new ArrayList<CapacityDeleteBO>();
		for(TaskOutputPO output: outputs){
			
			CapacityDeleteBO bo = isExist(bos, output.getCapacityIp());
			if(bo == null){
				bo = new CapacityDeleteBO();
				bo.setCapacityIp(output.getCapacityIp());
				bo.setInputs(new ArrayList());
				bo.setTasks(new ArrayList());
				bo.setOutputs(new ArrayList());
				
				bos.add(bo);
			}
			
			List<Long> outputInputIds = new ArrayList<Long>();
			Long prevId = output.getPrevId();
			Long nextId = output.getNextId();
			Long scheduleId = output.getScheduleId();
			Long inputId = output.getInputId();
			if(prevId != null){
				outputInputIds.add(prevId);
			}
			if(nextId != null){
				outputInputIds.add(nextId);
			}
			if(scheduleId != null){
				outputInputIds.add(scheduleId);
			}
			if(inputId != null){
				outputInputIds.add(inputId);
			}
			
			for(InputMapBO delete: needDeleteInputs){
				for(Long id: outputInputIds){
					if(id.equals(delete.getInputId())){
						bo.getInputs().add(delete.getInputBO());
					}
				}
			}

			List<TaskBO> tasks = JSONArray.parseArray(output.getTask(), TaskBO.class);
			List<OutputBO> outputBOs = JSONArray.parseArray(output.getOutput(), OutputBO.class);
			
			bo.getTasks().addAll(tasks);
			bo.getOutputs().addAll(outputBOs);
			
		}
		
		for(CapacityDeleteBO bo: bos){
			//发送协议
			AllRequest allRequest = new AllRequest();
			
			allRequest.setInput_array(new ArrayListWrapper<InputBO>().addAll(bo.getInputs()).getList());
			allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(bo.getTasks()).getList());
			allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(bo.getOutputs()).getList());
			
			capacityService.deleteAllAddMsgId(allRequest, bo.getCapacityIp(), capacityProps.getPort());
		}
		
		taskOutputDao.deleteInBatch(outputs);
		
	}
	
	/**
	 * 判断下发delete的能力信息是否存在<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月27日 下午1:21:01
	 * @param List<CapacityDeleteBO> bos 所有能力
	 * @param String capacityIp 能力ip
	 * @return CapacityDeleteBO 能力信息
	 */
	public CapacityDeleteBO isExist(List<CapacityDeleteBO> bos, String capacityIp){
		for(CapacityDeleteBO bo: bos){
			if(bo.getCapacityIp() != null && bo.getCapacityIp().equals(capacityIp)){
				return bo;
			}
		}
		
		return null;
	}
	
}
