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
import com.sumavision.tetris.bvc.business.bo.BusinessReturnBO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BusinessReturnService {
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
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
	
}
