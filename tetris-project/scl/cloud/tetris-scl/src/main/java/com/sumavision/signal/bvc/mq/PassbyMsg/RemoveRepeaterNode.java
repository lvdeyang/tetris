package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 17:20
 */

import com.sumavision.signal.bvc.entity.dao.PortMappingDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.signal.bvc.service.TaskExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RemoveRepeaterNode
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 17:20
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class RemoveRepeaterNode extends AbstractPassbyMsg {


    @Autowired
    private PortMappingDAO portMappingDao;

    @Autowired
    private TaskDAO taskDao;

    @Autowired
    private TaskExecuteService taskExecuteService;


    @Override
    public void exec(PassbyBO passby) throws Exception {
        String bundleId = passby.getBundle_id();

        List<PortMappingPO> mappings = portMappingDao.findBySrcTypeAndSrcBundleId(SrcType.ROLE, bundleId);

        List<Long> mappingIds = new ArrayList<Long>();
        for(PortMappingPO mapping: mappings){
            mappingIds.add(mapping.getId());
        }

        List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(mappingIds, TaskStatus.zero.getStatus());
        for(TaskPO task: tasks){
            taskExecuteService.taskDestory(task);
        }

        portMappingDao.deleteInBatch(mappings);

        System.out.println("二级节点任务销毁成功，节点bundleId为：" + bundleId);
    }
}
