package com.sumavision.bvc.device.monitor.live;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.VenusSeeRelationPassByContentBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.exception.AvtplNotFoundException;
import com.sumavision.bvc.device.monitor.exception.UserHasNoPermissionToRemoveLiveDeviceException;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceService;
import com.sumavision.bvc.device.monitor.live.exception.MonitorLiveDstVideoBundleCannotBeNullException;
import com.sumavision.bvc.device.monitor.live.exception.MonitorLiveNotExistException;
import com.sumavision.bvc.device.monitor.live.exception.MonitorLiveSourceVideoBundleCannotBeNullException;
import com.sumavision.bvc.device.monitor.live.exception.UserCannotBeFoundException;
import com.sumavision.bvc.device.monitor.live.exception.UserEncoderCannotBeFoundException;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserService;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdService;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdNotExistException;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;

/**
 * 直播增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午8:21:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorLiveService {

	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private MonitorLiveDAO monitorLiveDao;
	
	@Autowired
	private MonitorLiveDeviceService monitorLiveDeviceService;
	
	@Autowired
	private MonitorLiveUserService monitorLiveUserService;
	
	@Autowired
	private AVtplGearsDAO aVtplGearsDao;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private MonitorOsdService monitorOsdService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	/**
	 * 点播本地用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 下午2:52:52
	 * @param osdId osd显示id
	 * @param UserBO localUser 本地用户
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备接入层
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备接入层
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param String type 业务类型 WEBSITE_PLAYER, DEVICE
	 * @param Long userId 当前操作业务用户
	 * @return MonitorLiveVO 直播任务
	 */
	@Deprecated
	public MonitorLivePO localUserOnDemand(
			Long osdId,
			UserBO localUser,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			String type,
			Long userId) throws Exception{
		
		if(localUser == null) throw new UserCannotBeFoundException();
		String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(localUser);
		if(encoderId == null) throw new UserEncoderCannotBeFoundException();
		
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(encoderId).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		return add(osdId, 
				srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcVideoChannel.getChannelId(), srcVideoChannel.getBaseType(), 
				srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcAudioChannel.getChannelId(), srcAudioChannel.getBaseType(), 
				dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, 
				dstVideoBaseType, dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType, 
				type, userId);
	}
	
	/**
	 * 点播xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 下午2:51:31
	 * @param osdId osd显示id
	 * @param xtUser xt用户
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备接入层
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备接入层
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param String type 业务类型 WEBSITE_PLAYER, DEVICE
	 * @param Long userId 当前操作业务用户
	 * @param String username 业务用户名
	 * @param String userno 业务用户号码
	 * @return MonitorLiveVO 直播任务
	 */
	@Deprecated
	public void xtUserOnDemand(
			Long osdId,
			UserBO xtUser,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			String type,
			Long userId,
			String username,
			String userno) throws Exception{
		
		if(xtUser == null) throw new UserCannotBeFoundException();
//		String encoderId = resourceQueryUtil.queryEncodeBundleIdByUserId(xtUser.getId());
		String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(xtUser);
		if(encoderId == null) throw new UserEncoderCannotBeFoundException();
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avTpls==null || avTpls.size()<=0){
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(encoderId).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setPass_by(new ArrayList<PassByBO>());
		
		VenusSeeRelationPassByContentBO passByContent = new VenusSeeRelationPassByContentBO().setType("play")
																							 .setUuid(UUID.randomUUID().toString())
																							 .setOperate("start")
																							 .setVparam(codec);
		
		JSONObject venus_user = new JSONObject();
		JSONObject venus_decoder = new JSONObject();
		JSONObject venus_vsource = new JSONObject();
		JSONObject venus_asource = new JSONObject();
		venus_user.put("username", username);
		venus_user.put("userno", userno);
		venus_user.put("decoder", venus_decoder);
		
		venus_decoder.put("vsource", venus_vsource);
		venus_decoder.put("asource", venus_asource);
		
		venus_vsource.put("layerid", dstVideoLayerId);
		venus_vsource.put("bundleid", dstVideoBundleId);
		venus_vsource.put("channelid", dstVideoChannelId);
		
		venus_asource.put("layerid", dstAudioLayerId);
		venus_asource.put("bundleid", dstAudioBundleId);
		venus_asource.put("channelid", dstAudioChannelId);
		
		JSONObject relation_user = new JSONObject();
		JSONObject relation_encoder = new JSONObject();
		JSONObject relation_vsource = new JSONObject();
		JSONObject relation_asource = new JSONObject();
		
		relation_user.put("username", xtUser.getName());
		relation_user.put("userno", xtUser.getUserNo());
		relation_user.put("encoder", relation_encoder);
		
		relation_encoder.put("vsource", relation_vsource);
		relation_encoder.put("asource", relation_asource);
		
		relation_vsource.put("layerid", srcBundleEntity.getAccessNodeUid());
		relation_vsource.put("bundleid", srcBundleEntity.getBundleId());
		relation_vsource.put("channelid", srcVideoChannel.getChannelId());
		
		relation_asource.put("layerid", srcBundleEntity.getAccessNodeUid());
		relation_asource.put("bundleid", srcBundleEntity.getBundleId());
		relation_asource.put("channelid", srcAudioChannel.getChannelId());
		
		passByContent.setVenus_user(venus_user)
					 .setRelation_user(relation_user);
		
		PassByBO passBy = new PassByBO().setLayer_id(srcBundleEntity.getAccessNodeUid())
									    .setType("venus_see_relation")
									    .setPass_by_content(passByContent);
		
		logic.getPass_by().add(passBy);
	
		executeBusiness.execute(logic, "点播系统：开始点播xt用户");
		
	}
	
	/**
	 * 停止点播xt用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 下午2:51:31
	 * @param xtUser xt用户
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备接入层
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备接入层
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param Long userId 当前操作业务用户
	 * @param String username 业务用户名
	 * @param String userno 业务用户号码
	 * @return MonitorLiveVO 直播任务
	 */
	@Deprecated
	public void stopXtUserOnDemand(
			UserBO xtUser,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			Long userId,
			String username,
			String userno) throws Exception{
		
		if(xtUser == null) throw new UserCannotBeFoundException();
//		String encoderId = resourceQueryUtil.queryEncodeBundleIdByUserId(xtUser.getId());
		String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(xtUser);
		if(encoderId == null) throw new UserEncoderCannotBeFoundException();
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avTpls==null || avTpls.size()<=0){
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(encoderId).getList());
		BundlePO srcBundleEntity = srcBundleEntities.get(0);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setPass_by(new ArrayList<PassByBO>());
		
		VenusSeeRelationPassByContentBO passByContent = new VenusSeeRelationPassByContentBO().setType("play").setOperate("stop").setVparam(codec);
		
		JSONObject venus_user = new JSONObject();
		JSONObject venus_decoder = new JSONObject();
		JSONObject venus_vsource = new JSONObject();
		JSONObject venus_asource = new JSONObject();
		venus_user.put("username", username);
		venus_user.put("userno", userno);
		venus_user.put("decoder", venus_decoder);
		
		venus_decoder.put("vsource", venus_vsource);
		venus_decoder.put("asource", venus_asource);
		
		venus_vsource.put("layerid", dstVideoLayerId);
		venus_vsource.put("bundleid", dstVideoBundleId);
		venus_vsource.put("channelid", dstVideoChannelId);
		
		venus_asource.put("layerid", dstAudioLayerId);
		venus_asource.put("bundleid", dstAudioBundleId);
		venus_asource.put("channelid", dstAudioChannelId);
		
		JSONObject relation_user = new JSONObject();
		JSONObject relation_encoder = new JSONObject();
		JSONObject relation_vsource = new JSONObject();
		JSONObject relation_asource = new JSONObject();
		
		relation_user.put("username", xtUser.getName());
		relation_user.put("userno", xtUser.getUserNo());
		relation_user.put("encoder", relation_encoder);
		
		relation_encoder.put("vsource", relation_vsource);
		relation_encoder.put("asource", relation_asource);
		
		relation_vsource.put("layerid", srcBundleEntity.getAccessNodeUid());
		relation_vsource.put("bundleid", srcBundleEntity.getBundleId());
		relation_vsource.put("channelid", srcVideoChannel.getChannelId());
		
		relation_asource.put("layerid", srcBundleEntity.getAccessNodeUid());
		relation_asource.put("bundleid", srcBundleEntity.getBundleId());
		relation_asource.put("channelid", srcAudioChannel.getChannelId());
		
		passByContent.setVenus_user(venus_user)
					 .setRelation_user(relation_user);
		
		PassByBO passBy = new PassByBO().setLayer_id(srcBundleEntity.getAccessNodeUid())
									    .setType("venus_see_relation")
									    .setPass_by_content(passByContent);
		
		logic.getPass_by().add(passBy);
	
		executeBusiness.execute(logic, "点播系统：停止点播xt用户");
		
	}
	
	
	/**
	 * 添加直播任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月26日 下午5:21:39
	 * @param String osdId osd显示id
	 * @param String videoBundleId 视频源设备id
	 * @param String videoBundleName 视频源设备名称
	 * @parma String videoBundleType 视频源设备类型
	 * @param String videoLayerId 视频源设备接入层
	 * @param String videoChannelId 视频源通道id
	 * @param String videoBaseType 视频源通道类型
	 * @param String audioBundleId 音频源设备id
	 * @param String audioBundleName 音频源设备名称
	 * @param String audioBundleType 音频源设备类型
	 * @param String audioLayerId 音频源设备接入层id
	 * @param String audioChannelId 音频源通道id
	 * @param String audioBaseType 音频源通道类型
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备接入层
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备接入层
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param String type 业务类型 WEBSITE_PLAYER, DEVICE
	 * @param Long userId 操作业务用户
	 * @return MonitorLivePO 直播任务
	 */
	@Deprecated
	public MonitorLivePO add(
			Long osdId,
			String videoBundleId,
			String videoBundleName,
			String videoBundleType,
			String videoLayerId,
			String videoChannelId,
			String videoBaseType,
			String audioBundleId,
			String audioBundleName,
			String audioBundleType,
			String audioLayerId,
			String audioChannelId,
			String audioBaseType,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			String type,
			Long userId) throws Exception{
		
		if(videoBundleId==null || "".equals(videoBundleId)){
			throw new MonitorLiveSourceVideoBundleCannotBeNullException();
		}
		
		if(dstVideoBundleId==null || "".equals(dstVideoBundleId)){
			throw new MonitorLiveDstVideoBundleCannotBeNullException();
		}
		
		//规范一下参数
		audioBundleId = "".equals(audioBundleId)?null:audioBundleId;
		audioChannelId = "".equals(dstAudioChannelId)?null:audioChannelId;
		dstAudioBundleId = "".equals(dstAudioBundleId)?null:dstAudioBundleId;
		dstAudioChannelId = "".equals(dstAudioChannelId)?null:dstAudioChannelId;
		if(audioBundleId!=null && dstAudioBundleId==null){
			audioBundleId = null;
			dstAudioBundleId = null;
		}
		if(audioBundleId==null && dstAudioBundleId!=null){
			audioBundleId = null;
			dstAudioBundleId = null;
		}
		
		Set<String> bundleIds = new HashSetWrapper<String>().add(videoBundleId).add(dstVideoBundleId).getSet();
		if(audioBundleId != null) bundleIds.add(audioBundleId);
		if(dstAudioBundleId != null) bundleIds.add(dstAudioBundleId);
		List<BundlePO> bundles = bundleDao.findInBundleIds(new ArrayListWrapper<String>().addAll(bundleIds).getList());
		
		MonitorLivePO live = null;
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		
		List<MonitorLivePO> tasks = monitorLiveDao.findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelIdAndUserId(
				dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId.toString());
		if(tasks!=null && tasks.size()>0){
			live = tasks.get(0);
			targetAvtpl = avtplDao.findOne(live.getAvTplId());
			targetGear = aVtplGearsDao.findOne(live.getGearId());
		}else{
			live = new MonitorLivePO();
			//查询codec参数
			List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
			if(avTpls==null || avTpls.size()<=0){
				throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
			}
			targetAvtpl = avTpls.get(0);
			//查询codec模板档位
			Set<AvtplGearsPO> gears = targetAvtpl.getGears();
			for(AvtplGearsPO gear:gears){
				targetGear = gear;
				break;
			}
		}
		
		LogicBO logic = null;
		
		if(live.getId() != null){
			//挂断设备停止转发
			logic = new LogicBO().setUserId(userId.toString());
			DisconnectBundleBO disconnectVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			   .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																			   .setBundleId(live.getVideoBundleId())
																			   .setBundle_type(live.getVideoBundleType())
																			   .setLayerId(live.getVideoLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectVideoBundle).getList());
			
			if(live.getAudioBundleId()!=null && !live.getVideoBundleId().equals(live.getAudioBundleId())){
				DisconnectBundleBO disconnectAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
						   														   .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				   .setBundleId(live.getAudioBundleId())
																				   .setBundle_type(live.getAudioBundleType())
																				   .setLayerId(live.getAudioLayerId());
				logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectAudioBundle).getList());
			}
			DisconnectBundleBO disconnectDstVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					   														      .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				  .setBundleId(live.getDstVideoBundleId())
																			      .setBundle_type(live.getDstVideoBundleType())
																			      .setLayerId(live.getDstVideoLayerId());
			logic.getDisconnectBundle().add(disconnectDstVideoBundle);
				
			if(live.getAudioBundleId()!=null && live.getDstAudioBundleId()!=null && !live.getDstVideoBundleId().equals(live.getDstAudioBundleId())){
				DisconnectBundleBO disconnectDstAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
						   															  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					  .setBundleId(live.getDstAudioBundleId())
																				      .setBundle_type(live.getDstAudioBundleType())
																				      .setLayerId(live.getDstAudioLayerId());
				logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstAudioBundle).getList());
			}
			executeBusiness.execute(logic, "点播系统，复用点播业务：");
		}
		
		live.setVideoSourceType(MonitorLiveSourceType.DEVICE);
		live.setVideoBundleId(videoBundleId);
		live.setVideoBundleName(videoBundleName);
		live.setVideoBundleType(videoBundleType);
		live.setVideoLayerId(videoLayerId);
		live.setVideoChannelId(videoChannelId);
		live.setVideoBaseType(videoBaseType);
		live.setVideoChannelName(ChannelType.transChannelName(videoChannelId));
		
		if(audioBundleId != null){
			live.setAudioSourceType(MonitorLiveSourceType.DEVICE);
			live.setAudioBundleId(audioBundleId);
			live.setAudioBundleName(audioBundleName);
			live.setAudioBundleType(audioBundleType);
			live.setAudioLayerId(audioLayerId);
			live.setAudioChannelId(audioChannelId);
			live.setAudioBaseType(audioBaseType);
			live.setAudioChannelName(audioChannelId==null?null:ChannelType.transChannelName(audioChannelId));
		}
		
		live.setDstVideoBundleId(dstVideoBundleId);
		live.setDstVideoBundleName(dstVideoBundleName);
		live.setDstVideoBundleType(dstVideoBundleType);
		live.setDstVideoLayerId(dstVideoLayerId);
		live.setDstVideoChannelId(dstVideoChannelId);
		live.setDstVideoBaseType(dstVideoBaseType);
		live.setDstVideoChannelName(ChannelType.transChannelName(dstVideoChannelId));
		
		if(dstAudioBundleId != null){
			live.setDstAudioBundleId(dstAudioBundleId);
			live.setDstAudioBundleName(dstAudioBundleName);
			live.setDstAudioBundleType(dstAudioBundleType);
			live.setDstAudioLayerId(dstAudioLayerId);
			live.setDstAudioChannelId(dstAudioChannelId);
			live.setDstAudioBaseType(dstAudioBaseType);
			live.setDstAudioChannelName(dstAudioChannelId==null?null:ChannelType.transChannelName(dstAudioChannelId));
		}
		
		live.setUserId(userId.toString());
		live.setType(MonitorLiveType.valueOf(type));
		live.setAvTplId(targetAvtpl.getId());
		live.setGearId(targetGear.getId());
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
		if(osd != null){
			live.setOsdId(osd.getId());
			live.setOsdUsername(osd.getUsername());
		}
		
		live.setUpdateTime(new Date());
		
		monitorLiveDao.save(live);
		
		//呼叫设备
		logic = new LogicBO().setUserId(userId.toString())
				 			 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//呼叫源--这里面先认为源和目的设备不一样
		ConnectBundleBO connectVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
														          .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																  .setLock_type("write")
																  .setBundleId(live.getVideoBundleId())
																  .setLayerId(live.getVideoLayerId())
																  .setBundle_type(live.getVideoBundleType())
																  .setPass_by_str(new PassByBO().setUsername(findUsername(bundles, live.getVideoBundleId()))
																		                        .setTargetUsername(findUsername(bundles, live.getDstVideoBundleId())));
		ConnectBO connectVideoChannel = new ConnectBO().setChannelId(live.getVideoChannelId())
													   .setChannel_status("Open")
													   .setBase_type(live.getVideoBaseType())
													   .setCodec_param(codec);
		connectVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectVideoChannel).getList());
		logic.getConnectBundle().add(connectVideoBundle);
		
		if(live.getAudioBundleId() != null){
			if(live.getVideoBundleId().equals(live.getAudioBundleId())){
				ConnectBO connectAudioChannel = new ConnectBO().setChannelId(live.getAudioChannelId())
															   .setChannel_status("Open")
															   .setBase_type(live.getAudioBaseType())
															   .setCodec_param(codec);
				connectVideoBundle.getChannels().add(connectAudioChannel);
			}else{
				ConnectBundleBO connectAudioBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
				          												  .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																		  .setLock_type("write")
																		  .setBundleId(live.getAudioBundleId())
																		  .setLayerId(live.getAudioLayerId())
																		  .setBundle_type(live.getAudioBundleType())
																		  .setPass_by_str(new PassByBO().setUsername(findUsername(bundles, live.getAudioBundleId()))
																				  						.setTargetUsername(findUsername(bundles, live.getDstAudioBundleId())));
							
				ConnectBO connectAudioChannel = new ConnectBO().setChannelId(live.getAudioChannelId())
															   .setChannel_status("Open")
															   .setBase_type(live.getAudioBaseType())
															   .setCodec_param(codec);
				connectAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectAudioChannel).getList());
				logic.getConnectBundle().add(connectAudioBundle);
			}
		}
		
		//呼叫目的
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
				  													 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																	 .setLock_type("write")
																     .setBundleId(live.getDstVideoBundleId())
																     .setLayerId(live.getDstVideoLayerId())
																     .setBundle_type(live.getDstVideoBundleType())
																     .setPass_by_str(new PassByBO().setUsername(findUsername(bundles, live.getDstVideoBundleId()))
																    		                       .setTargetUsername(findUsername(bundles, live.getVideoBundleId())));
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(live.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(live.getDstVideoBaseType())
													      .setCodec_param(codec);
		ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("channel")
															 	  .setBundleId(live.getVideoBundleId())
															 	  .setLayerId(live.getVideoLayerId())
															 	  .setChannelId(live.getVideoChannelId());
		//设置osd内容
		BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId());
		connectDstVideoChannel.setOsds(monitorOsdService.protocol(osd, videoBundle.getUsername(), live.getVideoBundleName()));
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		if(live.getAudioChannelId()!=null && live.getDstAudioBundleId()!=null){
			if(live.getDstVideoBundleId().equals(live.getDstAudioBundleId())){
				ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(live.getDstAudioChannelId())
															      .setChannel_status("Open")
															      .setBase_type(live.getDstAudioBaseType())
															      .setCodec_param(codec);
				ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("channel")
					 	  												  .setBundleId(live.getAudioBundleId())
					 	  												  .setLayerId(live.getAudioLayerId())
					 	  												  .setChannelId(live.getAudioChannelId());
				connectDstAudioChannel.setSource_param(audioForwardSetSrc);
				connectDstVideoBundle.getChannels().add(connectDstAudioChannel);
				
			}else{
				ConnectBundleBO connectDstAudioBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
						  													 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			 .setLock_type("write")
																		     .setBundleId(live.getDstAudioBundleId())
																		     .setLayerId(live.getDstAudioLayerId())
																		     .setBundle_type(live.getDstAudioBundleType())
																		     .setPass_by_str(new PassByBO().setUsername(findUsername(bundles, live.getDstAudioBundleId()))
																		    		                       .setTargetUsername(findUsername(bundles, live.getAudioBundleId())));
				ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(live.getDstAudioChannelId())
															      .setChannel_status("Open")
															      .setBase_type(live.getDstAudioBaseType())
															      .setCodec_param(codec);
				ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("channel")
																		  .setBundleId(live.getAudioBundleId())
																		  .setLayerId(live.getAudioLayerId())
																		  .setChannelId(live.getAudioChannelId());
				connectDstAudioChannel.setSource_param(audioForwardSetSrc);
				connectDstAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstAudioChannel).getList());
				logic.getConnectBundle().add(connectDstAudioBundle);
				
			}
		}
		
		executeBusiness.execute(logic, "点播系统，开始直播业务：");
		
		return live;
	}
	
	/**
	 * 停止直播任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月27日 上午9:57:27
	 * @param Long id 直播id
	 */
	@Deprecated
	public void remove(Long id, Long userId) throws Exception{
		
		MonitorLivePO live = monitorLiveDao.findOne(id);
		
		if(live == null) return;
		
		if(!live.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionToRemoveLiveDeviceException(id, userId);
		}
		
		//挂断设备先清除字幕
		Set<String> bundleIds = new HashSetWrapper<String>().add(live.getVideoBundleId()).add(live.getDstVideoBundleId()).getSet();
		List<BundlePO> bundles = bundleDao.findInBundleIds(new ArrayListWrapper<String>().addAll(bundleIds).getList());
		AvtplPO targetAvtpl = avtplDao.findOne(live.getAvTplId());
		AvtplGearsPO targetGear = aVtplGearsDao.findOne(live.getGearId());
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	 .setLock_type("write")
																     .setBundleId(live.getDstVideoBundleId())
																     .setLayerId(live.getDstVideoLayerId())
																     .setBundle_type(live.getDstVideoBundleType())
																     .setPass_by_str(new PassByBO().setUsername(findUsername(bundles, live.getDstVideoBundleId()))
																    		                       .setTargetUsername(findUsername(bundles, live.getVideoBundleId())));
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(live.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(live.getDstVideoBaseType())
													      .setCodec_param(codec);
		ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("channel")
															 	  .setBundleId(live.getVideoBundleId())
															 	  .setLayerId(live.getVideoLayerId())
															 	  .setChannelId(live.getVideoChannelId());
		
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId());
		
		//先发清除字幕
		connectDstVideoChannel.setOsds(monitorOsdService.clearProtocol(videoBundle.getUsername(), live.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：清除字幕");
		
		
		//挂断设备停止转发
		logic = new LogicBO().setUserId(userId.toString());
		DisconnectBundleBO disconnectVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																	       .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																		   .setBundleId(live.getVideoBundleId())
																		   .setBundle_type(live.getVideoBundleType())
																		   .setLayerId(live.getVideoLayerId());
		logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectVideoBundle).getList());
		
		if(live.getAudioBundleId()!=null && !live.getVideoBundleId().equals(live.getAudioBundleId())){
			DisconnectBundleBO disconnectAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
				       														   .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																			   .setBundleId(live.getAudioBundleId())
																			   .setBundle_type(live.getAudioBundleType())
																			   .setLayerId(live.getAudioLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectAudioBundle).getList());
		}
		DisconnectBundleBO disconnectDstVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
				   															  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																			  .setBundleId(live.getDstVideoBundleId())
																		      .setBundle_type(live.getDstVideoBundleType())
																		      .setLayerId(live.getDstVideoLayerId());
		
		logic.getDisconnectBundle().add(disconnectDstVideoBundle);
			
		if(live.getAudioBundleId()!=null && live.getDstAudioBundleId()!=null && !live.getDstVideoBundleId().equals(live.getDstAudioBundleId())){
			DisconnectBundleBO disconnectDstAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					   															  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				  .setBundleId(live.getDstAudioBundleId())
																			      .setBundle_type(live.getDstAudioBundleType())
																			      .setLayerId(live.getDstAudioLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstAudioBundle).getList());
		}
		executeBusiness.execute(logic, "点播系统，停止直播业务：");
		
		monitorLiveDao.delete(live);
	}
	
	/**
	 * 清除用户播放器所有直播调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月9日 上午11:31:36
	 * @param Long userId 用户id
	 */
	public void removeAllWebplayerLive(Long userId, String userno) throws Exception{
		List<PlayerBundleBO> webplayers = resourceQueryUtil.queryPlayerBundlesByUserId(userId);
		if(webplayers==null || webplayers.size()<=0) return;
		
		Set<String> bundleIds = new HashSet<String>();
		for(PlayerBundleBO webplayer:webplayers){
			bundleIds.add(webplayer.getBundleId());
		}
		
		monitorLiveDeviceService.stopWebPlayerLives(bundleIds, userId, userno);
		
		monitorLiveUserService.stopWebPlayerLives(bundleIds, userId, userno);
		
	}
	
	/**
	 * 修改osd模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午5:20:59
	 * @param Long liveId 直播任务id
	 * @param Long osdId 字幕id
	 * @param Long userId 业务用户id
	 */
	@Deprecated
	public MonitorOsdPO changeOsd(Long liveId, Long osdId, Long userId) throws Exception{
		
		MonitorLivePO live = monitorLiveDao.findOne(liveId);
		if(live == null){
			throw new MonitorLiveNotExistException(liveId);
		}
		if(!live.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionToRemoveLiveDeviceException(liveId, userId);
		}
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId);
		if(osd == null){
			throw new MonitorOsdNotExistException(osdId);
		}
		
		live.setOsdId(osd.getId());
		live.setOsdUsername(osd.getUsername());
		monitorLiveDao.save(live);
		
		Set<String> bundleIds = new HashSetWrapper<String>().add(live.getVideoBundleId()).add(live.getDstVideoBundleId()).getSet();
		List<BundlePO> bundles = bundleDao.findInBundleIds(new ArrayListWrapper<String>().addAll(bundleIds).getList());
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		targetAvtpl = avtplDao.findOne(live.getAvTplId());
		targetGear = aVtplGearsDao.findOne(live.getGearId());
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	 .setLock_type("write")
																     .setBundleId(live.getDstVideoBundleId())
																     .setLayerId(live.getDstVideoLayerId())
																     .setBundle_type(live.getDstVideoBundleType())
																     .setPass_by_str(new PassByBO().setUsername(findUsername(bundles, live.getDstVideoBundleId()))
																    		                       .setTargetUsername(findUsername(bundles, live.getVideoBundleId())));
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(live.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(live.getDstVideoBaseType())
													      .setCodec_param(codec);
		/*ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("channel")
															 	  .setBundleId(live.getVideoBundleId())
															 	  .setLayerId(live.getVideoLayerId())
															 	  .setChannelId(live.getVideoChannelId());
		
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);*/
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		BundlePO videoBundle = bundleDao.findByBundleId(live.getVideoBundleId());
		
		//先发清除
		connectDstVideoChannel.setOsds(monitorOsdService.clearProtocol(videoBundle.getUsername(), live.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：清除字幕");
		
		//后发设置
		connectDstVideoChannel.setOsds(monitorOsdService.protocol(osd, videoBundle.getUsername(), live.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：重设字幕");
		
		return osd;
	}
	
	/**
	 * 查找设备号码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月25日 下午4:56:00
	 * @param List<BundlePO> bundleScope 查找范围
	 * @param String bundleId 查找目标设备id
	 * @return String 设备号码
	 */
	private String findUsername(List<BundlePO> bundleScope, String bundleId){
		BundlePO targetBundle = null;
		if(bundleScope!=null && bundleScope.size()>0){
			for(BundlePO bundle:bundleScope){
				if(bundle.getBundleId().equals(bundleId)){
					targetBundle = bundle;
					break;
				}
			}
		}
		if(targetBundle != null) return targetBundle.getUsername();
		return null;
	}
	
}
