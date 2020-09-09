package com.sumavision.tetris.cms.resource;

import java.util.ArrayList;
import java.util.List;

public enum Text {

	TAG("标签", "原创"),
	TITLE("标题", "@所有农民！中央一号文件来了，十大要点必看"),
	PARAGRAPH("段落", "中新经纬客户端2月19日电 中共中央、国务院《关于坚持农业农村优先发展 做好“三农”工作的若干意见》19日晚发布，落款时间为2019年1月3日，即为2019年“中央一号文件”。中央一号文件是中共中央每年发布的第一份文件，今年文件仍聚焦三农工作，同时要求下大力推进农村脱贫，政策“含金量”和信息量相当大。"),
	TIME("时间", "2019-02-19 21:06:15"),
	AUTHOR("作者", "中新经纬");
	
	private String name;
	
	private String content;
	
	public String getName(){
		return this.name;
	}
	
	public String getContent(){
		return this.content;
	}
	
	private Text(String name, String content){
		this.name = name;
		this.content = content;
	}
	
	public static List<TextVO> list(){
		Text[] texts = Text.values();
		List<TextVO> view_texts = new ArrayList<TextVO>();
		for(Text text:texts){
			view_texts.add(new TextVO().set(text));	
		}
		return view_texts;
	}
	
}
