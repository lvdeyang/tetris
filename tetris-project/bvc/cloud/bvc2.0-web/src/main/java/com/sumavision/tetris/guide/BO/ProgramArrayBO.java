package com.sumavision.tetris.guide.BO;

import java.util.List;

public class ProgramArrayBO {
	
	private int program_number;
	
	private List<MediaArrayBO> media_array;

	public int getProgram_number() {
		return program_number;
	}

	public ProgramArrayBO setProgram_number(int program_number) {
		this.program_number = program_number;
		return this;
	}

	public List<MediaArrayBO> getMedia_array() {
		return media_array;
	}

	public ProgramArrayBO setMedia_array(List<MediaArrayBO> media_array) {
		this.media_array = media_array;
		return this;
	}

}
