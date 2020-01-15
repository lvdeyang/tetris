package com.sumavision.tetris.sts.communication.httptool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.sts.common.CommonConstants.BondType;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lost on 2018/1/26.
 */
@Component
public class NetCardHttpUnit {
    @Autowired
    XToolHttpUnit xToolHttpUnit;

    @Autowired
    XToolMsgType xToolMsgType;
    
    //页面上刷新网卡信息使用
    private String netcardStatus;

    public List<NetCardInfoPO> getNetCardInfo(DeviceNodePO deviceNodePO) throws BaseException {
        List<NetCardInfoPO> netCardInfoPOS = new ArrayList<>();

        JSONObject jsonObject = xToolHttpUnit.getMsg(deviceNodePO.getDeviceIp() , XToolMsgType.GET_NETCARD_INFO);
        setNetcardStatus(jsonObject.toJSONString());

        JSONArray nets = jsonObject.getJSONArray("netcard");
        JSONArray routes = jsonObject.getJSONArray("route");

        for (int i = 0; i < nets.size(); i++) {
            NetCardInfoPO netCardInfoPO = parseNetCard(nets.getJSONObject(i) , deviceNodePO.getId());
            netCardInfoPO.setIndexNum(i + 1);
            JSONArray array = new JSONArray();
            for (int j = 0 ; j < routes.size() ; j++) {
                if (routes.getJSONObject(j).getString("dev").equals(netCardInfoPO.getName()))
                    array.add(routes.getJSONObject(j));
            }

            netCardInfoPO.setRoutes(array.toJSONString());
            netCardInfoPOS.add(netCardInfoPO);
        }

        return netCardInfoPOS;
    }

    private NetCardInfoPO parseNetCard(JSONObject jsonObject , Long deviceId) {
        NetCardInfoPO netCardInfoPO = new NetCardInfoPO();
        netCardInfoPO.setName(jsonObject.getString("name"));
        netCardInfoPO.setIpv4(jsonObject.getString("ipaddr"));
        netCardInfoPO.setIpv4Mask(jsonObject.getString("netmask"));
        netCardInfoPO.setMac(jsonObject.getString("mac"));
        netCardInfoPO.setEnable(jsonObject.getString("enable").equals("true") ?
                NetCardInfoPO.LINK_STATUS_NORMAL : NetCardInfoPO.LINK_STATUS_ABNORMAL);
        netCardInfoPO.setLinked(jsonObject.getString("linked").equals("true") ?
                NetCardInfoPO.LINK_STATUS_NORMAL : NetCardInfoPO.LINK_STATUS_ABNORMAL);

        if (netCardInfoPO.getEnable().equals(NetCardInfoPO.LINK_STATUS_NORMAL) &&
                netCardInfoPO.getLinked().equals(NetCardInfoPO.LINK_STATUS_NORMAL))
            netCardInfoPO.setStatus(NetCardInfoPO.LINK_STATUS_NORMAL);
        else
            netCardInfoPO.setStatus(NetCardInfoPO.LINK_STATUS_ABNORMAL);

        if (jsonObject.getString("master").equals("true"))
            netCardInfoPO.setBondType(BondType.MASTER);

        if (jsonObject.getString("slave").equals("true"))
            netCardInfoPO.setBondType(BondType.SLAVE);

        if (jsonObject.containsKey("vlan"))
            netCardInfoPO.setVlanName(jsonObject.getString("vlan"));

        if (jsonObject.containsKey("primary"))
            netCardInfoPO.setPrimaryName(jsonObject.getString("primary"));

        netCardInfoPO.setDeviceId(deviceId);

        return netCardInfoPO;
    }

    public void setNetCard(DeviceNodePO deviceNodePO, JSONObject jsonObject) throws BaseException {
        xToolHttpUnit.postMsg(deviceNodePO.getDeviceIp() , XToolMsgType.SET_NETCARD_INFO , jsonObject);
    }

    public void setNetCard(DeviceNodePO deviceNodePO, String type , JSONObject jsonObject) throws BaseException {
        xToolHttpUnit.postMsg(deviceNodePO.getDeviceIp() , type , jsonObject);
    }

    public boolean setCallBack(String deviceIp) throws BaseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", xToolMsgType.getCallBackUrl());
        jsonObject.put("time_out", 300);
        xToolHttpUnit.httpPost(XToolMsgType.getUrl(deviceIp , XToolMsgType.SET_NOTIFY) , jsonObject);

        return true;
    }

    public void cleanCallBack(String deviceIp) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "");
        jsonObject.put("time_out", 300);
        try {
            xToolHttpUnit.httpPost(XToolMsgType.getUrl(deviceIp , XToolMsgType.SET_NOTIFY) , jsonObject);
        } catch (Exception e) {

        }
    }

	public String getNetcardStatus() {
		return netcardStatus;
	}

	public void setNetcardStatus(String netcardStatus) {
		this.netcardStatus = netcardStatus;
	}
    
}
