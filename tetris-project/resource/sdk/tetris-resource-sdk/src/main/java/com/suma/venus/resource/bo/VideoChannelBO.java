/**
 * 
 */
package com.suma.venus.resource.bo;

/**
 * @author Administrator
 *
 */
public class VideoChannelBO {

//		"channelId":"通道id",
//		"baseType":"通道类型",
//		"codec":"编码格式",
//		"resolution":"分辨率",
//		"bitrate":"码率",
//		"fps":"帧率"

	private String channelId;

	private String baseType;
	
	private String codec;
	
	private String resolution;
	
	private String bitrate;
	
	private String fps;

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the baseType
	 */
	public String getBaseType() {
		return baseType;
	}

	/**
	 * @param baseType the baseType to set
	 */
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	/**
	 * @return the codec
	 */
	public String getCodec() {
		return codec;
	}

	/**
	 * @param codec the codec to set
	 */
	public void setCodec(String codec) {
		this.codec = codec;
	}

	/**
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * @param resolution the resolution to set
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	/**
	 * @return the bitrate
	 */
	public String getBitrate() {
		return bitrate;
	}

	/**
	 * @param bitrate the bitrate to set
	 */
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	/**
	 * @return the fps
	 */
	public String getFps() {
		return fps;
	}

	/**
	 * @param fps the fps to set
	 */
	public void setFps(String fps) {
		this.fps = fps;
	}
	
	
}
