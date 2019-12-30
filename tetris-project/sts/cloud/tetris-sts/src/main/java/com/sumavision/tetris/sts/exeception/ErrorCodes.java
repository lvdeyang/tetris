package com.sumavision.tetris.sts.exeception;

/**
 * Created by Lost on 2017/2/8.
 */
public class ErrorCodes {

    public final static String CONNECTION_FAILED = "创建连接失败";
    public final static String CONNECTION_INTERRUPTION = "连接中断";
    public final static String TIME_OUT = "返回超时";
    public final static String RESPONSE_ERROR = "返回错误";
    public final static String SYS_ERR = "系统错误";


    public final static String SLAVE_OPERATION_ERROR="当前为备机，用户不可操作";

    public final static String NAME_CONFLICT = "名称重复";
    public final static String SUBNET_CONFLICT = "子网分组冲突";
    public final static String SUBNET_CONTAIN_DEVICE = "分组下有设备";
    public final static String SUBNET_CONTAIN_SOURCE = "分组下有输入源";

    public final static String DEVICE_CONFLICT = "设备已经添加";
    public final static String NETCARD_ERROR = "网卡获取失败";

    public final static String FUN_UNIT_CONFLICT = "功能单元已经添加";
    public final static String NO_UNIT = "没有可用设备";
    public final static String NO_NETCARD = "没有可用网卡";
    public final static String DEVICE_CONTAIN_TASK = "设备存在任务";
    public final static String DEVICE_DELETE_MAIN = "删除设备为主网关";
    
    public final static String SOURCE_CONTAIN_TASK = "输入源存在任务";
    public final static String SOURCE_BACKUP_EXIST = "输入源在做备源";
    public final static String SOURCE_REFRESHING = "输入源刷新中";
    public final static String SOURCE_REFRESH_FAIL = "刷源失败";

    public final static String QUEUE_BLOCK = "操作队列阻塞";
    public final static String DATABASE_ERR = "数据库异常";
    
    public final static String PROGRAMID_ID_NULL = "节目ID,id为空";
    
    public final static String FTP_URL_ERR = "URL解析错误";
    

    /********Task 相关**********/
    public final static String TASK_ID_NULL = "任务ID为空";
    public final static String TASK_SYS_ERR = "任务分析内部错误";
    public final static String TASK_SQL_ERR = "任务数据库数据异常";
    public final static String TASK_PRO_NOT_FOUND = "节目不存在";
    public final static String TASK_DEVICE_NOT_FOUND = "无可用设备";
    public final static String TASK_INPUB_NOT_FOUND = "无可用输入";
    public final static String TASK_TRANS_AUTH_FULL = "可用设备的转码授权已满";
    public final static String TASK_OUTPUB_NOT_FOUND = "无可用输出";
    public final static String TASK_BACKUP_PRO_NOT_FOUND = "备份节目不存在";
    public final static String TASK_OUTPUT_REPEAT = "输出重复";
    public final static String TASK_VIDEO_PARAM_NOT_FOUND = "视频pid不存在";
    public final static String TASK_MODIFY_ERR = "任务信息修改错误";
    public final static String LIST_MODIFY_ERR = "黑白名单设置错误";
    public final static String TASK_IMPORT_ERR = "任务导入异常";
    public final static String TASK_OUTPUTPORT_ERR = "生成输出端口异常";
    public final static String TASK_COVER_PALYER_ERR= "任务盖播失败";
    public final static String TASK_NOT_ALLOW_COVER_PALYER= "网关任务不能盖播";
    public final static String DEL_COVER_PALYER_ERR= "删除盖播失败";
    public final static String CHANNEL_NOT_FOUND = "无可用授权通道";
    public final static String FTP_ERR = "FTP连接异常";
    public final static String FTP_PIC_ERR = "获取FTP图片错误";
    public final static String SOURCE_NOT_ALLOW_PREVIEW = "单播输入源任务不能预览";
    public final static String BITRATES_QUERY_ERR = "码率查询失败";
    public final static String INPUT_CONFLICT = "接受输入冲突";
    public final static String TASK_DEVICE_NOT_CHOOSE = "所选设备已删除";
    public final static String TASK_PARAM_PARSE_ERROR="任务参数解析错误";
    
    /******Source******/
    public final static String DELETE_SOURCE_GROUP_FAIL = "删除分组失败，分组下存在源";
    public final static String SOURCEGROUP_NAME_EXIST = "输入源分组已存在";
    public final static String SOURCE_URL_EXIST = "输入源已存在";
    public final static String SOURCE_ALTER_ERR = "输入源存在任务，关键信息不能修改";
    public final static String SOURCE_NAME_ERR = "输入源名称填写错误";
    public final static String SOURCE_MATFILE_USED = "存在任务在使用垫播文件，不能切换";
    public final static String SOURCE_SDI_DELETE_ERR = "SDI采集卡源不能删除";
    public final static String SOURCE_BACKUP_NOT_EXIST = "无备份源，切换失败";
    public final static String SOURCE_BACKUP_ABNORMAL = "备份源状态异常，切换失败";
    public final static String SOURCE_MAIN_ABNORMAL = "主源状态异常，切换失败";
    public final static String SOURCE_MAT_NOT_EXIST = "未配置垫播文件，切换失败";
    public final static String SOURCE_STOP = "输入源停止，不允许修改";
    
    /********TaskGroup*********/
    public final static String DELETE_TASK_GROUP_FAIL = "删除分组失败，分组下存在任务";
    public final static String TASKGROUP_NAME_EXIST = "任务分组已存在";
    
    /********数据库备份*********/
    public final static String BACKUP_MYSQL_ERROR = "数据库备份错误";
    public final static String BACKUP_FILE_NOT_FOUND = "数据库备份文件不存在";
    public final static String LOAD_MYSQL_ERROR = "数据库还原失败";
    public final static String LOAD_DEVICE_RESET_ERR = "设备重置异常";
    public final static String IMPORT_Mysql_ERROR = "数据库导入失败";
    public final static String SYNC_MYSQL_ERROR = "数据库同步恢复失败";
    public final static String RESTORE_MYSQL_ERROR = "数据库还原失败";
    
    public final static String PRESS_QUEUE_ERR = "压入主队列异常";
    
    /********EMR信息*********/
    public final static String IP_CONFLICT = "IP重复";
    
    /********设备解析*********/
    public static final String ANALYTIC_FAILURE = "设备解析失败";
    public static final String CHECK_FAILURE = "设备校验失败";
    public static final String NON_EXISTENT = "不存在";
    public static final String CHANNEL_AUTH_VIDEO_TYPE_ERROR = "设备授权编码格式异常";
    public static final String UPDATE_TRANS_AUTH_MATCH_ERROR= "新授权无法涵盖已用路数";


    /*******主备********/
    public static final String CANNOT_FIND_MASTER_DEVICE = "找不到主设备";
    public static final String CANNOT_FIND_STANDBY_DEVICE = "找不到主设备";
    public static final String STANDBY_DEVICENODE_SIZE_NOTENOUGH = "备机节点数不够";
    public static final String STANDBY_DEVICE_ENCAP_ABNORMAL = "备机缺少网关或网关异常";
    public static final String STANDBY_DEVICE_TRANS_ABNORMAL = "备机缺少转码或转码异常";
    public static final String STANDBY_DEVICE_ANYFUN_ABNORMAL = "备机缺少功能单元或功能单元异常";
    public static final String STANDBY_NETCARD_ERROR = "备机网卡异常";

    /**文件校验**/
    public static final String FILE_FORMAT_NOT_SUPPORT="文件格式不支持";

    public final static String DEVICE_NODE_NULL="找不到设备节点";//todo 未国际化
    public final static String FUNUNIT_OFFLINE="功能单元离线";//todo 未国际化
    public final static String FUNUNIT_NULL="找不到功能单元";//todo 未国际化
    public final static String OUTPUT_NOT_MATCH="输出节点不匹配";
    public final static String TASK_NOT_MATCH="任务节点不匹配";
    public final static String FUNUNIT_NOT_INPUT="功能单元无对应输入";
    public final static String FUNUNIT_NOT_TASK="功能单元无对应任务";
    public final static String FUNUNIT_NOT_OUTPUT="功能单元无对应输出";
    public final static String LOCAL_NOT_INPUT="本地无对应输入";
    public final static String LOCAL_NOT_TASK="本地无对应任务";
    public final static String LOCAL_NOT_OUTPUT="本地无对应输出";
    public final static String REDUNDANT_INPUT="冗余的输入";

    public final static String SYSTEM_INTERRUPTED="系统中断";
    public final static String MODIFY_IP_FAIL="切换IP失败";
    public final static String CANNOT_FIND_STANDBY_NETCARD="找不到与主输出网卡同一分组的从网卡";
    public final static String DOWN_TO_SLAVE_FAIL="主备切换失败";
    public final static String STANDBY_CRETE_TASK_ERROR="备机创建任务失败";
    public final static String TASK_OUTTIME="任务执行超时";


}
