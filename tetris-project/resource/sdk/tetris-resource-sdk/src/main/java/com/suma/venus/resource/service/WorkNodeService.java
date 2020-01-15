package com.suma.venus.resource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;

@Service
public class WorkNodeService extends CommonService<WorkNodePO>{

	@Autowired
	private WorkNodeDao workNodeDao;
	
	public List<WorkNodePO> findByIp(String ip){
		
		return workNodeDao.findByIp(ip);
	}
	
	public WorkNodePO findByNodeUid(String nodeUid){
		
		return workNodeDao.findByNodeUid(nodeUid);
	}
	
	public List<WorkNodePO> findByType(NodeType type){
		
		return workNodeDao.findByType(type);
	}
	
	public List<WorkNodePO> findByNameLike(String name){
		
		return workNodeDao.findByNameLike(name);
	}
}
