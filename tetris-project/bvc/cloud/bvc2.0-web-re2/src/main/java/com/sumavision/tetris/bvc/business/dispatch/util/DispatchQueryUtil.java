package com.sumavision.tetris.bvc.business.dispatch.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.business.dispatch.po.TetrisDispatchPO;
import com.sumavision.tetris.bvc.business.dispatch.bo.ChannelBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StartBundleDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StartUserDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopBundleDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchByUserIdAndMeetingCodeBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopUserDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.po.TetrisDispatchChannelPO;

@Service
public class DispatchQueryUtil {
	

	
	public TetrisDispatchPO queryDispatchPOById(Collection<TetrisDispatchPO> dispatchPOs, Long id){
		if(id == null) return null;
		for(TetrisDispatchPO dispatchPO : dispatchPOs){
			if(id.equals(dispatchPO.getId())){
				return dispatchPO;
			}
		}
		return null;
	}
	
	public TetrisDispatchPO queryDispatchPOByDstBundleId(Collection<TetrisDispatchPO> dispatchPOs, String bundleId){
		if(bundleId == null) return null;
		for(TetrisDispatchPO dispatchPO : dispatchPOs){
			if(bundleId.equals(dispatchPO.getBundleId())){
				return dispatchPO;
			}
		}
		return null;
	}
	
	public TetrisDispatchPO queryDispatchPOByUserIdAndMeetingCodeAndSource(
			Collection<TetrisDispatchPO> dispatchPOs, String userId, String meetingCode, String sourceBundleId, String sourceChannelId){
		if(dispatchPOs == null) return null;
		for(TetrisDispatchPO dispatchPO : dispatchPOs){
			//userId和meetingCode相同，channel个数为1
			if(userId.equals(dispatchPO.getUserId()) && meetingCode.equals(dispatchPO.getMeetingCode())
					&& dispatchPO.getChannels() != null && dispatchPO.getChannels().size()==1){
				for(TetrisDispatchChannelPO channelPO : dispatchPO.getChannels()){
					//sourceBundleId和sourceChannelId相同
					if(sourceBundleId.equals(channelPO.getSourceBundleId()) && sourceChannelId.equals(channelPO.getSourceChannelId())){
						return dispatchPO;
					}
				}
			}
		}
		return null;
	}
	
	public List<TetrisDispatchPO> queryDispatchPOByUserId(
			Collection<TetrisDispatchPO> dispatchPOs, String userId){
		if(dispatchPOs == null) return null;
		List<TetrisDispatchPO> result = new ArrayList<TetrisDispatchPO>();
		for(TetrisDispatchPO dispatchPO : dispatchPOs){
			if(userId.equals(dispatchPO.getUserId())){
				result.add(dispatchPO);
			}
		}
		return result;
	}
	
	public List<TetrisDispatchPO> queryDispatchPOByUserIdAndMeetingCode(
			Collection<TetrisDispatchPO> dispatchPOs, String userId, String meetingCode){
		if(dispatchPOs == null) return null;
		List<TetrisDispatchPO> result = new ArrayList<TetrisDispatchPO>();
		for(TetrisDispatchPO dispatchPO : dispatchPOs){
			//userId和meetingCode相同
			if(userId.equals(dispatchPO.getUserId()) && meetingCode.equals(dispatchPO.getMeetingCode())){
				result.add(dispatchPO);
			}
		}
		return result;
	}
	
	public TetrisDispatchPO queryDispatchPOBySrcBundleId(Collection<TetrisDispatchPO> dispatchPOs, String bundleId){
		if(bundleId == null) return null;
		for(TetrisDispatchPO dispatchPO : dispatchPOs){
			if(dispatchPO.getChannels() != null){
				for(TetrisDispatchChannelPO channelPO : dispatchPO.getChannels()){
					if(bundleId.equals(channelPO.getSourceBundleId())){
						return dispatchPO;
					}
				}
			}
		}
		return null;
	}
	
	public List<TetrisDispatchChannelPO> queryDispatchChannelPOBySrcBundleId(Collection<TetrisDispatchChannelPO> channelPOs, String srcBundleId){
		if(channelPOs==null || srcBundleId==null) return null;
		List<TetrisDispatchChannelPO> result = new ArrayList<TetrisDispatchChannelPO>();
		for(TetrisDispatchChannelPO channelPO : channelPOs){
			if(srcBundleId.equals(channelPO.getSourceBundleId())){
				result.add(channelPO);
			}
		}
		return result;
	}
	
	/**
	 * 统计DispatchBO中作为源、目的的bundleIds，及所有的userIds<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午2:58:50
	 * @param dispatch
	 * @param srcBundleIds
	 * @param dstBundleIds
	 * @param userIds
	 */
	public void statisticIds(DispatchBO dispatch, Set<String> srcBundleIds, Set<String> dstBundleIds, Set<String> userIds){
		
		for(StartBundleDispatchBO bundleDispatchBO : dispatch.getStartBundleDispatch()){
			dstBundleIds.add(bundleDispatchBO.getBundleId());
			userIds.add(bundleDispatchBO.getUserId());
			if(bundleDispatchBO.getChannels() != null){
				for(ChannelBO channel : bundleDispatchBO.getChannels()){
					if(channel.getSource_param() != null){
						srcBundleIds.add(channel.getSource_param().getBundleId());
					}
				}
			}
		}
		
		for(StartUserDispatchBO bundleUserBO : dispatch.getStartUserDispatch()){
			userIds.add(bundleUserBO.getUserId());
			if(bundleUserBO.getChannels() != null){
				for(ChannelBO channel : bundleUserBO.getChannels()){
					if(channel.getSource_param() != null){
						srcBundleIds.add(channel.getSource_param().getBundleId());
					}
				}
			}
		}
		
		for(StopBundleDispatchBO stopBundleDispatchBO : dispatch.getStopBundleDispatch()){
			dstBundleIds.add(stopBundleDispatchBO.getBundleId());
			userIds.add(stopBundleDispatchBO.getUserId());
		}
		
		for(StopTaskDispatchBO stopTaskDispatchBO : dispatch.getStopTaskDispatch()){
			userIds.add(stopTaskDispatchBO.getUserId());	
		}
		
		for(StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO stopBO : dispatch.getStopTaskDispatchByUserIdAndMeetingCodeAndSource()){
			userIds.add(stopBO.getUserId());
			srcBundleIds.add(stopBO.getSourceBundleId());
		}
		
		for(StopTaskDispatchByUserIdAndMeetingCodeBO stopBO : dispatch.getStopTaskDispatchByUserIdAndMeetingCode()){
			userIds.add(stopBO.getUserId());
		}
		
		for(StopUserDispatchBO stopUserDispatchBO : dispatch.getStopUserDispatch()){
			userIds.add(stopUserDispatchBO.getUserId());
		}
		
		srcBundleIds.remove(null);
		srcBundleIds.remove("");
		dstBundleIds.remove(null);
		dstBundleIds.remove("");
		userIds.remove(null);
		userIds.remove("");
		
	}
	
	public void complementBundleIds(Collection<TetrisDispatchPO> dispatchs, Set<String> bundleIds){
		
		if(dispatchs == null) return;
		
		for(TetrisDispatchPO dispatch : dispatchs){
			bundleIds.add(dispatch.getBundleId());
			if(dispatch.getChannels() != null){
				for(TetrisDispatchChannelPO channelPO : dispatch.getChannels()){
					bundleIds.add(channelPO.getSourceBundleId());
				}
			}
		}
		
		bundleIds.remove(null);
		bundleIds.remove("");
	}
	
}
