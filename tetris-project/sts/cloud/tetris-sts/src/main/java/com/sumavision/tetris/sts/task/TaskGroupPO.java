package com.sumavision.tetris.sts.task;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sumavision.tetris.sts.common.CommonPO;


@Entity
@Table(name="TaskGroupPO")
@XmlRootElement(name = "taskGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskGroupPO extends CommonPO<TaskGroupPO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3850940717866112441L;
	
	@XmlElement(name = "groupName")
	private String groupName;

	@Column
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	
}
