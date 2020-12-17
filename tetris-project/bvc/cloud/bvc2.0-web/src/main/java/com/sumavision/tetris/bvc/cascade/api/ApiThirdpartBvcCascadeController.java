package com.sumavision.tetris.bvc.cascade.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.control.device.monitor.live.MonitorLiveDeviceVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.bvc.device.monitor.live.MonitorLiveCommons;
import com.sumavision.bvc.device.monitor.live.call.MonitorLiveCallService;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceDAO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceQuery;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceService;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserService;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.cascade.ProtocolParser;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;

@Controller
@RequestMapping(value = "/api/thirdpart/bvc/cascade")
public class ApiThirdpartBvcCascadeController {
	
	@Autowired
	private ProtocolParser protocolParser;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private MonitorLiveUserService monitorLiveUserService;
	
	@Autowired
	private MonitorLiveCallService monitorLiveCallService;
	
	@Autowired
	private MonitorLiveDeviceService monitorLiveDeviceService;
	
	@Autowired
	private MonitorLiveCommons monitorLiveCommons;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private MonitorLiveDeviceDAO monitorLiveDeviceDao;
	
	@Autowired
	private MonitorLiveDeviceQuery monitorLiveDeviceQuery;
	
	@Autowired
	private QueryUtil queryUtil;
	
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ExtraInfoService extraInfoService;
	
	/**
	 * 查询服务节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午5:53:38
	 * @param seq String(uuid)
	 * @param cmd query_node_info
	 * @param layer_id 自己的接入id
	 * @return 
	 * {
	 *		layer_id:String(自己的接入id)
	 *		self:{ //本节点的信息
	 *			app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址)
	 *		},
	 *		supers:[{ //上级节点的信息
	 *			app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址),
	 *			is_publish:bool(是否发布信息，最多只有一个子节点可以为true)
	 *		}],
	 *		subors:[{ //下级节点的信息
	 *		app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址)
	 *		}],
	 *		relations:[{ //关联节点的信息
	 *			app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址)
	 *		}]
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/node/info")
	public Object queryNodeInfo(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String layer_id = requestWrapper.getString("layer_id");
		String seq = requestWrapper.getString("seq");
		String cmd = requestWrapper.getString("cmd");
		return resourceRemoteService.queryNodeInfo(layer_id, seq, cmd);
	}
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午6:01:16
	 * @param cmd:String(常量字符串, query_next_node),
	 * @param seq:String(命令标识号),
	 * @param no:String(待查询的号码),
	 * @param type:String(all, device, user, app)
	 * @return
	 * {
	 *	   seq:String(命令标识号，与请求相同),
	 *	   no:String(待查询的号码),
	 *	   next_sip_no:String(下一跳的信令号码,应该在上级、下级、关联之中)
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/next/node")
	public Object queryNextNode(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String no = requestWrapper.getString("no");
		String seq = requestWrapper.getString("seq");
		String cmd = requestWrapper.getString("cmd");
		String type = requestWrapper.getString("type");
		return resourceRemoteService.queryNextRouterNode(seq, cmd, no, type);
	}
	
	
	/**
	 * 级联业务信息转换<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 上午11:54:01
	 * @param seq:String(命令标识号),
	 * @param cmd:String(常量字符串, trigger_cmd),
	 * @param src_user 源的用户号码
	 * @param content xml
	 * @return 
	 * {
	 *	   status:200,
	 *	   message:"",
	 *	   data:{
	 *	       seq:String(命令标识号),
	 *	 	   cmd:String(常量字符串, trigger_cmd)
	 * 	   }
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/conversion/information")
	public synchronized Object conversionInformation(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String seq = requestWrapper.getString("seq");
		String cmd = requestWrapper.getString("cmd");
		String src_user = requestWrapper.getString("src_user");
		String content = requestWrapper.getString("content");
		protocolParser.parse(src_user, content.trim());
		return new HashMapWrapper<String, String>().put("seq", seq)
												   .put("cmd", cmd)
												   .getMap();
	}
	
	/**
	 * 联网向业务发送上线请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午6:18:27
	 * @param seq String 命令标识号
	 * @param cmd trigger_cmd
	 * @param app_num 上线的节点的应用号码
	 * @param status online，offline
	 * @return 
	 * {
	 *	   status:200,
	 *	   message:"",
	 *	   data:{
	 *	       seq:String(命令标识号),
	 *	 	   cmd:String(常量字符串, trigger_cmd)
	 * 	   }
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/report/node/online")
	public Object reportNodeOnline(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String seq = requestWrapper.getString("seq");
		String cmd = requestWrapper.getString("cmd");
		String app_num = requestWrapper.getString("app_num");
		String status = requestWrapper.getString("status");
		resourceRemoteService.updateRouter(app_num, status);
		return new HashMapWrapper<String, String>().put("seq", seq)
												   .put("cmd", cmd)
												   .getMap();
	}
	
	/**
	 * 联网向业务发送请求同步数据命令<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午6:24:42
	 * @param seq String 命令标识号
	 * @param cmd trigger_cmd
	 * @param app_num 上线的节点的应用号码
	 * @return
	 * {
	 *	   status:200,
	 *	   message:"",
	 *	   data:{
	 *	       seq:String(命令标识号),
	 *	 	   cmd:String(常量字符串, trigger_cmd),
	 *         app_num:String(需要同步信息节点的应用号码),
	 *	       sync_info:String(同步信息的xml),
	 *	       sync_route_info:String(同步路由信息的xml)
	 * 	   }
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/request/sync/info")
	public Object requestSyncInfo(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String seq = requestWrapper.getString("seq");
		String cmd = requestWrapper.getString("cmd");
		String app_num = requestWrapper.getString("app_num");
		return resourceRemoteService.querySyncInfo(seq, cmd, app_num);
	}
	
	/**
	 * 开启/停止一个跨节点点播/呼叫业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午6:32:51
	 * @param uuid:String
	 * @param operate:String(start; stop)
	 * @param src_userno:String(发起方的用户号码)
	 * @param dst_no:String(被叫的号码)
	 * @param type:String(play,call,play-call)
	 * @return
	 * {
	 * 	   status:200,
	 * 	   message:""
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/streaming")
	public synchronized Object streaming(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		System.out.println("*******************************************");
		System.out.println("******92api*/xt/business*******************");
		System.out.println("*******************************************");
		System.out.println(params.toJSONString());
		
		String uuid = params.getString("uuid");
		String type = params.getString("type");
		String operate = params.getString("operate");
		String src_userno = params.getString("src_userno");
		String dst_no = params.getString("dst_no");
		
		try{
			//做一下用户登录
			protocolParser.rebindRequest(src_userno, "streaming");
			UserBO srcUser = userUtils.queryUserByUserno(src_userno.replace("<sip:", ""));
			UserBO dstUser = userUtils.queryUserByUserno(dst_no);
			if("start".equals(operate)){
				if(dstUser != null){
					if("play".equals(type)){
						//开始xt点播本地用户
						monitorLiveUserService.startXtSeeLocal(uuid, dstUser, srcUser.getId(), srcUser.getName(), srcUser.getUserNo());
					}else if("call".equals(type)){
						
						//开始xt用户呼叫本地用户，根据登录的不同平台，调用不同的方法
						/*if(dstUser.isLogined()){
							//这个登录状态是qt客户端的
							commandUserServiceImpl.userCallUser_Cascade(srcUser, dstUser, -1, uuid);
						}else{
							//检查网页是否登录
							UserBO dstWebUser = userUtils.queryUserById(dstUser.getId(), TerminalType.PC_PLATFORM);
							if(dstWebUser.isLogined()){
								monitorLiveCallService.startXtCallLocal(uuid, dstUser, srcUser);
							}else{
								//拒绝
								throw new UserDoesNotLoginException(dstUser.getName());
							}
						}*/
//						monitorLiveCallService.startXtCallLocal(uuid, dstUser, srcUser);
						commandUserServiceImpl.userCallUser_Cascade(srcUser, dstUser, -1, uuid);
						
					}else if("play-call".equals(type)){
						
						//开始xt点播本地用户转xt呼叫本地用户，根据登录的不同平台，调用不同的方法
						/*if(dstUser.isLogined()){
							//这个登录状态是qt客户端的
							commandUserServiceImpl.transOuterVodInnerToCall(uuid, dstUser, srcUser);
						}else{
							//检查网页是否登录
							UserBO dstWebUser = userUtils.queryUserById(dstUser.getId(), TerminalType.PC_PLATFORM);
							if(dstWebUser.isLogined()){
								monitorLiveCallService.transXtCallLocal(uuid, dstUser, srcUser);
							}else{
								//拒绝
								throw new UserDoesNotLoginException(dstUser.getName());
							}
						}*/
						commandUserServiceImpl.transOuterVodInnerToCall(uuid, dstUser, srcUser);
						
					}
				}else{
					//开始点播设备
					BundlePO bundle = bundleDao.findByUsername(dst_no);
					List<ChannelSchemeDTO> videoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundle.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
					ChannelSchemeDTO videoChannel = videoChannels.get(0);
					List<ChannelSchemeDTO> audioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundle.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
					ChannelSchemeDTO audioChannel = (audioChannels==null||audioChannels.size()<=0)?null:audioChannels.get(0);
					if(audioChannel != null){
						monitorLiveDeviceService.startXtSeeLocal(
								null, uuid, 
								bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), videoChannel.getChannelId(), videoChannel.getBaseType(), 
								bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), audioChannel.getChannelId(), audioChannel.getBaseType(), 
								srcUser.getId(), srcUser.getUserNo());
					}else{
						monitorLiveDeviceService.startXtSeeLocal(
								null, uuid, 
								bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), videoChannel.getChannelId(), videoChannel.getBaseType(), 
								null, null, null, null, null, null, 
								srcUser.getId(), srcUser.getUserNo());
					}
				}
			}else if("stop".equals(operate)){
				if(dstUser != null){
					if("play".equals(type)){
						//停止xt点播本地用户
						monitorLiveUserService.stop(uuid, srcUser.getId(), srcUser.getUserNo());
					}else if("call".equals(type)){
						//停止xt用户呼叫本地用户（两个方法各调一次）
						monitorLiveCallService.stop(uuid, srcUser.getId());
						commandUserServiceImpl.stopCall_Cascade(UserClassify.LDAP.toString().equals(srcUser.getCreater())?srcUser:dstUser, null, uuid);
					}else if("paly-call".equals(type)){
						//停止xt点播本地用户转xt呼叫本地用户
					}
				}else{
					//停止点播设备
					monitorLiveDeviceService.stop(uuid, srcUser.getId(), srcUser.getUserNo());
				}
			}else if("accept".equals(operate)){
				if(dstUser != null){
					if("call".equals(type)){
						//xt用户同意接听呼叫
						commandUserServiceImpl.acceptCall_Cascade(dstUser, null, uuid);
					}else if("play-call".equals(type)){
						//TODO: xt用户同意点播转呼叫
						commandUserServiceImpl.acceptCall_Cascade(dstUser, null, uuid);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			if("start".equals(operate)){
				if(e instanceof BaseException){
					BaseException businessException = (BaseException)e;
					monitorLiveCommons.sendFailPassby(uuid, businessException.getProtocol(), businessException.getMessage(), 1l);
				}else{
					monitorLiveCommons.sendFailPassby(uuid, "0", "业务异常", 1l);
				}
			}
		}
		
		return params;
	}

	/**
	 * 本域设备被外域点播:接入能力，主动调用平台。如果是打开的，平台需要向对应的设备发送openbundle命令。<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午2:38:14
	 * @param 
	 *  bundleid:xxxx，设备的bundleId
   		video_channel:xxxx
   		audio_channel:xxxx
   		status: open //open, close  开始/关闭
	 * @return
	 * {
    	status:200以及其他,
    	message:'异常信息',
    	data:{
        	layer_id:xxx //在本域的接入节点
        	bundle_id:xxx //被点播的设备bundle id
    	}
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/open/status")
	public synchronized Object deviceOpenStatus(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		System.out.println("*******************************************");
		System.out.println("******BQ/xt/business：通过bundleId操作外域点播*******************");
		System.out.println("*******************************************");
		System.out.println(params.toJSONString());
		
		String bundleid = params.getString("bundleid");
//		String video_channel = params.getString("video_channel");
//		String audio_channel = params.getString(" audio_channel");
		String status = params.getString("ststus");
		BundlePO bundle = bundleDao.findByBundleId(bundleid);
		
		if("open".equals(status)){
			//开始点播设备
			List<ChannelSchemeDTO> videoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundle.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			ChannelSchemeDTO videoChannel = videoChannels.get(0);
			List<ChannelSchemeDTO> audioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundle.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			ChannelSchemeDTO audioChannel = (audioChannels==null||audioChannels.size()<=0)?null:audioChannels.get(0);
			if(audioChannel != null){
				monitorLiveDeviceService.startXtSeeLocal(
						null, null, 
						bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), videoChannel.getChannelId(), videoChannel.getBaseType(), 
						bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), audioChannel.getChannelId(), audioChannel.getBaseType(), 
						-1L, "");
			}else{
				monitorLiveDeviceService.startXtSeeLocal(
						null, null, 
						bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), videoChannel.getChannelId(), videoChannel.getBaseType(), 
						null, null, null, null, null, null, 
						-1L, "");
			}
			
		}else if("close".equals(status)){
			//停止点播设备
			List<MonitorLiveDevicePO> liveList = monitorLiveDeviceDao.findByVideoBundleIdAndType(bundleid, LiveType.XT_LOCAL);
			monitorLiveDeviceService.stopXtSeeLocal(liveList, true);
		}
		
		JSONObject JSONobject = new JSONObject();
		JSONobject.put("layer_id", bundle.getAccessNodeUid());
		JSONobject.put("bundle_id", bundleid);
		
		return JSONobject;
	}

	/**
	 * 分页查询点播设备任务<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月15日 上午10:58:42
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @param String type 点播设备类型{@code LiveType 中的枚举值}
	 * @return total int 总数据量
	 * @return rows List<MonitorLiveDeviceVO> 任务列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/device/lives")
	public Object loadDeviceLives(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		
		Integer currentPage = Integer.valueOf(params.getString("currentPage"));
		Integer pageSize = Integer.valueOf(params.getString("pageSize"));
		String type = params.getString("type");
		
		long total = 0;
		
		List<MonitorLiveDevicePO> entities = null;
		
		if(type == null || type.equals("")){
			total = monitorLiveDeviceDao.count();
			entities = monitorLiveDeviceQuery.findAll(currentPage, pageSize);
		}else{
			Page<MonitorLiveDevicePO> pagedEntities = monitorLiveDeviceQuery.findByTypeAndStatus(currentPage, pageSize, type, MonitorRecordStatus.RUN);
			entities = pagedEntities.getContent();
			total = pagedEntities.getTotalElements();
		}
		
		//外部点播本地的外部用户id集合
		Set<Long> outerUserIds = new HashSet<Long>();		
		List<String> bundleIds=new ArrayList<String>();
		entities.stream().forEach(entity->{
			if(entity.getVideoBundleId() != null) bundleIds.add(entity.getVideoBundleId());
			if(entity.getDstVideoBundleId() != null) bundleIds.add(entity.getDstVideoBundleId());
			if(LiveType.XT_LOCAL.equals(entity.getType())) outerUserIds.add(entity.getUserId());
		});
		List<ExtraInfoPO> allExtraInfos = extraInfoService.findByBundleIdIn(bundleIds);
		List<FolderUserMap> outerUserMaps = folderUserMapDao.findByUserIdIn(outerUserIds);
		
		List<MonitorLiveDeviceVO> rows =entities.stream().map(entity->{
			List<ExtraInfoPO> extraInfos = extraInfoService.queryExtraInfoBundleId(allExtraInfos, entity.getVideoBundleId());
			List<ExtraInfoPO> dstExtraInfos = extraInfoService.queryExtraInfoBundleId(allExtraInfos, entity.getDstVideoBundleId());
			if(LiveType.XT_LOCAL.equals(entity.getType())){
				//外部点播本地编码器，造一个ExtraInfoPO给dstExtraInfo设置值使用
				FolderUserMap userMap = queryUtil.queryUserMapByUserId(outerUserMaps, entity.getUserId());
				ExtraInfoPO extraInfo = new ExtraInfoPO();
				extraInfo.setName("extend_param");
				extraInfo.setValue("{\"region\":\"" + userMap.getUserNode() + "\"}");
				dstExtraInfos = new ArrayList<ExtraInfoPO>();
				dstExtraInfos.add(extraInfo);
			}
			try {
				return new MonitorLiveDeviceVO().set(entity,extraInfos,dstExtraInfos);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
		
		if(rows!=null && rows.size()>0){
			Set<Long> osdIds = new HashSet<Long>();
			
			for(MonitorLiveDeviceVO row:rows){
				osdIds.add(row.getOsdId()==null?0l:row.getOsdId());
			}
			
			List<MonitorOsdPO> osdEntities = monitorOsdDao.findAll(osdIds);
			
			if(osdEntities!=null && osdEntities.size()>0){
				for(MonitorLiveDeviceVO row:rows){
					for(MonitorOsdPO osdEntity:osdEntities){
						if(osdEntity.getId().equals(row.getOsdId())){
							row.setOsdName(osdEntity.getName())
							   .setOsdUsername(osdEntity.getUsername());
							break;
						}
					}
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
}
