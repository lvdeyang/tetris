package com.sumavision.tetris.sts.communication.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by Lost on 2017/1/23.
 */
public class XmlParse {

    static Logger logger = LogManager.getLogger(XmlParse.class);

    static Logger alarmLogger = LogManager.getLogger("Alarm");

    public static AbstractMsg parse(String xml , String socketAddress) {
        AbstractMsg msg = null;
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            if (root.attributeValue("cmd") != null && root.attributeValue("cmd").equals("salt"))
                return null;
            if (root.getName().equals(XmlManager.RESPONSE)){
                msg = parseResponse(root , socketAddress);
                logger.info("ack from {}. \n{}" , socketAddress , xml);
            } else if(root.getName().equals(XmlManager.RESPONSE_SET)){
                msg = parseResponseSet(root , socketAddress);
                logger.info("ack from {}. \n{}" , socketAddress , xml);
            } else {
                msg = parseAlarm(root , socketAddress);
                alarmLogger.warn("alarm from {}. \n{}" , socketAddress , xml);
            }
        } catch (DocumentException e) {
            logger.error("message from {} error. \n{}" , socketAddress , xml , e);
        } catch (NullPointerException e) {
            logger.error("message from {} discard. \n{}" , socketAddress , xml);
        }
        return msg;
    }

    private static AbstractMsg parseResponse(Element root , String socketAddress) {
        AckInfo ackInfo = new AckInfo();
        ackInfo.setId(Integer.parseInt(root.attributeValue("id")));
        ackInfo.setAck(Integer.parseInt(root.attributeValue("ack")));
        ackInfo.setType(root.attributeValue("cmd"));
        if(ackInfo.getAck() != 0){
            ackInfo.setErrMsg(root.elementText("body"));
        }
        ackInfo.setAckXml(root);
        MsgManager.ackInfoMap.get(ackInfo.getId()).offer(ackInfo);
        return ackInfo;
    }

    private static AbstractMsg parseResponseSet(Element root , String socketAddress) {
        AckInfo ackInfo = new AckInfo();
        ackInfo.setSocketAddress(socketAddress);
        ackInfo.setId(Integer.parseInt(root.attributeValue("id")));
        ackInfo.setAck(Integer.parseInt(root.attributeValue("ack")));
        ackInfo.setType(XmlManager.RESPONSE_SET);
        ackInfo.setAckXml(root);
        MsgManager.ackInfoMap.get(ackInfo.getId()).offer(ackInfo);
        return ackInfo;
    }

    private static AbstractMsg parseAlarm(Element root , String socketAddress) {
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.setSocketAddress(socketAddress);
        if (root.getName().equals(XmlManager.ALERT))
            alarmInfo.setType(XmlManager.ALERT);
        else if (root.getName().equals(XmlManager.RECOVER))
            alarmInfo.setType(XmlManager.RECOVER);
        alarmInfo.setId(XmlManager.attributeValueInt(root , "id"));
        alarmInfo.setAlarmCode(XmlManager.attributeValueInt(root , "type"));
        alarmInfo.setAlarmTime(root.attributeValue("time"));
        Element target = root.element("target");
        if (target.element("input") != null)
            alarmInfo.setInputId(XmlManager.attributeValueLong(target.element("input") , "id"));
        if (target.element("prog") != null)
            alarmInfo.setProgramId(XmlManager.attributeValueLong(target.element("prog") , "id"));
        if (target.element("program") != null)
            alarmInfo.setProgramId(XmlManager.attributeValueLong(target.element("program") , "id"));
        if (target.element("task") != null)
            alarmInfo.setTaskId(XmlManager.attributeValueLong(target.element("task") , "id"));
        if (target.element("output") != null)
            alarmInfo.setOutputId(XmlManager.attributeValueLong(target.element("output") , "id"));
        alarmInfo.setAlarmText(root.elementText("body").trim());
        return alarmInfo;
     //   MsgManager.alarmQueue.offer(alarmInfo);
    }

}
