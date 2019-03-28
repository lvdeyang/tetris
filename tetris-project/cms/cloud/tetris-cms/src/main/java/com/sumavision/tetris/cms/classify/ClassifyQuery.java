package com.sumavision.tetris.cms.classify;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserVO;

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
	
	@Autowired
	private ClassifyUserPermissionDAO classifyUserPermissionDao;
	
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
	 * 
	 * 分页查询分类<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午3:39:28
	 * @param user 用户
	 * @param currentPage 当前页
	 * @param pageSize 每页数据量
	 * @return Page<ClassifyPO> 分类列表
	 */
	public Page<ClassifyPO> findAllByUser(UserVO user, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		if(user.getGroupId() != null){
			return classifyDao.findAllByGroupId(user.getGroupId(), page);
		}else if(user.getUuid() != null){
			return classifyDao.findAllByUserId(user.getUuid(), page);
		}
		return null;
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
	public Map<String, Object> list(UserVO user, int currentPage, int pageSize) throws Exception{
		Page<ClassifyPO> pages= findAllByUser(user, currentPage, pageSize);
		List<ClassifyPO> classifies = pages.getContent();
		List<ClassifyVO> view_classifies = ClassifyVO.getConverter(ClassifyVO.class).convert(classifies, ClassifyVO.class);
		long total = pages.getTotalElements();
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
	public Map<String, Object> listWithExcept(UserVO user, Collection<Long> except, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ClassifyPO> pages = null;
		if(user.getGroupId() != null){
			pages = classifyDao.findWithExceptByGroupId(except, user.getGroupId(), page);
		}else if(user.getUuid() != null){
			pages = classifyDao.findWithExceptByUserId(except, user.getUuid(), page);
		}
		List<ClassifyPO> classifies = pages.getContent();
		long total = pages.getTotalElements();
		List<ClassifyVO> view_classifies = ArticleVO.getConverter(ClassifyVO.class).convert(classifies, ClassifyVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
											       .put("rows", view_classifies)
											       .getMap();
	}
	
	/**
	 * 校验分类和用户是否匹配<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午3:35:49
	 * @param classifyId 分类id
	 * @param user 用户
	 */
	public boolean hasPermission(Long classifyId, UserVO user) throws Exception{
		ClassifyUserPermissionPO permission = null;
		if(user.getGroupId() != null){
			permission = classifyUserPermissionDao.findByClassifyIdAndGroupId(classifyId, user.getGroupId());
		}else if(user.getUuid() != null){
			permission = classifyUserPermissionDao.findByClassifyIdAndUserId(classifyId, user.getUuid());
		}
		
		if(permission == null) return false;
		return true;
	}
}
