package com.sumavision.tetris.sts.common;

public class OptConstant {
	
	/**
	 * OPT优先级
	 */
	//普通操作
	public static int NORMAL_OPT_PRIORITY = 3;
	//转码与网关的备份操作
	public static int BACKUP_OPT_PRIORITY = 2;
	//微同步，同步
	public static int SYNC_OPT_PRIORITY = 1;
	
	public enum OptType{
		TASK,OTHER
	}
}
