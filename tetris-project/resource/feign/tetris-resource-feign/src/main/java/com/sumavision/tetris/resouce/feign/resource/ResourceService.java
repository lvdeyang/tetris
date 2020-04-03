package com.sumavision.tetris.resouce.feign.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class ResourceService {

	@Autowired
	private ResourceFeign resourceFeign;
	
	/**
	 * 查询用户有权限的用户，终端类型用于显示在线状态<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午8:40:17
	 * @param Long userId 用户id
	 * @param String terminalType 终端类型
	 * @return List<UserBO>
	 */
	public List<UserBO> queryUsersByUserId(Long userId, String terminalType) throws Exception{
		return JsonBodyResponseParser.parseArray(resourceFeign.queryUsersByUserId(userId, terminalType), UserBO.class);
	}
	
	/**
	 * 获取webrtc接入信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午3:38:36
	 * @return List<WorkNodeVO> 节点信息
	 */
	public List<WorkNodeVO> queryWebRtc() throws Exception{
		return JsonBodyResponseParser.parseArray(resourceFeign.queryWebRtc(), WorkNodeVO.class);
	}
	
	/**
	 * 查询用户资源（根据类型）<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午3:41:48
	 * @param List<Long> userIds 用户id
	 * @param String type 设备类型
	 * @return List<ResourceVO> 资源信息
	 */
	public List<ResourceVO> queryResource(List<Long> userIds, String type) throws Exception{
		return JsonBodyResponseParser.parseArray(resourceFeign.queryResource(JSON.toJSONString(userIds), type), ResourceVO.class);
	}
	
}
