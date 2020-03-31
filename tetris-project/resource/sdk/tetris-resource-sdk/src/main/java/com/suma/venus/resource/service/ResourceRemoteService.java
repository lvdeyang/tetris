package com.suma.venus.resource.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.suma.venus.resource.bo.DeviceInfoBO;


@Service
public class ResourceRemoteService {

	public String queryLocalLayerId() throws Exception{
		
		return "lvdeyang";
	}
	
	public List<String> querySerNodeList(List<DeviceInfoBO> devices) throws Exception{
		
		return null;
	}
	
}
