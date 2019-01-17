package com.sumavision.tetris.easy.process.access.point;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointNotExistException;
import com.sumavision.tetris.easy.process.access.service.exception.UserHasNoPermissionForServiceQueryException;
import com.sumavision.tetris.mims.app.user.UserClassify;
import com.sumavision.tetris.mims.app.user.UserQuery;
import com.sumavision.tetris.mims.app.user.UserVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/joint/constraint/expression")
public class JointConstraintExpressionController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private JointConstraintExpressionDAO jointConstraintExpressionDao;
	
	@Autowired
	private JointConstraintExpressionQuery jointConstraintExpressionTool;
	
	/**
	 * 分页查询接入点下联合约束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午5:10:46
	 * @param Long accessPointId 接入点id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<JointConstraintExpressionVO> rows 约束列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long accessPointId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
		AccessPointPO accessPoint =  accessPointDao.findOne(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		int total = jointConstraintExpressionDao.countByAccessPointId(accessPointId);
		
		List<JointConstraintExpressionPO> entities = jointConstraintExpressionTool.findByAccessPointId(accessPointId, currentPage, pageSize);
		
		List<JointConstraintExpressionVO> rows = JointConstraintExpressionVO.getConverter(JointConstraintExpressionVO.class).convert(entities, JointConstraintExpressionVO.class);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 删除约束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午5:32:01
	 * @param @PathVariable Long id 约束id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
		JointConstraintExpressionPO constraint =  jointConstraintExpressionDao.findOne(id);
		
		if(constraint != null){
			jointConstraintExpressionDao.delete(constraint);
		}
		
		return null;
	}
	
	/**
	 * 添加一个联合约束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午5:37:02
	 * @param Long accessPointId 接入点id
	 * @param String name 约束名称
	 * @param String expression 约束表达式
	 * @return JointConstraintExpressionVO 约束数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long accessPointId,
			String name,
			String expression,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
		AccessPointPO accessPoint =  accessPointDao.findOne(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		JointConstraintExpressionPO constraint = new JointConstraintExpressionPO();
		constraint.setName(name);
		constraint.setExpression(expression);
		constraint.setAccessPointId(accessPointId);
		constraint.setUpdateTime(new Date());
		jointConstraintExpressionDao.save(constraint);
		
		return new JointConstraintExpressionVO().set(constraint);
	}
	
}
