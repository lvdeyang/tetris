package com.sumavision.tetris.mims.app.media;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaQuery {
	@Autowired
	private MediaFeign mediaFeign;
	
	public List<JSONObject> queryByCondition(Long id, String name, String type, String startTime, String endTime, Long tagId) throws Exception{
		List<JSONObject> medias = JsonBodyResponseParser.parseArray(mediaFeign.queryByCondition(id, name, type, startTime, endTime, tagId), JSONObject.class);
		return medias;
	}
}
