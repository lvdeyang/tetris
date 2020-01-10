package com.suma.venus.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.LockChannelParamDao;
import com.suma.venus.resource.pojo.BundleEditableAttrPO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.TemplateEditableAttrPO;
import com.suma.venus.resource.service.BundleEditableAttrService;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.ChannelTemplateService;
import com.suma.venus.resource.service.TemplateEditableAttrService;
import com.suma.venus.resource.util.ChannelTemplateTreeUtil;
import com.suma.venus.resource.vo.BundleEditableAttrVO;
import com.suma.venus.resource.vo.ChannelTemplateTreeVO;
import com.suma.venus.resource.vo.ChannelTemplateVO;
import com.suma.venus.resource.vo.ConfigParamVO;
import com.suma.venus.resource.vo.EleTreeNodeVO;

/**
 * 能力方案配置controller
 * @author lxw
 *
 */
@Controller
@RequestMapping("/ability")
public class ConfigAbilityController extends ControllerBase{

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigAbilityController.class);
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private ChannelTemplateService channelTemplateService;
	
	@Autowired
	private ChannelTemplateTreeUtil channelTemplateTreeUtil;
	
	@Autowired
	private ChannelSchemeService channelSchemeService;
	
	@Autowired
	private TemplateEditableAttrService templateEditableAttrService;
	
	@Autowired
	private BundleEditableAttrService bundleEditableAttrService;
	
	@Autowired
	private ChannelSchemeDao channelSchemeDao;
	
	@Autowired
	private LockChannelParamDao lockChannelParamDao;
	
	@RequestMapping(value="/bundleTemplates",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> bundleTemplates(@RequestParam("bundleId") String bundleId){
		Map<String, Object> data = makeAjaxData();
		try {
			BundlePO bundle = bundleService.findByBundleId(bundleId);
			List<ChannelTemplatePO> channelTemplatePOs = channelTemplateService.findByDeviceModel(bundle.getDeviceModel());
			if(null == channelTemplatePOs || channelTemplatePOs.isEmpty()){
				data.put(ERRMSG, "没有该资源类型对应的能力模板");
				return data;
			}
			Set<ChannelTemplateVO> channelTemplateVOs = new HashSet<ChannelTemplateVO>();
			for (ChannelTemplatePO channelTemplatePO : channelTemplatePOs) {
				ChannelTemplateVO channelTemplateVO = new ChannelTemplateVO(channelTemplatePO.getDeviceModel());
				channelTemplateVOs.add(channelTemplateVO);
			}
			List<ChannelTemplateTreeVO> templates = new ArrayList<ChannelTemplateTreeVO>();
			for (ChannelTemplateVO channelTemplateVO : channelTemplateVOs) {
				ChannelTemplateTreeVO template = channelTemplateTreeUtil.createZTree(
						channelTemplateService.findByDeviceModel(channelTemplateVO.getDeviceModel()));
				templates.add(template);
			}
			data.put("templates", templates);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	@RequestMapping(value="/queryConfigedTemplate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryConfigedTemplate(@RequestParam("bundleId") String bundleId){
		Map<String, Object> data = makeAjaxData();
		try {
			Map<Long, List<ChannelSchemePO>> map = new HashMap<>();
			List<ChannelSchemePO> channelSchemePOs = channelSchemeService.findByBundleId(bundleId);
			if(channelSchemePOs.isEmpty()){
				data.put(WARNMSG, "请选择能力模板进行配置");
				return data;
			}
			
			List<ChannelTemplatePO> templatePOs = channelTemplateService.findByDeviceModel(
					channelTemplateService.get(channelSchemePOs.get(0).getChannelTemplateID()).getDeviceModel());
			//生成能力模板树
			ChannelTemplateTreeVO template = channelTemplateTreeUtil.createZTree(templatePOs);
			data.put("template", template);
			
			for (ChannelTemplatePO channelTemplate : templatePOs) {
				map.put(channelTemplate.getId(), new ArrayList<ChannelSchemePO>());
			}
			
			for (ChannelSchemePO channelSchemePO : channelSchemePOs) {
				map.get(channelSchemePO.getChannelTemplateID()).add(channelSchemePO);
			}
			List<ConfigParamVO> configParams = new ArrayList<ConfigParamVO>();
			for (Entry<Long, List<ChannelSchemePO>> entry : map.entrySet()) {
				Long templateId = entry.getKey();
				List<ChannelSchemePO> channelSchemes = entry.getValue();
				ConfigParamVO configParam = new ConfigParamVO();
				configParam.setChannelTemplateID(templateId);
				configParam.setChannelName(channelTemplateService.get(templateId).getChannelName());
				configParam.setChannelCnt(channelSchemes.size());
				configParams.add(configParam);
			}
			data.put("configParams", configParams);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	/**查询bundle对应的模板树、可编辑字段、通道（配置）*/
	@RequestMapping(value="/getBundleAbility",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBundleAbility(@RequestParam("bundleId") String bundleId){
		Map<String, Object> data = makeAjaxData();
		try {
			BundlePO bundlePO = bundleService.findByBundleId(bundleId);
			List<ChannelTemplatePO> channelTemplates = channelTemplateService.findByDeviceModel(bundlePO.getDeviceModel());
			if(channelTemplates.isEmpty()){
				data.put(ERRMSG, "不存在该设备类型对应的模板！");
				return data;
			}
			//生成模板属性树
			List<EleTreeNodeVO> tree = channelTemplateTreeUtil.createEleTree(channelTemplates);
			data.put("tree", tree);
			
			//生成通道数量配置
			Map<Long, List<ChannelSchemePO>> channelMap = new HashMap<>();
			List<ChannelSchemePO> channelSchemes = channelSchemeService.findByBundleId(bundleId);
			for (ChannelTemplatePO channelTemplate : channelTemplates) {
				channelMap.put(channelTemplate.getId(), new ArrayList<ChannelSchemePO>());
			}
			for (ChannelSchemePO channelScheme : channelSchemes) {
				channelMap.get(channelScheme.getChannelTemplateID()).add(channelScheme);
			}
			List<ConfigParamVO> configParams = new ArrayList<ConfigParamVO>();
			for (Entry<Long, List<ChannelSchemePO>> entry : channelMap.entrySet()) {
				Long templateId = entry.getKey();
				List<ChannelSchemePO> channelSchemePOs = entry.getValue();
				ConfigParamVO configParam = new ConfigParamVO();
				configParam.setChannelTemplateID(templateId);
				configParam.setChannelName(channelTemplateService.get(templateId).getChannelName());
				configParam.setChannelCnt(channelSchemePOs.size());
				configParams.add(configParam);
			}
			data.put("configChannels", configParams);
			
			//生成可编辑字段配置
			List<BundleEditableAttrVO> bundleEditableAttrs = new ArrayList<BundleEditableAttrVO>();
			List<TemplateEditableAttrPO> templateEditableAttrPOs = templateEditableAttrService.
					findByDeviceModel(bundlePO.getDeviceModel());
			for (TemplateEditableAttrPO templateEditableAttrPO : templateEditableAttrPOs) {
				BundleEditableAttrPO bundleEditableAttr = bundleEditableAttrService.findByBundleIdAndName(
						bundleId, templateEditableAttrPO.getName());
				if(null == bundleEditableAttr){
					bundleEditableAttrs.add(new BundleEditableAttrVO(templateEditableAttrPO.getName(), null, bundleId));
				} else {
					bundleEditableAttrs.add(new BundleEditableAttrVO(bundleEditableAttr.getName(), bundleEditableAttr.getValue(), bundleId));
				}
			}
			data.put("configEditableAttrs", bundleEditableAttrs);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	@RequestMapping(value="/configBundle",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> configBundle(@RequestParam("bundleId") String bundleId,
			@RequestParam("configChannels") String configChannels,@RequestParam("configEditableAttrs") String configEditableAttrs){
		Map<String, Object> data = makeAjaxData();
		try {
			//配置通道
			List<ConfigParamVO> configChannelList = JSONArray.parseArray(configChannels, ConfigParamVO.class);
			List<ChannelSchemePO> channelSchemesOnBundle = channelSchemeService.findByBundleId(bundleId);
			if(channelSchemesOnBundle.isEmpty()){//第一次配置
				for (ConfigParamVO configParam : configChannelList) {
					ChannelTemplatePO channelTemplatePO = channelTemplateService.get(configParam.getChannelTemplateID());
					Integer channelCnt = configParam.getChannelCnt();
					for (int i = 1; i <= channelCnt; i++) {
						ChannelSchemePO channelSchemePO = new ChannelSchemePO();
						channelSchemePO.setBundleId(bundleId);
						channelSchemePO.setChannelId(channelTemplatePO.getBaseType() + "_" + i);
						channelSchemePO.setChannelName(channelTemplatePO.getChannelName());
						channelSchemePO.setChannelTemplateID(configParam.getChannelTemplateID());
						channelSchemeService.save(channelSchemePO);
					}
				}
			} else { // 存在旧配置，修改配置
				for (ConfigParamVO configParam : configChannelList) {
					ChannelTemplatePO channelTemplatePO = channelTemplateService.get(configParam.getChannelTemplateID());
					Integer newChannelCnt = configParam.getChannelCnt();
					Integer oldChannelCnt = channelSchemeService.findByBundleIdAndChannelTemplateID(bundleId, configParam.getChannelTemplateID()).size();
					if(newChannelCnt > oldChannelCnt){//增加通道数
						for(int i = oldChannelCnt+1 ; i <= newChannelCnt ; i++){
							ChannelSchemePO channelSchemePO = new ChannelSchemePO();
							channelSchemePO.setBundleId(bundleId);
							channelSchemePO.setChannelId(channelTemplatePO.getBaseType() + "_" + i);
							channelSchemePO.setChannelName(channelTemplatePO.getChannelName());
							channelSchemePO.setChannelTemplateID(configParam.getChannelTemplateID());
							channelSchemeService.save(channelSchemePO);
						}
					} else if(newChannelCnt < oldChannelCnt){//减少通道数
						for (int i = newChannelCnt+1 ; i <= oldChannelCnt; i++) {
							String channelId = channelTemplatePO.getBaseType() + "_" + i;
							channelSchemeDao.deleteByBundleIdAndChannelId(bundleId, channelId);
							
							lockChannelParamDao.deleteByBundleIdAndChannelId(bundleId, channelId);
						}
						
					}
					
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							ModifyBundleConfigResp resp = (ModifyBundleConfigResp)interfaceFromResource.modifyBundleRequest(bundle);
//							//
//						}
//					}).start();
				}
			}
			
			//配置可编辑字段
			List<BundleEditableAttrPO> oldAttrs = bundleEditableAttrService.findByBundleId(bundleId);
			for (BundleEditableAttrPO oldAttrPO : oldAttrs) {
				bundleEditableAttrService.delete(oldAttrPO);
			}
			List<BundleEditableAttrVO> newAttrs = JSONArray.parseArray(configEditableAttrs, BundleEditableAttrVO.class);
			if (null != newAttrs) {
				for (BundleEditableAttrVO newAttrVO : newAttrs) {
					if(null != newAttrVO.getValue() && !newAttrVO.getValue().isEmpty()){
						bundleEditableAttrService.save(new BundleEditableAttrPO(newAttrVO.getName(), newAttrVO.getValue(), bundleId));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Fail to config bundle of id:" + bundleId,e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}
}
