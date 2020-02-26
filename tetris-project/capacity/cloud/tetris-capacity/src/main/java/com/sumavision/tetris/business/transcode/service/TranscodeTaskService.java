package com.sumavision.tetris.business.transcode.service;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.transcode.bo.CheckInputBO;
import com.sumavision.tetris.business.transcode.vo.AnalysisInputVO;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.capacity.bo.input.BackUpProgramBO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.request.AllRequest;
import com.sumavision.tetris.capacity.bo.request.PutTaskSourceRequest;
import com.sumavision.tetris.capacity.bo.request.ResultCodeResponse;
import com.sumavision.tetris.capacity.bo.response.AllResponse;
import com.sumavision.tetris.capacity.bo.response.AnalysisResponse;
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

/**
 * 流转码<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月12日 下午1:43:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TranscodeTaskService {
	
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
			uniq = inputBO.getFile().getUrl();
		}
		if(inputBO.getUdp_pcm() != null){
			uniq = new StringBuffer().append(inputBO.getUdp_pcm().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getUdp_pcm().getSource_port())
					 				 .toString();
		}
		
		TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
		if(inputPO == null){
			
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
		
		if(transcode.getInput_array().size() == 1){
			
			InputBO inputBO = transcode.getInput_array().iterator().next();
			List<TaskBO> taskBOs = transcode.getTask_array();
			List<OutputBO> outputBOs = transcode.getOutput_array();
			
			String uniq = generateUniq(inputBO);
			
			save(taskUuid, uniq, capacityIp, inputBO, taskBOs, outputBOs, BusinessType.TRANSCODE);
		}else if(transcode.getInput_array().size() > 1){
			InputBO backUpInput = null;
			List<CheckInputBO> checks = new ArrayList<CheckInputBO>(); 
			for(InputBO input: transcode.getInput_array()){
				String uniq = generateUniq(input);
				if(uniq.equals(BACK_UP)){
					backUpInput = input;
					checks.add(transferNormalInput(input, new StringBufferWrapper().append(uniq)
																		.append("-")
																		.append(taskUuid)
																		.toString(), taskUuid));
				}else{
					checks.add(transferNormalInput(input, uniq, taskUuid));
				}
			}
			
			//替换inputId
			if(backUpInput != null){
				if(checks != null && checks.size() > 0){
					List<BackUpProgramBO> programs = new ArrayList<BackUpProgramBO>();
					if(backUpInput.getBack_up_es() != null){
						programs = backUpInput.getBack_up_es().getProgram_array();
					}
					if(backUpInput.getBack_up_passby() != null){
						programs = backUpInput.getBack_up_passby().getProgram_array();
					}
					if(backUpInput.getBack_up_raw() != null){
						programs = backUpInput.getBack_up_raw().getProgram_array();
					}
					for(BackUpProgramBO program: programs){
						for(CheckInputBO check: checks){
							if(check.isExist() && program.getInput_id().equals(check.getReplaceInputId())){
								program.setInput_id(check.getExsitInputId());
								break;
							}
						}
					}
					
					List<Long> inputIds = new ArrayList<Long>();
					List<InputBO> needSendInputs = new ArrayList<InputBO>();
					for(CheckInputBO check: checks){
						inputIds.add(check.getInputId());
						if(!check.isExist()){
							needSendInputs.add(check.getInputBO());
						}
					}
					
					sendProtocal(taskUuid, capacityIp, JSON.toJSONString(inputIds), needSendInputs, transcode.getTask_array(), transcode.getOutput_array());
				}
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
			}else if(output.getInputList() != null){
				
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
			uniq = inputBO.getFile().getUrl();
		}
		if(inputBO.getUdp_pcm() != null){
			uniq = new StringBuffer().append(inputBO.getUdp_pcm().getSource_ip())
					 				 .append("%")
					 				 .append(inputBO.getUdp_pcm().getSource_port())
					 				 .toString();
		}
		if(inputBO.getBack_up_es() != null || inputBO.getBack_up_passby() != null || inputBO.getBack_up_raw() != null){
			uniq = BACK_UP;	
		}
		
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
	public void changeBackUp(String inputId, String index, String ip) throws Exception{
	
		ResultCodeResponse result = capacityService.changeBackUp(inputId, index, ip, capacityProps.getPort());
		
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
				List<TaskInputPO> inputPOs = taskInputDao.findByIdIn(inputIds);
				for(TaskInputPO inputPO: inputPOs){
					if(inputPO.getUniq().contains(BACK_UP)){
						taskInput = inputPO;
						break;
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
					
					capacityService.modifyTaskSourceAddMsgId(task.getId(), source);
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
					
					capacityService.modifyTaskSourceAddMsgId(task.getId(), source);
					
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
	
}
