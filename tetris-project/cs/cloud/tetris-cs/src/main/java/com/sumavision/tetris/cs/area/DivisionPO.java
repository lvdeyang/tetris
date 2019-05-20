package com.sumavision.tetris.cs.area;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name = "TETRIS_CS_DIVISION")
public class DivisionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	@Column(name="DIVISIONCODE")
	public String getDivisionCode() {
		return divisionCode;
	}
	
	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}
	
	@Column(name="DIVISIONLEVEL")
	public Integer getDivisionLevel() {
		return divisionLevel;
	}
	
	public void setDivisionLevel(Integer divisionLevel) {
		this.divisionLevel = divisionLevel;
	}
	
	@Column(name="DIVISIONNAME")
	public String getDivisionName() {
		return divisionName;
	}
	
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	@Column(name="FULLNAME")
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Column(name="PARENTCODE")
	public String getParentCode() {
		return parentCode;
	}
	
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	@Column(name="COVERAGEAREA")
	public Double getCoverageArea() {
		return coverageArea;
	}
	
	public void setCoverageArea(Double coverageArea) {
		this.coverageArea = coverageArea;
	}
	
	@Column(name="POPULATION")
	public Long getPopulation() {
		return population;
	}
	
	public void setPopulation(Long population) {
		this.population = population;
	}
	
	@Column(name="DIVISIONSTATE")
	public Integer getDivisionState() {
		return divisionState;
	}
	
	public void setDivisionState(Integer divisionState) {
		this.divisionState = divisionState;
	}
	
	@Column(name="LONGITUDE")
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@Column(name="LATITUDE")
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@Column(name="DIVISIONCODE12")
	public String getDivisionCode12() {
		return divisionCode12;
	}
	
	public void setDivisionCode12(String divisionCode12) {
		this.divisionCode12 = divisionCode12;
	}
	
	@Column(name="DEVISIONCODE14")
	public String getDivisionCode14() {
		return divisionCode14;
	}
	
	public void setDivisionCode14(String divisionCode14) {
		this.divisionCode14 = divisionCode14;
	}
	
	@Column(name="ORGINDIVISIONCODE")
	public String getOriginDivisionCode() {
		return originDivisionCode;
	}
	
	public void setOriginDivisionCode(String originDivisionCode) {
		this.originDivisionCode = originDivisionCode;
	}
	
	@Column(name="PARENTCODE12")
	public String getParentCode12() {
		return parentCode12;
	}
	
	public void setParentCode12(String parentCode12) {
		this.parentCode12 = parentCode12;
	}
	
	@Column(name="PARENTCODE14")
	public String getParentCode14() {
		return parentCode14;
	}
	
	public void setParentCode14(String parentCode14) {
		this.parentCode14 = parentCode14;
	}
	
	@Column(name="divisionBrevity")
	public String getDivisionBrevity() {
		return divisionBrevity;
	}
	
	public void setDivisionBrevity(String divisionBrevity) {
		this.divisionBrevity = divisionBrevity;
	}
	
	@Column(name="BOSSCODE")
	public String getBossCode() {
		return bossCode;
	}
	
	public void setBossCode(String bossCode) {
		this.bossCode = bossCode;
	}
	
	@Column(name="ISBOSS")
	public Integer getIsBoss() {
		return isBoss;
	}
	public void setIsBoss(Integer isBoss) {
		this.isBoss = isBoss;
	}
}
