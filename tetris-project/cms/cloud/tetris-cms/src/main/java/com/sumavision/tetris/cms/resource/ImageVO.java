package com.sumavision.tetris.cms.resource;

/**
 * 测试图片数据<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月20日 下午2:19:06
 */
public class ImageVO {

	private String name;
	
	private String previewUrl;

	public String getName() {
		return name;
	}

	public ImageVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public ImageVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public ImageVO set(Image entity){
		this.setName(entity.getName())
			.setPreviewUrl(entity.getPreviewUrl());
		return this;
	}
	
}
