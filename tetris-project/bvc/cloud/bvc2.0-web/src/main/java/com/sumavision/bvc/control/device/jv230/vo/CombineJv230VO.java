package com.sumavision.bvc.control.device.jv230.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineJv230VO extends AbstractBaseVO<CombineJv230VO, CombineJv230PO>{

	/** 拼接屏名称 */
	private String name;
	
	/** 前端渲染布局 */
	private String websiteDraw;
	
	/** 列数 */
	private String column;
	
	/** 行数 */
	private String row;
	
	public String getName() {
		return name;
	}

	public CombineJv230VO setName(String name) {
		this.name = name;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public CombineJv230VO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	public String getColumn() {
		return column;
	}

	public CombineJv230VO setColumn(String column) {
		this.column = column;
		return this;
	}

	public String getRow() {
		return row;
	}

	public CombineJv230VO setRow(String row) {
		this.row = row;
		return this;
	}

	@Override
	public CombineJv230VO set(CombineJv230PO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setWebsiteDraw(entity.getWebsiteDraw());
		
		if(entity.getWebsiteDraw() != null){
			JSONObject scopWebsiteDraw = JSON.parseObject(entity.getWebsiteDraw());
			this.setColumn(scopWebsiteDraw.getJSONObject("basic").getString("column"))
				.setRow(scopWebsiteDraw.getJSONObject("basic").getString("row"));
		}else{
			this.setColumn("4")
				.setRow("4");
		}
		return this;
	}
	
}
