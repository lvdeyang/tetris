package com.sumavision.bvc.device.command.basic.forward;

import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.tetris.commons.util.date.DateUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: ForwardReturnBO 
* @Description: 主席开始设备转发、文件转发的请求返回值；查询指挥内转发的返回值
* @author zsy
* @date 2019年11月15日 下午1:14:15 
*
 */
@Getter
@Setter
public class ForwardReturnBO {

	private String id;
	
	private String time = "";
	
	private String srcType = "";
	
	private String srcInfo = "";
	
	private String dstInstitutionInfo = "";
	
	private String dstUserInfo = "";
	
	private String status = "";
	
	public ForwardReturnBO setByDevice(CommandGroupForwardDemandPO demand){
		this.id = demand.getId().toString();
		this.time = DateUtil.format(demand.getUpdateTime(), DateUtil.dateTimePattern);
		this.srcType = demand.getDemandType().getCode();
		this.srcInfo = demand.getVideoBundleName();
		this.dstInstitutionInfo = "";
		this.dstUserInfo = demand.getDstUserName();
		this.status = demand.getExecuteStatus().getCode();
		return this;
	}
	
	public ForwardReturnBO setByFile(CommandGroupForwardDemandPO demand){
		this.id = demand.getId().toString();
		this.time = DateUtil.format(demand.getUpdateTime(), DateUtil.dateTimePattern);
		this.srcType = demand.getDemandType().getCode();
		this.srcInfo = demand.getResourceName();
		this.dstInstitutionInfo = "";
		this.dstUserInfo = demand.getDstUserName();
		this.status = demand.getExecuteStatus().getCode();
		return this;
	}

}
