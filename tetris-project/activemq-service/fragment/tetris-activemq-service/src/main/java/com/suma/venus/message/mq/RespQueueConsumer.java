package com.suma.venus.message.mq;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建接收回复的队列，全局唯一
 * @author admin
 */
public class RespQueueConsumer implements MessageListener{

	private static RespQueueConsumer respQueueConsumer;
	
	public static  Queue respQueue;
	
	public static String respQueueName;
	
	private QueueReceiver recevier;
	
	public static List<RespQueueConsumer> consumers = new ArrayList<>();
	
	private static Logger LOGGER = LoggerFactory.getLogger(RequestQueueConsumer.class);
	
	/**
	 *100个消费者 
	 */
	private static int CONSUMER_COUNT = 100;
	
	private RespQueueConsumer(String respQueueNameParam) throws Exception{
		respQueueName = respQueueNameParam;
		receiveMessage();
//		createQueue(respQueueName);
	}
	
	public static void getRespQueueConsumer(String respQueueNameParam) throws Exception {
		createQueue(respQueueNameParam);
		for (int i = 0; i < CONSUMER_COUNT; i++) {
			consumers.add(new RespQueueConsumer(respQueueNameParam));
		}	
		
//		if (respQueueConsumer == null) {
//			respQueueConsumer = new RespQueueConsumer(respQueueNameParam);  
//	    }  
//	    return respQueueConsumer;  
	}


	private static void createQueue(String respQueueConsumer) throws Exception{
		 LOGGER.info("============ createResoQueue queueName is "+respQueueConsumer);
		 respQueue = MQConnetion.queueSession.createQueue(respQueueConsumer);  
	}
	
	public void receiveMessage() throws Exception {
		// requestQueue =
		// MQConnetion.queueSession.createQueue(requestQueueName);
		recevier = MQConnetion.queueSession.createReceiver(respQueue);
		recevier.setMessageListener(this);
	}
	
	public void destroy(){
		try {
			respQueueConsumer = null;
			consumers = null;
			recevier.close();
		} catch (Exception e) {
			LOGGER.error("destroy receiver of [" + respQueueName + "] error", e);
		}
	}

	@Override
	public void onMessage(Message message) {
		 try {  
			 String textMessage = ((TextMessage) message).getText();
//			 String textMessage = EncoderUtil.decryptDES(((TextMessage) message).getText(), EncoderUtil.KEY);
				System.out.println("***********RespQueueConsumer onMessage setResMap" + message.getJMSCorrelationID()+"**"+textMessage);
//				System.out.println("Sender回复消息 : " + textMessage+ " JMSCorrelation" + message.getJMSCorrelationID());
//				System.out.println("in linstener responseFlag is "+ responseFlagMap.toString());
				//用JMSCorrelationID保证这个是发过去的message的回复
				if(null != message.getJMSCorrelationID() && !QueueMsgSender.responseFlagMap.isEmpty()
						&& null != QueueMsgSender.responseFlagMap.get("JMSCorrelationID='" + message.getJMSCorrelationID() + "'")){
					QueueMsgSender.responseFlagMap.get("JMSCorrelationID='" + message.getJMSCorrelationID() + "'").setResp(textMessage);
				}
	              
	        } catch (JMSException e) {  
	            e.printStackTrace();  
	        }
		
	}
}
