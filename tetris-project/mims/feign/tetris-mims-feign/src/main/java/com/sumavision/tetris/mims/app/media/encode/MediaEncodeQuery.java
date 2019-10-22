package com.sumavision.tetris.mims.app.media.encode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaEncodeQuery {
	@Autowired
	private MediaEncodeFeign mediaEncodeFeign;
	
	public String queryKey() throws Exception{
		return JsonBodyResponseParser.parseObject(mediaEncodeFeign.queryKey(), String.class);
	}
}
