package com.sumavision.tetris.easy.process.access.point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 接入点参数<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午3:41:59
 */
@Entity
@Table(name = "TETRIS_PROCESS_ACCESS_POINT_PARAM")
public class AccessPointParamPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 参数名称 */
	private String name;
	
	/** 流程内主键key */
	private String primaryKey;
	
	/** 参数层级路径primaryKey.primaryKey.primaryKey */
	private String primaryKeyPath;
	
	/** 子系统接入点参数主键 */
	private String referenceKey;
	
	/** 参数层级路径referenceKey.referenceKey.referenceKey */
	private String referenceKeyPath;
	
	/** 参数默认值 */
	private String defaultValue;
	
	/** 参数类型 */
	private ParamType type;
	
	/** 枚举值列表 */
	private String enums;
	
	/** 参数方向（用来区分是否是返回值） */
	private ParamDirection direction;
	
	/** 约束表达式 */
	private String constraintExpression;
	
	/** 参数顺序 */
	private Integer serial;
	
	/** 隶属接入点 */
	private Long accessPointId;
	
	/** 备注 */
	private String remarks;
	
	/** 父级参数id */
	private Long parentId;
	
	/** 父级参数id路径 /id/id/id */
	private String parentPath;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PRIMARY_KEY")
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Column(name = "PRIMARY_KEY_PATH")
	public String getPrimaryKeyPath() {
		return primaryKeyPath;
	}

	public void setPrimaryKeyPath(String primaryKeyPath) {
		this.primaryKeyPath = primaryKeyPath;
	}
	
	@Column(name = "REFERENCE_KEY_PATH")
	public String getReferenceKeyPath() {
		return referenceKeyPath;
	}

	public void setReferenceKeyPath(String referenceKeyPath) {
		this.referenceKeyPath = referenceKeyPath;
	}

	@Column(name = "REFERENCE_KEY")
	public String getReferenceKey() {
		return referenceKey;
	}

	public void setReferenceKey(String referenceKey) {
		this.referenceKey = referenceKey;
	}

	@Column(name = "DEFAULT_VALUE")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ParamType getType() {
		return type;
	}

	public void setType(ParamType type) {
		this.type = type;
	}
	
	@Column(name = "ENUMS")
	public String getEnums() {
		return enums;
	}

	public void setEnums(String enums) {
		this.enums = enums;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DIRECTION")
	public ParamDirection getDirection() {
		return direction;
	}

	public void setDirection(ParamDirection direction) {
		this.direction = direction;
	}

	@Column(name = "CONSTRAINT_EXPRESSION")
	public String getConstraintExpression() {
		return constraintExpression;
	}

	public void setConstraintExpression(String constraintExpression) {
		this.constraintExpression = constraintExpression;
	}

	@Column(name = "SERIAL")
	public Integer getSerial() {
		return serial;
	}

	public void setSerial(Integer serial) {
		this.serial = serial;
	}

	@Column(name = "ACCESS_POINT_ID")
	public Long getAccessPointId() {
		return accessPointId;
	}

	public void setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENT_PATH")
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
}
