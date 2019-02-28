package com.sumavision.tetris.cms.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月21日 下午3:06:17
 */
public enum Editor {

	INPUT("文本", "feather-icon-type", ""),
	PARAGRAPH("段落", "feather-icon-file-text", ""),
	IMAGE("图片", "feather-icon-image", ""),
	TIME("时间", "feather-icon-calendar", ""),
	VIDEO("视频", "feather-icon-film", ""),
	AUDIO("音频", "feather-icon-music", ""),
	VIDEO_STREAM("视频流", "feather-icon-video", ""),
	AUDIO_STREAM("音频流", "icon-bullhorn", ""),
	ARRAY_SIMPLE("基本数据列表", "icon-list", ""),
	ARRAY_IMAGE("图片列表", "icon-th", "");
	//ARRAY_OBJECT("复合数据列表", "icon-table", "");
	
	private String name;
	
	private String icon;
	
	private String style;
	
	public String getName(){
		return this.name;
	}
	
	public String getIcon(){
		return this.icon;
	}

	public String getStyle(){
		return this.style;
	}
	
	private Editor(String name, String icon, String style){
		this.name = name;
		this.icon = icon;
		this.style = style;
	}
	
	public static List<EditorVO> list(){
		Editor[] editors = Editor.values();
		List<EditorVO> view_editors = new ArrayList<EditorVO>();
		for(Editor editor:editors){
			view_editors.add(new EditorVO().set(editor));
		}
		return view_editors;
	}
	
}
