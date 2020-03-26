package com.sumavision.tetris.zoom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.resouce.feign.resource.ResourceService;
import com.sumavision.tetris.resouce.feign.resource.ResourceVO;
import com.sumavision.tetris.zoom.webrtc.WebRtcVO;

@Component
public class ZoomQuery {

	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 封装会议成员的bundle信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午2:09:07
	 * @param List<ZoomMemberVO> members 成员列表
	 * @return List<ZoomMemberVO> 成员列表
	 */
	public List<ZoomMemberVO> queryBundleInfo(List<ZoomMemberVO> members, WebRtcVO webrtc) throws Exception{
		List<ZoomMemberVO> terminals = new ArrayList<ZoomMemberVO>();
		List<Long> terminalUserIds = new ArrayList<Long>();
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		List<Long> jv220UserIds = new ArrayList<Long>();
		for(ZoomMemberVO member:members){
			if(ZoomMemberType.TERMINl.toString().equals(member.getType())){
				terminals.add(member);
				terminalUserIds.add(Long.valueOf(member.getUserId()));
			}else if(ZoomMemberType.JV220.toString().equals(member.getType())){
				jv220s.add(member);
				jv220UserIds.add(Long.valueOf(member.getUserId()));
			}
		}
		
		List<ResourceVO> terminalBundles = resourceService.queryResource(terminalUserIds, "pc");
		if(terminalBundles!=null && terminalBundles.size()>0 && terminals.size()>0){
			for(ZoomMemberVO terminal:terminals){
				for(ResourceVO terminalBundle:terminalBundles){
					if(terminalBundle.getUserId().equals(terminal.getUserId())){
						terminal.setBundleId(terminalBundle.getBundleId());
						terminal.setLayerId(webrtc.getLayerId());
						terminal.setVideoChannelId(terminalBundle.getVideoChannelId());
						terminal.setAudioChannelId(terminalBundle.getAudioChannelId());
						terminal.setScreenVideoChannelId(terminalBundle.getScreenVideoChannelId());
						terminal.setScreenAudioChannelId(terminalBundle.getScreenAudioChannelId());
						break;
					}
				}
			}
		}
		
		List<ResourceVO> jv220Bundles = resourceService.queryResource(terminalUserIds, "jv220");
		if(jv220Bundles!=null && jv220Bundles.size()>0 && jv220s.size()>0){
			for(ZoomMemberVO jv220:jv220s){
				for(ResourceVO jv220Bundle:jv220Bundles){
					if(jv220.getUserId().equals(jv220Bundle.getUserId())){
						jv220.setBundleId(jv220Bundle.getBundleId());
						jv220.setLayerId(jv220Bundle.getLayerId());
						jv220.setVideoChannelId(jv220Bundle.getVideoChannelId());
						jv220.setAudioChannelId(jv220Bundle.getAudioChannelId());
						jv220.setScreenVideoChannelId(jv220Bundle.getScreenVideoChannelId());
						jv220.setScreenAudioChannelId(jv220Bundle.getScreenAudioChannelId());
						break;
					}
				}
			}
		}
		return members;
	}
	
	/**
	 * 封装会议成员的bundle信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午2:09:07
	 * @param ZoomVO zoom 会议室信息
	 * @return ZoomVO 会议室信息
	 */
	public ZoomVO queryBundleInfo(ZoomVO zoom) throws Exception{
		queryBundleInfo(zoom.getMembers(), zoom.getWebRtc());
		return zoom;
	}
	
}
