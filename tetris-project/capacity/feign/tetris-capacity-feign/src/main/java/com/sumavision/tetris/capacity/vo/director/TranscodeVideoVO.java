package com.sumavision.tetris.capacity.vo.director;

import java.util.List;

/**
 * 转码视频参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月24日 下午2:56:09
 */
public class TranscodeVideoVO {

	/** 视频码率 */
	private Integer bitrate;
	
	/** 视频编码格式 */
	private String codec;
	
	/** 帧率变换 */
	private String fps;
	
	/** 宽高比 */
	private String ratio;
	
	/** 宽 */
	private Integer width;
	
	/** 高 */
	private Integer height;
	
	/** 文本osd */
	private List<TextVO> contents;
	
	/** 静态图片osd */
	private List<PictureVO> staticPics;
	
	/** 动态图片osd */
	private List<PictureVO> dynamicPics;

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
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

	public List<TextVO> getContents() {
		return contents;
	}

	public void setContents(List<TextVO> contents) {
		this.contents = contents;
	}

	public List<PictureVO> getStaticPics() {
		return staticPics;
	}

	public void setStaticPics(List<PictureVO> staticPics) {
		this.staticPics = staticPics;
	}

	public List<PictureVO> getDynamicPics() {
		return dynamicPics;
	}

	public void setDynamicPics(List<PictureVO> dynamicPics) {
		this.dynamicPics = dynamicPics;
	}
}
