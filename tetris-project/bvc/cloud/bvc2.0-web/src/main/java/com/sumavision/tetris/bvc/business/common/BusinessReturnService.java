package com.sumavision.tetris.bvc.business.common;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.tetris.bvc.business.bo.BusinessReturnBO;

@Service
public class BusinessReturnService {
	
	private static ThreadLocal<BusinessReturnBO> bussinessReturnThreadLocal=new ThreadLocal<BusinessReturnBO>(){
		/**
         * ThreadLocal中没有值的时候初始化方法。
         */
        @Override
        protected BusinessReturnBO initialValue()
        {
            BusinessReturnBO businessReturnBO=new BusinessReturnBO();
            businessReturnBO.setLogic(new LogicBO());
            businessReturnBO.setWebsocketCaches(new ArrayList<MessageSendCacheBO>());
            return businessReturnBO;
        }
	};
	
	/**
	 * 添加BusinessReturnBO中的内容<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月11日 上午11:30:24
	 */
	public void add(LogicBO logic,MessageSendCacheBO messageSendCacheBO){
		if(logic!=null){
			LogicBO mergedLogic=bussinessReturnThreadLocal.get().getLogic().merge(logic);
			bussinessReturnThreadLocal.get().setLogic(mergedLogic);
		}
		if(messageSendCacheBO!=null){
			bussinessReturnThreadLocal.get().getWebsocketCaches().add(messageSendCacheBO);
		}
	}
	
	/**
	 * 要执行前获取要执行的协议，并清除内容<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月11日 上午11:39:16
	 * @return
	 */
	public BusinessReturnBO getAndRemove(){
		BusinessReturnBO temp = bussinessReturnThreadLocal.get();
		bussinessReturnThreadLocal.remove();
		return temp;
	}
}
