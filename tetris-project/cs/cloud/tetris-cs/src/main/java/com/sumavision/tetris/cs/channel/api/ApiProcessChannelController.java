package com.sumavision.tetris.cs.channel.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.channel.PushVO;
import com.sumavision.tetris.cs.channel.SetAutoBroadBO;
import com.sumavision.tetris.cs.channel.SetOutputBO;
import com.sumavision.tetris.cs.channel.SetTerminalBroadBO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityRemoteDAO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityRemotePO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadStreamWay;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.cs.schedule.api.server.ApiServerScheduleVO;
import com.sumavision.tetris.mims.app.media.MediaQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/process/cs/channel")
public class ApiProcessChannelController {
	@Autowired
	private ChannelService channelService;

	@Autowired
	private ChannelDAO channelDAO;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private BroadAbilityBroadInfoService broadAbilityBroadInfoService;
	
	@Autowired
	private BroadAbilityRemoteDAO broadAbilityRemoteDAO;
	
	@Autowired
	private MediaQuery mediaQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 流程节点(文件转流)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月21日 下午4:45:02
	 * @param file_fileToStreamInfo 文件转流参数
	 * @param file_streamTranscodingInfo 流转码参数(透传)
	 * @param file_recordInfo 收录参数(透传)
	 * @param encryption 是否加密
	 * @return assetPath 文件转流后的udp地址
	 * @return transcode_streamTranscodingInfo 流转码参数
	 * @return transcode_recordInfo 收录参数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String file_assetPath, String file_fileToStreamInfo, String file_streamTranscodingInfo, String file_recordInfo, Boolean encryption, String __processInstanceId__, HttpServletRequest request) throws Exception{
		//本地地址和端口，让push的流推到本地，再转码
		FileToStreamVO vo = JSON.parseObject(file_fileToStreamInfo, FileToStreamVO.class);
		
		HashMapWrapper<String, Object> map = new HashMapWrapper<String, Object>();
		
		if (vo.isNeed()) {
			String ip = vo.getToolIp();
			Long port = broadAbilityBroadInfoService.queryLocalPort(ip, Long.parseLong(vo.getStartPort()));
			if (port != null && port != 0l) {
				List<BroadAbilityBroadInfoVO> infoVOs = new ArrayListWrapper<BroadAbilityBroadInfoVO>()
						.add(new BroadAbilityBroadInfoVO().setPreviewUrlIp(ip).setPreviewUrlPort(port.toString()))
						.getList();
				
				ChannelPO channel = channelService.add(
						"remote_udp",
						DateUtil.now(),
						"轮播推流",
						"",
						new SetTerminalBroadBO(),
						ChannelType.REMOTE,
						encryption == null ? false : encryption,
						new SetAutoBroadBO().setAutoBroad(false),
						new SetOutputBO().setOutput(infoVOs),"","","",false);
				
				if (channel != null) {
					//保存流程信息和回调地址
					BroadAbilityRemotePO remotePO = new BroadAbilityRemotePO();
					remotePO.setChannelId(channel.getId());
					remotePO.setProcessInstanceId(__processInstanceId__);
					remotePO.setStopCallbackUrl(vo.getStopCallback());
					remotePO.setBroadStreamWay(BroadStreamWay.ABILITY_FILE_STREAM_TRANSCODE);
					broadAbilityRemoteDAO.save(remotePO);
					
					ApiServerScheduleVO scheduleVO = new ApiServerScheduleVO();
					List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
					for (int i = 0; i < vo.getPlayCount(); i++) {
						ScreenVO screen = new ScreenVO();
						screen.setPreviewUrl((file_assetPath == null || file_assetPath.isEmpty()) ? vo.getFileUrl() : mediaQuery.queryByUrl(file_assetPath));
						screen.setDuration(vo.getDuration());
						screenVOs.add(screen);
					}
					scheduleVO.setScreens(screenVOs);
					scheduleVO.setBroadDate(DateUtil.format(DateUtil.getDateByMillisecond(DateUtil.getLongDate()+1000), DateUtil.dateTimePattern));
					
					scheduleService.addSchedules(channel.getId(), new ArrayListWrapper<ApiServerScheduleVO>().add(scheduleVO).getList());
					
					channelService.startBroadcast(channel.getId(), null);
					
					map.put("assetPath", new StringBufferWrapper().append("udp://@").append(ip).append(":").append(port).toString());
				}
				
				Thread.sleep(3000);
			}
		} else {
			map.put("assetPath", "");
		}
		
		return map.put("transcode_streamTranscodingInfo", file_streamTranscodingInfo)
				.put("transcode_recordInfo", file_recordInfo)
				.getMap();
	}
	
	/**
	 * 停止文件转流<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 下午5:23:43
	 * @param String messageId 任务id 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(String messageId, HttpServletRequest request) throws Exception {
		BroadAbilityRemotePO remotePO = broadAbilityRemoteDAO.findByProcessInstanceId(messageId);
		if (remotePO != null) {
			ChannelPO channel = channelDAO.findOne(remotePO.getChannelId());
			if (ChannelType.REMOTE.toString().equals(channel.getType())) {
				channelService.stopBroadcast(remotePO.getChannelId());
			}
		}
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/generate/with/internal/template")
	public Object generateWithInternalTemplate(
			String column,
			String name, 
			String type,
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			String keywords,
			String content,
			String region,
			String push,	
			String __processInstanceId__,
			HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<JSONObject> contents = JSONArray.parseArray(content, JSONObject.class);
		List<String> regions = JSONArray.parseArray(region, String.class);
		PushVO pushVO = JSONObject.parseObject(push, PushVO.class);
		channelService.generateWithInternalTemplate(name, author, publishTime, remark, keywords, contents, regions, pushVO, __processInstanceId__, user);
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/transcode/check/callback")
	public Object transcodeCheckCallback(HttpServletRequest request) throws Exception {
		return null;
	}
}
