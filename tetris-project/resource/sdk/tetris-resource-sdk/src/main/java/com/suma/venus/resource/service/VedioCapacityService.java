package com.suma.venus.resource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.VedioCapacityDAO;
import com.suma.venus.resource.pojo.VedioCapacityPO;

@Service
public class VedioCapacityService {

	@Autowired
	private VedioCapacityDAO vedioCapacityDAO;
	
	
	public List<VedioCapacityPO> findAll()throws Exception{
		List<VedioCapacityPO> vedioCapacityPOs = vedioCapacityDAO.findAll();
		if (vedioCapacityPOs == null || vedioCapacityPOs.isEmpty()) {
			VedioCapacityPO vedioCapacityPO = new VedioCapacityPO();
			vedioCapacityPO.setUserCapacity(200l);
			vedioCapacityPO.setVedioCapacity(1024l);;
			vedioCapacityPO.setTurnCapacity(20l);;
			vedioCapacityPO.setReplayCapacity(128l);;
			vedioCapacityDAO.save(vedioCapacityPO);
			vedioCapacityPOs.add(vedioCapacityPO);
		}
		return vedioCapacityPOs;
	}
	
}
