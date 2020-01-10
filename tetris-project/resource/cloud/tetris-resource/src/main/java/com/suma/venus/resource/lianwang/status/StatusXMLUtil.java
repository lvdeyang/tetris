package com.suma.venus.resource.lianwang.status;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.suma.application.ldap.contants.LdapContants;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.util.XMLBeanUtils;

@Component
public class StatusXMLUtil {
	
	@Autowired
	ChannelSchemeService channelSchemeService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private BundleDao bundleDao;
	
	private volatile boolean fullSending = false;
	
	//从userBO提取信息得到userStatusXml
	public UserStatusXML fromUserBO(UserBO userBO){
		UserStatusXML userStatusXML = new UserStatusXML();
		userStatusXML.setUserid(userBO.getUserNo());
		userStatusXML.setStatus(userBO.isLogined()?1:0);
		userStatusXML.setVisitednodeid(LdapContants.DEFAULT_NODE_UUID);
		//按绑定的编码器
		if(null != userBO.getEncoderId()){
			BundlePO bundle = bundleDao.findByBundleId(userBO.getEncoderId());
			if(null != bundle){
				userStatusXML.setBinddevid(bundle.getUsername());
			}
		}
		
		return userStatusXML;
	}
	
	//UserStatusXML提取部分信息得到userbo
	public UserBO toUserBO(UserStatusXML userXml){
		UserBO userBO = new UserBO();
		userBO.setUserNo(userXml.getUserid());
		userBO.setLogined(userXml.getStatus()==1 ? true : false);
		return userBO;
	}
	
	//从bundlePO提取信息得到DeviceStatusXML
	public DeviceStatusXML fromBundlePO(BundlePO bundlePO){
		DeviceStatusXML deviceStatusXML = new DeviceStatusXML();
		//devID为11位格式，对应的我们bundle的用户账号
		deviceStatusXML.setDevid(bundlePO.getUsername());
		deviceStatusXML.setDevtype(channelSchemeService.getCoderDeviceType(bundlePO.getBundleId()));
		deviceStatusXML.setStatus(ONLINE_STATUS.ONLINE==bundlePO.getOnlineStatus()?1:0);
		deviceStatusXML.setVisitednodeid(LdapContants.DEFAULT_NODE_UUID);
		
		return deviceStatusXML;
	}
	
	public DeviceStatusXML fromSerInfo(SerInfoPO serInfoPO){
		DeviceStatusXML deviceStatusXML = new DeviceStatusXML();
		//devID为11位格式，对应的我们bundle的用户账号
		deviceStatusXML.setDevid(serInfoPO.getSerNo());
		deviceStatusXML.setDevtype(serInfoPO.getSerType());
		//默认上线
		deviceStatusXML.setStatus(1);
		deviceStatusXML.setVisitednodeid(LdapContants.DEFAULT_NODE_UUID);
		return deviceStatusXML;
	}
	
	//通过消息队列将全量设备和用户状态信息发送到目标消息服务节点
	public void sendAllResourcesXmlMessage(List<SerInfoPO> serInfoPOs,List<BundlePO> localDevs, List<UserBO> localUserBOs,String layerId,int sharedSize) {
		//先判断当前是否正在进行全量状态信息发送中，如果发送中，就不再重复发了
		if(!fullSending){
			fullSending = true;
			
			NotifyUserDeviceXML resourcesXml = createResourcesXml(serInfoPOs,localDevs, localUserBOs);
			String xmlStr = XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, resourcesXml);
			int len = xmlStr.length();
			if(len <= sharedSize){
				//命令长度不大于sharedSize，整包发
				JSONObject messageJson = createWholeNotifyResourceMessage(resourcesXml,layerId);
				messageService.msgSend2SingleNode(layerId, messageJson.toJSONString());
			}else{
				//命令长度大于sharedSize，分包发
				int max = len/sharedSize;	//分包的总数量
				if(len%sharedSize != 0){
					max++;
				}
				for (int i = 1; i <= max; i++) {
					int begin = (i-1) * sharedSize;
					int end = (i*sharedSize>xmlStr.length()) ? xmlStr.length() : i*sharedSize;
					JSONObject sharedMessageJson = createSharedNotifyResourceMessage(
							xmlStr.substring(begin, end), layerId, resourcesXml.getSeq(), len, i, max);
					messageService.msgSend2SingleNode(layerId, sharedMessageJson.toJSONString());
					try {//消息发送间隔30ms
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			//发送结束
			fullSending = false;
		}

	}
	
	//通过消息队列将设备和用户状态信息发送到目标消息服务节点
	public void sendResourcesXmlMessage(List<SerInfoPO> serInfoPOs,List<BundlePO> localDevs, List<UserBO> localUserBOs,String layerId,int sharedSize) {
		NotifyUserDeviceXML resourcesXml = createResourcesXml(serInfoPOs,localDevs, localUserBOs);
		String xmlStr = XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, resourcesXml);
		int len = xmlStr.length();
		if(len <= sharedSize){
			//命令长度不大于sharedSize，整包发
			JSONObject messageJson = createWholeNotifyResourceMessage(resourcesXml,layerId);
			messageService.msgSend2SingleNode(layerId, messageJson.toJSONString());
		}else{
			//命令长度大于sharedSize，分包发
			int max = len/sharedSize;	//分包的总数量
			if(len%sharedSize != 0){
				max++;
			}
			for (int i = 1; i <= max; i++) {
				int begin = (i-1) * sharedSize;
				int end = (i*sharedSize>xmlStr.length()) ? xmlStr.length() : i*sharedSize;
				JSONObject sharedMessageJson = createSharedNotifyResourceMessage(
						xmlStr.substring(begin, end), layerId, resourcesXml.getSeq(), len, i, max);
				messageService.msgSend2SingleNode(layerId, sharedMessageJson.toJSONString());
				try {//消息发送间隔30ms
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//通过消息队列将路由信息发送到目标消息服务节点
	public void sendResourcesXmlMessage(List<SerNodePO> serNodePOs,String layerId) {
		NotifyRouteLinkXml routeLinkXml = createRouteLinkXml(serNodePOs);
		JSONObject messageJson = createNotifyRouteLinkMessage(routeLinkXml, layerId);
		messageService.msgSend2SingleNode(layerId, messageJson.toJSONString());
	}
	
	//根据设备和用户信息构造NotifyUserDeviceXML
	public NotifyUserDeviceXML createResourcesXml(List<SerInfoPO> serInfoPOs,List<BundlePO> localDevs, List<UserBO> localUserBOs) {
		NotifyUserDeviceXML resourcesXml = new NotifyUserDeviceXML();
		if(null != serInfoPOs){
			for(SerInfoPO serInfoPO : serInfoPOs){
				resourcesXml.getDevlist().add(fromSerInfo(serInfoPO));
			}
		}
		
		if(null != localDevs){
			for(BundlePO localDev : localDevs){
				resourcesXml.getDevlist().add(fromBundlePO(localDev));
			}
		}

		if(null != localUserBOs){
			for(UserBO localUser : localUserBOs){
				resourcesXml.getUserlist().add(fromUserBO(localUser));
			}
		}
		
		return resourcesXml;
	}
	
	//根据SerinfoPO构造NotifyRouteLinkXml
//	public NotifyRouteLinkXml createRouteLinkXml(List<SerInfoPO> serInfoPOs){
//		NotifyRouteLinkXml routeLinkXml = new NotifyRouteLinkXml();
//		if(null != serInfoPOs){
//			routeLinkXml.setMattype("raw");
//			routeLinkXml.setMatsize(serInfoPOs.size() * serInfoPOs.size());
//			StringBuilder sBuilder = new StringBuilder();
//			for(SerInfoPO serInfoPO : serInfoPOs){
//				routeLinkXml.getNlist().add(serInfoPO.getSerUuid());
//				//默认全通矩阵
//				sBuilder.append("1");
//			}
//			String singleMatcontent = sBuilder.toString();
//			sBuilder = new StringBuilder();
//			for(int i=0;i<serInfoPOs.size();i++){
//				sBuilder.append(singleMatcontent);
//				if(i != serInfoPOs.size()-1){
//					sBuilder.append(",");
//				}
//			}
//			routeLinkXml.setMatcontent(sBuilder.toString());
//		}
//		
//		return routeLinkXml;
//	}
	
	//根据SerinfoPO构造NotifyRouteLinkXml
	public NotifyRouteLinkXml createRouteLinkXml(List<SerNodePO> serNodePOs){
		NotifyRouteLinkXml routeLinkXml = new NotifyRouteLinkXml();
		if(null != serNodePOs){
			routeLinkXml.setMatsize(serNodePOs.size() * serNodePOs.size());
			StringBuilder sBuilder = new StringBuilder();
			//默认全通矩阵
			// 原始矩阵格式(raw)
			routeLinkXml.setMattype("raw");
			for(SerNodePO serNodePO : serNodePOs){
				routeLinkXml.getNlist().add(serNodePO.getNodeUuid());
				sBuilder.append("1");
			}
			String singleMatcontent = sBuilder.toString();
			sBuilder = new StringBuilder();
			for(int i=0;i<serNodePOs.size();i++){
				sBuilder.append(singleMatcontent);
				if(i != serNodePOs.size()-1){
					sBuilder.append(",");
				}
			}
			
			/**
			//默认全通矩阵(游程编码方式)
			routeLinkXml.setMattype("tight");
			for(SerNodePO serNodePO : serNodePOs){
				routeLinkXml.getNlist().add(serNodePO.getNodeUuid());
			}
			int value = 0;
			for(int i=1;i<serNodePOs.size();i++){
				value += i;
			}
			for(int i=0;i<serNodePOs.size();i++){
				if(i != 1){
					sBuilder.append("0");
				}else{
					sBuilder.append(value);
				}
				if(i != serNodePOs.size()-1){
					sBuilder.append(",");
				}
			}
			***/
			
			routeLinkXml.setMatcontent(sBuilder.toString());
		}
		
		return routeLinkXml;
	}
	
	//构造整包发送的Notify syncinfo 消息
	public JSONObject createWholeNotifyResourceMessage(NotifyUserDeviceXML resourcesXml,String layerId) {
		JSONObject contentJson = new JSONObject();
		contentJson.put("method", "publish");
		contentJson.put("content-type", "application/command+xml");
		contentJson.put("resources", XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, resourcesXml));
		JSONObject passbyJson = new JSONObject();
		passbyJson.put("bundle_id", "");
		passbyJson.put("layer_id", layerId);
		passbyJson.put("pass_by_content", contentJson);
		JSONObject msgBodyJson = new JSONObject();
		msgBodyJson.put("pass_by", passbyJson);
		
		JSONObject msgHeaderJson = new JSONObject();
		msgHeaderJson.put("destination_id", layerId);
		msgHeaderJson.put("message_name", "passby");
		msgHeaderJson.put("message_type", "passby");
		msgHeaderJson.put("sequence_id", UUID.randomUUID().toString());
		msgHeaderJson.put("source_id", "suma-venus-resource");
		
		JSONObject messageJson = new JSONObject();
		messageJson.put("message_header", msgHeaderJson);
		messageJson.put("message_body", msgBodyJson);
		
		JSONObject json = new JSONObject();
		json.put("message", messageJson);
		return json;
	}
	
	//构造分包发送的Notify syncinfo消息，seq表示消息内容序列号，len表示消息内容总长度，cur表示当前包号，max表示分包总数
	public JSONObject createSharedNotifyResourceMessage(String sharedXml,String layerId,
			String seq,int len,int cur,int max) {
		JSONObject contentJson = new JSONObject();
		contentJson.put("method", "publish");
		contentJson.put("content-type", "application/command+xml+packet_"+seq+"-"+len+"-"+cur+"-"+max);
		contentJson.put("resources", sharedXml);
		JSONObject passbyJson = new JSONObject();
		passbyJson.put("bundle_id", "");
		passbyJson.put("layer_id", layerId);
		passbyJson.put("pass_by_content", contentJson);
		JSONObject msgBodyJson = new JSONObject();
		msgBodyJson.put("pass_by", passbyJson);
		
		JSONObject msgHeaderJson = new JSONObject();
		msgHeaderJson.put("destination_id", layerId);
		msgHeaderJson.put("message_name", "passby");
		msgHeaderJson.put("message_type", "passby");
		msgHeaderJson.put("sequence_id", UUID.randomUUID().toString());
		msgHeaderJson.put("source_id", "suma-venus-resource");
		
		JSONObject messageJson = new JSONObject();
		messageJson.put("message_header", msgHeaderJson);
		messageJson.put("message_body", msgBodyJson);
		
		JSONObject json = new JSONObject();
		json.put("message", messageJson);
		return json;
	}
	
	public JSONObject createNotifyRouteLinkMessage(NotifyRouteLinkXml routeLinkXml,String layerId) {
		JSONObject contentJson = new JSONObject();
		contentJson.put("method", "publish");
		contentJson.put("content-type", "application/command+xml");
		contentJson.put("resources", XMLBeanUtils.beanToXml(NotifyRouteLinkXml.class, routeLinkXml));
		JSONObject passbyJson = new JSONObject();
		passbyJson.put("bundle_id", "");
		passbyJson.put("layer_id", layerId);
		passbyJson.put("pass_by_content", contentJson);
		JSONObject msgBodyJson = new JSONObject();
		msgBodyJson.put("pass_by", passbyJson);
		
		JSONObject msgHeaderJson = new JSONObject();
		msgHeaderJson.put("destination_id", layerId);
		msgHeaderJson.put("message_name", "passby");
		msgHeaderJson.put("message_type", "passby");
		msgHeaderJson.put("sequence_id", UUID.randomUUID().toString());
		msgHeaderJson.put("source_id", "suma-venus-resource");
		
		JSONObject messageJson = new JSONObject();
		messageJson.put("message_header", msgHeaderJson);
		messageJson.put("message_body", msgBodyJson);
		
		JSONObject json = new JSONObject();
		json.put("message", messageJson);
		return json;
	}
	
}
