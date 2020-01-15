
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-service 上午10:33:24  
* All right reserved.  
*  
*/

package com.sumavision.bvc.monitor.logic.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.jms.Message;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.RequestBaseBO;
import com.suma.venus.message.bo.VenusMessageHead.MsgType;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.ChannelTemplateService;
import com.sumavision.bvc.BO.AccessLayerRequestBO;
import com.sumavision.bvc.BO.BundleInfo;
import com.sumavision.bvc.BO.BundleSchemeBO;
import com.sumavision.bvc.BO.ChannelInfo;
import com.sumavision.bvc.BO.ChannelSchemeBO;
import com.sumavision.bvc.BO.ChannelStatusBody;
import com.sumavision.bvc.BO.CloseChannelRequest;
import com.sumavision.bvc.BO.CombineVideoDecodeChannelParam;
import com.sumavision.bvc.BO.CombineVideoDecodeChannelParam.Src;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;
import com.sumavision.bvc.BO.HangupParam;
import com.sumavision.bvc.BO.HangupRequest;
import com.sumavision.bvc.BO.IncomingCallParam;
import com.sumavision.bvc.BO.IncomingCallRequest;
import com.sumavision.bvc.BO.OpenChannelRequest;
import com.sumavision.bvc.BO.OperateChannelParam;
import com.sumavision.bvc.BO.PassByBO;
import com.sumavision.bvc.BO.ResponseBody;
import com.sumavision.bvc.DTO.ResultMap;
import com.sumavision.bvc.common.CommonConstant.ChannelTypeInfo;
import com.sumavision.bvc.common.CommonConstant.OperateType;
import com.sumavision.bvc.utils.ThreadPool;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc: bvc-monitor-service
 * @author: cll
 * @createTime: 2018年6月6日 上午10:33:24
 * @history:
 * @version: v1.0
 */
@Slf4j
@Component
public class AcessLayerTask extends asyncTask {
	
	@Autowired
	private ResourseLayerTask resourseLayerTask;
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private ChannelSchemeService channelSchemeService;
	
	@Autowired
	private ChannelTemplateService channelTemplateService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.suma.bvc.monitor.logic.callback.asyncTask#execute()
	 */

	public void execute() {
		// TODO Auto-generated method stub

	}

	/*
	 * (非 Javadoc)
	 * 
	 * <p>处理消息队列回调</p>
	 * 
	 * 
	 * @param msg
	 * 
	 * @see com.suma.venus.message.mq.Callback#execute(javax.jms.Message)
	 * 
	 */
	@Override
	public void execute(Message msg) {

	}

	public Future<ResultMap> acessResource(List<AccessLayerRequestBO> accessLayerRequestList) {
		return null;
	}		
	
	/**
	 * 修改合屏解码通道
	 * @param decodeChannelToModify
	 * @return
	 */
//	private ResultMap modifyCombineVideoDecode(ChannelSchemeBO decodeChannelToModify){
//		try{
//			//解码通道
//			if(decodeChannelToModify.getSrc() != null && !decodeChannelToModify.getSrc().isEmpty()){
//				ModifyCombineVideoDecodeChannelRequest modifyChannelRequest = new ModifyCombineVideoDecodeChannelRequest();
//				CombineVideoDecodeChannelParam paramBO = new CombineVideoDecodeChannelParam();
//				List<Src> srcList = transFromSrcBO(decodeChannelToModify.getSrc());
//				
//				paramBO.setMethod(MethodContent.ModifyChannel.toString());
//				paramBO.setBundleID(decodeChannelToModify.getBundleId());
//				paramBO.setChannelID(decodeChannelToModify.getChannelId().toString());
//				paramBO.setInterval(decodeChannelToModify.getPosition().getPollingTime());
//				if(paramBO.getInterval() == 0){
//					paramBO.setChlType(ChannelType.Single.toString());
//				}else{
//					paramBO.setChlType(ChannelType.Loop.toString());
//				}
//				paramBO.setSeq(modifyChannelRequest.getSequence_id());
//				paramBO.setSrcArray(srcList);
//				
//				modifyChannelRequest.setSource_id(RegisterStatus.getNodeId());
//				modifyChannelRequest.setDestination_id(decodeChannelToModify.getLayerId());
//				modifyChannelRequest.setMessage_name("modify_channel_request");
//				modifyChannelRequest.setModify_channel_request(paramBO);
//				
//				ResponseBO responseBO = messageService.msgSend2SingleNodeAndWaitResp(modifyChannelRequest.getDestination_id(), modifyChannelRequest, 5000);
//				Integer result = null;
//				if(responseBO != null){
//					MessageRespBO messageRespBO = (MessageRespBO)responseBO.getResp();
//					if(messageRespBO != null){
//						result = messageRespBO.getResult();
//						if(messageRespBO.getResult() == new Integer(1)){
//							return ResultMap.ok();
//						}
//					}
//				}
//				log.error("modifyCombineVideoDecode failed " + decodeChannelToModify + " and result is " + result);
//			}else if(decodeChannelToModify.getPosition().getPollingStatus() != null && !decodeChannelToModify.getPosition().getPollingStatus().isEmpty()){
//				//todo 只修改pollingstatus时，src可能没有，暂时直接返回成功
//				return ResultMap.ok();
//			}
//			
//			
//		}catch(Exception e){
//			log.error("modifyCombineVideoDecode failed " + decodeChannelToModify, e);
//		}
//		return null;
//	}
	
	/**
	 * 修改一个合屏操作中的解码通道
	 * @param channelsToModify
	 * @return
	 */
//	private ResultMap modifyCombineVideoDecode(List<ChannelSchemeBO> channelsToModify){
//		List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
//		for(ChannelSchemeBO channel:channelsToModify){
//			results.add(CompletableFuture.supplyAsync(()->modifyCombineVideoDecode(channel)));
//		}
//		//这里的策略和打开合屏解码通道不一样，这里只要一个解码通道修改成功就算成功
//		boolean modifySuccess = false;
//		
//		for(Future<ResultMap> result:results){
//			ResultMap resultMap = null;
//			try {
//				resultMap = result.get();				
//			} catch (InterruptedException e) {
//				log.error("", e);
//			} catch (ExecutionException e) {
//				log.error("", e);
//			}
//			if(resultMap != null &&  new Integer(200).equals(resultMap.get("code"))){
//				modifySuccess = true;				
//			}
//		}
//		//合屏解码通道修改成功
//		if(modifySuccess){
//			return ResultMap.ok();
//		}
//		return null;
//	}
	
	/**
	 * 修改一组合屏操作
	 * @param uuid2ChannelUpdateMap
	 * @return 每个合屏修改操作uuid与成功与否的map
	 */
	public ResultMap updateCombineVideoOrAudio(Map<String, List<ChannelSchemeBO>> uuid2ChannelUpdateMap, ChannelTypeInfo type){
		ResultMap uuid2Result = new ResultMap();
		if(uuid2ChannelUpdateMap == null || uuid2ChannelUpdateMap.isEmpty()){
			return uuid2Result;
		}
		
		try{
			List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
			List<String> uuidList = new ArrayList<String>();
			for(String uuid:uuid2ChannelUpdateMap.keySet()){
				List<ChannelSchemeBO> channelSchemeBOs = uuid2ChannelUpdateMap.get(uuid);
				results.add(CompletableFuture.supplyAsync(()->openCombineVideoOrAudioEncode(channelSchemeBOs.get(0),channelSchemeBOs.subList(1, channelSchemeBOs.size()), type, 0),
						ThreadPool.getScheduledExecutor()));
				uuidList.add(uuid);					
			}
			
			int i = 0;
			for(Future<ResultMap> result:results){
				ResultMap resultMap = result.get();
				if(resultMap != null && new Integer(200).equals(resultMap.get("code"))){
					uuid2Result.put(uuidList.get(i), true);
				}else{
					uuid2Result.put(uuidList.get(i), false);
				}
				i++;
			}
		}catch(Exception e){
			log.error("modifyCombineVideoDecode failed", e);
		}
		return uuid2Result;
	}
	
	/**
	 * 打开合屏编码通道
	 * @param encodeChannelToOpen 待打开的合屏编码
	 * @param decodeChannelsHasOpen 已打开的合屏解码
	 * @return
	 */
	private ResultMap openCombineVideoOrAudioEncode(ChannelSchemeBO encodeChannelToOpen, List<ChannelSchemeBO> decodeChannelsHasOpen, ChannelTypeInfo type, int operateIndex) {
		try{
			//编码通道
			if(encodeChannelToOpen != null){
				OpenChannelRequest openChannelRequest = new OpenChannelRequest();
				
				OperateChannelParam paramBO = new OperateChannelParam();
				openChannelRequest.getMessage().getMessage_body().put("open_channel_request", paramBO);
				BundleInfo bundleInfo = new BundleInfo();
				paramBO.setBundle(bundleInfo);
				
				bundleInfo.setBundle_id(encodeChannelToOpen.getBundleId());				
				bundleInfo.setBundle_type(bundleService.findByBundleId(encodeChannelToOpen.getBundleId()).getBundleType());				
				ChannelInfo channel = new ChannelInfo();
				bundleInfo.setChannel(channel);
				
				channel.setChannel_id(encodeChannelToOpen.getChannelId().toString());
				ChannelSchemePO channelSchemePO = channelSchemeService.findByBundleIdAndChannelId(encodeChannelToOpen.getBundleId(), encodeChannelToOpen.getChannelId());
				channel.setChannel_name(channelSchemePO.getChannelName());
				channel.setOperate_index(operateIndex);
				
				channel.setChannel_param(encodeChannelToOpen.getChannelParam());												
								
								
				openChannelRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
				openChannelRequest.getMessage().getMessage_header().setDestination_id(encodeChannelToOpen.getLayerId());
				openChannelRequest.getMessage().getMessage_header().setMessage_name("open_channel");
				
				messageService.msgSend2SingleNode(openChannelRequest.getMessage().getMessage_header().getDestination_id(), JSONObject.toJSONString(openChannelRequest));
				/*log.info("----------------------openCombineVideoOrAudioEncode result is " + (responseBO==null?null:JSONObject.toJSONString(responseBO)));				
				String result = null;
				String err_desc = null;
				if(responseBO != null && responseBO.getMessage().getMessage_body() != null){
					ResponseBody respBO = responseBO.getMessage().getMessage_body().getObject("open_channel_response", ResponseBody.class);
					if(respBO != null){
						result = respBO.getResult();
						if("success".equals(respBO.getResult())){
							log.info("openCombineVideoOrAudioEncode success " + encodeChannelToOpen);
							return ResultMap.ok();
						}else{
							err_desc = respBO.getError_description();
						}
					}
				}
				log.error("openCombineVideoOrAudioEncode failed " + encodeChannelToOpen + " and result is " + result + " , err_desc is " + err_desc);*/
			}
			
			
		}catch(Exception e){
			log.error("openCombineVideoOrAudioEncode failed " + encodeChannelToOpen, e);
		}
		return null;
	}
	
	/**
	 * 打开合屏解码通道
	 * @param decodeChannelToOpen
	 * @return
	 */
//	private ResultMap openCombineVideoDecode(ChannelSchemeBO decodeChannelToOpen) {
//		try{
//			//解码通道
//			if(decodeChannelToOpen.getSrc() != null && !decodeChannelToOpen.getSrc().isEmpty()){
//				OpenCombineVideoDecodeChannelRequest openChannelRequest = new OpenCombineVideoDecodeChannelRequest();
//				CombineVideoDecodeChannelParam paramBO = new CombineVideoDecodeChannelParam();
//				List<Src> srcList = transFromSrcBO(decodeChannelToOpen.getSrc());
//				
//				paramBO.setMethod(MethodContent.OpenChannel.toString());
//				paramBO.setBundleID(decodeChannelToOpen.getBundleId());
//				paramBO.setChannelID(decodeChannelToOpen.getChannelId().toString());
//				paramBO.setInterval(decodeChannelToOpen.getPosition().getPollingTime());
//				if(paramBO.getInterval() == 0){
//					paramBO.setChlType(ChannelType.Single.toString());
//				}else{
//					paramBO.setChlType(ChannelType.Loop.toString());
//				}
//				paramBO.setSeq(openChannelRequest.getSequence_id());
//				paramBO.setSrcArray(srcList);
//				
//				openChannelRequest.setSource_id(RegisterStatus.getNodeId());
//				openChannelRequest.setDestination_id(decodeChannelToOpen.getLayerId());
//				openChannelRequest.setMessage_name("open_channel_request");
//				openChannelRequest.setOpen_channel_request(paramBO);
//				
//				ResponseBO responseBO = messageService.msgSend2SingleNodeAndWaitResp(openChannelRequest.getDestination_id(), openChannelRequest, 5000);
//				Integer result = null;
//				if(responseBO != null){
//					MessageRespBO messageRespBO = (MessageRespBO)responseBO.getResp();
//					if(messageRespBO != null){
//						result = messageRespBO.getResult();
//						if(messageRespBO.getResult() == new Integer(1)){
//							return ResultMap.ok();
//						}
//					}
//				}
//				log.error("openCombineVideoDecode failed " + decodeChannelToOpen + " and result is " + result);
//			}
//			
//			
//		}catch(Exception e){
//			log.error("openCombineVideoDecode failed " + decodeChannelToOpen, e);
//		}
//		return null;
//	}
	
	/**
	 * 打开一个合屏操作中的所有通道(解码(不用打开了)和编码)
	 * @param channelsToOpen
	 * @param userId
	 * @return
	 */
	private ResultMap openCombineVideoOrAudio(List<ChannelSchemeBO> channelsToOpen, Long userId,ChannelTypeInfo type, int operateIndex){
		List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
		List<ChannelSchemeBO> decodeChannels = new ArrayList<ChannelSchemeBO>();
		if(channelsToOpen.size() > 1){			
			decodeChannels = channelsToOpen.subList(1, channelsToOpen.size());
		}
		ChannelSchemeBO encodeChannelToOpen = channelsToOpen.get(0);
		boolean openSuccess = true;
		/*for(ChannelSchemeBO channel:decodeChannelsToOpen){
			results.add(CompletableFuture.supplyAsync(()->openCombineVideoDecode(channel)));
		}
		int i = 0;
		List<ChannelSchemeBO> channelsToClose = new ArrayList<ChannelSchemeBO>();
		for(Future<ResultMap> result:results){
			ResultMap resultMap = null;
			try {
				resultMap = result.get();				
			} catch (InterruptedException e) {
				log.error("", e);
			} catch (ExecutionException e) {
				log.error("", e);
			}
			if(resultMap == null || !new Integer(200).equals(resultMap.get("code"))){
				openSuccess = false;				
			}else{
				channelsToClose.add(channelsToOpen.get(i));
			}
			i++;
		}*/
		//合屏解码通道不用打开了，进行打开合屏编码通道操作
		if(openSuccess){
			ResultMap resultMap = openCombineVideoOrAudioEncode(encodeChannelToOpen, decodeChannels, type, operateIndex);
			if(resultMap == null || !new Integer(200).equals(resultMap.get("code"))){
				//todo 回滚关闭已经打开成功的合屏解码通道
//				rollbackToCloseDecodeChannels(decodeChannelsToOpen);
				//回滚调用资源层接口释放通道
//				resourseLayerTask.unlockResource(/*channelsToOpen*/channelsToOpen.subList(0, 1), userId, true);
			}else{
				return ResultMap.ok();
			}
		}/*else{
			//todo 回滚关闭已经打开成功的合屏解码通道
			rollbackToCloseDecodeChannels(channelsToClose);
		}*/
		return null;
	}
	
	/**
	 * 打开通道，包括打开解码通道(不用打开了)、打开编码通道
	 * @param uuid2ChannelSelMap
	 * @param lockResultMap
	 * @param userId
	 * @return 操作uuid/taskId与成功与否的map
	 */
	public ResultMap openCombineVideoOrAudio(Map<String, List<ChannelSchemeBO>> uuid2ChannelSelMap, ResultMap lockResultMap, Long userId, ChannelTypeInfo type){
		ResultMap uuid2Result = new ResultMap();
		if(uuid2ChannelSelMap == null || uuid2ChannelSelMap.isEmpty()){
			return uuid2Result;
		}
		
		try{
			List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
			List<String> uuidList = new ArrayList<String>();
			for(String uuid:uuid2ChannelSelMap.keySet()){
				Boolean lockSuccess = ((ChannelStatusBody)lockResultMap.get(uuid)).isResult();
				int operateIndex = ((ChannelStatusBody)lockResultMap.get(uuid)).getOperateIndex();
				if(lockSuccess != null && lockSuccess){
					List<ChannelSchemeBO> channelSchemeBOs = uuid2ChannelSelMap.get(uuid);
					results.add(CompletableFuture.supplyAsync(()->openCombineVideoOrAudio(channelSchemeBOs, userId, type, operateIndex), ThreadPool.getScheduledExecutor()));
					uuidList.add(uuid);					
				}
			}
			
			int i = 0;
			for(Future<ResultMap> result:results){
				ResultMap resultMap = result.get();
				if(resultMap != null && new Integer(200).equals(resultMap.get("code"))){
					uuid2Result.put(uuidList.get(i), true);
				}else{
					uuid2Result.put(uuidList.get(i), false);
				}
				i++;
			}
		}catch(Exception e){
			log.error("openCombineVideo failed", e);
		}
		return uuid2Result;
	}
	
	/**
	 * 打开合屏解码通道
	 * @param decodeChannelToOpen
	 * @return
	 */
//	private ResultMap closeCombineVideo(ChannelSchemeBO channelToClose) {
//		try{
//			if(channelToClose.getCombineType() == ChannelTypeInfo.VIDEOENCODE){
//				CloseCombineVideoEncodeChannelRequest closeChannelRequest = new CloseCombineVideoEncodeChannelRequest();
//				CombineVideoEncodeChannelParam paramBO = new CombineVideoEncodeChannelParam();
//				
//				paramBO.setMethod(MethodContent.CloseChannel.toString());
//				paramBO.setBundleID(channelToClose.getBundleId());
//				paramBO.setChannelID(channelToClose.getChannelId().toString());
//				
//				paramBO.setSeq(closeChannelRequest.getSequence_id());
//				
//				closeChannelRequest.setSource_id(RegisterStatus.getNodeId());
//				closeChannelRequest.setDestination_id(channelToClose.getLayerId());
//				closeChannelRequest.setMessage_name("close_channel_request");
//				closeChannelRequest.setClose_channel_request(paramBO);
//				
//				ResponseBO responseBO = messageService.msgSend2SingleNodeAndWaitResp(closeChannelRequest.getDestination_id(), closeChannelRequest, 5000);
//				Integer result = null;
//				if(responseBO != null){
//					MessageRespBO messageRespBO = (MessageRespBO)responseBO.getResp();
//					if(messageRespBO != null){
//						result = messageRespBO.getResult();
//						if(messageRespBO.getResult() == new Integer(1)){
//							return ResultMap.ok();
//						}
//					}
//				}
//				log.error("closeCombineVideo failed " + channelToClose + " and result is " + result);
//			}else if(channelToClose.getCombineType() == ChannelTypeInfo.VIDEODECODE){
//				CloseCombineVideoDecodeChannelRequest closeChannelRequest = new CloseCombineVideoDecodeChannelRequest();
//				CombineVideoDecodeChannelParam paramBO = new CombineVideoDecodeChannelParam();
//				
//				paramBO.setMethod(MethodContent.CloseChannel.toString());
//				paramBO.setBundleID(channelToClose.getBundleId());
//				paramBO.setChannelID(channelToClose.getChannelId().toString());
//				
//				paramBO.setSeq(closeChannelRequest.getSequence_id());
//				
//				closeChannelRequest.setSource_id(RegisterStatus.getNodeId());
//				closeChannelRequest.setDestination_id(channelToClose.getLayerId());
//				closeChannelRequest.setMessage_name("close_channel_request");
//				closeChannelRequest.setClose_channel_request(paramBO);
//				
//				ResponseBO responseBO = messageService.msgSend2SingleNodeAndWaitResp(closeChannelRequest.getDestination_id(), closeChannelRequest, 5000);
//				Integer result = null;
//				if(responseBO != null){
//					MessageRespBO messageRespBO = (MessageRespBO)responseBO.getResp();
//					if(messageRespBO != null){
//						result = messageRespBO.getResult();
//						if(messageRespBO.getResult() == new Integer(1)){
//							return ResultMap.ok();
//						}
//					}
//				}
//				log.error("closeCombineVideo failed " + channelToClose + " and result is " + result);
//			}
//			
//			
//			
//		}catch(Exception e){
//			log.error("closeCombineVideo failed " + channelToClose, e);
//		}
//		return null;
//	}
	
	
	/**
	 * 关闭合屏/混音编码通道
	 * @param channelsToClose
	 * @param userId
	 * @return
	 */
	public ResultMap closeCombineVideoOrAudio(List<ChannelSchemeBO> channelsToClose, List<Integer> operateIndexList){
		ResultMap taskId2Result = new ResultMap();
		if(channelsToClose == null || channelsToClose.isEmpty()){
			return taskId2Result;
		}
		
		try{
			log.info("operateIndexList is " + operateIndexList + " and channelsToClose size is " + channelsToClose.size());
			List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
			List<String> taskIdList = new ArrayList<String>();
			int j = 0;
			for(ChannelSchemeBO channel:channelsToClose){
				int operateIndex = operateIndexList.get(j);
				results.add(CompletableFuture.supplyAsync(()->operateChannel(channel, operateIndex, null, OperateType.CLOSE),
						ThreadPool.getScheduledExecutor()));
				taskIdList.add(channel.getTaskId());
				j++;
			}
			
			int i = 0;
			for(Future<ResultMap> result:results){
				ResultMap resultMap = result.get();
				if(resultMap != null && new Integer(200).equals(resultMap.get("code"))){
					taskId2Result.put(taskIdList.get(i), true);
				}else{
					taskId2Result.put(taskIdList.get(i), false);
				}
				i++;
			}
		}catch(Exception e){
			log.error("closeCombineVideo failed", e);
		}
		return taskId2Result;
	}
	
	/**
	 * 操作通道
	 * @param channelToOperate
	 * @param userId
	 * @param operate
	 * @return
	 */
	public ResultMap operateChannel(ChannelSchemeBO channelToOperate, int operateIndex, Long userId, OperateType operate){
		try{
			RequestBaseBO requestBO = null;
			if(OperateType.OPEN == operate){
				OperateChannelParam param = new OperateChannelParam();
				
				BundleInfo bundleInfo = new BundleInfo();
				param.setBundle(bundleInfo);
				
				bundleInfo.setBundle_id(channelToOperate.getBundleId());			
				bundleInfo.setBundle_type(bundleService.findByBundleId(channelToOperate.getBundleId()).getBundleType());
				ChannelInfo channel = new ChannelInfo();
				bundleInfo.setChannel(channel);
				
				channel.setChannel_id(channelToOperate.getChannelId().toString());
				ChannelSchemePO channelSchemePO = channelSchemeService.findByBundleIdAndChannelId(channelToOperate.getBundleId(), channelToOperate.getChannelId());
				channel.setChannel_name(channelSchemePO.getChannelName());
				channel.setOperate_index(operateIndex);
//				JSONObject channel_param = new JSONObject();
				channel.setChannel_param(channelToOperate.getChannelParam());
				/*if(StringUtils.isEmpty(channelToOperate.getBaseType())){
					channel_param.put("base_type", channelTemplateService.get(channelSchemePO.getChannelTemplateID()).getBaseType());					
				}else{				
					channel_param.put("base_type", channelToOperate.getBaseType());
				}
				if(channelToOperate.getBaseType().contains("Video")){
					channel_param.put("base_param", channelToOperate.getChannelParam().getJSONObject("video_param"));
				}else{
					channel_param.put("base_param", channelToOperate.getChannelParam().getJSONObject("audio_param"));				
				}
				
				if(channelToOperate.getSrc() != null && !channelToOperate.getSrc().isEmpty()){
					SrcBO srcBO = channelToOperate.getSrc().get(0);
					JSONObject source = new JSONObject();
					channel_param.getJSONObject("base_param").put("source", source);
					source.put("layer_id", srcBO.getLayerId());
					source.put("bundle_id",srcBO.getBundleId());
					source.put("channel_id", srcBO.getChannelId());
				}*/
				
				OpenChannelRequest openChannelRequest = new OpenChannelRequest();			
				
				openChannelRequest.getMessage().getMessage_header().setDestination_id(channelToOperate.getLayerId());
				openChannelRequest.getMessage().getMessage_header().setMessage_name("open_channel");
				openChannelRequest.getMessage().getMessage_body().put("open_channel_request", param);
				openChannelRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
				requestBO = openChannelRequest;
			}/*else if(OperateType.MODIFY == operate){
				ModifyChannelRequest modifyChannelRequest = new ModifyChannelRequest();
				
				modifyChannelRequest.setDstID(channelToOperate.getLayerId());
				modifyChannelRequest.setMsgName("modify_channel_request");
				modifyChannelRequest.setModify_channel_request(param);
				modifyChannelRequest.setSrcID(RegisterStatus.getNodeId());
				requestBO = modifyChannelRequest;
			}*/else if(OperateType.CLOSE == operate){
				CloseChannelRequest closeChannelRequest = new CloseChannelRequest();
				
				closeChannelRequest.getMessage().getMessage_header().setDestination_id(channelToOperate.getLayerId());
				closeChannelRequest.getMessage().getMessage_header().setMessage_name("close_channel");
				closeChannelRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
				
				JSONObject closeChannelParam = new JSONObject();
				closeChannelRequest.getMessage().getMessage_body().put("close_channel_request",closeChannelParam);
				closeChannelParam.put("operate_index", operateIndex);
				JSONObject bundle = new JSONObject();
				closeChannelParam.put("bundle", bundle);
				bundle.put("bundle_id", channelToOperate.getBundleId());
				bundle.put("channel_id", channelToOperate.getChannelId());
				
				requestBO = closeChannelRequest;
			}
			log.info("-----------------start " + operate + " task with bundleId " + channelToOperate.getBundleId() + " and channelId " + channelToOperate.getChannelId() + 
					" and accessNodeId " + channelToOperate.getLayerId() + " " + new Date());
			messageService.msgSend2SingleNode(requestBO.getMessage().getMessage_header().getDestination_id(), JSONObject.toJSONString(requestBO));
			log.info("-----------------end " + operate + " task with bundleId " + channelToOperate.getBundleId() + " and channelId " + channelToOperate.getChannelId() + " " + new Date());
			/*log.info("----------------------operatechannel result is " + (responseBO==null?null:JSONObject.toJSONString(responseBO)));
			String result = "";
			boolean operateSuccess = false;
			if(responseBO != null && responseBO.getMessage().getMessage_body() != null){
				ResponseBody respBO = null;
				if(OperateType.OPEN == operate){					
					respBO = responseBO.getMessage().getMessage_body().getObject("open_channel_response", ResponseBody.class);
				}else if(OperateType.CLOSE == operate){
					respBO = responseBO.getMessage().getMessage_body().getObject("close_channel_response", ResponseBody.class);
				}
				if(respBO != null){
					if("success".equals(respBO.getResult())){
						operateSuccess = true;
					}else{
						operateSuccess = false;
						result += respBO.getError_description();
					}
				}
			}
			//操作成功
			if(operateSuccess && result.isEmpty()){
				log.info("operateChannel " + operate.toString() + " success " + channelToOperate);
				return ResultMap.ok();
			}else{
				if(OperateType.OPEN == operate){
					log.warn("becauseof openchannel failed " + channelToOperate + " so back to unlock this channel!");
					//回滚调用资源层接口释放通道
					List<ChannelSchemeBO> channelsToUnlock = new ArrayList<ChannelSchemeBO>();
					channelsToUnlock.add(channelToOperate);
					resourseLayerTask.unlockResource(channelsToUnlock, userId, true);
				}
				
			}
			log.info("operateChannel " + operate.toString() + " failed " + channelToOperate + " and result is " + result);*/
		}catch(Exception e){
			log.error("operateChannel " + operate.toString() +  " failed " + channelToOperate, e);
		}
		return null;
	}
	
	/**
	 * 操作通道
	 * @param channelsToOperate
	 * @param userId
	 * @param operate
	 * @return
	 */
	public ResultMap operateChannel(List<ChannelSchemeBO> channelsToOperate, List<Integer> operateIndexList , Long userId, OperateType operate){
		ResultMap taskId2Result = new ResultMap();
		if(channelsToOperate == null || channelsToOperate.isEmpty()){
			return taskId2Result;
		}
		
		try{
			log.info("operateIndexList is " + operateIndexList + " and channelsToOperate size is " + channelsToOperate.size());
			List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
			List<String> taskIdList = new ArrayList<String>();
			int j = 0;
			for(ChannelSchemeBO channel:channelsToOperate){
				int operateIndex = operateIndexList.get(j);
				if(channel.getChannelParam() == null){
					log.info("-------------this channel has no channelParam" + " bundleId is " + channel.getBundleId() + " and channelId is " + channel.getChannelId());
					continue;
				}
				log.info("-----------------add " +  operate + " task with bundleId " + channel.getBundleId() + " and channelId " + channel.getChannelId() + " " + new Date());
				results.add(CompletableFuture.supplyAsync(()->operateChannel(channel, operateIndex, userId, operate), ThreadPool.getScheduledExecutor()));
				taskIdList.add(channel.getTaskId());
				j++;
			}
			
			int i = 0;
			for(Future<ResultMap> result:results){
				ResultMap resultMap = result.get();
				if(resultMap != null && new Integer(200).equals(resultMap.get("code"))){
					taskId2Result.put(taskIdList.get(i), true);
				}else{
					taskId2Result.put(taskIdList.get(i), false);
				}
				i++;
			}
		}catch(Exception e){
			log.error("openOrModifyChannel failed", e);
		}
		return taskId2Result;
		
		
		
	}
	
	/**
	 * 操作bundle
	 * @param bundleToOperate
	 * @param userId
	 * @param operate
	 * @return
	 */
	private ResultMap operateBundle(BundleSchemeBO bundleToOperate, int operateIndex, Long userId, OperateType operate){
		try{
			RequestBaseBO requestBO = null;
			if(OperateType.OPEN == operate){
				//这里打开bundle也利用原来的打开channel的类
				OperateChannelParam param = new OperateChannelParam();
				
				BundleInfo bundleInfo = new BundleInfo();
				param.setBundle(bundleInfo);
				
				bundleInfo.setBundle_id(bundleToOperate.getBundleId());
//				bundleInfo.setBundle_type(bundleService.findByBundleId(bundleToOperate.getBundleId()).getBundleType());
				bundleInfo.setBundle_type(bundleToOperate.getBase_type());
				bundleInfo.setDevice_model(bundleToOperate.getDevice_model());
				bundleInfo.setOperate_index(operateIndex);
				bundleInfo.setScreens(bundleToOperate.getScreens());
				if(bundleToOperate.getPass_by_str() != null){					
					bundleInfo.setPass_by_str(bundleToOperate.getPass_by_str().toJSONString());
				}
				List<ChannelInfo> channels = new ArrayList<ChannelInfo>();
				if(bundleToOperate.getChannels() != null){
					for(ChannelSchemeBO channelSchemeBO:bundleToOperate.getChannels()){
						ChannelInfo channel = new ChannelInfo();
						channel.setChannel_id(channelSchemeBO.getChannelId());
						channel.setChannel_status(channelSchemeBO.getChannel_status());
						channel.setChannel_param(channelSchemeBO.getChannelParam());
						channels.add(channel);
					}
				}
				if(!channels.isEmpty()){					
					bundleInfo.setChannels(channels);
				}
				
				OpenChannelRequest openChannelRequest = new OpenChannelRequest();			
				
				openChannelRequest.getMessage().getMessage_header().setDestination_id(bundleToOperate.getLayerId());
				openChannelRequest.getMessage().getMessage_header().setMessage_name("open_bundle");
				openChannelRequest.getMessage().getMessage_body().put("open_bundle_request", param);
				openChannelRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
				requestBO = openChannelRequest;
			}else if(OperateType.CLOSE == operate){
				//这里关闭bundle也利用原来关闭channel的类
				CloseChannelRequest closeChannelRequest = new CloseChannelRequest();
				
				closeChannelRequest.getMessage().getMessage_header().setDestination_id(bundleToOperate.getLayerId());
				closeChannelRequest.getMessage().getMessage_header().setMessage_name("close_bundle");
				closeChannelRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
				
				JSONObject closeChannelParam = new JSONObject();
				closeChannelRequest.getMessage().getMessage_body().put("close_bundle_request",closeChannelParam);
				closeChannelParam.put("operate_index", operateIndex);
				JSONObject bundle = new JSONObject();
				closeChannelParam.put("bundle", bundle);
				bundle.put("bundle_id", bundleToOperate.getBundleId());
				bundle.put("bundle_type", bundleToOperate.getBase_type());
				bundle.put("device_model", bundleToOperate.getDevice_model());
				//pass_by_str参数暂时不发，等确定有再发
				/*if(bundleToOperate.getPass_by_str() != null){					
					bundle.put("pass_by_str", bundleToOperate.getPass_by_str().toJSONString());
				}*/
				
				requestBO = closeChannelRequest;
			}
			log.info("-----------------start " + operate + " bundleTask with bundleId " + bundleToOperate.getBundleId() + 
					" and accessNodeId " + bundleToOperate.getLayerId());
			messageService.msgSend2SingleNode(requestBO.getMessage().getMessage_header().getDestination_id(), JSONObject.toJSONString(requestBO));
			log.info("-----------------end " + operate + " bundleTask with bundleId " + bundleToOperate.getBundleId());
		}catch(Exception e){
			log.error("operateBundle " + operate.toString() +  " failed " + bundleToOperate, e);
		}
		return null;
	}
	
	/**
	 * 操作bundle
	 * @param bundlesToOperate
	 * @param userId
	 * @param operate
	 * @return
	 */
	public ResultMap operateBundle(List<BundleSchemeBO> bundlesToOperate, List<Integer> operateIndexList , Long userId, OperateType operate){
		ResultMap taskId2Result = new ResultMap();
		if(bundlesToOperate == null || bundlesToOperate.isEmpty()){
			return taskId2Result;
		}
		
		try{
			log.info("operateIndexList is " + operateIndexList + " and bundlesToOperate size is " + bundlesToOperate.size());
			List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
			List<String> taskIdList = new ArrayList<String>();
			int j = 0;
			for(BundleSchemeBO bundle:bundlesToOperate){
				int operateIndex = operateIndexList.get(j);
				if((bundle.getChannels() == null || bundle.getChannels().isEmpty()) && operate == OperateType.OPEN){
					log.info("-------------this bundle has no channels" + " bundleId is " + bundle.getBundleId());
					continue;
				}
				log.info("-----------------add " +  operate + " bundleTask with bundleId " + bundle.getBundleId());
				results.add(CompletableFuture.supplyAsync(()->operateBundle(bundle, operateIndex, userId, operate), ThreadPool.getScheduledExecutor()));
				taskIdList.add(bundle.getTaskId());
				j++;
			}
			
			int i = 0;
			for(Future<ResultMap> result:results){
				ResultMap resultMap = result.get();
				if(resultMap != null && new Integer(200).equals(resultMap.get("code"))){
					taskId2Result.put(taskIdList.get(i), true);
				}else{
					taskId2Result.put(taskIdList.get(i), false);
				}
				i++;
			}
		}catch(Exception e){
			log.error("openOrClose bundle failed", e);
		}
		return taskId2Result;
		
		
		
	}
	private ResultMap sendIncomingCall(IncomingCallParam incomingCall){
		try{
			IncomingCallRequest incomingCallRequest = new IncomingCallRequest();
			
			incomingCallRequest.getMessage().getMessage_body().put("incoming_call_request", incomingCall);
			incomingCallRequest.getMessage().getMessage_header().setDestination_id(incomingCall.getCallee_layer_id());
			incomingCallRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
			incomingCallRequest.getMessage().getMessage_header().setMessage_type(MsgType.passby.toString());
			incomingCallRequest.getMessage().getMessage_header().setMessage_name(MsgType.passby.toString());
			incomingCallRequest.getMessage().getMessage_header().setSequence_id(incomingCall.getUuid());
			
			messageService.msgSend2SingleNode(incomingCallRequest.getMessage().getMessage_header().getDestination_id(), JSONObject.toJSONString(incomingCallRequest));
			/*log.info("----------------------sendIncomingCall result is " + (responseBO==null?null:JSONObject.toJSONString(responseBO)));
			if(responseBO != null){				
				IncomingCallRespParam resp = responseBO.getMessage().getMessage_body().getObject("incoming_call_response",IncomingCallRespParam.class);
				if(resp != null && "true".equals(resp.getAccept())){
					log.info("incoming succeed " + resp);
					return ResultMap.ok();
				}
			}*/
		}catch(Exception e){
			log.error("sendI failed", e);
		}
		return null;
	}
	
	private int sendHangup(HangupParam hangup){
		try{
			HangupRequest hangupRequest = new HangupRequest();
			hangupRequest.getMessage().getMessage_body().put("hang_up_request", hangup);
			hangupRequest.getMessage().getMessage_header().setDestination_id(hangup.getCallee_layer_id());
			hangupRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
			hangupRequest.getMessage().getMessage_header().setMessage_type(MsgType.passby.toString());			
			hangupRequest.getMessage().getMessage_header().setMessage_name(MsgType.passby.toString());
			
			messageService.msgSend2SingleNode(hangupRequest.getMessage().getMessage_header().getDestination_id(), JSONObject.toJSONString(hangupRequest));
			return 1;
		}catch(Exception e){
			log.error("sendHangup failed", e);
		}
		return 0;
	}
	
	public int sendPassBy(PassByBO passby){
		try{
			HangupRequest passByRequest = new HangupRequest();
			passByRequest.getMessage().getMessage_body().put("pass_by", passby);
			passByRequest.getMessage().getMessage_header().setDestination_id(passby.getLayer_id());
			passByRequest.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
			passByRequest.getMessage().getMessage_header().setMessage_type(MsgType.passby.toString());			
			passByRequest.getMessage().getMessage_header().setMessage_name(MsgType.passby.toString());
			
			log.info("-----------------start pass_by task with bundleId " + passby.getBundle_id() + 
					" and accessNodeId " + passby.getLayer_id() + " " + new Date());
			messageService.msgSend2SingleNode(passByRequest.getMessage().getMessage_header().getDestination_id(), JSONObject.toJSONString(passByRequest));
			log.info("-----------------end pass_by task with bundleId " + passby.getBundle_id() + " and accessNodeId " + passby.getLayer_id() + " " + new Date());
			return 1;
		}catch(Exception e){
			log.error("sendPassBy failed", e);
		}
		return 0;
	}
	
	public void sendPassBys(List<PassByBO> passbys){
		if(passbys == null || passbys.isEmpty()){
			return;
		}
		
		try{
			for(PassByBO passby:passbys){
				CompletableFuture.supplyAsync(()->sendPassBy(passby), ThreadPool.getScheduledExecutor());
			}
		}catch(Exception e){
			log.error("sendPassBys failed", e);
		}
	}
	
	public void sendHangUps(List<HangupParam> hangups){
		if(hangups == null || hangups.isEmpty()){
			return;
		}
		
		try{
			for(HangupParam hangup:hangups){
				CompletableFuture.supplyAsync(()->sendHangup(hangup), ThreadPool.getScheduledExecutor());
			}
		}catch(Exception e){
			log.error("sendHangUps failed", e);
		}
	}
	
	public ResultMap sendIncomingCalls(List<IncomingCallParam> incomingCalls){
		ResultMap uuid2Result = new ResultMap();
		if(incomingCalls == null || incomingCalls.isEmpty()){
			return uuid2Result;
		}
		
		try{
			List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
			List<String> uuidList = new ArrayList<String>();
			for(IncomingCallParam incomingCall:incomingCalls){
				results.add(CompletableFuture.supplyAsync(()->sendIncomingCall(incomingCall), ThreadPool.getScheduledExecutor()));
				uuidList.add(incomingCall.getUuid());
			}
			
			int i = 0;
			for(Future<ResultMap> result:results){
				ResultMap resultMap = result.get();
				if(resultMap != null && new Integer(200).equals(resultMap.get("code"))){
					uuid2Result.put(uuidList.get(i), true);
				}else{
					uuid2Result.put(uuidList.get(i), false);
				}
				i++;
			}
		}catch(Exception e){
			log.error("sendIncomingCalls failed", e);
		}
		return uuid2Result;
	}	    

	
	private List<Src> transFromSrcBO(List<SrcBO> srcBOs){
		List<Src> srcList = new ArrayList<CombineVideoDecodeChannelParam.Src>();
		if(srcBOs == null || srcBOs.isEmpty()){
			return srcList;
		}
		
		for(SrcBO srcBO:srcBOs){
			Src src = new Src();
			src.setBundleID(srcBO.getBundleId());
			src.setChannelID(srcBO.getChannelId());
			src.setLayerID(srcBO.getLayerId());
			srcList.add(src);
		}
		return srcList;
	}
	
	
}
