package com.sumavision.tetris.sts.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.sts.task.tasklink.NodeIdDao;
import com.sumavision.tetris.sts.task.tasklink.NodeIdPO;


@Service
public class NodeIdManageUtil {

	@Autowired
	private NodeIdDao nodeIdDao;
	
	public synchronized Long getNewNodeId(){
		Long newId;
		List<NodeIdPO> list = nodeIdDao.findAll();
		if(list.isEmpty()){
			NodeIdPO nodeIdPO = new NodeIdPO();
			nodeIdDao.save(nodeIdPO);
			newId = nodeIdPO.getNodeId();
		}else{
			NodeIdPO nodeIdPO = list.get(0);
			newId = nodeIdPO.getNodeId() + 1;
			nodeIdPO.setNodeId(newId);
			nodeIdDao.save(nodeIdPO);
		}
		return newId;
	}
}
