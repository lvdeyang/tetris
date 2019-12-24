package com.sumavision.tetris.sts.device.monitor;

import com.alibaba.fastjson.JSONObject;
import com.suma.xianrd.device.Handler;
import com.suma.xianrd.device.info.DeviceBO;
import com.suma.xianrd.device.info.GpuDetailBO;
import com.sumavision.tetris.sts.common.CommonConstants.*;
import com.sumavision.tetris.sts.device.DeviceDaoService;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;
import com.sumavision.tetris.sts.device.threshold.ThresholdDao;
import com.sumavision.tetris.sts.device.threshold.ThresholdPO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.sts.device.monitor.DeviceMonitorDataPO.DeviceMonitorDateType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Component
public class DeviceMonitorService implements Runnable{

	private final Logger logger = LogManager.getLogger(DeviceMonitorService.class);
	
	@Autowired
	private DeviceMonitorDataDao deviceMonitorDataDao;
	
	@Autowired
	private DeviceDaoService deviceDaoService;
	
	@Autowired
	@Qualifier("DeviceMonitorHandler")
	private Handler deviceMonitorHandler;
	
	@Autowired
	private ThresholdDao thresholdDao;
	
//	@Autowired
//	private AlarmService alarmService;
	
//	@Autowired
//	private BackUpService backUpService;

	private ExecutorService cachedThreadPool;

	private ConcurrentHashMap<String,DeviceMonitorDataHandle> monitorDataHandleMap = new ConcurrentHashMap<>();

	@Scheduled(cron = "0 0/15 0/1 * * ? ")
	public void caculateAverageDeviceData(){
//		if(!backUpService.isMain()){
//			return;
//		}
		logger.info("caculateAverageDeviceData begin");
		List<DeviceNodePO> devicePOs = deviceDaoService.getDeviceNodeDao().findAll();
		Date curDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		calendar.add(Calendar.MINUTE, -15);
		for(DeviceNodePO deviceNodePO : devicePOs){
			List<DeviceMonitorDataPO> deviceMonitorDataPOs = deviceMonitorDataDao.
					findByDeviceIdAndDateTypeAndGetTimeBetween(deviceNodePO.getId(), DeviceMonitorDateType.ORIGINAL, calendar.getTime(), curDate);
			if(deviceMonitorDataPOs.size() == 0){
				continue;
			}
			Integer memOccupy = 0;
			Integer cpuOccupy = 0;
			Integer cpuTempture = 0;
			Integer gpuOccupy = 0;
			for(DeviceMonitorDataPO deviceMonitorDataPO : deviceMonitorDataPOs){
				memOccupy += deviceMonitorDataPO.getMemOccupy();
				cpuOccupy += deviceMonitorDataPO.getCpuOccupy();
				cpuTempture += deviceMonitorDataPO.getCpuTemperature();
				if(null != deviceMonitorDataPO.getGpuOccupy()){
					gpuOccupy += deviceMonitorDataPO.getGpuOccupy();
				}
			}
			DeviceMonitorDataPO averageMonitorDataPO = new DeviceMonitorDataPO();
			averageMonitorDataPO.setDeviceId(deviceNodePO.getId());
			averageMonitorDataPO.setGetTime(curDate);
			averageMonitorDataPO.setDateType(DeviceMonitorDataPO.DeviceMonitorDateType.FIFTEEN_MINUTES_AVERAGE);
			averageMonitorDataPO.setMemOccupy(memOccupy / deviceMonitorDataPOs.size());
			averageMonitorDataPO.setCpuOccupy(cpuOccupy / deviceMonitorDataPOs.size());
			averageMonitorDataPO.setCpuTemperature(cpuTempture / deviceMonitorDataPOs.size());
			averageMonitorDataPO.setGpuOccupy(gpuOccupy / deviceMonitorDataPOs.size());
			deviceMonitorDataDao.save(averageMonitorDataPO);
		}
		
		//清理历史数据，包括一天前的原始数据和一个月前的平均数据
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		deviceMonitorDataDao.deleteByDateTypeAndGetTimeLessThan(DeviceMonitorDateType.ORIGINAL, calendar.getTime());
		calendar.add(Calendar.DAY_OF_YEAR, -29);
		deviceMonitorDataDao.deleteByDateTypeAndGetTimeLessThan(DeviceMonitorDateType.FIFTEEN_MINUTES_AVERAGE, calendar.getTime());
	}
	
	/**
	 * 启动监测，设备添加时触发
	 * @param
	 */
	public void startMonitor(String deviceIp){
		try {
			deviceMonitorHandler.start(deviceIp, 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("start monitor err, ip :" + deviceIp,e);
		}
	}
	
	/**
	 * 停止监测，删除设备时触发
	 * @param
	 */
	public void stopMonitor(String deviceIp){
		try {
			deviceMonitorHandler.stop(deviceIp);
			if(monitorDataHandleMap.get(deviceIp) != null){
				//空消息会让处理线程停止
				monitorDataHandleMap.remove(deviceIp);//majing edit
				// origin code: monitorDataHandleMap.get(deviceIp).putMonitorData(new DeviceBO());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("stop monitor err, ip :" + deviceIp);
		}
	}
	
	/**
	 * 初始化时调用
	 * @param cachedThreadPool
	 */
	public void init(ExecutorService cachedThreadPool){
		List<DeviceNodePO> devicePOs = deviceDaoService.getDeviceNodeDao().findAll();
		for(DeviceNodePO deviceNodePO : devicePOs){
			startMonitor(deviceNodePO.getDeviceIp());
		}
		setCachedThreadPool(cachedThreadPool);
		cachedThreadPool.execute(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//获取数据，并存储数据
		logger.info("monitor get data begin");
		while(true){
			try {
				DeviceBO deviceBO = deviceMonitorHandler.take();
//				logger.info("monitor get data :"  + JSONObject.toJSONString(deviceBO));
				if(monitorDataHandleMap.get(deviceBO.getHostIp()) == null){
					DeviceNodePO deviceNodePO = deviceDaoService.getDeviceNodeDao().findByDeviceIp(deviceBO.getHostIp());
					if(deviceNodePO == null){
						//收到了设备不存在ip的监测信息，针对此ip发送不再监测的消息
						deviceMonitorHandler.stop(deviceBO.getHostIp());
						continue;
					}
					
					DeviceMonitorDataHandle dataHandle = monitorDataHandleMap.get(deviceBO.getHostIp());
					if(null == dataHandle){
						dataHandle = new DeviceMonitorDataHandle(deviceNodePO.getId(),deviceNodePO.getDeviceIp());
						monitorDataHandleMap.put(deviceBO.getHostIp(),dataHandle);
						cachedThreadPool.execute(dataHandle);
					}
					dataHandle.putMonitorData(deviceBO);
				}else{
					monitorDataHandleMap.get(deviceBO.getHostIp()).putMonitorData(deviceBO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("get devcie monitor data err",e);
			}
		}
	}
	
	
	public List<DeviceMonitorBO> getDeviceMonitorData(Long deviceId, DeviceMonitorTime time){
		List<DeviceMonitorBO> result = new ArrayList<DeviceMonitorBO>();
		Date curDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		DeviceMonitorDateType dateType = DeviceMonitorDateType.ORIGINAL;
		switch(time){
		case ONE_HOUR:
			calendar.add(Calendar.HOUR, -1);
			dateType = DeviceMonitorDateType.ORIGINAL;
			break;
		case SIX_HOUR:
			calendar.add(Calendar.HOUR, -6);
			dateType = DeviceMonitorDateType.ORIGINAL;
			break;
		case ONE_DAY:
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			dateType = DeviceMonitorDateType.FIFTEEN_MINUTES_AVERAGE;
			break;
		case ONE_WEEK:
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			dateType = DeviceMonitorDateType.FIFTEEN_MINUTES_AVERAGE;
			break;
		}
		
		List<DeviceMonitorDataPO> deviceMonitorDataPOs = deviceMonitorDataDao.
				findByDeviceIdAndDateTypeAndGetTimeBetween(deviceId, dateType, calendar.getTime(), curDate);
		//平均数据少于10条，取原始数据
		if(dateType.equals(DeviceMonitorDateType.FIFTEEN_MINUTES_AVERAGE)
				&& deviceMonitorDataPOs.size() < 10){
			dateType = DeviceMonitorDateType.ORIGINAL;
			deviceMonitorDataPOs = deviceMonitorDataDao.
					findByDeviceIdAndDateTypeAndGetTimeBetween(deviceId, dateType, calendar.getTime(), curDate);
		}
		int filterIndex = deviceMonitorDataPOs.size() / 100;
		filterIndex = filterIndex == 0 ? 1 : filterIndex;
		for(int index = 0; index < deviceMonitorDataPOs.size(); index++){
			if(index % filterIndex != 0){
				continue;
			}
			DeviceMonitorDataPO deviceMonitorDataPO = deviceMonitorDataPOs.get(index);
			result.add(new DeviceMonitorBO(deviceMonitorDataPO));
		}
		return result;
	}
	
	public List<DeviceMonitorBO> getDeviceMonitorData(Long deviceId){
		List<DeviceMonitorBO> result = new ArrayList<DeviceMonitorBO>();
		Page<DeviceMonitorDataPO> page = deviceMonitorDataDao.
				findByDeviceIdAndDateTypeOrderByGetTimeDesc(deviceId, DeviceMonitorDateType.ORIGINAL, new PageRequest(0, 100));
		page.getContent().stream().forEach(po -> {
			result.add(new DeviceMonitorBO(po));
		});
		return result;
	}
	
	//修改监测告警阈值
	public void setDeviceThresholdData(Integer cpu, Integer gpu, Integer memory, Integer temperature,Integer diskOccupyTh,Long netCardFlowMax){
		ThresholdPO thresholdPO = thresholdDao.findTopByIdIsNotNull();
		thresholdPO.setCpuOccupyTh(cpu);
		thresholdPO.setGpuOccupyTh(gpu);
		thresholdPO.setMemOccupyTh(memory);
		thresholdPO.setCpuTemperatureTh(temperature);
		thresholdPO.setDiskOccupyTh(diskOccupyTh);
		thresholdPO.setNetCardFlowMax(netCardFlowMax);
		thresholdDao.save(thresholdPO);
	}

	//从小工具上报的GPU信息中获取设备上编码分最低的卡的卡号
	public Integer queryBestCardNum(String deviceIP){
		Integer cardIndex = 0;
		if(monitorDataHandleMap.get(deviceIP) != null){
			DeviceBO deviceBO = monitorDataHandleMap.get(deviceIP).getLastDeviceBo();
			if(deviceBO.getGpu() != null && deviceBO.getGpu().getDetail() != null){
				Integer curEncoder = 100;
				for(GpuDetailBO detailBO : deviceBO.getGpu().getDetail()){
					if(detailBO.getEncoder() < curEncoder){
						curEncoder = detailBO.getEncoder();
						cardIndex = detailBO.getIndex();
					}
				}
			}

		}
		return cardIndex;
	}
	
	//从小工具上报的GPU信息中按照编码分从低到高的顺序给卡号排序
	public List<Integer> queryBestCardNums(String deviceIP){
		if(monitorDataHandleMap.get(deviceIP) != null){
			DeviceBO deviceBO = monitorDataHandleMap.get(deviceIP).getLastDeviceBo();
			//debug
			logger.info("get last deviceBO : " + JSONObject.toJSONString(deviceBO));
			if(deviceBO.getGpu() != null && deviceBO.getGpu().getDetail() != null){
				return deviceBO.getGpu().getDetail().stream().sorted(new Comparator<GpuDetailBO>() {
					@Override
					public int compare(GpuDetailBO o1, GpuDetailBO o2) {
						return o1.getEncoder() - o2.getEncoder();
					}
				}).map(GpuDetailBO::getIndex).collect(Collectors.toList());
			}
		}
		
		return null;
	}


	public void setCachedThreadPool(ExecutorService cachedThreadPool) {
		this.cachedThreadPool = cachedThreadPool;
	}

	@Bean(name="DeviceMonitorHandler")
	public Handler getDevcieMonitorHandler(){
		return new Handler();
	}
}
