package com.sumavision.tetris.application.alarm.service;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.alarm.bo.http.AlarmNotifyBO;
import com.sumavision.tetris.application.alarm.AlarmCode;
import com.sumavision.tetris.business.common.enumeration.FunUnitStatus;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.DeviceService;
import com.sumavision.tetris.device.backup.BackupService;
import com.sumavision.tetris.device.backup.condition.BackupConditionDao;
import com.sumavision.tetris.device.backup.condition.BackupConditionPO;
import com.sumavision.tetris.device.backup.condition.BackupConditionService;
import com.sumavision.tetris.device.group.DeviceGroupDao;
import com.sumavision.tetris.device.group.DeviceGroupPO;
import com.sumavision.tetris.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.device.xtool.httptool.NetCardHttpUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.business.api.vo.AlarmVO;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.capacity.bo.request.ResultCodeResponse;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.config.ServerProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class AlarmService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmService.class);


	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private CapacityProps capacityProps;
	
	@Autowired
	private ServerProps serverProps;
	
	@Autowired
	private SyncService syncService;

	@Autowired
	BackupService backupService;
	
	@Autowired
	private AlarmFeignClientService alarmFeignClientService;

	@Autowired
	DeviceDao deviceDao;

	@Autowired
	DeviceService deviceService;

	@Autowired
	DeviceGroupDao deviceGroupDao;

	@Autowired
	NetCardInfoDao netCardInfoDao;

	@Autowired
	NetCardHttpUnit netCardHttpUnit;

	@Autowired
	BackupConditionService backupConditionService;

	/**
	 * 设置能力告警地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午2:50:30
	 * @param String ip 能力ip
	 */
	public void setAlarmUrl(String ip) throws Exception{
		
		String eurake = serverProps.getDefaultZone().split("http://")[1].split(":")[0];
		
		String alarmUrl = new StringBufferWrapper().append("http://")
												   .append(eurake)
												   .append(":")
												   .append(8082)
												   .append("/tetris-capacity/api/thirdpart/capacity/alarm/notify?bundle_ip=")
												   .append(ip)
												   .toString();
		ResultCodeResponse response = capacityService.putAlarmUrl(ip, capacityProps.getPort(), alarmUrl);
		if(response.getResult_code().equals("1")){
			throw new BaseException(StatusCode.ERROR, "url格式错误");
		}
	}


	
	/**
	 * 告警通知<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午10:39:07
	 * @param String capacityIp 能力ip
	 * @param AlarmVO alarm 告警参数
	 */
	public void alarmNotify(String capacityIp, AlarmVO alarm) throws Exception{
		
		String alarmCode = alarm.getCodec();
		
		try {
			if("11070001".equals(alarmCode)){
				LOGGER.info("transform online");
				syncService.syncTransform(capacityIp);
			}
		} catch (Exception e) {
			LOGGER.error("sync fail",e);
		}
		
		JSONObject alarmObj = new JSONObject();
		if(alarm.getInput_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getInput_trigger()));
			alarmObj.put("detail",alarm.getDetail());
		}
		if(alarm.getTask_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getTask_trigger()));
			alarmObj.put("detail",alarm.getDetail());
		}
		if(alarm.getOutput_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getOutput_trigger()));
			alarmObj.put("detail",alarm.getDetail());
		}
		if(alarm.getLicense_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getLicense_trigger()));
			alarmObj.put("details",alarm.getDetail());
		}
		
		try{
			if(alarm.getStatus().equals("on")){
				alarmFeignClientService.triggerAlarm(alarmCode, capacityIp, alarmObj.toJSONString(), null, false, new Date());
			}
			if(alarm.getStatus().equals("off")){
				alarmFeignClientService.recoverAlarm(alarmCode, capacityIp, alarmObj.toJSONString(), null, new Date());
			}
			if(alarm.getStatus().equals("once")){
				alarmFeignClientService.triggerAlarm(alarmCode, capacityIp, alarmObj.toJSONString(), null, true, new Date());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * @MethodName: init
	 * @Description: TODO 告警初始化
	 * @Return: void
	 * @Author: Poemafar
	 * @Date: 2020/12/28 10:29
	 **/
	public void init() throws Exception {
		 subscribeAlarm();
	}

	/**
	 * @MethodName: subscribeAlarm
	 * @Description: TODO 订阅告警，失败重试30次，每10秒发一次
	 * @Return: void
	 * @Author: Poemafar
	 * @Date: 2020/12/28 10:29
	 **/
	public void subscribeAlarm() throws BaseException {
		Integer retryTime = 18;
		while(retryTime>0){
			retryTime--;
			try {
				alarmFeignClientService.subscribeAlarm(AlarmCode.DEVICE_OFFLINE,"/tetris-capacity/alarm/11011000",Boolean.FALSE);
				LOGGER.info("告警订阅成功");
				break;
			} catch (Exception e) {
				LOGGER.error("告警订阅失败: 剩余重试次数"+retryTime,e);
			}
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (retryTime <= 0){
			throw new BaseException(StatusCode.ERROR,"重试5分钟后，告警订阅失败");
		}
	}

	/**
	 * @MethodName: receiveAlarm
	 * @Description: TODO 接收并处理告警
	 * @param code 1
	 * @param alarmNotifyBO 2
	 * @Return: void
	 * @Author: Poemafar
	 * @Date: 2020/12/28 10:30
	 **/
	public void receiveAlarm(String code, AlarmNotifyBO alarmNotifyBO) {
		 if (AlarmCode.DEVICE_OFFLINE.equals(code)){
			 DevicePO device = deviceDao.findByDeviceIp(alarmNotifyBO.getAlarmDevice());
			 if (device == null){
				return;
			 }
			 if ("AUTO_RECOVER".equals(alarmNotifyBO.getAlarmStatus())){
			 	if (FunUnitStatus.OFF_LINE.equals(device.getFunUnitStatus())) {//是不同步的话，状态就不能改了
					deviceDao.updateFunUnitStatusById(FunUnitStatus.NORMAL, device.getId());
				}
			 }else if("UNTREATED".equals(alarmNotifyBO.getAlarmStatus())){
				 //设备离线，触发主备切换
				 deviceDao.updateFunUnitStatusById(FunUnitStatus.OFF_LINE,device.getId());
				 //判断下是否控制口断链
				 try {
				 	if (netCardHttpUnit.getNetCardInfo(device).isEmpty()){
						if (backupConditionService.checkAutoBackupByCtrlPort()){
							backupService.triggerAutoBackup(alarmNotifyBO.getAlarmDevice());
						}
					}else{
						backupService.triggerAutoBackup(alarmNotifyBO.getAlarmDevice());
					}
				 } catch (BaseException e) {
				 	LOGGER.info("netcard get fail, {}",e.getMessage());
				 	if (backupConditionService.checkAutoBackupByCtrlPort()){
						backupService.triggerAutoBackup(alarmNotifyBO.getAlarmDevice());
					}
				 }

			 }
		 }
	}

	public void netCardNoticeHandleFromXtool(String netStatus,Long deviceId){
		DevicePO devicePO = deviceDao.findOne(deviceId);
		if(devicePO == null){
			LOGGER.error("handleNetCardNotice err, device null");
			return;
		}
		DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(devicePO.getDeviceGroupId());
		if(deviceGroupPO == null){
			LOGGER.error("handleNetCardNotice err, deviceGroupPO null");
			return;
		}
		//解析，获取所有网卡状态，转为方便存取的map
		Map<String, Boolean> statusMap = new HashMap();
		try {
			JSONObject statusJsonObject = JSONObject.parseObject(netStatus);
			String type = statusJsonObject.getString("type");
			if(type == null || !type.equals("netchange")){
				return;
			}
			JSONArray netcardJsonArray = statusJsonObject.getJSONArray("netcard");

			for(int index = 0; index < netcardJsonArray.size(); index ++){
				JSONObject netcardJsonObject = netcardJsonArray.getJSONObject(index);
				String ip = netcardJsonObject.getString("ipaddr");
				boolean linkStatus = netcardJsonObject.getBooleanValue("linked");
				if(ip != null && !ip.equals("")){
					statusMap.put(ip, linkStatus);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("netcard link status");
		}

		/**
		 * 上报网口告警目前存在一个小概率问题：集群重启，期间是收不到小工具的网卡切换消息的
		 * 当然有逻辑能解决，就是在初始化的时候，获取到故障网卡，直接上报告警。这个牵扯的面可能有点宽，暂时认为没必要
		 * 下面代码的两条关键逻辑：
		 * 1、网卡异常变为正常：只要有告警，就发恢复； 判断网卡是否有配置，有的话触发失败任务重建
		 * 2、网卡正常变为异常：判断网卡是否有配置，有则触发备份+告警，无配置则啥也不做。
		 */
		List<NetCardInfoPO> errorNets=new ArrayList<>();
		for(Map.Entry<String, Boolean> entry : statusMap.entrySet()){
			List<NetCardInfoPO> netCardInfoPOs = netCardInfoDao.findByIpv4(entry.getKey());
			//正常按ip只会查到一个，这个for循环正常逻辑其实用不到，算是多一层保护吧
			for(NetCardInfoPO netCardInfoPO : netCardInfoPOs){
				//小概率存在查到的网卡ip不在上报设备上，所以过滤一下
				if(!netCardInfoPO.getDeviceId().equals(devicePO.getId())){//又是被!=号坑了,java基础，不要用==判对象相等了
					continue;
				}
				if(entry.getValue()){
					//网口正常
					if(netCardInfoPO.getStatus().equals(0)){
						netCardInfoDao.updateNetCardStatusById(netCardInfoPO.getId(), 1);
						//具有网络分组配置的网口恢复正常，触发任务重建操作
					}
//					alarmCtrUnit.recoverAlarm();
				}else{
					//网口异常
					if(netCardInfoPO.getInputNetGroupId() == null && netCardInfoPO.getOutputNetGroupId() == null){
						/*
						 * 没有配置网络分组的网卡，没有在业务上使用，所以不用触发备份和上报告警
						 * 上面的恢复之所以没判断，是因为有可能上报告警后网卡配置变了，这种情况还是需要能够恢复的
						 */
						continue;
					}
					if(netCardInfoPO.getStatus().equals(1)){
						netCardInfoDao.updateNetCardStatusById(netCardInfoPO.getId(), 0);
						//记录异常网卡
						errorNets.add(netCardInfoPO);
					}
//					alarmCtrUnit.alertAlarm();
				}
			}
		}
		if (backupConditionService.checkAutoBackupByNetCardError(errorNets)) {
			backupService.triggerAutoBackup(devicePO.getDeviceIp());
		}
	}
}
