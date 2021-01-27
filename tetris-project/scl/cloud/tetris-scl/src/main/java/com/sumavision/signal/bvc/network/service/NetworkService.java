package com.sumavision.signal.bvc.network.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import com.sumavision.signal.bvc.common.enumeration.NodeType;
import com.sumavision.signal.bvc.config.CapacityProps;
import com.sumavision.signal.bvc.entity.enumeration.ChannelType;
import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.Jv210Param;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.http.HttpClient;
import com.sumavision.signal.bvc.mq.ProcessReceivedMsg;
import com.sumavision.signal.bvc.mq.bo.BaseParamBO;
import com.sumavision.signal.bvc.mq.bo.BundleBO;
import com.sumavision.signal.bvc.mq.bo.ChannelBO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.signal.bvc.mq.bo.RectBO;
import com.sumavision.signal.bvc.mq.bo.ScreenBO;
import com.sumavision.signal.bvc.mq.bo.SourceBO;
import com.sumavision.signal.bvc.network.bo.CreateInputResponseBO;
import com.sumavision.signal.bvc.network.bo.CreateOutputResponseBO;
import com.sumavision.signal.bvc.network.bo.CutSwitchResponseBO;
import com.sumavision.signal.bvc.network.bo.InputChannelBO;
import com.sumavision.signal.bvc.network.bo.NetBundleBO;
import com.sumavision.signal.bvc.network.bo.SwitchRequestBO;
import com.sumavision.signal.bvc.network.bo.SwitchResponseBO;
import com.sumavision.signal.bvc.network.dao.NetworkInputDAO;
import com.sumavision.signal.bvc.network.dao.NetworkMapDAO;
import com.sumavision.signal.bvc.network.dao.NetworkOutputDAO;
import com.sumavision.signal.bvc.network.po.NetworkInputPO;
import com.sumavision.signal.bvc.network.po.NetworkMapPO;
import com.sumavision.signal.bvc.network.po.NetworkOutputPO;
import com.sumavision.signal.bvc.resource.util.ResourceQueryUtil;
import com.sumavision.signal.bvc.terminal.TerminalParam;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

@Service
public class NetworkService {
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private CapacityProps capacityProps;
	
	@Autowired
	private NetworkInputDAO networkInputDao;
	
	@Autowired
	private NetworkOutputDAO networkOutputDao;
	
	@Autowired
	private NetworkMapDAO networkMapDao;
	
	@Autowired
	private NetworkQuery networkQuery;
	
	@Autowired
	private NetworkHttpService networkHttpService;
	
	@Autowired
	private ProcessReceivedMsg processReceivedMsg;

	/**
	 * 处理openbundle中VenusTerminal类型<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月28日 上午9:11:23
	 * @param BundleBO bundle bundle信息
	 */
	public void processOpenBundleVenusTerminalMsg(BundleBO bundle) throws Exception{
		
		NetBundleBO netBundle = resourceQueryUtil.queryBundleByBundleId(bundle.getBundle_id());
		
		if(netBundle == null){
			throw new BaseException(StatusCode.FORBIDDEN, "bundleId为：" + bundle.getBundle_id() + " 不存在！");
		}
		
		if(netBundle.getBundleIp() == null){
			throw new BaseException(StatusCode.FORBIDDEN, "bundleId为：" + bundle.getBundle_id() + " 没有设置ip！");
		}
		
		netBundle.setNetId(capacityProps.getNetId());
		netBundle.setNetIp(capacityProps.getNetIp());
		if(netBundle.getNetId()== null){
			throw new BaseException(StatusCode.FORBIDDEN, "bundleId为：" + bundle.getBundle_id() + " 没有绑定网络调度系统！");
		}
		
		if(bundle.getDevice_model().equals("jv210")){
    		
    		processOpenBundleJv210Msg(bundle, netBundle);
    		
    	}
		
	}
	
	/**
	 * 处理closebundle中VenusTerminal类型<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午4:34:37
	 * @param BundleBO bundle bundle信息
	 */
	public void processCloseBundleVenusTerminalMsg(BundleBO bundle) throws Exception{
		
		NetBundleBO netBundle = resourceQueryUtil.queryBundleByBundleId(bundle.getBundle_id());
		
		if(netBundle == null){
			throw new BaseException(StatusCode.FORBIDDEN, "bundleId为：" + bundle.getBundle_id() + " 不存在！");
		}
		
		if(netBundle.getBundleIp() == null){
			throw new BaseException(StatusCode.FORBIDDEN, "bundleId为：" + bundle.getBundle_id() + " 没有设置ip！");
		}
		
		netBundle.setNetId(capacityProps.getNetId());
		netBundle.setNetIp(capacityProps.getNetIp());
		if(netBundle.getNetId()== null){
			throw new BaseException(StatusCode.FORBIDDEN, "bundleId为：" + bundle.getBundle_id() + " 没有绑定网络调度系统！");
		}
		
		if(bundle.getDevice_model().equals("jv210")){
    		
    		processCloseBundleJv210Msg(bundle, netBundle);
    		
    	}
		
	}
	
	/**
	 * 处理openbundle中VenusTerminal类型中jv210类型<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月28日 上午9:12:20
	 * @param BundleBO bundle
	 */
	public void processOpenBundleJv210Msg(BundleBO bundle, NetBundleBO netBundle) throws Exception{
		
    	String bundleId = bundle.getBundle_id();
		
    	List<ChannelBO> channels = bundle.getChannels();   
    	
    	//创建网络输入资源
    	List<InputChannelBO> needCreateInputChannels = new ArrayList<InputChannelBO>(); 
    	List<String> srcBundleIds = new ArrayList<String>();
    	
    	//取bundleId和channelId用于查询
    	srcBundleIds.add(bundleId);
    	for(ChannelBO channel:channels){
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			//不是在一个接入上也需要把输入建立出来--因为pulldata要用到
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				srcBundleIds.add(srcBundleId);
			}
    	}

    	List<NetworkInputPO> inputs = networkInputDao.findByBundleIdIn(srcBundleIds);
    	for(ChannelBO channel: channels){
    		if(ChannelType.fromType(channel.getChannel_id()).isEncode()){
    			if(!networkQuery.isCreateInput(inputs, channel.getChannel_id())){
    				InputChannelBO inputChannel = new InputChannelBO();
    				inputChannel.setBundleId(bundleId);
    				inputChannel.setChannelId(channel.getChannel_id());
    				needCreateInputChannels.add(inputChannel);
    			}
    		}
    		
    		BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null && source.getLayer_id().equals(NodeType.NETWORK.getId()) && !source.getBundle_id().equals(bundleId)){
					
				if(!networkQuery.isCreateInput(inputs, source.getChannel_id())){
					
					InputChannelBO srcChannelBO = new InputChannelBO();
					srcChannelBO.setBundleId(source.getBundle_id());
					srcChannelBO.setChannelId(source.getChannel_id());
					needCreateInputChannels.add(srcChannelBO);
				}
			}
    	}
    	
    	if(needCreateInputChannels.size() > 0){
    		int count = needCreateInputChannels.size();
    		List<CreateInputResponseBO> inputResponse = networkHttpService.createInput(netBundle.getNetIp(), netBundle.getNetId(), count);
    		for(int i=0; i<count; i++){
    			CreateInputResponseBO response = inputResponse.get(i);
    			InputChannelBO channel = needCreateInputChannels.get(i);
    			
    			NetworkInputPO input = new NetworkInputPO();
    			input.setBundleId(channel.getBundleId());
    			input.setChannelId(channel.getChannelId());
    			input.setNetId(netBundle.getNetId());
    			input.setNetIp(netBundle.getNetIp());
    			input.setPort(Long.valueOf(response.getPort()));
    			input.setSid(response.getSid().longValue());
    			
    			inputs.add(input);
    		}
    		
    		networkInputDao.saveAll(inputs);
    	}
    	
    	//创建网络输出资源
    	List<NetworkOutputPO> outputs = networkOutputDao.findByBundleId(bundleId);
    	
    	List<ChannelBO> needCreateOutputChannels = new ArrayList<ChannelBO>(); 
    	for(ChannelBO channel: channels){
    		if(ChannelType.fromType(channel.getChannel_id()).isDecode()){
    			if(!networkQuery.isCreateInput(inputs, channel.getChannel_id())){
    				needCreateOutputChannels.add(channel);
    			}
    		}
    	}
    	
    	if(needCreateOutputChannels.size() > 0){
    		List<String> ports = new ArrayList<String>();
    		for(ChannelBO channelBO: needCreateOutputChannels){
    			Long port = ChannelType.fromType(channelBO.getChannel_id()).getPort();
    			ports.add(port.toString());
    		}
    		
    		List<CreateOutputResponseBO> outputResponse = networkHttpService.createOutput(netBundle.getNetIp(), netBundle.getNetId(), netBundle.getBundleIp(), ports);
    		for(CreateOutputResponseBO response: outputResponse){
    			NetworkOutputPO output = new NetworkOutputPO();
    			output.setBundleId(bundleId);
    			output.setBundleIp(netBundle.getBundleIp());
    			output.setChannelId(ChannelType.fromPort(Long.valueOf(response.getData().getPort())).getType());
    			output.setNetId(netBundle.getNetId());
    			output.setNetIp(netBundle.getNetIp());
    			output.setPort(Long.valueOf(response.getData().getPort()));
    			output.setSid(response.getData().getSid().longValue());
    			
    			outputs.add(output);
    		}
    		
    		networkOutputDao.saveAll(outputs);
    	}
    	
    	//查找切换任务
    	List<Long> outputIds = new ArrayList<Long>();
    	for(NetworkOutputPO output: outputs){
    		outputIds.add(output.getId());
    	}
    	
    	List<NetworkMapPO> maps = networkMapDao.findByOutputIdIn(outputIds);
		
		String[][] TerminalEncodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalEncodeParam), String[][].class);
    	String[][] TerminalDecodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalDecodeParam), String[][].class);
    	String[][] TerminalCallingParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalCallingParam), String[][].class);
    	    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	//通话设置请求
    	String callingRes = HttpClient.get("http://" + netBundle.getBundleIp() + TerminalParam.GET_JV210_CALLSETTING_SUFFIX);
    	String[][] callingParam = TerminalParam.html2Data(callingRes, TerminalCallingParam);
    	
    	//编码设置请求
    	String encodeRes = HttpClient.get("http://" + netBundle.getBundleIp() + TerminalParam.GET_JV210_ENCODEPARAM_SUFFIX);
    	String[][] encodeParam = TerminalParam.html2Data(encodeRes, TerminalEncodeParam);
    	
    	//解码设置请求
    	String decodeRes = HttpClient.get("http://" + netBundle.getBundleIp() + TerminalParam.GET_JV210_DECODEPARAM_SUFFIX);
    	String[][] decodeParam = TerminalParam.html2Data(decodeRes, TerminalDecodeParam);
    	
    	//参数定义（video）
    	Long main_bitrate = 4000000l;
    	String main_resoluton = "1920x1080";
    	Long main_video_encode_port = 10000l;
    	String main_video_encode_address = "";
    	Long main_video_decode_port = 10000l;
    	Long sub_bitrate = 4000000l;
    	String sub_resoluton = "1920x1080";
    	Long sub_video_encode_port = 10000l;
    	String sub_video_encode_address = "";
    	Long sub_video_decode_port = 10000l;
    	//参数定义（audio）
    	String audio_codec = "aac";
    	Long sample_rate = 48000l;
    	String gain = "0";
    	Long audio_encode_port = 10000l;
    	String audio_encode_address = "";
    	Long audio_decode_port = 10000l;
    	
    	//标识（是否"呼叫"--设置参数）
    	boolean paramFlag = false;
    	
    	//标识（设置大小屏）
    	boolean screenFlag = false;
    	
    	//显示模式：单画面--"0",二等分--"1",画中画--"2"
    	String screenLayout = "0";
    	//画面：远端1--"0",远端2--"1",本地1--"2",本地2--"3"
    	String picture1 = "0";
    	String picture2 = "0";

    	List<NetworkMapPO> needSwitchMaps = new ArrayList<NetworkMapPO>();
    	List<NetworkMapPO> needCutMaps = new ArrayList<NetworkMapPO>();
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			NetworkOutputPO netDst = networkQuery.queryOutput(outputs, bundleId, channelId);
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				String srcChannelId = source.getChannel_id();
				String srcLayerId = source.getLayer_id();
				
				NetworkInputPO netSrc = networkQuery.queryInput(inputs, srcBundleId, srcChannelId);
				
				//layerId不相同，向其接入发pulldata
				if(srcLayerId != null && !srcLayerId.equals(NodeType.NETWORK.getId())){

					processReceivedMsg.processPullDataRunable(srcLayerId, NodeType.NETWORK.getId(), srcBundleId, srcChannelId, bundleId, channelId, netSrc.getNetIp(), netSrc.getPort().toString());
							
				}
				
				NetworkMapPO map = networkQuery.queryMap(maps, netDst.getId());
				
				if(map == null){
					map = new NetworkMapPO();
				}
				
				map.setInputId(netSrc.getId());
				map.setInputSid(netSrc.getSid());
				map.setOutputId(netDst.getId());
				map.setOutputSid(netDst.getSid());
				
				needSwitchMaps.add(map);
				
			}
			
			//校验是否被已经"呼叫"：
			if(encodeParam[0][0].equals("1") && encodeParam[1][0].equals("1") && encodeParam[4][0].equals("1") && decodeParam[0][0].equals("1")
					&& decodeParam[1][0].equals("1") && decodeParam[2][0].equals("1") && decodeParam[3][0].equals("1")){
				
				if(source == null){
					
					NetworkMapPO map = networkQuery.queryMap(maps, netDst.getId());
					if(map != null){
						needCutMaps.add(map);
					}
					
					paramFlag = false; 					
					screenFlag = true;
		    		screenLayout = "0";
		    		picture1 = "2";
				}
					
			}else{
				if(channelId.equals(ChannelType.VIDEOENCODE1.getType())){
	    			main_bitrate = baseParam.getBitrate();
	    			main_resoluton = baseParam.getResolution();
	    			
	    			NetworkInputPO encodeInput = networkQuery.queryInput(inputs, bundleId, channelId);
	    			main_video_encode_address = encodeInput.getNetIp();
	    	    	main_video_encode_port = encodeInput.getPort();
	    		}else if(channelId.equals(ChannelType.VIDEOENCODE2.getType())){
	    			sub_bitrate = baseParam.getBitrate();
	    			sub_resoluton = baseParam.getResolution();
	    			
	    			NetworkInputPO encodeInput = networkQuery.queryInput(inputs, bundleId, channelId);
	    			main_video_encode_address = encodeInput.getNetIp();
	    	    	main_video_encode_port = encodeInput.getPort();
	    		}else if(channelId.equals(ChannelType.AUDIOENCODE1.getType())){
	    			audio_codec = baseParam.getCodec();
	    			sample_rate = baseParam.getSample_rate();
	    			gain = baseParam.getGain();
	    			
	    			NetworkInputPO encodeInput = networkQuery.queryInput(inputs, bundleId, channelId);
	    			main_video_encode_address = encodeInput.getNetIp();
	    	    	main_video_encode_port = encodeInput.getPort();
	    		}else if(channelId.equals(ChannelType.VIDEODECODE1.getType())){
	    			NetworkOutputPO decodeOutput = networkQuery.queryOutput(outputs, bundleId, channelId);
	    			main_video_decode_port = decodeOutput.getPort();
	    		}else if(channelId.equals(ChannelType.VIDEODECODE2.getType())){
	    			NetworkOutputPO decodeOutput = networkQuery.queryOutput(outputs, bundleId, channelId);
	    			main_video_decode_port = decodeOutput.getPort();
	    		}else if(channelId.equals(ChannelType.AUDIODECODE1.getType())){
	    			NetworkOutputPO decodeOutput = networkQuery.queryOutput(outputs, bundleId, channelId);
	    			main_video_decode_port = decodeOutput.getPort(); 
	    		}
				
				paramFlag = true;
			}
		}  
    	
    	List<ScreenBO> screens = bundle.getScreens();
    	if(screens != null && screens.size() > 0){
    		//TODO:现在做的是对单屏处理（JV210）
    		for(ScreenBO screen: screens){
    			List<RectBO> rects = screen.getRects();
    			boolean isSingle = true; 
    			if(rects != null && rects.size() > 0){
    				for(RectBO rect: rects){
    					if(rect.getWidth().equals(10000l) && rect.getHeight().equals(10000l)){
    						picture1 = Jv210Param.fromName(rect.getChannel()).getValue();
    					}else{
    						picture2 = Jv210Param.fromName(rect.getChannel()).getValue();
    						if(rect.getType().equals("single")){
    							isSingle = false;
    						}
    					}
    				}
    			}
    			if(isSingle){
    				screenLayout = "0";
    			}else{
    				screenLayout = "2";
    			}
    		}
    		
    		screenFlag = true;
    	}
    	
    	if(paramFlag){
    		
    		screenFlag = true;
    		screenLayout = "0";
    		picture1 = "2";
        	
        	Long trasferMainBitrate = main_bitrate/1000l;
        	Long mainVideoBitrate = (long) (trasferMainBitrate * (7.0f/10.0f) > 4000l?4000l: trasferMainBitrate * (7.0f/10.0f));
        	Long mainAllBitrate = (long) (mainVideoBitrate * (28.0f/10.0f) > 10000l?10000l: mainVideoBitrate * (28.0f/10.0f));
        	String mainWidth = main_resoluton.split("x")[0];
        	String mainHeight = main_resoluton.split("x")[1];
        	
        	Long trasferSubBitrate = sub_bitrate/1000l;
        	Long subVideoBitrate = (long) (trasferSubBitrate * (7.0f/10.0f) > 4000l?4000l: trasferSubBitrate * (7.0f/10.0f));
        	Long subAllBitrate = (long) (subVideoBitrate * (28.0f/10.0f) > 10000l?10000l: subVideoBitrate * (28.0f/10.0f));
        	String subWidth = sub_resoluton.split("x")[0];
        	String subHeight = sub_resoluton.split("x")[1];
        	
        	//设置jv210编码
        	encodeParam[0][0] = "1";
        	encodeParam[0][1] = main_video_encode_address;
        	encodeParam[0][2] = main_video_encode_port.toString();
        	encodeParam[0][3] = audio_encode_address;
        	encodeParam[0][4] = audio_encode_port.toString();
        	
        	encodeParam[1][0] = "1";
        	encodeParam[1][1] = sub_video_encode_address;
        	encodeParam[1][2] = sub_video_encode_port.toString();

        	encodeParam[2][0] = mainAllBitrate.toString();
    		encodeParam[2][1] = mainVideoBitrate.toString();
    		encodeParam[2][2] = mainWidth;
    		encodeParam[2][3] = mainHeight;
    		
    		encodeParam[3][0] = subAllBitrate.toString();
    		encodeParam[3][1] = subVideoBitrate.toString();
    		encodeParam[3][2] = subWidth;
    		encodeParam[3][3] = subHeight;
    		
        	encodeParam[4][0] = "1";
    		encodeParam[4][1] = Jv210Param.fromName(audio_codec).getValue();
    		encodeParam[4][4] = Jv210Param.fromName(audio_codec).getRelation();
    		
    		//设置jv210解码
    		decodeParam[0][0] = "1";
    		decodeParam[0][4] = main_video_decode_port.toString();
    		decodeParam[0][5] = audio_decode_port.toString();
    		
    		decodeParam[1][0] = "1";
    		decodeParam[1][4] = sub_video_decode_port.toString();
    		
    		decodeParam[2][0] = "1";
    		
    		decodeParam[3][0] = "1";
    		decodeParam[3][3] = gain;
    		
    		List<BasicNameValuePair> encodeBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair encodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(encodeParam));
    		encodeBody.add(encodePair);
    		HttpAsyncClient.getInstance().formPost("http://" + netBundle.getBundleIp() + TerminalParam.POST_JV210_ENCODEPARAM_SUFFIX, null, encodeBody, null);
        	
    		List<BasicNameValuePair> decodeBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair decodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(decodeParam));
    		decodeBody.add(decodePair);
    		HttpAsyncClient.getInstance().formPost("http://" + netBundle.getBundleIp() + TerminalParam.POST_JV210_DECODEPARAM_SUFFIX, null, decodeBody, null);
    	  		
    	}
    	
    	if(passbyBO != null){
        	String type = passbyBO.getType();
        	if(type.equals("bind_repeater_node")){
        		screenFlag = false;
        	}
    	}
    	
    	if(screenFlag){
    		
    		callingParam[1][0] = screenLayout;
			callingParam[1][3] = picture1;
			callingParam[1][5] = picture2;
			
			callingParam[5][0] = decodeParam[0][5];
			callingParam[5][1] = decodeParam[0][4];
			callingParam[5][2] = decodeParam[1][4];
    		
    		List<BasicNameValuePair> callingBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair callingPair = new BasicNameValuePair("setString", TerminalParam.array2Data(callingParam));
    		callingBody.add(callingPair);
    		System.out.println("画面：" + screenLayout);
    		HttpAsyncClient.getInstance().formPost("http://" + netBundle.getBundleIp() + TerminalParam.POST_JV210_CALLSETTING_SUFFIX, null, callingBody, null);
    	}
    	
    	if(needSwitchMaps.size() > 0){
    		
        	//切换数据流请求参数
        	List<SwitchRequestBO> requests = new ArrayList<SwitchRequestBO>();
        	for(NetworkMapPO map: needSwitchMaps){
        		SwitchRequestBO request = new SwitchRequestBO();
        		request.setSid_in(map.getInputSid().intValue());
        		request.setSid_out(map.getOutputSid().intValue());
        		requests.add(request);
        	}
        	
        	List<SwitchResponseBO> switchResponse = networkHttpService.switchData(netBundle.getNetIp(), requests);
    		
        	networkMapDao.saveAll(needSwitchMaps);
    	}
    	
    	if(needCutMaps.size() > 0){
    		
    		//断开切换
    		List<Long> sids = new ArrayList<Long>();
    		for(NetworkMapPO map: needCutMaps){
    			sids.add(map.getOutputSid());
    		}
    		
    		List<CutSwitchResponseBO> cutSwitchResponse = networkHttpService.cutSwitch(netBundle.getNetIp(), sids);
    		
    		networkMapDao.deleteInBatch(needCutMaps);
    	}
	}
	
	 /**处理close_bundle信息的jv210类型信息
     * @throws Exception */
    private void processCloseBundleJv210Msg(BundleBO bundle, NetBundleBO netBundle) throws Exception{
    	
    	String[][] TerminalEncodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalEncodeParam), String[][].class);
    	String[][] TerminalDecodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalDecodeParam), String[][].class);
    	
    	String bundleId = bundle.getBundle_id();
    	
    	List<NetworkMapPO> maps = networkMapDao.findByBundleId(bundleId);
    	
    	//编码设置请求
    	String encodeRes = HttpClient.get("http://" + netBundle.getBundleIp() + TerminalParam.GET_JV210_ENCODEPARAM_SUFFIX);
    	String[][] encodeParam = TerminalParam.html2Data(encodeRes, TerminalEncodeParam);
    	
    	//通话设置请求
    	String decodeRes = HttpClient.get("http://" + netBundle.getBundleIp() + TerminalParam.GET_JV210_DECODEPARAM_SUFFIX);
    	String[][] decodeParam = TerminalParam.html2Data(decodeRes, TerminalDecodeParam);
    	
    	encodeParam[0][0] = "0";
    	encodeParam[1][0] = "0";
    	encodeParam[4][0] = "0";
    	decodeParam[0][0] = "0";
    	decodeParam[1][0] = "0";
    	decodeParam[2][0] = "0";
    	decodeParam[3][0] = "0";
    	
		List<BasicNameValuePair> encodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair encodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(encodeParam));
		encodeBody.add(encodePair);
		HttpAsyncClient.getInstance().formPost("http://" + netBundle.getBundleIp() + TerminalParam.POST_JV210_ENCODEPARAM_SUFFIX, null, encodeBody, null);
    	
		List<BasicNameValuePair> decodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair decodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(decodeParam));
		decodeBody.add(decodePair);
		HttpAsyncClient.getInstance().formPost("http://" + netBundle.getBundleIp() + TerminalParam.POST_JV210_DECODEPARAM_SUFFIX, null, decodeBody, null);
		
		if(maps.size() > 0){
			List<Long> sids = new ArrayList<Long>();
			for(NetworkMapPO map: maps){
				sids.add(map.getOutputSid());
			}
			
			List<CutSwitchResponseBO> cutSwitchResponse = networkHttpService.cutSwitch(netBundle.getNetIp(), sids);
		
			networkMapDao.deleteInBatch(maps);
		}
    }
	
}
