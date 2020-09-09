package com.suma.venus.resource.service;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.constant.VenusParamConstant.ParamType;
import com.suma.venus.resource.pojo.ChannelParamPO;

/**
 * param json 工具类
 * @author lxw
 *
 */
@Service
public class ParamJsonUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParamJsonUtil.class);
	
	@Autowired
	private ChannelParamService channelParamService;
	
	public void createChannelParamJson(JSONObject channelParamConstraint,List<ChannelParamPO> channelParams) {
		for (ChannelParamPO channelParamPO : channelParams) {
			JSONObject paramBodyJson = new JSONObject();
			channelParamConstraint.put(channelParamPO.getParamName(), paramBodyJson);
			paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_TYPE, channelParamPO.getParamType().getName());
			switch (channelParamPO.getParamType()) {
			case CONTAINER:
				JSONObject containerConstraint = new JSONObject();
				paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, containerConstraint);
				List<ChannelParamPO> childParamPOs = channelParamService.findByParentChannelParamId(channelParamPO.getId());
				createChannelParamJson(containerConstraint,childParamPOs);
				break;
			case STRING:
				paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, 
						createStringConstraintJson(channelParamPO.getStringMinLength(),channelParamPO.getStringMaxLength()));
				break;
			case NUM:
				paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, 
						createNumConstraintJson(channelParamPO.getNumMinValue(), channelParamPO.getNumMaxValue(), channelParamPO.getNumStep()));
				break;
			case CONSTANT:
				paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, 
						createConstConstraintJson(channelParamPO.getConstType(), channelParamPO.getConstValue()));
				break;
			case ENUM:
				paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, 
						createEnumConstraintJson(channelParamPO.getEnumValues()));
				break;
			case RSL:
				paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, 
						createRslConstraintJson(channelParamPO.getId()));
				break;
			case FPS:
				paramBodyJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, 
						createFpsConstraintJson(channelParamPO.getId()));
				break;
			default:
				break;
			}
				
		}
	}
	
	public JSONObject createStringConstraintJson(Integer minLength,Integer maxLength){
		JSONObject stringConstraint = new JSONObject();
		stringConstraint.put(VenusParamConstant.PARAM_JSON_KEY_MINLENGTH, minLength);
		stringConstraint.put(VenusParamConstant.PARAM_JSON_KEY_MAXLENGTH, maxLength);
		return stringConstraint;
	}
	
	public JSONObject createNumConstraintJson(Integer minValue,Integer maxValue,Integer step){
		JSONObject numConstraint = new JSONObject();
		numConstraint.put(VenusParamConstant.PARAM_JSON_KEY_MINVALUE, minValue);
		numConstraint.put(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE, maxValue);
		numConstraint.put(VenusParamConstant.PARAM_JSON_KEY_STEP, step);
		return numConstraint;
	}
	
	public JSONObject createConstConstraintJson(String constType,String constValue){
		JSONObject constConstraint = new JSONObject();
		constConstraint.put(VenusParamConstant.PARAM_JSON_KEY_CONSTTYPE, constType);
		if(constType.equalsIgnoreCase(ParamType.NUM.getName())){
			constConstraint.put(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE, Integer.valueOf(constValue));
		}else if(constType.equalsIgnoreCase(ParamType.STRING.getName())){
			constConstraint.put(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE, constValue);
		}
		
		return constConstraint;
	}
	
	public JSONObject createEnumConstraintJson(String enumValues){
		JSONObject enumConstraint = new JSONObject();
		String[] enumValueArray = enumValues.split(",");
		enumConstraint.put(VenusParamConstant.PARAM_JSON_KEY_VALUES, enumValueArray);
		return enumConstraint;
	}

	public JSONObject createRslConstraintJson(Long channelParamId){
		JSONObject rslConstraint = new JSONObject();
		List<ChannelParamPO> childChannelParams = channelParamService.findByParentChannelParamId(channelParamId);
		for (ChannelParamPO childChannelParam : childChannelParams) {
			if(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT.equalsIgnoreCase(childChannelParam.getParamName())){
				JSONObject dynamicConstraint = new JSONObject();
				rslConstraint.put(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT, dynamicConstraint);
				List<ChannelParamPO> dynamicConstraintParams = channelParamService.findByParentChannelParamId(childChannelParam.getId());
				for (ChannelParamPO dynamicConstraintParam : dynamicConstraintParams) {
					if(VenusParamConstant.PARAM_JSON_KEY_WIDTH.equalsIgnoreCase(dynamicConstraintParam.getParamName())){
						JSONObject widthJson = new JSONObject();
						widthJson.put(VenusParamConstant.PARAM_JSON_KEY_MINVALUE, dynamicConstraintParam.getNumMinValue());
						widthJson.put(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE, dynamicConstraintParam.getNumMaxValue());
						widthJson.put(VenusParamConstant.PARAM_JSON_KEY_STEP, dynamicConstraintParam.getNumStep());
						dynamicConstraint.put(VenusParamConstant.PARAM_JSON_KEY_WIDTH, widthJson);
					}else if(VenusParamConstant.PARAM_JSON_KEY_HEIGHT.equalsIgnoreCase(dynamicConstraintParam.getParamName())){
						JSONObject heightJson = new JSONObject();
						heightJson.put(VenusParamConstant.PARAM_JSON_KEY_MINVALUE, dynamicConstraintParam.getNumMinValue());
						heightJson.put(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE, dynamicConstraintParam.getNumMaxValue());
						heightJson.put(VenusParamConstant.PARAM_JSON_KEY_STEP, dynamicConstraintParam.getNumStep());
						dynamicConstraint.put(VenusParamConstant.PARAM_JSON_KEY_HEIGHT, heightJson);
					}
				}
			}else if(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT.equalsIgnoreCase(childChannelParam.getParamName())){
				rslConstraint.put(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT, 
						createEnumConstraintJson(childChannelParam.getEnumValues()));
			}
		}
		
		return rslConstraint;
	}
	
	public JSONObject createFpsConstraintJson(Long channelParamId){
		JSONObject fpsConstraint = new JSONObject();
		List<ChannelParamPO> childChannelParams = channelParamService.findByParentChannelParamId(channelParamId);
		for (ChannelParamPO childChannelParam : childChannelParams) {
			if(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT.equalsIgnoreCase(childChannelParam.getParamName())){
				JSONObject dynamicConstraint = new JSONObject();
				fpsConstraint.put(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT, dynamicConstraint);
				dynamicConstraint.put(VenusParamConstant.PARAM_JSON_KEY_MINVALUE, childChannelParam.getNumMinValue());
				dynamicConstraint.put(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE, childChannelParam.getNumMaxValue());
				dynamicConstraint.put(VenusParamConstant.PARAM_JSON_KEY_STEP, childChannelParam.getNumStep());
			}else if(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT.equalsIgnoreCase(childChannelParam.getParamName())){
				fpsConstraint.put(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT, 
						createEnumConstraintJson(childChannelParam.getEnumValues()));
			}
		}
		
		return fpsConstraint;
	}
	
	public JSONObject createMaxChannelCntJson(Integer max_channel_cnt) {
		JSONObject maxChannelCntJson = new JSONObject();
		maxChannelCntJson.put(VenusParamConstant.PARAM_JSON_KEY_TYPE, ParamType.CONSTANT.getName());
		maxChannelCntJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, 
				createConstConstraintJson(ParamType.NUM.getName(),String.valueOf(max_channel_cnt)));
		return maxChannelCntJson;
	}
	
	public boolean matchParam(JSONObject paramInstance,JSONObject paramConstraint) throws Exception{
		for (Entry<String, Object> entry : paramInstance.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			JSONObject constraintValue = paramConstraint.getJSONObject(key);
			if(null == constraintValue){
//				return false;
				continue;
			}
			String constraintParamType = constraintValue.getString("type");
			JSONObject constraint = constraintValue.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			if(ParamType.CONTAINER.getName().equals(constraintParamType)){//container类型参数，递归匹配
				JSONObject childParamInstance = paramInstance.getJSONObject(key);
				if(!matchParam(childParamInstance,constraint)){
					return false;
				}
			} else if(ParamType.NUM.getName().equals(constraintParamType)){//num类型参数
				if(!matchNumParam(value, constraint)){
					return false;
				}
			}else if(ParamType.CONSTANT.getName().equals(constraintParamType)){//constant类型参数
				if(!matchConstantParam(value, constraint)){
					return false;
				}
			}else if(ParamType.STRING.getName().equals(constraintParamType)){
				if(!matchStringParam(value, constraint)){
					return false;
				}
			}else if(ParamType.ENUM.getName().equals(constraintParamType)){
				if(!matchEnumParam(value, constraint)){
					return false;
				}
			}else if(ParamType.FPS.getName().equals(constraintParamType)){
				if(!matchFpsParam(value, constraint)){
					return false;
				}
			}else if(ParamType.RSL.getName().equals(constraintParamType)){
				if(!matchRslParam(value, constraint)){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean matchNumParam(Object value, JSONObject constraint) {
		Integer value_num = (Integer)value;
		Integer constraintMinValue = constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE);
		Integer constraintMaxValue = constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE);
		if(null != constraintMinValue && constraintMinValue > value_num){
			return false;
		}
		if(null != constraintMaxValue && constraintMaxValue < value_num){
			return false;
		}
		return true;
	}
	
	public boolean matchConstantParam(Object value, JSONObject constraint) {
		String constantType = constraint.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTTYPE);
		if(ParamType.NUM.getName().equals(constantType)){
			Integer constraintValue = constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
			if(!constraintValue.equals(value)){
				return false;
			}
		}else if(ParamType.STRING.getName().equals(constantType)){
			String constraintValue = constraint.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
			if(!constraintValue.equals(value)){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean matchStringParam(Object value, JSONObject constraint){
//		Integer constraintMinLength = constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINLENGTH);
//		Integer constraintMaxLength = constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXLENGTH);
//		String value_String = (String)value;
//		if(null != constraintMinLength && constraintMinLength > value_String.length()){
//			return false;
//		}
//		if(null != constraintMaxLength && constraintMaxLength < value_String.length()){
//			return false;
//		}
		
		return true;
	}
	
	public boolean matchEnumParam(Object value, JSONObject constraint){
		JSONArray constraintArray = constraint.getJSONArray(VenusParamConstant.PARAM_JSON_KEY_VALUES);
		if(null == constraintArray || !constraintArray.contains(value)){
			return false;
		}
		return true;
	}
	
	public boolean matchFpsParam(Object value,JSONObject constraint){
		JSONObject enumConstraint = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT);
		JSONObject dynamicConstraint = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT);
		if(null != enumConstraint){
			JSONArray enumValues = enumConstraint.getJSONArray(VenusParamConstant.PARAM_JSON_KEY_VALUES);
			if(enumValues.contains(value)){
				return true;
			}
		}
		if(null != dynamicConstraint){
			Integer constraintMinValue = dynamicConstraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE);
			Integer constraintMaxValue = dynamicConstraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE);
			try {
				Integer value_num = (Integer)value;
				if(value_num >= constraintMinValue && value_num <= constraintMaxValue){
					return true;
				}
			} catch (Exception e) {
				LOGGER.error("Fail to match FPS param",e);
			}
		}
		return false;
	}
	
	public boolean matchRslParam(Object value,JSONObject constraint){
		JSONObject enumConstraint = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_ENUMCONSTRAINT);
		JSONObject dynamicConstraint = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_DYNAMICCONSTRAINT);
		if(null != enumConstraint){
			JSONArray enumValues = enumConstraint.getJSONArray(VenusParamConstant.PARAM_JSON_KEY_VALUES);
			if(enumValues.contains(value)){
				return true;
			}
		}
		
		if(null != dynamicConstraint){
			try {
				Integer value_width = Integer.valueOf(((String)value).split("x")[0]);
				Integer value_length = Integer.valueOf(((String)value).split("x")[1]);
				Integer width_min_value = dynamicConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_WIDTH)
						.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE);
				Integer width_max_value = dynamicConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_WIDTH)
						.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE);
				Integer height_min_value = dynamicConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_HEIGHT)
						.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE);
				Integer height_max_value = dynamicConstraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_HEIGHT)
						.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE);
				if(value_width >= width_min_value && value_width <= width_max_value
						&& value_length >= height_min_value && value_length <= height_max_value){
					return true;
				}
			} catch (Exception e) {
				LOGGER.error("Fail to match RSL param",e);
			}
		}
		return false;
	}
}
