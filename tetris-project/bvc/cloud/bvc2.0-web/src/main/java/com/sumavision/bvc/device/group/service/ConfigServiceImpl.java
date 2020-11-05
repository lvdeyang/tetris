package com.sumavision.bvc.device.group.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.bo.CombineVideoBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PositionSrcBO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDAO;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.exception.ConfigHasNotSavedOrStartedException;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.CombineVideoPositionPO;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;

@Transactional(rollbackFor = Exception.class)
@Service
public class ConfigServiceImpl {

	@Autowired
	private DeviceGroupConfigVideoDAO deviceGroupConfigVideoDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/** 系统版本号 **/
	public static String version = null;
	
	/**
	 * @Title: 设置分屏轮询状态 <br/>
	 * @param videoId 视频id
	 * @param serialNum 屏幕序号
	 * @param pollingStatus 视频轮询状态
	 */
	public DeviceGroupConfigVideoPO setPollingStatus(
			Long videoId,
			int serialNum,
			String pollingStatus) throws Exception{
		
		DeviceGroupConfigVideoPO configVideo = deviceGroupConfigVideoDao.findOne(videoId);
		Set<DeviceGroupConfigVideoPositionPO> positions = configVideo.getPositions();
		DeviceGroupConfigVideoPositionPO targetPosition = null;
		if(positions!=null && positions.size()>0){
			for(DeviceGroupConfigVideoPositionPO position:positions){
				if(position.getSerialnum() == serialNum){
					targetPosition = position;
					break;
				}
			}
		}
		
		//配置还没有持久化，不存
		if(targetPosition == null) return configVideo;
		
		//配置与页面不一致，不存
		if(PictureType.STATIC.equals(targetPosition.getPictureType())) return configVideo;
		
		CombineVideoPO combineVideo = combineVideoDao.findByUuid(configVideo.getUuid());
		CombineVideoPositionPO targetCombineVideoPosition = null;
		if(combineVideo != null){
			Set<CombineVideoPositionPO> combineVideoPositions = combineVideo.getPositions();
			for(CombineVideoPositionPO combineVideoPosition:combineVideoPositions){
				if(combineVideoPosition.getSerialnum() == serialNum){
					targetCombineVideoPosition = combineVideoPosition;
					break;
				}
			}
		}
		
		boolean doProtocal = true;
		
		//没有发协议--只做保存
		if(targetCombineVideoPosition == null) doProtocal = false;
		
		//页面中的配置还没有执行--只做保存
		if(targetCombineVideoPosition!=null && PictureType.STATIC.equals(targetCombineVideoPosition.getPictureType())) doProtocal = false;
		
		//将状态存入数据库
		targetPosition.setPollingStatus(PollingStatus.valueOf(pollingStatus));
		deviceGroupConfigVideoDao.save(configVideo);
		
		if(targetCombineVideoPosition!=null && PictureType.POLLING.equals(targetCombineVideoPosition.getPictureType())){
			targetCombineVideoPosition.setPollingStatus(PollingStatus.valueOf(pollingStatus));
			combineVideoDao.save(combineVideo);
		}
		
		//这个地方往下发轮询切换协议
		if(doProtocal){
			System.out.println(pollingStatus);
		}
		
		return configVideo;
	}
	
	/**
	 * @Title: 切换到轮询中的下一画面 <br/>
	 * @param videoId 视频id
	 * @param serialNum 屏幕序号
	 * @param pollingStatus 视频轮询状态
	 */
	public DeviceGroupConfigVideoPO switchPollingNext(
			Long videoId,
			int serialNum) throws Exception{
		
		DeviceGroupConfigVideoPO configVideo = deviceGroupConfigVideoDao.findOne(videoId);
		Set<DeviceGroupConfigVideoPositionPO> positions = configVideo.getPositions();
		DeviceGroupConfigVideoPositionPO targetPosition = null;
		if(positions!=null && positions.size()>0){
			for(DeviceGroupConfigVideoPositionPO position:positions){
				if(position.getSerialnum() == serialNum){
					targetPosition = position;
					break;
				}
			}
		}
		
		//配置还没有持久化，无法执行
		if(targetPosition == null)
			throw new ConfigHasNotSavedOrStartedException();
		
		//配置与页面不一致，无法执行
		if(PictureType.STATIC.equals(targetPosition.getPictureType()))
			throw new ConfigHasNotSavedOrStartedException();
		
		CombineVideoPO combineVideo = combineVideoDao.findByUuid(configVideo.getUuid());
		CombineVideoPositionPO targetCombineVideoPosition = null;
		if(combineVideo != null){
			Set<CombineVideoPositionPO> combineVideoPositions = combineVideo.getPositions();
			for(CombineVideoPositionPO combineVideoPosition:combineVideoPositions){
				if(combineVideoPosition.getSerialnum() == serialNum){
					targetCombineVideoPosition = combineVideoPosition;
					break;
				}
			}
		}
		
		//没有发协议，无法执行
		if(targetCombineVideoPosition == null)
			throw new ConfigHasNotSavedOrStartedException();
		
		//页面中的配置还没有执行，无法执行
		if(targetCombineVideoPosition!=null && PictureType.STATIC.equals(targetCombineVideoPosition.getPictureType()))
			throw new ConfigHasNotSavedOrStartedException();
		
		//生成协议
		PositionSrcBO aPosition = new PositionSrcBO();
		aPosition.set(targetCombineVideoPosition, 0, 0)
				.setSrc(null)
				.setOperation("next_mixer_video_loop_index");
		CombineVideoBO aCombineVideoSwitchToNextSource = new CombineVideoBO();
		aCombineVideoSwitchToNextSource.setCodec_param(null)
				.setUuid(configVideo.getUuid())
				.setPosition(new ArrayList<PositionSrcBO>())
				.getPosition().add(aPosition);
		LogicBO logic = new LogicBO();
		logic.setCombineVideoOperation(new ArrayList<CombineVideoBO>())
				.getCombineVideoOperation().add(aCombineVideoSwitchToNextSource);
		
		//调用逻辑层
		executeBusiness.execute(logic, "切换轮询到下一画面");
		
		return configVideo;
	}
	
	/**
	 * @Title: 切换到轮询中的下一画面 <br/>
	 * @param videoId 视频id
	 * @param serialNum 屏幕序号
	 * @param pollingStatus 视频轮询状态
	 */
	public DeviceGroupConfigVideoPO switchPollingIndex(
			Long videoId,
			int serialNum,
			int pollingIndex,
			JSONObject sourceParam) throws Exception{
		
		DeviceGroupConfigVideoPO configVideo = deviceGroupConfigVideoDao.findOne(videoId);
		Set<DeviceGroupConfigVideoPositionPO> positions = configVideo.getPositions();
		DeviceGroupConfigVideoPositionPO targetPosition = null;
		if(positions!=null && positions.size()>0){
			for(DeviceGroupConfigVideoPositionPO position:positions){
				if(position.getSerialnum() == serialNum){
					targetPosition = position;
					break;
				}
			}
		}
		
		//配置还没有持久化，无法执行
		if(targetPosition == null)
			throw new ConfigHasNotSavedOrStartedException();
		
		//配置与页面不一致，无法执行
		if(PictureType.STATIC.equals(targetPosition.getPictureType()))
			throw new ConfigHasNotSavedOrStartedException();	
		
		CombineVideoPO combineVideo = combineVideoDao.findByUuid(configVideo.getUuid());
		CombineVideoPositionPO targetCombineVideoPosition = null;
		if(combineVideo != null){
			Set<CombineVideoPositionPO> combineVideoPositions = combineVideo.getPositions();
			for(CombineVideoPositionPO combineVideoPosition:combineVideoPositions){
				if(combineVideoPosition.getSerialnum() == serialNum){
					targetCombineVideoPosition = combineVideoPosition;
					break;
				}
			}
		}
		
		//没有发协议，无法执行
		if(targetCombineVideoPosition == null)
			throw new ConfigHasNotSavedOrStartedException();
		
		//页面中的配置还没有执行，无法执行
		if(targetCombineVideoPosition!=null && PictureType.STATIC.equals(targetCombineVideoPosition.getPictureType()))
			throw new ConfigHasNotSavedOrStartedException();
		
		//从已经下协议的src中查找并判断是否一致（不是从config的表中）
		List<CombineVideoSrcPO> srcs = targetCombineVideoPosition.getSrcs();
		try{
			CombineVideoSrcPO switchSrcPO = srcs.get(pollingIndex);
			if(switchSrcPO.getBundleId().equals(sourceParam.getString("bundleId"))
					&& switchSrcPO.getChannelId().equals(sourceParam.getString("channelId"))
					&& switchSrcPO.getMemberId().toString().equals(sourceParam.getInteger("memberId").toString())
					&& switchSrcPO.getMemberChannelId().toString().equals((sourceParam.getInteger("channelMemberId")).toString())){
				
			}else{
				throw new ConfigHasNotSavedOrStartedException();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new ConfigHasNotSavedOrStartedException();
		}

		videoServiceImpl.refreshCombineVideoWithPollingIndex(targetPosition.getVideo().getConfig().getGroup(), combineVideo, targetCombineVideoPosition.getUuid(), pollingIndex, true);

		return configVideo;
	}
	
	public String getVersion() {
		if(version == null){
			try {
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("version.ini");
				version = new BufferedReader(new InputStreamReader(inputStream))
						  .lines().parallel().collect(Collectors.joining("\n"));
			} catch (Exception e) {
				version = "V1.3.1z";
			}
		}
		return version;
	}
	
}
