package com.sumavision.tetris.sts.communication.xml;

import com.suma.xianrd.communication.message.CmdType;

import com.sumavision.tetris.sts.common.CommonConstants.*;
import com.sumavision.tetris.sts.task.source.SourcePO;
import org.dom4j.Element;

/**
 * Created by Lost on 2017/2/23.
 */
public class CommonXmlCreate {

    public static XmlElement generateNTP(String ip) {
        XmlElement xmlElement = XmlManager.getXml("ntp-service-start");
        xmlElement.getSendXml().addAttribute("ip" , ip);
        return xmlElement;
    }


    static Element streamElement(Element parent , String name) {
        return streamElement(parent , "MAP" , name , null);
    }

    static Element streamElement(Element parent , String type , String name) {
        return streamElement(parent , type , name , null);
    }

    static Element streamElement(Element parent , String type , String name , int value) {
        return streamElement(parent , type , name , String.valueOf(value));
    }

    static Element streamElement(Element parent , String type , String name , boolean value) {
        return streamElement(parent , type , name , String.valueOf(value));
    }

    static Element streamElement(Element parent , String type , String name , String text) {
        Element element = parent.addElement(type);
        element.addAttribute("name" , name);
        if (null != text)
            element.addText(text);
        return element;
    }
}
