package com.sumavision.tetris.sts.communication.xml;

import com.sumavision.tetris.sts.common.IdConstructor;
import org.dom4j.Element;

/**
 * Created by Lost on 2017/2/13.
 */
public class XmlElement {

    private Integer id;

    private String type;

    private Element sendXml;

    public XmlElement() {
        this.id = IdConstructor.getMsgId();
    }

    public XmlElement(String type) {
        this.id = IdConstructor.getMsgId();
        this.type = type;
    }

    public XmlElement generateXml(){
        this.sendXml = XmlManager.generateXml(this);
        return this;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Element getSendXml() {
        return sendXml;
    }

    public void setSendXml(Element sendXml) {
        this.sendXml = sendXml;
    }
}
