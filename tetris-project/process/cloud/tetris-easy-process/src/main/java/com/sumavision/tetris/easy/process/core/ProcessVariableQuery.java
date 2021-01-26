package com.sumavision.tetris.easy.process.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.easy.process.access.point.AccessPointParamDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointProcessPermissionDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointProcessPermissionPO;
import com.sumavision.tetris.easy.process.access.point.ParamDirection;

@Component
public class ProcessVariableQuery {

	@Autowired
	private ProcessVariableDAO processVariableDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private AccessPointProcessPermissionDAO accessPointProcessPermissionDao;
	
	/**
	 * 分页查询流程下的变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 上午10:29:05
	 * @param Long processId 流程id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ProcessVariablePO> 变量列表
	 */
	public List<ProcessVariablePO> findByProcessId(Long processId, int currentPage, int pageSize) throws Exception{
		Pageable page = PageRequest.of(currentPage - 1,  pageSize);
		Page<ProcessVariablePO> entities = processVariableDao.findByProcessId(processId, page);
		return entities.getContent();
	}
	
	/**
	 * 查询内置流程变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 上午9:37:23
	 * @return List<ProcessVariableVO> 变量列表
	 */
	public List<ProcessVariableVO> findInternalVariable() throws Exception{
		
		List<ProcessVariableVO> variables = new ArrayList<ProcessVariableVO>();
		
		//流程id
		ProcessVariableVO processInstanceId = new ProcessVariableVO().setPrimaryKey(InternalVariableKey.PROCESS_INSTANCE_ID.getVariableKey())
															 		 .setName(InternalVariableKey.PROCESS_INSTANCE_ID.getName())
															 		 .setOrigin(ProcessVariableOrigin.INTERNAL.getName());
		variables.add(processInstanceId);
		
		//启动用户id
		ProcessVariableVO startUserId = new ProcessVariableVO().setPrimaryKey(InternalVariableKey.START_USER_ID.getVariableKey())
														  .setName(InternalVariableKey.START_USER_ID.getName())
														  .setOrigin(ProcessVariableOrigin.INTERNAL.getName());
		variables.add(startUserId);
		
		//接入点id--这个变量不需要存在流程中--在调用接入点时直接获取
		/*ProcessVariableVO accessPointId = new ProcessVariableVO().setPrimaryKey(InternalVariableKey.ACCESSPOINTID.getVariableKey())
															     .setName(InternalVariableKey.ACCESSPOINTID.getName())
															     .setOrigin(ProcessVariableOrigin.INTERNAL.getName());
		variables.add(accessPointId);*/
		
		//xxxx
		
		return variables;
	}
	
	/**
	 * 查询流程下自定义变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 上午9:39:18
	 * @param Long processId 流程id
	 * @return List<ProcessVariableVO> 变量列表
	 */
	public List<ProcessVariableVO> findCustomVariable(Long processId) throws Exception{
		List<ProcessVariablePO> entities = processVariableDao.findByProcessId(processId);
		List<ProcessVariableVO> variables = new ArrayList<ProcessVariableVO>();
		if(entities!=null && entities.size()>0){
			for(ProcessVariablePO entity:entities){
				ProcessVariableVO variable = new ProcessVariableVO().set(entity, ProcessVariableOrigin.CUSTOM.getName());
				variables.add(variable);
			}
		}
		return variables;
	}
	
	/**
	 * 查询流程内接入点参数变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 上午9:42:43
	 * @param Long processId 流程id
	 * @return List<ProcessVariableVO> 变量列表
	 */
	public List<ProcessVariableVO> findAccessPointVariable(Long processId) throws Exception{
		List<ProcessVariableVO> variables = new ArrayList<ProcessVariableVO>();
		List<AccessPointProcessPermissionPO> permissions = accessPointProcessPermissionDao.findByProcessId(processId);
		if(permissions!=null && permissions.size()>0){
			List<Long> accessPointIds = new ArrayList<Long>();
			for(AccessPointProcessPermissionPO permission:permissions){
				accessPointIds.add(permission.getAccessPointId());
			}
			List<AccessPointParamPO> params = accessPointParamDao.findByAccessPointIdInAndDirection(accessPointIds, ParamDirection.FORWARD);
			if(params!=null && params.size()>0){
				for(AccessPointParamPO param:params){
					ProcessVariableVO variable = new ProcessVariableVO().set(param);
					variables.add(variable);
				}
			}
		}
		return variables;
	}
	
	/**
	 * 获取流程全部必要变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 上午9:53:28
	 * @param Long processId 流程id
	 * @return List<ProcessVariableVO> 变量列表
	 */
	public List<ProcessVariableVO> findProcessTotalVariables(Long processId) throws Exception{
		List<ProcessVariableVO> variables = new ArrayList<ProcessVariableVO>();
		
		//用户自定义参数
		List<ProcessVariableVO> customVariables = findCustomVariable(processId);
		if(customVariables!=null && customVariables.size()>0){
			variables.addAll(customVariables);
		}
		
		//内置参数
		List<ProcessVariableVO> internalVariables = findInternalVariable();
		if(internalVariables!=null && internalVariables.size()>0){
			variables.addAll(internalVariables);
		}
		
		//接入点参数
		List<ProcessVariableVO> accessPointVariables = findAccessPointVariable(processId);
		if(accessPointVariables!=null && accessPointVariables.size()>0){
			variables.addAll(accessPointVariables);
		}
		return variables;
	}
	
}
