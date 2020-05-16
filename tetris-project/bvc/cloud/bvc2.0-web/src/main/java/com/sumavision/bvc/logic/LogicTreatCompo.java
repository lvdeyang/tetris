package com.sumavision.bvc.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.ChannelTemplateService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.BO.BundleSchemeBO;
import com.sumavision.bvc.BO.Bundle_ChannelBO;
import com.sumavision.bvc.BO.BusinessOperateBO;
import com.sumavision.bvc.BO.ChannelSchemeBO;
import com.sumavision.bvc.BO.ChannelStatusBody;
import com.sumavision.bvc.BO.CombineAudioOperateBO;
import com.sumavision.bvc.BO.CombineVideoLayoutBO.LayoutPosBO;
import com.sumavision.bvc.BO.CombineVideoOperateBO;
import com.sumavision.bvc.BO.ConnectBO;
import com.sumavision.bvc.BO.ConnectBundleBO;
import com.sumavision.bvc.BO.ForwardSetBO;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;
import com.sumavision.bvc.BO.MediaMuxOutBO;
import com.sumavision.bvc.BO.MediaPushOperateBO;
import com.sumavision.bvc.DTO.ResultMap;
import com.sumavision.bvc.common.CommonConstant;
import com.sumavision.bvc.common.CommonConstant.ChannelTypeInfo;
import com.sumavision.bvc.common.CommonConstant.OperateType;
import com.sumavision.bvc.monitor.logic.manager.AcessLayerTask;
import com.sumavision.bvc.monitor.logic.manager.ResourseLayerTask;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  LogicTreat   
 * @Description:逻辑处理，处理业务层传下来的各种业务，包括但不限于呼叫、预览、转发、合屏/混音  
 * @author: 
 * @date:   2018年7月13日 下午2:46:48   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Component
@Scope("singleton")
public class LogicTreatCompo{
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ChannelSchemeService channelService;
	
	@Autowired
	private ChannelTemplateService channelTemplateService;
	
	@Autowired
	private ResourseLayerTask resourseLayer;
	
	@Autowired
	private AcessLayerTask accessLayer;
	
	/**
	 * 已选定的bundle下的channels
	 */
	private Map<String, ChannelInfo> bundleChannelMap = Collections.synchronizedMap(new HashMap<String, ChannelInfo>());
		
	/**
	 * 逻辑层提供给业务层的主接口，处理各种业务操作
	 * @param cmd
	 */
	public String operateChannels(String cmd){
		try{
			log.info("receive cmd is " + cmd);
			Date start = new Date();
//			JSONObject obj = JSONObject.parseObject(cmd);
//			String realCmd = JSONObject.toJSONString(obj);
			BusinessOperateBO businessOperateBO = JSONObject.parseObject(cmd, BusinessOperateBO.class);		
			log.info("JSONObject.parseObject 完成 cost " + (new Date().getTime()-start.getTime()) + " ms");
			if(businessOperateBO != null){
				//存储来电通知uuid和返回成功标志对
				ResultMap incomingCallResultMap = null;
				//存储新建合屏的uuid和锁定通道成功标志对
				ResultMap lockResultMap = null;
				//存储新建合屏的uuid和打开通道成功标志对
				ResultMap openResultMap = null;
				
				//存储修改合屏的uuid和锁定通道成功标志对
				ResultMap modify_lockResultMap = null;
				//存储修改合屏的uuid和修改通道成功标志对
				ResultMap modifyResultMap = null;
				
				//存储删除合屏的taskId和释放通道成功标志对
				ResultMap del_unlockResultMap = null;
				//存储删除合屏的taskId和关闭通道成功标志对
				ResultMap del_closeResultMap = null;
				
				
				//存储新建混音的uuid和锁定通道成功标志对
				ResultMap audio_lockResultMap = null;
				//存储新建混音的uuid和打开通道成功标志对
				ResultMap audio_openResultMap = null;
				
				//存储修改混音的uuid和锁定通道成功标志对
				ResultMap audio_modifyLockResultMap = null;
				//存储修改混音的uuid和修改通道成功标志对
				ResultMap audio_modifyResultMap = null;
				
				//存储删除混音的taskId和释放通道成功标志对
				ResultMap audio_del_unlockResultMap = null;
				//存储删除混音的taskId和关闭通道成功标志对
				ResultMap audio_del_closeResultMap = null;
				
				
				//存储新建MediaMuxOut的taskId和锁定通道成功标志对
				ResultMap mediaMux_lockResultMap = null;
				//存储新建新建MediaMuxOut的taskId和打开通道成功标志对
				ResultMap mediaMux_openResultMap = null;

				//存储修改MediaMuxOut的taskId和锁定通道成功标志对
				ResultMap modifyMediaMux_lockResultMap = null;
				//存储修改混音的uuid和修改通道成功标志对
				ResultMap modifyMediaMux_openResultMap = null;
				
				//存储删除MediaMuxOut的taskId和释放通道成功标志对
				ResultMap mediaMux_del_unlockResultMap = null;
				//存储删除MediaMuxOut的taskId和关闭通道成功标志对
				ResultMap mediaMux_del_closeResultMap = null;
				
				
				//存储新建MediaPush的taskId和锁定通道成功标志对
				ResultMap mediaPush_lockResultMap = null;
				//存储新建MediaPush的taskId和打开通道成功标志对
				ResultMap mediaPush_openResultMap = null;

//				//存储修改MediaPush的taskId和锁定通道成功标志对
//				ResultMap modifyMediaPush_lockResultMap = null;
//				//存储修改MediaPush的uuid和修改通道成功标志对
//				ResultMap modifyMediaPush_openResultMap = null;
				
				//存储删除MediaPush的taskId和释放通道成功标志对
				ResultMap mediaPush_del_unlockResultMap = null;
				//存储删除MediaPush的taskId和关闭通道成功标志对
				ResultMap mediaPush_del_closeResultMap = null;
				
				
				//存储删除大屏视频的taskId和释放通道成功标志对
				ResultMap jv230Forward_del_unlockResultMap = null;
				//存储删除大屏视频的taskId和关闭通道成功标志对
				ResultMap jv230Forward_del_closeResultMap = null;
				
				//存储删除大屏音频的taskId和释放通道成功标志对
				ResultMap jv230Audio_del_unlockResultMap = null;
				//存储删除大屏音频的taskId和关闭通道成功标志对
				ResultMap jv230Audio_del_closeResultMap = null;
				
				//存储大屏的taskId和锁定通道成功标志对
				ResultMap jv230_lockResultMap = null;
				//存储大屏的taskId和打开通道成功标志对
				ResultMap jv230_openResultMap = null;
				
				//存储呼叫的taskId和锁定通道成功标志对
				ResultMap connect_lockResultMap = null;
				//存储锁定的taskId和锁定通道成功标志对
				ResultMap only_lockResultMap = null;
				//存储呼叫的taskId和打开通道成功标志对
				ResultMap connect_openResultMap = null;
				
				//存储挂断的taskId和释放通道成功标志对
				ResultMap disconnect_unlockResultMap = null;
				//存储释放和释放通道成功标志对
				ResultMap only_unlockResultMap = null;
				//存储挂断的taskId和关闭通道成功标志对
				ResultMap disconnect_closeResultMap = null;
				
				//存储转发的taskId和锁定通道成功标志对
				ResultMap forwardSet_lockResultMap = null;
				//存储转发的taskId和修改通道成功标志对
				ResultMap forwardSet_modifyResultMap = null;
				
				JSONArray combineVideoResult = null;
				JSONArray combineAudioResult = null;
				JSONArray mediaMuxResult = null;
				JSONArray mediaPushResult = null;
				JSONArray connectBundleResult = new JSONArray();//记录bundle锁定结果，反馈到业务层
				String errMsg = null;
				/**
				 * videoSetMap存储合屏的解码通道与其编码通道uuid的对应关系；
				 * uuid2ChannelSelMap存储合屏的编码uuid与选择的合屏能力通道(List中先放编码通道，后放解码通道)的对应关系
				 * videoUpdateMap存储修改合屏的解码通道与其编码通道uuid的对应关系
				 * uuid2ChannelSetMap存储修改合屏的编码uuid与合屏能力通道(解码通道)的对应关系
				 */
				Map<String, CombineVideoOperateBO> videoSetMap = new HashMap<String, CombineVideoOperateBO>();
				Map<String, List<ChannelSchemeBO>> uuid2ChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
				Map<String, CombineVideoOperateBO> videoUpdateMap = new HashMap<String, CombineVideoOperateBO>();
				Map<String, List<ChannelSchemeBO>> uuid2ChannelUpdateMap = new HashMap<String, List<ChannelSchemeBO>>();
				
				Map<String, CombineAudioOperateBO> audioSetMap = new HashMap<String, CombineAudioOperateBO>();
				Map<String, List<ChannelSchemeBO>> audio_uuid2ChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
				Map<String, CombineAudioOperateBO> audioUpdateMap = new HashMap<String, CombineAudioOperateBO>();
				Map<String, List<ChannelSchemeBO>> audio_uuid2ChannelUpdateMap = new HashMap<String, List<ChannelSchemeBO>>();
				
				Map<String, MediaMuxOutBO> mediaMuxSetMap_cdn = new HashMap<String, MediaMuxOutBO>();
				Map<String, List<ChannelSchemeBO>> mediaMux_taskIdChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
				Map<String, MediaMuxOutBO> mediaMuxSetMap_emr = new HashMap<String, MediaMuxOutBO>();

				Map<String, MediaMuxOutBO> mediaMuxUpdateMap = new HashMap<String, MediaMuxOutBO>();
				Map<String, List<ChannelSchemeBO>> mediaMux_taskId2ChannelUpdateMap = new HashMap<String, List<ChannelSchemeBO>>();
				
				Map<String, MediaPushOperateBO> mediaPushSetMap = new HashMap<String, MediaPushOperateBO>();
				Map<String, List<ChannelSchemeBO>> mediaPush_taskId2ChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
//				Map<String, MediaPushOperateBO> mediaPushUpdateMap = new HashMap<String, MediaPushOperateBO>();
				Map<String, List<ChannelSchemeBO>> mediaPush_taskId2ChannelUpdateMap = new HashMap<String, List<ChannelSchemeBO>>();
				
				List<ChannelSchemeBO> dstChannelsToLock = new ArrayList<ChannelSchemeBO>();
				List<BundleSchemeBO> dstBundlesToLock = new ArrayList<BundleSchemeBO>();
				
				/**
				 * pass_by				
				 */
				accessLayer.sendPassBys(businessOperateBO.getPass_by());
				/**
				 * 挂断通知
				 */
				accessLayer.sendHangUps(businessOperateBO.getHang_up_request());
				
				/**
				 * 来电通知				 
				 */
				Date incomingStart = new Date();
				incomingCallResultMap = accessLayer.sendIncomingCalls(businessOperateBO.getIncoming_call_request());
				log.info("------------------------incoming cost " + (new Date().getTime()-incomingStart.getTime())/1000 + " s");
				/**
				 * 新建合屏
				 */
				if(businessOperateBO.getCombineVideoSet() != null && !businessOperateBO.getCombineVideoSet().isEmpty()){										
					for(CombineVideoOperateBO operateBO:businessOperateBO.getCombineVideoSet()){
						if(StringUtils.isEmpty(operateBO.getUuid())){
							operateBO.setUuid(UUID.randomUUID().toString());
						}
						videoSetMap.put(operateBO.getUuid(), operateBO);
					}
					//查询合屏能力
					List<BundlePO> dstBundlePOs = bundleService.queryByUserIdAndDevcieModelAndKeyword(businessOperateBO.getUserId(), CommonConstant.COMBINE_VIDEO, null);
					Set<String> bundleIds = new HashSet<String>();
					if(dstBundlePOs != null){
						for(BundlePO bundlePO:dstBundlePOs){
							bundleIds.add(bundlePO.getBundleId());
						}
					}
					log.info("--------------------MixVideo bundles are " + bundleIds);
					//选择合屏能力
					uuid2ChannelSelMap = selectCombineVideoCapability(bundleIds, videoSetMap);
					
					if(uuid2ChannelSelMap == null || uuid2ChannelSelMap.size() != videoSetMap.size()){
						if(uuid2ChannelSelMap.size()>0){
							for(String uuid:uuid2ChannelSelMap.keySet()){						
								errMsg = "没有足够可用的合屏能力通道,合屏bundleId为：" + uuid2ChannelSelMap.get(uuid).get(0).getBundleId();
								break;
							}
						}else {
							errMsg = "合屏能力已占满" ;
						}
						removeResourcesInTask(uuid2ChannelSelMap);
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();
						
					}
					
					for(String uuid:uuid2ChannelSelMap.keySet()){						
						dstChannelsToLock.add(uuid2ChannelSelMap.get(uuid).get(0));
						
					}
										
					
					/*//锁定合屏能力通道
					lockResultMap = resourseLayer.lockResource(uuid2ChannelSelMap, businessOperateBO.getUserId());
					//去掉本次合屏操作在bundleChannelMap存储的bundleChannel对
					removeResourcesInTask(uuid2ChannelSelMap);
					*//**
					 * 新建合屏,打开合屏通道
					 *//*
					openResultMap = accessLayer.openCombineVideoOrAudio(uuid2ChannelSelMap, lockResultMap, businessOperateBO.getUserId(),ChannelTypeInfo.VIDEOENCODE);*/
					combineVideoResult = generateResultJsonArray(uuid2ChannelSelMap);
				}
				/**
				 * 修改合屏
				 */
				if(businessOperateBO.getCombineVideoUpdate() != null && !businessOperateBO.getCombineVideoUpdate().isEmpty()){
					for(CombineVideoOperateBO operateBO:businessOperateBO.getCombineVideoUpdate()){
						if(StringUtils.isEmpty(operateBO.getUuid())){
							operateBO.setUuid(UUID.randomUUID().toString());
						}
						videoUpdateMap.put(operateBO.getUuid(), operateBO);
					}
					//组装生成合屏能力
					uuid2ChannelUpdateMap = generateCombineVideoCapability(videoUpdateMap);
					
					if(uuid2ChannelUpdateMap == null || uuid2ChannelUpdateMap.size() != videoUpdateMap.size()){
						removeResourcesInTask(uuid2ChannelSelMap);
						if(uuid2ChannelUpdateMap.size()>0){
							for(String uuid:uuid2ChannelUpdateMap.keySet()){
								errMsg = "组装合屏能力通道出错,合屏bundleId为：" + uuid2ChannelUpdateMap.get(uuid).get(0).getBundleId();
								break;
							}
						}else{
							errMsg = "组装合屏能力通道出错,无可用合屏";
						}
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					
					for(String uuid:uuid2ChannelUpdateMap.keySet()){
						dstChannelsToLock.add(uuid2ChannelUpdateMap.get(uuid).get(0));
					}
					/*//锁定合屏能力通道
					modify_lockResultMap = resourseLayer.lockResource(uuid2ChannelUpdateMap, businessOperateBO.getUserId());
					*//**
					 * 修改合屏,打开合屏通道
					 *//*
					modifyResultMap = accessLayer.openCombineVideoOrAudio(uuid2ChannelUpdateMap, modify_lockResultMap, businessOperateBO.getUserId(),ChannelTypeInfo.VIDEOENCODE);*/
				}
				
				//合屏处理完后，需要处理迭代三新增的以合屏为源的情况
				for(ChannelSchemeBO combineVideoChannel:dstChannelsToLock){
					if(!treatCombineVideoSource(combineVideoChannel.getCombineVideoSrc(), uuid2ChannelSelMap, uuid2ChannelUpdateMap)){
						removeResourcesInTask(uuid2ChannelSelMap);
						errMsg = "合屏命令有误，可能有不存在的合屏充当源";
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();
					}
				}
				
				/**
				 * 新建混音
				 */
				if(businessOperateBO.getCombineAudioSet() != null && !businessOperateBO.getCombineAudioSet().isEmpty()){										
					for(CombineAudioOperateBO operateBO:businessOperateBO.getCombineAudioSet()){
						/**
						 * 混音的解码路数不能超过16
						 */
						if(operateBO.getSrc() != null && operateBO.getSrc().size() > 16){
							removeResourcesInTask(uuid2ChannelSelMap);
							errMsg = "混音的解码路数超过了16";
							log.error("--------------" + errMsg);
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("errMsg", errMsg);
							jsonObj.put("result", -1);
							return jsonObj.toJSONString();	
						}
						if(StringUtils.isEmpty(operateBO.getUuid())){
							operateBO.setUuid(UUID.randomUUID().toString());
						}
						audioSetMap.put(operateBO.getUuid(), operateBO);
					}
					//查询混音能力
					List<BundlePO> dstBundlePOs = bundleService.queryByUserIdAndDevcieModelAndKeyword(businessOperateBO.getUserId(), CommonConstant.COMBINE_AUDIO, null);
					Set<String> bundleIds = new HashSet<String>();
					if(dstBundlePOs != null){
						for(BundlePO bundlePO:dstBundlePOs){
							bundleIds.add(bundlePO.getBundleId());
						}
					}				
					log.info("--------------------MixAudio bundles are " + bundleIds);
					//选择混音能力
					audio_uuid2ChannelSelMap = selectCombineAudioCapability(bundleIds, audioSetMap);
					
					if(audio_uuid2ChannelSelMap == null || audio_uuid2ChannelSelMap.size() != audioSetMap.size()){
						if(audio_uuid2ChannelSelMap.size()>0){
							for(String uuid:audio_uuid2ChannelSelMap.keySet()){
								errMsg = "没有足够可用的混音能力通道，混音bundleId为：" + audio_uuid2ChannelSelMap.get(uuid).get(0).getBundleId();
								break;
							}
						}else{
							errMsg = "混音能力已占满";
						}
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					
					for(String uuid:audio_uuid2ChannelSelMap.keySet()){
						dstChannelsToLock.add(audio_uuid2ChannelSelMap.get(uuid).get(0));
					}
					
					/*//锁定混音能力通道
					audio_lockResultMap = resourseLayer.lockResource(audio_uuid2ChannelSelMap, businessOperateBO.getUserId());
					//去掉本次混音操作在bundleChannelMap存储的bundleChannel对
					removeResourcesInTask(audio_uuid2ChannelSelMap);
					*//**
					 * 新建混音,打开混音通道
					 *//*
					audio_openResultMap = accessLayer.openCombineVideoOrAudio(audio_uuid2ChannelSelMap, audio_lockResultMap, businessOperateBO.getUserId(), ChannelTypeInfo.AUDIOENCODE);*/
					combineAudioResult = generateResultJsonArray(audio_uuid2ChannelSelMap);
				}
				/**
				 * 修改混音
				 */
				if(businessOperateBO.getCombineAudioUpdate() != null && !businessOperateBO.getCombineAudioUpdate().isEmpty()){
					for(CombineAudioOperateBO operateBO:businessOperateBO.getCombineAudioUpdate()){
						if(StringUtils.isEmpty(operateBO.getUuid())){
							operateBO.setUuid(UUID.randomUUID().toString());
						}
						audioUpdateMap.put(operateBO.getUuid(), operateBO);
					}
					//组装生成混音能力
					audio_uuid2ChannelUpdateMap = generateCombineAudioCapability(audioUpdateMap);
					
					if(audio_uuid2ChannelUpdateMap == null || audio_uuid2ChannelUpdateMap.size() != audioUpdateMap.size()){
						if(audio_uuid2ChannelSelMap.size()>0){
							for(String uuid:audio_uuid2ChannelSelMap.keySet()){
								errMsg = "没有足够可用的混音能力通道，混音bundleId为：" + audio_uuid2ChannelSelMap.get(uuid).get(0).getBundleId();
								break;
							}
						}else{
							errMsg = "无可用混音";
						}
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					
					for(String uuid:audio_uuid2ChannelUpdateMap.keySet()){
						dstChannelsToLock.add(audio_uuid2ChannelUpdateMap.get(uuid).get(0));
					}
					/*//锁定混音能力通道
					audio_modifyLockResultMap = resourseLayer.lockResource(audio_uuid2ChannelUpdateMap, businessOperateBO.getUserId());
					*//**
					 * 修改混音,打开混音通道
					 *//*
					audio_modifyResultMap = accessLayer.openCombineVideoOrAudio(audio_uuid2ChannelUpdateMap, audio_modifyLockResultMap, businessOperateBO.getUserId(), ChannelTypeInfo.AUDIOENCODE);*/
				}
				
				/**
				 * 新建MediaMuxOut
				 */
				if(businessOperateBO.getOutConnMediaMuxSet() != null && !businessOperateBO.getOutConnMediaMuxSet().isEmpty()){
					for(MediaMuxOutBO outBO:businessOperateBO.getOutConnMediaMuxSet()){
						if(StringUtils.isEmpty(outBO.getTaskId())){
							outBO.setTaskId(UUID.randomUUID().toString());
						}
						JSONObject base_param = outBO.getChannel_param().getJSONObject("base_param");
						if(base_param != null){
							assembleMediaMuxVideoAudioSrcParam(base_param, 
									uuid2ChannelSelMap, uuid2ChannelUpdateMap, audio_uuid2ChannelSelMap, audio_uuid2ChannelUpdateMap);
							if("rtp-ps".equals(base_param.getString("format"))){								
								mediaMuxSetMap_emr.put(outBO.getTaskId(), outBO);
							}else{								
								mediaMuxSetMap_cdn.put(outBO.getTaskId(), outBO);
							}
						}else{
							log.warn("----------录制命令缺少base_param,命令为：" + outBO);
						}
					}
					//查询MediaMuxOut能力
					List<BundlePO> dstBundlePOs = bundleService.queryByUserIdAndDevcieModelAndKeyword(businessOperateBO.getUserId(), CommonConstant.MEDIA_MUX_OUT, null);
					Set<String> bundleIds = new HashSet<String>();
					if(dstBundlePOs != null){
						for(BundlePO bundlePO:dstBundlePOs){
							bundleIds.add(bundlePO.getBundleId());
						}
					}
					if(!mediaMuxSetMap_cdn.isEmpty()){						
						log.info("--------------------MediaMuxOut_cdn bundles are " + bundleIds);
						//选择MediaMuxOut能力
						mediaMux_taskIdChannelSelMap = selectMediaMuxCapability(bundleIds, mediaMuxSetMap_cdn, CommonConstant.MEDIA_MUX_OUT);
					}
										
					//查询emr能力
					dstBundlePOs = bundleService.queryByUserIdAndDevcieModelAndKeyword(businessOperateBO.getUserId(), CommonConstant.MEDIA_MUX_OUT_EMR, null);
					bundleIds = new HashSet<String>();
					if(dstBundlePOs != null){
						for(BundlePO bundlePO:dstBundlePOs){
							bundleIds.add(bundlePO.getBundleId());
						}
					}
					if(!mediaMuxSetMap_emr.isEmpty()){
						log.info("--------------------MediaMuxOut_emr bundles are " + bundleIds);
						mediaMux_taskIdChannelSelMap.putAll(selectMediaMuxCapability(bundleIds, mediaMuxSetMap_emr, CommonConstant.MEDIA_MUX_OUT_EMR));
					}
					
					
					if(mediaMux_taskIdChannelSelMap == null || mediaMux_taskIdChannelSelMap.size() != (mediaMuxSetMap_cdn.size()+mediaMuxSetMap_emr.size())){
						if(mediaMux_taskIdChannelSelMap.size()>0){
							for(String uuid:mediaMux_taskIdChannelSelMap.keySet()){
								errMsg = "没有足够可用的录制能力通道，录制能力bundleId为：" + mediaMux_taskIdChannelSelMap.get(uuid).get(0).getBundleId();
							}
						}else{
							errMsg = "录制能力已占满";
						}
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						removeResourcesInTask(mediaMux_taskIdChannelSelMap);
						removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
						
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					
					for(String uuid:mediaMux_taskIdChannelSelMap.keySet()){
						dstChannelsToLock.add(mediaMux_taskIdChannelSelMap.get(uuid).get(0));
					}
					/*//锁定MediaMuxOut能力通道
					Date mediaMuxLocking = new Date();
					mediaMux_lockResultMap = resourseLayer.lockResource(mediaMux_taskIdChannelSelMap, businessOperateBO.getUserId());
					log.info("------------------------mediaMuxLocking cost " + (new Date().getTime()-mediaMuxLocking.getTime())/1000 + " s");
					//去掉本次MediaMuxOut操作在bundleChannelMap存储的bundleChannel对
					removeResourcesInTask(mediaMux_taskIdChannelSelMap);
					
					*//**
					 * 打开MediaMux通道
					 *//*
					Date mediaMuxOpenning = new Date();
					mediaMux_openResultMap = accessLayer.openCombineVideoOrAudio(mediaMux_taskIdChannelSelMap, mediaMux_lockResultMap, businessOperateBO.getUserId(), ChannelTypeInfo.MEDIAMUXENCODE);
					log.info("------------------------mediaMuxOpenning cost " + (new Date().getTime()-mediaMuxOpenning.getTime())/1000 + " s");*/
					mediaMuxResult = generateResultJsonArray(mediaMux_taskIdChannelSelMap);
				}
				
				/**
				 * 修改MediaMux
				 */
				if(businessOperateBO.getOutConnMediaMuxUpdate() != null && !businessOperateBO.getOutConnMediaMuxUpdate().isEmpty()){
					for(MediaMuxOutBO operateBO:businessOperateBO.getOutConnMediaMuxUpdate()){
						if(StringUtils.isEmpty(operateBO.getTaskId())){
							operateBO.setTaskId(UUID.randomUUID().toString());
						}
						JSONObject base_param = operateBO.getChannel_param().getJSONObject("base_param");
						assembleMediaMuxVideoAudioSrcParam(base_param, 
								uuid2ChannelSelMap, uuid2ChannelUpdateMap, audio_uuid2ChannelSelMap, audio_uuid2ChannelUpdateMap);
						mediaMuxUpdateMap.put(operateBO.getTaskId(), operateBO);
					}
					//组装生成录制能力
					mediaMux_taskId2ChannelUpdateMap = generateMediaMuxCapability(mediaMuxUpdateMap);
					
					if(mediaMux_taskId2ChannelUpdateMap == null || mediaMux_taskId2ChannelUpdateMap.size() != mediaMuxUpdateMap.size()){
						if(mediaMux_taskIdChannelSelMap.size()>0){
							for(String uuid:mediaMux_taskIdChannelSelMap.keySet()){
								errMsg = "组装录制能力通道出错，录制能力bundleId为：" + mediaMux_taskIdChannelSelMap.get(uuid).get(0).getBundleId();
							}
						}else{
							errMsg = "无可用录制能力";
						}
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						removeResourcesInTask(mediaMux_taskIdChannelSelMap);
						removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					
					for(String uuid:mediaMux_taskId2ChannelUpdateMap.keySet()){
						dstChannelsToLock.add(mediaMux_taskId2ChannelUpdateMap.get(uuid).get(0));
					}
					/*//锁定录制能力通道
					Date modify_mediaMuxLocking = new Date();
					modifyMediaMux_lockResultMap = resourseLayer.lockResource(mediaMux_taskId2ChannelUpdateMap, businessOperateBO.getUserId());
					log.info("------------------------modify_mediaMuxLocking cost " + (new Date().getTime()-modify_mediaMuxLocking.getTime())/1000 + " s");
					*//**
					 * 修改录制,打开录制通道
					 *//*
					Date modify_mediaMuxOpenning = new Date();
					modifyMediaMux_openResultMap = accessLayer.openCombineVideoOrAudio(mediaMux_taskId2ChannelUpdateMap, modifyMediaMux_lockResultMap, businessOperateBO.getUserId(), ChannelTypeInfo.MEDIAMUXENCODE);
					log.info("------------------------modify_mediaMuxOpenning cost " + (new Date().getTime()-modify_mediaMuxOpenning.getTime())/1000 + " s");*/
				}
				
				/**
				 * 新建MediaPush
				 */
				if(businessOperateBO.getMediaPushSet() != null && !businessOperateBO.getMediaPushSet().isEmpty()){
					for(MediaPushOperateBO operateBO : businessOperateBO.getMediaPushSet()){
						if(StringUtils.isEmpty(operateBO.getTaskId())){
							operateBO.setTaskId(UUID.randomUUID().toString());
						}
						mediaPushSetMap.put(operateBO.getUuid(), operateBO);						
					}
					//查询mediaPush能力
					List<BundlePO> dstBundlePOs = bundleService.queryByUserIdAndDevcieModelAndKeyword(businessOperateBO.getUserId(), CommonConstant.MEDIA_PUSH, null);
					Set<String> bundleIds = new HashSet<String>();
					if(dstBundlePOs != null){
						for(BundlePO bundlePO:dstBundlePOs){
							bundleIds.add(bundlePO.getBundleId());
						}
					}
					log.info("--------------------MediaPush bundles are " + bundleIds);
					//选择mediaPush能力
					mediaPush_taskId2ChannelSelMap = selectMediaPushCapability(bundleIds, mediaPushSetMap);
					if(mediaPush_taskId2ChannelSelMap ==null || mediaPush_taskId2ChannelSelMap.size() != mediaPushSetMap.size()){
						//TODO:这个方法可能要多次使用
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						removeResourcesInTask(mediaMux_taskIdChannelSelMap);
						removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
						errMsg = "没有足够可用的调阅能力通道";
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();
					}
					
					for(String uuid : mediaPush_taskId2ChannelSelMap.keySet()){
						dstChannelsToLock.add(mediaPush_taskId2ChannelSelMap.get(uuid).get(0));
					}
					
					mediaPushResult = generateResultJsonArray(mediaPush_taskId2ChannelSelMap);
				}
				
				/**
				 * 锁定通道
				 */
				if(businessOperateBO.getLock() != null && !businessOperateBO.getLock().isEmpty()){
					List<ChannelSchemeBO> lockChannels = ChannelSchemeBO.transFromDst(businessOperateBO.getLock());
					dstChannelsToLock.addAll(lockChannels);
//					only_lockResultMap = resourseLayer.lockResource(lockChannels, businessOperateBO.getUserId(), false);
				}								
				
				/**
				 * 新建大屏视频
				 */
				if(businessOperateBO.getJv230ForwardSet() != null && !businessOperateBO.getJv230ForwardSet().isEmpty()){
					log.info("--------------treat videoMatrixvideo");
					List<ChannelSchemeBO> jv230ForwardsTolock = new ArrayList<ChannelSchemeBO>();
					for(MediaMuxOutBO outBO:businessOperateBO.getJv230ForwardSet()){
						ChannelSchemeBO channel = new ChannelSchemeBO();
						channel.fillMediaMuxEncodeChannel(outBO);
						jv230ForwardsTolock.add(channel);
					}
					
					//整理channel_param
					assembleJv230ChannelParam(jv230ForwardsTolock);
					//整理sources
					assembleJv230VideoSrcParam(jv230ForwardsTolock, uuid2ChannelSelMap, uuid2ChannelUpdateMap, mediaPush_taskId2ChannelSelMap, mediaPush_taskId2ChannelUpdateMap);
					if(jv230ForwardsTolock == null || jv230ForwardsTolock.size() != businessOperateBO.getJv230ForwardSet().size()){
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						removeResourcesInTask(mediaMux_taskIdChannelSelMap);
						removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
						errMsg = "组装大屏视频能力通道出错";
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					dstChannelsToLock.addAll(jv230ForwardsTolock);
					
					/*jv230_lockResultMap = resourseLayer.lockResource(jv230ForwardsTolock, businessOperateBO.getUserId(), false);
					//获取待打开通道
					List<ChannelSchemeBO> jv230Forward_channelsToOpen = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(jv230ForwardsTolock != null && jv230_lockResultMap != null){
						for(ChannelSchemeBO channel:jv230ForwardsTolock){
							Boolean lockSuccess = ((ChannelStatusBody)jv230_lockResultMap.get(channel.getTaskId())).isResult();
							if(lockSuccess != null && lockSuccess){
								jv230Forward_channelsToOpen.add(channel);
								operateIndexList.add(((ChannelStatusBody)jv230_lockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					*//**
					 * 打开channel
					 *//*
					jv230_openResultMap = accessLayer.operateChannel(jv230Forward_channelsToOpen, operateIndexList, businessOperateBO.getUserId(), OperateType.OPEN);*/
				}
				
				/**
				 * 新建大屏音频
				 */
				if(businessOperateBO.getJv230AudioSet() != null && !businessOperateBO.getJv230AudioSet().isEmpty()){
					log.info("--------------treat videoMatrixaudio");
					List<ChannelSchemeBO> jv230AudiosTolock = new ArrayList<ChannelSchemeBO>();
					for(MediaMuxOutBO outBO:businessOperateBO.getJv230AudioSet()){
						ChannelSchemeBO channel = new ChannelSchemeBO();
						channel.fillMediaMuxEncodeChannel(outBO);
						jv230AudiosTolock.add(channel);
					}
					
					//整理channel_param
					assembleJv230ChannelParam(jv230AudiosTolock);
					//整理source
					assembleJv230AudioSrcParam(jv230AudiosTolock, audio_uuid2ChannelSelMap, audio_uuid2ChannelUpdateMap, mediaPush_taskId2ChannelSelMap, mediaPush_taskId2ChannelUpdateMap);
					if(jv230AudiosTolock == null || jv230AudiosTolock.size() != businessOperateBO.getJv230AudioSet().size()){
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						removeResourcesInTask(mediaMux_taskIdChannelSelMap);
						removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
						errMsg = "组装大屏音频能力通道出错";
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					dstChannelsToLock.addAll(jv230AudiosTolock);
					/*jv230_lockResultMap = resourseLayer.lockResource(jv230AudiosTolock, businessOperateBO.getUserId(), false);
					//获取待打开通道
					List<ChannelSchemeBO> jv230Audio_channelsToOpen = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(jv230AudiosTolock != null && jv230_lockResultMap != null){
						for(ChannelSchemeBO channel:jv230AudiosTolock){
							Boolean lockSuccess = ((ChannelStatusBody)jv230_lockResultMap.get(channel.getTaskId())).isResult();
							if(lockSuccess != null && lockSuccess){
								jv230Audio_channelsToOpen.add(channel);
								operateIndexList.add(((ChannelStatusBody)jv230_lockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					*//**
					 * 打开channel
					 *//*
					jv230_openResultMap = accessLayer.operateChannel(jv230Audio_channelsToOpen, operateIndexList, businessOperateBO.getUserId(), OperateType.OPEN);*/
				}
					
				/**
				 * 呼叫
				 */
				if(businessOperateBO.getConnect() != null && !businessOperateBO.getConnect().isEmpty()){
					//选择的能够执行的connect
					List<ConnectBO> selectConnectBOs = new ArrayList<ConnectBO>();
					/**
					 * 呼叫中的src使用uuid的换成bundleId和channelId
					 */
					Iterator<ConnectBO> iterator = businessOperateBO.getConnect().iterator();
					int orgSize = businessOperateBO.getConnect().size();
					while(iterator.hasNext()){
						ConnectBO connectBO = iterator.next();
						//编码通道无源信息
						if(connectBO.getSource_param() == null){
							selectConnectBOs.add(connectBO);
						}else{//解码通道源信息分为三种channel/combineVideo/combineAudio，后两种需要把uuid转换成layer、bundle、channel
							String uuid = connectBO.getSource_param().getUuid();
							if("combineVideo".equals(connectBO.getSource_param().getType())){
								ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,uuid2ChannelSelMap,uuid2ChannelUpdateMap);
								if(channelSchemeBO != null){
									connectBO.getSource_param().setUuid(null);
									connectBO.getSource_param().setBundleId(channelSchemeBO.getBundleId());
									connectBO.getSource_param().setChannelId(channelSchemeBO.getChannelId().toString());
									connectBO.getSource_param().setLayerId(channelSchemeBO.getLayerId());
									selectConnectBOs.add(connectBO);									
								}								
							}else if("combineAudio".equals(connectBO.getSource_param().getType())){
								ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,audio_uuid2ChannelSelMap, audio_uuid2ChannelUpdateMap);
								if(channelSchemeBO != null){
									connectBO.getSource_param().setUuid(null);
									connectBO.getSource_param().setBundleId(channelSchemeBO.getBundleId());
									connectBO.getSource_param().setChannelId(channelSchemeBO.getChannelId().toString());
									connectBO.getSource_param().setLayerId(channelSchemeBO.getLayerId());
									selectConnectBOs.add(connectBO);									
								}
								
							}else if("mediaPush".equals(connectBO.getSource_param().getType())){
								ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid, mediaPush_taskId2ChannelSelMap, mediaPush_taskId2ChannelUpdateMap);
								if(channelSchemeBO != null){
									String suffix = "_video";
									String base_type = connectBO.getBase_type();
									if(base_type.toLowerCase().indexOf("audio") != -1){
										suffix = "_audio";
									}
									connectBO.getSource_param().setUuid(null);
									connectBO.getSource_param().setBundleId(channelSchemeBO.getBundleId());
									connectBO.getSource_param().setChannelId(channelSchemeBO.getChannelId().toString() + suffix);
									connectBO.getSource_param().setLayerId(channelSchemeBO.getLayerId());
									selectConnectBOs.add(connectBO);									
								}
								
							}else{
								selectConnectBOs.add(connectBO);
							}
							
						}
						
					}
					
					List<ChannelSchemeBO> connect_channelsToLock = ChannelSchemeBO.transFromConnectBOs(selectConnectBOs);
					
					//整理channel_param
					assembleChannelParam(connect_channelsToLock);
					
					if(connect_channelsToLock == null || connect_channelsToLock.size() != orgSize){
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						removeResourcesInTask(mediaMux_taskIdChannelSelMap);
						removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
						errMsg = "组装呼叫能力通道出错";
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					
					dstChannelsToLock.addAll(connect_channelsToLock);
					/**
					 * 锁定channel
					 *//*
					Date connectLocking = new Date();
					connect_lockResultMap = resourseLayer.lockResource(connect_channelsToLock, businessOperateBO.getUserId(), false);
					log.info("------------------------connectLocking cost " + (new Date().getTime()-connectLocking.getTime())/1000 + " s");
					//获取待打开通道
					List<ChannelSchemeBO> connect_channelsToOpen = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(connect_channelsToLock != null && connect_lockResultMap != null){
						for(ChannelSchemeBO channel:connect_channelsToLock){
							Boolean lockSuccess = ((ChannelStatusBody)connect_lockResultMap.get(channel.getTaskId())).isResult();
							if(lockSuccess != null && lockSuccess){
								connect_channelsToOpen.add(channel);
								operateIndexList.add(((ChannelStatusBody)connect_lockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					*//**
					 * 打开channel
					 *//*
					Date connectOpening = new Date();
					connect_openResultMap = accessLayer.operateChannel(connect_channelsToOpen, operateIndexList, businessOperateBO.getUserId(), OperateType.OPEN);
					log.info("------------------------connectOpening cost " + (new Date().getTime()-connectOpening.getTime())/1000 + " s");*/
					
				}								
				
				/**
				 * 转发/转发删除
				 */
				if(businessOperateBO.getForwardSet() != null && !businessOperateBO.getForwardSet().isEmpty()){
					//选择的能够执行的forwardSet
					List<ForwardSetBO> selectForwardSetBOs = new ArrayList<ForwardSetBO>();
					/**
					 * 转发中的src使用uuid的换成bundleId和channelId
					 */
					Iterator<ForwardSetBO> iterator = businessOperateBO.getForwardSet().iterator();
					while(iterator.hasNext()){
						ForwardSetBO forwardSetBO = iterator.next();
						//编码通道无源信息
						if(forwardSetBO.getSrc() == null){
							selectForwardSetBOs.add(forwardSetBO);
						}else{//解码通道源信息分为三种channel/combineVideo/combineAudio，后两种需要把uuid转换成layer、bundle、channel
							String uuid = forwardSetBO.getSrc().getUuid();
							if("combineVideo".equals(forwardSetBO.getSrc().getType())){
								ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,uuid2ChannelSelMap,uuid2ChannelUpdateMap);
								if(channelSchemeBO != null){
									forwardSetBO.getSrc().setUuid(null);
									forwardSetBO.getSrc().setBundleId(channelSchemeBO.getBundleId());
									forwardSetBO.getSrc().setChannelId(channelSchemeBO.getChannelId().toString());
									forwardSetBO.getSrc().setLayerId(channelSchemeBO.getLayerId());
									selectForwardSetBOs.add(forwardSetBO);							
								}								
							}else if("combineAudio".equals(forwardSetBO.getSrc().getType())){
								ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,audio_uuid2ChannelSelMap, audio_uuid2ChannelUpdateMap);
								if(channelSchemeBO != null){
									forwardSetBO.getSrc().setUuid(null);
									forwardSetBO.getSrc().setBundleId(channelSchemeBO.getBundleId());
									forwardSetBO.getSrc().setChannelId(channelSchemeBO.getChannelId().toString());
									forwardSetBO.getSrc().setLayerId(channelSchemeBO.getLayerId());
									selectForwardSetBOs.add(forwardSetBO);
								}								
							}else if("mediaPush".equals(forwardSetBO.getSrc().getType())){
								ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid, mediaPush_taskId2ChannelSelMap, mediaPush_taskId2ChannelUpdateMap);
								if(channelSchemeBO != null){
									String suffix = "_video";
									String base_type = forwardSetBO.getDst().getBase_type();
									if(base_type.toLowerCase().indexOf("audio") != -1){
										suffix = "_audio";
									}
									forwardSetBO.getSrc().setUuid(null);
									forwardSetBO.getSrc().setBundleId(channelSchemeBO.getBundleId());
									forwardSetBO.getSrc().setChannelId(channelSchemeBO.getChannelId().toString() + suffix);
									forwardSetBO.getSrc().setLayerId(channelSchemeBO.getLayerId());
									selectForwardSetBOs.add(forwardSetBO);									
								}
								
							}else{
								selectForwardSetBOs.add(forwardSetBO);
							}
						}
						
					}
										
					List<ChannelSchemeBO> forwardSet_channelsToLock = ChannelSchemeBO.transFromForwardSetBOs(selectForwardSetBOs);
					
					/**
					 * 加上转发删除里的指令
					 */
					if(forwardSet_channelsToLock == null){
						forwardSet_channelsToLock = new ArrayList<ChannelSchemeBO>();
					}
					
					int forwardDelSize = 0;
					if(businessOperateBO.getForwardDel() != null && !businessOperateBO.getForwardDel().isEmpty()){
						forwardDelSize = businessOperateBO.getForwardDel().size();
						forwardSet_channelsToLock.addAll(ChannelSchemeBO.transFromForwardDelBOs(businessOperateBO.getForwardDel()));
					}
										
					//整理channel_param
					assembleChannelParam(forwardSet_channelsToLock);
					
					if(forwardSet_channelsToLock == null || forwardSet_channelsToLock.size() != (businessOperateBO.getForwardSet().size() + forwardDelSize)){
						removeResourcesInTask(uuid2ChannelSelMap);
						removeResourcesInTask(audio_uuid2ChannelSelMap);
						removeResourcesInTask(mediaMux_taskIdChannelSelMap);
						removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
						errMsg = "组装转发能力通道出错";
						log.error("--------------" + errMsg);
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("errMsg", errMsg);
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();						
					}
					
					dstChannelsToLock.addAll(forwardSet_channelsToLock);
					/**
					 * 锁定channel
					 *//*
					Date forwardLocking = new Date();
					forwardSet_lockResultMap = resourseLayer.lockResource(forwardSet_channelsToLock, businessOperateBO.getUserId(), false);
					log.info("------------------------forwardLocking cost " + (new Date().getTime()-forwardLocking.getTime())/1000 + " s");
					//获取待打开通道
					List<ChannelSchemeBO> forwardSet_channelsToOpen = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(forwardSet_channelsToLock != null && forwardSet_lockResultMap != null){
						for(ChannelSchemeBO channel:forwardSet_channelsToLock){
							Boolean lockSuccess = ((ChannelStatusBody)forwardSet_lockResultMap.get(channel.getTaskId())).isResult();
							if(lockSuccess != null && lockSuccess){
								forwardSet_channelsToOpen.add(channel);
								operateIndexList.add(((ChannelStatusBody)forwardSet_lockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					*//**
					 * 修改通道
					 *//*
					Date forwardOpening = new Date();
					forwardSet_modifyResultMap = accessLayer.operateChannel(forwardSet_channelsToOpen, operateIndexList, businessOperateBO.getUserId(), OperateType.OPEN);
					log.info("------------------------forwardOpening cost " + (new Date().getTime()-forwardOpening.getTime())/1000 + " s");*/
					
				}
				
				//呼叫bundle
				if(businessOperateBO.getConnectBundle() != null && !businessOperateBO.getConnectBundle().isEmpty()){
					log.info("-----------start treat connectBundle command");
					for(ConnectBundleBO bundleBO:businessOperateBO.getConnectBundle()){						
						List<Bundle_ChannelBO> selectChannelBOs = new ArrayList<Bundle_ChannelBO>();
						/**
						 * bundle呼叫中的src使用uuid的换成bundleId和channelId
						 */
						List<ChannelSchemeBO> connectBundle_channelsToLock = null;
						if(bundleBO.getChannels() != null && !bundleBO.getChannels().isEmpty()){
							Iterator<Bundle_ChannelBO> iterator = bundleBO.getChannels().iterator();
							int orgSize = bundleBO.getChannels().size();
							while(iterator.hasNext()){
								Bundle_ChannelBO channelBO = iterator.next();
								//编码通道无源信息
								if(channelBO.getSource_param() == null){
									selectChannelBOs.add(channelBO);
								}else{//解码通道源信息分为三种channel/combineVideo/combineAudio，后两种需要把uuid转换成layer、bundle、channel
									String uuid = channelBO.getSource_param().getUuid();
									if("combineVideo".equals(channelBO.getSource_param().getType())){
										ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,uuid2ChannelSelMap,uuid2ChannelUpdateMap);
										if(channelSchemeBO != null){
											channelBO.getSource_param().setUuid(null);
											channelBO.getSource_param().setBundleId(channelSchemeBO.getBundleId());
											channelBO.getSource_param().setChannelId(channelSchemeBO.getChannelId().toString());
											channelBO.getSource_param().setLayerId(channelSchemeBO.getLayerId());
											selectChannelBOs.add(channelBO);									
										}								
									}else if("combineAudio".equals(channelBO.getSource_param().getType())){
										ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,audio_uuid2ChannelSelMap, audio_uuid2ChannelUpdateMap);
										if(channelSchemeBO != null){
											channelBO.getSource_param().setUuid(null);
											channelBO.getSource_param().setBundleId(channelSchemeBO.getBundleId());
											channelBO.getSource_param().setChannelId(channelSchemeBO.getChannelId().toString());
											channelBO.getSource_param().setLayerId(channelSchemeBO.getLayerId());
											selectChannelBOs.add(channelBO);									
										}
										
									}else if("mediaPush".equals(channelBO.getSource_param().getType())){
										ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid, mediaPush_taskId2ChannelSelMap, mediaPush_taskId2ChannelUpdateMap);
										if(channelSchemeBO != null){
											String suffix = "_video";
											String base_type = channelBO.getBase_type();
											if(base_type.toLowerCase().indexOf("audio") != -1){
												suffix = "_audio";
											}
											channelBO.getSource_param().setUuid(null);
											channelBO.getSource_param().setBundleId(channelSchemeBO.getBundleId());
											channelBO.getSource_param().setChannelId(channelSchemeBO.getChannelId().toString() + suffix);
											channelBO.getSource_param().setLayerId(channelSchemeBO.getLayerId());
											selectChannelBOs.add(channelBO);									
										}
										
									}else{
										selectChannelBOs.add(channelBO);
									}
									
								}
								
							}
							
							connectBundle_channelsToLock = ChannelSchemeBO.transFromBundleChannelBOs(selectChannelBOs, bundleBO.getBundleId());
							
							//整理channel_param
							assembleChannelParam(connectBundle_channelsToLock);
							
							if(connectBundle_channelsToLock == null || connectBundle_channelsToLock.size() != orgSize){
								removeResourcesInTask(uuid2ChannelSelMap);
								removeResourcesInTask(audio_uuid2ChannelSelMap);
								removeResourcesInTask(mediaMux_taskIdChannelSelMap);
								removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
								errMsg = "组装bundle呼叫能力通道出错";
								log.error("--------------" + errMsg);
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("errMsg", errMsg);
								jsonObj.put("result", -1);
								return jsonObj.toJSONString();						
							}
						}
						
						BundleSchemeBO bundleSchemeBO = BundleSchemeBO.transFromConnectBundleBO(bundleBO);
						bundleSchemeBO.setChannels(connectBundle_channelsToLock);
						dstBundlesToLock.add(bundleSchemeBO);						
					}
				}
				
				//锁定bundle不打开（目前没用）
//				if(businessOperateBO.getLockBundle() != null && !businessOperateBO.getLockBundle().isEmpty()){
//					log.info("-----------start treat lockBundle command");
//					List<BundleSchemeBO> lockBundles = BundleSchemeBO.transFromDst(businessOperateBO.getLockBundle());
//					dstBundlesToLock.addAll(lockBundles);
//				}
				//锁定通道
				if(!dstChannelsToLock.isEmpty() || !dstBundlesToLock.isEmpty()){

					if(!dstChannelsToLock.isEmpty()){						
						log.info("--------------start lock dstChannels------------------");
					}
					ResultMap lockMap = resourseLayer.lockResource(dstChannelsToLock, businessOperateBO.getUserId(), true);
					/**
					 * 释放缓存中的通道
					 */
					removeResourcesInTask(uuid2ChannelSelMap);
					removeResourcesInTask(audio_uuid2ChannelSelMap);
					removeResourcesInTask(mediaMux_taskIdChannelSelMap);
					removeResourcesInTask(mediaPush_taskId2ChannelSelMap);
					Boolean lockResult = null;
					if(lockMap != null){						
						lockResult = (Boolean)lockMap.get("lockResult");
					}
					if(lockResult != null && lockResult || dstChannelsToLock.isEmpty()){//全部锁定成功
						if(!dstChannelsToLock.isEmpty()){						
							log.info("--------------lock dstChannels success------------------");
						}
						if(!dstBundlesToLock.isEmpty()){
							log.info("--------------start lock dstBundles------------------");
							//业务层新添加的参数，指定mustLockAll，批量锁定
							boolean mustLockAllBundle = businessOperateBO.isMustLockAllBundle();
//							ResultMap lockBundleMap = resourseLayer.lockBundleResource(dstBundlesToLock, businessOperateBO.getUserId(), true);
							ResultMap lockBundleMap = resourseLayer.batchLockBundleResource(dstBundlesToLock, businessOperateBO.getUserId(), mustLockAllBundle);
							Boolean lockBundleResult = (Boolean)lockBundleMap.get("lockResult");
							
							if(lockBundleResult != null && lockBundleResult){//mustLockAll==true表示所有bundle锁定成功；mustLockAll==false表示调用成功
								log.info("--------------lock dstBundles success------------------");
								//打开通道(open)
								log.info("--------------start open dstBundles------------------");
								
								List<Integer> operateIndexList = new ArrayList<Integer>();								
								List<BundleSchemeBO> bundlesToOperate = new ArrayList<BundleSchemeBO>();
								
								for(BundleSchemeBO bundle:dstBundlesToLock){
									ChannelStatusBody statusBody = ((ChannelStatusBody)lockBundleMap.get(bundle.getTaskId()));									
									if(statusBody != null){
										//通过statusBody的result判断，只取锁定成功的，并且反馈到业务
										boolean lockResult1 = statusBody.isResult();
										if(lockResult1){
											bundlesToOperate.add(bundle);
											operateIndexList.add(statusBody.getOperateIndex());
											
											//锁定成功的，返回给业务层
											JSONObject connectBundle = new JSONObject();
											connectBundle.put("bundleId", bundle.getBundleId());
											connectBundleResult.add(connectBundle);
										}
									}
								}
								accessLayer.operateBundle(bundlesToOperate, operateIndexList, businessOperateBO.getUserId(), OperateType.OPEN);
							}else{//bundle锁定失败需回滚
								if(!dstChannelsToLock.isEmpty()){
									log.info("--------------rollback all dstChannels------------------");
								}
								resourseLayer.rollbackToUnlockChannels(dstChannelsToLock, businessOperateBO.getUserId());
								JSONObject jsonObj = new JSONObject();
								JSONArray failBundleArray = generateBundleLockFailResultJsonArray(lockBundleMap, dstBundlesToLock);
								if(failBundleArray != null){
									jsonObj.put("lockFailBundle", failBundleArray);
								}
								jsonObj.put("errMsg", "部分能力设备锁定失败");
								jsonObj.put("result", -1);
								return jsonObj.toJSONString();
							}
						}
						//打开通道
						if(!dstChannelsToLock.isEmpty()){
							log.info("--------------start open dstChannels------------------");
						}
						List<Integer> operateIndexList = new ArrayList<Integer>();
						for(ChannelSchemeBO channel:dstChannelsToLock){
							ChannelStatusBody statusBody = ((ChannelStatusBody)lockMap.get(channel.getTaskId()));
							if(statusBody != null){								
								operateIndexList.add(statusBody.getOperateIndex());
							}
						}
						
						accessLayer.operateChannel(dstChannelsToLock, operateIndexList, businessOperateBO.getUserId(), OperateType.OPEN);
					}else{//锁定失败并回滚
						JSONObject jsonObj = new JSONObject();
						JSONArray failChannelArray = generateLockFailResultJsonArray(lockMap, dstChannelsToLock);
						if(failChannelArray != null){
							jsonObj.put("lockFailChannel", failChannelArray);
						}
						jsonObj.put("errMsg", "设备正忙");
						jsonObj.put("result", -1);
						return jsonObj.toJSONString();
					}					
				}
				
				/**
				 * 挂断
				 */
				if(businessOperateBO.getDisconnect() != null && !businessOperateBO.getDisconnect().isEmpty()){
					List<ChannelSchemeBO> disconnect_channelsToUnlock = ChannelSchemeBO.transFromDisConnectBOs(businessOperateBO.getDisconnect());
					/**
					 * 释放channel
					 */
					Date disconnectUnlocking = new Date();
					disconnect_unlockResultMap = resourseLayer.unlockResource(disconnect_channelsToUnlock, businessOperateBO.getUserId(), false);
					log.info("------------------------disconnectUnlocking cost " + (new Date().getTime()-disconnectUnlocking.getTime())/1000 + " s");
					//获取待关闭通道
					List<ChannelSchemeBO> disconnect_channelsToClose = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(disconnect_channelsToUnlock != null && disconnect_unlockResultMap != null){
						for(ChannelSchemeBO channel:disconnect_channelsToUnlock){
							Boolean unlockSuccess = ((ChannelStatusBody)disconnect_unlockResultMap.get(channel.getTaskId())).isResult();
							if(unlockSuccess != null && unlockSuccess){
								disconnect_channelsToClose.add(channel);
								operateIndexList.add(((ChannelStatusBody)disconnect_unlockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					/**
					 * 关闭channel
					 */
					Date disconnectClosing = new Date();
					disconnect_closeResultMap = accessLayer.operateChannel(disconnect_channelsToClose, operateIndexList, businessOperateBO.getUserId(), OperateType.CLOSE);
					log.info("------------------------disconnectClosing cost " + (new Date().getTime()-disconnectClosing.getTime())/1000 + " s");
				}
				
				/**
				 * bundle挂断
				 */
				if(businessOperateBO.getDisconnectBundle() != null && !businessOperateBO.getDisconnectBundle().isEmpty()){
					log.info("--------------------start disconnectBundles-----------------------");
					List<BundleSchemeBO> disconnect_bundlesToUnlock = BundleSchemeBO.transFromDisConnectBOs(businessOperateBO.getDisconnectBundle());
					/**
					 * 释放bundle
					 */
					//有可能在停会时，一部分设备已经从资源层删除，所以最后一个参数使用false，不要求全部解锁成功
					ResultMap disconnect_unlockBundleResultMap = resourseLayer.batchUnlockBundleResource(disconnect_bundlesToUnlock, businessOperateBO.getUserId(), false);
					//获取待关闭通道
					List<BundleSchemeBO> disconnect_bundlesToClose = new ArrayList<BundleSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(disconnect_bundlesToUnlock != null && disconnect_bundlesToUnlock != null){
						for(BundleSchemeBO bundle:disconnect_bundlesToUnlock){
							//解锁结果
							ChannelStatusBody unlockBody = (ChannelStatusBody)disconnect_unlockBundleResultMap.get(bundle.getTaskId());
							if(unlockBody != null){
								Boolean unlockSuccess = unlockBody.isResult();
								if(unlockSuccess != null && unlockSuccess){
									int operateCount = unlockBody.getOperateCount();
//									if("meeting".equals(businessOperateBO.getSystemMode()) || operateCount <= 0){
									if(operateCount <= 0){
										disconnect_bundlesToClose.add(bundle);
										operateIndexList.add(unlockBody.getOperateIndex());
									}
								}
							}
						}
					}
					/**
					 * 关闭bundle
					 */				
					accessLayer.operateBundle(disconnect_bundlesToClose, operateIndexList, businessOperateBO.getUserId(), OperateType.CLOSE);
				}
				
				/**
				 * 释放通道
				 */
				if(businessOperateBO.getUnlock() != null && !businessOperateBO.getUnlock().isEmpty()){
					List<ChannelSchemeBO> unlockChannels = ChannelSchemeBO.transFromDst(businessOperateBO.getUnlock());					
					only_unlockResultMap = resourseLayer.unlockResource(unlockChannels, businessOperateBO.getUserId(), false);
				}
				
				/**
				 * 释放bundle不关闭不发close（目前没有用）
				if(businessOperateBO.getUnlockBundle() != null && !businessOperateBO.getUnlockBundle().isEmpty()){
					log.info("--------------start unlockBundles-------------------");
					List<BundleSchemeBO> unlockbundles = BundleSchemeBO.transFromDst(businessOperateBO.getUnlockBundle());					
					resourseLayer.batchUnlockBundleResource(unlockbundles, businessOperateBO.getUserId(), true);
				}
				 */
				
				/**
				 * 合屏删除
				 */
				if(businessOperateBO.getCombineVideoDel() != null){
					List<ChannelSchemeBO> videoDel_channelstoUnlock = ChannelSchemeBO.transFromCombineVideoDelBOs(businessOperateBO.getCombineVideoDel());
					
					/**
					 * 释放channel
					 */
					del_unlockResultMap = resourseLayer.unlockResource(videoDel_channelstoUnlock, businessOperateBO.getUserId(), false);
					//获取待关闭通道
					List<ChannelSchemeBO> del_channelsToClose = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(videoDel_channelstoUnlock != null && del_unlockResultMap != null){
						for(ChannelSchemeBO channel:videoDel_channelstoUnlock){
							Boolean unlockSuccess = ((ChannelStatusBody)del_unlockResultMap.get(channel.getTaskId())).isResult();
							if(unlockSuccess != null && unlockSuccess){
								del_channelsToClose.add(channel);
								operateIndexList.add(((ChannelStatusBody)del_unlockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					/**
					 * 关闭channel
					 */
					del_closeResultMap = accessLayer.closeCombineVideoOrAudio(del_channelsToClose, operateIndexList);
				}
				
				/**
				 * 混音删除
				 */
				if(businessOperateBO.getCombineAudioDel() != null){
					List<ChannelSchemeBO> audioDel_channelstoUnlock = ChannelSchemeBO.transFromCombineAudioDelBOs(businessOperateBO.getCombineAudioDel());
					
					/**
					 * 释放channel
					 */
					audio_del_unlockResultMap = resourseLayer.unlockResource(audioDel_channelstoUnlock, businessOperateBO.getUserId(), false);
					//获取待关闭通道
					List<ChannelSchemeBO> del_channelsToClose = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(audioDel_channelstoUnlock != null && audio_del_unlockResultMap != null){
						for(ChannelSchemeBO channel:audioDel_channelstoUnlock){
							Boolean unlockSuccess = ((ChannelStatusBody)audio_del_unlockResultMap.get(channel.getTaskId())).isResult();
							if(unlockSuccess != null && unlockSuccess){
								del_channelsToClose.add(channel);
								operateIndexList.add(((ChannelStatusBody)audio_del_unlockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					/**
					 * 关闭channel
					 */
					audio_del_closeResultMap = accessLayer.closeCombineVideoOrAudio(del_channelsToClose, operateIndexList);
				}
				
				/**
				 * MediaMux删除
				 */
				if(businessOperateBO.getOutConnMediaMuxDel() != null){
					List<ChannelSchemeBO> mediaMuxDel_channelstoUnlock = ChannelSchemeBO.transFromDst(businessOperateBO.getOutConnMediaMuxDel());
					
					/**
					 * 释放channel
					 */
					mediaMux_del_unlockResultMap = resourseLayer.unlockResource(mediaMuxDel_channelstoUnlock, businessOperateBO.getUserId(), false);
					//获取待关闭通道
					List<ChannelSchemeBO> del_channelsToClose = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(mediaMuxDel_channelstoUnlock != null && mediaMux_del_unlockResultMap != null){
						for(ChannelSchemeBO channel:mediaMuxDel_channelstoUnlock){
							Boolean unlockSuccess = ((ChannelStatusBody)mediaMux_del_unlockResultMap.get(channel.getTaskId())).isResult();
							if(unlockSuccess != null && unlockSuccess){
								del_channelsToClose.add(channel);
								operateIndexList.add(((ChannelStatusBody)mediaMux_del_unlockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					/**
					 * 关闭channel
					 */
					mediaMux_del_closeResultMap = accessLayer.closeCombineVideoOrAudio(del_channelsToClose, operateIndexList);
				}
				
				/**
				 * MediaPush删除
				 */
				if(businessOperateBO.getMediaPushDel() != null){
					List<ChannelSchemeBO> mediaPushDel_channelstoUnlock = ChannelSchemeBO.transFromDst(businessOperateBO.getMediaPushDel());
					
					/**
					 * 释放channel
					 */
					mediaPush_del_unlockResultMap = resourseLayer.unlockResource(mediaPushDel_channelstoUnlock, businessOperateBO.getUserId(), false);
					//获取待关闭通道
					List<ChannelSchemeBO> del_channelsToClose = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(mediaPushDel_channelstoUnlock != null && mediaPush_del_unlockResultMap != null){
						for(ChannelSchemeBO channel:mediaPushDel_channelstoUnlock){
							Boolean unlockSuccess = ((ChannelStatusBody)mediaPush_del_unlockResultMap.get(channel.getTaskId())).isResult();
							if(unlockSuccess != null && unlockSuccess){
								del_channelsToClose.add(channel);
								operateIndexList.add(((ChannelStatusBody)mediaPush_del_unlockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					/**
					 * 关闭channel
					 */
					mediaPush_del_closeResultMap = accessLayer.closeCombineVideoOrAudio(del_channelsToClose, operateIndexList);
				}
				
				/**
				 *大屏视频删除
				 */
				if(businessOperateBO.getJv230ForwardDel() != null){
					List<ChannelSchemeBO> jv230ForwardDel_channelstoUnlock = ChannelSchemeBO.transFromDst(businessOperateBO.getJv230ForwardDel());
					
					/**
					 * 释放channel
					 */
					jv230Forward_del_unlockResultMap = resourseLayer.unlockResource(jv230ForwardDel_channelstoUnlock, businessOperateBO.getUserId(), false);
					//获取待关闭通道
					List<ChannelSchemeBO> del_channelsToClose = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(jv230ForwardDel_channelstoUnlock != null && jv230Forward_del_unlockResultMap != null){
						for(ChannelSchemeBO channel:jv230ForwardDel_channelstoUnlock){
							Boolean unlockSuccess = ((ChannelStatusBody)jv230Forward_del_unlockResultMap.get(channel.getTaskId())).isResult();
							if(unlockSuccess != null && unlockSuccess){
								del_channelsToClose.add(channel);
								operateIndexList.add(((ChannelStatusBody)jv230Forward_del_unlockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					/**
					 * 关闭channel
					 */
					jv230Forward_del_closeResultMap = accessLayer.closeCombineVideoOrAudio(del_channelsToClose, operateIndexList);
				}
				
				/**
				 *大屏音频删除
				 */
				if(businessOperateBO.getJv230AudioDel() != null){
					List<ChannelSchemeBO> jv230Audio_channelstoUnlock = ChannelSchemeBO.transFromDst(businessOperateBO.getJv230AudioDel());
					
					/**
					 * 释放channel
					 */
					jv230Audio_del_unlockResultMap = resourseLayer.unlockResource(jv230Audio_channelstoUnlock, businessOperateBO.getUserId(), false);
					//获取待关闭通道
					List<ChannelSchemeBO> del_channelsToClose = new ArrayList<ChannelSchemeBO>();
					List<Integer> operateIndexList = new ArrayList<Integer>();
					if(jv230Audio_channelstoUnlock != null && jv230Audio_del_unlockResultMap != null){
						for(ChannelSchemeBO channel:jv230Audio_channelstoUnlock){
							Boolean unlockSuccess = ((ChannelStatusBody)jv230Audio_del_unlockResultMap.get(channel.getTaskId())).isResult();
							if(unlockSuccess != null && unlockSuccess){
								del_channelsToClose.add(channel);
								operateIndexList.add(((ChannelStatusBody)jv230Audio_del_unlockResultMap.get(channel.getTaskId())).getOperateIndex());
							}
						}
					}
					/**
					 * 关闭channel
					 */
					jv230Audio_del_closeResultMap = accessLayer.closeCombineVideoOrAudio(del_channelsToClose, operateIndexList);
				}
				
				JSONObject jsonObj = new JSONObject();
				if(combineAudioResult != null){
					jsonObj.put("combineAudioSet", combineAudioResult);
				}else{
					jsonObj.put("combineAudioSet", new JSONArray());
				}
				
				if(combineVideoResult != null){
					jsonObj.put("combineVideoLayout", combineVideoResult);					
				}else{
					jsonObj.put("combineVideoLayout", new JSONArray());
				}
				
				if(mediaMuxResult != null){					
					jsonObj.put("OutConnMediaMuxSet", mediaMuxResult);
				}else{
					jsonObj.put("OutConnMediaMuxSet", new JSONArray());
				}
				
				if(mediaPushResult != null){					
					jsonObj.put("mediaPushSet", mediaPushResult);
				}else{
					jsonObj.put("mediaPushSet", new JSONArray());
				}
				
				if(connectBundleResult != null){
					jsonObj.put("connectBundle", connectBundleResult);
				}
				
				jsonObj.put("result", 0);
				log.info("------------------------LogicTreatCompo cost " + (new Date().getTime()-start.getTime())/1000 + " s");
				return jsonObj.toJSONString();
				
			}
		}catch(Exception e){
			log.error("operateChannels failed", e);
		}
		return null;
	}
	
	/**
	 * 从目标设备的能力集合中选择合屏能力通道并对应到每个合屏编码uuid中
	 * @param selBundles
	 * @param videoSet
	 * @return
	 */
	private Map<String, List<ChannelSchemeBO>> selectCombineVideoCapability(Set<String> selBundles, Map<String, CombineVideoOperateBO> videoSet){
		Map<String, List<ChannelSchemeBO>> uuid2ChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
		if(selBundles == null || selBundles.isEmpty()){
			return uuid2ChannelSelMap;
		}
		
		try{
			//调资源层接口根据deviceModel查询channelName-channelParamTemplate对
			Map<String, String> channelNameParamTempMap = resourceService.queryChannelMapByDeviceModel(CommonConstant.COMBINE_VIDEO);
			for(String uuid:videoSet.keySet()){
				CombineVideoOperateBO videoSetBO = videoSet.get(uuid);
				if(videoSetBO == null){
					log.info("combineVideoLayout " + uuid + " has no combineVideoSet");
					continue;
				}
				
				boolean encodeMatch = false;
				for(String channelName:channelNameParamTempMap.keySet()){
					String channelParamTemp = channelNameParamTempMap.get(channelName);
					if(channelName.endsWith(CommonConstant.MEDIAPROC_VIDEO_ENCODE)){
						//匹配channel,两个参数分别为当前channel的参数和要比较的模板，比较结果为当前模板与当前参数是否匹配
						JSONObject video_param = videoSetBO.getCodec_param().getJSONObject("video_param");						
						JSONObject base_param = new JSONObject();
						JSONObject encode_param = new JSONObject();
						JSONObject decode_param = new JSONObject();
						base_param.put("encode_param", encode_param);
						base_param.put("decode_param", decode_param);
						decode_param.put("codec_type", video_param.get("codec"));						
						encode_param.put("codec_type", video_param.get("codec"));						
						encode_param.put("resolution", video_param.get("resolution"));
						if(video_param.get("profile") != null){							
							encode_param.put("profile", video_param.get("profile"));						
						}
						if(video_param.get("bitrate") != null){							
							encode_param.put("bitrate", video_param.get("bitrate"));
						}
						videoSetBO.setCodec_param(base_param);
						JSONObject channelParam = new JSONObject();
						JSONObject param = new JSONObject();
						channelParam.put("channel_param", param);
						param.put("base_param", videoSetBO.getCodec_param());
						if(resourceService.matchChannelParam(channelParam.toJSONString(), channelParamTemp)){
							log.info("VideoMix match: " + channelParam.toJSONString());
							encodeMatch = true;
						}else{
							log.info("VideoMix dismatch: " + channelParam.toJSONString());
						}
					}/*else if(channelName.equals(CommonConstant.SIMPLE_VIDEO_DECODE)){
						decodeMatch = true;
						for(LayoutPosBO posBO:videoSetBO.getPostion()){
							if(!resourceService.matchChannelParam(posBO.getCodec_param().toJSONString(), channelParamTemp)){
								decodeMatch = false;
								break;
							}
						}
					}*/
				}
				
				//如果都能match上，就开始在bundle中选择合适的channel，这里要选择的bundle必须具有足够空闲的channels供写锁
				if(encodeMatch){
					List<ChannelSchemeBO> selectChannels = selectPropChannels(selBundles, 1, videoSetBO.getPosition().size(), ChannelTypeInfo.VIDEOENCODE);
					if(selectChannels != null && selectChannels.size() == (1/*+videoSetBO.getPostion().size()*/)){
						//加几个ChannelSchemeBO用来存储解码通道属性
						/*for(int i=0;i<videoSetBO.getPostion().size();i++){
							ChannelSchemeBO channelSchemeBO = new ChannelSchemeBO();
							selectChannels.add(channelSchemeBO);
						}*/
						
						selectChannels.get(0).fillCombineVideoEncodeChannel(videoSetBO);
						if(null != assembleVideoMixChannelParam(selectChannels.get(0), videoSetBO)){							
							uuid2ChannelSelMap.put(uuid, selectChannels);
						}else{
							removeSelectChannels(selectChannels);
						}
					}
				}
			}
		}catch(Exception e){
			log.error("selectCombineVideoCapability failed", e);
		}
		return uuid2ChannelSelMap;
	}
	
	/**
	 * 从目标设备的能力集合中选择混音能力通道并对应到每个混音编码uuid中
	 * @param selBundles
	 * @param videoSet
	 * @return
	 */
	private Map<String, List<ChannelSchemeBO>> selectCombineAudioCapability(Set<String> selBundles, Map<String, CombineAudioOperateBO> audioSet){
		Map<String, List<ChannelSchemeBO>> uuid2ChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
		if(selBundles == null || selBundles.isEmpty()){
			return uuid2ChannelSelMap;
		}
		
		try{
			//调资源层接口根据deviceModel查询channelName-channelParamTemplate对
			Map<String, String> channelNameParamTempMap = resourceService.queryChannelMapByDeviceModel(CommonConstant.COMBINE_AUDIO);
			for(String uuid:audioSet.keySet()){
				CombineAudioOperateBO audioSetBO = audioSet.get(uuid);
				if(audioSetBO == null){
					log.info("combineAudioLayout " + uuid + " has no combineAudioSet");
					continue;
				}
				boolean encodeMatch = false;
				for(String channelName:channelNameParamTempMap.keySet()){
					String channelParamTemp = channelNameParamTempMap.get(channelName);
					if(channelName.endsWith(CommonConstant.MEDIAPROC_AUDIO_ENCODE)){
						//匹配channel,两个参数分别为当前channel的参数和要比较的模板，比较结果为当前模板与当前参数是否匹配
						JSONObject audio_param = audioSetBO.getCodec_param().getJSONObject("audio_param");						
						JSONObject base_param = new JSONObject();
						JSONObject encode_param = new JSONObject();
						JSONObject decode_param = new JSONObject();
						base_param.put("encode_param", encode_param);
						base_param.put("decode_param", decode_param);
						decode_param.put("codec_type", audio_param.get("codec"));						
						encode_param.put("codec_type", audio_param.get("codec"));
						if(audio_param.get("sample_rate") != null){							
							encode_param.put("sample_rate", audio_param.get("sample_rate"));						
						}
						
						if(audio_param.get("sample_bytes") != null){							
							encode_param.put("sample_bytes", audio_param.get("sample_bytes"));						
						}
						
						if(audio_param.get("channel_cnt") != null){							
							encode_param.put("channel_cnt", audio_param.get("channel_cnt"));						
						}
						
						if(audio_param.get("bitrate") != null){							
							encode_param.put("bitrate", audio_param.get("bitrate"));						
						}
						
						if(audio_param.get("gain") != null){							
							encode_param.put("gain", audio_param.get("gain"));						
						}
						
						if(audio_param.get("algorithm") != null){							
							encode_param.put("algorithm", audio_param.get("algorithm"));						
						}
						audioSetBO.setCodec_param(base_param);
						JSONObject channelParam = new JSONObject();
						JSONObject param = new JSONObject();
						channelParam.put("channel_param", param);
						param.put("base_param", audioSetBO.getCodec_param());
						if(resourceService.matchChannelParam(channelParam.toJSONString(), channelParamTemp)){
							log.info("AudioMix match: " + channelParam.toJSONString());
							encodeMatch = true;
						}else{
							log.info("AudioMix dismatch: " + channelParam.toJSONString());
						}
					}/*else if(channelName.equals(CommonConstant.SIMPLE_VIDEO_DECODE)){
						decodeMatch = true;
						for(LayoutPosBO posBO:videoSetBO.getPostion()){
							if(!resourceService.matchChannelParam(posBO.getCodec_param().toJSONString(), channelParamTemp)){
								decodeMatch = false;
								break;
							}
						}
					}*/
				}
				
				//如果都能match上，就开始在bundle中选择合适的channel，这里要选择的bundle必须具有足够空闲的channels供写锁
				if(encodeMatch){
					List<ChannelSchemeBO> selectChannels = selectPropChannels(selBundles, 1, audioSetBO.getSrc().size(), ChannelTypeInfo.AUDIOENCODE);
					if(selectChannels != null && selectChannels.size() == (1/*+videoSetBO.getPostion().size()*/)){						
						selectChannels.get(0).fillCombineAudioEncodeChannel(audioSetBO);
						if(assembleAudioMixChannelParam(selectChannels.get(0), audioSetBO)){
							uuid2ChannelSelMap.put(uuid, selectChannels);
						}else{
							removeSelectChannels(selectChannels);
						}
					}
				}
			}
		}catch(Exception e){
			log.error("selectCombineAudioCapability failed", e);
		}
		return uuid2ChannelSelMap;
	}
	
	/**
	 * 从目标设备的能力集合中选择MediaMux能力通道并对应到每个taskId中
	 * @param selBundles
	 * @param videoSet
	 * @return
	 */
	private Map<String, List<ChannelSchemeBO>> selectMediaMuxCapability(Set<String> selBundles, Map<String, MediaMuxOutBO> mediaMuxSet, String recordType){
		Map<String, List<ChannelSchemeBO>> task2ChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
		if(selBundles == null || selBundles.isEmpty()){
			return task2ChannelSelMap;
		}
		
		try{
			//调资源层接口根据deviceModel查询channelName-channelParamTemplate对
			Map<String, String> channelNameParamTempMap = null;
			if(CommonConstant.MEDIA_MUX_OUT.equals(recordType)){				
				channelNameParamTempMap = resourceService.queryChannelMapByDeviceModel(CommonConstant.MEDIA_MUX_OUT);
			}else if(CommonConstant.MEDIA_MUX_OUT_EMR.equals(recordType)){
				channelNameParamTempMap = resourceService.queryChannelMapByDeviceModel(CommonConstant.MEDIA_MUX_OUT_EMR);				
			}
			for(String taskId:mediaMuxSet.keySet()){
				MediaMuxOutBO mediaMuxOutBO = mediaMuxSet.get(taskId);
				if(mediaMuxOutBO == null){
					log.info("MediaMuxOut " + taskId + " has no MediaMuxOutSet");
					continue;
				}

				boolean encodeMatch = false;
				for(String channelName:channelNameParamTempMap.keySet()){
					String channelParamTemp = channelNameParamTempMap.get(channelName);
					if(channelName.endsWith(CommonConstant.MEDIAMUX_ENCODE)){
						//匹配channel,两个参数分别为当前channel的参数和要比较的模板，比较结果为当前模板与当前参数是否匹配
						JSONObject baseChannelParam = mediaMuxOutBO.getChannel_param().getJSONObject("base_param");
						JSONObject videoSourceParam = baseChannelParam.getJSONObject("video_source");
						JSONObject audioSourceParam = baseChannelParam.getJSONObject("audio_source");
						JSONObject codecParam = baseChannelParam.getJSONObject("codec_param");
						//去掉codec_param元素
						baseChannelParam.remove("codec_param");
						if(videoSourceParam != null){
							JSONObject videoSrcParam = new JSONObject();
							if(videoSourceParam.get("layer_id") != null){
								videoSrcParam.put("layer_id", videoSourceParam.get("layer_id"));
							}
							if(videoSourceParam.get("bundle_id") != null){
								videoSrcParam.put("bundle_id", videoSourceParam.get("bundle_id"));
							}
							if(videoSourceParam.get("channel_id") != null){
								videoSrcParam.put("channel_id", videoSourceParam.get("channel_id"));
							}
							baseChannelParam.put("video_source", videoSrcParam);
						}
						if(audioSourceParam != null){
							JSONObject audioSrcParam = new JSONObject();
							if(audioSourceParam.get("layer_id") != null){
								audioSrcParam.put("layer_id", audioSourceParam.get("layer_id"));
							}
							if(audioSourceParam.get("bundle_id") != null){
								audioSrcParam.put("bundle_id", audioSourceParam.get("bundle_id"));
							}
							if(audioSourceParam.get("channel_id") != null){
								audioSrcParam.put("channel_id", audioSourceParam.get("channel_id"));
							}
							baseChannelParam.put("audio_source", audioSrcParam);
						}
						
						if(codecParam != null){
							JSONObject audioParam = codecParam.getJSONObject("audio_param");
							if(audioParam != null){
								baseChannelParam.put("audio_param", audioParam);
							}
							JSONObject videoParam = codecParam.getJSONObject("video_param");
							if(videoParam != null){
								baseChannelParam.put("video_param", videoParam);
							}
						}
						JSONObject channelParam = new JSONObject();
						channelParam.put("channel_param", mediaMuxOutBO.getChannel_param());
						if(resourceService.matchChannelParam(channelParam.toJSONString(), channelParamTemp)){
							log.info("MediaMuxOut match: " + channelParam.toJSONString());
							encodeMatch = true;
						}else{							
							log.info("MediaMuxOut dismatch: " + channelParam.toJSONString());
						}
					}/*else if(channelName.equals(CommonConstant.SIMPLE_VIDEO_DECODE)){
						decodeMatch = true;
						for(LayoutPosBO posBO:videoSetBO.getPostion()){
							if(!resourceService.matchChannelParam(posBO.getCodec_param().toJSONString(), channelParamTemp)){
								decodeMatch = false;
								break;
							}
						}
					}*/
				}
				
				//如果都能match上，就开始在bundle中选择合适的channel，这里要选择的bundle必须具有足够空闲的channels供写锁
				if(encodeMatch){
					List<ChannelSchemeBO> selectChannels = selectPropChannels(selBundles, 1, 0,ChannelTypeInfo.MEDIAMUXENCODE);
					if(selectChannels != null && selectChannels.size() == 1){						
						selectChannels.get(0).fillMediaMuxEncodeChannel(mediaMuxOutBO);
						if(assembleMediaMuxChannelParam(selectChannels)){							
							task2ChannelSelMap.put(taskId, selectChannels);
						}else{
							removeSelectChannels(selectChannels);
						}
					}
				}
			}
		}catch(Exception e){
			log.error("selectMediaMuxCapability failed", e);
		}
		return task2ChannelSelMap;
	}
	
	/**
	 * 从目标设备的能力集合中选择调阅能力通道并对应到每个调阅taskId中
	 * @param selBundles
	 * @param videoSet
	 * @return
	 */
	private Map<String, List<ChannelSchemeBO>> selectMediaPushCapability(Set<String> selBundles, Map<String, MediaPushOperateBO> mediaPushSet){
		Map<String, List<ChannelSchemeBO>> task2ChannelSelMap = new HashMap<String, List<ChannelSchemeBO>>();
		if(selBundles == null || selBundles.isEmpty()){
			return task2ChannelSelMap;
		}
		try{
			//调资源层接口根据deviceModel查询channelName-channelParamTemplate对
			Map<String, String> channelNameParamTempMap = resourceService.queryChannelMapByDeviceModel(CommonConstant.MEDIA_PUSH);
			for(String taskId : mediaPushSet.keySet()){
				MediaPushOperateBO mediaPushOperateBO = mediaPushSet.get(taskId);
				if(mediaPushOperateBO == null){
					log.info("mediaPush taskId:" + taskId + " has no mediaPushSet");
					continue;
				}
				boolean encodeMatch = false;
				for(String channelName : channelNameParamTempMap.keySet()){
					String channelParamTemp = channelNameParamTempMap.get(channelName);
					if(channelName.endsWith(CommonConstant.MEDIAPUSH_ENCODE)){
						//匹配channel,两个参数分别为当前channel的参数和要比较的模板，比较结果为当前模板与当前参数是否匹配
//						TODO:匹配参数，暂时直接通过
						encodeMatch = true;
//						if(resourceService.matchChannelParam(channelParam.toJSONString(), channelParamTemp)){
//							log.info("MediaPush match: " + channelParam.toJSONString());
//							encodeMatch = true;
//						}else{
//							log.info("MediaPush dismatch: " + channelParam.toJSONString());
//						}
					}
				}
				//如果都能match上，就开始在bundle中选择合适的channel，这里要选择的bundle必须具有足够空闲的channels供写锁
				if(encodeMatch){
					List<ChannelSchemeBO> selectChannels = selectPropChannels(selBundles, 1, 0, ChannelTypeInfo.MEDIAPUSHENCODE);
					if(selectChannels != null && selectChannels.size() == 1){
						selectChannels.get(0).fillMediaPushEncodeChannel(mediaPushOperateBO);
						if(assembleMediaPushChannelParam(selectChannels)){							
							task2ChannelSelMap.put(taskId, selectChannels);
						}else{
							removeSelectChannels(selectChannels);
						}
						task2ChannelSelMap.put(taskId, selectChannels);
					}
				}
			}
		}catch(Exception e){
			log.error("selectMediaPushCapability failed", e);
		}
		return task2ChannelSelMap;
	}
	
	/**
	 * 根据合屏更新map里的通道，生成合屏能力通道并对应到每个合屏编码uuid中
	 * @param videoUpdate
	 * @return
	 */
	private Map<String, List<ChannelSchemeBO>> generateCombineVideoCapability(Map<String, CombineVideoOperateBO> videoUpdate){
		Map<String, List<ChannelSchemeBO>> uuid2ChannelUpdateMap = new HashMap<String, List<ChannelSchemeBO>>();
		if(videoUpdate == null || videoUpdate.isEmpty()){
			return uuid2ChannelUpdateMap;
		}
		
		try{
			for(String layoutUuid:videoUpdate.keySet()){
				CombineVideoOperateBO videoUpdateBO = videoUpdate.get(layoutUuid);
				if(StringUtils.isEmpty(videoUpdateBO.getLayerId()) || StringUtils.isEmpty(videoUpdateBO.getBundleId()) || StringUtils.isEmpty(videoUpdateBO.getChannelId())){
					log.info("layerId or channelId or bundelId is null " + videoUpdateBO);
					continue;
				}
				JSONObject video_param = videoUpdateBO.getCodec_param().getJSONObject("video_param");						
				JSONObject base_param = new JSONObject();
				JSONObject encode_param = new JSONObject();
				JSONObject decode_param = new JSONObject();
				base_param.put("encode_param", encode_param);
				base_param.put("decode_param", decode_param);
				decode_param.put("codec_type", video_param.get("codec"));						
				encode_param.put("codec_type", video_param.get("codec"));						
				encode_param.put("resolution", video_param.get("resolution"));						
				if(video_param.get("profile") != null){							
					encode_param.put("profile", video_param.get("profile"));						
				}
				if(video_param.get("bitrate") != null){							
					encode_param.put("bitrate", video_param.get("bitrate"));
				}
				videoUpdateBO.setCodec_param(base_param);
				ChannelSchemeBO encodeChannel = new ChannelSchemeBO();
				encodeChannel.fillCombineVideoEncodeChannel(videoUpdateBO);
				if(null != assembleVideoMixChannelParam(encodeChannel, videoUpdateBO)){
					uuid2ChannelUpdateMap.put(layoutUuid, new ArrayList<ChannelSchemeBO>());
					uuid2ChannelUpdateMap.get(layoutUuid).add(encodeChannel);					
				}
			}
		}catch(Exception e){
			log.error("generateCombineVideoCapability failed", e);
		}
		return uuid2ChannelUpdateMap;
	}
	
	/**
	 * 根据混音更新map里的通道，生成混音能力通道并对应到每个混音编码uuid中
	 * @param videoUpdate
	 * @return
	 */
	private Map<String, List<ChannelSchemeBO>> generateCombineAudioCapability(Map<String, CombineAudioOperateBO> audioUpdate){
		Map<String, List<ChannelSchemeBO>> uuid2ChannelUpdateMap = new HashMap<String, List<ChannelSchemeBO>>();
		if(audioUpdate == null || audioUpdate.isEmpty()){
			return uuid2ChannelUpdateMap;
		}
		
		try{
			for(String layoutUuid:audioUpdate.keySet()){
				CombineAudioOperateBO audioUpdateBO = audioUpdate.get(layoutUuid);
				if(StringUtils.isEmpty(audioUpdateBO.getLayerId()) || StringUtils.isEmpty(audioUpdateBO.getBundleId()) || StringUtils.isEmpty(audioUpdateBO.getChannelId())){
					log.info("layerId or channelId or bundelId is null " + audioUpdateBO);
					continue;
				}
				JSONObject audio_param = audioUpdateBO.getCodec_param().getJSONObject("audio_param");						
				JSONObject base_param = new JSONObject();
				JSONObject encode_param = new JSONObject();
				JSONObject decode_param = new JSONObject();
				base_param.put("encode_param", encode_param);
				base_param.put("decode_param", decode_param);
				decode_param.put("codec_type", audio_param.get("codec"));						
				encode_param.put("codec_type", audio_param.get("codec"));						
				if(audio_param.get("sample_rate") != null){							
					encode_param.put("sample_rate", audio_param.get("sample_rate"));						
				}
				
				if(audio_param.get("sample_bytes") != null){							
					encode_param.put("sample_bytes", audio_param.get("sample_bytes"));						
				}
				
				if(audio_param.get("channel_cnt") != null){							
					encode_param.put("channel_cnt", audio_param.get("channel_cnt"));						
				}
				
				if(audio_param.get("bitrate") != null){							
					encode_param.put("bitrate", audio_param.get("bitrate"));						
				}
				
				if(audio_param.get("gain") != null){							
					encode_param.put("gain", audio_param.get("gain"));						
				}
				
				if(audio_param.get("algorithm") != null){							
					encode_param.put("algorithm", audio_param.get("algorithm"));						
				}
				audioUpdateBO.setCodec_param(base_param);
				ChannelSchemeBO encodeChannel = new ChannelSchemeBO();
				encodeChannel.fillCombineAudioEncodeChannel(audioUpdateBO);				
				if(assembleAudioMixChannelParam(encodeChannel, audioUpdateBO)){
					uuid2ChannelUpdateMap.put(layoutUuid, new ArrayList<ChannelSchemeBO>());
					uuid2ChannelUpdateMap.get(layoutUuid).add(encodeChannel);					
				}
			}
		}catch(Exception e){
			log.error("generateCombineAudioCapability failed", e);
		}
		return uuid2ChannelUpdateMap;
	}
	
	/**
	 * 根据录制更新map里的通道，生成录制能力通道并对应到每个录制taskId中
	 * @param videoUpdate
	 * @return
	 */
	private Map<String, List<ChannelSchemeBO>> generateMediaMuxCapability(Map<String, MediaMuxOutBO> mediaMuxUpdate){
		Map<String, List<ChannelSchemeBO>> task2ChannelUpdateMap = new HashMap<String, List<ChannelSchemeBO>>();
		if(mediaMuxUpdate == null || mediaMuxUpdate.isEmpty()){
			return task2ChannelUpdateMap;
		}
		
		try{
			for(String taskId:mediaMuxUpdate.keySet()){
				MediaMuxOutBO mediaMuxUpdateBO = mediaMuxUpdate.get(taskId);
				if(StringUtils.isEmpty(mediaMuxUpdateBO.getLayerId()) || StringUtils.isEmpty(mediaMuxUpdateBO.getBundleId()) || StringUtils.isEmpty(mediaMuxUpdateBO.getChannelId())){
					log.info("layerId or channelId or bundelId is null " + mediaMuxUpdateBO);
					continue;
				}
				JSONObject baseChannelParam = mediaMuxUpdateBO.getChannel_param().getJSONObject("base_param");
				JSONObject videoSourceParam = baseChannelParam.getJSONObject("video_source");
				JSONObject audioSourceParam = baseChannelParam.getJSONObject("audio_source");
				JSONObject codecParam = baseChannelParam.getJSONObject("codec_param");
				if(videoSourceParam != null){
					JSONObject videoSrcParam = new JSONObject();
					if(videoSourceParam.get("layer_id") != null){
						videoSrcParam.put("layer_id", videoSourceParam.get("layer_id"));
					}
					if(videoSourceParam.get("bundle_id") != null){
						videoSrcParam.put("bundle_id", videoSourceParam.get("bundle_id"));
					}
					if(videoSourceParam.get("channel_id") != null){
						videoSrcParam.put("channel_id", videoSourceParam.get("channel_id"));
					}
					baseChannelParam.put("video_source", videoSrcParam);
				}
				if(audioSourceParam != null){
					JSONObject audioSrcParam = new JSONObject();
					if(audioSourceParam.get("layer_id") != null){
						audioSrcParam.put("layer_id", audioSourceParam.get("layer_id"));
					}
					if(audioSourceParam.get("bundle_id") != null){
						audioSrcParam.put("bundle_id", audioSourceParam.get("bundle_id"));
					}
					if(audioSourceParam.get("channel_id") != null){
						audioSrcParam.put("channel_id", audioSourceParam.get("channel_id"));
					}
					baseChannelParam.put("audio_source", audioSrcParam);
				}
				
				if(codecParam != null){
					JSONObject audioParam = codecParam.getJSONObject("audio_param");
					if(audioParam != null){
						baseChannelParam.put("audio_param", audioParam);
					}
					JSONObject videoParam = codecParam.getJSONObject("video_param");
					if(videoParam != null){
						baseChannelParam.put("video_param", videoParam);
					}
				}
				
				ChannelSchemeBO encodeChannel = new ChannelSchemeBO();
				encodeChannel.fillMediaMuxEncodeChannel(mediaMuxUpdateBO);
				List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
				channels.add(encodeChannel);
				if(assembleMediaMuxChannelParam(channels)){					
					task2ChannelUpdateMap.put(taskId, channels);				
				}
			}
		}catch(Exception e){
			log.error("generateMediaMuxCapability failed", e);
		}
		return task2ChannelUpdateMap;
	}

	/**
	 * 在目标bundle集合里选择合适的编码/解码通道
	 * @param dstBundles
	 * @param encodeNum
	 * @param decodeNum
	 * @return 编码/解码通道集合，先存放编码通道，后存放解码通道
	 */
	private synchronized List<ChannelSchemeBO> selectPropChannels(Set<String> dstBundles, int encodeNum, int decodeNum, ChannelTypeInfo channelType){
		if(dstBundles == null){
			return null;
		}
		
		for(String dstBundle:dstBundles){
			List<ChannelSchemeBO> results = new ArrayList<ChannelSchemeBO>();
			BundlePO dstBundlePO = bundleService.findByBundleId(dstBundle);
			if(ONLINE_STATUS.ONLINE != dstBundlePO.getOnlineStatus()){
				log.info("dstBundle is not online bundleId is " + dstBundlePO.getBundleId());
				continue;
			}
			List<ChannelBody> channels = resourceService.queryChannelsOnBundle(dstBundle);
			ChannelInfo channelInfo = bundleChannelMap.get(dstBundle);
			Integer decodeCntUse = 0;
			if(channelInfo != null){
				if(channelType == ChannelTypeInfo.AUDIOENCODE){
					decodeCntUse = channelInfo.getDecodeAudioCntUse();
				}else if(channelType == ChannelTypeInfo.VIDEOENCODE){
					decodeCntUse = channelInfo.getDecodeVideoCntUse();
				}
			}
			if(channelType == ChannelTypeInfo.VIDEOENCODE){
				if(dstBundlePO.getFreeVideoSrcCnt() == null){
					log.info(CommonConstant.MEDIAPROC_VIDEO_ENCODE + " bundle " + dstBundle + " freeVideoSrcCnt is null");
					continue;
				}
				if(decodeNum > dstBundlePO.getFreeVideoSrcCnt()-decodeCntUse){
					log.info(CommonConstant.MEDIAPROC_VIDEO_ENCODE + " bundle " + dstBundle + " hasnot enough decodeChannels to use , it has (" + dstBundlePO.getFreeVideoSrcCnt() + "-" + decodeCntUse + "), but we need " + decodeNum);					
					continue;
				}else{
					log.info(CommonConstant.MEDIAPROC_VIDEO_ENCODE + " bundle " + dstBundle + " has enough decodeChannels to use , it has (" + dstBundlePO.getFreeVideoSrcCnt() + "-" + decodeCntUse + "), we only need " + decodeNum);
				}
			}else if(channelType == ChannelTypeInfo.AUDIOENCODE){
				if(dstBundlePO.getFreeAudioSrcCnt() == null){
					log.info(CommonConstant.MEDIAPROC_AUDIO_ENCODE + " bundle " + dstBundle + " freeAudioSrcCnt is null");
					continue;
				}
				if(decodeNum > dstBundlePO.getFreeAudioSrcCnt()-decodeCntUse){
					log.info(CommonConstant.MEDIAPROC_AUDIO_ENCODE + " bundle " + dstBundle + " hasnot enough decodeChannels to use , it has (" + dstBundlePO.getFreeAudioSrcCnt() + "-" + decodeCntUse + "), but we need " + decodeNum);					
					continue;
				}else{
					log.info(CommonConstant.MEDIAPROC_AUDIO_ENCODE + " bundle " + dstBundle + " has enough decodeChannels to use , it has (" + dstBundlePO.getFreeAudioSrcCnt() + "-" + decodeCntUse + "), we only need " + decodeNum);
				}
			}
			int i=0;
			for(ChannelBody channel:channels){				
				if(channelInfo != null && channelInfo.getChannels().contains(channel.getChannel_id())){
					continue;
				}
				if(channel.getChannel_state().equals(LockStatus.IDLE.toString()) && channel.getChannel_name().endsWith(CommonConstant.MEDIAPROC_VIDEO_ENCODE) && channelType == ChannelTypeInfo.VIDEOENCODE){					
					i++;
					if(i <= encodeNum){
						ChannelSchemeBO result = ChannelSchemeBO.transFromChannelBody(channel);
						results.add(0,result);						
						log.info("select channel " + result + "for " + CommonConstant.MEDIAPROC_VIDEO_ENCODE);
					}
				}else if(channel.getChannel_state().equals(LockStatus.IDLE.toString()) && channel.getChannel_name().endsWith(CommonConstant.MEDIAPROC_AUDIO_ENCODE) && channelType == ChannelTypeInfo.AUDIOENCODE){
					i++;
					if(i <= encodeNum){
						ChannelSchemeBO result = ChannelSchemeBO.transFromChannelBody(channel);
						results.add(0,result);
						log.info("select channel " + result + "for " + CommonConstant.MEDIAPROC_AUDIO_ENCODE);
					}
				}else if(channel.getChannel_state().equals(LockStatus.IDLE.toString()) && channel.getChannel_name().endsWith(CommonConstant.MEDIAMUX_ENCODE) && channelType == ChannelTypeInfo.MEDIAMUXENCODE){
					i++;
					if(i <= encodeNum){
						ChannelSchemeBO result = ChannelSchemeBO.transFromChannelBody(channel);
						results.add(0,result);						
						log.info("select channel " + result + "for " + CommonConstant.MEDIAMUX_ENCODE);
					}
				}else if(channel.getChannel_state().equals(LockStatus.IDLE.toString()) && channel.getChannel_name().endsWith(CommonConstant.MEDIAPUSH_ENCODE) && channelType == ChannelTypeInfo.MEDIAPUSHENCODE){
					i++;
					if(i <= encodeNum){
						ChannelSchemeBO result = ChannelSchemeBO.transFromChannelBody(channel);
						results.add(0,result);						
						log.info("select channel " + result + "for " + CommonConstant.MEDIAPUSH_ENCODE);
					}
				}
				if(i >= encodeNum){
					break;
				}
			}
			if(i >= encodeNum){
				if(channelInfo == null){
					bundleChannelMap.put(dstBundle, new ChannelInfo());
				}
				for(ChannelSchemeBO result:results){
					//设置接入层uid
					result.setLayerId(dstBundlePO.getAccessNodeUid());
					//暂时为null，后面是真实的bundletype
					result.setBundleType(dstBundlePO.getBundleType());
					//合屏通道所需解码路数
					result.setDecodeCntUse(decodeNum);
					bundleChannelMap.get(dstBundle).getChannels().add(result.getChannelId());
					if(channelType == ChannelTypeInfo.AUDIOENCODE){						
						bundleChannelMap.get(dstBundle).setDecodeAudioCntUse(decodeNum + bundleChannelMap.get(dstBundle).getDecodeAudioCntUse());
					}else if(channelType == ChannelTypeInfo.VIDEOENCODE){
						bundleChannelMap.get(dstBundle).setDecodeVideoCntUse(decodeNum + bundleChannelMap.get(dstBundle).getDecodeVideoCntUse());						
					}
				}
				return results;
			}
			
		}
		return null;
	}
	
	/**
	 * //去掉本次操作在bundleChannelMap存储的bundleChannel对
	 * @param uuid2ChannelSelMap
	 */
	private synchronized void removeResourcesInTask(Map<String, List<ChannelSchemeBO>> uuid2ChannelSelMap){
		if(uuid2ChannelSelMap == null){
			return;
		}
		
		for(String uuid:uuid2ChannelSelMap.keySet()){
			List<ChannelSchemeBO> channelsToRemove = uuid2ChannelSelMap.get(uuid);
			if(channelsToRemove != null){
				for(ChannelSchemeBO channel:channelsToRemove){
					if(channel.getBundleId() == null){
						continue;
					}
					ChannelInfo channelInfo = bundleChannelMap.get(channel.getBundleId());
					if(channelInfo == null){
						continue;
					}
					channelInfo.getChannels().remove(channel.getChannelId());
					if(channel.getCombineType() == ChannelTypeInfo.VIDEOENCODE){						
						channelInfo.setDecodeVideoCntUse(channelInfo.getDecodeVideoCntUse()-channel.getDecodeCntUse());
					}else if(channel.getCombineType() == ChannelTypeInfo.AUDIOENCODE){
						channelInfo.setDecodeAudioCntUse(channelInfo.getDecodeAudioCntUse()-channel.getDecodeCntUse());						
					}
					if(channelInfo.getChannels().isEmpty()){
						bundleChannelMap.remove(channel.getBundleId());
					}
				}
			}
		}
	}
	
	private synchronized void removeSelectChannels(List<ChannelSchemeBO> selectedChannels){
		if(selectedChannels == null){
			return;
		}
		
		for(ChannelSchemeBO channel:selectedChannels){
			if(channel.getBundleId() == null){
				continue;
			}
			ChannelInfo channelInfo = bundleChannelMap.get(channel.getBundleId());
			if(channelInfo == null){
				continue;
			}
			channelInfo.getChannels().remove(channel.getChannelId());
			if(channel.getCombineType() == ChannelTypeInfo.VIDEOENCODE){						
				channelInfo.setDecodeVideoCntUse(channelInfo.getDecodeVideoCntUse()-channel.getDecodeCntUse());
			}else if(channel.getCombineType() == ChannelTypeInfo.AUDIOENCODE){
				channelInfo.setDecodeAudioCntUse(channelInfo.getDecodeAudioCntUse()-channel.getDecodeCntUse());						
			}			
			if(channelInfo.getChannels().isEmpty()){
				bundleChannelMap.remove(channel.getBundleId());
			}
		}
	}
	
	/**
	 * 返回锁定失败的通道信息
	 * @param result
	 * @param id2ChannelMap
	 * @return
	 */
	private JSONArray generateLockFailResultJsonArray(ResultMap lockResultMap, List<ChannelSchemeBO> dstChannels){
		if(lockResultMap == null || dstChannels == null){
			return null;
		}
		
		try{
			JSONArray resultArray = new JSONArray();			
			for(ChannelSchemeBO channelBO:dstChannels){				
				if(StringUtils.isEmpty(channelBO.getTaskId())){
					continue;
				}
				ChannelStatusBody statusBody = (ChannelStatusBody)lockResultMap.get(channelBO.getTaskId());
				if(statusBody == null || !statusBody.isResult()){
					JSONObject resultObj = new JSONObject();
					resultObj.put("layerId", channelBO.getLayerId());						
					resultObj.put("bundleId", channelBO.getBundleId());						
					resultObj.put("channelId", channelBO.getChannelId());
					resultArray.add(resultObj);
				}
			}
			if(resultArray.size() > 0){
				return resultArray;
			}
		}catch(Exception e){
			log.error("generateLockFailResultJsonArray failed", e);
		}
		return null;
	}
	
	/**
	 * 返回锁定失败的bundle信息
	 * @param lockResultMap
	 * @param dstBundles
	 * @return
	 */
	private JSONArray generateBundleLockFailResultJsonArray(ResultMap lockResultMap, List<BundleSchemeBO> dstBundles){
		if(lockResultMap == null || dstBundles == null){
			return null;
		}
		
		try{
			JSONArray resultArray = new JSONArray();			
			for(BundleSchemeBO bundleBO:dstBundles){				
				if(StringUtils.isEmpty(bundleBO.getTaskId())){
					continue;
				}
				ChannelStatusBody statusBody = (ChannelStatusBody)lockResultMap.get(bundleBO.getTaskId());
				if(statusBody == null || !statusBody.isResult()){
					JSONObject resultObj = new JSONObject();
					resultObj.put("layerId", bundleBO.getLayerId());						
					resultObj.put("bundleId", bundleBO.getBundleId());
					resultArray.add(resultObj);
				}
			}
			if(resultArray.size() > 0){
				return resultArray;
			}
		}catch(Exception e){
			log.error("generateBundleLockFailResultJsonArray failed", e);
		}
		return null;
	}
	/**
	 * 合屏/混音/mediaMux操作的返回值
	 * @param id2ChannelMap
	 * @return
	 */
	private JSONArray generateResultJsonArray(Map<String, List<ChannelSchemeBO>> id2ChannelMap){
		if(id2ChannelMap == null){
			return null;
		}
		
		try{
			JSONArray resultArray = new JSONArray();
			for(String id:id2ChannelMap.keySet()){
				List<ChannelSchemeBO> channels = id2ChannelMap.get(id);
				if(channels != null && !channels.isEmpty()){
					JSONObject resultObj = new JSONObject();
					resultArray.add(resultObj);
					resultObj.put("uuid", channels.get(0).getUuid());						
					resultObj.put("layerId", channels.get(0).getLayerId());						
					resultObj.put("bundleId", channels.get(0).getBundleId());						
					resultObj.put("channelId", channels.get(0).getChannelId());						
				}				
			}
			if(resultArray.size() > 0){
				return resultArray;
			}
		}catch(Exception e){
			log.error("generateResultJsonArray", e);
		}
		return null;
	}
	
	/**
	 * 整理呼叫、转发的channelParam
	 * @param channels
	 */
	private void assembleChannelParam(List<ChannelSchemeBO> channels){
		if(channels == null || channels.isEmpty()){
			return;
		}
		
		Iterator<ChannelSchemeBO> iterator = channels.iterator();
		while(iterator.hasNext()){
			ChannelSchemeBO channelToOperate = iterator.next();
			JSONObject channel_param = new JSONObject();
			try{
				if(StringUtils.isEmpty(channelToOperate.getBaseType())){
					ChannelSchemePO channelSchemePO = channelService.findByBundleIdAndChannelId(channelToOperate.getBundleId(), channelToOperate.getChannelId());
					channel_param.put("base_type", channelTemplateService.get(channelSchemePO.getChannelTemplateID()).getBaseType());
					channelToOperate.setBaseType(channel_param.getString("base_type"));
				}else{				
					channel_param.put("base_type", channelToOperate.getBaseType());
				}
				if(channelToOperate.getBaseType().contains("Video")){
					channel_param.put("base_param", channelToOperate.getChannelParam().getJSONObject("video_param"));
				}else{
					channel_param.put("base_param", channelToOperate.getChannelParam().getJSONObject("audio_param"));
				}
				if(channelToOperate.getOsds() != null && !channelToOperate.getOsds().isEmpty()){
					channel_param.getJSONObject("base_param").put("osds", channelToOperate.getOsds());
				}
				if(channelToOperate.getSrc() != null && !channelToOperate.getSrc().isEmpty()){
					SrcBO srcBO = channelToOperate.getSrc().get(0);
					JSONObject source = new JSONObject();
					channel_param.getJSONObject("base_param").put("source", source);
					source.put("layer_id", srcBO.getLayerId());
					source.put("bundle_id",srcBO.getBundleId());
					source.put("channel_id", srcBO.getChannelId());
				}
				
				if(channelToOperate.getScreens() != null && !channelToOperate.getScreens().isEmpty()){
					channel_param.getJSONObject("base_param").put("screens", channelToOperate.getScreens());
				}
				channelToOperate.setChannelParam(channel_param);
			}catch(Exception ex){
				iterator.remove();
				log.error("assembleChannelParam failed " + channelToOperate, ex);
			}
			
		}
		
	}
	
	/**
	 * 整理大屏音频、视频的channelParam
	 * @param channels
	 */
	private void assembleJv230ChannelParam(List<ChannelSchemeBO> channels){
		if(channels == null || channels.isEmpty()){
			return;
		}
		
		Iterator<ChannelSchemeBO> iterator = channels.iterator();
		while(iterator.hasNext()){
			ChannelSchemeBO channelToOperate = iterator.next();
			try{
				JSONObject baseParam = channelToOperate.getChannelParam().getJSONObject("base_param");
				if(baseParam.get("codec_type") != null){
					baseParam.put("codec", baseParam.get("codec_type"));
					baseParam.remove("codec_type");
				}
				ChannelSchemePO channelSchemePO = channelService.findByBundleIdAndChannelId(channelToOperate.getBundleId(), channelToOperate.getChannelId());
				channelToOperate.getChannelParam().put("base_type", channelTemplateService.get(channelSchemePO.getChannelTemplateID()).getBaseType());
				log.info("------------------------MatrixChannelParam is " + channelToOperate.getChannelParam().toJSONString());
			}catch(Exception e){
				iterator.remove();
				log.error("assembleJv230ChannelParam faild " + channelToOperate, e);
			}			
		}
	}
	
	/**
	 * 整理大屏音频的源 "type":"channel/combineAudio"
	 * @param channels
	 * @param uuid2Channels
	 * @param uuid2updateChannels
	 */
	private void assembleJv230AudioSrcParam(List<ChannelSchemeBO> channels, 
			Map<String, List<ChannelSchemeBO>> uuid2Channels, Map<String, List<ChannelSchemeBO>> uuid2updateChannels,
			Map<String, List<ChannelSchemeBO>> mediaPush_taskId2ChannelSelMap, Map<String, List<ChannelSchemeBO>> mediaPush_taskId2ChannelUpdateMap){
		if(channels == null || channels.isEmpty()){
			return;
		}
		Iterator<ChannelSchemeBO> iterator = channels.iterator();
		while(iterator.hasNext()){
			ChannelSchemeBO channelToOperate = iterator.next();
			try{
				JSONObject source = channelToOperate.getChannelParam().getJSONObject("base_param").getJSONObject("source");
				String type = source.getString("type");
				String uuid = source.getString("uuid");
				if("combineVideo".equals(type)){
					log.error("jv230AudioSet has combineVideo source, so failed " + channelToOperate);
					iterator.remove();							
				}else if("combineAudio".equals(type)){
					ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,uuid2Channels, uuid2updateChannels);
					if(channelSchemeBO != null){
						source.put("layer_id", channelSchemeBO.getLayerId());
						source.put("bundle_id", channelSchemeBO.getBundleId());
						source.put("channel_id", channelSchemeBO.getChannelId());
						source.remove("type");
						source.remove("uuid");
					}else{
						log.error("jv230AudioSet hasnot find combineAudio source and uuid is " + uuid + " ,so failed " + channelToOperate);
						iterator.remove();
					}					
				}else if("mediaPush".equals(type)){
					ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid, mediaPush_taskId2ChannelSelMap, mediaPush_taskId2ChannelUpdateMap);
					if(channelSchemeBO != null){
						String suffix = "_audio";
						source.put("layer_id", channelSchemeBO.getLayerId());
						source.put("bundle_id", channelSchemeBO.getBundleId());
						source.put("channel_id", channelSchemeBO.getChannelId() + suffix);
						source.remove("type");
						source.remove("uuid");
					}					
				}else{
					source.remove("type");
					source.remove("uuid");
				}
			}catch(Exception e){
				iterator.remove();
				log.error("assembleJv230AudioSrcParam faild " + channelToOperate, e);
			}
		}
	}
	
	/**
	 * 整理大屏视频的源 "type":"channel/combineVideo"
	 * @param channels
	 * @param uuid2Channels
	 * @param uuid2updateChannels
	 */
	private void assembleJv230VideoSrcParam(List<ChannelSchemeBO> channels, 
			Map<String, List<ChannelSchemeBO>> uuid2Channels, Map<String, List<ChannelSchemeBO>> uuid2updateChannels,
			Map<String, List<ChannelSchemeBO>> mediaPush_taskId2ChannelSelMap, Map<String, List<ChannelSchemeBO>> mediaPush_taskId2ChannelUpdateMap){
		if(channels == null || channels.isEmpty()){
			return;
		}
		Iterator<ChannelSchemeBO> iterator = channels.iterator();
		while(iterator.hasNext()){
			ChannelSchemeBO channelToOperate = iterator.next();
			try{
				JSONArray sources = channelToOperate.getChannelParam().getJSONObject("base_param").getJSONArray("sources");
				for(int i=0;i<sources.size();i++){
					JSONObject source = sources.getJSONObject(i);
					String type = source.getString("type");
					String uuid = source.getString("uuid");
					if("combineAudio".equals(type)){
						log.error("jv230VideoSet has combineAudio source, so failed " + channelToOperate);
						iterator.remove();
						break;
					}else if("combineVideo".equals(type)){
						ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid,uuid2Channels, uuid2updateChannels);
						if(channelSchemeBO != null){
							source.put("layer_id", channelSchemeBO.getLayerId());
							source.put("bundle_id", channelSchemeBO.getBundleId());
							source.put("channel_id", channelSchemeBO.getChannelId());
							source.remove("type");
							source.remove("uuid");
						}else{
							log.error("jv230VideoSet hasnot find combineVideo source and uuid is " + uuid + " ,so failed " + channelToOperate);
							iterator.remove();
							break;
						}
						
					}else if("mediaPush".equals(type)){
						ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid, mediaPush_taskId2ChannelSelMap, mediaPush_taskId2ChannelUpdateMap);
						if(channelSchemeBO != null){
							String suffix = "_video";
							source.put("layer_id", channelSchemeBO.getLayerId());
							source.put("bundle_id", channelSchemeBO.getBundleId());
							source.put("channel_id", channelSchemeBO.getChannelId() + suffix);
							source.remove("type");
							source.remove("uuid");
						}					
					}else{
						source.remove("type");
						source.remove("uuid");
					}
				}
				
			}catch(Exception e){
				iterator.remove();
				log.error("assembleJv230VideoSrcParam faild " + channelToOperate, e);
			}
		}
	}
	
	/**
	 * 整理录制的视频音频源 "type":"combineVideo/combineAudio"
	 * @param channels
	 * @param uuid2Channels
	 * @param uuid2updateChannels
	 */
	private void assembleMediaMuxVideoAudioSrcParam(JSONObject base_param, 
			Map<String, List<ChannelSchemeBO>> uuid2ChannelSelMap, Map<String, List<ChannelSchemeBO>> uuid2ChannelUpdateMap,
			Map<String, List<ChannelSchemeBO>> audio_uuid2ChannelSelMap, Map<String, List<ChannelSchemeBO>> audio_uuid2ChannelUpdateMap){
		
		if(base_param.containsKey("video_source") && base_param.getJSONObject("video_source").containsKey("type")){
			JSONObject video_source = base_param.getJSONObject("video_source");
			String type = video_source.getString("type");
			if("combineVideo".equals(type)){
				String uuid = video_source.getString("uuid");
				ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid, uuid2ChannelSelMap, uuid2ChannelUpdateMap);
				if(channelSchemeBO != null){
					video_source.put("layer_id", channelSchemeBO.getLayerId());
					video_source.put("bundle_id", channelSchemeBO.getBundleId());
					video_source.put("channel_id", channelSchemeBO.getChannelId());
					video_source.remove("type");
					video_source.remove("uuid");
				}else{
					log.error("录制未找到合屏源 uuid = " + uuid + " , base_param = " + base_param.toJSONString());
				}
			}
		}
		
		if(base_param.containsKey("audio_source") && base_param.getJSONObject("audio_source").containsKey("type")){
			JSONObject audio_source = base_param.getJSONObject("audio_source");
			String type = audio_source.getString("type");
			if("combineAudio".equals(type)){
				String uuid = audio_source.getString("uuid");
				ChannelSchemeBO channelSchemeBO = queryFromChannelMap(uuid, audio_uuid2ChannelSelMap, audio_uuid2ChannelUpdateMap);
				if(channelSchemeBO != null){
					audio_source.put("layer_id", channelSchemeBO.getLayerId());
					audio_source.put("bundle_id", channelSchemeBO.getBundleId());
					audio_source.put("channel_id", channelSchemeBO.getChannelId());
					audio_source.remove("type");
					audio_source.remove("uuid");
				}else{
					log.error("录制未找到混音源 uuid = " + uuid + " , base_param = " + base_param.toJSONString());
				}
			}
		}
	}

	/**
	 * 整理录制的
	 * @param channels
	 */
	private boolean assembleMediaMuxChannelParam(List<ChannelSchemeBO> channels){
		if(channels == null || channels.isEmpty()){
			return false;
		}
		
		Iterator<ChannelSchemeBO> iterator = channels.iterator();
		while(iterator.hasNext()){
			ChannelSchemeBO channelToOperate = iterator.next();
			JSONObject channel_param = new JSONObject();
			try{
				ChannelSchemePO channelSchemePO = channelService.findByBundleIdAndChannelId(channelToOperate.getBundleId(), channelToOperate.getChannelId());
				if(StringUtils.isEmpty(channelToOperate.getBaseType())){
					channel_param.put("base_type", channelTemplateService.get(channelSchemePO.getChannelTemplateID()).getBaseType());					
				}else{				
					channel_param.put("base_type", channelToOperate.getBaseType());
				}
				channel_param.put("base_param", channelToOperate.getChannelParam().getJSONObject("base_param"));			
				channelToOperate.setChannelParam(channel_param);
			}catch(Exception ex){
				iterator.remove();
				log.error("assembleMediaMuxChannelParam failed " + channelToOperate, ex);
			}
			
		}
		if(channels.isEmpty()){
			return false;
		}
		return true;
		
	}
	
	/**
	 * 整理调阅的
	 * @param channels
	 */
	private boolean assembleMediaPushChannelParam(List<ChannelSchemeBO> channels){
		if(channels == null || channels.isEmpty()){
			return false;
		}
		
		Iterator<ChannelSchemeBO> iterator = channels.iterator();
		while(iterator.hasNext()){
			ChannelSchemeBO channelToOperate = iterator.next();
			JSONObject channel_param = new JSONObject();
			try{
				ChannelSchemePO channelSchemePO = channelService.findByBundleIdAndChannelId(channelToOperate.getBundleId(), channelToOperate.getChannelId());
				if(StringUtils.isEmpty(channelToOperate.getBaseType())){
					channel_param.put("base_type", channelTemplateService.get(channelSchemePO.getChannelTemplateID()).getBaseType());					
				}else{				
					channel_param.put("base_type", channelToOperate.getBaseType());
				}
				channel_param.put("base_param", channelToOperate.getChannelParam().getJSONObject("base_param"));			
				channelToOperate.setChannelParam(channel_param);
			}catch(Exception ex){
				iterator.remove();
				log.error("assembleMediaPushChannelParam failed " + channelToOperate, ex);
			}
			
		}
		if(channels.isEmpty()){
			return false;
		}
		return true;
		
	}
	
	/**
	 * 整理合屏的channel_param
	 * @param channelForVideoMix
	 * @param videoOperateBO
	 * @return src数组用于把其中以合屏为源的换成对应的channelId,bundleId,layerId
	 */
	private JSONArray assembleVideoMixChannelParam(ChannelSchemeBO channelForVideoMix, CombineVideoOperateBO videoOperateBO){
		JSONArray srcArray = new JSONArray();
		if(channelForVideoMix == null){
			return null;
		}
		try{
			JSONObject channel_param = new JSONObject();
			ChannelSchemePO channelSchemePO = channelService.findByBundleIdAndChannelId(channelForVideoMix.getBundleId(), channelForVideoMix.getChannelId());
			if(StringUtils.isEmpty(channelForVideoMix.getBaseType())){
				channel_param.put("base_type", channelTemplateService.get(channelSchemePO.getChannelTemplateID()).getBaseType());					
			}else{				
				channel_param.put("base_type", channelForVideoMix.getBaseType());
			}
			channel_param.put("base_param", videoOperateBO.getCodec_param());
			JSONObject encodeParamObj = channel_param.getJSONObject("base_param").getJSONObject("encode_param");
			if(encodeParamObj != null && videoOperateBO.getPosition() != null && !videoOperateBO.getPosition().isEmpty()){
				/**
				 * 构造screen_layouts
				 */
				JSONArray screen_layouts = new JSONArray();				
				encodeParamObj.put("screen_layouts", screen_layouts);
				for(LayoutPosBO posBO:videoOperateBO.getPosition()){
					JSONObject screenLayout = new JSONObject();
					screen_layouts.add(screenLayout);
					screenLayout.put("x", Integer.valueOf(posBO.getX()));
					screenLayout.put("y", Integer.valueOf(posBO.getY()));
					screenLayout.put("width", Integer.valueOf(posBO.getW()));
					screenLayout.put("height", Integer.valueOf(posBO.getH()));
					screenLayout.put("z_index", Integer.valueOf(posBO.getZ_index()));
					screenLayout.put("interval", posBO.getPollingTime());
					if(posBO.getPolling_index() != null){
						screenLayout.put("polling_index", Integer.valueOf(posBO.getPolling_index()));
					}
					if(new Integer(0).equals(posBO.getPollingTime())){						
						screenLayout.put("is_polling", "false");
					}else{
						screenLayout.put("is_polling", "true");						
					}
					
					/**
					 * 构造解码通道的源
					 */
					if(posBO.getSrc() != null && !posBO.getSrc().isEmpty()){
						JSONArray sources = new JSONArray();
						screenLayout.put("sources", sources);
						
						for(SrcBO src:posBO.getSrc()){
							JSONObject source = new JSONObject();
							sources.add(source);
							srcArray.add(source);
							source.put("type", src.getType());
							source.put("uuid", src.getUuid());
							source.put("layer_id", src.getLayerId());
							source.put("bundle_id", src.getBundleId());
							source.put("channel_id", src.getChannelId());
							if(src.getVisible() != null){
								source.put("visible", Integer.valueOf(src.getVisible()));
							}
						}						
					}										
				}
			}
			channelForVideoMix.setCombineVideoSrc(srcArray);
			channelForVideoMix.setChannelParam(channel_param);
			log.info("------------------------VideoMixChannelParam is " + channel_param.toJSONString());
		}catch(Exception ex){
			log.error("assembleVideoMixChannelParam failed " + channelForVideoMix, ex);
			return null;
		}
		return srcArray;
		
		
	}
	
	/**
	 * 整理混音的channel_param
	 * @param channels
	 */
	private boolean assembleAudioMixChannelParam(ChannelSchemeBO channelForAudioMix, CombineAudioOperateBO audioOperateBO){
		if(channelForAudioMix == null){
			return false;
		}
		
		try{
			JSONObject channel_param = new JSONObject();
			ChannelSchemePO channelSchemePO = channelService.findByBundleIdAndChannelId(channelForAudioMix.getBundleId(), channelForAudioMix.getChannelId());		
			if(StringUtils.isEmpty(channelForAudioMix.getBaseType())){
				channel_param.put("base_type", channelTemplateService.get(channelSchemePO.getChannelTemplateID()).getBaseType());					
			}else{				
				channel_param.put("base_type", channelForAudioMix.getBaseType());
			}
			channel_param.put("base_param", audioOperateBO.getCodec_param());
			JSONObject baseParamObj = channel_param.getJSONObject("base_param");
			if(audioOperateBO.getSrc() != null && !audioOperateBO.getSrc().isEmpty()){
				/**
				 * 构造sources
				 */
				JSONArray sources = new JSONArray();
				baseParamObj.put("sources", sources);
				
				for(SrcBO src:audioOperateBO.getSrc()){
					JSONObject source = new JSONObject();
					sources.add(source);
					source.put("layer_id", src.getLayerId());
					source.put("bundle_id", src.getBundleId());
					source.put("channel_id", src.getChannelId());
				}	
			}
			channelForAudioMix.setChannelParam(channel_param);
			log.info("------------------------AudioMixChannelParam is " + channel_param.toJSONString());
		}catch(Exception ex){
			log.error("assembleAudioMixChannelParam failed " + channelForAudioMix, ex);
			return false;
		}
		return true;
		
		
	}
	
	private ChannelSchemeBO queryFromChannelMap(String uuid,Map<String, List<ChannelSchemeBO>> uuid2Channels, Map<String, List<ChannelSchemeBO>> uuid2updateChannels){
		
		if(uuid2Channels == null && uuid2updateChannels == null){
			return null;
		}
		try{
			List<ChannelSchemeBO> destChannels = uuid2Channels.get(uuid);
			if(destChannels != null && destChannels.size() > 0){
				return destChannels.get(0);
			}	
			
			destChannels = uuid2updateChannels.get(uuid);
			if(destChannels != null && destChannels.size() > 0){
				return destChannels.get(0);
			}						
		}catch(Exception e){
			log.error("queryFromChannelMap failed", e);
		}		
		
		return null;
	}
	
	/**
	 * 将合屏中以合屏源换成对应的channelId,bundleId和layerId
	 * @param combineVideoSrc
	 */
	private boolean treatCombineVideoSource(JSONArray combineVideoSrc, Map<String, List<ChannelSchemeBO>> combineVideoMap, Map<String, List<ChannelSchemeBO>> combineVideoUpdateMap){
		if(combineVideoSrc == null || combineVideoSrc.isEmpty()){
			log.info("---------combineVideoSrc is null or empty");
			return true;
		}
		try{
			for(int i=0;i<combineVideoSrc.size();i++){
				JSONObject videoSrc = combineVideoSrc.getJSONObject(i);
				if(!"channel".equals(videoSrc.get("type"))){
					String uuid = videoSrc.getString("uuid");
					if(!combineVideoMap.containsKey(uuid) && !combineVideoUpdateMap.containsKey(uuid)){						
						log.info("combineVideoCmd hasnot this combineVideo with uuid " + uuid);
						return false;
					}
					ChannelSchemeBO combineVideoChannel = combineVideoMap.containsKey(uuid)?combineVideoMap.get(uuid).get(0):
						combineVideoUpdateMap.get(uuid).get(0);
					videoSrc.remove("type");
					videoSrc.remove("uuid");
					videoSrc.put("layer_id", combineVideoChannel.getLayerId());
					videoSrc.put("bundle_id", combineVideoChannel.getBundleId());
					videoSrc.put("channel_id", combineVideoChannel.getChannelId());
				}else{
					videoSrc.remove("type");					
					videoSrc.remove("uuid");
				}
			}
			return true;
		}catch(Exception e){
			log.error("treatCombineVideoSource", e);			
			return false;
		}
	}
	private static class ChannelInfo{
		
		List<String> channels = new ArrayList<String>();
		
		Integer decodeAudioCntUse = 0;
		Integer decodeVideoCntUse = 0;

		public List<String> getChannels() {
			return channels;
		}

		public void setChannels(List<String> channels) {
			this.channels = channels;
		}

		public Integer getDecodeAudioCntUse() {
			return decodeAudioCntUse;
		}

		public void setDecodeAudioCntUse(Integer decodeAudioCntUse) {
			this.decodeAudioCntUse = decodeAudioCntUse;
		}

		public Integer getDecodeVideoCntUse() {
			return decodeVideoCntUse;
		}

		public void setDecodeVideoCntUse(Integer decodeVideoCntUse) {
			this.decodeVideoCntUse = decodeVideoCntUse;
		}										
		
	}
}
