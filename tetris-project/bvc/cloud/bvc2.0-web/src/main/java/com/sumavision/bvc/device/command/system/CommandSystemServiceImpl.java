package com.sumavision.bvc.device.command.system;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandVodDAO;
import com.sumavision.bvc.command.group.dao.UserLiveCallDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.UserLiveCallPO;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.communication.http.HttpClient;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceLayerDAO;
import com.sumavision.tetris.auth.token.TerminalType;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandSystemServiceImpl 
* @Description: 指挥系统查询控制业务
* @author zsy
* @date 2019年12月23日 上午09:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandSystemServiceImpl {
	
	@Autowired
	private CommandGroupForwardDAO commandGroupForwardDao;
	
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private UserLiveCallDAO userLiveCallDao;
	
	@Autowired
	private CommandGroupForwardDemandDAO commandGroupForwardDemandDao;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	ResourceLayerDAO conn_layer;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	/**
	 * 获取指挥系统中所有的业务转发<br/>
	 * <p>包括指挥、指挥转发、点播、通话</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午7:00:15
	 * @param userId
	 * @return AllForwardBO列表
	 * @throws Exception
	 */
	public List<AllForwardBO> obtainAllForwards(Long userId) throws Exception{
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		
		//
		List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.QT_ZK);
		List<String> encoderIds = new ArrayList<String>();
		for(UserBO user : users){
			String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
			if(encoderId != null) encoderIds.add(encoderId);
		}
		List<BundlePO> userBundles = resourceBundleDao.findByBundleIds(encoderIds);
		if(userBundles != null) queryBundles.addAll(userBundles);
		
		List<AllForwardBO> allForwards = new ArrayList<AllForwardBO>();
		
		//指挥
		List<CommandGroupForwardPO> forwards = commandGroupForwardDao.findByExecuteStatus(ExecuteStatus.DONE);
		for(CommandGroupForwardPO forward : forwards){
			AllForwardBO allForward = new AllForwardBO().setByForward(forward);
			allForwards.add(allForward);
		}
		
		//指挥转发，只处理转发设备
		List<CommandGroupForwardDemandPO> demands = commandGroupForwardDemandDao.findByExecuteStatus(ForwardDemandStatus.DONE);
		for(CommandGroupForwardDemandPO demand : demands){
			if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_DEVICE)){
				AllForwardBO allForward = new AllForwardBO().setByDevice(demand);
				allForwards.add(allForward);
			}
		}
		
		//点播
		List<CommandVodPO> vods = commandVodDao.findAll();
		for(CommandVodPO vod : vods){
			AllForwardBO allForward = new AllForwardBO().setByVod(vod);
			allForwards.add(allForward);
		}
		
		//通话
		List<UserLiveCallPO> calls = userLiveCallDao.findAll();
		for(UserLiveCallPO call : calls){
			AllForwardBO calledToCall = new AllForwardBO().setByUserLiveCallPOCalledToCall(call);
			allForwards.add(calledToCall);
			AllForwardBO callToCalled = new AllForwardBO().setByUserLiveCallPOCallToCalled(call);
			allForwards.add(callToCalled);
		}
		
		//转发与bundle取交集。因为转发中的某些设备可能不在queryBundles里，尤其是播放器，所以同时统计需要额外查询的bundleId
		Set<String> extraBundleIds = new HashSet<String>();
		Set<BundlePO> usefulBundlesSet = new HashSet<BundlePO>();
		Set<AllForwardBO> usefulForwards = new HashSet<AllForwardBO>();
		for(BundlePO bundlePO : queryBundles){
			for(AllForwardBO allForward : allForwards){
				String srcBundleId = allForward.getSrcBundleId();
				String dstBundleId = allForward.getDstBundleId();
				if(bundlePO.getBundleId().equals(srcBundleId)
						|| bundlePO.getBundleId().equals(dstBundleId)){
					usefulBundlesSet.add(bundlePO);
					usefulForwards.add(allForward);
					if(bundlePO.getBundleId().equals(srcBundleId)){
						//添加目的设备
						BundlePO dstBundle = queryUtil.queryBundlePOByBundleId(queryBundles, dstBundleId);
						if(dstBundle == null){
							extraBundleIds.add(dstBundleId);
						}else{
							usefulBundlesSet.add(dstBundle);
						}
					}else if(bundlePO.getBundleId().equals(allForward.getDstBundleId())){
						//添加源设备
						BundlePO srcBundle = queryUtil.queryBundlePOByBundleId(queryBundles, srcBundleId);
						if(srcBundle == null){
							extraBundleIds.add(srcBundleId);							
						}else{
							usefulBundlesSet.add(srcBundle);
						}
					}
				}
			}
		}
		
		//额外查询
		List<BundlePO> extraBundles = resourceBundleDao.findByBundleIds(extraBundleIds);
		if(extraBundles != null) usefulBundlesSet.addAll(extraBundles);
		
		//Set转List
		List<BundlePO> usefulBundles = new ArrayList<BundlePO>(usefulBundlesSet);
		
		JSONArray bundles = obtainAllBundlesFromLayers(usefulBundles);
		
		//获取码率并添加（只考虑源和目的都是设备的情况）
		for(AllForwardBO usefulForward : usefulForwards){
			JSONObject srcBundle = queryBundleByBundleId(bundles, usefulForward.getSrcBundleId());
			String srcVideoBitrate = getSrcBitrateFromLocal(srcBundle, usefulForward.getSrcVideoChannelId());
			if(srcVideoBitrate != null) usefulForward.setSrcVideoBitrate(srcVideoBitrate);
			String srcAudioBitrate = getSrcBitrateFromLocal(srcBundle, usefulForward.getSrcAudioChannelId());
			if(srcAudioBitrate != null) usefulForward.setSrcAudioBitrate(srcAudioBitrate);
			
			JSONObject dstBundle = queryBundleByBundleId(bundles, usefulForward.getDstBundleId());
			String dstVideoBitrate = getDstBitrateFromLocal(dstBundle, usefulForward.getDstVideoChannelId());
			if(dstVideoBitrate != null) usefulForward.setDstVideoBitrate(dstVideoBitrate);
			String dstAudioBitrate = getDstBitrateFromLocal(dstBundle, usefulForward.getDstAudioChannelId());
			if(dstAudioBitrate != null) usefulForward.setDstAudioBitrate(dstAudioBitrate);
		}
		
		//转成List并排序
		List<AllForwardBO> list = new ArrayList<AllForwardBO>(usefulForwards);
		Collections.sort(list, new Comparator<AllForwardBO>(){
			public int compare(AllForwardBO o1, AllForwardBO o2) {
				return o1.getDstInfo().compareTo(o2.getDstInfo());
			}});
		
		return list;
		
	}
	
	/**
	 * 从接入层查询bundle状态<br/>
	 * <p>无法保证所有的bundle都能查到</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月25日 上午8:28:54
	 * @param bundlePOs 要查询的BundlePO列表
	 * @return JSONArray 查询到的bundle状态json数组
	 * @throws Exception
	 */
	private JSONArray obtainAllBundlesFromLayers(List<BundlePO> bundlePOs) throws Exception{
		
		Set<String> layerIds = new HashSet<String>();
		for(BundlePO bundlePO : bundlePOs){
			layerIds.add(bundlePO.getAccessNodeUid());
		}
		layerIds.remove(null);

		List<WorkNodePO> workNodes = conn_layer.findAll();
		
		JSONArray bundles = new JSONArray();
		for(String layerId : layerIds){
			WorkNodePO workNode = queryLayerByLayerId(workNodes, layerId);
			if(workNode == null) continue;
			String server = workNode.getIp() + ":8007";
			JSONArray getBundles = obtainBundlesFromLayer(server);
			
			//检查这些bundle是不是这个layer上的，防止从错误的layer上查到，同时，bundlePOs中没有的bundle也不会被添加进结果
			for(int i=0; i<getBundles.size(); i++){
				JSONObject bundleJson = getBundles.getJSONObject(i);
				String bundleId = bundleJson.getString("bundleID");
				BundlePO bundlePO = queryUtil.queryBundlePOByBundleId(bundlePOs, bundleId);
				if(bundlePO!=null && workNode.getNodeUid().equals(bundlePO.getAccessNodeUid())){
					bundles.add(bundleJson);
				}
			}
		}
		
		return bundles;
	}
	
	/**
	 * 获取一个接入层上所有的bundle状态信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月25日 上午8:33:07
	 * @param server 192.165.x.x:8007
	 * @return 该接入层上所有的bundle状态信息
	 * @throws Exception
	 */
	private JSONArray obtainBundlesFromLayer(String server) throws Exception{
		JSONArray bundles = new JSONArray();
		try{
			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(5).setConnectTimeout(1000).setSocketTimeout(1000).build();
			String header = "https";
	//		String server = "192.165.56.70:8007";
			String path = "action";
			String method = "get_monitor";
			String result1 = HttpClient.getHttps(header+"://"+server+ "/" + path + "/" + method, config);
			bundles = JSON.parseObject(result1).getJSONArray("jv210s");
		}catch(Exception e){
			log.error(server + " 获取失败");
			e.printStackTrace();
		}
		
		return bundles;
	}
	
	@SuppressWarnings("unused")
	private JSONArray testObtainLayerData() throws Exception{
		JSONArray bundles = new JSONArray();
//		HttpClient httpClient = new HttpClient();
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(5).setConnectTimeout(1000).setSocketTimeout(1000).build();
		String header = "https";
		String server = "192.165.56.70:8007";
		String path = "action";
		String method = "get_monitor";
		String result1 = HttpClient.getHttps(header+"://"+server+ "/" + path + "/" + method, config);
		bundles.addAll(JSON.parseObject(result1).getJSONArray("jv210s"));
		
		server = "192.165.56.134:8007";
		String result2 = HttpClient.getHttps(header+"://"+server+ "/" + path + "/" + method, config);
		bundles.addAll(JSON.parseObject(result2).getJSONArray("jv210s"));
		
		return bundles;
	}
	
	/**
	 * 从local字段中获取源码率<br/>
	 * <p>接入层从编码端接收到的码率。在端口复用时，该方法查到的是该端口的总码率</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月25日 上午9:10:52
	 * @param bundle 完整的json
	 * @param channelId
	 * @return xxxbps，查不到则为null
	 */
	private String getSrcBitrateFromLocal(JSONObject bundle, String channelId){
		try{
			JSONObject channel = queryChannelByChannelId(bundle, channelId);
			if(channel == null) return null;
			JSONArray local = channel.getJSONArray("local");
			String stream = local.getJSONObject(0).getString("stream");
			stream.split("\\|");
			String bitrate = stream.split("\\|")[1];
			if(bitrate.contains("bps")){
				return bpsToKbpsMbps(bitrate);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 从多个pear字段中获取源码率<br/>
	 * <p>实际上是源接入层给目的接入层的发送码率</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月25日 上午8:57:21
	 * @param bundle 完整的json
	 * @param channelId
	 * @param dstLayerIp 
	 * @return xxxbps，查不到则为null
	 */
	@SuppressWarnings("unused")
	private String getSrcBitrateFromPear(JSONObject bundle, String channelId, String dstLayerIp){
		try{
			JSONObject channel = queryChannelByChannelId(bundle, channelId);
			if(channel == null) return null;
			JSONArray peer = channel.getJSONArray("peer");
			
			//选择pear
			JSONObject thePeer = null;
			for(int i=0; i<peer.size(); i++){
				JSONObject aPeer = peer.getJSONObject(i);
				String stream = aPeer.getString("stream");
				if(stream.contains("-->" + dstLayerIp)){
					thePeer = aPeer;
					break;
				}
			}			
			String stream = thePeer.getString("stream");
			String bitrate = stream.split("\\|")[1];
			if(bitrate.contains("bps")){
				return bpsToKbpsMbps(bitrate);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 从local字段中获取目的码率<br/>
	 * <p>接入层发给解码端的码率</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月25日 上午9:09:56
	 * @param bundle 完整的json
	 * @param channelId
	 * @return xxxbps，查不到则为null
	 */
	private String getDstBitrateFromLocal(JSONObject bundle, String channelId){
		try{
			JSONObject channel = queryChannelByChannelId(bundle, channelId);
			if(channel == null) return null;
			JSONObject local = channel.getJSONObject("local");
			String stream = local.getString("stream");
			String bitrate = stream.split("\\|")[1];
			if(bitrate.contains("bps")){
				return bpsToKbpsMbps(bitrate);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 从pear字段中获取目的码率<br/>
	 * <p>实际上是目的接入层的接收码率</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月25日 上午9:08:57
	 * @param bundle 完整的json
	 * @param channelId
	 * @return xxxbps，查不到则为null
	 */
	@SuppressWarnings("unused")
	private String getDstBitrateFromPear(JSONObject bundle, String channelId){
		try{
			JSONObject channel = queryChannelByChannelId(bundle, channelId);
			if(channel == null) return null;
			JSONArray peer = channel.getJSONArray("peer");
			//正常peer只有1个元素
			JSONObject thePeer = peer.getJSONObject(0);			
			String stream = thePeer.getString("stream");
			String bitrate = stream.split("\\|")[1];
			if(bitrate.contains("bps")){
				return bpsToKbpsMbps(bitrate);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private String bpsToKbpsMbps(String bps){
		try{
			bps = bps.trim();
			int b = Integer.parseInt(bps.replace("bps", ""));
			DecimalFormat decimalFormat = new DecimalFormat(".00");
			if(b >= 1048576){
				return decimalFormat.format((float)b/1048576) + "Mbps";
			}else if(b >= 1024){
				return decimalFormat.format((float)b/1024) + "Kbps";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bps;
	}
	
	private WorkNodePO queryLayerByLayerId(List<WorkNodePO> layers, String layerId){
		for(WorkNodePO layer : layers){
			if(layerId.equals(layer.getNodeUid())){
				return layer;
			}
		}
		return null;
	}

	private JSONObject queryBundleByBundleId(JSONArray bundles, String bundleId){
		if(bundleId == null) return null;
		for(int i=0; i<bundles.size(); i++){
			JSONObject bundle = bundles.getJSONObject(i);
			if(bundleId.equals(bundle.getString("bundleID"))){
				return bundle;
			}
		}
		return null;
	}

	private JSONObject queryChannelByChannelId(JSONObject bundle, String channelId){
		if(channelId == null) return null;
		JSONArray channels = bundle.getJSONArray("channels");
		for(int i=0; i<channels.size(); i++){
			JSONObject channel = channels.getJSONObject(i);
			if(channelId.equals(channel.getString("id"))){
				return channel;
			}
		}
		return null;
	}
}
