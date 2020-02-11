package com.suma.venus.message.mq;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.BlobMessage;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suma.venus.message.util.SysConf;

public class TopicMsgSubscriber {

	private static Logger LOGGER = LoggerFactory.getLogger(TopicMsgSubscriber.class);

	private static String VENUS_TOPICNAME = "venusTopic";

	private static TopicMsgSubscriber msgSubscriber;

	private static MessageConsumer messageConsumer;

	private Topic topic;

	private TopicMsgSubscriber() {
		try {
			topic = MQConnetion.topicSession.createTopic(VENUS_TOPICNAME);
			messageConsumer = MQConnetion.topicSession.createConsumer(topic);
			messageConsumer.setMessageListener(new Listener());
		} catch (JMSException e) {
			LOGGER.error("TopicMsgSubscriber error", e);
		}
	}

	public static TopicMsgSubscriber getMsgSubscriber() {
		if (null == msgSubscriber) {
			msgSubscriber = new TopicMsgSubscriber();
		}
		return msgSubscriber;
	}

	public void destroy() {
		try {
			msgSubscriber = null;
			messageConsumer.close();
		} catch (JMSException e) {
			LOGGER.error("destroy msgPublisher error", e);
		}
	}

	private static class Listener implements MessageListener {

		public void onMessage(Message message) {
			try {
				if (message.getBooleanProperty("isFile")) {
					try {
						BlobMessage msg = (BlobMessage) message;
						System.out.println("收到消息" + msg.getStringProperty("FILE.NAME"));
						FileOutputStream out = new FileOutputStream(
								SysConf.getFileDownloadUrl() + "recv_" + msg.getStringProperty("FILE.NAME"));
						InputStream in = msg.getInputStream();
						byte[] buffer = new byte[1024 * 1024];
						int len = 0;
						while ((len = in.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}
						out.close();
						((ActiveMQBlobMessage) msg).deleteFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("完成文件接收：" + "recv_" + message.getStringProperty("FILE.NAME"));
				} else {
					TextMessage tm = (TextMessage) message;
//	            	System.out.println("Received message: " + EncoderUtil.decryptDES(tm.getText(), EncoderUtil.KEY));
					System.out.println("Received message: " + tm.getText());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
