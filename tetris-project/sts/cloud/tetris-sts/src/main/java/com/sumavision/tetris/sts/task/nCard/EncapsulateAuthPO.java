package com.sumavision.tetris.sts.task.nCard;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.sts.common.CommonPO;


@Entity
@Table
public class EncapsulateAuthPO extends CommonPO<EncapsulateAuthPO> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6615584190349703686L;

	private Integer outputNum;

	private Integer outputRestNum;

	private String encapsulateAuthJson;
	
	public Integer getOutputNum() {
		return outputNum;
	}

	public void setOutputNum(Integer outputNum) {
		this.outputNum = outputNum;
	}

	public Integer getOutputRestNum() {
		return outputRestNum;
	}

	public void setOutputRestNum(Integer outputRestNum) {
		this.outputRestNum = outputRestNum;
	}

	public String getEncapsulateAuthJson() {
		return encapsulateAuthJson;
	}

	public void setEncapsulateAuthJson(String encapsulateAuthJson) {
		this.encapsulateAuthJson = encapsulateAuthJson;
	}
	public void setEncapsulateAuthPO(EncapsulateAuth auth){
		this.outputNum = auth.getOutput_num();
		this.outputRestNum = auth.getOutput_num();
		this.encapsulateAuthJson = JSON.toJSONString(auth);
		
	}

}
