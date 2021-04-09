/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-service 上午10:26:39  
* All right reserved.  
*  
*/

package com.sumavision.bvc.monitor.logic.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.message.mq.Callback;
import com.suma.venus.message.mq.QueueMsgSender;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.feign.ResourceServiceClient;

/**
 * @desc: bvc-monitor-service
 * @author: cll 异步任务的抽象类
 * @createTime: 2018年6月6日 上午10:26:39
 * @history:
 * @version: v1.0
 */
@Component
public abstract class asyncTask implements Callback {
	/**
	 * Execute with callback
	 */

	Callback callback;
	@Autowired
	MessageService messageService;
	
	

	public asyncTask() {

	}

	public static ResponseBO sendMessage(String message) {
		// 向外发送请求，并给response队列回复消息
		QueueMsgSender sender = QueueMsgSender.getSenderByQueueName("resourceQueue");
		// 测试文件
		ResponseBO resp = sender.sendMsg(message, 10000l);
		return resp;
	}
}
