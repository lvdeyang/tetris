/**
 * 
 */
package com.suma.venus.resource.bo;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
 * @author Administrator
 *
 */
public class ProgramsBO {

	private String num;
	
	private String name;
	
	private JSONArray videos;
	
	private JSONArray audios;

	/**
	 * @return the num
	 */
	public String getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(String num) {
		this.num = num;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the videos
	 */
	public JSONArray getVideos() {
		return videos;
	}

	/**
	 * @param videos the videos to set
	 */
	public void setVideos(JSONArray videos) {
		this.videos = videos;
	}

	/**
	 * @return the audios
	 */
	public JSONArray getAudios() {
		return audios;
	}

	/**
	 * @param audios the audios to set
	 */
	public void setAudios(JSONArray audios) {
		this.audios = audios;
	}
	
}
