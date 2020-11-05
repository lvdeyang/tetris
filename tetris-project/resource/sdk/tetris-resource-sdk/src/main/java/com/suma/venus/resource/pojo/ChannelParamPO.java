package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.constant.VenusParamConstant.ParamType;

/**
 * 通道参数表
 * @author lxw
 *
 */
@Entity
public class ChannelParamPO extends CommonPO<ChannelParamPO>{

	/*父级通道模板ID，可空*/
	private Long parentChannelTemplateId;
	
	/*父级通道参数模板ID，可空*/
	private Long parentChannelParamId;
	
	private String paramName;
	
	private ParamType paramType;
	
	private Integer numMinValue;
	
	private Integer numMaxValue;
	
	private Integer numStep;
	
	private Integer stringMinLength;
	
	private Integer stringMaxLength;
	
	private String enumValues;
	
	private String constType;
	
	private String constValue;
	
	private ParamScope paramScope;

	@Column(name="parent_channel_template_id")
	public Long getParentChannelTemplateId() {
		return parentChannelTemplateId;
	}

	@Column(name="parent_channel_param_id")
	public Long getParentChannelParamId() {
		return parentChannelParamId;
	}

	@Column(name="param_name")
	public String getParamName() {
		return paramName;
	}

	@Column(name="param_type")
	@Enumerated(EnumType.STRING)
	public ParamType getParamType() {
		return paramType;
	}

	@Column(name="num_min_value")
	public Integer getNumMinValue() {
		return numMinValue;
	}

	@Column(name="num_max_value")
	public Integer getNumMaxValue() {
		return numMaxValue;
	}

	@Column(name="num_step")
	public Integer getNumStep() {
		return numStep;
	}

	@Column(name="string_min_length")
	public Integer getStringMinLength() {
		return stringMinLength;
	}

	@Column(name="string_max_length")
	public Integer getStringMaxLength() {
		return stringMaxLength;
	}

	@Column(name="enum_values")
	public String getEnumValues() {
		return enumValues;
	}

	@Column(name="const_type")
	public String getConstType() {
		return constType;
	}

	@Column(name="const_value")
	public String getConstValue() {
		return constValue;
	}
	
	@Column(name="param_scope")
	@Enumerated(EnumType.STRING)
	public ParamScope getParamScope() {
		return paramScope;
	}

	public void setParamScope(ParamScope paramScope) {
		this.paramScope = paramScope;
	}

	public void setParentChannelTemplateId(Long parentChannelTemplateId) {
		this.parentChannelTemplateId = parentChannelTemplateId;
	}

	public void setParentChannelParamId(Long parentChannelParamId) {
		this.parentChannelParamId = parentChannelParamId;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public void setParamType(ParamType paramType) {
		this.paramType = paramType;
	}

	public void setNumMinValue(Integer numMinValue) {
		this.numMinValue = numMinValue;
	}

	public void setNumMaxValue(Integer numMaxValue) {
		this.numMaxValue = numMaxValue;
	}

	public void setNumStep(Integer numStep) {
		this.numStep = numStep;
	}

	public void setStringMinLength(Integer stringMinLength) {
		this.stringMinLength = stringMinLength;
	}

	public void setStringMaxLength(Integer stringMaxLength) {
		this.stringMaxLength = stringMaxLength;
	}

	public void setEnumValues(String enumValues) {
		this.enumValues = enumValues;
	}

	public void setConstType(String constType) {
		this.constType = constType;
	}

	public void setConstValue(String constValue) {
		this.constValue = constValue;
	}

}
