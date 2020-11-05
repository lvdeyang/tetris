package com.sumavision.bvc.meeting.logic.record;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.feign.LiveAndAssetAuthServiceClient;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.meeting.logic.dao.OmcRecordDao;
import com.sumavision.bvc.meeting.logic.po.OmcRecordPO;
import com.sumavision.bvc.meeting.logic.record.mims.MimsService;
import com.sumavision.bvc.meeting.logic.record.omc.BoPO;
import com.sumavision.bvc.meeting.logic.record.omc.BoService;
import com.sumavision.bvc.meeting.logic.record.omc.CdnPO;
import com.sumavision.bvc.meeting.logic.record.omc.CdnService;
import com.sumavision.tetris.commons.util.date.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecordService {
	@Autowired DeviceGroupDAO deviceGroupDao;
	@Autowired BoService boService;
	@Autowired CdnService cdnService;
	@Autowired OmcRecordDao omcRecordDao;
	@Autowired ResourceServiceClient resourceServiceClient;
	@Autowired LiveAndAssetAuthServiceClient liveAndAssetAuthServiceClient;
	@Autowired MimsService mimsService;
	//对接BO后，想资源层添加直播/点播，设置权限，需要挪到业务
	@Autowired LiveAndAssetResourceAndAuth liveAndAssetResourceAndAuth;
	
	public void startOmcRecords(JSONObject combinedOperation) throws Exception{
		if(combinedOperation.containsKey("recordSet")){
			JSONArray recordSet = combinedOperation.getJSONArray("recordSet");
			for(int i=0; i<recordSet.size(); i++){
				JSONObject aRecordSet = recordSet.getJSONObject(i);
				startOmcRecord(aRecordSet);
			}
		}
	}
	
	public void stopOmcRecords(JSONObject combinedOperation){
		if(combinedOperation.containsKey("recordDel")){
			JSONArray recordDel = combinedOperation.getJSONArray("recordDel");
			for(int i=0; i<recordDel.size(); i++){
				JSONObject aRecordDel = recordDel.getJSONObject(i);
				stopOmcRecord(aRecordDel);
			}
		}
	}
	
	public void startOmcRecord(JSONObject aRecordSet) throws Exception{
		
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		BoPO boPo = BoPO.getFromOmcConfigFile();
		String useMsu = cdnPo.getUseMsu();
		
		String uuid = aRecordSet.getString("uuid");		
		//ts / rtp-ps / hls
		//format的赋值在MeetingEntity中也有同样代码
		String videoType = aRecordSet.getString("videoType");
		String format = "ts";
		//监控室录制，rtp-ps
		if(videoType.equals("2")){
			format = "rtp-ps";
		}
		//使用msu录制，则录hls
		if("true".equals(useMsu)){
			format = "hls";
		}
		aRecordSet.put("format", format);
		
		//为防止重名，修改videoName，后边加上时间
		String videoName = aRecordSet.getString("videoName");
		Date date = new Date();
		String startTimeStr = 	DateUtil.format(date, DateUtil.dateTimePattern).replace(" ", "-").replace(":", "");
		videoName = videoName + "-" + startTimeStr;//date.toString().replace(" ", "").replace(":", "");
		aRecordSet.put("videoName", videoName);
		
		String classification = "";
		String groupIdPrefix = "";
		//从group中读取“会议分类”，在videoName前边加上“会议分类_”的前缀再存储到数据库
		try{
			String groupUuid = aRecordSet.getString("groupUuid");
			DeviceGroupPO groupPO = deviceGroupDao.findByUuid(groupUuid);
			classification = groupPO.getDicProgramContent();//会议分类名
			if(classification==null || classification.equals("") || classification.equals("<default>") || classification.equals("默认")){
				classification = "";
			}else{
				classification = classification + "_";
			}
			groupIdPrefix = groupPO.getId() + "-";
		}catch(Exception e){			
		}
		
		String omcVideoName = classification + videoName;
				
		OmcRecordPO omcRecord = omcRecordDao.findByUuid(uuid);
		if(omcRecord != null) omcRecordDao.delete(omcRecord);
		
		OmcRecordPO po = new OmcRecordPO();
		po.setUuid(uuid);
//		po.setCdnRecvIp(cdnRecvIp);
//		po.setCdnRecvPort(cdnRecvPort);
		po.setCdnRecvFormat(format);
		po.setVideoType(videoType);
		po.setVideoName(omcVideoName);//这是添加了“会议分类_”的前缀、和时间后缀的name
		//使用msu录制hls的地址索引，求hashcode减小uuid的长度
		String urlIndex = groupIdPrefix + Integer.toString(uuid.hashCode()).replace("-", "m") + "/video";
		if(aRecordSet.containsKey("url"))
			urlIndex = aRecordSet.getString("url");
		String playUrl = "http://" + cdnPo.getMsuPlayIp() + ":" + cdnPo.getMsuPlayPort() + "/" + urlIndex + ".m3u8";
		if(aRecordSet.containsKey("playUrl"))
			playUrl = aRecordSet.getString("playUrl");
		if("true".equals(cdnPo.getUseMsu())){
			po.setPlayUrl(playUrl);
		}
		omcRecordDao.save(po);
		
		if("true".equals(cdnPo.getUseCdn())){
			//调用后，aRecordSet会增加 url（收流地址）、playUrl（直播地址）、offsetPlayUrl（时移地址）、cdnChannelId（CDN生成的channelID，用于拼接播放地址，和停止CDN录制）
			try{
				cdnService.startIpcUploadRequest(aRecordSet);
			}catch(Exception e){
				throw e;
			}
		}
		
		if("true".equals(cdnPo.getUseMsu())){
			//使用msu本地录制
			aRecordSet.put("url", urlIndex);
			aRecordSet.put("playUrl", playUrl);
			aRecordSet.put("offsetPlayUrl", "");
			aRecordSet.put("cdnChannelId", "");
		}
		
		if("true".equals(boPo.getUseBo())){
			//调用后，aRecordSet会增加 playUrl（直播地址）、videoLiveID、autoChannelId(cid)
			boService.startBoRecord(aRecordSet);
		}
		
//		boService.boEpgNotification(aRecordSet);
		
		//添加资源和权限（需要在业务做）
		liveAndAssetResourceAndAuth.afterStartOmcRecord(aRecordSet);
		
	}
	
	public void stopOmcRecord(JSONObject aRecordDel){
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		BoPO boPo = BoPO.getFromOmcConfigFile();
		OmcRecordPO po = omcRecordDao.getByUuid(aRecordDel.getString("uuid"));
		if(null == po){
			log.error("该录制是无用通道，无需删除：uuid = " + aRecordDel.getString("uuid"));
			return;
		}else if("true".equals(cdnPo.getUseCdn()) && po.getCdnChannelId() == null){
			log.error("无法正常停止录制，数据库中cdn的channelId为null，没有成功开始录制：uuid = " + aRecordDel.getString("uuid"));
			return;
		}
		
		
		if("true".equals(cdnPo.getUseCdn())){
			cdnService.stopCdnRecord(aRecordDel);		
			//将录制转为点播，永久保存，否则默认7天后删除
			String transferToVod = aRecordDel.getString("transferToVod");
			if("1".equals(transferToVod)){
				cdnService.cdnTransferContent(aRecordDel);			
			}
		}
		
		if("true".equals(cdnPo.getUseMsu())){
			//使用msu本地录制
			aRecordDel.put("transferToVod", "1");
			aRecordDel.put("assetPlayUrl", po.getPlayUrl());	
		}
		
		if("true".equals(boPo.getUseBo())){
			//调用后，aRecordDel会增加 videoID、assetPlayUrl、autoVideoId(pid)
			boService.stopBoRecord(aRecordDel);
		}
		
		//添加资源和权限（需要在业务做）
		liveAndAssetResourceAndAuth.afterStopOmcRecord(aRecordDel, po);

		omcRecordDao.delete(po);		
	}	
}
