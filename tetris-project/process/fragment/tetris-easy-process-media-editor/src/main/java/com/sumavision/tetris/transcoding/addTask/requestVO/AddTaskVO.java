package com.sumavision.tetris.transcoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.JSONObject;

@XmlRootElement(name = "TCSTranscodeCmd.Req")
public class AddTaskVO extends JSONObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MsgHeaderVO msgHeader;
	private TranscodeJobsVO transcodeJobs;

	public MsgHeaderVO getMsgHeader() {
		return msgHeader;
	}

	@XmlElement(name = "MsgHeader")
	public void setMsgHeader(MsgHeaderVO msgHeader) {
		this.msgHeader = msgHeader;
	}

	public TranscodeJobsVO getTranscodeJobs() {
		return transcodeJobs;
	}

	@XmlElement(name = "TranscodeJobs")
	public void setTranscodeJobs(TranscodeJobsVO transcodeJobs) {
		this.transcodeJobs = transcodeJobs;
	}
}
