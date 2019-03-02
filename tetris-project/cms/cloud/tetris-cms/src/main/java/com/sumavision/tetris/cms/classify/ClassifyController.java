package com.sumavision.tetris.cms.classify;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.cms.classify.exception.ClassifyNotExistException;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cms/classify")
public class ClassifyController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ClassifyQuery classifyQuery;
	
	@Autowired
	private ClassifyDAO classifyDao;
	
	@Autowired
	private ClassifyService classifyService;

	/**
	 * 查询所有的分类<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 下午4:06:29
	 * @return rows List<ClassifyVO> 文章列表
	 * @return total long 分类数量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<ClassifyPO> entities = classifyQuery.findAll(currentPage, pageSize);
		
		List<ClassifyVO> classifies = ClassifyVO.getConverter(ClassifyVO.class).convert(entities, ClassifyVO.class);
		
		Long total = classifyDao.count();
		
		return new HashMapWrapper<String, Object>().put("rows", classifies)
												   .put("total", total)
												   .getMap();
	}
	
	/**
	 * 添加分类<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 上午8:57:18
	 * @param String name 分类名称
	 * @param String remark 备注
	 * @return ClassifyVO 分类数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ClassifyPO classify = classifyService.add(user, name, remark);
		
		return new ClassifyVO().set(classify);
	}	
	
	/**
	 * 修改分类数据<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 上午8:59:48
	 * @param @PathVariable Long id 分类id
	 * @param String name 分类名称
	 * @param String remark 备注
	 * @return ClassifyVO 分类数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ClassifyPO classify = classifyDao.findOne(id);
		if(classify == null){
			throw new ClassifyNotExistException(id);
		}
		
		classify = classifyService.edit(classify, name, remark);
		
		return new ClassifyVO().set(classify);
	}
	
	/**
	 * 删除分类<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 上午9:49:01
	 * @param @PathVariable Long id 分类id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		ClassifyPO classify = classifyDao.findOne(id);
		
		if(classify != null){
			classifyDao.delete(classify);
		}
		
		return null;
	}
	
	/**
	 * 分页分类（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 下午5:33:31
	 * @param JSONString except 例外分类id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<ClassifyVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/except")
	public Object listWithExceptIds(
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(except == null){
			return classifyQuery.list(currentPage, pageSize);
		}else{
			List<Long> exceptIds = JSON.parseArray(except, Long.class);
			return classifyQuery.listWithExcept(exceptIds, currentPage, pageSize);
		}
		
	}
}
