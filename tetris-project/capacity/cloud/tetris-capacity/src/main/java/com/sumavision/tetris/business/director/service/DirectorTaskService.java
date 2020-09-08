package com.sumavision.tetris.business.director.service;

import java.util.*;

import com.google.common.collect.Lists;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.capacity.constant.EncodeConstant.*;
import com.sumavision.tetris.capacity.template.TemplateService;
import com.sumavision.tetris.sts.transformTemplate.jni.TransformJniLib;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.bo.InputCheckBO;
import com.sumavision.tetris.business.common.bo.InputMapBO;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.director.bo.DirectorRequestBO;
import com.sumavision.tetris.business.director.vo.OutputsVO;
import com.sumavision.tetris.business.director.vo.DestinationVO;
import com.sumavision.tetris.business.director.vo.DirectorTaskVO;
import com.sumavision.tetris.business.director.vo.PictureVO;
import com.sumavision.tetris.business.director.vo.SourceVO;
import com.sumavision.tetris.business.director.vo.TextVO;
import com.sumavision.tetris.business.director.vo.TranscodeAudioVO;
import com.sumavision.tetris.business.director.vo.TranscodeVO;
import com.sumavision.tetris.business.director.vo.TranscodeVideoVO;
import com.sumavision.tetris.capacity.bo.input.BackUpEsAndRawBO;
import com.sumavision.tetris.capacity.bo.input.BackUpProgramBO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.PidIndexBO;
import com.sumavision.tetris.capacity.bo.input.ProgramAudioBO;
import com.sumavision.tetris.capacity.bo.input.ProgramBO;
import com.sumavision.tetris.capacity.bo.input.ProgramElementBO;
import com.sumavision.tetris.capacity.bo.input.ProgramOutputBO;
import com.sumavision.tetris.capacity.bo.input.ProgramVideoBO;
import com.sumavision.tetris.capacity.bo.input.SrtTsBO;
import com.sumavision.tetris.capacity.bo.output.CommonTsOutputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.output.OutputMediaBO;
import com.sumavision.tetris.capacity.bo.output.OutputProgramBO;
import com.sumavision.tetris.capacity.bo.request.AllRequest;
import com.sumavision.tetris.capacity.bo.request.DeleteOutputsRequest;
import com.sumavision.tetris.capacity.bo.request.IdRequest;
import com.sumavision.tetris.capacity.bo.response.AllResponse;
import com.sumavision.tetris.capacity.bo.task.DynamicPictureOsdBO;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.FpsConvertBO;
import com.sumavision.tetris.capacity.bo.task.G711BO;
import com.sumavision.tetris.capacity.bo.task.H264BO;
import com.sumavision.tetris.capacity.bo.task.H265BO;
import com.sumavision.tetris.capacity.bo.task.OsdBO;
import com.sumavision.tetris.capacity.bo.task.PictureOsdObjectBO;
import com.sumavision.tetris.capacity.bo.task.PreProcessingBO;
import com.sumavision.tetris.capacity.bo.task.ResampleBO;
import com.sumavision.tetris.capacity.bo.task.ScaleBO;
import com.sumavision.tetris.capacity.bo.task.StaticPictureOsdBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.bo.task.TaskSourceBO;
import com.sumavision.tetris.capacity.bo.task.TextOsdBO;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.capacity.service.ResponseService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class DirectorTaskService {
	
	public static final String BACK_UP = "backup";
	
	@Autowired
	private TaskInputDAO taskInputDao;
	
	@Autowired
	private TaskOutputDAO taskOutputDao;
	
	@Autowired
	private ResponseService responseService;
	
	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private CapacityProps capacityProps;

	@Autowired
	private TemplateService templateService;
	
	/**
	 * 添加导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午4:16:05
	 * @param List<DirectorTaskVO> directors 导播参数
	 */
	public void addDirectorTask(List<DirectorTaskVO> directors) throws Exception{
		
		List<DirectorRequestBO> allRequest = new ArrayList<DirectorRequestBO>();
		for(DirectorTaskVO director: directors){
			
			String taskId = director.getTaskId();
			String capacityIp = director.getCapacityIp();
			List<SourceVO> sources = director.getSources();
			Collections.sort(sources, new SourceVO.IndexComparator());
			
			List<InputBO> needCreateInputs = new ArrayList<InputBO>();
			List<InputBO> allInputs = new ArrayList<InputBO>();
			List<Long> allInputIds = new ArrayList<Long>();
			for(SourceVO source: sources){
				InputCheckBO check = transferNormalInput(source, taskId);
				allInputs.add(check.getInputBO());
				allInputIds.add(check.getInputId());
				if(!check.isExist()){
					needCreateInputs.add(check.getInputBO());
				}
			}
			
			//生成备份源--选择索引
			InputBO backup = director2BackupInputBO(taskId, allInputs, director.getSelect_index());
			TaskInputPO input = new TaskInputPO();
			input.setUpdateTime(new Date());
			input.setUniq(new StringBufferWrapper().append(BACK_UP)
												   .append("-")
												   .append(taskId)
												   .toString());
			input.setTaskUuid(taskId);
			input.setInput(JSON.toJSONString(backup));
			input.setType(BusinessType.DIRECTOR);
			taskInputDao.save(input);
			
			needCreateInputs.add(backup);
			allInputIds.add(input.getId());
			
			//建立转码任务--因为有流透传，所以id放接口外面，因为透传区分不开音视频(现在强行用任务的id来区分)
			List<TaskBO> tasks = director2TaskBO(taskId, backup, director.getTranscode());
		
			List<OutputBO> outputs = director2OutputBO(taskId, tasks, director.getDestinations(), capacityIp);
		
			DirectorRequestBO request = sendProtocal(taskId, capacityIp, JSONObject.toJSONString(allInputIds), needCreateInputs, tasks, outputs);
		
			allRequest.add(request);
		}
		
		//批量处理request -- 保证一个失败则全部失败（暂时）
		allRequestProcess(allRequest);
	}
	
	/**
	 * 删除导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午4:35:50
	 * @param List<String> taskIds 删除的导播任务ids
	 */
	public void deleteDirectorTask(List<String> taskIds) throws Exception{
		
		List<TaskOutputPO> outputs = taskOutputDao.findByTaskUuidInAndType(taskIds, BusinessType.DIRECTOR);
		
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for(TaskOutputPO output: outputs){
			List<Long> inputIds = JSONArray.parseArray(output.getInputList(), Long.class);
			for(Long inputId: inputIds){
				if(map.get(inputId) == null){
					map.put(inputId, 1);
					continue;
				}
				int count = map.get(inputId);
				map.put(inputId, count + 1);
			}
		}
		
		List<InputMapBO> needDeleteInputs = new ArrayList<InputMapBO>();
		Set<Long> inputIds = map.keySet();
		if(inputIds != null && inputIds.size() > 0){
			List<TaskInputPO> inputs = taskInputDao.findByIdIn(inputIds);
			
			try {

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

			} catch (ObjectOptimisticLockingFailureException e) {
				
				// 版本不对，version校验
				System.out.println("delete校验version版本不对");
				Thread.sleep(300);
				deleteDirectorTask(taskIds);
			}
		}
		
		for(TaskOutputPO output: outputs){
			
			List<Long> outputInputIds = JSONArray.parseArray(output.getInputList(), Long.class);
			List<InputBO> deleteInputs = new ArrayList<InputBO>();
			for(InputMapBO delete: needDeleteInputs){
				for(Long id: outputInputIds){
					if(id.equals(delete.getInputId())){
						deleteInputs.add(delete.getInputBO());
					}
				}
			}
			
			//发送协议
			AllRequest allRequest = new AllRequest();
			
			List<TaskBO> tasks = JSONArray.parseArray(output.getTask(), TaskBO.class);
			List<OutputBO> outputBOs = JSONArray.parseArray(output.getOutput(), OutputBO.class);
			
			allRequest.setInput_array(new ArrayListWrapper<InputBO>().addAll(deleteInputs).getList());
			allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(tasks).getList());
			allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
			
			capacityService.deleteAllAddMsgId(allRequest, output.getCapacityIp(), capacityProps.getPort());
			
		}
		
		taskOutputDao.deleteInBatch(outputs);
		
	}
	
	/**
	 * 添加导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午9:00:00
	 * @param List<AddOutputsVO> outputVOs 导播输出
	 */
	public void addOutput(List<OutputsVO> outputVOs) throws Exception{
		
		List<String> taskIds = new ArrayList<String>();
		for(OutputsVO outputVO: outputVOs){
			taskIds.add(outputVO.getTaskId());
		}
		
		List<TaskOutputPO> taskOutputs = taskOutputDao.findByTaskUuidInAndType(taskIds, BusinessType.DIRECTOR);
		
		List<DirectorRequestBO> requestBOs = new ArrayList<DirectorRequestBO>();
			
		for(OutputsVO outputVO: outputVOs){
			
			String taskId = outputVO.getTaskId();
			List<DestinationVO> dsts = outputVO.getDsts();
			
			TaskOutputPO output = null;
			for(TaskOutputPO taskOutput: taskOutputs){
				if(taskOutput.getTaskUuid().equals(taskId)){
					output = taskOutput;
				}
			}
			
			if(output == null){
				throw new BaseException(StatusCode.ERROR, "任务不存在在！任务id为：" + taskId);
			}	
			
			List<TaskBO> tasks = JSONObject.parseArray(output.getTask(), TaskBO.class);
			
			List<OutputBO> outputs = JSONObject.parseArray(output.getOutput(), OutputBO.class);
			
			List<OutputBO> addOutputs = director2OutputBO(taskId, tasks, dsts, output.getCapacityIp());
			
			AllRequest allRequest = new AllRequest();
			allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(addOutputs).getList());
			
			DirectorRequestBO requestBO = new DirectorRequestBO();
			requestBO.setCapacityIp(output.getCapacityIp());
			requestBO.setRequest(allRequest);
			
			requestBOs.add(requestBO);
			
			outputs.addAll(addOutputs);
			
			output.setUpdateTime(new Date());
			output.setOutput(JSON.toJSONString(outputs));
			
		}
		
		allRequestProcess(requestBOs);
		
		taskOutputDao.save(taskOutputs);
		
	}
	
	/**
	 * 删除导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:25:19
	 * @param List<OutputsVO> outputVOs
	 */
	public void deleteOutput(List<OutputsVO> outputVOs) throws Exception{
		
		List<String> taskIds = new ArrayList<String>();
		for(OutputsVO outputVO: outputVOs){
			taskIds.add(outputVO.getTaskId());
		}
		
		List<TaskOutputPO> taskOutputs = taskOutputDao.findByTaskUuidInAndType(taskIds, BusinessType.DIRECTOR);
		
		for(OutputsVO outputVO: outputVOs){
			
			String taskId = outputVO.getTaskId();
			List<DestinationVO> dsts = outputVO.getDsts();
			
			TaskOutputPO output = null;
			for(TaskOutputPO taskOutput: taskOutputs){
				if(taskOutput.getTaskUuid().equals(taskId)){
					output = taskOutput;
				}
			}
			
			if(output == null){
				throw new BaseException(StatusCode.ERROR, "任务不存在在！任务id为：" + taskId);
			}	
			
			List<OutputBO> outputs = JSONObject.parseArray(output.getOutput(), OutputBO.class);
			
			List<OutputBO> needDeleteOutputs = new ArrayList<OutputBO>();
			for(OutputBO outputBO: outputs){
				for(DestinationVO dst: dsts){
					String ip = dst.getIp();
					String port = dst.getPort();
					if(dst.getType().equals("udp")){
						if(outputBO.getUdp_ts() == null) continue;
						if(outputBO.getUdp_ts().getIp().equals(ip) && outputBO.getUdp_ts().getPort().toString().equals(port)){
							needDeleteOutputs.add(outputBO);		
						}
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

				capacityService.deleteOutputsWithMsgId(delete, capacityProps.getIp());
				
				output.setUpdateTime(new Date());
				output.setOutput(JSON.toJSONString(outputs));
				
			}
		}
		
		taskOutputDao.save(taskOutputs);
		
	}
	
	/**
	 * 生成备份源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月16日 下午3:16:23
	 * @param String taskId 任务标识
	 * @param List<InputBO>sources 源信息
	 * @return InputBO 备份源
	 */
	public InputBO director2BackupInputBO(String taskId, List<InputBO> sources, String index) throws Exception {
		
		String backInputId = new StringBufferWrapper().append("backup-").append(taskId).toString();

		BackUpEsAndRawBO back_up_raw = new BackUpEsAndRawBO().setSelect_index(index).setMode("manual");
		back_up_raw.setProgram_array(new ArrayList<BackUpProgramBO>());
		int count=0;
		int elementCount = sources.size();
		for (InputBO source : sources) {
			
			String inputId = source.getId();

			ProgramElementBO velementBO = new ProgramElementBO().setType("video").setPid(513).setProgram_switch_array(
					new ArrayListWrapper<PidIndexBO>().addAll(generatePidIndex(elementCount, 0)).getList());
			ProgramElementBO aelementBO = new ProgramElementBO().setType("audio").setPid(514).setProgram_switch_array(
					new ArrayListWrapper<PidIndexBO>().addAll(generatePidIndex(elementCount, 1)).getList());
			//字幕先预留
//			ProgramElementBO selementBO = new ProgramElementBO().setType("subtitle").setPid(3).setProgram_switch_array(
//					new ArrayListWrapper<PidIndexBO>().addAll(generatePidIndex(elementCount, 2)).getList());
			List<ProgramElementBO> elementBOs = new ArrayList<ProgramElementBO>();
			elementBOs.add(velementBO);
			elementBOs.add(aelementBO);
//			elementBOs.add(selementBO);
			BackUpProgramBO backupPro = new BackUpProgramBO().setInput_id(inputId).setProgram_number(1)
					.setElement_array(new ArrayListWrapper<ProgramElementBO>().addAll(elementBOs).getList());
			back_up_raw.getProgram_array().add(backupPro);
			ProgramOutputBO outPro = new ProgramOutputBO().setProgram_number(1).setElement_array(elementBOs);
			if(count==0){
				back_up_raw.setOutput_program(outPro);
			}
			count++;
		}
		
		// 创建输入
		InputBO input = new InputBO().setBack_up_raw(back_up_raw).setId(backInputId)
				.setProgram_array(new ArrayList<ProgramBO>()).setNormal_map(new JSONObject());
		ProgramBO program = new ProgramBO().setProgram_number(1).setVideo_array(new ArrayList<ProgramVideoBO>())
				.setAudio_array(new ArrayList<ProgramAudioBO>());

		ProgramVideoBO video = new ProgramVideoBO().setPid(513);
		ProgramAudioBO audio = new ProgramAudioBO().setPid(514);

		program.getVideo_array().add(video);
		program.getAudio_array().add(audio);
		input.getProgram_array().add(program);

		return input;

	}

	/**
	 * 生成pid数组--内部约定（count代表需要生成的个数，index代表索引，0是视频，1是音频）<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月16日 下午2:56:12
	 * @param int count 个数
	 * @param int index 索引
	 * @return List<PidIndexBO>
	 */
	private List<PidIndexBO> generatePidIndex(int count, int index) {
		List<PidIndexBO> pidIndexBOs = new ArrayList<PidIndexBO>();
		for (int i = 0; i < count; i++) {
			PidIndexBO pidIndexBO = new PidIndexBO().setPid_index(index);
			pidIndexBOs.add(pidIndexBO);
		}
		return pidIndexBOs;
	}

	/**
	 * 导播源转InputBO--带校验<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午4:26:41
	 * @param SourceVO source 源信息
	 * @param String taskUuid 任务标识
	 * @return CheckInputBO
	 */
	public InputCheckBO transferNormalInput(SourceVO source, String taskUuid) throws Exception{
		
		String uniq = new StringBufferWrapper().append(source.getIp())
											   .append("@")
											   .append(source.getPort())
											   .toString();
		
		TaskInputPO input = taskInputDao.findByUniq(uniq);
		InputCheckBO check = new InputCheckBO();
		boolean isExist = false;
		InputBO inputBO = new InputBO();
		if(input == null){
			
			try {
				
				inputBO = transformSourceVo2Input(source, taskUuid);
				
				input = new TaskInputPO();
				input.setUpdateTime(new Date());
				input.setUniq(uniq);
				input.setTaskUuid(taskUuid);
				input.setInput(JSON.toJSONString(inputBO));
				input.setType(BusinessType.DIRECTOR);
				taskInputDao.save(input);
			
			} catch (ConstraintViolationException e) {
				
				//数据已存在（ip，port校验）
				System.out.println("校验输入已存在");
				Thread.sleep(300);
				transferNormalInput(source, taskUuid);
				
			} catch (Exception e) {
				
				if(!(e instanceof ConstraintViolationException)){
					throw e;
				}
				
			}
			
		}else{
			
			try {
				
				if(input.getCount().equals(0)){
					inputBO = transformSourceVo2Input(source, taskUuid);
					input.setInput(JSON.toJSONString(inputBO));
					input.setTaskUuid(taskUuid);
					input.setType(BusinessType.DIRECTOR);
				}else{
					inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
					isExist = true;
				}
				input.setUpdateTime(new Date());
				input.setCount(input.getCount() + 1);
				taskInputDao.save(input);
				
			} catch (ObjectOptimisticLockingFailureException e) {
				
				// 版本不对，version校验
				System.out.println("save校验version版本不对");
				Thread.sleep(300);
				transferNormalInput(source, taskUuid);
				
			}catch (Exception e) {
				
				if(!(e instanceof ObjectOptimisticLockingFailureException)){
					throw e;
				}
			}
		}
		
		check.setInputId(input.getId());
		check.setExist(isExist);
		check.setInputBO(inputBO);
		
		return check;
	}
	
	/**
	 * 导播源转换InputBO<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午4:11:23
	 * @param SourceVO source
	 * @param String taskId
	 * @return InputBO
	 */
	public InputBO transformSourceVo2Input(SourceVO source, String taskId) throws Exception{
		
		String inputId = new StringBufferWrapper().append("input-")
												  .append(taskId)
												  .append("-")
												  .append(source.getBundleId())
												  .toString();
		
		String sourceIp = source.getIp();
		int sourcePort = Integer.valueOf(source.getPort()).intValue();
		
		InputBO inputBO = new InputBO();
		inputBO.setId(inputId)
		       .setMedia_type_once_map(new JSONObject())
		       .setProgram_array(new ArrayList<ProgramBO>());
		
		if(source.getType().equals("srt")){
			//srt_ts
			SrtTsBO srt_ts = new SrtTsBO().setSource_ip(sourceIp)
					  					  .setSource_port(sourcePort)
					  					  //TODO:caller,listener,rendezvous
					  					  .setMode("caller");
			inputBO.setSrt_ts(srt_ts);
			
		}else{
			
		}
		
		ProgramBO program = new ProgramBO().setProgram_number(1)
										   .setVideo_array(new ArrayList<ProgramVideoBO>())
										   .setAudio_array(new ArrayList<ProgramAudioBO>());

		//字幕先预留
//	    ProgramSubtitleBO subtitle = new ProgramSubtitleBO().setPid(3)
//	    													.setDecode_mode("cpu");
		
		ProgramVideoBO video = new ProgramVideoBO().setPid(513)
								   				   .setDecode_mode("cpu");
		ProgramAudioBO audio = new ProgramAudioBO().setPid(514)
											       .setDecode_mode("cpu");
		
		program.getVideo_array().add(video);
		program.getAudio_array().add(audio);
//		program.getSubtitle_array().add(subtitle);
		
		inputBO.getProgram_array().add(program);
		
		return inputBO;
	}
	
	/**
	 * 导播参数生成task<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午1:08:21
	 * @param String videoTaskId
	 * @param String audioTaskId
	 * @param String subtitleTaskId
	 * @param String encodeVideoId
	 * @param String encodeAudioId
	 * @param String encodeSubtitleId
	 * @param InputBO input backup输入
	 * @param TranscodeVO transcode 转码参数
	 * @return List<TaskBO>
	 */
	public List<TaskBO> director2TaskBO(
			String taskId,
			InputBO input, 
			TranscodeVO transcode) throws Exception {
		
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
		String subtitleTaskId = new StringBufferWrapper().append("task-subtitle-")
														 .append(taskId)
														 .toString();
		String encodeSubtitleId = new StringBufferWrapper().append("encode-subtitle-")
														   .append(taskId)
														   .toString();

		List<TaskBO> tasks = new ArrayList<TaskBO>();
		
		/*******
		 * 视频 *
		 *******/
		if(transcode != null && transcode.getVideo() != null){
			
			TranscodeVideoVO transcodeVideo = transcode.getVideo();
			
			//必须有的参数
			String codec = transcodeVideo.getCodec();
			Integer bitrate = transcodeVideo.getBitrate();
			String ratio = transcodeVideo.getRatio();
			Integer width = transcodeVideo.getWidth();
			Integer height = transcodeVideo.getHeight();
			
			Integer format_bitrate = (int) (bitrate * 0.83);
			Integer format_max_bitrate = (int) (bitrate * 0.67);
			
			//按需参数
			String fps = transcodeVideo.getFps();
			List<TextVO> texts = transcodeVideo.getContents();
			List<PictureVO> staticPictures = transcodeVideo.getStaticPics();
			List<PictureVO> dynamicPictures = transcodeVideo.getDynamicPics();
			
			//视频转码
			TaskSourceBO videoSource = new TaskSourceBO().setInput_id(input.getId())
														 .setProgram_number(input.getProgram_array().get(0).getProgram_number())
														 .setElement_pid(input.getProgram_array().get(0).getVideo_array().get(0).getPid());

			TaskBO videoTask = new TaskBO().setId(videoTaskId)
										   .setType("video")
										   .setRaw_source(videoSource)
										   .setEncode_array(new ArrayList<EncodeBO>());
			
			EncodeBO videoEncode = new EncodeBO().setEncode_id(encodeVideoId);
			
			if("h264".equals(codec)){

				String x264Map = templateService.getVideoEncodeMap(TplVideoEncoder.VENCODER_X264);
				JSONObject x264Obj = JSONObject.parseObject(x264Map);
				x264Obj.put("bitrate",format_bitrate/1000);
				x264Obj.put("max_bitrate",format_max_bitrate/1000);
				x264Obj.put("ratio",ratio);
				x264Obj.put("resolution",width+"x"+height);
				if (fps != null) {
					x264Obj.put("fps",fps);
				}
//
//				H264BO h264 = new H264BO().setBitrate(format_bitrate)
//										  .setMax_bitrate(format_max_bitrate)
//										  .setRatio(ratio)
//										  .setWidth(width)
//										  .setHeight(height);
				
//				if(fps != null) h264.setFps(fps);
				videoEncode.setH264(x264Obj);
				
			}else if("h265".equals(codec)){

				String params = templateService.getVideoEncodeMap(TplVideoEncoder.VENCODER_X265);
				JSONObject obj = JSONObject.parseObject(params);
				obj.put("bitrate",format_bitrate/1000);
				obj.put("max_bitrate",format_max_bitrate/1000);
				obj.put("ratio",ratio);
				obj.put("resolution",width+"x"+height);
				if (fps != null) {
					obj.put("fps",fps);
				}

//				H265BO h265 = new H265BO().setBitrate(format_bitrate)
//						  				  .setMax_bitrate(format_max_bitrate)
//										  .setRatio(ratio)
//										  .setWidth(width)
//										  .setHeight(height);
				
//				if(fps != null) h265.setFps(fps);
				
				videoEncode.setHevc(obj);
				
			}
			
			if(width != null && height != null){
				//缩放
				if(videoEncode.getProcess_array() == null) videoEncode.setProcess_array(new ArrayList<PreProcessingBO>());
				
				ScaleBO scale = new ScaleBO().setPlat("cpu").setHeight(height).setWidth(width);
				
				PreProcessingBO scaleProcess = new PreProcessingBO().setScale(scale);
				
				videoEncode.getProcess_array().add(scaleProcess);
			}
			
			if(fps != null){
				//帧率变换
				if(videoEncode.getProcess_array() == null) videoEncode.setProcess_array(new ArrayList<PreProcessingBO>());
				
				FpsConvertBO fpsConvert = new FpsConvertBO().setPlat("cpu").setFps(fps);
				
				PreProcessingBO fpsProcess = new PreProcessingBO().setFps_convert(fpsConvert);
				
				videoEncode.getProcess_array().add(fpsProcess);
			}
			
			if(texts != null && texts.size() > 0){
				//文本osd
				if(videoEncode.getProcess_array() == null) videoEncode.setProcess_array(new ArrayList<PreProcessingBO>());
				
				TextOsdBO text_osd = new TextOsdBO().setPlat("cpu")
													.setText_osds(new ArrayList<OsdBO>());
				
				for(TextVO text: texts){
					OsdBO osd = new OsdBO().setContent(text.getContent())
										   .setX(text.getX())
										   .setY(text.getY())
										   .setWidth(text.getWidth())
										   .setHeight(text.getHeight());
					
					text_osd.getText_osds().add(osd);
				}
				
				PreProcessingBO fpsProcess = new PreProcessingBO().setText_osd(text_osd);
				
				videoEncode.getProcess_array().add(fpsProcess);
				
			}
			
			if(staticPictures != null && staticPictures .size() > 0){
				//静态图片osd
				if(videoEncode.getProcess_array() == null) videoEncode.setProcess_array(new ArrayList<PreProcessingBO>());
				
				StaticPictureOsdBO picture_osd = new StaticPictureOsdBO().setPlat("cpu")
																		 .setStatic_pic_osds(new ArrayList<PictureOsdObjectBO>());
				
				for(PictureVO picture: staticPictures){
					PictureOsdObjectBO osd = new PictureOsdObjectBO().setPath(picture.getPath())
																	 .setX(picture.getX())
																	 .setY(picture.getY())
																	 .setWidth(picture.getWidth())
																	 .setHeight(picture.getHeight());
					
					picture_osd.getStatic_pic_osds().add(osd);
				}
				
				PreProcessingBO fpsProcess = new PreProcessingBO().setStatic_pic_osd(picture_osd);
				
				videoEncode.getProcess_array().add(fpsProcess);
			}
			
			if(dynamicPictures != null && dynamicPictures.size() > 0){
				//动态图片osd
				if(videoEncode.getProcess_array() == null) videoEncode.setProcess_array(new ArrayList<PreProcessingBO>());
				
				DynamicPictureOsdBO picture_osd = new DynamicPictureOsdBO().setPlat("cpu")
						 												   .setDynamic_pic_osds(new ArrayList<PictureOsdObjectBO>());

				for(PictureVO picture: dynamicPictures){
					PictureOsdObjectBO osd = new PictureOsdObjectBO().setPath(picture.getPath())
																	 .setX(picture.getX())
																	 .setY(picture.getY())
																	 .setWidth(picture.getWidth())
																	 .setHeight(picture.getHeight());
				
					picture_osd.getDynamic_pic_osds().add(osd);
				}
				
				PreProcessingBO fpsProcess = new PreProcessingBO().setDynamic_pic_osd(picture_osd);
				
				videoEncode.getProcess_array().add(fpsProcess);
			}
			
			videoTask.getEncode_array().add(videoEncode);
			
			tasks.add(videoTask);
			
		}else{
			
			//视频透传
			TaskSourceBO videoSource = new TaskSourceBO().setInput_id(input.getId())
														 .setProgram_number(input.getProgram_array().get(0).getProgram_number())
														 .setElement_pid(input.getProgram_array().get(0).getVideo_array().get(0).getPid());

			TaskBO videoTask = new TaskBO().setId(videoTaskId)
										   .setType("passby")
										   .setEs_source(videoSource)
										   .setEncode_array(new ArrayList<EncodeBO>());
			
			EncodeBO videoEncode = new EncodeBO().setEncode_id(encodeVideoId)
						 						 .setPassby(new JSONObject());
			
			videoTask.getEncode_array().add(videoEncode);
			
			tasks.add(videoTask);
		}
		
		/*******
		 * 音频 *
		 *******/
		if(transcode != null && transcode.getAudio() != null){
			
			TranscodeAudioVO transcodeAudio = transcode.getAudio();
			
			//必须有的参数
			String codec = transcodeAudio.getCodec();
			Integer bitrate = transcodeAudio.getBitrate();
			Integer sampleRate = transcodeAudio.getSampleRate();
			
			//音频转码
			TaskSourceBO audioSource = new TaskSourceBO().setInput_id(input.getId())
					 									 .setProgram_number(input.getProgram_array().get(0).getProgram_number())
					 									 .setElement_pid(input.getProgram_array().get(0).getAudio_array().get(0).getPid());

			TaskBO audioTask = new TaskBO().setId(audioTaskId)
										   .setType("audio")
										   .setRaw_source(audioSource)
										   .setEncode_array(new ArrayList<EncodeBO>());

			EncodeBO audioEncode = new EncodeBO().setEncode_id(encodeAudioId);

			JSONObject audioObj = new JSONObject();

			if("aac".equals(codec)){

				String aacMap = templateService.getAudioEncodeMap("aac");
				audioObj = JSONObject.parseObject(aacMap);
				audioObj.put("bitrate",String.valueOf(bitrate/1000));
				audioObj.put("sample_rate",String.valueOf(sampleRate/1000));

//				AacBO aac = new AacBO().setAac()
//			   			   			   .setBitrate(String.valueOf(bitrate/1000))
//			   			   			   .setSample_rate(String.valueOf(sampleRate/1000));
				
				audioEncode.setAac(audioObj);
				
			}else if("pcma".equals(codec)){
				//TODO:转码不支持
				G711BO g711a = new G711BO().setBitrate(String.valueOf(bitrate/1000))
										   .setSample_rate(String.valueOf(sampleRate/1000))
										   .setSample_byte(8);
				
				audioEncode.setG711a(g711a);
				
			}else if("pcmu".equals(codec)){
				//TODO:转码不支持
				G711BO g711u = new G711BO().setBitrate(String.valueOf(bitrate/1000))
						   				   .setSample_rate(String.valueOf(sampleRate/1000))
						   				   .setSample_byte(8);

				audioEncode.setG711a(g711u);

			}else if("g729".equals(codec)){
				//TODO:转码不支持
			}
			
			if(audioEncode.getProcess_array() == null) audioEncode.setProcess_array(new ArrayList<PreProcessingBO>());
			
			ResampleBO resample = new ResampleBO().setSample_rate(sampleRate);

			if (audioObj!=null && !audioObj.isEmpty()){
				resample.setChannel_layout(audioObj.getString("channel_layout")) ;
				resample.setFormat(audioObj.getString("sample_fmt")) ;
			}

			PreProcessingBO audio_decode_processing = new PreProcessingBO().setResample(resample);
			audioEncode.getProcess_array().add(audio_decode_processing);
			
			audioTask.getEncode_array().add(audioEncode);
			
			tasks.add(audioTask);
			
		}else{
			
			//音频透传
			TaskSourceBO audioSource = new TaskSourceBO().setInput_id(input.getId())
														 .setProgram_number(input.getProgram_array().get(0).getProgram_number())
														 .setElement_pid(input.getProgram_array().get(0).getAudio_array().get(0).getPid());
			
			TaskBO audioTask = new TaskBO().setId(audioTaskId)
										   .setType("passby")
										   .setEs_source(audioSource)
										   .setEncode_array(new ArrayList<EncodeBO>());
			
			EncodeBO audioEncode = new EncodeBO().setEncode_id(encodeAudioId)
					                             .setPassby(new JSONObject());
			
			audioTask.getEncode_array().add(audioEncode);
			
			tasks.add(audioTask);
		}
		
		/*****************************************
		 * 字幕 -- 这里的subtitle指的是电影字幕，不是osd *
		 *****************************************/
		//先不用，预留
		if(transcode != null && transcode.getSubtitile() != null){
			
			//字幕转码
			TaskSourceBO subtitleSource = new TaskSourceBO().setInput_id(input.getId())
					 										.setProgram_number(input.getProgram_array().get(0).getProgram_number())
				 											.setElement_pid(input.getProgram_array().get(0).getSubtitle_array().get(0).getPid());

			TaskBO subtitleTask = new TaskBO().setId(subtitleTaskId)
											  .setType("subtitle")
											  .setRaw_source(subtitleSource)
											  .setEncode_array(new ArrayList<EncodeBO>());
			
			EncodeBO subtitleEncode = new EncodeBO().setEncode_id(encodeSubtitleId);
			
		}else{
			
			//字幕透传
			TaskSourceBO subtitleSource = new TaskSourceBO().setInput_id(input.getId())
															.setProgram_number(input.getProgram_array().get(0).getProgram_number())
															.setElement_pid(input.getProgram_array().get(0).getSubtitle_array().get(0).getPid());
			
			TaskBO subtitleTask = new TaskBO().setId(subtitleTaskId)
											  .setType("passby")
											  .setEs_source(subtitleSource)
											  .setEncode_array(new ArrayList<EncodeBO>());
			
			EncodeBO subtitleEncode = new EncodeBO().setEncode_id(encodeSubtitleId)
													.setPassby(new JSONObject());
			
			subtitleTask.getEncode_array().add(subtitleEncode);
			
			tasks.add(subtitleTask);
		}

		return tasks;

	}

	public String getEncodeTemplateParamByEncodeType(String encodeType) throws CommonException {
		 String tpl = "";
		 List<String> audioEncodeTypes = new ArrayList<>(Arrays.asList(
		 		"mp2",
				 "mp3",
				 "aac",
				 "heaac",
				 "heaac_v2",
				 "mpeg4-aac-lc",
				 "mpeg4-he-aac-lc",
				 "mpeg4-he-aac-v2-lc",
				 "dobly",
				 "ac3",
				 "eac3"
		 ));

		 Map<String, TplVideoEncoder> videoEncodeMap = new HashMap<>();
		 videoEncodeMap.put("h264",TplVideoEncoder.VENCODER_X264);
		 videoEncodeMap.put("x264",TplVideoEncoder.VENCODER_X264);
		 videoEncodeMap.put("h265",TplVideoEncoder.VENCODER_X265);
		 videoEncodeMap.put("x265",TplVideoEncoder.VENCODER_X265);
		 videoEncodeMap.put("mpeg2",TplVideoEncoder.VENCODER_CPU_MPEG2);
		 videoEncodeMap.put("m2v",TplVideoEncoder.VENCODER_CPU_MPEG2);
		 videoEncodeMap.put("avs2",TplVideoEncoder.VENCODER_AVS2);

		 if (audioEncodeTypes.contains(encodeType)){
			 TplAudioEncoder audioEncoder = TplAudioEncoder.getTplAudioEncoder(encodeType);
			 if (audioEncoder.equals(TplAudioEncoder.AENCODER_MP3)) {
				 tpl = TransformJniLib.getInstance().GetMp3EncParamTemplate("44.1");
			 }else{
				 tpl = TransformJniLib.getInstance().GetAudioEncParamTemplate(audioEncoder.ordinal(),0);
			 }
		 }
		 if (videoEncodeMap.containsKey(encodeType)){
			 TplVideoEncoder tplVideoEncoder = videoEncodeMap.get(encodeType);
			 tpl = TransformJniLib.getInstance().GetVideoEncParamTemplate(tplVideoEncoder.ordinal(),"main","4",0);
		 }else{
			 TplVideoEncoder tplVideoEncoder = TplVideoEncoder.getTplVideoEncoder(encodeType);
			 tpl = TransformJniLib.getInstance().GetVideoEncParamTemplate(tplVideoEncoder.ordinal(),"main","4",0);
		 }
		 return tpl;
	}

	/**
	 * 导播参数生成outputBO<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午3:09:43
	 * @param String taskId 任务标识
	 * @param String videoTaskId
	 * @param String audioTaskId
	 * @param String subtitleTaskId
	 * @param String encodeVideoId
	 * @param String encodeAudioId
	 * @param String encodeSubtitleId
	 * @param List<TaskBO> tasks 转码任务BO
	 * @param List<DestinationVO> dsts 导播输出参数
	 * @param String capacityIp 能力ip
	 * @return List<OutputBO> 输出
	 */
	public List<OutputBO> director2OutputBO(
			String taskId, 
			List<TaskBO> tasks, 
			List<DestinationVO> dsts, 
			String capacityIp) throws Exception {

		List<OutputBO> outputs = new ArrayList<OutputBO>();
		for(DestinationVO dst: dsts){
			
			String outputId = new StringBufferWrapper().append("output-")
													   .append(taskId)
													   .append("-")
													   .append(dst.getBundleId())
													   .toString();
			
			OutputBO output = new OutputBO().setId(outputId);
			
			if("udp".equals(dst.getType())){
				
				//输出udp
				CommonTsOutputBO udp_ts = new CommonTsOutputBO().setUdp_ts()
																.setIp(dst.getIp())
																.setPort(Integer.valueOf(dst.getPort()))
																.setLocal_ip(capacityIp)
																.setRate_ctrl("VBR")
																.setBitrate(dst.getBitrate().intValue())
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
					//字幕
					if(taskBO.getId().contains("subtitle")){
						OutputMediaBO media = new OutputMediaBO().setTask_id(taskBO.getId())
																 .setType("subtitle")
																 .setEncode_id(taskBO.getEncode_array().iterator().next().getEncode_id())
																 .setPid(515);
						medias.add(media);
					}

				}
				
				OutputProgramBO program = new OutputProgramBO().setProgram_number(301)
															   .setPmt_pid(101)
															   .setPcr_pid(100)
															   .setMedia_array(new ArrayListWrapper<OutputMediaBO>().addAll(medias).getList());
				
				udp_ts.getProgram_array().add(program);	
				output.setUdp_ts(udp_ts);
			}
		}

		return outputs;
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
	public DirectorRequestBO sendProtocal(
			String taskUuid,
			String capacityIp,
			String inputIds,
			List<InputBO> inputBOs,
			List<TaskBO> taskBOs,
			List<OutputBO> outputBOs) throws Exception{
		
		AllRequest allRequest = new AllRequest();
		DirectorRequestBO response = new DirectorRequestBO();
		try {
			
			TaskOutputPO output = new TaskOutputPO();
			output.setInputList(inputIds);
			output.setOutput(JSON.toJSONString(outputBOs));
			output.setTask(JSON.toJSONString(taskBOs));
			output.setTaskUuid(taskUuid);
			output.setType(BusinessType.DIRECTOR);
			output.setCapacityIp(capacityIp);
			output.setUpdateTime(new Date());
			
			taskOutputDao.save(output);

			allRequest.setInput_array(new ArrayListWrapper<InputBO>().addAll(inputBOs).getList());
			allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
			allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
			
			//AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, capacityIp, capacityProps.getPort());
			
			response.setCapacityIp(capacityIp);
			//response.setReponse(allResponse);
			response.setRequest(allRequest);
			
			//responseService.allResponseProcess(allResponse);
		
		} catch (Exception e) {
			
			if(!(e instanceof ConstraintViolationException)){
				throw e;
			}
			
		}
		return response;
	}
	
	/**
	 * 批量处理request<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午10:15:38
	 * @param List<DirectorResponseBO> responses
	 */
	public void allRequestProcess(List<DirectorRequestBO> requests) throws Exception{
		
		Set<String> ips = new HashSet<String>();
		for(DirectorRequestBO request: requests){
			ips.add(request.getCapacityIp());
		}
		
		List<DirectorRequestBO> combineRequests = new ArrayList<DirectorRequestBO>();
		try {
			
			for(String ip: ips){
				
				AllRequest request = new AllRequest();
				request.setInput_array(new ArrayList<InputBO>());
				request.setTask_array(new ArrayList<TaskBO>());
				request.setOutput_array(new ArrayList<OutputBO>());
				for(DirectorRequestBO requestBO: requests){
					if(ip.equals(requestBO.getCapacityIp())){
						request.getInput_array().addAll(requestBO.getRequest().getInput_array());
						request.getTask_array().addAll(requestBO.getRequest().getTask_array());
						request.getOutput_array().addAll(requestBO.getRequest().getOutput_array());
					}
				}
				AllResponse allResponse = capacityService.createAllAddMsgId(request, ip, capacityProps.getPort());
				DirectorRequestBO combine = new DirectorRequestBO();
				combine.setCapacityIp(ip);
				combine.setRequest(request);
				combineRequests.add(combine);
				
				responseService.allResponseProcess(allResponse);
			}
			
		} catch (BaseException e) {
			
			//回滚
			for(DirectorRequestBO combine: combineRequests){
				capacityService.deleteAllAddMsgId(combine.getRequest(), combine.getCapacityIp(), capacityProps.getPort());
			}
			
			throw e;
		}
		
	}
	
}
