package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;

public class InputSdiBO implements InputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7717323156169506038L;

	private String card_type;
	private Integer card_no;
	private Integer card_port;
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public Integer getCard_no() {
		return card_no;
	}
	public void setCard_no(Integer card_no) {
		this.card_no = card_no;
	}
	public Integer getCard_port() {
		return card_port;
	}
	public void setCard_port(Integer card_port) {
		this.card_port = card_port;
	}
	
}
