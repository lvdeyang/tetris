package com.sumavision.tetris.cms.classify;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
