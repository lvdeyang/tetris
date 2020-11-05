package com.sumavision.tetris.transcoding.getTemplates.VO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.transcoding.addTask.requestVO.MsgHeaderVO;

@XmlRootElement(name = "TCSTranscodeCmd.Resp")
public class TemplatesResponseVO extends JSONObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MsgHeaderVO msgHeader;
	private TranscodeJobsVO transcodeJobs;

	@XmlElement(name = "MsgHeader")
	public MsgHeaderVO getMsgHeader() {
		return msgHeader;
	}

	public void setMsgHeader(MsgHeaderVO msgHeader) {
		this.msgHeader = msgHeader;
	}

	@XmlElement(name = "TranscodeJobs")
	public TranscodeJobsVO getTranscodeJobs() {
		return transcodeJobs;
	}

	public void setTranscodeJobs(TranscodeJobsVO transcodeJobs) {
		this.transcodeJobs = transcodeJobs;
	}
}
