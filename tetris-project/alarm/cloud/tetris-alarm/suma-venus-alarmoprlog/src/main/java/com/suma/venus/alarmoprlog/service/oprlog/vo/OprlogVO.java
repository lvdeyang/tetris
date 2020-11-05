package com.suma.venus.alarmoprlog.service.oprlog.vo;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.suma.venus.alarmoprlog.orm.entity.OprlogPO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO.EOprlogType;

public class OprlogVO {

	private Long id;

	private String userName;

	private String oprName;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date oprTime;

	private String ip;

	private String sourceService;

	private String oprDetail;

	private String oprlogType;

	public static OprlogVO transFromPO(OprlogPO po) {

		if (null == po) {
			return null;
		}

		OprlogVO oprlogVO = new OprlogVO();
		BeanUtils.copyProperties(po, oprlogVO);

		if (po.getOprlogType() == null || po.getOprlogType().equals(EOprlogType.USER_OPR)) {
			oprlogVO.setOprlogType(EOprlogType.USER_OPR.getEnumName());
		} else {
			oprlogVO.setOprlogType(po.getOprlogType().getEnumName());
		}

		return oprlogVO;
	}

	public static List<OprlogVO> transFromPOs(Collection<OprlogPO> pos) {
		if (ObjectUtils.isEmpty(pos)) {
			return null;
		}

		List<OprlogVO> vos = new LinkedList<>();
		for (OprlogPO po : pos) {
			vos.add(transFromPO(po));
		}
		return vos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOprName() {
		return oprName;
	}

	public void setOprName(String oprName) {
		this.oprName = oprName;
	}

	public Date getOprTime() {
		return oprTime;
	}

	public void setOprTime(Date oprTime) {
		this.oprTime = oprTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSourceService() {
		return sourceService;
	}

	public void setSourceService(String sourceService) {
		this.sourceService = sourceService;
	}

	public String getOprDetail() {
		return oprDetail;
	}

	public void setOprDetail(String oprDetail) {
		this.oprDetail = oprDetail;
	}

	public String getOprlogType() {
		return oprlogType;
	}

	public void setOprlogType(String oprlogType) {
		this.oprlogType = oprlogType;
	}

}
