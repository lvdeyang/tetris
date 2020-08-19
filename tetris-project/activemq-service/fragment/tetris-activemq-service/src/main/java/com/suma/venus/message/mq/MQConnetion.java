package com.suma.venus.message.mq;

import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.jms.TopicConnection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

/**
 * @author admin
 * 用于建立连接，及Session，对外提供注册和注销接口
 */
public class MQConnetion {

	/**
	 *Mq的连接 
	 */
	private static ActiveMQConnectionFactory connectionFactory = null;
	
	/**
	 *接收队列Session 
	 */
	public static ActiveMQSession queueSession = null;
	
	/**
	 *广播用的topicSession 
	 */
	public static ActiveMQSession topicSession = null;
	
	public static QueueConnection queueConnection = null;
	
	public static TopicConnection topicConnection = null;
	
	
	/**
	 * 外部调用创建连接，可用于注册，实际注册还应该将topic及queue的相应生产者消费者打开
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public static void connectServer(String ip,int port,String fileFtp) throws Exception{
		openConnection(ip, port,fileFtp);
		createSession();
	}
	
	/**
	 * 断开连接,关闭所有打开的资源，可用于注销
	 * @throws Exception
	 */
	public static void disconnectServer() throws Exception{
		queueConnection.stop(); 
		//TODO 把发送的sender也要关掉
//        sender.close();  
		queueSession.close();  
		queueConnection.close();
		topicConnection.stop();
		topicSession.close();
		topicConnection.close();
		connectionFactory = null;
		queueConnection = null;
		topicConnection = null;
		queueSession = null;
		topicSession = null;
	}
	
	/**
	 * @param ip
	 * @param port
	 * 连接消息服务中心
	 */
	private static void openConnection(String ip,int port,String fileFtp) throws Exception{
		//与消息服务中心创建连接
		if(null == connectionFactory){
			//ip格式可能是   "ip1,ip2"
			String[] ipArr = ip.split(",");
			StringBuilder sBuilder = new StringBuilder();
			for (String ipValue : ipArr) {
				if(!ipValue.startsWith("__")){
					sBuilder.append("tcp://").append(ipValue).append(":").append(port).append(",");
				}
			}
			sBuilder.deleteCharAt(sBuilder.length()-1);
			connectionFactory = new ActiveMQConnectionFactory("failover:(" + sBuilder.toString() + ")?jms.blobTransferPolicy.defaultUploadUrl="+fileFtp);
		}
		//创建队列连接
		if(null == queueConnection){
			queueConnection = connectionFactory.createQueueConnection();
			queueConnection.start();
		}
		//创建主题连接
		if(null == topicConnection){
			topicConnection = connectionFactory.createTopicConnection();
			topicConnection.start();
		}
	}
	
	/**
	 * @throws Exception
	 * 创建Session
	 */
	private static void createSession() throws Exception{
		if(null == queueSession){
			//设置成事务型session
			queueSession = (ActiveMQSession) queueConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		if(null == topicSession){
			topicSession = (ActiveMQSession)topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
	}
	
	
}
