package com.sumavision.bvc.meeting.logic.record.mims;

import java.io.IOException;
import java.util.Properties;

public class MimsProperties {
	
	private String useMims;
	
	private String appId;
	
	private String appSecret;
	
	private String url;
	
	public static MimsProperties getProperties(){
		
		Properties prop = new Properties();
		try {
			prop.load(MimsProperties.class.getClassLoader().getResourceAsStream("mims.properties"));
			
			MimsProperties mims = new MimsProperties();
			mims.setUseMims(prop.getProperty("mims.isUsed"));
			mims.setAppId(prop.getProperty("mims.appId"));
			mims.setAppSecret(prop.getProperty("mims.appSecret"));
			mims.setUrl(prop.getProperty("mims.url"));
			return mims;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getUseMims() {
		return useMims;
	}

	public void setUseMims(String useMims) {
		this.useMims = useMims;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
