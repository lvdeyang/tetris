package com.sumavision.tetris.sts.common;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.sts.common.OptConstant.OptType;
import com.sumavision.tetris.sts.exeception.CommonException;


/**
 * 压入总线程的统一消息队列结构，任务操作和任务迁移等都继承此类，通过不同优先级实现先后控制
 * @author gaofeng
 *
 */
public abstract class CommonOpt implements Comparable<CommonOpt>{

	/**
	 * 优先级，下面的compare规则是数字越小，优先级越高
	 */
	protected int priority;
	
	protected OptType optType;
	
	protected Long optId;
	
	/**
	 * 创建时间，同一优先级的消息，创建时间越早越优先
	 */
	protected long createTime;
	
//	private AlarmReportToCmpUtil alarmReportToCmpUtil;

	public abstract void execute() throws CommonException;
	
	public CommonOpt(){
		this(OptType.OTHER, 0l);
//		alarmReportToCmpUtil = SpringBeanFactory.getBean(AlarmReportToCmpUtil.class);
	}
	
	public CommonOpt(OptType optType,Long optId){
		setCreateTime(new Date().getTime());
		setOptType(optType);
		setOptId(optId);
//		alarmReportToCmpUtil = SpringBeanFactory.getBean(AlarmReportToCmpUtil.class);
	}
	
	/**
	 * 操作完成后给前台推送的方法
	 * @param json
	 */
	public void sendToFront(JSONObject json){
		
	}
			
	@Override
	public int compareTo(CommonOpt o) {
		// TODO Auto-generated method stub
		if(this.priority < o.priority){
			return -1;
		}else if(this.priority == o.priority){
			return this.createTime < o.createTime ? -1 : 1;
		}else{
			return 1;
		}
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public OptType getOptType() {
		return optType;
	}

	public void setOptType(OptType optType) {
		this.optType = optType;
	}

	public Long getOptId() {
		return optId;
	}

	public void setOptId(Long optId) {
		this.optId = optId;
	}

}
