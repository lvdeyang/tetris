package com.sumavision.tetris.cms.article;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ArticleType {

	TEXT("文本"),
	AVIDEO("音视频");
	
	private String name;
	
	public String getName(){
		return this.name;
	}
	
	private ArticleType(String name) {
		this.name = name;
	}
	
	/**
	 * 根据文章类型名称获取类型<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午5:17:19
	 * @param name 名称
	 * @return ArticleType 类型
	 */
	public static ArticleType fromName(String name) throws Exception{
		ArticleType[] values = ArticleType.values();
		for(ArticleType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
