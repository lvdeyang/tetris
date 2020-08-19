package com.sumavision.signal.bvc.director.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.director.bo.AddOutputBO;
import com.sumavision.signal.bvc.director.bo.AddTaskBO;
import com.sumavision.signal.bvc.director.bo.BundleBO;
import com.sumavision.signal.bvc.director.bo.CodeParamBO;
import com.sumavision.signal.bvc.director.bo.DeleteOutputBO;
import com.sumavision.signal.bvc.director.bo.DeleteTaskBO;
import com.sumavision.signal.bvc.director.dao.DirectorDstDAO;
import com.sumavision.signal.bvc.director.dao.DirectorSourceDAO;
import com.sumavision.signal.bvc.director.dao.DirectorTaskDAO;
import com.sumavision.signal.bvc.director.enumeration.DeviceType;
import com.sumavision.signal.bvc.director.po.DirectorDstPO;
import com.sumavision.signal.bvc.director.po.DirectorSourcePO;
import com.sumavision.signal.bvc.director.po.DirectorTaskPO;
import com.sumavision.signal.bvc.entity.enumeration.ChannelType;
import com.sumavision.signal.bvc.entity.enumeration.OneOneFiveMParam;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.signal.bvc.mq.bo.PlayerBO;
import com.sumavision.signal.bvc.terminal.OneOneFiveMTerminal;
import com.sumavision.signal.bvc.terminal.TerminalParam;
import com.sumavision.tetris.capacity.server.DirectorService;
import com.sumavision.tetris.capacity.vo.director.DestinationVO;
import com.sumavision.tetris.capacity.vo.director.DirectorTaskVO;
import com.sumavision.tetris.capacity.vo.director.OutputsVO;
import com.sumavision.tetris.capacity.vo.director.SourceVO;
import com.sumavision.tetris.capacity.vo.director.TranscodeVO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class DirectorTaskService {
	
	@Autowired
	private DirectorTaskDAO directorTaskDao;
	
	@Autowired
	private DirectorSourceDAO directorSourceDao;
	
	@Autowired
	private DirectorDstDAO directorDstDao;
	
	@Autowired
	private OneOneFiveMTerminal oneOneFiveMTerminal;
	
	@Autowired
	private DirectorService directorService;
	
	/**
	 * 添加云导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月13日 下午2:53:21
	 * @param List<TaskBO> tasks 云导播任务
	 */
	public void addTasks(List<AddTaskBO> tasks) throws Exception{
		
		CapacityPortCache capacityPortCache = CapacityPortCache.getInstance();
		DevicePortCache devicePortCache = DevicePortCache.getInstance();
		
		//持久化数据
		List<DirectorTaskPO> directorTasks = new ArrayList<DirectorTaskPO>();
		List<DirectorSourcePO> directorSources = new ArrayList<DirectorSourcePO>();
		List<DirectorDstPO> directorDsts = new ArrayList<DirectorDstPO>();
		
		//能力协议
		List<DirectorTaskVO> capacityTasks = new ArrayList<DirectorTaskVO>();
		
		for(AddTaskBO task: tasks){
			
			String taskId = task.getTaskId();
			
			DirectorTaskVO capacityTask = new DirectorTaskVO();
			capacityTask.setTaskId(taskId);
			capacityTask.setSelect_index(task.getSelect_index());
			
			DirectorTaskPO directorTask = new DirectorTaskPO();
			directorTask.setTaskId(taskId);
			directorTask.setTranscodeParam(JSON.toJSONString(task.getTranscodeParam()));
			
			directorTasks.add(directorTask);
			
			List<BundleBO> sources = task.getSource();
			if(sources == null || sources.size() <= 0) continue;
			List<BundleBO> dsts = task.getDestination();
			if(dsts == null || dsts.size() <= 0) continue;
			
			//TODO:根据节点(基站)id获取可用能力id和ip
			String sourceCapacityId = "";
			String sourceCapacityIp = "";
			CopyOnWriteArraySet<Long> ports = capacityPortCache.getByKey(sourceCapacityIp);
			capacityTask.setCapacityIp(sourceCapacityIp);
			capacityTask.setSources(new ArrayList<SourceVO>());
			
			//源
			for(BundleBO source: sources){
				
				//TODO:暂时写成false
				boolean isEncode = false;
				
				String ipPort = devicePortCache.getByKey(source.getBundleId());
				//协商端口并加入缓存
				Long port;
				
				DirectorSourcePO directorSource = new DirectorSourcePO();
				directorSource.setBundleId(source.getBundleId());
				directorSource.setCapacityId(sourceCapacityId);
				directorSource.setCapacityIp(sourceCapacityIp);
				directorSource.setChannelId(source.getChannelId());
				directorSource.setCodeParam(JSON.toJSONString(source.getCodeParam()));
				directorSource.setDeviceIp(source.getDeviceIp());
				directorSource.setDeviceModel(source.getDeviceModel());
				directorSource.setPassBy(JSON.toJSONString(source.getPassBy()));
				directorSource.setTaskId(taskId);
				
				if(ipPort == null){
					port = generatePort(ports);
					ports.add(port);
					capacityPortCache.add(sourceCapacityIp, port);
					devicePortCache.add(source.getBundleId(), new StringBufferWrapper().append(sourceCapacityIp)
							                                                           .append("@")
							                                                           .append(port)
							                                                           .toString());
					
					directorSource.setCapacityPort(port);
					
					isEncode = true;
				}else{
					String exsitCapacityIp = ipPort.split("@")[0];
					port = Long.valueOf(ipPort.split("@")[1]);
					
					directorSource.setCapacityPort(port);
					
					//TODO:当选出来的转码能力和已经存在的转码能力不一样时怎么处理？
				}
				
				directorSources.add(directorSource);
				
				//设置5G编码
				if(isEncode){
					if(source.getDeviceModel().equals("5G")){
						
						CodeParamBO codeParam = source.getCodeParam();
						//音频码率
						Long audioBitrate = codeParam.getAudioBitrate();
						//音频编码
						String audioCodec = codeParam.getAudioCodec();
						//音频采样率
						Long sampleRate = codeParam.getSample_rate();
						//视频帧率
						String fps = codeParam.getFps();
						//视频分辨率
						String resolution = codeParam.getResolution();
						//视频码率
						Long videoBitrate = codeParam.getVideoBitrate();
						//视频编码
						String videoCodec = codeParam.getVideoCodec();
						//视频发送码率
		    			Long send_bitrate = videoBitrate * 4;
		    			//格式化分辨率
		    			String format_resolution = new StringBufferWrapper().append(resolution)
		    																.append("P")
		    																.append(fps)
		    																.toString();
		    			//为了保证enum的name唯一
						String format_bitrate = new StringBufferWrapper().append(audioBitrate)
																		 .append("abr")
																		 .toString();
						String format_sample_rate = new StringBufferWrapper().append(sampleRate)
																			 .append("sr")
																			 .toString();
						
		    			
		    			//视频编码参数设置
		            	JSONObject vidEncParams = new JSONObject();
		            	JSONObject prog1vidEnc = new JSONObject();
		            	prog1vidEnc.put("onoff", 1);
		            	prog1vidEnc.put("vidEncRes", OneOneFiveMParam.fromName(format_resolution).getProtocal());
		            	prog1vidEnc.put("vidEncBR", videoBitrate);
		            	prog1vidEnc.put("vidSysBR", send_bitrate);
		            	prog1vidEnc.put("vidEncStd", OneOneFiveMParam.fromName(videoCodec).getProtocal());
		            	vidEncParams.put("prog1vidEnc", prog1vidEnc);
		            	
		        		HttpAsyncClient.getInstance().httpAsyncPost("http://" + source.getDeviceIp() + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.vidEncSet(vidEncParams).toJSONString(), null, null);
		            	
						//音频编码参数设置
						JSONObject audEncParams = new JSONObject();
						JSONObject prog1audEnc = new JSONObject();
						prog1audEnc.put("audSampleRate", OneOneFiveMParam.fromName(format_sample_rate).getProtocal());
						JSONArray audEncPara = new JSONArray();
						JSONObject audEnc = new JSONObject();
						audEnc.put("audOnOff", 1);
						audEnc.put("audRate", OneOneFiveMParam.fromName(format_bitrate).getProtocal());
						audEnc.put("audStd", OneOneFiveMParam.fromName(audioCodec).getProtocal());
						audEncPara.add(audEnc);
						prog1audEnc.put("audEncPara", audEncPara);
						audEncParams.put("prog1audEnc", prog1audEnc);
						
						HttpAsyncClient.getInstance().httpAsyncPost("http://" + source.getDeviceIp() + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.audEncSet(audEncParams).toJSONString(), null, null);
						
						//GBE设置参数
			    		JSONObject gbeNet0PortCfg = new JSONObject();
			    		JSONArray portCfg = new JSONArray();
			    		JSONObject portJson = new JSONObject();
			    		portJson.put("portIdx", 1);
			    		portJson.put("dstIP",sourceCapacityIp);
			    		portJson.put("dstUDP", port);
			    		portJson.put("switch", 1);
			    		portCfg.add(portJson);
			    		gbeNet0PortCfg.put("gbeNet0PortCfg", gbeNet0PortCfg);
			    		
			    		HttpAsyncClient.getInstance().httpAsyncPost("http://" + source.getDeviceIp() + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.gbePortCfg(gbeNet0PortCfg).toJSONString(), null, null);
					}
				}
				
				//能力协议转换--源
				SourceVO sourceVO = new SourceVO();
				sourceVO.setBundleId(source.getBundleId());
				sourceVO.setIndex(Long.valueOf(source.getIndex()));
				sourceVO.setIp(source.getDeviceIp());
				sourceVO.setPort(port.toString());
						
				if(source.getDeviceModel().equals("5G")){
					sourceVO.setType("srt");
				}
				
				capacityTask.getSources().add(sourceVO);
				
			}
			
			//目的
			capacityTask.setDestinations(new ArrayList<DestinationVO>());
			for(BundleBO dst: dsts){
				
				DestinationVO destination = new DestinationVO();
				destination.setBitrate(dst.getCodeParam().getVideoBitrate());
				destination.setBundleId(dst.getBundleId());
				
				DeviceType dstType = DeviceType.fromDeviceModel(dst.getDeviceModel());
				if(dstType.equals(DeviceType.FIVEG)){
					//TODO：5G背包解码--协议暂时不支持					
					DirectorDstPO directorDst = process5GDst(dst, taskId);
					directorDsts.add(directorDst);
					
					//能力协议转换--5G目的
					destination.setIp(dst.getDeviceIp());
					destination.setPort(directorDst.getDevicePort().toString());
					destination.setType("udp");
					
				}else if(dstType.equals(DeviceType.PLAYER)){
					DirectorDstPO directorDst = processPlayerDst(dst, taskId);
					directorDsts.add(directorDst);
					
					//能力协议转换--播放器目的
					destination.setIp(directorDst.getDeviceIp());
					destination.setPort(directorDst.getDevicePort().toString());
					destination.setType("udp");
					
				}
				
				capacityTask.getDestinations().add(destination);
				
			}
			
			//TODO:转码--协议数据结构未定，暂用string透传
			String transcodeParam = task.getTranscodeParam();
			TranscodeVO transcode = JSONObject.parseObject(transcodeParam, TranscodeVO.class);
			capacityTask.setTranscode(transcode);
			
			capacityTasks.add(capacityTask);
			
 		}
		
		//向能力服务发协议
		directorService.addDirector(capacityTasks);
		
		//持久化
		directorTaskDao.save(directorTasks);
		directorSourceDao.save(directorSources);
		directorDstDao.save(directorDsts);
	}
	
	/**
	 * 删除云导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月13日 下午4:26:26
	 * @param List<DeleteTaskBO> tasks
	 */
	public void deleteTasks(List<DeleteTaskBO> tasks) throws Exception{
		
		CapacityPortCache capacityPortCache = CapacityPortCache.getInstance();
		DevicePortCache devicePortCache = DevicePortCache.getInstance();
		
		List<String> taskIds = new ArrayList<String>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		for(DeleteTaskBO task: tasks){
			taskIds.add(task.getTaskId());
			for(BundleBO bundle: task.getSource()){
				bundles.add(bundle);
			}
		}
		
		List<DirectorSourcePO> sources = directorSourceDao.findByTaskIdIn(taskIds);
		for(BundleBO bundle: bundles){
			for(DirectorSourcePO source: sources){
				if(source.getBundleId().equals(bundle.getBundleId())){
					capacityPortCache.remove(source.getCapacityIp(), source.getCapacityPort());
					devicePortCache.remove(bundle.getBundleId());
				}
			}
		}
		
		//设备http
		for(BundleBO bundle: bundles){
			
			String deviceIp = bundle.getDeviceIp();
			
			//GBE设置参数
    		JSONObject gbeNet0PortCfg = new JSONObject();
    		JSONArray portCfg = new JSONArray();
    		JSONObject port = new JSONObject();
    		port.put("portIdx", 1);
    		port.put("switch", 0);
    		portCfg.add(port);
    		gbeNet0PortCfg.put("gbeNet0PortCfg", gbeNet0PortCfg);
    		
    		HttpAsyncClient.getInstance().httpAsyncPost("http://" + deviceIp + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.gbePortCfg(gbeNet0PortCfg).toJSONString(), null, null);
	    	
    		//TODO:解码参数--设备暂时不支持
		}
		
		//能力转换协议--删除备份源任务
		directorService.deleteDirector(taskIds);
		
		directorTaskDao.deleteByTaskIdIn(taskIds);
		directorSourceDao.deleteByTaskIdIn(taskIds);
		directorDstDao.deleteByTaskIdIn(taskIds);
		
	}
	
	/**
	 * 添加导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 上午11:42:36
	 * @param List<AddOutputBO> outputs 添加导播输出数据
	 */
	public void addOutputs(List<AddOutputBO> outputs) throws Exception{
		
		List<OutputsVO> outputsVOs = new ArrayList<OutputsVO>();
		
		List<DirectorDstPO> directorDsts = new ArrayList<DirectorDstPO>();
		
		for(AddOutputBO output: outputs){
			
			String taskId = output.getTaskId();
			List<BundleBO> dsts = output.getBundles();
			
			OutputsVO outputsVO = new OutputsVO();
			outputsVO.setTaskId(taskId);
			outputsVO.setDsts(new ArrayList<DestinationVO>());
			
			//目的
			for(BundleBO dst: dsts){
				
				DestinationVO destination = new DestinationVO();
				destination.setBitrate(dst.getCodeParam().getVideoBitrate());
				destination.setBundleId(dst.getBundleId());
				
				DeviceType dstType = DeviceType.fromDeviceModel(dst.getDeviceModel());
				if(dstType.equals(DeviceType.FIVEG)){
					//TODO：5G背包解码--协议暂时不支持					
					DirectorDstPO directorDst = process5GDst(dst, taskId);
					directorDsts.add(directorDst);
					
					//能力协议转换--5G目的
					destination.setIp(dst.getDeviceIp());
					destination.setPort(directorDst.getDevicePort().toString());
					destination.setType("udp");
					
				}else if(dstType.equals(DeviceType.PLAYER)){
					DirectorDstPO directorDst = processPlayerDst(dst, taskId);
					directorDsts.add(directorDst);
					
					//能力协议转换--播放器目的
					destination.setIp(directorDst.getDeviceIp());
					destination.setPort(directorDst.getDevicePort().toString());
					destination.setType("udp");
					
				}
				outputsVO.getDsts().add(destination);
			}
			
			outputsVOs.add(outputsVO);
		}
		
		directorService.addOutput(outputsVOs);
		
		directorDstDao.save(directorDsts);
	}
	
	/**
	 * 删除导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:44:03
	 * @param List<DeleteOutputBO outputs 
	 */
	public void deleteOutputs(List<DeleteOutputBO> outputs) throws Exception{
		
		List<OutputsVO> outputVOs = new ArrayList<OutputsVO>();
		
		List<String> taskIds = new ArrayList<String>();
		
		for(DeleteOutputBO output: outputs){
			taskIds.add(output.getTaskId());
		}
		
		List<DirectorDstPO> directorDsts = directorDstDao.findByTaskIdIn(taskIds);
		List<DirectorDstPO> needDeleteDsts = new ArrayList<DirectorDstPO>();
		List<BundleBO> needDeleteBundles = new ArrayList<BundleBO>();
		for(DeleteOutputBO output: outputs){
			OutputsVO outputVO = new OutputsVO();
			outputVO.setTaskId(output.getTaskId());
			outputVO.setDsts(new ArrayList<DestinationVO>());
			
			for(BundleBO bundle: output.getBundles()){
				for(DirectorDstPO dst: directorDsts){
					if(dst.getTaskId().equals(output.getTaskId()) && dst.getBundleId().equals(bundle.getBundleId())){
						needDeleteDsts.add(dst);
						DestinationVO destination = new DestinationVO();
						destination.setBundleId(dst.getBundleId());
						destination.setIp(dst.getDeviceIp());
						destination.setPort(dst.getDevicePort().toString());
						if(dst.getDeviceModel().equals(DeviceType.FIVEG)){
							destination.setType("udp");
						}
						if(dst.getDeviceModel().equals(DeviceType.PLAYER)){
							destination.setType("udp");
						}
						outputVO.getDsts().add(destination);
						break;
					}
				}
				needDeleteBundles.add(bundle);
			}
			
			outputVOs.add(outputVO);
		}
		
		for(BundleBO bundle: needDeleteBundles){
			
			String deviceIp = bundle.getDeviceIp();
			
			//TODO:http关闭解码开关--设备协议暂时不支持
			
		}
		
		//能力服务删除导播任务输出协议
		directorService.deleteOutput(outputVOs);
		
		directorDstDao.deleteInBatch(needDeleteDsts);
		
	}
	
	/**
	 * 协商新端口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 上午9:14:09
	 * @param List<Long> ports 已有端口
	 * @return Long 新端口
	 */
	public Long generatePort(CopyOnWriteArraySet<Long> ports){
		if(ports == null || ports.size() <= 0){
			return 10000l;
		}
		
		List<Long> portList = new ArrayList<Long>();
		for(Long port: ports){
			portList.add(port);
		}
		
		Collections.sort(portList);
		
		Long newPort = portList.get(portList.size()-1) + 2l;
		
		return newPort;
	}
	
	/**
	 * 5G背包设备解码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月13日 上午11:47:12
	 * @param BundleBO dst 设备信息
	 * @param String taskId 任务id
	 * @return DirectorDstPO 导播输出数据
	 */
	public DirectorDstPO process5GDst(BundleBO dst, String taskId) throws Exception{
		
		DeviceType dstType = DeviceType.fromDeviceModel(dst.getDeviceModel());
		String dstIp = dst.getDeviceIp();
		String channelId = dst.getChannelId();
		Long dstPort = ChannelType.fromType(channelId).getPort();
		
		DirectorDstPO directorDst = new DirectorDstPO();
		directorDst.setBundleId(dst.getBundleId());
		directorDst.setChannelId(channelId);
		directorDst.setCodeParam(JSON.toJSONString(dst.getCodeParam()));
		directorDst.setDeviceIp(dstIp);
		directorDst.setDeviceModel(dst.getDeviceModel());
		directorDst.setDeviceType(dstType);
		directorDst.setDevicePort(dstPort);
		directorDst.setPassBy(dst.getPassBy());
		directorDst.setTaskId(taskId);
		
		return directorDst;
	}
	
	/**
	 * 播放器解码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月13日 下午2:03:02
	 * @param BundleBO dst 播放器数据
	 * @param String taskId 任务id
	 * @return DirectorDstPO 导播输出数据
	 */
	public DirectorDstPO processPlayerDst(BundleBO dst, String taskId) throws Exception{
	
    	String passby = dst.getPassBy();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	DeviceType dstType = DeviceType.fromDeviceModel(dst.getDeviceModel());
		String channelId = dst.getChannelId();
		
		DirectorDstPO directorDst = new DirectorDstPO();
		directorDst.setBundleId(dst.getBundleId());
		directorDst.setChannelId(channelId);
		directorDst.setCodeParam(JSON.toJSONString(dst.getCodeParam()));
		directorDst.setDeviceModel(dst.getDeviceModel());
		directorDst.setDeviceType(dstType);
		directorDst.setPassBy(dst.getPassBy());
		directorDst.setTaskId(taskId);
    	
    	if(passbyBO.getType().equals("udp_decode")){
    		
    		//能力转换协议会用ip和端口
    		PlayerBO player = JSON.parseObject(passbyBO.getPass_by_content().toJSONString(), PlayerBO.class);
    		String udpUrl = player.getUdpUrl();
    		String ip = udpUrl.split("udp://")[1].split(":")[0];
    		String port = udpUrl.split("udp://")[1].split(":")[1];
    		
    		directorDst.setDeviceIp(ip);
    		directorDst.setDevicePort(Long.valueOf(port));
    		
    	}
    	
    	return directorDst;
		
	}
}
