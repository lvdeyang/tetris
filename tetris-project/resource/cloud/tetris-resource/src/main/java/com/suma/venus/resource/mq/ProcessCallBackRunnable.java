package com.suma.venus.resource.mq;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suma.venus.resource.utils.SpringContextUtil;

public class ProcessCallBackRunnable implements Runnable{

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCallBackRunnable.class);
	
	private Message msg;

	public ProcessCallBackRunnable(Message msg) {
		this.msg = msg;
	}

	@Override
	public void run() {
		try {
			Boolean isFile = msg.getBooleanProperty("isFile");
			if (null == isFile || !isFile) {
				// 非文件类型的根据消息中的方法名回调相应的接口
				String textMessage =((TextMessage) msg).getText();
//				String textMessage = EncoderUtil.decryptDES(((TextMessage) msg).getText(), EncoderUtil.KEY);
				if(null == textMessage || textMessage.trim().isEmpty()){
					LOGGER.warn("==============ProcessCallBackMessageRunnable textMessage is null ");
					return;
				}
				
				LOGGER.info("==============ProcessCallBackMessageRunnable textMessage is : " + textMessage);

				ProcessReceivedMsg process = SpringContextUtil.getBean(ProcessReceivedMsg.class);
				process.process(textMessage);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
	}

}
