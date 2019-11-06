package com.sumavision.tetris.streamTranscoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.xml.XmlUtil;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.streamTranscoding.addTask.requestVO.MessageVO;
import com.sumavision.tetris.streamTranscoding.addTask.responseVO.ResponseVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamTranscodingAdapter {
	
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;
	
	public ResponseVO addTask(MessageVO messageVO) throws Exception{
		String questXmlString = XmlUtil.toEasyXml(messageVO, MessageVO.class);
		
		System.out.println(questXmlString);
		
		String responseTranscoding = HttpRequestUtil.httpXmlPost(ToolRequestType.ADD_TASK.getUrl(), questXmlString);
		
		ResponseVO responseJSON = XmlUtil.XML2Obj(responseTranscoding, ResponseVO.class);
		
		return responseJSON;
	}
	
	public com.sumavision.tetris.streamTranscoding.deleteTask.responseVO.ResponseVO deleteTask(com.sumavision.tetris.streamTranscoding.deleteTask.requestVO.MessageVO messageVO) throws Exception{
		String questXmlString = XmlUtil.toEasyXml(messageVO, com.sumavision.tetris.streamTranscoding.deleteTask.requestVO.MessageVO.class);
		
		System.out.println(questXmlString);
		
		String responseTranscoding = HttpRequestUtil.httpXmlPost(ToolRequestType.DELETE_TASK.getUrl(), questXmlString);
		
		com.sumavision.tetris.streamTranscoding.deleteTask.responseVO.ResponseVO responseJSON = XmlUtil.XML2Obj(responseTranscoding, com.sumavision.tetris.streamTranscoding.deleteTask.responseVO.ResponseVO.class);
		
		return responseJSON;
	}
	
	public ResponseVO addOutput(com.sumavision.tetris.streamTranscoding.addOutput.requestVO.MessageVO messageVO) throws Exception{
		String questXmlString = XmlUtil.toEasyXml(messageVO, com.sumavision.tetris.streamTranscoding.addOutput.requestVO.MessageVO.class);
		
		System.out.println(questXmlString);
		
		String responseTranscoding = HttpRequestUtil.httpXmlPost(ToolRequestType.ADD_OUTPUT.getUrl(), questXmlString);
		
		ResponseVO responseJSON = XmlUtil.XML2Obj(responseTranscoding, ResponseVO.class);
		
		return responseJSON;
	}
	
	public com.sumavision.tetris.streamTranscoding.deleteOutput.responseVO.ResponseVO deleteOutput(com.sumavision.tetris.streamTranscoding.deleteOutput.requestVO.MessageVO messageVO) throws Exception{
		String questXmlString = XmlUtil.toEasyXml(messageVO, com.sumavision.tetris.streamTranscoding.deleteOutput.requestVO.MessageVO.class);
		
		System.out.println(questXmlString);
		
		String responseTranscoding = HttpRequestUtil.httpXmlPost(ToolRequestType.DELETE_OUTPUT.getUrl(), questXmlString);
		
		com.sumavision.tetris.streamTranscoding.deleteOutput.responseVO.ResponseVO responseJSON = XmlUtil.XML2Obj(responseTranscoding, com.sumavision.tetris.streamTranscoding.deleteOutput.responseVO.ResponseVO.class);
		
		return responseJSON;
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
		
		String json = readProfile();
		JSONObject jsonObject = JSONObject.parseObject(json);

		String ftpUserName = jsonObject.getString("ftpUserName");
		String ftpPassword = jsonObject.getString("ftpPassword");

		String ftpPath = null;

		String[] split = httpUrl.split(":");

		//没有端口的Url地址
		if (split.length == 2) {
			String[] splite2 = split[1].split("//");
			
			String ip = splite2[1].substring(0, splite2[1].indexOf("/"));
			
			String path = splite2[1].substring(splite2[1].indexOf("/"));
			
			ftpPath = new StringBuilder("ftp://").append(ftpUserName).append(":").append(ftpPassword).append("@")
					.append(ip).append(path).toString();
		//有端口的url地址
		} else if (split.length == 3) {
			String ip = split[1].split("//")[1];

			String path = split[2].substring(split[2].indexOf("/"));

			ftpPath = new StringBuilder("ftp://").append(ftpUserName).append(":").append(ftpPassword).append("@")
					.append(ip).append(path).toString();
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
		
		String port = mimsServerPropsQuery.queryProps().getPort();
		
		String[] split;
		
		if (ftpUrl.contains("@")) {
			split = ftpUrl.split("@");
			
		}else {
			split = ftpUrl.split("//");
		}
		
		String ip = split[1].substring(0, split[1].indexOf("/"));
		
		String path = split[1].substring(split[1].indexOf("/"));
		
		httpPath = new StringBuilder("http://").append(ip).append(":").append(port).append(path).toString();
		
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
			String json = readProfile();
			JSONObject jsonObject = JSONObject.parseObject(json);

			String ftpUserName = jsonObject.getString("ftpUserName");
			String ftpPassword = jsonObject.getString("ftpPassword");

			return new StringBuilder("ftp://").append(ftpUserName).append(":").append(ftpPassword).append("@")
					.append(mimsServerPropsQuery.queryProps().getIp()).append("/").append(url).toString();
		}else {
			return url;
		}
	}
	
	/**
	 * 获取配置文件内小工具信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月18日 上午9:55:51
	 * @return String ip 小工具ip
	 * @return String port 小工具请求port
	 * @return int updStartPort 小工具接收流端口
	 */
	public Map<String, Object> getToolInfo() throws Exception{
		String json = readProfile();
		JSONObject jsonObject = JSONObject.parseObject(json);
		
		return new HashMapWrapper<String, Object>().put("ip", jsonObject.getString("toolIp"))
				.put("port", jsonObject.getString("toolPort"))
				.getMap();
	}
	
	/**
	 * 获取配置文件内收录服务器信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月18日 上午9:55:51
	 * @return String ip 小工具ip
	 * @return String startport 文件转流起始端口(收录起始端口为 +2000)
	 */
	public Map<String, String> getRecordInfo() throws Exception{
		String json = readProfile();
		JSONObject jsonObject = JSONObject.parseObject(json);
		
		return new HashMapWrapper<String, String>()
				.put("ip", jsonObject.getString("recordIp"))
				.put("port", jsonObject.getString("recordPort"))
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
}
