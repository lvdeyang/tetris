package com.suma.application.ldap.base.contants;
/**
 * <P> 公共的常量类
 *     1.名称区别于Platform所以加ZK
 *     2.因原常量类太多记不住,增加此类
 * ClassName: ZKCommContants 
 * @info 	: ZBX 2015年10月18日
 */
public interface ZKCommContants {
	// 1. 公用  二互斥
	public final static String STRING_BOOLEAN_YES = "yes"; 
	public final static String STRING_BOOLEAN_NO = "no"; 
	
	// 2. 站点类型
	public final static String SITE_TYPE_COMMAND = "command";
	public final static String SITE_TYPE_MEETING = "meeting"; 
	public final static String SITE_TYPE_COMMAND_MEETING = "command_meeting";

	// 3. 会话创建者
	public final static String SESSION_CREATOR = "yes";

	// 3. 成员的数据库状态
	public final static String DB_STATUS_ADD = "add";
	public final static String DB_STATUS_DEL = "del";
	public final static String DB_STATUS_SAVE = "save";
	
	// 4. 会议模式
    public final static String MEETING_MODE_DISCUSS="discuss";
    public final static String MEETING_MODE_SIMPLE="simple";
    
    // 5. 成员角色	- SiteMem
    public final static String ROLE_CHAIRMAN = "chairman";
    public final static String ROLE_ORDINARY = "ordinary";
    public final static String ROLE_MEETINGTALKER = "meetingtalker";
    
    // 6. 待办事项类型
    public final static String ITEM_TYPE_APPLY_SPEAK  = "apply_speak";	// 发言申请
    public final static String ITEM_TYPE_STOP_SPEAK  = "stop_speak";    //终止发言
    public final static String ITEM_TYPE_INVIATR_SESS = "inviate_sess";	// 会议邀请
    public final static String ITEM_TYPE_APPLY_TRANS  = "apply_trans";	// 转发申请
    public final static String ITEM_TYPE_CURR_TRANS   = "curr_trans";	// 当前转发
    
    public final static String ITEM_TYPE_GROUP_SPECIAL  = "group_special";  // 对专向小组有权限的人的待办事项：   用于删除、展示小组等
    public final static String ITEM_TYPE_GROUP_REPLACE  = "group_replace";  // 对接替小组有权限的人的待办事项：   用于删除、展示小组等
    public final static String ITEM_TYPE_GROUP_SKIP     = "group_skip";  	// 对越级小组有权限的人的待办事项：   用于删除、展示小组等
    public final static String ITEM_TYPE_GROUP_AUTH     = "group_auth";  	// 对授权小组有权限的人的待办事项：   用于删除、展示小组等
    public final static String ITEM_TYPE_GROUP_TEAM     = "group_team";  	// 对协同小组有权限的人的待办事项：   用于删除、展示小组等
    
    
    // 6.1 
    public final static String ITEM_CURR_TRANS_RECIVE   = "trans_recive";	// 当前转发  接收
    public final static String ITEM_CURR_TRANS_STOP     = "trans_stop";		// 当前转发  不接收
    public final static String ITEM_CURR_TRANS_DELETE   = "trans_delete";	// 当前转发  不再通知
    
    // 7. 会话状态
    public final static String SESSION_STATUS_START = "start";
    public final static String SESSION_STATUS_PAUSE = "pause";
    public final static String SESSION_STATUS_STOP  = "stop";
    
    // 8. 在线状态
    public final static String STATUS_ONLINE = "online";
    public final static String STATUS_OFFLINE = "offline";
    public final static String STATUS_OFFLINE_MAINTAIN = "offline_maintain";
    
    // 9. 音视频混音
	public final static String AV_MODE_MIX = "mix";
    public final static String AV_MODE_SINGLE = "single";
    
    // 10.指挥小组
	public final static String COMMAND_GROUP_SPECIL = "specilcommand";   	//专向指挥   1
	public final static String COMMAND_GROUP_TEAMWORK = "teamworkcommand";  //协同指挥   2
	public final static String COMMAND_GROUP_AUTHORIZE = "authorizecommand";//授权指挥   3   1 2 3 互斥
	public final static String COMMAND_GROUP_REPLACE = "replacecommand";   	//接替指挥   4   
	public final static String COMMAND_GROUP_SKIP = "skipcommand";   		//越级指挥   5   4 5  与 1 2 3不互斥
	public final static String COMMAND_GROUP_TRANSMIT = "transmitcommand";  //指挥转发   
	public final static String COMMAND_GROUP_TEMPORARY = "temporarycommand";//临时指挥   
	
	public final static String MEETING_COMMAND = "meetingcommand";   		//指挥融合会议
	public final static String MONITOR_COMMAND = "monitorcommand";   		//指挥融合监控
	
	// 11. 会话类型    
	public final static String MONITOR="monitor";
    public final static String COMMAND="command";
    public final static String MEETING="meeting";
    
    // 12. 被转发源类型
    public final static String TRANS_TARGET_TYPE_SITE="site";
    public final static String TRANS_TARGET_TYPE_DEV="dev";
    
    public final static String TRANS_SRC_TYPE_SITE="site";
    public final static String TRANS_SRC_TYPE_DEV="dev";
    
    public final static String TRANS_CURR_TRANS_RECIVE   = "trans_recive";	// 当前转发  接收
    public final static String TRANS_CURR_TRANS_STOP     = "trans_stop";		// 当前转发  不接收
    public final static String TRANS_CURR_TRANS_DELETE   = "trans_delete";	// 当前转发  不再通知
    
    
    public final static String WATCH_TARGET_TYPE_SITE="site";
    public final static String WATCH_TARGET_TYPE_DEV="dev";
    
    public final static String WATCH_SRC_TYPE_SITE="site";
    public final static String WATCH_SRC_TYPE_DEV="dev";
    
    public final static String MONITOR_TARGET_TYPE_SITE="site";
    public final static String MONITOR_TARGET_TYPE_DEV="dev";
    
    public final static String MONITOR_SRC_TYPE_SITE="site";
    public final static String MONITOR_SRC_TYPE_DEV="dev";
    
    
    // 13. 设备状态 (由信令控制单元通知获取)
    public final static String DEV_STATUS_ONLINE="online";
    public final static String DEV_STATUS_OFFLINE="offline";
    
    // 14. 设备占用
    public final static String DEV_USED_NO = "used_no";
    
    public final static String DEV_USED_STATUS_TRY = "used_try";
    public final static String DEV_USED_STATUS_REAL = "used_real";
    
    public final static String DEV_USED_SITEMEM_EN ="sitemem_en";
    public final static String DEV_USED_SITEMEM_DE ="sitemem_de";
    
    public final static String DEV_USED_TRANS_TARGET ="trans_target";
    public final static String DEV_USED_TRANS_SRC ="trans_src";
    
    public final static String DEV_USED_WATCH_TARGET ="watch_target";
    public final static String DEV_USED_WATCH_SRC ="watch_src";
    
    public final static String DEV_USED_MONITOR_TARGET ="monitor_target"; 
    public final static String DEV_USED_MONITOR_SRC ="monitor_src";
    
    // 15   会话录制 是否完成
    public final static String MEDIA_SESS_STATUS_RUN ="run";
    public final static String MEDIA_SESS_STATUS_STOP ="stop";
    
    
    // 16 分屏数目
    public final static int SCREEN_SPLIT_ONE = 1;
    public final static int SCREEN_SPLIT_TWO = 2;
    public final static int SCREEN_SPLIT_FOUR = 4;
    
    // 17 Layout 类型
    public final static String MEMVIDEO_TYPE_AUTO ="auto";
    public final static String MEMVIDEO_TYPE_MANUAL ="manual";
    
    public final static String SCREEN_TYPE_AUTO ="auto";
    public final static String SCREEN_TYPE_MANUAL ="manual";
    
   
    // 18 Long num
    public final static Long ZERO_LONG = 0L;
    public final static Long ONE_LONG = 1L;
    public final static Long TWO_LONG = 2L;
    public final static Long THREE_LONG = 3L;
    public final static Long FOUR_LONG = 4L;
    
    // 19 监控轮询默认的一屏时长
    public final static Long TIME_SPAN_LONG = 180L;
    
    // 20 监控业务类型
    public final static String MONITOR_TYPE_ADD_SCREEN ="addscreen";
    public final static String MONITOR_TYPE_SET_SCREEN ="setscreen";
    public final static String MONITOR_TYPE_SET_SRC ="setsrc";
    public final static String MONITOR_TYPE_SET_SPAN ="setspan";
    public final static String MONITOR_TYPE_PUSH_SCREEN ="pushscreen";
    
    // 20.1 监控业务删除标记
    public final static String MONITOR_TYPE_DEL_DEV ="deldev";
    public final static String MONITOR_TYPE_DEL_SCREEN ="delscreen";
    public final static String MONITOR_TYPE_DEL_SRC ="delsrc";
    
    // 21 手动配置删除
    public final static String WATCH_TYPE_DEL_WATCH ="delwatch";
    public final static String WATCH_TYPE_DEL_SRC ="delsrc";
    
    //超级组织信息
    public final static String SUPER_INST_ID="10001";
    public final static String SUPER_INST_NO="1001";
    public final static String SUPER_INST_NAME="超级组织";
    public final static String SUPER_INST_ORG="0000";
    
    //超级帐号信息
    public final static String SUPER_USER_ID="10001";
    public final static String SUPER_USER_NO="1001";
    public final static String SUPER_USER_LEVEL="1";
    public final static String SUPER_USER_NODE="000";
    public final static String SUPER_USER_TYPE="1";
}
