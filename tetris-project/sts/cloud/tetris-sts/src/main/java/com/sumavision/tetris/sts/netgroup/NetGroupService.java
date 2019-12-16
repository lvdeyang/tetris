package com.sumavision.tetris.sts.netgroup;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.ErrorCodes;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Poemafar on 2019/12/16 13:49
 */
@Service
public class NetGroupService {

    @Autowired
    NetGroupDao netGroupDao;


    public List<NetGroupPO> findAll(){
        return netGroupDao.findAll().stream().filter(ng->BooleanUtils.isNotTrue(ng.getBeDelete())).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = BaseException.class)
    public void save(NetGroupPO netGroupPO) throws BaseException {
        netGroupPO.setCreateTime(new Date());
        saveCheck(netGroupPO);
        netGroupDao.save(netGroupPO);
    }

    private void saveCheck(NetGroupPO netGroupPO) throws BaseException {
        if (netGroupPO.getId() != null) {
            if (!netGroupDao.findByNetNameAndIdNotAndBeDeleteFalse(netGroupPO.getNetName(), netGroupPO.getId()).isEmpty()) {
                throw new BaseException(StatusCode.FORBIDDEN, ErrorCodes.NAME_CONFLICT);
            }
        } else {
            if (netGroupDao.findTopByNetNameAndBeDeleteFalse(netGroupPO.getNetName()) != null){
                throw new BaseException(StatusCode.FORBIDDEN,ErrorCodes.NAME_CONFLICT);
            }
        }
    }

    public void delete(Long id) {
        NetGroupPO netGroupPO = netGroupDao.findOne(id);
        deleteCheck(netGroupPO);
        netGroupPO.setUpdateTime(new Date());
        netGroupPO.setBeDelete(true);
        netGroupDao.save(netGroupPO);
    }

    private void deleteCheck(NetGroupPO netGroupPO)   {

    }
}
