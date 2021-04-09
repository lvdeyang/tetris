package com.sumavision.bvc.meeting.logic.record.mims;

import org.apache.http.message.BasicHttpResponse;

import com.alibaba.fastjson.JSON;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordFragmentDAO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.communication.http.HttpCallBack;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.PublishStreamDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.po.RecordLiveChannelPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.tetris.commons.context.SpringContext;

public class MimsResourceCallBack extends HttpCallBack<Long, Object, Object>{

	public MimsResourceCallBack(Long param1, Object param2) {
		super(param1, param2);
	}

	@Override
	public void completed(Object result) {
		try{
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			
			System.out.println("媒资返回：" + res);
//			临时对回调注释，不做处理
			Long publishId = this.getParam1();
			PublishStreamDAO publishStreamDao = SpringContext.getBean(PublishStreamDAO.class);
			
			String status = JSON.parseObject(res).getString("status");
			if(status.equals("200")){
				String data = JSON.parseObject(res).getString("data");
				Long id = JSON.parseObject(data).getLong("id");
				
				System.out.println("publishId:" + publishId + " id:" + id);
				
				PublishStreamPO publish = publishStreamDao.findOne(publishId);
				publish.setMimsId(id);
				
				publishStreamDao.save(publish);
				
				//加一条RecordLiveChannelPO
				RecordPO record = publish.getRecord();
				String groupUuid = record.getGroup().getUuid();
				DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao = SpringContext.getBean(DeviceGroupAuthorizationDAO.class);
				//调用前已经确定建立了authorizationPO，不会为null
				DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(groupUuid);	
				//添加RecordLiveChannelPO和authorizationPO
				RecordLiveChannelPO recordLiveChannelPO = new RecordLiveChannelPO();
				recordLiveChannelPO.setAuthorization(authorizationPO);
				recordLiveChannelPO.setRecordUuid(record.getUuid());
				recordLiveChannelPO.setName(record.getVideoName());
				recordLiveChannelPO.setCid(id.toString());
				recordLiveChannelPO.setPlayUrl(publish.getUrl());
				authorizationPO.getLiveChannels().add(recordLiveChannelPO);
				deviceGroupAuthorizationDao.save(authorizationPO);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}

	@Override
	public void failed(Exception ex) {
		System.out.println("媒资异常！");
		ex.printStackTrace();
		
	}

	@Override
	public void cancelled() {
		// TODO Auto-generated method stub
		
	}

}
