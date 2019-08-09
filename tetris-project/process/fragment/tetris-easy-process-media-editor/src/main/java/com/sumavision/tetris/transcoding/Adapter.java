package com.sumavision.tetris.transcoding;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.xml.XmlUtil;
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
	CompleteNotifyService completeNotifyService;

	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return TemplatesResponseVO xml解析后的数据结构
	 */
	public TemplatesResponseVO getTemplate(AddTaskVO getTemplates) {
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
	public AddTaskResponseVO addTask(AddTaskVO addTask) throws IOException {
		String questXmlString = XmlUtil.toEasyXml(addTask, AddTaskVO.class);

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
	public GetStatusResponseVO questStatus(AddTaskVO questStatus) {
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
	public String changeHttpToFtp(String httpUrl) throws IOException {
		URL url = this.getClass().getClassLoader().getResource("profile.json");

		File file = new File(url.getPath());

		String json = FileUtils.readFileToString(file);
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
	public String changeFtpToHttp(String ftpUrl){
		String httpPath = null;
		
		String port = "8085";
		
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
}