package com.sumavision.bvc.BO;

public class ChannelStatusBody {

	/**
	 * 是否成功
	 */
	private boolean result;
	
	/**
	 * 操作顺序码,result为true时有效
	 */
	private int operateIndex;
	
	/**
	 * 操作计数
	 */
	private int operateCount = 0;

	public boolean isResult() {
		return result;
	}

	public ChannelStatusBody setResult(boolean result) {
		this.result = result;
		return this;
	}
			
	public int getOperateIndex() {
		return operateIndex;
	}

	public void setOperateIndex(int operateIndex) {
		this.operateIndex = operateIndex;
	}

	public int getOperateCount() {
		return operateCount;
	}

	public void setOperateCount(int operateCount) {
		this.operateCount = operateCount;
	}

	public ChannelStatusBody(int operateIndex){
		this.result = true;
		this.operateIndex = operateIndex;
	}

	public ChannelStatusBody(int operateIndex, int operateCount){
		this.result = true;
		this.operateIndex = operateIndex;
		this.operateCount = operateCount;
	}

	public ChannelStatusBody(boolean result){
		this.result = result;
	}
	
	public ChannelStatusBody(){
		
	}
	
}
