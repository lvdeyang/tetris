package com.sumavision.bvc.device.command.emergent.broadcast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastSpeakPO;
import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastSpeakerPO;
import com.sumavision.bvc.command.group.dao.CommandBroadcastSpeakDAO;
import com.sumavision.bvc.communication.http.HttpClient;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.resouce.feign.bundle.BundleFeignService;
import com.sumavision.tetris.resouce.feign.bundle.BundleVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandEmergentBroadcastServiceImpl 
* @Description: 应急指挥调度
* @author zsy
* @date 2020年3月10日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandEmergentBroadcastServiceImpl {
	
	//应急广播喊话系统的IP端口
	@Value("${emergent.smartexpressIssueUrl}")
	private String smartexpressIssueUrl;
	
	@Autowired
	private CommandBroadcastSpeakDAO commandBroadcastSpeakDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleFeignService bundleFeignService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 开始喊话<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午11:20:44
	 * @param bundleIds
	 * @param eventLevel
	 * @param eventType
	 * @return
	 * @throws Exception
	 */
	public CommandBroadcastSpeakPO speakStart(List<String> bundleIds, int eventLevel, String eventType) throws Exception{
		
		//从bundleId列表查询所有的bundlePO
		List<BundlePO> bundles = resourceBundleDao.findByBundleIds(bundleIds);
		
		//校验bundles个数
		if(bundles.size() == 0){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择扬声器");
		}
		
		//TODO:把bundleIds转换成23位的标识符
		List<String> bundleIdentifications = new ArrayList<String>();
		for(BundlePO bundle : bundles){
//			bundleIdentifications.add(bundle.getIdentification());
		}
		String terminalEbrids = StringUtils.join(bundleIdentifications.toArray(), ",");
		
		JSONObject params = new JSONObject();
		params.put("eventLevel", eventLevel);
		params.put("eventType", eventType);
		params.put("fromBVC", true);
		params.put("eventType", eventType);
		params.put("terminalEbrids", terminalEbrids);
		
		String url = "http://" + smartexpressIssueUrl + "/smartexpress-issue-ui/speakerByDS/startCallAjax";
		
		
		log.info("开始喊话：" + url + " 参数：" + params.toJSONString());
		
		String r = null;
		try {
			r = HttpClient.post(url, params.toJSONString());//请求头数据格式可能不对
		} catch (Exception e) {
			e.printStackTrace();
			log.error("开始喊话请求失败");
			throw new BaseException(StatusCode.FORBIDDEN, "喊话失败");
		}
		log.info("开始喊话请求返回：" + r);
		
		if(r.contains("@$$@请求失败")){
			throw new BaseException(StatusCode.FORBIDDEN, "喊话失败，" + r.split("@$$@")[0]);
		}		
		
		CommandBroadcastSpeakPO speak = new CommandBroadcastSpeakPO();
		speak.setCreatetime(new Date());
		speak.setSpeakers(new ArrayList<CommandBroadcastSpeakerPO>());
		for(BundlePO bundle : bundles){
			CommandBroadcastSpeakerPO speaker = new CommandBroadcastSpeakerPO().set(bundle);
			speaker.setSpeak(speak);
			speak.getSpeakers().add(speaker);
		}
		commandBroadcastSpeakDao.save(speak);		
		
		return speak;
	}	
	
	/**
	 * 停止喊话<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午11:21:42
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public void speakStop(Long taskId) throws Exception{
		
		if(taskId == null){
			throw new BaseException(StatusCode.FORBIDDEN, "停止喊话，任务id有误");
		}
		log.info("对接url：" + smartexpressIssueUrl);
		
		synchronized (new StringBuffer().append("command-emergent-broadcast-").append(taskId).toString().intern()) {
					
			CommandBroadcastSpeakPO speak = commandBroadcastSpeakDao.findOne(taskId);
			
			if(speak == null){
				log.info("停止喊话的任务不存在，可能已经停止，id：" + taskId);
				return;
			}
			
			JSONObject params = new JSONObject();
			params.put("fromBVC", true);
			params.put("messageId", speak.getId());
			
			String url = "http://" + smartexpressIssueUrl + "/smartexpress-issue-ui/speakerByDS/stopCallAjax";			
			
			log.info("开始喊话：" + url + " 参数：" + params.toJSONString());
			String r = null;
			try {
				r = HttpClient.post(url, params.toJSONString());//请求头数据格式可能不对
			} catch (Exception e) {
				e.printStackTrace();
				log.error("停止喊话请求失败");
				throw new BaseException(StatusCode.FORBIDDEN, "停止喊话失败");
			}
			log.info("停止喊话请求返回：" + r);
			
			if(r.contains("@$$@请求失败")){
				throw new BaseException(StatusCode.FORBIDDEN, "停止喊话失败，" + r.split("@$$@")[0]);
			}
			
			commandBroadcastSpeakDao.delete(speak);
		}
	}
	
	/**
	 * 查询所有喊话列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 下午4:56:58
	 * @return 格式见《应急广播bvc相关接口》
	 */
	public JSONObject queryAllSpeakList(){
		
		List<CommandBroadcastSpeakPO> speaks = commandBroadcastSpeakDao.findAll();
		JSONArray broadcastList = new JSONArray();
		for(CommandBroadcastSpeakPO speak : speaks){
			JSONObject speakJSON = new JSONObject();
			JSONArray bundles = new JSONArray();
			for(CommandBroadcastSpeakerPO speaker : speak.getSpeakers()){
				JSONObject bundleJSON = new JSONObject();
				bundleJSON.put("bundleId", speaker.getBundleId());
				bundleJSON.put("bundleName", speaker.getBundleName());
				bundles.add(bundleJSON);
			}
			speakJSON.put("id", speak.getId().toString());
			speakJSON.put("bundles", bundles);
			broadcastList.add(speakJSON);
		}
		JSONObject result = new JSONObject();
		result.put("broadcastList", broadcastList);
		
		return result;
	}
	
	/**
	 * 向用户推送设备信息，并返回设备信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午11:23:17
	 * @throws Exception 
	 */
	public List<BundleVO> queryAndNotifyDevices(Long longitude, Long latitude, Long raidus) throws Exception{

		//向资源查询
		List<BundleVO> bundleVOs = bundleFeignService.queryVisibleBundle(longitude, latitude, raidus);
		
		//生成消息
		JSONObject message = new JSONObject();
//		JSONArray bundles = new JSONArray();
//		for(){
//			JSONObject bundle = new JSONObject();
//			bundle.put("id", "");
//			
//			bundles.add(bundle);
//		}
		message.put("businessType", "emergentBroadcastRelativeDevices");
		message.put("businessInfo", "应急广播推送设备");
		message.put("bundles", bundleVOs);
		
		//websocket给所有用户推送
		List<Long> consumeIds = new ArrayList<Long>();
		List<UserVO> userVOs = userQuery.queryAllUserBaseInfo("qt指控软件");
		for(UserVO userVO : userVOs){
			WebsocketMessageVO ws = websocketMessageService.send(userVO.getId(), JSON.toJSONString(message), WebsocketMessageType.COMMAND);
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
		
		//打印
		log.info("给" + userVOs.size() + "个用户推送应急广播设备：" + message);
		
		return bundleVOs;
	}
	
}
