package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;

@Service
public class WorkNodeService extends CommonService<WorkNodePO>{

	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private BundleDao bundleDao;
	
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
	
	/**
	 * 均匀分配原则原则接入层<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 上午8:49:16
	 * @param List<WorkNodePO> nodes 所有接入层
	 * @return WorkNodePO 选择出来的接入层
	 */
	public WorkNodePO choseWorkNode(List<WorkNodePO> nodes) throws Exception{
		
		WorkNodePO chose = null;
		
		if(nodes != null && nodes.size() > 0){
			List<String> layerIds = new ArrayList<String>();
			for(WorkNodePO node: nodes){
				layerIds.add(node.getNodeUid());
			}
			List<BundlePO> bundles = bundleDao.findByAccessNodeUidIn(layerIds);
			
			int count = 0;
			for(WorkNodePO node: nodes){
				int number = 0;
				for(BundlePO bundle: bundles){
					if(bundle.getAccessNodeUid().equals(node.getNodeUid())){
						number ++;
					}
				}
				if(number > count){
					chose = node;
					count = number;
				}
			}
		}
		
		return chose;
	}
}
