package com.sumavision.tetris.business.director.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.director.bo.InputCheckBO;
import com.sumavision.tetris.business.director.vo.DirectorTaskVO;
import com.sumavision.tetris.business.director.vo.SourceVO;
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
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class DirectorTaskService {
	
	@Autowired
	private TaskInputDAO taskInputDao;
	
	public void addDirectorTask(List<DirectorTaskVO> directors) throws Exception{
		
		for(DirectorTaskVO director: directors){
			String taskId = director.getTaskId();
			List<SourceVO> sources = director.getSources();
			
			List<InputBO> needCreateInputs = new ArrayList<InputBO>();
			List<InputBO> allInputs = new ArrayList<InputBO>();
			for(SourceVO source: sources){
				InputCheckBO check = transferNormalInput(source, taskId);
				allInputs.add(check.getInput());
				if(!check.isExsit()){
					needCreateInputs.add(check.getInput());
				}
			}
			
			//生成备份源
			InputBO backup = director2BackupInputBO(taskId, allInputs);
		}
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
	public InputBO director2BackupInputBO(String taskId, List<InputBO> sources) throws Exception {

		String backInputId = new StringBufferWrapper().append("backup-").append(taskId).toString();

		BackUpEsAndRawBO back_up_es = new BackUpEsAndRawBO().setSelect_index("0").setMode("manual");
		back_up_es.setProgram_array(new ArrayList<BackUpProgramBO>());
		int count=0;
		for (InputBO source : sources) {
			
			String inputId = source.getId();

			ProgramElementBO velementBO = new ProgramElementBO().setPid(1).setProgram_switch_array(
					new ArrayListWrapper<PidIndexBO>().addAll(generatePidIndex(2, 0)).getList());
			ProgramElementBO aelementBO = new ProgramElementBO().setPid(2).setProgram_switch_array(
					new ArrayListWrapper<PidIndexBO>().addAll(generatePidIndex(2, 1)).getList());
			List<ProgramElementBO> elementBOs = new ArrayList<ProgramElementBO>();
			elementBOs.add(velementBO);
			elementBOs.add(aelementBO);
			BackUpProgramBO backupPro = new BackUpProgramBO().setInput_id(inputId).setProgram_number(1)
					.setElement_array(new ArrayListWrapper<ProgramElementBO>().addAll(elementBOs).getList());
			back_up_es.getProgram_array().add(backupPro);
			ProgramOutputBO outPro = new ProgramOutputBO().setProgram_number(1).setElement_array(elementBOs);
			if(count==0){
				back_up_es.setOutput_program(outPro);
			}
			count++;
		}
		
		// 创建输入
		InputBO input = new InputBO().setBack_up_raw(back_up_es).setId(backInputId)
				.setProgram_array(new ArrayList<ProgramBO>()).setNormal_map(new JSONObject());
		ProgramBO program = new ProgramBO().setProgram_number(1).setVideo_array(new ArrayList<ProgramVideoBO>())
				.setAudio_array(new ArrayList<ProgramAudioBO>());

		ProgramVideoBO video = new ProgramVideoBO().setPid(2);
		ProgramAudioBO audio = new ProgramAudioBO().setPid(1);

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
	 * @return InputCheckBO
	 */
	public InputCheckBO transferNormalInput(SourceVO source, String taskUuid) throws Exception{
		
		String uniq = new StringBufferWrapper().append(source.getIp())
											   .append("@")
											   .append(source.getPort())
											   .toString();
		
		TaskInputPO input = taskInputDao.findByUniq(uniq);
		InputCheckBO check = new InputCheckBO();
		boolean isExsit = false;
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
					isExsit = true;
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
		
		check.setExsit(isExsit);
		check.setInput(inputBO);
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

		ProgramVideoBO video = new ProgramVideoBO().setPid(2)
								   				   .setDecode_mode("cpu");
		ProgramAudioBO audio = new ProgramAudioBO().setPid(1)
											       .setDecode_mode("cpu");
		
		program.getVideo_array().add(video);
		program.getAudio_array().add(audio);
		
		inputBO.getProgram_array().add(program);
		
		return inputBO;
	}
	
}
