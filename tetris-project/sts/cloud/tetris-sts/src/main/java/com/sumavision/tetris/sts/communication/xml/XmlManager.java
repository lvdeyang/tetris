package com.sumavision.tetris.sts.communication.xml;


import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.ErrorCodes;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Lost on 2017/1/23.
 */
public class XmlManager {

    final static String MSG = "message";
    final static String MSG_SET = "message-set";
    final static String RESPONSE = "response";
    final static String RESPONSE_SET = "response-set";
    public final static String ALERT = "alert";
    public final static String RECOVER = "recover";

    private static Element newXml(){
        Element element = DocumentHelper.createElement(MSG);
        element.addElement("target");
        element.addElement("body");
        return element;
    }

    public static XmlElement getXml(String type){
        return new XmlElement(type).generateXml();
    }

    public static Element generateXml(XmlElement xmlElement){
        Element element = newXml();
        element.addAttribute("id" , xmlElement.getId().toString());
        element.addAttribute("cmd" , xmlElement.getType());
        return element;
    }

    public static Element generateXml(MsgInfo msgInfo){
        Element element = DocumentHelper.createElement(MSG_SET);
        element.addAttribute("id" , msgInfo.getId().toString());
        for (XmlElement xmlElement : msgInfo.getXmlElements()) {
            element.add(xmlElement.getSendXml());
        }
        return element;
    }

    public static <T> T parseXml(String xml , Class<T> type) throws BaseException {
        StringReader reader = new StringReader(xml);
        try {
            JAXBContext context = JAXBContext.newInstance(type);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {

            throw new BaseException(StatusCode.ERROR,ErrorCodes.RESPONSE_ERROR);
        } finally {
            reader.close();
        }
    }

    public static <T> String generateXml(T obj , Class<T> type) throws BaseException {
        try {
            JAXBContext context = JAXBContext.newInstance(type);
            final Marshaller marshaller = context.createMarshaller();
            final StringWriter stringWriter = new StringWriter();
            marshaller.marshal(obj, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            throw new BaseException(StatusCode.ERROR,ErrorCodes.RESPONSE_ERROR);
        }
    }

    public static Integer attributeValueInt(Element element , String name) {
        String str = element.attributeValue(name);
        return str == null || str.isEmpty() || str.equals("null") ? null : Integer.parseInt(str);
    }

    public static Integer elementTextInt(Element element , String name) {
        String str = element.elementText(name);
        return str == null || str.isEmpty() || str.equals("null") ? null : Integer.parseInt(str);
    }

    public static Long attributeValueLong(Element element , String name) {
        String str = element.attributeValue(name);
        return str == null || str.isEmpty() || str.equals("null") ? null : Long.parseLong(str);
    }

    public static Long elementTextLong(Element element , String name) {
        String str = element.elementText(name);
        return str == null || str.isEmpty() || str.equals("null") ? null : Long.parseLong(str);
    }
}
