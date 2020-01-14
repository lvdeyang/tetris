package com.suma.venus.message.mq;

import com.suma.venus.message.bo.VenusMessageHead.MsgType;

/**
 * @author admin 消息返回数据结构
 */
public class ResponseBO {

	/**
	 * 消息处理结果
	 */
	protected int result;

	/**
	 * 消息返回的内容
	 */
	protected Object resp;

	private VenusMessage message = new VenusMessage();

	public ResponseBO() {
		this.getMessage().getMessage_header().setMessage_type(MsgType.response.toString());
	}

	public ResponseBO(String dstID) {
		this.getMessage().getMessage_header().setMessage_type(MsgType.response.toString());
		this.getMessage().getMessage_header().setDestination_id(dstID);
	}

	public ResponseBO(int result, String dstID, String requestID) {
		this.result = result;
		this.getMessage().getMessage_header().setMessage_type(MsgType.response.toString());
		this.getMessage().getMessage_header().setDestination_id(dstID);
		this.getMessage().getMessage_header().setSequence_id(requestID);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Object getResp() {
		return resp;
	}

	public void setResp(Object resp) {
		this.resp = resp;
	}

	public VenusMessage getMessage() {
		return message;
	}

	public void setMessage(VenusMessage message) {
		this.message = message;
	}

}
