package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 输出节目参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午2:40:20
 */
public class ProgramOutputBO {

	/** 节目号 */
	private Integer program_number;
	
	/** 媒体数组 */
	private List<ProgramElementBO> element_array;

	public Integer getProgram_number() {
		return program_number;
	}

	public ProgramOutputBO setProgram_number(Integer program_number) {
		this.program_number = program_number;
		return this;
	}

	public List<ProgramElementBO> getElement_array() {
		return element_array;
	}

	public ProgramOutputBO setElement_array(List<ProgramElementBO> element_array) {
		this.element_array = element_array;
		return this;
	}

	public ProgramOutputBO() {
	}

	public ProgramOutputBO(ProgramBO programBO){
		this.program_number=programBO.getProgram_number();
		List<ProgramElementBO> elementBOS = new ArrayList<>();
		List<PidIndexBO> pidIndexBOS = new ArrayList<>();
		pidIndexBOS.add(new PidIndexBO().setPid_index(0));
		List<ProgramVideoBO> videos = programBO.getVideo_array();
		if (!CollectionUtils.isEmpty(videos)) {
			ProgramElementBO eleBO = new ProgramElementBO().setPid(videos.get(0).getPid()).setProgram_switch_array(pidIndexBOS);
			elementBOS.add(eleBO);
		}
		List<ProgramAudioBO> audios = programBO.getAudio_array();
		if (!CollectionUtils.isEmpty(audios)) {
			ProgramElementBO eleBO = new ProgramElementBO().setPid(audios.get(0).getPid()).setProgram_switch_array(pidIndexBOS);
			elementBOS.add(eleBO);
		}
		List<ProgramSubtitleBO> subtitles = programBO.getSubtitle_array();
		if (!CollectionUtils.isEmpty(subtitles)) {
			ProgramElementBO eleBO = new ProgramElementBO().setPid(subtitles.get(0).getPid()).setProgram_switch_array(pidIndexBOS);
			elementBOS.add(eleBO);
		}
		this.element_array=elementBOS;
	}

}
