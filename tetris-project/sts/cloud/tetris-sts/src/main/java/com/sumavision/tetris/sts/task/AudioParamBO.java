package com.sumavision.tetris.sts.task;

import java.io.Serializable;
import java.util.Objects;

import com.sumavision.tetris.sts.task.source.AudioParamPO;

public class AudioParamBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -234538095816951415L;

	private Integer pid;
	
	private int trackId;
	
	private String codec;
	
	private Long sample;
	
	private Long bitrate;
	
	private Long volume;
	
	private String agcGain;
	
	private String audioDupMode;
	
	private String chLayout;
	
	private String denoise;

	public AudioParamPO transToAudioParamPO(){
		AudioParamPO audioParamPO = new AudioParamPO();
		audioParamPO.setPid(pid);
		audioParamPO.setTrackId(trackId);
		audioParamPO.setCodec(codec);
		audioParamPO.setSample(sample);
		audioParamPO.setBitrate(bitrate);
		audioParamPO.setVolume(volume);
		audioParamPO.setAgcGain(agcGain);
		audioParamPO.setAudioDupMode(audioDupMode);
		audioParamPO.setChLayout(chLayout);
		audioParamPO.setDenoise(denoise);
		
		return audioParamPO;
	}


	public boolean keyParamCompare(Object o) {
		if (this == o) return true;
		if (!(o instanceof AudioParamBO)) return false;
		AudioParamBO that = (AudioParamBO) o;
		return trackId == that.trackId &&
				Objects.equals(pid, that.pid) &&
				Objects.equals(codec, that.codec) &&
				Objects.equals(sample, that.sample) &&
				Objects.equals(bitrate, that.bitrate) &&
				Objects.equals(volume, that.volume) &&
				Objects.equals(agcGain, that.agcGain) &&
				Objects.equals(audioDupMode, that.audioDupMode) &&
				Objects.equals(chLayout, that.chLayout) &&
				Objects.equals(denoise, that.denoise);
	}

	@Override
	public int hashCode() {

		return Objects.hash(pid, trackId, codec, sample, bitrate, volume, agcGain, audioDupMode, chLayout, denoise);
	}

	public int getTrackId() {
		return trackId;
	}



	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}



	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public Long getSample() {
		return sample;
	}

	public void setSample(Long sample) {
		this.sample = sample;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public String getAgcGain() {
		return agcGain;
	}

	public void setAgcGain(String agcGain) {
		this.agcGain = agcGain;
	}

	public String getAudioDupMode() {
		return audioDupMode;
	}

	public void setAudioDupMode(String audioDupMode) {
		this.audioDupMode = audioDupMode;
	}

	public String getChLayout() {
		return chLayout;
	}

	public void setChLayout(String chLayout) {
		this.chLayout = chLayout;
	}



	public String getDenoise() {
		return denoise;
	}



	public void setDenoise(String denoise) {
		this.denoise = denoise;
	}
	
}
