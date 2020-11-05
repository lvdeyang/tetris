package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 业务层通道类型 
 * @author lvdeyang
 * @date 2018年7月31日 下午7:33:46 
 */
public enum ChannelType {

	VIDEOENCODE1("视频编码1", "VenusVideoIn-1", "encode", "video"),
	VIDEOENCODE2("视频编码2", "VenusVideoIn-2", "encode", "video"),
	VIDEOENCODE3("视频编码3", "VenusVideoIn-3", "encode", "video"),
	VIDEOENCODE4("视频编码4", "VenusVideoIn-4", "encode", "video"),
	VIDEOENCODE5("视频编码5", "VenusVideoIn-5", "encode", "video"),
	VIDEODECODE1("视频解码1", "VenusVideoOut-1", "decode", "video"),
	VIDEODECODE2("视频解码2", "VenusVideoOut-2", "decode", "video"),
	VIDEODECODE3("视频解码3", "VenusVideoOut-3", "decode", "video"),
	VIDEODECODE4("视频解码4", "VenusVideoOut-4", "decode", "video"),
	VIDEODECODE5("视频解码5", "VenusVideoOut-5", "decode", "video"),
	VIDEODECODE6("视频解码6", "VenusVideoOut-6", "decode", "video"),
	VIDEODECODE7("视频解码7", "VenusVideoOut-7", "decode", "video"),
	VIDEODECODE8("视频解码8", "VenusVideoOut-8", "decode", "video"),
	VIDEODECODE9("视频解码9", "VenusVideoOut-9", "decode", "video"),
	VIDEODECODE10("视频解码10", "VenusVideoOut-10", "decode", "video"),
	VIDEODECODE11("视频解码11", "VenusVideoOut-11", "decode", "video"),
	VIDEODECODE12("视频解码12", "VenusVideoOut-12", "decode", "video"),
	VIDEODECODE13("视频解码13", "VenusVideoOut-13", "decode", "video"),
	VIDEODECODE14("视频解码14", "VenusVideoOut-14", "decode", "video"),
	VIDEODECODE15("视频解码15", "VenusVideoOut-15", "decode", "video"),
	VIDEODECODE16("视频解码16", "VenusVideoOut-16", "decode", "video"),
	AUDIOENCODE1("音频编码1", "VenusAudioIn-1", "encode", "audio"),
	AUDIODECODE1("音频解码1", "VenusAudioOut-1", "decode", "audio");
	
	private String name;
	
	private String channelType;
	
	private String encodeType;
	
	private String streamType;
	
	private ChannelType(String name, String channelType, String encodeType, String streamType){
		this.name = name;
		this.channelType = channelType;
		this.encodeType = encodeType;
		this.streamType = streamType;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getChannelType(){
		return this.channelType;
	}
	
	public String getChannelId(){
		return this.channelType.replace("-", "_");
	}
	
	public String getEncodeType(){
		return this.encodeType;
	}
	
	public String getStreamType(){
		return this.streamType;
	}
	
	/**
	 * @Title: 判断是否是视频编码通道 <br/>
	 * @return boolean 
	 */
	public boolean isVideoEncode(){
		return "video".equals(this.getStreamType()) && "encode".equals(this.getEncodeType());
	}
	
	/**
	 * @Title: 判断是否是视频解码通道<br/> 
	 * @return boolean 
	 */
	public boolean isVideoDecode(){
		return "video".equals(this.getStreamType()) && "decode".equals(this.getEncodeType());
	}
	
	/**
	 * @Title: 判断是否是音频编码通道<br/> 
	 * @return boolean 
	 */
	public boolean isAudioEncode(){
		return "audio".equals(this.getStreamType()) && "encode".equals(this.getEncodeType());
	}
	
	/**
	 * @Title: 判断是否是音频解码通道<br/> 
	 * @return boolean 
	 */
	public boolean isAudioDecode(){
		return "audio".equals(this.getStreamType()) && "decode".equals(this.getEncodeType());
	}
	
	/**
	 * @Title: 根据名称获取通道类型<br/> 
	 * @param name 名称
	 * @throws Exception 
	 * @return ChannelType 通道类型
	 */
	public static ChannelType fromName(String name) throws Exception{
		ChannelType[] values = ChannelType.values();
		for(ChannelType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	/**
	 * @Title: 根据资源层提供的类型获取通道类型<br/> 
	 * @param channelType 资源层提供的类型
	 * @throws Exception 
	 * @return ChannelType 通道类型
	 */
	public static ChannelType fromChannelType(String channelType) throws Exception{
		ChannelType[] values = ChannelType.values();
		for(ChannelType value:values){
			if(value.getChannelType().equals(channelType)){
				return value;
			}
		}
		throw new ErrorTypeException("ChannelType", channelType);
	}
	
	/**
	 * 转换通道别名<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月26日 下午4:59:37
	 * @param String channelId 通道id
	 * @return String 通道别名
	 */
	public static String transChannelName(String channelId) throws Exception{
		if(channelId == null) return null;
		return fromChannelType(channelId.replace("_", "-")).getName();
	}
	
}
