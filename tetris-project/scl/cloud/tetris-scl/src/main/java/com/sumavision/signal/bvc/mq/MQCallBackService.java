package com.sumavision.signal.bvc.mq;

import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suma.venus.message.mq.Callback;

@Component
public class MQCallBackService implements Callback {

	@Autowired
	private ProcessReceivedMsg receiveMsg;

	@Autowired
	private MessageHandler messageHandler;

	@Value("${constant.message.mode:0}")
	private Integer mode;

	@Override
	public void execute(Message msg) {
		// TODO Auto-generated method stub
		ProcessCallBackRunnable runnable = new ProcessCallBackRunnable(msg,receiveMsg, messageHandler,mode);
		new Thread(runnable).start();
	}

}
