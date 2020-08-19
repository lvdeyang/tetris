package com.sumavision.tetris.capacity.bo.input;

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
	
}
