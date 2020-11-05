package com.sumavision.tetris.zoom.call.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.resouce.feign.resource.ResourceService;
import com.sumavision.tetris.resouce.feign.resource.ResourceVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.zoom.call.user.exception.CallUserBusyException;
import com.sumavision.tetris.zoom.call.user.exception.CallUserNotExistException;

@Service
@Transactional(rollbackFor = Exception.class)
public class CallUserService {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CallUserDAO callUserDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 呼叫用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月25日 下午7:30:40
	 * @param String userno 被呼叫用户号码
	 * @param CallUserType type 呼叫类型
	 * @return CallUserVO 呼叫用户信息
	 */
	public CallUserVO invite(
			String userno,
			CallUserType type) throws Exception{
		UserVO srcUser = userQuery.current();
		UserVO dstUser = userQuery.queryUserByNo(userno);
		String uniqueKey = CallUserPO.generateUniqueKey(srcUser.getId().toString(), dstUser.getId().toString());
		CallUserPO entity = callUserDao.findByUniqueKey(uniqueKey);
		if(entity == null){
			List<ResourceVO> srcBundleInfos = resourceService.queryResource(new ArrayListWrapper<Long>().add(srcUser.getId()).getList(), "pc");
			ResourceVO srcBundleInfo = srcBundleInfos.get(0);
			List<ResourceVO> dstBundleInfos = resourceService.queryResource(new ArrayListWrapper<Long>().add(dstUser.getId()).getList(), "pc");
			ResourceVO dstBundleInfo = dstBundleInfos.get(0);
			entity = new CallUserPO();
			entity.setSrcUserId(srcUser.getId().toString());
			entity.setSrcUserno(srcUser.getUserno());
			entity.setSrcNickname(srcUser.getNickname());
			entity.setSrcBundleId(srcBundleInfo.getBundleId());
			entity.setSrcLayerId(srcBundleInfo.getLayerId());
			entity.setSrcVideoChannelId(srcBundleInfo.getVideoChannelId());
			entity.setSrcAudioChannelId(srcBundleInfo.getAudioChannelId());
			entity.setDstUserId(dstUser.getId().toString());
			entity.setDstUserno(dstUser.getUserno());
			entity.setDstNickname(dstUser.getNickname());
			entity.setDstBundleId(dstBundleInfo.getBundleId());
			entity.setDstLayerId(dstBundleInfo.getLayerId());
			entity.setDstVideoChannelId(dstBundleInfo.getVideoChannelId());
			entity.setDstAudioChannelId(dstBundleInfo.getAudioChannelId());
			entity.setStatus(CallUserStatus.INVITE);
			entity.setType(type);
			//如果有并发问题会在这个地方报错
			callUserDao.save(entity);
			
			CallUserVO content = new CallUserVO().set(entity);
			websocketMessageService.push(dstUser.getId().toString(), "callUserInvite", JSON.parseObject(JSON.toJSONString(content)), srcUser.getId().toString(), srcUser.getNickname());
			
			return content;
		}else{
			throw new CallUserBusyException();
		}
	}
	
	/**
	 * 呼叫接听<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午9:09:08
	 * @param Long id 呼叫任务id
	 * @return CallUserVO 呼叫用户信息
	 */
	public CallUserVO accept(Long id) throws Exception{
		CallUserPO callUser = callUserDao.findOne(id);
		if(callUser == null){
			throw new CallUserNotExistException(id);
		}
		callUser.setStatus(CallUserStatus.IN_THE_CALL);
		callUserDao.save(callUser);
		CallUserVO content = new CallUserVO().set(callUser);
		websocketMessageService.push(callUser.getSrcUserId(), "callUserAccept", JSON.parseObject(JSON.toJSONString(content)), callUser.getDstUserId(), callUser.getDstNickname());
		return content;
	}
	
	/**
	 * 呼叫拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午9:11:57
	 * @param Long id 呼叫任务id
	 * @return CallUserVO 呼叫用户信息
	 */
	public CallUserVO refuse(Long id) throws Exception{
		CallUserPO callUser = callUserDao.findOne(id);
		if(callUser == null){
			throw new CallUserNotExistException(id);
		}
		callUserDao.delete(callUser);
		CallUserVO content = new CallUserVO().set(callUser);
		websocketMessageService.push(callUser.getSrcUserId(), "callUserRefuse", JSON.parseObject(JSON.toJSONString(content)), callUser.getDstUserId(), callUser.getDstNickname());
		return content;
	}
	
	/**
	 * 呼叫结束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午9:25:30
	 * @param Long id 呼叫任务id
	 * @return CallUserVO 呼叫用户信息
	 */
	public CallUserVO stop(Long id) throws Exception{
		UserVO user = userQuery.current();
		CallUserPO callUser = callUserDao.findOne(id);
		if(callUser == null){
			throw new CallUserNotExistException(id);
		}
		callUserDao.delete(callUser);
		CallUserVO content = new CallUserVO().set(callUser);
		String targetUserId = null;
		String srcUserId = null;
		String srcUserNickname = null;
		if(user.getId().toString().equals(callUser.getSrcUserId())){
			targetUserId = callUser.getDstUserId();
			srcUserId = callUser.getSrcUserId();
			srcUserNickname = callUser.getSrcNickname();
		}else if(user.getId().toString().equals(callUser.getDstUserId())){
			targetUserId = callUser.getSrcUserId();
			srcUserId = callUser.getDstUserId();
			srcUserNickname = callUser.getDstNickname();
		}
		websocketMessageService.push(targetUserId, "callUserStop", JSON.parseObject(JSON.toJSONString(content)), srcUserId, srcUserNickname);
		return content;
	}
	
}
