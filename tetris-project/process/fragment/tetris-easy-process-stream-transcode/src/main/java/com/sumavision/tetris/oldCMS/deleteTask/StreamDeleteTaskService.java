package com.sumavision.tetris.oldCMS.deleteTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.oldCMS.StreamAdapter;
import com.sumavision.tetris.oldCMS.OldCMSRequestType;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamDeleteTaskService {
	
	@Autowired
	private StreamAdapter streamAdapter;
	
	public void delete(UserVO userVO, Long messageId) throws Exception {
		JSONObject requestJSON = new JSONObject();
		
		requestJSON.put("uniqId", messageId);
		
		JSONObject responseJSON = streamAdapter.deleteTask(requestJSON);
		
		if (!responseJSON.get("errMsg").equals("success")) {
			throw new HttpRequestErrorException(OldCMSRequestType.DELETE_TASK.getAction());
		}
	}
}
