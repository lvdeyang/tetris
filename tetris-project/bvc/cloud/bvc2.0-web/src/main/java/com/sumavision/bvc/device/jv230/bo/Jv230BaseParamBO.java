package com.sumavision.bvc.device.jv230.bo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;

public class Jv230BaseParamBO {

	/**
	 * 视频需要
	 */
	/**h264/h265*/
	private String codec_type;
	
	/**是否是轮询"true"/"false"*/
	private String is_polling;
	
	/**轮询时间：is_polling为"true"时添加*/
	private int interval;
	
	/**源模式：0--auto（自动全屏）；1--stay_ratio（自动全屏保持宽高比）；2--cut（裁剪）*/
	private int src_mode;
	
	/**源标识--同一个源该字段要相同*/
	private String src_identify;
	
	/**共享源数量--判断源跨屏发组播*/
	private int src_share_cnt;
	
	/**源裁剪位置--src_mode为2时有效*/
	private PositionBO src_cut;
	
	/**显示在jv230上的区域位置*/
	private PositionBO display_rect;
	
	/**
	 * 视频源列表
	 * */
	private List<Jv230SourceBO> sources;
	
	/**
	 * 音频需要
	 */
	/**pcmu/pcma/aac*/
	private String codec;
	
	private Jv230SourceBO source;

	public String getCodec_type() {
		return codec_type;
	}

	public Jv230BaseParamBO setCodec_type(String codec_type) {
		this.codec_type = codec_type;
		return this;
	}

	public String getIs_polling() {
		return is_polling;
	}

	public Jv230BaseParamBO setIs_polling(String is_polling) {
		this.is_polling = is_polling;
		return this;
	}

	public int getInterval() {
		return interval;
	}

	public Jv230BaseParamBO setInterval(int interval) {
		this.interval = interval;
		return this;
	}

	public int getSrc_mode() {
		return src_mode;
	}

	public Jv230BaseParamBO setSrc_mode(int src_mode) {
		this.src_mode = src_mode;
		return this;
	}

	public String getSrc_identify() {
		return src_identify;
	}

	public Jv230BaseParamBO setSrc_identify(String src_identify) {
		this.src_identify = src_identify;
		return this;
	}

	public int getSrc_share_cnt() {
		return src_share_cnt;
	}

	public Jv230BaseParamBO setSrc_share_cnt(int src_share_cnt) {
		this.src_share_cnt = src_share_cnt;
		return this;
	}

	public PositionBO getSrc_cut() {
		return src_cut;
	}

	public Jv230BaseParamBO setSrc_cut(PositionBO src_cut) {
		this.src_cut = src_cut;
		return this;
	}

	public PositionBO getDisplay_rect() {
		return display_rect;
	}

	public Jv230BaseParamBO setDisplay_rect(PositionBO display_rect) {
		this.display_rect = display_rect;
		return this;
	}

	public String getCodec() {
		return codec;
	}

	public Jv230BaseParamBO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public List<Jv230SourceBO> getSources() {
		return sources;
	}

	public Jv230BaseParamBO setSources(List<Jv230SourceBO> sources) {
		this.sources = sources;
		return this;
	}	
	
	public Jv230SourceBO getSource() {
		return source;
	}

	public Jv230BaseParamBO setSource(Jv230SourceBO source) {
		this.source = source;
		return this;
	}

	public Jv230BaseParamBO setVideo(
			DeviceGroupAvtplPO avtpl,
			PositionBO src_cut, 
			PositionBO display_rect, 
			List<Jv230SourceBO> sources){
		this.setCodec_type(avtpl.getVideoFormat().getName())
			.setSrc_cut(src_cut)
			.setDisplay_rect(display_rect)
			.setSources(sources);
		
		return this;
	}
	
	public Jv230BaseParamBO setAudio(DeviceGroupAvtplPO avtpl, Jv230SourceBO source){
		this.setCodec(avtpl.getAudioFormat().getName())
			.setSource(source);
		
		return this;
	}
	
	public Jv230BaseParamBO setNull(){
		this.setSources(new ArrayList<Jv230SourceBO>())
			.setSrc_cut(new PositionBO())
			.setDisplay_rect(new PositionBO())
			.setCodec_type("h264")
			.setIs_polling("false");
		
		return this;
	}
	
	public Jv230BaseParamBO setAudioNull(){
		this.setSource(new Jv230SourceBO())
			.setCodec("aac");
		
		return this;
	}
}
