package com.suma.venus.resource.lianwang.status;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.LdapContants;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.util.XMLBeanUtils;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class StatusXMLUtil {
	
	@Autowired
	ChannelSchemeService channelSchemeService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TetrisDispatchService tetrisDispatchService;
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	private volatile boolean fullSending = false;
	
	//从userBO提取信息得到userStatusXml
	public UserStatusXML fromUserBO(UserBO userBO, SerNodePO node){
		
		UserStatusXML userStatusXML = new UserStatusXML();
		userStatusXML.setUserid(userBO.getUserNo());
		userStatusXML.setStatus(userBO.isLogined()?1:0);
		userStatusXML.setVisitednodeid(node.getNodeUuid());
		//按绑定的编码器
		/*if(null != userBO.getEncoderId()){
			BundlePO bundle = bundleDao.findByBundleId(userBO.getEncoderId());
			if(null != bundle){
				userStatusXML.setBinddevid(bundle.getUsername());
			}
		}*/
		
		//本地编码器
		if(null != userBO.getLocal_encoder()){
			userStatusXML.setBinddevid(userBO.getLocal_encoder().getEncoderNo());
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
	public DeviceStatusXML fromBundlePO(BundlePO bundlePO, SerNodePO node){
		DeviceStatusXML deviceStatusXML = new DeviceStatusXML();
		//devID为11位格式，对应的我们bundle的用户账号
		deviceStatusXML.setDevid(bundlePO.getUsername());
		deviceStatusXML.setDevtype(channelSchemeService.getCoderDeviceType(bundlePO.getBundleId()));
		deviceStatusXML.setStatus(ONLINE_STATUS.ONLINE==bundlePO.getOnlineStatus()?1:0);
		deviceStatusXML.setVisitednodeid(node.getNodeUuid());
		
		return deviceStatusXML;
	}
	
	public DeviceStatusXML fromSerInfo(SerInfoPO serInfoPO, SerNodePO node){
		DeviceStatusXML deviceStatusXML = new DeviceStatusXML();
		//devID为11位格式，对应的我们bundle的用户账号
		deviceStatusXML.setDevid(serInfoPO.getSerNo());
		deviceStatusXML.setDevtype(serInfoPO.getSerType());
		//默认上线
		deviceStatusXML.setStatus(1);
		deviceStatusXML.setVisitednodeid(node.getNodeUuid());
		return deviceStatusXML;
	}
	
	//通过消息队列将全量设备和用户状态信息发送到目标消息服务节点
	public void sendAllResourcesXmlMessage(List<SerInfoPO> serInfoPOs,List<BundlePO> localDevs, List<UserBO> localUserBOs,String layerId,int sharedSize) throws Exception {
		//先判断当前是否正在进行全量状态信息发送中，如果发送中，就不再重复发了
		if(!fullSending){
			fullSending = true;
			
			NotifyUserDeviceXML resourcesXml = createResourcesXml(serInfoPOs,localDevs, localUserBOs);
			
			//整包发送
			JSONObject messageJson = createWholeResourceMessage(resourcesXml,layerId);
			PassByBO passByBO = JSONObject.parseObject(messageJson.toJSONString(), PassByBO.class);
			
			tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
			
			/*String xmlStr = XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, resourcesXml);
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
			}*/
			
			//发送结束
			fullSending = false;
		}

	}
	
	//通过消息队列将设备和用户状态信息发送到目标消息服务节点
	public void sendResourcesXmlMessage(List<SerInfoPO> serInfoPOs,List<BundlePO> localDevs, List<UserBO> localUserBOs,String layerId,int sharedSize) throws Exception {
		NotifyUserDeviceXML resourcesXml = createResourcesXml(serInfoPOs,localDevs, localUserBOs);
		String xmlStr = XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, resourcesXml);
		
		//不分包
		JSONObject messageJson = createWholeResourceMessage(resourcesXml, layerId);
		PassByBO passByBO = JSONObject.parseObject(messageJson.toJSONString(), PassByBO.class);
		
		tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
		
		//考虑分包
//		int len = xmlStr.length();
//		if(len <= sharedSize){
//			//命令长度不大于sharedSize，整包发
//			JSONObject messageJson = createWholeNotifyResourceMessage(resourcesXml,layerId);
//			messageService.msgSend2SingleNode(layerId, messageJson.toJSONString());
//		}else{
//			//命令长度大于sharedSize，分包发
//			int max = len/sharedSize;	//分包的总数量
//			if(len%sharedSize != 0){
//				max++;
//			}
//			for (int i = 1; i <= max; i++) {
//				int begin = (i-1) * sharedSize;
//				int end = (i*sharedSize>xmlStr.length()) ? xmlStr.length() : i*sharedSize;
//				JSONObject sharedMessageJson = createSharedNotifyResourceMessage(
//						xmlStr.substring(begin, end), layerId, resourcesXml.getSeq(), len, i, max);
//				messageService.msgSend2SingleNode(layerId, sharedMessageJson.toJSONString());
//				try {//消息发送间隔30ms
//					Thread.sleep(30);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	//通过消息队列将路由信息发送到目标消息服务节点
	public void sendResourcesXmlMessage(List<SerNodePO> serNodePOs,String layerId) throws Exception {
		NotifyRouteLinkXml routeLinkXml = createRouteLinkXml(serNodePOs);
		JSONObject messageJson = createRouteLinkMessage(routeLinkXml, layerId);
		PassByBO passByBO = JSONObject.parseObject(messageJson.toJSONString(), PassByBO.class);
		
		tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
		
	}
	
	//根据设备和用户信息构造NotifyUserDeviceXML
	public NotifyUserDeviceXML createResourcesXml(List<SerInfoPO> serInfoPOs,List<BundlePO> localDevs, List<UserBO> localUserBOs) {
		
		SerNodePO serNode = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		
		NotifyUserDeviceXML resourcesXml = new NotifyUserDeviceXML();
		if(null != serInfoPOs){
			for(SerInfoPO serInfoPO : serInfoPOs){
				resourcesXml.getDevlist().add(fromSerInfo(serInfoPO, serNode));
			}
		}
		
		if(null != localDevs){
			for(BundlePO localDev : localDevs){
				resourcesXml.getDevlist().add(fromBundlePO(localDev, serNode));
			}
		}

		if(null != localUserBOs){
			for(UserBO localUser : localUserBOs){
				resourcesXml.getUserlist().add(fromUserBO(localUser, serNode));
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
	/*public NotifyRouteLinkXml createRouteLinkXml(List<SerNodePO> serNodePOs){
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
			
			*//**
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
			***//*
			
			routeLinkXml.setMatcontent(sBuilder.toString());
		}
		
		return routeLinkXml;
	}*/
	public NotifyRouteLinkXml createRouteLinkXml(List<SerNodePO> serNodePOs) throws Exception{
		NotifyRouteLinkXml routeLinkXml = new NotifyRouteLinkXml();
		if(null != serNodePOs){
			routeLinkXml.setMatsize(serNodePOs.size() * serNodePOs.size());
			// 紧凑格式矩阵(tight)
			routeLinkXml.setMattype("tight");
			for(SerNodePO serNodePO : serNodePOs){
				routeLinkXml.getNlist().add(serNodePO.getNodeUuid());
			}
			
			StringBufferWrapper sBuilder = new StringBufferWrapper();
			for(int i=0; i<serNodePOs.size()-1; i++){
				for(int j=i+1; j<serNodePOs.size(); j++){
					if(isConnected(serNodePOs.get(i), serNodePOs.get(j))){
						sBuilder.append("1");
					}else{
						sBuilder.append("0");
					}
				}
			}
			
			routeLinkXml.setMatcontent(runLengthEncoding(sBuilder.toString()));
		}
		
		return routeLinkXml;
	}
	
	
	//构造整包发送的publish syncinfo 消息
	public JSONObject createWholeResourceMessage(NotifyUserDeviceXML resourcesXml,String layerId) {
		
		JSONObject content = new JSONObject();
		content.put("cmd", "send_node_message");
		content.put("type", "sync");
		content.put("commandname", "syncinfo");
		content.put("content", XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, resourcesXml));
		
		JSONObject passbyJson = new JSONObject();
		passbyJson.put("bundle_id", "");
		passbyJson.put("layer_id", layerId);
		passbyJson.put("pass_by_content", content);
		JSONObject msgBodyJson = new JSONObject();
		msgBodyJson.put("pass_by", passbyJson);
		
		return passbyJson;
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
	
	/**
	 * 生成路由信息同步消息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 上午10:04:35
	 */
	public JSONObject createRouteLinkMessage(NotifyRouteLinkXml routeLinkXml,String layerId) {
		
		JSONObject content = new JSONObject();
		content.put("cmd", "send_node_message");
		content.put("type", "sync");
		content.put("commandname", "syncroutelink");
		content.put("content", XMLBeanUtils.beanToXml(NotifyRouteLinkXml.class, routeLinkXml));
		
		JSONObject passbyJson = new JSONObject();
		passbyJson.put("bundle_id", "");
		passbyJson.put("layer_id", layerId);
		passbyJson.put("pass_by_content", content);
		JSONObject msgBodyJson = new JSONObject();
		msgBodyJson.put("pass_by", passbyJson);
		
		return passbyJson;
	}
	
	/**
	 * 判断两个serNode是否联通<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 上午9:05:13
	 * @param SerNodePO node1 节点1
	 * @param SerNodePO node2 节点2
	 * @return boolean 是/否
	 */
	public boolean isConnected(SerNodePO node1, SerNodePO node2) throws Exception{
		
		if(node1.getNodeRouter() != null){
			List<String> routers = JSONArray.parseArray(node1.getNodeRouter(), String.class);
			if(routers.contains(node2.getNodeUuid())){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 游程编码算法<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午3:31:37
	 * @param String mat 游程编码前 （1111001110）
	 * @return String 游程编码后 （0,4,2,3,1）
	 */
	public String runLengthEncoding(String mat) throws Exception{
		
		StringBufferWrapper sb = new StringBufferWrapper();
		String[] mats = mat.split("");
		
		boolean flag = false;
		int count = 0;
		for(int i=0; i<mats.length; i++){
			
			if(!flag){
				if(i == mats.length-1){
					if(mats[i].equals("0")){
						count ++;
						sb.append(count);
					}
					if(mats[i].equals("1")){
						sb.append(count).append(",1");
					}
				}else{
					if(mats[i].equals("0")){
						count ++;
					}
					if(mats[i].equals("1")){
						sb.append(count).append(",");
						count = 1;
						flag = true;
					}
				}
			}else{
				
				if(i == mats.length-1){
					if(mats[i].equals("1")){
						count ++;
						sb.append(count);
					}
					if(mats[i].equals("0")){
						sb.append(count).append(",1");
					}
				}else{
					if(mats[i].equals("1")){
						count ++;
					}
					if(mats[i].equals("0")){
						sb.append(count).append(",");
						count = 1;
						flag = false;
					}
				}
			}

		}
		
		return sb.toString();
	}
	
	/** 
	 * 游程解码算法<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午3:35:37
	 * @param String str 游程解码前
	 * @return String 游程解码后
	 */
	public String runLengthDecoding(String str) throws Exception{
		
		StringBufferWrapper sb = new StringBufferWrapper();
		String[] strs = str.split(",");
		
		for(int i=0;i<strs.length;i++){
			if(i%2 == 0){
				for(int j=0; j<Integer.valueOf(strs[i]); j++){
					sb.append("0");
				}
			}else{
				for(int j=0; j<Integer.valueOf(strs[i]); j++){
					sb.append("1");				
				}
			}
		}
		
		return sb.toString();
	}
	
}
