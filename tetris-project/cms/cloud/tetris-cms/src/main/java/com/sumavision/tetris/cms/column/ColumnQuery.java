package com.sumavision.tetris.cms.column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cms.template.TemplateTagPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
@Component
public class ColumnQuery {
	@Autowired
	private ColumnDAO columnDao;

	public List<ColumnVO> querycolumnTree() throws Exception {

		List<ColumnPO> columns = columnDao.findAll();

		List<ColumnVO> rootcolumns = generateRootcolumns(columns);

		packcolumnTree(rootcolumns, columns);

		return rootcolumns;
	}

	private List<ColumnVO> generateRootcolumns(Collection<ColumnPO> columns) throws Exception {
		if (columns == null || columns.size() <= 0)
			return null;
		List<ColumnVO> rootcolumns = new ArrayList<ColumnVO>();
		for (ColumnPO column : columns) {
			if (column.getParentId() == 0) {
				rootcolumns.add(new ColumnVO().set(column));
			}
		}
		return rootcolumns;
	}

	public void packcolumnTree(List<ColumnVO> rootcolumns, List<ColumnPO> totalcolumns) throws Exception {
		if (rootcolumns == null || rootcolumns.size() <= 0)
			return;
		for (int i = 0; i < rootcolumns.size(); i++) {
			ColumnVO rootcolumn = rootcolumns.get(i);
			for (int j = 0; j < totalcolumns.size(); j++) {
				ColumnPO column = totalcolumns.get(j);
				if (column.getParentId() != 0 && column.getParentId() == rootcolumn.getId()) {
					if (rootcolumn.getSubColumns() == null)
						rootcolumn.setSubColumns(new ArrayList<ColumnVO>());
					rootcolumn.getSubColumns().add(new ColumnVO().set(column));
				}
			}
			if (rootcolumn.getSubColumns() != null && rootcolumn.getSubColumns().size() > 0) {
				packcolumnTree(rootcolumn.getSubColumns(), totalcolumns);
			}
		}
	}
	
	
	public List<ColumnPO> findAllSubTags(Long id) throws Exception{
		return columnDao.findAllSubColumns(new StringBufferWrapper().append("'%/")
															          .append(id)
															          .append("'")
															          .toString(), 
											 new StringBufferWrapper().append("'%/")
																      .append(id)
																      .append("/%'")
																      .toString());
	}
	
}
