package com.sumavision.tetris.sts.task.source;


import java.io.Serializable;

public class AudioElement implements Serializable,Comparable<AudioElement> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2502315044040347250L;

	private Integer pid;

    private String type;

    private Long bitrate;

    private Integer channels;

    private Integer sampleRate;
    
    //解码方式,默认cpu
    private String decode_mode;

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

    public Long getBitrate() {
        return bitrate;
    }

    public void setBitrate(Long bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public Integer getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }
    
    public String getDecode_mode() {
		return decode_mode;
	}

	public void setDecode_mode(String decode_mode) {
		this.decode_mode = decode_mode;
	}

    public boolean equals(AudioElement other){
    	if(!this.getPid().equals(other.getPid())){
    		return false;
    	}
    	if(!this.getType().equals(other.getType())){
    		return false;
    	}
    	return true;
    }

	@Override
	public int compareTo(AudioElement o) {
		// TODO Auto-generated method stub
		if(this.getPid().intValue() < o.getPid().intValue()){
			return -1;
		}
		return 1;
	}
	
	public AudioElement clone(){
		AudioElement audioElement = new AudioElement();
		audioElement.setDecode_mode(getDecode_mode());
		audioElement.setBitrate(getBitrate());
		audioElement.setChannels(getChannels());
		audioElement.setPid(getPid());
		audioElement.setSampleRate(getSampleRate());
		audioElement.setType(getType());
		return audioElement;
	}
}
