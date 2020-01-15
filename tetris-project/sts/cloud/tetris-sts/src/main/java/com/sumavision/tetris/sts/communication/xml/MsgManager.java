package com.sumavision.tetris.sts.communication.xml;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Lost on 2017/1/24.
 */
public class MsgManager {

    public static final ConcurrentMap<Integer , LinkedBlockingQueue<AckInfo>> ackInfoMap = new ConcurrentHashMap<>();

    public static final LinkedBlockingQueue<AlarmInfo> alarmQueue = new LinkedBlockingQueue<>();

    public static MsgInfo getMsg(){
        return new MsgInfo();
    }

    public static MsgInfo getMsg(String type){
        return new MsgInfo(XmlManager.getXml(type)).generateXml();
    }

    public static MsgInfo getMsg(String socketAddress, String type){
        return new MsgInfo(socketAddress.split(":")[0],
                Integer.parseInt(socketAddress.split(":")[1]), XmlManager.getXml(type)).generateXml();
    }

    public static MsgInfo getMsg(String ip, Integer port, String type){
        return new MsgInfo(ip, port, XmlManager.getXml(type)).generateXml();
    }

    public static MsgInfo getMsg(XmlElement xmlElement){
        return new MsgInfo(xmlElement).generateXml();
    }

    public static MsgInfo getMsg(List<XmlElement> xmlElements){
        return new MsgInfo(xmlElements).generateXml();
    }

    public static MsgInfo getMsg(String socketAddress, XmlElement xmlElement){
        return new MsgInfo(socketAddress.split(":")[0], Integer.parseInt(socketAddress.split(":")[1]), xmlElement).generateXml();
    }

    public static MsgInfo getMsg(String socketAddress , List<XmlElement> xmlElements){
        return new MsgInfo(socketAddress.split(":")[0], Integer.parseInt(socketAddress.split(":")[1]), xmlElements).generateXml();
    }


}
