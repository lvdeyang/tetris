package com.sumavision.tetris.easy.process.media.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaEditorService {
	@Autowired
	private MediaEditorFeign mediaEditorFeign;
	
	public String start(String transcodeJob,String param, String name, Long folderId, String tags) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaEditorFeign.start(transcodeJob, param, name, folderId, tags), String.class);
	}
}
