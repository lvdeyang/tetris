package com.sumavision.bvc.device.monitor.live;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorLiveSplitConfigService {

	@Autowired
	private MonitorLiveSplitConfigDAO monitorLiveSplitConfigDAO;
	
	/**
	 * 添加屏幕配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午1:46:06
	 * @param Long userId 用户 id
	 * @param Integer serial 屏幕序号
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备层id
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstVideoChannelName 目标视频设备通道名称
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备层id
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param String dstAudioChannelName 目标音频设备通道名称
	 * @return MonitorLiveSplitConfigPO 屏幕配置
	 */
	public MonitorLiveSplitConfigPO add(
			Long userId,
			Integer serial,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstVideoChannelName,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			String dstAudioChannelName) throws Exception{
		
		MonitorLiveSplitConfigPO config = new MonitorLiveSplitConfigPO();
		config.setUpdateTime(new Date());
		config.setUserId(userId);
		config.setSerial(serial);
		config.setDstVideoBundleId(dstVideoBundleId);
		config.setDstVideoBundleName(dstVideoBundleName);
		config.setDstVideoBundleType(dstVideoBundleType);
		config.setDstVideoLayerId(dstVideoLayerId);
		config.setDstVideoChannelId(dstVideoChannelId);
		config.setDstVideoBaseType(dstVideoBaseType);
		config.setDstVideoChannelName(dstVideoChannelName);
		config.setDstAudioBundleId(dstAudioBundleId);
		config.setDstAudioBundleName(dstAudioBundleName);
		config.setDstAudioBundleType(dstAudioBundleType);
		config.setDstAudioLayerId(dstAudioLayerId);
		config.setDstAudioChannelId(dstAudioChannelId);
		config.setDstAudioBaseType(dstAudioBaseType);
		config.setDstAudioChannelName(dstAudioChannelName);
		monitorLiveSplitConfigDAO.save(config);
		
		return config;
	}
	
	/**
	 * 删除视频配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午1:52:10
	 * @param Long id 配置id
	 */
	public void remove(Long id) throws Exception{
		monitorLiveSplitConfigDAO.delete(id);
	}
	
}
