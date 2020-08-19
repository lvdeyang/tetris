package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ChannelType {
	
	VIDEOENCODE1("视频编码1", "VenusVideoIn_1", 5010l, "video", "encode"),
	VIDEOENCODE2("视频编码2", "VenusVideoIn_2", 5020l, "video", "encode"),
	VIDEOENCODE3("视频编码3", "VenusVideoIn_3", 5030l, "video", "encode"),
	VIDEOENCODE4("视频编码4", "VenusVideoIn_4", 5040l, "video", "encode"),
	VIDEOENCODE5("视频编码5", "VenusVideoIn_5", 5050l, "video", "encode"),
	VIDEODECODE1("视频解码1", "VenusVideoOut_1", 4000l, "video", "decode"),
	VIDEODECODE2("视频解码2", "VenusVideoOut_2", 5000l, "video", "decode"),
	VIDEODECODE3("视频解码3", "VenusVideoOut_3", 6000l, "video", "decode"),
	VIDEODECODE4("视频解码4", "VenusVideoOut_4", 7000l, "video", "decode"),
	VIDEODECODE5("视频解码5", "VenusVideoOut_5", 8000l, "video", "decode"),
	AUDIOENCODE1("音频编码1", "VenusAudioIn_1", 6010l, "audio", "encode"),
	AUDIODECODE1("音频解码1", "VenusAudioOut_1", 4002l, "audio", "decode");
	
	private String name;
	
	private String type;
	
	private Long port;
	
	private String streamType;
	
	private String rateType;
		
	private ChannelType(String name, String type, Long port, String streamType, String rateType){
		this.name = name;
		this.type = type;
		this.port = port;
		this.streamType = streamType;
		this.rateType = rateType;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Long getPort() {
		return port;
	}
	
	public String getStreamType() {
		return streamType;
	}
	
	public String getRateType() {
		return rateType;
	}
	
	/**
	 * @Title: 判断是否是视频通道 <br/>
	 * @return boolean 
	 */
	public boolean isVideo(){
		return "video".equals(this.getStreamType());
	}
	
	/**
	 * @Title: 判断是否是音频通道 <br/>
	 * @return boolean 
	 */
	public boolean isAudio(){
		return "audio".equals(this.getStreamType());
	}
	
	public boolean isDecode(){
		return "decode".equals(this.getRateType());
	}
	
	public boolean isEncode(){
		return "encode".equals(this.getRateType());
	}
	
	public static final ChannelType fromType(String type) throws Exception{
		ChannelType[] values = ChannelType.values();
		for(ChannelType value: values){
			if(value.getType().equals(type)){
				return value;
			}
		}
		throw new ErrorTypeException("type", type);
	}
	
	public static final ChannelType fromPort(Long port) throws Exception{
		ChannelType[] values = ChannelType.values();
		for(ChannelType value: values){
			if(value.getPort().equals(port)){
				return value;
			}
		}
		throw new ErrorTypeException("port", port);
	}
}
