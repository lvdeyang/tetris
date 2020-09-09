package com.suma.venus.resource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.suma.venus.resource.dao.ScreenRectTemplateDao;
import com.suma.venus.resource.pojo.ChannelParamPO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.service.ChannelParamService;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.ChannelTemplateConvertService;
import com.suma.venus.resource.service.ChannelTemplateService;
import com.suma.venus.resource.util.ChannelTemplateTreeUtil;
import com.suma.venus.resource.vo.ChannelTemplateTreeVO;
import com.suma.venus.resource.vo.ChannelTemplateVO;
import com.suma.venus.resource.vo.EleTreeNodeVO;

/**
 * 能力模板管理controller
 * @author lxw
 *
 */
@Controller
@RequestMapping("/template")
public class TemplateManageController extends ControllerBase{

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateManageController.class);
	
	@Autowired
	private ChannelTemplateService channelTemplateService;
	
	@Autowired
	private ChannelParamService channelParamService;
	
	@Autowired
	private ChannelTemplateConvertService channelTemplateConvertService;
	
	@Autowired
	private ChannelTemplateTreeUtil channelTemplateTreeUtil;
	
	@Autowired
	private ChannelSchemeService channelSchemeService;
	
	@Autowired
	private ScreenRectTemplateDao screenRectTemplateDao;
	
	@RequestMapping(value="/abilityTemplates",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryAbilityTemplates(@RequestParam("name") String name){
		Map<String, Object> data = makeAjaxData();
		try {
			List<ChannelTemplatePO> templatePOs = channelTemplateService.findByKeyword(name);
			if(null != templatePOs && !templatePOs.isEmpty()){
				List<ChannelTemplateVO> templates = new ArrayList<ChannelTemplateVO>();
				for (ChannelTemplatePO po : templatePOs) {
					ChannelTemplateVO vo = new ChannelTemplateVO(po.getDeviceModel(), po.getBundleType());
					if(!templates.contains(vo)){
						vo.setId(po.getId());
						templates.add(vo);
					}
				}
				data.put("templates", templates);
			}
		} catch (Exception e) {
			LOGGER.error(ERRMSG.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	@RequestMapping(value="/channelTemplates",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryChannelTemplates(@RequestParam("deviceModel") String deviceModel,@RequestParam("channelVersion") String channelVersion){
		Map<String, Object> data = makeAjaxData();
		try {
			List<ChannelTemplatePO> channelTemplatePOs = channelTemplateService.findByDeviceModel(deviceModel);
			List<ChannelTemplateVO> channelTemplates = new ArrayList<ChannelTemplateVO>();
			for (ChannelTemplatePO po : channelTemplatePOs) {
				ChannelTemplateVO vo = new ChannelTemplateVO(deviceModel, po.getBundleType());
				vo.setId(po.getId());
				vo.setChannelName(po.getChannelName());
				channelTemplates.add(vo);
			}
			
			data.put("channelTemplates",channelTemplates);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	@RequestMapping(value="/import",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importTemplate(@RequestParam("filePoster") MultipartFile uploadFile){
		Map<String, Object> data = makeAjaxData();

		try {
			channelTemplateConvertService.convertTemplate(new String(uploadFile.getBytes()));
		} catch (Exception e) {
			LOGGER.error(e.toString());
			if("TEMPLATE_EXIST".equals(e.getMessage())){
				data.put(ERRMSG, "模板已存在！");
			}else{
				data.put(ERRMSG, "内部错误");
			}
		}
		
		return data;
	}
	
	@RequestMapping(value="/detail",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> detail(@RequestParam("channelTemplateId") Long channelTemplateId){
		Map<String, Object> data = makeAjaxData();

		try {
			ChannelTemplatePO channelTemplate = channelTemplateService.get(channelTemplateId);
			List<ChannelTemplatePO> templates = channelTemplateService.findByDeviceModel(channelTemplate.getDeviceModel());
			ChannelTemplateTreeVO vo = channelTemplateTreeUtil.createZTree(templates);
			data.put("tree", vo.getParamTree());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	
	@RequestMapping(value="/tree",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> tree(@RequestParam("channelTemplateId") Long channelTemplateId){
		Map<String, Object> data = makeAjaxData();

		try {
			ChannelTemplatePO channelTemplate = channelTemplateService.get(channelTemplateId);
			List<ChannelTemplatePO> templates = channelTemplateService.findByDeviceModel(channelTemplate.getDeviceModel());
			List<EleTreeNodeVO> tree = channelTemplateTreeUtil.createEleTree(templates);
			data.put("tree", tree);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	
	@RequestMapping(value="/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@RequestParam("templateIds") String templateIds){
		Map<String, Object> data = makeAjaxData();

		try {
			String[] template_id_array = templateIds.split(";");
			for (String template_id : template_id_array) {
				ChannelTemplatePO template = channelTemplateService.get(Long.valueOf(template_id));
				List<ChannelTemplatePO> channelTemplates = channelTemplateService.findByDeviceModel(template.getDeviceModel());
				if(!channelTemplates.isEmpty()){
					boolean canDelete = true;
					for (ChannelTemplatePO channelTemplate : channelTemplates) {
						//检查是否有bundle配置了该通道模板
						List<ChannelSchemePO> channelSchemes = channelSchemeService.findByChannelTemplateID(channelTemplate.getId());
						if(channelSchemes.size() > 0){
							canDelete = false;
						}
					}
					
					if(canDelete){
						for (ChannelTemplatePO channelTemplate : channelTemplates) {
							//删除模板参数表
							List<ChannelParamPO> channelParams = channelParamService.findByParentChannelTemplateId(channelTemplate.getId());
							for (ChannelParamPO channelParam : channelParams) {
								channelParamService.deleteParamAndDescendant(channelParam);
							}
							
							//删除模板表
							channelTemplateService.delete(channelTemplate);
						}
						
						List<ScreenRectTemplatePO> screenRectTemplatePOs = screenRectTemplateDao.findByDeviceModel(template.getDeviceModel());
						if(null != screenRectTemplatePOs){
							for (ScreenRectTemplatePO screenRectTemplatePO : screenRectTemplatePOs) {
								screenRectTemplateDao.delete(screenRectTemplatePO);
							}
						}
						
					} else {
						data.put(ERRMSG, "存在能力资源配置了该模板，故无法删除！");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
	
	@RequestMapping(value="/getDeviceModels",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllDeviceModel(){
		Map<String, Object> data = makeAjaxData();
		try {
			Set<String> deviceModels = channelTemplateService.findAllDeviceModel();
			data.put("deviceModels", deviceModels);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}
	
}
