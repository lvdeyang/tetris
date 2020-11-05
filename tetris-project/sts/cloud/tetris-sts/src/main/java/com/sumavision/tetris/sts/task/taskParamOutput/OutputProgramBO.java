package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;


public class OutputProgramBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8659010691677723657L;
	private Integer program_number;
	private String name;
	private String provider;
	private String character_set;
	private Integer pmt_pid;
	private Integer pcr_pid;
	private ArrayList<OutputMediaBO> media_array;
	
	public Integer getProgram_number() {
		return program_number;
	}
	public void setProgram_number(Integer program_number) {
		this.program_number = program_number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getCharacter_set() {
		return character_set;
	}
	public void setCharacter_set(String character_set) {
		this.character_set = character_set;
	}
	public Integer getPmt_pid() {
		return pmt_pid;
	}
	public void setPmt_pid(Integer pmt_pid) {
		this.pmt_pid = pmt_pid;
	}
	public Integer getPcr_pid() {
		return pcr_pid;
	}
	public void setPcr_pid(Integer pcr_pid) {
		this.pcr_pid = pcr_pid;
	}
	public ArrayList<OutputMediaBO> getMedia_array() {
		return media_array;
	}
	public void setMedia_array(ArrayList<OutputMediaBO> media_array) {
		this.media_array = media_array;
	}  

}
