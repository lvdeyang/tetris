package com.sumavision.tetris.easy.process.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointProcessPermissionDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointProcessPermissionPO;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessService {

	@Autowired
	private AccessPointProcessPermissionDAO accessPointProcessPermissionDao;
	
	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private ProcessVariableDAO processVariableDao;
	
	/**
	 * 保存流程的bpmn配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月28日 下午4:07:01
	 * @param ProcessPO process 流程
	 * @param String bpmn bpmn配置
	 * @param Collection<AccessPointPO> accessPoints 选中的接入点
	 * @return ProcessPO 流程数据
	 */
	public ProcessPO saveBpmn(
			ProcessPO process, 
			String bpmn,
			Collection<AccessPointPO> accessPoints) throws Exception{
		
		List<AccessPointProcessPermissionPO> permissions = accessPointProcessPermissionDao.findByProcessId(process.getId());
		if(permissions!=null && permissions.size()>0){
			accessPointProcessPermissionDao.deleteInBatch(permissions);
		}
		
		permissions = new ArrayList<AccessPointProcessPermissionPO>();
		if(accessPoints!=null && accessPoints.size()>0){
			for(AccessPointPO accessPoint:accessPoints){
				AccessPointProcessPermissionPO permission = new AccessPointProcessPermissionPO();
				permission.setProcessId(process.getId());
				permission.setAccessPointId(accessPoint.getId());
				permission.setUpdateTime(new Date());
				permissions.add(permission);
			}
			accessPointProcessPermissionDao.save(permissions);
		}
		
		process.setBpmn(bpmn);
		process.setUpdateTime(new Date());
		processDao.save(process);
		
		return process;
	}
	
	/**
	 * 删除流程<br/>
	 * <p>
	 * 	删除流程与接入点关联信息<br/>
	 * 	删除流程下变量列表<br/>
	 *  删除流程<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午3:10:58
	 * @param ProcessPO process 待删流程
	 */
	public void delete(ProcessPO process) throws Exception{
		
		List<AccessPointProcessPermissionPO> permissions = accessPointProcessPermissionDao.findByProcessId(process.getId());
		if(permissions!=null && permissions.size()>0){
			accessPointProcessPermissionDao.deleteInBatch(permissions);
		}
		
		List<ProcessVariablePO> variables = processVariableDao.findByProcessId(process.getId());
		if(variables!=null && variables.size()>0){
			processVariableDao.deleteInBatch(variables);
		}
		
		processDao.delete(process);
	}
	
}
