package com.sumavision.tetris.capacity.bo.output;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.template.MediaVO;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.application.template.OutputVO;
import com.sumavision.tetris.application.template.ProgramVO;
import com.sumavision.tetris.business.common.Util.IpV4Util;
import com.sumavision.tetris.capacity.bo.input.ProgramBO;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommonTsOutputBO extends BaseTsOutputBO<CommonTsOutputBO>{

	public CommonTsOutputBO setUdp_ts(){
		this.setRate_ctrl("VBR")
			.setBitrate(8000000);
		return this;
	}

	public CommonTsOutputBO() {
	}



}
