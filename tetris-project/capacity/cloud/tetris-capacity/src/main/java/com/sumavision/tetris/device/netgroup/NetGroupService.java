package com.sumavision.tetris.device.netgroup;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Poemafar on 2019/12/16 13:49
 */
@Service
public class NetGroupService {

    @Autowired
    NetGroupDao netGroupDao;

    @Autowired
    NetCardInfoDao netCardInfoDao;


    public List<NetGroupPO> findAll() throws BaseException {
        List<NetGroupPO> all = netGroupDao.findAll();
        if (all==null || all.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"未找到网卡分组");
        }
        return all;
    }

    public NetGroupPO findOne(Long id) throws BaseException {
        NetGroupPO netGroupPO= netGroupDao.findOne(id);
        if (netGroupPO == null) {
            throw new BaseException(StatusCode.FORBIDDEN, "不存在");
        }
        return netGroupPO;
    }

    @Transactional(rollbackFor = BaseException.class)
    public void save(NetGroupPO netGroupPO) throws BaseException {
        netGroupPO.setUpdateTime(new Date());
        saveCheck(netGroupPO);
        netGroupDao.save(netGroupPO);
    }

    private void saveCheck(NetGroupPO netGroupPO) throws BaseException {
        if (netGroupPO.getId() != null) {
            if (!netGroupDao.findByNetNameAndIdNot(netGroupPO.getNetName(), netGroupPO.getId()).isEmpty()) {
                throw new BaseException(StatusCode.FORBIDDEN,"名称不能重复");
            }
        } else {
            if (netGroupDao.findTopByNetName(netGroupPO.getNetName()) != null){
                throw new BaseException(StatusCode.FORBIDDEN,"名称不能重复");
            }
        }
    }

    public void delete(Long id) throws BaseException {
        NetGroupPO netGroupPO = netGroupDao.findOne(id);
        netGroupPO.setUpdateTime(new Date());
        netGroupDao.delete(netGroupPO);
    }



    /**
     * 判断网卡分组是否在使用
     * @param id
     * @return
     */
    public Boolean beUsedByNetGroupId(Long id){
        Integer inUseNum =  netCardInfoDao.countDistinctByInputNetGroupId(id);
        Integer outUseNum =  netCardInfoDao.countDistinctByOutputNetGroupId(id);
        if (inUseNum > 0 || outUseNum>0){
            return true;
        }
        return false;
    }
}
