/**
 * 
 */
package com.suma.venus.resource.bo;

/**
 * @author Administrator
 *
 */
public class AudioChannelBO {

//		"channelId":"通道id",
//		"baseType":"通道类型",
//		"codec":"编码格式",
//		"samplateRate":"采样率",
//		"bitrate":"码率"

	private String channelId;

	private String baseType;
	
	private String codec;
	
	private String samplateRate;
	
	private String bitrate;

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
	 * @return the samplateRate
	 */
	public String getSamplateRate() {
		return samplateRate;
	}

	/**
	 * @param samplateRate the samplateRate to set
	 */
	public void setSamplateRate(String samplateRate) {
		this.samplateRate = samplateRate;
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
	
	
}
