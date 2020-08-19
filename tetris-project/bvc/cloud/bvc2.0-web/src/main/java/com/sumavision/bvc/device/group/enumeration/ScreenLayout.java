package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 屏幕布局类型<br/> 
 * @author lvdeyang
 * @date 2018年9月18日 下午6:33:32 
 */
public enum ScreenLayout {

	SINGLE("单画面", true),
	REMOTE_LARGE("远端大本地小", true),
	REMOTE_SMARLL("远端小本地大", true),
	PPT_MODE("ppt模式", true),
	SMALL("小屏", false);
	
	private String name;
	
	private boolean visible;

	private ScreenLayout(String name, boolean visible){
		this.name = name;
		this.visible = visible;
	}
	
	public String getName(){
		return this.name;
	}	
	
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @Title: 根据名称获取屏幕布局类型<br/> 
	 * @param name 名称
	 * @return ScreenLayout 屏幕布局类型
	 */
	public static ScreenLayout fromName(String name) throws Exception{
		ScreenLayout[] values = ScreenLayout.values();
		for(ScreenLayout value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
