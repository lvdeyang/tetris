package com.sumavision.tetris.business.package5g.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.capacity.bo.input.CommonTsBO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.ProgramAudioBO;
import com.sumavision.tetris.capacity.bo.input.ProgramBO;
import com.sumavision.tetris.capacity.bo.input.ProgramVideoBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.output.OutputRtpEsBO;
import com.sumavision.tetris.capacity.bo.output.OutputRtpesMediaBO;
import com.sumavision.tetris.capacity.bo.request.AllRequest;
import com.sumavision.tetris.capacity.bo.response.AllResponse;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.bo.task.TaskSourceBO;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.capacity.service.ResponseService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class PackageService {
	
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

	public String addTask(
			String srcIp, 
			String srcPort, 
			String dstIp, 
			String dstPort) throws Exception{
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		
		String uniq = new StringBufferWrapper().append(srcIp)
											   .append("%")
											   .append(srcPort)
											   .toString();
		
		save(uuid, uniq, srcIp, srcPort, dstIp, dstPort, BusinessType.PACKAGE);
		
		return uuid;
	}
	
	public void delete(String taskUuid) throws Exception {
		
		TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskUuid, BusinessType.PACKAGE);
		
		if(output != null){
			
			TaskInputPO input = taskInputDao.findOne(output.getInputId());
			
			if(input != null){
				
				try {

					input.setUpdateTime(new Date());
					if(input.getCount() >= 1){
						input.setCount(input.getCount() - 1);
					}
					taskInputDao.save(input);
					
					AllRequest allRequest = new AllRequest();
					
					OutputBO outputBO = JSONObject.parseObject(output.getOutput(), OutputBO.class);
					List<TaskBO> tasks = JSONObject.parseArray(output.getTask(), TaskBO.class);
					InputBO inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
					
					if(input.getCount().equals(0) && input.getInput() != null){
						allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());
					}
					if(tasks != null){
						allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(tasks).getList());
					}
					if(outputBO != null){
						allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().add(outputBO).getList());
					}
				
					capacityService.deleteAllAddMsgId(allRequest, capacityProps.getIp(), capacityProps.getPort());
					
					output.setOutput(null);
					output.setTask(null);
					
					taskOutputDao.delete(output);
					
				} catch (ObjectOptimisticLockingFailureException e) {
					
					// 版本不对，version校验
					System.out.println("delete校验version版本不对");
					Thread.sleep(300);
					delete(taskUuid);
				}
			}
		}
	}
	
	public void save(
			String taskUuid,
			String uniq,
			String srcIp, 
			String srcPort, 
			String dstIp, 
			String dstPort,
			BusinessType businessType) throws Exception{
		
		TaskInputPO input = taskInputDao.findByUniq(uniq);
		
		if(input == null){
			
			AllRequest allRequest = new AllRequest();
			InputBO inputBO = new InputBO();
			List<TaskBO> taskBOs = new ArrayList<TaskBO>();
 			List<OutputBO> outputBOs = new ArrayList<OutputBO>();
			try {
				
				//转输入
				inputBO = package2InputBO(taskUuid, srcIp, srcPort);
				
				//转任务
				taskBOs = package2Tasks(taskUuid, inputBO);
				
				//转输出
				outputBOs = package2OutputBO(taskUuid, taskBOs, srcIp, dstIp, dstPort);
				
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
				output.setCapacityIp(srcIp);
				output.setUpdateTime(new Date());
				
				taskOutputDao.save(output);

				allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());
				allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
				allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
				
				AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, srcIp, capacityProps.getPort());
				
				responseService.allResponseProcess(allResponse);
			
			} catch (ConstraintViolationException e) {
				
				//数据已存在（ip，port校验）
				System.out.println("校验输入已存在");
				Thread.sleep(300);
				save(taskUuid, uniq, srcIp, srcPort, dstIp, dstPort, businessType);
				
			} catch (BaseException e){
				
				capacityService.deleteAllAddMsgId(allRequest, srcIp, capacityProps.getPort());
				throw e;
				
			} catch (Exception e) {
				
				if(!(e instanceof ConstraintViolationException)){
					throw e;
				}
				
			}
		}else{
			
			AllRequest allRequest = new AllRequest();
			InputBO inputBO = new InputBO();
			List<TaskBO> taskBOs = new ArrayList<TaskBO>();
 			List<OutputBO> outputBOs = new ArrayList<OutputBO>();
			try {
				
				if(input.getCount().equals(0)){
					//转输入
					inputBO = package2InputBO(taskUuid, srcIp, srcPort);
				}else{
					inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
				}
				
				//转任务
				taskBOs = package2Tasks(taskUuid, inputBO);
				
				//转输出
				outputBOs = package2OutputBO(taskUuid, taskBOs, srcIp, dstIp, dstPort);
				
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
				output.setCapacityIp(srcIp);
				output.setUpdateTime(new Date());
				
				taskOutputDao.save(output);
				
				if(input.getCount().equals(1)){
					
					allRequest.setInput_array(new ArrayListWrapper<InputBO>().add(inputBO).getList());

				}
				
				allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(taskBOs).getList());
				allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
				
				AllResponse allResponse = capacityService.createAllAddMsgId(allRequest, srcIp, capacityProps.getPort());
				
				responseService.allResponseProcess(allResponse);
							
			} catch (ObjectOptimisticLockingFailureException e) {
				
				// 版本不对，version校验
				System.out.println("save校验version版本不对");
				Thread.sleep(300);
				save(taskUuid, uniq, srcIp, srcPort, dstIp, dstPort, businessType);
				
			} catch (BaseException e){
				
				capacityService.deleteAllAddMsgId(allRequest, srcIp, capacityProps.getPort());
				throw e;
				
			} catch (Exception e) {
				
				if(!(e instanceof ObjectOptimisticLockingFailureException)){
					throw e;
				}
			}
			
		}
	}
	
	public InputBO package2InputBO(
			String id, 
			String srcIp, 
			String srcPort) throws Exception{
		
		String inputId = new StringBufferWrapper().append("input-")
												  .append(id)
												  .toString();
		
		InputBO inputBO = new InputBO();
		
		CommonTsBO udp_ts = new CommonTsBO().setSource_ip(srcIp)
											.setSource_port(Integer.valueOf(srcPort));
		
		inputBO.setId(inputId)
			   .setUdp_ts(udp_ts)
			   .setMedia_type_once_map(new JSONObject())
			   .setProgram_array(new ArrayList<ProgramBO>());

		ProgramBO program = new ProgramBO().setProgram_number(1)
										   .setVideo_array(new ArrayList<ProgramVideoBO>())
										   .setAudio_array(new ArrayList<ProgramAudioBO>());
		
		ProgramVideoBO video = new ProgramVideoBO().setPid(513);
		ProgramAudioBO audio = new ProgramAudioBO().setPid(514);
		
		program.getVideo_array().add(video);
		program.getAudio_array().add(audio);
		
		inputBO.getProgram_array().add(program);
		
		return inputBO;
	}
	
	public List<TaskBO> package2Tasks(String id, InputBO input) throws Exception{
		
		String videoTaskId = new StringBufferWrapper().append("task-video-")
													  .append(id)
													  .toString();
		
		String audioTaskId = new StringBufferWrapper().append("task-audio-")
													  .append(id)
													  .toString();
		
		String encodeVideoId = new StringBufferWrapper().append("encode-video-")
													    .append(id)
													    .toString();
		
		String encodeAudioId = new StringBufferWrapper().append("encode-audio-")
													    .append(id)
													    .toString();
		
		List<TaskBO> tasks = new ArrayList<TaskBO>();
		
		//视频
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
		
		//音频
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
		
		return tasks;
	}
	
	public List<OutputBO> package2OutputBO(
			String id,
			List<TaskBO> tasks,
			String srcIp,
			String dstIp,
			String dstPort) throws Exception{
		
		List<OutputBO> outputs = new ArrayList<OutputBO>();
		
		//TODO：参数不明确
		for(TaskBO taskBO: tasks){
			
			if(taskBO.getId().contains("video")){
				
				OutputRtpesMediaBO media = new OutputRtpesMediaBO().setTask_id(taskBO.getId())
																   .setEncode_id(taskBO.getEncode_array().iterator().next().getEncode_id())
																   .setPayload_type(96);
				
				String outputId = new StringBufferWrapper().append("output-")
														   .append("video")
														   .append("-")
														   .append(id)
														   .toString();
				
				OutputBO output = new OutputBO();			
				output.setId(outputId);
				
				OutputRtpEsBO rtp_es = new OutputRtpEsBO().setIp(dstIp)
														  .setPort(Integer.valueOf(dstPort))
														  .setLocal_ip(srcIp)
														  .setMedia(media);
				
				output.setRtp_es(rtp_es);
				
				outputs.add(output);
			}
			
			if(taskBO.getType().equals("audio")){
				
				OutputRtpesMediaBO media = new OutputRtpesMediaBO().setTask_id(taskBO.getId())
																   .setEncode_id(taskBO.getEncode_array().iterator().next().getEncode_id())
																   .setPayload_type(97);
				
				String outputId = new StringBufferWrapper().append("output-")
														   .append("audio")
														   .append("-")
														   .append(id)
														   .toString();
				
				OutputBO output = new OutputBO();			
				output.setId(outputId);
				
				OutputRtpEsBO rtp_es = new OutputRtpEsBO().setIp(dstIp)
														  .setPort(Integer.valueOf(dstPort))
														  .setLocal_ip(srcIp)
														  .setMedia(media);
				
				output.setRtp_es(rtp_es);
				
				outputs.add(output);
			}
		}
		
		return outputs;
	}
	
	
}
