package com.sumavision.tetris.bvc.util;

import java.util.Collection;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;

/**
 * 多码率工具类<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月11日 上午11:33:40
 */
@Service
public class MultiRateUtil {
	
	/**
	 * 根据需要的码率，找编码通道<br/>
	 * <p>如果没有匹配的通道，则向低码率寻找</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午1:40:03
	 * @param bundleChannels 不同编码能力的编码通道列表
	 * @param channelParamsType 最高能支持的解码能力
	 * @return
	 */
	public BusinessGroupMemberTerminalBundleChannelPO queryEncodeChannel(Collection<BusinessGroupMemberTerminalBundleChannelPO> bundleChannels, ChannelParamsType channelParamsType) {
		
		if(bundleChannels == null) return null;
		
		BusinessGroupMemberTerminalBundleChannelPO result = null;
		
		for(BusinessGroupMemberTerminalBundleChannelPO bundleChannel : bundleChannels){
			if(bundleChannel.getChannelParamsType().getLevel() <= channelParamsType.getLevel()){
				if(result == null){
					result = bundleChannel;
				}else if(bundleChannel.getChannelParamsType().getLevel() > result.getChannelParamsType().getLevel()){
					result = bundleChannel;
				}
			}
		}		
		return result;
	}
	
	/**
	 * 如有高清则返回高清，否则向低码率寻找<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:57:53
	 * @param bundleChannels 编码设备通道列表
	 * @return
	 */
	public BusinessGroupMemberTerminalBundleChannelPO queryDefultEncodeChannel(Collection<BusinessGroupMemberTerminalBundleChannelPO> bundleChannels) {
		
		BusinessGroupMemberTerminalBundleChannelPO result = queryEncodeChannel(bundleChannels, ChannelParamsType.HD);
		
		if(result != null) return result;
		
		result = queryEncodeChannel(bundleChannels, ChannelParamsType.DEFAULT);
		
		return result;
	}
	
	/**
	 * 找设备解码通道<br/>
	 * <p>目前假设1个终端解码通道对应1个设备解码通道，直接返回第1个设备通道</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午1:43:01
	 * @param bundleChannels 解码设备通道列表
	 * @return
	 */
	public BusinessGroupMemberTerminalBundleChannelPO queryDefultDecodeChannel(Collection<BusinessGroupMemberTerminalBundleChannelPO> bundleChannels) {
		
		if(bundleChannels == null || bundleChannels.size() == 0) return null;
		for(BusinessGroupMemberTerminalBundleChannelPO c : bundleChannels){
			return c;
		}
		return null;
	}
	
	/**
	 * 选择默认参数档位<br/>
	 * <p>以第3挡，HD</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月7日 上午11:22:04
	 * @param gears
	 * @return
	 */
	public DeviceGroupAvtplGearsPO queryDefaultGear(Collection<DeviceGroupAvtplGearsPO> gears){
		
		if(gears == null) return null;
		for(DeviceGroupAvtplGearsPO gear : gears){
			if(ChannelParamsType.HD.equals(gear.getChannelParamsType())){
				return gear;
			}
		}
		return null;
	}
	
}
