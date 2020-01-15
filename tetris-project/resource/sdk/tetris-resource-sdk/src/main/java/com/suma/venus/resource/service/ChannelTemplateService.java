package com.suma.venus.resource.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.pojo.ChannelTemplatePO;

@Service
public class ChannelTemplateService extends CommonService<ChannelTemplatePO>{

	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelTemplateService.class);
	
	@Autowired
	private ChannelTemplateDao channelTemplateDao;
	
	public List<ChannelTemplatePO> findByDeviceModel(String deviceModel){
		
		return channelTemplateDao.findByDeviceModel(deviceModel);
	}
	
	public List<ChannelTemplatePO> findByChannelName(String channelName){
		
		return channelTemplateDao.findByChannelName(channelName);
	}
	
	public ChannelTemplatePO findByDeviceModelAndChannelName(String deviceModel,String channelName){
		
		return channelTemplateDao.findByDeviceModelAndChannelName(deviceModel, channelName);
	}
	
	public List<ChannelTemplatePO> findByBaseType(String baseType){
		
		return channelTemplateDao.findByBaseType(baseType);
	}
	
	public List<ChannelTemplatePO> findByExternType(String externType){
		
		return channelTemplateDao.findByExternType(externType);
	}
	
	public List<ChannelTemplatePO> findByKeyword(String keyword){
		if(null == keyword || keyword.isEmpty()){
			return channelTemplateDao.findAll();
		}
		
		return channelTemplateDao.findByKeyword(keyword);
	}

	public Set<String> findAllDeviceModel(){
		return channelTemplateDao.findAllDeviceModel();
	}
}
