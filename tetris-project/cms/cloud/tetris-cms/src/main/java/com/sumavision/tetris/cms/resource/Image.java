package com.sumavision.tetris.cms.resource;

import java.util.ArrayList;
import java.util.List;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public enum Image {

	_190x124("190x124", "190x124.jpg"),
	_400x618("400x618", "400x618.jpg"),
	_600x550("600x550", "600x550.jpg"),
	_640x272("640x272", "640x272.jpg"),
	_640x624("640x624", "640x624.jpg"),
	_640x875("640x875", "640x875.jpg"),
	_980x653("980x653", "980x653.jpg");
	
	private static final String BASEFOLDER = "cms/resource/image/";
	
	private String name;
	
	private String previewUrl;
	
	public String getName(){
		return this.name;
	}
	
	public String getPreviewUrl(){
		return this.previewUrl;
	}
	
	private Image(String name, String previewUrl){
		this.name = name;
		this.previewUrl = new StringBufferWrapper().append(BASEFOLDER).append(previewUrl).toString();
	}
	
	public static List<ImageVO> list() throws Exception{
		Image[] images = Image.values();
		List<ImageVO> view_images = new ArrayList<ImageVO>();
		for(Image image:images){
			view_images.add(new ImageVO().set(image));
		}
		return view_images;
	}
	
}
