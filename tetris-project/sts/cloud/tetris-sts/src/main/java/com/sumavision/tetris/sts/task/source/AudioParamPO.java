package com.sumavision.tetris.sts.task.source;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeAudioAacBO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeAudioDolbyBO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeAudioG711aBO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeAudioG711uBO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeAudioMp2BO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeAudioMp3BO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeCommon;

import java.io.Serializable;


@Entity
@Table(name="audio_param")
public class AudioParamPO extends CommonPO<AudioParamPO> implements Serializable,Comparable<AudioParamPO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7237846036508658183L;

	private Integer pid;
	
	/**
	 * 作用同video参数中的trackid
	 */
	private Integer trackId;
	
	private String codec;
	
	private Long sample;
	
	private Long bitrate;
	
	private Long volume;
	
	private String agcGain;
	
	private String audioDupMode;
	
	private String chLayout;
	
	private String denoise;

	@Column
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	@Column
	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}
	@Column
	public Long getSample() {
		return sample;
	}

	public void setSample(Long sample) {
		this.sample = sample;
	}
	@Column
	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}
	@Column
	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}
	@Column
	public String getAgcGain() {
		return agcGain;
	}

	public void setAgcGain(String agcGain) {
		this.agcGain = agcGain;
	}
	@Column
	public String getAudioDupMode() {
		return audioDupMode;
	}

	public void setAudioDupMode(String audioDupMode) {
		this.audioDupMode = audioDupMode;
	}
	@Column
	public String getChLayout() {
		return chLayout;
	}

	public void setChLayout(String chLayout) {
		this.chLayout = chLayout;
	}
	@Column
	public Integer getTrackId() {
		return trackId;
	}

	public void setTrackId(Integer trackId) {
		this.trackId = trackId;
	}

	@Column
	public String getDenoise() {
		return denoise;
	}

	public void setDenoise(String denoise) {
		this.denoise = denoise;
	}

	@Override
	public int compareTo(AudioParamPO o) {
		// TODO Auto-generated method stub
		if(this.getTrackId() > o.getTrackId()){
			return 1;
		}
		return -1;
	}
    //新协议中h264（h265类似）包括：CPU-x264、CPU-uux264、GPU-Intel、GPU-Nvidia
	public EncodeCommon getEncodeCommon(String codec){
		EncodeCommon encodeCommon = null;
		//采样位数sample_byte默认2，sample_fmt默认"s16"，之后修改
		switch (codec) {
		case "aac":
		case "he-aac":
		case "he-aac-v2":	
			encodeCommon = new EncodeAudioAacBO(bitrate.intValue(), sample.intValue(), "s16", 2, chLayout, getType(codec));
			break;
		case "mp3":
			encodeCommon = new EncodeAudioMp3BO(bitrate.intValue(), sample.intValue(), 2, "s16", chLayout);
			break;
		case "mpeg2-audio":
		case "mp2":
			encodeCommon = new EncodeAudioMp2BO(bitrate.intValue(), sample.intValue(), 2, "s16", chLayout);
			break;
		case "ac3":
		case "eac3":
			encodeCommon = new EncodeAudioDolbyBO(bitrate.intValue(), sample.intValue(), 2, "s16", chLayout, getType(codec));
			break;
		case  "g711a":
			encodeCommon = new EncodeAudioG711aBO(bitrate.intValue(), sample.intValue(), 2, chLayout, 20);
			break;
		case  "g711u":
			encodeCommon = new EncodeAudioG711uBO(bitrate.intValue(), sample.intValue(), 2, chLayout, 20);
			break;
		}
		
		return encodeCommon;
	}
	
	public Integer getChannelByLayout(String channelLayout){
		switch (channelLayout) {
			case "mono":
				return 1;
			case "stereo":
				return 2;
			case "2.1":
				return 3;
			case "3.0":
				return 3;
			case "4.0":
				return 4;
			case "5.0":
				return 5;
			case "5.1":
				return 6;
			case "7.1":
				return 8;
			default:
				return 2;
		}
	}
	
		
	public String getType(String codec){
		switch (codec) {
			case "aac":
				return "mpeg2-aac-lc";
			case "he-aac":
				return "mpeg2-he-aac-lc";
			case "he-aac-v2":
				return "mpeg2-he-aac-v2-lc";
			case "ac3":
				return "ac3";
			case "eac3":
				return "eac3";
				
			default:
				return "ac3";
		}
	}
	
	public Boolean isSameWithCfg(AudioParamPO audioParamPO){
		if(audioParamPO == null){
			return false;
		}
		if (this.pid == null) {
			if (audioParamPO.pid != null)
				return false;
		} else if (!this.pid.equals(audioParamPO.pid))
			return false;
		if (this.codec == null) {
			if (audioParamPO.codec != null)
				return false;
		} else if (!this.codec.equals(audioParamPO.codec))
			return false;
		if (this.bitrate == null) {
			if (audioParamPO.bitrate != null)
				return false;
		} else if (!(this.bitrate).equals(audioParamPO.bitrate/1000))
			return false;
		if (this.sample == null) {
			if (audioParamPO.sample != null)
				return false;
		} else if (!(this.sample).equals(audioParamPO.sample))
			return false;
		if (this.volume == null) {
			if (audioParamPO.volume != null)
				return false;
		} else if (!(this.volume).equals(audioParamPO.volume))
			return false;
		if (this.agcGain == null) {
			if (audioParamPO.agcGain != null)
				return false;
		} else if (!(this.agcGain).equals(audioParamPO.agcGain))
			return false;
		if (this.audioDupMode == null) {
			if (audioParamPO.audioDupMode != null)
				return false;
		} else if (!(this.audioDupMode).equals(audioParamPO.audioDupMode))
			return false;
		if (this.chLayout == null) {
			if (audioParamPO.chLayout != null)
				return false;
		} else if (!(this.chLayout).equals(audioParamPO.chLayout))
			return false;
		if (this.denoise == null) {
			if (audioParamPO.denoise != null)
				return false;
		} else if (!(this.denoise).equals(audioParamPO.denoise))
			return false;
		return true;
	}}
