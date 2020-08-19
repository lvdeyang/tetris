package com.sumavision.tetris.transcoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XmlUtil;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.transcoding.addTask.requestVO.AddTaskVO;
import com.sumavision.tetris.transcoding.addTask.rsponseVO.AddTaskResponseVO;
import com.sumavision.tetris.transcoding.completeNotify.CompleteNotifyService;
import com.sumavision.tetris.transcoding.completeNotify.VO.NotifyResponseVO;
import com.sumavision.tetris.transcoding.getStatus.VO.GetStatusResponseVO;
import com.sumavision.tetris.transcoding.getTemplates.VO.TemplatesResponseVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class Adapter {
	@Autowired
	private CompleteNotifyService completeNotifyService;
	
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;

	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return TemplatesResponseVO xml解析后的数据结构
	 */
	public TemplatesResponseVO getTemplate(AddTaskVO getTemplates) throws Exception{
		String questXmlString = XmlUtil.toEasyXml(getTemplates, AddTaskVO.class);

		String requestGetTemplates = HttpRequestUtil.httpXmlPost(RequestUrlType.GET_TEMPLETE_NAME_LIST_URL.getUrl(),
				questXmlString);

		TemplatesResponseVO templatesRespons = XmlUtil.XML2Obj(requestGetTemplates, TemplatesResponseVO.class);

		return templatesRespons;
	}

	/**
	 * 添加云转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return AddTaskResponseVO xml解析后的数据结构
	 */
	public AddTaskResponseVO addTask(AddTaskVO addTask) throws Exception {
		String questXmlString = XmlUtil.toEasyXml(addTask, AddTaskVO.class);
		
		System.out.println(questXmlString);

		String requestTranscoding = HttpRequestUtil.httpXmlPost(RequestUrlType.ADD_TASK_RUL.getUrl(), questXmlString);

		AddTaskResponseVO addTaskResponse = XmlUtil.XML2Obj(requestTranscoding, AddTaskResponseVO.class);

		return addTaskResponse;
	}

	/**
	 * 获取云转码任务状态<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return GetStatusResponseVO xml解析后的数据结构
	 */
	public GetStatusResponseVO questStatus(AddTaskVO questStatus) throws Exception {
		String questXmlString = XmlUtil.toEasyXml(questStatus, AddTaskVO.class);

		String requestGetStatus = HttpRequestUtil.httpXmlPost(RequestUrlType.GET_STATUS.getUrl(), questXmlString);

		GetStatusResponseVO getStatusResponse = XmlUtil.XML2Obj(requestGetStatus, GetStatusResponseVO.class);

		return getStatusResponse;
	}

	/**
	 * 云转码任务完成后回调<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return
	 */
	public void completeNotiry(String xmlString) throws Exception {
		NotifyResponseVO notifyResponse = XmlUtil.XML2Obj(xmlString, NotifyResponseVO.class);

		completeNotifyService.dealNotify(notifyResponse);
	}

	/**
	 * 创建云转码任务时生成唯一任务标识<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return String
	 */
	public String getTransactionId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String dateStringParse = sdf.format(date);
		String transactionId = dateStringParse + "-" + UUID.randomUUID().toString().substring(33);

		return transactionId;
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
		
		String ftpPath = null;

		String[] split = httpUrl.split(":");

		ServerProps serverProps = mimsServerPropsQuery.queryProps();
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
					  .toString();;
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
		
		String port = mimsServerPropsQuery.queryProps().getPort();
		
		String[] split;
		
		if (ftpUrl.contains("@")) {
			split = ftpUrl.split("@");
		}else {
			split = ftpUrl.split("//");
		}
		
		String ip = mimsServerPropsQuery.queryProps().getFtpIp();
		
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
			ServerProps serverProps = mimsServerPropsQuery.queryProps();
			return new StringBufferWrapper().append("ftp://").append(serverProps.getFtpUsername())
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
		}finally{
			if(in != null) in.close();
			if(reader != null) reader.close();
		}
		return json;
	}
}
