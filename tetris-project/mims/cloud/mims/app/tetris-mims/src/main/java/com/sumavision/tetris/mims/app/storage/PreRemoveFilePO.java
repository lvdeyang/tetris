package com.sumavision.tetris.mims.app.storage;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 存储文件预删除文件<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月23日 下午3:48:55
 */
@Entity
@Table(name = "MIMS_STORE_PRE_REMOVE_FILE")
public class PreRemoveFilePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
}
