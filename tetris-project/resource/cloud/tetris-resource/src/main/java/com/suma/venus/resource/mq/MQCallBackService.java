package com.suma.venus.resource.mq;

import javax.jms.Message;

import com.suma.venus.message.mq.Callback;

public class MQCallBackService implements Callback{

	@Override
	public void execute(Message msg) {
		ProcessCallBackRunnable runnable = new ProcessCallBackRunnable(msg);
		new Thread(runnable).start();
	}

}
