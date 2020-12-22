package com.sumavision.bvc.control.device.command.group.user.decoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.command.group.dao.CommandGroupDecoderSchemeDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupDecoderScreenDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderSchemePO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.control.device.command.group.vo.decoder.DecoderSchemeVO;
import com.sumavision.bvc.control.device.command.group.vo.decoder.DecoderScreenVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.cast.CommandDecoderSchemeServiceImpl;
import com.sumavision.bvc.device.command.cast.CommandDecoderServiceImpl;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * 解码器上屏及其方案<br/>
 * <b>作者:</b>zsy<br/>
 * <b>日期：</b>2020年5月13日
 */
@Slf4j
@Controller
@RequestMapping(value = "/command/decoder")
public class CommandUserDecoderController {
	
	/** 发起业务时，synchronized锁的前缀 */
	private static final String lockUserPrefix = "controller-userId-";
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandGroupDecoderSchemeDAO commandGroupDecoderSchemeDao;
	
	@Autowired
	private CommandGroupDecoderScreenDAO commandGroupDecoderScreenDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandDecoderServiceImpl commandDecoderServiceImpl;
	
	@Autowired
	private CommandDecoderSchemeServiceImpl commandDecoderSchemeServiceImpl;
	
	/**
	 * 查询某个用户所有的解码器上屏方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月13日 下午3:49:50
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/scheme")
	public Object queryAllScheme(
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(user.getId()).toString().intern()) {
			
			//查找该用户配置信息
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
			if(null == userInfo){
				//如果没有则建立默认
				userInfo = commandUserServiceImpl.generateDefaultUserInfo(user.getId(), user.getName(), true);
			}		
			List<CommandGroupDecoderSchemePO> decoderSchemes = userInfo.getDecoderSchemes();
			
			List<DecoderSchemeVO> schemeVOs = new ArrayList<DecoderSchemeVO>();		
			if(decoderSchemes != null){
				for(CommandGroupDecoderSchemePO decoderScheme : decoderSchemes){
					DecoderSchemeVO schemeVO = new DecoderSchemeVO().set(decoderScheme);
					schemeVOs.add(schemeVO);
				}
			}
			
			Map<String, Object> map = new HashMapWrapper<String, Object>()
					.put("decoderSchemes", schemeVOs)
					.getMap();
	//		log.info("queryAllScheme: " + JSON.toJSON(map));
			
			return map;
		}
	}
	
	/**
	 * 新建一个上屏方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:43:47
	 * @param name
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/scheme")
	public Object saveScheme(
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		CommandGroupDecoderSchemePO scheme = commandDecoderSchemeServiceImpl.saveScheme(user.getId(), name);
		
		return new DecoderSchemeVO().set(scheme);
	}
	
	/**
	 * 查询一个上屏方案的完整信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:44:06
	 * @param schemeId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/scheme")
	public Object queryScheme(
			String schemeId,
			HttpServletRequest request) throws Exception{
		
//		UserVO user = userUtils.getUserFromSession(request);
		CommandGroupDecoderSchemePO scheme = commandGroupDecoderSchemeDao.findOne(Long.parseLong(schemeId));		
		if(scheme == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到方案，id: " + schemeId);
		}
		
		DecoderSchemeVO schemeVO = new DecoderSchemeVO().set(scheme);
		return schemeVO;
	}
	
	/**
	 * 修改一个上屏方案的名称<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:44:29
	 * @param schemeId
	 * @param name
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/scheme")
	public Object editScheme(
			String schemeId,
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		commandDecoderSchemeServiceImpl.editScheme(user.getId(), Long.parseLong(schemeId), name);
		return null;
	}
	
	/**
	 * 删除多个上屏方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:44:49
	 * @param schemeIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/schemes")
	public Object removeSchemes(
			String schemeIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		List<Long> schemeIdArray = JSONArray.parseArray(schemeIds, Long.class);
		commandDecoderSchemeServiceImpl.removeSchemes(user.getId(), schemeIdArray);
		
		return null;
	}	
	
	/**
	 * 查询一个分屏关联的解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsu<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:45:11
	 * @param screenId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/screen/cast/devices")
	public Object queryScreenCastDevices(
			String screenId,
			HttpServletRequest request) throws Exception{
		
//		UserVO user = userUtils.getUserFromSession(request);		
		CommandGroupDecoderScreenPO screen = commandGroupDecoderScreenDao.findOne(Long.parseLong(screenId));
		if(screen == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到分屏，id: " + screenId);
		}
		
		List<CommandGroupUserPlayerCastDevicePO> castDevices = screen.getCastDevices();
		JSONArray result = new JSONArray();
		if(castDevices == null) return result;
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			JSONObject device = new JSONObject();
			device.put("bundleId", castDevice.getDstBundleId());
			device.put("name", castDevice.getDstBundleName());
			result.add(device);
		}
		
		return result;
	}
	
	/**
	 * 给分屏绑定一个解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午2:48:56
	 * @param serial
	 * @param bundleId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/screen/cast/device")
	public Object addScreenCastDevice(
			String screenId,
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		CommandGroupDecoderScreenPO screen = commandDecoderSchemeServiceImpl.screenAddDecoders(user.getId(), Long.parseLong(screenId), new ArrayListWrapper<String>().add(bundleId).getList());
		
		return new DecoderScreenVO().set(screen);
	}
	
	/**
	 * 给分屏解绑一个解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午2:48:35
	 * @param serial
	 * @param bundleId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/screen/cast/device")
	public Object removeScreenCastDevice(
			String screenId,
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		CommandGroupDecoderScreenPO screen = commandDecoderSchemeServiceImpl.screenRemoveDecoders(user.getId(), Long.parseLong(screenId), new ArrayListWrapper<String>().add(bundleId).getList());
		
		return new DecoderScreenVO().set(screen);
	}
	
	/**
	 * 从播放器给分屏上屏<br/>
	 * <p>拖动一个播放器到分屏区域</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:45:43
	 * @param serial
	 * @param screenId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/from/player/to/screen")
	public Object fromPlayerToScreen(
			int serial,
			String screenId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		CommandGroupDecoderScreenPO screen = commandDecoderServiceImpl.fromPlayerToScreen(user.getId(), serial, Long.parseLong(screenId));
		
		return new DecoderScreenVO().set(screen);
	}
	
	/**
	 * 从各类型的资源给分屏上屏<br/>
	 * <p>拖动一个资源到分屏区域</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月26日 上午10:46:38
	 * @param type 取值为 file/user/device
	 * @param id 资源id，可能是文件、设备、用户的id
	 * @param screenId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/from/resource/to/screen")
	public Object fromResourceToScreen(
			String type,
			String id,
			String screenId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		CommandGroupDecoderScreenPO screen = commandDecoderServiceImpl.fromResourceToScreen(user.getId(), type, id, Long.parseLong(screenId));
		
		return new DecoderScreenVO().set(screen);
	}
	
	/**
	 * 停止一个分屏的全部上屏<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:46:10
	 * @param screenId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop/screen")
	public Object stopScreen(
			String screenId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		CommandGroupDecoderScreenPO screen = commandDecoderServiceImpl.stopScreen(user.getId(), Long.parseLong(screenId));
		
		return new DecoderScreenVO().set(screen);
	}
}
