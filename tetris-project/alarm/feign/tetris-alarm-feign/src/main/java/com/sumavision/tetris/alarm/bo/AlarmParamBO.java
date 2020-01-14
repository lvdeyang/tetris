package com.sumavision.tetris.alarm.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class AlarmParamBO extends AlarmParamBOBase {

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = Calendar.getInstance().getTime();

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
