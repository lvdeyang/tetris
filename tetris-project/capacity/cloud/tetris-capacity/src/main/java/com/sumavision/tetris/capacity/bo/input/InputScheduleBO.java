package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.template.SourceVO;

import java.util.ArrayList;

/**
 * 排期参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年4月28日 上午9:11:48
 */
public class InputScheduleBO {
	
	private String stream_type;
	
	private ProgramOutputBO output_program;

	public String getStream_type() {
		return stream_type;
	}

	public InputScheduleBO setStream_type(String stream_type) {
		this.stream_type = stream_type;
		return this;
	}

	public ProgramOutputBO getOutput_program() {
		return output_program;
	}

	public InputScheduleBO setOutput_program(ProgramOutputBO output_program) {
		this.output_program = output_program;
		return this;
	}


	public InputScheduleBO() {
	}

	public InputScheduleBO(SourceVO tmplInputBO) {
		this.stream_type=tmplInputBO.getStream_type();
		ProgramOutputBO programOutputBO = new ProgramOutputBO();
		if (tmplInputBO.getOutput_program()!=null){
			programOutputBO = JSONObject.parseObject(tmplInputBO.getOutput_program(),ProgramOutputBO.class);//默认音视频都有，如果只有音频就把音频的删掉
			if ("audio".equals(tmplInputBO.getMediaType())){
				for (int i = 0; i < programOutputBO.getElement_array().size(); i++) {
					ProgramElementBO eleBO = programOutputBO.getElement_array().get(i);
					if (!"audio".equals(eleBO.getType())){
						programOutputBO.getElement_array().remove(eleBO);
					}
				}
			}
		}else{
			if (tmplInputBO.getMediaType()==null || "video".equals(tmplInputBO.getMediaType())) {
				ProgramElementBO velementBO = new ProgramElementBO().setType("video").setPid(513);
				ProgramElementBO aelementBO = new ProgramElementBO().setType("audio").setPid(514);

				programOutputBO.getElement_array().add(velementBO);
				programOutputBO.getElement_array().add(aelementBO);
			}
			if ("audio".equals(tmplInputBO.getMediaType())){
				ProgramElementBO aelementBO = new ProgramElementBO().setType("audio").setPid(514);
				programOutputBO.getElement_array().add(aelementBO);
			}
		}
		this.setOutput_program(programOutputBO);
	}
}
