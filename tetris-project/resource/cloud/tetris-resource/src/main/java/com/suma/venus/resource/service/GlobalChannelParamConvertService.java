package com.suma.venus.resource.service;


import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.pojo.ChannelTemplatePO;

/**
 * 全局channel param template处理类
 * @author lxw
 *
 */
@Service
public class GlobalChannelParamConvertService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalChannelParamConvertService.class);
	
	@Autowired
	private ChannelTemplateConvertService channelTemplateConvertService;
	
	@Autowired
	private ChannelTemplateDao channelTemplateDao;
	
	@Transactional(rollbackFor = Exception.class)
	public void convertParamTemplate(String paramJsonStr) throws Exception{
		JSONObject paramJson = JSONObject.parseObject(paramJsonStr);
		for(Entry<String,Object> entry : paramJson.entrySet()){
			String baseChannelTemplateName = entry.getKey();
			JSONObject channelParamConstraint = ((JSONObject)entry.getValue()).getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
					.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM).getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			
			ChannelTemplatePO templatePO = new ChannelTemplatePO();
			templatePO.setChannelName(baseChannelTemplateName);
			if(channelParamConstraint.containsKey("base_type")){
				templatePO.setParamScope(ParamScope.GLOBAL_BASIC);
				channelTemplateDao.save(templatePO);
			}else if(channelParamConstraint.containsKey("extern_type")){
				templatePO.setParamScope(ParamScope.GLOBAL_EXTERN);
				channelTemplateDao.save(templatePO);
			}else{
				LOGGER.error("Fail to convert global channel param : param string format error");
				return;
			}
			channelTemplateConvertService.saveChannelParamFromConstraint(templatePO,channelParamConstraint);
			
			break;
		}
	}
	
}
