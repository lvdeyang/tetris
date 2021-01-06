package com.sumavision.tetris.device.xtool.httptool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.enumeration.BondType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
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

    public List<NetCardInfoPO> getNetCardInfo(DevicePO DevicePO) throws BaseException {
        List<NetCardInfoPO> netCardInfoPOS = new ArrayList<>();

        JSONObject jsonObject = xToolHttpUnit.getMsg(DevicePO.getDeviceIp() , XToolMsgType.GET_NETCARD_INFO);
        System.out.println(jsonObject);
        setNetcardStatus(jsonObject.toJSONString());

        JSONArray nets = jsonObject.getJSONArray("netcard");
        JSONArray routes = jsonObject.getJSONArray("route");

        for (int i = 0; i < nets.size(); i++) {
        	if(nets.getJSONObject(i).getString("name").contains(":")){
        		continue;
        	}
            NetCardInfoPO netCardInfoPO = parseNetCard(nets.getJSONObject(i) , DevicePO.getId());
            netCardInfoPO.setCardNum(i + 1);
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
        netCardInfoPO.setEnable(jsonObject.getString("enable").equals("true") ? 1 : 0);
        netCardInfoPO.setLinked(jsonObject.getString("linked").equals("true") ? 1 : 0);

        if (netCardInfoPO.getEnable().equals(1) &&
                netCardInfoPO.getLinked().equals(1))
            netCardInfoPO.setStatus(1);
        else
            netCardInfoPO.setStatus(0);

        if (jsonObject.getString("master").equals("true"))
            netCardInfoPO.setBondType(BondType.MASTER);

        if (jsonObject.getString("slave").equals("true"))
            netCardInfoPO.setBondType(BondType.SLAVE);

        if (jsonObject.containsKey("vlan"))
            netCardInfoPO.setVlanName(jsonObject.getString("vlan"));

        if (jsonObject.containsKey("primary"))
            netCardInfoPO.setPrimaryName(jsonObject.getString("primary"));
        
        if (jsonObject.containsKey("vips"))
            netCardInfoPO.setVirtualIpv4(jsonObject.getString("vips"));

        netCardInfoPO.setDeviceId(deviceId);

        return netCardInfoPO;
    }

    public void setNetCard(DevicePO DevicePO, JSONObject jsonObject) throws BaseException {
        xToolHttpUnit.postMsg(DevicePO.getDeviceIp() , XToolMsgType.SET_NETCARD_INFO , jsonObject);
    }

    public void setNetCard(DevicePO DevicePO, String type , JSONObject jsonObject) throws BaseException {
        xToolHttpUnit.postMsg(DevicePO.getDeviceIp() , type , jsonObject);
    }


    public void setNtpClient(String deviceIp, JSONObject jsonObject) throws BaseException {
        xToolHttpUnit.postMsg(deviceIp , xToolMsgType.SET_NTP_CLIENT , jsonObject);
    }

    public boolean setCallBack(String deviceIp) throws BaseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", xToolMsgType.getCallBackUrl());
        jsonObject.put("time_out", 300);
        try {
            xToolHttpUnit.httpPost(XToolMsgType.getUrl(deviceIp , XToolMsgType.SET_NOTIFY) , jsonObject);
        } catch (BaseException e) {
            throw new BaseException(StatusCode.ERROR,"fail to set notify param to xstreamtool in device: "+deviceIp);
        }
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
