package com.sumavision.tetris.sts.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.NameFilter;
import com.sumavision.tetris.sts.common.NodeIdManageUtil;
import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.device.auth.DeviceChannelAuthDao;
import com.sumavision.tetris.sts.task.createNode.CreateJsonInputNode;
import com.sumavision.tetris.sts.task.createNode.CreateJsonOutputNode;
import com.sumavision.tetris.sts.task.createNode.CreateJsonProgramNode;
import com.sumavision.tetris.sts.task.createNode.CreateJsonTaskNode;
import com.sumavision.tetris.sts.task.source.AudioElement;
import com.sumavision.tetris.sts.task.source.AudioParamPO;
import com.sumavision.tetris.sts.task.source.FilterIpSegment;
import com.sumavision.tetris.sts.task.source.InputPO;
import com.sumavision.tetris.sts.task.source.ProgramDao;
import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.source.SourceDao;
import com.sumavision.tetris.sts.task.source.SourcePO;
import com.sumavision.tetris.sts.task.source.SourcePO.SourceType;
import com.sumavision.tetris.sts.task.source.SubtitleElement;
import com.sumavision.tetris.sts.task.source.VideoElement;
import com.sumavision.tetris.sts.task.source.VideoParamPO;
import com.sumavision.tetris.sts.task.taskParamInput.Igmpv3;
import com.sumavision.tetris.sts.task.taskParamInput.Igmpv3Ip;
import com.sumavision.tetris.sts.task.taskParamInput.InputCommon;
import com.sumavision.tetris.sts.task.taskParamInput.InputNode;
import com.sumavision.tetris.sts.task.taskParamInput.InputNodeNormalMap;
import com.sumavision.tetris.sts.task.taskParamInput.InputProgramAudioBO;
import com.sumavision.tetris.sts.task.taskParamInput.InputProgramBO;
import com.sumavision.tetris.sts.task.taskParamInput.InputProgramSubtitleBO;
import com.sumavision.tetris.sts.task.taskParamInput.InputProgramVideoBO;
import com.sumavision.tetris.sts.task.taskParamInput.InputSrtBO;
import com.sumavision.tetris.sts.task.taskParamInput.InputUdpRtpBO;
import com.sumavision.tetris.sts.task.taskParamInput.InputUrlBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputNode;
import com.sumavision.tetris.sts.task.taskParamTask.AudioResampleBO;
import com.sumavision.tetris.sts.task.taskParamTask.DecodeProcess;
import com.sumavision.tetris.sts.task.taskParamTask.Encode;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeAudioCommon;
import com.sumavision.tetris.sts.task.taskParamTask.EncodePassbyBO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeVideoCommon;
import com.sumavision.tetris.sts.task.taskParamTask.EsSourceBO;
import com.sumavision.tetris.sts.task.taskParamTask.NameFiltersFor264265;
import com.sumavision.tetris.sts.task.taskParamTask.PassbySource;
import com.sumavision.tetris.sts.task.taskParamTask.RawSourceBO;
import com.sumavision.tetris.sts.task.taskParamTask.SourceCommon;
import com.sumavision.tetris.sts.task.taskParamTask.TaskEncodeProcess;
import com.sumavision.tetris.sts.task.taskParamTask.TaskFuzzyBO;
import com.sumavision.tetris.sts.task.taskParamTask.TaskLogoBO;
import com.sumavision.tetris.sts.task.taskParamTask.TaskNode;
import com.sumavision.tetris.sts.task.taskParamTask.TaskOsdBO;
import com.sumavision.tetris.sts.task.taskParamTask.TaskScaleBO;
import com.sumavision.tetris.sts.task.taskParamTask.TaskSubtitleBO;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;
import com.sumavision.tetris.sts.task.tasklink.TransTaskPO;

@Component
public class NodeUtil {
	@Autowired
	SourceDao sourceDao;
	@Autowired
	DeviceChannelAuthDao deviceChannelAuthDao;
	@Autowired
	ProgramDao programDao;
	@Autowired
	NodeIdManageUtil nodeIdManageUtil;
	
	private OutputMediaEncodeMessage outputMediaEncodeMessage;
	//记录创建task时生成TaskNodeId，方便回滚删除时使用
	private List<String> delTaskNodeIds;
	//记录创建output时生成OutputNodeId，方便回滚删除时使用
	private List<String> delOutputNodeIds;

	
	public ArrayList<InputNode> getInputArray(InputPO inputPO, ProgramPO programPO){
		SourcePO sourcePO = sourceDao.findOne(inputPO.getSourceId());
		ArrayList<InputNode> inputNodes = new ArrayList<InputNode>();
		InputNode inputNode = new InputNode();
		InputNodeNormalMap normal_map = new InputNodeNormalMap();
		inputNode.setNormal_map(normal_map);
		
		//program_array
		ArrayList<InputProgramBO> program_array = new ArrayList<InputProgramBO>();
		InputProgramBO createInputProgramBO = new InputProgramBO();
		createInputProgramBO.setName(programPO.getProgramName());
		createInputProgramBO.setPcr_pid(programPO.getPcrPid());
		createInputProgramBO.setProgram_number(programPO.getProgramNum());
		createInputProgramBO.setProvider(programPO.getProgramProvider());
		createInputProgramBO.setPmt_pid(programPO.getPmtPid());
		// 暂时协议没有pmt
//		createInputProgramBO.setSubtitle_array(JSON.parseArray(programPO.getSubtitleJson(), ProgramSubtitleBO.class));
//		createInputProgramBO.setAudio_array(JSON.parseArray(programPO.getAudioJson(), ProgramAudioBO.class));
//		createInputProgramBO.setVideo_array(JSON.parseArray(programPO.getVideoJson(), ProgramVideoBO.class));

		//decode_mode修改，后续在页面上添加后修改
		List<InputProgramVideoBO> video_array = JSON.parseArray(programPO.getVideoJson(), InputProgramVideoBO.class);
		for (InputProgramVideoBO programVideoBO : video_array) {
			programVideoBO.setDecode_mode("cpu");
			programVideoBO.setNv_card_idx(0);
		}
		
		List<InputProgramAudioBO> audio_array = JSON.parseArray(programPO.getAudioJson(), InputProgramAudioBO.class);
		for (InputProgramAudioBO programAudioBO : audio_array) {
			programAudioBO.setDecode_mode("cpu");
		}
		
		if (programPO.getSubtitleJson()!=null) {
			List<InputProgramSubtitleBO> subtitle_array = JSON.parseArray(programPO.getSubtitleJson(), InputProgramSubtitleBO.class);
			for (InputProgramSubtitleBO programSubtitleBO : subtitle_array) {
				programSubtitleBO.setDecode_mode("cpu");
			}
			createInputProgramBO.setSubtitle_array(subtitle_array);
		}
		
		createInputProgramBO.setAudio_array(audio_array);
		createInputProgramBO.setVideo_array(video_array);
		
		program_array.add(createInputProgramBO);
		
		inputNode.setProgram_array(program_array);
		inputNode.setId(inputPO.getNodeId().toString());
		inputNode.setInputCommon(getInputCommon(sourcePO));
		inputNodes.add(inputNode);
		return inputNodes;
	}
	
	
	
	public ArrayList<TaskNode> getTaskArray(TransTaskPO transTaskPO){
		SourcePO sourcePO = sourceDao.findOne(transTaskPO.getSourceId());
		outputMediaEncodeMessage = new OutputMediaEncodeMessage();
		delTaskNodeIds = new ArrayList<String>();
		ArrayList<TaskNode> task_array = new ArrayList<TaskNode>();
		List<NameFilter> nameFilters = new ArrayList<NameFilter>();
		
		Integer cardNumber = deviceChannelAuthDao.findOne(transTaskPO.getDeviceChannelId()).getCardNumber();
		ProgramPO programPO = programDao.findBySourceIdAndNum(transTaskPO.getSourceId(), transTaskPO.getProgramNum());
		
		List<VideoParamPO> videoParamPOList = new ArrayList<VideoParamPO>(transTaskPO.getVideoParams());
		List<AudioParamPO> audioParamPOList = new ArrayList<AudioParamPO>(transTaskPO.getAudioParams());
		Collections.sort(videoParamPOList);
		Collections.sort(audioParamPOList);
		if (videoParamPOList.size() >= 1) {
			//确定创建任务时，创建几个video和audio
			Map<Integer, Boolean> videoPidMap = getVideoPidMap(programPO.getVideoElements(), videoParamPOList);
			
			ArrayList<String> videoEncodeId = new ArrayList<String>();
			ArrayList<String> videoTaskId = new ArrayList<String>();
		
			for (VideoElement videoElement : programPO.getVideoElements()) {
				if (!videoPidMap.get(videoElement.getPid())) {
					continue;
				}
				
				TaskNode videoTaskNode = new TaskNode();
				videoTaskNode.setId(nodeIdManageUtil.getNewNodeId().toString());
				if (transTaskPO.getTaskType()==0) {
					//网关任务
					videoTaskNode.setType("passby");
				}else {
					videoTaskNode.setType("video");
				}
				videoTaskNode.setSourceCommon(getSourceCommon(transTaskPO, videoElement.getPid()));
				delTaskNodeIds.add(videoTaskNode.getId());
				
				if (transTaskPO.getTaskType() == 1) {
					NameFilter sourceCommonNameFilter = new NameFilter() {
						@Override
						public String process(Object object, String name, Object value) {
							// TODO Auto-generated method stub
							if (value instanceof SourceCommon) {
								return "raw_source";
							}
							return name;
						}
					};
					nameFilters.add(sourceCommonNameFilter);
				}else {
					NameFilter sourceCommonNameFilter = new NameFilter() {
						@Override
						public String process(Object object, String name, Object value) {
							// TODO Auto-generated method stub
							if (value instanceof SourceCommon) {
								switch (sourcePO.getSourceType()) {
							        case PASSBY:
										return "passby_source";
									case SDI:
										return "raw_source";
									case STREAM:
										return "es_source";
									default:
										return "es_source";
								}
							}
							return name;
						}
					};
					nameFilters.add(sourceCommonNameFilter);
				}
				

				if (transTaskPO.getTaskType() == 1) {
					ArrayList<DecodeProcess> decode_process_array = new ArrayList<DecodeProcess>();
					DecodeProcess decodeProcess = new DecodeProcess();
					// 去交错plat参数暂时默认cpu，后期需要在集群中添加该功能cpu/msdk/cuda
//					TaskDeinterlaceBO deinterlace = new TaskDeinterlaceBO(programPO.getDeinterlaceMode(), "cpu", cardNumber);
//					decodeProcess.setDeinterlace(deinterlace);
					decode_process_array.add(decodeProcess);
					videoTaskNode.setDecode_process_array(decode_process_array);
				}

				ArrayList<Encode> encode_array = new ArrayList<Encode>();
				for (VideoParamPO videoParamPO : videoParamPOList) {
					if (videoElement.getPid().equals(videoParamPO.getPid())) {
						Encode encode = new Encode();
						encode.setEncode_id(nodeIdManageUtil.getNewNodeId().toString());
						videoEncodeId.add(encode.getEncode_id());
						videoTaskId.add(videoTaskNode.getId());
						
						if (transTaskPO.getTaskType() == 1) {
							NameFiltersFor264265 nameFiltersFor264265 = getEncodeCommon(transTaskPO, videoParamPO, null);
							encode.setEncodeCommon(nameFiltersFor264265.getEncodeCommon());
							nameFilters.addAll(nameFiltersFor264265.getNameFilters());
							NameFilter nameFilter = new NameFilter() {
								@Override
								public String process(Object object, String name, Object value) {
									// TODO Auto-generated method stub
									if (value instanceof EncodeVideoCommon) {
										switch (videoParamPO.getCodec()) {
									        case "h264":
												return "h264";
											case "h265":
												return "hevc";
											case "mpeg2":
											case "mpeg2-video":
												return "mpeg2";
											case "avs":
												return "avs_plus";
										}
									}
									return name;
								}
							};
							nameFilters.add(nameFilter);
							
							ArrayList<TaskEncodeProcess> process_array = new ArrayList<TaskEncodeProcess>();
							
							TaskEncodeProcess osdTaskEncodeProcess = null;
							TaskEncodeProcess enhanceTaskEncodeProcess = new TaskEncodeProcess();
							TaskEncodeProcess scaleTaskEncodeProcess = new TaskEncodeProcess();
							TaskEncodeProcess cutTaskEncodeProcess = new TaskEncodeProcess();
							//osd
							String osdJsonStr = videoParamPO.getOsdJson();
							List<OsdCfgBO> osdList = OsdCfgBO.transFromJson(osdJsonStr);
							if (null != osdList && osdList.size()>=1) {
								osdTaskEncodeProcess = new TaskEncodeProcess();
								ArrayList<TaskOsdBO> osd = new ArrayList<TaskOsdBO>();
								ArrayList<TaskSubtitleBO> subtile = new ArrayList<TaskSubtitleBO>();
								ArrayList<TaskLogoBO> logo = new ArrayList<TaskLogoBO>();
								ArrayList<TaskFuzzyBO> fuzzy = new ArrayList<TaskFuzzyBO>();
								for (OsdCfgBO osdBo : osdList) {
									if (osdBo.getOsdType().contains("logo")) {
										// 1表示cpu优先，选择cpu优先还是内存优先，集群暂时未开发
										TaskLogoBO taskLogoBO = new TaskLogoBO(
												osdBo.getPosition(), osdBo.getZone(),
												osdBo.getPicUrl() == null ? null : osdBo.getPicUrl().replace("ftp://", ""),
												1);
										logo.add(taskLogoBO);
									} else if (osdBo.getOsdType().contains("subtitle")) {
										Boolean showBkg = false;
										Boolean showOutline = false;
										if (osdBo.getShowBkg().equals("Yes")) {
											showBkg = true;
										}
										if (osdBo.getShowOutLine().equals("Yes")) {
											showOutline = true;
										}
										TaskSubtitleBO taskSubtitleBO = new TaskSubtitleBO(
												osdBo.getTrackType(),
												osdBo.getTextContent(),
												osdBo.getFontType(), osdBo.getPosition(),
												osdBo.getZone(), osdBo.getFontColor(),
												osdBo.getTrackSpeed().intValue(),
												osdBo.getFontSize(), showBkg, showOutline,
												osdBo.getBkgColor(),
												osdBo.getOutLineColor());
										subtile.add(taskSubtitleBO);
									} else {
										//5表示mosaic_radius范围为5，集群暂未开发
										TaskFuzzyBO taskFuzzyBO = new TaskFuzzyBO(
												osdBo.getPosition(), 
												osdBo.getZone(), 
												osdBo.getFuzzyEffect(), 
												5);
										fuzzy.add(taskFuzzyBO);
									}
								}
								TaskOsdBO taskOsdBO = new TaskOsdBO(subtile, logo, fuzzy);
								osd.add(taskOsdBO);
								osdTaskEncodeProcess.setOsd(osd);
							}
							
							//enhance 图像增强
//							TaskEnhanceBO enhance = new TaskEnhanceBO(
//									videoParamPO.getBrightness(), 
//									Integer.parseInt(videoParamPO.getContrast()), 
//									Integer.parseInt(videoParamPO.getSaturation()), 
//									videoParamPO.getDenoise(), 
//									videoParamPO.getSharpen(), 
//									videoParamPO.getColorSpace(), 
//									videoParamPO.getColorTransfer(), 
//									videoParamPO.getColorPrimary(), 
//									videoParamPO.getColorRange());
							TaskScaleBO scale = new TaskScaleBO(
									videoParamPO.getWidth(), 
									videoParamPO.getHeight(), 
									"cpu", 
									videoParamPO.getScaleMode(), 
									videoParamPO.getRatio(), 
									cardNumber);
//							if (null!=videoParamPO.getCutRight()) {
//								TaskCutBO cut = new TaskCutBO(
//										Integer.parseInt(videoParamPO.getCutRight())-Integer.parseInt(videoParamPO.getCutLeft()), 
//										Integer.parseInt(videoParamPO.getCutBottom())-Integer.parseInt(videoParamPO.getCutTop()), 
//										Integer.parseInt(videoParamPO.getCutLeft()), 
//										Integer.parseInt(videoParamPO.getCutTop()));
//								cutTaskEncodeProcess.setCut(cut);
//							}
//							
//							enhanceTaskEncodeProcess.setEnhance(enhance);
							scaleTaskEncodeProcess.setScale(scale);
							
							if (osdTaskEncodeProcess!=null) {
								process_array.add(osdTaskEncodeProcess);
							}
							process_array.add(enhanceTaskEncodeProcess);
							process_array.add(scaleTaskEncodeProcess);
							process_array.add(cutTaskEncodeProcess);
							encode.setProcess_array(process_array);
						}else {
							encode.setEncodeCommon(new EncodePassbyBO());
							NameFilter nameFilter = new NameFilter() {
								@Override
								public String process(Object object, String name, Object value) {
									// TODO Auto-generated method stub
									if (transTaskPO.getTaskType() == 0) {
										return "passby";
									}
									return name;
								}
							};
							nameFilters.add(nameFilter);
						}
						
						encode_array.add(encode);
					}
				}
				videoTaskNode.setEncode_array(encode_array);
				task_array.add(videoTaskNode);
			}
			outputMediaEncodeMessage.setVideoEncodeId(videoEncodeId);
			outputMediaEncodeMessage.setVideoTaskId(videoTaskId);
		}
		
		
		if (audioParamPOList.size() >= 1) {
			ArrayList<String> audioEncodeId = new ArrayList<String>();
			ArrayList<String> audioTaskId = new ArrayList<String>();
			//确定创建任务时，创建几个audio
			Map<Integer, Boolean> audioPidMap = getAudioPidMap(programPO.getAudioElements(), audioParamPOList);
			
			for (AudioElement audioElement : programPO.getAudioElements()) {
				if (!audioPidMap.get(audioElement.getPid())) {
					continue;
				}
				
				TaskNode audioTaskNode = new TaskNode();
				audioTaskNode.setId(nodeIdManageUtil.getNewNodeId().toString());
				if (transTaskPO.getTaskType() == 1) {
					audioTaskNode.setType("audio");
				}else {
					audioTaskNode.setType("passby");
				}
				
				audioTaskNode.setSourceCommon(getSourceCommon(transTaskPO, audioElement.getPid()));
				delTaskNodeIds.add(audioTaskNode.getId());
				
				ArrayList<Encode> encode_array = new ArrayList<Encode>();
				for (AudioParamPO audioParamPO : audioParamPOList) {
					ArrayList<TaskEncodeProcess> process_array = new ArrayList<TaskEncodeProcess>();
					Encode encode = new Encode();
					encode.setEncode_id(nodeIdManageUtil.getNewNodeId().toString());
					audioEncodeId.add(encode.getEncode_id());
					audioTaskId.add(audioTaskNode.getId());
					if (transTaskPO.getTaskType() == 1) {
						NameFiltersFor264265 nameFiltersFor264265 = getEncodeCommon(transTaskPO, null, audioParamPO);
						encode.setEncodeCommon(nameFiltersFor264265.getEncodeCommon());
						nameFilters.addAll(nameFiltersFor264265.getNameFilters());
						
						NameFilter nameFilter = new NameFilter() {
							@Override
							public String process(Object object, String name, Object value) {
								// TODO Auto-generated method stub
								if (value instanceof EncodeAudioCommon) {
									switch (audioParamPO.getCodec()) {
										case "aac":
										case "he-aac":
										case "he-aac-v2":
								            return "aac";
								        case "mp3":
								            return "mp3";
								        case "mpeg2-audio":
								        case "mp2":
								        	 return "mp2";
								        case "dra":
								            return "dra";
								        case "ac3":
								        case "eac3":
								            return "dolby";
									}
								}
								return name;
							}
						};
						nameFilters.add(nameFilter);
						TaskEncodeProcess audioTaskEncodeProcess = new TaskEncodeProcess(); 
//						AudioProcessBO audioProcessBO = new AudioProcessBO(
//								audioParamPO.getSample().intValue(), 
//								audioParamPO.getCodec(), 
//								audioParamPO.getVolume().intValue(), 
//								audioParamPO.getChLayout(), 
//								audioParamPO.getDenoise(), 
//								audioParamPO.getAudioDupMode(), 
//								audioParamPO.getSample().intValue(),
//								audioParamPO.getAgcGain());
//						audioTaskEncodeProcess.setAudioProcess(audioProcessBO);
//						
						AudioResampleBO resample = new AudioResampleBO(audioParamPO.getSample().intValue(), audioParamPO.getChLayout(), "s16");
						audioTaskEncodeProcess.setResample(resample);
						
						process_array.add(audioTaskEncodeProcess);
						encode.setProcess_array(process_array);
						
					}else {
						encode.setEncodeCommon(new EncodePassbyBO());
						NameFilter nameFilter = new NameFilter() {
							@Override
							public String process(Object object, String name, Object value) {
								// TODO Auto-generated method stub
								if (transTaskPO.getTaskType() == 0) {
									return "passby";
								}
								return name;
							}
						};
						nameFilters.add(nameFilter);
					}
					encode_array.add(encode);
				}
				audioTaskNode.setEncode_array(encode_array);
				task_array.add(audioTaskNode);
			}
			outputMediaEncodeMessage.setAudioEncodeId(audioEncodeId);
			outputMediaEncodeMessage.setAudioTaskId(audioTaskId);
		}
			
		List<SubtitleElement> programSubtitles = programDao.findBySourceIdAndNum(transTaskPO.getSourceId(), transTaskPO.getProgramNum()).getSubtitleElements();
		if (programSubtitles!=null && programSubtitles.size() >= 1) {
			ArrayList<String> subtitleEncodeId = new ArrayList<String>();
			ArrayList<String> subtitleTaskId = new ArrayList<String>();
			for (SubtitleElement subtitleElement : programSubtitles) {
				TaskNode subtitleTaskNode = new TaskNode();
				subtitleTaskNode.setId(nodeIdManageUtil.getNewNodeId().toString());
				delTaskNodeIds.add(subtitleTaskNode.getId());
				//字幕轨无编码
				subtitleEncodeId.add("");
				subtitleTaskId.add(subtitleTaskNode.getId());
				
				if (transTaskPO.getTaskType() == 1) {
					subtitleTaskNode.setType("subtitle");
				}else {
					subtitleTaskNode.setType("passby");
				}
				
				subtitleTaskNode.setSourceCommon(getSourceCommon(transTaskPO, programSubtitles.get(0).getPid()));
				task_array.add(subtitleTaskNode);
			}
			outputMediaEncodeMessage.setSubtitleEncodeId(subtitleEncodeId);
			outputMediaEncodeMessage.setSubtitleTaskId(subtitleTaskId);
		}
		
		return task_array;
	}
	
	
	
	public JSONObject getJsonInputNode(InputPO inputPO, ProgramPO programPO) {
		SourcePO sourcePO = sourceDao.findOne(inputPO.getSourceId());
		CreateJsonInputNode createInputNode = new CreateJsonInputNode();
		ArrayList<InputNode> inputNodes = new ArrayList<InputNode>();
		InputNode inputNode = new InputNode();
		
		ArrayList<InputProgramBO> program_array = new ArrayList<InputProgramBO>();

		InputProgramBO createInputProgramBO = new InputProgramBO();
		createInputProgramBO.setName(programPO.getProgramName());
		createInputProgramBO.setPcr_pid(programPO.getPcrPid());
		createInputProgramBO.setProgram_number(programPO.getProgramNum());
		createInputProgramBO.setProvider(programPO.getProgramProvider());
		createInputProgramBO.setPmt_pid(programPO.getPmtPid());
		// 暂时协议没有pmt
//		createInputProgramBO.setSubtitle_array(JSON.parseArray(programPO.getSubtitleJson(), ProgramSubtitleBO.class));
//		createInputProgramBO.setAudio_array(JSON.parseArray(programPO.getAudioJson(), ProgramAudioBO.class));
//		createInputProgramBO.setVideo_array(JSON.parseArray(programPO.getVideoJson(), ProgramVideoBO.class));

		//decode_mode修改，后续在页面上添加后修改
		List<InputProgramVideoBO> video_array = JSON.parseArray(programPO.getVideoJson(), InputProgramVideoBO.class);
		for (InputProgramVideoBO programVideoBO : video_array) {
			programVideoBO.setDecode_mode("cpu");
			programVideoBO.setNv_card_idx(0);
		}
		
		List<InputProgramAudioBO> audio_array = JSON.parseArray(programPO.getAudioJson(), InputProgramAudioBO.class);
		for (InputProgramAudioBO programAudioBO : audio_array) {
			programAudioBO.setDecode_mode("cpu");
		}
		
		if (programPO.getSubtitleJson()!=null) {
			List<InputProgramSubtitleBO> subtitle_array = JSON.parseArray(programPO.getSubtitleJson(), InputProgramSubtitleBO.class);
			for (InputProgramSubtitleBO programSubtitleBO : subtitle_array) {
				programSubtitleBO.setDecode_mode("cpu");
			}
			createInputProgramBO.setSubtitle_array(subtitle_array);
		}
		
		createInputProgramBO.setAudio_array(audio_array);
		createInputProgramBO.setVideo_array(video_array);
		program_array.add(createInputProgramBO);
		inputNode.setProgram_array(program_array);
		
		InputNodeNormalMap normal_map = new InputNodeNormalMap();
		inputNode.setNormal_map(normal_map);
		inputNode.setId(inputPO.getNodeId().toString());
		inputNode.setInputCommon(getInputCommon(sourcePO));
		inputNodes.add(inputNode);
		createInputNode.setInput_array(inputNodes);
		return getCreateInputNode(createInputNode, sourcePO.getProtoType());
	}
	
	
	
//	public JSONObject getJsonDelInputNode(InputPO inputPO){
//		DelJsonInputNode delJsonInputNode = new DelJsonInputNode();
//		ArrayList<InputNode> inputNodes = new ArrayList<InputNode>();
//		InputNode inputNode = new InputNode();
//		inputNode.setId(inputPO.getNodeId().toString());
//		inputNodes.add(inputNode);
//		delJsonInputNode.setInput_array(inputNodes);
//		
//		return JSONObject.parseObject(JSONObject.toJSON(delJsonInputNode).toString());
//	}
	
	
//	public JSONObject getJsonDelTaskNode(TransTaskPO transTaskPO){
//		DelJsonTaskNode delJsonTaskNode = new DelJsonTaskNode();
//		ArrayList<TaskNode> task_array = new ArrayList<TaskNode>();
//		for (String id : delTaskNodeIds) {
//			TaskNode taskNode = new TaskNode();
//			taskNode.setId(id);
//			task_array.add(taskNode);
//		}
//		delJsonTaskNode.setTask_array(task_array);
//		return JSONObject.parseObject(JSONObject.toJSON(delJsonTaskNode).toString());
//	}
	
//	public JSONObject getJsonDelOutputsNode(List<OutputPO> outputPOs){
//		DelJsonOutputNode delJsonOutputNode = new DelJsonOutputNode();
//		ArrayList<OutputNode> output_array = new ArrayList<OutputNode>();
//		for (String id : delOutputNodeIds) {
//			OutputNode outputNode = new OutputNode();
//			outputNode.setId(id);
//			output_array.add(outputNode);
//		}
//		delJsonOutputNode.setOutput_array(output_array);
//		return JSONObject.parseObject(JSONObject.toJSON(delJsonOutputNode).toString());
//	}

	public JSONObject getJsonProgramNode(ProgramPO programPO) {
		CreateJsonProgramNode createJsonProgramNode = new CreateJsonProgramNode();
		ArrayList<InputProgramBO> program_array = new ArrayList<InputProgramBO>();

		InputProgramBO createInputProgramBO = new InputProgramBO();
		createInputProgramBO.setName(programPO.getProgramName());
		createInputProgramBO.setPcr_pid(programPO.getPcrPid());
		createInputProgramBO.setProgram_number(programPO.getProgramNum());
		createInputProgramBO.setProvider(programPO.getProgramProvider());
		createInputProgramBO.setPmt_pid(programPO.getPmtPid());
		// 暂时协议没有pmt
//		createInputProgramBO.setSubtitle_array(JSON.parseArray(programPO.getSubtitleJson(), ProgramSubtitleBO.class));
//		createInputProgramBO.setAudio_array(JSON.parseArray(programPO.getAudioJson(), ProgramAudioBO.class));
//		createInputProgramBO.setVideo_array(JSON.parseArray(programPO.getVideoJson(), ProgramVideoBO.class));

		//decode_mode修改，后续在页面上添加后修改
		List<InputProgramVideoBO> video_array = JSON.parseArray(programPO.getVideoJson(), InputProgramVideoBO.class);
		for (InputProgramVideoBO programVideoBO : video_array) {
			programVideoBO.setDecode_mode("cpu");
			programVideoBO.setNv_card_idx(0);
		}
		
		List<InputProgramAudioBO> audio_array = JSON.parseArray(programPO.getAudioJson(), InputProgramAudioBO.class);
		for (InputProgramAudioBO programAudioBO : audio_array) {
			programAudioBO.setDecode_mode("cpu");
		}
		
		if (programPO.getSubtitleJson()!=null) {
			List<InputProgramSubtitleBO> subtitle_array = JSON.parseArray(programPO.getSubtitleJson(), InputProgramSubtitleBO.class);
			for (InputProgramSubtitleBO programSubtitleBO : subtitle_array) {
				programSubtitleBO.setDecode_mode("cpu");
			}
			createInputProgramBO.setSubtitle_array(subtitle_array);
		}
		
		createInputProgramBO.setAudio_array(audio_array);
		createInputProgramBO.setVideo_array(video_array);
		
		program_array.add(createInputProgramBO);
		createJsonProgramNode.setProgram_array(program_array);

		return JSONObject.parseObject(JSONObject.toJSON(createJsonProgramNode).toString());
	}

//	public JSONObject getJsonDelProgramNode(ProgramPO programPO){
//		DelJsonProgramNode delJsonProgramNode = new DelJsonProgramNode();
//		ArrayList<CreateInputProgramBO> program_array = new ArrayList<CreateInputProgramBO>();
//		CreateInputProgramBO createInputProgramBO = new CreateInputProgramBO();
//		createInputProgramBO.setProgram_number(programPO.getProgramNum());
//		program_array.add(createInputProgramBO);
//		delJsonProgramNode.setProgram_array(program_array);
//		return JSONObject.parseObject(JSONObject.toJSON(delJsonProgramNode).toString());
//	}
	
	public JSONObject getJsonTaskNode(TransTaskPO transTaskPO) {
		SourcePO sourcePO = sourceDao.findOne(transTaskPO.getSourceId());
		
		CreateJsonTaskNode createJsonTaskNode = new CreateJsonTaskNode();
		outputMediaEncodeMessage = new OutputMediaEncodeMessage();
		delTaskNodeIds = new ArrayList<String>();
		ArrayList<TaskNode> task_array = new ArrayList<TaskNode>();
		List<NameFilter> nameFilters = new ArrayList<NameFilter>();
		
		Integer cardNumber = deviceChannelAuthDao.findOne(transTaskPO.getDeviceChannelId()).getCardNumber();
		ProgramPO programPO = programDao.findBySourceIdAndNum(transTaskPO.getSourceId(), transTaskPO.getProgramNum());
		
		List<VideoParamPO> videoParamPOList = new ArrayList<VideoParamPO>(transTaskPO.getVideoParams());
		List<AudioParamPO> audioParamPOList = new ArrayList<AudioParamPO>(transTaskPO.getAudioParams());
		Collections.sort(videoParamPOList);
		Collections.sort(audioParamPOList);
		if (videoParamPOList.size() >= 1) {
			//确定创建任务时，创建几个video和audio
			Map<Integer, Boolean> videoPidMap = getVideoPidMap(programPO.getVideoElements(), videoParamPOList);
			
			ArrayList<String> videoEncodeId = new ArrayList<String>();
			ArrayList<String> videoTaskId = new ArrayList<String>();
		
			for (VideoElement videoElement : programPO.getVideoElements()) {
				if (!videoPidMap.get(videoElement.getPid())) {
					continue;
				}
				
				TaskNode videoTaskNode = new TaskNode();
				videoTaskNode.setId(nodeIdManageUtil.getNewNodeId().toString());
				if (transTaskPO.getTaskType()==0) {
					//网关任务
					videoTaskNode.setType("passby");
				}else {
					videoTaskNode.setType("video");
				}
				videoTaskNode.setSourceCommon(getSourceCommon(transTaskPO, videoElement.getPid()));
				delTaskNodeIds.add(videoTaskNode.getId());
				
				if (transTaskPO.getTaskType() == 1) {
					NameFilter sourceCommonNameFilter = new NameFilter() {
						@Override
						public String process(Object object, String name, Object value) {
							// TODO Auto-generated method stub
							if (value instanceof SourceCommon) {
								return "raw_source";
							}
							return name;
						}
					};
					nameFilters.add(sourceCommonNameFilter);
				}else {
					NameFilter sourceCommonNameFilter = new NameFilter() {
						@Override
						public String process(Object object, String name, Object value) {
							// TODO Auto-generated method stub
							if (value instanceof SourceCommon) {
								switch (sourcePO.getSourceType()) {
							        case PASSBY:
										return "passby_source";
									case SDI:
										return "raw_source";
									case STREAM:
										return "es_source";
									default:
										return "es_source";
								}
							}
							return name;
						}
					};
					nameFilters.add(sourceCommonNameFilter);
				}
				

				if (transTaskPO.getTaskType() == 1) {
					ArrayList<DecodeProcess> decode_process_array = new ArrayList<DecodeProcess>();
					DecodeProcess decodeProcess = new DecodeProcess();
					// 去交错plat参数暂时默认cpu，后期需要在集群中添加该功能cpu/msdk/cuda
//					TaskDeinterlaceBO deinterlace = new TaskDeinterlaceBO(programPO.getDeinterlaceMode(), "cpu", cardNumber);
//					decodeProcess.setDeinterlace(deinterlace);
					decode_process_array.add(decodeProcess);
					videoTaskNode.setDecode_process_array(decode_process_array);
				}

				ArrayList<Encode> encode_array = new ArrayList<Encode>();
				for (VideoParamPO videoParamPO : videoParamPOList) {
					if (videoElement.getPid().equals(videoParamPO.getPid())) {
						Encode encode = new Encode();
						encode.setEncode_id(nodeIdManageUtil.getNewNodeId().toString());
						videoEncodeId.add(encode.getEncode_id());
						videoTaskId.add(videoTaskNode.getId());
						
						if (transTaskPO.getTaskType() == 1) {
							NameFiltersFor264265 nameFiltersFor264265 = getEncodeCommon(transTaskPO, videoParamPO, null);
							encode.setEncodeCommon(nameFiltersFor264265.getEncodeCommon());
							nameFilters.addAll(nameFiltersFor264265.getNameFilters());
							NameFilter nameFilter = new NameFilter() {
								@Override
								public String process(Object object, String name, Object value) {
									// TODO Auto-generated method stub
									if (value instanceof EncodeVideoCommon) {
										switch (videoParamPO.getCodec()) {
									        case "h264":
												return "h264";
											case "h265":
												return "hevc";
											case "mpeg2":
											case "mpeg2-video":
												return "mpeg2";
											case "avs":
												return "avs_plus";
										}
									}
									return name;
								}
							};
							nameFilters.add(nameFilter);
							
							ArrayList<TaskEncodeProcess> process_array = new ArrayList<TaskEncodeProcess>();
							
							TaskEncodeProcess osdTaskEncodeProcess = null;
							TaskEncodeProcess enhanceTaskEncodeProcess = new TaskEncodeProcess();
							TaskEncodeProcess scaleTaskEncodeProcess = new TaskEncodeProcess();
							TaskEncodeProcess cutTaskEncodeProcess = new TaskEncodeProcess();
							//osd
							String osdJsonStr = videoParamPO.getOsdJson();
							List<OsdCfgBO> osdList = OsdCfgBO.transFromJson(osdJsonStr);
							if (null != osdList && osdList.size()>=1) {
								osdTaskEncodeProcess = new TaskEncodeProcess();
								ArrayList<TaskOsdBO> osd = new ArrayList<TaskOsdBO>();
								ArrayList<TaskSubtitleBO> subtile = new ArrayList<TaskSubtitleBO>();
								ArrayList<TaskLogoBO> logo = new ArrayList<TaskLogoBO>();
								ArrayList<TaskFuzzyBO> fuzzy = new ArrayList<TaskFuzzyBO>();
								for (OsdCfgBO osdBo : osdList) {
									if (osdBo.getOsdType().contains("logo")) {
										// 1表示cpu优先，选择cpu优先还是内存优先，集群暂时未开发
										TaskLogoBO taskLogoBO = new TaskLogoBO(
												osdBo.getPosition(), osdBo.getZone(),
												osdBo.getPicUrl() == null ? null : osdBo.getPicUrl().replace("ftp://", ""),
												1);
										logo.add(taskLogoBO);
									} else if (osdBo.getOsdType().contains("subtitle")) {
										Boolean showBkg = false;
										Boolean showOutline = false;
										if (osdBo.getShowBkg().equals("Yes")) {
											showBkg = true;
										}
										if (osdBo.getShowOutLine().equals("Yes")) {
											showOutline = true;
										}
										TaskSubtitleBO taskSubtitleBO = new TaskSubtitleBO(
												osdBo.getTrackType(),
												osdBo.getTextContent(),
												osdBo.getFontType(), osdBo.getPosition(),
												osdBo.getZone(), osdBo.getFontColor(),
												osdBo.getTrackSpeed().intValue(),
												osdBo.getFontSize(), showBkg, showOutline,
												osdBo.getBkgColor(),
												osdBo.getOutLineColor());
										subtile.add(taskSubtitleBO);
									} else {
										//5表示mosaic_radius范围为5，集群暂未开发
										TaskFuzzyBO taskFuzzyBO = new TaskFuzzyBO(
												osdBo.getPosition(), 
												osdBo.getZone(), 
												osdBo.getFuzzyEffect(), 
												5);
										fuzzy.add(taskFuzzyBO);
									}
								}
								TaskOsdBO taskOsdBO = new TaskOsdBO(subtile, logo, fuzzy);
								osd.add(taskOsdBO);
								osdTaskEncodeProcess.setOsd(osd);
							}
							
							//enhance 图像增强
//							TaskEnhanceBO enhance = new TaskEnhanceBO(
//									videoParamPO.getBrightness(), 
//									Integer.parseInt(videoParamPO.getContrast()), 
//									Integer.parseInt(videoParamPO.getSaturation()), 
//									videoParamPO.getDenoise(), 
//									videoParamPO.getSharpen(), 
//									videoParamPO.getColorSpace(), 
//									videoParamPO.getColorTransfer(), 
//									videoParamPO.getColorPrimary(), 
//									videoParamPO.getColorRange());
							TaskScaleBO scale = new TaskScaleBO(
									videoParamPO.getWidth(), 
									videoParamPO.getHeight(), 
									"cpu", 
									videoParamPO.getScaleMode(), 
									videoParamPO.getRatio(), 
									cardNumber);
//							if (null!=videoParamPO.getCutRight()) {
//								TaskCutBO cut = new TaskCutBO(
//										Integer.parseInt(videoParamPO.getCutRight())-Integer.parseInt(videoParamPO.getCutLeft()), 
//										Integer.parseInt(videoParamPO.getCutBottom())-Integer.parseInt(videoParamPO.getCutTop()), 
//										Integer.parseInt(videoParamPO.getCutLeft()), 
//										Integer.parseInt(videoParamPO.getCutTop()));
//								cutTaskEncodeProcess.setCut(cut);
//							}
//							
//							enhanceTaskEncodeProcess.setEnhance(enhance);
							scaleTaskEncodeProcess.setScale(scale);
							
							if (osdTaskEncodeProcess!=null) {
								process_array.add(osdTaskEncodeProcess);
							}
							process_array.add(enhanceTaskEncodeProcess);
							process_array.add(scaleTaskEncodeProcess);
							process_array.add(cutTaskEncodeProcess);
							encode.setProcess_array(process_array);
						}else {
							encode.setEncodeCommon(new EncodePassbyBO());
							NameFilter nameFilter = new NameFilter() {
								@Override
								public String process(Object object, String name, Object value) {
									// TODO Auto-generated method stub
									if (transTaskPO.getTaskType() == 0) {
										return "passby";
									}
									return name;
								}
							};
							nameFilters.add(nameFilter);
						}
						
						encode_array.add(encode);
					}
				}
				videoTaskNode.setEncode_array(encode_array);
				task_array.add(videoTaskNode);
			}
			outputMediaEncodeMessage.setVideoEncodeId(videoEncodeId);
			outputMediaEncodeMessage.setVideoTaskId(videoTaskId);
		}
		
		
		if (audioParamPOList.size() >= 1) {
			ArrayList<String> audioEncodeId = new ArrayList<String>();
			ArrayList<String> audioTaskId = new ArrayList<String>();
			//确定创建任务时，创建几个audio
			Map<Integer, Boolean> audioPidMap = getAudioPidMap(programPO.getAudioElements(), audioParamPOList);
			
			for (AudioElement audioElement : programPO.getAudioElements()) {
				if (!audioPidMap.get(audioElement.getPid())) {
					continue;
				}
				
				TaskNode audioTaskNode = new TaskNode();
				audioTaskNode.setId(nodeIdManageUtil.getNewNodeId().toString());
				if (transTaskPO.getTaskType() == 1) {
					audioTaskNode.setType("audio");
				}else {
					audioTaskNode.setType("passby");
				}
				
				audioTaskNode.setSourceCommon(getSourceCommon(transTaskPO, audioElement.getPid()));
				delTaskNodeIds.add(audioTaskNode.getId());
				
				ArrayList<Encode> encode_array = new ArrayList<Encode>();
				for (AudioParamPO audioParamPO : audioParamPOList) {
					ArrayList<TaskEncodeProcess> process_array = new ArrayList<TaskEncodeProcess>();
					Encode encode = new Encode();
					encode.setEncode_id(nodeIdManageUtil.getNewNodeId().toString());
					audioEncodeId.add(encode.getEncode_id());
					audioTaskId.add(audioTaskNode.getId());
					if (transTaskPO.getTaskType() == 1) {
						NameFiltersFor264265 nameFiltersFor264265 = getEncodeCommon(transTaskPO, null, audioParamPO);
						encode.setEncodeCommon(nameFiltersFor264265.getEncodeCommon());
						nameFilters.addAll(nameFiltersFor264265.getNameFilters());
						
						NameFilter nameFilter = new NameFilter() {
							@Override
							public String process(Object object, String name, Object value) {
								// TODO Auto-generated method stub
								if (value instanceof EncodeAudioCommon) {
									switch (audioParamPO.getCodec()) {
										case "aac":
										case "he-aac":
										case "he-aac-v2":
								            return "aac";
								        case "mp3":
								            return "mp3";
								        case "mpeg2-audio":
								        case "mp2":
								        	 return "mp2";
								        case "dra":
								            return "dra";
								        case "ac3":
								        case "eac3":
								            return "dolby";
									}
								}
								return name;
							}
						};
						nameFilters.add(nameFilter);
						TaskEncodeProcess audioTaskEncodeProcess = new TaskEncodeProcess(); 
//						AudioProcessBO audioProcessBO = new AudioProcessBO(
//								audioParamPO.getSample().intValue(), 
//								audioParamPO.getCodec(), 
//								audioParamPO.getVolume().intValue(), 
//								audioParamPO.getChLayout(), 
//								audioParamPO.getDenoise(), 
//								audioParamPO.getAudioDupMode(), 
//								audioParamPO.getSample().intValue(),
//								audioParamPO.getAgcGain());
//						audioTaskEncodeProcess.setAudioProcess(audioProcessBO);
//						
						AudioResampleBO resample = new AudioResampleBO(audioParamPO.getSample().intValue(), audioParamPO.getChLayout(), "s16");
						audioTaskEncodeProcess.setResample(resample);
						
						process_array.add(audioTaskEncodeProcess);
						encode.setProcess_array(process_array);
						
					}else {
						encode.setEncodeCommon(new EncodePassbyBO());
						NameFilter nameFilter = new NameFilter() {
							@Override
							public String process(Object object, String name, Object value) {
								// TODO Auto-generated method stub
								if (transTaskPO.getTaskType() == 0) {
									return "passby";
								}
								return name;
							}
						};
						nameFilters.add(nameFilter);
					}
					encode_array.add(encode);
				}
				audioTaskNode.setEncode_array(encode_array);
				task_array.add(audioTaskNode);
			}
			outputMediaEncodeMessage.setAudioEncodeId(audioEncodeId);
			outputMediaEncodeMessage.setAudioTaskId(audioTaskId);
		}
			
		List<SubtitleElement> programSubtitles = programDao.findBySourceIdAndNum(transTaskPO.getSourceId(), transTaskPO.getProgramNum()).getSubtitleElements();
		if (programSubtitles!=null && programSubtitles.size() >= 1) {
			ArrayList<String> subtitleEncodeId = new ArrayList<String>();
			ArrayList<String> subtitleTaskId = new ArrayList<String>();
			for (SubtitleElement subtitleElement : programSubtitles) {
				TaskNode subtitleTaskNode = new TaskNode();
				subtitleTaskNode.setId(nodeIdManageUtil.getNewNodeId().toString());
				delTaskNodeIds.add(subtitleTaskNode.getId());
				//字幕轨无编码
				subtitleEncodeId.add("");
				subtitleTaskId.add(subtitleTaskNode.getId());
				
				if (transTaskPO.getTaskType() == 1) {
					subtitleTaskNode.setType("subtitle");
				}else {
					subtitleTaskNode.setType("passby");
				}
				
				subtitleTaskNode.setSourceCommon(getSourceCommon(transTaskPO, programSubtitles.get(0).getPid()));
				task_array.add(subtitleTaskNode);
			}
			outputMediaEncodeMessage.setSubtitleEncodeId(subtitleEncodeId);
			outputMediaEncodeMessage.setSubtitleTaskId(subtitleTaskId);
		}
		
		createJsonTaskNode.setTask_array(task_array);
		//return JSONObject.parseObject(JSONObject.toJSON(createJsonTaskNode).toString());
		return JSONObject.parseObject(JSON.toJSONString(createJsonTaskNode, (NameFilter[])nameFilters.toArray(new NameFilter[nameFilters.size()])));
	}
	
	
	
	public JSONObject getJsonOutputNode(List<OutputPO> outputs, ProgramPO programPO, OutputMediaEncodeMessage outputMediaEncodeMessage) {
		CreateJsonOutputNode createJsonOutputNode = new CreateJsonOutputNode();
		ArrayList<NameFilter> nameFilters = new ArrayList<NameFilter>();
		ArrayList<OutputNode> output_array = new ArrayList<OutputNode>();
		delOutputNodeIds = new ArrayList<String>();
		for (OutputPO outputPO : outputs) {
			OutputNode outputNode = new OutputNode();
			outputNode.setId(nodeIdManageUtil.getNewNodeId().toString());
			delOutputNodeIds.add(outputNode.getId());
			
			OutputCommon outputCommon = outputPO.getOutputBO().generateOutputCommon(outputPO, programPO, outputMediaEncodeMessage);
			outputNode.setOutputCommon(outputCommon);
			
			NameFilter nameFilter = new NameFilter() {
				@Override
				public String process(Object object, String name, Object value) {
					// TODO Auto-generated method stub
					if (value instanceof OutputCommon) {
						switch (outputPO.getType()) {
							case TSUDP:
								return "udp_ts";
							case TSRTP:
								return "rtp_ts";
							case HTTPTS:
								return "http_ts";
							case TSSRT:
								return "srt_ts";
							case HLS:
								return "hls";
							case DASH:
								return "dash";
							case RTSPRTP:
								return "rtsp";
							case MSS:
								return "mss";							
							case RTPES:
								return "rtp_es";	
							case RTMPFLV:
								return "rtmp";
							case TSRTPPASSBY:
								return "rtp_ts_passby";	
							case TSSRTPASSBY:
								return "srt_ts_passby";	
							case TSUDPPASSBY:
								return "udp_passby";
							case HTTPTSPASSBY:
								return "http_ts_passby";
							default:
								return "udp_ts";
						}
					}
					return name;
				}
			};
			nameFilters.add(nameFilter);
			
			output_array.add(outputNode);
		} 
		createJsonOutputNode.setOutput_array(output_array);
		return JSONObject.parseObject(JSON.toJSONString(createJsonOutputNode, (NameFilter[])nameFilters.toArray(new NameFilter[nameFilters.size()])));
	}
	
//	public JSONObject getJsonBackupNode(InputPO mainInput,InputPO backupInput,ProgramPO mainProgramPO,ProgramPO backupProgramPO){
//		CreateJsonBackupNode createJsonBackupNode = new CreateJsonBackupNode();
//		BackupBO back_up = new BackupBO();
//		back_up.setMode("auto");
//		back_up.setSelect_index(1);
//		ArrayList<ProgramBO> program_array = new ArrayList<ProgramBO>();
//		ProgramBO programBO = new ProgramBO();
//		programBO.setInput_id(backupInput.getNodeId().toString());
//		programBO.setProgram_id(backupProgramPO.getProgramNum());
//		
//		ArrayList<ElementBO> element_array = new ArrayList<ElementBO>();
//		List<AudioElement> audioElements = mainProgramPO.getAudioElements();
//		List<VideoElement> videoElements = mainProgramPO.getVideoElements();
//		for (VideoElement videoElement : videoElements) {
//			ElementBO elementBO = new ElementBO();
//			elementBO.setPid(videoElement.getPid());
//			element_array.add(elementBO);
//		}
//		for (AudioElement audioElement : audioElements) {
//			ElementBO elementBO = new ElementBO();
//			elementBO.setPid(audioElement.getPid());
//			element_array.add(elementBO);
//		}
//		programBO.setElement_array(element_array);
//		program_array.add(programBO);
//		back_up.setProgram_array(program_array);
//		createJsonBackupNode.setBack_up(back_up);
//		return JSONObject.parseObject(JSONObject.toJSON(createJsonBackupNode).toString());
//	}
//	
//	public JSONObject getJsonDelBackupNode(InputBackupMapPO inputBackupMapPO){
//		
//		return null;
//	}
	
	
//	private OutputCommon getOutputCommon(OutputPO outputPO, ProgramPO programPO, OutputMediaEncodeMessage outputMediaEncodeMessage){
//		OutputCommon outputCommon = null;
//		switch (outputPO.getType()) {
//    		case TSUDP:
//    			break;
//    		case TSUDPPASSBY:
//    			
//    		case TSSRT:
//    			return "TS-SRT";
//    		case TSSRTPASSBY:
//    			return "TS-SRT-PASSBY";
//    		case DASH:
//    			return "DASH";
//    		case HDS:
//    			//return "HDS-FLV";
//    			return "HDS";
//    		case HLS:
//    			return "HLS";
//    		case MSS:
//    			return "HTTP-SSM";
//			case HTTPTSPASSBY:
//    			return "TS-HTTP-PASSBY";
//    		case HTTPFLV:
//    			return "HTTP-FLV";
//    		case HTTPTS:
//    			return "TS-HTTP";
//    		case TSRTP:
//    			return "TS-RTP";
//    		case RTSPRTP:
//    			return "RTSP";
//    		case RTMPFLV:
//    			return "RTMP-FLV";
//    		default:
//    			break;
//    		}
//		return outputCommon;
//	}
	


	

	private Map<Integer, Boolean> getVideoPidMap(List<VideoElement> programList, List<VideoParamPO> transTaskList){
		Map<Integer, Boolean> pidMap = new HashMap<Integer, Boolean>();
		for (VideoElement p : programList) {
			pidMap.put(p.getPid(), false);
			for (VideoParamPO t : transTaskList) {
				if (p.getPid().equals(t.getPid())) {
					pidMap.replace(p.getPid(), true);
					break;
				}
			}
		}
		return pidMap;
	}
	
	private Map<Integer, Boolean> getAudioPidMap(List<AudioElement> programList, List<AudioParamPO> transTaskList){
		Map<Integer, Boolean> pidMap = new HashMap<Integer, Boolean>();
		for (AudioElement p : programList) {
			pidMap.put(p.getPid(), false);
			for (AudioParamPO t : transTaskList) {
				if (p.getPid().equals(t.getPid())) {
					pidMap.replace(p.getPid(), true);
					break;
				}
			}
		}
		return pidMap;
	}
	
	public NameFiltersFor264265 getEncodeCommon(TransTaskPO transTaskPO, VideoParamPO videoParamPO, AudioParamPO audioParamPO) {
		// TODO Auto-generated method stub
		NameFiltersFor264265 nameFiltersFor264265 = null;
		if (null==videoParamPO && null!=audioParamPO) {
			nameFiltersFor264265 = new NameFiltersFor264265();
			nameFiltersFor264265.setEncodeCommon(audioParamPO.getEncodeCommon(audioParamPO.getCodec()));
		}else if (null==audioParamPO && null!=videoParamPO) {
			//参数页面暂未开发，调试修改这里
			nameFiltersFor264265 = videoParamPO.getEncodeCommon(videoParamPO.getCodec());
		}
		return nameFiltersFor264265;
	}

	// 由于InputNode中inputCommon参数名称在生成json时会根据源类型而改变，所以只能出此下策，以后有更好方法再修改
	public JSONObject getCreateInputNode(CreateJsonInputNode inputNode, ProtoType type) {
		JSONObject param = new JSONObject();
		param.put("msg_id", inputNode.getMsg_id());

		NameFilter nameFilter = new NameFilter() {
			@Override
			public String process(Object object, String name, Object value) {
				// TODO Auto-generated method stub
				if (value instanceof InputCommon) {
					switch (type) {
					case TSUDP:
						return "udp_ts";
					case TSRTP:
						return "rtp_ts";
					case HTTPTS:
						return "http_ts";
					case TSSRT:
						return "srt_ts";
					case HLS:
						return "hls";
					case DASH:
						return "dash";
					case MSS:
						return "mss";
					case RTSPRTP:
						return "rtsp";
					case RTMPFLV:
						return "rtmp";
					case HTTPFLV:
						return "http_flv";
					default:
						return "udp_ts";
					}
				}
				return name;
			}
		};

		ArrayList<InputNode> inputNodes = inputNode.getInput_array();
		JSONArray jsonArray = new JSONArray();
		for (InputNode i : inputNodes) {
			jsonArray.add(JSONObject.parseObject(JSON.toJSONString(i, nameFilter)));
		}

//		ArrayList<InputNode> inputNodes = inputNode.getInput_array();
//		String input_array = "[";
//		for (int i = 0; i<inputNodes.size(); i++) {
//			input_array = input_array + JSON.toJSONString(inputNodes.get(i),nameFilter);
//			if (i<inputNodes.size()-1) {
//				input_array = input_array + ",";
//			}
//		}
//		input_array = input_array + "]";
		
		param.put("input_array", jsonArray);
		return param;
	}

	public InputCommon getInputCommon(SourcePO sourcePO) {
		InputCommon inputCommon = null;
		switch (sourcePO.getProtoType()) {
		case TSUDP:
		case TSRTP:
			Igmpv3 igmpv3 = null;
			if (sourcePO.getIsIgmpv3()) {
				igmpv3 = new Igmpv3();
				//startip,endip那样格式
				//igmpv3.setIp_array(sourcePO.getFilterIpSegments());
				
				//ip:字符串格式
				ArrayList<Igmpv3Ip> ip_array = new ArrayList<Igmpv3Ip>();
				ArrayList<String> ipStringArray = new ArrayList<String>();
				for (FilterIpSegment f : sourcePO.getFilterIpSegments()) {
					ipStringArray.removeAll(getAllIp(f.getStartIp(), f.getEndIp()));
					ipStringArray.addAll(getAllIp(f.getStartIp(), f.getEndIp()));
				}
				for (String s : ipStringArray) {
					Igmpv3Ip igmpv3Ip = new Igmpv3Ip(s);
					ip_array.add(igmpv3Ip);
				}
				
				//字符串格式
//				ArrayList<String> ip_array = new ArrayList<String>();
//				for (FilterIpSegment f : sourcePO.getFilterIpSegments()) {
//					ip_array.removeAll(getAllIp(f.getStartIp(), f.getEndIp()));
//					ip_array.addAll(getAllIp(f.getStartIp(), f.getEndIp()));
//				}
				igmpv3.setIp_array(ip_array);
				igmpv3.setMode(sourcePO.getFilterMode().toString().toLowerCase());
			}
			inputCommon = new InputUdpRtpBO(sourcePO.getSourceIp(),
					sourcePO.getSourcePort(), sourcePO.getLocalIp(), igmpv3);
			break;
		case HTTPTS:
		case HLS:
		case DASH:
		case MSS:
		case RTSPRTP:
		case RTMPFLV:
		case HTTPFLV:
			inputCommon = new InputUrlBO(sourcePO.getSourceUrl());
			break;
		case TSSRT:
			if (sourcePO.getModeSelect().equals("caller")) {
				String[] ipPort = sourcePO.getSourceUrl().split(":");
				String ip = ipPort[1].substring(2);
				String port = ipPort[2];
				inputCommon = new InputSrtBO(ip, Integer.valueOf(port), sourcePO.getModeSelect(), sourcePO.getLatency());
			}else {
				inputCommon = new InputSrtBO(sourcePO.getSourceIp(), sourcePO.getSourcePort(), sourcePO.getModeSelect(), sourcePO.getLatency());
			}
			break;
		default:
			break;
		}
		return inputCommon;
	}
	
	private ArrayList<String> getAllIp(String startIp, String endIp) {
	    ArrayList<String> ips = new ArrayList<String>();
	    String[] ipfromd = startIp.split("\\.");
	    String[] iptod = endIp.split("\\.");
	    int[] int_ipf = new int[4];
	    int[] int_ipt = new int[4];
	    for (int i = 0; i < 4; i++) {
	        int_ipf[i] = Integer.parseInt(ipfromd[i]);
	        int_ipt[i] = Integer.parseInt(iptod[i]);
	    }
	    for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
	        for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1]
	                : 255); B++) {
	            for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2]
	                    : 255); C++) {
	                for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3]
	                        : 255); D++) {
	                    ips.add(new String(A + "." + B + "." + C + "." + D));
	                }
	            }
	        }
	    }
	    return ips;
	}

	public SourceCommon getSourceCommon(TransTaskPO transTaskPO, Integer pid) {
		SourceCommon sourceCommon = null;
		SourcePO sourcePO = sourceDao.findOne(transTaskPO.getSourceId());
		if (sourcePO.getSourceType() == SourceType.SDI) {
			sourceCommon = new RawSourceBO(transTaskPO.getInputId().toString(),transTaskPO.getProgramNum(), pid);
		}else if (sourcePO.getSourceType() == SourceType.PASSBY) {
			sourceCommon = new PassbySource(transTaskPO.getInputId().toString());
		}else {
			sourceCommon = new EsSourceBO(transTaskPO.getInputId().toString(),transTaskPO.getProgramNum(), pid); 
		}
		return sourceCommon;
	}

	public OutputMediaEncodeMessage getOutputMediaEncodeMessage() {
		return outputMediaEncodeMessage;
	}

	public void setOutputMediaEncodeMessage(OutputMediaEncodeMessage outputMediaEncodeMessage) {
		this.outputMediaEncodeMessage = outputMediaEncodeMessage;
	}
	
	

}
