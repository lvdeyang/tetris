package com.sumavision.bvc.meeting.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sumavision.bvc.device.group.exception.ConfigHasNotSavedOrStartedException;
import com.sumavision.bvc.meeting.logic.dao.CombineAudioDstDao;
import com.sumavision.bvc.meeting.logic.dao.CombineVideoDstDao;
import com.sumavision.bvc.meeting.logic.dao.MediaPushDao;
import com.sumavision.bvc.meeting.logic.dao.OmcRecordDao;
import com.sumavision.bvc.meeting.logic.dao.OutConnMediaMuxDao;
import com.sumavision.bvc.meeting.logic.po.CombineAudioDstPO;
import com.sumavision.bvc.meeting.logic.po.CombineVideoDstPO;
import com.sumavision.bvc.meeting.logic.po.MediaPushPO;
import com.sumavision.bvc.meeting.logic.po.OutConnMediaMuxPO;
import com.sumavision.bvc.meeting.logic.record.omc.CdnPO;
import com.sumavision.bvc.meeting.logic.record.omc.CdnService;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  MeetingEntity   
 * @Description: 逻辑通道转换，处理业务层下发的业务参数，包括但不限于呼叫、预览、转发、合屏/混音，
 * @Description: operationCombine主要处理包括将已有合屏混音channel合并到转发、呼叫、修改及删除合屏混音；删除转发命令合并到转发，转发合并到呼叫，删除转发合并到挂断；
 * @Description: recordOperationCombine处理录制流程，将录制命令转换为推流、OMC对接
 * @Description: executeSuccess需要在执行成功后调用，将新建的channel存入数据库，将删除的任务从数据库中删除
 * @author: zsy
 * @date:   2018年7月13日 下午2:46:48   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Slf4j
@Component
@Service
public class MeetingEntity {
	@Autowired
	CombineVideoDstDao combineVideoDstDao;
	@Autowired
	CombineAudioDstDao combineAudioDstDao;
	@Autowired
	OutConnMediaMuxDao outConnMediaMuxDao;
	@Autowired
	MediaPushDao mediaPushDao;
	@Autowired
	CdnService cdnService;
	@Autowired
	OmcRecordDao omcRecordDao;

	/**
	 * PO的map缓存表，每次调用之前应该clear()清空
	 */
	Map<String, AbstractBasePO> poMap = new HashMap<String, AbstractBasePO>();

	public JSONObject operationCombine(JSONObject orgOperation) throws Exception{
		poMap.clear();
		
		JSONObject combinedOperation = (JSONObject) orgOperation.clone();
		//将已存在的合屏/混音channel替换到转发命令
		if(combinedOperation.containsKey("forwardSet")){
			JSONArray forwardSet = combinedOperation.getJSONArray("forwardSet");
			for(int i=0; i<forwardSet.size(); i++){
				JSONObject aForward = forwardSet.getJSONObject(i);
				if(!aForward.containsKey("src")){
					continue;
				}
				String srcType = aForward.getJSONObject("src").getString("type");
				if(srcType.equals("combineVideo")){
					//查数据库，是否存在该合屏
					String uuid = aForward.getJSONObject("src").getString("uuid");
					CombineVideoDstPO combineVideoDstPO = (CombineVideoDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineVideoDstDao);
					if(null != combineVideoDstPO){
						//存在该合屏，取出channelID
						aForward.getJSONObject("src").put("type", "channel");
						aForward.getJSONObject("src").put("layerId", combineVideoDstPO.getLayerId());
						aForward.getJSONObject("src").put("bundleId", combineVideoDstPO.getBundleId());
						aForward.getJSONObject("src").put("channelId", combineVideoDstPO.getChannelId());
					}else{
						//新建操作在combineVideoSet中或返回中完成
					}
				}else if(srcType.equals("combineAudio")){
					//查数据库，是否存在该混音
					String uuid = aForward.getJSONObject("src").getString("uuid");
					CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
					if(null != combineAudioDstPO){
						//存在该混音，取出channelID
						aForward.getJSONObject("src").put("type", "channel");
						aForward.getJSONObject("src").put("layerId", combineAudioDstPO.getLayerId());
						aForward.getJSONObject("src").put("bundleId", combineAudioDstPO.getBundleId());
						aForward.getJSONObject("src").put("channelId", combineAudioDstPO.getChannelId());
					}else{
						//新建操作在combineAudioSet中或返回中完成
					}
				}else if(srcType.equals("mediaPush")){
					//查数据库，是否存在该调阅
					String uuid = aForward.getJSONObject("src").getString("uuid");
					MediaPushPO mediaPushPO = (MediaPushPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, mediaPushDao);
					if(null != mediaPushPO){			
						//存在该调阅，取出channelID，根据视/音频类型，给channelID添加后缀
						String suffix = "_video";
						String base_type = aForward.getJSONObject("dst").getString("base_type");
						if(base_type.toLowerCase().indexOf("audio") != -1){
							suffix = "_audio";
						}
						aForward.getJSONObject("src").put("type", "channel");
						aForward.getJSONObject("src").put("layerId", mediaPushPO.getLayerId());
						aForward.getJSONObject("src").put("bundleId", mediaPushPO.getBundleId());
						aForward.getJSONObject("src").put("channelId", mediaPushPO.getChannelId() + suffix);
					}else{
						//新建操作在mediaPushSet中或返回中完成
					}
				}
			}
		}
		//将已存在的合屏/混音channel替换到呼叫命令
		if(combinedOperation.containsKey("connect")){
			JSONArray connect = combinedOperation.getJSONArray("connect");
			for(int i=0; i<connect.size(); i++){
				JSONObject aConnect = connect.getJSONObject(i);
				//可能出现没有source_param的情况
				if(aConnect.containsKey("source_param")){
					String srcType = aConnect.getJSONObject("source_param").getString("type");
					if(srcType.equals("combineVideo")){
						//查数据库，是否存在该合屏
						String uuid = aConnect.getJSONObject("source_param").getString("uuid");
						CombineVideoDstPO combineVideoDstPO = (CombineVideoDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineVideoDstDao);
						if(null != combineVideoDstPO){
							//存在该合屏，取出channelID
							aConnect.getJSONObject("source_param").put("type", "channel");
							aConnect.getJSONObject("source_param").put("layerId", combineVideoDstPO.getLayerId());
							aConnect.getJSONObject("source_param").put("bundleId", combineVideoDstPO.getBundleId());
							aConnect.getJSONObject("source_param").put("channelId", combineVideoDstPO.getChannelId());
						}
					}else if(srcType.equals("combineAudio")){
						//查数据库，是否存在该混音
						String uuid = aConnect.getJSONObject("source_param").getString("uuid");
						CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
						if(null != combineAudioDstPO){
							//存在该混音，取出channelID
							aConnect.getJSONObject("source_param").put("type", "channel");
							aConnect.getJSONObject("source_param").put("layerId", combineAudioDstPO.getLayerId());
							aConnect.getJSONObject("source_param").put("bundleId", combineAudioDstPO.getBundleId());
							aConnect.getJSONObject("source_param").put("channelId", combineAudioDstPO.getChannelId());
						}
					}else if(srcType.equals("mediaPush")){
						//查数据库，是否存在该调阅
						String uuid = aConnect.getJSONObject("source_param").getString("uuid");
						MediaPushPO mediaPushPO = (MediaPushPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, mediaPushDao);
						if(null != mediaPushPO){			
							//存在该调阅，取出channelID，根据视/音频类型，给channelID添加后缀
							String suffix = "_video";
							String base_type = aConnect.getString("base_type");
							if(base_type.toLowerCase().indexOf("audio") != -1){
								suffix = "_audio";
							}
							aConnect.getJSONObject("source_param").put("type", "channel");
							aConnect.getJSONObject("source_param").put("layerId", mediaPushPO.getLayerId());
							aConnect.getJSONObject("source_param").put("bundleId", mediaPushPO.getBundleId());
							aConnect.getJSONObject("source_param").put("channelId", mediaPushPO.getChannelId() + suffix);
						}else{
							//新建操作在mediaPushSet中或返回中完成
						}
					}
				}
			}
		}
		//迭代三：将已存在的合屏/混音channel替换到connectBundle
		if(combinedOperation.containsKey("connectBundle")){
			JSONArray connectBundle = combinedOperation.getJSONArray("connectBundle");
			for(int i=0; i<connectBundle.size(); i++){
				JSONObject aConnectBundle = connectBundle.getJSONObject(i);
				if(aConnectBundle.containsKey("channels")){
					//获取connectBundle[].channels[]中的一个channel
					JSONArray connect = aConnectBundle.getJSONArray("channels");
					for(int j=0; j<connect.size(); j++){
						JSONObject aConnect = connect.getJSONObject(j);
						//可能出现没有source_param的情况
						if(aConnect.containsKey("source_param")){
							String srcType = aConnect.getJSONObject("source_param").getString("type");
							if(srcType.equals("combineVideo")){
								//查数据库，是否存在该合屏
								String uuid = aConnect.getJSONObject("source_param").getString("uuid");
								CombineVideoDstPO combineVideoDstPO = (CombineVideoDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineVideoDstDao);
								if(null != combineVideoDstPO){
									//存在该合屏，取出channelID
									aConnect.getJSONObject("source_param").put("type", "channel");
									aConnect.getJSONObject("source_param").put("layerId", combineVideoDstPO.getLayerId());
									aConnect.getJSONObject("source_param").put("bundleId", combineVideoDstPO.getBundleId());
									aConnect.getJSONObject("source_param").put("channelId", combineVideoDstPO.getChannelId());
								}
							}else if(srcType.equals("combineAudio")){
								//查数据库，是否存在该混音
								String uuid = aConnect.getJSONObject("source_param").getString("uuid");
								CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
								if(null != combineAudioDstPO){
									//存在该混音，取出channelID
									aConnect.getJSONObject("source_param").put("type", "channel");
									aConnect.getJSONObject("source_param").put("layerId", combineAudioDstPO.getLayerId());
									aConnect.getJSONObject("source_param").put("bundleId", combineAudioDstPO.getBundleId());
									aConnect.getJSONObject("source_param").put("channelId", combineAudioDstPO.getChannelId());
								}
							}else if(srcType.equals("mediaPush")){
								//查数据库，是否存在该调阅
								String uuid = aConnect.getJSONObject("source_param").getString("uuid");
								MediaPushPO mediaPushPO = (MediaPushPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, mediaPushDao);
								if(null != mediaPushPO){			
									//存在该调阅，取出channelID，根据视/音频类型，给channelID添加后缀
									String suffix = "_video";
									String base_type = aConnect.getString("base_type");
									if(base_type.toLowerCase().indexOf("audio") != -1){
										suffix = "_audio";
									}
									aConnect.getJSONObject("source_param").put("type", "channel");
									aConnect.getJSONObject("source_param").put("layerId", mediaPushPO.getLayerId());
									aConnect.getJSONObject("source_param").put("bundleId", mediaPushPO.getBundleId());
									aConnect.getJSONObject("source_param").put("channelId", mediaPushPO.getChannelId() + suffix);
								}else{
									//新建操作在mediaPushSet中或返回中完成
								}
							}
						}
					}
				}
			}
		}
				
		//将相同目的的删除转发合并到转发中 TODO:迭代三不能去掉，因为只有这里判重，“剩下的删除转发forwardDel全部转换到forwardSet”没有判重
		if(combinedOperation.containsKey("forwardSet") && combinedOperation.containsKey("forwardDel")){
			JSONArray forwardSet = combinedOperation.getJSONArray("forwardSet");
			JSONArray forwardDel = combinedOperation.getJSONArray("forwardDel");
			//delIndex用于记录需要删除的index（不能在遍历途中直接删除）
			List<Integer> delIndex = new LinkedList<Integer>();
			for(int i=0; i<forwardSet.size(); i++){
				JSONObject aForwardSet = forwardSet.getJSONObject(i);
				for(int j=0; j<forwardDel.size(); j++){
					JSONObject aForwardDel = forwardDel.getJSONObject(j);
					String forwardDelDstLayerId = aForwardDel.getJSONObject("dst").getString("layerId");
					String forwardDelDstBundleId = aForwardDel.getJSONObject("dst").getString("bundleId");
					String forwardDelDstChannelId = aForwardDel.getJSONObject("dst").getString("channelId");
					String forwardDstLayerId = aForwardSet.getJSONObject("dst").getString("layerId");
					String forwardDstBundleId = aForwardSet.getJSONObject("dst").getString("bundleId");
					String forwardDstChannelId = aForwardSet.getJSONObject("dst").getString("channelId");
					if(forwardDstBundleId.equals(forwardDelDstBundleId) && forwardDstChannelId.equals(forwardDelDstChannelId)){// && forwardDstLayerId.equals(forwardDelDstLayerId)){
						//判断转发目的与删除相同，进行合并：删除“删除转发”命令
						delIndex.add(j);
						break;
					}
				}
			}
			//根据delIndex删除forwardDel中的元素
			Collections.sort(delIndex);
			for(int j=delIndex.size()-1; j>=0; j--){
				forwardDel.remove(Integer.parseInt(delIndex.get(j).toString()));
			}
		}
		
		//将没有src的转发移到删除转发中 迭代三去掉
//		if(!combinedOperation.containsKey("forwardDel")){
//			combinedOperation.put("forwardDel", new JSONArray());
//		}
//		if(combinedOperation.containsKey("forwardSet")){
//			JSONArray forwardSet = combinedOperation.getJSONArray("forwardSet");
//			//delIndex用于记录需要删除的index（不能在遍历途中直接删除）
//			List<Integer> delIndex = new LinkedList<Integer>();
//			for(int i=0; i<forwardSet.size(); i++){
//				JSONObject aForwardSet = forwardSet.getJSONObject(i);
//				if(!aForwardSet.containsKey("src")){
//					combinedOperation.getJSONArray("forwardDel").add(aForwardSet);
//					delIndex.add(i);
//				}
//			}
//			//根据delIndex删除forwardDel中的元素
//			Collections.sort(delIndex);
//			for(int j=delIndex.size()-1; j>=0; j--){
//				forwardSet.remove(Integer.parseInt(delIndex.get(j).toString()));
//			}
//		}
		
		//将删除转发合并到挂断中
		if(combinedOperation.containsKey("disconnect") && combinedOperation.containsKey("forwardDel")){
			JSONArray disconnect = combinedOperation.getJSONArray("disconnect");
			JSONArray forwardDel = combinedOperation.getJSONArray("forwardDel");
			//delIndex用于记录需要删除的index（不能在遍历途中直接删除）
			List<Integer> delIndex = new LinkedList<Integer>();
			for(int i=0; i<disconnect.size(); i++){
				JSONObject aDisconnect = disconnect.getJSONObject(i);
				for(int j=0; j<forwardDel.size(); j++){
					JSONObject aForwardDel = forwardDel.getJSONObject(j);
					String forwardDstLayerId = aForwardDel.getJSONObject("dst").getString("layerId");
					String forwardDstBundleId = aForwardDel.getJSONObject("dst").getString("bundleId");
					String forwardDstChannelId = aForwardDel.getJSONObject("dst").getString("channelId");
					String connectLayerId = aDisconnect.getString("layerId");
					String connectBundleId = aDisconnect.getString("bundleId");
					String connectChannelId = aDisconnect.getString("channelId");
					if(forwardDstBundleId.equals(connectBundleId) && forwardDstChannelId.equals(connectChannelId)){// && forwardDstLayerId.equals(connectLayerId)){
						//判断转发目的与呼叫对象相同，进行合并：删除挂断命令中的source信息
						if(aDisconnect.containsKey("source_param")){
							aDisconnect.remove("source_param");
						}
						delIndex.add(j);
						break;
					}
				}
			}
			//根据delIndex删除forwardDel中的元素
			Collections.sort(delIndex);
			for(int j=delIndex.size()-1; j>=0; j--){
				forwardDel.remove(Integer.parseInt(delIndex.get(j).toString()));
			}
		}
		
		//迭代三：将删除转发合并到disconnectBundle中
		if(combinedOperation.containsKey("disconnectBundle") && combinedOperation.containsKey("forwardDel")){
			JSONArray disconnectBundle = combinedOperation.getJSONArray("disconnectBundle");
			JSONArray forwardDel = combinedOperation.getJSONArray("forwardDel");
			//delIndex用于记录需要删除的index（不能在遍历途中直接删除）
			List<Integer> delIndex = new LinkedList<Integer>();
			for(int i=0; i<disconnectBundle.size(); i++){
				JSONObject aDisconnectBundle = disconnectBundle.getJSONObject(i);
				for(int j=0; j<forwardDel.size(); j++){
					JSONObject aForwardDel = forwardDel.getJSONObject(j);
					String forwardDstLayerId = aForwardDel.getJSONObject("dst").getString("layerId");
					String forwardDstBundleId = aForwardDel.getJSONObject("dst").getString("bundleId");
//					String forwardDstChannelId = aForwardDel.getJSONObject("dst").getString("channelId");
					String connectLayerId = aDisconnectBundle.getString("layerId");
					String connectBundleId = aDisconnectBundle.getString("bundleId");
//					String connectChannelId = aDisconnectBundle.getString("channelId");
					if(forwardDstBundleId.equals(connectBundleId)){// && forwardDstLayerId.equals(connectLayerId)){
						//判断转发目的与呼叫对象相同，进行合并						
						delIndex.add(j);
						break;
					}
				}				
			}
			//根据delIndex删除forwardDel中的元素
			Collections.sort(delIndex);
			for(int j=delIndex.size()-1; j>=0; j--){
				forwardDel.remove(Integer.parseInt(delIndex.get(j).toString()));
			}
		}
		
		//迭代三：剩下的删除转发 forwardDel [全部转换]到 forwardSet
		if(combinedOperation.containsKey("forwardSet") && combinedOperation.containsKey("forwardDel")){
			JSONArray forwardSet = combinedOperation.getJSONArray("forwardSet");
			JSONArray forwardDel = combinedOperation.getJSONArray("forwardDel");
			for(int j=0; j<forwardDel.size(); j++){
				forwardSet.add(forwardDel.getJSONObject(j));
			}
			combinedOperation.remove("forwardDel");			
		}
		
		//将转发合并到呼叫中 保留，用于依然使用connect的终端
		if(combinedOperation.containsKey("connect") && combinedOperation.containsKey("forwardSet")){
			JSONArray connect = combinedOperation.getJSONArray("connect");
			JSONArray forwardSet = combinedOperation.getJSONArray("forwardSet");
			MeetingEntityUtil.forwardSetOrDelIntoConnect(forwardSet, connect);
		}		
		//将删除转发合并到呼叫中
		if(combinedOperation.containsKey("connect") && combinedOperation.containsKey("forwardDel")){
			JSONArray connect = combinedOperation.getJSONArray("connect");
			JSONArray forwardDel = combinedOperation.getJSONArray("forwardDel");
			MeetingEntityUtil.forwardSetOrDelIntoConnect(forwardDel, connect);
		}
		
		//迭代三 【将转发 forwardSet（可能没有源）全部转换到呼叫 connectBundle 中】
		//除IPC的forwardSet全部转换到connectBundle
		//一个bundle可能出现在多个forwardSet中的screens，display和overlaps[]转换到dect
		if(!combinedOperation.containsKey("connect")){
			combinedOperation.put("connect", new JSONArray());
		}
		if(!combinedOperation.containsKey("connectBundle")){
			combinedOperation.put("connectBundle", new JSONArray());
		}
		if(combinedOperation.containsKey("forwardSet")){
			List<Integer> delIndex = new LinkedList<Integer>();
			JSONArray forwardSet = combinedOperation.getJSONArray("forwardSet");
			JSONArray connectBundle = combinedOperation.getJSONArray("connectBundle");
			for(int i=0; i<forwardSet.size(); i++){
				JSONObject aForwardSet = forwardSet.getJSONObject(i);
				//判断bundle_type，只有VenusTerminal需要转换
				String bundle_type = aForwardSet.getJSONObject("dst").getString("bundle_type");
				if(!bundle_type.equals("VenusTerminal") && !bundle_type.equals("VenusVirtual") && !bundle_type.equals("VenusProxy")){
					continue;
				}
				//设index删除
				delIndex.add(i);
				//标志位，有没有找到bundle
				boolean hasBundle = false;
				String forwardDstLayerId = aForwardSet.getJSONObject("dst").getString("layerId");
				String forwardDstBundleId = aForwardSet.getJSONObject("dst").getString("bundleId");
				String forwardDstChannelId = aForwardSet.getJSONObject("dst").getString("channelId");
				for(int j=0; j<connectBundle.size(); j++){
					JSONObject aConnectBundle = connectBundle.getJSONObject(j);
					//相同则转换，不同则继续
					String connectLayerId = aConnectBundle.getString("layerId");
					String connectBundleId = aConnectBundle.getString("bundleId");
					if(connectBundleId.equals(forwardDstBundleId)){
						hasBundle = true;
						//screens
						if(aForwardSet.containsKey("screens")){
							if(!aConnectBundle.containsKey("screens")){
								aConnectBundle.put("screens", new JSONArray());
							}
							JSONArray forwardscreens = aForwardSet.getJSONArray("screens");
							JSONArray bundlescreens = aConnectBundle.getJSONArray("screens");
							forwardscreensToBundlescreens(forwardscreens, bundlescreens, forwardDstChannelId);
							bundlescreens.clone();
						}						
						//bundle相同，找channel
						JSONArray channels = aConnectBundle.getJSONArray("channels");

						//标志位，有没有找到channel
						boolean hasChannel = false;
						
						for(int k=0; k<channels.size(); k++){
							JSONObject aChannel = channels.getJSONObject(k);
							String channelId = aChannel.getString("channelId");
							if(channelId.equals(forwardDstChannelId)){
								//找到通道
								if(aForwardSet.containsKey("src")){
									aChannel.put("source_param", aForwardSet.getJSONObject("src"));
								}else{
									//转发中没有源，所以去掉呼叫中的源
									aChannel.remove("source_param");
								}								
								hasChannel = true;
								break;
							}
						}
						if(!hasChannel){
							//在该bundle中建立一个通道
							JSONObject newChannel = forwardToChannel(aForwardSet);							
							channels.add(newChannel);
						}
						break;
					}//if(同一个bundle)
				}//for(bundle)
				if(!hasBundle){
					//没有转换则新建一个bundle
					JSONObject newBundle = new JSONObject();
					newBundle.put("taskId", aForwardSet.getString("taskId"));
					newBundle.put("lock_type", "write");
					newBundle.put("layerId", aForwardSet.getJSONObject("dst").getString("layerId"));
					newBundle.put("bundleId", aForwardSet.getJSONObject("dst").getString("bundleId"));
					newBundle.put("bundle_type", aForwardSet.getJSONObject("dst").getString("bundle_type"));
					JSONArray channels = new JSONArray();
					newBundle.put("channels", channels);
					
					//在该bundle中建立一个通道
					JSONObject newChannel = forwardToChannel(aForwardSet);
					channels.add(newChannel);
					
					//取出screens
					if(aForwardSet.containsKey("screens")){
						newBundle.put("screens", new JSONArray());
						JSONArray forwardscreens = aForwardSet.getJSONArray("screens");
						JSONArray bundlescreens = newBundle.getJSONArray("screens");
						forwardscreensToBundlescreens(forwardscreens, bundlescreens, forwardDstChannelId);
					}
					connectBundle.add(newBundle);
				}				
			}//for(forwardSet)
			//根据delIndex删除forwardDel中的元素
			Collections.sort(delIndex);
			for(int j=delIndex.size()-1; j>=0; j--){
				forwardSet.remove(Integer.parseInt(delIndex.get(j).toString()));
			}
		}
		
		//处理业务的合屏Set和Update。set和update可能都有新建和修改，逻辑层把新建都放到set中，修改放到update中，再传给下一层
		JSONArray newCombineVideoSet = new JSONArray();
		JSONArray newCombineVideoUpdate = new JSONArray();
		if(combinedOperation.containsKey("combineVideoSet")){
			JSONArray combineVideoSet = combinedOperation.getJSONArray("combineVideoSet");
			for(int i=0; i<combineVideoSet.size(); i++){
				JSONObject aCombineVideoSet = combineVideoSet.getJSONObject(i);
				setOrUpdateCombineVideo(aCombineVideoSet, newCombineVideoSet, newCombineVideoUpdate);
			}
		}
		if(combinedOperation.containsKey("combineVideoUpdate")){
			JSONArray combineVideoUpdate = combinedOperation.getJSONArray("combineVideoUpdate");
			for(int i=0; i<combineVideoUpdate.size(); i++){
				JSONObject aCombineVideoUpdate = combineVideoUpdate.getJSONObject(i);
				setOrUpdateCombineVideo(aCombineVideoUpdate, newCombineVideoSet, newCombineVideoUpdate);
			}
		}
		combinedOperation.put("combineVideoSet", newCombineVideoSet);
		combinedOperation.put("combineVideoUpdate", newCombineVideoUpdate);		
		
		//合屏中的轮询立即切换到下一画面
		if(!combinedOperation.containsKey("pass_by")){
			combinedOperation.put("pass_by", new JSONArray());
		}
		if(combinedOperation.containsKey("combineVideoOperation")){
			JSONArray combineVideoOperation = combinedOperation.getJSONArray("combineVideoOperation");
			for(int i=0; i<combineVideoOperation.size(); i++){
				JSONObject aCombineVideoOperation = combineVideoOperation.getJSONObject(i);
				combineVideoOperation(aCombineVideoOperation, combinedOperation.getJSONArray("pass_by"));
			}
		}
		
		
		//处理业务的混音Set和Update。set和update可能都有新建和修改，逻辑层把新建都放到set中，修改放到update中，再传给下一层
		JSONArray newCombineAudioSet = new JSONArray();
		JSONArray newCombineAudioUpdate = new JSONArray();
		if(combinedOperation.containsKey("combineAudioSet")){
			JSONArray combineAudioSet = combinedOperation.getJSONArray("combineAudioSet");
			for(int i=0; i<combineAudioSet.size(); i++){
				JSONObject aCombineAudioSet = combineAudioSet.getJSONObject(i);
				setOrUpdateCombineAudio(aCombineAudioSet, newCombineAudioSet, newCombineAudioUpdate);
			}
		}
		if(combinedOperation.containsKey("combineAudioUpdate")){
			JSONArray combineAudioUpdate = combinedOperation.getJSONArray("combineAudioUpdate");
			for(int i=0; i<combineAudioUpdate.size(); i++){
				JSONObject aCombineAudioUpdate = combineAudioUpdate.getJSONObject(i);
				setOrUpdateCombineAudio(aCombineAudioUpdate, newCombineAudioSet, newCombineAudioUpdate);
			}
		}
		combinedOperation.put("combineAudioSet", newCombineAudioSet);
		combinedOperation.put("combineAudioUpdate", newCombineAudioUpdate);		
		
		//处理业务的调阅Set和Update。set和update可能都有新建和修改，逻辑层把新建都放到set中，修改放到update中，再传给下一层
		JSONArray newMediPushSet = new JSONArray();
		JSONArray newMediPushUpdate = new JSONArray();
		if(combinedOperation.containsKey("mediaPushSet")){
			JSONArray mediaPushSet = combinedOperation.getJSONArray("mediaPushSet");
			for(int i=0; i<mediaPushSet.size(); i++){
				JSONObject aMediaPushSet = mediaPushSet.getJSONObject(i);
				setOrUpdateMediaPush(aMediaPushSet, newMediPushSet, newMediPushUpdate);
			}
		}
		if(combinedOperation.containsKey("mediaPushUpdate")){
			JSONArray mediaPushUpdate = combinedOperation.getJSONArray("mediaPushUpdate");
			for(int i=0; i<mediaPushUpdate.size(); i++){
				JSONObject aMediaPushUpdate = mediaPushUpdate.getJSONObject(i);
				setOrUpdateMediaPush(aMediaPushUpdate, newMediPushSet, newMediPushUpdate);
			}
		}
		combinedOperation.put("mediaPushSet", newMediPushSet);
		combinedOperation.put("mediaPushUpdate", newMediPushUpdate);
		
		//处理删除合屏
		JSONArray combineVideoDelEncode = new JSONArray();
		if(orgOperation.containsKey("combineVideoDel")){
			List<Integer> delIndex = new LinkedList<Integer>();
			JSONArray combineVideoDel = orgOperation.getJSONArray("combineVideoDel");
			for(int i=0; i<combineVideoDel.size(); i++){
				JSONObject aCombineVideoDel = combineVideoDel.getJSONObject(i);
				String uuid = aCombineVideoDel.getString("uuid");
				
				//如果在set或update中有，则删除这个del
				boolean hasCombineVideoSet = MeetingEntityUtil.hasCombineVideoOrAudio(combinedOperation.getJSONArray("combineVideoSet"), uuid);
				boolean hasCombineVideoUpdate = MeetingEntityUtil.hasCombineVideoOrAudio(combinedOperation.getJSONArray("combineVideoUpdate"), uuid);
				if(hasCombineVideoSet || hasCombineVideoUpdate){
					delIndex.add(i);
					break;
				}
				
				//编码通道
				CombineVideoDstPO combineVideoDstPO = (CombineVideoDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineVideoDstDao);
				if(combineVideoDstPO != null){
					JSONObject aEncode = new JSONObject();
					aEncode.put("taskId", aCombineVideoDel.getString("taskId"));
					aEncode.put("layerId", combineVideoDstPO.getLayerId());
					aEncode.put("bundleId", combineVideoDstPO.getBundleId());
					aEncode.put("channelId", combineVideoDstPO.getChannelId());
					aEncode.put("uuid", combineVideoDstPO.getUuid());
					combineVideoDelEncode.add(aEncode);
				}else{
					//没找到已有合屏，报错，不处理
					log.error("删除合屏时没找到已有合屏（逻辑层未建立），uuid = " + uuid);
				}
			}
			//根据delIndex删除combineVideoDel中的元素
			Collections.sort(delIndex);
			for(int j=delIndex.size()-1; j>=0; j--){
				combineVideoDel.remove(Integer.parseInt(delIndex.get(j).toString()));
			}
		}
		JSONObject combineVideoDel4excute = new JSONObject();
		combineVideoDel4excute.put("encode", combineVideoDelEncode);
		combinedOperation.remove("combineVideoDel");
		combinedOperation.put("combineVideoDel", combineVideoDel4excute);
		
		//处理删除混音
		JSONArray combineAudioDelEncode = new JSONArray();	
		if(orgOperation.containsKey("combineAudioDel")){
			List<Integer> delIndex = new LinkedList<Integer>();
			JSONArray combineAudioDel = orgOperation.getJSONArray("combineAudioDel");
			for(int i=0; i<combineAudioDel.size(); i++){
				JSONObject aCombineAudioDel = combineAudioDel.getJSONObject(i);
				String uuid = aCombineAudioDel.getString("uuid");
				
				//如果在set或update中有，则删除这个del
				boolean hasCombineAudioSet = MeetingEntityUtil.hasCombineVideoOrAudio(combinedOperation.getJSONArray("combineAudioSet"), uuid);
				boolean hasCombineAudioUpdate = MeetingEntityUtil.hasCombineVideoOrAudio(combinedOperation.getJSONArray("combineAudioUpdate"), uuid);
				if(hasCombineAudioSet || hasCombineAudioUpdate){
					delIndex.add(i);
					break;
				}
				
				//编码通道
				CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
				if(combineAudioDstPO != null){
					JSONObject aEncode = new JSONObject();
					aEncode.put("taskId", aCombineAudioDel.getString("taskId"));
					aEncode.put("layerId", combineAudioDstPO.getLayerId());
					aEncode.put("bundleId", combineAudioDstPO.getBundleId());
					aEncode.put("channelId", combineAudioDstPO.getChannelId());
					aEncode.put("uuid", combineAudioDstPO.getUuid());
					combineAudioDelEncode.add(aEncode);
				}else{
					//没找到已有混音，报错，不处理
					log.error("删除混音时没找到已有混音（逻辑层未建立），uuid = " + uuid);
				}
			}
			//根据delIndex删除combineVideoDel中的元素
			Collections.sort(delIndex);
			for(int j=delIndex.size()-1; j>=0; j--){
				combineAudioDel.remove(Integer.parseInt(delIndex.get(j).toString()));
			}
		}
		JSONObject combineAudioDel4excute = new JSONObject();
		combineAudioDel4excute.put("encode", combineAudioDelEncode);
		combinedOperation.remove("combineAudioDel");
		combinedOperation.put("combineAudioDel", combineAudioDel4excute);
		
		//处理删除调阅
//		JSONArray mediaPushDel = new JSONArray();
		if(orgOperation.containsKey("mediaPushDel")){
			List<Integer> delIndex = new LinkedList<Integer>();
			JSONArray mediaPushDel = orgOperation.getJSONArray("mediaPushDel");
			for(int i=0; i<mediaPushDel.size(); i++){				
				JSONObject aMediaPushDel = mediaPushDel.getJSONObject(i);
				String uuid = aMediaPushDel.getString("uuid");
				MediaPushPO mediaPushPO = (MediaPushPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, mediaPushDao);
				if(mediaPushPO != null){
					aMediaPushDel.put("layerId", mediaPushPO.getLayerId());
					aMediaPushDel.put("bundleId", mediaPushPO.getBundleId());
					aMediaPushDel.put("channelId", mediaPushPO.getChannelId());					
				}else{
					//没找到已有调阅，报错，不处理
					log.error("删除调阅时没找到已有调阅（逻辑层未建立），uuid = " + uuid);
				}
			}
		}
		
		//迭代三 发布[rtmp]
		if(!combinedOperation.containsKey("OutConnMediaMuxSet")){
			combinedOperation.put("OutConnMediaMuxSet", new JSONArray());
		}
		if(combinedOperation.containsKey("publishStreamSet")){
			JSONArray publishStreamSet = combinedOperation.getJSONArray("publishStreamSet");
			for(int i=0; i<publishStreamSet.size(); i++){
				JSONObject aPublishStreamSet = publishStreamSet.getJSONObject(i);
				JSONObject aOutConnMediaMuxSet = publishStreamSet(aPublishStreamSet);
				combinedOperation.getJSONArray("OutConnMediaMuxSet").add(aOutConnMediaMuxSet);
			}
		}
		
		//大屏的视频		
		if(combinedOperation.containsKey("jv230ForwardSet")){
			JSONArray jv230ForwardSet = combinedOperation.getJSONArray("jv230ForwardSet");
			for(int i=0; i<jv230ForwardSet.size(); i++){
				JSONObject aJv230ForwardSet = jv230ForwardSet.getJSONObject(i);
				JSONObject base_param = aJv230ForwardSet.getJSONObject("channel_param").getJSONObject("base_param");
				if(base_param.containsKey("sources") && base_param.getJSONArray("sources")!=null){
					JSONArray sources = base_param.getJSONArray("sources");
					for(int j=0; j<sources.size(); j++){
						JSONObject source = sources.getJSONObject(j);
						if("combineVideo".equals(source.getString("type"))){
							String uuid = source.getString("uuid");
							CombineVideoDstPO combineVideoDstPO = (CombineVideoDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineVideoDstDao);
							if(null != combineVideoDstPO){
								//存在该混音，取出channelID
								source.put("type", "channel");
								source.put("layer_id", combineVideoDstPO.getLayerId());
								source.put("bundle_id", combineVideoDstPO.getBundleId());
								source.put("channel_id", combineVideoDstPO.getChannelId());
							}
						}
					}
				}				
			}
		}
		
		
		if(combinedOperation.containsKey("jv230AudioSet")){
			JSONArray jv230AudioSet = combinedOperation.getJSONArray("jv230AudioSet");
			for(int i=0; i<jv230AudioSet.size(); i++){
				JSONObject aJv230AudioSet = jv230AudioSet.getJSONObject(i);
				JSONObject base_param = aJv230AudioSet.getJSONObject("channel_param").getJSONObject("base_param");
				if(base_param.containsKey("source") && base_param.getJSONObject("source")!=null && base_param.getJSONObject("source").containsKey("type")){	
					JSONObject source = base_param.getJSONObject("source");
					if(source.getString("type").equals("combineAudio")){
						String uuid = source.getString("uuid");
						CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
						if(null != combineAudioDstPO){
							//存在该混音，取出channelID
							source.put("type", "channel");
							source.put("layer_id", combineAudioDstPO.getLayerId());
							source.put("bundle_id", combineAudioDstPO.getBundleId());
							source.put("channel_id", combineAudioDstPO.getChannelId());
						}else{
							log.error("大屏接收混音时没找到已有混音（逻辑层未建立），uuid = " + uuid);
						}
					}
				}				
			}
		}
		
		//添加或修改录制
		JSONArray OutConnMediaMuxSet = new JSONArray();
		JSONArray OutConnMediaMuxUpdate = new JSONArray();
		if(combinedOperation.containsKey("recordSet")){
			JSONArray recordSet = combinedOperation.getJSONArray("recordSet");
			for(int i=0; i<recordSet.size(); i++){
				JSONObject aRecordSet = recordSet.getJSONObject(i);
				setOrUpdateRecord(aRecordSet, OutConnMediaMuxSet, OutConnMediaMuxUpdate);
			}
		}
		
		//修改录制 后续考虑怎么暂停
		if(combinedOperation.containsKey("recordUpdate")){
			JSONArray recordUpdate = combinedOperation.getJSONArray("recordUpdate");
			for(int i=0; i<recordUpdate.size(); i++){
				JSONObject aRecordUpdate = recordUpdate.getJSONObject(i);
				setOrUpdateRecord(aRecordUpdate, OutConnMediaMuxSet, OutConnMediaMuxUpdate);
			}
		}
		
		//为了结合下面注掉的修改
		if(!combinedOperation.containsKey("OutConnMediaMuxSet")){
			combinedOperation.put("OutConnMediaMuxSet", new JSONArray());
		}
		if(!combinedOperation.containsKey("OutConnMediaMuxUpdate")){
			combinedOperation.put("OutConnMediaMuxUpdate", new JSONArray());
		}
		for(int i=0; i<OutConnMediaMuxSet.size(); i++){
			combinedOperation.getJSONArray("OutConnMediaMuxSet").add(OutConnMediaMuxSet.get(i));
		}
		for(int i=0; i<OutConnMediaMuxUpdate.size(); i++){
			combinedOperation.getJSONArray("OutConnMediaMuxUpdate").add(OutConnMediaMuxUpdate.get(i));
		}
//		combinedOperation.put("OutConnMediaMuxSet", OutConnMediaMuxSet);
//		combinedOperation.put("OutConnMediaMuxUpdate", OutConnMediaMuxUpdate);
			
		//删除录制
		JSONArray OutConnMediaMuxDel = new JSONArray();
		if(combinedOperation.containsKey("recordDel")){
			JSONArray recordDel = combinedOperation.getJSONArray("recordDel");
			for(int i=0; i<recordDel.size(); i++){
				JSONObject aRecordDel = recordDel.getJSONObject(i);
				String uuid = aRecordDel.getString("uuid");
				//查找该uuid的录制推流
				OutConnMediaMuxPO outConnMediaMuxPO = (OutConnMediaMuxPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, outConnMediaMuxDao);
				if(null != outConnMediaMuxPO){
					JSONObject aOutConnMediaMuxDel = new JSONObject();
					if(aRecordDel.containsKey("taskId")){
						aOutConnMediaMuxDel.put("taskId", aRecordDel.getString("taskId"));					
					}
					aOutConnMediaMuxDel.put("uuid", uuid);
					aOutConnMediaMuxDel.put("layerId", outConnMediaMuxPO.getLayerId());
					aOutConnMediaMuxDel.put("bundleId", outConnMediaMuxPO.getBundleId());
					aOutConnMediaMuxDel.put("channelId", outConnMediaMuxPO.getChannelId());
					OutConnMediaMuxDel.add(aOutConnMediaMuxDel);
					
					//向aRecordDel中加入数据
					aRecordDel.put("url", outConnMediaMuxPO.getUrl());
					aRecordDel.put("format", outConnMediaMuxPO.getFormat());
				}else{
					//没找到已有录制的推流
					log.error("删除录制时没找到已有录制OutConnMediaMuxPO（逻辑层未建立），uuid = " + uuid);
				}
				//迭代三 删除发布[rtmp]
				List<OutConnMediaMuxPO> rtmpPOs = outConnMediaMuxDao.getByRecordUuid(uuid);
				for(OutConnMediaMuxPO rtmpPO : rtmpPOs){
					JSONObject rtmpDel = new JSONObject();
					if(aRecordDel.containsKey("taskId")){
						rtmpDel.put("taskId", aRecordDel.getString("taskId"));					
					}
					rtmpDel.put("uuid", rtmpPO.getUuid());//附：uuid字段并没有实际使用
					rtmpDel.put("layerId", rtmpPO.getLayerId());
					rtmpDel.put("bundleId", rtmpPO.getBundleId());
					rtmpDel.put("channelId", rtmpPO.getChannelId());					
					OutConnMediaMuxDel.add(rtmpDel);
				}		
				//OMC删数据库的操作在omc的对接中完成
			}
		}
		combinedOperation.put("OutConnMediaMuxDel", OutConnMediaMuxDel);
		
		return combinedOperation;
	}
	
	//将录制任务拆分出来，同时从原任务中删除
	@Deprecated
	public JSONObject splitOperation(JSONObject orgOperation){
		JSONObject splitedOperation = new JSONObject();
		if(orgOperation.containsKey("userId")){
			splitedOperation.put("userId", orgOperation.getString("userId"));
		}
		if(orgOperation.containsKey("systemMode")){
			splitedOperation.put("systemMode", orgOperation.getString("systemMode"));
		}
		if(orgOperation.containsKey("recordSet")){
			splitedOperation.put("recordSet", orgOperation.getJSONArray("recordSet"));
			orgOperation.remove("recordSet");
		}
		if(orgOperation.containsKey("recordUpdate")){
			splitedOperation.put("recordUpdate", orgOperation.getJSONArray("recordUpdate"));
			orgOperation.remove("recordUpdate");
		}
		if(orgOperation.containsKey("recordDel")){
			splitedOperation.put("recordDel", orgOperation.getJSONArray("recordDel"));
			orgOperation.remove("recordDel");
		}
		return splitedOperation;
	}
	
	//处理大屏命令，将其中的混音转换成通道
	@Deprecated
	public JSONObject bigscreenOperationCombine(JSONObject orgOperation){
		poMap.clear();
		JSONObject combinedOperation = (JSONObject) orgOperation.clone();
		
		if(combinedOperation.containsKey("jv230AudioSet")){
			JSONArray jv230AudioSet = combinedOperation.getJSONArray("jv230AudioSet");
			for(int i=0; i<jv230AudioSet.size(); i++){
				JSONObject aJv230AudioSet = jv230AudioSet.getJSONObject(i);
				JSONObject base_param = aJv230AudioSet.getJSONObject("channel_param").getJSONObject("base_param");
				if(base_param.containsKey("source") && base_param.getJSONObject("source")!=null && base_param.getJSONObject("source").containsKey("type")){	
					JSONObject source = base_param.getJSONObject("source");
					if(source.getString("type").equals("combineAudio")){
						String uuid = source.getString("uuid");
						CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
						if(null != combineAudioDstPO){
							//存在该混音，取出channelID
							source.put("type", "channel");
							source.put("layer_id", combineAudioDstPO.getLayerId());
							source.put("bundle_id", combineAudioDstPO.getBundleId());
							source.put("channel_id", combineAudioDstPO.getChannelId());
						}else{
							log.error("大屏接收混音时没找到已有混音（逻辑层未建立），uuid = " + uuid);
						}
					}
				}				
			}
		}
			
		return combinedOperation;
	}
	
	/*
	 * 通道执行后调用。对于删除的任务，删数据库；对于新增的任务，存channelId等信息
	 */
	public void executeSuccess(JSONObject jsonObj){
		//该方法执行前，需要根据executeResult判断是否成功
		poMap.clear();
		JSONObject executeResult = jsonObj.getJSONObject("executeResult");
		JSONObject combinedOperation = jsonObj.getJSONObject("combinedOperation");
		
		//删除旧的
		if(combinedOperation.containsKey("combineAudioDel")){
			JSONObject combineAudioDel = combinedOperation.getJSONObject("combineAudioDel");			
			//src取消，仅处理encode
			if(combineAudioDel.containsKey("encode")){
				JSONArray combineAudioDelEncode = combineAudioDel.getJSONArray("encode");
				List<CombineAudioDstPO> combineAudioDsts = new ArrayList<CombineAudioDstPO>();
				for(int i=0; i<combineAudioDelEncode.size(); i++){
					JSONObject aEncode = combineAudioDelEncode.getJSONObject(i);
					String uuid = aEncode.getString("uuid");
					//LDY 修改uuid查询   根据uuid删除数据库中的dst
					//List<CombineAudioDstPO> combineAudioDstPOs = combineAudioDstDao.findByUuid(uuid);
					//combineAudioDstDao.deleteInBatch(combineAudioDstPOs);		
					
					CombineAudioDstPO combineAudioDst = combineAudioDstDao.findByUuid(uuid);
					if(combineAudioDst != null)combineAudioDsts.add(combineAudioDst);
				}
				combineAudioDstDao.deleteInBatch(combineAudioDsts);
			}			
		}
		if(combinedOperation.containsKey("combineVideoDel")){
			JSONObject combineVideoDel = combinedOperation.getJSONObject("combineVideoDel");
			//src取消，仅处理encode
			if(combineVideoDel.containsKey("encode")){
				JSONArray combineVideoDelEncode = combineVideoDel.getJSONArray("encode");
				List<CombineVideoDstPO> combineVideoDsts = new ArrayList<CombineVideoDstPO>();
				for(int i=0; i<combineVideoDelEncode.size(); i++){
					JSONObject aEncode = combineVideoDelEncode.getJSONObject(i);
					String uuid = aEncode.getString("uuid");
					//LDY 修改uuid查询  根据uuid删除数据库中的dst //deleteByUuid(uuid);					
					//List<CombineVideoDstPO> combineVideoDstPOs = combineVideoDstDao.findByUuid(uuid);
					//combineVideoDstDao.deleteInBatch(combineVideoDstPOs);
					
					CombineVideoDstPO combineVideoDst = combineVideoDstDao.findByUuid(uuid);
					if(combineVideoDst != null) combineVideoDsts.add(combineVideoDst);
				}
				combineVideoDstDao.deleteInBatch(combineVideoDsts);
			}
		}
		if(combinedOperation.containsKey("OutConnMediaMuxDel")){
			JSONArray OutConnMediaMuxDel = combinedOperation.getJSONArray("OutConnMediaMuxDel");
			List<OutConnMediaMuxPO> outConnMediaMuxs = new ArrayList<OutConnMediaMuxPO>();
			for(int i=0; i<OutConnMediaMuxDel.size(); i++){
				JSONObject aOutConnMediaMuxDel = OutConnMediaMuxDel.getJSONObject(i);
				String uuid = aOutConnMediaMuxDel.getString("uuid");
				//LDY 修改uuid查询  根据uuid删除数据库中的OutConnMediaMuxDel					
				//List<OutConnMediaMuxPO> outConnMediaMuxPOs = outConnMediaMuxDao.findByUuid(uuid);
				//outConnMediaMuxDao.deleteInBatch(outConnMediaMuxPOs);
				
				OutConnMediaMuxPO outConnMediaMux = outConnMediaMuxDao.findByUuid(uuid);
				if(outConnMediaMux != null) outConnMediaMuxs.add(outConnMediaMux);
			}
			outConnMediaMuxDao.deleteInBatch(outConnMediaMuxs);
		}
		if(combinedOperation.containsKey("mediaPushDel")){
			JSONArray mediaPushDel = combinedOperation.getJSONArray("mediaPushDel");
			List<MediaPushPO> mediaPushPOs = new ArrayList<MediaPushPO>();
			for(int i=0; i<mediaPushDel.size(); i++){
				JSONObject aMediaPushDel = mediaPushDel.getJSONObject(i);
				String uuid = aMediaPushDel.getString("uuid");				
				MediaPushPO mediaPushPO = mediaPushDao.findByUuid(uuid);
				if(mediaPushPO != null) mediaPushPOs.add(mediaPushPO);
			}
			mediaPushDao.deleteInBatch(mediaPushPOs);
		}
		
		//创建、存储新的
		if(executeResult.containsKey("combineVideoLayout")){
			JSONArray combineVideoLayout = executeResult.getJSONArray("combineVideoLayout");
			List<CombineVideoDstPO> pos = new ArrayList<CombineVideoDstPO>();
			for(int i=0; i<combineVideoLayout.size(); i++){
				JSONObject aCombineVideoLayout = combineVideoLayout.getJSONObject(i);
				String uuid = aCombineVideoLayout.getString("uuid");
				
				//LDY 修改uuid查询 添加前保证没有已存在的
				//List<CombineVideoDstPO> combineVideoDstPOs = combineVideoDstDao.findByUuid(uuid);
				//combineVideoDstDao.deleteInBatch(combineVideoDstPOs);
				
				CombineVideoDstPO combineVideoDst = combineVideoDstDao.findByUuid(uuid);
				if(combineVideoDst != null) combineVideoDstDao.delete(combineVideoDst);
				
				CombineVideoDstPO po = new CombineVideoDstPO();
				po.setUuid(uuid);
				po.setLayerId(aCombineVideoLayout.getString("layerId"));
				po.setBundleId(aCombineVideoLayout.getString("bundleId"));
				po.setChannelId(aCombineVideoLayout.getString("channelId"));
				pos.add(po);
			}
			combineVideoDstDao.save(pos);
		}
		if(executeResult.containsKey("combineAudioSet")){
			JSONArray combineAudioSet = executeResult.getJSONArray("combineAudioSet");
			List<CombineAudioDstPO> pos = new ArrayList<CombineAudioDstPO>();
			for(int i=0; i<combineAudioSet.size(); i++){
				JSONObject aCombineAudioSet = combineAudioSet.getJSONObject(i);
				String uuid = aCombineAudioSet.getString("uuid");
				
				//LDY 修改uuid查询 添加前保证没有已存在的
				//List<CombineAudioDstPO> combineAudioDstPOs = combineAudioDstDao.findByUuid(uuid);
				//combineAudioDstDao.deleteInBatch(combineAudioDstPOs);
				
				CombineAudioDstPO combineAudioDst = combineAudioDstDao.findByUuid(uuid);
				if(combineAudioDst != null)combineAudioDstDao.delete(combineAudioDst);
				
				CombineAudioDstPO po = new CombineAudioDstPO();
				po.setUuid(uuid);
				po.setLayerId(aCombineAudioSet.getString("layerId"));
				po.setBundleId(aCombineAudioSet.getString("bundleId"));
				po.setChannelId(aCombineAudioSet.getString("channelId"));
				pos.add(po);
			}
			combineAudioDstDao.save(pos);
		}
		if(executeResult.containsKey("mediaPushSet")){
			JSONArray mediaPushSet = executeResult.getJSONArray("mediaPushSet");
			List<MediaPushPO> pos = new ArrayList<MediaPushPO>();
			for(int i=0; i<mediaPushSet.size(); i++){
				JSONObject aMediaPushSet = mediaPushSet.getJSONObject(i);
				String uuid = aMediaPushSet.getString("uuid");
				
				MediaPushPO mediaPushPO = mediaPushDao.findByUuid(uuid);
				if(mediaPushPO != null)mediaPushDao.delete(mediaPushPO);
				
				MediaPushPO po = new MediaPushPO();
				po.setUuid(uuid);
				po.setLayerId(aMediaPushSet.getString("layerId"));
				po.setBundleId(aMediaPushSet.getString("bundleId"));
				po.setChannelId(aMediaPushSet.getString("channelId"));
//				po.setFileSource(aMediaPushSet.getJSONObject("src").getString("file_source"));
				pos.add(po);
			}
			mediaPushDao.save(pos);
		}
		if(executeResult.containsKey("OutConnMediaMuxSet")){
			JSONArray OutConnMediaMuxSet = executeResult.getJSONArray("OutConnMediaMuxSet");
			List<OutConnMediaMuxPO> pos = new ArrayList<OutConnMediaMuxPO>();
			for(int i=0; i<OutConnMediaMuxSet.size(); i++){
				JSONObject aOutConnMediaMuxSet = OutConnMediaMuxSet.getJSONObject(i);
				String uuid = aOutConnMediaMuxSet.getString("uuid");
				
				//LDY 修改uuid查询 添加前保证没有已存在的
				//List<OutConnMediaMuxPO> OutConnMediaMuxPOs = outConnMediaMuxDao.findByUuid(uuid);
				//outConnMediaMuxDao.deleteInBatch(OutConnMediaMuxPOs);
				OutConnMediaMuxPO outConnMediaMuxPO = outConnMediaMuxDao.findByUuid(uuid);
				if(outConnMediaMuxPO != null) outConnMediaMuxDao.delete(outConnMediaMuxPO);
				
				OutConnMediaMuxPO po = new OutConnMediaMuxPO();
				po.setUuid(uuid);
				po.setLayerId(aOutConnMediaMuxSet.getString("layerId"));
				po.setBundleId(aOutConnMediaMuxSet.getString("bundleId"));
				po.setChannelId(aOutConnMediaMuxSet.getString("channelId"));
				//从combinedOperation.OutConnMediaMuxSet中找到这个录制的url和format
				if(combinedOperation.containsKey("OutConnMediaMuxSet")){
					JSONArray OutConnMediaMuxSet1 = combinedOperation.getJSONArray("OutConnMediaMuxSet");
					boolean hasUuid = false;
					for(int i1=0; i1<OutConnMediaMuxSet1.size(); i1++){
						JSONObject aOutConnMediaMuxSet1 = OutConnMediaMuxSet1.getJSONObject(i1);
						String outUuid = aOutConnMediaMuxSet1.getString("uuid");
						if(outUuid.equals(uuid)){
							String url = aOutConnMediaMuxSet1.getJSONObject("channel_param").getJSONObject("base_param").getString("url");
							String format = aOutConnMediaMuxSet1.getJSONObject("channel_param").getJSONObject("base_param").getString("format");
							po.setUrl(url);
							po.setFormat(format);
							if(aOutConnMediaMuxSet1.containsKey("recordUuid")){
								po.setRecordUuid(aOutConnMediaMuxSet1.getString("recordUuid"));
							}
							hasUuid = true;
							break;
						}
					}
					if(!hasUuid){
						//返回的数据的uuid在combinedOperation.OutConnMediaMuxSet中找不到
						System.out.println("录制发流数据有误，uuid = " + uuid);						
					}
				}
				pos.add(po);
			}
			outConnMediaMuxDao.save(pos);
		}
		return;
	}
	
	//合屏的set和update统一处理，参数aCombineVideoSet可能是新建也可能是修改，需要根据uuid查询，查不到就是新建，查到就是修改
	private void setOrUpdateCombineVideo(JSONObject aCombineVideoSet, JSONArray newCombineVideoSet, JSONArray newCombineVideoUpdate){
		//迭代三 支持用合屏做源
		JSONArray position = aCombineVideoSet.getJSONArray("position");
		for(int i=0; i<position.size(); i++){
			JSONObject aPosition = position.getJSONObject(i);
			JSONArray src = aPosition.getJSONArray("src");
			for(int j=0; j<src.size(); j++){
				JSONObject aSrc = src.getJSONObject(j);
				if(!aSrc.containsKey("type")){
					aSrc.put("type", "channel");
				}else{
					IdBO idBO = getChannelidFromUuid(aSrc);
					if(null!=idBO){
						aSrc.put("layerId", idBO.getLayerId());
						aSrc.put("bundleId", idBO.getBundleId());
						aSrc.put("channelId", idBO.getChannelId());
						aSrc.put("type", "channel");
					}
				}
			}
		}
		
		String uuid = aCombineVideoSet.getString("uuid");
		//查找该uuid的合屏
		CombineVideoDstPO combineVideoDstPO = (CombineVideoDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineVideoDstDao);
		if(null != combineVideoDstPO){
			//修改合屏
			aCombineVideoSet.put("layerId", combineVideoDstPO.getLayerId());
			aCombineVideoSet.put("bundleId", combineVideoDstPO.getBundleId());
			aCombineVideoSet.put("channelId", combineVideoDstPO.getChannelId());
			newCombineVideoUpdate.add(aCombineVideoSet);
		}else{
			//新建合屏
			newCombineVideoSet.add(aCombineVideoSet);
		}
	}
	
	private void combineVideoOperation(JSONObject aCombineVideoOperation, JSONArray pass_by) throws Exception{
		JSONArray position = aCombineVideoOperation.getJSONArray("position");
		for(int i=0; i<position.size(); i++){
			JSONObject aPosition = position.getJSONObject(i);
			if(aPosition.containsKey("operation") && aPosition.getString("operation").equals("next_mixer_video_loop_index")){
				//切换至下一画面
				JSONObject aPass_by = new JSONObject();
				JSONObject pass_by_content = new JSONObject();
				JSONObject screen = new JSONObject();
				
				CombineVideoDstPO combineVideoDstPO = combineVideoDstDao.getByUuid(aCombineVideoOperation.getString("uuid"));
				if(null != combineVideoDstPO){
					aPass_by.put("type", "next_mixer_video_loop_index");
					aPass_by.put("layer_id", combineVideoDstPO.getLayerId());
					aPass_by.put("bundle_id", combineVideoDstPO.getBundleId());
					pass_by_content.put("channel_id", combineVideoDstPO.getChannelId());
					screen.put("x", aPosition.getInteger("x"));
					screen.put("y", aPosition.getInteger("y"));
					screen.put("width", aPosition.getInteger("w"));
					screen.put("height", aPosition.getInteger("h"));
					pass_by_content.put("screen", screen);
					aPass_by.put("pass_by_content", pass_by_content);
					pass_by.add(aPass_by);
				}else{
					//没找到，抛错
					throw new ConfigHasNotSavedOrStartedException();
				}
			}
		}
	}
	
	//混音的set和update统一处理，参数aCombineAudioSet可能是新建也可能是修改，需要根据uuid查询，查不到就是新建，查到就是修改
	private void setOrUpdateCombineAudio(JSONObject aCombineAudioSet, JSONArray newCombineAudioSet, JSONArray newCombineAudioUpdate){
		String uuid = aCombineAudioSet.getString("uuid");
		//查找该uuid的混音
		CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
		if(null != combineAudioDstPO){
			//修改混音
			aCombineAudioSet.put("layerId", combineAudioDstPO.getLayerId());
			aCombineAudioSet.put("bundleId", combineAudioDstPO.getBundleId());
			aCombineAudioSet.put("channelId", combineAudioDstPO.getChannelId());
			newCombineAudioUpdate.add(aCombineAudioSet);
		}else{
			//新建混音
			newCombineAudioSet.add(aCombineAudioSet);
		}
	}
	
	//调阅的set和update统一处理，参数aMediaPushSet可能是新建也可能是修改，需要根据uuid查询，查不到就是新建，查到就是修改
	private void setOrUpdateMediaPush(JSONObject aMediaPushSet, JSONArray newMediPushSet, JSONArray newMediPushUpdate){
		String uuid = aMediaPushSet.getString("uuid");
		//查找该uuid的调阅
		MediaPushPO mediaPushPO = (MediaPushPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, mediaPushDao);
		if(null != mediaPushPO){
			//修改调阅
			aMediaPushSet.put("layerId", mediaPushPO.getLayerId());
			aMediaPushSet.put("bundleId", mediaPushPO.getBundleId());
			aMediaPushSet.put("channelId", mediaPushPO.getChannelId());
			newMediPushUpdate.add(aMediaPushSet);
		}else{
			//新建调阅
			newMediPushSet.add(aMediaPushSet);
		}
	}
	
	//录制的set和update统一处理，参数aRecordSet可能是新建也可能是修改，需要根据uuid查询，查不到就是新建，查到就是修改
	private void setOrUpdateRecord(JSONObject aRecordSet, JSONArray OutConnMediaMuxSet, JSONArray OutConnMediaMuxUpdate){
		String uuid = aRecordSet.getString("uuid");
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
		String useMsu = cdnPo.getUseMsu();
		
		JSONObject aOutConnMediaMuxSet = new JSONObject();
		if(aRecordSet.containsKey("taskId")){
			aOutConnMediaMuxSet.put("taskId", aRecordSet.getString("taskId"));					
		}
		aOutConnMediaMuxSet.put("uuid", aRecordSet.getString("uuid"));
		JSONObject channel_param = new JSONObject();
		JSONObject base_param = new JSONObject();
		
		//视频源转换为通道
		if(aRecordSet.containsKey("video_source")){
			JSONObject video_source = aRecordSet.getJSONObject("video_source");
			IdBO videoBo = getChannelidFromUuid(video_source);
			if(null != videoBo){
				video_source.put("type", "channel");
				video_source.put("layer_id", videoBo.getLayerId());
				video_source.put("bundle_id", videoBo.getBundleId());
				video_source.put("channel_id", videoBo.getChannelId());
			}
			if(!video_source.getString("type").equals("")){
				base_param.put("video_source", aRecordSet.getJSONObject("video_source"));
			}			
		}
		
		//音频源转换为通道
		if(aRecordSet.containsKey("audio_source")){
			JSONObject audio_source = aRecordSet.getJSONObject("audio_source");
			IdBO audioBo = getChannelidFromUuid(audio_source);
			if(null != audioBo){
				audio_source.put("type", "channel");
				audio_source.put("layer_id", audioBo.getLayerId());
				audio_source.put("bundle_id", audioBo.getBundleId());
				audio_source.put("channel_id", audioBo.getChannelId());
			}
			if(!audio_source.getString("type").equals("")){
				base_param.put("audio_source", aRecordSet.getJSONObject("audio_source"));
			}
		}
		
		//迭代三 ts和hls添加编解码参数
		//format的赋值在RecordService中也有同样代码
		String videoType = aRecordSet.getString("videoType");
		String format = "ts";
		//监控室录制，rtp-ps
		if(videoType.equals("2")){
			format = "rtp-ps";
		}
		//使用msu录制，则录hls
		if("true".equals(useMsu)){
			format = "hls";
		}
		aRecordSet.put("format", format);
		if(aRecordSet.containsKey("codec_param")){
			if(format.equals("ts") || format.equals("hls")){
				JSONObject codec_param = MeetingEntityUtil.codecParamForCdnJoiner(aRecordSet.getJSONObject("codec_param"));
				base_param.put("codec_param", codec_param);
			}
		}
		
		channel_param.put("base_param", base_param);
		aOutConnMediaMuxSet.put("channel_param", channel_param);
		
		//迭代三 如果有发布[rtmp]，需要一起更新
		List<OutConnMediaMuxPO> rtmpPOs = outConnMediaMuxDao.getByRecordUuid(uuid);
		for(OutConnMediaMuxPO rtmpPO : rtmpPOs){
			String jsonStr = JSON.toJSONString(aOutConnMediaMuxSet, SerializerFeature.DisableCircularReferenceDetect);
			JSONObject newRtmp = JSON.parseObject(jsonStr);//, Object.class);//(JSONObject) aOutConnMediaMuxSet.clone();
			newRtmp.put("uuid", rtmpPO.getUuid());
			newRtmp.put("layerId", rtmpPO.getLayerId());
			newRtmp.put("bundleId", rtmpPO.getBundleId());
			newRtmp.put("channelId", rtmpPO.getChannelId());
			JSONObject base_param2 = newRtmp.getJSONObject("channel_param").getJSONObject("base_param");
			base_param2.put("url", rtmpPO.getUrl());
			base_param2.put("format", rtmpPO.getFormat());
			//ts和rtmp添加编解码参数
			if(aRecordSet.containsKey("codec_param")){
//				base_param2.put("codec_param", aRecordSet.getJSONObject("codec_param"));
				JSONObject codec_param2 = MeetingEntityUtil.codecParamForCdnJoiner(aRecordSet.getJSONObject("codec_param"));
				base_param2.put("codec_param", codec_param2);
			}
			OutConnMediaMuxUpdate.add(newRtmp);
		}
		//如果是发布直播(rtmp)则不需要处理录制
		if(videoType.equals("5")){
			return;
		}
		
		//查找该uuid的录制推流
		OutConnMediaMuxPO outConnMediaMuxPO = (OutConnMediaMuxPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, outConnMediaMuxDao);
		if(null != outConnMediaMuxPO){
			//更新
			base_param.put("url", outConnMediaMuxPO.getUrl());
			base_param.put("format", outConnMediaMuxPO.getFormat());
			
			aOutConnMediaMuxSet.put("layerId", outConnMediaMuxPO.getLayerId());
			aOutConnMediaMuxSet.put("bundleId", outConnMediaMuxPO.getBundleId());
			aOutConnMediaMuxSet.put("channelId", outConnMediaMuxPO.getChannelId());
			
			OutConnMediaMuxUpdate.add(aOutConnMediaMuxSet);		
		}else{
			//新建
			String udpUrl = aRecordSet.getString("url");
//			String format = aRecordSet.getString("format");
			base_param.put("url", udpUrl);
			base_param.put("format", format);
			
			OutConnMediaMuxSet.add(aOutConnMediaMuxSet);
		}
	}
	
	//迭代三 发布[rtmp]。rtmp的更新在setOrUpdateRecord()中
	private JSONObject publishStreamSet(JSONObject aPublishStreamSet){
		String uuid = aPublishStreamSet.getString("uuid");
		String recordUuid = aPublishStreamSet.getString("recordUuid");
		
		//组装命令
		JSONObject aOutConnMediaMuxSet = new JSONObject();
		if(aPublishStreamSet.containsKey("taskId")){
			aOutConnMediaMuxSet.put("taskId", aPublishStreamSet.getString("taskId"));					
		}
		aOutConnMediaMuxSet.put("uuid", uuid);
		aOutConnMediaMuxSet.put("recordUuid", recordUuid);
		JSONObject channel_param = new JSONObject();
		JSONObject base_param = new JSONObject();
		
		//视频源转换为通道
		if(aPublishStreamSet.containsKey("video_source")){
			JSONObject video_source = aPublishStreamSet.getJSONObject("video_source");
			IdBO videoBo = getChannelidFromUuid(video_source);
			if(null != videoBo){
				video_source.put("type", "channel");
				video_source.put("layer_id", videoBo.getLayerId());
				video_source.put("bundle_id", videoBo.getBundleId());
				video_source.put("channel_id", videoBo.getChannelId());
			}
			if(!video_source.getString("type").equals("")){
				base_param.put("video_source", aPublishStreamSet.getJSONObject("video_source"));
			}			
		}
		
		//音频源转换为通道
		if(aPublishStreamSet.containsKey("audio_source")){
			JSONObject audio_source = aPublishStreamSet.getJSONObject("audio_source");
			IdBO audioBo = getChannelidFromUuid(audio_source);
			if(null != audioBo){
				audio_source.put("type", "channel");
				audio_source.put("layer_id", audioBo.getLayerId());
				audio_source.put("bundle_id", audioBo.getBundleId());
				audio_source.put("channel_id", audioBo.getChannelId());
			}
			if(!audio_source.getString("type").equals("")){
				base_param.put("audio_source", aPublishStreamSet.getJSONObject("audio_source"));
			}
		}
		//编解码参数
		if(aPublishStreamSet.containsKey("codec_param")){
//			base_param.put("codec_param", aPublishStreamSet.getJSONObject("codec_param"));
			JSONObject codec_param = MeetingEntityUtil.codecParamForCdnJoiner(aPublishStreamSet.getJSONObject("codec_param"));
			base_param.put("codec_param", codec_param);
		}
		
		channel_param.put("base_param", base_param);
		aOutConnMediaMuxSet.put("channel_param", channel_param);
		
		String rtmpUrl = aPublishStreamSet.getString("url");
		String format = aPublishStreamSet.getString("format");
		base_param.put("url", rtmpUrl);
		base_param.put("format", format);
		
		return aOutConnMediaMuxSet;
	}
	
	//从forward转换成channel
	private JSONObject forwardToChannel(JSONObject aForwardSet){
		JSONObject newChannel = (JSONObject) aForwardSet.getJSONObject("dst").clone();
		newChannel.put("channel_status", "Open");
		if(aForwardSet.containsKey("src")){
			newChannel.put("source_param", aForwardSet.getJSONObject("src"));
		}
		return newChannel;
	}
	
	//将转发中的screens 全部准换到 connectBundle中。必须保证参数都不为null
	private void forwardscreensToBundlescreens(JSONArray forwardscreens, JSONArray bundlescreens, String channelId){
		for(int i=0; i<forwardscreens.size(); i++){
			JSONObject fs = forwardscreens.getJSONObject(i);
			String fs_screen_id = fs.getString("id");
			boolean hasScreen = false;
			for(int j=0; j<bundlescreens.size(); j++){
				JSONObject bs = bundlescreens.getJSONObject(j);
				String bs_screen_id = bs.getString("screen_id");
				if(bs_screen_id.equals(fs_screen_id)){
					//是同一个screen
					hasScreen = true;
					forwardscreenToBundlescreen(fs, bs, channelId);
					break;					
				}
			}
			if(!hasScreen){
				//转换screen，加入bundlescreens中去
				JSONObject newScreen = new JSONObject();
				newScreen.put("screen_id", fs.getString("id"));
				newScreen.put("screen_status", fs.getString("screen_status"));
				forwardscreenToBundlescreen(fs, newScreen, channelId);
				bundlescreens.add(newScreen);
			}			
		}
	}
	//将转发 和 connectBundle中的[同一个]screen进行合并。必须保证参数是同一个screen，且都不为null
	private void forwardscreenToBundlescreen(JSONObject forwardscreen, JSONObject bundlescreen, String channelId){
		bundlescreen.put("screen_status", forwardscreen.getString("screen_status"));
				
		//将display转换为一个overlap
		JSONArray overlaps = forwardscreen.getJSONArray("overlaps");
		if(null == overlaps){
			forwardscreen.put("overlaps", new JSONArray());
			overlaps = forwardscreen.getJSONArray("overlaps");
		}
		if(!forwardscreen.containsKey("overlaps")){
			forwardscreen.put("overlaps", new JSONArray());
		}if(!bundlescreen.containsKey("rects")){
			bundlescreen.put("rects", new JSONArray());
		}
		if(forwardscreen.containsKey("display")){
			JSONObject display = forwardscreen.getJSONObject("display");
			JSONObject newOverlap = new JSONObject();
			if(display.containsKey("display_rect")){
				newOverlap = display.getJSONObject("display_rect");				
			}else{
				log.error(channelId + " 通道的转发中，某screen没有display_rect");
			}
			newOverlap.put("rect_id", display.getString("rect_id"));
			newOverlap.put("channel_id", channelId);
			if(display.containsKey("src_cut")){
				newOverlap.put("src_cut", display.getJSONObject("src_cut"));
			}
			overlaps.add(newOverlap);
		}		
		
		//遍历
		
		JSONArray rects = bundlescreen.getJSONArray("rects");		
		for(int i=0; i<overlaps.size(); i++){
			JSONObject aOverlap = overlaps.getJSONObject(i);
			String overlap_rect_id = aOverlap.getString("rect_id");
			boolean hasRect = false;
			for(int j=0; j<rects.size(); j++){
				JSONObject aRect = rects.getJSONObject(j);
				String bundle_rect_id = aRect.getString("rect_id");
				if(bundle_rect_id.equals(overlap_rect_id)){
					//找到同一个rect
					hasRect = true;
					aRect = aOverlap;
					aRect.put("channel", aRect.getString("channel_id"));
					aRect.remove("channel_id");
					if(aRect.containsKey("src_cut")){
						aRect.put("cut", aRect.getJSONObject("src_cut"));
						aRect.remove("src_cut");
					}
					break;					
				}
			}
			if(!hasRect){
				JSONObject aRect = aOverlap;
				aRect.put("channel", aRect.getString("channel_id"));
				aRect.remove("channel_id");
				if(aRect.containsKey("src_cut")){
					aRect.put("cut", aRect.getJSONObject("src_cut"));
					aRect.remove("src_cut");
				}
				rects.add(aRect);
			}
		}
	}
	
	/**
	 * @param srcJson格式：{
                "type":"combineVideo/combineAudio",
                "uuid":"a-c-d"//混音或合屏的uuid
            }
       @return IdBO，若无法查到uuid，或type=channel，都会返回null
	 */
	private IdBO getChannelidFromUuid(JSONObject srcJson){
		IdBO idBo = new IdBO();
		String uuid = srcJson.getString("uuid");
		if(srcJson.getString("type").equals("combineVideo")){
			//查数据库，是否存在该合屏
			CombineVideoDstPO combineVideoDstPO = (CombineVideoDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineVideoDstDao);
			if(null != combineVideoDstPO){
				//存在该合屏，取出channelID
				idBo.setLayerId(combineVideoDstPO.getLayerId());
				idBo.setBundleId(combineVideoDstPO.getBundleId());
				idBo.setChannelId(combineVideoDstPO.getChannelId());
			}else{
				return null;
			}
		}else if(srcJson.getString("type").equals("combineAudio")){
			//查数据库，是否存在该混音
			CombineAudioDstPO combineAudioDstPO = (CombineAudioDstPO)MeetingEntityUtil.queryByUuidFromMapOrDb(uuid, poMap, combineAudioDstDao);
			if(null != combineAudioDstPO){
				//存在该混音，取出channelID
				idBo.setLayerId(combineAudioDstPO.getLayerId());
				idBo.setBundleId(combineAudioDstPO.getBundleId());
				idBo.setChannelId(combineAudioDstPO.getChannelId());
			}else{
				return null;
			}
		}else{//(srcJson.getString("type").equals("channel"))
			return null;
		}
		return idBo;
	}
	
	private class IdBO {
		private String layerId;
		private String bundleId;
		private String channelId;
		public String getLayerId() {
			return layerId;
		}
		public void setLayerId(String layerId) {
			this.layerId = layerId;
		}
		public String getBundleId() {
			return bundleId;
		}
		public void setBundleId(String bundleId) {
			this.bundleId = bundleId;
		}
		public String getChannelId() {
			return channelId;
		}
		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}
	}
}
