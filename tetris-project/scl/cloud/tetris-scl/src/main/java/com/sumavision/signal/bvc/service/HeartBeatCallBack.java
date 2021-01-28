package com.sumavision.signal.bvc.service;

import java.util.Map;

import org.apache.http.message.BasicHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.base.bo.BundleOfflineRequest;
import com.suma.venus.resource.base.bo.BundleOnlineRequest;
import com.suma.venus.resource.base.bo.GetBundleInfoRequest;
import com.sumavision.signal.bvc.entity.dao.HeartBeatBundleDAO;
import com.sumavision.signal.bvc.entity.enumeration.OnlineStatus;
import com.sumavision.signal.bvc.entity.po.HeartBeatBundlePO;
import com.sumavision.signal.bvc.feign.ResourceServiceClient;
import com.sumavision.signal.bvc.http.HttpCallBack;
import com.sumavision.signal.bvc.mq.bo.BundleBO;
import com.sumavision.tetris.commons.context.SpringContext;

/**
 * 心跳回调<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年8月2日 下午5:09:00
 */
public class HeartBeatCallBack extends HttpCallBack<Long, Object, Object, Object, Object, Object, Object>{
	
	private static final Logger LOG = LoggerFactory.getLogger(HeartBeatCallBack.class);

	public HeartBeatCallBack(Long param1, Object param2, Object param3, Object param4) {
		super(param1, param2, param3, param4);
	}

	@Override
	public void completed(Object result) {
		
		BasicHttpResponse response = (BasicHttpResponse)result;
		try {
			String res = this.parseResponse(response);
			
			Long id = this.getParam1();
			HeartBeatBundleDAO heartBeatBundleDao = SpringContext.getBean(HeartBeatBundleDAO.class);
			ResourceServiceClient resourceServiceClient = SpringContext.getBean(ResourceServiceClient.class);
			HeartBeatService heartBeatService = SpringContext.getBean(HeartBeatService.class);
			
			HeartBeatBundlePO bundle = heartBeatBundleDao.findById(id);
			
			if(res.equals("<html>0<*1*></html>")){
				//修改状态
				if(bundle.getOnlineStatus().equals(OnlineStatus.OFFLINE)){
					bundle.setOnlineStatus(OnlineStatus.ONLINE);
					heartBeatBundleDao.save(bundle);
					
					LOG.info("设备ip为：" + bundle.getDeviceIp() + "  在线");
					
					//在线状态上报
					BundleOnlineRequest onlineRequest = new BundleOnlineRequest();
					BundleBody body = new BundleBody();
					body.setBundle_id(bundle.getBundleId());
					body.setLayer_id(bundle.getLayerId());
					onlineRequest.setBundle_online_request(body);

					resourceServiceClient.bundleOnline(onlineRequest);
					
					GetBundleInfoRequest bundleInfoRequest = new GetBundleInfoRequest();
					BundleBody bundleBody = new BundleBody();
					bundleBody.setBundle_id(bundle.getBundleId());
					bundleInfoRequest.setGet_bundle_info_request(bundleBody);
					
					Map<String, Object> bundleResult = resourceServiceClient.getBundleInfo(bundleInfoRequest);
					BundleBO bundleInfo = JSONObject.parseObject(JSONObject.toJSONString(bundleResult.get("get_bundle_info_response")), BundleBO.class);
					
					if(bundle.getDeviceModel().equals("jv210")){
						heartBeatService.bundleJv210Resume(bundleInfo);
					}else if(bundle.getDeviceModel().equals("proxy")){
						heartBeatService.bundleProxyResume(bundleInfo);
					}

				}
				
			}else{
				//修改状态
				if(bundle.getOnlineStatus().equals(OnlineStatus.ONLINE)){
					bundle.setOnlineStatus(OnlineStatus.OFFLINE);
					heartBeatBundleDao.save(bundle);
					
					//离线状态上报
					BundleOfflineRequest offlineRequest = new BundleOfflineRequest();
					BundleBody body = new BundleBody();
					body.setBundle_id(bundle.getBundleId());
					body.setLayer_id(bundle.getLayerId());
					offlineRequest.setBundle_offline_request(body);
					
					resourceServiceClient.bundleOffline(offlineRequest);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void failed(Exception ex) {
		Long id = this.getParam1();
		HeartBeatBundleDAO heartBeatBundleDao = SpringContext.getBean(HeartBeatBundleDAO.class);
		ResourceServiceClient resourceServiceClient = SpringContext.getBean(ResourceServiceClient.class);
		HeartBeatBundlePO bundle = heartBeatBundleDao.findById(id);
		
		//修改状态
		if(bundle.getOnlineStatus().equals(OnlineStatus.ONLINE)){
			bundle.setOnlineStatus(OnlineStatus.OFFLINE);
			heartBeatBundleDao.save(bundle);
			
			//离线状态上报
			BundleOfflineRequest offlineRequest = new BundleOfflineRequest();
			BundleBody body = new BundleBody();
			body.setBundle_id(bundle.getBundleId());
			body.setLayer_id(bundle.getLayerId());
			offlineRequest.setBundle_offline_request(body);
			
			resourceServiceClient.bundleOffline(offlineRequest);
		}
		
	}

	@Override
	public void cancelled() {
		Long id = this.getParam1();
		LOG.info("设备heaartBeatBundleId为：" + id + "  cancel");
	}

}
