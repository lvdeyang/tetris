package com.sumavision.tetris.mims.app.media;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 审核状态<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年7月18日 上午8:35:09
 */
public enum ReviewStatus {

	REVIEW_UPLOAD_WAITING("上传审核中"),
	REVIEW_UPLOAD_REFUSE("上传审核拒绝"),
	REVIEW_EDIT_WAITING("修改审核中"),
	REVIEW_EDIT_REFUSE("修改审核拒绝"),
	REVIEW_DELETE_WAITING("删除审核中"),
	REVIEW_DELETE_REFUSE("删除审核拒绝");
	
	private String name;
	
	private ReviewStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ReviewStatus fromName(String name) throws Exception{
		ReviewStatus[] values = ReviewStatus.values();
		for(ReviewStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
