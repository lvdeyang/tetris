package com.sumavision.tetris.cms.resource;

/**
 * 测试文本数据<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月20日 下午2:18:47
 */
public class TextVO {

	private String name;
	
	private String content;

	public String getName() {
		return name;
	}

	public TextVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getContent() {
		return content;
	}

	public TextVO setContent(String content) {
		this.content = content;
		return this;
	}
	
	public TextVO set(Text entity){
		this.setName(entity.getName())
			.setContent(entity.getContent());
		return this;
	}
	
}
