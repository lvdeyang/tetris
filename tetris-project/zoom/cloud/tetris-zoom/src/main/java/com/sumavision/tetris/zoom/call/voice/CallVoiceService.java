package com.sumavision.tetris.zoom.call.voice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CallVoiceService {

	public CallVoiceVO invite(Long userno) throws Exception{
		return null;
	}
	
}
