package com.sumavision.bvc.device.monitor.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.MediaPushSetBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.exception.AvtplNotFoundException;
import com.sumavision.bvc.device.monitor.exception.UserHasNoPermissionToRemoveMonitorRecordPlaybackTaskException;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdService;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeIpMissingException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodePortMissionException;
import com.sumavision.bvc.device.monitor.playback.exception.MonitorRecordPlaybackDstVideoCannotBeNullException;
import com.sumavision.bvc.device.monitor.playback.exception.MonitorRecordPlaybackTaskNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.ResourceNotExistException;
import com.sumavision.bvc.device.monitor.record.MonitorRecordDAO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 录制回放任务增删改业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午8:29:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorRecordPlaybackTaskService {
	
	@Autowired
	private MonitorRecordDAO monitorRecordDao;
	
	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private AVtplGearsDAO aVtplGearsDao;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private MonitorOsdService monitorOsdService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private BundleDao bundleDao;
	
	/**
	 * 添加调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午9:56:51
	 * @param Long fileId 文件id
	 * @param Long osdId osd id
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoBundleName 目的视频设备名称
	 * @param String dstVideoBundleType 目的视频设备类型
	 * @param String dstVideoLayerId 目的视频设备接入层
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstVideoBaseType 目的视频通道类型
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioBundleName 目的音频设备名称
	 * @param String dstAudioBundleType 目的音频设备类型
	 * @param String dstAudioLayerId 目的音频设备接入层
	 * @param String dstAudioChannelId 目的音频通道id
	 * @param String dstAudioBaseType 目的音频通道类型
	 * @param long userId 操作业务用户
	 * @return MonitorRecordPlaybackTaskPO 任务
	 */
	public MonitorRecordPlaybackTaskPO addTask(
			Long fileId,
			Long osdId,
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
			Long userId) throws Exception{
		
		if(dstVideoBundleId==null || "".equals(dstVideoBundleId)){
			throw new MonitorRecordPlaybackDstVideoCannotBeNullException();
		}
		
		//规划一下参数
		dstAudioBundleId = "".equals(dstAudioBundleId)?null:dstAudioBundleId;
		dstAudioChannelId = "".equals(dstAudioChannelId)?null:dstAudioChannelId;
		
		MonitorRecordPO file = monitorRecordDao.findOne(fileId);
		
		AccessNodeBO targetLayer = null;
		List<AccessNodeBO> layers = resourceService.queryAccessNodeByNodeUids(new ArrayListWrapper<String>().add(file.getStoreLayerId()).getList());
		if(layers==null || layers.size()<=0){
			throw new AccessNodeNotExistException(file.getStoreLayerId());
		}
		targetLayer = layers.get(0);
		if(targetLayer.getIp() == null){
			throw new AccessNodeIpMissingException(file.getStoreLayerId());
		}
		if(targetLayer.getPort() == null){
			throw new AccessNodePortMissionException(file.getStoreLayerId());
		}
		
		MonitorRecordPlaybackTaskPO task = null;
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		
		List<MonitorRecordPlaybackTaskPO> tasks = monitorRecordPlaybackTaskDao.findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelIdAndUserId(
				dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId.toString());
		
		if(tasks!=null && tasks.size()>0){
			task = tasks.get(0);
			targetAvtpl = avtplDao.findOne(task.getAvTplId());
			targetGear = aVtplGearsDao.findOne(task.getGearId());
		}else{
			task = new MonitorRecordPlaybackTaskPO();
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
		
		if(task.getId() != null){
			//删除调阅挂断设备
			logic = new LogicBO().setUserId(userId.toString());
			logic.setMediaPushDel(new ArrayListWrapper<MediaPushSetBO>().add(new MediaPushSetBO().setUuid(task.getUuid())).getList());
			DisconnectBundleBO disconnectDstVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																				  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				  .setBundleId(task.getDstVideoBundleId())
																			      .setBundle_type(task.getDstVideoBundleType())
																			      .setLayerId(task.getDstVideoLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstVideoBundle).getList());
			
			if(task.getDstAudioBundleId()!=null && !task.getDstVideoBundleId().equals(task.getDstAudioBundleId())){
				DisconnectBundleBO disconnectDstAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																					  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					  .setBundleId(task.getDstAudioBundleId())
																				      .setBundle_type(task.getDstAudioBundleType())
																				      .setLayerId(task.getDstAudioLayerId());
				logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstAudioBundle).getList());
			}
			executeBusiness.execute(logic, "点播系统，复用调阅任务先停任务：");
		}

		task.setFileUuid(file.getUuid());
		task.setFileName(file.getFileName());
		task.setType(MonitorRecordPlaybackTaskType.RECORD);
		task.setDstVideoBundleId(dstVideoBundleId);
		task.setDstVideoBundleName(dstVideoBundleName);
		task.setDstVideoBundleType(dstVideoBundleType);
		task.setDstVideoLayerId(dstVideoLayerId);
		task.setDstVideoChannelId(dstVideoChannelId);
		task.setDstVideoBaseType(dstVideoBaseType);
		task.setDstVideoChannelName(ChannelType.transChannelName(dstVideoChannelId));
		
		if(dstAudioBundleId != null){
			task.setDstAudioBundleId(dstAudioBundleId);
			task.setDstAudioBundleName(dstAudioBundleName);
			task.setDstAudioBundleType(dstAudioBundleType);
			task.setDstAudioLayerId(dstAudioLayerId);
			task.setDstAudioChannelId(dstAudioChannelId);
			task.setDstAudioBaseType(dstAudioBaseType);
			task.setDstAudioChannelName(ChannelType.transChannelName(dstAudioChannelId));
		}
		
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
		if(osd != null){
			task.setOsdId(osd.getId());
			task.setOsdUsername(osd.getUsername());
		}
		
		task.setUserId(userId.toString());
		task.setUpdateTime(new Date());
		monitorRecordPlaybackTaskDao.save(task);
		
		//开始调阅，呼叫设备，设备转发
		logic = new LogicBO().setUserId(userId.toString())
							 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//呼叫设备
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																	 .setLock_type("write")
																     .setBundleId(task.getDstVideoBundleId())
																     .setLayerId(task.getDstVideoLayerId())
																     .setBundle_type(task.getDstVideoBundleType());
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(task.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(task.getDstVideoBaseType())
													      .setCodec_param(codec);
		ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("mediaPush")
															 	  .setUuid(task.getUuid());
		
		/** 设置osd */
		BundlePO videoBundle = bundleDao.findByBundleId(file.getVideoBundleId().indexOf("_")>=0?file.getVideoBundleId().split("_")[0]:file.getVideoBundleId());
		connectDstVideoChannel.setOsds(monitorOsdService.protocol(osd, videoBundle.getUsername(), file.getVideoBundleName()));
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		if(task.getDstAudioBundleId() != null){
			if(task.getDstVideoBundleId().equals(task.getDstAudioBundleId())){
				ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(task.getDstAudioChannelId())
															      .setChannel_status("Open")
															      .setBase_type(task.getDstAudioBaseType())
															      .setCodec_param(codec);
				ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("mediaPush")
					 	  												  .setUuid(task.getUuid());
				connectDstAudioChannel.setSource_param(audioForwardSetSrc);
				connectDstVideoBundle.getChannels().add(connectDstAudioChannel);
			}else{
				ConnectBundleBO connectDstAudioBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
						 													 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			 .setLock_type("write")
																		     .setBundleId(task.getDstAudioBundleId())
																		     .setLayerId(task.getDstAudioLayerId())
																		     .setBundle_type(task.getDstAudioBundleType());
				ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(task.getDstAudioChannelId())
															      .setChannel_status("Open")
															      .setBase_type(task.getDstAudioBaseType())
															      .setCodec_param(codec);
				ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("mediaPush")
																		  .setUuid(task.getUuid());
				connectDstAudioChannel.setSource_param(audioForwardSetSrc);
				connectDstAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstAudioChannel).getList());
				logic.getConnectBundle().add(connectDstAudioBundle);
			}
		}
		
		//调阅命令
//		String urlIndex = file.getId() + "-" + Integer.toString(file.getUuid().hashCode()).replace("-", "m") + "/video";
		String urlIndex = new StringBufferWrapper().append("http://")
												   .append(targetLayer.getIp())
												   .append(":")
												   .append(targetLayer.getPort())
												   .append("/")
												   .append(file.getPreviewUrl())
												   .toString();
		MediaPushSetBO mediaPushSet = new MediaPushSetBO().setUuid(task.getUuid())
														  .setFile_source(urlIndex)
														  .setCodec_param(codec);
		logic.setMediaPushSet(new ArrayListWrapper<MediaPushSetBO>().add(mediaPushSet).getList());
		
		executeBusiness.execute(logic, "点播系统，开始调阅：");
		
		return task;
		
	}
	
	/**
	 * 添加点播文件调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午9:56:51
	 * @param String resourceId 点播资源id
	 * @param Long osdId osd id
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoBundleName 目的视频设备名称
	 * @param String dstVideoBundleType 目的视频设备类型
	 * @param String dstVideoLayerId 目的视频设备接入层
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstVideoBaseType 目的视频通道类型
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioBundleName 目的音频设备名称
	 * @param String dstAudioBundleType 目的音频设备类型
	 * @param String dstAudioLayerId 目的音频设备接入层
	 * @param String dstAudioChannelId 目的音频通道id
	 * @param String dstAudioBaseType 目的音频通道类型
	 * @param long userId 操作业务用户
	 * @return MonitorRecordPlaybackTaskPO 任务
	 */
	public MonitorRecordPlaybackTaskPO addTask(
			String resourceId,
			Long osdId,
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
			Long userId) throws Exception{
		
		if(dstVideoBundleId==null || "".equals(dstVideoBundleId)){
			throw new MonitorRecordPlaybackDstVideoCannotBeNullException();
		}
		
		JSONObject file = resourceService.queryOnDemandResourceById(resourceId);
		if(file == null) throw new ResourceNotExistException(resourceId);
		
		MonitorRecordPlaybackTaskPO task = null;
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		
		List<MonitorRecordPlaybackTaskPO> tasks = monitorRecordPlaybackTaskDao.findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelIdAndUserId(
				dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId.toString());
		
		if(tasks!=null && tasks.size()>0){
			task = tasks.get(0);
			targetAvtpl = avtplDao.findOne(task.getAvTplId());
			targetGear = aVtplGearsDao.findOne(task.getGearId());
		}else{
			task = new MonitorRecordPlaybackTaskPO();
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
		
		if(task.getId() != null){
			//删除调阅挂断设备
			logic = new LogicBO().setUserId(userId.toString());
			logic.setMediaPushDel(new ArrayListWrapper<MediaPushSetBO>().add(new MediaPushSetBO().setUuid(task.getUuid())).getList());
			DisconnectBundleBO disconnectDstVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																				  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				  .setBundleId(task.getDstVideoBundleId())
																			      .setBundle_type(task.getDstVideoBundleType())
																			      .setLayerId(task.getDstVideoLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstVideoBundle).getList());
			
			if(task.getDstAudioBundleId()!=null && !task.getDstVideoBundleId().equals(task.getDstAudioBundleId())){
				DisconnectBundleBO disconnectDstAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																					  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																					  .setBundleId(task.getDstAudioBundleId())
																				      .setBundle_type(task.getDstAudioBundleType())
																				      .setLayerId(task.getDstAudioLayerId());
				logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstAudioBundle).getList());
			}
			executeBusiness.execute(logic, "点播系统，复用调阅任务先停任务：");
		}

		task.setFileUuid(file.getString("resourceId"));
		task.setFileName(file.getString("name"));
		task.setType(MonitorRecordPlaybackTaskType.IMPORT);
		task.setDstVideoBundleId(dstVideoBundleId);
		task.setDstVideoBundleName(dstVideoBundleName);
		task.setDstVideoBundleType(dstVideoBundleType);
		task.setDstVideoLayerId(dstVideoLayerId);
		task.setDstVideoChannelId(dstVideoChannelId);
		task.setDstVideoBaseType(dstVideoBaseType);
		task.setDstVideoChannelName(ChannelType.transChannelName(dstVideoChannelId));
		task.setDstAudioBundleId(dstAudioBundleId);
		task.setDstAudioBundleName(dstAudioBundleName);
		task.setDstAudioBundleType(dstAudioBundleType);
		task.setDstAudioLayerId(dstAudioLayerId);
		task.setDstAudioChannelId(dstAudioChannelId);
		task.setDstAudioBaseType(dstAudioBaseType);
		task.setDstAudioChannelName(ChannelType.transChannelName(dstAudioChannelId));
		task.setAvTplId(targetAvtpl.getId());
		task.setGearId(targetGear.getId());
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId==null?0:osdId);
		if(osd != null){
			task.setOsdId(osd.getId());
			task.setOsdUsername(osd.getUsername());
		}
		
		task.setUserId(userId.toString());
		task.setUpdateTime(new Date());
		monitorRecordPlaybackTaskDao.save(task);
		
		//开始调阅，呼叫设备，设备转发
		logic = new LogicBO().setUserId(userId.toString())
							 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//呼叫设备
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																	 .setLock_type("write")
																     .setBundleId(task.getDstVideoBundleId())
																     .setLayerId(task.getDstVideoLayerId())
																     .setBundle_type(task.getDstVideoBundleType());
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(task.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(task.getDstVideoBaseType())
													      .setCodec_param(codec);
		ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("mediaPush")
															 	  .setUuid(task.getUuid());
		//这个地方就不传字幕了
		connectDstVideoChannel.setOsds(monitorOsdService.protocol(osd, null, null));
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		if(task.getDstAudioBundleId() != null){
			if(task.getDstVideoBundleId().equals(task.getDstAudioBundleId())){
				ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(task.getDstAudioChannelId())
															      .setChannel_status("Open")
															      .setBase_type(task.getDstAudioBaseType())
															      .setCodec_param(codec);
				ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("mediaPush")
					 	  												  .setUuid(task.getUuid());
				connectDstAudioChannel.setSource_param(audioForwardSetSrc);
				connectDstVideoBundle.getChannels().add(connectDstAudioChannel);
			}else{
				ConnectBundleBO connectDstAudioBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																			 .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			 .setLock_type("write")
																		     .setBundleId(task.getDstAudioBundleId())
																		     .setLayerId(task.getDstAudioLayerId())
																		     .setBundle_type(task.getDstAudioBundleType());
				ConnectBO connectDstAudioChannel = new ConnectBO().setChannelId(task.getDstAudioChannelId())
															      .setChannel_status("Open")
															      .setBase_type(task.getDstAudioBaseType())
															      .setCodec_param(codec);
				ForwardSetSrcBO audioForwardSetSrc = new ForwardSetSrcBO().setType("mediaPush")
																		  .setUuid(task.getUuid());
				connectDstAudioChannel.setSource_param(audioForwardSetSrc);
				connectDstAudioBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstAudioChannel).getList());
				logic.getConnectBundle().add(connectDstAudioBundle);
			}
		}
		
		//调阅命令
//		String urlIndex = file.getId() + "-" + Integer.toString(file.getUuid().hashCode()).replace("-", "m") + "/video";
		String urlIndex = file.getString("previewUrl");
		MediaPushSetBO mediaPushSet = new MediaPushSetBO().setUuid(task.getUuid())
														  .setFile_source(urlIndex)
														  .setCodec_param(codec);
		logic.setMediaPushSet(new ArrayListWrapper<MediaPushSetBO>().add(mediaPushSet).getList());
		
		executeBusiness.execute(logic, "点播系统，开始调阅：");
		
		return task;
		
	}
	
	/**
	 * 修改调阅任务字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午9:07:37
	 * @param Long taskId 调阅任务id
	 * @param Long osdId 字幕id
	 * @param Long userId 业务用户id
	 * @return MonitorOsdPO 字幕
	 */
	public MonitorOsdPO changeOsd(Long taskId, Long osdId, Long userId) throws Exception{
		
		MonitorRecordPlaybackTaskPO task = monitorRecordPlaybackTaskDao.findOne(taskId);
		if(task == null){
			throw new MonitorRecordPlaybackTaskNotExistException(taskId);
		}
		if(!task.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionToRemoveMonitorRecordPlaybackTaskException(task.getId(), userId);
		}
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId);
		if(osd == null){
			throw new MonitorOsdNotExistException(osdId);
		}
		
		task.setOsdId(osd.getId());
		task.setOsdUsername(osd.getUsername());
		monitorRecordPlaybackTaskDao.save(task);
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 					 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		AvtplPO targetAvtpl = avtplDao.findOne(task.getAvTplId());
		AvtplGearsPO targetGear = aVtplGearsDao.findOne(task.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	 .setLock_type("write")
																	 .setBundleId(task.getDstVideoBundleId())
																     .setLayerId(task.getDstVideoLayerId())
																     .setBundle_type(task.getDstVideoBundleType());
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(task.getDstVideoChannelId())
												      	  .setChannel_status("Open")
													      .setBase_type(task.getDstVideoBaseType())
													      .setCodec_param(codec);
		/*ForwardSetSrcBO videoForwardSetSrc = new ForwardSetSrcBO().setType("mediaPush")
														 	  .setUuid(task.getUuid());
		connectDstVideoChannel.setSource_param(videoForwardSetSrc);*/
		
		/** 设置osd */
		MonitorRecordPO file = monitorRecordDao.findByUuid(task.getFileUuid());
		BundlePO videoBundle = bundleDao.findByBundleId(file.getVideoBundleId());
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		//清除字幕
		connectDstVideoChannel.setOsds(monitorOsdService.clearProtocol(videoBundle.getUsername(), file.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：录制回放清除字幕");
		
		//设置字幕
		connectDstVideoChannel.setOsds(monitorOsdService.protocol(osd, videoBundle.getUsername(), file.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：录制回放设置字幕");
		
		return osd;
	}
	
	/**
	 * 停止调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月28日 下午4:56:07
	 * @param Long id 任务id
	 * @param Long userId 业务用户id
	 */
	public void remove(Long id, Long userId) throws Exception{
		
		MonitorRecordPlaybackTaskPO task = monitorRecordPlaybackTaskDao.findOne(id);
		
		if(task == null) return;
		
		if(!task.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionToRemoveMonitorRecordPlaybackTaskException(task.getId(), userId);
		}
		
		//清除字幕
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 					 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		AvtplPO targetAvtpl = avtplDao.findOne(task.getAvTplId());
		AvtplGearsPO targetGear = aVtplGearsDao.findOne(task.getGearId());
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	 .setLock_type("write")
																     .setBundleId(task.getDstVideoBundleId())
																     .setLayerId(task.getDstVideoLayerId())
																     .setBundle_type(task.getDstVideoBundleType());
		ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(task.getDstVideoChannelId())
													      .setChannel_status("Open")
													      .setBase_type(task.getDstVideoBaseType())
													      .setCodec_param(codec);
		
		MonitorRecordPO file = monitorRecordDao.findByUuid(task.getFileUuid());
		BundlePO videoBundle = bundleDao.findByBundleId(file.getVideoBundleId().indexOf("_")>=0?file.getVideoBundleId().split("_")[0]:file.getVideoBundleId());
		connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
		logic.getConnectBundle().add(connectDstVideoBundle);
		
		connectDstVideoChannel.setOsds(monitorOsdService.clearProtocol(videoBundle.getUsername(), file.getVideoBundleName()));
		executeBusiness.execute(logic, "点播系统：录制回放清除字幕");
		
		//删除调阅挂断设备
		logic = new LogicBO().setUserId(userId.toString());
		logic.setMediaPushDel(new ArrayListWrapper<MediaPushSetBO>().add(new MediaPushSetBO().setUuid(task.getUuid())).getList());
		DisconnectBundleBO disconnectDstVideoBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																			  .setBundleId(task.getDstVideoBundleId())
																		      .setBundle_type(task.getDstVideoBundleType())
																		      .setLayerId(task.getDstVideoLayerId());
		logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstVideoBundle).getList());
		
		if(task.getDstAudioBundleId()!=null && !task.getDstVideoBundleId().equals(task.getDstAudioBundleId())){
			DisconnectBundleBO disconnectDstAudioBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					  															  .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				  .setBundleId(task.getDstAudioBundleId())
																			      .setBundle_type(task.getDstAudioBundleType())
																			      .setLayerId(task.getDstAudioLayerId());
			logic.setDisconnectBundle(new ArrayListWrapper<DisconnectBundleBO>().add(disconnectDstAudioBundle).getList());
		}
		executeBusiness.execute(logic, "点播系统，停止调阅任务：");
		
		monitorRecordPlaybackTaskDao.delete(task);
	}
	
}
