package com.sumavision.tetris.easy.process.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessService {

	@Autowired
	private ProcessFeign processFeign;
	
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
	public String startByKey(
			String primaryKey,
			String variables) throws Exception{
		
		return JsonBodyResponseParser.parseObject(processFeign.startByKey(primaryKey, variables), String.class);
	}
	
	/**
	 * �첽����ڵ�ص�<br/>
	 * <b>����:</b>lvdeyang<br/>
	 * <b>�汾��</b>1.0<br/>
	 * <b>���ڣ�</b>2019��3��28�� ����10:58:46
	 * @param String __processId__ ����ʵ��id
	 * @param String __accessPointId__ �ص������id
	 * @param JSONString variables �ش����̱���
	 */
	public void receiveTaskTrigger(
			String __processId__,
			Long __accessPointId__,
			String variables) throws Exception{
		
		processFeign.receiveTaskTrigger(__processId__, __accessPointId__, variables);
	}
	
}
