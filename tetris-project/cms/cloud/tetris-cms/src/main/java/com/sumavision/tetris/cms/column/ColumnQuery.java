package com.sumavision.tetris.cms.column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserVO;
@Component
public class ColumnQuery {
	
	@Autowired
	private ColumnDAO columnDao;
	
	@Autowired
	private ColumnUserPermissionDAO columnUserPermissionDao;
	
	public List<ColumnVO> queryColumnRoot() throws Exception {

		List<ColumnPO> columns = columnDao.findAll();

		List<ColumnVO> rootColumns = generateRootcolumns(columns);

		return rootColumns;
	}

	/**
	 * 根据用户查询栏目树<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午11:45:09
	 * @param user 用户
	 * @return List<ColumnVO> 栏目树
	 */
	public List<ColumnVO> querycolumnTree(UserVO user) throws Exception {

		List<ColumnPO> columns = null;
		if(user.getGroupId() != null){
			columns = columnDao.findByGroupId(user.getGroupId());
		}else if(user.getUuid() != null){
			columns = columnDao.findByUserId(user.getUuid());
		}

		List<ColumnVO> rootcolumns = generateRootcolumns(columns);

		packcolumnTree(rootcolumns, columns);

		return rootcolumns;
	}

	private List<ColumnVO> generateRootcolumns(Collection<ColumnPO> columns) throws Exception {
		if (columns == null || columns.size() <= 0)
			return null;
		List<ColumnVO> rootcolumns = new ArrayList<ColumnVO>();
		for (ColumnPO column : columns) {
			if (column.getParentId() == null) {
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
				if (column.getParentId() != null && column.getParentId() == rootcolumn.getId()) {
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
		return columnDao.findAllSubColumns(new StringBufferWrapper().append("%/")
															          .append(id)
															          .toString(), 
											 new StringBufferWrapper().append("%/")
																      .append(id)
																      .append("/%")
																      .toString());
	}
	
	/**
	 * 校验栏目和用户是否匹配<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午3:50:13
	 * @param columnId 栏目id
	 * @param user 用户
	 */
	public boolean hasPermission(Long columnId, UserVO user) throws Exception{
		ColumnUserPermissionPO permission = null;
		if(user.getGroupId() != null){
			permission = columnUserPermissionDao.findByColumnIdAndGroupId(columnId, user.getGroupId());
		}else if (user.getUuid() != null) {
			permission = columnUserPermissionDao.findByColumnIdAndUserId(columnId, user.getUuid());
		}
		
		if(permission == null) return false;
		return true;
	}
}
