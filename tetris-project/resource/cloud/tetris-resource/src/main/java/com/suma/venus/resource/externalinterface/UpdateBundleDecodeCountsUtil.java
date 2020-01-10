package com.suma.venus.resource.externalinterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.LockChannelParamPO;
import com.suma.venus.resource.service.BundleService;

@Service
public class UpdateBundleDecodeCountsUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateBundleDecodeCountsUtil.class);
	
	@Autowired
	private BundleService bundleService;

	/**lock_channel时，在新的事务中更新bundle上的解码数*/
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void shrinkBundleDecodeCounts(ChannelBody channel, LockChannelParamPO lockChannelParamPO) throws Exception{
		BundlePO bundle = bundleService.findByBundleId(channel.getBundle_id());
		//占用音频解码数
		if(null != channel.getAudio_decode_cnt() ){
			//bundle剩余的音频解码能力大于当前要使用的解码能力数
			bundle.setFreeAudioSrcCnt(bundle.getFreeAudioSrcCnt() - channel.getAudio_decode_cnt());
			lockChannelParamPO.setUseAudioSrcCnt(channel.getAudio_decode_cnt());
			LOGGER.info("lock channel cost " + channel.getAudio_decode_cnt() + " audio decode counts of bundle; " 
					+ bundle.getFreeAudioSrcCnt() + " left on bundle");
		}
		
		//占用视频解码数
		if(null != channel.getVideo_decode_cnt()){
			bundle.setFreeVideoSrcCnt(bundle.getFreeVideoSrcCnt() - channel.getVideo_decode_cnt());
			lockChannelParamPO.setUseVideoSrcCnt(channel.getVideo_decode_cnt());
			LOGGER.info("lock channel cost " + channel.getVideo_decode_cnt() + " video decode counts of bundle; " 
					+ bundle.getFreeVideoSrcCnt() + " left on bundle");
		}
		
		bundleService.save(bundle);
	}
	
	/**release_channel时，在新的事务中更新bundle上的解码数*/
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void expandBundleDecodeCounts(LockChannelParamPO lockChannelParamPO) throws Exception{
		BundlePO bundle = bundleService.findByBundleId(lockChannelParamPO.getBundleId());
		if(lockChannelParamPO.getUseAudioSrcCnt() > 0){
			Integer freeAudioSrcCnt = lockChannelParamPO.getUseAudioSrcCnt() + bundle.getFreeAudioSrcCnt();
			bundle.setFreeAudioSrcCnt(freeAudioSrcCnt <= bundle.getMaxAudioSrcCnt() ? freeAudioSrcCnt : bundle.getMaxAudioSrcCnt());
			LOGGER.info("release channel return " + lockChannelParamPO.getUseAudioSrcCnt() + " audio decode counts to bundle; " 
					+ bundle.getFreeAudioSrcCnt() + " left on bundle");
		}
		
		if(lockChannelParamPO.getUseVideoSrcCnt() > 0){
			Integer freeVideoSrcCnt = lockChannelParamPO.getUseVideoSrcCnt() + bundle.getFreeVideoSrcCnt();
			bundle.setFreeVideoSrcCnt(freeVideoSrcCnt <= bundle.getMaxVideoSrcCnt() ? freeVideoSrcCnt : bundle.getMaxVideoSrcCnt());
			LOGGER.info("release channel return " + lockChannelParamPO.getUseVideoSrcCnt() + " video decode counts to bundle; " 
					+ bundle.getFreeVideoSrcCnt() + " left on bundle");
		}
		bundleService.save(bundle);
	}
	
}
