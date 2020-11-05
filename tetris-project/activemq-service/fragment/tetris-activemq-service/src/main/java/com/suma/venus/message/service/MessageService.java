package com.suma.venus.message.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.RequestBaseBO;
import com.suma.venus.message.mq.AllSenders;
import com.suma.venus.message.mq.Callback;
import com.suma.venus.message.mq.MQConnetion;
import com.suma.venus.message.mq.QueueMsgSender;
import com.suma.venus.message.mq.RequestQueueConsumer;
import com.suma.venus.message.mq.RespQueueConsumer;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.mq.TopicMsgPublisher;
import com.suma.venus.message.mq.TopicMsgSubscriber;
import com.suma.venus.message.util.MessageIds;
import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.message.util.SysConf;

/**
 * 消息服务Service
 *
 */
@Service
public class MessageService {

	private static Logger logger = LoggerFactory.getLogger(RequestQueueConsumer.class);

	private static final long MAX_SHORT_MSG_LEN = 1024 * 1024 * 4;
	public static final long FILE_TIMEOUT = 10 * 60000L;
	public static final long MSG_TIMEOUT = 60 * 60 * 1000L;

	// 消息等待轮询周期
	public static final long MSG_POLLING_PERIOD = 100;

	private RespQueueConsumer respConsumer;
	private RequestQueueConsumer reqConsumer;
	private TopicMsgSubscriber topicSubcriber;
	private TopicMsgPublisher topicPublisher;

	private ExecutorService fixedThreadPool;

	private static String selectorId = null;

	static {
		SysConf.initSysConf();
		RegisterStatus.init();
		selectorId = RegisterStatus.getSelectorId();
	}

	/**
	 * 初始化：注册、恢复消息服务
	 * 
	 * @param callback 消息回调接口
	 */
	public void recoveryMessageService(Callback callback) {

		if (RegisterStatus.isRegistered()) {
			msgServReg(RegisterStatus.getNodeId(), selectorId, callback);
		}
	}

	public String getRecoveryNodeId() {
		return RegisterStatus.getNodeId();
	}

	public int msgServLogout(String workNodeID) {
		try {
			RegisterStatus.setRegistered(false);
			topicSubcriber.destroy();
			topicPublisher.destroy();
			respConsumer.destroy();
			reqConsumer.destroy();
			AllSenders.sendersMap.clear();
			MQConnetion.disconnectServer();
			fixedThreadPool.shutdown();
		} catch (Exception e) {
			logger.error("message Service Logout fail: " + e);
			return -1;
		}
		return 0;
	}

	public int msgServReg(String workNodeID, String selectorID, Callback callback) {
		try {
			RegisterStatus.setNodeId(workNodeID);
			RegisterStatus.setRegistered(true);
			MQConnetion.connectServer(SysConf.getMqServerIp(), SysConf.getMqServerPort(), SysConf.getFileServer());

			RequestQueueConsumer.getRequestQueueConsumer(workNodeID,
					"selector_id='" + selectorID + "' or selector_id is NULL or selector_id=''");
			topicSubcriber = TopicMsgSubscriber.getMsgSubscriber();
			topicPublisher = TopicMsgPublisher.getMsgPublisher();
			List<RequestQueueConsumer> consumers = RequestQueueConsumer.consumers;
			for (RequestQueueConsumer consumer : consumers) {
				consumer.setCallback(callback);
			}

			fixedThreadPool = Executors.newFixedThreadPool(100);

		} catch (Exception e) {
			logger.error("message Service Register fail: " + e);
			return -1;
		}
		return 0;
	}

	@Deprecated
	public List<ResponseBO> msgSend(List<String> dstNodeID, Object msgContent, Long timeout) {
		List<ResponseBO> respList = new ArrayList<ResponseBO>();
		List<String> completeNode = new ArrayList<String>();
		try {
			if (!dstNodeID.isEmpty()) {
				if (msgContent instanceof String) {
					for (String nodeId : dstNodeID) {
						fixedThreadPool.execute(new Runnable() {
							public void run() {
								try {
									respList.add(sendStringMsg(nodeId, (String) msgContent, timeout));
								} catch (Exception e) {
									logger.error("Send string message error: " + e);
								} finally {
									completeNode.add(nodeId);
								}
							}
						});
					}
				} else if (msgContent instanceof File) {
					for (String nodeId : dstNodeID) {
						fixedThreadPool.execute(new Runnable() {
							public void run() {
								try {
									respList.add(sendFileMsg(nodeId, (File) msgContent, timeout));
								} catch (Exception e) {
									logger.error("Send file message error: " + e);
								} finally {
									completeNode.add(nodeId);
								}
							}
						});
					}
				} else {
					for (String nodeId : dstNodeID) {
						fixedThreadPool.execute(new Runnable() {
							public void run() {
								try {
									respList.add(sendObjMsg(nodeId, msgContent));
								} catch (Exception e) {
									logger.error("Send object message error: " + e);
								} finally {
									completeNode.add(nodeId);
								}
							}
						});
					}
				}
			}
		} catch (Exception e) {
			logger.error("Send message error: " + e);
		}
		while (completeNode.size() != dstNodeID.size()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("sleep error: " + e);
			}
		}
		return respList;
	}

	private ResponseBO sendStringMsg(String nodeId, String msgContent, Long timeout) throws Exception {
		ResponseBO resp;
		QueueMsgSender sender = QueueMsgSender.getSenderByQueueName(nodeId);
		if ((msgContent).length() > MAX_SHORT_MSG_LEN) {
			File file = new File("/" + UUID.randomUUID() + ".msg");
			OutputStream os = new FileOutputStream(file);
			os.write((msgContent).getBytes());
			os.flush();
			os.close();
			resp = sender.sendFile(file, timeout);
			resp.getMessage().getMessage_header().setDestination_id(nodeId);
			file.delete();
		} else {
			resp = sender.sendMsg(msgContent, timeout);
			resp.getMessage().getMessage_header().setDestination_id(nodeId);
		}
		return resp;
	}

	private ResponseBO sendFileMsg(String nodeId, File msgContent, Long timeout) {
		QueueMsgSender sender = QueueMsgSender.getSenderByQueueName(nodeId);
		ResponseBO resp = sender.sendFile(msgContent, timeout);
		resp.getMessage().getMessage_header().setDestination_id(nodeId);
		return resp;
	}

	private ResponseBO sendObjMsg(String nodeId, Object msgContent) throws Exception {
		QueueMsgSender sender = QueueMsgSender.getSenderByQueueName(nodeId);
		File file = new File("/" + UUID.randomUUID() + ".msg");
		OutputStream os = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(msgContent);
		oos.close();
		ResponseBO resp = sender.sendFile(file, FILE_TIMEOUT);
		resp.getMessage().getMessage_header().setDestination_id(nodeId);
		file.delete();
		return resp;
	}

	/**
	 * @param msgContent
	 */
	@Deprecated
	public void msgSend(Object msgContent) {
		try {
			if (msgContent instanceof String) {
				if (((String) msgContent).length() > MAX_SHORT_MSG_LEN) {
					File file = new File("/" + UUID.randomUUID() + ".msg");
					OutputStream os = new FileOutputStream(file);
					os.write(((String) msgContent).getBytes());
					os.flush();
					os.close();
					topicPublisher.sendFile(file);
					file.delete();
				} else {
					topicPublisher.sendMsg((String) msgContent);
				}
			} else if (msgContent instanceof File) {
				topicPublisher.sendFile((File) msgContent);
			} else {
				File file = new File("/" + UUID.randomUUID() + ".msg");
				OutputStream os = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(msgContent);
				oos.close();
				topicPublisher.sendFile(file);
				file.delete();
			}
		} catch (Exception e) {
			logger.error("Send publish message error: " + e);
		}
	}

	/**
	 * 同步接口 发送请求消息至单个节点并等待目标节点回复信息
	 * 
	 * @param dstNodeId 目标节点
	 * @param requestBO 请求消息体
	 * @param timeOut   超时时间,单位毫秒,这个超时时间指等待消息回复的时间
	 * @return resoonseBo 返回处理结果，包含处理的errMsg及信息
	 * @throws Exception
	 * 
	 */
	@Deprecated
	public ResponseBO msgSend2SingleNodeAndWaitResp(String dstNodeId, RequestBaseBO requestBO, int timeOut)
			throws Exception {
		// 填充selector_id
		requestBO.getMessage().getMessage_header().setSource_selector_id(selectorId);

		List<String> nodeIds = new ArrayList<String>();
		nodeIds.add(dstNodeId);
		String requestId = requestBO.getMessage().getMessage_header().getSequence_id();
		// 消息投递到目标节点，即返回responses
		List<ResponseBO> responses = msgSend(nodeIds, JSONObject.toJSONString(requestBO), (long) timeOut);
		if (null != responses && !responses.isEmpty()) {
			if (responses.get(0).getResult() == 0) {
				// 等待目标节点返回处理结果的响应消息
				while (timeOut > 0) {
					timeOut -= MSG_POLLING_PERIOD;
					ResponseBO resp = (ResponseBO) MessageIds.messageInfoMap.get(requestId);
					if (null != resp) {
						MessageIds.messageInfoMap.remove(requestId);
						return resp;
					}
					Thread.sleep(MSG_POLLING_PERIOD);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * 异步消息发送接口，只发消息不关心返回结果
	 * 
	 * 
	 * @param dstNodeID
	 * @param msg
	 */
	public void msgSend2SingleNode(String dstNodeID, String msg) {
		msgSendAsyncWithSelector(dstNodeID, msg, MSG_TIMEOUT, null);
	}

	/**
	 * 带超时时间的异步消息发送接口，只发消息不关心返回结果
	 * 
	 * 
	 * @param dstNodeID
	 * @param msg
	 * @param timeOut   超时时间，单位毫秒，代表在timeout之后如果消息还没收收取会从消息中心删除
	 */
	public void msgSend2SingleNode(String dstNodeID, String msg, Long timeOut) {
		msgSendAsyncWithSelector(dstNodeID, msg, timeOut, null);
	}

	/**
	 * 带超时时间和selector分组id的异步消息发送接口，只发消息不关心返回结果
	 * 
	 * 
	 * @param dstNodeID  目标节点
	 * @param msg        消息内容
	 * @param timeout    消息超时时间，单位毫秒，代表在timeout之后如果消息还没收收取会从消息中心删除
	 * @param selectorId 消息selector_id。发送response消息强烈建议填写，字段由request中的header获取
	 */
	public void msgSend2SingleNode(String dstNodeID, String msg, Long timeOut, String selectorId) {
		msgSendAsyncWithSelector(dstNodeID, msg, timeOut, selectorId);
	}

	/**
	 * 
	 * 同时向多个节点发送同一异步条消息
	 * 
	 * @param dstNodeIDs
	 * @param msg
	 * 
	 */
	public void sendMsg2MultiNode(List<String> dstNodeIDs, String msg) {
		for (String dstNodeID : dstNodeIDs) {
			msgSendAsyncWithSelector(dstNodeID, msg, MSG_TIMEOUT, null);
		}
	}

	/**
	 * TODO
	 * 
	 * 向一个节点发送多条异步消息
	 * 
	 * @param dstNodeID 
	 * @param msgList
	 */
	public void sendMultiMsg2SingleNode(String dstNodeID, List<String> msgList) {
		for (String msg : msgList) {
			msgSend2SingleNode(dstNodeID, msg);
		}
	}

	/**
	 * 回复消息
	 * 
	 * @param dstNodeId 目标节点
	 * @param resp 恢复内容
	 * @param selectorId 消息selector_id，可空，但强烈建议填写
	 */
	public void writeResp(String dstNodeId, ResponseBO resp, String selectorId) {
		msgSendAsyncWithSelector(dstNodeId, JSONObject.toJSONString(resp), null, selectorId);
	}
	
	/**
	 * 回复消息
	 * 
	 * @param dstNodeId 目标节点
	 * @param resp 恢复内容
	 */
	
	@Deprecated
	public void writeResp(String dstNodeId, ResponseBO resp) {
		msgSendAsyncWithSelector(dstNodeId, JSONObject.toJSONString(resp), null, null);
	}

	/**
	 * 异步发送基础方法
	 */
	private void msgSendAsyncWithSelector(String dstNodeID, String msg, Long timeOut, String selectorId) {

		if (timeOut == null || timeOut <= 0L) {
			timeOut = MSG_TIMEOUT;
		}

		try {
			QueueMsgSender sender = QueueMsgSender.getSenderByQueueName(dstNodeID);
			sender.sendMsgAsyncWithSelector(msg, timeOut, selectorId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
