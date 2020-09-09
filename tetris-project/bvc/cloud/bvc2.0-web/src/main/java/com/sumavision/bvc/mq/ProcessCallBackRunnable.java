package com.sumavision.bvc.mq;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessCallBackRunnable implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCallBackRunnable.class);

	private Message msg;
	
	private ProcessReceivedMsg receivedMsg;

	public ProcessCallBackRunnable(Message msg, ProcessReceivedMsg receivedMsg) {
		this.msg = msg;
		this.receivedMsg = receivedMsg;
	}

	@Override
	public void run() {
		try {
			Boolean isFile = msg.getBooleanProperty("isFile");
			if (null == isFile || !isFile) {
				// 非文件类型的根据消息中的方法名回调相应的接口
//				String textMessage = EncoderUtil.decryptDES(((TextMessage) msg).getText(), EncoderUtil.KEY);
				String textMessage = ((TextMessage) msg).getText();
				if (null == textMessage || textMessage.trim().isEmpty()) {
					LOGGER.warn("==============ProcessCallBackMessageRunnable textMessage is null ");
					return;
				}

				//LOGGER.info("==============ProcessCallBackMessageRunnable textMessage is : " + textMessage);

				receivedMsg.process(textMessage);
			}
		} catch (Exception e) {
			LOGGER.error("ProcessCallBackMessageRunnable failed",e);
		}
	}

}
