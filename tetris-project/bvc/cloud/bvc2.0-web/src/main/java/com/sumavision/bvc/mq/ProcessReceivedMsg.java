package com.sumavision.bvc.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.VenusMessageHead.MsgType;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.util.MessageIds;
import com.suma.venus.resource.dao.LockBundleParamDao;
import com.suma.venus.resource.pojo.LockBundleParamPO;
import com.sumavision.bvc.BO.BundleSchemeBO;
import com.sumavision.bvc.BO.ChannelStatusBody;
import com.sumavision.bvc.BO.IncomingCallRespParam;
import com.sumavision.bvc.BO.ResponseBody;
import com.sumavision.bvc.DTO.ResultMap;
import com.sumavision.bvc.common.CommonConstant;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.bvc.monitor.logic.manager.ResourseLayerTask;


/**
 * 消息处理类，处理接收到的activemq消息
 *
 * @author lxw 2018年6月12日
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcessReceivedMsg {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessReceivedMsg.class);
    
    @Autowired
    private ResourseLayerTask resourseLayerTask;
    
    @Autowired
    private MeetingServiceImpl meetingServiceImpl;
    
    @Autowired
    private LockBundleParamDao lockBundleParamDao;
    
    @Autowired
    private DeviceGroupDAO deviceGroupDao;
    /*
     * 根据接口协议的数据类型解析msg中的方法名，根据相应方法名注入上层解析接口进行处理
     */
    public void process(String msg) throws Exception {
    	ResponseBO responseBo = JSONObject.parseObject(msg, ResponseBO.class);
    	String msgType = responseBo.getMessage().getMessage_header().getMessage_type();
    	if(MsgType.request.toString().equalsIgnoreCase(msgType) || MsgType.notification.toString().equalsIgnoreCase(msgType) 
        		|| MsgType.alert.toString().equalsIgnoreCase(msgType) || MsgType.passby.toString().equalsIgnoreCase(msgType)){
            switch (responseBo.getMessage().getMessage_header().getMessage_name()) {
            case "lock_channel_request":
            	lockChannel(msg);
                break;
            case "passby":
            	processPassbyMsg(msg);
            	break;
            case "triggerAlarmNotify":
            	processAlarmMsg(msg);
            default:
//            	messageService.writeResp(responseBo.getMessage().getMessage_header().getSource_id(), null);
                break;
            }
        }else if(MsgType.response.toString().equalsIgnoreCase(msgType)){
            //处理回复消息
            processRespMsg(msg);
        }
    }

    /**处理收到的lock_channel请求消息*/
    private void lockChannel(String msg){    	
    }
    
    /**处理收到的响应类消息*/
    private void processAlarmMsg(String textMessage) {
    	LOGGER.info("receive msgAlarm " + JSONObject.toJSONString(textMessage));
        ResponseBO responseBo = JSONObject.parseObject(textMessage, ResponseBO.class);
        LOGGER.info("----------------------processAlarmMsg message is " + (responseBo==null?null:JSONObject.toJSONString(responseBo)));
        treateTriggerAlarm(responseBo);
    }
    
    /**处理收到的响应类消息*/
    private void processRespMsg(String textMessage) {
    	LOGGER.info("receive msgResp " + JSONObject.toJSONString(textMessage));
        ResponseBO responseBo = JSONObject.parseObject(textMessage, ResponseBO.class);
        LOGGER.info("----------------------processRespMsg message is " + (responseBo==null?null:JSONObject.toJSONString(responseBo)));
        String msgUid = responseBo.getMessage().getMessage_header().getSequence_id();
        if(null != msgUid){
            MessageIds.messageInfoMap.put(msgUid, responseBo);
        }
        
        String messageName = responseBo.getMessage().getMessage_header().getMessage_name();
        //处理open_bundle_response消息，回调业务接口
        if("open_bundle".equalsIgnoreCase(messageName)){
        	treateOpenBundleResp(responseBo);        	
        }
    }
    
    /**处理收到的passby消息*/
    private void processPassbyMsg(String textMessage) {
    	LOGGER.info("receive msgPassby " + JSONObject.toJSONString(textMessage));
        ResponseBO responseBo = JSONObject.parseObject(textMessage, ResponseBO.class);
        LOGGER.info("----------------------processPassbyMsg message is " + (responseBo==null?null:JSONObject.toJSONString(responseBo)));
        IncomingCallRespParam respParam = responseBo.getMessage().getMessage_body().getObject("incoming_call_response", IncomingCallRespParam.class);
        if(null != respParam){        	
        	LOGGER.info("----------------------processPassbyMsg message is " + respParam);        
        }
    }
    
    /**
     * 处理open_bundle_response消息
     * @param responseBo
     */
    private void treateOpenBundleResp(ResponseBO responseBo){    	
    	try{
    		ResponseBody resp = responseBo.getMessage().getMessage_body().getObject("open_bundle_response", ResponseBody.class);
    		
    		String bundleId = resp.getBundleId();
    		String groupUuid = null;
    		DeviceGroupPO groupPO = null;
    		boolean bAccept = true;
    		Long userId = null;    		
    		
    		switch(resp.getResult()){
    		
    		case ResponseBody.SUCCESS:
    		case ResponseBody.REJECT:
    			if(resp.getPass_by_str() != null){
    				String pass_by_str = resp.getPass_by_str();
    				JSONObject pass_by_str_json = JSONObject.parseObject(pass_by_str);
    	    		JSONObject pass_by_content = pass_by_str_json.getJSONObject("pass_by_content");
        			if(pass_by_content != null){
        				String accept = pass_by_content.getString("accept");
            			if("false".equals(accept)){
            				LOGGER.info("处理open_bundle_response的拒绝接听的情况");
            				userId = pass_by_content.getLong("userId");
            				groupUuid = pass_by_content.getString("groupUuid");
            				bundleId = pass_by_str_json.getString("bundle_id");
            				bAccept = false;
        					
            			}else if("true".equals(accept)){
            				LOGGER.info("处理open_bundle_response的接听");
            				groupUuid = pass_by_content.getString("groupUuid");
            				bundleId = pass_by_str_json.getString("bundle_id");
            				bAccept = true;
            			}else{
            				LOGGER.error("open_bundle_response的接听或拒绝，解析pass_by_content有误，accept = " + accept);
            			}
            		}else{
            			LOGGER.error("用户同意或拒绝接听时无法解析pass_by_content，pass_by_str = " + pass_by_str);
            		}
    			}
    			if(groupUuid != null){
    				groupPO = deviceGroupDao.findByUuid(groupUuid);
    			}
    			break;
    			
    		case ResponseBody.BUNDLE_NOT_FOUND:
    		case ResponseBody.BUNDLE_OFFLINE:
    		case ResponseBody.UNKNOWN_FAILED:
    			LOGGER.info("处理open_bundle_response:" + resp.getResult());
    			if(null == bundleId || "".equals(bundleId)){
    				break;
    			}
    			groupUuid = getGroupUuidByBundleIdFromResource(bundleId);
    			if(groupUuid == null){
    	    		//TODO:未查到，可能已经释放
    				bAccept = true;
    				break;
    	    	}
    			groupPO = deviceGroupDao.findByUuid(groupUuid);
    	    	if(groupPO == null){
    	    		LOGGER.error("未找到会议，groupUuid = " + groupUuid);
    	    		break;
    	    	}
    	    	if(GroupType.MEETING.equals(groupPO.getType())){
    	    		bAccept = true;
    	    		//会议不处理
    	    		groupPO = null;
    	    	}else if(GroupType.MULTIPLAYERVIDEO.equals(groupPO.getType()) || GroupType.MULTIPLAYERAUDIO.equals(groupPO.getType())){
    	    		bAccept = false;
    	    	}else{
    	    		LOGGER.error("无法识别的会议类型：" + groupPO.getType());
    	    	}
    			break;
    			
    		case ResponseBody.TIME_OUT:
    		case ResponseBody.INVALID_OPERATE_INDEX:
    		default:
    			LOGGER.info("处理open_bundle_response:" + resp.getResult());
    			groupUuid = getGroupUuidByBundleIdFromResource(bundleId);
    			groupPO = deviceGroupDao.findByUuid(groupUuid);
    			bAccept = false;
    			break;
    		}    		
    		
    		if(!bAccept){
    			//释放bundle
    			List<BundleSchemeBO> bundlesToUnlock = new ArrayList<BundleSchemeBO>();
				BundleSchemeBO bundle = new BundleSchemeBO();
				bundle.setBundleId(bundleId);
				bundle.setTaskId(UUID.randomUUID().toString());
				bundlesToUnlock.add(bundle);
				//解锁bundle
    			ResultMap unlockResult = resourseLayerTask.batchUnlockBundleResource(bundlesToUnlock, userId, false);
    			if(unlockResult != null){
    				ChannelStatusBody statusBody = ((ChannelStatusBody)unlockResult.get(bundle.getTaskId()));
    				if(statusBody != null && statusBody.isResult()){
    					//调业务接口
//    					meetingServiceImpl.incomingCallResponse(groupUuid, bundleId, false);
    				}else{
    					LOGGER.error("open_bundle_response unlock bundle failed: " + bundle);
    				}
    			}
    		}
    		if(groupPO!=null && bundleId!=null){
    			LOGGER.info("********** bundleId = " + bundleId + ", accept = " + bAccept + " **********");
    			meetingServiceImpl.incomingCallResponse(groupPO, bundleId, bAccept);
    		}else{
    			LOGGER.info("无需处理异常，groupUuid = " + groupUuid + ", bundleId = " + bundleId);
    		}
    	}catch(Exception e){
    		LOGGER.error("treateOpenBundleResp failed", e);
    	}
    	
    }
    
    private void treateTriggerAlarm(ResponseBO responseBo){
    	try{
    		JSONObject triggerObj = responseBo.getMessage().getMessage_body().getJSONObject("trigger_alarm_notify_param");
    		if(triggerObj != null){
    			triggerObj.getString("sourceObj");
    			String alarmCode = triggerObj.getString("alarmCode");
//    			BundlePO srcBundle = bundleService.findByBundleId(src);
    			//设备离线
				if(CommonConstant.DEVICE_OFFLINE.equals(alarmCode)){
	    			//调业务接口
					//multiplayerChatServiceImpl.memberOffline(src);
				}
    			
    		}
    	}catch(Exception e){
    		LOGGER.error("treateTriggerAlarm failed", e);
    	}
    }
    
    /**
     * 根据bundleId，从资源层读取passby中的groupUuid
     */
    private String getGroupUuidByBundleIdFromResource(String bundleId){
    	try{
	    	LockBundleParamPO lockBundleParamPO = lockBundleParamDao.findByBundleId(bundleId);
	    	String passByStr = lockBundleParamPO.getPassByStr();
	    	JSONObject passByStrJson = JSON.parseObject(passByStr);
	    	return passByStrJson.getJSONObject("pass_by_content").getString("groupUuid");
    	}catch(Exception e){
    		LOGGER.info("不能从资源层查询到passby信息的groupUuid，可能已经解锁，bundleId = " + bundleId);
    	}
    	return null;
    }    
}
