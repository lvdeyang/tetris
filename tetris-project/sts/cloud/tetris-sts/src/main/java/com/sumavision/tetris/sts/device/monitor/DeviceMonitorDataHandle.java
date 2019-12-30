package com.sumavision.tetris.sts.device.monitor;

import com.alibaba.fastjson.JSONArray;
import com.suma.xianrd.device.info.DeviceBO;
import com.suma.xianrd.device.info.NetworkBO;
import com.sumavision.tetris.sts.common.SpringBeanFactory;
import com.sumavision.tetris.sts.device.threshold.ThresholdDao;
import com.sumavision.tetris.sts.device.threshold.ThresholdPO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DeviceMonitorDataHandle implements Runnable{

    static Logger logger = LogManager.getLogger(DeviceMonitorDataHandle.class);

    private Long deviceId;

    private String deviceIp;

    private DeviceMonitorDataDao deviceMonitorDataDao;

    private ThresholdDao thresholdDao;

//    暂时不告警
//    private AlarmService alarmService;

    //最新一次上报的监测数据
    private DeviceBO lastDeviceBo;

    private BlockingQueue<DeviceBO> deviceBOQueue = new LinkedBlockingQueue<>();

    public DeviceMonitorDataHandle(Long deviceId,String deviceIp){
        setDeviceId(deviceId);
        setDeviceIp(deviceIp);
        deviceMonitorDataDao = SpringBeanFactory.getBean(DeviceMonitorDataDao.class);
        thresholdDao = SpringBeanFactory.getBean(ThresholdDao.class);
//        alarmService = SpringBeanFactory.getBean(AlarmService.class);
    }

    @Override
    public void run() {
        int count = 0;
        while(true){
            try {
                DeviceBO deviceBO = deviceBOQueue.take();
//                logger.info("monitor get data :" + JSONObject.toJSONString(deviceBO));
                if(deviceBO.getHostIp() == null || deviceBO.getHostIp().equals("")){
                    logger.info("host : " + deviceIp + " monitor end");
                    break;
                }
                setLastDeviceBo(deviceBO);
                count++;
                if(count < 5){
                    //每收到5次数据，记录并处理一次
                    continue;
                }
                count = 0;

                DeviceMonitorDataPO deviceMonitorDataPO = new DeviceMonitorDataPO();
                deviceMonitorDataPO.setDeviceId(deviceId);
                deviceMonitorDataPO.setGetTime(deviceBO.getCpu().getGettime());
                deviceMonitorDataPO.setCpuOccupy(deviceBO.getCpu().getOccupy());
                deviceMonitorDataPO.setCpuTemperature(deviceBO.getCpu().getTemperature());
                deviceMonitorDataPO.setGpuOccupy(null==deviceBO.getGpu().getOccupy() ? 0 : deviceBO.getGpu().getOccupy());
                deviceMonitorDataPO.setMemOccupy(deviceBO.getMem().getOccupy());
                deviceMonitorDataPO.setMemSize(deviceBO.getMem().getSize());
                if(deviceBO.getDisk().getSize() == 0){
                    deviceMonitorDataPO.setDiskOccupy(0);
                }else{
                    deviceMonitorDataPO.setDiskOccupy((int)(deviceBO.getDisk().getUsed() * 100 / deviceBO.getDisk().getSize()));
                }
                deviceMonitorDataPO.setNetCardFlowJsonArray(JSONArray.toJSONString(deviceBO.getNetarray()));
                deviceMonitorDataDao.save(deviceMonitorDataPO);

                //监测是否超过阈值
                ThresholdPO thresholdPO = thresholdDao.findTopByIdIsNotNull(); //暂时通过Dao查询来实现，之后页面添加修改阈值后，直接从相应的controller中对变量进行修改
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String alarmTimeString = sdf.format(deviceBO.getCpu().getGettime());
                if (thresholdPO.getCpuOccupyTh() < deviceBO.getCpu().getOccupy()) {
                    //上报cpu占用超过阈值告警
//                    alarmService.deviceMonitorHandle(alarmTimeString, deviceIp, AlarmConstants.LOCAL_MONITOR_CPU_ALERT,deviceMonitorDataPO.getId());
                }

                if(null != deviceBO.getGpu().getOccupy() && thresholdPO.getGpuOccupyTh() < deviceBO.getGpu().getOccupy()){
//                	alarmService.deviceMonitorHandle(alarmTimeString, deviceIp, AlarmConstants.LOCAL_MONITOR_GPU_ALERT,deviceMonitorDataPO.getId());
                }

                if (thresholdPO.getMemOccupyTh() <= deviceBO.getMem().getOccupy()) {
//                    alarmService.deviceMonitorHandle(alarmTimeString, deviceIp, AlarmConstants.LOCAL_MONITOR_MEMORY_ALERT,deviceMonitorDataPO.getId());
                }

                if (thresholdPO.getCpuTemperatureTh() <= deviceBO.getCpu().getTemperature()) {
//                    alarmService.deviceMonitorHandle(alarmTimeString, deviceIp, AlarmConstants.LOCAL_MONITOR_TEMPERATURE_ALERT,deviceMonitorDataPO.getId());
                }

                if(thresholdPO.getDiskOccupyTh() <= deviceMonitorDataPO.getDiskOccupy()){
//                    alarmService.deviceMonitorHandle(alarmTimeString, deviceIp, AlarmConstants.LOCAL_MONITOR_DISK_ALERT,deviceMonitorDataPO.getId());
                }

                for(NetworkBO networkBO : deviceBO.getNetarray()){
                    if(thresholdPO.getNetCardFlowMax() <= networkBO.getDownload() || thresholdPO.getNetCardFlowMax() <= networkBO.getUpload()){
//                        alarmService.deviceMonitorHandle(alarmTimeString, deviceIp, AlarmConstants.LOCAL_MONITOR_NETCARD_FLOW_ALERT, deviceMonitorDataPO.getId());
                        //只要有一个网口流量超出，就算一次
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void putMonitorData(DeviceBO deviceBO){
        deviceBOQueue.offer(deviceBO);
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public DeviceBO getLastDeviceBo() {
        return lastDeviceBo;
    }

    public void setLastDeviceBo(DeviceBO lastDeviceBo) {
        this.lastDeviceBo = lastDeviceBo;
    }
}
