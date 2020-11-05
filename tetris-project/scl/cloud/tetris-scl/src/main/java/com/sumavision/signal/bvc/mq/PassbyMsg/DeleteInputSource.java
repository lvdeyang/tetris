package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 17:27
 */

import com.sumavision.signal.bvc.entity.dao.CapacityPermissionPortDAO;
import com.sumavision.signal.bvc.entity.po.CapacityPermissionPortPO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: CreateInputSource
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 17:27
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class DeleteInputSource extends AbstractPassbyMsg {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteInputSource.class);

    @Autowired
    CapacityPermissionPortDAO capacityPermissionPortDAO;

    @Override
    public void exec(PassbyBO passby) throws Exception {

        String bundleId = passby.getBundle_id();
        CapacityPermissionPortPO permission = capacityPermissionPortDAO.findByBundleId(bundleId);
        if (permission == null ){
            LOGGER.warn("the bundle not exist, id:{}",passby.getBundle_id());
//            throw new BaseException(StatusCode.FORBIDDEN,"bundle not exist, id:"+passby.getBundle_id());
        }else{
            capacityPermissionPortDAO.delete(permission);
        }
    }
}
