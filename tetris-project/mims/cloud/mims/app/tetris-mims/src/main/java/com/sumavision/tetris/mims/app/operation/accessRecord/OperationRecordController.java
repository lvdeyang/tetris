package com.sumavision.tetris.mims.app.operation.accessRecord;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/operation/access/record")
public class OperationRecordController {
	@Autowired
	private OperationRecordQuery operationRecordQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 根据用户id获取点播记录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午4:49:42
	 * @param Long userId 用户id
	 * @return List<OperationRecordVO> 记录数组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/{userId}")
	public Object getUserRecord(@PathVariable Long userId, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return operationRecordQuery.queryByUserId(userId);
	}
}
