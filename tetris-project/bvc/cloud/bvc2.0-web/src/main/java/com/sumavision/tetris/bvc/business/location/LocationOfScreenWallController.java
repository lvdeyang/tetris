package com.sumavision.tetris.bvc.business.location;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value="location/of/screen/wall")
public class LocationOfScreenWallController {
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private LocationOfScreenWallService locationOfScreenWallService;
	
	/**
	 * 屏幕墙的编解码信息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:16:26
	 * @param locationTemplateLayoutId 屏幕墙id
	 * @return List<LocationOfScreenWallPO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="all/screen/information")
	public Object allScreenInformation(
			Long locationTemplateLayoutId) throws Exception{
		
		return locationOfScreenWallService.allScreenInformation(locationTemplateLayoutId);
	}
	
	/**
	 * 绑定解码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:58:34
	 * @param bundleId 设备id
	 * @param bundleName 设备名字
	 * @param locationX x方向的位置
	 * @param locationY y方向的位置
	 * @param LocationTemplateLayoutId 屏幕墙模板id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/bind/decoder")
	public Object bindDecoder(
			String bundleId,
			String bundleName,
			Integer locationX,
			Integer locationY,
			Long locationTemplateLayoutId,
			HttpServletRequest request) throws Exception{
		
		return locationOfScreenWallService.bindDecoder(bundleId, bundleName, locationX, locationY, locationTemplateLayoutId);
		
	}
	
	/**
	 * 解绑解码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午11:08:55
	 * @param id 屏幕墙LocationOfScreenWallPO的id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/unbind/decoder")
	public Object unbindDecoder(
			Long id) throws Exception{
		
		if(id == null){
			throw new BaseException(StatusCode.FORBIDDEN, "还没有绑定解码器");
		}
		
		locationOfScreenWallService.unbindDecoder(id);
		
		return null;
	}
	
	/**
	 * 解绑所有未执行转发解码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午11:08:55
	 * @param locationTemplateLayoutId 屏幕墙的id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/unbind/all/decoder")
	public Object unbindAllDecoder(
			Long locationTemplateLayoutId) throws Exception{
		
		if(locationTemplateLayoutId == null){
			return null;
		}
		
		locationOfScreenWallService.unbindAllDecoder(locationTemplateLayoutId);
		
		return null;
	}
	
	/**
	 * 绑定编码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 上午9:32:27
	  * @param bundleId 设备id
	 * @param bundleName 设备名字
	 * @param locationX x方向的位置
	 * @param locationY y方向的位置
	 * @param LocationTemplateLayoutId 屏幕墙模板id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/bind/encoder")
	public Object bindEncoder(
			String bundleId,
			String bundleName,
			Integer locationX,
			Integer locationY,
			Long locationTemplateLayoutId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		return locationOfScreenWallService.bindEncoder(bundleId, bundleName, locationX, locationY, locationTemplateLayoutId, user);
	}
	
	/**
	 * 解绑编码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:16:26
	 * @param id 屏幕id
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/unbind/encoder")
	public Object unbindEncoder(
			Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		locationOfScreenWallService.unbindEncoder(id, user);
		
		return null;
	}
	
	/**
	 * 解绑所有编码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:42:18
	 * @param ids 要解绑的屏幕LocationOfScreenWallPO的id
	 * @param LocationTemplateLayoutId 屏幕墙id
	 * @param unbindAll false解绑屏幕墙指定id编码器,true解绑屏幕墙所有编码器
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/unbind/all/stop/encoder")
	public Object unbindALLStopEncoder(
			String ids,
			Long LocationTemplateLayoutId,
			Boolean unbindAll,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<Long> idList = null;
		
		if(ids != null){
			idList = JSONArray.parseArray(ids, Long.class);
		}
		
		locationOfScreenWallService.unbindALLEncoder(idList, LocationTemplateLayoutId, unbindAll, user);
		
		return null;
	}
	
	/**
	 * 更新对应屏幕的转发状态<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月5日 下午2:07:34
	 * @param id 屏幕的id
	 * @param monitorLiveDeviceId
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/exchange/location/Status")
	public Object exchangeLocationStatus(
			Long id,
			Long monitorLiveDeviceId) throws Exception{
		
		return locationOfScreenWallService.exchangeLocationStatus(id, monitorLiveDeviceId);
	}
	
}
