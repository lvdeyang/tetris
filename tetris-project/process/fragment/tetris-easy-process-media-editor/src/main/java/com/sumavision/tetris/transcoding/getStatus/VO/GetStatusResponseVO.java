package com.sumavision.tetris.transcoding.getStatus.VO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sumavision.tetris.transcoding.addTask.requestVO.MsgHeaderVO;

@XmlRootElement(name = "TCSTranscodeCmd.Resp")
public class GetStatusResponseVO {
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
