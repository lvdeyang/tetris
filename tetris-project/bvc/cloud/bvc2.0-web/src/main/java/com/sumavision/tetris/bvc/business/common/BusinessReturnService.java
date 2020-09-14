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
         * ThreadLocal没有被当前线程赋值时或当前线程刚调用remove方法后调用get方法，返回此方法值
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
	 * 修改BusinessReturnBO中的内容<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月11日 上午11:30:24
	 */
	public void resetBusinessReturnBO(LogicBO logic,MessageSendCacheBO messageSendCacheBO){
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
	public BusinessReturnBO getBusinessReturnBO(){
		BusinessReturnBO temp = bussinessReturnThreadLocal.get();
		bussinessReturnThreadLocal.remove();
		return temp;
	}
}
