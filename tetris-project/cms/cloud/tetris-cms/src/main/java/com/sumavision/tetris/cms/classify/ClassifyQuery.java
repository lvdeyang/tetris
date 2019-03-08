package com.sumavision.tetris.cms.classify;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

/**
 * 分类查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月28日 下午5:01:37
 */
@Component
public class ClassifyQuery {

	@Autowired
	private ClassifyDAO classifyDao;
	
	/**
	 * 分页查询分类<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 下午4:11:33
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ClassifyPO> 分类列表
	 */
	public List<ClassifyPO> findAll(int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ClassifyPO> classifies = classifyDao.findAll(page);
		return classifies.getContent();
	}
	
	/**
	 * 分页查询分类（前端接口）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 下午5:32:06
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ClassifyVO> 用户列表
	 */
	public Map<String, Object> list(int currentPage, int pageSize) throws Exception{
		List<ClassifyPO> classifies= findAll(currentPage, pageSize);
		List<ClassifyVO> view_classifies = ClassifyVO.getConverter(ClassifyVO.class).convert(classifies, ClassifyVO.class);
		long total = classifyDao.count();
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", view_classifies)
												   .getMap();
	}
	
	/**
	 * 分页查询分类（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 下午6:37:06
	 * @param Collection<Long> except 例外分类id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 数据总量
	 * @return List<ClassifyPO> rows 用户列表
	 */
	public Map<String, Object> listWithExcept(Collection<Long> except, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ClassifyPO> pages = classifyDao.findWithExcept(except, page);
		List<ClassifyPO> classifies = pages.getContent();
		long total = pages.getTotalElements();
		List<ClassifyVO> view_classifies = ArticleVO.getConverter(ClassifyVO.class).convert(classifies, ClassifyVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
											       .put("rows", view_classifies)
											       .getMap();
	}
}
