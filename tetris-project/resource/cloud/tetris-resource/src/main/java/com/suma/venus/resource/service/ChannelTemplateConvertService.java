package com.suma.venus.resource.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.NumParamBO;
import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.constant.VenusParamConstant.ParamType;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelParamDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.dao.ScreenRectTemplateDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.pojo.ChannelParamPO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;

@Service
public class ChannelTemplateConvertService {

	@Autowired
	private ChannelTemplateDao channelTemplateDao;
	
	@Autowired
	private ChannelParamDao channelParamDao;
	
	@Autowired
	private ChannelParamConvertUtil channelParamConvertUtil;
	
	@Autowired
	private ScreenRectTemplateDao screenRectTemplateDao;
	
	@Autowired
	private ChannelParamService channelParamService;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ChannelSchemeDao channelSchemeDao;
	
	@Autowired
	private ScreenSchemeDao screenSchemeDao;
	
	@Transactional(rollbackFor = Exception.class)
	public void convertTemplate(String templateJsonStr) throws Exception{
		if(null == templateJsonStr || templateJsonStr.isEmpty()){
			throw new Exception("empty template");
		}
		
		JSONObject templateJson = JSONObject.parseObject(templateJsonStr);
		
		for(Entry<String,Object> entry : templateJson.entrySet()){
			String device_model = entry.getKey();
			/**这种类型的模板是否已存在*/
			boolean beExisted = false;
			if(!channelTemplateDao.findByDeviceModel(device_model).isEmpty()){
//				throw new RuntimeException("TEMPLATE_EXIST");
				beExisted = true;
			}
			String bundle_type = templateJson.getJSONObject(device_model)
					.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
					.getJSONObject("bundle")
					.getJSONObject("bundle_type")
					.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
					.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
			
			if(bundle_type.equals("VenusTerminal") || bundle_type.equals("VenusProxy")){//如果是终端类型资源，按照新模板结构解析
				parseTerminalTemplate(templateJson, device_model, bundle_type, beExisted);
				
			} else {
				parseNonTerminalTemplate(templateJson, device_model, bundle_type, beExisted);				
			}
				
			break;
		}
	}

	private void parseTerminalTemplate(JSONObject templateJson, String device_model, String bundle_type,boolean beExisted) throws Exception {
		//channels
		JSONArray channelJsonArray = templateJson.getJSONObject(device_model)
				.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
				.getJSONObject("bundle")
				.getJSONArray(VenusParamConstant.PARAM_JSON_KEY_CHANNELS);
		if(!beExisted){//channle的修改没法搞，只考虑新建
			for(Iterator<Object> iterator = channelJsonArray.iterator();iterator.hasNext();){
				JSONObject channelJson = (JSONObject)iterator.next();
				String channelId = channelJson.getJSONObject("channel_id")
						.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
						.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
				String channelName = channelId.split("_")[0];
				
				ChannelTemplatePO channelTemplatePO = channelTemplateDao.findByDeviceModelAndChannelName(device_model, channelName);
				if(null == channelTemplatePO){//该类型的通道尚未存储
					channelTemplatePO = new ChannelTemplatePO();
					channelTemplatePO.setDeviceModel(device_model);
					channelTemplatePO.setChannelName(channelName);
					channelTemplatePO.setBundleType(bundle_type);
					channelTemplatePO.setDefaultChannelIds(channelId);
					channelTemplateDao.save(channelTemplatePO);
					
					JSONObject channel_param_constraint = channelJson.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM)
															.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
					saveChannelParamFromConstraint(channelTemplatePO, channel_param_constraint);
				} else {//该类型的通道已存储
					channelTemplatePO.setDefaultChannelIds(channelTemplatePO.getDefaultChannelIds()+","+channelId);
					channelTemplateDao.save(channelTemplatePO);
				}
			}
		}
		
		//screens
		JSONArray screenJsonArray = templateJson.getJSONObject(device_model)
				.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
				.getJSONObject("bundle").getJSONArray("screens");
		for (Iterator<Object> iterator = screenJsonArray.iterator(); iterator.hasNext();) {
			JSONObject screenJson = (JSONObject) iterator.next();
			String screenId = screenJson.getJSONObject("screen_id")
					.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
					.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
			
			//TODO 为对应类型bundle增加该种屏配置
			if(beExisted && screenRectTemplateDao.findByDeviceModelAndScreenId(device_model, screenId).isEmpty()){
				Set<String> bundleIdSet = bundleDao.queryBundleIdByDeviceModel(device_model);
				for (String bundleId : bundleIdSet) {
					ScreenSchemePO screenSchemePO = new ScreenSchemePO();
					screenSchemePO.setBundleId(bundleId);
					screenSchemePO.setDeviceModel(device_model);
					screenSchemePO.setScreenId(screenId);
					screenSchemePO.setStatus(LockStatus.IDLE);
					screenSchemeDao.save(screenSchemePO);
				}
			}
			
			JSONArray rectJsonArray = screenJson.getJSONArray("rects");
			for (Iterator<Object> rectIterator = rectJsonArray.iterator(); rectIterator.hasNext();) {
				JSONObject rectJson = (JSONObject) rectIterator.next();
				
				JSONObject rectIdJson = rectJson.getJSONObject("rect_id");
				
				String rectId = rectIdJson.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
						.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
				
				ScreenRectTemplatePO screenTemplatePO = screenRectTemplateDao.findByDeviceModelAndScreenIdAndRectId(device_model, screenId, rectId);
				if(null != screenTemplatePO){
					//该分屏模板已存在
					screenTemplatePO.setParam(getScreenTemplateParamJson(rectJson, screenTemplatePO).toJSONString());
					
					screenRectTemplateDao.save(screenTemplatePO);
				} else {
					//分屏模板不存在，需要新建
					screenTemplatePO = new ScreenRectTemplatePO();
					screenTemplatePO.setDeviceModel(device_model);
					screenTemplatePO.setScreenId(screenId);
					screenTemplatePO.setRectId(rectId);
					screenTemplatePO.setParam(getScreenTemplateParamJson(rectJson, screenTemplatePO).toJSONString());
					
					screenRectTemplateDao.save(screenTemplatePO);
				}
			}
		}
		
	}

	private JSONObject getScreenTemplateParamJson(JSONObject rectJson, ScreenRectTemplatePO screenTemplatePO) {
		JSONObject paramJson = new JSONObject();
		
		for(Entry<String, Object> entry : rectJson.entrySet()){
			String key = entry.getKey();
			JSONObject json = (JSONObject)entry.getValue();
			if("channel".equals(key)){
				JSONArray channel_value_array = json.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
						.getJSONArray(VenusParamConstant.PARAM_JSON_KEY_VALUES);
				StringBuilder sBuilder = new StringBuilder();
				for (int i=0;i<channel_value_array.size();i++) {
					sBuilder.append(channel_value_array.getString(i)).append(",");
				}
				screenTemplatePO.setChannel(sBuilder.toString());
			} else if("cut".equals(key)){
				JSONObject newJson = new JSONObject();
				for(Entry<String, Object> cutEntry : json.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT).entrySet()){
					JSONObject cutParamJson = (JSONObject)cutEntry.getValue();
					if("num".equals(cutParamJson.getString("type"))){
						JSONObject constraint = cutParamJson.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
						NumParamBO numParamBO = new NumParamBO(constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE), 
								constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE), constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_STEP));
						newJson.put(key, numParamBO);
					}
				}
				paramJson.put("cut", newJson);
			} else if("num".equals(json.getString("type"))){
				JSONObject constraint = json.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
				NumParamBO numParamBO = new NumParamBO(constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MINVALUE), 
						constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE), constraint.getInteger(VenusParamConstant.PARAM_JSON_KEY_STEP));
				paramJson.put(key, numParamBO);
			}
		}
		return paramJson;
	}

	/**解析非VenusTerminal的资源模型*/
	private void parseNonTerminalTemplate(JSONObject templateJson, String device_model, String bundle_type,boolean beExisted)
			throws Exception {
		JSONObject channel_elem_constraint = templateJson.getJSONObject(device_model)
				.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
				.getJSONObject("bundle")
				.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CHANNELS)
				.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
				.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_ELEMCONSTRAINT);
		JSONArray channel_name_array = channel_elem_constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CHANNELNAME)
				.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
				.getJSONArray(VenusParamConstant.PARAM_JSON_KEY_VALUES);
		if(!beExisted){//新的资源模板
			createNewNonTerminalTemplate(device_model, bundle_type, channel_elem_constraint, channel_name_array);
		} else {//资源模板已存在，需要更新
			modifyNonTerminalTemplate(device_model, bundle_type, channel_elem_constraint, channel_name_array);
		}
	}

	private void modifyNonTerminalTemplate(String device_model, String bundle_type, JSONObject channel_elem_constraint,
			JSONArray channel_name_array) throws Exception {
		/**定义一个set记录旧模板上的channel_name*/
		Set<String> oldChannelNames = channelTemplateDao.findChannelNameByDeviceModel(device_model);
		/**定义一个set，存放新模板和旧模板共有的channel_name*/
		Set<String> commonChannelNameSet = new HashSet<String>();
		for (int i=0;i < channel_name_array.size(); i++) {
			String channel_name = channel_name_array.getString(i);
			JSONObject constraint = channel_elem_constraint.getJSONObject(channel_name)
					.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			Integer max_channel_cnt = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_MAXCHANNELCNT)
										.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
										.getInteger(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
			
			ChannelTemplatePO channelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(device_model, channel_name);
			/**判断channel_name这种类型的通道是否存在*/
			if(null != channelTemplate){
				commonChannelNameSet.add(channelTemplate.getChannelName());
				//删除旧的模板参数
				List<ChannelParamPO> channelParams = channelParamService.findByParentChannelTemplateId(channelTemplate.getId());
				for (ChannelParamPO channelParam : channelParams) {
					channelParamService.deleteParamAndDescendant(channelParam);
				}
				
				channelTemplate.setMaxChannelCnt(max_channel_cnt);
				channelTemplate.setBundleType(bundle_type);
				channelTemplateDao.save(channelTemplate);
				
				JSONObject channel_param_constraint = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM)
						.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
				saveChannelParamFromConstraint(channelTemplate, channel_param_constraint);
			} else {
				channelTemplate = new ChannelTemplatePO();
				channelTemplate.setDeviceModel(device_model);
				channelTemplate.setChannelName(channel_name);
				channelTemplate.setMaxChannelCnt(max_channel_cnt);
				channelTemplate.setBundleType(bundle_type);
				channelTemplateDao.save(channelTemplate);
				
				JSONObject channel_param_constraint = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM)
						.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
				saveChannelParamFromConstraint(channelTemplate, channel_param_constraint);
				
				//为这种类型的所有bundle创建该通道模板的配置
				Set<String> bundleIdSet = bundleDao.queryBundleIdByDeviceModel(device_model);
				for (String bundleId : bundleIdSet) {
					for (int j = 1; j <= channelTemplate.getMaxChannelCnt(); j++) {
						ChannelSchemePO channelScheme = new ChannelSchemePO();
						channelScheme.setBundleId(bundleId);
						channelScheme.setChannelId(channelTemplate.getBaseType() + "_" + j);
						channelScheme.setChannelName(channelTemplate.getChannelName());
						channelScheme.setChannelTemplateID(channelTemplate.getId());
						channelSchemeDao.save(channelScheme);
					}
				}
			}
		}
		
		//删除旧资源模板有而新资源模板没有的通道
		oldChannelNames.removeAll(commonChannelNameSet);
		for(String toDeleteChannelName : oldChannelNames){
			ChannelTemplatePO toDeleteChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(device_model, toDeleteChannelName);
			//删除其模板参数表
			List<ChannelParamPO> channelParams = channelParamService.findByParentChannelTemplateId(toDeleteChannelTemplate.getId());
			for (ChannelParamPO channelParam : channelParams) {
				channelParamService.deleteParamAndDescendant(channelParam);
			}
			
			//删除所有bundle上关联该通道模板的配置
			channelSchemeDao.deleteByChannelName(toDeleteChannelName);
			
			channelTemplateDao.delete(toDeleteChannelTemplate);
		}
	}

	/**非终端类型资源模板新建逻辑*/
	private void createNewNonTerminalTemplate(String device_model, String bundle_type,
			JSONObject channel_elem_constraint, JSONArray channel_name_array) throws Exception {
		for (int i=0;i < channel_name_array.size(); i++) {
			String channel_name = channel_name_array.getString(i);
			JSONObject constraint = channel_elem_constraint.getJSONObject(channel_name)
					.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			Integer max_channel_cnt = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_MAXCHANNELCNT)
										.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT)
										.getInteger(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE);
			
			ChannelTemplatePO channelTemplate = new ChannelTemplatePO();
			channelTemplate.setDeviceModel(device_model);
			channelTemplate.setChannelName(channel_name);
			channelTemplate.setMaxChannelCnt(max_channel_cnt);
			channelTemplate.setBundleType(bundle_type);
			channelTemplateDao.save(channelTemplate);
			
			JSONObject channel_param_constraint = constraint.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM)
													.getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			saveChannelParamFromConstraint(channelTemplate, channel_param_constraint);
		}
	}

	public void saveChannelParamFromConstraint(ChannelTemplatePO channelTemplate, JSONObject channel_param_constraint) throws Exception {
		for(Entry<String, Object> paramEntry : channel_param_constraint.entrySet()){
			String paramName = paramEntry.getKey();
			JSONObject param_constraint = ((JSONObject)paramEntry.getValue()).getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			if("base_type".equalsIgnoreCase(paramName)){
				ChannelParamPO param = createChildParam_const(channelTemplate.getId(), paramName, param_constraint);
				channelTemplate.setBaseType(param.getConstValue());
				channelTemplateDao.save(channelTemplate);
			}else if("base_version".equalsIgnoreCase(paramName) || "extern_version".equalsIgnoreCase(paramName)){
				createChildParam_const(channelTemplate.getId(), paramName, param_constraint);
			}else if("base_param".equalsIgnoreCase(paramName) || "extern_param".equalsIgnoreCase(paramName)){
				ChannelParamPO channelParam = new ChannelParamPO();
				channelParam.setParamName(paramName);
				channelParam.setParamType(ParamType.CONTAINER);
				channelParam.setParentChannelTemplateId(channelTemplate.getId());
				channelParamDao.save(channelParam);
				channelParamConvertUtil.createBundleChildParam(param_constraint,channelParam.getId());
			}else if ("extern_type".equalsIgnoreCase(paramName)) {
				ChannelParamPO param = createChildParam_const(channelTemplate.getId(), paramName, param_constraint);
				channelTemplate.setExternType(param.getConstValue());
				channelTemplateDao.save(channelTemplate);
			}
		}
	}
	
	private ChannelParamPO createChildParam_const(Long parent_channel_template_id,String paramName,JSONObject childParamConstraint) {
		ChannelParamPO childParam = new ChannelParamPO();
		childParam.setParamName(paramName);
		childParam.setParamType(ParamType.CONSTANT);
		childParam.setParentChannelTemplateId(parent_channel_template_id);
		childParam.setConstType(childParamConstraint.getString(VenusParamConstant.PARAM_JSON_KEY_CONSTTYPE));
		childParam.setConstValue(String.valueOf(childParamConstraint.get(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE)));
		channelParamDao.save(childParam);
		return childParam;
	}

}
