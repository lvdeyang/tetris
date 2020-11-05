package com.sumavision.tetris.bvc.model.agenda.combine;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(value = "/tetris/bvc/model/agenda/combine/audio")
public class CombineAudioController {

	@Autowired
	private CombineAudioQuery combineAudioQuery;
	
	@Autowired
	private CombineAudioService combineAudioService;
	
	/**
	 * 查询业务下的混音<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午3:52:48
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @return List<CombineAudioVO> 混音列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long businessId,
			String businessType,
			HttpServletRequest request) throws Exception{
		
		return combineAudioQuery.load(businessId, businessType);
	}
	
	/**
	 * 查询混音源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月8日 下午1:56:13
	 * @param Long combineAudioId 混音id
	 * @return List<CombineAudioSrcVO> 混音源列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/srcs")
	public Object loadSrcs(
			Long combineAudioId, 
			HttpServletRequest request) throws Exception{
		
		return combineAudioQuery.loadSrcs(combineAudioId);
	}
	
	/**
	 * 添加混音<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午3:46:21
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @param String name 混音名称
	 * @return CombineAudioVO 混音
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long businessId,
			String businessType,
			String name,
			HttpServletRequest request) throws Exception{
		
		return combineAudioService.add(businessId, businessType, name);
	}
	
	/**
	 * 修改混音名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午3:59:48
	 * @param Long id 混音id
	 * @param String name 名称
	 * @return CombineAudioVO 混音
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/name")
	public Object editName(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return combineAudioService.editName(id, name);
	}
	
	/**
	 * 编辑混音源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月8日 上午11:48:47
	 * @param Long id 混音id
	 * @param JSONArray srcs 源列表
	 * @return CombineAudioVO 混音
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/srcs")
	public Object editSrcs(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		Long id = requestWrapper.getLong("id");
		JSONArray srcs = requestWrapper.getJSONArray("srcs");
		return combineAudioService.editSrcs(id, srcs);
	}
	
	/**
	 * 删除混音<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午4:03:42
	 * @param Long id 混音id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		combineAudioService.delete(id);
		return null;
	}
	
}
