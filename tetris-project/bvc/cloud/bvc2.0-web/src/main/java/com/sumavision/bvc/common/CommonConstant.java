package com.sumavision.bvc.common;

/**
 * 
 * @ClassName:  CommonConstant   
 * @Description:TODO  
 * @author: 
 * @date:   2018年7月20日 下午3:41:41   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class CommonConstant {
	
	//合屏的设备类型(devicemodel)
	public static final String COMBINE_VIDEO = "mixer";

	//混音的设备类型(devicemodel)
	public static final String COMBINE_AUDIO = "mixer";
	
	//MediaMuxout的设备类型(devicemodel)
    public static final String MEDIA_MUX_OUT = "cdn";
    public static final String MEDIA_MUX_OUT_EMR = "emr";
    
    //MediaPush的设备类型(devicemodel)
    public static final String MEDIA_PUSH = "vod";
	
	//写锁
	public static final String WRITE_LOCK = "write";
	
	//读锁
	public static final String READ_LOCK = "read";
	
	//普通的视频编码channel,用于普通的编码发送
	public static final String SIMPLE_VIDEO_ENCODE = "simple_video_encode";
	//普通的视频解码channel,用于普通的解码上屏
	public static final String SIMPLE_VIDEO_DECODE = "simple_video_decode";
	//普通的音频编码channel,用于普通的编码发送
	public static final String SIMPLE_AUDIO_ENCODE = "simple_audio_encode";
	//普通的音频解码channel,用于普通的解码播放
	public static final String SIMPLE_AUDIO_DECODE = "simple_audio_decode";
	//媒体处理的视频编码channel,需要源信息，编码内容对应当前媒体处理的解码channel集合，以做合屏或转码操作
	public static final String MEDIAPROC_VIDEO_ENCODE = "VenusVideoMix";
	//媒体处理的音频编码channel,需要源信息，编码内容对应当前媒体处理的解码channel集合，以做混音处理
	public static final String MEDIAPROC_AUDIO_ENCODE = "VenusAudioMix";
	//媒体处理的MediaMux编码channel,以做MediaMux处理
	public static final String MEDIAMUX_ENCODE = "OutConnMediaMuxOutParam";
	//媒体处理的MediaMux编码channel,以做MediaMux处理
	public static final String MEDIAPUSH_ENCODE = "vod_InConnMediaMuxInParam";
	
	
	public static final String OPEN_CHANNEL_RESPONSE = "open_channel_response";
	
	public static final String CLOSE_CHANNEL_RESPONSE = "close_channel_response";
	
	/**
	 * 
	 * @ClassName:  OperateType   
	 * @Description:与资源层、接入层交互的channel操作  
	 * @author: 
	 * @date:   2018年7月30日 下午2:28:52   
	 *     
	 * @Copyright: 2018 Sumavision. All rights reserved. 
	 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
	 */
	public enum OperateType{
		LOCK,
		UNLOCK,
		OPEN,
		MODIFY,
		CLOSE
	}
	
	/**
	 * 
	 * @ClassName:  ChannelType   
	 * @Description:channel类型  
	 * @author: 
	 * @date:   2018年7月30日 下午2:29:18   
	 *     
	 * @Copyright: 2018 Sumavision. All rights reserved. 
	 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
	 */
	public enum ChannelTypeInfo{
		AUDIOENCODE,
		AUDIODECODE,
		VIDEOENCODE,
		VIDEODECODE,
		MEDIAMUXENCODE,
		MEDIAPUSHENCODE
	}
	
	//设备离线的告警码alarmCode
	public static final String DEVICE_OFFLINE = "11010001";
}
