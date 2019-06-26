package com.sumavision.tetris.transcoding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public TemplatesResponseVO getTemplate(AddTaskVO getTemplates) {
		String questXmlString = XmlUtil.toEasyXml(getTemplates, AddTaskVO.class);

		String requestGetTemplates = HttpRequestUtil.httpXmlPost(RequestUrlType.GET_TEMPLETE_NAME_LIST_URL.getUrl(),
				questXmlString);

		TemplatesResponseVO templatesRespons = XmlUtil.XML2Obj(requestGetTemplates, TemplatesResponseVO.class);

		return templatesRespons;
	}

	public AddTaskResponseVO addTask(AddTaskVO addTask) {
		String questXmlString = XmlUtil.toEasyXml(addTask, AddTaskVO.class);

		String requestTranscoding = HttpRequestUtil.httpXmlPost(RequestUrlType.ADD_TASK_RUL.getUrl(), questXmlString);

		AddTaskResponseVO addTaskResponse = XmlUtil.XML2Obj(requestTranscoding, AddTaskResponseVO.class);

		return addTaskResponse;
	}

	public GetStatusResponseVO questStatus(AddTaskVO questStatus) {
		String questXmlString = XmlUtil.toEasyXml(questStatus, AddTaskVO.class);

		String requestGetStatus = HttpRequestUtil.httpXmlPost(RequestUrlType.GET_STATUS.getUrl(), questXmlString);

		GetStatusResponseVO getStatusResponse = XmlUtil.XML2Obj(requestGetStatus, GetStatusResponseVO.class);
		
		return getStatusResponse;
	}

	public void completeNotiry(String xmlString) throws Exception {
		NotifyResponseVO notifyResponse = XmlUtil.XML2Obj(xmlString, NotifyResponseVO.class);

		completeNotifyService.dealNotify(notifyResponse);
	}

	public String getTransactionId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String dateStringParse = sdf.format(date);
		String transactionId = dateStringParse + "-" + UUID.randomUUID().toString().substring(33);

		return transactionId;
	}
}
