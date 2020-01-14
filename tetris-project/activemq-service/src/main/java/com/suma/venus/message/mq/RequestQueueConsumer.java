package com.suma.venus.message.mq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.VenusMessageHead;
import com.suma.venus.message.service.MessageService;
import com.suma.venus.message.util.MessageIds;
import com.suma.venus.message.util.SysConf;

/**
 * 处理其他节点请求
 * 
 * @author admin
 */
public class RequestQueueConsumer implements MessageListener {

	private static Logger LOGGER = LoggerFactory.getLogger(RequestQueueConsumer.class);

	private RequestQueueConsumer requestQueueConsumer;

	public static Queue requestQueue;

	private static String requestQueueName;

	public static List<RequestQueueConsumer> consumers = new ArrayList<>();

	private Callback callback;

	private QueueReceiver recevier;

	/**
	 * 100个消费者
	 */
	//TODO
	private static int CONSUMER_COUNT = 100;

	public RequestQueueConsumer(String requestQueueNameParam, String selectorID ) throws Exception {
		requestQueueName = requestQueueNameParam;
		receiveMessage(selectorID);
	}

	public static void getRequestQueueConsumer(String requestQueueName, String selectorID) throws Exception {
		requestQueue = MQConnetion.queueSession.createQueue(requestQueueName);
		for (int i = 0; i < CONSUMER_COUNT; i++) {
			consumers.add(new RequestQueueConsumer(requestQueueName, selectorID));
		}
		System.out.println("================getRequestQueueConsumer init finish=============");
		// if (requestQueueConsumer == null) {
		// requestQueueConsumer = new RequestQueueConsumer(requestQueueName);
		// }
		// return requestQueueConsumer;
	}

	public void receiveMessage(String selectorID) throws Exception {
		// requestQueue =
		// MQConnetion.queueSession.createQueue(requestQueueName);
		recevier = MQConnetion.queueSession.createReceiver(requestQueue, selectorID);
		recevier.setMessageListener(this);
	}

	public void destroy() {
		try {
			requestQueueConsumer = null;
			consumers = null;
			recevier.close();
		} catch (Exception e) {
			LOGGER.error("destroy receiver of [" + requestQueueName + "] error", e);
		}
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public void onMessage(Message message) {

		try {

			// 文件类型
			if (message.getBooleanProperty("isFile")) {
				// 这里处理一下消息的时间戳，如果是一个过于早的消息直接丢弃
				/**
				 * message.getJMSTimestamp()是以发送端or消息服务端系统时钟为基准的，当接收端系统时钟不一致时， 会导致消息丢失
				 * 整个系统是不大可能做各个节点的时钟同步的(said by scy)，故需要修改 modified by lxw
				 */
				// if(System.currentTimeMillis() - message.getJMSTimestamp() >
				// MessageService.FILE_TIMEOUT){
				// LOGGER.info("==============too late to receive file message,lose this
				// message============");
				// return;
				// }
				try {
					BlobMessage msg = (BlobMessage) message;
					LOGGER.info("收到消息" + msg.getStringProperty("FILE.NAME"));
					// String fPath = System.getProperty("user.dir") +
					// "/testMqFile";
					// File filePath = new File(fPath);
					// if(!filePath.exists()){
					// filePath.mkdir();
					// }
					// FileOutputStream out = new FileOutputStream(
					// fPath + msg.getStringProperty("FILE.NAME"));
					// TODO 这里的路径后头要处理
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
					Queue responseQueue = (Queue) message.getJMSReplyTo();
					if (responseQueue != null) {
						TextMessage responseMsg = MQConnetion.queueSession.createTextMessage();
						responseMsg.setJMSCorrelationID(message.getJMSMessageID());
						// TODO 按照实际需求回复收到消息调用上层回调
						responseMsg.setText("This message is reply from client：" + msg.getStringProperty("FILE.NAME"));
						// responseMsg.setText(EncoderUtil.encryptDES(
						// "This message is reply from client："
						// + msg.getStringProperty("FILE.NAME"),
						// EncoderUtil.KEY));
						responseMsg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
						// 回复的消息也通过非持久化的方式发送回去，否则可能引起persience store full的错误
						MQConnetion.queueSession.createSender(responseQueue)
								.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
						MQConnetion.queueSession.createSender(responseQueue).send(responseMsg);
						LOGGER.info(
								"客户端回复队列：" + responseQueue + "  JMSCorrelation" + responseMsg.getJMSCorrelationID());
					} else {
						LOGGER.error("服务端回复队列为空");
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LOGGER.info("完成文件接收：" + "recv_" + message.getStringProperty("FILE.NAME"));
			} else {
				System.out.println("message come requestQueueConsumer");
				String textMessage = ((TextMessage) message).getText();
				// String textMessage = EncoderUtil.decryptDES(
				// ((TextMessage) message).getText(), EncoderUtil.KEY);

				// 这里处理一下消息的时间戳，如果是一个过于早的消息直接丢弃
				/**
				 * message.getJMSTimestamp()是以发送端or消息服务端系统时钟为基准的，当接收端系统时钟不一致时， 会导致消息丢失
				 * 整个系统是不大可能做各个节点的时钟同步的(said by scy)，故需要修改 modified by lxw
				 */
				// if(System.currentTimeMillis() - message.getJMSTimestamp() >
				// MessageService.MSG_TIMEOUT){
				// LOGGER.info("==============too late to receive text message,lose this
				// message:"+textMessage);
				// return;
				// }
				System.out.println("request接收消息 : " + textMessage + "  JMSMessage" + message.getJMSMessageID());
				LOGGER.info("request接收消息 : " + textMessage + "  JMSMessage" + message.getJMSMessageID());

				Object messageObj = JSONObject.parseObject(textMessage).get("message");

				VenusMessage venusMessage = JSONObject.parseObject(messageObj.toString(), VenusMessage.class);

				VenusMessageHead header = venusMessage.getMessage_header();
				if (null != header) {
					switch (header.getMessage_type()) {
					case "request":
						Queue responseQueue = (Queue) message.getJMSReplyTo();
						if (responseQueue != null) {
							
						} else {
							System.out.println("服务端回复队列为空");
							LOGGER.error("服务端回复队列为空");
						}
						break;
					case "response":
						
						System.out.println("***********RequestQueueConsumer onMessage case response setResMap" + message.getJMSCorrelationID()+"**"+textMessage);

						
						if (null != header.getSequence_id() && !QueueMsgSender.responseFlagMap.isEmpty()
								&& null != QueueMsgSender.responseFlagMap.get(header.getSequence_id())) {
							QueueMsgSender.responseFlagMap.get(header.getSequence_id()).setResp(textMessage);
						}
						break;
					case "notification":

						break;
					case "alert":

						break;
					case "passby":

						break;
					default:
						break;
					}
				}
				

				// 用解码后的消息重新放入到message中，避免了上层再解密
				// ((TextMessage)message).setText(textMessage);
			}
			// 调用上层执行，需要返回的处理完再重新发送请求，好像是这样？？？
			if (null != callback) {
				callback.execute(message);
			}

		} catch (JMSException e) {
			System.out.println("requestQueueConsumer onMessage error" + e);
			LOGGER.error("requestQueueConsumer onMessage error", e);
		}
	}

}
