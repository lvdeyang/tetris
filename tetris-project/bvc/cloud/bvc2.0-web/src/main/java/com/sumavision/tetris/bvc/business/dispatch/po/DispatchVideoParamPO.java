package com.sumavision.tetris.bvc.business.dispatch.po;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.business.dispatch.bo.VideoParamBO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="TETRIS_DISPATCH_CHANNEL_VIDEO_PARAM")
public class DispatchVideoParamPO extends AbstractBasePO {
	
	private static final long serialVersionUID = 1L;

	private String resolution = "";
	
	private String codec = "";
	
	private String bit_depth = "8bit";
	
	private String chroma_subsampling = "YUV420";
	
	private String fps = "25.0";
	
	private int bitrate = 0;
	
	private String profile = "main";
	
	/** 关联调度通道 */
	private TetrisDispatchChannelPO channel;

	public String getResolution() {
		return resolution;
	}

	public DispatchVideoParamPO setResolution(String resolution) {
		this.resolution = resolution;
		return this;
	}

	public String getCodec() {
		return codec;
	}

	public DispatchVideoParamPO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public String getBit_depth() {
		return bit_depth;
	}

	public DispatchVideoParamPO setBit_depth(String bit_depth) {
		this.bit_depth = bit_depth;
		return this;
	}

	public String getChroma_subsampling() {
		return chroma_subsampling;
	}

	public DispatchVideoParamPO setChroma_subsampling(String chroma_subsampling) {
		this.chroma_subsampling = chroma_subsampling;
		return this;
	}

	public String getFps() {
		return fps;
	}

	public DispatchVideoParamPO setFps(String fps) {
		this.fps = fps;
		return this;
	}

	public int getBitrate() {
		return bitrate;
	}

//	public String getBitrate() {
//		return String.valueOf(bitrate);
//	}

//	public DispatchVideoParamPO setBitrate(String bitrate) {
//		this.bitrate = Integer.parseInt(bitrate);
//		return this;
//	}

	public DispatchVideoParamPO setBitrate(int bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public String getProfile() {
		return profile;
	}

	public DispatchVideoParamPO setProfile(String profile) {
		this.profile = profile;
		return this;
	}

	@OneToOne
	@JoinColumn(name = "DISPATCH_CHANNEL_ID")
	public TetrisDispatchChannelPO getChannel() {
		return channel;
	}

	public DispatchVideoParamPO setChannel(TetrisDispatchChannelPO channel) {
		this.channel = channel;
		return this;
	}
	
	public DispatchVideoParamPO set(VideoParamBO video_param){
		this.resolution = video_param.getResolution();		
		this.codec = video_param.getCodec();
		this.bit_depth = video_param.getBit_depth();
		this.chroma_subsampling = video_param.getChroma_subsampling();
		this.fps = video_param.getFps();
		this.bitrate = video_param.getBitrate();		
		this.profile = video_param.getProfile();
		return this;
	}
	
}
