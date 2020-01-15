package com.sumavision.tetris.sts.device.netcard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.ErrorCodes;
import com.sumavision.tetris.sts.communication.httptool.NetCardHttpUnit;
import com.sumavision.tetris.sts.device.node.DeviceNodeDao;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Lost on 2018/1/24.
 */
@Service
public class NetCardService {

    @Autowired
    NetCardInfoDao netCardInfoDao;

    @Autowired
    DeviceNodeDao deviceNodeDao;

    @Autowired
    NetCardHttpUnit netCardHttpUnit;

    public void setNetGroup(JSONArray nets) {
        for (int i = 0 ; i < nets.size() ; i++) {
            JSONObject net = nets.getJSONObject(i);
            Long id = net.getLong("id");
            Long netGroupId = net.getLong("netGroupId");
            NetCardInfoPO netCardInfoPO = netCardInfoDao.findOne(id);
            netCardInfoPO.setNetGroupId(netGroupId);
            netCardInfoDao.save(netCardInfoPO);
        }
    }

    public List<NetCardInfoPO> getNetCardInfo(Long deviceId) throws BaseException {
        DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceId);
        return getNetCardInfo(deviceNodePO);
    }

    public List<NetCardInfoPO> getNetCardInfo(DeviceNodePO deviceNodePO) throws BaseException {
        List<NetCardInfoPO> old = netCardInfoDao.findByDeviceId(deviceNodePO.getId());
        updateNet(old , netCardHttpUnit.getNetCardInfo(deviceNodePO));
        return netCardInfoDao.findByDeviceIdOrderByIndexNum(deviceNodePO.getId());
    }

    public List<NetCardInfoPO> setNet(Long deviceId ,  JSONObject object) throws BaseException {
        DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceId);
        netCardHttpUnit.setNetCard(deviceNodePO, object);
        return getNetCardInfo(deviceId);
    }

    public List<NetCardInfoPO> setNet(Long deviceId , String type ,  JSONObject object) throws BaseException {
        DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceId);
        netCardHttpUnit.setNetCard(deviceNodePO, type , object);
        return getNetCardInfo(deviceId);
    }

    public boolean updateNet(List<NetCardInfoPO> old , List<NetCardInfoPO> now) {
        boolean flag = true;
        List<NetCardInfoPO> delList = new ArrayList<>();
        for (NetCardInfoPO o_ : old) {
            NetCardInfoPO n_ = getSameNet(o_ , now);
            if (n_ == null) {
                if (o_.getNetGroupId() != null)
                    flag = false;
                delList.add(o_);
//                old.remove(o_);
                netCardInfoDao.delete(o_);
            } else {
                o_.setIndexNum(n_.getIndexNum());
                o_.setIpv4(n_.getIpv4());
                o_.setIpv4Mask(n_.getIpv4Mask());
                o_.setStatus(n_.getStatus());
                o_.setLinked(n_.getLinked());
                o_.setEnable(n_.getEnable());
                o_.setVlanName(n_.getVlanName());
                o_.setBondType(n_.getBondType());
                o_.setPrimaryName(n_.getPrimaryName());
                o_.setRoutes(n_.getRoutes());
            }
        }
        old.removeAll(delList);
        for (NetCardInfoPO n_ : now) {
            NetCardInfoPO t_ = getSameNet(n_ , old);
            if (t_ == null)
                old.add(n_);
        }
        netCardInfoDao.save(old);
        return flag;
    }

    private NetCardInfoPO getSameNet(NetCardInfoPO one , List<NetCardInfoPO> netCardInfoPOS) {
        for (NetCardInfoPO n : netCardInfoPOS) {
            if (n.getName().equals(one.getName()) && n.getMac().equals(one.getMac()))
                return n;
        }
        return null;
    }

    public void setCallBack(DeviceNodePO deviceNodePO) throws BaseException {
        netCardHttpUnit.setCallBack(deviceNodePO.getDeviceIp());
    }

    public boolean setCallBack(String deviceIp) {
        try {
            return netCardHttpUnit.setCallBack(deviceIp);
        } catch (Exception e) {
            return false;
        }
    }

    public void cleanCallBack(DeviceNodePO deviceNodePO) {
        map.remove(deviceNodePO.getId());
        netCardHttpUnit.cleanCallBack(deviceNodePO.getDeviceIp());
    }
    
    /**
     * 重置设备相关的网卡所属分组，用于reset设备时调用
     * @param deviceId
     */
    public void resetNetCardNetGroup(Long deviceId){
    	List<NetCardInfoPO> netCardList = netCardInfoDao.findByDeviceId(deviceId);
    	netCardList.forEach(netCard -> netCard.setNetGroupId(null));
    	netCardInfoDao.save(netCardList);
    }

    private ConcurrentHashMap<Long , String> map = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, String> getMap() {
        return map;
    }

    public Long getNetGroupIdByNetCardIp(String netCardIp) throws BaseException {
        List<NetCardInfoPO> netCardInfoPOS = netCardInfoDao.findByIpv4(netCardIp);
        if(netCardInfoPOS.isEmpty()){
            throw new BaseException(StatusCode.ERROR,MessageFormat.format("cannot found netGroupId by this netCardIp: {0}",netCardIp));
        } else if (netCardInfoPOS.size()!=1){
            throw new BaseException(StatusCode.ERROR,"inputNetCardIp has more than one in xManager!");
        }else{
            return netCardInfoPOS.get(0).getNetGroupId();
        }
    }

}
