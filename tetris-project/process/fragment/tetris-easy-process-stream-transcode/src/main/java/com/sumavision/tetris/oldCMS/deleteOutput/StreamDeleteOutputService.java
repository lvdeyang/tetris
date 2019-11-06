package com.sumavision.tetris.oldCMS.deleteOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.oldCMS.StreamAdapter;
import com.sumavision.tetris.oldCMS.OldCMSRequestType;
import com.sumavision.tetris.streamTranscoding.api.server.OutParamVO;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.user.UserVO;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamDeleteOutputService {
	@Autowired
	private StreamAdapter streamAdapter;
	
	/**
	 * 流转码任务删除输出(请求旧的流转码)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:49:03
	 * @param user 用户信息
	 * @param messageId 任务Id(对应uniqId)
	 * @param outParamVO 输出信息
	 */
	public void delete(UserVO user, Long messageId, OutParamVO outParamVO) throws Exception {
		JSONObject requestJSON = new JSONObject();
		
		requestJSON.put("uniqId", messageId);
		requestJSON.put("outputParam", outParamVO);
		
		JSONObject responseJSON = streamAdapter.deleteOutput(requestJSON);
		
		if (!responseJSON.get("errMsg").equals("success")) {
			throw new HttpRequestErrorException(OldCMSRequestType.DELETE_OUTPUT.getAction());
		}
	}
}
