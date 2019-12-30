package com.sumavision.tetris.cs.channel.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityRemoteDAO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityRemotePO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.cs.schedule.api.server.ApiServerScheduleVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/process/cs/channel")
public class ApiProcessChannelController {
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private BroadAbilityBroadInfoService broadAbilityBroadInfoService;
	
	@Autowired
	private BroadAbilityRemoteDAO broadAbilityRemoteDAO;
	
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
	public Object add(String file_fileToStreamInfo, String file_streamTranscodingInfo, String file_recordInfo, Boolean encryption, String __processInstanceId__, HttpServletRequest request) throws Exception{
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
				
				ChannelPO channel = channelService.add("remote_udp", DateUtil.now(), "轮播推流", "", ChannelType.REMOTE, encryption == null ? false : encryption, false, null, null, null, null, null, infoVOs);
				
				if (channel != null) {
					//保存流程信息和回调地址
					BroadAbilityRemotePO remotePO = new BroadAbilityRemotePO();
					remotePO.setChannelId(channel.getId());
					remotePO.setProcessInstanceId(__processInstanceId__);
					remotePO.setStopCallbackUrl(vo.getStopCallback());
					broadAbilityRemoteDAO.save(remotePO);
					
					ApiServerScheduleVO scheduleVO = new ApiServerScheduleVO();
					List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
					for (int i = 0; i < vo.getPlayCount(); i++) {
						ScreenVO screen = new ScreenVO();
						screen.setPreviewUrl(vo.getFileUrl());
						screen.setDuration(vo.getDuration());
						screenVOs.add(screen);
					}
					scheduleVO.setScreens(screenVOs);
					scheduleVO.setBroadDate(DateUtil.format(DateUtil.getDateByMillisecond(DateUtil.getLongDate()+1000), DateUtil.dateTimePattern));
					
					scheduleService.addSchedules(channel.getId(), new ArrayListWrapper<ApiServerScheduleVO>().add(scheduleVO).getList());
					
					channelService.startBroadcast(channel.getId());
					
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
			channelService.stopBroadcast(remotePO.getChannelId());
		}
		return null;
	}
}
