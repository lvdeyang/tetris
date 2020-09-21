package com.sumavision.tetris.guide.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
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
import com.sumavision.tetris.guide.BO.GuideOutputArrayBO;
import com.sumavision.tetris.guide.BO.GuideSourceOutputBO;
import com.sumavision.tetris.guide.BO.GuideSourcesBO;
import com.sumavision.tetris.guide.BO.GuideTaskArrayBO;
import com.sumavision.tetris.guide.BO.MediaArrayBO;
import com.sumavision.tetris.guide.BO.ProgramArrayBO;
import com.sumavision.tetris.guide.BO.UdpTsBO;
import com.sumavision.tetris.guide.BO.VideoOrAudioSourceBO;
import com.sumavision.tetris.guide.control.AudioParametersDAO;
import com.sumavision.tetris.guide.control.AudioParametersPO;
import com.sumavision.tetris.guide.control.GuideDAO;
import com.sumavision.tetris.guide.control.GuidePO;
import com.sumavision.tetris.guide.control.OutputSettingDAO;
import com.sumavision.tetris.guide.control.OutputSettingPO;
import com.sumavision.tetris.guide.control.SourceDAO;
import com.sumavision.tetris.guide.control.SourcePO;
import com.sumavision.tetris.guide.control.SourceType;
import com.sumavision.tetris.guide.control.VideoParametersDAO;
import com.sumavision.tetris.guide.control.VideoParametersPO;

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
		List<SourcePO> sourceList=sourceDao.findByGuideIdOrderBySourceNumber(guideId);
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
			pass_by_content.put("type","udp_ts");
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
			
			passBy.setBundle_id(outputSource.getUuid());
			passBy.setLayer_id(layer_id);
			passBy.setType("creatBackupSources");
			
			//sources开始
			List<SourcePO> sources=sourceDao.findByGuideIdOrderBySourceNumber(outputSource.getGuideId());
			List<GuideSourcesBO> guideSources=new ArrayList<GuideSourcesBO>();
			for(SourcePO sourcePo:sources){
				GuideSourcesBO guideSourcesBo=new GuideSourcesBO();
				guideSourcesBo.setAudio_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getUuid())
						.setChannel_id(ChannelType.AUDIOENCODE1.getChannelId())
						.setLayer_id(layer_id))
				.setVideo_source(new VideoOrAudioSourceBO()
						.setBundle_id(sourcePo.getUuid())
						.setChannel_id(ChannelType.VIDEOENCODE1.getChannelId())
						.setLayer_id(layer_id));
				guideSources.add(guideSourcesBo);
			}
			//sources结束
			
//			task_array开始
			List<GuideTaskArrayBO> guideTaskArrays=new ArrayList<GuideTaskArrayBO>();
			//这里应该是一个集合
			VideoParametersPO videoParametersPO= videoParametersDao.findByGuideId(outputSource.getGuideId());
			HashMap<String,Object> videoEncodeArray=new HashMap<String,Object>();
			HashMap<String,Object> videoParameter=new HashMap<String,Object>();
			AudioParametersPO audioParametersPO =auidoParametersDao.findByGuideId(outputSource.getGuideId());
			HashMap<String,Object> audioEncodeArray=new HashMap<String,Object>();
			HashMap<String,Object> audioParameter=new HashMap<String,Object>();
			JSONArray videoEncodeArrays=new JSONArray();
			JSONArray audioEncodeArrays=new JSONArray();
			
			videoEncodeArray.put("encode_id","ENCODE_ID_1");
//			videoParameter.put("profile", videoParametersPO.getProfile());
			videoParameter.put("fps", videoParametersPO.getFps());
			videoParameter.put("bitrate", videoParametersPO.getBitrate());
			videoParameter.put("resolution", videoParametersPO.getResolution());
			videoParameter.put("max_bitrate", videoParametersPO.getMaxBitrate());
			videoEncodeArray.put(videoParametersPO.getCodingObject().getName(),videoParameter);
			videoEncodeArrays.add(videoEncodeArray);
			GuideTaskArrayBO videoGuideTaskArrayBO=new GuideTaskArrayBO()
					.setId("TASK_ID_1")
					.setType("video")
					.setEncode_array(videoEncodeArrays);
			
			audioEncodeArray.put("encode_id", "ENCODE_ID_2");
//			audioParameter.put("sample_fmt", audioParametersPO.getSampleFmt());
			audioParameter.put("bitrate", audioParametersPO.getBitrate());
			audioParameter.put("type", audioParametersPO.getCodingType().getName());
			audioEncodeArray.put(audioParametersPO.getCodingFormat().getName(),audioParameter);
			audioEncodeArrays.add(audioEncodeArray);
			GuideTaskArrayBO audieGuideTaskArrayBO=new GuideTaskArrayBO()
					.setId("TASK_ID_2")
					.setType("audio")
					.setEncode_array(audioEncodeArrays);
			
			guideTaskArrays.add(videoGuideTaskArrayBO);
			guideTaskArrays.add(audieGuideTaskArrayBO);
//			task_array结束
			
//			oupput_array开始
			List<GuideOutputArrayBO> GuideOutputArrays=new ArrayList<GuideOutputArrayBO>();
			List<MediaArrayBO> mediaArrayBOs=new ArrayList<MediaArrayBO>();
			MediaArrayBO videoMediaArrayBO=new MediaArrayBO()
					.setEncode_id("ENCODE_ID_1")
					.setType("video")
					.setTask_id("TASK_ID_1");
			MediaArrayBO audioMediaArrayBO=new MediaArrayBO()
					.setEncode_id("ENCODE_ID_2")
					.setType("audio")
					.setTask_id("TASK_ID_2");
			mediaArrayBOs.add(videoMediaArrayBO);
			mediaArrayBOs.add(audioMediaArrayBO);
			
			List<ProgramArrayBO> programArrayBOs=new ArrayList<ProgramArrayBO>();
			ProgramArrayBO programArrayBO=new ProgramArrayBO()
					.setProgram_number(301)
					.setMedia_array(mediaArrayBOs);
			programArrayBOs.add(programArrayBO);
			
			String[] params=outputSource.getOutputAddress().split(":");
			String ip=params[1].replace("//", "");
			String port=params[2];
			UdpTsBO udpTsBo=new UdpTsBO()
					.setIp(ip)
					.setPort(Integer.parseInt(port))
					.setProgram_array(programArrayBOs)
					.setLocal_ip("192.165.56.18");
			
			GuideOutputArrayBO guideOutputArray=new GuideOutputArrayBO();
			guideOutputArray.setId("OUTPUT_ID").setUdp_ts(udpTsBo);
			GuideOutputArrays.add(guideOutputArray);
//			oupput_array结束
			
			GuideSourceOutputBO guideSourceOutput=new GuideSourceOutputBO();
			guideSourceOutput.setOutput_udp_url(outputSource.getOutputAddress());
			guideSourceOutput.setSources(guideSources);
			guideSourceOutput.setTask_array(guideTaskArrays);
			guideSourceOutput.setOutput_array(GuideOutputArrays);
			
			passBy.setPass_by_content(guideSourceOutput);
			return passBy;
		}).collect(Collectors.toList());
		LogicBO outputLogic=new LogicBO();
		if(outputLogic.getPass_by()==null){
			outputLogic.setPass_by(new ArrayList<PassByBO>());
		}
		outputLogic.getPass_by().addAll(outputPassBys);
//		executeBusiness.execute(outputLogic,  "打开输出编码");
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
	public void exchange(Long sourceId,Long guideId) throws Exception{
		//切换源
		//GuidePO guidePo=guideDao.findOne(guideId);
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
			logic.setPass_by(new ArrayList<PassByBO>());
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
			logic.setPass_by(new ArrayList<PassByBO>());
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
		List<SourcePO> sourceList=sourceDao.findByGuideIdOrderBySourceNumber(guideId);
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
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic =closeEncoder( -1L, packageSources, codec);
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
				connectEncoderAudioChannel.setMode(TransmissionMode.MULTICAST.getCode()).setMulti_addr(audioAddr);
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
					             .setLayerId(bundlePO.getAccessNodeUid());
			logic.getDisconnectBundle().add(disconnectEncoderBundle);
		}
		return logic;
	}
//	ChannelType.VIDEOENCODE1.getChannelId()
//	ChannelType.AUDIOENCODE1.getChannelId()
}
