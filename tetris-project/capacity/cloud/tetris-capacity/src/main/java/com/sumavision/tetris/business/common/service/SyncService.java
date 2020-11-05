package com.sumavision.tetris.business.common.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.request.AllRequest;
import com.sumavision.tetris.capacity.bo.request.GetEntiretiesResponse;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class SyncService {

	private static final Logger LOG = LoggerFactory.getLogger(SyncService.class);

	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private TaskInputDAO taskInputDao;
	
	@Autowired
	private TaskOutputDAO taskOutputDao;
	
	@Autowired
	private CapacityProps capacityProps;
	
	/**
	 * 转换模块同步<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月8日 上午11:48:12
	 * @param String deviceIp 转换模块ip
	 */
	public void sync(String deviceIp) throws Exception{

		LOG.info("sync start, deviceIp :{}",deviceIp);
		//获取转换模块上全部信息
		GetEntiretiesResponse entirety= capacityService.getEntireties(deviceIp);
		
		List<InputBO> _inputs = entirety.getInput_array();
		List<TaskBO> _tasks = entirety.getTask_array();
		List<OutputBO> _outputs = entirety.getOutput_array();
		
		//获取持久化的全部任务信息
		List<InputBO> inputs = new ArrayList<InputBO>();
		List<TaskBO> tasks = new ArrayList<TaskBO>();
		List<OutputBO> outputs = new ArrayList<OutputBO>();
		
		List<TaskOutputPO> outputPOs = taskOutputDao.findByCapacityIp(deviceIp);
		if(outputPOs != null && outputPOs.size() > 0){
			Set<Long> inputIds = new HashSet<Long>();
			for(TaskOutputPO outputPO: outputPOs){
				
				List<TaskBO> taskBOs = JSONObject.parseArray(outputPO.getTask(), TaskBO.class);
				if(taskBOs != null && taskBOs.size() > 0){
					tasks.addAll(taskBOs);
				}
				
				List<OutputBO> outputBOs = JSONObject.parseArray(outputPO.getOutput(), OutputBO.class);
				if(outputBOs != null && outputBOs.size() > 0){
					outputs.addAll(outputBOs);
				}
				
				if(!StringUtils.isEmpty(outputPO.getInputId())){
					//单源
					inputIds.add(outputPO.getInputId());
				}else if(!StringUtils.isEmpty(outputPO.getInputList())){
					//备份源
					inputIds.addAll(JSONArray.parseArray(outputPO.getInputList(), Long.class));
				}else if(!StringUtils.isEmpty(outputPO.getCoverId())){
					//盖播
					inputIds.add(outputPO.getCoverId());
				}else if(!StringUtils.isEmpty(outputPO.getScheduleId())){
					//排期
					inputIds.add(outputPO.getScheduleId());
				}else if(!StringUtils.isEmpty(outputPO.getPrevId())){
					//追加排期prev
					inputIds.add(outputPO.getPrevId());
				}else if(!StringUtils.isEmpty(outputPO.getNextId())){
					//追加排期next
					inputIds.add(outputPO.getNextId());
				}
			}
			
			List<TaskInputPO> inputPOs = taskInputDao.findByIdIn(inputIds);	
			if(inputPOs != null && inputPOs.size() > 0){
				for(TaskInputPO inputPO: inputPOs){
					if(inputPO.getCount() > 0){
						InputBO inputBO = JSONObject.parseObject(inputPO.getInput(), InputBO.class);
						if(inputBO != null){
							inputs.add(inputBO);
						}
					}
				}
			}
			
		}
		
		//增删校验
		List<InputBO> needAddInputs = new ArrayList<InputBO>();
		List<TaskBO> needAddTasks = new ArrayList<TaskBO>();
		List<OutputBO> needAddOutputs = new ArrayList<OutputBO>();
		List<InputBO> needDeleteInputs = new ArrayList<InputBO>();
		List<TaskBO> needDeleteTasks = new ArrayList<TaskBO>();
		List<OutputBO> needDeleteOutputs = new ArrayList<OutputBO>();
		
		for(InputBO _input: _inputs){
			boolean isExist = false;
			for(InputBO input: inputs){
				if(input.getId().equals(_input.getId())){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				needDeleteInputs.add(_input);
			}
		}
		
		for(TaskBO _task: _tasks){
			boolean isExist = false;
			for(TaskBO task: tasks){
				if(task.getId().equals(_task.getId())){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				needDeleteTasks.add(_task);
			}
		}
		
		for(OutputBO _output: _outputs){
			boolean isExist = false;
			for(OutputBO output: outputs){
				if(output.getId().equals(_output.getId())){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				needDeleteOutputs.add(_output);
			}
		}
		
		for(InputBO input: inputs){
			boolean isExist = false;
			for(InputBO _input: _inputs){
				if(_input.getId().equals(input.getId())){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				needAddInputs.add(input);
			}
		}
		
		for(TaskBO task: tasks){
			boolean isExist = false;
			for(TaskBO _task: _tasks){
				if(_task.getId().equals(task.getId())){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				needAddTasks.add(task);
			}
		}
		
		for(OutputBO output: outputs){
			boolean isExist = false;
			for(OutputBO _output: _outputs){
				if(_output.getId().equals(output.getId())){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				needAddOutputs.add(output);
			}
		}
		
		LOG.info("sync send cmd, deviceIp :{}",deviceIp);
		//新增
		if(needAddInputs.size() > 0 || needAddTasks.size() > 0 || needAddOutputs.size() > 0){
			AllRequest add = new AllRequest();
			if(needAddInputs.size() > 0){
				add.setInput_array(new ArrayListWrapper<InputBO>().addAll(needAddInputs).getList());
			}
			if(needAddTasks.size() > 0){
				add.setTask_array(new ArrayListWrapper<TaskBO>().addAll(needAddTasks).getList());
			}
			if(needAddOutputs.size() > 0){
				add.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(needAddOutputs).getList());
			}
			capacityService.createAllAddMsgId(add, deviceIp, capacityProps.getPort());
		}
		
		//删除
		if(needDeleteInputs.size() > 0 || needDeleteTasks.size() > 0 || needDeleteOutputs.size() > 0){
			AllRequest delete = new AllRequest();
			if(needDeleteInputs.size() > 0){
				delete.setInput_array(new ArrayListWrapper<InputBO>().addAll(needDeleteInputs).getList());
			}
			if(needDeleteTasks.size() > 0){
				delete.setTask_array(new ArrayListWrapper<TaskBO>().addAll(needDeleteTasks).getList());
			}
			if(needDeleteOutputs.size() > 0){
				delete.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(needDeleteOutputs).getList());
			}
			capacityService.deleteAllAddMsgId(delete, deviceIp, capacityProps.getPort());
		}
		LOG.info("sync end, deviceIp :{}",deviceIp);
	}
}
