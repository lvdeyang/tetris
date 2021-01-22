/**
 * 
 */
package com.sumavision.tetris.cs.channel;


import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年12月16日 下午2:04:59
 */
/**aac,heaac,heaacv2,mp2,mp3,ac3,eac3,audiopassby*/
/**音频编码类型*/
public enum AudioCodec {
	AAC("AAC"),
	HEAAC("HEAAC"),
	HEAACV2("HEAACV2"),
	MP2("MP2"),
	MP3("MP3"),
	AC3("AC3"),
	EAC3("EAC3"),
	AUDIOPASSBY("AUDIOPASSBY");
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	private AudioCodec(String name){
		this.name=name;
	}
	
	public static AudioCodec fromName(String name) throws Exception{
		AudioCodec[] values = AudioCodec.values();
		for (AudioCodec value : values) {
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
