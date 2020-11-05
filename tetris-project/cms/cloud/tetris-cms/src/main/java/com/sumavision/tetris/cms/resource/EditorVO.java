package com.sumavision.tetris.cms.resource;

public class EditorVO {

	private String key;
	
	private String name;
	
	private String icon;
	
	private String style;

	public String getKey() {
		return key;
	}

	public EditorVO setKey(String key) {
		this.key = key;
		return this;
	}

	public String getName() {
		return name;
	}

	public EditorVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public EditorVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public EditorVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public EditorVO set(Editor entity){
		this.setKey(entity.toString())
			.setName(entity.getName())
			.setIcon(entity.getIcon())
			.setStyle(entity.getStyle());
		return this;
	}
	
}
