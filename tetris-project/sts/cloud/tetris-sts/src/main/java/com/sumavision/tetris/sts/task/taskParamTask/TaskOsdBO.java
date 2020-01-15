package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskOsdBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3537267717066710182L;
	
	private ArrayList<TaskSubtitleBO> subtile;
	private ArrayList<TaskLogoBO> logo;
	private ArrayList<TaskFuzzyBO> fuzzy;
	public TaskOsdBO(ArrayList<TaskSubtitleBO> subtile, ArrayList<TaskLogoBO> logo, ArrayList<TaskFuzzyBO> fuzzy) {
		super();
		this.subtile = subtile;
		this.logo = logo;
		this.fuzzy = fuzzy;
	}
//	screen_cap缺失
	public ArrayList<TaskSubtitleBO> getSubtile() {
		return subtile;
	}
	public void setSubtile(ArrayList<TaskSubtitleBO> subtile) {
		this.subtile = subtile;
	}
	public ArrayList<TaskLogoBO> getLogo() {
		return logo;
	}
	public void setLogo(ArrayList<TaskLogoBO> logo) {
		this.logo = logo;
	}
	public ArrayList<TaskFuzzyBO> getFuzzy() {
		return fuzzy;
	}
	public void setFuzzy(ArrayList<TaskFuzzyBO> fuzzy) {
		this.fuzzy = fuzzy;
	}
	
	
}
