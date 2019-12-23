package com.sumavision.tetris.sts.device.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sumavision.tetris.sts.common.CommonPO;





/**
 * 通道信息
 * 此表信息分为两类，一类是关联到deviceInfoPO的，由deviceId可以获得
 * 另一类关联到NCardAbilityAuthPO的，由nCardId可以获得
 *
 */
@Entity
@Table
public class DeviceChannelAuthPO  extends CommonPO<DeviceChannelAuthPO> implements Serializable, Cloneable, Comparable<DeviceChannelAuthPO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7571203046346079524L;

	static Logger logger = LogManager.getLogger(DeviceChannelAuthPO.class);

	private Long inputType;
	
	private String outputType;
	
	private Integer authNum;
	
	private Integer authNumRest;
	
	private Integer cardNumber;
	
	private String deviceType;

	public Long getInputType() {
		return inputType;
	}

	public void setInputType(Long inputType) {
		this.inputType = inputType;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public Integer getAuthNum() {
		return authNum;
	}

	public void setAuthNum(Integer authNum) {
		this.authNum = authNum;
	}

	public Integer getAuthNumRest() {
		return authNumRest;
	}

	public void setAuthNumRest(Integer authNumRest) {
		if(authNumRest > this.authNum){
			this.authNumRest = this.authNum;
		}else{
			this.authNumRest = authNumRest;
		}
	}

	public Integer getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(Integer cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	@Column
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@Override
	public int compareTo(DeviceChannelAuthPO o) {
		// TODO Auto-generated method stub
		if(this.getAuthNumRest() < o.getAuthNumRest()){
			return 1;
		}
		if (this.getAuthNumRest() == o.getAuthNumRest()){
			return 0;
		}
		return -1;
	}
	public DeviceChannelAuthPO clone(){
		DeviceChannelAuthPO o = null;
		try {
			o = (DeviceChannelAuthPO)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		return o;
	}


}
