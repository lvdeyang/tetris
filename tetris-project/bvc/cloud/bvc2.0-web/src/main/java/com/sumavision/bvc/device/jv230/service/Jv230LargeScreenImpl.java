package com.sumavision.bvc.device.jv230.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.CombineVideoPositionPO;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.jv230.bo.Jv230BaseParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ChannelParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ForwardBO;
import com.sumavision.bvc.device.jv230.bo.Jv230SourceBO;
import com.sumavision.bvc.device.jv230.bo.PositionBO;
import com.sumavision.bvc.device.jv230.dao.Jv230ChannelDAO;
import com.sumavision.bvc.device.jv230.exception.Jv230DecodeAlreadyOccupiedException;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230PO;

/**
 * @ClassName: 大屏逻辑
 * @author wjw
 * @date 2018年8月24日 上午9:01:36 
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class Jv230LargeScreenImpl {
	
	@Autowired
	private Jv230ChannelDAO jv230ChannelDao;
	
	/**
	 * @Title: 生成单个音频协议
	 * @param group 设备组信息
	 * @param encodeAudioChannel 源通道
	 * @param Jv230ChannelPO Jv230通道信息
	 * @return LogicBO 协议数据
	 * @throws Exception
	 */
	public LogicBO generateSingleAudio(
			DeviceGroupPO group,
			DeviceGroupMemberChannelPO encodeAudioChannel,
			Jv230ChannelPO channel) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setJv230AudioSet(new ArrayList<Jv230ForwardBO>());
		
		//参数信息
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		
		Jv230SourceBO source = new Jv230SourceBO().setType("channel")
												  .setLayer_id(encodeAudioChannel.getMember().getLayerId())
												  .setBundle_id(encodeAudioChannel.getBundleId())
												  .setChannel_id(encodeAudioChannel.getChannelId());
		
		Jv230BaseParamBO baseParam = new Jv230BaseParamBO().setAudio(avtpl, source);
		
		Jv230ChannelParamBO channelParam = new Jv230ChannelParamBO().set(channel, baseParam);
		
		Jv230ForwardBO forward = new Jv230ForwardBO().set(channel, channelParam);
		
		logic.getJv230AudioSet().add(forward);
		
		return logic;		
	}
	
	/**
	 * @Title: 生成混音协议
	 * @param group 设备组信息
	 * @param combineAudioUuid 混音uuid
	 * @param Jv230ChannelPO Jv230通道信息
	 * @return LogicBO 协议数据
	 * @throws Exception
	 */
	public LogicBO generateCombineAudio(
			DeviceGroupPO group,
			String combineAudioUuid,
			Jv230ChannelPO channel) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setJv230AudioSet(new ArrayList<Jv230ForwardBO>());
		
		//参数信息
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		
		Jv230SourceBO source = new Jv230SourceBO().setType("combineAudio")
												  .setUuid(combineAudioUuid);
		
		Jv230BaseParamBO baseParam = new Jv230BaseParamBO().setAudio(avtpl, source);
		
		Jv230ChannelParamBO channelParam = new Jv230ChannelParamBO().set(channel, baseParam);
		
		Jv230ForwardBO forward = new Jv230ForwardBO().set(channel, channelParam);
		
		logic.getJv230AudioSet().add(forward);
		
		return logic;		
	}
	
	/**
	 * @Title: 比较合屏配置和大屏配置，两者布局相同和布局不同分别处理
	 * @param group 设备组信息
	 * @param video 合屏信息
	 * @param largescreenPO 大屏信息
	 * @return LogicBO 协议数据
	 * @throws Exception
	 */
	public LogicBO comparedCombineVideoAndLargeScreen(
			DeviceGroupPO group,
			CombineVideoPO video, 
			CombineJv230PO largescreenPO) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		Set<CombineVideoPositionPO> combineVideoPositions = video.getPositions();
		Set<Jv230PO> Jv230s = largescreenPO.getBundles();
		
		//先判断合屏布局和大屏布局是否一致，一样的话不用计算位置信息，直接满屏
		String combineVideoWebsiteDraw = video.getWebsiteDraw();
		String largeScreenWebsiteDraw = largescreenPO.getWebsiteDraw();
		if(combineVideoWebsiteDraw.equals(largeScreenWebsiteDraw)){
			logic = combineVideoEqualsLargeScreen(group, combineVideoPositions, Jv230s);
		}else {
			logic = combineVideoInequalsLargeScreen(group, combineVideoPositions, Jv230s);
		}
		
		return logic;
	}
	
	/**
	 * @Title: 大屏布局和合屏布局相同时调用
	 * @param group 设备组信息
	 * @param combineVideoPositions 所有合屏源信息
	 * @param Jv230s 大屏包含物理屏信息
	 * @return LogicBO 协议数据
	 * @throws Exception
	 */
	public LogicBO combineVideoEqualsLargeScreen(
			DeviceGroupPO group, 
			Set<CombineVideoPositionPO> combineVideoPositions, 
			Set<Jv230PO> Jv230s) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
		
		List<Jv230ForwardBO> forwardList = new ArrayList<Jv230ForwardBO>();
		
		for(Jv230PO Jv230: Jv230s){		
			for(CombineVideoPositionPO combineConfigVideoPosition: combineVideoPositions){		
				if(Jv230.getSerialnum() == combineConfigVideoPosition.getSerialnum()){				
					
					List<Jv230ForwardBO> jv230ForwardBOs = singlePositionEqualsLargeScreen(group, combineConfigVideoPosition, Jv230);	
					forwardList.addAll(jv230ForwardBOs);
					break;
				}
			}			
		}
		
		logic.getJv230ForwardSet().addAll(forwardList);
		
		return logic;
	}
	
	/**
	 * @Title: 大屏布局和合屏布局不同时调用
	 * @param group 设备组信息
	 * @param combineVideoPositions 所有合屏源信息
	 * @param Jv230s 大屏包含物理屏信息
	 * @return LogicBO 协议数据
	 * @throws Exception
	 */
	public LogicBO combineVideoInequalsLargeScreen(
			DeviceGroupPO group, 
			Set<CombineVideoPositionPO> combineVideoPositions, 
			Set<Jv230PO> Jv230s) throws Exception{
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
		
		List<Jv230ForwardBO> forwardList = new ArrayList<Jv230ForwardBO>();
		
		for(Jv230PO Jv230: Jv230s){
			List<Jv230ForwardBO> forwardBOs = new ArrayList<Jv230ForwardBO>();
			if(isSinglePositionBelongJv230s(combineVideoPositions, Jv230)){
				CombineVideoPositionPO combineVideoPosition = findSinglePositionBelongJv230s(combineVideoPositions, Jv230);
				forwardBOs = singlePositionEqualsLargeScreen(group, combineVideoPosition, Jv230);
			}else{
				forwardBOs = singlePositionInequalsLargeScreen(group, combineVideoPositions, Jv230);
			}
			forwardList.addAll(forwardBOs);
		}
		
		//计算共享源
		calculateShareCount(forwardList);
		
		logic.getJv230ForwardSet().addAll(forwardList);
		
		return logic;
	}

	/**
	 * @Title: 判断合屏某一源是否与Jv230物理屏位置重合
	 * @param combineVideoPositions 所有合屏源信息
	 * @param Jv230  单个Jv230物理屏信息
	 * @return boolean 布尔型
	 * @throws Exception
	 */
	private boolean isSinglePositionBelongJv230s(Set<CombineVideoPositionPO> combineVideoPositions, Jv230PO Jv230){
		boolean flag = false;
		for(CombineVideoPositionPO combineVideoPosition: combineVideoPositions){
			if(combineVideoPosition.getX().equals(Jv230.getX()) 
					&& combineVideoPosition.getY().equals(Jv230.getY())
					&& combineVideoPosition.getW().equals(Jv230.getW()) 
					&& combineVideoPosition.getH().equals(Jv230.getH())){
			
					flag = true;
					break;
			}
		}
		
		return flag;		
	}
	
	/**
	 * @Title: 找出和某一Jv230位置重合的合屏源
	 * @param combineVideoPositions 所有合屏源信息
	 * @param Jv230  单个Jv230物理屏信息
	 * @return CombineVideoPositionPO 合屏某个源
	 * @throws Exception
	 */
	private CombineVideoPositionPO findSinglePositionBelongJv230s(Set<CombineVideoPositionPO> combineVideoPositions, Jv230PO Jv230){
		CombineVideoPositionPO CombineVideoPositionPO = new CombineVideoPositionPO();
		for(CombineVideoPositionPO combineVideoPosition: combineVideoPositions){
			if(combineVideoPosition.getX().equals(Jv230.getX()) 
					&& combineVideoPosition.getY().equals(Jv230.getY())
					&& combineVideoPosition.getW().equals(Jv230.getW()) 
					&& combineVideoPosition.getH().equals(Jv230.getH())){
			
					CombineVideoPositionPO = combineVideoPosition;
					break;
			}
		}
		
		return CombineVideoPositionPO;		
	}
	
	/**
	 * @Title: 单个Jv230与单个合屏源位置重合时调用
	 * @param group 设备组
	 * @param combineVideoPosition 单个合屏源信息 
	 * @param Jv230  单个Jv230物理屏信息
	 * @return List<Jv230ForwardBO> 协议数据
	 * @throws Exception
	 */
	private List<Jv230ForwardBO> singlePositionEqualsLargeScreen(
			DeviceGroupPO group, 
			CombineVideoPositionPO combineVideoPosition, 
			Jv230PO Jv230)throws Exception{
		
		List<Jv230ForwardBO> forwardBOs = new ArrayList<Jv230ForwardBO>();
		
		//源位置和显示位置
		PositionBO srcPosition = new PositionBO().setX((int) 0)
												 .setY((int) 0)
												 .setWidth((int) 10000)
												 .setHeight((int) 10000);
		PositionBO displayPosition = new PositionBO().setX((int) 0)
												 .setY((int) 0)
												 .setWidth((int) 10000)
												 .setHeight((int) 10000)
												 .setZ_index((int) 1);
		
		Set<Jv230ChannelPO> Jv230Channels = Jv230.getChannels();
		Set<Jv230ChannelPO> Jv230VideoDecodeChannels = findVideoDecodeChannels(Jv230Channels);
		Set<Jv230ChannelPO> copyJv230Channels = new HashSet<Jv230ChannelPO>();
		for(Jv230ChannelPO Jv230Channel: Jv230VideoDecodeChannels){
			Jv230ChannelPO channelPO = new Jv230ChannelPO();
			BeanUtils.copyProperties(Jv230Channel, channelPO);
			copyJv230Channels.add(channelPO);
			Jv230Channel.setOccupied(false);
		}
		Jv230ChannelPO occupyChannel = occupyChannel(Jv230VideoDecodeChannels);
		
		Jv230ForwardBO forwardBO = singlePositionRelateLargeScreen(group, combineVideoPosition, occupyChannel, srcPosition, displayPosition);
		forwardBOs.add(forwardBO);
		
		List<Jv230ForwardBO> unOccupyChannel = unOccupyChannel(Jv230VideoDecodeChannels, copyJv230Channels);
		forwardBOs.addAll(unOccupyChannel);
		
		return forwardBOs;
	}
	
	/**
	 * @Title: 单个Jv230与单个合屏源位置不重合时调用
	 * @param group 设备组信息
	 * @param combineVideoPositions 单个合屏源信息
	 * @param Jv230  单个Jv230物理屏信息
	 * @return List<Jv230ForwardBO> 协议数据 
	 * @throws Exception
	 */
	private List<Jv230ForwardBO> singlePositionInequalsLargeScreen(
			DeviceGroupPO group, 
			Set<CombineVideoPositionPO> combineVideoPositions, 
			Jv230PO Jv230) throws Exception{
		
		List<Jv230ForwardBO> forwardBOList = new ArrayList<Jv230ForwardBO>();
		Set<Jv230ChannelPO> Jv230Channels = Jv230.getChannels();
		Set<Jv230ChannelPO> Jv230VideoDecodeChannels = findVideoDecodeChannels(Jv230Channels);
		
		int count = 0;
		for(CombineVideoPositionPO combineVideoPosition: combineVideoPositions){
			boolean flag = calculateRelativePosition(combineVideoPosition, Jv230, null, null, true);
			if(flag){
				count++;
			}
		}
		
		if(count > Jv230VideoDecodeChannels.size()) throw new Jv230DecodeAlreadyOccupiedException();
		
		//复制占用解码前通道数据
		Set<Jv230ChannelPO> copyJv230Channels = new HashSet<Jv230ChannelPO>();
		for(Jv230ChannelPO Jv230Channel: Jv230VideoDecodeChannels){
			Jv230ChannelPO channelPO = new Jv230ChannelPO();
			BeanUtils.copyProperties(Jv230Channel, channelPO);
			copyJv230Channels.add(channelPO);
			Jv230Channel.setOccupied(false);;
		}
		
		for(CombineVideoPositionPO combineVideoPosition: combineVideoPositions){
			PositionBO srcPosition = new PositionBO();
			PositionBO displayPosition = new PositionBO();
			
			//判断合屏源是否覆盖Jv230物理屏，若覆盖，更新相对位置信息
			boolean flag = calculateRelativePosition(combineVideoPosition, Jv230, srcPosition, displayPosition, false);
			if(flag){
				//协商通道占用
				Jv230ChannelPO occupyChannel = occupyChannel(Jv230VideoDecodeChannels);
				Jv230ForwardBO forwardBO = singlePositionRelateLargeScreen(group, combineVideoPosition, occupyChannel, srcPosition, displayPosition);
				forwardBOList.add(forwardBO);
			}
		}
		
		//未占用通道协议处理
		List<Jv230ForwardBO> unOccupyChannelForwardBOs = unOccupyChannel(Jv230VideoDecodeChannels, copyJv230Channels);
		forwardBOList.addAll(unOccupyChannelForwardBOs);
		
		//todo
		jv230ChannelDao.save(Jv230VideoDecodeChannels);
		
		return forwardBOList;   
	}
	
	/**
	 * @Title: 计算合屏源覆盖Jv230位置，求出公共区域，计算出分别相对于源和Jv230的位置
	 * 			isCount用来判断是不是只进行是否覆盖计算，true只做是否覆盖判断，false更新位置信息
	 * @param combineVideoPosition 合屏源信息
	 * @param Jv230 Jv230物理屏信息
	 * @param srcPosition 相对于源位置
	 * @param displayPosition 相对于Jv230物理屏位置
	 * @param isCount 布尔值（针对不同调用）
	 * @return boolean 布尔值：是否覆盖源 
	 * @throws Exception
	 */
	private boolean calculateRelativePosition(CombineVideoPositionPO combineVideoPosition, 
			Jv230PO Jv230, 
			PositionBO srcPosition, 
			PositionBO displayPosition, 
			boolean isCount) throws Exception{
		
		float srcX = transFloat(combineVideoPosition.getX());
		float srcY = transFloat(combineVideoPosition.getY());
		float srcW = transFloat(combineVideoPosition.getW());
		float srcH = transFloat(combineVideoPosition.getH());
		float dstX = transFloat(Jv230.getX());
		float dstY = transFloat(Jv230.getY());
		float dstW = transFloat(Jv230.getW());
		float dstH = transFloat(Jv230.getH());
		
		float sameX = 0f; 
		float sameY = 0f; 
		float sameW = 0f; 
		float sameH = 0f; 
		
		float EC = 0.0001f;
		
		//计算出公共区域的位置信息(src左上坐标点相对dst左上坐标点的计算)
		if(srcX >= dstX && srcY >= dstY && srcX+EC < (dstX+dstW) && srcY+EC < (dstY+dstH)){
			sameX = srcX;
			sameY = srcY;
			if((srcX-dstX+srcW) <= dstW+EC){
				sameW = srcW;
			}else{
				sameW = dstW-srcX+dstX;
			}
			if((srcY-dstY+srcH) <= dstH+EC){
				sameH = srcH;
			}else{
				sameH = dstH-srcY+dstY;
			}	
			
			if(isCount){
				return true;
			}
		}else if(srcX < dstX && (srcX+srcW) > dstX+EC && srcY < dstY && (srcY+srcH) >dstY+EC){
			sameX = dstX;
			sameY = dstY;
			if((srcW-(dstX-srcX)) <= dstW+EC){
				sameW = srcW-(dstX-srcX);
			}else{
				sameW = dstW;
			}
			if((srcH-(dstY-srcY)) <= dstH+EC){
				sameH = srcH-(dstY-srcY);
			}else{
				sameH = dstH;
			}
			
			if(isCount){
				return true;
			}
		}else if(srcX < dstX && (srcX+srcW) > dstX+EC && srcY >= dstY && srcY+EC < (dstY+dstH)){
			sameX = dstX;
			sameY = srcY;
			if((srcW-(dstX-srcX)) <= dstW+EC){
				sameW = srcW-(dstX-srcX);
			}else{
				sameW = dstW;
			}
			if((srcY-dstY+srcH) <= dstH+EC){
				sameH = srcH;
			}else{
				sameH = dstH-(srcY-dstY);
			}
			
			if(isCount){
				return true;
			}
		}else if(srcY < dstY && (srcY+srcH) >dstY+EC && srcX >= dstX && srcX+EC < (dstX+dstW)){
			sameX = srcX;
			sameY = dstY;
			if((srcX-dstX+srcW) <= dstW+EC){
				sameW = srcW;
			}else{
				sameW = dstW-srcX+dstX;
			}
			if((srcH-(dstY-srcY)) <= dstH+EC){
				sameH = srcH-(dstY-srcY);
			}else{
				sameH = dstH;
			}
			
			if(isCount){
				return true;
			}
		}else{
			return false; 
		}
		
		//计算公共区域相对于src的相对位置
		int srcPositionX = Math.round(((sameX-srcX)/srcW) * 10000);
		int srcPositionY = Math.round(((sameY-srcY)/srcH) * 10000);
		int srcPositionW = Math.round((sameW/srcW) * 10000);
		int srcPositionH = Math.round((sameH/srcH) * 10000);
		
		//计算公共区域相对于display的相对位置
		int displayPositionX = Math.round(((sameX-dstX)/dstW) * 10000);
		int displayPositionY = Math.round(((sameY-dstY)/dstH) * 10000);
		int displayPositionW = Math.round((sameW/dstW) * 10000);
		int displayPositionH = Math.round((sameH/dstH) * 10000);
		
		srcPosition.setX(srcPositionX)
				   .setY(srcPositionY)
				   .setWidth(srcPositionW)
				   .setHeight(srcPositionH);
		
		displayPosition.setX(displayPositionX)
					   .setY(displayPositionY)
					   .setWidth(displayPositionW)
					   .setHeight(displayPositionH)
					   .setZ_index((int) 1);
		
		return true;
	}
	
	/**
	 * @Title: 单个源与对应物理屏协议生成
	 * @param group 设备组信息
	 * @param combineVideoPosition 合屏源信息
	 * @param Jv230Channel 占用解码通道信息
	 * @param srcPosition 相对于源位置信息
	 * @param displayPosition 相对于物理屏位置信息
	 * @param count 物理屏上的源个数
	 * @return Jv230ForwardBO 协议数据
	 * @throws Exception
	 */
	private Jv230ForwardBO singlePositionRelateLargeScreen(
			DeviceGroupPO group, 
			CombineVideoPositionPO combineVideoPosition, 
			Jv230ChannelPO Jv230Channel, 
			PositionBO srcPosition, 
			PositionBO displayPosition) throws Exception{
		
		//参数信息
		DeviceGroupAvtplPO avtpl = group.getAvtpl();							
		
		List<CombineVideoSrcPO> srcs = combineVideoPosition.getSrcs();
		List<Jv230SourceBO> sources = new ArrayList<Jv230SourceBO>();
		
		for(CombineVideoSrcPO src: srcs){
			Jv230SourceBO sourceBO = new Jv230SourceBO().setVideo(src);
			sources.add(sourceBO);
		}
		
		Jv230BaseParamBO base_param = new Jv230BaseParamBO().setVideo(avtpl, srcPosition, displayPosition, sources)
															.setSrc_identify(combineVideoPosition.getUuid());
		
		if(combineVideoPosition.getPictureType().equals(PictureType.POLLING)){
			base_param.setIs_polling("true")
					  .setInterval(Integer.parseInt(combineVideoPosition.getPollingTime()));
		}else if(combineVideoPosition.getPictureType().equals(PictureType.STATIC)){
			base_param.setIs_polling("false");
		}
		
		Jv230ChannelParamBO channel_param = new Jv230ChannelParamBO().set(Jv230Channel, base_param);
		
		Jv230ForwardBO jv230ForwardBO = new Jv230ForwardBO().set(Jv230Channel, channel_param);	
		
		return jv230ForwardBO;
	}
	
	/**
	 * @Title: 协商可占用的解码通道
	 * @param channels 一个Jv230的所有解码通道
	 * @return Jv230ChannelPO 一个解码通道
	 * @throws Exception
	 */
	public Jv230ChannelPO occupyChannel(Set<Jv230ChannelPO> channels) throws Exception{
		Jv230ChannelPO channelPO = new Jv230ChannelPO();
		for(Jv230ChannelPO channel:channels){
				if(!channel.isOccupied()){
					channel.setOccupied(true); 
					channelPO = channel;
					break;
				}		
		}
		
		return channelPO;
	}
	
	/**
	 * @Title: 未占用通道协议下发（未占用通道与之前相比，由占用变成不占用，下发空源协议）
	 * @param channels 通道占用后的通道信息
	 * @param copyChannels 通道占用前的通道信息
	 * @return List<Jv230ForwardBO> 协议数据
	 * @throws Exception
	 */
	public List<Jv230ForwardBO> unOccupyChannel(Set<Jv230ChannelPO> channels, Set<Jv230ChannelPO> copyChannels) throws Exception{
		List<Jv230ForwardBO> forwardBOList = new ArrayList<Jv230ForwardBO>();

		for(Jv230ChannelPO channel: channels){
			if(!channel.isOccupied()){
				for(Jv230ChannelPO copyChannel: copyChannels){
					if(copyChannel.getId() == channel.getId()){
						if(copyChannel.isOccupied()){
							Jv230BaseParamBO baseParamBO = new Jv230BaseParamBO().setNull();
							Jv230ChannelParamBO channelParamBO = new Jv230ChannelParamBO().set(copyChannel,baseParamBO);
							Jv230ForwardBO forwardBO = new Jv230ForwardBO().set(channel, channelParamBO);
							forwardBOList.add(forwardBO);
						}
					}
				}
			}
		}
	
		return forwardBOList;
	}
	
	/**
	 * @Title: 找出Jv230视频解码通道
	 * @param Jv230所有通道
	 * @return
	 * @throws Exception 
	 */
	public Set<Jv230ChannelPO> findVideoDecodeChannels(Set<Jv230ChannelPO> channels) throws Exception{
		Set<Jv230ChannelPO> videoDecodeChannels = new HashSet<Jv230ChannelPO>();
		for(Jv230ChannelPO channel: channels){
			if(channel.getType().isVideoDecode()){
				videoDecodeChannels.add(channel);
			}
		}
		
		return videoDecodeChannels;		
	}
	
	/**
	 * @Title: 比例转为float<br/> 
	 * @param proportion 比例
	 * @return Long 计算结果
	 */
	public float transFloat(String proportion){
		String[] structure = proportion.split("/");
		float value = Float.valueOf(structure[0])/Float.valueOf(structure[1]);
		
		return value;
	}
	
	/**
	 * @Title: 计算共享源并添加协议
	 * @param forwardBOs 协议数据
	 * @return 
	 */
	public void calculateShareCount(List<Jv230ForwardBO> forwardBOs){
		Set<String> uuids = new HashSet<String>(); 
		for(Jv230ForwardBO forwardBO: forwardBOs){
			String forwardUuid = forwardBO.getChannel_param().getBase_param().getSrc_identify();
			if(forwardUuid != null){
				uuids.add(forwardUuid);
			}
		}
		
		for(String uuid: uuids){
			int count = 0;
			for(Jv230ForwardBO forwardBO: forwardBOs){
				String forwardUuid = forwardBO.getChannel_param().getBase_param().getSrc_identify();
				if(forwardUuid != null){
					if(forwardUuid.equals(uuid)){
						count++;
					}
				}			
			}
			
			for(Jv230ForwardBO forwardBO: forwardBOs){
				String forwardUuid = forwardBO.getChannel_param().getBase_param().getSrc_identify();
				if(forwardUuid != null){
					if(forwardUuid.equals(uuid)){
						forwardBO.getChannel_param().getBase_param().setSrc_share_cnt(count);
						if(count == 1){
							forwardBO.getChannel_param().getBase_param().setSrc_mode((int) 1);
						}else if(count > 1){
							forwardBO.getChannel_param().getBase_param().setSrc_mode((int) 2);
						}
					}
				}			
			}
		}
	}
}
