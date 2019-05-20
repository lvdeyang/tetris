package com.sumavision.tetris.easy.process.core;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-easy-process", configuration = FeignConfiguration.class)
public interface ProcessFeign {

	/**
	 * �������̵�������������<br/>
	 * <p>
	 * 	�����ı������ࣺ���̱���+��������<br/>
	 * 	�ӿ��жԱ����Ĵ���<br/>
	 * 		1.��������������ϵ����<br/>
	 * 		2.����������ֵԼ��У��<br/>
	 * 		3.�������̱����е�����ֵ<br/>
	 * 		4.�������ñ���<br/>
	 * </p>
	 * <b>����:</b>lvdeyang<br/>
	 * <b>�汾��</b>1.0<br/>
	 * <b>���ڣ�</b>2019��1��9�� ����2:41:31
	 * @param String primaryKey ��������
	 * @param JSONString variables ���̱�Ҫ������ʼֵ
	 * @return String processInstanceId ����ʵ��id
	 */
	@RequestMapping(value = "/process/feign/start/by/key")
	public JSONObject startByKey(
			@RequestParam("primaryKey") String primaryKey,
			@RequestParam("variables") String variables);
	
	/**
	 * �첽����ڵ�ص�<br/>
	 * <b>����:</b>lvdeyang<br/>
	 * <b>�汾��</b>1.0<br/>
	 * <b>���ڣ�</b>2019��3��28�� ����10:58:46
	 * @param String __processId__ ����ʵ��id
	 * @param String __accessPointId__ �ص������id
	 * @param JSONString variables �ش����̱���
	 */
	@RequestMapping(value = "/process/feign/receive/task/trigger")
	public JSONObject receiveTaskTrigger(
			@RequestParam("__processId__") String __processId__,
			@RequestParam("__accessPointId__") Long __accessPointId__,
			@RequestParam("variables") String variables);
}
