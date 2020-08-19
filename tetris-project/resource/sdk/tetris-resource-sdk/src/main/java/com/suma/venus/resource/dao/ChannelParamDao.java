package com.suma.venus.resource.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.pojo.ChannelParamPO;

@RepositoryDefinition(domainClass = ChannelParamPO.class, idClass = Long.class)
public interface ChannelParamDao extends CommonDao<ChannelParamPO>{

	List<ChannelParamPO> findByParentChannelTemplateId(Long parentChannelTemplateId);
	
	List<ChannelParamPO> findByParentChannelParamId(Long parentChannelParamId);
	
	ChannelParamPO findByParentChannelTemplateIdAndParamName(Long parentChannelTemplateId,String paramName);
	
	ChannelParamPO findByParamNameAndConstValueAndParamScope(String paramName,String constValue,ParamScope paramScope);
}
