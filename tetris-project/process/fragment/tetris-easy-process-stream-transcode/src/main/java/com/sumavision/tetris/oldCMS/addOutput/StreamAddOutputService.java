package com.sumavision.tetris.oldCMS.addOutput;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.oldCMS.StreamAdapter;
import com.sumavision.tetris.oldCMS.OldCMSRequestType;
import com.sumavision.tetris.streamTranscoding.api.server.OutParamVO;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamAddOutputService {
	@Autowired
	private StreamAdapter streamAdapter;
	
	public void addOutput(UserVO user, Long messageId, List<OutParamVO> outParams) throws Exception {
		JSONObject requestJSON = new JSONObject();
		
		requestJSON.put("uniqId", messageId);
		requestJSON.put("outputParam", outParams);
		
		JSONObject responseJSON = streamAdapter.addOutput(requestJSON);
		
		if (!responseJSON.get("errMsg").equals("success")) {
			throw new HttpRequestErrorException(OldCMSRequestType.ADD_OUTPUT.getAction());
		}
	}
}
