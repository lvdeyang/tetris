package com.suma.venus.message.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysConf {

	private static final Logger logger = LoggerFactory.getLogger(SysConf.class);

	private static String mqServerIp;

	private static int mqServerPort;

	private static String fileServer;

	private static String fileDownloadUrl;

	/**
	 * 是否使用消息服务
	 */
	private static Boolean beUseMq;

	public static void initSysConf() {
		try {
			Properties conf = new Properties();
			// ClassLoader.getSystemResourceAsStream 获取系统级的资源文件
			// TODO 以后依赖之后要注意路径是否正确
			conf.load(SysConf.class.getResourceAsStream("/properties/conf.properties"));

			mqServerIp = conf.getProperty("MQ.ServerIP");

			fileServer = conf.getProperty("MQ.FileFtpUrl");

			mqServerPort = Integer.valueOf(conf.getProperty("MQ.ServerPort")).intValue();

			fileDownloadUrl = conf.getProperty("fileDownloadUrl");

			beUseMq = Boolean.valueOf(conf.getProperty("MQ.BeUse"));

		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public static String getMqServerIp() {
		return mqServerIp;
	}

	public static int getMqServerPort() {
		return mqServerPort;
	}

	public static String getFileServer() {
		return fileServer;
	}

	public static String getFileDownloadUrl() {
		return fileDownloadUrl;
	}

	public static Boolean getBeUseMq() {
		return beUseMq;
	}

}
