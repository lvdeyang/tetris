package com.suma.venus.alarmoprlog.service.oprlog.vo;

public class QueryOprlogVO extends OprlogVO {

	private String queryTimeStart;

	private String queryTimeEnd;

	private Integer pageIndex;

	private Integer pageSize;

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getQueryTimeStart() {
		return queryTimeStart;
	}

	public void setQueryTimeStart(String queryTimeStart) {
		this.queryTimeStart = queryTimeStart;
	}

	public String getQueryTimeEnd() {
		return queryTimeEnd;
	}

	public void setQueryTimeEnd(String queryTimeEnd) {
		this.queryTimeEnd = queryTimeEnd;
	}

}
