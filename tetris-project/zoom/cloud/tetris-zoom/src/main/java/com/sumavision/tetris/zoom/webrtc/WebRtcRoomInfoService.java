package com.sumavision.tetris.zoom.webrtc;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.resouce.feign.resource.ResourceService;
import com.sumavision.tetris.resouce.feign.resource.WorkNodeVO;
import com.sumavision.tetris.zoom.webrtc.exception.NoWebrtcMoudleFoundException;
import com.sumavision.tetris.zoom.webrtc.exception.WebrtcResponseErrorException;

@Service
@Transactional(rollbackFor = Exception.class)
public class WebRtcRoomInfoService {

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private WebRtcRoomInfoDAO webRtcRoomInfoDao;
	
	/**
	 * post请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:35:01
	 * @param String url 请求地址
	 * @param Map<String, Object> params 参数
	 * @param String errorMsg 异常信息
	 * @return String 接口返回
	 */
	public static JSONObject doPost(String url, Map<String, Object> params, String errorMsg) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			if(params!=null && params.size()>0){
				httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));
			}
			response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200){
	        	HttpEntity entity = response.getEntity();
	        	String result = FileUtil.readAsString(entity.getContent());
	 	        EntityUtils.consume(entity);
	 	        return JSON.parseObject(result);
	        }else{
	        	throw new WebrtcResponseErrorException(url, errorMsg);
	        }
		}finally{
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
	/**
	 * 向webrtc模块创建房间<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午4:23:43
	 * @param Long zoomId 会议室id
	 * @param String zoomCode 会议室号码
	 * @return WebRtcVO webrtc模块信息
	 */
	public WebRtcVO createRoom(Long zoomId, String zoomCode) throws Exception{
		WorkNodeVO workNode = queryUseableWebrtcModule();
		WebRtcRoomInfoPO webRtcRoomInfo = new WebRtcRoomInfoPO();
		webRtcRoomInfo.setZoomId(zoomId);
		webRtcRoomInfo.setRoomId(zoomCode);
		webRtcRoomInfo.setUpdateTime(new Date());
		webRtcRoomInfo.setIp(workNode.getIp());
		webRtcRoomInfo.setWebRtcLayerId(workNode.getNodeUid());
		webRtcRoomInfo.setWebRtcId(workNode.getId());
		webRtcRoomInfo.setWebRtcHttpPort(workNode.getWebrtcHttpPort().toString());
		webRtcRoomInfo.setWebRtcWebSocketPort(workNode.getWebrtcWebsocketPort().toString());
		webRtcRoomInfo.setWebRtcLayerHttpPort(workNode.getPort().toString());
		webRtcRoomInfoDao.save(webRtcRoomInfo);
		
		String url = new StringBufferWrapper().append("http://")
											  .append(webRtcRoomInfo.getIp())
											  .append(":")
											  .append(webRtcRoomInfo.getWebRtcLayerHttpPort())
											  .append("/action/create_room")
											  .toString();
		
		JSONObject response = doPost(url, new HashMapWrapper<String, Object>().put("roomId", zoomCode).getMap(), new StringBufferWrapper().append("webrtc会议室创建失败，roomId：").append(zoomCode).toString());
		if("failed".equals(response.getString("ret"))){
			throw new WebrtcResponseErrorException(url, new StringBufferWrapper().append("webrtc会议室创建失败，roomId重复，roomId：").append(zoomCode).toString());
		}
		
		return new WebRtcRoomInfoVO().set(webRtcRoomInfo).transform();
	}
	
	/**
	 * 销毁webrtc模块房间<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午5:15:33
	 * @param Long zoomId 会议室id
	 */
	public void destroyRoom(Long zoomId) throws Exception{
		List<WebRtcRoomInfoPO> webRtcRoomInfos = webRtcRoomInfoDao.findByZoomId(zoomId);
		if(webRtcRoomInfos==null || webRtcRoomInfos.size()<=0) return;
		for(WebRtcRoomInfoPO webRtcRoomInfo:webRtcRoomInfos){
			String url = new StringBufferWrapper().append("http://")
												  .append(webRtcRoomInfo.getIp())
												  .append(":")
												  .append(webRtcRoomInfo.getWebRtcLayerHttpPort())
												  .append("/action/destroy_room")
												  .toString();
			
			doPost(url, new HashMapWrapper<String, Object>().put("roomId", webRtcRoomInfo.getRoomId()).getMap(), new StringBufferWrapper().append("webrtc会议销毁建失败，roomId：").append(webRtcRoomInfo.getRoomId()).toString());
		}
		
		webRtcRoomInfoDao.deleteInBatch(webRtcRoomInfos);
	}
	
	/**
	 * 获取可用的webrtc模块<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午4:31:24
	 * @return WorkNodeVO webrtc模块信息
	 */
	private WorkNodeVO queryUseableWebrtcModule() throws Exception{
		List<WorkNodeVO> modules = resourceService.queryWebRtc();
		if(modules==null || modules.size()<=0){
			throw new NoWebrtcMoudleFoundException();
		}
		Random random = new Random();
		return modules.get(random.nextInt(modules.size()));
	}
	
}
