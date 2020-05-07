package com.sumavision.tetris.mims.app.media.picture;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaPictureService {

	@Autowired
	private MediaPictureFeign mediaPictureFeign;
	
	public List<MediaPictureVO> remove(List<Long> ids) throws Exception {
		return JsonBodyResponseParser.parseArray(mediaPictureFeign.remove(JSONArray.toJSONString(ids)), MediaPictureVO.class);
	}
}
