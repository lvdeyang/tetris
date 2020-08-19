package com.sumavision.bvc.control.device.jv230.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.device.jv230.tree.handler.impl.EncodeExceptTemplateQueryHandlerImpl;
import com.sumavision.bvc.control.device.jv230.tree.handler.impl.RootExceptSuperQueryhandlerImpl;
import com.sumavision.bvc.control.device.jv230.tree.util.UITreeUtil;
import com.sumavision.bvc.control.device.jv230.tree.vo.UINodeVO;
import com.sumavision.bvc.control.device.jv230.vo.CombineJv230ConfigVO;
import com.sumavision.bvc.control.device.jv230.vo.ConfigLocationVo;
import com.sumavision.bvc.control.device.jv230.vo.ConfigTaskVO;
import com.sumavision.bvc.control.device.jv230.vo.DecodeVo;
import com.sumavision.bvc.control.device.jv230.vo.LargescreenInfoVo;
import com.sumavision.bvc.control.device.jv230.vo.PhyscreenVo;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.jv230.dao.CombineJv230ConfigDAO;
import com.sumavision.bvc.device.jv230.dao.CombineJv230DAO;
import com.sumavision.bvc.device.jv230.po.CombineJv230ConfigPO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.ConfigLocationPO;
import com.sumavision.bvc.device.jv230.po.ConfigTaskPO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.bvc.device.jv230.service.LargescreenControlServiceImpl;
import com.sumavision.tetris.commons.exception.CommonException;

@Controller
@RequestMapping(value = "/largescreenControlController")
public class LargescreenControlcontroller {
	
	@Autowired
	private CombineJv230ConfigDAO combineJv230ConfigDao;
	
	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private LargescreenControlServiceImpl largescreenControlServiceImpl;

	/**
	 * @Title: getLargescreenRelationPhyscreen 
	 * @Description: 获取一个大屏信息
	 * @param devId 大屏id
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/getLargescreenRelationPhyscreen")
	public Map<String, Object> getLargescreenRelationPhyscreen(
			Long devId,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//获取大屏信息
			LargescreenInfoVo largescreenInfo = new LargescreenInfoVo();

			CombineJv230PO largescreen = combineJv230Dao.findOne(devId);
			
			if(largescreen == null){
				largescreenInfo = null;
			}else{
				List<PhyscreenVo> phyVos = new ArrayList<PhyscreenVo>();
				Set<Jv230PO> phys = largescreen.getBundles();
				if(phys == null){
					largescreenInfo = null;
				}else{
					for(Jv230PO phy:phys){
						Set<Jv230ChannelPO> decodes = phy.getChannels();
						List<Jv230ChannelPO> decodeList = new ArrayList<Jv230ChannelPO>();
						for(Jv230ChannelPO decode: decodes){
							if(decode.getType().isVideoDecode()){
								decodeList.add(decode);
							}
						}
						List<DecodeVo> decodeVos = DecodeVo.getConverter(DecodeVo.class).convert(decodeList, DecodeVo.class);
						PhyscreenVo phyVo = new PhyscreenVo();
						phyVo = new PhyscreenVo().set(phy);
						phyVo.getDecodeList().addAll(decodeVos);
						phyVos.add(phyVo);
					}
					largescreenInfo.set(largescreen);
					largescreenInfo.getPhyscreens().addAll(phyVos);
				}
			}
			
			map.put("largescreenInfo", largescreenInfo);
		}catch(Exception e){
			e.printStackTrace();
			if (handleException(e) != null) {
				map.put("msg", handleException(e));
			} else {
				map.put("msg", "未知程序异常");
			}
		}
		return map;
	}
	
	/**
	 *  获取一个大屏配置的详细信息 
	 * @Title: getLargescreenConfigListInfo 
	 * @Description: 获取一个大屏配置的详细信息 
	 * @param largescreenConfigId 大屏配置id
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/getLargescreenConfigListInfo")
	public Map<String, Object> getLargescreenConfigListInfo(
			Long largescreenConfigId,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try{			
			CombineJv230ConfigPO largescreenConfigList = combineJv230ConfigDao.findOne(largescreenConfigId);
			CombineJv230ConfigVO largescreenConfigListVo = new CombineJv230ConfigVO().set(largescreenConfigList);

			Set<ConfigTaskPO> configTaskList = largescreenConfigList.getConfigTasks();
			
			for(ConfigTaskPO configTask:configTaskList){
				ConfigTaskVO configTaskVo = new ConfigTaskVO().set(configTask);
				Set<ConfigLocationPO> configLocationList = configTask.getConfigLocations();
				for(ConfigLocationPO configLocation:configLocationList){
					ConfigLocationVo configLocationVo = new ConfigLocationVo().set(configLocation);
					configTaskVo.getConfigLocationList().add(configLocationVo);
				}
				largescreenConfigListVo.getConfigTaskVoList().add(configTaskVo);
			}
			
			map.put("largescreenConfigInfo", largescreenConfigListVo);
		}catch(Exception e){
			e.printStackTrace();
			if (handleException(e) != null) {
				map.put("msg", handleException(e));
			} else {
				map.put("msg", "未知程序异常");
			}
		}
		return map;
	}
		
	/**
	 *  获取设备列表
	 * @Title: getEncodeBelongInst 
	 * @param instId 层级（文件夹）id
	 * @param openLevel 打开层级数
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value="/getEncodeBelongInst")
	public Map<String, Object> getEncodeBelongInst(
			String instId,
			String openLevel,
			HttpServletRequest request, 
			HttpServletResponse response){
	    Map<String, Object> map = new HashMap<String, Object>();
	    try {
	    	//获取userId
			long accountId = userUtils.getUserIdFromSession(request);
			
			//获取根节点组织
			if(instId == null){
				//创建根节点
				UINodeVO rootNode = UITreeUtil.getTree(openLevel, new RootExceptSuperQueryhandlerImpl(), new EncodeExceptTemplateQueryHandlerImpl(), true, false, accountId);
				sortedUINodeVOList(rootNode.getChildrenList(), "nodeContent");
				map.put("node", rootNode);
			}else{
				JSONObject params = new JSONObject();
				params.put("instId", instId);
				params.put("accountId", accountId);
				List<UINodeVO> nodeList = UITreeUtil.getNodeList(new EncodeExceptTemplateQueryHandlerImpl(params), false, true, false);
				sortedUINodeVOList(nodeList, "nodeContent");
				map.put("nodeList", nodeList);
			}
			
	        map = packSuccessMap(map);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	map = packServerExceptionMap(map);
			if(handleException(e) != null){
				map.put("msg", handleException(e));
			}else if(e.getMessage() != null){
				map.put("msg", e.getMessage());
			}else{
				map.put("msg", "未知的程序异常");
			}
	    }
	    return map;
	}
	
	/**
	 *  获取轮询设备列表
	 * @Title: getPollingEncodeBelongInst 
	 * @param instId 层级（文件夹）id
	 * @param openLevel 打开层级数
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value="/getPollingEncodeBelongInst")
	public Map<String, Object> getPollingEncodeBelongInst(
			String instId,
			String openLevel,
			HttpServletRequest request, 
			HttpServletResponse response){
	    Map<String, Object> map = new HashMap<String, Object>();
	    try {
	    	//获取userId
			long accountId = userUtils.getUserIdFromSession(request);
			
			//获取根节点组织
			if(instId == null){
				//创建根节点
				UINodeVO rootNode = UITreeUtil.getTree(openLevel, new RootExceptSuperQueryhandlerImpl(), new EncodeExceptTemplateQueryHandlerImpl(), false, true, accountId);
				sortedUINodeVOList(rootNode.getChildrenList(), "nodeContent");
				map.put("node", rootNode);
			}else{
				JSONObject params = new JSONObject();
				params.put("instId", instId);
				params.put("accountId", accountId);
				List<UINodeVO> nodeList = UITreeUtil.getNodeList(new EncodeExceptTemplateQueryHandlerImpl(params), false, false, true);
				sortedUINodeVOList(nodeList, "nodeContent");
				map.put("nodeList", nodeList);
			}
			
	        map = packSuccessMap(map);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	map = packServerExceptionMap(map);
			if(handleException(e) != null){
				map.put("msg", handleException(e));
			}else if(e.getMessage() != null){
				map.put("msg", e.getMessage());
			}else{
				map.put("msg", "未知的程序异常");
			}
	    }
	    return map;
	}
	
	/**
	 * 保存一个大屏配置
	 * @Title: saveLargescreenConfigList 
	 * @Description: 保存一个大屏配置
	 * @param @param request
	 * @param @param response
	 * @param @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/saveLargescreenConfigList")
	public Map<String, Object> saveLargescreenConfigList(
			Long largescreenConfigId,
			String task,
			HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try{					
			//保存大屏配置信息
			CombineJv230ConfigPO config = largescreenControlServiceImpl.saveLargescreenConfigList(largescreenConfigId, task);
			
			map.put("id", config.getId());
			
			map = packSuccessMap(map);
		}catch(Exception e){
			e.printStackTrace();
			if (handleException(e) != null) {
				map.put("msg", handleException(e));
			} else {
				map.put("msg", "未知程序异常");
			}
		}
		return map;
	}
	
	/**
	 * 保存一个大屏混音配置
	* @Title: saveLargescreenAudio 
	* @Description: 保存一个大屏混音配置
	* @param largescreenId 大屏id
	* @param largescreenConfigId 大屏配置id
	* @param task audio任务
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/saveLargescreenAudio")
	public Map<String, Object> saveLargescreenAudio(
			Long largescreenId,
			Long largescreenConfigId,
			String task,
			HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			
			//保存一个大屏混音配置信息
			largescreenControlServiceImpl.saveLargescreenAudio(largescreenConfigId, task);
			
			map = packSuccessMap(map);
		}catch(Exception e){
			e.printStackTrace();
			if (handleException(e) != null) {
				map.put("msg", handleException(e));
			} else {
				map.put("msg", "未知程序异常");
			}
		}
		return map;
	}
	
	/**
	 * 上屏操作（先删除上屏再上屏）
	 * @Title: startLargescreenControl 
	 * @param largescreenConfigId 大屏配置id
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/startLargescreenControl")
	public Map<String, Object> startLargescreenControl(
			Long largescreenConfigId,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//获取userId
			long accountId = userUtils.getUserIdFromSession(request);
			
			largescreenControlServiceImpl.startLargescreenControl(largescreenConfigId, accountId);
			
			map = packSuccessMap(map);
		}catch(Exception e){
			e.printStackTrace();
			if (handleException(e) != null) {
				map.put("msg", handleException(e));
			} else {
				map.put("msg", "未知程序异常");
			}
		}
		return map;
	}
	
	/**
	 * 下屏操作
	 * @Title: stopLargescreenControl 
	 * @param largescreenConfigId 大屏配置id
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/stopLargescreenControl")
	public Map<String, Object> stopLargescreenControl(
			Long largescreenConfigId,
			HttpServletRequest request,
			HttpServletResponse response){
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//获取userId
			long accountId = userUtils.getUserIdFromSession(request);
			
			largescreenControlServiceImpl.stopLargescreenControl(largescreenConfigId, accountId);
			
			map = packSuccessMap(map);
		}catch(Exception e){
			e.printStackTrace();
			if (handleException(e) != null) {
				map.put("msg", handleException(e));
			} else {
				map.put("msg", "未知程序异常");
			}
		}
		return map;
	}
	
	/******************
	 ***   公共方法        ***
	 ******************/
	
	//排序UINodeVO类
	public <T> void sortedUINodeVOList(List<UINodeVO> list, String fieldName){
		if(list!=null && list.size()>0){
			for (UINodeVO uiNodeVO : list) {
				List<UINodeVO> childList = uiNodeVO .getChildrenList();
				if(childList!=null && childList.size()>0){
					sortedUINodeVOList(childList, fieldName);
				}
			}
			sorted(list, fieldName);
		}
	}
	
	//根据指定字段排序
	public <T> void sorted(List<T> list, String fieldName){
		final String targetFieldName = fieldName;
		if(list!=null && list.size()>1){
			Collections.sort(list, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					String o1Value = getFieldValue(o1, targetFieldName);
					String o2Value = getFieldValue(o2, targetFieldName);
					return o1Value.compareTo(o2Value);
				}
			});
		}
	}
	
	public <T> String getFieldValue(T t, String fieldName){
		try {
			Class<? extends Object> tClass = t.getClass();
			java.lang.reflect.Field field = (java.lang.reflect.Field) tClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return String.valueOf(field.get(t));
		} catch (Exception e) {
			throw new CommonException(new Error("排序时未获取到比较元素！"));
		}
	}
	
	protected static String handleException(Exception e) {
		String msg = "";
		if (e instanceof InvocationTargetException) {
			Throwable targetEx = ((InvocationTargetException) e).getTargetException();
			if (targetEx != null) {
				msg = targetEx.getMessage();
			}
		} else {
			msg = e.getMessage();
		}
		
		return msg;
	}
	
	//成功状态
	protected Map<String, Object> packSuccessMap(Map<String, Object> map){
		map.put("status", "200");
		map.put("message", "成功");

		return map;
	}
	
	//服务器异常状态
	protected Map<String, Object> packServerExceptionMap(Map<String, Object> map){
		map.put("status", "500");
		map.put("message", "服务器程序异常");
		return map;
	}
}
