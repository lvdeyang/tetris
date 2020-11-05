package com.suma.venus.resource.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLBeanUtils {
	
    public static String beanToXml(Class clazz,Object bean){
        StringWriter sw = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
            //该值默认为false,true则不会创建即头信息,即<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            jaxbMarshaller.marshal(bean, sw);
            sw.append("\n");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }
    
    public static <T> T xmlToBean(String xmlPath, Class<T> load) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xmlPath.toLowerCase()));
    }
    
    public static void main(String[] args){
    	try {
//    		System.out.println("ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes("utf-8").length);
//    		
//    		System.out.println("abcdefghijklmnopqrstuvwxyz".getBytes("utf-8").length);
//    		
//    		System.out.println("0123456789".getBytes("utf-8").length);
//    		
//    		System.out.println("<>".getBytes("utf-8").length);
//    		
//    		System.out.println(3/5);
//    		
//    		System.out.println(3%5);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
    	
    	/**
    	List<String> nlist = new ArrayList<String>();
    	nlist.add("aaaaaa");
    	nlist.add("bbbbbb");
    	NotifyRouteLinkXml routelinkXml = new NotifyRouteLinkXml(4, "raw", "11,11", nlist);
    	
    	String xmlRouteLink = XMLBeanUtils.beanToXml(NotifyRouteLinkXml.class, routelinkXml);
    	
    	NotifyUserDeviceXML notifyXML = new NotifyUserDeviceXML();
    	List<UserStatusXML> userlist = new ArrayList<UserStatusXML>();
    	List<DeviceStatusXML> devlist = new ArrayList<DeviceStatusXML>();
    	notifyXML.setUserlist(userlist);
    	notifyXML.setDevlist(devlist);
    	userlist.add(new UserStatusXML("12345678901", 1, LdapContants.DEFAULT_NODE_UUID, "12345678901"));
//    	devlist.add(new DeviceStatusXML("98765432101", 1, 2, LdapContants.DEFAULT_NODE_UUID));
    	
    	System.out.println(XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, notifyXML));
    	
    	String xml = "<notify><commandname>syncinfo</commandname><devlist><devitem><devid>98765432101</devid><devtype>2</devtype><status>1</status><visitednodeid>f9ec9048025840e4a427ab8dc8475652</visitednodeid></devitem></devlist><seq>1de2aea109ce440d9158d9170675dd7c</seq><ts>1558775291815</ts><userlist><useritem><binddevid>12345678901</binddevid><status>1</status><userid>12345678901</userid><visitednodeid>f9ec9048025840e4a427ab8dc8475652</visitednodeid></useritem></userlist></notify>";
    	try {
    		NotifyUserDeviceXML xmlResourceBean = XMLBeanUtils.xmlToBean(xmlRouteLink, NotifyUserDeviceXML.class);
    		
			NotifyRouteLinkXml xmlRouteLinkBean = XMLBeanUtils.xmlToBean(xml, NotifyRouteLinkXml.class);
			System.out.println("debug");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	**/
    }
}
