package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 16:59
 */

import com.alibaba.fastjson.JSONObject;
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
 * @ClassName: SwitchSource
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 16:59
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class SwitchSource extends AbstractPassbyMsg {


    @Autowired
    CapacityPermissionPortDAO capacityPermissionPortDAO;
    @Autowired
    private TransformModuleService transformModuleService;

    @Override
    public void exec(PassbyBO passbyBO) throws Exception {
        String bundleId = passbyBO.getBundle_id();
        CapacityPermissionPortPO permission = capacityPermissionPortDAO.findByBundleId(bundleId);
        if (permission == null) { //切换任务不存在
            throw new BaseException(StatusCode.ERROR,"cannot find task, id:"+bundleId);
        }

        JSONObject content = passbyBO.getPass_by_content();
        if (content.containsKey("source")){
            JSONObject sourceObj = content.getJSONObject("source");
            JSONObject video_source = sourceObj.getJSONObject("video_source");
            String sourceBundleId = video_source.getString("bundle_id");
            CapacityPermissionPortPO sourcePerm = capacityPermissionPortDAO.findByBundleId(sourceBundleId);
            if (sourcePerm == null) {
                throw new BaseException(StatusCode.ERROR,"cannot find task, id:"+bundleId);
            }
            //todo 需要拼接切换命令
            //传taskid,传inputId,能力服务去找对应的源去切
            transformModuleService.switchSource(permission.getTaskId(),sourcePerm.getBundleId());
        }else{
            throw new BaseException(StatusCode.ERROR, "cannot find source");
        }
    }
}
