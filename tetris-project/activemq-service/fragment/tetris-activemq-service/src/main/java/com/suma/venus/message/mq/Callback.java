package com.suma.venus.message.mq;

import javax.jms.Message;

/**
 * 消息接收回调接口
 */
public interface Callback {
	public void execute(Message msg);
}
