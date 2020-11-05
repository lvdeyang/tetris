package com.sumavision.tetris.sts.exeception;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.sts.common.ConstantUtil;


@Component
public class ExceptionI18Message {
	static Logger logger = LogManager.getLogger(ExceptionI18Message.class);
	
	@Autowired
	ConstantUtil constantUtil;
	
	public String getLocaleMessage(String key){
		Properties props = new Properties();
		InputStream in;
		try {
			if (constantUtil.getLanguage().equals("c")) {
				in= new BufferedInputStream(new FileInputStream(constantUtil.getPathRoot()+ "/WEB-INF/classes/i18n/errorCode_zh_CN.properties"));
				//in= new BufferedInputStream(new FileInputStream("D:/java/Tomcat7/webapps/web-app/WEB-INF/classes/i18n/errorCode_zh_CN.properties"));
			}else {
				in= new BufferedInputStream(new FileInputStream(constantUtil.getPathRoot()+ "/WEB-INF/classes/i18n/errorCode_en_US.properties"));
				//in= new BufferedInputStream(new FileInputStream("D:/java/Tomcat7/webapps/web-app/WEB-INF/classes/i18n/errorCode_en_US.properties"));
			}
			props.load(new InputStreamReader(in, "UTF-8"));
			in.close();
			if(null==props.getProperty(key)){
				return key;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return key;
		}
		
	    return props.getProperty(key);
	}
}

