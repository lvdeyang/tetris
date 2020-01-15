
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-ui 上午10:15:46  
* All right reserved.  
*  
*/

package com.sumavision.bvc.PO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @desc: bvc-monitor-ui
 * @author: kpchen
 * @createTime: 2018年6月5日 上午10:15:46
 * @history:
 * @version: v1.0
 */
@Data
@Entity
@Table(name = "Terminal")
public class ResourcePO {
	@Id
	@Column(name = "t_id")
	@GeneratedValue
	private Long id;
	private String terminalAgip; // IP地址
	private Integer terminalAgPort; // 服务器端口
	private String terminalIp; // 终端Ip
	private String terminalName; // 终端名称
	private Integer device; // 设备类型
	private Integer terminalType; // 协议：1.SIP 2.H.323 3.SAT 4.SIP-WL
	private Integer networkSystem; // 网系：1卫星高清 2卫星标清 3会议高清
									// 4会议标清5监控高清6监控标清7地波传播8短波电离层反射9超短波（微波）视距中继10人造卫星中继11对流层散射信道
	private String e164Number; // E164号码
	private String audioFormat; // 音频格式： All G711A G711U G729
	private Integer videoQuality; // 视频质量 0.无处理（off）1.高质量（quality） 2.低延时 （speed）
	private Integer loseRate; // 丢包率0-100
	private Integer videoCapacityId; // 关联视频能力表
	private String username; // 用户名
	private String password; // 密码
	private Integer currentSendRate; // 当前发送速率
	private Integer maxSendRate; // 最大发送速率
	private Integer status; // 设备是否在线 0:离线 1：在线 2：已经调度起来
	private String sendFlag; // 2：not set，1：send,0:doesn't send
	private String recvFlag; // 2：not set，1：recv,0:doesn't recv
	private String enDecodeType; // 0:encoder and decoder, 1:encoder,
									// 2:decoder，不用设置
	private Integer isTelnumber; //
	private String tokenNum; // store all the tokennums;
	private String tourPath; // store all the tourPaths;
	private Integer packetLength; // 媒体包长度
	private Integer flowBitrate; // 流控输出码率 单位：Kb 默认值：0 范围：[0,8192]
	private Integer loseSolution; // 丢帧策略 默认值：0 范围：0(无处理) 1(丢GOP) 2(丢帧)
	private Integer doubleMedia; // 双流开关 默认值：0 范围：0(关闭) 1(打开)
	private Integer doubleMediaRole; // 双流角色类型 默认值：0 范围：0(无效角色) 1(胶片) 2(活动图像)
	private Integer moreSliceFlag; // 多SLICE编码 默认值：1 范围：0(关闭) 1(打开)
	private Integer samePortFlag; // 收发采用同一端口 默认值：0 范围：0(关闭) 1(打开)
	private Integer terminalTrans; // 是否转码：0 透传 1 转码 2 转发
	private String previewTiTleConfig;// 终端预览显示字幕配置
	private String previewTiTle;// 终端预览自定义字幕内容
	private String ptzSupport; // 0:不支持，1：支持ptz操作，2：未知
	private String mediaType;
	private String recvLocalIP;
	private String recvURL;
	private String sendLocalIP;
	private String sendURL;
	private String filePath;
	private Integer circleFlag;
	private Integer maxReTransDalay;
	private String loseRateStrategy;
	private String attachDeviceIp;// 隶属于那个设备
	private String uniqueId;// 终端的唯一不变的id
	private Integer iframeAlarm;
}
