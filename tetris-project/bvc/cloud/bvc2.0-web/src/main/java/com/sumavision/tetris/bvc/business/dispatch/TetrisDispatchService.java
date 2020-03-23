package com.sumavision.tetris.bvc.business.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.dispatch.bo.ChannelBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchResponseBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchResponseBodyBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.SourceParamBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StartBundleDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StartUserDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopBundleDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchByUserIdAndMeetingCodeBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopUserDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.dao.TetrisDispatchDAO;
import com.sumavision.tetris.bvc.business.dispatch.enumeration.DispatchType;
import com.sumavision.tetris.bvc.business.dispatch.po.TetrisDispatchPO;
import com.sumavision.tetris.bvc.business.dispatch.po.TetrisDispatchChannelPO;
import com.sumavision.tetris.bvc.business.dispatch.util.DispatchQueryUtil;

import lombok.extern.slf4j.Slf4j;

import com.sumavision.tetris.bvc.business.dispatch.util.DispatchCodecParamUtil;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TetrisDispatchService {
	
	@Autowired
	private DispatchCodecParamUtil dispatchCodecParamUtil;
	
	@Autowired
	private DispatchQueryUtil dispatchQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private TetrisDispatchDAO tetrisDispatchDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;	
	
	public DispatchResponseBO dispatch(DispatchBO dispatch) throws Exception{
		
		log.info("调度：" + JSON.toJSONString(dispatch));
		
		if(dispatch.getStartBundleDispatch() == null) dispatch.setStartBundleDispatch(new ArrayList<StartBundleDispatchBO>());
		if(dispatch.getStartUserDispatch() == null) dispatch.setStartUserDispatch(new ArrayList<StartUserDispatchBO>());
		if(dispatch.getStopBundleDispatch() == null) dispatch.setStopBundleDispatch(new ArrayList<StopBundleDispatchBO>());
		if(dispatch.getStopTaskDispatch() == null) dispatch.setStopTaskDispatch(new ArrayList<StopTaskDispatchBO>());
		if(dispatch.getStopTaskDispatchByUserIdAndMeetingCodeAndSource() == null) dispatch.setStopTaskDispatchByUserIdAndMeetingCodeAndSource(new ArrayList<StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO>());
		if(dispatch.getStopTaskDispatchByUserIdAndMeetingCode() == null) dispatch.setStopTaskDispatchByUserIdAndMeetingCode(new ArrayList<StopTaskDispatchByUserIdAndMeetingCodeBO>());
		if(dispatch.getStopUserDispatch() == null) dispatch.setStopUserDispatch(new ArrayList<StopUserDispatchBO>());
		
		DispatchResponseBO dispatchResponseBO = new DispatchResponseBO();
		
		LogicBO logic = new LogicBO()
				.setUserId("-1")
				.setMustLockAllBundle(false)
				.setConnectBundle(new ArrayList<ConnectBundleBO>())
				.setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
		
		//AUTO参数 TODO:测试
		AvtplPO avtpl = dispatchCodecParamUtil.generateAvtpl("AUTO", null);
		AvtplGearsPO targetGear = null;
		Set<AvtplGearsPO> gears = avtpl.getGears();
		for(AvtplGearsPO gear : gears){
			targetGear = gear;
			break;
		}
		CodecParamBO audoCodec = new CodecParamBO().set(avtpl, targetGear);
		
		//所有需要统计的：
		//newDstBundles，用于持久化，生成dst的open协议（注意DispatchStatus为RECORD_ONLY的不生成协议）
		List<TetrisDispatchPO> newDstBundles = new ArrayList<TetrisDispatchPO>();

		//newDstUsers，用于持久化
		List<TetrisDispatchPO> newDstUsers = new ArrayList<TetrisDispatchPO>();
		
		//deleteDstBundles，用于持久化，生成dst的close协议（注意DispatchStatus为RECORD_ONLY的不生成协议）
		List<TetrisDispatchPO> deleteDstBundles = new ArrayList<TetrisDispatchPO>();
		
		//deleteDstUsers，用于持久化
		List<TetrisDispatchPO> deleteDstUsers = new ArrayList<TetrisDispatchPO>();
		
		//不需要的源deleteSrcChannels，不能使用PO统计，仅用于生成呼叫协议，没有src源的、recordOnly的不需要统计
		List<IdBO> deleteSrcIdBOs = new ArrayList<IdBO>();
		
		//新的源，使用TetrisDispatchChannelPO统计，仅用于生成呼叫协议，没有src源的、recordOnly的不需要统计
		List<TetrisDispatchChannelPO> newSrcChannels = new ArrayList<TetrisDispatchChannelPO>();
		
		
		
		//统计DispatchBO中作为源、目的的bundleIds，及所有的userIds
		Set<String> srcBundleIds = new HashSet<String>();
		Set<String> dstBundleIds = new HashSet<String>();
		Set<String> userIds = new HashSet<String>();
		dispatchQueryUtil.statisticIds(dispatch, srcBundleIds, dstBundleIds, userIds);
		
		//把涉及到的 DispatchPO 都查出来，要包含目的和源，包括用户调度。
		String queryNull = "@in@case@there@is@none@";
		userIds.add(queryNull);
		dstBundleIds.add(queryNull);
		srcBundleIds.add(queryNull);
//		Set<String> p1 = userIds.size()>0?userIds:null;
//		Set<String> p2 = dstBundleIds.size()>0?dstBundleIds:null;
//		Set<String> p3 = srcBundleIds.size()>0?srcBundleIds:null;
		List<TetrisDispatchPO> existedDispatchs = tetrisDispatchDao.findByReletiveUserIdsAndBundleIds(userIds, dstBundleIds, srcBundleIds);
		
		//从 existedDispatchs 的目的和源，把bundleIds补全
		Set<String> bundleIds = new HashSet<String>();
		bundleIds.addAll(srcBundleIds);
		bundleIds.addAll(dstBundleIds);
		dispatchQueryUtil.complementBundleIds(existedDispatchs, bundleIds);
		
		//dst判重操作，既停止，又新建的目的，把停止忽略掉，但是要加到返回结果中
		
		//统计src通道，合并成？查询后区分出哪些是新增、修改的（那么就有删掉的）、没变化的，统计哪些bundle需要新增？
		//查一下src源通道被呼过吗，有就不需要呼
		//统计dst目的bundle
		
		//【StartBundleDispatch】
		for(StartBundleDispatchBO bundleDispatchBO : dispatch.getStartBundleDispatch()){
			if(bundleDispatchBO.getChannels() == null){
				bundleDispatchBO.setChannels(new ArrayList<ChannelBO>());
			}
			for(ChannelBO channelBO : bundleDispatchBO.getChannels()){
				//先把channelBO的参数补上
				dispatchCodecParamUtil.complementCodecParam(channelBO);
			}
			
			//在existedDispatchs中找该bundle
			String temp = "";
			TetrisDispatchPO bundleDispatchPO = dispatchQueryUtil.queryDispatchPOByDstBundleId(existedDispatchs, bundleDispatchBO.getBundleId());
			if(bundleDispatchPO == null){
				//这个dst bundle是新增的
				bundleDispatchPO = new TetrisDispatchPO().setBundleAndChannel(bundleDispatchBO);
				newDstBundles.add(bundleDispatchPO);
				if(!bundleDispatchBO.isRecordOnly()){
					for(TetrisDispatchChannelPO channelPO : bundleDispatchPO.getChannels()){
						if(channelPO.getSourceType() != null){
							newSrcChannels.add(channelPO);
						}
					}
				}else{
					temp = "（仅统计，不调度）";
				}
				log.info("新增一个bundleId: " + bundleDispatchBO.getBundleId() + " 的设备调度" + temp);
			}else{
				if(bundleDispatchBO.isRecordOnly()){
					temp = "（仅统计，不调度）";
				}
				log.info("修改bundleId: " + bundleDispatchBO.getBundleId() + " 的设备调度" + temp);
				DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
				responseBO.setCode(200).setTaskId(bundleDispatchBO.getTaskId()).setResult("success")
					.setDispatchId(bundleDispatchPO.getId());
				dispatchResponseBO.getDispatchResponse().add(responseBO);
				
				//po的channels不能为null，以便遍历
				if(bundleDispatchPO.getChannels() == null){
					bundleDispatchPO.setChannels(new ArrayList<TetrisDispatchChannelPO>());
				}
				
				for(ChannelBO channelBO : bundleDispatchBO.getChannels()){
					
					boolean isNewChannel = true;
					//开始对比channel 后续考虑reconrdOnly状态的改变
					for(TetrisDispatchChannelPO channelPO : bundleDispatchPO.getChannels()){
						if(channelPO.getChannelId().equals(channelBO.getChannelId())){
							isNewChannel = false;
							//对比src源是否改变
							if(channelPO.getSourceType() == null){
								if(channelBO.getSource_param() == null){
									//新老都没有src源
									log.info("bundleId: " + bundleDispatchBO.getBundleId() + " channelId: " + channelBO.getChannelId() + " 前后都没有src源");
								}else{
									//有新的src源:统计通道可以吗？
									log.info("bundleId: " + bundleDispatchBO.getBundleId() + " channelId: " + channelBO.getChannelId() + " 有了src源");
									channelPO.set(channelBO);
									if(!bundleDispatchPO.isRecordOnly()){
										if(channelPO.getSourceType() != null){
											newSrcChannels.add(channelPO);
										}
									}
								}
							}else{
								if(channelBO.getSource_param() == null){
									//减去这个老PO的src源:统计通道可以吗？
									log.info("bundleId: " + bundleDispatchBO.getBundleId() + " channelId: " + channelBO.getChannelId() + " 删掉了src源");
									IdBO d = new IdBO().set(channelPO);
									if(!bundleDispatchPO.isRecordOnly()){
										deleteSrcIdBOs.add(d);
									}
									channelPO.clearSource();
								}else{
									//判断是否换源
									if(channelPO.sourceEquals(channelBO)){
										//源不变
										log.info("bundleId: " + bundleDispatchBO.getBundleId() + " channelId: " + channelBO.getChannelId() + " 的src源没有变化");
									}else{
										//换源了，减去PO的源，新增BO的源
										log.info("bundleId: " + bundleDispatchBO.getBundleId() + " channelId: " + channelBO.getChannelId() + " 的src源发生变化");
										IdBO d = new IdBO().set(channelPO);
										channelPO.set(channelBO);
										//后续考虑reconrdOnly状态的改变
										if(!bundleDispatchPO.isRecordOnly()){
											deleteSrcIdBOs.add(d);
											newSrcChannels.add(channelPO);
										}
									}
								}
							}
							break;
						}
					}//遍历完一遍bundleDispatchPO.getChannels()
					
					if(isNewChannel){
						//把新通道加入TetrisDispatchChannelPO中
						log.info("bundleId: " + bundleDispatchBO.getBundleId() + " 增加了通道channelId: " + channelBO.getChannelId());
						TetrisDispatchChannelPO channelPO = new TetrisDispatchChannelPO().set(channelBO);
						channelPO.setDispatch(bundleDispatchPO);
						bundleDispatchPO.getChannels().add(channelPO);
						if(!bundleDispatchPO.isRecordOnly()){
							if(channelPO.getSourceType() != null){
								newSrcChannels.add(channelPO);
							}
						}
					}
					
				}//遍历完channelBO
			}//修改一个设备调度
			
			
			
		}//遍历完StartBundleDispatchBO
		
		tetrisDispatchDao.save(newDstBundles);
		tetrisDispatchDao.save(existedDispatchs);
		
		//save之后有了id，生成返回BO
		for(TetrisDispatchPO newDstBundle : newDstBundles){
			DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
			responseBO.setCode(200).setTaskId(newDstBundle.getTaskId()).setResult("success")
				.setDispatchId(newDstBundle.getId());
			dispatchResponseBO.getDispatchResponse().add(responseBO);
		}
		
		
		
		
		
		//【StartUserDispatch】
		for(StartUserDispatchBO bundleUserBO : dispatch.getStartUserDispatch()){
			if(bundleUserBO.getChannels() == null){
				bundleUserBO.setChannels(new ArrayList<ChannelBO>());
			}
			for(ChannelBO channelBO : bundleUserBO.getChannels()){
				//先把channelBO的参数补上
				dispatchCodecParamUtil.complementCodecParam(channelBO);
			}
			
			String temp = "";
			if(bundleUserBO.isRecordOnly()){
				temp = "（仅统计，不调度）";
			}
			
			//区分新建还是更新
			Long dispatchId = bundleUserBO.getDispatchId();
			if(dispatchId == null){
				//新建
				log.info("新增一个userId: " + bundleUserBO.getUserId() + " 的用户调度" + temp);
				TetrisDispatchPO userDispatchPO = new TetrisDispatchPO().setBundleAndChannel(bundleUserBO);
				newDstUsers.add(userDispatchPO);
				if(!bundleUserBO.isRecordOnly()){
					for(TetrisDispatchChannelPO channelPO : userDispatchPO.getChannels()){
						if(channelPO.getSourceType() != null){
							newSrcChannels.add(channelPO);
						}
					}
				}
			}else{
				//更新
				TetrisDispatchPO userDispatchPO = dispatchQueryUtil.queryDispatchPOById(existedDispatchs, dispatchId);
				if(userDispatchPO == null){
					DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
					responseBO.setCode(601).setTaskId(bundleUserBO.getTaskId()).setResult("fail")
						.setErrMsg("任务不存在，无法更新！可能已被业务停止");
					dispatchResponseBO.getDispatchResponse().add(responseBO);
					log.warn("修改用户调度dispatchId: " + dispatchId + " 任务不存在");
					continue;
				}
				//TODO:修改用户调度，需要对比新老通道，后续完成
				log.info("修改userId: " + bundleUserBO.getUserId() + " dispatchId: " + dispatchId + " 的用户调度" + temp);				
			}
			
		}//遍历完StartUserDispatchBO
		
		tetrisDispatchDao.save(newDstUsers);
		tetrisDispatchDao.save(existedDispatchs);
		
		//save之后有了id，生成返回BO
		for(TetrisDispatchPO newDstUser : newDstUsers){
			DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
			responseBO.setCode(200).setTaskId(newDstUser.getTaskId()).setResult("success")
				.setDispatchId(newDstUser.getId());
			dispatchResponseBO.getDispatchResponse().add(responseBO);
		}
		
		
		
		
		//【StopBundleDispatch】
		for(StopBundleDispatchBO stopBundleDispatchBO : dispatch.getStopBundleDispatch()){
						
			//在existedDispatchs中找该bundle
			TetrisDispatchPO bundleDispatchPO = dispatchQueryUtil.queryDispatchPOByDstBundleId(existedDispatchs, stopBundleDispatchBO.getBundleId());
			
			//没找到调度任务，该任务不存在
			if(bundleDispatchPO == null){
				DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
				responseBO.setCode(601).setTaskId(stopBundleDispatchBO.getTaskId()).setResult("fail")
					.setErrMsg("任务不存在，可能已被业务停止");
				dispatchResponseBO.getDispatchResponse().add(responseBO);
				log.info("停止设备调度bundleId: " + stopBundleDispatchBO.getBundleId() + " 任务不存在");
				continue;
			}
			
			//打印
			String temp = "";
			if(bundleDispatchPO.isRecordOnly()){
				temp = "（仅统计，不调度）";
			}
			log.info("停止一个bundleId: " + bundleDispatchPO.getBundleId() + " 的设备调度" + temp);
			
			stopDispatch(bundleDispatchPO, existedDispatchs, deleteDstBundles, deleteDstUsers, deleteSrcIdBOs);
			
			
		}//遍历完StopBundleDispatchBO
		
		//【stopTaskDispatch】
		//按任务id停止调度，可以停止设备调度和用户调度
		for(StopTaskDispatchBO stopTaskDispatchBO : dispatch.getStopTaskDispatch()){
			
			//找出该调度
			Long dispatchId = Long.parseLong(stopTaskDispatchBO.getDispatchId());
			TetrisDispatchPO taskDispatchPO = dispatchQueryUtil.queryDispatchPOById(existedDispatchs, dispatchId);
			if(taskDispatchPO == null){
				//没找到调度任务，该任务不存在
				DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
				responseBO.setCode(601).setTaskId(stopTaskDispatchBO.getTaskId()).setResult("fail")
					.setErrMsg("任务不存在，可能已被业务停止");
				dispatchResponseBO.getDispatchResponse().add(responseBO);
				log.info("停止任务调度dispatchId: " + dispatchId + " 任务不存在");
				continue;
			}
			
			//打印
			String temp = "";
			if(taskDispatchPO.isRecordOnly()){
				temp = "（仅统计，不调度）";
			}
			log.info("停止一个任务调度id: " + taskDispatchPO.getId() + "，类型为：" + taskDispatchPO.getDispatchType().getName() + temp);
			
			stopDispatch(taskDispatchPO, existedDispatchs, deleteDstBundles, deleteDstUsers, deleteSrcIdBOs);
			
		}
		
		
		//【stopTaskDispatchByUserIdAndMeetingCodeAndSource】
		//仅用于停止“用户调度”任务，否则返回错误
		for(StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO stopBO : dispatch.getStopTaskDispatchByUserIdAndMeetingCodeAndSource()){
			
			//找出该调度，类型必须是用户调度
			String userId = stopBO.getUserId();
			String meetingCode = stopBO.getMeetingCode();
			String sourceBundleId = stopBO.getSourceBundleId();
			String sourceChannelId = stopBO.getSourceChannelId();
			
			//找出来调度
			TetrisDispatchPO dispatchPO = dispatchQueryUtil.queryDispatchPOByUserIdAndMeetingCodeAndSource(existedDispatchs, userId, meetingCode, sourceBundleId, sourceChannelId);
			if(dispatchPO == null){
				//没找到调度任务，该任务不存在
				DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
				responseBO.setCode(601).setTaskId(stopBO.getTaskId()).setResult("fail")
					.setErrMsg("任务不存在，可能已被业务停止");
				dispatchResponseBO.getDispatchResponse().add(responseBO);
				log.info("通过userId " + userId + " 和源bundleId " + sourceBundleId + " channelId " + sourceChannelId + "停止任务调度，任务不存在");
				continue;
			}else{
				if(dispatchPO.getDispatchType().equals(DispatchType.BUNDLE_BUNDLE)){
					DispatchResponseBodyBO responseBO = new DispatchResponseBodyBO();
					responseBO.setCode(601).setTaskId(stopBO.getTaskId()).setResult("fail")
						.setErrMsg("该调度类型不是用户调度");
					dispatchResponseBO.getDispatchResponse().add(responseBO);
					log.info("通过userId " + userId + " 和meetingCode " + meetingCode + "停止任务调度，该调度类型不是用户调度");
					continue;
				}
			}
			
			//打印
			String temp = "";
			if(dispatchPO.isRecordOnly()){
				temp = "（仅统计，不调度）";
			}
			log.info("通过userId和源停止一个用户调度id: " + dispatchPO.getId() + temp);
			
			stopDispatch(dispatchPO, existedDispatchs, deleteDstBundles, deleteDstUsers, deleteSrcIdBOs);
			
		}
		
		//【StopTaskDispatchByUserIdAndMeetingCodeBO】
		//可以停止设备调度和用户调度，一次停止该用户在该会议下的全部调度
		for(StopTaskDispatchByUserIdAndMeetingCodeBO stopBO : dispatch.getStopTaskDispatchByUserIdAndMeetingCode()){
			
			//找出该调度，类型必须是用户调度
			String userId = stopBO.getUserId();
			String meetingCode = stopBO.getMeetingCode();			
			List<TetrisDispatchPO> dispatchPOs = dispatchQueryUtil.queryDispatchPOByUserIdAndMeetingCode(existedDispatchs, userId, meetingCode);
			
			log.info("通过userId " + userId + " 和meetingCode " + meetingCode + "停止调度个数：" + dispatchPOs.size());
			
			for(TetrisDispatchPO dispatchPO : dispatchPOs){
				
				//打印
				String temp = "";
				if(dispatchPO.isRecordOnly()){
					temp = "（仅统计，不调度）";
				}
				log.info("通过userId " + userId + " 和meetingCode " + meetingCode + " 停止调度id: " + dispatchPO.getId() + "，类型为：" + dispatchPO.getDispatchType().getName() + temp);
				
				stopDispatch(dispatchPO, existedDispatchs, deleteDstBundles, deleteDstUsers, deleteSrcIdBOs);
				
			}//遍历完该用户该会议的所有userDispatchPOs
			
		}
		
		//【stopUserDispatch】
		//可以停止设备调度和用户调度
		for(StopUserDispatchBO stopUserDispatchBO : dispatch.getStopUserDispatch()){
			String userId = stopUserDispatchBO.getUserId();
			//找出该用户所有的调度
			List<TetrisDispatchPO> userDispatchPOs = dispatchQueryUtil.queryDispatchPOByUserId(existedDispatchs, userId);

			log.info("停止用户userId: " + userId + " 的所有调度，个数：" + userDispatchPOs.size());
			
			for(TetrisDispatchPO userDispatchPO : userDispatchPOs){
				
				//打印
				String temp = "";
				if(userDispatchPO.isRecordOnly()){
					temp = "（仅统计，不调度）";
				}
				log.info("停止用户userId: " + userId + " 的调度id: " + userDispatchPO.getId() + "，类型为：" + userDispatchPO.getDispatchType().getName() + temp);
				
				stopDispatch(userDispatchPO, existedDispatchs, deleteDstBundles, deleteDstUsers, deleteSrcIdBOs);
				
			}//遍历完该用户的所有userDispatchPOs
		}//遍历完stopUserDispatch
		
		
		

		//deleteInBatch会报错“外键约束”
//		tetrisDispatchDao.deleteInBatch(deleteDstBundles);
//		tetrisDispatchDao.deleteInBatch(deleteDstUsers);
		for(TetrisDispatchPO deleteDstBundle : deleteDstBundles){
			tetrisDispatchDao.delete(deleteDstBundle);
		}
		for(TetrisDispatchPO deleteDstUser : deleteDstUsers){
			tetrisDispatchDao.delete(deleteDstUser);
		}
		
		
		//得向资源层查询bundle_type、deviceModel
		List<BundlePO> bundlePOs = resourceBundleDao.findByBundleIds(bundleIds);
		List<ChannelSchemeDTO> channelDTOs = resourceChannelDao.findByBundleIds(bundleIds);//, ResourceChannelDAO.ENCODE_VIDEO);
		
		
		
		
		
		
		
		//呼叫新的newDstBundles、修改的dst目的existedDispatchs。后续考虑reconrdOnly的改变
		List<TetrisDispatchPO> newAndExistedDispatchs = new ArrayList<TetrisDispatchPO>();
		newAndExistedDispatchs.addAll(newDstBundles);
		newAndExistedDispatchs.addAll(existedDispatchs);
		for(TetrisDispatchPO dstBundle : newAndExistedDispatchs){
			if(dstBundle.isRecordOnly()){
				continue;
			}
			BundlePO bundle = queryUtil.queryBundlePOByBundleId(bundlePOs, dstBundle.getBundleId());
			if(bundle != null){
				List<TetrisDispatchChannelPO> dispatchChannelPOs = dstBundle.getChannels();
				//下边3个List参数必须一一对应
				List<ChannelSchemeDTO> channels = new ArrayList<ChannelSchemeDTO>();
				List<ForwardSetSrcBO> forwardSrcs = new ArrayList<ForwardSetSrcBO>();				
				List<CodecParamBO> codecParamBOs = new ArrayList<CodecParamBO>();
				for(TetrisDispatchChannelPO dispatchChannelPO : dispatchChannelPOs){
					ChannelSchemeDTO channel = queryUtil.queryChannelDTOsByBundleIdAndChannelId(channelDTOs, dstBundle.getBundleId(), dispatchChannelPO.getChannelId());
					channels.add(channel);
					
					ForwardSetSrcBO forwardSrc = null;
					if(dispatchChannelPO.getSourceType() != null){
						//如果无源，forwardSrc为null即可
						forwardSrc = new ForwardSetSrcBO().setType("channel")
								.setBundleId(dispatchChannelPO.getSourceBundleId())
								.setChannelId(dispatchChannelPO.getSourceChannelId())
								.setLayerId(dispatchChannelPO.getSourceLayerId());
					}
					forwardSrcs.add(forwardSrc);					
					
					CodecParamBO codecPrarmBO = new CodecParamBO().set(dispatchChannelPO.getVideoParam(), dispatchChannelPO.getAudioParam());
					codecParamBOs.add(codecPrarmBO);
				}
				//这里给目的通道一起加上源
				ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().set(dstBundle, bundle, forwardSrcs, channels, codecParamBOs);
				logic.getConnectBundle().add(protocalConnectBundleBO);
				
			}else{
				log.warn("呼叫目的bundleId " + dstBundle.getBundleId() + " 无法从资源层查询到此bundle");
			}
		}
				
		//呼叫新的源newSrcChannels（一定要呼叫，后续改成如果已有则不呼叫）后续考虑src可能与dst是同一个bundle
		//统计哪些bundle处理过的Map，防止重复处理
		Set<String> srcBundleIdSet = new HashSet<String>();
		for(TetrisDispatchChannelPO newSrcChannel : newSrcChannels){
			
			String srcBundleId = newSrcChannel.getSourceBundleId();
			if(srcBundleIdSet.contains(srcBundleId)){
				//已经处理过bundleId，继续
				continue;
			}			
			srcBundleIdSet.add(srcBundleId);
			
			BundlePO bundle = queryUtil.queryBundlePOByBundleId(bundlePOs, srcBundleId);
			if(bundle != null){
				
				//把newSrcChannels里边所有以该bundle做源的都找出来
				List<TetrisDispatchChannelPO> bundleNewSrcChannels = dispatchQueryUtil.queryDispatchChannelPOBySrcBundleId(newSrcChannels, srcBundleId);
				
				//统计这个源bundle里的哪些channel处理过的Map，防止重复处理
				Set<String> srcChannelIdSet = new HashSet<String>();
				
				List<ChannelSchemeDTO> channels = new ArrayList<ChannelSchemeDTO>();
				List<CodecParamBO> codecParamBOs = new ArrayList<CodecParamBO>();
				for(TetrisDispatchChannelPO dispatchChannelPO : bundleNewSrcChannels){
					String srcChannelId = dispatchChannelPO.getSourceChannelId();
					if(srcChannelIdSet.contains(srcChannelId)){
						//已经处理过channelId，继续
						continue;
					}
					srcChannelIdSet.add(srcChannelId);
					
					ChannelSchemeDTO channel = queryUtil.queryChannelDTOsByBundleIdAndChannelId(channelDTOs, srcBundleId, srcChannelId);
					channels.add(channel);
					CodecParamBO codecPrarmBO = new CodecParamBO().set(dispatchChannelPO.getVideoParam(), dispatchChannelPO.getAudioParam());
					codecParamBOs.add(codecPrarmBO);
				}
				
				ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().set(newSrcChannel.getDispatch(), bundle, null, channels, codecParamBOs);
				logic.getConnectBundle().add(protocalConnectBundleBO);
			}else{
				log.warn("呼叫源bundleId " + srcBundleId + " 无法从资源层查询到此bundle");
			}
		}
		
		
		
		//统计被停止的src源通道deleteSrcIdBOs，查询一下，不使用的源bundle，就可以挂断；使用bundle但是不用某个channel了，就把channel给close
		//deleteSrcIdBOs：如果newSrcChannels里边有，那么一定要呼叫，不需要关闭；统计一下当前有多少在用，减去deleteSrcIdBOs里边的，可知是否还用，通道设计到的bundle也是\
		//统计哪些bundle关闭过的Map，防止重复处理
		Map<String, Integer> bundleIdMap = new HashMap<String, Integer>();
		for(IdBO deleteSrcIdBO : deleteSrcIdBOs){
			//TODO:如果newSrcChannels里边有，那么一定要呼叫，不需要关闭
			String deleteSrcBundleId = deleteSrcIdBO.getBundleId();
			if(bundleIdMap.containsKey(deleteSrcBundleId)){
				//已经关闭过，继续
				continue;
			}
			
			//统计当前是否还在使用（已经排除了newSrcChannels里边没有）从newAndExistedDispatchs统计即可
			TetrisDispatchPO existedDispatch = dispatchQueryUtil.queryDispatchPOBySrcBundleId(newAndExistedDispatchs, deleteSrcIdBO.getBundleId());
			if(existedDispatch == null){
				
//				//该srcBundle不用了//生成logic关闭bundle
				bundleIdMap.put(deleteSrcBundleId, 1);
				
				//先考虑整个bundle不使用的时候close，后续考虑对单个通道close
				BundlePO bundle = queryUtil.queryBundlePOByBundleId(bundlePOs, deleteSrcBundleId);
				
				if(bundle == null){
					//找不到该bundle，可能已被资源层删除
					log.warn("关闭源bundleId " + deleteSrcBundleId + " 无法从资源层查询到此bundle");
					continue;
				}
				
				DisconnectBundleBO disconnectBundleBO = new DisconnectBundleBO().setDisconnectBundle(bundle, audoCodec);
				logic.getDisconnectBundle().add(disconnectBundleBO);
			}
		}
		
		
		
		//后续考虑connect/disconnect的判重
		//统计被停止的dst目的deleteDstBundles
		for(TetrisDispatchPO deleteDstBundle : deleteDstBundles){
			BundlePO bundle = queryUtil.queryBundlePOByBundleId(bundlePOs, deleteDstBundle.getBundleId());
			if(bundle == null){
				//找不到该bundle，可能已被资源层删除
				log.warn("关闭目的bundleId " + deleteDstBundle.getBundleId() + " 无法从资源层查询到此bundle");
				continue;
			}
			
			DisconnectBundleBO disconnectBundleBO = new DisconnectBundleBO().setDisconnectBundle(bundle, audoCodec);
			logic.getDisconnectBundle().add(disconnectBundleBO);
		}
		
		executeBusiness.execute(logic, "调度");
		log.info("调度返回：" + JSON.toJSONString(dispatchResponseBO));
		
		return dispatchResponseBO;
		
	}
	
	public void passby(List<PassByBO> passbyBOs) throws Exception{
		LogicBO logic = new LogicBO().setPass_by(passbyBOs);
		executeBusiness.execute(logic, "透传");
	}
	
	/**
	 * 停止一个调度的公共处理方法<br/>
	 * <p>支持设备调度和用户调度</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午2:13:00
	 * @param dispatchPO 需要停止的调度
	 * @param existedDispatchs
	 * @param deleteDstBundles
	 * @param deleteSrcIdBOs
	 */
	private void stopDispatch(
			TetrisDispatchPO dispatchPO,
			List<TetrisDispatchPO> existedDispatchs,
			List<TetrisDispatchPO> deleteDstBundles,
			List<TetrisDispatchPO> deleteDstUsers,
			List<IdBO> deleteSrcIdBOs){
		
		if(dispatchPO == null) return;
		
		existedDispatchs.remove(dispatchPO);
		if(dispatchPO.getDispatchType().equals(DispatchType.BUNDLE_BUNDLE)){
			deleteDstBundles.add(dispatchPO);
		}else if(dispatchPO.getDispatchType().equals(DispatchType.BUNDLE_USER)){
			deleteDstUsers.add(dispatchPO);
		}
		
		//po的channels不能为null以进行遍历
		if(dispatchPO.getChannels() == null){
			dispatchPO.setChannels(new ArrayList<TetrisDispatchChannelPO>());
		}
		
		//统计这个bundle的源
		for(TetrisDispatchChannelPO channelPO : dispatchPO.getChannels()){
			if(channelPO.getSourceType() != null && channelPO.getSourceType().equals("channel")){
				//有源时需要统计处理
				if(!dispatchPO.isRecordOnly()){
					IdBO d = new IdBO().set(channelPO);
					deleteSrcIdBOs.add(d);
					log.info("停止调度id: " + dispatchPO.getId() + " 的src源也需要减少计数："
							+ " bundleId: " + channelPO.getSourceChannelId()
							+ " channelId: " + channelPO.getSourceChannelId());
				}
			}
		}//遍历完channelPO
	}
	
	private class IdBO {
		private String layerId;
		private String bundleId;
		private String channelId;
		public String getLayerId() {
			return layerId;
		}
		public void setLayerId(String layerId) {
			this.layerId = layerId;
		}
		public String getBundleId() {
			return bundleId;
		}
		public void setBundleId(String bundleId) {
			this.bundleId = bundleId;
		}
		public String getChannelId() {
			return channelId;
		}
		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}
		IdBO set(SourceParamBO src){
			if(src == null) return null;
			this.layerId = src.getLayerId();
			this.bundleId = src.getBundleId();
			this.channelId = src.getChannelId();
			return this;
		}
		IdBO set(TetrisDispatchChannelPO dstChannel){
			if(dstChannel == null) return null;
			this.layerId = dstChannel.getSourceLayerId();
			this.bundleId = dstChannel.getSourceBundleId();
			this.channelId = dstChannel.getSourceChannelId();
			return this;
		}
	}
	
}
