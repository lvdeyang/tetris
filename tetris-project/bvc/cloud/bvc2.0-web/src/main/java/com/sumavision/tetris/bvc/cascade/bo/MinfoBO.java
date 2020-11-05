package com.sumavision.tetris.bvc.cascade.bo;

/**
 * 组成员信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午1:50:19
 */
public class MinfoBO {

	/** 成员id 设备|用户号码 */
	private String mid;
	
	/** 成员名称 */
	private String mname;
	
	/** 成员类型，en表示编码设备，ende表示编解码设备,usr表示用户 */
	private String mtype;
	
	/** 成员状态，1表示正在业务、2表示暂时离开、3表示已退出 */
	private String mstatus;
	
	/** 指挥：上级id，会议：主席id， 如果当前成员是主席，没有pid字段*/
	private String pid;

	public String getMid() {
		return mid;
	}

	public MinfoBO setMid(String mid) {
		this.mid = mid;
		return this;
	}

	public String getMname() {
		return mname;
	}

	public MinfoBO setMname(String mname) {
		this.mname = mname;
		return this;
	}

	public String getMtype() {
		return mtype;
	}

	public MinfoBO setMtype(String mtype) {
		this.mtype = mtype;
		return this;
	}

	public String getMstatus() {
		return mstatus;
	}

	public MinfoBO setMstatus(String mstatus) {
		this.mstatus = mstatus;
		return this;
	}

	public String getPid() {
		return pid;
	}

	public MinfoBO setPid(String pid) {
		this.pid = pid;
		return this;
	}
	
}
