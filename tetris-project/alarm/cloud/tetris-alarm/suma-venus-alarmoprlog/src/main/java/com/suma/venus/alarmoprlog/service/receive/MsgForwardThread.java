package com.suma.venus.alarmoprlog.service.receive;

// import javax.jms.Message;
// import javax.jms.TextMessage;

import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.controller.alarm.AlarmController;
// import com.suma.venus.message.bo.VenusMessageHead;
// import com.suma.venus.message.bo.VenusMessageHead.MsgType;
// import com.suma.venus.message.mq.VenusMessage;

/**
 * 
 * 告警消息排队、分发线程
 * 
 * @author chenmo
 *
 */
// @Component
/*
public class MsgForwardThread extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(MsgForwardThread.class);

	@Autowired
	private AlarmHandler alarmHandler;

	@Autowired
	private OprlogHandler oprlogHandler;

	
	 // 缓冲队列
	private static LinkedBlockingDeque<Message> alarmOprlogQueue = new LinkedBlockingDeque<>(204800);

	
	 // 将操作日志压入缓存
	public static void push(Message alarmMsg) {
		try {
			alarmOprlogQueue.putLast(alarmMsg);
		} catch (InterruptedException e) {
			LOGGER.error("Cannot put one AlarmBO in Queue", e);
		}
	}

	@Override
	public void run() {

		LOGGER.info("MsgForwardThread start");
		while (true) {
			try {
				Message msg = alarmOprlogQueue.takeFirst();
				handleMsg(msg);
			} catch (InterruptedException e) {
				// LOGGER.error(e.toString());
			}
		}
	}

	private void handleMsg(Message msg) {

		try {

			// LOGGER.info("-----------handleAlarm AlarmXMLBO=" + alarm.toString());

			Boolean isFile = msg.getBooleanProperty("isFile");
			if (null == isFile || !isFile) {
				// 非文件类型的根据消息中的方法名回调相应的接口
				// String textMessage = EncoderUtil.decryptDES(((TextMessage) msg).getText(),
				// EncoderUtil.KEY);
				String textMessage = ((TextMessage) msg).getText();

				LOGGER.info("-----------alarmoprlog service receive msg= " + textMessage);

				if (null == textMessage || textMessage.trim().isEmpty()) {
					return;
				}

				VenusMessage venusMessage = null;
				VenusMessageHead messageHead = null;

				// TODO 验证Venus消息头
				try {

					venusMessage = JSONObject.parseObject(textMessage).getObject("message", VenusMessage.class);

					messageHead = venusMessage.getMessage_header();

					if (messageHead == null || StringUtils.isEmpty(messageHead.getSource_id())
							|| StringUtils.isEmpty(messageHead.getDestination_id())
							|| StringUtils.isEmpty(messageHead.getSequence_id())) {
						
						LOGGER.warn("-----------message pattern illeage, messageid==" + messageHead.getSequence_id());
						// TODO 回复信息？
						return;
					}

				} catch (Exception e) {
					LOGGER.error("transfer to venusMessage exception, exception==" + e);
					return;
				}

				String msgType = messageHead.getMessage_type();

				if (!(msgType.equals(MsgType.alert.toString()) || msgType.equals(MsgType.request.toString()))) {
					// 暂时不处理其他类型
					LOGGER.warn("-----------message type ignore, messageid==" + messageHead.getSequence_id());
					return;
				}

				switch (messageHead.getMessage_name()) {
				case "triggerAlarm":
					// 触发告警
					try {
						alarmHandler.handleTrigger(venusMessage);
					} catch (Exception e) {
						// TODO
						LOGGER.error("-----------handleTriggerAlarm exception, messageid==" + messageHead.getSequence_id() + ", e ==" + e);

					}
					break;
				case "recoverAlarm": 
					//  恢复告警
					try {
						alarmHandler.handleRecovery(venusMessage);
					} catch (Exception e) {
						// TODO
						LOGGER.error("-----------handleRecoverAlarm exception, messageid==" + messageHead.getSequence_id() + ", e ==" + e);

					}
					break;
				case "subscribeAlarm": 
					// 告警订阅 
					try {
						alarmHandler.handleSubscribe(venusMessage);
					} catch (Exception e) {
						// TODO
						LOGGER.error("-----------handleSubscribeAlarm exception, messageid==" + messageHead.getSequence_id() + ", e ==" + e);
					}
					break;
				case "unSubscribeAlarm": 
					// 取消告警订阅 
					try {
						alarmHandler.handleUnSubscribe(venusMessage);
					} catch (Exception e) {
						// TODO
						LOGGER.error("-----------handleUnSubscribe exceptio, messageid==" + messageHead.getSequence_id() + ", e ==" + e);
					}

					break;
				case "oprlog": 
					// 取消告警订阅
					try {
						oprlogHandler.handleOprlog(venusMessage);
					} catch (Exception e) {
						// TODO
						LOGGER.error("-----------handleOprlog exception, messageid==" + messageHead.getSequence_id() + ", e ==" + e);
					}
					break;
				default:
					LOGGER.warn("-----------messageName illeage, messageid==" + messageHead.getSequence_id());
					break;
				}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			LOGGER.error("MsgForwardThread handleMsg error: " + e);
		}
	}

}
*/
