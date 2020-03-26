package com.sumavision.tetris.bvc.business.dispatch.po;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.business.dispatch.bo.AudioParamBO;
import com.sumavision.tetris.orm.po.AbstractBasePO;


@Entity
@Table(name="TETRIS_DISPATCH_CHANNEL_AUDIO_PARAM")
public class DispatchAudioParamPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	private String codec = "";

	private String bitrate = "64000";
	
	private int sample_rate = 48000;
	
	private int channel_cnt = 1;
	
	private int bit_width = 16;
	
	/** 音量增益，音量调节0-100，协议范围为0-3000*/
	private int gain = 0;
	
	/** 关联调度通道 */
	private TetrisDispatchChannelPO channel;

	public String getCodec() {
		return codec;
	}

	public DispatchAudioParamPO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public String getBitrate() {
		return bitrate;
	}

	public DispatchAudioParamPO setBitrate(String bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public int getSample_rate() {
		return sample_rate;
	}

	public DispatchAudioParamPO setSample_rate(int sample_rate) {
		this.sample_rate = sample_rate;
		return this;
	}

	public int getChannel_cnt() {
		return channel_cnt;
	}

	public DispatchAudioParamPO setChannel_cnt(int channel_cnt) {
		this.channel_cnt = channel_cnt;
		return this;
	}

	public int getBit_width() {
		return bit_width;
	}

	public DispatchAudioParamPO setBit_width(int bit_width) {
		this.bit_width = bit_width;
		return this;
	}

	public int getGain() {
		return gain;
	}

	public DispatchAudioParamPO setGain(int gain) {
		this.gain = gain;
		return this;
	}

	@OneToOne
	@JoinColumn(name = "DISPATCH_CHANNEL_ID")
	public TetrisDispatchChannelPO getChannel() {
		return channel;
	}

	public DispatchAudioParamPO setChannel(TetrisDispatchChannelPO channel) {
		this.channel = channel;
		return this;
	}
	
	public DispatchAudioParamPO set(AudioParamBO audio_param){
		this.bit_width = audio_param.getBit_width();
		this.bitrate = audio_param.getBitrate();
		this.channel_cnt = audio_param.getChannel_cnt();
		this.codec = audio_param.getCodec();
		this.gain = audio_param.getGain();
		this.sample_rate = audio_param.getSample_rate();
		return this;
	}
}
