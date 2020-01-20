package com.suma.venus.alarmoprlog.service.receive;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.alarmoprlog.orm.dao.IOprlogDAO;
import com.suma.venus.alarmoprlog.orm.entity.OprlogPO;
import com.sumavision.tetris.alarm.bo.http.OprlogParamBO;

// @Service
/*
public class OprlogHandler {

	@Autowired
	IOprlogDAO oprlogDAO;

	public void handleOprlog(VenusMessage oprlogMsg) {

		try {
			OprlogParamBO oprlogParamBO = oprlogMsg.getMessage_body().getObject("oprlog_param", OprlogParamBO.class);

			OprlogPO oprlogPO = new OprlogPO();

			BeanUtils.copyProperties(oprlogParamBO, oprlogPO);

			oprlogDAO.save(oprlogPO);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
*/
