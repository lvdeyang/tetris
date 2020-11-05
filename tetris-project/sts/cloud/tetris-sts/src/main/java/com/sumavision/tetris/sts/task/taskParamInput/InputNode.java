package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;
import java.util.ArrayList;

public class InputNode implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1090029821990552991L;
	private String id;
	private ArrayList<InputProgramBO> program_array;
	private InputCommon inputCommon;
	private InputNodeNormalMap normal_map;
	private InputNodeMediaTypeOnceMap media_type_once_map;
	
	private Integer result_code;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<InputProgramBO> getProgram_array() {
		return program_array;
	}
	public void setProgram_array(ArrayList<InputProgramBO> program_array) {
		this.program_array = program_array;
	}
	public InputCommon getInputCommon() {
		return inputCommon;
	}
	public void setInputCommon(InputCommon inputCommon) {
		this.inputCommon = inputCommon;
	}
	public Integer getResult_code() {
		return result_code;
	}
	public void setResult_code(Integer result_code) {
		this.result_code = result_code;
	}
	public InputNodeNormalMap getNormal_map() {
		return normal_map;
	}
	public void setNormal_map(InputNodeNormalMap normal_map) {
		this.normal_map = normal_map;
	}
	public InputNodeMediaTypeOnceMap getMedia_type_once_map() {
		return media_type_once_map;
	}
	public void setMedia_type_once_map(InputNodeMediaTypeOnceMap media_type_once_map) {
		this.media_type_once_map = media_type_once_map;
	}
	
	public static Object[] getAllIp(String ipfrom, String ipto) {
	    ArrayList<String> ips = new ArrayList<String>();
	    String[] ipfromd = ipfrom.split("\\.");
	    String[] iptod = ipto.split("\\.");
	    int[] int_ipf = new int[4];
	    int[] int_ipt = new int[4];
	    for (int i = 0; i < 4; i++) {
	        int_ipf[i] = Integer.parseInt(ipfromd[i]);
	        int_ipt[i] = Integer.parseInt(iptod[i]);
	    }
	    for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
	        for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1]
	                : 255); B++) {
	            for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2]
	                    : 255); C++) {
	                for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3]
	                        : 255); D++) {
	                    ips.add(new String(A + "." + B + "." + C + "." + D));
	                }
	            }
	        }
	    }
	    return ips.toArray();
	}
	
}
