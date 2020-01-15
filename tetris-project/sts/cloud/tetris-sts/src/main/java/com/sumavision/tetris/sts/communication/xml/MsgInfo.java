package com.sumavision.tetris.sts.communication.xml;

import com.sumavision.tetris.sts.common.IdConstructor;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lost on 2017/1/23.
 */
public class MsgInfo extends AbstractMsg{

    private Element sendXml;

    private List<XmlElement> xmlElements;

    public MsgInfo(){

    }

    public MsgInfo(XmlElement xmlElement) {
        this.xmlElements = new ArrayList<XmlElement>();
        this.xmlElements.add(xmlElement);
    }

    public MsgInfo(List<XmlElement> xmlElements) {
        this.xmlElements = xmlElements;
    }

    public MsgInfo(String ip , Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public MsgInfo(String ip , Integer port , XmlElement xmlElement) {
        this.ip = ip;
        this.port = port;
        this.xmlElements = new ArrayList<XmlElement>();
        this.xmlElements.add(xmlElement);
    }

    public MsgInfo(String ip , Integer port , List<XmlElement> xmlElements) {
        this.ip = ip;
        this.port = port;
        this.xmlElements = xmlElements;
    }

    public MsgInfo generateXml(){
        if (xmlElements.size() == 1){
            this.id = xmlElements.get(0).getId();
            this.type = xmlElements.get(0).getType();
            this.sendXml = xmlElements.get(0).getSendXml();
        } else if (xmlElements.size() > 1) {
            this.id = IdConstructor.getMsgId();
            this.type = XmlManager.MSG_SET;
            this.sendXml = XmlManager.generateXml(this);
        }
        return this;
    }

    public Element getSendXml() {
        return sendXml;
    }

    public void setSendXml(Element sendXml) {
        this.sendXml = sendXml;
    }

    public List<XmlElement> getXmlElements() {
        return xmlElements;
    }

    public void setXmlElements(List<XmlElement> xmlElements) {
        this.xmlElements = xmlElements;
    }
}
