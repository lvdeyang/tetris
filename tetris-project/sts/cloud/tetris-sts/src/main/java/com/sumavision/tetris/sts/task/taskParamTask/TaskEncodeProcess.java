package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskEncodeProcess implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8700014917932273833L;
	
	private ArrayList<TaskOsdBO> osd;
	private TaskEnhanceBO enhance; 
	private AudioProcessBO audioProcess;
	private AudioResampleBO resample;
	private TaskScaleBO scale;
	private TaskCutBO cut;
	public ArrayList<TaskOsdBO> getOsd() {
		return osd;
	}
	public void setOsd(ArrayList<TaskOsdBO> osd) {
		this.osd = osd;
	}
	public TaskEnhanceBO getEnhance() {
		return enhance;
	}
	public void setEnhance(TaskEnhanceBO enhance) {
		this.enhance = enhance;
	}
	public AudioProcessBO getAudioProcess() {
		return audioProcess;
	}
	public void setAudioProcess(AudioProcessBO audioProcess) {
		this.audioProcess = audioProcess;
	}
	public TaskScaleBO getScale() {
		return scale;
	}
	public void setScale(TaskScaleBO scale) {
		this.scale = scale;
	}
	public TaskCutBO getCut() {
		return cut;
	}
	public void setCut(TaskCutBO cut) {
		this.cut = cut;
	}
	public AudioResampleBO getResample() {
		return resample;
	}
	public void setResample(AudioResampleBO resample) {
		this.resample = resample;
	}
	
	
}
