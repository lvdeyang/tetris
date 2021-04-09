package com.sumavision.tetris.bvc.business.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO.ResultDstBO;
import com.sumavision.tetris.bvc.business.bo.BusinessReturnBO;
import com.sumavision.tetris.bvc.business.group.record.GroupRecordService;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用ThreadLocal缓存logic和websocket信息<br/>
 * <p>【注意】需要在进程的开头写上businessReturnService.init(Boolean.TRUE);进行初始化</p>
 * <p>在生成logic后使用dealLogic()；生成websocket之后使用dealWebsocket()</p>
 * <p>其它需要传递的信息，由 DeliverExecuteService 处理并最终执行，参加DeliverExecuteService.java</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月22日 上午11:29:18
 */
@Slf4j
@Service
public class BusinessReturnService {
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private GroupRecordService groupRecordService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	private static ThreadLocal<BusinessReturnBO> bussinesReturnThreadLocal=new ThreadLocal<BusinessReturnBO>(){
		/**
         * ThreadLocal中没有值的时候初始化方法。
         */
        @Override
        protected BusinessReturnBO initialValue()
        {
            BusinessReturnBO businessReturnBO=new BusinessReturnBO();
            businessReturnBO.setLogic(new LogicBO());
            businessReturnBO.setWebsocketCaches(new HashMap<MessageSendCacheBO, String>());
            businessReturnBO.setSegmentedExecute(Boolean.FALSE);
            return businessReturnBO;
        }
	};
	
	public Boolean getSegmentedExecute() {
		return bussinesReturnThreadLocal.get().getSegmentedExecute();
	}

	/**
	 * <br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月16日 下午4:14:19
	 * @param segmentedExecute 是否合并命令下发。默认合并下发
	 */
	public void init(Boolean segmentedExecute){
		bussinesReturnThreadLocal.remove();
		if(segmentedExecute!=null){
			this.bussinesReturnThreadLocal.get().setSegmentedExecute(segmentedExecute);;
		}
	}
	
	/**
	 * 添加BusinessReturnBO中的内容<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月11日 上午11:30:24
	 */
	public void add(LogicBO logic,MessageSendCacheBO messageSendCacheBO,String logStr){
		if(logic!=null){
			LogicBO mergedLogic=bussinesReturnThreadLocal.get().getLogic().merge(logic);
			bussinesReturnThreadLocal.get().setLogic(mergedLogic);
		}
		if(messageSendCacheBO!=null){
			bussinesReturnThreadLocal.get().getWebsocketCaches().put(messageSendCacheBO, logStr);
		}
	}
	
	/**
	 * 要执行前获取要执行的协议和推送的消息，并清除内容<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月11日 上午11:39:16
	 * @return
	 */
	public BusinessReturnBO getAndRemove(){
		BusinessReturnBO temp = bussinesReturnThreadLocal.get();
		bussinesReturnThreadLocal.remove();
		return temp;
	}
	
	public BusinessReturnBO get(){
		BusinessReturnBO temp = bussinesReturnThreadLocal.get();
		return temp;
	}
	
	/**
	 * 执行协议，推送消息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月16日 下午4:13:31
	 * @throws Exception
	 */
	public void execute() throws Exception{
		BusinessReturnBO businessReturnBO=getAndRemove();
		executeBusiness.execute(businessReturnBO.getLogic(), "最终下发命令执行");
		
		List<Long> consumeIds = new ArrayList<Long>();
		Map<MessageSendCacheBO, String> caches=businessReturnBO.getWebsocketCaches();
		
		for (Entry<MessageSendCacheBO, String> entry : caches.entrySet()) {
			WebsocketMessageVO ws = websocketMessageService.send(entry.getKey().getUserId(), entry.getKey().getMessage(), entry.getKey().getType(), entry.getKey().getFromUserId(), entry.getKey().getFromUsername());
			log.info(entry.getValue());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
	
	public void executeWebsocket() throws Exception{
		
		BusinessReturnBO businessReturnBO=get();
		
		List<Long> consumeIds = new ArrayList<Long>();
		Map<MessageSendCacheBO, String> caches=businessReturnBO.getWebsocketCaches();
		
		for (Entry<MessageSendCacheBO, String> entry : caches.entrySet()) {
			WebsocketMessageVO ws = websocketMessageService.send(entry.getKey().getUserId(), entry.getKey().getMessage(), entry.getKey().getType(), entry.getKey().getFromUserId(), entry.getKey().getFromUsername());
			log.info(entry.getValue());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
	
	/**
	 * 对外接口：将logic信息存入缓存，或直接执行<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月22日 上午11:27:19
	 * @param logic
	 * @param logStr
	 * @param groupId 业务组id，录制业务需要该值
	 * @throws Exception
	 */
	public void dealLogic(LogicBO logic, String logStr, Long groupId) throws Exception{
		if(this.getSegmentedExecute()){
			this.add(logic, null, logStr);
		}else{
			ExecuteBusinessReturnBO response = executeBusiness.execute(logic, logStr==null?"执行逻辑命令":logStr);
			//录制处理
			if(groupId != null){
				groupRecordService.saveStoreInfo(response, groupId);
			}
		}
	}
	
	/**
	 * 对外接口：将websocket信息存入缓存，或直接执行<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月22日 上午11:25:21
	 * @param messageCaches
	 * @throws Exception
	 */
	public void dealWebsocket(List<MessageSendCacheBO> messageCaches) throws Exception{
		if(this.getSegmentedExecute()){
			for(MessageSendCacheBO cache : messageCaches){
				this.add(null, cache, null);
			}
		}else{
			List<Long> consumeIds = new ArrayList<Long>();
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
	}
	
}
