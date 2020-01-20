package com.suma.venus.message.mq;

import java.io.File;
import java.io.FileInputStream;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueReceiver;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;

import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订阅模式下的消息发送者 系统内仅存在一个publisher
 * 
 * @author admin
 *
 */
public class TopicMsgPublisher {

	private static Logger LOGGER = LoggerFactory.getLogger(TopicMsgPublisher.class);

	private static String VENUS_TOPICNAME = "venusTopic";

	private String topicName;

	private Topic topic;

	private static TopicPublisher publisher;

	private static TopicMsgPublisher msgPublisher;

	private TopicMsgPublisher() {
		try {
			topicName = VENUS_TOPICNAME;
			topic = MQConnetion.topicSession.createTopic(topicName);
			publisher = MQConnetion.topicSession.createPublisher(topic);
			// 非持久化发送
			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			LOGGER.error("TopicMsgPublisher error", e);
		}

	}

	public static TopicMsgPublisher getMsgPublisher() {
		if (null == msgPublisher) {
			msgPublisher = new TopicMsgPublisher();
		}
		return msgPublisher;
	}

	public void destroy() {
		try {
			msgPublisher = null;
			publisher.close();
		} catch (JMSException e) {
			LOGGER.error("destroy msgPublisher error", e);
		}
	}

	/**
	 * 只管发送消息
	 * 
	 * @param msgInfo
	 */
	public void sendMsg(String msgInfo) {
		if (null == msgInfo || msgInfo.trim().isEmpty()) {
			return;
		}
		try {
			TextMessage msg = MQConnetion.topicSession.createTextMessage();
			msg.setText(msgInfo);
//			msg.setText(EncoderUtil.encryptDES(msgInfo, EncoderUtil.KEY));
			msg.setBooleanProperty("isFile", false);
			publisher.send(msg);
			System.out.println("published msg is " + msgInfo);
		} catch (JMSException e) {
			LOGGER.error("TopicMsgPublisher error", e);
		}

	}

	public void sendFile(File file) {
		if (null == file) {
			return;
		}

		try {
			FileInputStream in = new FileInputStream(file);
			BlobMessage msg = ((ActiveMQSession) MQConnetion.topicSession).createBlobMessage(in);
			msg.setStringProperty("FILE.NAME", file.getName());
			msg.setLongProperty("FILE.SIZE", file.length());
			msg.setBooleanProperty("isFile", true);

			System.out.println("发送消息：" + "ActiveMq 发送的消息" + file.getName());
			publisher.send(msg);

		} catch (Exception e) {
			LOGGER.error("TopicMsgPublisher sendMsg sendFile error", e);
		}
		return;
	}

}
