package com.sumavision.tetris.guide.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
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
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.guide.BO.GuideOutputArrayBO;
import com.sumavision.tetris.guide.BO.GuideSourceOutputBO;
import com.sumavision.tetris.guide.BO.GuideSourcesBO;
import com.sumavision.tetris.guide.BO.VideoOrAudioSourceBO;
import com.sumavision.tetris.guide.control.AudioParametersDAO;
import com.sumavision.tetris.guide.control.GuideDAO;
import com.sumavision.tetris.guide.control.GuidePO;
import com.sumavision.tetris.guide.control.OutputGroupDAO;
import com.sumavision.tetris.guide.control.OutputGroupPO;
import com.sumavision.tetris.guide.control.OutputSettingDAO;
import com.sumavision.tetris.guide.control.OutputSettingPO;
import com.sumavision.tetris.guide.control.SourceDAO;
import com.sumavision.tetris.guide.control.SourcePO;
import com.sumavision.tetris.guide.control.SourceType;
import com.sumavision.tetris.guide.control.SwitchingMode;
import com.sumavision.tetris.guide.control.VideoParametersDAO;

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
	
	@Autowired
	private VideoParametersDAO videoParametersDao;
	
	@Autowired
	private AudioParametersDAO auidoParametersDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	
	@Autowired
	private OutputGroupDAO outputGroupDao;
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
//		GuidePO guidePo=guideDao.findOne(guideId);
		List<SourcePO> sourceListTemp=sourceDao.findByGuideIdOrderBySourceNumber(guideId);
		
		List<SourcePO> sourceList=new ArrayList<SourcePO>();
		
		for(SourcePO source:sourceListTemp){
			if(source.getSource()!=null){
				sourceList.add(source);
			}
		}
		
		//虚拟源相关的集合
		List<SourcePO> virtualSources=sourceList.stream().filter(source->{
			if(SourceType.URL.equals(source.getSourceType())){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		//5G背包相关的集合
		List<SourcePO> packageSources=new ArrayList<SourcePO>(sourceList);
		packageSources.removeAll(virtualSources);
		
		//对最多12个5G背包生成logic协议，缺少userId,参数模板codec
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO sourceLogic = getLogic(-1L,packageSources,codec);
		
		//将最多12个虚拟地址源生成协议
		List <PassByBO> startPassBys=virtualSources.stream().map(source->{
			PassByBO passBy=new PassByBO();
			passBy.setBundle_id(source.getUuid());
			passBy.setLayer_id(layer_id);
			passBy.setType("creatInputSource");
			JSONObject pass_by_content=new JSONObject();
			source.getSource();
			pass_by_content.put("url", source.getSource());
			
//			if(source.getSource().contains("udp")){
//				pass_by_content.put("type","udp_ts");
//			}else{
//				pass_by_content.put("type","srt_ts");
//				pass_by_content.put("mode", Mode.CALLER.getCode()); //目前先写死
//			}
			
			if(source.getSourceProtocol()!=null){
				pass_by_content.put("type",source.getSourceProtocol().getName());
				pass_by_content.put("srt_mode", Mode.CALLER.getCode()); //目前先写死
			}
			
			
			passBy.setPass_by_content(pass_by_content);
			
			return passBy;
		}).collect(Collectors.toList());
		
		if(sourceLogic.getPass_by()==null){
			sourceLogic.setPass_by(new ArrayList<PassByBO>());
		}
		sourceLogic.getPass_by().addAll(startPassBys);
		
		//执行logic
		executeBusiness.execute(sourceLogic,"打开5G背包与源的编码");
		
		//2.创建输出源   
	 	System.out.println("------------------------------输出源------------------------------------------");
	 	
//	 	List<OutputGroupPO> outputGroups=outputGroupDao.findByGuideId(guideId);
//	 	if(outputGroups!=null&&outputGroups.get(0).getGuideId()!=null){
//	 		List<OutputSettingPO> outputSources=outputSettingDao.findByGroupId(outputGroups.get(0).getGuideId());
//			if(outputSources==null){
//				throw new BaseException(StatusCode.ERROR,"备份源为空"); 
//			}
//			List <PassByBO> outputPassBys=outputSources.stream().map(outputSource->{
//				return getNewOutPutSettingPassBy(outputSource,outputGroups.get(0));
//			}).collect(Collectors.toList());
//			LogicBO outputLogic=new LogicBO();
//			if(outputLogic.getPass_by()==null){
//				outputLogic.setPass_by(new ArrayList<PassByBO>());
//			}
//			outputLogic.getPass_by().addAll(outputPassBys);
//		 	executeBusiness.execute(outputLogic,  "备份源编码");
//	 	}
	 	
	 	List<OutputGroupPO> outputGroups=outputGroupDao.findByGuideId(guideId);
	 	if(outputGroups!=null&&outputGroups.get(0).getGuideId()!=null){
	 		List<OutputSettingPO> outputSources=outputSettingDao.findByGroupId(outputGroups.get(0).getId());
			if(outputSources==null){
				throw new BaseException(StatusCode.ERROR,"备份源为空"); 
			}
			
			PassByBO passBy=getOutputSettingPassBy(outputGroups.get(0));
			
			LogicBO outputLogic=new LogicBO();
			if(outputLogic.getPass_by()==null){
				outputLogic.setPass_by(new ArrayList<PassByBO>());
			}
			outputLogic.getPass_by().add(passBy);
		 	executeBusiness.execute(outputLogic,  "备份源编码");
	 	}
	 	
	 	//备份源输出结束
	 	
	 	
	 	//创建预监输出开始
	 	System.out.println("-------------------------预监-----------------------------------------");
	 	LogicBO previewsLogic=new LogicBO();
	 	if(previewsLogic.getPass_by()==null){
	 		previewsLogic.setPass_by(new ArrayList<PassByBO>());
		}
	 	previewsLogic.getPass_by().addAll(getPreviewOutputPassBy());
	 	executeBusiness.execute(previewsLogic,  "预监编码");
	 	//创建预监输出结束
	 	
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
		//GuidePO guidePo=guideDao.findOne(guideId);
		SourcePO source=sourceDao.findOne(sourceId);
		
		List<OutputGroupPO> outputGroups=outputGroupDao.findByGuideId(guideId);
	 	if(outputGroups!=null&&outputGroups.get(0).getGuideId()!=null){
	 		
	 		List<OutputSettingPO> outputs=outputSettingDao.findByGroupId(outputGroups.get(0).getId());
	 		
	 		if(outputs==null||outputs.size()==0){
				throw new BaseException(StatusCode.ERROR, "备份源为空");
			}
			/*OutputSettingPO outputSetting =outputs.get(0);
			
			if(outputSetting==null){
				throw new BaseException(StatusCode.ERROR, "备份源为空");
			}*/
			LogicBO logic=new LogicBO();
			logic.setPass_by(new ArrayList<PassByBO>());
			
			outputs.stream().map(outputSetting->{
				if(source.getSourceType().equals(SourceType.URL)){
					PassByBO passBy=new PassByBO();
					passBy.setBundle_id(outputSetting.getUuid());
					passBy.setLayer_id(layer_id);
					passBy.setType("switchSource");
					
					JSONObject pass_by_content=new JSONObject();
					pass_by_content.put("source",new GuideSourcesBO()
							.setAudio_source(new VideoOrAudioSourceBO()
									.setBundle_id(source.getUuid())
									.setLayer_id(layer_id)
									.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId()))
							.setVideo_source(new VideoOrAudioSourceBO()
									.setBundle_id(source.getUuid())
									.setLayer_id(layer_id)
									.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId()))
								);
					
					passBy.setPass_by_content(pass_by_content);
					logic.getPass_by().add(passBy);
				}else if(source.getSourceType().equals(SourceType.KNAPSACK_5G)){
					PassByBO passBy=new PassByBO();
					passBy.setBundle_id(outputSetting.getUuid());
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
					logic.getPass_by().add(passBy);
				}
				return null;
			}).count();
			
			executeBusiness.execute(logic,  "打开虚拟源编码");
	 	}
		
//		if(source.getSourceType().equals(SourceType.URL)){
//			PassByBO passBy=new PassByBO();
//			passBy.setBundle_id(outputSetting.getUuid());
//			passBy.setLayer_id(layer_id);
//			passBy.setType("switchSource");
//			
//			JSONObject pass_by_content=new JSONObject();
//			pass_by_content.put("source",new GuideSourcesBO()
//					.setAudio_source(new VideoOrAudioSourceBO()
//							.setBundle_id(source.getUuid())
//							.setLayer_id(layer_id)
//							.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId()))
//					.setVideo_source(new VideoOrAudioSourceBO()
//							.setBundle_id(source.getUuid())
//							.setLayer_id(layer_id)
//							.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId()))
//						);
//			
//			passBy.setPass_by_content(pass_by_content);
//			LogicBO logic=new LogicBO();
//			logic.setPass_by(new ArrayList<PassByBO>());
//			logic.getPass_by().add(passBy);
//			executeBusiness.execute(logic,  "打开虚拟源编码");
//		}else if(source.getSourceType().equals(SourceType.KNAPSACK_5G)){
//			PassByBO passBy=new PassByBO();
//			passBy.setBundle_id(outputSetting.getUuid());
//			passBy.setLayer_id(layer_id);
//			passBy.setType("switchSource");
//			
//			JSONObject pass_by_content=new JSONObject();
//			pass_by_content.put("source",new GuideSourcesBO()
//					.setAudio_source(new VideoOrAudioSourceBO()
//							.setBundle_id(source.getSource())
//							.setLayer_id(layer_id)
//							.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId()))
//					.setVideo_source(new VideoOrAudioSourceBO()
//							.setBundle_id(source.getSource())
//							.setLayer_id(layer_id)
//							.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId()))
//						);
//			
//			passBy.setPass_by_content(pass_by_content);
//			LogicBO logic=new LogicBO();
//			logic.setPass_by(new ArrayList<PassByBO>());
//			logic.getPass_by().add(passBy);
//			executeBusiness.execute(logic,  "打开5G背包编码");
//		}
		
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
		List<SourcePO> sourcess=sourceDao.findByGuideIdOrderBySourceNumber(guideId);
		
		List<SourcePO> sourceList =new ArrayList<SourcePO>();
		for(SourcePO source:sourcess){
			if(source.getSource()!=null){
				sourceList.add(source);
			}
		}
		
		List<SourcePO> virtualSources=sourceList.stream().filter(source->{
			if(source.getSourceType().equals(SourceType.URL)){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		List<SourcePO> packageSources=new ArrayList<SourcePO>(sourceList);
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
		System.out.println("-------------------------删除输出源----------------------------------");
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic =closeEncoder( -1L, packageSources, codec);
		
		if(logic.getPass_by()==null){
			logic.setPass_by(new ArrayList<PassByBO>());
		}
		
		logic.getPass_by().addAll(deleteSources);
		
		//2.删除所有输出源
		//删除所有虚拟输出源
		List<OutputGroupPO> outputGroups=outputGroupDao.findByGuideId(guideId);
	 	if(outputGroups!=null&&outputGroups.get(0).getGuideId()!=null){
	 		
	 		List<OutputSettingPO> sourceOutputList=outputSettingDao.findByGroupId(outputGroups.get(0).getId());
	 		
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
	 	}
		
		//删除预监
		List<SourcePO> sources=sourceDao.findByIsPreviewOut(true);
		if(sources!=null){
			for(SourcePO source:sources){
				PassByBO pass=new PassByBO();
				pass.setBundle_id("preview_"+source.getUuid());
				pass.setLayer_id(layer_id);
				pass.setType("deleteAllBackupSources");
				
				JSONObject pass_by_content=new JSONObject();
				pass.setPass_by_content(pass_by_content);
				
				logic.getPass_by().add(pass);
			}
		}
//		//删除预监
		
		executeBusiness.execute(logic,  "删除虚拟源，输出源与5G背包");
	}
	
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
							    .setLayerId(layer_id)
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
				connectEncoderAudioChannel.setMode(TransmissionMode.MULTICAST.getCode()).setMulti_addr(audioAddr).setSrc_multi_ip(bundlePO.getMulticastSourceIp());
			}
			connectEncoderBundle.getChannels().add(connectEncoderAudioChannel);
			
//			connectEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectEncoderVideoChannel).add(connectEncoderAudioChannel).getList());
			logic.getConnectBundle().add(connectEncoderBundle);
		}
		
		return logic;
	}
	
	//缺少group
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
			DisconnectBundleBO disconnectEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
					             .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
					             .setBundleId(source.getSource())
					             .setBundle_type(bundlePO.getBundleType())
					             .setLayerId(layer_id);
			logic.getDisconnectBundle().add(disconnectEncoderBundle);
		}
		return logic;
	}
	
//	/**
//	 * 获取备份源对应的PassBy<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年9月24日 上午9:48:24
//	 * @param outputSource
//	 * @return
//	 */
//	public PassByBO getNewOutPutSettingPassBy(OutputSettingPO outputSource , OutputGroupPO outputGroup){
//
//		PassByBO passBy=new PassByBO();
//		
//		passBy.setBundle_id(outputSource.getUuid());
//		passBy.setLayer_id(layer_id);
//		passBy.setType("CreateTask");
//		
//		//sources开始
//		List<SourcePO> sourceList=sourceDao.findByGuideIdOrderBySourceNumber(outputGroup.getGuideId());
//		
////		List<OutputGroupPO> outputGroups=outputGroupDao.findByGuideId(outputSource.getg);
////	 	if(outputGroups!=null&&outputGroups.get(0).getGuideId()!=null){
////	 		
////	 		List<OutputSettingPO> sourceOutputList=outputSettingDao.findByGroupId(outputGroups.get(0).geId());
////	 	}
//	 		
//		List<SourcePO> sources=new ArrayList<SourcePO>();
//		for(SourcePO source:sourceList){
//			if(source.getSource()!=null){
//				sources.add(source);
//			}
//		}
//		
//		List<GuideSourcesBO> guideSources=new ArrayList<GuideSourcesBO>();
//		for(SourcePO sourcePo:sources){
//			
//			if(sourcePo.getSourceType().equals(SourceType.KNAPSACK_5G)){
//				GuideSourcesBO guideSourcesBo=new GuideSourcesBO();
//				guideSourcesBo.setAudio_source(new VideoOrAudioSourceBO()
//						.setBundle_id(sourcePo.getSource())
//						.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId())
//						.setLayer_id(layer_id)
//						.setTemplate_source_id("source1"))
//				.setVideo_source(new VideoOrAudioSourceBO()
//						.setBundle_id(sourcePo.getSource())
//						.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId())
//						.setLayer_id(layer_id)
//						.setTemplate_source_id("source12"));
//				guideSources.add(guideSourcesBo);
//			}else{
//				GuideSourcesBO guideSourcesBo=new GuideSourcesBO();
//				guideSourcesBo.setAudio_source(new VideoOrAudioSourceBO()
//						.setBundle_id(sourcePo.getUuid())
//						.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId())
//						.setLayer_id(layer_id).setTemplate_source_id("source1"))
//				.setVideo_source(new VideoOrAudioSourceBO()
//						.setBundle_id(sourcePo.getUuid())
//						.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId())
//						.setLayer_id(layer_id).setTemplate_source_id("source2"));
//				guideSources.add(guideSourcesBo);
//			}
//		}
//		//sources结束
//
////		oupput_array开始
//		List<GuideOutputArrayBO> GuideOutputArrays=new ArrayList<GuideOutputArrayBO>();
//		
//		GuideOutputArrayBO guideOutputArray=new GuideOutputArrayBO();
//		guideOutputArray.setUrl(outputSource.getOutputAddress())
//		                .setBitrate(outputSource.getBitrate())
//		                .setRate_ctrl(outputSource.getRateCtrl());
//		GuideOutputArrays.add(guideOutputArray);
////		oupput_array结束
//		
//		GuideSourceOutputBO guideSourceOutput=new GuideSourceOutputBO();
//		guideSourceOutput.setTask_temple("task_temple_name1")
//						.setMap_sources(guideSources);
//		
//		if(outputSource.getSwitchingMode()!=null){
//			if(SwitchingMode.TRANSCODE.equals(outputSource.getSwitchingMode())){
//				guideSourceOutput.setTask_common_type(outputSource.getSwitchingMode().getCode());
//			}else{
//				guideSourceOutput.setTask_common_type("STREAM");
//				guideSourceOutput.setTask_common_mode(outputSource.getSwitchingMode().getCode());
//			}
//		}
//		
//		guideSourceOutput.setMap_outputs(GuideOutputArrays);
//				
//		passBy.setPass_by_content(guideSourceOutput);
//		return passBy;
//	}
	
	/**
	 * 获取备份源对应的PassBy<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月24日 上午9:48:24
	 * @param outputSource
	 * @return
	 */
	public PassByBO getOutputSettingPassBy(OutputGroupPO outputGroup){

		PassByBO passBy=new PassByBO();
		
		passBy.setBundle_id(outputGroup.getUuid());
		passBy.setLayer_id(layer_id);
		passBy.setType("CreateTask");
		
		//sources开始
		List<SourcePO> sourceList=sourceDao.findByGuideIdOrderBySourceNumber(outputGroup.getGuideId());
		
//		if(sourceList==null){
//			return null;
//		}
	 		
		List<SourcePO> sources=new ArrayList<SourcePO>();
		for(SourcePO source:sourceList){
			if(source.getSource()!=null){
				sources.add(source);
			}
		}
		
		List<GuideOutputArrayBO> GuideOutputArrays=new ArrayList<GuideOutputArrayBO>();
		
		List<GuideSourcesBO> guideSources=new ArrayList<GuideSourcesBO>();
		for(SourcePO sourcePo:sources){
			
			if(sourcePo.getSourceType().equals(SourceType.KNAPSACK_5G)){
				GuideSourcesBO guideSourcesBo=new GuideSourcesBO();
				guideSourcesBo.setAudio_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getSource())
						.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId())
						.setLayer_id(layer_id)
						.setTemplate_source_id("source1"))
				.setVideo_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getSource())
						.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId())
						.setLayer_id(layer_id)
						.setTemplate_source_id("source12"));
				guideSources.add(guideSourcesBo);
			}else{
				GuideSourcesBO guideSourcesBo=new GuideSourcesBO();
				guideSourcesBo.setAudio_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getUuid())
						.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId())
						.setLayer_id(layer_id).setTemplate_source_id("source1"))
				.setVideo_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getUuid())
						.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId())
						.setLayer_id(layer_id).setTemplate_source_id("source2"));
				guideSources.add(guideSourcesBo);
			}
		}
		//sources结束

//			map_outputs开始
	 	if(outputGroup!=null&&outputGroup.getGuideId()!=null){
	 		
	 		List<OutputSettingPO> sourceOutputList=outputSettingDao.findByGroupId(outputGroup.getId());
	 		
	 		for(OutputSettingPO outputSource:sourceOutputList){

				GuideOutputArrayBO guideOutputArray=new GuideOutputArrayBO();
				guideOutputArray.setUrl(outputSource.getOutputAddress())
				                .setBitrate(outputSource.getBitrate())
				                .setRate_ctrl(outputSource.getRateCtrl());
				GuideOutputArrays.add(guideOutputArray);

	 		}
	 		
	 	}
//		map_outputs结束

		
		GuideSourceOutputBO guideSourceOutput=new GuideSourceOutputBO();
		guideSourceOutput.setTask_temple("task_temple_name1")
						.setMap_sources(guideSources);
		
		if(outputGroup.getSwitchingMode()!=null){
			if(SwitchingMode.TRANSCODE.equals(outputGroup.getSwitchingMode())){
				guideSourceOutput.setTask_common_type(outputGroup.getSwitchingMode().toString());
			}else{
				guideSourceOutput.setTask_common_type("STREAM");
				guideSourceOutput.setTask_common_mode(outputGroup.getSwitchingMode().toString());
			}
		}
		
		guideSourceOutput.setMap_outputs(GuideOutputArrays);
				
		passBy.setPass_by_content(guideSourceOutput);
		return passBy;
	}
	
	/**
	 * 预监<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月24日 下午7:20:50
	 * @return
	 */
	public List<PassByBO> getPreviewOutputPassBy(){
		
		List<SourcePO> sources=sourceDao.findByIsPreviewOut(true);
		List<PassByBO> passBys=new ArrayList<PassByBO>();
		
		for(SourcePO sourcePo:sources){
			PassByBO passBy=new PassByBO();
			
			passBy.setBundle_id("preview_"+sourcePo.getUuid());
			passBy.setLayer_id(layer_id);
			passBy.setType("CreateTask");
			
			List<GuideSourcesBO> guideSources=new ArrayList<GuideSourcesBO>();
			
			if(sourcePo.getSourceType().equals(SourceType.KNAPSACK_5G)){
				GuideSourcesBO guideSourcesBo=new GuideSourcesBO();
				guideSourcesBo.setAudio_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getSource())
						.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId())
						.setLayer_id(layer_id)
						.setTemplate_source_id("source1"))
				.setVideo_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getSource())
						.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId())
						.setLayer_id(layer_id)
						.setTemplate_source_id("source12"));
				guideSources.add(guideSourcesBo);
			}else{
				GuideSourcesBO guideSourcesBo=new GuideSourcesBO();
				guideSourcesBo.setAudio_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getUuid())
						.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId())
						.setLayer_id(layer_id).setTemplate_source_id("source1"))
				.setVideo_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getUuid())
						.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId())
						.setLayer_id(layer_id).setTemplate_source_id("source2"));
				guideSources.add(guideSourcesBo);
			}
			
			List<GuideOutputArrayBO> GuideOutputArrays=new ArrayList<GuideOutputArrayBO>();
			
			GuideOutputArrayBO guideOutputArray=new GuideOutputArrayBO();
			guideOutputArray.setUrl(sourcePo.getPreviewOut());
			GuideOutputArrays.add(guideOutputArray);
			
			GuideSourceOutputBO guideSourceOutput=new GuideSourceOutputBO();
			guideSourceOutput.setTask_temple("task_temple_name1")
							.setMap_sources(guideSources);
			
			guideSourceOutput.setTask_common_type("PASSBY");
			
			guideSourceOutput.setMap_outputs(GuideOutputArrays);
			
			passBy.setPass_by_content(guideSourceOutput);
			
			passBys.add(passBy);
		}
		return passBys;
	}
	
	enum Mode{
		CALLER("caller"),
		LISTENER("listener");
		
		private String code;
		
		private Mode(String code){
			this.code=code;
		}

		public String getCode() {
			return code;
		}

	}
}

