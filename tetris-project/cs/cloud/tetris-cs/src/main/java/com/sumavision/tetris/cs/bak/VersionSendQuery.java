package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;

@Component
public class VersionSendQuery {
	@Autowired
	VersionSendDAO versionSendDao;

	/**
	 * 添加播发版本<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @param version 版本号
	 * @param broadId 播发交互id
	 * @param MediaCompressVO 播发tar包信息
	 * @param filePath 播发tar包存储地址
	 * @return List<ResourceSendPO> 地区列表
	 */
	public void addVersion(Long channelId, String version, String broadId, MediaCompressVO mediaCompress, String filePath) {
		VersionSendPO versionPO = new VersionSendPO();
		versionPO.setChannelId(channelId);
		versionPO.setVersion(version);
		versionPO.setBroadId(broadId);
		versionPO.setFileName(mediaCompress.getName());
		versionPO.setFileSize(mediaCompress.getSize());
		versionPO.setFilePath(filePath);

		versionSendDao.save(versionPO);
	}

	/**
	 * 根据频道id获取最后一个版本号<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return String 版本号
	 */
	public String getLastVersion(Long channelId) {
		List<VersionSendPO> versionList = versionSendDao.findByChannelId(channelId);
		String versionCode = "";
		if (versionList != null && versionList.size() > 0) {
			for (VersionSendPO item : versionList) {
				String itemVersionCode = item.getVersion().split("v")[1];
				try {
					if (versionCode.equals("") || Long.parseLong(versionCode) < Long.parseLong(itemVersionCode)) {
						versionCode = itemVersionCode;
					}
				} catch (NumberFormatException e) {
					break;
				}
			}
		}
		return versionCode.isEmpty() ? versionCode : "v" + versionCode;
	}
	
	/**
	 * 根据频道id获取播发交互id<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return String 播发交互id
	 */
	public String getBroadId(Long channelId) {
		String lastVersion = getLastVersion(channelId);
		return lastVersion.isEmpty() ? lastVersion :  channelId.toString() + lastVersion.split("v")[1];
	}
	
	/**
	 * 根据版本号获取频道id<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return String 播发交互id
	 */
	public Long getChannelId(String lastVersionNum){
		VersionSendPO versionSendPO = versionSendDao.findByBroadId(lastVersionNum);
		return versionSendPO != null ? versionSendPO.getChannelId() : null;
	}

	/**
	 * 创建新版本号<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param version 原版本号
	 * @return String 播发交互id
	 */
	public String getNewVersion(String version) {
		String newVersionCode = "v000001";
		if (version != null && !version.isEmpty()) {
			String versionCode = version.split("v")[1];
			Long newVersion = Long.parseLong(versionCode) + 1;
			newVersionCode = String.format("v" + "%06d", newVersion);
		}
		return newVersionCode;
	}
	
	/**
	 * 根据频道id创建最后版本信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return VersionSendPO 版本信息
	 */
	public VersionSendPO getLastVersionSendPO(Long channelId){
		String broadId = getBroadId(channelId);
		return broadId.isEmpty() ? null : versionSendDao.findByChannelIdAndBroadId(channelId, broadId);
	}
}
