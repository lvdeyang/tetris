/**
 * 
 */
package com.suma.venus.resource.bo;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class ProgramsBO {

	private String num;
	
	private String name;
	
	private List<VideoChannelBO> videos;
	
	private List<AudioChannelBO> audios;

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
	public List<VideoChannelBO> getVideos() {
		return videos;
	}

	/**
	 * @param videos the videos to set
	 */
	public void setVideos(List<VideoChannelBO> videos) {
		this.videos = videos;
	}

	/**
	 * @return the audios
	 */
	public List<AudioChannelBO> getAudios() {
		return audios;
	}

	/**
	 * @param audios the audios to set
	 */
	public void setAudios(List<AudioChannelBO> audios) {
		this.audios = audios;
	}
	
	
}
