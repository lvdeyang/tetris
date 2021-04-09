package com.sumavision.bvc.command.group.user.layout.scheme;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 播放器分屏布局
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum PlayerSplitLayout {

	SPLIT_1("一分屏", 1, 0),
	SPLIT_4("四分屏", 4, 1),
	SPLIT_6("六分屏", 6, 2),
	SPLIT_9("九分屏", 9, 3),
	SPLIT_16("十六分屏", 16, 4),
	SPLIT_2("二分屏", 2, 5),
	SPLIT_12("十二分屏", 12, 6),
	SPLIT_13("十三分屏", 13, 7),
	SPLIT_8("八分屏", 8, 8),
	SPLIT_10("十分屏", 10, 9),
	SPLIT_10zsys("左上右上十分屏", 10, 10),
	SPLIT_13c("中心十三分屏", 13, 11),
	SPLIT_4z("左四分屏", 4, 12),
	SPLIT_6zsys("左上右上六分屏", 6, 13),
	SPLIT_9zs("左上九分屏", 9, 14),
	SPLIT_9cs("中上九分屏", 9, 15);
//	CUSTOM("自定义")
	
	private String name;
	
	private int playerCount;
	
	private int id;
	
	private PlayerSplitLayout(String name, int playerCount, int id){
		this.name = name;
		this.playerCount = playerCount;
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}

	public int getPlayerCount() {
		return playerCount;
	}
	
	/**
	 * @Title: 根据名称获取会议配置类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ConfigType 会议配置类型
	 */
	public static PlayerSplitLayout fromName(String name) throws Exception{
		PlayerSplitLayout[] values = PlayerSplitLayout.values();
		for(PlayerSplitLayout value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static PlayerSplitLayout fromId(int id) throws Exception{
		PlayerSplitLayout[] values = PlayerSplitLayout.values();
		for(PlayerSplitLayout value:values){
			if(value.getId() == id){
				return value;
			}
		}
		throw new ErrorTypeException("id", id);
	}
	
	public static PlayerSplitLayout fromPlayerCount(int playerCount) throws Exception{
		PlayerSplitLayout[] values = PlayerSplitLayout.values();
		for(PlayerSplitLayout value:values){
			if(value.getPlayerCount() == playerCount){
				return value;
			}
		}
		throw new ErrorTypeException("playerCount", playerCount);
	}
}
