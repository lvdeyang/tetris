package com.suma.venus.message.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterStatus {

	private static final Logger logger = LoggerFactory.getLogger(RegisterStatus.class);

	private static Properties conf;

	private static String filePath;

	public static void init() {
		try {
			conf = new Properties();
			filePath = RegisterStatus.class.getResource("/").toString().substring(6)
					+ "properties/registerStatus.properties";
			String os = System.getProperty("os.name");
			if (!os.toLowerCase().startsWith("win")) {
				filePath = "/" + filePath;
			}
			logger.info(">=>>>>>>>>>>>>>>>>registerStatus.properties path = " + filePath);
			// filePath = "D:/TTTTT/registerStatus.properties";
			// ClassLoader.getSystemResourceAsStream 获取系统级的资源文件
			// TODO 以后依赖之后要注意路径是否正确
			if (new File(filePath).exists()) {
				conf.load(new FileInputStream(filePath));
			} else {
				conf.load(RegisterStatus.class.getResourceAsStream("/properties/registerStatus.properties"));
				updateConfFile();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	private static void updateConfFile() throws FileNotFoundException, IOException {
		FileOutputStream oFile = new FileOutputStream(filePath, false);
		conf.store(oFile, "" + new Date());
		oFile.close();
	}

	public static boolean isRegistered() {
		String isReg = conf.getProperty("isRegistered");
		return Boolean.parseBoolean(isReg);
	}

	public static void setRegistered(boolean isReg) {
		conf.replace("isRegistered", String.valueOf(isReg));
		try {
			updateConfFile();
		} catch (Exception e) {
			logger.info("store registerStatus.properties error: " + e);
		}
	}

	public static void setNodeId(String nodeId) {
		conf.replace("nodeId", nodeId);
		try {
			updateConfFile();
		} catch (Exception e) {
			logger.info("store registerStatus.properties error: " + e);
		}
	}

	public static String getNodeId() {
		return conf.getProperty("nodeId");
	}

	public static String getSelectorId() {
		return conf.getProperty("nodeId") + "-" + conf.getProperty("localIp");
	}
}
