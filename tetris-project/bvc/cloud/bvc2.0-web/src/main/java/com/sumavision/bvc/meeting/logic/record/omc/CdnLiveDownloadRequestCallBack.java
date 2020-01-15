package com.sumavision.bvc.meeting.logic.record.omc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
//import com.suma.zk.application.media.cache.CdnPortTableCache;
//import com.suma.zk.application.media.dao.CdnPortTableDAO;
//import com.suma.zk.application.media.dao.MediaStoreDevDao;
//import com.suma.zk.application.media.dao.MonitorStoreDevDao;
//import com.suma.zk.application.media.po.MediaStoreDevPO;
//import com.suma.zk.application.media.po.MonitorStoreDevPO;

//import platform.base.util.SpringContextUtil;
import com.sumavision.bvc.communication.http.HttpCallBack;
import com.sumavision.bvc.meeting.logic.dao.OmcRecordDao;
import com.sumavision.bvc.meeting.logic.po.CombineVideoDstPO;
import com.sumavision.bvc.meeting.logic.po.OmcRecordPO;
import com.sumavision.tetris.commons.context.SpringContext;


public class CdnLiveDownloadRequestCallBack extends HttpCallBack<JSONObject, String, Object>{
//	@Autowired OmcRecordDao omcRecordDao;
	
	public CdnLiveDownloadRequestCallBack(JSONObject param1){
		super(param1);
	}
	
	public CdnLiveDownloadRequestCallBack(JSONObject param1, String param2){
		super(param1, param2);
	}

	public CdnLiveDownloadRequestCallBack(JSONObject param1, String param2, Object param3){
		super(param1, param2, param3);
	}

	@Override
	public void completed(Object result) {
		try{
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			System.out.println("cdn回复LiveDownloadRequest：" + res);
			String channelId = res.toString().split("channelID")[1].replace(">", "").replace("</", "");
			
			//TODO:channelId存入数据库
			JSONObject aRecordSet = this.getParam1();
			String uuid = aRecordSet.getString("uuid");
			OmcRecordDao omcRecordDao = SpringContext.getBean(OmcRecordDao.class);
//			OmcRecordDao omcRecordDao = (OmcRecordDao) this.getParam3();
			OmcRecordPO omcRecordPO = omcRecordDao.getByUuid(uuid);
			if(null == omcRecordPO){
				omcRecordPO = new OmcRecordPO();
				omcRecordPO.setUuid(uuid);
			}
			omcRecordPO.setCdnChannelId(channelId);
			omcRecordDao.save(omcRecordPO);
			
			
			//调用BO
			aRecordSet.put("cdnChannelId", channelId);
//			BoService boService = new BoService();
			BoService boService = SpringContext.getBean(BoService.class);
			boService.startBoRecord(aRecordSet);
			boService.boEpgNotification(aRecordSet);
			
//			OmcRecordDao omcRecordDao1 = (OmcRecordDao) this.getParam3();
//			MediaStoreDevDao mediaStoreDevDao = SpringContextUtil.getBean(MediaStoreDevDao.class);
//			MonitorStoreDevDao monitorStoreDevDao = SpringContextUtil.getBean(MonitorStoreDevDao.class);
//			
//			mediaStore.setChannelid(channelId);
//			mediaStoreDevDao.update(mediaStore);
//			
//			MonitorStoreDevPO monitorStoreDev = monitorStoreDevDao.get(mediaStore.getMonitorStoreId());
//			monitorStoreDev.setChannelid(channelId);
//			monitorStoreDevDao.update(monitorStoreDev);
//			
//			//持久化占用端口
//			CdnPortTableDAO cdnPortTableDAO = SpringContextUtil.getBean(CdnPortTableDAO.class);
//			cdnPortTableDAO.save(((Integer)this.getParam2()).intValue());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Exception ex) {
		ex.printStackTrace();
//		CdnPortTableCache portTable = CdnPortTableCache.getInstance();
//		portTable.releasePort(((Integer)this.getParam2()));
		System.out.println("cdn访问出错，端口回滚！");
		
		//测试代码
//		OmcRecordDao omcRecordDao = SpringContextUtil.getBean(OmcRecordDao.class);
//		JSONObject aRecordSet = this.getParam1();
//		String uuid = aRecordSet.getString("uuid");
//		OmcRecordDao omcRecordDao = (OmcRecordDao) this.getParam3();
//		OmcRecordPO omcRecordPO = omcRecordDao.getByUuid(uuid);
//		omcRecordPO.setCdnChannelId("xxxxxxxxxxxxxxxxxxx");
//		omcRecordDao.save(omcRecordPO);
//		aRecordSet.put("cdnChannelId", "xxxxxxxxxxxxxxxxxxx");
//		BoService boService = new BoService();
//		boService.startBoRecord(aRecordSet);
//		boService.boEpgNotification(aRecordSet);
	}

	@Override
	public void cancelled() {
//		CdnPortTableCache portTable = CdnPortTableCache.getInstance();
//		portTable.releasePort(((Integer)this.getParam2()));
		System.out.println("cdn访问链接关闭了，端口回滚！");
	}
	
}
