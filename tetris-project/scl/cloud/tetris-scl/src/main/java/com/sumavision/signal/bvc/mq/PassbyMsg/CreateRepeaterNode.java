package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 17:15
 */

import com.sumavision.signal.bvc.entity.dao.*;
import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.signal.bvc.service.QueryUtilService;
import com.sumavision.signal.bvc.service.TaskExecuteService;
import com.sumavision.signal.bvc.service.TerminalMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: CreateRepeaterNode
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 17:15
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class CreateRepeaterNode extends AbstractPassbyMsg {

    @Autowired
    private RepeaterDAO repeaterDao;

    @Autowired
    private InternetAccessDAO internetAccessDao;

    @Autowired
    private PortMappingDAO portMappingDao;

    @Autowired
    private QueryUtilService queryUtilService;

    @Autowired
    private TerminalMappingService terminalMappingService;

    @Autowired
    private TaskDAO taskDao;

    @Autowired
    private TaskExecuteService taskExecuteService;


    @Override
    public void exec(PassbyBO passby) throws Exception {
        synchronized (this) {
            //暂时这么取
            List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
            List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);

            RepeaterPO main = null;
            RepeaterPO backup = null;
            if(mainRepeaters.size() > 0) main = mainRepeaters.get(0);
            if(backupRepeaters.size() > 0) backup = backupRepeaters.get(0);

            String bundleId = passby.getBundle_id();
            String channels = passby.getPass_by_content().getString("channels");
            String[] channelIds = channels.split("%");

            //TODO:取一个转发器的输入网口
            InternetAccessPO access = internetAccessDao.findByMainRepeater();
            //查询portMapping是否已经存在
            List<PortMappingPO> mappings = portMappingDao.findBySrcTypeAndSrcBundleId(SrcType.ROLE, bundleId);

            //端口维护
            ConcurrentHashMap<String, ArrayList<Long>> mapping = new ConcurrentHashMap<String, ArrayList<Long>>();
            mapping.put(access.getAddress(), new ArrayList<Long>());

            //查portMapping
            List<PortMappingPO> accessPorts = portMappingDao.findByDstAddressAndDstType(access.getAddress(), DstType.REPEATER);
            for(PortMappingPO accessPort: accessPorts){
                mapping.get(access.getAddress()).add(accessPort.getDstPort());
            }

            List<PortMappingPO> mappingPOs = new ArrayList<PortMappingPO>();
            List<Long> mappingIds = new ArrayList<Long>();
            for(String channelId: channelIds){

                PortMappingPO mappingPO = queryUtilService.queryMappingBySrcChannelId(mappings, channelId);
                if(mappingPO == null){
                    //协商端口
                    Long newPort = terminalMappingService.generatePort(mapping.get(access.getAddress()));

                    PortMappingPO portPO = new PortMappingPO();
                    portPO.setUpdateTime(new Date());
                    portPO.setSrcType(SrcType.ROLE);
                    portPO.setSrcBundleId(bundleId);
                    portPO.setSrcChannelId(channelId);
                    portPO.setDstType(DstType.REPEATER);
                    portPO.setDstRepeaterId(access.getRepeaterId());
                    portPO.setDstAccessId(access.getId());
                    portPO.setDstAddress(access.getAddress());
                    portPO.setDstPort(newPort);

                    mapping.get(access.getAddress()).add(newPort);
                    mappingPOs.add(portPO);
                }else{
                    mappingPOs.add(mappingPO);
                }
            }

            portMappingDao.save(mappingPOs);

            for(PortMappingPO mappingPO: mappingPOs){
                mappingIds.add(mappingPO.getId());
            }

            List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);

            for(PortMappingPO mappingPO: mappingPOs){

                if(main != null){
                    TaskPO mainTask = queryUtilService.queryTask(tasks, mappingPO.getId(), main.getIp());
                    if(mainTask == null || !mainTask.getStatus().equals(TaskStatus.zero.getStatus())){
                        taskExecuteService.taskCreatePost(main.getIp(), mappingPO, null, null);
                    }
                }
                if(backup != null){
                    TaskPO backupTask = queryUtilService.queryTask(tasks, mappingPO.getId(), backup.getIp());
                    if(backupTask == null || !backupTask.getStatus().equals(TaskStatus.zero.getStatus())){
                        taskExecuteService.taskCreatePost(backup.getIp(), mappingPO, null, null);
                    }
                }
            }

            System.out.println("二级节点任务创建成功，节点bundleId为：" + bundleId);
        }
    }
}
