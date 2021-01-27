package com.sumavision.tetris.device.netcard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.xtool.httptool.NetCardHttpUnit;
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
    DeviceDao deviceDao;

    @Autowired
    NetCardHttpUnit netCardHttpUnit;

    /**
     * 返回占用的netgroupid
     * @param nets
     * @return
     */
    public void setNetGroup(JSONArray nets) {
        for (int i = 0 ; i < nets.size() ; i++) {
            JSONObject net = nets.getJSONObject(i);
            Long id = net.getLong("id");
            NetCardInfoPO netCardInfoPO = netCardInfoDao.findById(id);
            if(net.containsKey("inputNetGroupId")) {
                Long inputNetGroupId = net.getLong("inputNetGroupId");
                netCardInfoPO.setInputNetGroupId(inputNetGroupId);
            }
            if (net.containsKey("outputNetGroupId")) {
                Long outputNetGroupId = net.getLong("outputNetGroupId");
                netCardInfoPO.setOutputNetGroupId(outputNetGroupId);
            }
            netCardInfoDao.save(netCardInfoPO);
        }
    }

    public List<NetCardInfoPO> getNetCardInfo(Long deviceId) throws BaseException {
        DevicePO devicePO = deviceDao.findById(deviceId);
        return getNetCardInfo(devicePO);
    }

    public List<NetCardInfoPO> getNetCardInfo(DevicePO devicePO) throws BaseException {
        List<NetCardInfoPO> old = netCardInfoDao.findByDeviceId(devicePO.getId());
        updateNet(old , netCardHttpUnit.getNetCardInfo(devicePO));
        return netCardInfoDao.findByDeviceIdOrderByCardNum(devicePO.getId());
    }

    public List<NetCardInfoPO> setNet(Long deviceId ,  JSONObject object) throws BaseException {
        DevicePO devicePO = deviceDao.findById(deviceId);
        netCardHttpUnit.setNetCard(devicePO, object);
        return getNetCardInfo(deviceId);
    }

    public List<NetCardInfoPO> setNet(Long deviceId , String type ,  JSONObject object) throws BaseException {
        DevicePO devicePO = deviceDao.findById(deviceId);
        netCardHttpUnit.setNetCard(devicePO, type , object);
        return getNetCardInfo(deviceId);
    }

    public boolean updateNet(List<NetCardInfoPO> old , List<NetCardInfoPO> now) throws BaseException {
        boolean flag = true;
        List<NetCardInfoPO> delList = new ArrayList<>();
        for (NetCardInfoPO o_ : old) {
            NetCardInfoPO n_ = getSameNet(o_ , now);
            if (n_ == null) {
                if (o_.getInputNetGroupId() != null || o_.getOutputNetGroupId() !=null) {
                    flag = false;
                    throw new BaseException(StatusCode.ERROR,"已配分组的网卡信息丢失");
                }else {
                    delList.add(o_);
                    netCardInfoDao.delete(o_);
                }
            } else {
            	if (o_.getBeCtrl().equals(Boolean.TRUE)){
            		o_.setVirtualIpv4(n_.getVirtualIpv4());
                    continue;//控制口不更新
                }else{
                	o_.setCardNum(n_.getCardNum());
                    o_.setIpv4(n_.getIpv4());
                    o_.setIpv4Mask(n_.getIpv4Mask());
                    o_.setStatus(n_.getStatus());
                    o_.setLinked(n_.getLinked());
                    o_.setEnable(n_.getEnable());
                    o_.setVlanName(n_.getVlanName());
                    o_.setBondType(n_.getBondType());
                    o_.setPrimaryName(n_.getPrimaryName());
                    o_.setRoutes(n_.getRoutes());
                    o_.setVirtualIpv4(n_.getVirtualIpv4());
                }
            }
        }
        old.removeAll(delList);
        for (NetCardInfoPO n_ : now) {
            NetCardInfoPO t_ = getSameNet(n_ , old);
            if (t_ == null)
                old.add(n_);
        }
        netCardInfoDao.saveAll(old);
        return flag;
    }

    private NetCardInfoPO getSameNet(NetCardInfoPO one , List<NetCardInfoPO> netCardInfoPOS) {
        for (NetCardInfoPO n : netCardInfoPOS) {
            if (n.getName().equals(one.getName()) && n.getMac().equals(one.getMac()))
                return n;
        }
        return null;
    }

    public void setCallBack(DevicePO devicePO) throws  BaseException {
        netCardHttpUnit.setCallBack(devicePO.getDeviceIp());
    }

    public void setNtpClient(String deviceIp,String serverIp) throws  BaseException {
        JSONObject bodyObj = new JSONObject();
        bodyObj.put("server_ip",serverIp);
        netCardHttpUnit.setNtpClient(deviceIp,bodyObj);
    }

    public boolean setCallBack(String deviceIp) {
        try {
            return netCardHttpUnit.setCallBack(deviceIp);
        } catch (Exception e) {
            return false;
        }
    }

    public void cleanCallBack(DevicePO devicePO) {
        map.remove(devicePO.getId());
        netCardHttpUnit.cleanCallBack(devicePO.getDeviceIp());
    }
    
    /**
     * 重置设备相关的网卡所属分组，用于reset设备时调用
     * @param deviceId
     */
    public void resetNetCardNetGroup(Long deviceId){
    	List<NetCardInfoPO> netCardList = netCardInfoDao.findByDeviceId(deviceId);
    	netCardList.forEach(netCard -> netCard.setInputNetGroupId(null));
    	netCardList.forEach(netCard -> netCard.setOutputNetGroupId(null));
    	netCardInfoDao.saveAll(netCardList);
    }

    private ConcurrentHashMap<Long , String> map = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, String> getMap() {
        return map;
    }

    public Long getInNetGroupIdByNetCardIp(String netCardIp) throws BaseException {
        List<NetCardInfoPO> netCardInfoPOS = netCardInfoDao.findByIpv4(netCardIp);
        if(netCardInfoPOS.isEmpty()){
            throw new BaseException(StatusCode.ERROR,MessageFormat.format("cannot found netGroupId by this netCardIp: {0}",netCardIp));
        } else if (netCardInfoPOS.size()!=1){
            throw new BaseException(StatusCode.ERROR,"inputNetCardIp has more than one in xManager!");
        }else{
            return netCardInfoPOS.get(0).getInputNetGroupId();
        }
    }

    public Long getOutNetGroupIdByNetCardIp(String netCardIp) throws BaseException {
        List<NetCardInfoPO> netCardInfoPOS = netCardInfoDao.findByIpv4(netCardIp);
        if(netCardInfoPOS.isEmpty()){
            throw new BaseException(StatusCode.ERROR,MessageFormat.format("cannot found netGroupId by this netCardIp: {0}",netCardIp));
        } else if (netCardInfoPOS.size()!=1){
            throw new BaseException(StatusCode.ERROR,"outputNetCardIp has more than one in xManager!");
        }else{
            return netCardInfoPOS.get(0).getOutputNetGroupId();
        }
    }

    public NetCardInfoDao getNetCardInfoDao(){
        return this.netCardInfoDao;
    }
    
    public String getVipsByDeviceIdAndNetGroupId(Long deviceId, Long netGroupId){
    	String srcIPList = "";
    	NetCardInfoPO po = netCardInfoDao.findTopByDeviceIdAndOutputNetGroupId(deviceId, netGroupId);
    	if(po.getIpv4() != null && !po.getIpv4().equals("")){
    		srcIPList ="[{\"netmask\":\""+ po.getIpv4Mask() +"\",\"name\":\""+ po.getName() +"\",\"ipaddr\":\""+ po.getIpv4() +"\"}";
    	}else if((po.getIpv4() == null || po.getIpv4().equals("")) && po.getIpv6() != null && !po.getIpv6().equals("")) {
    		srcIPList ="[{\"netmask\":\""+ po.getIpv6Mask() +"\",\"name\":\""+ po.getName() +"\",\"ipaddr\":\""+ po.getIpv6() +"\"}";
    	}
    	if(po.getVirtualIpv4() == null || po.getVirtualIpv4().equals("[]")){
			srcIPList += "]";
		}else{
			srcIPList += "," + po.getVirtualIpv4().substring(1);
		}
    	return srcIPList;
    }

}
