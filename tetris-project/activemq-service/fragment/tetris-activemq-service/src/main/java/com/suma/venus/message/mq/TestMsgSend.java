package com.suma.venus.message.mq;

import java.io.File;

import com.suma.venus.message.util.SysConf;

public class TestMsgSend {

	public static void main(String argvs[]){
		try {
//			testTopic();
			testQueue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testQueue(){
		try {
			//读取系统配置
			SysConf.initSysConf();
			//创建消息连接
			MQConnetion.connectServer(SysConf.getMqServerIp(), SysConf.getMqServerPort(),SysConf.getFileServer());
			//接收回复的队列，全局唯一
//			RespQueueConsumer.getRequestQueueConsumer("response");
			//处理其他节点请求的队列
			//TODO
			//RequestQueueConsumer.getRequestQueueConsumer("requestQueue");
//			requestConsumer.receiveMessage();
			//向外发送请求，并给response队列回复消息
			QueueMsgSender sender = QueueMsgSender.getSenderByQueueName("requestQueue");
			//测试文件
//				ResponseBO resp = sender.sendFile(new File("F:\\音乐\\1111.ts"), 10000l);
				//ResponseBO resp = sender.sendFile(new File("F:\\音乐\\建党95周年特别节目.wav"), 10*60000l);
			//测试消息
				ResponseBO resp = sender.sendMsg("终于发出去了~", 10000l);
				System.out.println(resp);
			
//			MQConnetion.disconnectServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testTopic(){
		try{
			SysConf.initSysConf();
			MQConnetion.connectServer(SysConf.getMqServerIp(), SysConf.getMqServerPort(),SysConf.getFileServer());
			//先启动订阅者
			TopicMsgSubscriber.getMsgSubscriber();
			//再启动发布者发布消息
			TopicMsgPublisher publisher = TopicMsgPublisher.getMsgPublisher();
			publisher.sendFile(new File("F:\\音乐\\1111.ts"));
//			for(int i=0;i<10;i++){
//				TopicMsgPublisher.sendMsg("This is "+i+"条消息");
//			}
			MQConnetion.disconnectServer();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
