
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-service 上午10:32:54  
* All right reserved.  
*  
*/

package com.sumavision.bvc.monitor.logic.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.jms.Message;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.resource.base.bo.BatchLockBundleParam;
import com.suma.venus.resource.base.bo.BatchLockBundleRespBody;
import com.suma.venus.resource.base.bo.BatchLockBundleRespParam;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.base.bo.ChannelStatusParam;
import com.suma.venus.resource.base.bo.ErrorDescription;
import com.suma.venus.resource.base.bo.LockBundleParam;
import com.suma.venus.resource.base.bo.LockBundleRespParam;
import com.suma.venus.resource.base.bo.LockChannelParam;
import com.suma.venus.resource.base.bo.LockChannelRespParam;
import com.suma.venus.resource.base.bo.ReleaseChannelRespParam;
import com.suma.venus.resource.base.bo.ScreenBody;
import com.sumavision.bvc.BO.BundleSchemeBO;
import com.sumavision.bvc.BO.ChannelSchemeBO;
import com.sumavision.bvc.BO.ChannelStatusBody;
import com.sumavision.bvc.BO.LockChannelRequest;
import com.sumavision.bvc.BO.ReleaseChannelRequest;
import com.sumavision.bvc.BO.ResourceLayerRequestBO;
import com.sumavision.bvc.DO.DO;
import com.sumavision.bvc.DO.ResourceDO;
import com.sumavision.bvc.DTO.ResultMap;
import com.sumavision.bvc.common.CommonConstant.ChannelTypeInfo;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.utils.ThreadPool;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc: bvc-monitor-service
 * @author: cll
 * @createTime: 2018年6月6日 上午10:32:54
 * @history:
 * @version: v1.0
 */
@Slf4j
@Component
public class ResourseLayerTask extends asyncTask {

	@Autowired
	private ResourceServiceClient resourceServiceClient;

	/*
	 * (non-Javadoc) 申请资源
	 */

	public Future<List<ResourceDO>> applyResource(DO bussinessBO) {
		return CompletableFuture.supplyAsync(() -> send2Resource(bussinessBO), ThreadPool.getScheduledExecutor());
	}

	private List<ResourceDO> send2Resource(DO bussinessBO) {

		return null;
	}
	
	/*
	 * 是否计数
	 */
	private boolean whetherOperateCount(BundleSchemeBO bundleSchemeBO) {
		if("vod".equals(bundleSchemeBO.getBusinessType())){
			if("start".equals(bundleSchemeBO.getOperateType()) || "stop".equals(bundleSchemeBO.getOperateType())){
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc) 锁定资源
	 */
	public ResultMap lockResource(ChannelSchemeBO channelSchemeBO, Long userId) {
		try{			
			LockChannelRequest lockChannelRequest = new LockChannelRequest();
			LockChannelParam lockChannelParam = new LockChannelParam();
			ChannelBody channelBody = new ChannelBody();
			lockChannelParam.setUserId(userId);
			try{
				lockChannelParam.setTaskId(Long.valueOf(channelSchemeBO.getTaskId()));				
			}catch(Exception ex){}
			lockChannelParam.setChannels(new ArrayList<ChannelBody>());
			lockChannelParam.getChannels().add(channelBody);
			
			channelBody.setBundle_id(channelSchemeBO.getBundleId());
			channelBody.setChannel_id(channelSchemeBO.getChannelId());
			channelBody.setChannel_param(channelSchemeBO.getChannelParam());
			if(channelSchemeBO.getDecodeCntUse() != 0){
				if(channelSchemeBO.getCombineType() == ChannelTypeInfo.VIDEOENCODE){						
					channelBody.setVideo_decode_cnt(channelSchemeBO.getDecodeCntUse());
				}else if(channelSchemeBO.getCombineType() == ChannelTypeInfo.AUDIOENCODE){
					channelBody.setAudio_decode_cnt(channelSchemeBO.getDecodeCntUse());
				}
			}
			lockChannelRequest.setLock_channel_request(lockChannelParam);
			log.info("-----------------start locktask with bundleId " + channelSchemeBO.getBundleId() + " and channelId " + channelSchemeBO.getChannelId() + " " + new Date());
			LockChannelRespParam lockChannelRespParam = resourceServiceClient.lockChannel(lockChannelRequest);
			log.info("-----------------end locktask with bundleId " + channelSchemeBO.getBundleId() + " and channelId " + channelSchemeBO.getChannelId() + " " + new Date());
			if(lockChannelRespParam != null && lockChannelRespParam.getLock_channel_response() != null && "success".equals(lockChannelRespParam.getLock_channel_response().getResult())){
				log.info("lockResource success bundleId is " + channelSchemeBO.getBundleId() + " and channelId is " + channelSchemeBO.getChannelId());
				List<ChannelStatusParam> channelStatusParams = lockChannelRespParam.getLock_channel_response().getChannels();
				return ResultMap.ok(channelStatusParams.get(0).getOperate_index());
			}
			String errMsg = null;
			if(lockChannelRespParam != null && lockChannelRespParam.getLock_channel_response() != null && "fail".equals(lockChannelRespParam.getLock_channel_response().getResult())){
				ErrorDescription errorDescription = lockChannelRespParam.getLock_channel_response().getError_description();
				if(errorDescription != null && errorDescription.getError_code() != null){					
					errMsg =  errorDescription.getError_code().toString();
				}
			}
			
			log.error("lockResource failed bundleId is " + channelSchemeBO.getBundleId() + " and channelId is " + channelSchemeBO.getChannelId() + " and errCode is " + errMsg);
		}catch(Exception e){
			log.error("lockResource failed bundleId is " + channelSchemeBO.getBundleId() + " and channelId is " + channelSchemeBO.getChannelId(), e);
		}
		
		return null;
	}
	
	/**
	 * 向资源层锁定通道
	 * @param channelsToLock待锁定的通道
	 * @param userId 用户ID
	 * @param isRollBack 是否回滚
	 * @return
	 */
	public ResultMap lockResource(List<ChannelSchemeBO> channelsToLock, Long userId, boolean isRollBack){
		if(channelsToLock == null || channelsToLock.isEmpty()){		
			return null;
		}
		List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
		for(ChannelSchemeBO channel:channelsToLock){
			log.info("-----------------add locktask with bundleId " + channel.getBundleId() + " and channelId " + channel.getChannelId() + " " + new Date());
			results.add(CompletableFuture.supplyAsync(()->lockResource(channel, userId), ThreadPool.getScheduledExecutor()));
		}
		boolean lockSuccess = true;
		ResultMap taskId2Result = new ResultMap();
		int i = 0;
		Integer operateIndex = null;
		List<ChannelSchemeBO> channelsToUnlock = new ArrayList<ChannelSchemeBO>();
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
				lockSuccess = false;
				if(StringUtils.isNotEmpty(channelsToLock.get(i).getTaskId())){
					taskId2Result.put(channelsToLock.get(i).getTaskId(), new ChannelStatusBody());
				}
				log.error("锁定失败： bundleId is " + channelsToLock.get(i).getBundleId() + " and channelId is " + channelsToLock.get(i).getChannelId());
			}else{
				channelsToUnlock.add(channelsToLock.get(i));
				if(StringUtils.isNotEmpty(channelsToLock.get(i).getTaskId())){
					taskId2Result.put(channelsToLock.get(i).getTaskId(),  new ChannelStatusBody((Integer)resultMap.get("operateIndex")));
				}
				operateIndex = (Integer)resultMap.get("operateIndex");
			}
			i++;
		}
		if(isRollBack){
			if(lockSuccess){
				taskId2Result.put(ResultMap.LOCK_RESULT, true);
				return taskId2Result;
			}else{
				//todo 回滚解锁已经锁定成功的通道
				log.warn("becauseof lockchannel failed so back to unlock channels!,需要解锁的channels:" + channelsToUnlock);
				rollbackToUnlockChannels(channelsToUnlock, userId);
				taskId2Result.put(ResultMap.LOCK_RESULT, false);
				return taskId2Result;
			}
		}else{
			log.info("-----------------lockChannels end " + new Date());
			return taskId2Result;
		}
		
		
	}
	
	private ResultMap lockBundleResource(BundleSchemeBO bundleSchemeBO, Long userId) {
		try{
			LockBundleParam lockBundleParam = new LockBundleParam();
			lockBundleParam.setOperateCountSwitch(whetherOperateCount(bundleSchemeBO));
			lockBundleParam.setBundleId(bundleSchemeBO.getBundleId());
			
			//如果bundleSchemeBO中含有userId则使用，否则使用通用的参数
			Long independentUserId = bundleSchemeBO.getUserId();
			if(null != independentUserId){
				lockBundleParam.setUserId(independentUserId);
			}else{
				lockBundleParam.setUserId(userId);
			}
			
			if(bundleSchemeBO.getPass_by_str() != null){				
				lockBundleParam.setPassByStr(bundleSchemeBO.getPass_by_str().toJSONString());
			}
			if(bundleSchemeBO.getScreens() != null && bundleSchemeBO.getScreens().size() > 0){				
				List<ScreenBody> screens = JSONArray.parseArray(bundleSchemeBO.getScreens().toJSONString(), ScreenBody.class);
				lockBundleParam.setScreens(screens);
			}
			if(bundleSchemeBO.getChannels() != null && !bundleSchemeBO.getChannels().isEmpty()){
				List<ChannelBody> channelBodies = new ArrayList<ChannelBody>();
				for(ChannelSchemeBO channel:bundleSchemeBO.getChannels()){
					ChannelBody channelBody = channel.transToChannelBody();
					channelBodies.add(channelBody);
				}
				lockBundleParam.setChannels(channelBodies);
			}
//			log.info("-----------------start lockBundleTask with bundleId " + bundleSchemeBO.getBundleId());
			LockBundleRespParam lockBundleRespParam = resourceServiceClient.lockBundle(lockBundleParam);
			log.info("-----------------end lockBundleTask with bundleId " + bundleSchemeBO.getBundleId()  + " 锁定后锁定计数为" + lockBundleRespParam.getOperate_count());
			if(lockBundleRespParam != null && "success".equals(lockBundleRespParam.getResult())){
//				log.info("lockBundleResource success bundleId is " + bundleSchemeBO.getBundleId());
				return ResultMap.ok(lockBundleRespParam.getOperate_index());
			}
			String errMsg = null;
			if(lockBundleRespParam != null && "fail".equals(lockBundleRespParam.getResult())){
				ErrorDescription errorDescription = lockBundleRespParam.getError_description();
				if(errorDescription != null && errorDescription.getError_code() != null){					
					errMsg =  errorDescription.getError_code().toString();
				}
			}
			
			log.error("lockBundleResource failed bundleId is " + bundleSchemeBO.getBundleId() + " and errCode is " + errMsg);
		}catch(Exception e){
			log.error("lockBundleResource failed bundleId is " + bundleSchemeBO.getBundleId(),e);
		}
		
		return null;
	}
	
	/**
	 * 向资源层锁定bundle通道
	 * @param bundlesToLock待锁定的通道
	 * @param userId 用户ID
	 * @param isRollBack 是否回滚
	 * @return
	 */
	@Deprecated
	public ResultMap lockBundleResource(List<BundleSchemeBO> bundlesToLock, Long userId, boolean isRollBack){
		if(bundlesToLock == null || bundlesToLock.isEmpty()){		
			return null;
		}
		List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
		for(BundleSchemeBO bundle:bundlesToLock){
//			log.info("-----------------add lockBundletask with bundleId " + bundle.getBundleId());
			results.add(CompletableFuture.supplyAsync(()->lockBundleResource(bundle, userId), ThreadPool.getScheduledExecutor()));
		}
		boolean lockSuccess = true;
		ResultMap taskId2Result = new ResultMap();
		int i = 0;
		List<BundleSchemeBO> bundlesToUnlock = new ArrayList<BundleSchemeBO>();
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
				lockSuccess = false;
				if(StringUtils.isNotEmpty(bundlesToLock.get(i).getTaskId())){
					taskId2Result.put(bundlesToLock.get(i).getTaskId(), new ChannelStatusBody());
				}
				log.error("锁定失败： bundleId is " + bundlesToLock.get(i).getBundleId());
			}else{
				bundlesToUnlock.add(bundlesToLock.get(i));
				if(StringUtils.isNotEmpty(bundlesToLock.get(i).getTaskId())){
					taskId2Result.put(bundlesToLock.get(i).getTaskId(),  new ChannelStatusBody((Integer)resultMap.get("operateIndex")));
				}
			}
			i++;
		}
		if(isRollBack){
			if(lockSuccess){
				taskId2Result.put(ResultMap.LOCK_RESULT, true);
				return taskId2Result;
			}else{
				//todo 回滚解锁已经锁定成功的bundle
				log.warn("becauseof lockbundle failed so back to unlock bundles!, 需要解锁的bundles：" + bundlesToUnlock);
				rollbackToUnlockBundles(bundlesToUnlock, userId);
				taskId2Result.put(ResultMap.LOCK_RESULT, false);
				return taskId2Result;
			}
		}else{
			log.info("-----------------lockBundles end " + new Date());
			return taskId2Result;
		}		
	}
	
	//返回值使用“bundleResultMapList”的key来存储所有通道的锁定结果（List<ResultMap>型），其中的顺序与bundleSchemeBOs一致，即：“list的第i个元素，是bundleSchemeBOs中的第i个的结果”
	private ResultMap batchLockBundleResourceUtil(List<BundleSchemeBO> bundleSchemeBOs, Long userId, boolean mustLockAll) {
		String bundleListStr = "[ ";
		try{
			List<LockBundleParam> lockBundleParams = new ArrayList<LockBundleParam>();
			BatchLockBundleParam batchLockBundleParam = new BatchLockBundleParam();
			batchLockBundleParam.setBundles(lockBundleParams);
			batchLockBundleParam.setUserId(userId);//userId单独设置到每个LockBundleParam。已不需要在此设置
			batchLockBundleParam.setMustLockAll(mustLockAll);
			for(BundleSchemeBO bundleSchemeBO : bundleSchemeBOs){
				LockBundleParam lockBundleParam = new LockBundleParam();
				lockBundleParam.setOperateCountSwitch(whetherOperateCount(bundleSchemeBO));
				lockBundleParam.setBundleId(bundleSchemeBO.getBundleId());
				
				//如果bundleSchemeBO中含有userId则使用，否则使用通用的参数
				Long independentUserId = bundleSchemeBO.getUserId();
				if(null != independentUserId){
					lockBundleParam.setUserId(independentUserId);
				}else{
					lockBundleParam.setUserId(userId);
				}
				
				if(bundleSchemeBO.getPass_by_str() != null){				
					lockBundleParam.setPassByStr(bundleSchemeBO.getPass_by_str().toJSONString());
				}
				if(bundleSchemeBO.getScreens() != null && bundleSchemeBO.getScreens().size() > 0){				
					List<ScreenBody> screens = JSONArray.parseArray(bundleSchemeBO.getScreens().toJSONString(), ScreenBody.class);
					lockBundleParam.setScreens(screens);
				}
				if(bundleSchemeBO.getChannels() != null && !bundleSchemeBO.getChannels().isEmpty()){
					List<ChannelBody> channelBodies = new ArrayList<ChannelBody>();
					for(ChannelSchemeBO channel:bundleSchemeBO.getChannels()){
						ChannelBody channelBody = channel.transToChannelBody();
						channelBodies.add(channelBody);
					}
					lockBundleParam.setChannels(channelBodies);
				}
				lockBundleParams.add(lockBundleParam);
				bundleListStr += bundleSchemeBO.getBundleId() + ", ";				
			}
			bundleListStr += "]";	
			//log.info("-----------------start lockBundleTask with bundleId " + bundleListStr);
			BatchLockBundleRespParam batchLockBundleRespParam = resourceServiceClient.batchLockBundle(batchLockBundleParam);
			//log.info("-----------------end lockBundleTask with bundleId " + bundleListStr);
			if(batchLockBundleRespParam == null){
				log.error("lockBundleResource failed, 返回null, bundleId is " + bundleListStr);
				return ResultMap.failure();
			}
			
			ResultMap resultMap = new ResultMap();
			List<ResultMap> bundleResultMapList = new ArrayList<ResultMap>();
			
			//不全部锁定时，不用判断result
			if(!mustLockAll || BatchLockBundleRespParam.SUCCESS.equals(batchLockBundleRespParam.getResult())){
				log.info("lockBundleResource success bundleId is " + bundleListStr);
				if(mustLockAll){
					log.info("mustLockAll = true，所有bundle都已锁定");
				}else{
					log.info("mustLockAll = false，不要求锁定所有的bundle");
				}
				resultMap = ResultMap.ok();
				resultMap.put(ResultMap.BUNDLE_RESULT_MAP_LIST, bundleResultMapList);
				//添加每个bundle的锁定结果
				List<BatchLockBundleRespBody> batchLockBundleRespBodyList = batchLockBundleRespParam.getOperateBundlesResult();
				
				//将batchLockBundleRespBodyList按照bundleSchemeBOs的顺讯重新排序为orderedRespBodyList
				List<BatchLockBundleRespBody> orderedRespBodyList = new ArrayList<BatchLockBundleRespBody>();
				for(BundleSchemeBO bundleSchemeBO : bundleSchemeBOs){
					for(BatchLockBundleRespBody batchLockBundleRespBody : batchLockBundleRespBodyList){
						if(batchLockBundleRespBody.getBundleId().equals(bundleSchemeBO.getBundleId())){
							orderedRespBodyList.add(batchLockBundleRespBody);
							break;
						}
					}
				}
				for(BatchLockBundleRespBody batchLockBundleRespBody : orderedRespBodyList){
					boolean operateResult = batchLockBundleRespBody.isOperateResult();
					if(operateResult){
						log.info("bundle " + batchLockBundleRespBody.getBundleId()  + " 锁定后锁定计数为 " + batchLockBundleRespBody.getOperate_count());
						ResultMap bundleResultMap = ResultMap.ok(batchLockBundleRespBody.getOperate_index());//锁定不需要加入计数，如果需要，参考解锁的代码
						bundleResultMapList.add(bundleResultMap);
					}else{
						log.info("bundle " + batchLockBundleRespBody.getBundleId()  + " 锁定失败，锁定计数为 " + batchLockBundleRespBody.getOperate_count());
						ResultMap bundleResultMap = ResultMap.failure();
						bundleResultMapList.add(bundleResultMap);
					}
				}
				return resultMap;
			}

			String errMsg = null;
			if(BatchLockBundleRespParam.FAIL.equals(batchLockBundleRespParam.getResult())){
				ErrorDescription errorDescription = batchLockBundleRespParam.getError_description();
				if(errorDescription != null && errorDescription.getError_code() != null){					
					errMsg =  errorDescription.getError_code().toString();
				}
			}
			
			log.error("lockBundleResource failed bundleId is " + bundleListStr + " and errCode is " + errMsg);
		}catch(Exception e){
			log.error("lockBundleResource failed bundleId is " + bundleListStr, e);
		}

		return ResultMap.failure();
	}
	
	/**
	 * 向资源层批量锁定bundle通道
	 * @param bundlesToLock待锁定的通道
	 * @param userId 用户ID
	 * @param needRollBack 当mustLockAll==false时，如果部分bundle没有成功锁定，是否回滚，把锁定也释放。这个参数已经没什么意义，如果需要回滚，那么mustLockAll使用true即可
	 * @param mustLockAll 是否必须锁定全部bundle才算成功（否则资源层不锁定任何bundle）
	 * @return ResultMap的key-value包括：lockResult-true/false, taskId-ChannelStatusBody
	 * @return mustLockAll==true时，lockResult==false表示全部锁定失败；mustLockAll==false时，lockResult==false表示调用出错，lockResult==true表示调用成功，但不表示全部锁定成功
	 */
	public ResultMap batchLockBundleResource(List<BundleSchemeBO> bundlesToLock, Long userId, boolean mustLockAll){
		if(bundlesToLock == null || bundlesToLock.isEmpty()){		
			return null;
		}
		//批量方法
		ResultMap batchLockResultMap = batchLockBundleResourceUtil(bundlesToLock, userId, mustLockAll);		
		
		int resultCode = (int) batchLockResultMap.get(ResultMap.CODE);
		//锁定失败。mustLockAll==true则是全失败；mustLockAll==false则是调用出错
		if(!new Integer(200).equals(resultCode)){
			return ResultMap.failure();
		}
		
		@SuppressWarnings("unchecked")
		List<ResultMap> bundleResultMapList = (List<ResultMap>) batchLockResultMap.get(ResultMap.BUNDLE_RESULT_MAP_LIST);
		if(null == bundleResultMapList){
			bundleResultMapList = new ArrayList<ResultMap>();
		}
		
		boolean allLockSuccess = true;
		ResultMap taskId2Result = new ResultMap();
		int i = 0;
		List<BundleSchemeBO> bundlesToUnlock = new ArrayList<BundleSchemeBO>();
		//把结果放进taskId2Result；并把成功的放进bundlesToUnlock
		for(ResultMap bundleResultMap : bundleResultMapList){
			if(bundleResultMap == null || !new Integer(200).equals(bundleResultMap.get(ResultMap.CODE))){
				allLockSuccess = false;
				if(StringUtils.isNotEmpty(bundlesToLock.get(i).getTaskId())){
					taskId2Result.put(bundlesToLock.get(i).getTaskId(), new ChannelStatusBody(false));
				}
				log.error("锁定失败： bundleId is " + bundlesToLock.get(i).getBundleId());
			}else{
				bundlesToUnlock.add(bundlesToLock.get(i));
				if(StringUtils.isNotEmpty(bundlesToLock.get(i).getTaskId())){
					taskId2Result.put(bundlesToLock.get(i).getTaskId(), new ChannelStatusBody((Integer)bundleResultMap.get(ResultMap.OPERATE_INDEX)));
				}
			}
			i++;
		}
		
		if(mustLockAll){
			taskId2Result.put(ResultMap.LOCK_RESULT, allLockSuccess);
		}else{
			taskId2Result.put(ResultMap.LOCK_RESULT, true);
		}
		return taskId2Result;
	}
	
	/*public ResultMap lockResource(Map<String, List<ChannelSchemeBO>> uuid2ChannelSelMap, Long userId){
		ResultMap uuid2Result = new ResultMap();
		if(uuid2ChannelSelMap == null || uuid2ChannelSelMap.isEmpty()){
			return uuid2Result;
		}
		
		try{
			List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
			List<String> uuidList = new ArrayList<String>();
			for(String uuid:uuid2ChannelSelMap.keySet()){
				List<ChannelSchemeBO> channelSchemeBOs = uuid2ChannelSelMap.get(uuid);
				results.add(CompletableFuture.supplyAsync(()->lockResource(channelSchemeBOs.subList(0, 1), userId, true),ThreadPool.getScheduledExecutor()));
				uuidList.add(uuid);
			}
			
			int i = 0;
			for(Future<ResultMap> result:results){
				ResultMap resultMap = result.get();
				if(resultMap != null && new Integer(200).equals(resultMap.get("code"))){
					uuid2Result.put(uuidList.get(i), new ChannelStatusBody((Integer)resultMap.get("operateIndex")));
				}else{
					uuid2Result.put(uuidList.get(i), new ChannelStatusBody());
				}
				i++;
			}
		}catch(Exception e){
			log.error("lockChannelRequest to resource failed", e);
		}
		return uuid2Result;
	}*/
	
	public void rollbackToUnlockChannels(List<ChannelSchemeBO> channelsToUnlock, Long userId){
		try{
			CompletableFuture.supplyAsync(()->unlockResource(channelsToUnlock, userId, true),ThreadPool.getScheduledExecutor());
		}catch(Exception e){
			log.error("rollbackToUnlockChannels failed", e);
		}
	}
	
	private void rollbackToUnlockBundles(List<BundleSchemeBO> bundlesToUnlock, Long userId){
		try{
			//CompletableFuture.supplyAsync(()->unlockBundleResource(bundlesToUnlock, userId, true),ThreadPool.getScheduledExecutor());
			batchUnlockBundleResource(bundlesToUnlock, userId, false);
		}catch(Exception e){
			log.error("rollbackToUnlockBundles failed", e);
		}
	}
	
	public ResultMap unlockResource(ChannelSchemeBO channelSchemeBO, Long userId) {
		try{
			ReleaseChannelRequest releaseChannelRequest = new ReleaseChannelRequest();
			LockChannelParam unlockChannelParam = new LockChannelParam();
			ChannelBody channelBody = new ChannelBody();
			unlockChannelParam.setUserId(userId);
			try{				
				unlockChannelParam.setTaskId(Long.valueOf(channelSchemeBO.getTaskId()));
			}catch(Exception ex){}
			unlockChannelParam.setChannels(new ArrayList<ChannelBody>());
			unlockChannelParam.getChannels().add(channelBody);
			
			channelBody.setBundle_id(channelSchemeBO.getBundleId());
			channelBody.setChannel_id(channelSchemeBO.getChannelId());			
			
			releaseChannelRequest.setRelease_channel_request(unlockChannelParam);
			log.info("-----------------start unlocktask with bundleId " + channelSchemeBO.getBundleId() + " and channelId " + channelSchemeBO.getChannelId() + " " + new Date());
			ReleaseChannelRespParam releaseChannelRespParam = resourceServiceClient.releaseChannel(releaseChannelRequest);
			log.info("-----------------end unlocktask with bundleId " + channelSchemeBO.getBundleId() + " and channelId " + channelSchemeBO.getChannelId() + " " + new Date());
			if(releaseChannelRespParam != null && releaseChannelRespParam.getRelease_channel_response() != null && "success".equals(releaseChannelRespParam.getRelease_channel_response().getResult())){
				List<ChannelStatusParam> channelStatusParams = releaseChannelRespParam.getRelease_channel_response().getChannels();
				log.info("unlockResource succeed bundleId is " + channelSchemeBO.getBundleId() + " and channelId is " + channelSchemeBO.getChannelId());
				return ResultMap.ok(channelStatusParams.get(0).getOperate_index());
			}
			
			String errMsg = null;
			if(releaseChannelRespParam != null && releaseChannelRespParam.getRelease_channel_response() != null && "fail".equals(releaseChannelRespParam.getRelease_channel_response().getResult())){
				ErrorDescription errorDescription = releaseChannelRespParam.getRelease_channel_response().getError_description();
				if(errorDescription != null && errorDescription.getError_code() != null){					
					errMsg =  errorDescription.getError_code().toString();
				}
			}
			log.error("unlockResource failed bundleId is " + channelSchemeBO.getBundleId() + " and channelId is " + channelSchemeBO.getChannelId() + " and errCode is " + errMsg);
		}catch(Exception e){
			log.error("releaseResource failed bundleId is " + channelSchemeBO.getBundleId() + " and channelId is " + channelSchemeBO.getChannelId(), e);
		}
		return null;
	}
	
	private ResultMap unlockBundleResource(BundleSchemeBO bundleSchemeBO, Long userId) {
		try{
			LockBundleParam releaseBundleParam = new LockBundleParam();
			releaseBundleParam.setOperateCountSwitch(whetherOperateCount(bundleSchemeBO));
			releaseBundleParam.setBundleId(bundleSchemeBO.getBundleId());
			//如果bundleSchemeBO中含有userId则使用，否则使用通用的参数
			Long independentUserId = bundleSchemeBO.getUserId();
			if(null != independentUserId){
				releaseBundleParam.setUserId(independentUserId);
			}else{
				releaseBundleParam.setUserId(userId);
			}
			
			if(bundleSchemeBO.getScreens() != null && bundleSchemeBO.getScreens().size() > 0){				
				List<ScreenBody> screens = JSONArray.parseArray(bundleSchemeBO.getScreens().toJSONString(), ScreenBody.class);
				releaseBundleParam.setScreens(screens);
			}
			if(bundleSchemeBO.getChannels() != null && !bundleSchemeBO.getChannels().isEmpty()){
				List<ChannelBody> channelBodies = new ArrayList<ChannelBody>();
				for(ChannelSchemeBO channel:bundleSchemeBO.getChannels()){
					ChannelBody channelBody = new ChannelBody();
					channelBody.setChannel_id(channel.getChannelId());
					channelBodies.add(channelBody);
				}
				releaseBundleParam.setChannels(channelBodies);
			}
//			log.info("-----------------start unlockBundleTask with bundleId " + bundleSchemeBO.getBundleId());
			LockBundleRespParam releaseBundleRespParam = resourceServiceClient.releaseBundle(releaseBundleParam);
//			log.info("-----------------end unlockBundleTask with bundleId " + bundleSchemeBO.getBundleId());
			if(releaseBundleRespParam != null && "success".equals(releaseBundleRespParam.getResult())){
				Integer operateCount = releaseBundleRespParam.getOperate_count();
				log.info("unlockBundleResource succeed bundleId is " + bundleSchemeBO.getBundleId() + " 解锁后锁定计数为" + operateCount);
				if(null == operateCount){
					return ResultMap.ok(releaseBundleRespParam.getOperate_index(), 0);
				}
				return ResultMap.ok(releaseBundleRespParam.getOperate_index(), releaseBundleRespParam.getOperate_count());
//				return ResultMap.ok(releaseBundleRespParam.getOperate_index());
			}
			
			String errMsg = null;
			if(releaseBundleRespParam != null && "fail".equals(releaseBundleRespParam.getResult())){
				ErrorDescription errorDescription = releaseBundleRespParam.getError_description();
				if(errorDescription != null && errorDescription.getError_code() != null){					
					errMsg =  errorDescription.getError_code().toString();
				}
			}
			log.error("unlockBundleResource failed bundleId is " + bundleSchemeBO.getBundleId()  + " and errCode is " + errMsg);
		}catch(Exception e){
			log.error("unlockBundleResource failed bundleId is " + bundleSchemeBO.getBundleId(),e);
		}
		return null;
	}
	
	@Deprecated
	public ResultMap unlockBundleResource(List<BundleSchemeBO> bundlesToUnlock, Long userId, boolean isRollback){
		if(bundlesToUnlock == null || bundlesToUnlock.isEmpty()){		
			return null;
		}
		List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
		for(BundleSchemeBO bundle:bundlesToUnlock){
//			log.info("-----------------add unlockBundleTask with bundleId " + bundle.getBundleId());
			results.add(CompletableFuture.supplyAsync(()->unlockBundleResource(bundle, userId), ThreadPool.getScheduledExecutor()));
		}
		boolean lockSuccess = true;
		ResultMap taskId2Result = new ResultMap();
		int i = 0;
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
				lockSuccess = false;
				if(StringUtils.isNotEmpty(bundlesToUnlock.get(i).getTaskId())){
					taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody());
				}
				
			}else{
				if(StringUtils.isNotEmpty(bundlesToUnlock.get(i).getTaskId())){
					if(resultMap.containsKey("operateCount")){
						taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody((Integer)resultMap.get("operateIndex"), (Integer)resultMap.get("operateCount")));
					}else{
						taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody((Integer)resultMap.get("operateIndex")));
					}
				}
			}
			i++;
		}
		if(isRollback){
			if(lockSuccess){
				return ResultMap.ok();
			}else{
				//rollback do nothing
				return null;
			}
		}else{
			return taskId2Result;
		}
	}

	private ResultMap batchUnlockBundleResourceUtil(List<BundleSchemeBO> bundleSchemeBOs, Long userId, boolean mustUnlockAll) {		
		String bundleListStr = "[ ";
		try{
			List<LockBundleParam> releaseBundleParams = new ArrayList<LockBundleParam>();
			BatchLockBundleParam batchUnockBundleParam = new BatchLockBundleParam();
			batchUnockBundleParam.setBundles(releaseBundleParams);
			batchUnockBundleParam.setUserId(userId);//userId单独设置到每个LockBundleParam。已不需要在此设置
			batchUnockBundleParam.setMustLockAll(mustUnlockAll);
			
			for(BundleSchemeBO bundleSchemeBO : bundleSchemeBOs){
				LockBundleParam releaseBundleParam = new LockBundleParam();
				releaseBundleParam.setOperateCountSwitch(whetherOperateCount(bundleSchemeBO));
				releaseBundleParam.setBundleId(bundleSchemeBO.getBundleId());
				//如果bundleSchemeBO中含有userId则使用，否则使用通用的参数
				Long independentUserId = bundleSchemeBO.getUserId();
				if(null != independentUserId){
					batchUnockBundleParam.setUserId(independentUserId);
				}else{
					batchUnockBundleParam.setUserId(userId);
				}
				
				if(bundleSchemeBO.getScreens() != null && bundleSchemeBO.getScreens().size() > 0){				
					List<ScreenBody> screens = JSONArray.parseArray(bundleSchemeBO.getScreens().toJSONString(), ScreenBody.class);
					releaseBundleParam.setScreens(screens);
				}
				if(bundleSchemeBO.getChannels() != null && !bundleSchemeBO.getChannels().isEmpty()){
					List<ChannelBody> channelBodies = new ArrayList<ChannelBody>();
					for(ChannelSchemeBO channel:bundleSchemeBO.getChannels()){
						ChannelBody channelBody = new ChannelBody();
						channelBody.setChannel_id(channel.getChannelId());
						channelBodies.add(channelBody);
					}
					releaseBundleParam.setChannels(channelBodies);
				}
				releaseBundleParams.add(releaseBundleParam);
				bundleListStr += bundleSchemeBO.getBundleId() + ", ";
			}
			bundleListStr += "]";
//			log.info("-----------------start unlockBundleTask with bundleId " + bundleListStr);
			BatchLockBundleRespParam batchUnlockBundleRespParam = resourceServiceClient.batchReleaseBundle(batchUnockBundleParam);
//			log.info("-----------------end unlockBundleTask with bundleId " + bundleListStr);
			if(batchUnlockBundleRespParam == null){
				log.error("unlockBundleResource failed, 返回null, bundleId is " + bundleListStr);
				return ResultMap.failure();
			}			
			ResultMap resultMap = new ResultMap();
			List<ResultMap> bundleResultMapList = new ArrayList<ResultMap>();
			
			//不全部锁定时，不用判断result
			if(!mustUnlockAll || BatchLockBundleRespParam.SUCCESS.equals(batchUnlockBundleRespParam.getResult())){
				log.info("unlockBundleResource succeed bundleId is " + bundleListStr);
				if(mustUnlockAll){
					log.info("mustUnlockAll = true，所有bundle都已释放");
				}else{
					log.info("mustUnlockAll = false，不要求释放所有的bundle");
				}
				resultMap = ResultMap.ok();
				resultMap.put(ResultMap.BUNDLE_RESULT_MAP_LIST, bundleResultMapList);
				//添加每个bundle的锁定结果
				List<BatchLockBundleRespBody> batchLockBundleRespBodyList = batchUnlockBundleRespParam.getOperateBundlesResult();
				
				//将batchLockBundleRespBodyList按照bundleSchemeBOs的顺讯重新排序为orderedRespBodyList
				List<BatchLockBundleRespBody> orderedRespBodyList = new ArrayList<BatchLockBundleRespBody>();
				for(BundleSchemeBO bundleSchemeBO : bundleSchemeBOs){
					for(BatchLockBundleRespBody batchLockBundleRespBody : batchLockBundleRespBodyList){
						if(batchLockBundleRespBody.getBundleId().equals(bundleSchemeBO.getBundleId())){
							orderedRespBodyList.add(batchLockBundleRespBody);
							break;
						}
					}
				}
				for(BatchLockBundleRespBody batchLockBundleRespBody : orderedRespBodyList){
					if(!batchLockBundleRespBody.isOperateResult()){
						ResultMap bundleResultMap = ResultMap.failure();
						bundleResultMapList.add(bundleResultMap);
						continue;
					}
					Integer operateCount = batchLockBundleRespBody.getOperate_count();
					log.info("unlockBundleResource succeed bundleId is " + batchLockBundleRespBody.getBundleId() + " 解锁后锁定计数为" + operateCount);
					if(null == operateCount){
						operateCount = 0;
					}
					ResultMap bundleResultMap = ResultMap.ok(batchLockBundleRespBody.getOperate_index(), operateCount);
					bundleResultMapList.add(bundleResultMap);
				}
				return resultMap;
			}

			String errMsg = null;
			if(BatchLockBundleRespParam.FAIL.equals(batchUnlockBundleRespParam.getResult())){
				ErrorDescription errorDescription = batchUnlockBundleRespParam.getError_description();
				if(errorDescription != null && errorDescription.getError_code() != null){					
					errMsg =  errorDescription.getError_code().toString();
				}
			}
			
			log.error("unlockBundleResource failed bundleId is " + bundleListStr + " and errCode is " + errMsg);
		}catch(Exception e){
			log.error("unlockBundleResource failed bundleId is " + bundleListStr, e);
		}
		
		return ResultMap.failure();
	}

	public ResultMap batchUnlockBundleResource(List<BundleSchemeBO> bundlesToUnlock, Long userId, boolean mustUnlockAll){
		if(bundlesToUnlock == null || bundlesToUnlock.isEmpty()){		
			return null;
		}
		//批量方法
		ResultMap batchUnlockResultMap = batchUnlockBundleResourceUtil(bundlesToUnlock, userId, mustUnlockAll);
		
		int resultCode = (int) batchUnlockResultMap.get(ResultMap.CODE);
		//释放失败。mustUnlockAll==true则是全失败；mustUnlockAll==false则是调用出错
		if(!new Integer(200).equals(resultCode)){
			return ResultMap.failure();
		}
		
		@SuppressWarnings("unchecked")
		List<ResultMap> bundleResultMapList = (List<ResultMap>) batchUnlockResultMap.get(ResultMap.BUNDLE_RESULT_MAP_LIST);
		if(null == bundleResultMapList){
			bundleResultMapList = new ArrayList<ResultMap>();
		}		
		
		boolean allUnlockSuccess = true;
		ResultMap taskId2Result = new ResultMap();
		int i = 0;
		//把结果放进taskId2Result；并把成功的放进 bundlesToUnlock
		for(ResultMap bundleResultMap : bundleResultMapList){
			if(bundleResultMap == null || !new Integer(200).equals(bundleResultMap.get(ResultMap.CODE))){
				allUnlockSuccess = false;
				if(StringUtils.isNotEmpty(bundlesToUnlock.get(i).getTaskId())){
					taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody());
				}
				log.error("释放失败： bundleId is " + bundlesToUnlock.get(i).getBundleId());
			}else{
				if(StringUtils.isNotEmpty(bundlesToUnlock.get(i).getTaskId())){
//					taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody((Integer)bundleResultMap.get("operateIndex"), (Integer)bundleResultMap.get("operateCount")));
//					taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody((Integer)bundleResultMap.get("operateIndex")));
					if(bundleResultMap.containsKey("operateCount")){
						taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody((Integer)bundleResultMap.get("operateIndex"), (Integer)bundleResultMap.get("operateCount")));
					}else{
						taskId2Result.put(bundlesToUnlock.get(i).getTaskId(), new ChannelStatusBody((Integer)bundleResultMap.get("operateIndex")));
					}
				}
			}
			i++;
		}

		if(mustUnlockAll){
			taskId2Result.put(ResultMap.LOCK_RESULT, allUnlockSuccess);
		}else{
			taskId2Result.put(ResultMap.LOCK_RESULT, true);
		}
		return taskId2Result;
	}

	public ResultMap unlockResource(List<ChannelSchemeBO> channelsToUnlock, Long userId, boolean isRollback){
		if(channelsToUnlock == null || channelsToUnlock.isEmpty()){		
			return null;
		}
		List<Future<ResultMap>> results = new ArrayList<Future<ResultMap>>();
		for(ChannelSchemeBO channel:channelsToUnlock){
			log.info("-----------------add unlocktask with bundleId " + channel.getBundleId() + " and channelId " + channel.getChannelId() + " " + new Date());
			results.add(CompletableFuture.supplyAsync(()->unlockResource(channel, userId), ThreadPool.getScheduledExecutor()));
		}
		boolean lockSuccess = true;
		ResultMap taskId2Result = new ResultMap();
		int i = 0;
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
				lockSuccess = false;
				if(StringUtils.isNotEmpty(channelsToUnlock.get(i).getTaskId())){
					taskId2Result.put(channelsToUnlock.get(i).getTaskId(), new ChannelStatusBody());
				}
				
			}else{
				if(StringUtils.isNotEmpty(channelsToUnlock.get(i).getTaskId())){
					taskId2Result.put(channelsToUnlock.get(i).getTaskId(), new ChannelStatusBody((Integer)resultMap.get("operateIndex")));
				}
			}
			i++;
		}
		if(isRollback){
			if(lockSuccess){
				return ResultMap.ok();
			}else{
				//rollback do nothing
				return null;
			}
		}else{
			return taskId2Result;
		}
		
	}
	/*
	 * (非 Javadoc)
	 * 
	 * <p>接收回调</p>
	 * 
	 * <p>接收消息队列资源层的回复 </p>
	 * 
	 * @param msg
	 * 
	 * @see com.suma.venus.message.mq.Callback#execute(javax.jms.Message)
	 * 
	 */
	@Override
	public void execute(Message msg) {

		// if (callback != null) {
		// callback.execute(msg);
		// }
	}

	/*
	 * (非 Javadoc)
	 * 
	 * <p>申请资源</p>
	 * 
	 * <p>Description: </p>
	 * 
	 */
	public static ResponseBO applyResoure(ResourceLayerRequestBO requestBO) {
		return sendMessage("");

	}

}
