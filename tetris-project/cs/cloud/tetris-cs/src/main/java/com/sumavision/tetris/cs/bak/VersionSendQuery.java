package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VersionSendQuery {
	@Autowired
	VersionSendDAO versionSendDao;

	public void addVersion(Long channelId, String version) {
		VersionSendPO versionPO = new VersionSendPO();
		versionPO.setChannelId(channelId);
		versionPO.setVersion(version);
		
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
		return versionCode == "" ? versionCode : "v" + versionCode;
	}
	
	public String getNewVersion(String version){
		String newVersionCode = "v000001";
		if(version != null && !version.isEmpty()){
			String versionCode = version.split("v")[1];
			Long newVersion = Long.parseLong(versionCode) + 1;
			newVersionCode = String.format("v" + "%06d", newVersion);
		}
		return newVersionCode;
	}
}
