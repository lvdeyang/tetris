package com.sumavision.tetris.transcoding.getTemplates;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.xml.XmlUtil;
import com.sumavision.tetris.transcoding.Adapter;
import com.sumavision.tetris.transcoding.RequestCmdType;
import com.sumavision.tetris.transcoding.addTask.requestVO.AddTaskVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.MsgHeaderVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TranscodeJobsVO;
import com.sumavision.tetris.transcoding.getTemplates.VO.TemplatesResponseVO;

@Component
public class TemplatesRequest {
	@Autowired
	Adapter adapter;
	
	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<String> 模板名称列表
	 */
	public List<String> getTemplates(){
		MsgHeaderVO msgHeader = new MsgHeaderVO();
		msgHeader.setTransactionId(adapter.getTransactionId());
		msgHeader.setCmdType(RequestCmdType.GET_TEMPLATE_NAME_LIST.getTypeName());
		
		TranscodeJobsVO transcodeJobs = new TranscodeJobsVO();
		
		AddTaskVO addTask = new AddTaskVO();
		
		addTask.setMsgHeader(msgHeader);
		addTask.setTranscodeJobs(transcodeJobs);
		
		TemplatesResponseVO response = adapter.getTemplate(addTask);
		
		return response.getTranscodeJobs().getTemplateNames();
	}
}
