package com.sumavision.tetris.cs.bak;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class VersionSendQuery {
	@Autowired
	private VersionSendDAO versionSendDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private Path path;

	/**
	 * 添加发布播发版本<br/>
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
	public void addVersion(
			Long channelId,
			String version,
			String broadId,
			MediaCompressVO mediaCompress,
			String filePath,
			VersionSendType type,
			String zoneStorePath,
			String zoneDownloadPath) {
		VersionSendPO versionPO = new VersionSendPO();
		versionPO.setChannelId(channelId);
		versionPO.setVersion(version);
		versionPO.setBroadId(broadId);
		if (mediaCompress != null) {
			versionPO.setFileName(mediaCompress.getName());
			versionPO.setFileSize(mediaCompress.getSize());
		}
		versionPO.setFilePath(filePath);
		versionPO.setFileType(type);
		versionPO.setZoneStorePath(zoneStorePath);
		versionPO.setZoneDownloadPath(zoneDownloadPath);

		versionSendDao.save(versionPO);
	}

	/**
	 * 添加同步数据播发版本<br/>
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
	public void addUpdateVersion(Long channelId, String version, String broadId, MediaCompressVO mediaCompress, String filePath) {
		VersionSendPO versionPO = new VersionSendPO();
		versionPO.setChannelId(channelId);
		versionPO.setVersion(version);
		versionPO.setBroadId(broadId);
		if (mediaCompress != null) {
			versionPO.setFileName(mediaCompress.getName());
			versionPO.setFileSize(mediaCompress.getSize());
		}
		versionPO.setFilePath(filePath);
		versionPO.setFileType(VersionSendType.Update);

		versionSendDao.save(versionPO);
	}
	
	/**
	 * 根据播发id获取播发版本信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午3:34:25
	 * @param String broadId 播发id
	 * @return VersionSendPO
	 */
	public VersionSendPO getFromBroadId(String broadId) throws Exception {
		return versionSendDao.findByBroadId(broadId);
	}
	
	/**
	 * 根据频道id获取最后一个版本信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return VersionSendPO 版本信息
	 */
	public VersionSendPO getLastVersionPO(Long channelId) throws Exception {
		List<VersionSendPO> versionList = versionSendDao.findByChannelIdOrderByBroadIdDesc(channelId);
		if (versionList == null || versionList.isEmpty()) {
			return null;
		} else {
			return versionList.get(0);
		}
	}
	
	/**
	 * 根据频道id获取最后一个非文件同步播发版本信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return VersionSendPO 版本信息
	 */
	public VersionSendPO getLastBroadVersionPO(Long channelId) throws Exception {
		List<VersionSendPO> versionList = versionSendDao.findByChannelIdAndFileTypeNotOrderByBroadIdDesc(channelId, VersionSendType.Update);
		if (versionList == null || versionList.isEmpty()) {
			return null;
		} else {
			return versionList.get(0);
		}
	}

	/**
	 * 根据频道id获取最后一个版本号<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return String 版本号
	 */
	public String getLastVersion(Long channelId) throws Exception{
//		List<VersionSendPO> versionList = versionSendDao.findByChannelId(channelId);
//		String versionCode = "";
//		if (versionList != null && versionList.size() > 0) {
//			for (VersionSendPO item : versionList) {
//				String itemVersionCode = item.getVersion().split("v")[1];
//				try {
//					if (versionCode.equals("") || Long.parseLong(versionCode) < Long.parseLong(itemVersionCode)) {
//						versionCode = itemVersionCode;
//					}
//				} catch (NumberFormatException e) {
//					break;
//				}
//			}
//		}
//		return versionCode.isEmpty() ? versionCode : "v" + versionCode;
		VersionSendPO lastVersion = getLastVersionPO(channelId);
		if (lastVersion == null) {
			return "";
		} else {
			return lastVersion.getVersion();
		}
	}
	
	/**
	 * 根据频道id获取最后一个发布播发版本号<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return String 版本号
	 */
	public String getLastBroadVersion(Long channelId) throws Exception {
//		List<VersionSendPO> versionList = versionSendDao.findByChannelId(channelId);
//		String versionCode = "";
//		if (versionList != null && versionList.size() > 0) {
//			for (VersionSendPO item : versionList) {
//				if (item.getFileType() == VersionSendType.Update) continue;
//				String itemVersionCode = item.getVersion().split("v")[1];
//				try {
//					if (versionCode.equals("") || Long.parseLong(versionCode) < Long.parseLong(itemVersionCode)) {
//						versionCode = itemVersionCode;
//					}
//				} catch (NumberFormatException e) {
//					break;
//				}
//			}
//		}
//		return versionCode.isEmpty() ? versionCode : "v" + versionCode;
		VersionSendPO lastVersion = getLastBroadVersionPO(channelId);
		if (lastVersion == null) {
			return "";
		} else {
			return lastVersion.getVersion();
		}
	}
	
	/**
	 * 根据频道id获取发布播发交互id<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return String 播发交互id
	 */
	public String getLastBroadId(Long channelId) throws Exception {
		VersionSendPO lastVersion = getLastBroadVersionPO(channelId);
		if (lastVersion != null) {
			String lastBroadId = lastVersion.getBroadId();
			if (lastBroadId != null && !lastBroadId.isEmpty()) {
				return lastBroadId;
			}
		}
		return "";
	}
	
	/**
	 * 根据版本号获取频道id<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return String 播发交互id
	 */
	public Long getChannelId(String broadId){
		VersionSendPO versionSendPO = versionSendDao.findByBroadId(broadId);
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
	public VersionSendPO getLastBroadVersionSendPO(Long channelId) throws Exception{
		String broadId = getLastBroadId(channelId);
		return broadId.isEmpty() ? null : versionSendDao.findByChannelIdAndBroadId(channelId, broadId);
	}
	
	/**
	 * 获取分片存放的本地地址和http请求<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午2:10:53
	 * @param ChannelPO channel 频道信息
	 */
	public Map<String, String> getZonePath(ChannelPO channel) throws Exception {
		UserVO user = userQuery.current();
		String separator = File.separator;
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper().append(webappPath)
												   .append("upload")
												   .append(separator)
												   .append("tmp")
												   .append(separator).append(user.getGroupName())
												   .append(separator).append(channel.getUuid())
												   .toString();
		Date date = new Date();
		String version = new StringBufferWrapper().append(ChannelPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(version).toString();
		File file = new File(folderPath);
		if(!file.exists()) file.mkdirs();
		//这个地方保证每个任务的路径都不一样
		Thread.sleep(1);
		
		String storePath = new StringBufferWrapper().append(folderPath)
												    .toString();
		
		String downloadUrl = new StringBufferWrapper().append("upload/tmp/")
												     .append(user.getGroupName())
												     .append("/")
												     .append(channel.getUuid())
												     .append("/")
												     .append(version)
												     .toString();
		return new HashMapWrapper<String, String>()
				.put("zoneStorePath", storePath)
				.put("zoneDownloadUrl", downloadUrl)
				.getMap();
	}
}
