package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class TaskSubtitleBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5439867308148379785L;
	
	private String track_type;
	private String text_content;
	private String front_type;
	private String position;
	private String zone;
	private String ftont_color;
	private Integer track_speed;
	private Integer font_size;
	private Boolean show_bkg;
	private Boolean show_outline;
	private String back_color;
	private String outline_color;
	
	public TaskSubtitleBO(String track_type, String text_content,
			String front_type, String position, String zone,
			String ftont_color, Integer track_speed, Integer font_size,
			Boolean show_bkg, Boolean show_outline, String back_color,
			String outline_color) {
		super();
		this.track_type = track_type;
		this.text_content = text_content;
		this.front_type = front_type;
		this.position = position;
		this.zone = zone;
		this.ftont_color = ftont_color;
		this.track_speed = track_speed;
		this.font_size = font_size;
		this.show_bkg = show_bkg;
		this.show_outline = show_outline;
		this.back_color = back_color;
		this.outline_color = outline_color;
	}
	public String getTrack_type() {
		return track_type;
	}
	public void setTrack_type(String track_type) {
		this.track_type = track_type;
	}
	public String getText_content() {
		return text_content;
	}
	public void setText_content(String text_content) {
		this.text_content = text_content;
	}
	public String getFront_type() {
		return front_type;
	}
	public void setFront_type(String front_type) {
		this.front_type = front_type;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getFtont_color() {
		return ftont_color;
	}
	public void setFtont_color(String ftont_color) {
		this.ftont_color = ftont_color;
	}
	public Integer getTrack_speed() {
		return track_speed;
	}
	public void setTrack_speed(Integer track_speed) {
		this.track_speed = track_speed;
	}
	public Integer getFont_size() {
		return font_size;
	}
	public void setFont_size(Integer font_size) {
		this.font_size = font_size;
	}
	public Boolean getShow_bkg() {
		return show_bkg;
	}
	public void setShow_bkg(Boolean show_bkg) {
		this.show_bkg = show_bkg;
	}
	public Boolean getShow_outline() {
		return show_outline;
	}
	public void setShow_outline(Boolean show_outline) {
		this.show_outline = show_outline;
	}
	public String getBack_color() {
		return back_color;
	}
	public void setBack_color(String back_color) {
		this.back_color = back_color;
	}
	public String getOutline_color() {
		return outline_color;
	}
	public void setOutline_color(String outline_color) {
		this.outline_color = outline_color;
	}
	
	
}
