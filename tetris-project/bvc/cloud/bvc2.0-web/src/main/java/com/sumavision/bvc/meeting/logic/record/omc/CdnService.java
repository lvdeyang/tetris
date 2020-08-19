package com.sumavision.bvc.meeting.logic.record.omc;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.communication.http.HttpAsyncClient;
import com.sumavision.bvc.communication.http.HttpClient;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.meeting.logic.dao.OmcRecordDao;
import com.sumavision.bvc.meeting.logic.po.OmcRecordPO;
import com.sumavision.bvc.meeting.logic.record.omc.CdnLiveDownloadRequestCallBack;
import com.sumavision.bvc.meeting.logic.record.omc.CdnPO;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.date.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CdnService {
	@Autowired OmcRecordDao omcRecordDao;
	@Autowired DeviceGroupDAO deviceGroupDao;
	
	//LiveDownloadRequest，早先用于开始录制，后边可能弃用
	@Deprecated
	public void startCdnRecord(JSONObject aRecordSet){
		String uuid = aRecordSet.getString("uuid");
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		OmcRecordPO omcRecordPo = omcRecordDao.getByUuid(uuid);
		if(omcRecordPo == null){
			//TODO:错误
			System.out.println("startCdnRecord - LiveDownloadRequest 无法执行，数据库中未找到：" + uuid);
			return;
		}
		
//		//选择端口
		String usePort = omcRecordPo.getCdnRecvPort();
		//TODO:名称
		String channelName = omcRecordPo.getVideoName();//"bvc_request_channel";
		
		
//		//生成udp地址
		String udp = new StringBuilder().append("udp://")
											   .append(cdnPo.getIp())
											   .append(":")
											   .append(usePort)
											   .append("/")
											   .toString();
		////TODO:名称处理结束时间默认加一天
//		Date startTime = mediaStore.getStartTime();
//		Date endTime = DateUtil.addDay(mediaStore.getStartTime(), 1);
//		String startTimeStr = DateUtil.format(mediaStore.getStartTime(), DateUtil.dateTimePattern);
//		String stopTimeStr = DateUtil.format(endTime, DateUtil.dateTimePattern);
		Date date = new Date();
		String startTimeStr = 	DateUtil.format(date, DateUtil.dateTimeUTCPatten);
		Date stopDate = new Date();
		stopDate.setDate(stopDate.getDate()+1);
		String stopTimeStr = 	DateUtil.format(stopDate, DateUtil.dateTimeUTCPatten);
		
		
		String xml = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
												.append("<LiveDownloadRequest><channelID></channelID>") 
											   .append("<channelName>").append(channelName).append("</channelName>")
											   .append("<sourceURL>").append(udp).append("</sourceURL>")
											   .append("<startTime>").append(startTimeStr).append("</startTime>")
											   .append("<endTime>").append(stopTimeStr).append("</endTime>")
											   .append("<record>").append("1").append("</record>")
											   .append("</LiveDownloadRequest>")
											   .toString();
		
		String url = new StringBuilder().append("http://")
											   .append(cdnPo.getIp())
											   .append(":")
											   .append(cdnPo.getDevicePort())
											   .append("/LiveDownloadRequest")
											   .toString();
		
//		mediaStore.setUdp(udp);
		
		//LiveDownloadRequest
//		JSONObject callbackInfo = new JSONObject();
//		callbackInfo.put("uuid", uuid);
		System.out.println("cdn请求：LiveDownloadRequest");
		System.out.println("参数：" + xml);
		try {
			//在回调方法中需要使用omcRecordDao，没找到注入方法，所以在回调参数中传入omcRecordDao
			HttpAsyncClient.getInstance().xmlPost(url, null, xml, new CdnLiveDownloadRequestCallBack(aRecordSet, usePort, omcRecordDao));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* IpcUploadRequest，用于开始录制，通过此接口获取CDN收流地址及channelID，再给其推流
	 * 获得cdn的channelID，写入aRecordSet的cdnChannelId
	 * 获得cdn的收流地址，写入aRecordSet的url
	 */
	public void startIpcUploadRequest(JSONObject aRecordSet) throws Exception{
		String uuid = aRecordSet.getString("uuid");
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		OmcRecordPO omcRecordPo = omcRecordDao.getByUuid(uuid);
		if(omcRecordPo == null){
			//TODO:错误
			log.error("startIpcUploadRequest - IpcUploadRequest 无法执行，数据库中未找到omc录制信息，uuid = ：" + uuid);
			return;
		}
		
//			//选择端口
//		String usePort = omcRecordPo.getCdnRecvPort();
		//TODO:名称
		String channelName = omcRecordPo.getVideoName();//"bvc_request_channel";
		
		String format = aRecordSet.getString("format");
		String streamType = "TS/UDP";
		String meatData = "";
		
		//存储位置码
		String groupUuid = aRecordSet.getString("groupUuid");
		DeviceGroupPO groupPO = deviceGroupDao.findByUuid(groupUuid);
//		String areaCode = aRecordSet.getString("areaCode");
		String areaCode = groupPO.getDicStorageLocationCode();
		String areaCodeStr = "<areaCode>" + areaCode + "</areaCode>";
		if(areaCode==null || areaCode.equals("") || areaCode.equals("<default>") || areaCode.equals("默认")){
			areaCodeStr = "";
		}
		
		if(format.equals("rtp-ps")){
			streamType = "PS/RTP";
			meatData = "<metaData><video codec=\"H264\" pt=\"96\" payload=\"PS\" fps=\"25\" resolution=\"720*576\" timestamps=\"90000\"/>"
					+ "<audio codec=\"G711A\" pt=\"8\" payload=\"PS\" sample=\"48000\" channels=\"2\" timestamps=\"90000\"/></metaData>";
		}
		
		String xml = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
												.append("<IpcUploadRequest>")
												.append(areaCodeStr)
											   .append("<channelName>").append(channelName).append("</channelName>")
											   .append("<record>1</record>")
											   .append("<streamType>").append(streamType).append("</streamType>")
											   .append(meatData)//meatData
											   .append("</IpcUploadRequest>")
											   .toString();
		
		String url = new StringBuilder().append("http://")
											   .append(cdnPo.getIp())
											   .append(":")
											   .append(cdnPo.getDevicePort())
											   .append("/IpcUploadRequest")
											   .toString();		

		log.info("cdn请求：IpcUploadRequest，参数：" + xml);		
		String r = null;
		try {
			r = HttpClient.post(url, xml);
		} catch (Exception e) {
			e.printStackTrace();
			//TODO:返回失败
			throw e;
		}
		log.info("cdn回复IpcUploadRequest：" + r);
		String channelId = r.toString().split("channelID")[1].replace(">", "").replace("</", "");
		String uploadAddr = r.toString().split("uploadAddr")[1].replace(">", "").replace("</", "");
		String cdnRecvIp = uploadAddr.toString().split(":")[1].replace("/", "");
		String port1 = uploadAddr.toString().split(":")[2].split("-")[0];
		String port2 = uploadAddr.toString().split(":")[2].split("-")[1];
		
		String playUrl = new StringBuilder().append("http://")
				   .append(cdnPo.getPlayIp())
				   .append(":")
				   .append(cdnPo.getPlayPort())
				   .append("/ipvs/")
				   .append(channelId)
//				   .append(".m3u8?playtype=tvod&sid=xxx&param=xxx")
				   .append("/live.ts?playtype=live&sid=xxx&param=xxx")
				   .toString();		
		String hlsUrl = new StringBuilder().append("http://")
				   .append(cdnPo.getPlayIp())
				   .append(":")
				   .append(cdnPo.getPlayPort())
				   .append("/ipvs/")
				   .append(channelId)
				   .append(".m3u8?playtype=tstv&offset=60&sid=xxx&param=xxx")
				   .toString();
		
		omcRecordPo.setCdnChannelId(channelId);
		omcRecordPo.setCdnRecvIp(cdnRecvIp);
		omcRecordPo.setCdnRecvPort(port1);
		omcRecordPo.setCdnRecvPort2(port2);
		omcRecordPo.setPlayUrl(playUrl);
		omcRecordDao.save(omcRecordPo);
		
		//aRecordSet中加入channelId、发流地址url、播放地址
		String udp = new StringBuilder().append("udp://")
				   .append(cdnRecvIp)
				   .append(":")
				   .append(port1)
				   .append("/")
				   .toString();
		if(format.equals("rtp-ps")){
			udp = new StringBuilder().append("rtp-ps://")
					   .append(cdnRecvIp)
					   .append(":")
					   .append(port1)
					   .append("|")
					   .append(port2)
//					   .append("/")
					   .toString();
		}
		aRecordSet.put("cdnChannelId", channelId);
		aRecordSet.put("url", udp);
		aRecordSet.put("playUrl", playUrl);
		aRecordSet.put("offsetPlayUrl", hlsUrl);
	}
	
	//LiveStop
	public void stopCdnRecord(JSONObject aRecordDel){
		String uuid = aRecordDel.getString("uuid");
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		OmcRecordPO omcRecordPo = omcRecordDao.getByUuid(uuid);
		if(omcRecordPo == null){
			//TODO:错误
			System.out.println("stopCdnRecord - 逻辑层数据库中未找到：" + uuid + " 录制，无法执行LiveStop/LiveDelete");
			return;
		}
		
//		//获取channelId
		String channelId = omcRecordPo.getCdnChannelId();
		//名称
//		String channelName = omcRecordPo.getVideoName();
		//转换成点播则用LiveStop，不转换则用LiveDelete
		String stopOrDeleteStr = "LiveDelete";
		String transferToVod = aRecordDel.getString("transferToVod");
		if("1".equals(transferToVod)){
			stopOrDeleteStr = "LiveStop";
		}
		
		String xml = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
				   								.append("<").append(stopOrDeleteStr).append(">").append("<channelID>")
											   .append(channelId)
											   .append("</channelID>").append("</").append(stopOrDeleteStr).append(">")
											   .toString();
		
		String url = new StringBuilder().append("http://")
											   .append(cdnPo.getIp())
											   .append(":")
											   .append(cdnPo.getDevicePort())
											   .append("/")
											   .append(stopOrDeleteStr)
											   .toString();
		
//		mediaStore.setUdp(udp);
		
		//LiveStop
		//callbackInfo应携带数据库条目 或 端口信息，在callback中释放端口
		JSONObject callbackInfo = new JSONObject();
		log.info("cdn请求：" + stopOrDeleteStr + " 参数：" + xml);
//		System.out.println("参数：" + xml);
		try {
			HttpAsyncClient.getInstance().xmlPost(url, null, xml, new CdnLiveStopCallBack(callbackInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TransferContent
	//录制转为点播
	public void cdnTransferContent(JSONObject aRecordDel){
		String uuid = aRecordDel.getString("uuid");
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		OmcRecordPO omcRecordPo = omcRecordDao.getByUuid(uuid);
		if(omcRecordPo == null){
			//TODO:错误
			System.out.println("cdnTransferContent - TransferContent 无法执行，数据库中未找到：" + uuid);
			return;
		}
		
		
//		//TODO:sourceURL
		String sourceURL = omcRecordPo.getPlayUrl();
		sourceURL = new StringBuilder().append("http://")
				   .append(cdnPo.getIp())
				   .append(":")
				   .append(cdnPo.getPlayPort())
				   .append("/ipvs/")
				   .append(omcRecordPo.getCdnChannelId())
				   .append(".m3u8?playtype=tvod&sid=xxx&param=xxx")
//				   .append(".live.ts?playtype=live&sid=xxx&param=xxx")
				   .toString();
		//TODO:名称
		String contentName = omcRecordPo.getVideoName();
		String volumeName = cdnPo.getCiAdapterValueName();
		
		String providerID = "provider.com";
		//TODO:生成唯一assetID
		String assetID = DateUtil.format(new Date(), DateUtil.currentDateTimePattern);
		String subID1 = "800";
		String subID2 = "2000";
		String transferBitRate = "13000000";
		String responseURL = "http://192.168.1.1:8001/";
		String startNext = "false";
		String serviceType = "1";		
		
		String xml = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
												.append("<TransferContent ")
												.append("providerID=\"").append(providerID).append("\"").append(" ")
												.append("assetID=\"").append(assetID).append("\"").append(" ")
												.append("transferBitRate=\"").append(transferBitRate).append("\"").append(" ")
												.append("contentName=\"").append(contentName).append("\"").append(" ")
												.append("volumeName=\"").append(volumeName).append("\"").append(" ")
												.append("responseURL=\"").append(responseURL).append("\"").append(" ")
												.append("startNext=\"").append(startNext).append("\"").append(" ")
												.append(">")
												.append("<Input").append(" ")
												.append("subID=\"").append(subID1).append("\"").append(" ")
												.append("sourceURL=\"").append(sourceURL).append("\"").append(" ")
												.append("serviceType=\"").append(serviceType).append("\"").append(" ")
											   .append("/>")
											   .append("</TransferContent>")
											   .toString();
		
		String url = new StringBuilder().append("http://")
											   .append(cdnPo.getCiAdapterIp())
											   .append(":")
											   .append(cdnPo.getCiAdapterDevicePort())
											   .append("/TransferContent")
											   .toString();
		
		
		//LiveDownloadRequest
		JSONObject callbackInfo = new JSONObject();
		callbackInfo.put("uuid", uuid);
		System.out.println("cdn请求：TransferContent");
		System.out.println("参数：" + xml);
		try {
			HttpAsyncClient.getInstance().xmlPost(url, null, xml, new CdnTransferContentCallBack(callbackInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String chooseRecvPort(CdnPO cdnPo){
		List<OmcRecordPO> omcRecordPos = omcRecordDao.findAll();
		String udpStartPort = cdnPo.getUdpStartPort();
		String udpStopPort = cdnPo.getUdpStopPort();
		int startPort = Integer.parseInt(udpStartPort);
		int stopPort = Integer.parseInt(udpStopPort);
		int port = startPort;
		for(; port<=stopPort; port++){
			String portStr = String.valueOf(port);
			boolean useable = true;
			for(OmcRecordPO po : omcRecordPos){
				String poPort = po.getCdnRecvPort();
				if(portStr.equals(poPort)){
					useable = false;
					break;
				}
			}
			if(useable){
				return String.valueOf(port);
			}
		}
		return null;
	}
	
	public String generateUdpUrl(){
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		String usePort = chooseRecvPort(cdnPo);
		if(usePort == null){
			return null;
		}
		String udpUrl = new StringBuilder().append("udp://")
				   .append(cdnPo.getIp())
				   .append(":")
				   .append(usePort)
				   .append("/")
				   .toString();
		return udpUrl;
	}
	
	public String packUdpUrl(String ip, String port){
		String udpUrl = new StringBuilder().append("udp://")
				   .append(ip)
				   .append(":")
				   .append(port)
				   .append("/")
				   .toString();
		return udpUrl;
	}
}
