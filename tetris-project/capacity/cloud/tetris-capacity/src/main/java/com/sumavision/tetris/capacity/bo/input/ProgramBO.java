package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 节目参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月28日 下午3:20:54
 */
public class ProgramBO {

	//新加节目映射参数
	/** 不做节目映射，由集群指定节目参数 */
	private JSONObject normal_map;

	/** 各种类型媒体最多一个，靠媒体类型自动映射 */
	private JSONObject media_type_once_map;

	/** 输入切换时 */
	private JSONObject program_number_map;

	private JSONObject pid_map;


	/** 节目号 [1,65535] */
	private Integer program_number;
	
	/** 节目名称 */
	private String name;
	
	/** 节目提供商 */
	private String provider;
	
	/** 节目PMT和PID [0,8191]*/
	private Integer pmt_pid;
	
	private Integer pcr_pid;
	
	private List<ProgramVideoBO> video_array;
	
	private List<ProgramAudioBO> audio_array;
	
	private List<ProgramSubtitleBO> subtitle_array;

	private String input_id;

	public Integer getProgram_number() {
		return program_number;
	}

	public ProgramBO setProgram_number(Integer program_number) {
		this.program_number = program_number;
		return this;
	}

	public String getName() {
		return name;
	}

	public ProgramBO setName(String name) {
		this.name = name;
		return this;
	}

	public String getProvider() {
		return provider;
	}

	public ProgramBO setProvider(String provider) {
		this.provider = provider;
		return this;
	}

	public Integer getPmt_pid() {
		return pmt_pid;
	}

	public ProgramBO setPmt_pid(Integer pmt_pid) {
		this.pmt_pid = pmt_pid;
		return this;
	}

	public Integer getPcr_pid() {
		return pcr_pid;
	}

	public ProgramBO setPcr_pid(Integer pcr_pid) {
		this.pcr_pid = pcr_pid;
		return this;
	}

	public List<ProgramVideoBO> getVideo_array() {
		return video_array;
	}

	public ProgramBO setVideo_array(List<ProgramVideoBO> video_array) {
		this.video_array = video_array;
		return this;
	}

	public List<ProgramAudioBO> getAudio_array() {
		return audio_array;
	}

	public ProgramBO setAudio_array(List<ProgramAudioBO> audio_array) {
		this.audio_array = audio_array;
		return this;
	}

	public List<ProgramSubtitleBO> getSubtitle_array() {
		return subtitle_array;
	}

	public ProgramBO setSubtitle_array(List<ProgramSubtitleBO> subtitle_array) {
		this.subtitle_array = subtitle_array;
		return this;
	}

	public String getInput_id() {
		return input_id;
	}

	public void setInput_id(String input_id) {
		this.input_id = input_id;
	}

	public JSONObject getNormal_map() {
		return normal_map;
	}

	public ProgramBO setNormal_map(JSONObject normal_map) {
		this.normal_map = normal_map;
		return this;
	}

	public JSONObject getMedia_type_once_map() {
		return media_type_once_map;
	}

	public ProgramBO setMedia_type_once_map(JSONObject media_type_once_map) {
		this.media_type_once_map = media_type_once_map;
		return this;
	}

	public JSONObject getProgram_number_map() {
		return program_number_map;
	}

	public ProgramBO setProgram_number_map(JSONObject program_number_map) {
		this.program_number_map = program_number_map;
		return this;
	}

	public JSONObject getPid_map() {
		return pid_map;
	}

	public ProgramBO setPid_map(JSONObject pid_map) {
		this.pid_map = pid_map;
		return this;
	}
}
