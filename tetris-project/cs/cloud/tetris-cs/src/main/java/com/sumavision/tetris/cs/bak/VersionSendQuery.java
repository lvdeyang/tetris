package com.sumavision.tetris.cs.bak;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;

@Component
public class VersionSendQuery {
	@Autowired
	VersionSendDAO versionSendDao;

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
	
	public String getBroadId(Long channelId) {
		String lastVersion = getLastVersion(channelId);
		return lastVersion.isEmpty() ? lastVersion :  channelId.toString() + lastVersion.split("v")[1];
	}
	
	public Long getChannelId(String lastVersionNum){
		VersionSendPO versionSendPO = versionSendDao.findByBroadId(lastVersionNum);
		return versionSendPO != null ? versionSendPO.getChannelId() : null;
	}

	public String getNewVersion(String version) {
		String newVersionCode = "v000001";
		if (version != null && !version.isEmpty()) {
			String versionCode = version.split("v")[1];
			Long newVersion = Long.parseLong(versionCode) + 1;
			newVersionCode = String.format("v" + "%06d", newVersion);
		}
		return newVersionCode;
	}
	
	public VersionSendPO getLastVersionSendPO(Long channelId){
		String broadId = getBroadId(channelId);
		return broadId.isEmpty() ? null : versionSendDao.findByChannelIdAndBroadId(channelId, broadId);
	}
}
