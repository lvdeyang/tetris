package com.sumavision.tetris.sts.device;

import com.sumavision.tetris.sts.device.auth.ChannelVideoTypeDao;
import com.sumavision.tetris.sts.device.auth.ChannelVideoTypePO;
import com.sumavision.tetris.sts.device.auth.DeviceChannelAuthPO;
import com.sumavision.tetris.sts.device.auth.EncapsulateAuthPO;
import com.sumavision.tetris.sts.device.group.DeviceGroupDao;
import com.sumavision.tetris.sts.device.group.DeviceGroupPO;
import com.sumavision.tetris.sts.device.group.DeviceGroupVO;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.sts.device.node.DeviceNodeDao;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;
import com.sumavision.tetris.sts.netgroup.NetGroupDao;
import com.sumavision.tetris.sts.netgroup.NetGroupPO;
import com.sumavision.tetris.sts.task.source.SourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Lost on 2017/2/27.
 */
@Component
public class DeviceDaoService {

    @Autowired
    DeviceGroupDao deviceGroupDao;

    @Autowired
    DeviceNodeDao deviceNodeDao;

    @Autowired
    NetGroupDao netGroupDao;

    @Autowired
    NetCardInfoDao netCardInfoDao;

//    @Autowired
//    TaskLinkDaoService taskLinkDaoService;
    
    @Autowired
    SourceDao sourceDao;

    @Autowired
    ChannelVideoTypeDao channelVideoTypeDao;

    @Autowired
    DeviceDao deviceDao;
    



    /*
    * 刷源查找设备
    * */
//    public String findDeviceBySource(SourcePO sourcePO){
//    	if(sourcePO.getSourceType().equals(SourcePO.SourceType.SDI)){
//    		DeviceNodePO deviceNodePO = deviceNodeDao.findOne(sourcePO.getDeviceId());
//    		if(deviceNodePO == null){
//    			throw new BaseException(StatusCode.ERROR, ErrorCodes.SYS_ERR);
//    		}
//    		if(deviceNodePO.getTransSocketAddress() == null){
//    			throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.NO_UNIT));
//    		}
//    		return deviceNodePO.getDeviceIp();
//    	}else{
//    		if (sourcePO.getDisplayType().equals(CommonConstants.INPUT_UNICAST)
//                    && (sourcePO.getProtoType().equals(ProtoType.TSUDP)
//                    || sourcePO.getProtoType().equals(ProtoType.TSRTP))) {
//    			//优先找网关，没有网关的情况下，如果源是tsudp，再查找转码设备
//                List<DeviceNodePO> deviceNodePOS = deviceNodeDao.findEncapsulate(sourcePO.getSourceIp());
//                if (deviceNodePOS.isEmpty()) {
//                	if(!sourcePO.getProtoType().equals(ProtoType.TSUDP)){
//                		throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.NO_UNIT));
//                	}else{
//                		deviceNodePOS = deviceNodeDao.findTrans(sourcePO.getSourceIp());
//                		if(deviceNodePOS.isEmpty()){
//                			throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.NO_UNIT));
//                		}
//                	}
//                }
//                //正常使用场景中，单播源的ip只能查出来一个设备，如果这个设备下没有对应网卡分组的网卡，则找不到刷源设备
//                for(DeviceNodePO deviceNodePO : deviceNodePOS){
//                	String localIp = findDataLocalIp(deviceNodePO.getId(), sourcePO.getNetGroupId());
//                	if(localIp != null){
//                		sourcePO.setLocalIp(sourcePO.getSourceIp());
//                		if(deviceNodePO.getEncapsulateSocketAddress() != null){
//                			return deviceNodePO.getEncapsulateSocketAddress();
//                		}else{
//                			return deviceNodePO.getTransSocketAddress();
//                		}
//                	}
//                }
//                throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.NO_UNIT));
//            } else {
//                Object[] objects = deviceNodeDao.findEncapsulateAndNetCardByNetGroupId(sourcePO.getNetGroupId());
//                if (objects.length == 0) {
//                	if(!sourcePO.getProtoType().equals(ProtoType.TSUDP)){
//                		throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.NO_UNIT));
//                	}else{
//                		objects = deviceNodeDao.findTransAndNetCardByNetGroupId(sourcePO.getNetGroupId());
//                		if(objects.length == 0){
//                			throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.NO_UNIT));
//                		}
//                	}
//                }
//                Object[] obj = (Object[])objects[0];
//                NetCardInfoPO netCardInfoPO = (NetCardInfoPO)obj[1];
//                sourcePO.setLocalIp(netCardInfoPO.getVirtualIpv4() == null ? netCardInfoPO.getIpv4() : netCardInfoPO.getVirtualIpv4());
//                return ((DeviceNodePO)obj[0]).getEncapsulateSocketAddress() != null ?
//                		((DeviceNodePO)obj[0]).getEncapsulateSocketAddress() :
//                			((DeviceNodePO)obj[0]).getTransSocketAddress();
//            }
//    	}
//
//    }

    /*
    * 页面返回
    * */
    public Map findDeviceGroupAndNetGroups() {
        Map map = new HashMap();
        List<DeviceGroupPO> deviceGroupPOs = deviceGroupDao.findAll();//findByTypeNot(GroupType.SDM);
        map.put("deviceGroups" , deviceGroupPOs);
        List<NetGroupPO> netGroupPOs = netGroupDao.findOutputNetGroup();
        map.put("netGroups" , netGroupPOs);
        return map;
    }

    /*
    * 页面返回
    * */
//    public Map findDeviceGroupMap(Long id) {
//        Map map = new HashMap();
//        DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(id);
//        map.put("deviceGroup" , deviceGroupPO);
//        List<DeviceInfoVO> deviceVOS = new ArrayList<>();
//        List<DeviceNodePO> deviceNodePOS = deviceNodeDao.findByDeviceGroupId(id);
//        deviceNodePOS.stream().forEach(deviceInfoPO -> deviceVOS.add(new DeviceInfoVO(deviceInfoPO)));
//        map.put("devices" , deviceVOS);
//        Map deviceMap = new HashMap();
//        deviceMap.put(BackType.MAIN , deviceNodeDao.findCountMainByDeviceGroupId(id));
//        deviceMap.put(BackType.BACK , deviceNodeDao.findCountBackUpByDeviceGroupId(id));
//        map.put("deviceNum" , deviceMap);
//        map.put("taskNum" , taskLinkDaoService.findAllStatusTasklinkNumBySdmGroupId(id));
//        return map;
//    }

    /*
    * 页面返回
    * */
//    public Map findSdmDevice(Long id) {
//        Map map = new HashMap();
//        DevicePO sdmDevice = deviceDao.findOne(id);
//        DeviceVO sdmDeviceVO = new DeviceVO(sdmDevice);
//        DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(sdmDevice.getGroupId());
//        sdmDeviceVO.setDataNetIds(deviceGroupPO.getDataNetIds());
//        sdmDeviceVO.setTransmitNetId(deviceGroupPO.getTransmitNetId());
//        List<DeviceInfoVO> deviceNodes = new ArrayList<>();
//        List<DeviceChannelAuthPO> deviceChannelAuthPOS = new ArrayList<>();
//        for(DeviceNodePO deviceNodePO : deviceNodeDao.findByDeviceId(id)) {
////            DeviceInfoVO deviceNode = new DeviceInfoVO(deviceNodePO, netCardInfoDao.findByDeviceIdOrderByIndexNum(deviceNodePO.getId()));
//            //sdm生产版本  过滤掉控制口 yzx add
//            List<NetCardInfoPO> netCardInfoPOS = null;
//            if (constantUtil.getDeployType().equals("prod")){
//               netCardInfoPOS = netCardInfoDao.findByDeviceIdAndBeCtrlOrderByIndexNum(deviceNodePO.getId(),false);
//            }else{
//               netCardInfoPOS = netCardInfoDao.findByDeviceIdOrderByIndexNum(deviceNodePO.getId());
//            }
//            DeviceInfoVO deviceNode = new DeviceInfoVO(deviceNodePO, netCardInfoPOS);
//            deviceNodes.add(deviceNode);
//            deviceChannelAuthPOS.addAll(deviceNodePO.getDeviceChannelAuthPOs());
//        }
//        sdmDeviceVO.setAuthChannels(DeviceChannelAuthVO.getAuthVOList(deviceChannelAuthPOS));
//        deviceNodes.sort(Comparator.comparing(DeviceInfoVO::getPosition));
//        sdmDeviceVO.setDeviceInfoVOs(deviceNodes);
//        map.put("sdmDevice" , sdmDeviceVO);
//        map.put("netGroups" , netGroupDao.findAll());
//        map.put("taskNum" , taskLinkDaoService.findAllStatusTasklinkNumBySdmDeviceId(id));
//
//        //返回同一设备分组内的备设备
//        List<DeviceVO> backDevices = new ArrayList<DeviceVO>();
//        for(DevicePO devicePO : deviceDao.findByGroupIdAndDeviceTypeAndBackType(sdmDevice.getGroupId(), sdmDevice.getDeviceType(), BackType.BACK)){
//        	backDevices.add(new DeviceVO(devicePO));
//        }
//        map.put("backDevices", backDevices);
//        return map;
//    }

    /*
    * 页面返回
    * */
    public List<DeviceGroupVO> findDeviceGroup() {
        List<DeviceGroupVO> deviceGroups = new ArrayList<>();
        List<DeviceGroupPO> deviceGroupPOs = deviceGroupDao.findAll();
        for (DeviceGroupPO deviceGroupPO : deviceGroupPOs) {
        	DeviceGroupVO deviceGroupVO = new DeviceGroupVO(deviceGroupPO);
        	List<DevicePO> devicePOs = deviceDao.findByGroupId(deviceGroupPO.getId());
        	devicePOs.stream().forEach(devicePO -> {
				DeviceVO deviceVO = new DeviceVO(devicePO, deviceNodeDao.findByDeviceId(devicePO.getId()));
				deviceGroupVO.getDevices().add(deviceVO);
        	});
        	
        	deviceGroups.add(deviceGroupVO);
        }
        return deviceGroups;
    }
    
//    /*
//     * 页面返回 设备&&状态
//     * */
//     public Map findAllDeviceStatus() {
//         Map map = new HashMap();
//         List<DeviceNodePO> deviceNodePOS = deviceNodeDao.findAll();
//         deviceNodePOS.stream().forEach(deviceInfoPO -> map.put(deviceInfoPO.getDeviceIp(),deviceInfoPO.getEncapsulateStatus()));
//         return map;
//     }
    

     /*
    * 页面返回
    * */
//     public Map findDevice(Long id) {
//         Map map = new HashMap();
//         DeviceNodePO deviceNodePO = deviceNodeDao.findOne(id);
//         DevicePO devicePO = deviceDao.findOne(deviceNodePO.getDeviceId());
////         DeviceInfoVO deviceNodeVO = new DeviceInfoVO(deviceNodePO, netCardInfoDao.findByDeviceIdOrderByIndexNum(id));
//         //sdm过滤掉控制口 yzx add
//         List<NetCardInfoPO> netCardInfoPOS = null;
//         netCardInfoPOS = netCardInfoDao.findByDeviceIdOrderByIndexNum(id);
//         DeviceInfoVO deviceNodeVO = new DeviceInfoVO(deviceNodePO, netCardInfoPOS);
//         DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(deviceNodePO.getDeviceGroupId());
//         map.put("deviceNode" , deviceNodeVO);
//         map.put("device", new DeviceVO(devicePO));
//         map.put("deviceGroup" , deviceGroupPO);
//         map.put("card" , sourceDao.findByDeviceId(id));
//         //map.put("taskNum" , taskLinkDaoService.findAllStatusTaskLinkNumByDeviceId(id));
//         map.put("taskNum" , taskLinkDaoService.findAllStatusTasklinkNumBySdmDeviceId(id));
//         map.put("netGroups" , netGroupDao.findAll());
//
//         //返回同一设备分组内的备设备
//         List<DeviceVO> backDevices = new ArrayList<DeviceVO>();
//         for(DevicePO backDevice : deviceDao.findByGroupIdAndDeviceTypeAndBackType(deviceNodePO.getDeviceGroupId(), devicePO.getDeviceType(), BackType.BACK)){
//         	backDevices.add(new DeviceVO(backDevice));
//         }
//         map.put("backDevices", backDevices);
//
//         return map;
//     }

	public String findDataLocalIp(Long deviceId , Long netGroupId) {
        List<NetCardInfoPO> netCardInfoPOs = netCardInfoDao.findByNetGroupIdAndDeviceIdAndStatus(netGroupId, deviceId, NetCardInfoPO.LINK_STATUS_NORMAL);
        if (netCardInfoPOs != null && netCardInfoPOs.size() > 0) {
            return netCardInfoPOs.get(0).getIpv4();
        }
        return null;
    }

	public String findTransmitLocalIp(Long deviceId) {
        NetCardInfoPO netCardInfoPO = netCardInfoDao.findTopByDeviceIdAndBeCtrlAndStatus(deviceId , true, NetCardInfoPO.LINK_STATUS_NORMAL);
        if (netCardInfoPO != null) {
            return netCardInfoPO.getIpv4();
        }else{
        	DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceId);
        	return deviceNodePO.getDeviceIp();
        }
    }
	
	public String findTransmitLocalIp(Long deviceId,Long transmitId) {
		NetCardInfoPO netCardInfoPO = null;
		if(null == transmitId){
			netCardInfoPO = netCardInfoDao.findTopByDeviceIdAndBeCtrlAndStatus(deviceId , true, NetCardInfoPO.LINK_STATUS_NORMAL);
		} else {
			netCardInfoPO = netCardInfoDao.findTopByDeviceIdAndNetGroupIdAndStatus(deviceId, transmitId, NetCardInfoPO.LINK_STATUS_NORMAL);
		}
        if (netCardInfoPO != null) {
            return netCardInfoPO.getIpv4();
        }else{
        	DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceId);
        	return deviceNodePO.getDeviceIp();
        }
    }


    /**
     * 备份后授权归零
     * 
     */
    public void setDeviceAuthUsedZero(){
    	List<DeviceNodePO> deviceInfoList = deviceNodeDao.findAll();
    	for (DeviceNodePO deviceNodePO : deviceInfoList) {
    		if(deviceNodePO != null){
    			EncapsulateAuthPO encapsulateAuthPO = deviceNodePO.getEncapsulateAuthPO();
    			encapsulateAuthPO.setOutputRestNum(encapsulateAuthPO.getOutputNum());
    			List<DeviceChannelAuthPO> deviceChannelAuthPOs = deviceNodePO.getDeviceChannelAuthPOs();
    			for (DeviceChannelAuthPO deviceChannelAuthPO : deviceChannelAuthPOs) {
					deviceChannelAuthPO.setAuthNumRest(deviceChannelAuthPO.getAuthNum());
				}
        	}
        	deviceNodeDao.save(deviceNodePO);
		}
    }

    

    /**
     * 根据sdm查询授权相关的剩余和已用
     * @return
     */
    public Map<String, Integer> findSumAuth(){
    	Map<String, Integer> detailMap = new HashMap<String, Integer>();
    	//List<DeviceAuthPO> deviceAuthPOs = deviceAuthDao.findAll();
    	Integer cpuFreeNum = 0;
    	Integer cpuUsedNum = 0;
    	Integer cpuNum = 0;
    	
    	Integer gpuFreeNum = 0;
    	Integer gpuUsedNum = 0;
    	Integer gpuNum = 0;
    	
//    	for(DeviceAuthPO deviceAuth : deviceAuthPOs){
//    		if (deviceAuth != null) {
//    			cpuFreeNum += (deviceAuth.getCpuNum() - deviceAuth.getCpuUsedNum());
//        		cpuUsedNum += deviceAuth.getCpuUsedNum();
//        		cpuNum += deviceAuth.getCpuNum();
//        		gpuFreeNum += (deviceAuth.getGpuNum() - deviceAuth.getGpuUsedNum());
//        		gpuUsedNum += deviceAuth.getGpuUsedNum();
//        		gpuNum += deviceAuth.getGpuNum();
//    		}
//    	}
    	detailMap.put("cpuFreeNum", cpuFreeNum);
    	detailMap.put("cpuUsedNum", cpuUsedNum);
    	detailMap.put("cpuNum", cpuNum);
    	
    	detailMap.put("gpuFreeNum", gpuFreeNum);
    	detailMap.put("gpuUsedNum", gpuUsedNum);
    	detailMap.put("gpuNum", gpuNum);
    	return detailMap;
    }
    

    
//    public BackType getBackTypeBySocketAddress(String socketAddress){
//    	FunUnitType funUnitType = SystemCtrl.getFunUnitType(socketAddress);
//    	if(funUnitType == null){
//    		return null;
//    	}
//    	DeviceNodePO deviceNodePO = deviceNodeDao.findBySocketAddress(socketAddress);
//    	if(deviceNodePO == null){
//    		return null;
//    	}
//    	switch(funUnitType){
//    	case TRANS:
//    		return deviceNodePO.getTransBackType();
//    	case ENCAPSULATE:
//    		return deviceNodePO.getEncapsulateBackType();
//		default:
//			return null;
//    	}
//    }
    
//    public List<DeviceNodePO> findDeviceOrderByCpuUsed(Long deviceGroupId){
//    	List<DeviceNodePO> deviceInfoPOs = deviceInfoDao.findByDeviceGroupIdOrderByDeviceAuth(deviceGroupId);
//    	for(Iterator<DeviceNodePO> it = deviceInfoPOs.iterator();it.hasNext();){
//    		DeviceNodePO deviceInfoPO = it.next();
////    		if(deviceInfoPO.getDeviceAuthPO().getCpuNum() == null || deviceInfoPO.getDeviceAuthPO().getCpuNum() == 0){
////    			it.remove();
////    		}
//    	}
//    	if(deviceInfoPOs.size() == 0){
//    		return null;
//    	}
//    	return deviceInfoPOs;
//    }
    
	public List<Long> findAllSdmDeviceId(Long deviceGroupId ) {
		List<DeviceNodePO> deviceNodePOS = deviceNodeDao.findByDeviceGroupId(deviceGroupId);
		List<Long> deviceIds = new ArrayList<Long>();
		for (Iterator<DeviceNodePO> it = deviceNodePOS.iterator(); it.hasNext();) {
			DeviceNodePO deviceNodePO = it.next();
			deviceIds.add(deviceNodePO.getId());
		}
		return deviceIds;
	}
    


    public ChannelVideoTypePO getChannleTypeByVideoTypeLikeAndWidthAndHeightAndType(String videoType , Integer width, Integer height, String type){
    	if(type.equals("in")){
    		if(videoType.contains("avs")){
    			videoType = "avs";
    		}else if(!videoType.equals("yuv")){
    			videoType = "non-avs";
    		}
    	}else if(videoType.contains("avs")){
    		videoType = "avs";
    	}

        ResolutionBO taskResolutionBO = new ResolutionBO(width,height);
        return channelVideoTypeDao.findTopByVideoTypeAndWidthAndHeightAndType(videoType,taskResolutionBO.getWidth(),taskResolutionBO.getHeight(),type);
    }



	public DeviceNodeDao getDeviceNodeDao() {
		return deviceNodeDao;
	}


	/**
     * 设备统计：总数量、NONE(不统计) 、DEFAULT、NORMAL 、NEED_SYNC、OFF_LINE(转码与网关分开统计)
     * @return count
     */
//	public int findTransDefaultCount() {
//		return deviceNodeDao.findTransDefaultCount();
//	}
//	public int findTransNeedSyncCount() {
//		return deviceNodeDao.findTransNeedSyncCount();
//	}
//	public int findTransNormalCount() {
//		return deviceNodeDao.findTransNormalCount();
//	}
//	public int findTransOfflineCount() {
//		return deviceNodeDao.findTransOfflineCount();
//	}
//	public int findTransTotalCount() {
//		return deviceNodeDao.findTransTotalCount();
//	}
//
//	public int findEncapsulateDefaultCount() {
//		return deviceNodeDao.findEncapsulateDefaultCount();
//	}
//	public int findEncapsulateNeedSyncCount() {
//		return deviceNodeDao.findEncapsulateNeedSyncCount();
//	}
//	public int findEncapsulateNormalCount() {
//		return deviceNodeDao.findEncapsulateNormalCount();
//	}
//	public int findEncapsulateOfflineCount() {
//		return deviceNodeDao.findEncapsulateOfflineCount();
//	}
//	public int findEncapsulateTotalCount() {
//		return deviceNodeDao.findEncapsulateTotalCount();
//	}
    
    
}
