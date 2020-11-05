package com.sumavision.tetris.bvc.model.agenda.combine;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Controller("com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoController")
@RequestMapping(value = "/tetris/bvc/model/agenda/combine/video")
public class CombineVideoController {

	@Autowired
	private CombineVideoQuery combineVideoQuery;
	
	@Autowired
	private CombineVideoService combineVideoService;
	
	/**
	 * 查询业务下的合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:10:56
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @return List<CombineVideoVO> 合屏列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long businessId,
			String businessType,
			HttpServletRequest request) throws Exception{
		
		return combineVideoQuery.load(businessId, businessType);
	}
	
	/**
	 * 查询合屏分屏参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午1:55:06
	 * @param Long combineVideoId 合屏id
	 * @return List<CombineVideoPositionVO> 分屏参数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/positions")
	public Object loadPositions(
			Long combineVideoId,
			HttpServletRequest request) throws Exception{
		
		return combineVideoQuery.loadPositions(combineVideoId);
	}
	
	/**
	 * 添加合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:26:48
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @param String name 合屏名称
	 * @return CombineVideoVO 合屏
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long businessId,
			String businessType,
			String name,
			HttpServletRequest request) throws Exception{
		
		return combineVideoService.add(businessId, businessType, name);
	}
	
	/**
	 * 修改合屏名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:31:41
	 * @param Long id 合屏id
	 * @param String name 名称
	 * @return CombineVideoVO 合屏
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/name")
	public Object editName(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return combineVideoService.editName(id, name);
	}
	
	/**
	 * 编辑分屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月6日 下午5:29:39
	 * @param Long 合屏id
	 * @param JSONString websiteDraw 页面布局参数
	 * @param JSONArray positions 分屏参数信息
	 * @return CombineVideoVO 合屏
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/position")
	public Object editPosition(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		Long id = requestWrapper.getLong("id");
		JSONObject websiteDraw = requestWrapper.getJSONObject("websiteDraw");
		JSONArray positions = requestWrapper.getJSONArray("positions");
		return combineVideoService.editPosition(id, websiteDraw, positions);
	}
	
	/**
	 * 删除合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:50:22
	 * @param Long id 合屏id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		combineVideoService.delete(id);
		return null;
	}
	
}
