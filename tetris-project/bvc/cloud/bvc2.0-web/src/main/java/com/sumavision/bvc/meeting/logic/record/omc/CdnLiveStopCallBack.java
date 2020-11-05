package com.sumavision.bvc.meeting.logic.record.omc;

import org.apache.http.message.BasicHttpResponse;

import com.alibaba.fastjson.JSONObject;
//import com.suma.zk.application.media.cache.CdnPortTableCache;
//import com.suma.zk.application.media.dao.CdnPortTableDAO;
//import com.suma.zk.application.media.dao.MediaStoreDevDao;
//import com.suma.zk.application.media.dao.MonitorStoreDevDao;
//import com.suma.zk.application.media.po.MediaStoreDevPO;
//import com.suma.zk.application.media.po.MonitorStoreDevPO;

//import platform.base.util.SpringContextUtil;
import com.sumavision.bvc.communication.http.HttpCallBack;

public class CdnLiveStopCallBack extends HttpCallBack<JSONObject, Object, Object>{

	public CdnLiveStopCallBack(JSONObject param1){
		super(param1);
	}
	
	public CdnLiveStopCallBack(JSONObject param1, Object param2){
		super(param1, param2);
	}

	public CdnLiveStopCallBack(JSONObject param1, Object param2, Object param3){
		super(param1, param2, param3);
	}

	@Override
	public void completed(Object result) {
		try{
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			System.out.println("Cdn返回LiveStop/LiveDelete：" + response.toString());
			System.out.println("cdn返回LiveStop/LiveDelete：" + res);
			//删除端口占用			
//			MediaStoreDevPO mediaStore = (MediaStoreDevPO)this.getParam1();
//			int port = Integer.parseInt(mediaStore.getUdp().split(":")[2].replace("/", ""));
//			CdnPortTableCache portTable = CdnPortTableCache.getInstance();
//			portTable.releasePort(port);
//			CdnPortTableDAO cdnPortTableDAO = SpringContextUtil.getBean(CdnPortTableDAO.class);
//			cdnPortTableDAO.delete(port);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Exception ex) {
		ex.printStackTrace();
		System.out.println("cdn访问失败了！");
	}

	@Override
	public void cancelled() {
		System.out.println("cdn访问链接关闭了！");
	}
	
}
