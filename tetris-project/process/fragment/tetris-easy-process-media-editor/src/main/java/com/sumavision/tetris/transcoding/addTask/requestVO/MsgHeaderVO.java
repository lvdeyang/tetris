package com.sumavision.tetris.transcoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlElement;

public class MsgHeaderVO {
	private String transactionId;
	private String cmdType;

	public String getTransactionId() {
		return transactionId;
	}

	@XmlElement(name = "TransactionId")
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCmdType() {
		return cmdType;
	}

	@XmlElement(name = "CmdType")
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}
}
