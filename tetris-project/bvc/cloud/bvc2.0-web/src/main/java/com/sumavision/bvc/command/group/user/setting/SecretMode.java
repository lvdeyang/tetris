package com.sumavision.bvc.command.group.user.setting;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 
* @ClassName: SecretMode
* @Description: 专向指挥模式
* @author zsy
* @date 2019年11月29日 上午10:15:06 
*
 */
public enum SecretMode {
	
	NO_AUDIO("隐音频专向", "noAudio"),
	NO_AUDIO_VIDEO("隐音视频专向", "noAudioAndVideo");
	
	private String name;
	
	private String code;
	
	private SecretMode(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName(){
		return this.name;
	}
	
	public String getCode() {
		return code;
	}

	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static SecretMode fromName(String name) throws Exception{
		SecretMode[] values = SecretMode.values();
		for(SecretMode value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

	/**
	 * @Title: 根据code获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static SecretMode fromCode(String code) throws Exception{
		SecretMode[] values = SecretMode.values();
		for(SecretMode value:values){
			if(value.getCode().equals(code)){
				return value;
			}
		}
		throw new ErrorTypeException("code", code);
	}
	
}
