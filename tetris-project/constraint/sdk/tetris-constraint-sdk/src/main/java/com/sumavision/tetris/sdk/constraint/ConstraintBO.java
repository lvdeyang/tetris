package com.sumavision.tetris.sdk.constraint;

import java.util.List;

/**
 * 约束<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月14日 上午9:49:04
 */
public class ConstraintBO<T> {

	/** 约束bean */
	private T bean;
	
	/** SpringContext根据字节码字节码获取bean */
	private Class<?> beanClass;
	
	/** SpringContext根据beanName获取bean */
	private String beanName;
	
	/** SpringContext根据beanId获取bean */
	private String beanId;
	
	/** 约束的唯一标识 */
	private String id;
	
	/** bean的显示名称 */
	private String name;
	
	/** 备注 */
	private String remarks;

	private List<ParamBO> params;
	
	public T getBean() {
		return bean;
	}

	public ConstraintBO<T> setBean(T bean) {
		this.bean = bean;
		return this;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public ConstraintBO<T> setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
		return this;
	}

	public String getBeanName() {
		return beanName;
	}

	public ConstraintBO<T> setBeanName(String beanName) {
		this.beanName = beanName;
		return this;
	}

	public String getBeanId() {
		return beanId;
	}

	public ConstraintBO<T> setBeanId(String beanId) {
		this.beanId = beanId;
		return this;
	}

	public String getId() {
		return id;
	}

	public ConstraintBO<T> setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public ConstraintBO<T> setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public ConstraintBO<T> setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<ParamBO> getParams() {
		return params;
	}

	public void setParams(List<ParamBO> params) {
		this.params = params;
	}
	
}
