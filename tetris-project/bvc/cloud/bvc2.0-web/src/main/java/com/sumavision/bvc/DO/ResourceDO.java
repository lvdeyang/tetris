package com.sumavision.bvc.DO;

import lombok.Data;

@Data
public class ResourceDO {
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
}
