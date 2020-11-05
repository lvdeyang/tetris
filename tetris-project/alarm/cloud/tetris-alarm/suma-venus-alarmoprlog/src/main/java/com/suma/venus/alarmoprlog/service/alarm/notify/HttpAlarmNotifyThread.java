package com.suma.venus.alarmoprlog.service.alarm.notify;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.ISubscribeAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO.EAlarmNotifyPattern;
import com.suma.venus.alarmoprlog.service.alarm.vo.AlarmRetryNotifyVO;

/**
 * 告警HTTP通知 线程
 *
 * <p>
 * 新的告警产生后，通知所有订阅此类告警的终端，请求。
 * 
 * @author 陈默
 * @see
 * @since 1.0
 */
public class HttpAlarmNotifyThread implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpAlarmNotifyThread.class);

	private ISubscribeAlarmDAO subscribeAlarmDAO;

	private IAlarmDAO alarmDAO;

	private AlarmPO alarmPO;

	private SubscribeAlarmPO subscribeAlarmPO;

	private RestTemplate loadBalancedRestTemplate;

	private RestTemplate restTemplate;

	private boolean isRetry = false;

	public HttpAlarmNotifyThread(AlarmPO alarmPO, SubscribeAlarmPO subscribeAlarmPO,
			ISubscribeAlarmDAO subscribeAlarmDAO, IAlarmDAO alarmDAO, RestTemplate loadBalancedRestTemplate,
			RestTemplate restTemplate, boolean isRetry) {
		this.alarmPO = alarmPO;
		this.subscribeAlarmDAO = subscribeAlarmDAO;
		this.alarmDAO = alarmDAO;
		this.loadBalancedRestTemplate = loadBalancedRestTemplate;
		this.restTemplate = restTemplate;
		this.isRetry = isRetry;
		this.subscribeAlarmPO = subscribeAlarmPO;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (isRetry) {
			retryHttpNotify(new AlarmRetryNotifyVO(alarmPO, subscribeAlarmPO));
		} else {
			httpAlarmNotify(alarmPO);
		}
	}

	/**
	 * 实时告警的http通知
	 *
	 * <p>
	 * 实时告警的http通知
	 * 
	 * @param alarmPO
	 * @see
	 * @since
	 * 
	 */
	private void httpAlarmNotify(AlarmPO alarmPO) {
		try {
			AlarmNotifyUtils.pushWebSoketAlarm(alarmPO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			List<SubscribeAlarmPO> subscribeAlarmPOs = subscribeAlarmDAO
					.findByAlarmInfo_AlarmCode(alarmPO.getLastAlarm().getAlarmInfo().getAlarmCode());

			if (subscribeAlarmPOs == null || subscribeAlarmPOs.isEmpty()) {
				// 没有被订阅则直接返回
				return;
			}

			// 去除重复的订阅，防止重复通知
			for (int i = 0; i < subscribeAlarmPOs.size(); i++) {
				for (int j = i + 1; j < subscribeAlarmPOs.size(); j++) {
					if (subscribeAlarmPOs.get(i).getCallbackUrl().equals(subscribeAlarmPOs.get(j).getCallbackUrl())) {
						subscribeAlarmPOs.remove(j);
						j--;
					}
				}
			}

			// 告警产生的通知
			if (alarmPO.getAlarmStatus().equals(EAlarmStatus.UNTREATED)) {
				if ((alarmPO.getAlarmCount() > 1 && alarmNotifyExp(alarmPO)) || alarmPO.getAlarmCount() == 1) {
					// 已超时旧告警或者新告警通知
					for (SubscribeAlarmPO subscribeAlarmPO : subscribeAlarmPOs) {

						if (!AlarmNotifyUtils.startNotify(alarmPO, subscribeAlarmPO, alarmDAO, restTemplate,
								loadBalancedRestTemplate)) {
							// 告警没发出去
							HttpAlarmNotifyRetryHandler
									.putAlarmRetryNotifyVO(new AlarmRetryNotifyVO(alarmPO, subscribeAlarmPO));
						}
					}
				} else {
					// 每次都通知模式的告警通知
					for (SubscribeAlarmPO subscribeAlarmPO : subscribeAlarmPOs) {
						if (subscribeAlarmPO.getAlarmNotifyPattern().equals(EAlarmNotifyPattern.NOTIFY_EVERYTIME)) {
							if (!AlarmNotifyUtils.startNotify(alarmPO, subscribeAlarmPO, alarmDAO, restTemplate,
									loadBalancedRestTemplate)) {
								// 告警没发出去
								HttpAlarmNotifyRetryHandler
										.putAlarmRetryNotifyVO(new AlarmRetryNotifyVO(alarmPO, subscribeAlarmPO));
							}
						}
					}
				}

				// 告警恢复等的通知
			} else {
				for (SubscribeAlarmPO subscribeAlarmPO : subscribeAlarmPOs) {
					if (!AlarmNotifyUtils.startNotify(alarmPO, subscribeAlarmPO, alarmDAO, restTemplate,
							loadBalancedRestTemplate)) {
						// 告警没发出去
						HttpAlarmNotifyRetryHandler
								.putAlarmRetryNotifyVO(new AlarmRetryNotifyVO(alarmPO, subscribeAlarmPO));
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("Http alarm notify failed: " + e.toString());
		}
	}

	private void retryHttpNotify(AlarmRetryNotifyVO alarmRetryNotifyVO) {
		if (!AlarmNotifyUtils.startNotify(alarmRetryNotifyVO.getAlarmPO(), alarmRetryNotifyVO.getSubscribeAlarmPO(),
				alarmDAO, restTemplate, loadBalancedRestTemplate)) {
			// 再次失败
			alarmRetryNotifyVO.setRetryNum(alarmRetryNotifyVO.getRetryNum() + 1);
			HttpAlarmNotifyRetryHandler.putAlarmRetryNotifyVO(alarmRetryNotifyVO);
		}
	}

	/**
	 * 判断一个旧的实时告警是否超时，是否需要通知
	 *
	 * <p>
	 * 判断一个旧的实时告警是否超时，是否需要通知，超时时间暂定为10分钟
	 * 
	 * @return
	 * @see
	 * @since
	 * 
	 */
	private boolean alarmNotifyExp(AlarmPO alarmPO) {
		// 暂定超时时间为10分钟
		if (alarmPO.getLastNotifyTime() == null) {
			return true;
		}

		if (alarmPO.getLastAlarm().getCreateTime().getTime()
				- alarmPO.getLastNotifyTime().getTime() >= (10 * 60 * 1000)) {
			return true;
		}
		return false;
	}

	public void setSubscribeAlarmService(ISubscribeAlarmDAO subscribeAlarmDAO) {
		this.subscribeAlarmDAO = subscribeAlarmDAO;
	}

	public void setAlarmService(IAlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

}
