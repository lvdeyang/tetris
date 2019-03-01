package com.sumavision.tetris.cms.column;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleDAO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 内容模板增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月18日 下午1:31:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ColumnService {

	@Autowired
	private ColumnDAO columnDao;	

	@Autowired
	private ColumnQuery columnQuery;
	
	@Autowired
	private ColumnRelationArticleDAO columnRelationArticleDao;
	
	@Autowired
	private ArticleDAO articleDao;

	public ColumnPO addRoot() throws Exception {

		ColumnPO columnPO = new ColumnPO();
		columnPO.setName("新建的标签");
		columnPO.setUpdateTime(new Date());

		columnDao.save(columnPO);

		return columnPO;
	}

	public ColumnPO append(ColumnPO parent) throws Exception {

		StringBufferWrapper parentPath = new StringBufferWrapper();
		if (parent.getParentId() == null) {
			parentPath.append("/").append(parent.getId());
		} else {
			parentPath.append(parent.getParentPath()).append("/").append(parent.getId());
		}

		ColumnPO columnPO = new ColumnPO();
		columnPO.setName("新建的标签");
		columnPO.setParentId(parent.getId());
		columnPO.setParentPath(parentPath.toString());
		columnPO.setUpdateTime(new Date());

		columnDao.save(columnPO);

		return columnPO;
	}

	public ColumnPO update(ColumnPO columnPO, String name, String remark) throws Exception {

		columnPO.setName(name);
		columnPO.setRemark(remark);
		columnPO.setUpdateTime(new Date());
		columnDao.save(columnPO);

		return columnPO;
	}

	public void remove(ColumnPO columnPO) throws Exception {

		List<ColumnPO> subColumnPOs = columnQuery.findAllSubTags(columnPO.getId());

		Set<Long> colIds = new HashSetWrapper<Long>().add(columnPO.getId()).getSet();
		if (subColumnPOs != null && subColumnPOs.size() > 0) {
			for (ColumnPO column : subColumnPOs) {
				colIds.add(column.getId());
			}
		}

		if (subColumnPOs == null)
			subColumnPOs = new ArrayList<ColumnPO>();
		subColumnPOs.add(columnPO);
		columnDao.deleteInBatch(subColumnPOs);

	}

	public void move(ColumnPO sourceCol, ColumnPO targetCol) throws Exception {

		StringBufferWrapper parentPath = new StringBufferWrapper();
		if (targetCol.getParentId() == null) {
			parentPath.append("/").append(targetCol.getId());
		} else {
			parentPath.append(targetCol.getParentPath()).append("/").append(targetCol.getId());
		}

		sourceCol.setParentId(targetCol.getId());
		sourceCol.setParentPath(parentPath.toString());

		List<ColumnPO> subCols = columnQuery.findAllSubTags(sourceCol.getId());

		if (subCols != null && subCols.size() > 0) {
			for (ColumnPO subCol : subCols) {
				String[] paths = subCol.getParentPath()
						.split(new StringBufferWrapper().append("/").append(sourceCol.getId()).toString());
				if (paths.length == 1) {
					subCol.setParentPath(parentPath.append("/").append(targetCol.getId()).toString());
				} else {
					subCol.setParentPath(parentPath.append("/").append(targetCol.getId()).append(paths[1]).toString());
				}
			}
		}

		if (subCols == null || subCols.size() <= 0)
			subCols = new ArrayList<ColumnPO>();
		subCols.add(sourceCol);

		columnDao.save(subCols);
	}

	/**
	 * 置顶一个标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 上午10:05:39
	 * 
	 * @param TemplateTagPO
	 *            tag 待置顶的标签
	 */
	public void top(ColumnPO columnPO) throws Exception {

		if (columnPO.getParentId() == null)
			return;

		List<ColumnPO> subColumns = columnQuery.findAllSubTags(columnPO.getId());

		if (subColumns != null && subColumns.size() > 0) {

			String reg = new StringBufferWrapper().append("/").append(columnPO.getParentId()).toString();

			for (ColumnPO subCol : subColumns) {
				subCol.setParentPath(subCol.getParentPath().split(reg)[1]);
			}

		}
		
		columnPO.setParentId(null);
		columnPO.setParentPath(null);

		if (subColumns == null)
			subColumns = new ArrayList<ColumnPO>();
		subColumns.add(columnPO);
		columnDao.save(subColumns);

	}
	
	/**
	 * 查询一个栏目下的内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 上午10:05:39
	 * 
	 * @param columnId 栏目id
	 */
	public ColumnVO query(Long columnId) throws Exception {
		
		ColumnPO column = columnDao.findOne(columnId);
		
		ColumnVO view_column = new ColumnVO().set(column);
		
		//栏目下子栏目
		List<ColumnPO> subColumns = columnQuery.findAllSubTags(columnId);		
		
		if(subColumns != null && subColumns.size()>0){
			List<ColumnVO> view_subColumns = ColumnVO.getConverter(ColumnVO.class).convert(subColumns, ColumnVO.class);			
			if(view_column.getSubColumns() == null) view_column.setSubColumns(new ArrayList<ColumnVO>());
			view_column.getSubColumns().addAll(view_subColumns);
		}

		//栏目下文章
		List<Long> articleIds = columnRelationArticleDao.findArticleIdByColumnId(columnId);
		if(articleIds != null && articleIds.size()>0){
			List<ArticlePO> articles = articleDao.findAll(articleIds);
			if(articles != null && articles.size()>0){
				List<ArticleVO> view_articles = ArticleVO.getConverter(ArticleVO.class).convert(articles, ArticleVO.class);
				if(view_column.getArticles() == null) view_column.setArticles(new ArrayList<ArticleVO>());
				view_column.getArticles().addAll(view_articles);
			}
		}
		
		return view_column;
	}

}
