package com.suma.venus.message.bo;

/**
 * Venus消息头
 * 
 * @author lxw
 */
public class VenusMessageHead {

	protected String message_type;

	protected String message_name;

	protected String source_id;

	protected String destination_id;

	protected String source_selector_id;

	protected String sequence_id;

	public enum MsgType {
		request, response, notification, alert, passby
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getMessage_name() {
		return message_name;
	}

	public void setMessage_name(String message_name) {
		this.message_name = message_name;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getDestination_id() {
		return destination_id;
	}

	public void setDestination_id(String destination_id) {
		this.destination_id = destination_id;
	}

	public String getSequence_id() {
		return sequence_id;
	}

	public void setSequence_id(String sequence_id) {
		this.sequence_id = sequence_id;
	}

	public String getSource_selector_id() {
		return source_selector_id;
	}

	public void setSource_selector_id(String source_selector_id) {
		this.source_selector_id = source_selector_id;
	}

}
