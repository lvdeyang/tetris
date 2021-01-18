package com.sumavision.tetris.capacity.constant;

/**
 * url常量<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月28日 下午2:35:41
 */
public final class UrlConstant {

	public static final String URL_VERSION = "/v0.0";

	/** 前缀 */
	public static final String URL_PREFIX = "http://";
	
	/** 组合 */
	public static final String URL_COMBINE = "combination";

	/**
	 * 指令队列
	 */
	public static final String URL_QUEUE = "queue";

	/** 输入 */
	public static final String URL_INPUT = "inputs";
	
	/** 修改指定输入 */
	public static final String URL_INPUT_PARAM = "param";
	
	/** 节目 */
	public static final String URL_INPUT_PROGRAM = "programs";
	
	/** 排期 */
	public static final String URL_INPUT_SCHEDULE = "schedule";
	
	public static final String URL_INPUT_PROGRAM_ELEMENTS = "elements";
	
	/** 修改指定节目解码配置 */
	public static final String URL_INPUT_PROGRAM_DECODE = "decode_mode";
	
	/** 获取输入源 */
	public static final String URL_INPUT_ANALYSIS = "analysis";
	
	/** 获取所有任务 */
	public static final String URL_TASK = "tasks";
	
	/** 任务编码 */
	public static final String URL_TASK_ENCODE = "encoders";
	
	/** 任务解码后处理 */
	public static final String URL_TASK_DECODE_PROCESS = "decode_process";
	
	/** 任务源 */
	public static final String URL_TASK_SOURCE = "source";
	
	/** 任务节目切换 */
	public static final String URL_TASK_SOURCE_INDEX = "select_index";
	
	/** 输出 */
	public static final String URL_OUTPUT = "outputs";
	
	/** 全部 */
	public static final String URL_ENTIRETY = "entireties";
	
	/** 授权 */
	public static final String URL_AUTHORIZATION = "authorization";
	
	/** 告警 */
	public static final String URL_ALARM = "alarmurl";
	
	/** 心跳 */
	public static final String URL_HEARTBEAT = "heartbeaturl";
	
	/** 告警列表 */
	public static final String URL_ALARMLIST = "alarmlist";

	/** 硬件平台 */
	public static final String GET_PLATFORM = "platform";

	private UrlConstant() {
	}
}
