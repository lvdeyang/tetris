package com.sumavision.tetris.record.api.process;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.record.RecordService;
import com.sumavision.tetris.streamTranscodingProcessVO.RecordVO;

@Controller
@RequestMapping(value = "/api/process/record")
public class ApiProcessRecordController {
	
	@Autowired
	private RecordService recordService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addRecord(String record_recordInfo, String record_messageId, HttpServletRequest request) throws Exception{
		RecordVO recordVO = JSON.parseObject(record_recordInfo, RecordVO.class);
		if (recordVO.isRecord()) {
			String url = new StringBufferWrapper().append("udp://")
					.append(recordVO.getAssetIp())
					.append(":")
					.append(recordVO.getAssetPort())
					.toString();
			
			recordService.add(record_messageId == null ? null : Long.parseLong(record_messageId), url);
		}
		
		return new HashMapWrapper<String, Object>().put("id", record_messageId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object deleteRecord(Long messageId, Long recordId, HttpServletRequest request) throws Exception{
		recordService.delete(messageId, recordId);
		
		return null;
	}
}
