package com.sumavision.signal.bvc.capacity.bo.input;

import java.util.List;

/**
 * 排期节目参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年4月28日 上午9:15:15
 */
public class ScheduleProgramBO {
	
	/** 输入id */
	private String input_id;
	
	/** 节目号 */
	private Integer program_number;
	
	/** 媒体数组 */
	private List<ProgramElementBO> element_array;
	
	/** 文件播放参数 */
	private ProgramFileBO file;
	
	/** 直播流播放参数 */
	private ProgramStreamBO live;

	public String getInput_id() {
		return input_id;
	}

	public ScheduleProgramBO setInput_id(String input_id) {
		this.input_id = input_id;
		return this;
	}

	public Integer getProgram_number() {
		return program_number;
	}

	public ScheduleProgramBO setProgram_number(Integer program_number) {
		this.program_number = program_number;
		return this;
	}

	public List<ProgramElementBO> getElement_array() {
		return element_array;
	}

	public ScheduleProgramBO setElement_array(List<ProgramElementBO> element_array) {
		this.element_array = element_array;
		return this;
	}

	public ProgramFileBO getFile() {
		return file;
	}

	public ScheduleProgramBO setFile(ProgramFileBO file) {
		this.file = file;
		return this;
	}

	public ProgramStreamBO getLive() {
		return live;
	}

	public ScheduleProgramBO setLive(ProgramStreamBO live) {
		this.live = live;
		return this;
	}

	
}
