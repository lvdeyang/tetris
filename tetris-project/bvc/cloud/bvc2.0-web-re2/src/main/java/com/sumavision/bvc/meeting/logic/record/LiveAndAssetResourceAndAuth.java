package com.sumavision.bvc.meeting.logic.record;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.auth.bo.SetUserAuthByUsernamesAssetBO;
import com.sumavision.bvc.device.auth.bo.SetUserAuthByUsernamesLiveBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PassByContentBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.RecordAssetDAO;
import com.sumavision.bvc.device.group.dao.RecordLiveChannelDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.RecordAssetPO;
import com.sumavision.bvc.device.group.po.RecordLiveChannelPO;
import com.sumavision.bvc.device.group.service.AuthorizationServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.resource.bo.AssetResourceBO;
import com.sumavision.bvc.device.resource.bo.CreateResourceBO;
import com.sumavision.bvc.device.resource.bo.LiveChannelResourceBO;
import com.sumavision.bvc.feign.LiveAndAssetAuthServiceClient;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.meeting.logic.po.OmcRecordPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LiveAndAssetResourceAndAuth {
	@Autowired ResourceServiceClient resourceServiceClient;
	@Autowired LiveAndAssetAuthServiceClient liveAndAssetAuthServiceClient;
	@Autowired DeviceGroupDAO deviceGroupDao;
	@Autowired DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	@Autowired AuthorizationServiceImpl authorizationServiceImpl;
	@Autowired RecordLiveChannelDAO recordLiveChannelDao;
	@Autowired RecordAssetDAO RecordAssetDao;
	@Autowired ExecuteBusinessProxy executeBusiness;
		
	//开始录制时，添加直播资源，添加直播权限
	public void afterStartOmcRecord(JSONObject aRecordSet){		
		//存储直播、权限到数据库
		String groupUuid = aRecordSet.getString("groupUuid");
		if(null == groupUuid) return;
		DeviceGroupPO groupPO = deviceGroupDao.findByUuid(groupUuid);
		if(null == groupPO) return;
		DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(groupUuid);
		//如果该会议没有权限就新建一个
		if(null == authorizationPO){
			List<String> bundleIds = new ArrayList<String>();
			try {
				authorizationPO = authorizationServiceImpl.save(groupPO.getId(), bundleIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		//添加RecordLiveChannelPO和authorizationPO
		RecordLiveChannelPO recordLiveChannelPO = new RecordLiveChannelPO();
		recordLiveChannelPO.setAuthorization(authorizationPO);
		recordLiveChannelPO.setRecordUuid(aRecordSet.getString("uuid"));
		recordLiveChannelPO.setName(aRecordSet.getString("videoName"));
		recordLiveChannelPO.setCid(aRecordSet.getString("autoChannelId"));
		recordLiveChannelPO.setVideoLiveID(aRecordSet.getString("videoLiveID"));
		recordLiveChannelPO.setPlayUrl(aRecordSet.getString("playUrl"));
		recordLiveChannelPO.setOffsetPlayUrl(aRecordSet.getString("offsetPlayUrl"));
		authorizationPO.getLiveChannels().add(recordLiveChannelPO);
		deviceGroupAuthorizationDao.save(authorizationPO);
		
		//推送
		if(authorizationPO.getAuthorizationMembers().size() > 0){
			LogicBO logic = new LogicBO();
			logic.setPass_by(new ArrayList<PassByBO>());
			for(DeviceGroupAuthorizationMemberPO member : authorizationPO.getAuthorizationMembers()){
				PassByBO passBy = new PassByBO().setType("playlist_change_notify")
												.setLayer_id(member.getLayerId())
												.setBundle_id(member.getBundleId())
												.setPass_by_content(new PassByContentBO());
				logic.getPass_by().add(passBy);
			}
			try {
				executeBusiness.execute(logic, "发布点播后，向终端推送“播放列表变更”消息：");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		
		//添加直播到资源
		LiveChannelResourceBO lBo = new LiveChannelResourceBO();
		lBo.setName(aRecordSet.getString("videoName"));
		lBo.setCid(aRecordSet.getString("autoChannelId"));
		lBo.setPlayUrl(aRecordSet.getString("playUrl"));
		lBo.setLocationID("");
		lBo.setCategoryLiveID("");
		CreateResourceBO newBo = new CreateResourceBO();
		newBo.getCreate_resource_request().setUserId(1L);
		newBo.getCreate_resource_request().setResource(lBo);
		try{
//			resourceServiceClient.createResource(newBo);
		}catch(Exception e){
//			e.printStackTrace();
			log.warn("创建直播资源的createResource报错");
		}
		
		//添加直播权限
		SetUserAuthByUsernamesLiveBO bo = new SetUserAuthByUsernamesLiveBO();
		bo.setCid(aRecordSet.getString("videoLiveID"));
		ArrayList<String> stringArray = new ArrayList<String>();
		//临时写死有权限的用户名为omcuser
		stringArray.add("omcuser");
		bo.setUsernames(stringArray);
		try{
//			liveAndAssetAuthServiceClient.setUserAuthByUsernames(bo);
		}catch(Exception e){
//			e.printStackTrace();
			log.warn("添加直播权限的setUserAuthByUsernames报错");
		}
	}
	
	//停止录制时，删除直播资源，删除直播权限，添加点播到资源，添加点播权限（仅演示时添加点播权限）
	public void afterStopOmcRecord(JSONObject aRecordDel, OmcRecordPO po){
		
		if(null != po){
			//删RecordLiveChannelPO
			String recordUuid = aRecordDel.getString("uuid");
			List<RecordLiveChannelPO> recordLiveChannelPOs = recordLiveChannelDao.findByRecordUuid(recordUuid);
			if(null != recordLiveChannelPOs){
				for(RecordLiveChannelPO live : recordLiveChannelPOs){
					live.setAuthorization(null);
				}
				recordLiveChannelDao.deleteInBatch(recordLiveChannelPOs);
			}

			//推送
			String groupUuid = aRecordDel.getString("groupUuid");
			if(null == groupUuid) return;
			DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(groupUuid);
			if(null == authorizationPO) return;
			if(null!=authorizationPO && authorizationPO.getAuthorizationMembers().size() > 0){
				LogicBO logic = new LogicBO();
				logic.setPass_by(new ArrayList<PassByBO>());
				for(DeviceGroupAuthorizationMemberPO member : authorizationPO.getAuthorizationMembers()){
					PassByBO passBy = new PassByBO().setType("playlist_change_notify")
													.setLayer_id(member.getLayerId())
													.setBundle_id(member.getBundleId())
													.setPass_by_content(new PassByContentBO());
					logic.getPass_by().add(passBy);
				}
				try {
					executeBusiness.execute(logic, "发布点播后，向终端推送“播放列表变更”消息：");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			//调用资源层 删除直播资源
			LiveChannelResourceBO lcBo=new LiveChannelResourceBO();
			lcBo.setCid(po.getBoAutoLiveId());
			try{
//				resourceServiceClient.deleteLiveChannel(lcBo);
			}catch(Exception e){
//				e.printStackTrace();
				log.warn("删除直播资源时deleteLiveChannel报错");
			}
			
			//调用资源层 删除直播权限
			SetUserAuthByUsernamesLiveBO bo = new SetUserAuthByUsernamesLiveBO();
			bo.setCid(po.getBoAutoLiveId());
			ArrayList<String> stringArray = new ArrayList<String>();
			bo.setUsernames(stringArray);
			try{
//				liveAndAssetAuthServiceClient.setUserAuthByUsernames(bo);
			}catch(Exception e){
//				e.printStackTrace();
				log.warn("清空直播权限时setUserAuthByUsernames报错");
			}
			
			String transferToVod = aRecordDel.getString("transferToVod");
			if("0".equals(transferToVod)){
				//不转为点播
				return;
			}
			
			//添加RecordAssetPO
			RecordAssetPO recordAssetPO = new RecordAssetPO();
			recordAssetPO.setAuthorization(authorizationPO);
			recordAssetPO.setName(po.getVideoName());
			recordAssetPO.setPid(aRecordDel.getString("autoVideoId"));
			recordAssetPO.setVideoID(aRecordDel.getString("videoID"));
			recordAssetPO.setAssetPlayUrl(aRecordDel.getString("assetPlayUrl"));
			authorizationPO.getAssets().add(recordAssetPO);
			deviceGroupAuthorizationDao.save(authorizationPO);
			
			//调用资源层 添加点播
			AssetResourceBO lBo = new AssetResourceBO();
			lBo.setName(po.getVideoName());
//			lBo.setPid(aRecordDel.getString("videoID"));
			lBo.setPid(aRecordDel.getString("autoVideoId"));//pid
			lBo.setAssetPlayUrl(aRecordDel.getString("assetPlayUrl"));
			lBo.setLocationID("");
			lBo.setCategoryID("");
			CreateResourceBO newBo = new CreateResourceBO();
			newBo.getCreate_resource_request().setUserId(1L);
			newBo.getCreate_resource_request().setResource(lBo);
			try{
//				resourceServiceClient.createResource(newBo);
			}catch(Exception e){
//				e.printStackTrace();
				log.warn("创建点播资源的createResource报错");
			}
			
			//调用资源层 添加点播权限（用户名：omcuser）
			SetUserAuthByUsernamesAssetBO bo2 = new SetUserAuthByUsernamesAssetBO();
			//bo2.setPid(aRecordDel.getString("videoID"));
			bo2.setPid(aRecordDel.getString("autoVideoId"));			
		
			ArrayList<String> stringArray2 = new ArrayList<String>();
			stringArray2.add("omcuser");
			bo2.setUsernames(stringArray2);
			try{
//				liveAndAssetAuthServiceClient.setUserAuthByUsernames(bo2);
			}catch(Exception e){
//				e.printStackTrace();
				log.warn("添加点播权限的setUserAuthByUsernames报错");
			}
		}		
		
	}	
}
