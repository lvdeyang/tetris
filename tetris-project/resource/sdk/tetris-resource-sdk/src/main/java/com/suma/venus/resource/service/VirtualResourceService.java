package com.suma.venus.resource.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.VirtualResourceDao;
import com.suma.venus.resource.pojo.VirtualResourcePO;

@Service
public class VirtualResourceService extends CommonService<VirtualResourcePO>{

	@Autowired
	private VirtualResourceDao virtualResourceDao;
	
	public List<VirtualResourcePO> findByResourceId(String resourceId){
		
		return virtualResourceDao.findByResourceId(resourceId);
	}
	
	public List<VirtualResourcePO> findByAttrNameAndAttrValue(String attrName,String attrValue){
		
		return virtualResourceDao.findByAttrNameAndAttrValue(attrName, attrValue);
	}
	
	public VirtualResourcePO findByResourceIdAndAttrName(String resourceId,String attrName){
		
		return virtualResourceDao.findByResourceIdAndAttrName(resourceId, attrName);
	}
	
	public Set<String> queryAllResourceId(){
		return virtualResourceDao.queryAllResourceId();
	}
	
	public Set<String> queryResourceIdByAttrNameAndAttrValue(String attrName,String attrValue){
		
		return virtualResourceDao.queryResourceIdByAttrNameAndAttrValue(attrName, attrValue);
	}
	
	public Set<String> queryResourceIdByKeyword(String keyword){
		
		return virtualResourceDao.queryResourceIdByKeyword(keyword);
	}
	
	//根据同一resourceId将散列的虚拟资源字段PO封装成一个json实体
	public JSONObject packageResource(String resourceId){
		List<VirtualResourcePO> virtualResourcePOs = virtualResourceDao.findByResourceId(resourceId);
		if(virtualResourcePOs == null || virtualResourcePOs.isEmpty()){
			return null;
		}
		
		JSONObject jsonObj = new JSONObject();
		for (VirtualResourcePO virtualResourcePO : virtualResourcePOs) {
			jsonObj.put(virtualResourcePO.getAttrName(), virtualResourcePO.getAttrValue());
		}
		
		jsonObj.put("resourceId", resourceId);
		return jsonObj;
	}
}
