package com.sumavision.signal.bvc.mq;

import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.message.mq.Callback;

@Component
public class MQCallBackService implements Callback {

	@Autowired
	private ProcessReceivedMsg receiveMsg;

	@Override
	public void execute(Message msg) {
		// TODO Auto-generated method stub
		ProcessCallBackRunnable runnable = new ProcessCallBackRunnable(msg,receiveMsg);
		new Thread(runnable).start();
	}

}
