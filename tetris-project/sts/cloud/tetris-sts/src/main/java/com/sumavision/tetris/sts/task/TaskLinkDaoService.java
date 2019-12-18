package com.sumavision.tetris.sts.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.sts.device.DeviceGroupDao;
import com.sumavision.tetris.sts.device.DeviceNodeDao;
import com.sumavision.tetris.sts.task.source.SourceDao;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkDao;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO.TaskLinkStatus;

/**
 * 涉及查询与更新的业务
 *
 * @author gaofeng
 */
@Service
public class TaskLinkDaoService {

    static Logger logger = LogManager.getLogger(TaskLinkDaoService.class);

    @Autowired
    TaskLinkDao taskLinkDao;

    @Autowired
    DeviceGroupDao deviceGroupDao;

    @Autowired
    SourceDao sourceDao;

    

    public Page<TaskLinkPO> getTasklinkByKeyWords(String keyWord, List<Long> groupIdList, List<Long> deviceGroupList, List<Long> deviceNodeList, int pageNum, int pageSize, Boolean alarmFlag, TaskLinkStatus taskLinkStatus) {
        if (deviceGroupList.isEmpty()) {
            return null;// 没有设备组，就没设备，没设备就没任务
        }
        if (deviceNodeList.size() > 1) {
            //不用过滤节点查询
            if (alarmFlag == null || taskLinkStatus == null) {
                return taskLinkDao.findByKeyword(keyWord, groupIdList, deviceNodeList, new PageRequest(pageNum, pageSize), deviceGroupList);
            }
            return taskLinkDao.findByKeyword(keyWord, groupIdList, deviceGroupList, deviceNodeList, alarmFlag, taskLinkStatus, new PageRequest(pageNum, pageSize));
        } else if (deviceNodeList.size() == 1) {
            return taskLinkDao.findByKeyword(keyWord, groupIdList, deviceNodeList.get(0), new PageRequest(pageNum, pageSize), deviceGroupList);
        }
        return null;
    }

   

}
