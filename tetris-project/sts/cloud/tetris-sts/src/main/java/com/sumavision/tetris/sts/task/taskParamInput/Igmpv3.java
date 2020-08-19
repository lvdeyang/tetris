package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;
import java.util.ArrayList;


public class Igmpv3 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -833120696002033750L;
	private String mode;
	private ArrayList<Igmpv3Ip> ip_array;
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public ArrayList<Igmpv3Ip> getIp_array() {
		return ip_array;
	}
	public void setIp_array(ArrayList<Igmpv3Ip> ip_array) {
		this.ip_array = ip_array;
	}
}
