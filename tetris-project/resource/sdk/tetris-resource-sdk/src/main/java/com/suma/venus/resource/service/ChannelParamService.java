package com.suma.venus.resource.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.dao.ChannelParamDao;
import com.suma.venus.resource.pojo.ChannelParamPO;

@Service
public class ChannelParamService extends CommonService<ChannelParamPO> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelParamService.class);
	
	@Autowired
	private ChannelParamDao channelParamDao;

	public List<ChannelParamPO> findByParentChannelTemplateId(Long parentChannelTemplateId) {
		return channelParamDao.findByParentChannelTemplateId(parentChannelTemplateId);
	}

	public List<ChannelParamPO> findByParentChannelParamId(Long parentChannelParamId) {
		return channelParamDao.findByParentChannelParamId(parentChannelParamId);
	}
	
	ChannelParamPO findByParentChannelTemplateIdAndParamName(Long parentChannelTemplateId,String paramName){
		return channelParamDao.findByParentChannelTemplateIdAndParamName(parentChannelTemplateId, paramName);
	}
	
	ChannelParamPO findByParamNameAndConstValueAndParamScope(String paramName,String constValue,ParamScope paramScope){
		return channelParamDao.findByParamNameAndConstValueAndParamScope(paramName, constValue, paramScope);
	}

	/**
	 * 根据parentChannelParamId删除所有后代ChannelParam
	 */
	public void deleteDescendantParam(Long parentChannelParamId){
		List<ChannelParamPO> childParamPOs = findByParentChannelParamId(parentChannelParamId);
		if(!childParamPOs.isEmpty()){
			for (ChannelParamPO childParamPO : childParamPOs) {
				deleteParamAndDescendant(childParamPO);
			}
		}
	}

	/**
	 * 删除parampo及其所有后代parampo
	 */
	public void deleteParamAndDescendant(ChannelParamPO paramPO) {
		List<ChannelParamPO> childParamPOs = findByParentChannelParamId(paramPO.getId());
		if(!childParamPOs.isEmpty()){
			for (ChannelParamPO childParamPO : childParamPOs) {
				deleteParamAndDescendant(childParamPO);
			}
		} 
		
		delete(paramPO);
	}
}
