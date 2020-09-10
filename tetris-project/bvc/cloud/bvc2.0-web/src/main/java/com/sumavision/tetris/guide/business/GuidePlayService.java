package com.sumavision.tetris.guide.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.MediaPushSetBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.guide.BO.GuideSourceOutputBO;
import com.sumavision.tetris.guide.BO.GuideSourcesBO;
import com.sumavision.tetris.guide.BO.VideoOrAudioSourceBO;
import com.sumavision.tetris.guide.control.GuideDAO;
import com.sumavision.tetris.guide.control.GuidePO;
import com.sumavision.tetris.guide.control.OutputSettingDAO;
import com.sumavision.tetris.guide.control.OutputSettingPO;
import com.sumavision.tetris.guide.control.SourceDAO;
import com.sumavision.tetris.guide.control.SourcePO;
import com.sumavision.tetris.guide.control.SourceType;

@Service
public class GuidePlayService {
	public static String layer_id="tetris-scl";
	
	@Autowired
	private GuideDAO guideDao;
	
	@Autowired
	private SourceDAO sourceDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private OutputSettingDAO outputSettingDao;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	//没有添加注释@service
	/**
	 * 开会<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 下午4:23:59
	 * @param guideId
	 * @throws Exception 
	 */
	public void start(Long guideId) throws Exception{
		//1.创建源：呼叫5G背包；打开虚拟源地址
		GuidePO guidePo=guideDao.findOne(guideId);
		List<SourcePO> sourceList=sourceDao.findByGuideId(guideId);
		//虚拟源相关的集合
		List<SourcePO> virtualSources=sourceList.stream().filter(source->{
			if(source.getSourceType().equals(SourceType.URL)){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		//5G背包相关的集合
		List<SourcePO> packageSources=new ArrayList<SourcePO>(sourceList);
		packageSources.removeAll(virtualSources);
		
		//对最多12个5G背包生成logic协议，缺少userId,参数模板codec
		LogicBO sourceLogic = getLogic(null,packageSources,null);
		
		//将最多12个虚拟地址源生成协议
		List <PassByBO> startPassBys=virtualSources.stream().map(source->{
			PassByBO passBy=new PassByBO();
			passBy.setBundle_id(source.getUuid());
			passBy.setLayer_id(layer_id);
			passBy.setType("creatInputSource");
			JSONObject pass_by_content=new JSONObject();
			pass_by_content.put("input_udp_url", source.getSource());
			passBy.setPass_by_content(pass_by_content);
			
			return passBy;
		}).collect(Collectors.toList());
		//需要判空么
		sourceLogic.getPass_by().addAll(startPassBys);
		
		//执行logic
		executeBusiness.execute(sourceLogic,"打开5G背包与源的编码");
		
		//2.创建输出源
		List<OutputSettingPO> outputSources=outputSettingDao.findByGuideId(guideId);
		List <PassByBO> outputPassBys=outputSources.stream().map(outputSource->{
			PassByBO passBy=new PassByBO();
			//缺少参数
			passBy.setBundle_id(outputSource.getUuid());
			passBy.setLayer_id(layer_id);
			passBy.setType("creatBackupSources");
			
			JSONObject pass_by_content=new JSONObject();
			pass_by_content.put("output_udp_url", outputSource.getOutputAddress());
			pass_by_content.put("sources", null);
			GuideSourceOutputBO guideSourceOutput=new GuideSourceOutputBO();
			
			
			guideSourceOutput.setOutput_udp_url(outputSource.getOutputAddress());
			
			return passBy;
		}).collect(Collectors.toList());
		LogicBO outputLogic=new LogicBO();
		outputLogic.getPass_by().addAll(outputPassBys);
		executeBusiness.execute(sourceLogic,  "打开输出编码");
	}
	
	/**
	 * 切换源<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 下午4:24:22
	 * @param guideId
	 * @param sourceId
	 * @throws Exception 
	 */
	public void exchange(Long guideId,Long sourceId) throws Exception{
		//切换源
		GuidePO guidePo=guideDao.findOne(guideId);
		SourcePO source=sourceDao.findOne(sourceId);
		
		//不太清楚切换源协议是否同时适用5G背包与虚拟源
		if(source.getSourceType().equals(SourceType.URL)){
			PassByBO passBy=new PassByBO();
			passBy.setBundle_id(source.getSource());
			passBy.setLayer_id(layer_id);
			passBy.setType("switchSource");
			
			JSONObject pass_by_content=new JSONObject();
			pass_by_content.put("source",new GuideSourcesBO()
					.setAudio_source(new VideoOrAudioSourceBO()
							.setBundle_id(source.getSource())
							.setLayer_id(layer_id)
							.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId()))
					.setVideo_source(new VideoOrAudioSourceBO()
							.setBundle_id(source.getSource())
							.setLayer_id(layer_id)
							.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId()))
						);
			
			passBy.setPass_by_content(pass_by_content);
			LogicBO logic=new LogicBO();
			logic.getPass_by().add(passBy);
			executeBusiness.execute(logic,  "打开虚拟源编码");
		}else if(source.getSourceType().equals(SourceType.KNAPSACK_5G)){
			PassByBO passBy=new PassByBO();
			passBy.setBundle_id(source.getSource());
			passBy.setLayer_id(layer_id);
			passBy.setType("switchSource");
			
			JSONObject pass_by_content=new JSONObject();
			pass_by_content.put("source",new GuideSourcesBO()
					.setAudio_source(new VideoOrAudioSourceBO()
							.setBundle_id(source.getSource())
							.setLayer_id(layer_id)
							.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId()))
					.setVideo_source(new VideoOrAudioSourceBO()
							.setBundle_id(source.getSource())
							.setLayer_id(layer_id)
							.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId()))
						);
			
			passBy.setPass_by_content(pass_by_content);
			LogicBO logic=new LogicBO();
			logic.getPass_by().add(passBy);
			executeBusiness.execute(logic,  "打开5G背包编码");
		}
		
	}
	
	/**
	 * 关闭会议<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 下午4:24:38
	 * @param guideId
	 * @throws Exception 
	 */
	public void stop(Long guideId) throws Exception{
		//1.删除源
		//删除虚拟源
		GuidePO guidePo=guideDao.findOne(guideId);
		List<SourcePO> sourceList=sourceDao.findByGuideId(guideId);
		List<SourcePO> virtualSources=sourceList.stream().filter(source->{
			if(source.getSourceType().equals(SourceType.URL)){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		List<SourcePO> packageSources=new ArrayList<SourcePO>();
		packageSources.removeAll(virtualSources);
		
		List <PassByBO> deleteSources=virtualSources.stream().map(source->{
			PassByBO passBy=new PassByBO();
			passBy.setBundle_id(source.getUuid());
			passBy.setLayer_id(layer_id);
			passBy.setType("deleteInputSource");
			JSONObject pass_by_content=new JSONObject();
			passBy.setPass_by_content(pass_by_content);
			return passBy;
		}).collect(Collectors.toList());
		//刪除5G背包中的源
		//close()
		LogicBO logic =closeEncoder(null, null, packageSources, null);
		logic.getPass_by().addAll(deleteSources);
		
		//2.删除所有输出源
		//删除所有虚拟输出源
		List<OutputSettingPO> sourceOutputList=outputSettingDao.findByGuideId(guideId);
		List<PassByBO> deleteOutputs=sourceOutputList.stream().map(source->{
			PassByBO passBy=new PassByBO();
			passBy.setBundle_id(source.getUuid());
			passBy.setLayer_id(layer_id);
			passBy.setType("deleteAllBackupSources");
			
			JSONObject pass_by_content=new JSONObject();
			passBy.setPass_by_content(pass_by_content);
			
			return passBy;
		}).collect(Collectors.toList());
		logic.getPass_by().addAll(deleteOutputs);
		executeBusiness.execute(logic,  "删除虚拟源，输出源与5G背包");
	}
	
	
	//缺少一个参数codec
	/**
	 * 获取logic协议，填充其中ConnectBundle部分<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 上午11:12:01
	 * @param userId
	 * @param packageSources
	 * @param codec
	 * @return
	 */
	public LogicBO getLogic(
			Long userId,
			List<SourcePO> packageSources,
			CodecParamBO codec){
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setPass_by(new ArrayList<PassByBO>());
		
		for(SourcePO sourcePO : packageSources){
//			ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//			BundlePO bundlePO = bundleDao.findByBundleId(video.getBundleId());
			//类型
			BundlePO bundlePO=bundleDao.findByBundleId(sourcePO.getSource());
			ConnectBundleBO connectEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
					            .setOperateType(ConnectBundleBO.OPERATE_TYPE)
							    .setLock_type("write")
							    .setBundleId(sourcePO.getSource())
							    .setLayerId(bundlePO.getAccessNodeUid())
							    .setBundle_type(bundlePO.getBundleType());
			ConnectBO connectEncoderVideoChannel = new ConnectBO().setChannelId(ChannelType.VIDEOENCODE1.getChannelId())
					      .setChannel_status("Open")
					      //固定
					      .setBase_type("VenusVideoIn")
					      .setCodec_param(codec);
			connectEncoderBundle.getChannels().add(connectEncoderVideoChannel);
//			ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
			
			ConnectBO connectEncoderAudioChannel = new ConnectBO().setChannelId(ChannelType.AUDIOENCODE1.getChannelId())
				      .setChannel_status("Open")
				      .setBase_type("VenusAudioIn")
				      .setCodec_param(codec);
			if(Boolean.TRUE.equals(bundlePO.getMulticastEncode())){
				String audioAddr = multicastService.addrAddPort(bundlePO.getMulticastEncodeAddr(), 4);
				connectEncoderAudioChannel.setMode(TransmissionMode.MULTICAST.getCode()).setMulti_addr(audioAddr);
			}
			connectEncoderBundle.getChannels().add(connectEncoderAudioChannel);
			
//			connectEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectEncoderVideoChannel).add(connectEncoderAudioChannel).getList());
			logic.getConnectBundle().add(connectEncoderBundle);
		}
		return logic;
	}
	
	//缺少group与codec
	/**
	 * 关闭源<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 下午5:21:06
	 * @param group
	 * @param userId
	 * @param Sources
	 * @param codec
	 * @return
	 * @throws Exception
	 */
	public LogicBO closeEncoder(
			GroupPO group,
			Long userId,
			List<SourcePO> Sources,
			CodecParamBO codec) throws Exception{
		
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
									 .setMediaPushDel(new ArrayList<MediaPushSetBO>())
									 .setPass_by(new ArrayList<PassByBO>());
		
		for(SourcePO source : Sources){
//			ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
			BundlePO bundlePO=bundleDao.findByBundleId(source.getSource());
			PassByBO passBy = new PassByBO().setHangUp(group, source.getSource() , bundlePO.getAccessNodeUid());
			DisconnectBundleBO disconnectEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					             .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
					             .setBundleId(source.getSource())
					             .setBundle_type(bundlePO.getBundleType())
					             .setLayerId(bundlePO.getAccessNodeUid())
					             .setPass_by_str(passBy);
			logic.getDisconnectBundle().add(disconnectEncoderBundle);
		}
		
		return logic;
	
	}
//	ChannelType.VIDEOENCODE1.getChannelId()
//	ChannelType.AUDIOENCODE1.getChannelId()
}
