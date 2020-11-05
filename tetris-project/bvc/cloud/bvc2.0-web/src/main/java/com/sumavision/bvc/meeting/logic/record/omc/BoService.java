package com.sumavision.bvc.meeting.logic.record.omc;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.communication.http.HttpClient;
import com.sumavision.bvc.meeting.logic.dao.OmcRecordDao;
import com.sumavision.bvc.meeting.logic.po.OmcRecordPO;
import com.sumavision.bvc.meeting.logic.record.exception.CreateLocationAndClassfyErrorException;
import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.tetris.commons.util.date.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoService {
	@Autowired OmcRecordDao omcRecordDao;

	//BO发布直播频道
	//https://host:port/api/bo/cmc/general/videoOpeningNotification
	public void startBoRecord(JSONObject aRecordSet){
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		BoPO boPo = BoPO.getFromOmcConfigFile();

		//获取uuid
		String uuid = aRecordSet.getString("uuid");
		//获取channelID
		String channelId = aRecordSet.getString("cdnChannelId");
		//获取videoType
		String videoType = aRecordSet.getString("videoType");
		String videoName = aRecordSet.getString("videoName");
		
		//生成播放地址
		String playUrl = aRecordSet.getString("playUrl");
		String hlsUrl = aRecordSet.getString("offsetPlayUrl");
		
		//生成url
		String url = new StringBuilder().append("http://")
				   .append(boPo.getIp())
				   .append(":")
				   .append(boPo.getDevicePort())
				   .append("/api/bo/cmc/general/videoOpeningNotification")
				   .toString();
		
		//生成参数
		//参数只能是包含：中文字、英文字母、数字和下划线、+、-
		JSONObject params = new JSONObject();
		JSONObject info = new JSONObject();
		info.put("playUrl", hlsUrl+"|"+playUrl);
//		System.out.println(hlsUrl+"|"+playUrl+":very niu be Mr.huang");
		info.put("videoType", videoType);
//		Date date = new Date();
		info.put("videoName", videoName);// + "-" + date.toString().replace(" ", "").replace(":", ""));
		info.put("description", "");
		
		//locationID
		String locationID = null;
		JSONArray ids = new JSONArray();
		if(aRecordSet.containsKey("locationID")){
			locationID = aRecordSet.getString("locationID");
			if(null!=locationID && !locationID.equals("")){
				//locationID可能是由逗号分隔的多个ID
				String[] ida = locationID.split(",");
				for(String id : ida){
					if(id.equals("<default>")){
						ids.add("");
					}else{
						ids.add(id);
					}
				}
				//空字符串代表默认地区，若逗号出现在开头或结尾，会被split丢掉
				if(locationID.startsWith(",") || locationID.endsWith(",")){
					ids.add("");
				}
				info.put("locationIDList", ids);
			}else{
				locationID = null;
				ids.add("");
				info.put("locationIDList", ids);
			}
		}
		//videoCategoryID
		String categoryLiveID = null;
		if(aRecordSet.containsKey("categoryLiveID")){
			categoryLiveID = aRecordSet.getString("categoryLiveID");
			if(null!=categoryLiveID && !categoryLiveID.equals("") && !categoryLiveID.equals("<default>")){
				info.put("videoCategoryID", categoryLiveID);
			}else{
				categoryLiveID = null;
			}
		}
		//categoryID，只取出，不放入info，用于存数据库
		String categoryID = null;
		if(aRecordSet.containsKey("categoryID")){
			categoryID = aRecordSet.getString("categoryID");
			if(null!=categoryID && !categoryID.equals("")){				
			}else{
				categoryID = null;
			}
		}
		params.put("info", info);
		
		log.info("bo请求videoOpeningNotification：" + url + " 参数：" + params.toJSONString());
		
		String r = null;
		try {
			r = HttpClient.post(url, params.toJSONString());
		
		} catch (Exception e) {
			e.printStackTrace();
			log.error("bo请求 videoOpeningNotification 失败");
			return;
		}
		log.info("bo返回videoOpeningNotification：" + r);
		
		//返回后将videoLiveID和playUrl一起存入数据库
		try {
			JSONObject result = JSON.parseObject(r);
			String videoLiveID = result.getJSONObject("data").getJSONObject("result").getString("videoLiveID");
			//改一下名
			String autoChannelId = result.getJSONObject("data").getJSONObject("result").getString("autoChannelId");
	
			OmcRecordPO omcRecordPO = omcRecordDao.getByUuid(uuid);
			if(null == omcRecordPO){
				omcRecordPO = new OmcRecordPO();
				omcRecordPO.setUuid(uuid);
			}
			omcRecordPO.setBoAutoLiveId(autoChannelId);
			omcRecordPO.setBoVideoLiveID(videoLiveID);
			omcRecordPO.setPlayUrl(playUrl);
			omcRecordPO.setLocationID(locationID);
			omcRecordPO.setCategoryLiveID(categoryLiveID);
			omcRecordPO.setCategoryID(categoryID);
			omcRecordDao.save(omcRecordPO);
			
			//aRecordSet添加videoLiveID和playUrl
			aRecordSet.put("videoLiveID", videoLiveID);
			aRecordSet.put("autoChannelId", autoChannelId);
		    System.out.println("bo返回的channelID：******************************"+autoChannelId);
//			aRecordSet.put("playUrl", playUrl);
//			aRecordSet.put("offsetPlayUrl", hlsUrl);
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println("bo请求videoOpeningNotification返回的json解析出错，可能失败");
			log.error("bo请求videoOpeningNotification返回的json解析出错，可能失败");
		}
		
	}
		
	//https://host:port/api/bo/cmc/vod/general/mediaShowTime
	//必须先获取videoLiveID
	public void stopBoRecord(JSONObject aRecordDel){
		String uuid = aRecordDel.getString("uuid");
//		OmcRecordDao omcRecordDao = SpringContextUtil.getBean(OmcRecordDao.class);
		OmcRecordPO omcRecordPO = omcRecordDao.getByUuid(uuid);
		if(null == omcRecordPO){
			System.out.println("删除录制时BO出错，找不到数据库条目，uuid = " + uuid);
		}
		
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		BoPO boPo = BoPO.getFromOmcConfigFile();

		//获取videoLiveID
		String videoLiveID = omcRecordPO.getBoVideoLiveID();
		String videoLiveAutoId=omcRecordPO.getBoAutoLiveId();
		String channelId = omcRecordPO.getCdnChannelId();
		
		//获取videoType：操作类型，1表示下架直播频道并上架点播，2表示下架直播频道，3表示上架点播节目。
//		String videoType = omcRecordPO.getVideoType();		
		String videoType = "2";
		String transferToVod = aRecordDel.getString("transferToVod");
		if("1".equals(transferToVod)){
			videoType = "1";
		}
		
		String assetName = omcRecordPO.getVideoName();
		//获取stopType
		String stopType = "1";
		//点播分类
		String categoryID = omcRecordPO.getCategoryID();
		//地区
		String locationID = omcRecordPO.getLocationID();
		
//		String usePort = cdnPo.getUdpStartPort();
		
		//生成播放地址
		String playUrl = new StringBuilder().append("http://")
				   .append(cdnPo.getPlayIp())
				   .append(":")
				   .append(cdnPo.getPlayPort())
				   .append("/ipvs/")
				   .append(channelId)
				   .append(".m3u8?playtype=tvod&sid=xxx&param=xxx")
//				   .append("/live.ts?playtype=live&sid=xxx&param=xxx")
				   .toString();
		
		//生成url
		String url = new StringBuilder().append("http://")
				   .append(boPo.getIp())
				   .append(":")
				   .append(boPo.getDevicePort())
				   .append("/api/bo/cmc/vod/general/mediaShowTime")
				   .toString();
		
		//生成参数
		//参数只能是包含：中文字、英文字母、数字和下划线、+、-
		JSONObject params = new JSONObject();
		JSONObject info = new JSONObject();
		info.put("playUrl", playUrl);
//		Date date = new Date();
		info.put("assetName", assetName);// + "-" + date.toString().replace(" ", "").replace(":", ""));
		info.put("videoType", videoType);
		info.put("description", "vod");
		info.put("stopType", stopType);
		info.put("videoLiveID", videoLiveID);
//		info.put("ImageURL", "");
		//locationID
		if(null!=locationID && !locationID.equals("")){
//			info.put("locationID", locationID);//去掉，因为点播的locationID没有用
		}
		//videoCategoryID
		if(null!=categoryID && !categoryID.equals("") && !categoryID.equals("<default>")){
			info.put("categoryID", categoryID);
		}
		
		params.put("info", info);
		
		log.info("bo请求mediaShowTime：" + url + " 参数：" + params.toJSONString());
		
		String r = null;
		try {
			r = HttpClient.post(url, params.toJSONString());
			log.info("bo返回mediaShowTime：" + r);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("bo请求 mediaShowTime 失败");
			return;
		}
		
		if("0".equals(transferToVod)){
			//不转换为点播
			return;
		}
		
		try {
			JSONObject result = JSON.parseObject(r);
			String videoID = result.getJSONObject("data").getJSONObject("result").getString("videoID");
			String autoVideoId=result.getJSONObject("data").getJSONObject("result").getString("autoVideoId");
			//aRecordDel添加videoID和assetPlayUrl
			aRecordDel.put("videoID", videoID);
			aRecordDel.put("autoVideoId", autoVideoId);
			aRecordDel.put("assetPlayUrl", playUrl);
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println("bo请求mediaShowTime返回的json解析出错，可能失败");
			log.error("bo请求mediaShowTime返回的json解析出错，可能失败");
		}
	}
	
	//https://host:port/api/bo/cmc/vod/general/mediaShowTime
	//必须先获取videoLiveID
	public void boEpgNotification(JSONObject aRecordSet){
//		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		BoPO boPo = BoPO.getFromOmcConfigFile();

		//获取channelID
//		String channelId = null;
		String assetName = aRecordSet.getString("videoName");
//		String stopType = "1";
		String videoLiveID = aRecordSet.getString("videoLiveID");
		
		Date date = new Date();
		String startTime = 	DateUtil.format(date, DateUtil.currentDateTimePattern);
		Date stopDate = new Date();
		stopDate.setDate(stopDate.getDate()+1);
		String endTime = 	DateUtil.format(stopDate, DateUtil.currentDateTimePattern);
						
		//生成url
		String url = new StringBuilder().append("http://")
				   .append(boPo.getIp())
				   .append(":")
				   .append(boPo.getDevicePort())
				   .append("/api/bo/cmc/general/videoEPGNotification")
				   .toString();
		
		//生成参数
		JSONObject params = new JSONObject();
		JSONObject info = new JSONObject();
		info.put("epgName", assetName);
		info.put("startTime", startTime);
		info.put("endTime", endTime);
		info.put("videoLiveID", videoLiveID);
		params.put("info", info);
		
		System.out.println("bo请求videoEPGNotification：" + url);
		System.out.println("bo请求参数：" + params.toJSONString());
		
		String r = null;
		try {
			r = HttpClient.post(url, params.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("bo返回videoEPGNotification：" + r);
		
//		//处理结束时间默认加一天
//		Date endTime = DateUtil.addDay(mediaStore.getStartTime(), 1);

	}
	
	//新建地区
	//https://host:port/api/bo/gn/location/videoCreateLocation
	public JSONObject createLocation(JSONObject aRecordSet) throws Exception{
		BoPO boPo = BoPO.getFromOmcConfigFile();
		//生成url
		String url = new StringBuilder().append("http://")
				   .append(boPo.getIp())
				   .append(":")
				   .append(boPo.getDevicePort())
				   .append("/api/bo/gn/location/videoCreateLocation")
				   .toString();
		
		//生成参数
		String regionName = aRecordSet.getString("name");
		String regionCode = aRecordSet.getString("code");
		String ip = aRecordSet.getString("ip");;
		JSONObject params = new JSONObject();
		JSONObject info = new JSONObject();
		info.put("regionName", regionName);
		info.put("regionCode", regionCode);
		info.put("IP", ip);
		params.put("info", info);
		
		System.out.println("bo请求videoCreateLocation：" + url);
		System.out.println("bo请求参数：" + params.toJSONString());
		
		String r = null;
		try {
			r = HttpClient.post(url, params.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("bo返回videoCreateLocation：" + r);
		JSONObject obj=JSONObject.parseObject(r);
		
		//创建地区失败，抛出异常
		String errorMessage = obj.getString("errorMessage");
		if(!obj.getString("status").equals("0")){
			throw new CreateLocationAndClassfyErrorException(DicType.REGION, errorMessage);
		}
		
		return obj;
	}
	
	//新建频道分类
	//https://host:port/api/bo/cmc/general/videoChannelCategoryCreate
	public JSONObject createChannelCategory(JSONObject aRecordSet) throws Exception{
		JSONObject obj = new JSONObject();
		BoPO boPo = BoPO.getFromOmcConfigFile();
		
		if("true".equals(boPo.getUseBo())){
		//生成url
		String url = new StringBuilder().append("http://")
				   .append(boPo.getIp())
				   .append(":")
				   .append(boPo.getDevicePort())
				   .append("/api/bo/cmc/general/videoChannelCategoryCreate")
				   .toString();
		
		//生成参数
		String categoryName = aRecordSet.getString("name");
		JSONObject params = new JSONObject();
		JSONObject info = new JSONObject();
		info.put("categoryName", categoryName);
		params.put("info", info);
		
		System.out.println("bo请求videoChannelCategoryCreate：" + url);
		System.out.println("bo请求参数：" + params.toJSONString());
		
		String r = null;
		try {
			r = HttpClient.post(url, params.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("bo返回videoChannelCategoryCreate：" + r);
		obj = JSONObject.parseObject(r);
		
		//创建分类失败，抛出异常
		String errorMessage = obj.getString("errorMessage");
		if(!obj.getString("status").equals("0")){
			throw new CreateLocationAndClassfyErrorException(DicType.LIVE, errorMessage);
		}
		}//if("true".equals(boPo.getUseBo()))
		else{
			obj.put("data", new JSONObject());
			obj.getJSONObject("data").put("result", new JSONObject());
			obj.getJSONObject("data").getJSONObject("result").put("categoryLiveID", null);
		}
		
		return obj;
	}
	
	//新建媒资分类。可分多级，通过parentID关联到上一级
	//https://host:port/api/bo/cmc/vod/general/videoAddCategory
	public JSONObject createVideoCategory(JSONObject aRecordSet) throws Exception{
		BoPO boPo = BoPO.getFromOmcConfigFile();
		//生成url
		String url = new StringBuilder().append("http://")
				   .append(boPo.getIp())
				   .append(":")
				   .append(boPo.getDevicePort())
				   .append("/api/bo/cmc/vod/general/videoAddCategory")
				   .toString();
		
		//生成参数
		String categoryName = aRecordSet.getString("name");
		String categoryDesc = aRecordSet.getString("name");
		
		//locationID，测试locationIDList后删除
		String locationID = "";
		if(aRecordSet.containsKey("parentRegionCode")){
			locationID = aRecordSet.getString("parentRegionCode");
			if(locationID==null || locationID.equals("") || locationID.equals("<default>") || locationID.equals("默认")){
				locationID = "";
			}
		}
		//locationIDList，还需前端支持多选
		String parentRegionCode = null;
		JSONArray ids = new JSONArray();
		if(aRecordSet.containsKey("parentRegionCode")){
			parentRegionCode = aRecordSet.getString("parentRegionCode");
			if(null!=parentRegionCode && !parentRegionCode.equals("")){
				//parentRegionCode可能是由逗号分隔的多个ID
				String[] ida = parentRegionCode.split(",");
				for(String id : ida){
					if(id.equals("<default>") || id.equals("默认")){
						ids.add("");
					}else{
						ids.add(id);
					}
				}
				//空字符串代表默认地区，若逗号出现在开头或结尾，会被split丢掉
				if(parentRegionCode.startsWith(",") || parentRegionCode.endsWith(",")){
					ids.add("");
				}
			}else{
				parentRegionCode = null;
				ids.add("");
			}
		}
		//【test】
//		ids.add("md-loca_101_39088521-e3fb-44e5-b5af-e46ba71af209");
		
		String ImageURL = "";
		String parentID = "";
		if(aRecordSet.containsKey("parentID")){
			parentID = aRecordSet.getString("parentID");
			if(parentID==null || parentID.equals("")){
				parentID = "";
			}
		}		
		
		JSONObject params = new JSONObject();
		JSONObject info = new JSONObject();
		info.put("categoryName", categoryName);
		info.put("categoryDesc", categoryDesc);
		info.put("ImageURL", ImageURL);
		info.put("locationID", locationID);//测试locationIDList后删除
		if(ids.size() > 0){
			info.put("locationIDList", ids);
		}
		info.put("parentID", parentID);
		params.put("info", info);
		
		System.out.println("bo请求videoAddCategory：" + url);
		System.out.println("bo请求参数：" + params.toJSONString());
		
		String r = null;
		try {
			r = HttpClient.post(url, params.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("bo返回videoAddCategory：" + r);
		JSONObject obj=JSONObject.parseObject(r);
		
		//创建分类失败，抛出异常
		String errorMessage = obj.getString("errorMessage");
		if(!obj.getString("status").equals("0")){
			throw new CreateLocationAndClassfyErrorException(DicType.DEMAND, errorMessage);
		}
		
		return obj;
	}
}
