package com.sumavision.tetris.mims.app.media.encode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AudioFileEncodeQuery {
	@Autowired
	private AudioFileEncodeDAO audioFileEncodeDAO;
	
	public AudioFileEncodePO queryFromMediaId(Long mediaId) throws Exception {
		return audioFileEncodeDAO.findByMediaId(mediaId);
	}
	
	public List<AudioFileEncodePO> queryFromMediaIds(List<Long> mediaIds) throws Exception {
		List<AudioFileEncodePO> encodePOs = audioFileEncodeDAO.findByMediaIdIn(mediaIds);
		
		return encodePOs;
	}
}
