package com.sumavision.tetris.cms.classify;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cms.article.ArticleClassifyPermissionDAO;
import com.sumavision.tetris.user.UserVO;

/**
 * 分类增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月28日 下午5:02:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ClassifyService {
	
	@Autowired
	private ClassifyDAO classifyDao;
	
	@Autowired
	private ClassifyUserPermissionDAO classifyUserPermissionDao;
	
	@Autowired
	private ArticleClassifyPermissionDAO articleClassifyPermissionDao;

	/**
	 * 添加分类<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 上午9:02:13
	 * @param UserVO user 用户
	 * @param String name 分类名
	 * @param String remark 备注
	 * @return ClassifyPO 文章数据
	 */
	public ClassifyPO add(
			UserVO user, 
			String name, 
			String remark) throws Exception{
		
		ClassifyPO classify = new ClassifyPO();
		classify.setName(name);
		classify.setRemark(remark);
		classify.setUpdateTime(new Date());

		classifyDao.save(classify);
		
		addPermission(classify, user);
		
		return classify;
	}
	
	/**
	 * 修改分类数据<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 上午9:04:07
	 * @param ClassifyPO classify 分类
	 * @param String name 分类名称
	 * @param String remark 备注
	 * @return ClassifyPO 文章
	 */
	public ClassifyPO edit(
			ClassifyPO classify, 
			String name, 
			String remark) throws Exception{
		
		classify.setName(name);
		classify.setRemark(remark);
		classify.setUpdateTime(new Date());
		classifyDao.save(classify);
		
		return classify;
	}
	
	/**
	 * 添加分类用户关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午9:45:47
	 * @param classify 分类信息
	 * @param user 用户信息
	 * @return ClassifyUserPermissionPO 分类用户关联
	 */
	public ClassifyUserPermissionPO addPermission(
			ClassifyPO classify,
			UserVO user) throws Exception{
		
		ClassifyUserPermissionPO permission = new ClassifyUserPermissionPO();
		permission.setClassifyId(classify.getId());
		permission.setUserId(user.getUuid());
		permission.setGroupId(user.getGroupId());
		
		classifyUserPermissionDao.save(permission);
		
		return permission;
	}
	
	/**
	 * 删除分类及关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午10:29:18
	 * @param classify 分类
	 */
	public void remove(ClassifyPO classify) throws Exception{
		classifyUserPermissionDao.deleteByClassifyId(classify.getId());
		articleClassifyPermissionDao.deleteByClassifyId(classify.getId());
		classifyDao.delete(classify);
	}
}
