package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;
import java.util.List;

public class InputProgramBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4063921812392012809L;

	private Integer program_number;
	private String name;
	private String provider;
	private Integer pcr_pid;
	private Integer pmt_pid;
	private List<InputProgramVideoBO> video_array;   
	private List<InputProgramAudioBO> audio_array;   
	private List<InputProgramSubtitleBO> subtitle_array;
	private Integer result_code; 
	
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
	public Integer getPcr_pid() {
		return pcr_pid;
	}
	public void setPcr_pid(Integer pcr_pid) {
		this.pcr_pid = pcr_pid;
	}
	public Integer getPmt_pid() {
		return pmt_pid;
	}
	public void setPmt_pid(Integer pmt_pid) {
		this.pmt_pid = pmt_pid;
	}
	public Integer getResult_code() {
		return result_code;
	}
	public void setResult_code(Integer result_code) {
		this.result_code = result_code;
	}
	public List<InputProgramVideoBO> getVideo_array() {
		return video_array;
	}
	public void setVideo_array(List<InputProgramVideoBO> video_array) {
		this.video_array = video_array;
	}
	public List<InputProgramAudioBO> getAudio_array() {
		return audio_array;
	}
	public void setAudio_array(List<InputProgramAudioBO> audio_array) {
		this.audio_array = audio_array;
	}
	public List<InputProgramSubtitleBO> getSubtitle_array() {
		return subtitle_array;
	}
	public void setSubtitle_array(List<InputProgramSubtitleBO> subtitle_array) {
		this.subtitle_array = subtitle_array;
	}
}
