package com.sumavision.bvc.BO;

import com.sumavision.bvc.BO.ForwardSetBO.DstBO;

/**
 * 
 * @ClassName:  ForwardDelBo   
 * @Description:删除转发操作BO  
 * @author: 
 * @date:   2018年7月13日 下午4:43:03   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ForwardDelBo {
	
	/**
	 * 任务Id
	 */
	private String taskId;
	
	private DstBO dst;

	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public DstBO getDst() {
		return dst;
	}

	public void setDst(DstBO dst) {
		this.dst = dst;
	}

	@Override
	public String toString() {
		return "ForwardDelBo [taskId=" + taskId + ", dst=" + dst + "]";
	}
	
	

}
