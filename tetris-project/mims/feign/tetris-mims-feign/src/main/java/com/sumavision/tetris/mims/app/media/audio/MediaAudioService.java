package com.sumavision.tetris.mims.app.media.audio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaAudioService {
	
	@Autowired
	private MediaAudioFeign mediaAudioFeign;

	public MediaAudioVO add(
			UserVO user,
			String name,
			String fileName,
			Long size,
			String folderType,
			String mimeType,
			String uploadTempPath) throws Exception{
		
		return JsonBodyResponseParser.parseObject(mediaAudioFeign.add(user.getUuid(), user.getNickname(), user.getGroupId(), name, fileName, size, folderType, mimeType, uploadTempPath), MediaAudioVO.class);
	}
	
}
