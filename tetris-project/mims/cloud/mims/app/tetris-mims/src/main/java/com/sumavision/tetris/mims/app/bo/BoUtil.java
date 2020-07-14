package com.sumavision.tetris.mims.app.bo;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.boolean_expr_return;

public class BoUtil {
	
	public static boolean uploadFile(String filePath,String dst,String ftpServer,
			int ftpPort,String userName,String password){
		FtpUtil ftpUtil=new FtpUtil(ftpServer, ftpPort, userName, password);
		String[] filePaths=filePath.split("/");
		ftpUtil.uploadFile(dst, filePaths[filePaths.length-1], filePath);
		return true;
	}
	
	public static boolean injectBo(AdiBo bo) throws Exception{
		// TODO Auto-generated method stub
        //1.创建SAXReader对象用于读取xml文件
        SAXReader reader = new SAXReader();
        //2.读取xml文件，获得Document对象
        Document doc = reader.read(ResourceUtils.getFile("classpath:adi.xml"));
        //3.获取根元素
        Element root = doc.getRootElement();
        //4.获取根元素下的所有子元素（通过迭代器）
        Iterator<Element> it = root.elementIterator();
        while(it.hasNext()){
            
            Element e = it.next();
            
            if(e.getName().equals("OpenGroupAsset")){
            	Element openElement = e.element("VODRelease");
            	openElement.setAttributeValue("assetID", bo.getMediaId());
            }else if(!e.getName().equals("AcceptContentAsset")){
            	e.setAttributeValue("groupAssetID", bo.getMediaId());
            	List<Element> innerElements = e.elements();
            	for (Element innerElement : innerElements) {
					innerElement.setAttributeValue("assetID",bo.getMediaId());
					if(innerElement.getName().equals("Title")){
						Element subinele=innerElement.element("TitleFull");
						subinele.setText(bo.getFileName());
						innerElement.element("programType").setText(bo.getFormat());
					}
				}

            }

        }
        System.out.println(doc.asXML());
		return true;
	}

	
	
}
