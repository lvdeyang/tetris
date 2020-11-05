package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TaskVO {
	private Long id;
	private String name;
	private String status;
	private AudioVO audio;
	private VideoVO video;
	
	public TaskVO(){}
	
	public TaskVO(Long id, AudioVO audio, VideoVO video) {
		this(id, "", "3", audio, video);
	}
	
	public Long getId() {
		return id;
	}
	
	@XmlAttribute(name = "id")
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}
	
	@XmlAttribute(name = "status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	public AudioVO getAudio() {
		return audio;
	}
	
	@XmlElement(name = "audio")
	public void setAudio(AudioVO audio) {
		this.audio = audio;
	}

	public VideoVO getVideo() {
		return video;
	}

	@XmlElement(name = "video")
	public void setVideo(VideoVO video) {
		this.video = video;
	}
	
	public TaskVO(Long id,
			String name,
			String status,
			AudioVO audio,
			VideoVO video) {
		this.id = id;
		this.name = name;
		this.status = status;
		if (audio != null) this.audio = audio;
		if (video != null) this.video = video;
	}
	
	public TaskVO copy() {
		return new TaskVO(this.getId(), this.getAudio().copy(), this.getVideo().copy());
	}
}
