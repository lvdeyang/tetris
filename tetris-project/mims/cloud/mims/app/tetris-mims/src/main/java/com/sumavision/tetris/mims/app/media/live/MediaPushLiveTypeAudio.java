package com.sumavision.tetris.mims.app.media.live;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaPushLiveTypeAudio {
	AAC("aac"),
	MP_2("mp2"),
	MP_3("mp3"),
	DDP("ddp"),
	AC_3("ac3"),
	DTS("dts"),
	DRA("dra"),
	PCM("pcm"),
	M1P("m1p"),
	AMR("amr");

	private String name;

	public String getName() {
		return name;
	}
	
	private MediaPushLiveTypeAudio(String name) {
		this.name = name;
	}
	
	public static MediaPushLiveTypeAudio fromName(String name) throws Exception {
		MediaPushLiveTypeAudio[] values = MediaPushLiveTypeAudio.values();
		for (MediaPushLiveTypeAudio mediaPushLiveAudioType : values) {
			if (mediaPushLiveAudioType.getName().equals(name)) {
				return mediaPushLiveAudioType;
			}
		}
		throw new ErrorTypeException("MediaPushLiveVideoType", name);
	}
	
	/**
	 * 获取流类型String数组<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:49:04
	 * @return List<String> 流类型数组
	 */
	public static List<String> queryAllType() throws Exception {
		List<String> types = new ArrayList<String>();
		MediaPushLiveTypeAudio[] audioTypes = MediaPushLiveTypeAudio.values();
		for (MediaPushLiveTypeAudio audioType : audioTypes) {
			types.add(audioType.getName());
		}
		return types;
	}
}
