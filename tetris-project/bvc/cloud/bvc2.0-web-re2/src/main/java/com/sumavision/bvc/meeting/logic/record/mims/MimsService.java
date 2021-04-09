package com.sumavision.bvc.meeting.logic.record.mims;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordFragmentDAO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.communication.http.HttpAsyncClient;
import com.sumavision.bvc.communication.http.HttpClient;
import com.sumavision.bvc.device.group.dao.PublishStreamDAO;
import com.sumavision.bvc.device.group.dao.RecordDAO;
import com.sumavision.bvc.device.group.po.GroupRecordPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeIpMissingException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodePortMissionException;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional
public class MimsService {
	
	@Autowired
	private PublishStreamDAO publishStreamDao;

	@Autowired
	private CommandGroupRecordFragmentDAO commandGroupRecordFragmentDAO;
	
	@Autowired
	private RecordDAO recordDAO;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 生成发布媒资<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月19日 下午2:29:44
	 * @param String playUrl 发布url
	 * @param String name 名称
	 */
	public void generateMimsResource(PublishStreamPO publish) throws Exception{

		MimsProperties mimsProperties = MimsProperties.getProperties();
		String appId = mimsProperties.getAppId();
		String appSecret = mimsProperties.getAppSecret();
		String url = mimsProperties.getUrl();
		String useMims = mimsProperties.getUseMims();
		String playUrl = publish.getTransferUrl();
		String name = publish.getRecord().getVideoName();
		Long publisId = publish.getId();
		
		if("true".equals(useMims)){
			String timeTamp = String.valueOf(new Date().getTime());
			String sign = sign(appId, timeTamp, appSecret);
			
			List<String> previewUrl = new ArrayList<String>();
			previewUrl.add(playUrl);
			
			JSONObject object = new JSONObject();
			
			object.put("previewUrl", JSON.toJSONString(previewUrl));
			object.put("name", name);
			object.put("tags", new ArrayList<String>());
			object.put("keyWords", new ArrayList<String>());
			object.put("remark", "");
			
			List<BasicNameValuePair> postBody = new ArrayList<BasicNameValuePair>();
			if(object != null){
				Set<String> keys = object.keySet();
				for(String key:keys){
					postBody.add(new BasicNameValuePair(key, object.getString(key)));  
				}
			}
			
			HttpAsyncClient.getInstance().formPost("http://" + url + "/tetris-mims/api/server/media/video/stream/task/add?appId=" + appId + "&timestamp=" + timeTamp + "&sign=" + sign, null, postBody, new MimsResourceCallBack(publisId, publishStreamDao));
		}
		
	}
	
	/**
	 * 删除发布媒资<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月22日 下午1:19:16
	 * @param List<PublishStreamPO> publishStreams 需要删除的媒资信息
	 */
	public void removeMimsResource(List<PublishStreamPO> publishStreams) throws Exception{
		
		MimsProperties mimsProperties = MimsProperties.getProperties();
		String appId = mimsProperties.getAppId();
		String appSecret = mimsProperties.getAppSecret();
		String url = mimsProperties.getUrl();
		String useMims = mimsProperties.getUseMims();
		
		if("true".equals(useMims)){
			String timeTamp = String.valueOf(new Date().getTime());
			String sign = sign(appId, timeTamp, appSecret);
			
			List<Long> mimsIds = new ArrayList<Long>();
			for(PublishStreamPO publish: publishStreams){
				if(publish.getMimsId() != null){
					mimsIds.add(publish.getMimsId());
				}
			}
			
			if(mimsIds != null && mimsIds.size() > 0){
				
				JSONObject object = new JSONObject();
				
				object.put("mediaIds", JSON.toJSONString(mimsIds));
				
				List<BasicNameValuePair> postBody = new ArrayList<BasicNameValuePair>();
				if(object != null){
					Set<String> keys = object.keySet();
					for(String key:keys){
						postBody.add(new BasicNameValuePair(key, object.getString(key)));  
					}
				}
				
				HttpClient.encodepost("http://" + url + "/tetris-mims/api/server/media/video/stream/remove?appId=" + appId + "&timestamp=" + timeTamp + "&sign=" + sign, object);
//				HttpAsyncClient.getInstance().formPost("http://" + url + "/tetris-mims/api/server/media/video/stream/remove?appId=" + appId + "&timestamp=" + timeTamp + "&sign=" + sign, null, postBody, null);
			}
		}
	}
	
	/**
	 * byte[]转16进制字符串<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:49:48
	 * @param byte[] src 
	 * @return String 16进制字符串
	 */
	public static String bytesToHexString(byte[] src){      
        StringBuilder stringBuilder = new StringBuilder();      
        if (src == null || src.length <= 0) {      
            return null;      
        }      
        for (int i = 0; i < src.length; i++) {      
            int v = src[i] & 0xFF;      
            String hv = Integer.toHexString(v);      
            if (hv.length() < 2) {      
                stringBuilder.append(0);      
            }      
            stringBuilder.append(hv);      
        }      
        return stringBuilder.toString();      
    } 
	
	/**
	 * 字符串编码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:32:58
	 * @param String message 待编码字符串
	 * @return String 编码后十六进制
	 */
	public String encode(String message) throws Exception{
		if(message == null) return null;
		MessageDigest messageDigest = null;
		String encodeMessage = "";
		messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(message.getBytes("UTF-8"));
		encodeMessage = ByteUtil.bytesToHexString(messageDigest.digest());
		return encodeMessage;
	}
	
	public String sign(String appId, String timestamp, String appSecret) throws Exception{
		List<String> resources = new ArrayListWrapper<String>().add(appId)
															   .add(timestamp)
															   .add(appSecret)
															   .getList();
		Collections.sort(resources, new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		String unSigned = new StringBufferWrapper().append(resources.get(0))
												   .append(resources.get(1))
												   .append(resources.get(2))
												   .toString();
		return encode(unSigned);
	}
	
	/**
	 * 重构生成发布媒资<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月31日 下午1:50:21
	 * @param groupRecords 录制视频片段
	 * @throws Exception
	 */
	public void generateTetrisMimsResource(Collection <GroupRecordPO> groupRecords) throws Exception{

		MimsProperties mimsProperties = MimsProperties.getProperties();
		String appId = mimsProperties.getAppId();
		String appSecret = mimsProperties.getAppSecret();
		String url = mimsProperties.getUrl();
		String useMims = mimsProperties.getUseMims();
		
		
		for(GroupRecordPO groupRecord:groupRecords){
			//拼接播放地址
			AccessNodeBO targetLayer = null;
			List<AccessNodeBO> layers = null;
			if(groupRecord.getStoreLayerId() != null){
				layers = resourceService.queryAccessNodeByNodeUids(new ArrayListWrapper<String>().add(groupRecord.getStoreLayerId()).getList());
			}
			if(layers==null || layers.size()<=0){
				throw new AccessNodeNotExistException(groupRecord.getStoreLayerId());
			}
			targetLayer = layers.get(0);
			if(targetLayer.getIp() == null){
				throw new AccessNodeIpMissingException(groupRecord.getStoreLayerId());
			}
			if(targetLayer.getPort() == null){
				throw new AccessNodePortMissionException(groupRecord.getStoreLayerId());
			}
			String playUrl = new StringBufferWrapper().append("http://")
					   .append(targetLayer.getIp())
					   .append(":")
					   .append(targetLayer.getPort())
					   .append("/")
					   .append(groupRecord.getPreviewUrl())
					   .toString();
			//拼接播放名称
			String name = new StringBuffer()
					.append(groupRecord.getRecordInfo().getGroupName())
					.append(" "+DateUtil.format(groupRecord.getUpdateTime()))
					.append(" "+groupRecord.getInfo())
					.toString()
					.replace(" ", "-");
			Long publisId = groupRecord.getId();
			
			if("true".equals(useMims)){
				String timeTamp = String.valueOf(System.currentTimeMillis());
				String sign = sign(appId, timeTamp, appSecret);
				
				List<String> previewUrl = new ArrayList<String>();
				previewUrl.add(playUrl);
				
				JSONObject object = new JSONObject();
				
				object.put("httpUrl", JSON.toJSONString(previewUrl));
				object.put("name", name);
				object.put("tags", new ArrayList<String>());
				object.put("keyWords", new ArrayList<String>());
				object.put("remark", "");
				
				List<BasicNameValuePair> postBody = new ArrayList<BasicNameValuePair>();
				if(object != null){
					Set<String> keys = object.keySet();
					for(String key:keys){
						postBody.add(new BasicNameValuePair(key, object.getString(key)));  
					}
				}
				String path ="http://" + url + "/tetris-mims/api/server/media/video/task/add/remote?appId=" + appId + "&timestamp=" + timeTamp + "&sign=" + sign+"&httpUrl="+playUrl+"&name="+name;
				System.out.println(path);
				HttpAsyncClient.getInstance().formPost(path, null, postBody, new MimsResourceCallBack(publisId, commandGroupRecordFragmentDAO));
			}
		}
		
		
	}
}
