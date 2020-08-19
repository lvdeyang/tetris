package com.sumavision.tetris.mims.app.media.live;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaPushLiveTypeVideo {
	MPEG_2("mpeg2"),
	MPEG_4("mpeg4"),
	H_263("h263"),
	H_264("h264"),
	SOR("sor"),
	VP_6("vp6"),
	VP_6F("vp6f"),
	VP_6A("vp6a"),
	AVS("avs"),
	AVS_2("avs2"),
	REAL_8("real8"),
	REAL_9("real9"),
	VC_1("vc1");
	
	private String name;

	public String getName() {
		return name;
	}
	
	private MediaPushLiveTypeVideo(String name) {
		this.name = name;
	}
	
	public static MediaPushLiveTypeVideo fromName(String name) throws Exception {
		MediaPushLiveTypeVideo[] values = MediaPushLiveTypeVideo.values();
		for (MediaPushLiveTypeVideo mediaPushLiveVideoType : values) {
			if (mediaPushLiveVideoType.getName().equals(name)) {
				return mediaPushLiveVideoType;
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
		MediaPushLiveTypeVideo[] videoTypes = MediaPushLiveTypeVideo.values();
		for (MediaPushLiveTypeVideo videoType : videoTypes) {
			types.add(videoType.getName());
		}
		return types;
	}
}
