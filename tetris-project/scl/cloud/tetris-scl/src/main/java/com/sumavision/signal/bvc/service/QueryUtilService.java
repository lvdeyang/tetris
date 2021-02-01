package com.sumavision.signal.bvc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;

@Service
@Transactional(rollbackFor = Exception.class)
public class QueryUtilService {

    /** 查询编码端口映射 */
    public PortMappingPO queryEncodePortMappingByChannel(List<PortMappingPO> mappings, String bundleId, String channelId){
    	for(PortMappingPO mapping: mappings){
    		if(mapping.getSrcBundleId() != null && mapping.getSrcBundleId().equals(bundleId) && mapping.getSrcChannelId() != null && mapping.getSrcChannelId().equals(channelId)){
    			return mapping;
    		}
    	}
    	return null;
    }
    
    /** 查询解码端口映射 */
    public PortMappingPO queryDecodePortMappingByChannel(List<PortMappingPO> mappings, String bundleId, String channelId){
    	for(PortMappingPO mapping: mappings){
    		if(mapping.getDstBundleId() != null && mapping.getDstBundleId().equals(bundleId) && mapping.getDstChannelId() != null && mapping.getDstChannelId().equals(channelId)){
    			return mapping;
    		}
    	}
    	return null;
    }
    
    /** 查询源映射 */
    public PortMappingPO querySrcPortMapping(List<PortMappingPO> mappings, String bundleId, String channelId, SrcType srcType){
    	for(PortMappingPO mapping: mappings){
    		if(mapping.getSrcType() != null && mapping.getSrcType().equals(srcType) && mapping.getSrcBundleId() != null && mapping.getSrcChannelId() != null && mapping.getSrcBundleId().equals(bundleId) && mapping.getSrcChannelId().equals(channelId)){
    			return mapping;
    		}
    	}
    	return null;
    }
    
    /** 查询源映射 */
    public PortMappingPO querySrcPortMapping(List<PortMappingPO> mappings, String bundleId, String channelId){
    	for(PortMappingPO mapping: mappings){
    		if(mapping.getSrcBundleId() != null && mapping.getSrcChannelId() != null && mapping.getSrcBundleId().equals(bundleId) && mapping.getSrcChannelId().equals(channelId)){
    			return mapping;
    		}
    	}
    	return null;
    }
    
    /** 查询目的映射 */
    public PortMappingPO queryDstPortMapping(List<PortMappingPO> mappings, String bundleId, String channelId){
    	for(PortMappingPO mapping: mappings){
    		if(mapping.getDstType() != null && mapping.getDstType().equals(DstType.TERMINAL) && mapping.getDstBundleId() != null && mapping.getDstChannelId() != null && mapping.getDstBundleId().equals(bundleId) && mapping.getDstChannelId().equals(channelId)){
    			return mapping;
    		}
    	}
    	return null;
    }
    
    /** 根据mappingId查询已创建任务 */
    public List<TaskPO> queryTaskByMappingId(List<TaskPO> tasks, Long id){
    	List<TaskPO> taskPOs = new ArrayList<TaskPO>();
    	for(TaskPO task: tasks){
    		if(task.getMappingId().equals(id) && task.getStatus().equals(TaskStatus.zero.getStatus())){
    			taskPOs.add(task);
    		}
    	}
    	
    	return taskPOs;
    }
    
    /** 根据bundleId查询bind */
    public TerminalBindRepeaterPO queryBindByBundleId(List<TerminalBindRepeaterPO> binds, String bundleId){
    	if(binds != null && binds.size() > 0){
    		for(TerminalBindRepeaterPO bind: binds){
    			if(bind.getBundleId() != null && bind.getBundleId().equals(bundleId)){
    				return bind;
    			}
    		}
    	}
    	return null;
    }
    
    /** 根据源channelId查询 */
    public PortMappingPO queryMappingBySrcChannelId(List<PortMappingPO> mappings, String channelId){
    	if(mappings != null && mappings.size() > 0){
    		for(PortMappingPO mapping: mappings){
    			if(mapping.getSrcChannelId() != null && mapping.getSrcChannelId().equals(channelId)){
    				return mapping;
    			}
    		}
    	}
    	return null;
    }
    
    /** 判断任务是否已有 */
    public boolean isTask(List<TaskPO> tasks, Long mappingId){
    	if(tasks != null && tasks.size() > 0){
    		for(TaskPO task: tasks){
    			if(task.getMappingId().equals(mappingId)){
    				return true;
    			}
    		}
    	}
		return false;
	}
    
    /** 根据mappingId和集群ip查询任务 */
    public TaskPO queryTask(List<TaskPO> tasks, Long mappingId, String ip){
    	if(tasks != null && tasks.size() > 0 && mappingId != null && ip != null){
    		for(TaskPO task: tasks){
    			if(task.getMappingId() != null && task.getIp() != null && task.getMappingId().equals(mappingId) && task.getIp().equals(ip)){
    				return task;
    			}
    		}
    	}
    	return null;
    }
    
    /** 根据taskId查询任务 */
    public TaskPO queryTaskByTaskId(Collection<TaskPO> tasks, String taskId){
    	if(tasks != null && tasks.size() > 0 && taskId != null){
    		for(TaskPO task: tasks){
    			if(task.getTaskId() != null && task.getTaskId().equals(taskId)){
    				return task;
    			}
    		}
    	}
    	
    	return null;
    }
    
}
