package com.sumavision.tetris.application;/**
 * Created by Poemafar on 2020/11/16 16:48
 */

import com.sumavision.tetris.application.alarm.service.AlarmService;
import com.sumavision.tetris.application.template.feign.TemplateTaskService;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.enumeration.BackType;
import com.sumavision.tetris.business.common.enumeration.BackupStrategy;
import com.sumavision.tetris.business.common.enumeration.FunUnitStatus;
import com.sumavision.tetris.business.common.enumeration.NetGroupType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.capacity.TetrisCapacityApplication;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.DeviceService;
import com.sumavision.tetris.device.backup.condition.BackupConditionDao;
import com.sumavision.tetris.device.backup.condition.BackupConditionPO;
import com.sumavision.tetris.device.backup.condition.BackupConditionService;
import com.sumavision.tetris.device.group.DeviceGroupDao;
import com.sumavision.tetris.device.group.DeviceGroupPO;
import com.sumavision.tetris.device.netgroup.NetGroupDao;
import com.sumavision.tetris.device.netgroup.NetGroupPO;
import com.sumavision.tetris.resouce.feign.bundle.BundleFeignService;
import com.sumavision.tetris.resouce.feign.bundle.BundleFeignVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: InitApplication
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/16 16:48
 */
@Component
public class InitApplication implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitApplication.class);

    @Autowired
    TemplateTaskService templateTaskService;

    @Autowired
    TaskInputDAO taskInputDao;

    @Autowired
    NetGroupDao netGroupDao;

    @Autowired
    DeviceGroupDao deviceGroupDao;

    @Autowired
    BundleFeignService bundleFeignService;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DeviceService deviceService;

    @Autowired
    AlarmService alarmService;

    @Autowired
    SyncService syncService;

    @Autowired
    BackupConditionDao backupConditionDao;


    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${constant.init.alarm:true}")
    Boolean initAlarm;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOGGER.info("CAPACITY SERVICE START TO INIT ... ...");

        //初始化数据
        initSqlData();

        //初始化模板
        templateTaskService.init();

        startTimerToClearNoUseInputs();

        //初始化告警
        if (initAlarm) {
            try {
                alarmService.init();
            } catch (Exception e) {
                LOGGER.error("告警初始化失败", e);
                stopService();
            }
        }

        LOGGER.info("CAPACITY SERVICE INIT COMPLETED");
    }

    /**
     * 进程终止
     */
    public void stopService(){
        int exitCode = SpringApplication.exit(webApplicationContext,
                (ExitCodeGenerator) () -> 0);
        System.exit(exitCode);
        LOGGER.info("能力服务进程终止");
    }

    /**
     * 清楚无用输入
     */
    public void startTimerToClearNoUseInputs(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    //1小时清除
                    List<TaskInputPO> inputs = taskInputDao.findByCount(0);
                    if(inputs != null && inputs.size() > 0){
                        Date date = new Date();
                        List<TaskInputPO> needRemoveInputs = new ArrayList<TaskInputPO>();
                        for(TaskInputPO input: inputs){
                            if((date.getTime() - input.getUpdateTime().getTime()) > 1000 * 60 * 60){
                                needRemoveInputs.add(input);
                            }
                        }
                        if(needRemoveInputs.size() > 0){
                            taskInputDao.deleteInBatch(needRemoveInputs);
                        }
                    }

                } catch (Exception e) {
                    LOGGER.error("清除输入线程执行异常！", e);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,0,60*1000);

    }

    /**
     * @MethodName: initSqlData
     * @Description: TODO 初始化数据库数据
     * @Return: void
     * @Author: Poemafar
     * @Date: 2021/1/5 10:08
     **/
    public void initSqlData(){
        initBackupCondition();
        initNetGroup();
        initDeviceGroup();
        initDevices();
    }

    /**
     * 初始化网卡分组
     */
    public void initNetGroup(){
        Map<String, NetGroupType> defaultNetGroup  =  new HashMap<>();
        defaultNetGroup.put("输入",NetGroupType.INPUT);
        defaultNetGroup.put("主输入",NetGroupType.INPUT);
        defaultNetGroup.put("备输入",NetGroupType.INPUT);
        defaultNetGroup.put("输出",NetGroupType.OUTPUT);
        for (String netName:defaultNetGroup.keySet()){
            NetGroupPO netGroupPO = netGroupDao.findTopByNetName(netName);
            if (netGroupPO == null) {
                netGroupPO = new NetGroupPO();
                netGroupPO.setNetName(netName);
                netGroupPO.setNetType(defaultNetGroup.get(netName));
                netGroupDao.save(netGroupPO);
            }
            netGroupPO = null;
        }
    }

    public void initBackupCondition(){
        BackupConditionPO backupConditionPO = backupConditionDao.findTopByIdIsNotNull();
        if (backupConditionPO==null){
            backupConditionPO = new BackupConditionPO();
            backupConditionPO.setDiskOverride(false);
            backupConditionPO.setMemOverride(false);
            backupConditionPO.setCpuOverride(false);
            backupConditionPO.setGpuOverride(false);
            backupConditionPO.setCtrlPortDisconnect(false);
            backupConditionPO.setInputNetCardError(BackupConditionPO.NetCardErrorType.ALL);
            backupConditionPO.setOutputNetCardError(true);
            backupConditionDao.save(backupConditionPO);
        }
    }

    public void initDeviceGroup(){
        DeviceGroupPO deviceGroupPO = deviceGroupDao.findByBeDefault(true);
        if (deviceGroupPO==null){
            deviceGroupPO = new DeviceGroupPO();
            deviceGroupPO.setName("默认分组");
            deviceGroupPO.setUpdateTime(new Date());
            deviceGroupPO.setBackupStrategy(BackupStrategy.NPLUSM);
            deviceGroupPO.setAutoBackupFlag(true);
            deviceGroupPO.setBeDefault(true);
            deviceGroupDao.save(deviceGroupPO);
        }
    }

    public void initDevices(){
        List<DevicePO> all = deviceDao.findAll();
        if (all==null || all.isEmpty()){
            DeviceGroupPO defaultGroup = deviceGroupDao.findByBeDefault(true);
            try {
                List<BundleFeignVO> bundles = bundleFeignService.queryTranscodeDevice();
                if (bundles==null || bundles.isEmpty()){
                    return;
                }
                HashMap<String,BundleFeignVO> deviceMap = new HashMap<>();
                for (int i = 0; i < bundles.size(); i++) {
                    BundleFeignVO bundleFeignVO = bundles.get(i);
                    if (bundleFeignVO.getDeviceIp()==null || bundleFeignVO.getDeviceIp().isEmpty()){
                        continue;
                    }
                    deviceMap.put(bundleFeignVO.getDeviceIp(),bundleFeignVO);
                }
                for (String key: deviceMap.keySet()) {
                    BundleFeignVO bundle = deviceMap.get(key);
                    DevicePO devicePO = new DevicePO();
                    devicePO.setDeviceGroupId(defaultGroup.getId());
                    devicePO.setDeviceIp(bundle.getDeviceIp());
                    devicePO.setDevicePort(bundle.getDevicePort());
                    devicePO.setName(bundle.getBundle_name());
                    devicePO.setBackType(BackType.DEFAULT);
                    devicePO.setBundleId(bundle.getBundle_id());
                    devicePO.setFunUnitStatus(bundle.getBundle_status().equals("ONLINE")? FunUnitStatus.NORMAL:FunUnitStatus.OFF_LINE);
                    deviceDao.save(devicePO);
                    try {
                        //尝试设置告警地址
                        alarmService.setAlarmUrl(devicePO.getDeviceIp());
                    } catch (Exception e) {
                        LOGGER.error("告警地址设置失败",e);
                    }
                    try {
                        deviceService.setNetCardsForDevice(devicePO);//有可能获取网卡信息失败
                    } catch (BaseException e) {
                        LOGGER.warn("网卡信息获取失败",e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //同步设备信息
            for (int j = 0; j < all.size(); j++) {
                DevicePO devicePO = all.get(j);
                if (devicePO.getBundleId()==null){
                    continue;
                }
                try {
                    BundleFeignVO bundle = bundleFeignService.queryDeviceByBundleId(devicePO.getBundleId());
                    if ("OFF_LINE".equals(bundle.getBundle_status())) {
                        deviceDao.updateFunUnitStatusById(FunUnitStatus.OFF_LINE,devicePO.getId());
                    } else if ("ONLINE".equals(bundle.getBundle_status())&& !FunUnitStatus.NORMAL.equals(devicePO.getFunUnitStatus())) {
                        syncService.syncTransform(devicePO.getDeviceIp());
                    }
                } catch (Exception e) {
                    LOGGER.info("device cannot find from resource service, "+devicePO.getId(),e);
                }
            }
        }
    }
}
