package com.suma.venus.resource.service;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.constant.VenusParamConstant.ParamType;
import com.suma.venus.resource.dao.ChannelParamDao;
import com.suma.venus.resource.pojo.ChannelParamPO;

/**
 * channel参数转化及数据存储工具类
 * @author lxw
 *
 */
@Service
public class ChannelParamConvertUtil {
	
	@Autowired
	private ChannelParamDao channelParamDao;
	
	/**
	 * 转化资源模型的参数模板
	 * @param channelTemplateId
	 * @param child_param_constraint
	 * @param parent_channel_param_id
	 * @throws Exception
	 */
	public void createBundleChildParam(JSONObject child_param_constraint,Long parent_channel_param_id) throws Exception{
		for (Entry<String, Object> childParamEntry : child_param_constraint.entrySet()) {
			String paramName = childParamEntry.getKey();
			JSONObject childParamJson = (JSONObject)childParamEntry.getValue();
			String childParamType = childParamJson.getString(VenusParamConstant.PARAM_JSON_KEY_TYPE);
			JSONObject childParamConstraint = childParamJson.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			if(ParamType.CONTAINER.getName().equals(childParamType)){
				//递归
				ChannelParamPO childParam = new ChannelParamPO();
				childParam.setParamName(paramName);
				childParam.setParamType(ParamType.CONTAINER);
				childParam.setParentChannelParamId(parent_channel_param_id);
				channelParamDao.save(childParam);
				createBundleChildParam(childParamConstraint,childParam.getId());
			}else if(ParamType.NUM.getName().equals(childParamType)){
				createChildParam_num(parent_channel_param_id, paramName,childParamConstraint,null);
			}else if(ParamType.STRING.getName().equals(childParamType)){
				createChildParam_string(parent_channel_param_id, paramName,childParamConstraint,null);
			}else if(ParamType.ENUM.getName().equals(childParamType)){
				createChildParam_enum(parent_channel_param_id, paramName,childParamConstraint,null);
			}else if(ParamType.CONSTANT.getName().equals(childParamType)){
				createChildParam_const(parent_channel_param_id, paramName,childParamConstraint,null);
			}else if(ParamType.RSL.getName().equals(childParamType)){
				createChildParam_rsl(parent_channel_param_id, paramName,childParamConstraint,null);
			}else if(ParamType.FPS.getName().equals(childParamType)){
				createChildParam_fps(parent_channel_param_id, paramName,childParamConstraint,null);
			}
		}
	}
	
	/**
	 * 转化普适意义的参数模板
	 * @param child_param_constraint
	 * @param parent_channel_param_id
	 * @param paramScope
	 * @throws Exception
	 */
	public void createChildParam(JSONObject child_param_constraint,Long parent_channel_param_id,ParamScope paramScope) throws Exception{
		for (Entry<String, Object> childParamEntry : child_param_constraint.entrySet()) {
			String paramName = childParamEntry.getKey();
			JSONObject childParamJson = (JSONObject)childParamEntry.getValue();
			String childParamType = childParamJson.getString(VenusParamConstant.PARAM_JSON_KEY_TYPE);
			JSONObject childParamConstraint = childParamJson.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			if(ParamType.CONTAINER.getName().equals(childParamType)){
				//递归
				ChannelParamPO childParam = new ChannelParamPO();
				childParam.setParamName(paramName);
				childParam.setParamType(ParamType.CONTAINER);
				childParam.setParentChannelParamId(parent_channel_param_id);
				channelParamDao.save(childParam);
				createChildParam(childParamConstraint,childParam.getId(),paramScope);
			}else if(ParamType.NUM.getName().equals(childParamType)){
				createChildParam_num(parent_channel_param_id, paramName,childParamConstraint,paramScope);
			}else if(ParamType.STRING.getName().equals(childParamType)){
				createChildParam_string(parent_channel_param_id, paramName,childParamConstraint,paramScope);
			}else if(ParamType.ENUM.getName().equals(childParamType)){
				createChildParam_enum(parent_channel_param_id, paramName,childParamConstraint,paramScope);
			}else if(ParamType.CONSTANT.getName().equals(childParamType)){
				createChildParam_const(parent_channel_param_id, paramName,childParamConstraint,paramScope);
			}else if(ParamType.RSL.getName().equals(childParamType)){
				createChildParam_rsl(parent_channel_param_id, paramName,childParamConstraint,paramScope);
			}else if(ParamType.FPS.getName().equals(childParamType)){
				createChildParam_fps(parent_channel_param_id, paramName,childParamConstraint,paramScope);
			}
		}
	}
	
	private void createChildParam_num(Long parent_channel_param_id,String paramName, JSONObject childParamConstraint,ParamScope paramScope) {
		ChannelParamPO childParam = new ChannelParamPO();
		childParam.setParamName(paramName);
		childParam.setParamType(ParamType.NUM);
		childParam.setParentChannelParamId(parent_channel_param_id);
		childParam.setNumMaxValue(childParamConstraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE));
		childParam.setNumMinValue(childParamConstraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE));
		childParam.setNumStep(childParamConstraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_STEP));
		childParam.setParamScope(paramScope);
		channelParamDao.save(childParam);
	}
	
	private void createChildParam_string(Long parent_channel_param_id,String paramName, JSONObject childParamConstraint,ParamScope paramScope) {
		ChannelParamPO childParam = new ChannelParamPO();
		childParam.setParamName(paramName);
		childParam.setParamType(ParamType.STRING);
		childParam.setParentChannelParamId(parent_channel_param_id);
		childParam.setStringMaxLength(childParamConstraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXLENGTH));
		childParam.setStringMinLength(childParamConstraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINLENGTH));
		childParam.setParamScope(paramScope);
		channelParamDao.save(childParam);
	}
	
	private void createChildParam_enum(Long parent_channel_param_id,String paramName, JSONObject childParamConstraint,ParamScope paramScope) {
		ChannelParamPO childParam = new ChannelParamPO();
		childParam.setParamName(paramName);
		childParam.setParamType(ParamType.ENUM);
		childParam.setParentChannelParamId(parent_channel_param_id);
		StringBuilder sBuilder = createEnumValues(childParamConstraint);
		childParam.setEnumValues(sBuilder.toString());
		childParam.setParamScope(paramScope);
		channelParamDao.save(childParam);
	}
	
	private void createChildParam_rsl(Long parent_channel_param_id,String paramName, JSONObject childParamConstraint,ParamScope paramScope) {
		ChannelParamPO childParam = new ChannelParamPO();
		childParam.setParamName(paramName);
		childParam.setParamType(ParamType.RSL);
		childParam.setParentChannelParamId(parent_channel_param_id);
		childParam.setParamScope(paramScope);
		channelParamDao.save(childParam);
		JSONObject dynamic_constraint = childParamConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT);
		JSONObject enum_constraint = childParamConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT);
		if(null != dynamic_constraint && !dynamic_constraint.isEmpty()){
			ChannelParamPO dynamic_constraint_container = new ChannelParamPO();
			dynamic_constraint_container.setParamType(ParamType.CONTAINER);
			dynamic_constraint_container.setParamName(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT);
			dynamic_constraint_container.setParentChannelParamId(childParam.getId());
			dynamic_constraint_container.setParamScope(paramScope);
			channelParamDao.save(dynamic_constraint_container);
			for (Entry<String, Object> dynamicEntry : dynamic_constraint.entrySet()) {
				ChannelParamPO dynamic_param = new ChannelParamPO();
				dynamic_param.setParamName(dynamicEntry.getKey());
				dynamic_param.setParamType(ParamType.NUM);
				dynamic_param.setParentChannelParamId(dynamic_constraint_container.getId());
				JSONObject constraint = (JSONObject)dynamicEntry.getValue();
				dynamic_param.setNumMaxValue(constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE));
				dynamic_param.setNumMinValue(constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE));
				dynamic_param.setNumStep(constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_STEP));
				dynamic_param.setParamScope(paramScope);
				channelParamDao.save(dynamic_param);
			}
		}
		
		if(null != enum_constraint && !enum_constraint.isEmpty()){
			ChannelParamPO enum_constraint_param = new ChannelParamPO();
			enum_constraint_param.setParamType(ParamType.ENUM);
			enum_constraint_param.setParamName(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT);
			enum_constraint_param.setParentChannelParamId(childParam.getId());
			StringBuilder sBuilder = createEnumValues(enum_constraint);
			enum_constraint_param.setEnumValues(sBuilder.toString());
			enum_constraint_param.setParamScope(paramScope);
			channelParamDao.save(enum_constraint_param);
		}
	}
	
	private void createChildParam_fps(Long parent_channel_param_id,String paramName, JSONObject childParamConstraint,ParamScope paramScope) {
		ChannelParamPO childParam = new ChannelParamPO();
		childParam.setParamName(paramName);
		childParam.setParamType(ParamType.FPS);
		childParam.setParentChannelParamId(parent_channel_param_id);
		childParam.setParamScope(paramScope);
		channelParamDao.save(childParam);
		JSONObject dynamic_constraint = childParamConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT);
		JSONObject enum_constraint = childParamConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT);
		if(null != dynamic_constraint && !dynamic_constraint.isEmpty()){
			ChannelParamPO dynamic_constraint_container = new ChannelParamPO();
			dynamic_constraint_container.setParamType(ParamType.NUM);
			dynamic_constraint_container.setParamName(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT);
			dynamic_constraint_container.setParentChannelParamId(childParam.getId());
			dynamic_constraint_container.setNumMaxValue(dynamic_constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE));
			dynamic_constraint_container.setNumMinValue(dynamic_constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE));
			dynamic_constraint_container.setNumStep(dynamic_constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_STEP));
			dynamic_constraint_container.setParamScope(paramScope);
			channelParamDao.save(dynamic_constraint_container);
		}
		
		if(null != enum_constraint && !enum_constraint.isEmpty()){
			ChannelParamPO enum_constraint_param = new ChannelParamPO();
			enum_constraint_param.setParamType(ParamType.ENUM);
			enum_constraint_param.setParamName(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT);
			enum_constraint_param.setParentChannelParamId(childParam.getId());
			StringBuilder sBuilder = createEnumValues(enum_constraint);
			enum_constraint_param.setEnumValues(sBuilder.toString());
			enum_constraint_param.setParamScope(paramScope);
			channelParamDao.save(enum_constraint_param);
		}
	}
	
	private void createChildParam_const(Long parent_channel_param_id,String paramName, JSONObject childParamConstraint,ParamScope paramScope) {
		ChannelParamPO childParam = new ChannelParamPO();
		childParam.setParamName(paramName);
		childParam.setParamType(ParamType.CONSTANT);
		childParam.setParentChannelParamId(parent_channel_param_id);
		childParam.setConstType(childParamConstraint.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTTYPE));
		childParam.setConstValue(String.valueOf(childParamConstraint.get(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE)));
		childParam.setParamScope(paramScope);
		channelParamDao.save(childParam);
	}

	private StringBuilder createEnumValues(JSONObject enum_constraint) {
		JSONArray enum_value_array = enum_constraint.getJSONArray(VenusParamConstant.PARAM_JSON_KEY_VALUES);
		StringBuilder sBuilder = new StringBuilder();
		for (int i=0;i<enum_value_array.size();i++) {
			sBuilder.append(enum_value_array.getString(i)).append(",");
		}
		return sBuilder.deleteCharAt(sBuilder.length()-1);
	}
	
}
