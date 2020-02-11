package com.sumavision.bvc.meeting.logic;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.logic.LogicTreatCompo;
import com.sumavision.bvc.meeting.logic.execption.ExecuteBusinessException;
import com.sumavision.bvc.meeting.logic.record.RecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  ExecuteBusiness   
 * @Description: 供业务层调用的接口：executeJsonOrder
 * @author: zsy
 * @date:   2018年7月13日 下午2:46:48   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Slf4j
@Service
public class ExecuteBusiness {
	@Autowired
	MeetingEntity meetingEntity;
	@Autowired
	RecordService recordService;
	@Autowired
	LogicTreatCompo logicTreatCompo;
	@Autowired
	private ResourceService resourceService;
	
	//点播系统模式下，使用Admin锁定资源
	public static final String username = "应急广播管理员用户";
	public static Long adminUserId = null;
	
	//以下配置会从logic.properties读取，不要在这里改
	String juintLogic = "true";
	String juintCdn = "true";
//	String systemMode = "vod";
	
	public ExecuteBusinessReturnBO executeJsonOrder(JSONObject jsonFromBusiness) throws ExecuteBusinessException{
		
//		jsonFromBusiness.put("systemMode", systemMode);
		
		//测试：手动补上userId
		if(!jsonFromBusiness.containsKey("userId")){
			jsonFromBusiness.put("userId", "1");
		}
		
		//点播系统模式下，使用Admin锁定资源（已不再使用）
//		if("vod".equals(systemMode)){
//			if(null == adminUserId){
//				UserBO admin = resourceService.queryUserInfoByUsername(username);
//				adminUserId = admin.getId().toString();
//			}			
//			jsonFromBusiness.put("userId", adminUserId);
//		}
		if(null == adminUserId){
			UserBO admin = resourceService.queryUserInfoByUsername(username);
			adminUserId = admin.getId();
		}
		
		//处理OMC对接
		if(juintCdn.equals("true")){
			//OMC开始录制，包括CDN录制，BO添加直播
			try {
				recordService.startOmcRecords(jsonFromBusiness);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ExecuteBusinessException("CDN对接异常");
			}
			
			//OMC停止录制，包括CDN停止录制，CDN转点播，BO直播转点播
			recordService.stopOmcRecords(jsonFromBusiness);
		}
		
		//处理基本命令
		JSONObject combinedOperation;
		try {
			combinedOperation = meetingEntity.operationCombine(jsonFromBusiness);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new ExecuteBusinessException("参数解析有误");
		}
		log.info("基本命令转换得到并下发：");
		System.out.println(combinedOperation);
		//下发执行，得到结果
		JSONObject result = new JSONObject();
		if(juintLogic.equals("true")){
			String resultStr = logicTreatCompo.operateChannels(JSON.toJSONString(combinedOperation, SerializerFeature.DisableCircularReferenceDetect));
			result = JSON.parseObject(resultStr);
		}
		//判断是否成功
		int resultCode = 0;
		try{
			resultCode = result.getIntValue("result");
			result.getString("errMsg");
		}catch(Exception e){
			log.info("执行结果解析失败：result = " + result);
		}
		if(0 != resultCode){
			//执行失败
			log.info("执行失败，errMsg = " + result.getString("errMsg"));
			throw new ExecuteBusinessException(result.getString("errMsg"));
		}
		JSONObject successJson = new JSONObject();
		successJson.put("executeResult", result);
		successJson.put("combinedOperation", combinedOperation);
		meetingEntity.executeSuccess(successJson);
		
		//返回bundle锁定结果、录制通道信息
		ExecuteBusinessReturnBO returnBO = JSONObject.parseObject(JSONObject.toJSONString(result), ExecuteBusinessReturnBO.class);
		return returnBO;
	}
	
	/*
	 * 配置文件 logic.properties 中的两个参数
	 * logic.juintLogic 是否进行通道锁定和打开，即是否调用logicTreatCompo.operateChannels，开发业务时可以置为false
	 * logic.juintCdn 是否与CDN和BO对接
	 */
	@Autowired
	public void getConfig() {
		try {
			Properties prop = new Properties();
			prop.load(ExecuteBusiness.class.getClassLoader().getResourceAsStream("logic.properties"));
			juintLogic = prop.getProperty("logic.juintLogic");
			juintCdn = prop.getProperty("logic.juintCdn");
//			systemMode = prop.getProperty("logic.systemMode");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
