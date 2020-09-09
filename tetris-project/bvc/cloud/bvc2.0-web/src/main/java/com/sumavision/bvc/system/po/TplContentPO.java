package com.sumavision.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.sumavision.bvc.system.enumeration.TplContentType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 会议模板内容 
 * @Description: 一个会议模板包含：一个录制方案，多个角色、多个布局方案、多个通道别名<br/>
 * @author lvdeyang
 * @date 2018年7月24日 下午4:03:29 
 */
@Entity
@Table(name="BVC_SYSTEM_TPL_CONTENT")
public class TplContentPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 内容id */
	private Long contentId;
	
	/** 内容类型 */
	private TplContentType type;
	
	private TplPO tpl;

	@Column(name = "CONTENT_ID")
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public TplContentType getType() {
		return type;
	}

	public void setType(TplContentType type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name = "TPL_ID")
	public TplPO getTpl() {
		return tpl;
	}

	public void setTpl(TplPO tpl) {
		this.tpl = tpl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentId == null) ? 0 : contentId.hashCode());
		result = prime * result + ((tpl == null) ? 0 : tpl.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		TplContentPO other = (TplContentPO) obj;
		if (contentId == null) {
			if (other.contentId != null)
				return false;
		} else if (!contentId.equals(other.contentId))
			return false;
		if (tpl == null) {
			if (other.tpl != null)
				return false;
		} else if (!tpl.equals(other.tpl))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
