package com.suma.venus.resource.externalinterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.RequestBaseBO;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;

@Service
public class InterfaceFromResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceFromResource.class);
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private WorkNodeDao workNodeDao;
	
	/**对接入层发起del_bundle请求*/
	public void delBundleRequest(BundlePO bundle){
		try {
			if(!StringUtils.isEmpty(bundle.getAccessNodeUid())){
				WorkNodePO layerNode = workNodeDao.findByNodeUid(bundle.getAccessNodeUid());
				if(null != layerNode && ONLINE_STATUS.ONLINE.equals(layerNode.getOnlineStatus())){
					RequestBaseBO request = new RequestBaseBO();
					request.getMessage().getMessage_header().setMessage_name("del_bundle");
					request.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
					request.getMessage().getMessage_header().setDestination_id(bundle.getAccessNodeUid());
					BundleBody bundleBody = new BundleBody();
					bundleBody.setBundle_id(bundle.getBundleId());
					request.getMessage().getMessage_body().put("del_bundle_request", bundleBody);

					//向接入层发送del_bundle消息
					messageService.msgSend2SingleNode(bundle.getAccessNodeUid(), JSONObject.toJSONString(request));
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.toString());
		}

	}
	
	/**对接入层发起logout_bundle请求，踢出bundle账号*/
	public void logoutBundleRequest(BundlePO bundle){
		try {
			
			if(!StringUtils.isEmpty(bundle.getAccessNodeUid())){
				WorkNodePO layerNode = workNodeDao.findByNodeUid(bundle.getAccessNodeUid());
				if(null != layerNode && ONLINE_STATUS.ONLINE.equals(layerNode.getOnlineStatus())){
					RequestBaseBO request = new RequestBaseBO();
					request.getMessage().getMessage_header().setMessage_name("logout_bundle");
					request.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
					request.getMessage().getMessage_header().setDestination_id(bundle.getAccessNodeUid());
					BundleBody bundleBody = new BundleBody();
					bundleBody.setBundle_id(bundle.getBundleId());
					request.getMessage().getMessage_body().put("logout_bundle_request", bundleBody);
					
					//向接入层发送logout_bundle消息
					messageService.msgSend2SingleNode(bundle.getAccessNodeUid(), JSONObject.toJSONString(request));
				}
			}
				
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}

	}
	
	/**对接入层发起clear_bundle请求，释放bundle*/
	public void clearBundleRequest(BundlePO bundle){
		try {
			
			if(!StringUtils.isEmpty(bundle.getAccessNodeUid())){
				WorkNodePO layerNode = workNodeDao.findByNodeUid(bundle.getAccessNodeUid());
				if(null != layerNode && ONLINE_STATUS.ONLINE.equals(layerNode.getOnlineStatus())){
					RequestBaseBO request = new RequestBaseBO();
					request.getMessage().getMessage_header().setMessage_name("clear_bundle");
					request.getMessage().getMessage_header().setSource_id(RegisterStatus.getNodeId());
					request.getMessage().getMessage_header().setDestination_id(bundle.getAccessNodeUid());
					BundleBody bundleBody = new BundleBody();
					bundleBody.setBundle_id(bundle.getBundleId());
					request.getMessage().getMessage_body().put("clear_bundle_request", bundleBody);
					//向接入层发送clear_bundle消息
					messageService.msgSend2SingleNode(bundle.getAccessNodeUid(), JSONObject.toJSONString(request));
				}
			}

//			return mqMessageSend.msgSendAndWaitResp(bundle.getAccessNodeUid(), request, 3 * 1000);
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
		
	}
	
}
