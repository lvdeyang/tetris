package com.sumavision.tetris.business.director.vo;

import java.util.List;

/**
 * 转码参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月17日 上午8:24:30
 */
public class TranscodeVO {
	
	private TranscodeVideoVO video;
	
	private TranscodeAudioVO audio;
	
	private TranscodeSubtitleVO subtitile;

	public TranscodeVideoVO getVideo() {
		return video;
	}

	public void setVideo(TranscodeVideoVO video) {
		this.video = video;
	}

	public TranscodeAudioVO getAudio() {
		return audio;
	}

	public void setAudio(TranscodeAudioVO audio) {
		this.audio = audio;
	}

	public TranscodeSubtitleVO getSubtitile() {
		return subtitile;
	}

	public void setSubtitile(TranscodeSubtitleVO subtitile) {
		this.subtitile = subtitile;
	}
	
}
