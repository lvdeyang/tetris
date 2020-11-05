package com.sumavision.tetris.alarm.bo;

import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class AlarmParamBO extends AlarmParamBOBase {

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = Calendar.getInstance().getTime();

	private String type;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
