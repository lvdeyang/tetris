package com.sumavision.bvc.control.device.monitor.live;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.control.device.monitor.device.ChannelVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.live.MonitorLiveSplitConfigDAO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceDAO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceQuery;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceService;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserDAO;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserQuery;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserService;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;

@Service
public class MonitorLiveUtil {

	@Autowired
	private ResourceRemoteService resourceRemoteService;

	@Autowired
	private MonitorLiveUtil monitorLiveService;
	
	@Autowired
	private MonitorLiveDeviceDAO monitorLiveDeviceDao;
	
	@Autowired
	private MonitorLiveUserDAO monitorLiveUserDao;
	
	@Autowired
	private MonitorLiveDeviceQuery monitorLiveDeviceQuery;
	
	@Autowired
	private MonitorLiveUserQuery monitorLiveUserQuery;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorLiveDeviceService monitorLiveDeviceService;
	
	@Autowired
	private MonitorLiveUserService monitorLiveUserService;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private MonitorLiveSplitConfigDAO monitorLiveSplitConfigDAO;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private ExtraInfoService extraInfoService;
	
	/**
	 * MonitorLiveController中的vodDevice方法的移植<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 上午10:04:28
	 * @param osdId
	 * @param srcType
	 * @param srcBundleId
	 * @param srcVideoChannelId
	 * @param srcAudioChannelId
	 * @param dstType
	 * @param dstBundleId
	 * @param dstVideoChannelId
	 * @param dstAudioChannelId
	 * @param type
	 * @param String userId 
	 * @param String userNo,
	 * @param String userName
	 * @return
	 * @throws Exception
	 */
	public MonitorLiveDevicePO vodDevice(
			Long osdId,
			String srcType,//BUNDLE/CHANNEL BUNDLE则自动选择编码通道（暂不支持CHANNEL）
			String srcBundleId,
			String srcVideoChannelId,
			String srcAudioChannelId,
			String dstType,//BUNDLE/CHANNEL BUNDLE则自动选择解码通道（暂不支持CHANNEL）
			String dstBundleId,
			String dstVideoChannelId,
			String dstAudioChannelId,
			String type,
			Long userId,
			String userNo,
			String userName) throws Exception{
		
		MonitorLiveDevicePO entity = null;
		
		//dst
		BundlePO dstBundle = bundleService.findByBundleId(dstBundleId);
		List<ChannelSchemeDTO> dstQueryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleId).getList(), 1);
		ChannelVO dstEncodeVideo = null;
		ChannelVO dstEncodeAudio = null;
		if(dstQueryChannels!=null && dstQueryChannels.size()>0){
			for(ChannelSchemeDTO channel:dstQueryChannels){
				if(dstEncodeVideo==null && "VenusVideoOut".equals(channel.getBaseType())){
					dstEncodeVideo = new ChannelVO().set(channel);
				}else if(dstEncodeAudio==null && "VenusAudioOut".equals(channel.getBaseType())){
					dstEncodeAudio = new ChannelVO().set(channel);
				}
			}
		}
		String dstVideoBundleId = dstBundle.getBundleId();
		String dstVideoBundleName = dstBundle.getBundleName();
		String dstVideoBundleType = dstBundle.getBundleType();
		String dstVideoLayerId = dstBundle.getAccessNodeUid();
		dstVideoChannelId = dstEncodeVideo.getChannelId();
		String dstVideoBaseType = dstEncodeVideo.getBaseType();
		String dstVideoChannelName = dstEncodeVideo.getName();
		String dstAudioBundleId = dstBundle.getBundleId();
		String dstAudioBundleName = dstBundle.getBundleName();
		String dstAudioBundleType = dstBundle.getBundleType();
		String dstAudioLayerId = dstBundle.getAccessNodeUid();
		dstAudioChannelId = dstEncodeAudio.getChannelId();
		String dstAudioBaseType = dstEncodeAudio.getBaseType();
		String dstAudioChannelName = dstEncodeAudio.getName();
		
		//src
		BundlePO srcBundle = bundleService.findByBundleId(srcBundleId);
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleId).getList(), 0);
		ChannelVO encodeVideo = null;
		ChannelVO encodeAudio = null;
		if(queryChannels!=null && queryChannels.size()>0){
			for(ChannelSchemeDTO channel:queryChannels){
				if(encodeVideo==null && "VenusVideoIn".equals(channel.getBaseType())){
					encodeVideo = new ChannelVO().set(channel);
				}else if(encodeAudio==null && "VenusAudioIn".equals(channel.getBaseType())){
					encodeAudio = new ChannelVO().set(channel);
				}
			}
		}
		String videoBundleId = srcBundle.getBundleId();
		String videoBundleName = srcBundle.getBundleName();
		String videoBundleType = srcBundle.getBundleType();
		String videoLayerId = srcBundle.getAccessNodeUid();
		String videoChannelId = encodeVideo.getChannelId();
		String videoBaseType = encodeVideo.getBaseType();
		String videoChannelName = encodeVideo.getName();
		String audioBundleId = srcBundle.getBundleId();
		String audioBundleName = srcBundle.getBundleName();
		String audioBundleType = srcBundle.getBundleType();
		String audioLayerId = srcBundle.getAccessNodeUid();
		String audioChannelId = encodeAudio.getChannelId();
		String audioBaseType = encodeAudio.getBaseType();
		String audioChannelName = encodeAudio.getName();
		
		if(!queryUtil.isLdapBundle(srcBundle)){
			entity = monitorLiveDeviceService.startLocalSeeLocal(
					osdId, 
					videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType, 
					audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType, 
					dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType, 
					dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType, 
					type, userId, userNo, 
					false, null);
		}else{
			
			videoLayerId = resourceRemoteService.queryLocalLayerId();
			videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
			audioLayerId = videoLayerId;
			audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
			
			entity = monitorLiveDeviceService.startLocalSeeXt(
					osdId, 
					videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType, 
					audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType, 
					dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType, 
					dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType, 
					type, userId, userNo);
		}

		operationLogService.send(userName, "新建转发", srcBundle.getBundleName() + " 转发给 " + dstBundle.getBundleName());
		
		return entity;
	}
}
