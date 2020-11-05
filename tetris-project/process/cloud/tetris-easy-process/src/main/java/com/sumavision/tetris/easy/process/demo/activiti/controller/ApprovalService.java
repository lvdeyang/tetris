package com.sumavision.tetris.easy.process.demo.activiti.controller;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

/**
 * Service任务节点<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年10月30日 下午5:27:03
 */
@Component
public class ApprovalService {

	/**
	 * 经理内部接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:27:18
	 * @param execution 当前流程实例
	 * @param time 会议室申请占用时间
	 */
	public void managerInternalApproval(DelegateExecution execution, float time){
		System.out.println("经理内部接口，申请时间：" + time);
	}
	
	/**
	 * 经理同步接口<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月25日 上午11:50:20
	 * @param execution
	 * @param time
	 */
	public void managerSynchronousApproval(DelegateExecution execution, float time){
		System.out.println("经理同步接口，申请时间：" + time);
	}
	
	/**
	 * ceo异步接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月30日 下午5:27:18
	 * @param execution 当前流程实例
	 * @param time 会议室申请占用时间
	 */
	public void ceoAsynchronousApproval(DelegateExecution execution, int time){
		System.out.println("ceo异步接口，申请时间：" + time);
	}
	
}
