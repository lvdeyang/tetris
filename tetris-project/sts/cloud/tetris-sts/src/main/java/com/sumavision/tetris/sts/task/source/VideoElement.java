package com.sumavision.tetris.sts.task.source;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.dialect.identity.GetGeneratedKeysDelegate;

import java.io.Serializable;

public class VideoElement implements Serializable,Comparable<VideoElement>{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6698042031700018906L;

	private Integer pid;

    private String type;

    private Integer width;

    private Integer height;

    private Float fps;

    private Long bitrate;

    private String ratio;
    
    //解码方式,默认cpu
    private String decode_mode;
    
    private Integer nv_card_idx;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getBitrate() {
        return bitrate;
    }

    public void setBitrate(Long bitrate) {
        this.bitrate = bitrate;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
    
    public String getDecode_mode() {
		return decode_mode;
	}

	public void setDecode_mode(String decode_mode) {
		this.decode_mode = decode_mode;
	}
	
	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public void setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
	}

	/**
     * 自定义对比
     * @param other
     * @return
     */
    public boolean equals(VideoElement other){
    	if(!this.getPid().equals(other.getPid())){
    		return false;
    	}
    	if(!this.getType().equals(other.getType())){
    		return false;
    	}
    	if(!this.getHeight().equals(other.getHeight())){
    		return false;
    	}
    	if(!this.getWidth().equals(other.getWidth())){
    		return false;
    	}
    	return true;
    }

	@Override
	public int compareTo(VideoElement o) {
		// TODO Auto-generated method stub
		if(this.getPid().intValue() < o.getPid().intValue()){
			return 01;
		}
		return 0;
	}
	
	public VideoElement clone(){
		VideoElement videoElement = new VideoElement();
		videoElement.setBitrate(getBitrate());
		videoElement.setFps(getFps());
		videoElement.setHeight(getHeight());
		videoElement.setWidth(getWidth());
		videoElement.setPid(getPid());
		videoElement.setRatio(getRatio());
		videoElement.setType(getType());
		videoElement.setDecode_mode(getDecode_mode());
		videoElement.setNv_card_idx(getNv_card_idx());
		return videoElement;
	}
}
