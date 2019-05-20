package com.sumavision.tetris.cs.area;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DivisionVO extends AbstractBaseVO<DivisionVO, DivisionPO>{
	private String divisionCode;
	private Integer divisionLevel;
	private String divisionName;
	private String fullName;
	private String parentCode;
	private Double coverageArea;
	private Long population;
	private Integer divisionState;
	private String longitude;
	private String latitude;
	private String divisionCode12;
	private String divisionCode14;
	private String originDivisionCode;
	private String parentCode12;
	private String parentCode14;
	private String divisionBrevity;
	private String bossCode;
	private Integer isBoss;

	/* (non-Javadoc)
	 * @see com.sumavision.tetris.mvc.converter.AbstractBaseVO#set(java.lang.Object)
	 */
	@Override
	public DivisionVO set(DivisionPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setDivisionCode(entity.getDivisionCode())
		.setDivisionLevel(entity.getDivisionLevel())
		.setDivisionName(entity.getDivisionName())
		.setFullName(entity.getFullName())
		.setParentCode(entity.getParentCode())
		.setCoverageArea(entity.getCoverageArea())
		.setPopulation(entity.getPopulation())
		.setDivisionState(entity.getDivisionState())
		.setLongitude(entity.getLongitude())
		.setLatitude(entity.getLatitude())
		.setDivisionCode12(entity.getDivisionCode12())
		.setDivisionCode14(entity.getDivisionCode14())
		.setOriginDivisionCode(entity.getOriginDivisionCode())
		.setParentCode12(entity.getParentCode12())
		.setParentCode14(entity.getParentCode14())
		.setDivisionBrevity(entity.getDivisionBrevity())
		.setBossCode(entity.getBossCode())
		.setIsBoss(entity.getIsBoss());
		
		return this;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public DivisionVO setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
		return this;
	}

	public Integer getDivisionLevel() {
		return divisionLevel;
	}

	public DivisionVO setDivisionLevel(Integer divisionLevel) {
		this.divisionLevel = divisionLevel;
		return this;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public DivisionVO setDivisionName(String divisionName) {
		this.divisionName = divisionName;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public DivisionVO setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public String getParentCode() {
		return parentCode;
	}

	public DivisionVO setParentCode(String parentCode) {
		this.parentCode = parentCode;
		return this;
	}

	public Double getCoverageArea() {
		return coverageArea;
	}

	public DivisionVO setCoverageArea(Double coverageArea) {
		this.coverageArea = coverageArea;
		return this;
	}

	public Long getPopulation() {
		return population;
	}

	public DivisionVO setPopulation(Long population) {
		this.population = population;
		return this;
	}

	public Integer getDivisionState() {
		return divisionState;
	}

	public DivisionVO setDivisionState(Integer divisionState) {
		this.divisionState = divisionState;
		return this;
	}

	public String getLongitude() {
		return longitude;
	}

	public DivisionVO setLongitude(String longitude) {
		this.longitude = longitude;
		return this;
	}

	public String getLatitude() {
		return latitude;
	}

	public DivisionVO setLatitude(String latitude) {
		this.latitude = latitude;
		return this;
	}

	public String getDivisionCode12() {
		return divisionCode12;
	}

	public DivisionVO setDivisionCode12(String divisionCode12) {
		this.divisionCode12 = divisionCode12;
		return this;
	}

	public String getDivisionCode14() {
		return divisionCode14;
	}

	public DivisionVO setDivisionCode14(String divisionCode14) {
		this.divisionCode14 = divisionCode14;
		return this;
	}

	public String getOriginDivisionCode() {
		return originDivisionCode;
	}

	public DivisionVO setOriginDivisionCode(String originDivisionCode) {
		this.originDivisionCode = originDivisionCode;
		return this;
	}

	public String getParentCode12() {
		return parentCode12;
	}

	public DivisionVO setParentCode12(String parentCode12) {
		this.parentCode12 = parentCode12;
		return this;
	}

	public String getParentCode14() {
		return parentCode14;
	}

	public DivisionVO setParentCode14(String parentCode14) {
		this.parentCode14 = parentCode14;
		return this;
	}

	public String getDivisionBrevity() {
		return divisionBrevity;
	}

	public DivisionVO setDivisionBrevity(String divisionBrevity) {
		this.divisionBrevity = divisionBrevity;
		return this;
	}

	public String getBossCode() {
		return bossCode;
	}

	public DivisionVO setBossCode(String bossCode) {
		this.bossCode = bossCode;
		return this;
	}

	public Integer getIsBoss() {
		return isBoss;
	}

	public DivisionVO setIsBoss(Integer isBoss) {
		this.isBoss = isBoss;
		return this;
	}
}
