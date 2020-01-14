package com.suma.venus.message.mq;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.VenusMessageHead;
import com.suma.venus.message.service.MessageService;

/**
 * 队列类型消息发送
 * 
 * @author admin
 *
 */
public class QueueMsgSender implements MessageListener {

	private static Logger LOGGER = LoggerFactory.getLogger(QueueMsgSender.class);

	/**
	 * 目标queueName
	 */
	public String queueName;

	public Queue sendQueue;

	public QueueSender sender;

	public static Map<String, ResponseBO> responseFlagMap = new ConcurrentHashMap<String, ResponseBO>();

	/**
	 * 所有sender持有一个reply，否则会产生多个respQueue的consumer
	 */
//	public static QueueReceiver reply;

	/**
	 * 获取一个MsgSender实例
	 * 
	 * @param queueName
	 * @return
	 */
	public static QueueMsgSender getSenderByQueueName(String queueName) {
		if (null == queueName || queueName.trim().isEmpty()) {
			return null;
		}
		// 判断全局Map里是否有这个，没有的话产生并且放入Map，有的话直接使用Map中的sender
		if (null == AllSenders.sendersMap || AllSenders.sendersMap.isEmpty()
				|| null == AllSenders.sendersMap.get(queueName)) {
			return new QueueMsgSender(queueName);
		} else {
			return AllSenders.sendersMap.get(queueName);
		}
	}

	private QueueMsgSender(String queueNameParam) {
		if (null == queueNameParam || queueNameParam.trim().isEmpty()) {
			return;
		}
		LOGGER.info("===============create queueSender queueName is " + queueNameParam);
		try {
			queueName = queueNameParam;
			sendQueue = MQConnetion.queueSession.createQueue(queueName);
			sender = MQConnetion.queueSession.createSender(sendQueue);
//			if(null == reply){
//				reply =  MQConnetion.queueSession.createReceiver(RespQueueConsumer.respQueue);  
//				reply.setMessageListener(this); 
//			}
			// 设置消息的超时时间，10s没被消费掉就让消息失效
//			sender.setTimeToLive(SysConf.getTimeToLive());
			// 非持久化发送
			sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			AllSenders.sendersMap.put(queueName, this);
		} catch (JMSException e) {
			LOGGER.error("create queueSender error", e);
		}
	}

	/**
	 * 发送String类型消息接口
	 * 
	 * @param msgInfo 消息内容
	 * @param timeOut 超时时间
	 * @return
	 */
	public ResponseBO sendMsg(String msgInfo, Long timeOut) {
		if (null == msgInfo || msgInfo.trim().isEmpty()) {
			return null;
		}

		// 如果外面没有设置超时时间，这里默认5s
		if (null == timeOut || timeOut == 0) {
			timeOut = MessageService.MSG_TIMEOUT;
		}

		// 此处跟着上层的命名规则走，queueName必然有Node的相关信息
		ResponseBO resp = null;
		if (null == queueName || queueName.trim().isEmpty()) {
			resp = new ResponseBO();
		} else {
			resp = new ResponseBO(queueName.split("_")[0]);
		}

		try {
			// 设置消息的超时时间，10s没被消费掉就让消息失效
			sender.setTimeToLive(timeOut);
			TextMessage msg = MQConnetion.queueSession.createTextMessage();
			msg.setJMSReplyTo(RequestQueueConsumer.requestQueue); // 设置回复队列

			// TODO 先不编码
			String sendMsg = new String(msgInfo.getBytes(), "UTF-8");
			// ISO-8859-1编码
			// String sendMsg = new String(msgInfo.getBytes("GBK"), "ISO-8859-1");

			msg.setText(sendMsg);
			// msg.setText(EncoderUtil.encryptDES(msgInfo, EncoderUtil.KEY));
			msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			msg.setBooleanProperty("isFile", false);

			// chenmo修改，超时时间,超过timeout会被丢弃
			LOGGER.info("ActiveMQ QueueMsgSender sendMsg=" + sendMsg);
			sender.send(msg, DeliveryMode.NON_PERSISTENT, 4, timeOut);

			Object messageObj = JSONObject.parseObject(msgInfo).get("message");

			VenusMessage venusMessage = JSONObject.parseObject(messageObj.toString(), VenusMessage.class);

			VenusMessageHead header = venusMessage.getMessage_header();

			if (null != header) {
				String filter = header.getSequence_id();
				// TODO
				responseFlagMap.put(filter, resp);

				while (null == resp.getResp() && timeOut > 0) {
					timeOut = timeOut - MessageService.MSG_POLLING_PERIOD;
					Thread.sleep(MessageService.MSG_POLLING_PERIOD);
				}
				if (null == resp.getResp()) {
					// 如果到这里还是没有返回的内容那么认为失败
					resp.setResult(-1);
				}
				responseFlagMap.remove(filter);
			}

		} catch (Exception e) {
			resp.setResult(-1);
			LOGGER.error("sysSendMsg  error", e);
		}

		return resp;
	}

	/**
	 * 发送文件
	 * 
	 * @param file
	 * @param timeOut
	 * @return
	 */
	public ResponseBO sendFile(File file, Long timeOut) {
		if (null == file) {
			return null;
		}

		// 如果外面没有设置超时时间，这里默认10min
		if (null == timeOut || timeOut == 0) {
			timeOut = MessageService.FILE_TIMEOUT;
		}
		ResponseBO resp = null;
		if (null == queueName || queueName.trim().isEmpty()) {
			resp = new ResponseBO();
		} else {
			resp = new ResponseBO(queueName.split("_")[0]);
		}

		try {
			// 设置消息的超时时间，10s没被消费掉就让消息失效,不知道为什么这个值会导致在某些机器上无法消费??
//			sender.setTimeToLive(timeOut);
			FileInputStream in = new FileInputStream(file);
			BlobMessage msg = ((ActiveMQSession) MQConnetion.queueSession).createBlobMessage(in);
			msg.setStringProperty("FILE.NAME", file.getName());
			msg.setLongProperty("FILE.SIZE", file.length());
			msg.setBooleanProperty("isFile", true);
			msg.setJMSReplyTo(RespQueueConsumer.respQueue);
			msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);

			String filter = "JMSCorrelationID='" + msg.getJMSMessageID() + "'";
			// TODO 用queueName设置resp中的节点信息
			responseFlagMap.put(filter, resp);

			sender.send(msg);

//	        QueueReceiver reply =  MQConnetion.queueSession.createReceiver(RespQueueConsumer.respQueue);  
//	        reply.setMessageListener(this); 

			while (null == resp.getResp() && timeOut > 0) {
				timeOut = timeOut - MessageService.MSG_POLLING_PERIOD;
				Thread.sleep(MessageService.MSG_POLLING_PERIOD);
			}
			if (null == resp.getResp()) {
				// 如果到这里还是没有返回的内容那么认为失败
				resp.setResult(-1);
			}
			// 测试这里remove掉了resp对象是否还存在
			responseFlagMap.remove(filter);
			System.out.println("sender responseFlag after remove filter is " + responseFlagMap.toString());
		} catch (Exception e) {
			resp.setResult(-1);
			LOGGER.error("sysSendMsg  error", e);
		}
		return resp;
	}

	/*
	 * 消费ResponseQueue，ResponseQueueConsumer保证系统内只有一个queue，每个发送者自行消费回复
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		try {
			String textMessage = new String(((TextMessage) message).getText().getBytes("ISO-8859-1"), "GBK");
//			String textMessage = ((TextMessage) message).getText();
//			String textMessage = EncoderUtil.decryptDES(((TextMessage) message).getText(), EncoderUtil.KEY);
//			System.out.println("Sender回复消息 : " + textMessage+ " JMSCorrelation" + message.getJMSCorrelationID());
//			System.out.println("in linstener responseFlag is "+ responseFlagMap.toString());
			// 用JMSCorrelationID保证这个是发过去的message的回复

			System.out.println("************QueueMsgSender onMessage setResMap: " + textMessage + "  JMSMessage"
					+ message.getJMSMessageID());

			if (null != message.getJMSCorrelationID() && !responseFlagMap.isEmpty()
					&& null != responseFlagMap.get("JMSCorrelationID='" + message.getJMSCorrelationID() + "'")) {
				responseFlagMap.get("JMSCorrelationID='" + message.getJMSCorrelationID() + "'").setResp(textMessage);
			}
		} catch (JMSException e) {
			LOGGER.error("sysSendMsg receive response error", e);
		} catch (Exception e) {
			LOGGER.error("sysSendMsg1 receive response error", e);
		}
	}

	/**
	 * 测试接口
	 * 
	 * @param msgInfo 消息内容
	 * @param timeOut 超时时间
	 * @return
	 */
	public void sendMsgAsyncWithSelector(String msgInfo, Long timeOut, String selectorId) throws Exception {
		if (null == msgInfo || msgInfo.trim().isEmpty()) {
			return;
		}

		if (timeOut == null || timeOut <= 0L) {
			return;
		}

		TextMessage msg = MQConnetion.queueSession.createTextMessage();
		msg.setJMSReplyTo(RequestQueueConsumer.requestQueue); // 设置回复队列

		String sendMsg = new String(msgInfo.getBytes(), "UTF-8");
		// 对端是C++
		// String sendMsg = new String(msgInfo.getBytes("GBK"), "ISO-8859-1");
		LOGGER.info("ActiveMQ QueueMsgSender sendMsg=" + sendMsg);
		msg.setText(sendMsg);
		// msg.setText(EncoderUtil.encryptDES(msgInfo, EncoderUtil.KEY));
		
		msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		msg.setBooleanProperty("isFile", false);

		if (selectorId != null) {
			msg.setStringProperty("selector_id", selectorId);
		}

		sender.setTimeToLive(timeOut);
		sender.send(msg, DeliveryMode.NON_PERSISTENT, 4, timeOut);

	}

}
