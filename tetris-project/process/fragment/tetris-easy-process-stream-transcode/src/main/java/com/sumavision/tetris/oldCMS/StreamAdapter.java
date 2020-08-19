package com.sumavision.tetris.oldCMS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBuilderWrapper;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamAdapter {
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;
	
	public JSONObject addTask(JSONObject requestJSON) throws Exception {
		System.out.println(requestJSON.toJSONString());
		
		JSONObject response = HttpRequestUtil.httpFormPost(OldCMSRequestType.ADD_TASK.getUrl(), jsonToGetString(requestJSON));
		
		return response;
	}
	
	public JSONObject deleteTask(JSONObject requestJSON) throws Exception {
		System.out.println(requestJSON.toJSONString());
		
		JSONObject responsJSON = HttpRequestUtil.httpFormPost(OldCMSRequestType.DELETE_TASK.getUrl(), jsonToGetString(requestJSON));
		
		return responsJSON;
	}
	
	public JSONObject addOutput(JSONObject requestJSON) throws Exception {
		System.out.println(requestJSON.toJSONString());
		
		JSONObject response = HttpRequestUtil.httpFormPost(OldCMSRequestType.ADD_OUTPUT.getUrl(), jsonToGetString(requestJSON));
		
		return response;
	}
	
	public JSONObject deleteOutput(JSONObject requestJSON) throws Exception {
		System.out.println(requestJSON.toJSONString());
		
		JSONObject response = HttpRequestUtil.httpFormPost(OldCMSRequestType.DELETE_OUTPUT.getUrl(), jsonToGetString(requestJSON));
		
		return response;
	}

	/**
	 * http请求地址转ftp<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return String
	 */
	public String changeHttpToFtp(String httpUrl) throws Exception {
		
		if (httpUrl.endsWith(".m3u8")) return httpUrl;
		
		ServerProps serverProps = mimsServerPropsQuery.queryProps();

		String ftpPath = null;

		String[] split = httpUrl.split(":");

		//没有端口的Url地址
		if (split.length == 2) {
			String[] splite2 = split[1].split("//");
			
			String path = splite2[1].substring(splite2[1].indexOf("/"));
			
			ftpPath = new StringBufferWrapper().append("ftp://").append(serverProps.getFtpUsername())
					  .append(":")
					  .append(serverProps.getFtpPassword())
					  .append("@")
					  .append(serverProps.getFtpIp())
					  .append(":")
					  .append(serverProps.getFtpPort())
					  .append(path)
					  .toString();
		//有端口的url地址
		} else if (split.length == 3) {
			String path = split[2].substring(split[2].indexOf("/"));

			ftpPath = new StringBufferWrapper().append("ftp://").append(serverProps.getFtpUsername())
					  .append(":")
					  .append(serverProps.getFtpPassword())
					  .append("@")
					  .append(serverProps.getFtpIp())
					  .append(":")
					  .append(serverProps.getFtpPort())
					  .append(path)
					  .toString();
		}

		return ftpPath;
	}
	
	/**
	 * ftp请求地址转http<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return String
	 */
	public String changeFtpToHttp(String ftpUrl) throws Exception{
		String httpPath = null;
		
		String[] split;
		
		if (ftpUrl.contains("@")) {
			split = ftpUrl.split("@");
			
		}else {
			split = ftpUrl.split("//");
		}
		
		String path = split[1].substring(split[1].indexOf("/"));
		
		ServerProps serverProps = mimsServerPropsQuery.queryProps();
		httpPath = new StringBuilder("http://").append(serverProps.getIp()).append(":").append(serverProps.getPort()).append(path).toString();
		
		return httpPath;
	}
	
	/**
	 * 获取url中不带协议的ftp<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月13日 上午11:57:19
	 * @param url
	 */
	public String addTreatyToUrl(String url) throws Exception{
		String[] split = url.split(":");
		
		if (split.length == 1) {
			ServerProps serverProps = mimsServerPropsQuery.queryProps();

			return new StringBuilder("ftp://")
					.append(serverProps.getFtpUsername())
					.append(":")
					.append(serverProps.getFtpPassword())
					.append("@")
					.append(serverProps.getFtpIp())
					.append(":")
					.append(serverProps.getFtpPort())
					.append("/")
					.append(url)
					.toString();
		}else {
			return url;
		}
	}
	
	public Map<String, String> getOldCMSInfo() throws Exception{
		String json = readProfile();
		JSONObject jsonObject = JSONObject.parseObject(json);
		
		return new HashMapWrapper<String, String>().put("ip", jsonObject.getString("cmsIp"))
				.put("port", jsonObject.getString("cmsPort"))
				.put("startPort", jsonObject.getString("udpStartPort"))
				.getMap();
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
			in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("streamTranscodingProfile.json")));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null){
			    buffer.append(line);
			}
			json = buffer.toString();
		}finally{
			if(in != null) in.close();
			if(reader != null) reader.close();
		}
		return json;
	}
	
	/**
	 * 根据json获取用&拼接的字符串<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午3:48:06
	 * @param params
	 * @return
	 */
	public String jsonToGetString(JSONObject params) {
		if (params == null) return "";
		StringBuilderWrapper builder = new StringBuilderWrapper();
		boolean isFirst = true;
		for (String key : params.keySet()) {
			if (key != null && params.get(key) != null) {
				if (!isFirst) {
					builder.append("&");
				} else {
					isFirst = false;
				}
				builder.append(key).append("=").append(params.get(key));
			}
		}
		return builder.toString();
	}
}
