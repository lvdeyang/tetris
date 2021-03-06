package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;

@Service
public class ChannelSchemeService extends CommonService<ChannelSchemePO> {

	@Autowired
	private ChannelSchemeDao channelSchemeDao;

	@Autowired
	private ChannelTemplateDao channelTemplateDao;

	public List<ChannelSchemePO> findByBundleId(String bundleId) {

		return channelSchemeDao.findByBundleId(bundleId);
	}

	public List<ChannelSchemePO> findByBundleIdAndChannelTemplateID(String bundleId, Long channelTemplateID) {

		return channelSchemeDao.findByBundleIdAndChannelTemplateID(bundleId, channelTemplateID);
	}

	public ChannelSchemePO findByBundleIdAndChannelId(String bundleId, String channelId) {

		return channelSchemeDao.findByBundleIdAndChannelId(bundleId, channelId);
	}

	public List<ChannelSchemePO> findByChannelName(String channelName) {

		return channelSchemeDao.findByChannelName(channelName);
	}

	public List<ChannelSchemePO> findByChannelTemplateID(Long channelTemplateID) {
		return channelSchemeDao.findByChannelTemplateID(channelTemplateID);
	}

	public int deleteByBundleId(String bundleId) {
		return channelSchemeDao.deleteByBundleId(bundleId);
	}

	public List<ChannelSchemePO> findByBundleIdAndChannelStatus(String bundleId, LockStatus channelStatus) {
		return channelSchemeDao.findByBundleIdAndChannelStatus(bundleId, channelStatus);
	}

	// 根据资源通道配置情况判断是编码器还是解码器还是编解码一体
	public Integer getCoderDeviceType(String bundleId) {
		List<ChannelSchemePO> encodeChannels = channelSchemeDao.findEncodeChannelByBundleId(bundleId);
		List<ChannelSchemePO> decodeChannels = channelSchemeDao.findDecodeChannelByBundleId(bundleId);
		if (!encodeChannels.isEmpty() && !decodeChannels.isEmpty()) {
			// 有编码通道和解码通道，为编解码一体设备
			return 1;
		} else if (!encodeChannels.isEmpty()) {
			// 只有编码通道，为编码设备
			return 2;
		} else if (!decodeChannels.isEmpty()) {
			// 只有解码通道,为解码设备
			return 3;
		}

		return null;
	}

	// 根据通道模板和bundleID生成一路实际通道数据
	public ChannelSchemePO getChannelSchemePO(String bundleId, ChannelTemplatePO channelTemplate) {
		ChannelSchemePO channelSchemePO = new ChannelSchemePO();
		channelSchemePO.setBundleId(bundleId);
		channelSchemePO.setChannelId(channelTemplate.getBaseType() + "_" + 1);
		channelSchemePO.setChannelName(channelTemplate.getChannelName());
		channelSchemePO.setChannelTemplateID(channelTemplate.getId());
		return channelSchemePO;
	}

	// 生成设备类型编码通道(音频和视频)
	public List<ChannelSchemePO> createAudioAndVideoEncodeChannel(BundlePO bundle) {
		ChannelTemplatePO audioEncodeChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundle.getDeviceModel(), "VenusAudioIn");
		ChannelTemplatePO videoEncodeChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundle.getDeviceModel(), "VenusVideoIn");
		List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
		if (null != audioEncodeChannelTemplate && null != videoEncodeChannelTemplate) {
			channelSchemePOs.add(getChannelSchemePO(bundle.getBundleId(), audioEncodeChannelTemplate));
			channelSchemePOs.add(getChannelSchemePO(bundle.getBundleId(), videoEncodeChannelTemplate));
		}
		return channelSchemePOs;
	}

	// 生成设备类型解码通道(音频和视频)
	public List<ChannelSchemePO> createAudioAndVideoDecodeChannel(BundlePO bundle) {
		List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
		ChannelTemplatePO audioDecodeChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundle.getDeviceModel(), "VenusAudioOut");
		ChannelTemplatePO videoDecodeChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundle.getDeviceModel(), "VenusVideoOut");
		if (null != audioDecodeChannelTemplate && null != videoDecodeChannelTemplate) {
			channelSchemePOs.add(getChannelSchemePO(bundle.getBundleId(), audioDecodeChannelTemplate));
			channelSchemePOs.add(getChannelSchemePO(bundle.getBundleId(), videoDecodeChannelTemplate));
		}
		return channelSchemePOs;
	}

}
