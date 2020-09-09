package com.sumavision.tetris.mims.app.material;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 素材类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 上午9:46:38
 */
public enum MaterialType {

	FOLDER("文件夹", "icon-folder-close", new String[]{"font-size:20px; position:relative; top:2px; color:#FFD659;"}, new String[]{}),
	PICTURE("图片", "icon-picture", new String[]{"font-size:16px; position:relative; top:1px;"}, new String[]{"image/jpeg", "image/png", "image/gif"}),
	VIDEO("视频", "icon-film", new String[]{"font-size:15px; position:relative; left:1px;"}, new String[]{"video/mp4"}),
	AUDIO("音频", "icon-music", new String[]{"font-size:18px; position:relative; top:1px;"}, new String[]{"audio/mpeg"}),
	ARTICLE("文章", "icon-book", new String[]{"font-size:17px; position:relative; top:1px; left:1px;"}, new String[]{}),
	TEXT("文本", "icon-file-alt", new String[]{"font-size:17px; position:relative; left:2px; top:1px;"}, new String[]{"text/plain"});
	
	/** 名称 */
	private String name;
	
	/** 图标 */
	private String icon;
	
	/** 图标微调 */
	private String[] style;
	
	/** 映射媒体类型 */
	private String[] mimetypes;
	
	private MaterialType(String name, String icon, String[] style, String[] mimetypes) {
		this.name = name;
		this.icon = icon;
		this.style = style;
		this.mimetypes = mimetypes;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getIcon(){
		return this.icon;
	}
	
	public String[] getStyle(){
		return this.style;
	}
	
	public String[] getMimetypes(){
		return this.mimetypes;
	}
	
	public static MaterialType fromName(String name) throws Exception{
		MaterialType[] values = MaterialType.values();
		for(MaterialType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static MaterialType fromMimetype(String mimetype) throws Exception{
		MaterialType[] values = MaterialType.values();
		for(MaterialType value:values){
			String[] mimetypes = value.getMimetypes();
			for(String type:mimetypes){
				if(type.equals(mimetype)){
					return value;
				}
			}
		}
		throw new ErrorTypeException("mimetype", mimetype);
	}
	
}
