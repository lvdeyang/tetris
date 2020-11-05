package com.sumavision.tetris.bvc.business.dispatch.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * 调度返回结果
 * @author zsy
 *
 */
public class DispatchResponseBO {

	/** 开始、更新调度需要返回dispatchId；有错误的调度需要返回错误信息；其它情况可能不给返回 */
	private List<DispatchResponseBodyBO> dispatchResponse = new ArrayList<DispatchResponseBodyBO>();

	public List<DispatchResponseBodyBO> getDispatchResponse() {
		return dispatchResponse;
	}

	public DispatchResponseBO setDispatchResponse(List<DispatchResponseBodyBO> dispatchResponse) {
		this.dispatchResponse = dispatchResponse;
		return this;
	}

}
