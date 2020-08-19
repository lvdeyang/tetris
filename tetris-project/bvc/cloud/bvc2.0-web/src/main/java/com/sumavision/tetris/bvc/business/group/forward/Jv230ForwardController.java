package com.sumavision.tetris.bvc.business.group.forward;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/business/jv230/forward")
public class Jv230ForwardController {

	@Autowired
	private Jv230ForwardQuery jv230ForwardQuery;
	
	@Autowired
	private Jv230ForwardService jv230ForwardService;
	
	/**
	 * 查询用户有权限的设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月27日 下午2:04:30
	 * @return List<TreeNodeVO> 设备列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/usable/bundles")
	public Object queryUseableBundles(HttpServletRequest request) throws Exception{
		
		return jv230ForwardQuery.queryUsableBundles();
	}
	
	/**
	 * 查询终端的上屏设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月17日 上午11:33:15
	 * @return List<TreeNodeVO> 设备树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/forward/bundles")
	public Object queryForwardBundles(HttpServletRequest request) throws Exception{
	
		return jv230ForwardQuery.queryForwardBundles();
	}
	
	/**
	 * 查询jv230转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月17日 下午1:24:52
	 * @param String bundleId jv230 bundleId
	 * @return List<Jv230ForwardVO> 转发列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/jv230/forwards")
	public Object queryJv230Forwards(
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		return jv230ForwardQuery.queryJv230Forwards(bundleId);
	}
	
	/**
	 * 查询终端合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月29日 上午9:23:41
	 * @return List<Jv230ForwardVO> 合屏源列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/combine/video")
	public Object queryCombineVideo(HttpServletRequest request) throws Exception{
		
		return jv230ForwardQuery.queryCombineVideo();
	}
	
	/**
	 * qt终端全部上屏jv230（这里默认是16视频解6音频解）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:30:12
	 * @param String bundleId jv230 设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/total/forward")
	public Object totalForward(
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		jv230ForwardService.totalForward(bundleId);
		return null;
	}
	
	/**
	 * qt终端切换分屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:02
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/split")
	public Object changeSplit(HttpServletRequest request) throws Exception{
		
		jv230ForwardService.changeSplit();
		return null;
	}
	
	/**
	 * qt终端某个分屏内容变化<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:24
	 * @param int serialNum 分屏序号
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/forward/by/serial/num")
	public Object changeForwardBySerialNum(
			int serialNum,
			HttpServletRequest request) throws Exception{
		
		jv230ForwardService.changeForwardBySerialNum(serialNum);
		return null;
	}
	
	/**
	 * qt终端某个分屏内容变化<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:24
	 * @param String bundleId jv230设备id
	 * @param int serialNum 分屏序号
	 * @return Jv230ForwardVO 视频转发信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/forward/by/bundle/id/and/serial/num")
	public Object changeForwardByBundleIdAndSerialNum(
			String bundleId,
			int serialNum,
			HttpServletRequest request) throws Exception{
		
		return jv230ForwardService.changeForwardByBundleIdAndSerialNum(bundleId, serialNum);
	}
	
	/**
	 * qt结束某个jv220上某个分屏上的内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:45:14
	 * @param String bundleId 设备id
	 * @param int serialNum 分屏序号
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/forward/by/bundle/id/and/serial/num")
	public Object deleteForwardByBundleIdAndSerialNum(
			String bundleId, 
			int serialNum, 
			HttpServletRequest request) throws Exception{
		
		jv230ForwardService.deleteForwardByBundleIdAndSerialNum(bundleId, serialNum);
		return null;
	}
	
	/**
	 * qt结束某个分屏上的内容（有事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:57
	 * @param int serialNum 分屏序号
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/forward/by/serial/num")
	public Object deleteForwardBySerialNum(
			int serialNum,
			HttpServletRequest request) throws Exception{
		
		jv230ForwardService.deleteForwardBySerialNum(serialNum);
		return null;
	}
	
	/**
	 * qt结束某个jv230全部转发（有事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:44:49
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/forward/by/bundle/id")
	public Object deleteForwardByBundleId(
			String bundleId, 
			HttpServletRequest request) throws Exception{
		
		jv230ForwardService.deleteForwardByBundleId(bundleId);
		return null;
	}
	
	/**
	 * qt结束全部上屏内容(有事务)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:32:24
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/total/forward")
	public Object deleteTotalForward(HttpServletRequest request) throws Exception{
		
		jv230ForwardService.deleteTotalForward();
		return null;
	}
	
}
