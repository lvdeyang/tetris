package com.suma.venus.resource.externalinterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.base.bo.LockChannelParam;
import com.suma.venus.resource.constant.ErrorCode;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.LockChannelParamPO;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.LockChannelParamService;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;

@Service
public class UpdateSingleChannelUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSingleChannelUtil.class);
	
	@Autowired
	private LockChannelParamService lockChannelParamService;
	
	@Autowired
	private ChannelSchemeService channelSchemeService;
	
	@Autowired
	private UpdateBundleDecodeCountsUtil updateBundleDecodeCountsUtil;
	
	/**lock_channel时，在新的事务中更新并提交channel*/
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public ChannelSchemePO lockAndUpdateSingleChannel(LockChannelParam lockChannelParam, ChannelBody channel)throws Exception {
		ChannelSchemePO channelSchemePO = channelSchemeService.findByBundleIdAndChannelId(
				channel.getBundle_id(), channel.getChannel_id());
		if(LockStatus.BUSY.equals(channelSchemePO.getChannelStatus())){//当前通道已被锁定
			LockChannelParamPO lockedChannelParamPO = lockChannelParamService.findByBundleIdAndChannelId(
					channel.getBundle_id(), channel.getChannel_id());
			if(null != lockedChannelParamPO){
				Long lockedUserId = lockedChannelParamPO.getUserId();
				if(!lockedUserId.equals(lockChannelParam.getUserId())){//判断是否同一用户重新锁定
					LOGGER.error("Channel locked by userid=" + lockChannelParam.getUserId()+";Cannot locked again by userid="+lockedUserId);
					throw new Exception(ErrorCode.CHANNEL_BUSY.toString());
				}
				//更新旧通道参数
				lockedChannelParamPO.setChannelParam(JSONObject.toJSONString(channel.getChannel_param()));
				
				lockChannelParamService.save(lockedChannelParamPO);
			}
		} else {
			//可能有残留数据，有的话直接修改，没有的话再新建
			LockChannelParamPO lockChannelParamPO = lockChannelParamService.findByBundleIdAndChannelId(
					channel.getBundle_id(), channel.getChannel_id());
			if(null == lockChannelParamPO){
				lockChannelParamPO = new LockChannelParamPO();
				lockChannelParamPO.setBundleId(channel.getBundle_id());
				lockChannelParamPO.setChannelId(channel.getChannel_id());
			}
			
			//记录该channel上对应的参数
			lockChannelParamPO.setChannelParam(JSONObject.toJSONString(channel.getChannel_param()));
			lockChannelParamPO.setTaskId(lockChannelParam.getTaskId());
			lockChannelParamPO.setUserId(lockChannelParam.getUserId());
			
			//占用音频解码数或占用视频解码数
			if(null != channel.getAudio_decode_cnt() || null != channel.getVideo_decode_cnt()){
				//给对应的bundl加锁同步
				synchronized (channel.getBundle_id().intern()) {
					//更新bundle上的可用解码数
					updateBundleDecodeCountsUtil.shrinkBundleDecodeCounts(channel, lockChannelParamPO);
				}
			}
			
			lockChannelParamService.save(lockChannelParamPO);
		}

		channelSchemePO.setChannelStatus(LockStatus.BUSY);
		channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
		channelSchemeService.save(channelSchemePO);
		return channelSchemePO;
	}
	
	/**unlock_channel时，在新的事务中更新并提交channel*/
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public ChannelSchemePO unlockAndUpdateSinggleChannel(ChannelBody channel) throws Exception {
		ChannelSchemePO channelSchemePO = channelSchemeService.findByBundleIdAndChannelId(
				channel.getBundle_id(), channel.getChannel_id());
		//该channel上对应的当前指定锁定任务信息
		LockChannelParamPO lockChannelParamPO = lockChannelParamService.findByBundleIdAndChannelId(channel.getBundle_id(), channel.getChannel_id());
		
		if(null != lockChannelParamPO){
			if(lockChannelParamPO.getUseAudioSrcCnt() > 0 || lockChannelParamPO.getUseVideoSrcCnt() > 0){
				synchronized (lockChannelParamPO.getBundleId().intern()) {
					//更新bundle上的可用解码数
					updateBundleDecodeCountsUtil.expandBundleDecodeCounts(lockChannelParamPO);
				}
			}
			
//				lockChannelParamService.delete(lockChannelParamPO);
			lockChannelParamService.deleteByBundleIdAndChannelId(channel.getBundle_id(), channel.getChannel_id());
		}
		
		channelSchemePO.setChannelStatus(LockStatus.IDLE);
		channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
		channelSchemeService.save(channelSchemePO);
		return channelSchemePO;
	}

}
