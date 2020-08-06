package com.sumavision.tetris.mims.config.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class MimsServerPropsQuery {

	@Autowired
	private ServerProps serverProps;
	
	/**
	 * 查询媒资服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:36
	 * @return ServerProps 服务属性
	 */
	public ServerProps queryProps() throws Exception{
		String json = readProfile();
		JSONObject jsonObject = JSONObject.parseObject(json);

		String ftpIp = jsonObject.getString("ftpIp");
		String ftpPort = jsonObject.getString("ftpPort");
		String ftpUserName = jsonObject.getString("ftpUsername");
		String ftpPassword = jsonObject.getString("ftpPassword");
		
		String omcftpIp = jsonObject.getString("omcftpIp");
		String omcftpPort = jsonObject.getString("omcftpPort");
		String omcftpUserName = jsonObject.getString("omcftpUsername");
		String omcftpPassword = jsonObject.getString("omcftpPassword");
		
		serverProps.setFtpIp(ftpIp.isEmpty() ? serverProps.getIp() : ftpIp);
		serverProps.setFtpPort(ftpPort.isEmpty() ? "21" : ftpPort);
		serverProps.setFtpUsername(ftpUserName);
		serverProps.setFtpPassword(ftpPassword);
		
		serverProps.setOmcftpIp(omcftpIp);
		serverProps.setOmcftpPort(omcftpPort);
		serverProps.setOmcftpUsername(omcftpUserName);
		serverProps.setOmcftpPassword(omcftpPassword);
		
		return serverProps;
	}

	/**
	 * 生成资源http访问全路径<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月22日 下午5:42:19
	 * @param String primeryPath 资源路径
	 * @return String 资源访问http地址
	 * @throws Exception 
	 */
	public String generateHttpPreviewUrl(String primeryPath) throws Exception{
		return new StringBufferWrapper().append("http://").append(queryProps().getFtpIp())
														  .append(":")
														  .append(serverProps.getPort())
														  .append("/")
														  .append(primeryPath)
														  .toString();
	}
	
	public String generateFtpPreviewUrl(String primeryPath) throws Exception{
		ServerProps serverProps = queryProps();
		return new StringBufferWrapper().append("ftp://").append(serverProps.getFtpUsername())
				  .append(":")
				  .append(serverProps.getFtpPassword())
				  .append("@")
				  .append(serverProps.getFtpIp())
				  .append(":")
				  .append(serverProps.getFtpPort())
				  .append("/")
				  .append(primeryPath)
				  .toString();
	}
	
	/**
	 * 获取profile.json配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午2:26:47
	 * @return String json
	 */
	public String readProfile() throws Exception{
		BufferedReader in = null;
		InputStreamReader reader = null;
		String json = null;
		try{
			in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("profile.json")));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null){
			    buffer.append(line);
			}
			json = buffer.toString();
		} finally{
			if(in != null) in.close();
			if(reader != null) reader.close();
		}
		return json;
	}
	
}
