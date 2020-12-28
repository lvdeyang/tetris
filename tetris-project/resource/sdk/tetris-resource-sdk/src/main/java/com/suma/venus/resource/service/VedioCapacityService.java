package com.suma.venus.resource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.BundleTypeCapacityDAO;
import com.suma.venus.resource.dao.VedioCapacityDAO;
import com.suma.venus.resource.pojo.BundleTypeCapacityPO;
import com.suma.venus.resource.pojo.VedioCapacityPO;

@Service
public class VedioCapacityService {

	@Autowired
	private VedioCapacityDAO vedioCapacityDAO;
	
	@Autowired
	private BundleTypeCapacityDAO bundleTypeCapacityDAO;
	
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
	
	/**
	 * 运维授权接入设备限制<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月25日 下午5:32:13
	 * @param serverNum jv210限制
	 * @param cdnNum cdn限制
	 * @param screenNum mixer限制
	 */
	public void setBundleCapacity(Long serverNum, Long cdnNum, Long screenNum){
		List<BundleTypeCapacityPO> bundleTypeCapacityPOs = bundleTypeCapacityDAO.findAll();
		if(null == bundleTypeCapacityPOs || bundleTypeCapacityPOs.isEmpty()){
			BundleTypeCapacityPO bundleTypeCapacityPO = new BundleTypeCapacityPO();
			bundleTypeCapacityPOs.add(bundleTypeCapacityPO);
		}
		bundleTypeCapacityPOs.get(0).setJv210(serverNum);
		bundleTypeCapacityPOs.get(0).setCdn(cdnNum);
		bundleTypeCapacityPOs.get(0).setMixer(screenNum);
		bundleTypeCapacityDAO.save(bundleTypeCapacityPOs);
	}
}
