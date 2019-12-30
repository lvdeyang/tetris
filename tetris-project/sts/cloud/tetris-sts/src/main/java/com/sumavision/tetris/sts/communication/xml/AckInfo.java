package com.sumavision.tetris.sts.communication.xml;

import org.dom4j.Element;

/**
 * Created by Lost on 2017/1/23.
 * response message.
 */
public class AckInfo extends AbstractMsg{

    private Integer ack;

    private String errMsg;

    private Element ackXml;

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Element getAckXml() {
        return ackXml;
    }

    public void setAckXml(Element ackXml) {
        this.ackXml = ackXml;
    }
}
