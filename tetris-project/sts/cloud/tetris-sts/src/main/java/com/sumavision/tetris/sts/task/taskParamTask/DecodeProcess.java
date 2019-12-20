package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;
import java.util.ArrayList;


public class DecodeProcess implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4318824640401620208L;
	private TaskDeinterlaceBO deinterlace;
	//private TaskScaleBO scale;
	//private TaskCutBO cut;

	public TaskDeinterlaceBO getDeinterlace() {
		return deinterlace;
	}
	public void setDeinterlace(TaskDeinterlaceBO deinterlace) {
		this.deinterlace = deinterlace;
	}
//	public TaskScaleBO getScale() {
//		return scale;
//	}
//	public void setScale(TaskScaleBO scale) {
//		this.scale = scale;
//	}
//	public TaskCutBO getCut() {
//		return cut;
//	}
//	public void setCut(TaskCutBO cut) {
//		this.cut = cut;
//	}
//	
}
