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


public class CdnTransferContentCallBack extends HttpCallBack<JSONObject, String, Object>{

	public CdnTransferContentCallBack(JSONObject param1){
		super(param1);
	}
	
	public CdnTransferContentCallBack(JSONObject param1, String param2){
		super(param1, param2);
	}

	public CdnTransferContentCallBack(JSONObject param1, String param2, Object param3){
		super(param1, param2, param3);
	}

	@Override
	public void completed(Object result) {
		try{
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			System.out.println("Cdn返回TransferContent：" + response.toString());
			System.out.println("Cdn回复TransferContent：" + res);
//			String channelId = res.toString().split("channelID")[1].replace(">", "").replace("</", "");
//			
//			//TODO:存入数据库
//			JSONObject callbackInfo = this.getParam1();
//			String uuid = callbackInfo.getString("uuid");
//			OmcRecordDao omcRecordDao = SpringContextUtil.getBean(OmcRecordDao.class);
//			OmcRecordPO omcRecordPO = omcRecordDao.getByUuid(uuid);
//			if(null == omcRecordPO){
//				omcRecordPO = new OmcRecordPO();
//				omcRecordPO.setUuid(uuid);
//			}
//			omcRecordPO.setChannelId(channelId);
//			omcRecordDao.save(omcRecordPO);
//			
//			
//			//TODO:调用BO
//			BoService boService = new BoService();
//			boService.startBoRecord();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Exception ex) {
		ex.printStackTrace();
//		CdnPortTableCache portTable = CdnPortTableCache.getInstance();
//		portTable.releasePort(((Integer)this.getParam2()));
		System.out.println("CdnTransferContent访问出错");
	}

	@Override
	public void cancelled() {
//		CdnPortTableCache portTable = CdnPortTableCache.getInstance();
//		portTable.releasePort(((Integer)this.getParam2()));
		System.out.println("CdnTransferContent访问链接关闭了");
	}
	
}
