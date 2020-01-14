package com.sumavision.tetris.sts.device.auth;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.sts.common.CommonPO;


/**
 * 输入与输出视频类型信息
 * 此表信息既可以转码表示输入类型，也可以表示转码输出类型
 *
 */
@Entity
@Table(name="channel_video_type")
public class ChannelVideoTypePO  extends CommonPO<ChannelVideoTypePO> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7532631386692592013L;

	private String videoType;
	
	private Integer width;
	
	private Integer height;
	
	private Float fps;
	
	private String type;

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Float getFps() {
		return fps;
	}

	public void setFps(Float fps) {
		this.fps = fps;
	}
	
	public String generateTypeString(){
		return videoType + " " + width + "*" + height;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
