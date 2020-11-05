package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 17:27
 */

import com.sumavision.signal.bvc.capacity.TransformModuleService;
import com.sumavision.signal.bvc.entity.dao.CapacityPermissionPortDAO;
import com.sumavision.signal.bvc.entity.po.CapacityPermissionPortPO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: CreateInputSource
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 17:27
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class DeleteAllBackupSources extends AbstractPassbyMsg {

    @Autowired
    CapacityPermissionPortDAO permissionDao;

    @Autowired
    TransformModuleService transformModuleService;

    @Override
    public synchronized void exec(PassbyBO passby) throws Exception { //todo 同时删能力服务更新数据库有问题，先加同步吧
        String bundleId = passby.getBundle_id();
        if (bundleId.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"cannot find bundle id in passby msg");
        }
        CapacityPermissionPortPO permission = permissionDao.findByBundleId(bundleId);
        if (permission==null){
            //todo 没有备源任务存在
        }else{
            String taskId = permission.getTaskId();
            transformModuleService.delDirectorTask(taskId);
            permissionDao.delete(permission);
        }
    }
}
