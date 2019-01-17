package com.sumavision.tetris.constraint;

import java.util.Comparator;
import java.util.List;

/**
 * 约束参数<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月14日 上午9:59:35
 */
public class ParamBO {

	/** 参数类型 */
	private Class<?> type;
	
	/** 参数key */
	private String key;
	
	/** 参数值 */
	private Object value;
	
	/** 参数名 */
	private String name;
	
	/** 约束 */
	private String constraintExpression;
	
	/** 参数顺序 */
	private int serial;
	
	/** 备注说明 */
	private String remarks;
	
	/** 参数类型 */
	private ParamType paramType;
	
	/** 枚举列表 */
	private List<String> enums;

	public Class<?> getType() {
		return type;
	}

	public ParamBO setType(Class<?> type) {
		this.type = type;
		return this;
	}

	public String getKey() {
		return key;
	}

	public ParamBO setKey(String key) {
		this.key = key;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public ParamBO setValue(Object value) {
		this.value = value;
		return this;
	}

	public String getName() {
		return name;
	}

	public ParamBO setName(String name) {
		this.name = name;
		return this;
	}

	public String getConstraintExpression() {
		return constraintExpression;
	}

	public ParamBO setConstraintExpression(String constraintExpression) {
		this.constraintExpression = constraintExpression;
		return this;
	}

	public int getSerial() {
		return serial;
	}

	public ParamBO setSerial(int serial) {
		this.serial = serial;
		return this;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public ParamBO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}
	
	public ParamType getParamType() {
		return paramType;
	}

	public ParamBO setParamType(ParamType paramType) {
		this.paramType = paramType;
		return this;
	}

	public List<String> getEnums() {
		return enums;
	}

	public ParamBO setEnums(List<String> enums) {
		this.enums = enums;
		return this;
	}

	/**
	 * 参数排序器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月14日 上午11:50:55
	 */
	public static class ParamComparator implements Comparator<ParamBO>{

		@Override
		public int compare(ParamBO o1, ParamBO o2) {
			//升序
			return o1.getSerial() - o2.getSerial();
		}
		
	}
	
}
