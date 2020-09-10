package com.sumavision.tetris.guide.BO;

import java.util.List;

public class ProgramArray {
	
	private int program_number;
	
	private List<MediaArray> media_array;

	public int getProgram_number() {
		return program_number;
	}

	public ProgramArray setProgram_number(int program_number) {
		this.program_number = program_number;
		return this;
	}

	public List<MediaArray> getMedia_array() {
		return media_array;
	}

	public ProgramArray setMedia_array(List<MediaArray> media_array) {
		this.media_array = media_array;
		return this;
	}

}
